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

Runs continuously, only processing configured devices.
Reads configuration from /data/seelevel/*.json
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
from typing import Dict

from gi.repository import GLib

MFG_ID_SEELEVEL = 305
CONFIG_DIR = "/data/seelevel"


class SeeLevelService:
    """Main service that processes only configured devices"""
    
    def __init__(self):
        self.sensor_processes: Dict[str, subprocess.Popen] = {}
        self.configured_sensors: Dict[str, dict] = {}
        self.current_mac = None
        self.current_data = None
        self.btmon_proc = None
        
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
    
    def start_sensor_process(self, mac: str, sensor_num: int, config: dict):
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
        cmd = ['python3', '/data/dbus-seelevel-sensor.py', mac, str(sensor_num), custom_name]
        
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

    def write_sensor_data(self, mac: str, sensor_num: int, data_str: str, volume: int, total: int):
        """Write sensor data to process stdin"""
        sensor_key = f"{mac}_{sensor_num}"
        
        if sensor_key not in self.sensor_processes:
            return
        
        proc = self.sensor_processes[sensor_key]
        if proc.poll() is not None:
            return  # Process died
        
        data = {
            'data': data_str,
            'volume': volume,
            'total': total
        }
        
        try:
            proc.stdin.write(json.dumps(data) + '\n')
            proc.stdin.flush()
        except Exception as e:
            logging.error(f"Failed to write to {sensor_key}: {e}")

    def parse_btmon_line(self, line: str):
        """Parse btmon output"""
        line = line.strip()
        
        mac_match = re.search(r'Address: ([0-9A-F:]{17})', line)
        if mac_match:
            self.current_mac = mac_match.group(1)
            return
        
        company_match = re.search(r'Company: Cypress Semiconductor \((\d+)\)', line)
        if company_match and int(company_match.group(1)) == MFG_ID_SEELEVEL:
            self.current_data = "pending"
            return
        
        if self.current_data == "pending" and self.current_mac:
            data_match = re.search(r'Data: ([0-9a-f]+)', line)
            if data_match:
                hex_data = data_match.group(1)
                self.process_seelevel_data(self.current_mac, hex_data)
                self.current_data = None

    def process_seelevel_data(self, mac: str, hex_data: str):
        """Process SeeLevel data for configured sensors only"""
        try:
            data = bytes.fromhex(hex_data)
            if len(data) < 14:
                return
            
            sensor_num = data[3]
            sensor_key = f"{mac}_{sensor_num}"
            
            # Only process configured sensors
            if sensor_key not in self.configured_sensors:
                return
            
            data_str = data[4:7].decode('ascii', errors='ignore')
            
            # Skip OPN (disconnected)
            if data_str.strip() == "OPN":
                return
            
            volume_str = data[7:10].decode('ascii', errors='ignore')
            total_str = data[10:13].decode('ascii', errors='ignore')
            
            try:
                volume = int(volume_str)
            except:
                volume = 0
            try:
                total = int(total_str)
            except:
                total = 0
            
            # Start process if needed
            config = self.configured_sensors[sensor_key]
            if sensor_key not in self.sensor_processes:
                self.start_sensor_process(mac, sensor_num, config)
            
            # Write data
            self.write_sensor_data(mac, sensor_num, data_str, volume, total)
            
        except:
            pass

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
        
        if self.btmon_proc:
            self.btmon_proc.terminate()
            self.btmon_proc.wait()
        
        for proc in self.sensor_processes.values():
            try:
                proc.terminate()
                proc.wait(timeout=2)
            except:
                proc.kill()
        
        sys.exit(0)

    def run(self):
        """Run the service"""
        signal.signal(signal.SIGINT, self.cleanup)
        signal.signal(signal.SIGTERM, self.cleanup)
        
        try:
            self.btmon_proc = subprocess.Popen(
                ['btmon'],
                stdout=subprocess.PIPE,
                stderr=subprocess.DEVNULL,
                universal_newlines=True,
                bufsize=1
            )
            logging.info("Started btmon")
        except Exception as e:
            logging.error(f"Failed to start btmon: {e}")
            return 1
        
        GLib.io_add_watch(
            self.btmon_proc.stdout,
            GLib.IO_IN | GLib.IO_HUP,
            self.process_btmon_output
        )
        
        mainloop = GLib.MainLoop()
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
    
    logging.info("SeeLevel 709-BT Service v1.0")
    service = SeeLevelService()
    sys.exit(service.run())


if __name__ == '__main__':
    main()
