# Dependencies

This project bundles external Python libraries in the `data/ext/` directory for deployment to Venus OS systems, which typically lack internet access and package management tools.

## Why Bundle Dependencies?

Venus OS (Cerbo GX, Venus GX, etc.) systems:
- **No pip or package manager** - cannot install from PyPI
- **Limited/no internet access** - especially in marine/RV environments  
- **Immutable root filesystem** - custom software must be self-contained

This approach follows the pattern used by Victron's own [dbus-serialbattery](https://github.com/mr-manuel/venus-os_dbus-serialbattery) project.

## Bundled Libraries

### Bleak (v0.22.3)
- **Purpose**: Bluetooth Low Energy (BLE) communication
- **Source**: https://github.com/hbldh/bleak
- **License**: MIT
- **Why needed**: Communicates with SeeLevel tank sensors via BLE

###pycryptodome (Crypto module)
- **Purpose**: Cryptographic operations for BLE advertisement decryption
- **Source**: https://github.com/Legrandin/pycryptodome
- **License**: BSD/Public Domain
- **Why needed**: Decrypts encrypted Victron BLE instant readout data

### victron-ble (v0.8.0)
- **Purpose**: Victron BLE protocol decoder
- **Source**: https://github.com/keshavdv/victron-ble  
- **License**: MIT
- **Why needed**: Parses Victron-specific BLE advertisement formats

### velib_python
- **Purpose**: Venus OS D-Bus integration library
- **Source**: https://github.com/victronenergy/velib_python
- **License**: MIT
- **Why needed**: Publishes data to Venus OS D-Bus for GUI/VRM integration

## Updating Dependencies

To update a bundled library:

1. Install the desired version locally:
   ```bash
   pip install bleak==0.22.3 --target=/tmp/deps
   ```

2. Copy to `data/ext/`:
   ```bash
   cp -r /tmp/deps/bleak data/ext/
   ```

3. Test on Venus OS to ensure compatibility

4. Update this file with the new version

## For Developers

When developing locally, you can install dependencies normally:

```bash
pip install bleak pycryptodome victron-ble
```

The code will preferentially import from `data/ext/` when running on Venus OS, falling back to system packages for local development.

