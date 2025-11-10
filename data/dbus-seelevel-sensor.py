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
SeeLevel Sensor Process - Minimal DBus service that accepts update commands via stdin
"""

import sys
import json
import os

sys.path.insert(1, '/opt/victronenergy/dbus-systemcalc-py/ext/velib_python')

try:
    from vedbus import VeDbusService
    from dbus.mainloop.glib import DBusGMainLoop
    from gi.repository import GLib
except ImportError as e:
    print(f"Error importing: {e}")
    sys.exit(1)

DBusGMainLoop(set_as_default=True)

VE_PROD_ID_TANK_SENSOR = 0xA142
VE_PROD_ID_TEMPERATURE_SENSOR = 0xA143
VE_PROD_ID_BATTERY_MONITOR = 0xA381

FLUID_TYPE_FRESH_WATER = 1
FLUID_TYPE_WASTE_WATER = 2
FLUID_TYPE_BLACK_WATER = 5
FLUID_TYPE_LPG = 8

SENSOR_TYPES = [
                {
                 0: ("Fresh Water", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_FRESH_WATER),
                 1: ("Toilet Water", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_BLACK_WATER),
                 2: ("Wash Water", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_WASTE_WATER),
                 3: ("LPG", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_LPG),
                 4: ("LPG 2", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_LPG),
                 5: ("Galley Water", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_WASTE_WATER),
                 6: ("Galley Water 2", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_WASTE_WATER),
                 7: ("Temp", "temperature", VE_PROD_ID_TEMPERATURE_SENSOR, None),
                 8: ("Temp 2", "temperature", VE_PROD_ID_TEMPERATURE_SENSOR, None),
                 9: ("Temp 3", "temperature", VE_PROD_ID_TEMPERATURE_SENSOR, None),
                 10: ("Temp 4", "temperature", VE_PROD_ID_TEMPERATURE_SENSOR, None),
                 11: ("Chemical", "tank", VE_PROD_ID_TANK_SENSOR, 0),
                 12: ("Chemical 2", "tank", VE_PROD_ID_TANK_SENSOR, 0),
                 13: ("Battery", "battery", VE_PROD_ID_BATTERY_MONITOR, None)
                },
                {
                 0: ("Fresh Water", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_FRESH_WATER),
                 1: ("Wash Water", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_WASTE_WATER),
                 2: ("Toilet Water", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_BLACK_WATER),
                 3: ("Fresh Water 2", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_FRESH_WATER),
                 4: ("Wash Water 2", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_WASTE_WATER),
                 5: ("Toilet Water 2", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_BLACK_WATER),
                 6: ("Wash Water 3", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_WASTE_WATER),
                 7: ("LPG", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_LPG),
                 8: ("Battery", "battery", VE_PROD_ID_BATTERY_MONITOR, None)
                }
               ]

class SeeLevelSensor:
    """Minimal sensor process - just maintains DBus and applies updates"""
    
    def __init__(self, mac: str, sensor_type_id: int, sensor_num: int, custom_name: str = None, 
                 tank_capacity_gallons: float = 0):
        self.mac = mac
        self.sensor_type_id = sensor_type_id
        self.sensor_num = sensor_num
        self.custom_name = custom_name
        self.tank_capacity_gallons = tank_capacity_gallons
        
        if sensor_num not in SENSOR_TYPES[sensor_type_id]:
            sys.exit(1)
        
        name, role, product_id, fluid_type = SENSOR_TYPES[sensor_type_id][sensor_num]
        
        mac_clean = mac.replace(':', '').lower()
        device_id = f"seelevel_{mac_clean}_{sensor_num:02x}"
        device_name = self.custom_name if self.custom_name else name
        service_name = f"com.victronenergy.{role}.{device_id}"
        
        self.service = VeDbusService(service_name, register=False)
        
        # Stable device instance
        mac_parts = mac.split(':')
        instance_base = int(mac_parts[-2] + mac_parts[-1], 16)
        device_instance = 100 + (instance_base % 100) + sensor_num
        
        self.service.add_path('/Mgmt/ProcessName', 'dbus-seelevel')
        self.service.add_path('/Mgmt/ProcessVersion', '1.0.1')
        self.service.add_path('/Mgmt/Connection', 'Bluetooth LE')
        self.service.add_path('/DeviceInstance', device_instance)
        self.service.add_path('/ProductId', product_id)
        self.service.add_path('/ProductName', self._get_product_name(product_id))
        self.service.add_path('/CustomName', device_name)
        self.service.add_path('/Connected', 1)
        self.service.add_path('/Status', 0)
        self.service.add_path('/Alarm', 0)  # Alarm state (0-9, where 0 = no alarm)
        
        if (sensor_type_id == 0 and sensor_num == 13) or (sensor_type_id == 1 and sensor_num == 8):  # Battery
            self.service.add_path('/Dc/0/Voltage', 0)
        elif sensor_type_id == 0 and sensor_num in [7, 8, 9, 10]:  # Temperature
            self.service.add_path('/Temperature', 0)
        else:  # Tank
            self.service.add_path('/Level', 0)
            if self.tank_capacity_gallons > 0:
                capacity_m3 = round(self.tank_capacity_gallons * 0.00378541, 3)
                self.service.add_path('/Remaining', 0)
                self.service.add_path('/Capacity', capacity_m3)
            self.service.add_path('/FluidType', fluid_type or 0)
        
        # Add SwitchableOutput paths for enable/disable control
        # Default enabled state: tanks and temperatures enabled, battery disabled
        default_enabled = (role in ["tank", "temperature"])
        
        self.service.add_path('/SwitchableOutput/0/Name', device_name)
        self.service.add_path('/SwitchableOutput/0/Type', 1)  # Toggle switch
        self.service.add_path('/SwitchableOutput/0/State', default_enabled, writeable=True,
                              onchangecallback=self._on_enabled_changed)
        self.service.add_path('/SwitchableOutput/0/Status', 0x00)  # OK
        self.service.add_path('/SwitchableOutput/0/Current', 0)
        
        # Settings
        self.service.add_path('/SwitchableOutput/0/Settings/CustomName', device_name, writeable=True)
        self.service.add_path('/SwitchableOutput/0/Settings/Type', 1)
        self.service.add_path('/SwitchableOutput/0/Settings/Group', 0)
        self.service.add_path('/SwitchableOutput/0/Settings/ShowUIControl', 1)
        self.service.add_path('/SwitchableOutput/0/Settings/PowerOnState', default_enabled)
        
        self.registered = False
        self.service_name = service_name
        self.enabled = default_enabled

    def _on_enabled_changed(self, path: str, value):
        """Handle enabled state changes"""
        # Convert value to bool (handle both int and string)
        new_enabled = bool(int(value) if isinstance(value, str) else value)
        
        if self.enabled != new_enabled:
            self.enabled = new_enabled
            # When disabled, we could set Connected=0 or just continue running
            # For now, we'll just track the state
        
        return True
    
    @staticmethod
    def _get_product_name(product_id: int) -> str:
        names = {
            VE_PROD_ID_TANK_SENSOR: "Tank Sensor",
            VE_PROD_ID_TEMPERATURE_SENSOR: "Temperature Sensor",
            VE_PROD_ID_BATTERY_MONITOR: "Battery Monitor",
        }
        return names.get(product_id, "Unknown")

    def read_stdin(self, source, condition):
        """Read update commands from stdin"""
        if condition == GLib.IO_HUP:
            # Parent closed stdin - terminate
            sys.exit(0)
        
        try:
            line = sys.stdin.readline()
            if not line:
                # EOF - parent died
                sys.exit(0)
            
            line = line.strip()
            
            # Check for shutdown command
            if line == "SHUTDOWN":
                sys.exit(0)
            
            # Parse sensor value and optional alarm (format: "value" or "value:alarm")
            if ':' in line:
                parts = line.split(':', 1)
                sensor_value = int(parts[0])
                alarm_state = int(parts[1])
            else:
                sensor_value = int(line)
                alarm_state = None
            
            self.update(sensor_value, alarm_state)
            
        except ValueError:
            # Invalid data, ignore
            pass
        except Exception:
            # Any other error, terminate
            sys.exit(1)
        
        return True

    def update(self, sensor_value: int, alarm_state: int = None):
        """Update DBus paths with new value and optional alarm"""
        if (self.sensor_type_id == 0 and self.sensor_num == 13) or (self.sensor_type_id == 1 and self.sensor_num == 8):  # Battery
            # Both BTP3 and BTP7: voltage × 10
            voltage = sensor_value / 10.0
            self.service['/Dc/0/Voltage'] = voltage
        elif self.sensor_type_id == 0 and self.sensor_num in [7, 8, 9, 10]:  # Temperature
            temp_c = (sensor_value - 32.0) * 5.0 / 9.0
            self.service['/Temperature'] = round(temp_c, 1)
        else:  # Tank
            level = sensor_value
            self.service['/Level'] = level
            
            if self.tank_capacity_gallons > 0:
                capacity_m3 = round(self.tank_capacity_gallons * 0.00378541, 3)
                remaining_m3 = round(capacity_m3 * level / 100.0, 3)
                self.service['/Capacity'] = capacity_m3
                self.service['/Remaining'] = remaining_m3
        
        # Set alarm state if provided (0-9, where 0 = no alarm)
        if alarm_state is not None:
            self.service['/Alarm'] = alarm_state
        
        self.service['/Status'] = 0
        
        # Register on first update
        if not self.registered:
            self.service.register()
            self.registered = True

    def run(self):
        """Run the sensor process"""
        GLib.io_add_watch(
            os.dup(sys.stdin.fileno()),
            GLib.IO_IN | GLib.IO_HUP,
            self.read_stdin
        )
        
        mainloop = GLib.MainLoop()
        
        try:
            mainloop.run()
        except KeyboardInterrupt:
            pass


def main():
    if len(sys.argv) < 4:
        sys.exit(1)
    
    mac = sys.argv[1]
    sensor_type_id = int(sys.argv[2])
    sensor_num = int(sys.argv[3])
    custom_name = sys.argv[4] if len(sys.argv) > 4 else None
    tank_capacity_gallons = float(sys.argv[5]) if len(sys.argv) > 5 else 0
    
    sensor = SeeLevelSensor(mac, sensor_type_id, sensor_num, custom_name, tank_capacity_gallons)
    sensor.run()


if __name__ == '__main__':
    main()
