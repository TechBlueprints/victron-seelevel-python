#!/bin/bash
#
# Enable script for victron-seelevel-python (dbus-seelevel service)
# This script is run on every boot via rc.local to ensure the service is properly set up
#

# Fix permissions
chmod +x /data/apps/victron-seelevel-python/data/*.py
chmod +x /data/apps/victron-seelevel-python/service/run
chmod +x /data/apps/victron-seelevel-python/service/log/run
chmod +x /data/apps/victron-seelevel-python/diagnose.sh

# Create rc.local if it doesn't exist
if [ ! -f /data/rc.local ]; then
    echo "#!/bin/bash" > /data/rc.local
    chmod 755 /data/rc.local
fi

# Add enable script to rc.local (runs on every boot)
RC_ENTRY="bash /data/apps/victron-seelevel-python/enable.sh"
grep -qxF "$RC_ENTRY" /data/rc.local || echo "$RC_ENTRY" >> /data/rc.local

# Remove old-style symlink-only entries from rc.local
sed -i '/ln -sf \/data\/apps\/victron-seelevel-python\/service \/service\/dbus-seelevel/d' /data/rc.local
sed -i '/ln -sf \/data\/apps\/dbus-seelevel\/service \/service\/dbus-seelevel/d' /data/rc.local

# Create symlink to service directory
if [ -L /service/dbus-seelevel ]; then
    rm /service/dbus-seelevel
fi
ln -s /data/apps/victron-seelevel-python/service /service/dbus-seelevel

echo "dbus-seelevel enabled"

