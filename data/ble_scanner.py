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
        self.mac_addresses = [mac.lower().replace(':', '_') for mac in (mac_addresses or [])]
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
            
            if 'com.victronenergy.ble.advertisements' not in dbus_iface.ListNames():
                logger.info("dbus-ble-advertisements service not found on D-Bus")
                return False
            
            # Check service health
            try:
                service = bus.get_object('com.victronenergy.ble.advertisements', '/')
                iface = dbus.Interface(service, 'com.victronenergy.ble.Advertisements')
                version = iface.GetVersion()
                logger.info(f"dbus-ble-advertisements service found (version: {version})")
                return True
            except Exception as e:
                logger.warning(f"dbus-ble-advertisements service exists but unhealthy: {e}")
                return False
                
        except Exception as e:
            logger.info(f"D-Bus check failed: {e}")
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
        bus_name = dbus.service.BusName(f'com.victronenergy.{self.service_name}', self.bus)
        
        # Register for manufacturer IDs
        for mfg_id in self.manufacturer_ids:
            path = f'/ble_advertisements/{self.service_name}/mfgr/{mfg_id}'
            obj = dbus.service.Object(bus_name, path)
            self.registration_objects.append(obj)
            logger.info(f"  Registered interest in manufacturer ID: 0x{mfg_id:04X} at {path}")
        
        # Register for specific MAC addresses
        for mac in self.mac_addresses:
            path = f'/ble_advertisements/{self.service_name}/addr/{mac}'
            obj = dbus.service.Object(bus_name, path)
            self.registration_objects.append(obj)
            logger.info(f"  Registered interest in MAC: {mac} at {path}")
        
        # Subscribe to Advertisement signals on our paths
        # The router will emit signals on paths that match our registrations
        self._signal_match = self.bus.add_signal_receiver(
            self._dbus_advertisement_callback,
            signal_name='Advertisement',
            dbus_interface='com.victronenergy.ble.Advertisements',
            bus_name='com.victronenergy.ble.advertisements'
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
