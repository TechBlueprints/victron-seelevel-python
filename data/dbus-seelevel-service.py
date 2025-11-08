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

from gi.repository import GLib

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

CONFIG_DIR = "/data/apps/dbus-seelevel/config"
HEARTBEAT_INTERVAL = 300  # 5 minutes


class SeeLevelService:
    """Main service that spawns sensor processes"""
    
    def __init__(self):
        self.sensor_processes: Dict[str, subprocess.Popen] = {}
        self.configured_sensors: Dict[str, dict] = {}
        self.last_update: Dict[str, float] = {}  # sensor_key -> timestamp of last update sent
        self.last_value: Dict[str, int] = {}  # sensor_key -> last value seen
        self.ble_scanner = None
        
        self.load_config()
        
    def load_config(self):
        """Load sensor configurations from files"""
        if not os.path.exists(CONFIG_DIR):
            logging.error(f"Config directory not found: {CONFIG_DIR}")
            logging.error("Run dbus-seelevel-discover.py first to discover devices")
            sys.exit(1)
        
        config_files = glob.glob(f"{CONFIG_DIR}/*.json")
        
        if not config_files:
            logging.error(f"No configuration files found in {CONFIG_DIR}")
            logging.error("Run dbus-seelevel-discover.py first to discover devices")
            sys.exit(1)
        
        for config_file in config_files:
            try:
                with open(config_file, 'r') as f:
                    config = json.load(f)
                
                if not config.get('enabled', True):
                    logging.info(f"Skipping disabled sensor: {config['sensor_type']}")
                    continue
                
                mac = config['mac']
                sensor_type_id = config['sensor_type_id']
                sensor_num = config['sensor_num']
                sensor_key = f"{mac}_{sensor_num}"
                
                self.configured_sensors[sensor_key] = config
                logging.info(f"Loaded config: {config['custom_name']} ({mac})")
                
            except Exception as e:
                logging.error(f"Failed to load {config_file}: {e}")
        
        if not self.configured_sensors:
            logging.error("No enabled sensors found in configuration")
            sys.exit(1)
        
        logging.info(f"Loaded {len(self.configured_sensors)} enabled sensor(s)")
    
    def start_sensor_process(self, mac: str, sensor_type_id: int, sensor_num: int, config: dict):
        """Start a sensor process"""
        sensor_key = f"{mac}_{sensor_num}"
        
        if sensor_key in self.sensor_processes:
            if self.sensor_processes[sensor_key].poll() is None:
                return  # Still running
            else:
                del self.sensor_processes[sensor_key]
        
        custom_name = config.get('custom_name', 'Unknown')
        logging.info(f"Starting process for {custom_name}")
        
        # Build command with optional tank parameters
        cmd = ['python3', '/data/apps/dbus-seelevel/data/dbus-seelevel-sensor.py', mac, str(sensor_type_id), str(sensor_num), custom_name]
        
        # Add tank-specific parameters if present
        if 'tank_capacity_gallons' in config:
            cmd.append(str(config['tank_capacity_gallons']))
        
        proc = subprocess.Popen(
            cmd,
            stdin=subprocess.PIPE,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            universal_newlines=True,
            bufsize=1
        )
        
        self.sensor_processes[sensor_key] = proc

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
    
    def parse_btmon_line(self, line: str):
        """Parse btmon output - DEPRECATED, kept for fallback mode"""
        line = line.strip()
        
        # Match MAC address - support both "Address:" and "LE Address:"
        mac_match = re.search(r'(?:LE )?Address: ([0-9A-F:]{17})', line)
        if mac_match:
            self.current_mac = mac_match.group(1)
            return
        
        company_match = re.search(r'Company: .* \((\d+)\)', line)
        if company_match and (int(company_match.group(1)) == MFG_ID_CYPRESS or int(company_match.group(1)) == MFG_ID_SEELEVEL):
            if (int(company_match.group(1)) == MFG_ID_SEELEVEL):
                self.sensor_type_id = 1
            else:
                self.sensor_type_id = 0
            self.current_data = "pending"
            return
        
        if self.current_data == "pending" and self.current_mac:
            data_match = re.search(r'Data: ([0-9a-f]+)', line)
            if data_match:
                hex_data = data_match.group(1)
                self.process_seelevel_data(self.current_mac, hex_data, self.sensor_type_id)
                self.current_data = None

    def process_seelevel_data(self, mac: str, hex_data: str, sensor_type_id: int):
        """Process SeeLevel data and decide if update needed"""
        data = bytes.fromhex(hex_data)
        if len(data) < 14:
            return
        
        if sensor_type_id == 0:
            sensor_num = data[3]
            sensor_key = f"{mac}_{sensor_num}"
        
            # Only process configured sensors
            if sensor_key not in self.configured_sensors:
                return
            
            data_str = data[4:7].decode('ascii', errors='ignore').strip()
            
            # Skip OPN (disconnected)
            if data_str == "OPN":
                return
            
            config = self.configured_sensors[sensor_key]
            
            # Parse sensor value
            try:
                sensor_value = int(data_str)
            except ValueError:
                if data_str == "ERR":
                    logging.info(f"{config['custom_name']}: Error")
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
                # Only process configured sensors
                if sensor_key not in self.configured_sensors:
                    continue
                sensor_value = data[sensor_num+3]  # Bytes 3-10
                if sensor_value > 100:
                    config = self.configured_sensors[sensor_key]
                    if sensor_value in STATUS_SEELEVEL:
                        logging.info(f"{config['custom_name']}: {STATUS_SEELEVEL[sensor_value]}")
                    else:
                        logging.info(f"{config['custom_name']}: Unknown Error #{sensor_value}")
                    continue  # Skip this sensor but process others
                self.process_sensor_update(mac, sensor_key, sensor_value, sensor_type_id, sensor_num, alarm_state=None)
            
            # Process battery sensor (byte 11, sensor_num 8)
            sensor_num = 8
            sensor_key = f"{mac}_{sensor_num}"
            if sensor_key in self.configured_sensors:
                sensor_value = data[11]  # Battery voltage × 100
                self.process_sensor_update(mac, sensor_key, sensor_value, sensor_type_id, sensor_num, alarm_state=None)

    def process_sensor_update(self, mac: str, sensor_key: str, sensor_value: int, sensor_type_id: int, sensor_num: int, alarm_state: int = None):
        """Process SeeLevel data and decide if update needed"""
        try:
            config = self.configured_sensors[sensor_key]
            # Check if we should send update
            now = time.time()
            value_changed = sensor_key not in self.last_value or self.last_value[sensor_key] != sensor_value
            time_for_heartbeat = (sensor_key not in self.last_update) or (now - self.last_update[sensor_key] >= HEARTBEAT_INTERVAL)
            
            if value_changed or time_for_heartbeat:
                # Start process if needed
                if sensor_key not in self.sensor_processes:
                    self.start_sensor_process(mac, sensor_type_id, sensor_num, config)
                
                # Send update (with alarm state if BTP3)
                self.send_update(sensor_key, sensor_value, alarm_state)
                
                # Log only on value changes
                if value_changed:
                    # Add alarm indicator for BTP3
                    alarm_suffix = f" [ALARM {alarm_state}]" if alarm_state and alarm_state > 0 else ""
                    
                    if (sensor_type_id == 0 and sensor_num == 13) or (sensor_type_id == 1 and sensor_num == 8):  # Battery
                        # Both BTP3 and BTP7: voltage × 10
                        voltage = sensor_value / 10.0
                        logging.info(f"{config['custom_name']}: {voltage}V (changed){alarm_suffix}")
                    elif sensor_type_id == 0 and sensor_num in [7, 8, 9, 10]:  # Temperature
                        temp_c = (sensor_value - 32.0) * 5.0 / 9.0
                        logging.info(f"{config['custom_name']}: {temp_c:.1f}°C (changed){alarm_suffix}")
                    else:  # Tank
                        tank_capacity_gallons = config.get('tank_capacity_gallons', 0)
                        if tank_capacity_gallons > 0:
                            capacity_m3 = round(tank_capacity_gallons * 0.00378541, 3)
                            remaining_m3 = round(capacity_m3 * sensor_value / 100.0, 3)
                            logging.info(f"{config['custom_name']}: {sensor_value}% ({remaining_m3}/{capacity_m3} m³) (changed){alarm_suffix}")
                        else:
                            logging.info(f"{config['custom_name']}: {sensor_value}% (changed){alarm_suffix}")
                
                # Track last update time and value
                self.last_update[sensor_key] = now
                self.last_value[sensor_key] = sensor_value            
        
        except Exception as e:
            logging.error(f"Process error: {e}")

    def process_btmon_output(self, source, condition):
        """GLib callback"""
        if condition == GLib.IO_HUP:
            return False
        
        try:
            line = source.readline()
            if line:
                self.parse_btmon_line(line)
        except:
            pass
        
        return True

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
            # For async scanner, we'd need to stop it properly
            # But since we're using D-Bus or btmon subprocess, just set to None
            self.ble_scanner = None
        
        sys.exit(0)
    
    async def scan_continuously(self):
        """Continuously scan for BLE advertisements using the best available scanner"""
        logging.info("Initializing BLE scanner...")
        
        try:
            # Get list of MAC addresses from configured sensors
            mac_addresses = list(set(config['mac'] for config in self.configured_sensors.values()))
            
            # Create scanner with pluggable backend
            # Prefer D-Bus scanner, fall back to btmon (via Btmon wrapper)
            # Note: For now, we'll only support D-Bus. btmon support can be added later.
            self.ble_scanner = create_scanner(
                advertisement_callback=self.advertisement_callback,
                service_name="seelevel",
                manufacturer_ids=[MFG_ID_CYPRESS, MFG_ID_SEELEVEL],  # Both BTP3 and BTP7
                mac_addresses=mac_addresses,
                prefer_dbus=True
            )
            
            await self.ble_scanner.start()
            logging.info("BLE scanner started successfully")
            
            # Keep the scanner running
            while True:
                await asyncio.sleep(60)  # Keep alive check every minute
                
        except Exception as e:
            logging.error(f"BLE scan error: {e}")
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
        signal.signal(signal.SIGINT, self.cleanup)
        signal.signal(signal.SIGTERM, self.cleanup)
        
        # Set up GLib main loop
        mainloop = GLib.MainLoop()
        
        # Create async event loop and integrate with GLib
        loop = asyncio.new_event_loop()
        asyncio.set_event_loop(loop)
        
        # Schedule the BLE scanner as a background task
        def start_ble_scanner():
            asyncio.ensure_future(self.scan_continuously(), loop=loop)
            return False  # Don't repeat
        
        # Schedule BLE scanner to start after a short delay
        GLib.idle_add(start_ble_scanner)
        
        # Schedule async event loop processing
        def process_async():
            loop.run_until_complete(asyncio.sleep(0))  # Process pending tasks
            return True  # Keep repeating
        
        GLib.timeout_add(100, process_async)  # Process every 100ms
        
        logging.info("Service running...")
        
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
