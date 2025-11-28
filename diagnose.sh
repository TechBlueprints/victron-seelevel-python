#!/bin/bash
#
# SeeLevel 709-BT Diagnostic Script
# Checks all services, switches, and logs to diagnose why tanks aren't appearing
#

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo "======================================================================"
echo "         SeeLevel 709-BT Diagnostic Check"
echo "======================================================================"
echo ""

ERRORS=0
WARNINGS=0

# Helper functions
print_pass() {
    echo -e "${GREEN}✓${NC} $1"
}

print_fail() {
    echo -e "${RED}✗${NC} $1"
    ERRORS=$((ERRORS + 1))
}

print_warn() {
    echo -e "${YELLOW}⚠${NC} $1"
    WARNINGS=$((WARNINGS + 1))
}

print_info() {
    echo -e "${BLUE}ℹ${NC} $1"
}

# Test 1: Check if services are running
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "TEST 1: Service Status"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Check BLE Router
BLE_STATUS=$(svstat /service/dbus-ble-advertisements 2>&1)
if echo "$BLE_STATUS" | grep -q "up"; then
    BLE_PID=$(echo "$BLE_STATUS" | sed -n 's/.*pid \([0-9]*\).*/\1/p')
    BLE_UPTIME=$(echo "$BLE_STATUS" | sed -n 's/.*) \([0-9]*\) seconds.*/\1/p')
    print_pass "BLE Router running (PID: $BLE_PID, uptime: ${BLE_UPTIME}s)"
else
    print_fail "BLE Router NOT running"
    echo "         Fix: svc -u /service/dbus-ble-advertisements"
fi

# Check SeeLevel Service
SEELEVEL_STATUS=$(svstat /service/dbus-seelevel 2>&1)
if echo "$SEELEVEL_STATUS" | grep -q "up"; then
    SEELEVEL_PID=$(echo "$SEELEVEL_STATUS" | sed -n 's/.*pid \([0-9]*\).*/\1/p')
    SEELEVEL_UPTIME=$(echo "$SEELEVEL_STATUS" | sed -n 's/.*) \([0-9]*\) seconds.*/\1/p')
    print_pass "SeeLevel service running (PID: $SEELEVEL_PID, uptime: ${SEELEVEL_UPTIME}s)"
else
    print_fail "SeeLevel service NOT running"
    echo "         Fix: svc -u /service/dbus-seelevel"
fi

echo ""

# Test 2: Check discovery switches
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "TEST 2: Discovery Switch Status"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Check BLE Router discovery
BLE_DISCOVERY=$(dbus -y com.victronenergy.switch.ble.advertisements /ble_advertisements/relay_0/State GetValue 2>/dev/null)
if [ "$BLE_DISCOVERY" = "1" ]; then
    print_pass "BLE Router discovery: ENABLED"
elif [ "$BLE_DISCOVERY" = "0" ]; then
    print_warn "BLE Router discovery: DISABLED"
    echo "         Note: Discovery can be disabled after devices are found"
    echo "         To enable: dbus -y com.victronenergy.switch.ble.advertisements /ble_advertisements/relay_0/State SetValue %1"
else
    print_fail "BLE Router discovery: CANNOT READ STATE"
fi

# Check SeeLevel discovery
SEELEVEL_DISCOVERY=$(dbus -y com.victronenergy.switch.seelevel_monitor /SwitchableOutput/relay_0/State GetValue 2>/dev/null)
if [ "$SEELEVEL_DISCOVERY" = "1" ]; then
    print_pass "SeeLevel Tank discovery: ENABLED"
elif [ "$SEELEVEL_DISCOVERY" = "0" ]; then
    print_fail "SeeLevel Tank discovery: DISABLED (must be enabled for sensors to work)"
    echo "         Fix: dbus -y com.victronenergy.switch.seelevel_monitor /SwitchableOutput/relay_0/State SetValue %1"
else
    print_fail "SeeLevel Tank discovery: CANNOT READ STATE"
fi

echo ""

# Test 3: Check manufacturer ID registrations in logs
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "TEST 3: Manufacturer ID Registration"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if [ -f /var/log/dbus-seelevel/current ]; then
    # Check for Cypress (305) registration
    if tail -n 500 /var/log/dbus-seelevel/current | grep -q "Registered interest in manufacturer ID: 0x0131"; then
        print_pass "Registered for Cypress (MFG ID 305 / 0x0131) - BTP3 devices"
    else
        print_fail "NOT registered for Cypress (MFG ID 305)"
    fi
    
    # Check for SeeLevel (3264) registration
    if tail -n 500 /var/log/dbus-seelevel/current | grep -q "Registered interest in manufacturer ID: 0x0CC0"; then
        print_pass "Registered for SeeLevel (MFG ID 3264 / 0x0CC0) - BTP7 devices"
    else
        print_fail "NOT registered for SeeLevel (MFG ID 3264)"
    fi
    
    # Check if scanner started
    if tail -n 500 /var/log/dbus-seelevel/current | grep -q "BLE scanner started successfully"; then
        print_pass "BLE scanner started and listening for advertisements"
    else
        print_warn "Cannot confirm BLE scanner started successfully"
    fi
else
    print_fail "SeeLevel log file not found: /var/log/dbus-seelevel/current"
fi

echo ""

# Test 4: Check for discovered sensors
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "TEST 4: Sensor Discovery Status"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Check if sensors.json exists
if [ -f /data/apps/dbus-seelevel/data/sensors.json ]; then
    SENSOR_COUNT=$(cat /data/apps/dbus-seelevel/data/sensors.json | grep -o '"mac"' | wc -l)
    if [ "$SENSOR_COUNT" -gt 0 ]; then
        print_pass "Found $SENSOR_COUNT previously discovered sensor(s) in sensors.json"
        
        # Show discovered sensors
        echo ""
        print_info "Discovered sensors:"
        cat /data/apps/dbus-seelevel/data/sensors.json | grep -E '"name"|"mac"|"enabled"' | head -30
    else
        print_warn "sensors.json exists but no sensors discovered yet"
    fi
else
    print_warn "No sensors.json file (first run or no sensors discovered yet)"
fi

# Check recent logs for advertisements
echo ""
if [ -f /var/log/dbus-seelevel/current ]; then
    AD_COUNT=$(tail -n 500 /var/log/dbus-seelevel/current 2>/dev/null | grep -c "Advertisement received" || echo "0")
    AD_COUNT=$(echo "$AD_COUNT" | tr -d '\n\r ')
    if [ "$AD_COUNT" -gt 0 ] 2>/dev/null; then
        print_pass "Received $AD_COUNT BLE advertisements recently"
        
        # Show recent advertisements
        echo ""
        print_info "Recent advertisements (last 5):"
        tail -n 500 /var/log/dbus-seelevel/current | grep "Advertisement received" | tail -n 5 | sed 's/^/         /'
        
        # Check for discovered sensors in logs
        DISC_COUNT=$(tail -n 500 /var/log/dbus-seelevel/current 2>/dev/null | grep -c "Discovered sensor" || echo "0")
        DISC_COUNT=$(echo "$DISC_COUNT" | tr -d '\n\r ')
        if [ "$DISC_COUNT" -gt 0 ] 2>/dev/null; then
            print_pass "Successfully discovered $DISC_COUNT sensor(s)"
            echo ""
            print_info "Discovered sensors (last 5):"
            tail -n 500 /var/log/dbus-seelevel/current | grep "Discovered sensor" | tail -n 5 | sed 's/^/         /'
        else
            print_warn "Receiving advertisements but no sensors discovered"
            echo "         This may mean discovery switch is disabled"
        fi
    else
        print_warn "No BLE advertisements received recently"
        echo "         This may mean:"
        echo "         - BLE Router discovery is disabled"
        echo "         - Your SeeLevel device is not broadcasting"
        echo "         - Your SeeLevel device MAC is disabled in BLE Router"
    fi
fi

echo ""

# Test 5: Check D-Bus service registration
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "TEST 5: D-Bus Service Registration"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Check if SeeLevel is on D-Bus
if dbus-send --system --print-reply --dest=org.freedesktop.DBus /org/freedesktop/DBus org.freedesktop.DBus.ListNames 2>/dev/null | grep -q "com.victronenergy.switch.seelevel_monitor"; then
    print_pass "SeeLevel service registered on D-Bus"
else
    print_fail "SeeLevel service NOT found on D-Bus"
fi

# Check if BLE Router is on D-Bus
BLE_ON_DBUS=$(dbus-send --system --print-reply --dest=org.freedesktop.DBus /org/freedesktop/DBus org.freedesktop.DBus.ListNames 2>/dev/null | grep -c "com.victronenergy.switch.ble.advertisements" || echo "0")
if [ "$BLE_ON_DBUS" -gt 0 ]; then
    print_pass "BLE Router registered on D-Bus"
else
    print_fail "BLE Router NOT found on D-Bus"
fi

echo ""

# Test 6: Check for active sensor processes
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "TEST 6: Active Sensor Processes"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

SENSOR_PROCS=$(ps | grep "dbus-seelevel-sensor.py" | grep -v grep | wc -l)
if [ "$SENSOR_PROCS" -gt 0 ]; then
    print_pass "Found $SENSOR_PROCS active sensor process(es)"
    echo ""
    print_info "Sensor processes:"
    ps | grep "dbus-seelevel-sensor.py" | grep -v grep | sed 's/^/         /'
else
    print_warn "No active sensor processes found"
    echo "         Sensors only start when:"
    echo "         - Sensors are discovered"
    echo "         - Sensor switches are enabled"
fi

echo ""

# Summary
echo "======================================================================"
echo "                          SUMMARY"
echo "======================================================================"
echo ""

if [ $ERRORS -eq 0 ] && [ $WARNINGS -eq 0 ]; then
    echo -e "${GREEN}✓ All checks passed!${NC}"
    echo ""
    echo "Your SeeLevel system appears to be configured correctly."
    echo "If tanks still don't appear, wait 30-60 seconds for discovery."
elif [ $ERRORS -eq 0 ]; then
    echo -e "${YELLOW}⚠ $WARNINGS warning(s) found${NC}"
    echo ""
    echo "System is mostly working but some items need attention."
    echo "Check warnings above for details."
else
    echo -e "${RED}✗ $ERRORS error(s) found${NC}"
    if [ $WARNINGS -gt 0 ]; then
        echo -e "${YELLOW}⚠ $WARNINGS warning(s) found${NC}"
    fi
    echo ""
    echo "Critical issues detected. Please fix errors above."
fi

echo ""
echo "======================================================================"
echo "                      QUICK FIXES"
echo "======================================================================"
echo ""
echo "Enable BLE Router discovery:"
echo "  dbus -y com.victronenergy.switch.ble.advertisements /ble_advertisements/relay_0/State SetValue %1"
echo ""
echo "Enable SeeLevel Tank discovery:"
echo "  dbus -y com.victronenergy.switch.seelevel_monitor /SwitchableOutput/relay_0/State SetValue %1"
echo ""
echo "Restart services:"
echo "  svc -t /service/dbus-ble-advertisements"
echo "  svc -t /service/dbus-seelevel"
echo ""
echo "Monitor for advertisements:"
echo "  tail -f /var/log/dbus-seelevel/current"
echo ""
echo "======================================================================"

exit $ERRORS

