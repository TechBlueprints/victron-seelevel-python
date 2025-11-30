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

HEARTBEAT_INTERVAL = 300  # 5 minutes
LOG_INTERVAL = 3600  # 1 hour - only log unchanged values this often


class SeeLevelService:
    """Main service that spawns sensor processes"""
    
    def __init__(self):
        self.sensor_processes: Dict[str, subprocess.Popen] = {}
        self.discovered_sensors: Dict[str, dict] = {}  # sensor_key -> {mac, sensor_type_id, sensor_num, name, type, enabled, relay_id}
        self.last_update: Dict[str, float] = {}  # sensor_key -> timestamp of last update sent
        self.last_value: Dict[str, int] = {}  # sensor_key -> last value seen
        self.last_log_time: Dict[str, float] = {}  # sensor_key -> timestamp of last log
        self.ble_scanner = None
        
        # Initialize D-Bus for signal handling
        dbus.mainloop.glib.DBusGMainLoop(set_as_default=True)
        self.bus = dbus.SystemBus()
        
        # Migrate settings from old service name if needed
        self._migrate_settings()
        
        # Create the SeeLevel Monitor service (using .switch prefix for GUI recognition)
        self.switch_service = VeDbusService('com.victronenergy.switch.seelevel', self.bus, register=False)
        
        # Add mandatory paths
        self.switch_service.add_path('/Mgmt/ProcessName', __file__)
        self.switch_service.add_path('/Mgmt/ProcessVersion', '1.0.1')
        self.switch_service.add_path('/Mgmt/Connection', 'SeeLevel Monitor')
        self.switch_service.add_path('/DeviceInstance', 120)
        self.switch_service.add_path('/ProductId', 0xFFFF)
        self.switch_service.add_path('/ProductName', 'SeeLevel Monitor')
        self.switch_service.add_path('/CustomName', 'SeeLevel Monitor')
        self.switch_service.add_path('/FirmwareVersion', '1.0.1')
        self.switch_service.add_path('/HardwareVersion', None)
        self.switch_service.add_path('/Serial', 'SEE001')
        self.switch_service.add_path('/Connected', 1)
        self.switch_service.add_path('/State', 0x100)  # Connected state
        
        # Add master "SeeLevel Discovery" switch (relay_discovery)
        self.config_enabled = True  # Default to enabled so switches are visible
        self.switch_service.add_path('/SwitchableOutput/relay_discovery/Name', '* SeeLevel Discovery')
        self.switch_service.add_path('/SwitchableOutput/relay_discovery/Type', 1)  # Toggle switch
        self.switch_service.add_path('/SwitchableOutput/relay_discovery/State', 1, 
                                     writeable=True, onchangecallback=self._on_config_switch_changed)
        self.switch_service.add_path('/SwitchableOutput/relay_discovery/Status', 0x00)
        self.switch_service.add_path('/SwitchableOutput/relay_discovery/Current', 0)
        self.switch_service.add_path('/SwitchableOutput/relay_discovery/Settings/CustomName', '', writeable=True)
        self.switch_service.add_path('/SwitchableOutput/relay_discovery/Settings/Type', 1, writeable=True)
        self.switch_service.add_path('/SwitchableOutput/relay_discovery/Settings/ValidTypes', 2)
        self.switch_service.add_path('/SwitchableOutput/relay_discovery/Settings/Function', 2, writeable=True)
        self.switch_service.add_path('/SwitchableOutput/relay_discovery/Settings/ValidFunctions', 4)
        self.switch_service.add_path('/SwitchableOutput/relay_discovery/Settings/Group', '', writeable=True)
        self.switch_service.add_path('/SwitchableOutput/relay_discovery/Settings/ShowUIControl', 1, writeable=True)
        
        # Load persisted sensors from settings
        self._load_discovered_sensors()
        
        # Register device in settings (for GUI device list)
        from settingsdevice import SettingsDevice
        settings = {
            "ClassAndVrmInstance": [
                "/Settings/Devices/seelevel/ClassAndVrmInstance",
                "switch:120",
                0,
                0,
            ],
            "DiscoveryEnabled": [
                "/Settings/Devices/seelevel/DiscoveryEnabled",
                1,  # Default: ON
                0,
                1,
            ],
        }
        self._settings = SettingsDevice(
            self.bus,
            settings,
            eventCallback=self._on_settings_changed,
            timeout=10
        )
        
        # Restore discovery state from saved settings
        discovery_state = self._settings['DiscoveryEnabled']
        self.switch_service['/SwitchableOutput/relay_discovery/State'] = discovery_state
        self.config_enabled = bool(discovery_state)
        if discovery_state:
            logging.info("Discovery enabled from saved settings")
        else:
            logging.info("Discovery disabled from saved settings")
        
        # Register the service after all paths are added
        self.switch_service.register()
        logging.info("registered ourselves on D-Bus as com.victronenergy.seelevel")
    
    def _migrate_settings(self):
        """Migrate settings from old service name to new settings path"""
        old_path = "/Settings/Devices/seelevel_monitor/ClassAndVrmInstance"
        new_path = "/Settings/Devices/seelevel/ClassAndVrmInstance"
        
        try:
            # Check if old settings exist
            settings_obj = self.bus.get_object('com.victronenergy.settings', old_path, introspect=False)
            settings_iface = dbus.Interface(settings_obj, 'com.victronenergy.BusItem')
            old_value = settings_iface.GetValue()
            
            if old_value:
                logging.info(f"Migrating settings from {old_path} to {new_path}: {old_value}")
                
                # Set the new path with the old value
                try:
                    new_obj = self.bus.get_object('com.victronenergy.settings', new_path, introspect=False)
                    new_iface = dbus.Interface(new_obj, 'com.victronenergy.BusItem')
                    new_iface.SetValue(old_value)
                    logging.info(f"Successfully migrated settings to {new_path}")
                except Exception as e:
                    logging.info(f"New settings path doesn't exist yet (will be created): {e}")
                
                # Delete the old path
                try:
                    settings_obj.Delete()
                    logging.info(f"Deleted old settings path: {old_path}")
                except Exception as e:
                    logging.warning(f"Could not delete old settings path {old_path}: {e}")
                    
        except dbus.exceptions.DBusException as e:
            # Old settings don't exist - this is fine (fresh install or already migrated)
            logging.debug(f"No old settings to migrate from {old_path}: {e}")
        except Exception as e:
            logging.warning(f"Error during settings migration: {e}")
        
    def _load_discovered_sensors(self):
        """Load persisted sensor enabled states from settings.
        
        Sensor info (mac, type, name) is rediscovered via BLE.
        Only the enabled/disabled state is persisted in settings.
        """
        # Nothing to load on startup - sensors will be rediscovered via BLE
        # Their enabled state will be loaded from settings when they're discovered
        logging.info("Sensors will be discovered via BLE advertisements")
    
    def _get_sensor_enabled_setting(self, relay_id: str) -> bool:
        """Get sensor enabled state from settings, defaulting based on sensor type"""
        try:
            settings_path = f"/Settings/Devices/seelevel/Sensor_{relay_id}"
            settings_obj = self.bus.get_object('com.victronenergy.settings', settings_path, introspect=False)
            settings_iface = dbus.Interface(settings_obj, 'com.victronenergy.BusItem')
            value = settings_iface.GetValue()
            return bool(value)
        except:
            # Setting doesn't exist yet - will be created when sensor is discovered
            return None
    
    def _set_sensor_enabled_setting(self, relay_id: str, enabled: bool, sensor_type: str = 'tank'):
        """Save sensor enabled state to settings"""
        try:
            settings_path = f"/Settings/Devices/seelevel/Sensor_{relay_id}"
            # Default: tanks and temperatures enabled, battery disabled
            default_enabled = 1 if sensor_type in ['tank', 'temperature'] else 0
            
            settings_obj = self.bus.get_object('com.victronenergy.settings', '/Settings', introspect=False)
            settings_iface = dbus.Interface(settings_obj, 'com.victronenergy.Settings')
            # AddSetting(group, name, default, type, min, max)
            settings_iface.AddSetting(
                'Devices/seelevel',
                f'Sensor_{relay_id}',
                default_enabled,
                'i',  # integer
                0,
                1
            )
            
            # Now set the actual value
            sensor_obj = self.bus.get_object('com.victronenergy.settings', settings_path, introspect=False)
            sensor_iface = dbus.Interface(sensor_obj, 'com.victronenergy.BusItem')
            sensor_iface.SetValue(1 if enabled else 0)
            
            logging.debug(f"Saved sensor {relay_id} enabled={enabled} to settings")
        except Exception as e:
            logging.error(f"Failed to save sensor setting: {e}")
    
    def _on_settings_changed(self, setting, old_value, new_value):
        """Callback when a setting changes in com.victronenergy.settings"""
        logging.debug(f"Setting changed: {setting} = {new_value}")
        # Settings are already updated by SettingsDevice, no action needed
    
    def _on_config_switch_changed(self, path: str, value):
        """Handle config switch state changes - show/hide all sensor switches"""
        new_enabled = bool(int(value) if isinstance(value, str) else value)
        
        logging.info(f"Config switch changed: new_enabled={new_enabled}, old={self.config_enabled}")
        
        # Save to persistent settings
        self._settings['DiscoveryEnabled'] = 1 if new_enabled else 0
        
        if self.config_enabled != new_enabled:
            self.config_enabled = new_enabled
            
            # Update ShowUIControl for all sensor switches
            show_value = 1 if new_enabled else 0
            for sensor_key, sensor_info in self.discovered_sensors.items():
                relay_id = sensor_info.get('relay_id')
                if relay_id:
                    output_path = f'/SwitchableOutput/relay_{relay_id}/Settings/ShowUIControl'
                    try:
                        self.switch_service[output_path] = show_value
                        logging.debug(f"Set {output_path} = {show_value}")
                    except Exception as e:
                        logging.error(f"Failed to set {output_path}: {e}")
            
            # Note: Discovery switch itself stays visible (don't hide relay_discovery)
            
            logging.info(f"SeeLevel Discovery {'enabled' if new_enabled else 'disabled'} - sensor switches {'visible' if new_enabled else 'hidden'}")
        
        return True
    
    def _create_switch(self, sensor_key: str, sensor_info: dict):
        """Create a switch for a sensor on the SeeLevel Monitor device
        
        Uses context manager to emit ItemsChanged signal so GUI picks up new switches.
        """
        # Use sensor_key (MAC_sensornum) as relay_id for clarity
        # sensor_key format: "d8:3b:da:f8:24:06_0" becomes relay_d83bdaf82406_0
        # Remove colons from MAC, keep underscore before sensor number
        if 'relay_id' not in sensor_info:
            # Split MAC and sensor_num, remove colons from MAC, rejoin with underscore
            mac, sensor_num = sensor_key.rsplit('_', 1)
            sensor_info['relay_id'] = f"{mac.replace(':', '')}_{sensor_num}"
        
        # Default enabled state: tanks and temperatures enabled, battery disabled
        if 'enabled' not in sensor_info:
            sensor_info['enabled'] = (sensor_info['type'] in ['tank', 'temperature'])
        
        relay_id = sensor_info['relay_id']
        output_path = f'/SwitchableOutput/relay_{relay_id}'
        
        # Show sensor switches based on config_enabled state
        show_ui = 1 if self.config_enabled else 0
        
        # Create switch paths using context manager to emit ItemsChanged signal
        with self.switch_service as ctx:
            ctx.add_path(f'{output_path}/Name', sensor_info['name'])
            ctx.add_path(f'{output_path}/Type', 1)  # Toggle switch
            ctx.add_path(f'{output_path}/State', 1 if sensor_info['enabled'] else 0, 
                         writeable=True, onchangecallback=lambda p, v: self._on_switch_changed(sensor_key, p, v))
            ctx.add_path(f'{output_path}/Status', 0x00)  # OK
            ctx.add_path(f'{output_path}/Current', 0)
            
            # Settings - match relay_discovery structure exactly
            ctx.add_path(f'{output_path}/Settings/CustomName', '', writeable=True)
            ctx.add_path(f'{output_path}/Settings/Type', 1, writeable=True)
            ctx.add_path(f'{output_path}/Settings/ValidTypes', 2)
            ctx.add_path(f'{output_path}/Settings/Function', 2, writeable=True)
            ctx.add_path(f'{output_path}/Settings/ValidFunctions', 4)
            ctx.add_path(f'{output_path}/Settings/Group', '', writeable=True)
            ctx.add_path(f'{output_path}/Settings/ShowUIControl', show_ui, writeable=True)
            ctx.add_path(f'{output_path}/Settings/PowerOnState', 1 if sensor_info['enabled'] else 0)
        
        logging.info(f"Created switch for {sensor_info['name']} at {output_path}, enabled={sensor_info['enabled']}")
    
    def _on_switch_changed(self, sensor_key: str, path: str, value):
        """Handle switch state changes - start or stop sensor process"""
        new_enabled = bool(int(value) if isinstance(value, str) else value)
        sensor_info = self.discovered_sensors.get(sensor_key)
        
        if not sensor_info:
            return True
        
        old_enabled = sensor_info.get('enabled', False)
        
        if old_enabled != new_enabled:
            sensor_info['enabled'] = new_enabled
            # Save to settings
            relay_id = sensor_info.get('relay_id')
            if relay_id:
                self._set_sensor_enabled_setting(relay_id, new_enabled, sensor_info.get('type', 'tank'))
            
            if new_enabled:
                # Start the sensor process
                logging.info(f"Enabling sensor: {sensor_info['name']}")
                self._start_sensor_process(sensor_key, sensor_info)
            else:
                # Stop the sensor process
                logging.info(f"Disabling sensor: {sensor_info['name']}")
                self._stop_sensor_process(sensor_key)
        
        return True
    
    def _stop_sensor_process(self, sensor_key: str):
        """Stop a sensor process by sending SHUTDOWN command"""
        if sensor_key in self.sensor_processes:
            proc = self.sensor_processes[sensor_key]
            if proc.poll() is None:  # Still running
                try:
                    # Send shutdown command
                    proc.stdin.write("SHUTDOWN\n")
                    proc.stdin.flush()
                    # Wait for graceful shutdown
                    proc.wait(timeout=2)
                except (BrokenPipeError, subprocess.TimeoutExpired):
                    # If graceful shutdown fails, force kill
                    proc.terminate()
                    try:
                        proc.wait(timeout=1)
                    except subprocess.TimeoutExpired:
                        proc.kill()
                        proc.wait()
            del self.sensor_processes[sensor_key]
            logging.info(f"Stopped sensor process for {sensor_key}")
    
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
            stdout=None,  # Inherit stdout - goes to main service log
            stderr=None,  # Inherit stderr - goes to main service log
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
        
        # Generate relay_id for this sensor
        relay_id = f"{mac.replace(':', '')}_{sensor_num}"
        
        # Check settings for persisted enabled state
        persisted_enabled = self._get_sensor_enabled_setting(relay_id)
        if persisted_enabled is not None:
            # Use persisted state
            enabled = persisted_enabled
        else:
            # Default: tanks and temperatures enabled, battery disabled
            enabled = (sensor_class in ['tank', 'temperature'])
        
        sensor_info = {
            'mac': mac,
            'sensor_type_id': sensor_type_id,
            'sensor_num': sensor_num,
            'name': name,  # e.g., "Fresh Water"
            'type': sensor_class,
            'enabled': enabled,
            'relay_id': relay_id
        }
        
        self.discovered_sensors[sensor_key] = sensor_info
        
        # Create switch for this sensor
        self._create_switch(sensor_key, sensor_info)
        
        # Save enabled state to settings (creates setting if needed)
        self._set_sensor_enabled_setting(relay_id, enabled, sensor_class)
        
        logging.info(f"Discovered sensor: {sensor_info['name']} (enabled={enabled})")
        
        # Start the sensor process if enabled
        if enabled:
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
        
        # Log all SeeLevel advertisements
        mfg_name = "BTP7/SEELEVEL" if manufacturer_id == MFG_ID_SEELEVEL else "BTP3/CYPRESS"
        logging.debug(f"Advertisement received: {mfg_name} from {mac} (name='{name}', rssi={rssi}dBm, len={len(data)})")
        
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
            data_str = data[4:7].decode('ascii', errors='ignore').strip()
            
            # Log raw advertisement data
            logging.debug(f"BTP3 Advertisement: MAC={mac}, sensor_num={sensor_num}, data_str='{data_str}', hex={hex_data[:28]}")
            
            # Skip OPN (disconnected) and ERR
            if data_str in ["OPN", "ERR"]:
                logging.debug(f"Skipping {data_str} state for sensor_num {sensor_num}")
                return
            
            # Discover sensor if not already known
            if sensor_key not in self.discovered_sensors:
                logging.info(f"Discovering NEW sensor: MAC={mac}, sensor_num={sensor_num}, data_str='{data_str}'")
                self._add_discovered_sensor(mac, sensor_type_id, sensor_num)
            
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
            logging.debug(f"BTP7 Advertisement: MAC={mac}, hex={hex_data[:28]}")
            
            # Process 8 tank sensors (sensor_num 0-7)
            for sensor_num in range(8):
                sensor_key = f"{mac}_{sensor_num}"
                
                # Discover sensor if not already known
                if sensor_key not in self.discovered_sensors:
                    sensor_value = data[sensor_num+3]  # Bytes 3-10
                    logging.info(f"Discovering NEW BTP7 sensor: MAC={mac}, sensor_num={sensor_num}, value={sensor_value}")
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
                sensor_value = data[11]  # Battery voltage × 100
                logging.info(f"Discovering NEW BTP7 battery sensor: MAC={mac}, sensor_num={sensor_num}, voltage={sensor_value/10.0}V")
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
                # Start process if needed and enabled
                if sensor_key not in self.sensor_processes and sensor_info.get('enabled', False):
                    self._start_sensor_process(sensor_key, sensor_info)
                
                # Send update (with alarm state if BTP3) - only if process is running
                if sensor_key in self.sensor_processes:
                    self.send_update(sensor_key, sensor_value, alarm_state)
                
                # Throttled logging - only log on value changes or hourly
                time_for_log = (sensor_key not in self.last_log_time) or (now - self.last_log_time[sensor_key] >= LOG_INTERVAL)
                should_log = value_changed or time_for_log
                
                if should_log:
                    self.last_log_time[sensor_key] = now
                    # Add alarm indicator for BTP3
                    alarm_suffix = f" [ALARM {alarm_state}]" if alarm_state and alarm_state > 0 else ""
                    change_indicator = " (changed)" if value_changed else ""
                    
                    if (sensor_type_id == 0 and sensor_num == 13) or (sensor_type_id == 1 and sensor_num == 8):  # Battery
                        # Both BTP3 and BTP7: voltage × 10
                        voltage = sensor_value / 10.0
                        logging.info(f"{sensor_info['name']}: {voltage}V{change_indicator}{alarm_suffix}")
                    elif sensor_type_id == 0 and sensor_num in [7, 8, 9, 10]:  # Temperature
                        temp_c = (sensor_value - 32.0) * 5.0 / 9.0
                        logging.info(f"{sensor_info['name']}: {temp_c:.1f}°C{change_indicator}{alarm_suffix}")
                    else:  # Tank
                        tank_capacity_gallons = sensor_info.get('tank_capacity_gallons', 0)
                        if tank_capacity_gallons > 0:
                            capacity_m3 = round(tank_capacity_gallons * 0.00378541, 3)
                            remaining_m3 = round(capacity_m3 * sensor_value / 100.0, 3)
                            logging.info(f"{sensor_info['name']}: {sensor_value}% ({remaining_m3}/{capacity_m3} m³){change_indicator}{alarm_suffix}")
                        else:
                            logging.info(f"{sensor_info['name']}: {sensor_value}%{change_indicator}{alarm_suffix}")
                
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
                service_name="com.victronenergy.switch.seelevel",
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
