#!/bin/bash
#
# Installation script for victron-seelevel-python on Venus OS (Cerbo GX)
#
# This script sets up the service to run automatically via daemontools (supervise/svc)
# It assumes the repository has already been cloned to /data/apps/victron-seelevel-python
#

set -e

INSTALL_DIR="/data/apps/victron-seelevel-python"
OLD_INSTALL_DIR="/data/apps/dbus-seelevel"
SERVICE_NAME="dbus-seelevel"

echo "========================================"
echo "Installing victron-seelevel-python"
echo "========================================"
echo ""

# Check if running on Venus OS
if [ ! -d "/data/apps" ]; then
    echo "Error: /data/apps not found. This script must run on Venus OS."
    exit 1
fi

# Check that we're running from the correct location
if [ ! -f "$INSTALL_DIR/data/dbus-seelevel-service.py" ]; then
    echo "Error: Script must be run from $INSTALL_DIR"
    echo "Expected to find: $INSTALL_DIR/data/dbus-seelevel-service.py"
    exit 1
fi

# Check if dbus-ble-advertisements is installed
echo "Checking for dbus-ble-advertisements service..."
if ! dbus-send --system --print-reply --dest=org.freedesktop.DBus /org/freedesktop/DBus org.freedesktop.DBus.ListNames 2>/dev/null | grep -q "com.victronenergy.switch.ble_advertisements"; then
    echo ""
    echo "=========================================="
    echo "dbus-ble-advertisements NOT FOUND"
    echo "=========================================="
    echo ""
    echo "This service requires the dbus-ble-advertisements router."
    echo ""
    echo "Install it first:"
    echo "  curl -fsSL https://raw.githubusercontent.com/TechBlueprints/dbus-ble-advertisements/main/install.sh | bash"
    exit 1
fi

echo "✓ dbus-ble-advertisements service found"
echo ""

# Clean up old install location if it exists
if [ -d "$OLD_INSTALL_DIR" ] && [ "$OLD_INSTALL_DIR" != "$INSTALL_DIR" ]; then
    echo "Cleaning up old install location: $OLD_INSTALL_DIR"
    rm -rf "$OLD_INSTALL_DIR"
    echo "✓ Removed old install"
fi

# Make scripts executable
echo "Setting permissions..."
chmod +x "$INSTALL_DIR/data/dbus-seelevel-service.py"
chmod +x "$INSTALL_DIR/data/dbus-seelevel-sensor.py"
chmod +x "$INSTALL_DIR/service/run"
chmod +x "$INSTALL_DIR/service/log/run"
chmod +x "$INSTALL_DIR/diagnose.sh"
echo "✓ Permissions set"

# Add to rc.local to persist across reboots
RC_LOCAL="/data/rc.local"
RC_ENTRY="ln -sf $INSTALL_DIR/service /service/$SERVICE_NAME"
OLD_RC_ENTRY="ln -sf $OLD_INSTALL_DIR/service /service/$SERVICE_NAME"

if [ ! -f "$RC_LOCAL" ]; then
    echo "Creating /data/rc.local..."
    echo "#!/bin/bash" > "$RC_LOCAL"
    chmod 755 "$RC_LOCAL"
fi

# Remove old rc.local entry if present
if grep -qF "$OLD_RC_ENTRY" "$RC_LOCAL" 2>/dev/null; then
    echo "Removing old rc.local entry..."
    grep -vF "$OLD_RC_ENTRY" "$RC_LOCAL" > "$RC_LOCAL.tmp" && mv "$RC_LOCAL.tmp" "$RC_LOCAL"
fi

if ! grep -qF "$RC_ENTRY" "$RC_LOCAL"; then
    echo "Adding service to rc.local for persistence across reboots..."
    echo "$RC_ENTRY" >> "$RC_LOCAL"
    echo "✓ Added to rc.local"
else
    echo "✓ Already in rc.local"
fi

# Link to daemontools service directory
echo "Registering service with daemontools..."
if [ -L "/service/$SERVICE_NAME" ]; then
    echo "Service link already exists, updating..."
    svc -d /service/$SERVICE_NAME 2>/dev/null || true
    rm -f /service/$SERVICE_NAME
fi

ln -sf "$INSTALL_DIR/service" /service/$SERVICE_NAME

# Wait for service to start and register on D-Bus
echo "Waiting for service to start..."
sleep 3

# Ensure both discovery switches are always visible in GUI
echo "Ensuring discovery switches are visible..."
dbus -y com.victronenergy.switch.ble_advertisements /SwitchableOutput/relay_discovery/Settings/ShowUIControl SetValue 1 2>/dev/null && echo "✓ BLE Router discovery switch is visible" || echo "Note: BLE Router switch will be visible once service fully starts"
dbus -y com.victronenergy.switch.seelevel /SwitchableOutput/relay_discovery/Settings/ShowUIControl SetValue 1 2>/dev/null && echo "✓ SeeLevel discovery switch is visible" || echo "Note: SeeLevel switch will be visible once service fully starts"

echo ""
echo "=========================================="
echo "Installation complete!"
echo "=========================================="
echo ""
echo "The service will start automatically and begin discovering sensors."
echo ""
echo "IMPORTANT: Enable both discoveries for sensors to appear:"
echo "  1. BLE Router New Device Discovery (in BLE Router device)"
echo "  2. SeeLevel Tank Discovery (in SeeLevel Monitor device)"
echo ""
echo "Access via GUI:"
echo "  - Tap the round toggle icon (top left) to open device list"
echo "  - Enable both discovery switches"
echo ""
echo "Run diagnostic to verify setup:"
echo "  bash $INSTALL_DIR/diagnose.sh"
echo ""
echo "Service management commands:"
echo "  svc -u /service/$SERVICE_NAME  # Start service"
echo "  svc -d /service/$SERVICE_NAME  # Stop service"
echo "  svc -t /service/$SERVICE_NAME  # Restart service"
echo "  svstat /service/$SERVICE_NAME  # Check status"
echo ""
echo "View logs:"
echo "  tail -f /var/log/$SERVICE_NAME/current"
echo ""
