#!/usr/bin/env python3
"""
Pluggable BLE Scanner Interface

Provides multiple BLE scanning backends:
- BleakScanner: Uses Bleak library for direct BLE scanning
- DBusAdvertisementScanner: Uses dbus-ble-advertisements router service
- BtmonScanner: Uses btmon subprocess for raw HCI monitoring
"""

import asyncio
import logging
import subprocess
import time
from abc import ABC, abstractmethod
from typing import Callable, Optional
import dbus
import dbus.service
from gi.repository import GLib

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


class BleakBLEScanner(BLEScanner):
    """BLE scanner using Bleak library"""
    
    def __init__(self, advertisement_callback: Callable[[str, int, bytes, int, str, str], None], 
                 manufacturer_id_filter: Optional[int] = None):
        super().__init__(advertisement_callback)
        self.manufacturer_id_filter = manufacturer_id_filter
        self.scanner = None
        
        try:
            from bleak import BleakScanner
            from bleak.backends.device import BLEDevice
            from bleak.backends.scanner import AdvertisementData
            self.BleakScanner = BleakScanner
            self.BLEDevice = BLEDevice
            self.AdvertisementData = AdvertisementData
            self._available = True
        except ImportError:
            logger.warning("Bleak library not available")
            self._available = False
    
    def is_available(self) -> bool:
        return self._available
    
    def _bleak_callback(self, device, advertisement_data):
        """Internal callback that adapts Bleak format to our standard format"""
        # Filter by manufacturer ID if specified
        if self.manufacturer_id_filter is not None:
            if self.manufacturer_id_filter not in advertisement_data.manufacturer_data:
                return
            mfg_data = advertisement_data.manufacturer_data[self.manufacturer_id_filter]
            mfg_id = self.manufacturer_id_filter
        else:
            # Use first manufacturer data found
            if not advertisement_data.manufacturer_data:
                return
            mfg_id = list(advertisement_data.manufacturer_data.keys())[0]
            mfg_data = advertisement_data.manufacturer_data[mfg_id]
        
        # Get device name (may be None)
        device_name = device.name or ""
        
        # Call user's callback with standardized format
        # Note: Bleak doesn't provide interface info, use "hci0" as default
        self.advertisement_callback(
            device.address.lower(),
            mfg_id,
            mfg_data,
            advertisement_data.rssi if hasattr(advertisement_data, 'rssi') else -999,
            "hci0",
            device_name
        )
    
    async def start(self):
        """Start Bleak scanner"""
        if not self.is_available():
            raise RuntimeError("Bleak scanner not available")
        
        logger.info("Starting Bleak BLE scanner...")
        self.scanner = self.BleakScanner(detection_callback=self._bleak_callback)
        await self.scanner.start()
        self.running = True
        logger.info(f"Bleak scanner started (filtering for mfg ID: 0x{self.manufacturer_id_filter:04X})" if self.manufacturer_id_filter else "Bleak scanner started (no filter)")
    
    async def stop(self):
        """Stop Bleak scanner"""
        if self.scanner:
            await self.scanner.stop()
            self.running = False
            logger.info("Bleak scanner stopped")


class BtmonBLEScanner(BLEScanner):
    """BLE scanner using btmon subprocess for raw HCI monitoring"""
    
    def __init__(self, advertisement_callback: Callable[[str, int, bytes, int, str, str], None],
                 manufacturer_ids: list[int] = None):
        """
        Initialize btmon scanner
        
        Args:
            advertisement_callback: Callback for advertisements
            manufacturer_ids: List of manufacturer IDs to filter for (None = all)
        """
        super().__init__(advertisement_callback)
        self.manufacturer_ids = manufacturer_ids or []
        self.btmon_proc = None
        self.current_mac = None
        self.current_mfg_id = None
        self.current_rssi = None
        self.current_interface = None
        self.current_name = None
        self.device_names = {}  # Cache of MAC -> name
    
    def is_available(self) -> bool:
        """Check if btmon is available"""
        import shutil
        return shutil.which('btmon') is not None
    
    async def start(self):
        """Start btmon scanner"""
        if not self.is_available():
            raise RuntimeError("btmon not available")
        
        logger.info("Starting btmon BLE scanner...")
        
        # Start btmon subprocess
        self.btmon_proc = subprocess.Popen(
            ['btmon'],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            bufsize=1,
            universal_newlines=True
        )
        
        # Set up GLib callback to read btmon output
        GLib.io_add_watch(
            self.btmon_proc.stdout,
            GLib.IO_IN | GLib.IO_HUP,
            self._process_btmon_output
        )
        
        self.running = True
        mfg_ids_str = ", ".join(f"0x{id:04X}" for id in self.manufacturer_ids) if self.manufacturer_ids else "all"
        logger.info(f"Btmon scanner started (filtering for manufacturer IDs: {mfg_ids_str})")
    
    async def stop(self):
        """Stop btmon scanner"""
        if self.btmon_proc:
            try:
                self.btmon_proc.terminate()
                self.btmon_proc.wait(timeout=1)
            except:
                try:
                    self.btmon_proc.kill()
                except:
                    pass
        self.running = False
        logger.info("Btmon scanner stopped")
    
    def _process_btmon_output(self, source, condition):
        """GLib callback to process btmon output"""
        if condition & GLib.IO_HUP:
            logger.error("btmon process ended unexpectedly")
            return False
        
        try:
            line = source.readline()
            if line:
                self._parse_btmon_line(line.strip())
            return True
        except Exception as e:
            logger.error(f"Error reading btmon output: {e}")
            return False
    
    def _parse_btmon_line(self, line: str):
        """Parse btmon output line"""
        import re
        
        # Match MAC address
        mac_match = re.search(r'(?:Address|LE Address):\s+([0-9A-Fa-f:]{17})', line)
        if mac_match:
            self.current_mac = mac_match.group(1).upper()
            self.current_name = None  # Reset name for new device
            return
        
        # Match RSSI
        rssi_match = re.search(r'RSSI:\s+(-?\d+)', line)
        if rssi_match:
            self.current_rssi = int(rssi_match.group(1))
            return
        
        # Match interface (hci0, hci1, etc.)
        if 'hci' in line.lower():
            interface_match = re.search(r'(hci\d+)', line, re.IGNORECASE)
            if interface_match:
                self.current_interface = interface_match.group(1).lower()
                return
        
        # Match device name
        if line.startswith('Name:') or line.startswith('Name (complete):'):
            name_match = re.search(r'Name.*?:\s+(.+)', line)
            if name_match:
                self.current_name = name_match.group(1).strip()
                if self.current_mac:
                    self.device_names[self.current_mac] = self.current_name
                return
        
        # Match manufacturer data
        mfg_match = re.search(r'Company:\s+.*?\((\d+)\)', line)
        if mfg_match:
            self.current_mfg_id = int(mfg_match.group(1))
            return
        
        # Match data line (hex bytes)
        if self.current_mac and self.current_mfg_id is not None:
            # Look for hex data lines (format: "    xx xx xx xx ...")
            if re.match(r'^\s+[0-9a-fA-F]{2}(\s+[0-9a-fA-F]{2})*', line):
                # Extract hex bytes
                hex_bytes = re.findall(r'[0-9a-fA-F]{2}', line)
                if hex_bytes:
                    # Check if we should filter this manufacturer ID
                    if not self.manufacturer_ids or self.current_mfg_id in self.manufacturer_ids:
                        data = bytes.fromhex(''.join(hex_bytes))
                        rssi = self.current_rssi or 0
                        interface = self.current_interface or 'hci0'
                        name = self.device_names.get(self.current_mac, self.current_name or '')
                        
                        # Call the standardized callback
                        self.advertisement_callback(
                            self.current_mac,
                            self.current_mfg_id,
                            data,
                            rssi,
                            interface,
                            name
                        )


class DBusAdvertisementScanner(BLEScanner):
    """BLE scanner using dbus-ble-advertisements router service"""
    
    def __init__(self, advertisement_callback: Callable[[str, int, bytes, int, str, str], None],
                 service_name: str,
                 manufacturer_ids: list[int] = None,
                 mac_addresses: list[str] = None):
        """
        Initialize D-Bus advertisement scanner
        
        Args:
            advertisement_callback: Callback for advertisements
            service_name: Name of this service (for registration path, e.g., "orion-tr")
            manufacturer_ids: List of manufacturer IDs to register for (None = don't filter by manufacturer)
            mac_addresses: List of MAC addresses to register for
        """
        super().__init__(advertisement_callback)
        # Sanitize service name for D-Bus path compatibility (replace hyphens with underscores)
        self.service_name = service_name.replace('-', '_')
        self.manufacturer_ids = manufacturer_ids or []
        self.mac_addresses = [m.lower().replace(':', '') for m in (mac_addresses or [])]
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
            
            # Check if service exists
            proxy = bus.get_object('com.victronenergy.ble.advertisements', '/ble_advertisements')
            interface = dbus.Interface(proxy, 'com.victronenergy.ble.Advertisements')
            
            # Check if service is healthy
            status = interface.GetStatus()
            if status == "running":
                logger.info("dbus-ble-advertisements service is available and running")
                return True
            else:
                logger.warning(f"dbus-ble-advertisements service status: {status}")
                return False
                
        except dbus.exceptions.DBusException as e:
            logger.debug(f"dbus-ble-advertisements not available: {e}")
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
        name_str = str(name) if name else ""
        
        # Call user's callback
        self.advertisement_callback(mac_str, mfg_id, data_bytes, rssi_int, interface_str, name_str)
    
    async def stop(self):
        """Stop D-Bus advertisement scanner by unregistering"""
        if self._signal_match:
            self._signal_match.remove()
            self._signal_match = None
        
        for obj in self.registration_objects:
            obj.remove_from_connection()
        self.registration_objects.clear()
        
        self.running = False
        logger.info("D-Bus advertisement scanner stopped")


def create_scanner(advertisement_callback: Callable[[str, int, bytes, int, str, str], None],
                   service_name: str = None,
                   manufacturer_id: int = None,
                   manufacturer_ids: list[int] = None,
                   mac_addresses: list[str] = None,
                   prefer_dbus: bool = True) -> BLEScanner:
    """
    Factory function to create the best available BLE scanner
    
    Args:
        advertisement_callback: Callback for advertisements (mac, mfg_id, data, rssi, interface)
        service_name: Name of this service (required for D-Bus scanner)
        manufacturer_id: Single manufacturer ID to filter for (deprecated, use manufacturer_ids)
        manufacturer_ids: List of manufacturer IDs to filter for
        mac_addresses: List of MAC addresses to monitor
        prefer_dbus: If True, prefer D-Bus scanner when available
    
    Returns:
        BLEScanner instance
    
    Raises:
        RuntimeError: If no scanner backend is available
    """
    # Normalize manufacturer IDs to a list
    if manufacturer_ids is None:
        manufacturer_ids = [manufacturer_id] if manufacturer_id is not None else []
    
    scanners_to_try = []
    
    if prefer_dbus:
        # Try D-Bus scanner first
        if service_name:
            dbus_scanner = DBusAdvertisementScanner(
                advertisement_callback,
                service_name,
                manufacturer_ids=manufacturer_ids,
                mac_addresses=mac_addresses or []
            )
            scanners_to_try.append(("dbus-ble-advertisements", dbus_scanner))
        
        # Fall back to btmon (supports multiple manufacturer IDs)
        btmon_scanner = BtmonBLEScanner(advertisement_callback, manufacturer_ids=manufacturer_ids)
        scanners_to_try.append(("btmon", btmon_scanner))
        
        # Fall back to Bleak (only supports single manufacturer ID)
        if manufacturer_id or (manufacturer_ids and len(manufacturer_ids) == 1):
            mfg_id = manufacturer_id or (manufacturer_ids[0] if manufacturer_ids else None)
            bleak_scanner = BleakBLEScanner(advertisement_callback, mfg_id)
            scanners_to_try.append(("Bleak", bleak_scanner))
    else:
        # Try btmon first (supports multiple manufacturer IDs)
        btmon_scanner = BtmonBLEScanner(advertisement_callback, manufacturer_ids=manufacturer_ids)
        scanners_to_try.append(("btmon", btmon_scanner))
        
        # Try Bleak (only supports single manufacturer ID)
        if manufacturer_id or (manufacturer_ids and len(manufacturer_ids) == 1):
            mfg_id = manufacturer_id or (manufacturer_ids[0] if manufacturer_ids else None)
            bleak_scanner = BleakBLEScanner(advertisement_callback, mfg_id)
            scanners_to_try.append(("Bleak", bleak_scanner))
        
        # Fall back to D-Bus
        if service_name:
            dbus_scanner = DBusAdvertisementScanner(
                advertisement_callback,
                service_name,
                manufacturer_ids=manufacturer_ids,
                mac_addresses=mac_addresses or []
            )
            scanners_to_try.append(("dbus-ble-advertisements", dbus_scanner))
    
    # Try each scanner in order
    for name, scanner in scanners_to_try:
        if scanner.is_available():
            logger.info(f"Using {name} scanner")
            return scanner
        else:
            logger.debug(f"{name} scanner not available")
    
    raise RuntimeError("No BLE scanner backend available")

