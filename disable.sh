#!/bin/bash
#
# Disable script for victron-seelevel-python (dbus-seelevel service)
# Cleanly stops and removes the service and all its settings
#

# remove comment for easier troubleshooting
#set -x

INSTALL_DIR="/data/apps/victron-seelevel-python"
SERVICE_NAME="dbus-seelevel"

echo
echo "Disabling $SERVICE_NAME..."

# Remove service symlink
rm -rf "/service/$SERVICE_NAME" 2>/dev/null || true

# Kill any remaining processes
pkill -f "supervise $SERVICE_NAME" 2>/dev/null || true
pkill -f "multilog .* /var/log/$SERVICE_NAME" 2>/dev/null || true
pkill -f "python.*seelevel" 2>/dev/null || true

# Remove enable script from rc.local
sed -i "/.*victron-seelevel-python.*/d" /data/rc.local 2>/dev/null || true
sed -i "/.*dbus-seelevel.*/d" /data/rc.local 2>/dev/null || true

echo "Service stopped and rc.local cleaned"

# Clean up D-Bus settings
echo "Cleaning up D-Bus settings..."

# Function to delete a settings path
delete_setting() {
    local path="$1"
    dbus -y com.victronenergy.settings "$path" SetValue "" 2>/dev/null || true
}

# Clean up current settings paths
for path in $(dbus -y com.victronenergy.settings / GetValue 2>/dev/null | grep -oE "Settings/Devices/seelevel/[^']*" | sort -u); do
    echo "  Removing /$path"
    delete_setting "/$path"
done

# Clean up old settings paths (from previous naming conventions)
for path in $(dbus -y com.victronenergy.settings / GetValue 2>/dev/null | grep -oE "Settings/Devices/seelevel_monitor/[^']*" | sort -u); do
    echo "  Removing old /$path"
    delete_setting "/$path"
done

echo
echo "$SERVICE_NAME disabled and settings cleaned"
echo
echo "Note: To completely remove, also delete: $INSTALL_DIR"
echo "      You may also want to remove old install dir: /data/apps/dbus-seelevel"
echo
