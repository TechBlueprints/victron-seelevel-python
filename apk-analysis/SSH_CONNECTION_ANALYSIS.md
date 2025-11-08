# SSH Connection Limiting on Cerbo GX

**Issue**: Multiple rapid SSH connections get blocked/timeout  
**Cause**: Default SSH daemon rate limiting (MaxStartups)

---

## SSH Configuration Analysis

### Active Settings:

```
Protocol 2
macs hmac-sha2-256-etm@openssh.com,hmac-sha2-512-etm@openssh.com,hmac-sha2-256,hmac-sha2-512
PermitRootLogin yes
PermitEmptyPasswords yes
Compression no
ClientAliveInterval 15
ClientAliveCountMax 4
UseDNS no
```

### Commented Defaults (from sshd_config):

```
#LoginGraceTime 2m
#MaxSessions 10
#MaxStartups 10
ClientAliveInterval 15
```

---

## Root Cause: MaxStartups

### What is MaxStartups?

**Default Value**: `10` (when commented, OpenSSH uses `10:30:100`)

**Meaning**: `start:rate:full`
- `10` = Accept 10 concurrent unauthenticated connections
- `30` = Start dropping 30% of new connections after 10
- `100` = Drop all new connections after 100 pending

### How It Works:

1. **First 10 connections**: All accepted immediately
2. **Connections 11-100**: Randomly dropped (30% drop rate initially)
3. **After 100 pending**: All new connections refused

### Why This Happens:

When we SSH multiple times rapidly:
1. Each `ssh` command opens a connection
2. Connection stays "pending" during authentication (~1-2 seconds)
3. If we open 10+ connections before first ones complete, rate limiting kicks in
4. New connections start timing out or getting refused

---

## Related Settings

### ClientAliveInterval 15

**What it does**: Server sends keepalive packets every 15 seconds  
**Purpose**: Detect dead connections  
**Impact**: Closes stale connections after `15 * 4 = 60 seconds` of no response

### LoginGraceTime 2m (default)

**What it does**: Maximum time allowed for authentication  
**Impact**: Pending connections held for up to 2 minutes before cleanup

---

## Why This Affects Us

When running multiple `ssh` commands in quick succession (like our checks):

```bash
ssh root@10.10.0.118 "svstat /service/dbus-seelevel"      # Connection 1
ssh root@10.10.0.118 "tail /var/log/dbus-seelevel/current" # Connection 2
ssh root@10.10.0.118 "ps | grep dbus-seelevel"           # Connection 3
ssh root@10.10.0.118 "dbus -y | grep seelevel"           # Connection 4
```

Each command:
1. Opens new TCP connection
2. Performs SSH handshake (~200-500ms)
3. Authenticates (~200-500ms)
4. Runs command
5. Closes connection

If commands overlap, we can easily hit the 10 connection limit.

---

## Solutions

### Option 1: Use SSH Multiplexing (Recommended)

**Reuse a single SSH connection for multiple commands**:

Add to your `~/.ssh/config`:
```
Host cerbo
    HostName 10.10.0.118
    User root
    ControlMaster auto
    ControlPath ~/.ssh/control-%r@%h:%p
    ControlPersist 10m
```

**Benefits**:
- First connection opens master socket
- Subsequent connections reuse it instantly
- No authentication delay
- Won't hit MaxStartups limit
- Much faster (no handshake overhead)

**Usage**:
```bash
ssh cerbo "svstat /service/dbus-seelevel"
ssh cerbo "tail /var/log/dbus-seelevel/current"
ssh cerbo "ps | grep dbus-seelevel"
# All use same connection!
```

### Option 2: Add Delays Between Commands

**Simple but slower**:
```bash
ssh root@10.10.0.118 "command1"
sleep 1
ssh root@10.10.0.118 "command2"
sleep 1
ssh root@10.10.0.118 "command3"
```

**Downside**: Adds 1-2 seconds per command

### Option 3: Batch Commands

**Run multiple commands in single SSH session**:
```bash
ssh root@10.10.0.118 "
    svstat /service/dbus-seelevel
    echo '---'
    tail -30 /var/log/dbus-seelevel/current | tai64nlocal
    echo '---'
    ps | grep dbus-seelevel
    echo '---'
    dbus -y | grep seelevel
"
```

**Benefits**:
- Single connection
- All commands run sequentially
- No rate limiting

**Downside**: All-or-nothing (if one fails, harder to debug)

### Option 4: Increase MaxStartups (Not Recommended)

**Edit `/etc/ssh/sshd_config` on Cerbo**:
```
MaxStartups 50:30:200
```

**Then restart SSH**:
```bash
/etc/init.d/sshd restart
```

**Downside**:
- Requires root access to Cerbo
- Changes lost on firmware update
- Opens security hole (allows more brute-force attempts)

---

## Best Practice for Our Use Case

### Recommended: SSH Multiplexing

**Setup** (one-time):
```bash
mkdir -p ~/.ssh/control
cat >> ~/.ssh/config << 'EOF'

Host cerbo
    HostName 10.10.0.118
    User root
    ControlMaster auto
    ControlPath ~/.ssh/control/%r@%h:%p
    ControlPersist 10m

EOF
```

**Usage**:
```bash
# First connection opens master
ssh cerbo "svstat /service/dbus-seelevel"

# These reuse master (instant, no authentication)
ssh cerbo "tail /var/log/dbus-seelevel/current | tai64nlocal"
ssh cerbo "ps | grep dbus-seelevel"
ssh cerbo "dbus -y | grep seelevel"

# Master persists for 10 minutes after last use
```

**Benefits**:
- ✅ No rate limiting issues
- ✅ Much faster (no handshake per command)
- ✅ No Cerbo configuration changes needed
- ✅ Secure (uses existing authentication)
- ✅ Automatic cleanup (10-minute timeout)

---

## Why This Matters for Automation

When running status checks or deployments, SSH multiplexing:

1. **Prevents timeouts** from rate limiting
2. **Speeds up operations** (no handshake overhead)
3. **Reduces load on Cerbo** (fewer authentication attempts)
4. **Improves reliability** (no random connection failures)

Example timing without multiplexing:
```
Command 1: 0.5s (handshake) + 0.5s (auth) + 0.1s (run) = 1.1s
Command 2: 0.5s + 0.5s + 0.1s = 1.1s
Command 3: 0.5s + 0.5s + 0.1s = 1.1s
Command 4: 0.5s + 0.5s + 0.1s = 1.1s
Total: 4.4 seconds
```

With multiplexing:
```
Command 1: 0.5s + 0.5s + 0.1s = 1.1s (opens master)
Command 2: 0.0s + 0.0s + 0.1s = 0.1s (reuses master)
Command 3: 0.0s + 0.0s + 0.1s = 0.1s
Command 4: 0.0s + 0.0s + 0.1s = 0.1s
Total: 1.4 seconds (3x faster!)
```

---

## Summary

**Problem**: MaxStartups default (10) limits concurrent SSH connections  
**Solution**: Use SSH multiplexing to reuse connections  
**Benefit**: Faster, more reliable, no Cerbo config changes needed  

**Implementation**:
```bash
# Add to ~/.ssh/config
Host cerbo
    HostName 10.10.0.118
    User root
    ControlMaster auto
    ControlPath ~/.ssh/control/%r@%h:%p
    ControlPersist 10m
```

Then use `ssh cerbo` instead of `ssh root@10.10.0.118` for all commands!





