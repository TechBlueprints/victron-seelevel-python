#!/usr/bin/env python3
"""
Pluggable BLE Scanner Interface

Provides BLE scanning via dbus-ble-advertisements router service.
For standalone operation without the router, see the legacy-standalone-btmon branch.
"""

import asyncio
import logging
from abc import ABC, abstractmethod
from typing import Callable
import dbus
import dbus.service

logger = logging.getLogger(__name__)


class BLEScanner(ABC):
    """Abstract base class for BLE scanning backends"""
    
    def __init__(self, advertisement_callback: Callable[[str, int, bytes, int, str, str], None]):
        """
        Initialize scanner
        
        Args:
            advertisement_callback: Function to call when advertisement received
                                   Signature: (mac, manufacturer_id, data, rssi, interface, name)
        """
        self.advertisement_callback = advertisement_callback
        self.running = False
    
    @abstractmethod
    async def start(self):
        """Start scanning"""
        pass
    
    @abstractmethod
    async def stop(self):
        """Stop scanning"""
        pass
    
    @abstractmethod
    def is_available(self) -> bool:
        """Check if this scanner backend is available"""
        pass


class DBusAdvertisementScanner(BLEScanner):
    """BLE scanner using dbus-ble-advertisements router service"""
    
    def __init__(self, advertisement_callback: Callable[[str, int, bytes, int, str, str], None],
                 service_name: str,
                 manufacturer_ids: list[int] = None,
                 mac_addresses: list[str] = None):
        super().__init__(advertisement_callback)
        self.service_name = service_name.replace('-', '_')  # Sanitize for D-Bus paths
        self.manufacturer_ids = manufacturer_ids or []
        # MAC addresses are no longer used - router handles device filtering via UI toggles
        self.bus = None
        self.registration_objects = []
        self._signal_match = None
    
    def is_available(self) -> bool:
        """Check if dbus-ble-advertisements service is available and healthy"""
        try:
            # Ensure D-Bus mainloop is set up (safe to call multiple times)
            from dbus.mainloop.glib import DBusGMainLoop
            DBusGMainLoop(set_as_default=True)
            
            bus = dbus.SystemBus()
            
            # Check if service exists on D-Bus
            proxy = bus.get_object('org.freedesktop.DBus', '/org/freedesktop/DBus')
            dbus_iface = dbus.Interface(proxy, 'org.freedesktop.DBus')
            
            names = dbus_iface.ListNames()
            logger.info(f"Checking for router service in {len(names)} D-Bus names...")
            
            if 'com.victronenergy.switch.ble_router' not in names:
                logger.info("dbus-ble-advertisements service not found on D-Bus")
                logger.info(f"Available services: {[n for n in names if 'victron' in str(n) or 'ble' in str(n)]}")
                return False
            
            logger.info("Router service name found on D-Bus, checking health...")
            
            # Check service health by verifying the /ble_advertisements path exists
            try:
                service = bus.get_object('com.victronenergy.switch.ble_router', '/ble_advertisements')
                # Just verify we can get the object - no need to call GetVersion
                logger.info("dbus-ble-advertisements service found and healthy")
                return True
            except Exception as e:
                logger.warning(f"dbus-ble-advertisements service exists but unhealthy: {e}")
                logger.warning(f"Error details: {type(e).__name__}: {str(e)}")
                return False
                
        except Exception as e:
            logger.info(f"D-Bus check failed: {e}")
            logger.info(f"Error details: {type(e).__name__}: {str(e)}")
            return False
    
    async def start(self):
        """Start D-Bus advertisement scanner by registering interests"""
        if not self.is_available():
            raise RuntimeError("dbus-ble-advertisements service not available")
        
        logger.info(f"Registering with dbus-ble-advertisements as '{self.service_name}'...")
        
        # Import GLib D-Bus mainloop integration
        from dbus.mainloop.glib import DBusGMainLoop
        
        # Ensure D-Bus is attached to GLib mainloop
        # This is safe to call multiple times
        DBusGMainLoop(set_as_default=True)
        
        self.bus = dbus.SystemBus()
        
        # Use the router's bus name for registration objects
        router_bus_name = dbus.service.BusName('com.victronenergy.switch.ble_router', self.bus)
        
        # Register for manufacturer IDs only
        # The router will filter by enabled/disabled device toggles in the UI
        for mfg_id in self.manufacturer_ids:
            path = f'/ble_advertisements/{self.service_name}/mfgr/{mfg_id}'
            obj = dbus.service.Object(router_bus_name, path)
            self.registration_objects.append(obj)
            logger.info(f"  Registered interest in manufacturer ID: 0x{mfg_id:04X} at {path}")
        
        # No longer registering for specific MAC addresses - router handles device filtering
        
        # Subscribe to Advertisement signals on our paths
        # The router will emit signals on paths that match our registrations
        self._signal_match = self.bus.add_signal_receiver(
            self._dbus_advertisement_callback,
            signal_name='Advertisement',
            dbus_interface='com.techblueprints.BleAdvertisements',
            bus_name='com.victronenergy.switch.ble_router'
        )
        
        self.running = True
        logger.info("D-Bus advertisement scanner started and listening for signals")
    
    def _dbus_advertisement_callback(self, mac, manufacturer_id, data, rssi, interface, name):
        """Callback for D-Bus Advertisement signals"""
        # Convert D-Bus types to Python types
        mac_str = str(mac)
        mfg_id = int(manufacturer_id)
        data_bytes = bytes(data)
        rssi_int = int(rssi)
        interface_str = str(interface)
        name_str = str(name)
        
        # Call the user's callback
        self.advertisement_callback(
            mac_str,
            mfg_id,
            data_bytes,
            rssi_int,
            interface_str,
            name_str
        )
    
    async def stop(self):
        """Stop D-Bus advertisement scanner"""
        if self._signal_match:
            self._signal_match.remove()
            self._signal_match = None
        
        # Clean up registration objects
        self.registration_objects.clear()
        
        self.running = False
        logger.info("D-Bus advertisement scanner stopped")


def create_scanner(advertisement_callback: Callable,
                   service_name: str,
                   manufacturer_ids: list[int] = None,
                   mac_addresses: list[str] = None,
                   **kwargs) -> BLEScanner:
    """
    Create the appropriate BLE scanner based on what's available.
    
    This version only supports dbus-ble-advertisements router.
    For standalone operation, see the legacy-standalone-btmon branch.
    """
    # Try D-Bus scanner
    logger.info("Checking for dbus-ble-advertisements service...")
    dbus_scanner = DBusAdvertisementScanner(
        advertisement_callback=advertisement_callback,
        service_name=service_name,
        manufacturer_ids=manufacturer_ids,
        mac_addresses=mac_addresses
    )
    
    if dbus_scanner.is_available():
        logger.info("Using D-Bus advertisement scanner")
        return dbus_scanner
    
    # No scanner available
    raise RuntimeError(
        "dbus-ble-advertisements service not available. "
        "Please install and start dbus-ble-advertisements, or use the legacy-standalone-btmon branch."
    )
