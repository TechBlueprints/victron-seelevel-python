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
SeeLevel 709-BT Discovery Tool

Scans for SeeLevel devices and creates configuration files.
Run this once to discover your devices, then edit the configs as needed.
"""

import re
import subprocess
import sys
import time
import json
import os
from typing import Dict, Set

MFG_ID_SEELEVEL = 305

SENSOR_TYPES = {
    0: "Fresh Water", 1: "Toilet Water", 2: "Wash Water", 3: "LPG", 4: "LPG 2",
    5: "Galley Water", 6: "Galley Water 2", 7: "Temp", 8: "Temp 2",
    9: "Temp 3", 10: "Temp 4", 11: "Chemical", 12: "Chemical 2", 13: "Battery"
}

CONFIG_DIR = "/data/seelevel"


class SeeLevelDiscovery:
    """Discovers SeeLevel sensors and creates config files"""
    
    def __init__(self):
        self.discovered: Set[str] = set()
        self.current_mac = None
        self.current_data = None
        self.last_discovery_time = None
        self.should_continue = True
        
        # Create config directory
        os.makedirs(CONFIG_DIR, exist_ok=True)
        
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
        """Process SeeLevel data and create config"""
        try:
            data = bytes.fromhex(hex_data)
            if len(data) < 14:
                return
            
            sensor_num = data[3]
            data_str = data[4:7].decode('ascii', errors='ignore')
            
            # Check if sensor is disconnected
            is_disconnected = (data_str.strip() == "OPN")
            
            if sensor_num not in SENSOR_TYPES:
                return
            
            sensor_key = f"{mac}_{sensor_num}"
            
            if sensor_key in self.discovered:
                return  # Already found
            
            self.discovered.add(sensor_key)
            print()  # New line after scanning dots
            self.create_config(mac, sensor_num, is_disconnected)
            # Reset timer AFTER user finishes configuring (in create_config)
            
            # If user chose not to continue, stop scanning
            if not self.should_continue:
                return
            
        except:
            pass

    def create_config(self, mac: str, sensor_num: int, is_disconnected: bool = False):
        """Create configuration file for a sensor"""
        sensor_name = SENSOR_TYPES[sensor_num]
        
        # Use friendly name for filename, with spaces replaced by dashes
        friendly_filename = sensor_name.replace(' ', '-').lower()
        config_file = f"{CONFIG_DIR}/{friendly_filename}.json"
        
        # If file exists, append MAC to make it unique
        if os.path.exists(config_file):
            # Check if this file is for a different MAC
            try:
                with open(config_file, 'r') as f:
                    existing_config = json.load(f)
                if existing_config.get('mac') != mac:
                    # Different MAC, need unique filename
                    mac_suffix = mac.split(':')[-2] + mac.split(':')[-1]
                    config_file = f"{CONFIG_DIR}/{friendly_filename}_{mac_suffix.lower()}.json"
            except:
                # Can't read file, use MAC suffix
                mac_suffix = mac.split(':')[-2] + mac.split(':')[-1]
                config_file = f"{CONFIG_DIR}/{friendly_filename}_{mac_suffix.lower()}.json"
        
        # Check if this specific config already exists
        if os.path.exists(config_file):
            print(f"\n{'='*80}")
            print(f"Found: {sensor_name}")
            print(f"MAC Address: {mac}")
            print(f"Status: ALREADY CONFIGURED")
            print(f"Config file: {config_file}")
            print(f"{'='*80}")
            print()
            if not self.ask_yes_no("Reconfigure this sensor?", default=False):
                print(f"✓ Keeping existing configuration")
                return
            # If yes, continue to reconfigure (timer already reset by ask_yes_no)
        
        # Interactive configuration
        print(f"\n{'='*80}")
        print(f"Found: {sensor_name}")
        print(f"MAC Address: {mac}")
        if is_disconnected:
            print(f"Status: DISCONNECTED (OPN)")
        print(f"{'='*80}")
        print()
        print("Options:")
        print("  (y)es     - Add and enable this sensor")
        print("  (n)o      - Skip this sensor (no config file created)")
        print("  (d)isable - Add config but keep sensor disabled")
        print()
        
        # Default: 'd' for disconnected sensors and battery, 'y' for everything else
        if is_disconnected or sensor_num == 13:  # Disconnected or Battery
            default_choice = 'd'
        else:
            default_choice = 'y'
        
        # Ask if user wants to add this sensor
        add_choice = self.ask_add_sensor("Add this sensor?", default=default_choice)
        if add_choice == 'n':
            print(f"✗ Skipped: {sensor_name}")
            return
        
        # If 'd', add but disabled
        enabled = (add_choice == 'y')
        
        # Ask for custom name
        custom_name = input(f"Custom name (press Enter for '{sensor_name}'): ").strip()
        if not custom_name:
            custom_name = sensor_name
        
        # Check if this is a tank sensor (not temp or battery)
        is_tank = sensor_num not in [7, 8, 9, 10, 13]
        
        config = {
            "mac": mac,
            "sensor_num": sensor_num,
            "sensor_type": sensor_name,
            "custom_name": custom_name,
            "enabled": enabled,
            "discovered": time.strftime("%Y-%m-%d %H:%M:%S")
        }
        
        # Add tank-specific configuration
        if is_tank:
            print(f"\nTank Configuration:")
            capacity = self.ask_number("Tank capacity in gallons (0 = percentage only)", default=0)
            config["tank_capacity_gallons"] = capacity
        
        with open(config_file, 'w') as f:
            json.dump(config, f, indent=2)
        
        status = "✓ Enabled" if enabled else "✗ Disabled"
        print(f"\n{status}: {custom_name} -> {config_file}")
        
        # Ask if user wants to continue scanning (timer reset happens in ask_yes_no)
        print()
        if not self.ask_yes_no("Continue scanning for more sensors?", default=True):
            self.should_continue = False
    
    def ask_add_sensor(self, question: str, default: str = 'y') -> str:
        """Ask if sensor should be added: (y)es, (n)o, or (d)isabled"""
        default_display = {
            'y': 'Y/n/d',
            'n': 'y/N/d',
            'd': 'y/n/D'
        }.get(default, 'Y/n/d')
        
        while True:
            try:
                response = input(f"{question} [{default_display}]: ").strip().lower()
                # Reset idle timer on any user input
                self.last_discovery_time = time.time()
                if not response:
                    return default
                if response in ['y', 'yes']:
                    return 'y'
                if response in ['n', 'no']:
                    return 'n'
                if response in ['d', 'disabled', 'disable']:
                    return 'd'
                print("Please answer 'y' (yes/enabled), 'n' (no/skip), or 'd' (add but disabled)")
            except (KeyboardInterrupt, EOFError):
                print("\n\nDiscovery cancelled by user")
                self.should_continue = False
                raise KeyboardInterrupt
    
    
    def ask_yes_no(self, question: str, default: bool = True) -> bool:
        """Ask a yes/no question"""
        default_str = "Y/n" if default else "y/N"
        while True:
            try:
                response = input(f"{question} [{default_str}]: ").strip().lower()
                # Reset idle timer on any user input
                self.last_discovery_time = time.time()
                if not response:
                    return default
                if response in ['y', 'yes']:
                    return True
                if response in ['n', 'no']:
                    return False
                print("Please answer 'y' or 'n'")
            except (KeyboardInterrupt, EOFError):
                print("\n\nDiscovery cancelled by user")
                self.should_continue = False
                raise KeyboardInterrupt
    
    def ask_number(self, question: str, default: float = 0) -> float:
        """Ask for a number"""
        while True:
            try:
                response = input(f"{question} [{default}]: ").strip()
                # Reset idle timer on any user input
                self.last_discovery_time = time.time()
                if not response:
                    return default
                try:
                    return float(response)
                except ValueError:
                    print("Please enter a valid number")
            except (KeyboardInterrupt, EOFError):
                print("\n\nDiscovery cancelled by user")
                self.should_continue = False
                raise KeyboardInterrupt

    def check_idle_timeout(self):
        """Check if we've been idle for 30 seconds since last user interaction"""
        if self.last_discovery_time is None:
            return False
        
        idle_time = time.time() - self.last_discovery_time
        if idle_time >= 30:
            print(f"\n{'='*80}")
            if len(self.discovered) == 0:
                print(f"No sensors found in 30 seconds...")
            else:
                print(f"No new sensors found in 30 seconds...")
            if not self.ask_yes_no("Continue scanning?", default=False):
                return True
            # Timer already reset by ask_yes_no
        return False

    def run(self):
        """Run discovery"""
        print(f"Scanning for SeeLevel 709-BT devices...")
        print(f"Config directory: {CONFIG_DIR}")
        print("-" * 80)
        print("Scanning", end='', flush=True)
        
        btmon_proc = None
        try:
            btmon_proc = subprocess.Popen(
                ['btmon'],
                stdout=subprocess.PIPE,
                stderr=subprocess.DEVNULL,
                universal_newlines=True,
                bufsize=1
            )
            
            self.last_discovery_time = time.time()
            last_idle_check = time.time()
            last_progress_dot = time.time()
            
            while self.should_continue:
                line = btmon_proc.stdout.readline()
                if line:
                    self.parse_btmon_line(line)
                
                # Show progress dot every 5 seconds
                if time.time() - last_progress_dot >= 5:
                    print(".", end='', flush=True)
                    last_progress_dot = time.time()
                
                # Check for idle timeout every second
                if time.time() - last_idle_check >= 1:
                    if self.check_idle_timeout():
                        break
                    last_idle_check = time.time()
            
        except KeyboardInterrupt:
            print("\n\nDiscovery cancelled by user")
            if btmon_proc:
                btmon_proc.terminate()
                try:
                    btmon_proc.wait(timeout=2)
                except:
                    btmon_proc.kill()
            sys.exit(0)
        finally:
            if btmon_proc and btmon_proc.poll() is None:
                btmon_proc.terminate()
                try:
                    btmon_proc.wait(timeout=2)
                except:
                    btmon_proc.kill()
        
        print("-" * 80)
        print(f"\nDiscovered {len(self.discovered)} sensor(s)")
        print(f"\nConfiguration files created in: {CONFIG_DIR}")
        print("\nTo customize:")
        print(f"  1. Edit the .json files in {CONFIG_DIR}")
        print(f"  2. Change 'custom_name' to rename devices")
        print(f"  3. Set 'enabled' to false to suppress devices")
        print(f"  4. Run the main service: python3 /data/dbus-seelevel-service.py")
        
        return 0


def main():
    """Main entry point"""
    discovery = SeeLevelDiscovery()
    sys.exit(discovery.run())


if __name__ == '__main__':
    main()
