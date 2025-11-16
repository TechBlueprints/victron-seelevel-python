# Dependencies

This project bundles external Python libraries in the `data/ext/` directory for deployment to Venus OS systems, which typically lack internet access and package management tools.

## Why Bundle Dependencies?

Venus OS (Cerbo GX, Venus GX, etc.) systems:
- **No pip or package manager** - cannot install from PyPI
- **Limited/no internet access** - especially in marine/RV environments  
- **Immutable root filesystem** - custom software must be self-contained

This approach follows the pattern used by Victron's own [dbus-serialbattery](https://github.com/mr-manuel/venus-os_dbus-serialbattery) project.

## Bundled Libraries

### velib_python
- **Purpose**: Venus OS D-Bus integration library
- **Source**: https://github.com/victronenergy/velib_python
- **License**: MIT
- **Why needed**: Publishes SeeLevel tank sensor data to Venus OS D-Bus for GUI/VRM integration

## Architecture Note

This service uses the [dbus-ble-advertisements](https://github.com/techblueprints/dbus-ble-advertisements) router for BLE scanning. It does **not** directly interact with Bluetooth hardware - all BLE advertisements are received via D-Bus signals from the router service.

The SeeLevel protocol uses simple manufacturer data parsing (Cypress ID 305, SeeLevel ID 3264) and does not require encryption libraries.

## Updating Dependencies

To update velib_python:

1. Clone the latest version:
   ```bash
   git clone https://github.com/victronenergy/velib_python /tmp/velib_python
   ```

2. Copy to `data/ext/`:
   ```bash
   cp -r /tmp/velib_python data/ext/
   ```

3. Test on Venus OS to ensure compatibility

4. Update this file with the new commit/version

## For Developers

When developing locally, you can install velib_python from source:

```bash
git clone https://github.com/victronenergy/velib_python
export PYTHONPATH="${PYTHONPATH}:$(pwd)/velib_python"
```

The code will preferentially import from `data/ext/velib_python/` when running on Venus OS.

