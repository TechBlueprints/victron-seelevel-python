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
SeeLevel Sensor Process - Reads from stdin pipe
"""

import sys
import time
import logging
import json

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

SENSOR_TYPES = {
    0: ("Fresh Water", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_FRESH_WATER),
    1: ("Waste Water", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_BLACK_WATER),  # Display as "Waste Water" but use BLACK_WATER type for Victron
    2: ("Gray Water", "tank", VE_PROD_ID_TANK_SENSOR, FLUID_TYPE_WASTE_WATER),
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
    13: ("Battery", "battery", VE_PROD_ID_BATTERY_MONITOR, None),
}

class SeeLevelSensor:
    """Individual sensor process that reads from stdin"""
    
    def __init__(self, mac: str, sensor_num: int, custom_name: str = None, 
                 tank_capacity_gallons: float = 0):
        self.mac = mac
        self.sensor_num = sensor_num
        self.custom_name = custom_name
        self.tank_capacity_gallons = tank_capacity_gallons
        
        if sensor_num not in SENSOR_TYPES:
            logging.error(f"Unknown sensor type: {sensor_num}")
            sys.exit(1)
        
        name, role, product_id, fluid_type = SENSOR_TYPES[sensor_num]
        
        mac_clean = mac.replace(':', '').lower()
        device_id = f"seelevel_{mac_clean}_{sensor_num:02x}"
        
        # Use custom_name from __init__ parameter if provided
        device_name = self.custom_name if hasattr(self, 'custom_name') and self.custom_name else name
        
        service_name = f"com.victronenergy.{role}.{device_id}"
        
        self.service = VeDbusService(service_name, register=False)
        
        # Create stable device instance based on MAC and sensor number
        # Use last 2 bytes of MAC + sensor number to create unique instance
        mac_parts = mac.split(':')
        instance_base = int(mac_parts[-2] + mac_parts[-1], 16)
        device_instance = 100 + (instance_base % 100) + sensor_num
        
        self.service.add_path('/Mgmt/ProcessName', 'dbus-seelevel')
        self.service.add_path('/Mgmt/ProcessVersion', '1.0.0')
        self.service.add_path('/Mgmt/Connection', 'Bluetooth LE')
        self.service.add_path('/DeviceInstance', device_instance)
        self.service.add_path('/ProductId', product_id)
        self.service.add_path('/ProductName', self._get_product_name(product_id))
        self.service.add_path('/CustomName', device_name)
        self.service.add_path('/Connected', 1)
        self.service.add_path('/Status', 0)
        
        if sensor_num == 13:
            self.service.add_path('/Dc/0/Voltage', 0)
        elif sensor_num in [7, 8, 9, 10]:
            self.service.add_path('/Temperature', 0)
        else:
            self.service.add_path('/Level', 0)
            # Only add capacity/remaining if we have a configured capacity
            if self.tank_capacity_gallons > 0:
                self.service.add_path('/Remaining', 0)
                self.service.add_path('/Capacity', 0)
            self.service.add_path('/FluidType', fluid_type or 0)
        
        # Don't register yet - wait for first data
        self.registered = False
        self.service_name = service_name

    @staticmethod
    def _get_product_name(product_id: int) -> str:
        names = {
            VE_PROD_ID_TANK_SENSOR: "Tank Sensor",
            VE_PROD_ID_TEMPERATURE_SENSOR: "Temperature Sensor",
            VE_PROD_ID_BATTERY_MONITOR: "Battery Monitor",
        }
        return names.get(product_id, "Unknown")

    def read_stdin(self, source, condition):
        """Read data from stdin (GLib callback)"""
        if condition == GLib.IO_HUP:
            logging.error("Stdin closed, exiting")
            sys.exit(1)
        
        try:
            line = sys.stdin.readline()
            if not line:
                return True
            
            data = json.loads(line.strip())
            self.update(data['data'], data['volume'], data['total'])
            
        except Exception as e:
            logging.error(f"Error reading stdin: {e}")
        
        return True

    def update(self, data_str: str, volume: int, total: int):
        """Update sensor value"""
        try:
            sensor_data = int(data_str.strip())
        except ValueError:
            if data_str.strip() == "ERR":
                self.service['/Status'] = 4
                logging.info(f"Error status received")
            return
        
        if self.sensor_num == 13:
            voltage = sensor_data / 10.0
            self.service['/Dc/0/Voltage'] = voltage
            logging.info(f"Updated voltage: {voltage}V")
        elif self.sensor_num in [7, 8, 9, 10]:
            temp_c = (sensor_data - 32.0) * 5.0 / 9.0
            self.service['/Temperature'] = round(temp_c, 1)
            logging.info(f"Updated temperature: {temp_c:.1f}°C")
        else:
            # Just pass through the sensor value directly - let Victron handle it based on fluid type
            level = sensor_data
            
            self.service['/Level'] = level
            logging.info(f"Updated level: {level}%")
            
            # Calculate capacity and remaining if configured
            if self.tank_capacity_gallons > 0:
                capacity_m3 = round(self.tank_capacity_gallons * 0.00378541, 3)
                self.service['/Capacity'] = capacity_m3
                
                # /Remaining should always be the liquid volume (not space)
                # level is already the filled percentage after inversion
                remaining_m3 = round(capacity_m3 * level / 100.0, 3)
                self.service['/Remaining'] = remaining_m3
                logging.info(f"  Capacity: {capacity_m3} m³ ({self.tank_capacity_gallons} gal)")
                logging.info(f"  Remaining: {remaining_m3} m³ ({level}% filled)")
        
        self.service['/Status'] = 0
        
        # Register on first update
        if not self.registered:
            self.service.register()
            logging.info(f"Registered: {self.service_name}")
            self.registered = True

    def run(self):
        """Run the sensor process"""
        # Watch stdin for data using file descriptor
        import os
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
    if len(sys.argv) < 3:
        print("Usage: dbus-seelevel-sensor.py <mac> <sensor_num> [custom_name] [tank_capacity_gallons]")
        sys.exit(1)
    
    mac = sys.argv[1]
    sensor_num = int(sys.argv[2])
    custom_name = sys.argv[3] if len(sys.argv) > 3 else None
    tank_capacity_gallons = float(sys.argv[4]) if len(sys.argv) > 4 else 0
    
    logging.basicConfig(
        level=logging.INFO,
        format=f'[{mac}_{sensor_num}] %(message)s'
    )
    
    sensor = SeeLevelSensor(mac, sensor_num, custom_name, tank_capacity_gallons)
    sensor.run()


if __name__ == '__main__':
    main()
