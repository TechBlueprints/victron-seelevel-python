#!/bin/bash
#
# Installation script for victron-seelevel-python on Venus OS (Cerbo GX)
#
# This script installs the service to /data/apps/dbus-seelevel
# and sets it up to run automatically via daemontools (supervise/svc)
#

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
INSTALL_DIR="/data/apps/dbus-seelevel"

echo "========================================"
echo "Installing victron-seelevel-python"
echo "========================================"
echo ""

# Check if running on Venus OS
if [ ! -d "/data/apps" ]; then
    echo "Error: /data/apps not found. This script must run on Venus OS."
    exit 1
fi

# Check if dbus-ble-advertisements is installed
echo "Checking for dbus-ble-advertisements service..."
if ! dbus-send --system --print-reply --dest=org.freedesktop.DBus /org/freedesktop/DBus org.freedesktop.DBus.ListNames 2>/dev/null | grep -q "com.victronenergy.switch.ble.advertisements"; then
    echo ""
    echo "=========================================="
    echo "dbus-ble-advertisements NOT FOUND"
    echo "=========================================="
    echo ""
    echo "This service requires the dbus-ble-advertisements router."
    echo ""
    
    # Check if we can auto-install
    if command -v curl >/dev/null 2>&1; then
        echo "Would you like to install it now? (y/n)"
        read -r response
        if [ "$response" = "y" ] || [ "$response" = "Y" ]; then
            echo ""
            echo "Installing dbus-ble-advertisements..."
            curl -fsSL https://raw.githubusercontent.com/TechBlueprints/dbus-ble-advertisements/main/install.sh | bash
            if [ $? -eq 0 ]; then
                echo ""
                echo "✓ dbus-ble-advertisements installed successfully"
                echo ""
            else
                echo ""
                echo "ERROR: Failed to install dbus-ble-advertisements"
                echo "Please install manually from: https://github.com/TechBlueprints/dbus-ble-advertisements"
                exit 1
            fi
        else
            echo ""
            echo "Installation cancelled. You must install dbus-ble-advertisements first:"
            echo "  curl -fsSL https://raw.githubusercontent.com/TechBlueprints/dbus-ble-advertisements/main/install.sh | bash"
            echo ""
            echo "Or use the legacy-standalone-btmon branch:"
            echo "  https://github.com/TechBlueprints/victron-seelevel-python/tree/legacy-standalone-btmon"
            exit 1
        fi
    else
        echo "Manual installation required:"
        echo "  wget -qO- https://raw.githubusercontent.com/TechBlueprints/dbus-ble-advertisements/main/install.sh | bash"
        echo ""
        echo "Or use the legacy-standalone-btmon branch:"
        echo "  https://github.com/TechBlueprints/victron-seelevel-python/tree/legacy-standalone-btmon"
        exit 1
    fi
fi

echo "✓ dbus-ble-advertisements service found"
echo ""

# Create installation directory
echo "Creating installation directory..."
mkdir -p "$INSTALL_DIR/data"

# Copy files
echo "Copying files..."
cp -r "$SCRIPT_DIR/data/"* "$INSTALL_DIR/data/"
cp -r "$SCRIPT_DIR/service" "$INSTALL_DIR/"

# Make scripts executable
chmod +x "$INSTALL_DIR/data/dbus-seelevel-service.py"
chmod +x "$INSTALL_DIR/data/dbus-seelevel-sensor.py"
chmod +x "$INSTALL_DIR/service/run"
chmod +x "$INSTALL_DIR/service/log/run"

# Add to rc.local to persist across reboots
RC_LOCAL="/data/rc.local"
RC_ENTRY="bash $INSTALL_DIR/install-reboot.sh > $INSTALL_DIR/startup.log 2>&1 &"
SERVICE_LINK="/service/dbus-seelevel"

if [ ! -f "$RC_LOCAL" ]; then
    echo "Creating /data/rc.local..."
    echo "#!/bin/bash" > "$RC_LOCAL"
    chmod 755 "$RC_LOCAL"
fi

# Create a simple reboot script that just recreates the symlink
cat > "$INSTALL_DIR/install-reboot.sh" << 'REBOOT_SCRIPT'
#!/bin/bash
# Recreate service symlink on reboot
INSTALL_DIR="/data/apps/dbus-seelevel"
SERVICE_LINK="/service/dbus-seelevel"

if [ ! -L "$SERVICE_LINK" ]; then
    ln -s "$INSTALL_DIR/service" "$SERVICE_LINK"
fi
REBOOT_SCRIPT

chmod +x "$INSTALL_DIR/install-reboot.sh"

if ! grep -qF "$RC_ENTRY" "$RC_LOCAL"; then
    echo "Adding service to rc.local for persistence across reboots..."
    echo "$RC_ENTRY" >> "$RC_LOCAL"
    echo "✓ Added to rc.local"
else
    echo "✓ Already in rc.local"
fi

# Link to daemontools service directory
echo "Registering service with daemontools..."
if [ -L "/service/dbus-seelevel" ]; then
    echo "Service link already exists, removing..."
    svc -d /service/dbus-seelevel
    rm /service/dbus-seelevel
fi

ln -s "$INSTALL_DIR/service" /service/dbus-seelevel

echo ""
echo "=========================================="
echo "Installation complete!"
echo "=========================================="
echo ""
echo "IMPORTANT: You must run the discovery tool to configure your sensors:"
echo "  python3 $INSTALL_DIR/data/dbus-seelevel-discover.py"
echo ""
echo "After configuration, the service will start automatically."
echo ""
echo "Service management commands:"
echo "  svc -u /service/dbus-seelevel  # Start service"
echo "  svc -d /service/dbus-seelevel  # Stop service"
echo "  svc -t /service/dbus-seelevel  # Restart service"
echo "  svstat /service/dbus-seelevel  # Check status"
echo ""
echo "View logs:"
echo "  tail -f /var/log/dbus-seelevel/current"
echo ""

