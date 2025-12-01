#!/bin/bash
#
# Remote installer for victron-seelevel-python on Venus OS
# 
# Usage:
#   curl -fsSL https://raw.githubusercontent.com/TechBlueprints/victron-seelevel-python/main/install.sh | bash
#

set -e

REPO_URL="https://github.com/TechBlueprints/victron-seelevel-python.git"
INSTALL_DIR="/data/apps/victron-seelevel-python"
OLD_INSTALL_DIR="/data/apps/dbus-seelevel"
SERVICE_NAME="dbus-seelevel"
BLE_ROUTER_INSTALLER="https://raw.githubusercontent.com/TechBlueprints/dbus-ble-advertisements/main/install.sh"

echo "========================================"
echo "SeeLevel 709-BT Service Installer"
echo "========================================"
echo ""

# Check if running on Venus OS
if [ ! -d "/data/apps" ]; then
    echo "Error: /data/apps not found. This script must run on Venus OS."
    exit 1
fi

# Step 0: Install dbus-ble-advertisements (required dependency)
echo "Step 0: Installing required dependency (dbus-ble-advertisements)..."
echo ""
if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$BLE_ROUTER_INSTALLER" | bash
elif command -v wget >/dev/null 2>&1; then
    wget -qO- "$BLE_ROUTER_INSTALLER" | bash
else
    echo "Error: Neither curl nor wget found."
    exit 1
fi
echo ""
echo "✓ Dependency installation complete"
echo ""

# Step 1: Ensure git is installed
echo "Step 1: Checking for git..."
if ! command -v git >/dev/null 2>&1; then
    echo "Git not found. Installing git..."
    if ! opkg install git; then
        echo "Error: Failed to install git."
        exit 1
    fi
    echo "✓ Git installed successfully"
else
    echo "✓ Git already installed"
fi
echo ""

# Step 2: Clone or update repository
echo "Step 2: Setting up repository..."
cd /data/apps

NEEDS_RESTART=false

# Migrate from old install location if it exists
if [ -d "$OLD_INSTALL_DIR" ] && [ ! -d "$INSTALL_DIR" ]; then
    echo "Migrating from old install location..."
    # Stop old service if running
    svc -d /service/$SERVICE_NAME 2>/dev/null || true
    rm -f /service/$SERVICE_NAME 2>/dev/null || true
    # Move to new location
    mv "$OLD_INSTALL_DIR" "$INSTALL_DIR"
    echo "✓ Migrated to $INSTALL_DIR"
    NEEDS_RESTART=true
elif [ -d "$OLD_INSTALL_DIR" ] && [ -d "$INSTALL_DIR" ]; then
    echo "Removing old install location (new location exists)..."
    svc -d /service/$SERVICE_NAME 2>/dev/null || true
    rm -rf "$OLD_INSTALL_DIR"
    echo "✓ Removed old install at $OLD_INSTALL_DIR"
fi

if [ -d "$INSTALL_DIR" ]; then
    echo "Directory exists: $INSTALL_DIR"
    cd "$INSTALL_DIR"
    
    # Add to safe directories (ownership consideration)
    git config --global --add safe.directory "$INSTALL_DIR" 2>/dev/null || true
    
    # Check if it's already a git repository with correct remote
    if [ -d .git ]; then
        CURRENT_REMOTE=$(git remote get-url origin 2>/dev/null || echo "")
        if [ "$CURRENT_REMOTE" != "$REPO_URL" ]; then
            echo "Updating remote URL to $REPO_URL..."
            git remote set-url origin "$REPO_URL" 2>/dev/null || git remote add origin "$REPO_URL"
        fi
        
        echo "Fetching latest changes..."
        git fetch origin
        
        # Check current commit vs remote
        LOCAL=$(git rev-parse HEAD 2>/dev/null || echo "none")
        REMOTE=$(git rev-parse origin/main 2>/dev/null || echo "none")
        
        if [ "$LOCAL" != "$REMOTE" ]; then
            echo "Updates available. Resetting to latest..."
            # Discard any local changes and reset to origin/main
            git checkout main 2>/dev/null || git checkout -b main origin/main
            git reset --hard origin/main
            NEEDS_RESTART=true
            echo "✓ Repository updated to latest"
        else
            echo "✓ Already up to date"
        fi
    else
        echo "Not a git repository. Converting..."
        
        # Back up any existing files
        if [ -f "data/dbus-seelevel-service.py" ]; then
            echo "Backing up existing installation..."
            mkdir -p /tmp/seelevel-backup
            cp -r data /tmp/seelevel-backup/ 2>/dev/null || true
        fi
        
        # Initialize as git repo
        git init
        git remote add origin "$REPO_URL"
        
        # Fetch and reset to main
        git fetch origin
        git checkout -b main origin/main 2>/dev/null || git checkout main
        git reset --hard origin/main
        git branch --set-upstream-to=origin/main main 2>/dev/null || true
        
        NEEDS_RESTART=true
        echo "✓ Converted to git repository and updated to latest"
    fi
else
    echo "Directory does not exist. Cloning repository..."
    git clone "$REPO_URL" "$INSTALL_DIR"
    cd "$INSTALL_DIR"
    
    # Add to safe directories
    git config --global --add safe.directory "$INSTALL_DIR" 2>/dev/null || true
    
    NEEDS_RESTART=false  # New install, not a restart
    echo "✓ Repository cloned"
fi
echo ""

# Step 3: Install or restart service
echo "Step 3: Installing/updating service..."

# Check if service is already running
if [ -L "/service/$SERVICE_NAME" ] && svstat "/service/$SERVICE_NAME" 2>/dev/null | grep -q "up"; then
    if [ "$NEEDS_RESTART" = true ]; then
        echo "Service is already installed and running."
        echo "Updates detected. Restarting service..."
        svc -t "/service/$SERVICE_NAME"
        sleep 2
        
        # Verify it restarted
        if svstat "/service/$SERVICE_NAME" 2>/dev/null | grep -q "up"; then
            echo "✓ Service restarted successfully"
        else
            echo "Warning: Service may not have restarted properly. Check logs:"
            echo "  tail -f /var/log/$SERVICE_NAME/current"
        fi
    else
        echo "Service is already installed and running."
        echo "✓ No updates needed"
    fi
else
    echo "Service not installed or not running. Running installation..."
    bash "$INSTALL_DIR/install-service.sh"
    FRESH_INSTALL=true
fi
echo ""

echo "========================================"
echo "Installation Complete!"
echo "========================================"
echo ""

# Check if discoveries are enabled (skip for fresh installs as discovery is enabled by default)
if [ "$FRESH_INSTALL" != true ]; then
    # Wait a moment for D-Bus to be ready after restart
    sleep 2
    
    BLE_DISCOVERY=$(dbus -y com.victronenergy.switch.ble_advertisements /SwitchableOutput/relay_discovery/State GetValue 2>/dev/null || echo "")
    SEELEVEL_DISCOVERY=$(dbus -y com.victronenergy.switch.seelevel /SwitchableOutput/relay_discovery/State GetValue 2>/dev/null || echo "")
    
    WARNINGS=false
    
    if [ "$BLE_DISCOVERY" = "0" ]; then
        echo "⚠️  WARNING: BLE Router discovery is currently DISABLED"
        WARNINGS=true
    fi
    
    if [ "$SEELEVEL_DISCOVERY" = "0" ]; then
        echo "⚠️  WARNING: SeeLevel Tank discovery is currently DISABLED"
        WARNINGS=true
    fi
    
    if [ "$WARNINGS" = true ]; then
        echo ""
        echo "For sensors to appear, BOTH discoveries must be enabled:"
        echo "  1. BLE Router New Device Discovery"
        echo "  2. SeeLevel Tank Discovery"
        echo ""
        echo "See: https://github.com/TechBlueprints/victron-seelevel-python#switches-not-visible"
        echo ""
    else
        echo "✓ Both discoveries are enabled"
        echo ""
    fi
fi

echo "Run diagnostic to verify setup:"
echo "  bash $INSTALL_DIR/diagnose.sh"
echo ""
echo "Service status:"
svstat "/service/$SERVICE_NAME"
echo ""
echo "View logs:"
echo "  tail -f /var/log/$SERVICE_NAME/current"
echo ""
echo "Service management:"
echo "  svc -u /service/$SERVICE_NAME  # Start"
echo "  svc -d /service/$SERVICE_NAME  # Stop"
echo "  svc -t /service/$SERVICE_NAME  # Restart"
echo ""

