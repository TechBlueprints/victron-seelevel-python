#!/usr/bin/env python3
# Copyright 2025 Clint Goudie-Nice
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""
SeeLevel 709-BT Service

Main process: monitors BLE, parses data, tracks changes, spawns minimal sensor processes.
Updates sent to sensors only when value changes OR 5+ minutes elapsed.
"""

import re
import subprocess
import sys
import time
import logging
import signal
import json
import os
import glob
import asyncio
from typing import Dict
import dbus
import dbus.service
import dbus.mainloop.glib

from gi.repository import GLib

# Import velib_python for D-Bus
sys.path.insert(1, os.path.join(os.path.dirname(__file__), 'ext', 'velib_python'))
from vedbus import VeDbusService
from settingsdevice import SettingsDevice

# Import pluggable BLE scanner
from ble_scanner import create_scanner, BLEScanner

MFG_ID_CYPRESS = 305 # 709-BTP3
MFG_ID_SEELEVEL = 3264 # 709-BTP7

STATUS_SEELEVEL = {
                   101: "Short Circuit",
                   102: "Open",
                   103: "Bitcount error",
                   104: "Configured as non stacked but received stacked data",
                   105: "Stacked, missing bottom sender data",
                   106: "Stacked, missing top sender data",
                   108: "Bad Checksum",
                   110: "Tank disabled",
                   111: "Tank init"
                  }

SENSOR_TYPES = [
    {  # BTP3
        0: ("Fresh Water", "tank"),
        1: ("Toilet Water", "tank"),
        2: ("Wash Water", "tank"),
        3: ("LPG", "tank"),
        4: ("LPG 2", "tank"),
        5: ("Galley Water", "tank"),
        6: ("Galley Water 2", "tank"),
        7: ("Temp", "temperature"),
        8: ("Temp 2", "temperature"),
        9: ("Temp 3", "temperature"),
        10: ("Temp 4", "temperature"),
        11: ("Chemical", "tank"),
        12: ("Chemical 2", "tank"),
        13: ("Battery", "battery")
    },
    {  # BTP7
        0: ("Fresh Water", "tank"),
        1: ("Wash Water", "tank"),
        2: ("Toilet Water", "tank"),
        3: ("Fresh Water 2", "tank"),
        4: ("Wash Water 2", "tank"),
        5: ("Toilet Water 2", "tank"),
        6: ("Wash Water 3", "tank"),
        7: ("LPG", "tank"),
        8: ("Battery", "battery")
    }
]

CONFIG_DIR = "/data/apps/dbus-seelevel/config"
DEVICES_FILE = "/data/apps/dbus-seelevel/sensors.json"
HEARTBEAT_INTERVAL = 300  # 5 minutes


class SeeLevelService:
    """Main service that spawns sensor processes"""
    
    def __init__(self):
        self.sensor_processes: Dict[str, subprocess.Popen] = {}
        self.discovered_sensors: Dict[str, dict] = {}  # sensor_key -> {mac, sensor_type_id, sensor_num, name, type}
        self.last_update: Dict[str, float] = {}  # sensor_key -> timestamp of last update sent
        self.last_value: Dict[str, int] = {}  # sensor_key -> last value seen
        self.ble_scanner = None
        
        # Initialize D-Bus for signal handling
        dbus.mainloop.glib.DBusGMainLoop(set_as_default=True)
        self.bus = dbus.SystemBus()
        
        # Load persisted sensors
        self._load_discovered_sensors()
        
    def _load_discovered_sensors(self):
        """Load persisted sensor information from JSON file"""
        if not os.path.exists(DEVICES_FILE):
            logging.info("No persisted sensors found, starting fresh")
            return
        
        try:
            with open(DEVICES_FILE, 'r') as f:
                self.discovered_sensors = json.load(f)
            
            logging.info(f"Loaded {len(self.discovered_sensors)} persisted sensors")
                
        except Exception as e:
            logging.error(f"Failed to load sensors from {DEVICES_FILE}: {e}")
            self.discovered_sensors = {}
    
    def _save_discovered_sensors(self):
        """Save discovered sensors to JSON file"""
        try:
            os.makedirs(os.path.dirname(DEVICES_FILE), exist_ok=True)
            with open(DEVICES_FILE, 'w') as f:
                json.dump(self.discovered_sensors, f, indent=2)
        except Exception as e:
            logging.error(f"Failed to save sensors to {DEVICES_FILE}: {e}")
    
    def _start_sensor_process(self, sensor_key: str, sensor_info: dict):
        """Start a sensor process"""
        if sensor_key in self.sensor_processes:
            if self.sensor_processes[sensor_key].poll() is None:
                return  # Still running
            else:
                del self.sensor_processes[sensor_key]
        
        mac = sensor_info['mac']
        sensor_type_id = sensor_info['sensor_type_id']
        sensor_num = sensor_info['sensor_num']
        custom_name = sensor_info['name']
        
        logging.info(f"Starting process for {custom_name}")
        
        # Build command with optional tank parameters
        cmd = ['python3', '/data/apps/dbus-seelevel/data/dbus-seelevel-sensor.py', mac, str(sensor_type_id), str(sensor_num), custom_name]
        
        # Add tank-specific parameters if present
        if 'tank_capacity_gallons' in sensor_info:
            cmd.append(str(sensor_info['tank_capacity_gallons']))
        
        proc = subprocess.Popen(
            cmd,
            stdin=subprocess.PIPE,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            universal_newlines=True,
            bufsize=1
        )
        
        self.sensor_processes[sensor_key] = proc
    
    def _add_discovered_sensor(self, mac: str, sensor_type_id: int, sensor_num: int):
        """Add a newly discovered sensor"""
        sensor_key = f"{mac}_{sensor_num}"
        
        if sensor_key in self.discovered_sensors:
            return  # Already exists
        
        # Get sensor name and type from SENSOR_TYPES
        if sensor_num not in SENSOR_TYPES[sensor_type_id]:
            logging.warning(f"Unknown sensor: {mac} type_id={sensor_type_id} num={sensor_num}")
            return
        
        name, sensor_class = SENSOR_TYPES[sensor_type_id][sensor_num]
        
        sensor_info = {
            'mac': mac,
            'sensor_type_id': sensor_type_id,
            'sensor_num': sensor_num,
            'name': name,  # e.g., "Fresh Water"
            'type': sensor_class
        }
        
        self.discovered_sensors[sensor_key] = sensor_info
        self._save_discovered_sensors()
        
        logging.info(f"Discovered sensor: {sensor_info['name']}")
        
        # Start the sensor process (it will handle its own enabled state)
        self._start_sensor_process(sensor_key, sensor_info)

    def send_update(self, sensor_key: str, sensor_value: int, alarm_state: int = None):
        """Send update to sensor process (value and optional alarm for BTP3)"""
        if sensor_key not in self.sensor_processes:
            return
        
        proc = self.sensor_processes[sensor_key]
        if proc.poll() is not None:
            return  # Process died
        
        try:
            # Send as "value:alarm" or just "value" if no alarm
            if alarm_state is not None:
                proc.stdin.write(f"{sensor_value}:{alarm_state}\n")
            else:
                proc.stdin.write(f"{sensor_value}\n")
            proc.stdin.flush()
        except Exception as e:
            logging.error(f"Failed to write to {sensor_key}: {e}")

    def advertisement_callback(self, mac: str, manufacturer_id: int, data: bytes, rssi: int, interface: str, name: str):
        """
        Called when a BLE advertisement is received
        
        This callback uses the standardized format from our pluggable scanner interface.
        Args:
            mac: MAC address (uppercase, with colons)
            manufacturer_id: Manufacturer ID from advertisement
            data: Raw manufacturer data bytes
            rssi: Signal strength
            interface: HCI interface (e.g., "hci0")
            name: Device name (empty string if unknown)
        """
        # Filter for SeeLevel manufacturer IDs
        if manufacturer_id not in [MFG_ID_CYPRESS, MFG_ID_SEELEVEL]:
            return
        
        # Determine sensor type ID from manufacturer ID
        sensor_type_id = 1 if manufacturer_id == MFG_ID_SEELEVEL else 0
        
        # Process the advertisement data
        hex_data = data.hex()
        self.process_seelevel_data(mac, hex_data, sensor_type_id)
    
    def process_seelevel_data(self, mac: str, hex_data: str, sensor_type_id: int):
        """Process SeeLevel data and dynamically discover/update sensors"""
        data = bytes.fromhex(hex_data)
        if len(data) < 14:
            return
        
        if sensor_type_id == 0:
            # BTP3: Single sensor per advertisement
            sensor_num = data[3]
            sensor_key = f"{mac}_{sensor_num}"
            
            # Discover sensor if not already known
            if sensor_key not in self.discovered_sensors:
                self._add_discovered_sensor(mac, sensor_type_id, sensor_num)
            
            data_str = data[4:7].decode('ascii', errors='ignore').strip()
            
            # Skip OPN (disconnected) and ERR
            if data_str in ["OPN", "ERR"]:
                return
            
            # Parse sensor value
            try:
                sensor_value = int(data_str)
            except ValueError:
                return
            
            # Parse alarm byte (byte 13, ASCII digit 0-9) for BTP3
            alarm_state = None
            if len(data) >= 14 and data[13] >= ord('0') and data[13] <= ord('9'):
                alarm_state = data[13] - ord('0')
            
            self.process_sensor_update(mac, sensor_key, sensor_value, sensor_type_id, sensor_num, alarm_state)
        else:
            # BTP7: Bytes 3-10 are 8 tank sensors, byte 11 is battery voltage × 100
            # Process 8 tank sensors (sensor_num 0-7)
            for sensor_num in range(8):
                sensor_key = f"{mac}_{sensor_num}"
                
                # Discover sensor if not already known
                if sensor_key not in self.discovered_sensors:
                    self._add_discovered_sensor(mac, sensor_type_id, sensor_num)
                
                sensor_value = data[sensor_num+3]  # Bytes 3-10
                if sensor_value > 100:
                    # Error state, skip
                    continue
                
                self.process_sensor_update(mac, sensor_key, sensor_value, sensor_type_id, sensor_num, alarm_state=None)
            
            # Process battery sensor (byte 11, sensor_num 8)
            sensor_num = 8
            sensor_key = f"{mac}_{sensor_num}"
            
            # Discover sensor if not already known
            if sensor_key not in self.discovered_sensors:
                self._add_discovered_sensor(mac, sensor_type_id, sensor_num)
            
            sensor_value = data[11]  # Battery voltage × 100
            self.process_sensor_update(mac, sensor_key, sensor_value, sensor_type_id, sensor_num, alarm_state=None)

    def process_sensor_update(self, mac: str, sensor_key: str, sensor_value: int, sensor_type_id: int, sensor_num: int, alarm_state: int = None):
        """Process SeeLevel data and decide if update needed"""
        try:
            # Only process if sensor is discovered
            if sensor_key not in self.discovered_sensors:
                return
            
            sensor_info = self.discovered_sensors[sensor_key]
            
            # Check if we should send update
            now = time.time()
            value_changed = sensor_key not in self.last_value or self.last_value[sensor_key] != sensor_value
            time_for_heartbeat = (sensor_key not in self.last_update) or (now - self.last_update[sensor_key] >= HEARTBEAT_INTERVAL)
            
            if value_changed or time_for_heartbeat:
                # Start process if needed
                if sensor_key not in self.sensor_processes:
                    self._start_sensor_process(sensor_key, sensor_info)
                
                # Send update (with alarm state if BTP3)
                self.send_update(sensor_key, sensor_value, alarm_state)
                
                # Log only on value changes
                if value_changed:
                    # Add alarm indicator for BTP3
                    alarm_suffix = f" [ALARM {alarm_state}]" if alarm_state and alarm_state > 0 else ""
                    
                    if (sensor_type_id == 0 and sensor_num == 13) or (sensor_type_id == 1 and sensor_num == 8):  # Battery
                        # Both BTP3 and BTP7: voltage × 10
                        voltage = sensor_value / 10.0
                        logging.info(f"{sensor_info['name']}: {voltage}V (changed){alarm_suffix}")
                    elif sensor_type_id == 0 and sensor_num in [7, 8, 9, 10]:  # Temperature
                        temp_c = (sensor_value - 32.0) * 5.0 / 9.0
                        logging.info(f"{sensor_info['name']}: {temp_c:.1f}°C (changed){alarm_suffix}")
                    else:  # Tank
                        tank_capacity_gallons = sensor_info.get('tank_capacity_gallons', 0)
                        if tank_capacity_gallons > 0:
                            capacity_m3 = round(tank_capacity_gallons * 0.00378541, 3)
                            remaining_m3 = round(capacity_m3 * sensor_value / 100.0, 3)
                            logging.info(f"{sensor_info['name']}: {sensor_value}% ({remaining_m3}/{capacity_m3} m³) (changed){alarm_suffix}")
                        else:
                            logging.info(f"{sensor_info['name']}: {sensor_value}% (changed){alarm_suffix}")
                
                # Track last update time and value
                self.last_update[sensor_key] = now
                self.last_value[sensor_key] = sensor_value            
        
        except Exception as e:
            logging.error(f"Process error: {e}")

    def cleanup(self, signum=None, frame=None):
        """Cleanup on exit"""
        logging.info("Shutting down...")
        
        # Send graceful shutdown to all sensor processes
        for sensor_key, proc in self.sensor_processes.items():
            try:
                if proc.poll() is None:  # Still running
                    proc.stdin.write("SHUTDOWN\n")
                    proc.stdin.flush()
            except:
                pass
        
        # Wait briefly for graceful shutdown
        import time
        time.sleep(0.5)
        
        # Force terminate any remaining processes
        for proc in self.sensor_processes.values():
            try:
                if proc.poll() is None:
                    proc.terminate()
                    proc.wait(timeout=1)
            except:
                try:
                    proc.kill()
                except:
                    pass
        
        # Stop BLE scanner
        if self.ble_scanner:
            # Scanner cleanup handled by stop() method
            self.ble_scanner = None
        
        sys.exit(0)
    
    async def scan_continuously(self):
        """Continuously scan for BLE advertisements using D-Bus router"""
        logging.info("=== Initializing BLE scanner ===")
        logging.info(f"Discovered sensors: {len(self.discovered_sensors)}")
        
        try:
            # Create D-Bus scanner
            # Only register for manufacturer IDs - router handles device filtering via UI toggles
            logging.info("Creating D-Bus scanner...")
            self.ble_scanner = create_scanner(
                advertisement_callback=self.advertisement_callback,
                service_name="seelevel",
                manufacturer_ids=[MFG_ID_CYPRESS, MFG_ID_SEELEVEL]  # Both BTP3 and BTP7
            )
            logging.info("D-Bus scanner created")
            
            logging.info("Starting scanner...")
            await self.ble_scanner.start()
            logging.info("=== BLE scanner started successfully ===")
            
            # Keep the scanner running
            while True:
                await asyncio.sleep(60)  # Keep alive check every minute
                
        except Exception as e:
            logging.error(f"BLE scan error: {e}")
            import traceback
            traceback.print_exc()
            raise
        finally:
            if self.ble_scanner:
                try:
                    await self.ble_scanner.stop()
                    logging.info("BLE scanner stopped")
                except:
                    pass

    def run(self):
        """Run the service"""
        logging.info("=== Starting service run() ===")
        signal.signal(signal.SIGINT, self.cleanup)
        signal.signal(signal.SIGTERM, self.cleanup)
        
        # Set up GLib main loop
        logging.info("Setting up GLib main loop...")
        mainloop = GLib.MainLoop()
        
        # Create async event loop and integrate with GLib
        logging.info("Creating asyncio event loop...")
        loop = asyncio.new_event_loop()
        asyncio.set_event_loop(loop)
        
        # Schedule the BLE scanner as a background task
        def start_ble_scanner():
            logging.info("Scheduling BLE scanner task...")
            asyncio.ensure_future(self.scan_continuously(), loop=loop)
            return False  # Don't repeat
        
        # Schedule BLE scanner to start after a short delay
        logging.info("Scheduling BLE scanner startup...")
        GLib.idle_add(start_ble_scanner)
        
        # Schedule async event loop processing
        # Process asyncio tasks by running all ready callbacks
        def process_async():
            # Run all callbacks that are ready
            loop.call_soon(loop.stop)
            loop.run_forever()
            return True  # Keep repeating
        
        logging.info("Scheduling async event loop processing...")
        GLib.timeout_add(10, process_async)  # Process every 10ms
        
        logging.info("=== Service running, entering main loop ===")
        
        try:
            mainloop.run()
        except KeyboardInterrupt:
            self.cleanup()
        
        return 0


def main():
    logging.basicConfig(
        level=logging.INFO,
        format='%(asctime)s - %(message)s'
    )
    
    logging.info("SeeLevel 709-BT Service v1.0.1")
    service = SeeLevelService()
    sys.exit(service.run())


if __name__ == '__main__':
    main()
