#!/bin/bash
#
# Disable script for victron-seelevel-python (dbus-seelevel service)
# Cleanly stops and removes the service
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

echo "$SERVICE_NAME disabled"
echo

