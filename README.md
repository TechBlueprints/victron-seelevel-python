# victron-seelevel-python

Python-based Bluetooth integration for SeeLevel 709-BT sensors on Victron Venus OS. Interactive discovery tool and DBus service for monitoring RV tank levels, temperatures, and battery voltage on Cerbo GX devices.

> **Note**: This Python implementation was created as a workaround for users who cannot build the C version. If [Pull Request #11](https://github.com/victronenergy/dbus-ble-sensors/pull/11) (addressing [Issue #1508](https://github.com/victronenergy/venus/issues/1508)) is accepted into the official Victron firmware, this package will no longer be needed as native SeeLevel support will be included in Venus OS.

## Overview

This Python implementation provides an alternative to the C daemon while users are waiting for Victron to accept the PR. It features:

- **Interactive Discovery Tool** - Scan for and configure sensors with guided prompts
- **Multi-Process Architecture** - Separate process for each sensor for stability
- **Automatic DBus Integration** - Sensors appear automatically in Victron UI and VRM
- **Persistent Configuration** - JSON-based config files for easy customization
- **Daemontools Service** - Auto-start on boot with automatic restart on failure

## Supported Sensors

- **Tank Sensors**: Fresh Water, Waste Water (Black), Gray Water, Galley Water, LPG, Chemical
- **Temperature Sensors**: Up to 4 temperature probes
- **Battery Monitor**: Voltage monitoring

## Project Structure

```
victron-seelevel-python/
├── data/
│   ├── dbus-seelevel-discover.py    # Interactive discovery tool
│   ├── dbus-seelevel-service.py     # Main service daemon
│   └── dbus-seelevel-sensor.py      # Individual sensor process
├── service/
│   ├── run                          # Daemontools service script
│   └── log/
│       └── run                      # Log management script
├── LICENSE                          # Apache 2.0 license
└── README.md                        # This file
```

## Prerequisites

- Victron Cerbo GX (or other Venus OS device)
- SeeLevel 709-BT sensor system
- SSH access to your Cerbo GX

## Quick Start

1. [Enable SSH](#enable-ssh-access) on your Cerbo GX
2. Copy scripts: `scp data/*.py root@<cerbo-ip>:/data/`
3. Run discovery: `ssh root@<cerbo-ip> 'python3 /data/dbus-seelevel-discover.py'`
4. Install service: `scp -r service root@<cerbo-ip>:/service/dbus-seelevel`
5. Verify running: `ssh root@<cerbo-ip> 'svstat /service/dbus-seelevel'`

See the [Installation Guide](#installation-guide) below for detailed steps.

---

# Installation Guide

## Enable SSH Access

### From the New UI (v3.x)

1. Open the Cerbo GX web interface in your browser
2. Navigate to **Settings → General**
3. Scroll down to **SSH on LAN**
4. Enable SSH access
5. Note: Default login is `root` with no password (or the password you've set)

### From the Old UI (v2.x)

1. Open the Cerbo GX web interface in your browser
2. Navigate to **Settings → General**
3. Find **SSH on LAN** and enable it
4. Note: Default login is `root` with no password (or the password you've set)

### From the Device Screen

1. On the Cerbo GX screen, go to **Settings**
2. Select **General**
3. Find **SSH on LAN** and enable it

## Installation Steps

### 1. Connect to Your Cerbo GX

```bash
ssh root@<cerbo-ip-address>
```

Replace `<cerbo-ip-address>` with your Cerbo's IP address (e.g., `192.168.1.100`).

### 2. Copy the Python Scripts

From your local machine (in the `victron-seelevel-python` directory), copy the three required Python scripts to the Cerbo:

```bash
scp data/*.py root@<cerbo-ip-address>:/data/
```

### 3. Make Scripts Executable

On the Cerbo GX:

```bash
chmod +x /data/dbus-seelevel-*.py
```

### 4. Run Discovery to Configure Your Sensors

```bash
python3 /data/dbus-seelevel-discover.py
```

This interactive script will:
- Scan for SeeLevel sensors
- Prompt you to configure each sensor found:
  - Choose whether to add it (yes/no/disabled)
  - Set a custom name
  - For tanks: Set capacity in gallons
- Create configuration files in `/data/seelevel/`
- Ask if you want to continue scanning after each sensor

**Tips:**
- Press Ctrl+C at any prompt to exit immediately
- The script will prompt if no sensors found for 30 seconds
- Battery sensors default to disabled
- Disconnected sensors (OPN) default to disabled
- You can re-run discovery later; it will ask if you want to reconfigure existing sensors

### 5. Test the Service

Before setting it up as a permanent service, test it manually:

```bash
python3 /data/dbus-seelevel-service.py
```

You should see output like:
```
2025-10-05 02:19:24,942 - SeeLevel 709-BT Service v1.0
2025-10-05 02:19:24,950 - Loaded config: Fresh Water (00:A0:50:8D:95:69)
2025-10-05 02:19:24,952 - Loaded config: Waste Water (00:A0:50:8D:95:69)
2025-10-05 02:19:24,955 - Loaded config: Gray Water (00:A0:50:8D:95:69)
2025-10-05 02:19:24,959 - Loaded 3 enabled sensor(s)
2025-10-05 02:19:24,966 - Started btmon
2025-10-05 02:19:24,974 - Service running...
```

Check your Cerbo UI - you should see your sensors appearing with live data. Press Ctrl+C to stop the test.

### 6. Install as a Permanent Service

Copy the service files from your local machine to the Cerbo:

```bash
scp -r service root@<cerbo-ip-address>:/service/dbus-seelevel
```

This copies the pre-configured service directory structure including:
- `/service/dbus-seelevel/run` - Main service script
- `/service/dbus-seelevel/log/run` - Log management script

Make the service scripts executable:

```bash
ssh root@<cerbo-ip-address> 'chmod +x /service/dbus-seelevel/run /service/dbus-seelevel/log/run'
```

The service will start automatically within a few seconds. Supervise monitors the `/service/` directory and automatically starts any services it finds.

### 7. Verify the Service is Running

Check the service status:

```bash
svstat /service/dbus-seelevel
```

You should see something like:
```
/service/dbus-seelevel: up (pid 11152) 461 seconds
```

View the logs:

```bash
tail -f /var/log/dbus-seelevel/current
```

Press Ctrl+C to stop watching the logs.

## File Locations

- **Scripts**: `/data/dbus-seelevel-*.py`
- **Configuration**: `/data/seelevel/*.json`
- **Service**: `/service/dbus-seelevel/`
- **Logs**: `/var/log/dbus-seelevel/`

## Configuration Files

Each sensor gets its own JSON configuration file in `/data/seelevel/`. Example:

**`/data/seelevel/fresh-water.json`:**
```json
{
  "mac": "00:A0:50:8D:95:69",
  "sensor_num": 0,
  "sensor_type": "Fresh Water",
  "custom_name": "Fresh Water",
  "enabled": true,
  "discovered": "2025-10-05 01:22:14",
  "tank_capacity_gallons": 50.0
}
```

You can manually edit these files to:
- Change `custom_name` to rename the sensor in the UI
- Set `enabled` to `false` to disable a sensor
- Adjust `tank_capacity_gallons` (0 = percentage only, no volume)

After editing, restart the service:

```bash
svc -t /service/dbus-seelevel
```

## Service Management

The service uses daemontools/supervise for management:

- **Check status**: `svstat /service/dbus-seelevel`
- **Stop service**: `svc -d /service/dbus-seelevel`
- **Start service**: `svc -u /service/dbus-seelevel`
- **Restart service**: `svc -t /service/dbus-seelevel`
- **View logs**: `tail -f /var/log/dbus-seelevel/current`

The service will:
- ✅ Start automatically on boot
- ✅ Auto-restart if it crashes
- ✅ Run continuously in the background
- ✅ Rotate logs automatically (4 files, 25KB each)

## Troubleshooting

### Sensors Not Appearing in UI

1. Check the service is running: `svstat /service/dbus-seelevel`
2. Check the logs: `tail -f /var/log/dbus-seelevel/current`
3. Verify configuration files exist: `ls -la /data/seelevel/`
4. Ensure sensors are enabled in config files
5. Check Bluetooth is working: `btmon` (Ctrl+C to stop)

### Wrong Values Displayed

1. Check the sensor configuration in `/data/seelevel/*.json`
2. For wrong capacity, adjust `tank_capacity_gallons`
3. Restart service after changes: `svc -t /service/dbus-seelevel`

### Service Won't Start

1. Check the run script is executable: `ls -la /service/dbus-seelevel/run`
2. Check for Python errors: `tail -f /var/log/dbus-seelevel/current`
3. Verify scripts exist: `ls -la /data/dbus-seelevel-*.py`
4. Test manually: `python3 /data/dbus-seelevel-service.py`

### Re-running Discovery

You can re-run discovery at any time:

```bash
python3 /data/dbus-seelevel-discover.py
```

It will detect existing configurations and ask if you want to reconfigure them.

## Uninstallation

To remove the service:

```bash
# Stop the service
svc -d /service/dbus-seelevel

# Remove the service directory
rm -rf /service/dbus-seelevel

# Remove the scripts and configs (optional)
rm -f /data/dbus-seelevel-*.py
rm -rf /data/seelevel

# Remove the logs (optional)
rm -rf /var/log/dbus-seelevel
```

## Support

For issues or questions:
- Check the logs: `/var/log/dbus-seelevel/current`
- Review configuration files: `/data/seelevel/*.json`
- Verify Bluetooth is working on your Cerbo GX
- Ensure your SeeLevel 709-BT is powered on and broadcasting

---

# Technical Reference

## SeeLevel 709-BT Specification

The Garnet 709-BT hardware supports Bluetooth Low Energy (BLE), and is configured as a Broadcaster transmitting advertisement packets. It continuously cycles through its connected sensors sending out sensor data. No BLE connection is required to read the data.

### BLE Packet Format

**Manufacturer ID**: 305 (0x0131) - Cypress Semiconductor

**Payload** (14 bytes):
- **Bytes 0-2**: Coach ID (24-bit unique hardware ID, little-endian)
- **Byte 3**: Sensor Number (0-13)
- **Bytes 4-6**: Sensor Data (3 ASCII characters)
- **Bytes 7-9**: Sensor Volume (3 ASCII characters, gallons)
- **Bytes 10-12**: Sensor Total (3 ASCII characters, gallons)
- **Byte 13**: Sensor Alarm (ASCII digit '0'-'9')

### Sensor Numbers

| Number | Sensor Type |
|--------|-------------|
| 0 | Fresh Water |
| 1 | Black Water (displayed as "Waste Water") |
| 2 | Gray Water |
| 3 | LPG |
| 4 | LPG 2 |
| 5 | Galley Water |
| 6 | Galley Water 2 |
| 7 | Temperature |
| 8 | Temperature 2 |
| 9 | Temperature 3 |
| 10 | Temperature 4 |
| 11 | Chemical |
| 12 | Chemical 2 |
| 13 | Battery (voltage × 10) |

### Status Codes

In the Sensor Data field (bytes 4-6):
- **"OPN"**: Sensor open/disconnected (device not created)
- **"ERR"**: Sensor error (device shown with error status)
- **Numeric**: Actual sensor reading

### Unit Conversions

- **Tank Volume/Capacity**: Gallons × 0.00378541 = m³
- **Temperature**: (°F - 32) × 5/9 = °C
- **Battery Voltage**: Value ÷ 10 = Volts
- **Tank Level**: Direct percentage (0-100)

### Victron DBus Mappings

**Product IDs**:
- Tank Sensor: `0xA142` (VE_PROD_ID_TANK_SENSOR)
- Temperature Sensor: `0xA143` (VE_PROD_ID_TEMPERATURE_SENSOR)
- Battery Monitor: `0xA381` (VE_PROD_ID_BATTERY_MONITOR)

**Fluid Types**:
- Fresh Water: `1` (FLUID_TYPE_FRESH_WATER)
- Waste Water: `2` (FLUID_TYPE_WASTE_WATER)
- Black Water: `5` (FLUID_TYPE_BLACK_WATER)
- LPG: `8` (FLUID_TYPE_LPG)
- Chemical: `0` (Custom)

**Note**: Sensor 1 (Black Water) is displayed as "Waste Water" but uses `FLUID_TYPE_BLACK_WATER` (5) for Victron compatibility.

---

## License

Copyright 2025 Clint Goudie-Nice

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
