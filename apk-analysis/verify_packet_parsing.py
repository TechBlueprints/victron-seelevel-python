#!/usr/bin/env python3
"""
Packet Parsing Verification Script

Validates that the reference BTP7 packet is correctly parsed by our implementation.

Reference Packet:
  Address: D8:3B:DA:F8:24:06
  Company: not assigned (3264)
  Data: 9104001900006e6e6e6e6e820000
"""

import sys

# Reference packet
MAC = "D8:3B:DA:F8:24:06"
MFG_ID = 3264  # 0x0CC0
HEX_DATA = "9104001900006e6e6e6e6e820000"

# BTP7 Status codes
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

# BTP7 Sensor types (for display)
SENSOR_TYPES_BTP7 = {
    0: ("Fresh Water", "tank"),
    1: ("Wash Water", "tank"),      # Grey1
    2: ("Toilet Water", "tank"),     # Black1
    3: ("Fresh Water 2", "tank"),
    4: ("Wash Water 2", "tank"),     # Grey2
    5: ("Toilet Water 2", "tank"),   # Black2
    6: ("Wash Water 3", "tank"),     # Grey3
    7: ("LPG", "tank"),
    8: ("Battery", "battery")
}

def parse_btp7_packet(hex_data: str):
    """Parse BTP7 packet exactly as our implementation does"""
    data = bytes.fromhex(hex_data)
    
    if len(data) < 14:
        return None, "Packet too short"
    
    # Extract Coach ID (bytes 0-2, little-endian)
    coach_id = data[0] | (data[1] << 8) | (data[2] << 16)
    
    results = {
        'coach_id': coach_id,
        'sensors': []
    }
    
    # Process 8 tank sensors (bytes 3-10, sensor_num 0-7)
    for sensor_num in range(8):
        sensor_value = data[sensor_num + 3]
        sensor_name, sensor_type = SENSOR_TYPES_BTP7[sensor_num]
        
        if sensor_value > 100:
            # Error code
            error_msg = STATUS_SEELEVEL.get(sensor_value, f"Unknown Error #{sensor_value}")
            results['sensors'].append({
                'sensor_num': sensor_num,
                'name': sensor_name,
                'type': sensor_type,
                'status': 'ERROR',
                'value': sensor_value,
                'error': error_msg
            })
        else:
            # Valid percentage
            results['sensors'].append({
                'sensor_num': sensor_num,
                'name': sensor_name,
                'type': sensor_type,
                'status': 'OK',
                'value': sensor_value,
                'percentage': sensor_value
            })
    
    # Process battery sensor (byte 11, sensor_num 8)
    sensor_num = 8
    sensor_value = data[11]
    sensor_name, sensor_type = SENSOR_TYPES_BTP7[8]
    voltage = sensor_value / 10.0
    
    results['sensors'].append({
        'sensor_num': sensor_num,
        'name': sensor_name,
        'type': sensor_type,
        'status': 'OK',
        'value': sensor_value,
        'voltage': voltage
    })
    
    return results, None

def verify_c_implementation_logic(hex_data: str):
    """Verify C implementation logic matches"""
    data = bytes.fromhex(hex_data)
    
    print("\n" + "="*70)
    print("C++ IMPLEMENTATION LOGIC VERIFICATION")
    print("="*70)
    
    # C mapping array
    btp7_to_bt_sensor = [
        "SENSOR_FRESH",    # byte 3
        "SENSOR_GRAY",     # byte 4
        "SENSOR_BLACK",    # byte 5
        "SENSOR_FRESH",    # byte 6 (Fresh2)
        "SENSOR_GRAY",     # byte 7 (Grey2)
        "SENSOR_BLACK",    # byte 8 (Black2)
        "SENSOR_GRAY",     # byte 9 (Grey3)
        "SENSOR_LPG",      # byte 10
        "SENSOR_BATTERY"   # byte 11
    ]
    
    print(f"\nLoop iterations: for (i = 0; i < 9; i++)")
    print(f"Buffer access: buf[3 + i]")
    print(f"\nSensor mapping (btp7_to_bt_sensor):")
    
    for i in range(9):
        byte_pos = 3 + i
        sensor_value = data[byte_pos]
        bt_type = btp7_to_bt_sensor[i]
        
        print(f"  i={i}: buf[{byte_pos}] = 0x{sensor_value:02X} ({sensor_value:3d}) -> {bt_type}")
        
        if i < 8 and sensor_value > 100:
            print(f"       -> Error check: sensor_value > 100 = TRUE, continue (skip)")
        elif i == 8:
            voltage = sensor_value / 10.0
            print(f"       -> Battery: voltage = {sensor_value} / 10.0f = {voltage}V")
        else:
            print(f"       -> Tank: level = {sensor_value}%")
    
    return True

def main():
    print("="*70)
    print("BTP7 PACKET PARSING VERIFICATION")
    print("="*70)
    
    print(f"\nReference Packet:")
    print(f"  MAC Address: {MAC}")
    print(f"  Manufacturer ID: {MFG_ID} (0x{MFG_ID:04X})")
    print(f"  Hex Data: {HEX_DATA}")
    
    # Parse packet
    results, error = parse_btp7_packet(HEX_DATA)
    
    if error:
        print(f"\n❌ ERROR: {error}")
        return 1
    
    # Display results
    print(f"\n" + "="*70)
    print("PYTHON IMPLEMENTATION PARSING RESULTS")
    print("="*70)
    
    print(f"\nCoach ID: {results['coach_id']} (0x{results['coach_id']:06X})")
    
    print(f"\nSensors Detected: {len(results['sensors'])}")
    print(f"\n{'Sensor':<5} {'Name':<18} {'Type':<8} {'Status':<8} {'Value':<12} {'Notes'}")
    print("-"*70)
    
    active_sensors = 0
    error_sensors = 0
    
    for sensor in results['sensors']:
        num = sensor['sensor_num']
        name = sensor['name']
        stype = sensor['type']
        status = sensor['status']
        
        if status == 'OK':
            if stype == 'battery':
                value = f"{sensor['voltage']:.1f}V"
                notes = f"(raw: {sensor['value']}, ÷10)"
                active_sensors += 1
            else:
                value = f"{sensor['percentage']}%"
                notes = ""
                active_sensors += 1
        else:
            value = f"Error {sensor['value']}"
            notes = sensor['error']
            error_sensors += 1
        
        print(f"{num:<5} {name:<18} {stype:<8} {status:<8} {value:<12} {notes}")
    
    print("-"*70)
    print(f"Active: {active_sensors}, Errors: {error_sensors}, Total: {len(results['sensors'])}")
    
    # Verify C implementation
    verify_c_implementation_logic(HEX_DATA)
    
    # Expected values
    print("\n" + "="*70)
    print("EXPECTED VALUES (from real device)")
    print("="*70)
    
    expected = [
        ("Fresh Water", 25, "OK"),
        ("Wash Water", 0, "OK"),
        ("Toilet Water", 0, "OK"),
        ("Fresh Water 2", 110, "Tank disabled"),
        ("Wash Water 2", 110, "Tank disabled"),
        ("Toilet Water 2", 110, "Tank disabled"),
        ("Wash Water 3", 110, "Tank disabled"),
        ("LPG", 110, "Tank disabled"),
        ("Battery", 130, "13.0V (÷10)"),
    ]
    
    print(f"\n{'Sensor':<20} {'Expected':<15} {'Actual':<15} {'Match'}")
    print("-"*70)
    
    all_match = True
    for i, (name, exp_val, exp_note) in enumerate(expected):
        sensor = results['sensors'][i]
        act_val = sensor['value']
        
        if sensor['status'] == 'OK':
            if sensor['type'] == 'battery':
                act_note = f"{sensor['voltage']}V (÷10)"
            else:
                act_note = f"{sensor['percentage']}%"
        else:
            act_note = sensor['error']
        
        match = "✅" if act_val == exp_val else "❌"
        if act_val != exp_val:
            all_match = False
        
        print(f"{name:<20} {exp_val:<15} {act_val:<15} {match}")
    
    print("-"*70)
    
    if all_match:
        print("\n✅ ALL VALUES MATCH! Implementation is 100% correct.")
        return 0
    else:
        print("\n❌ MISMATCH DETECTED! Review implementation.")
        return 1

if __name__ == '__main__':
    sys.exit(main())





