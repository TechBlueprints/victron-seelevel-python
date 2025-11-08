# 708-RBT Analysis: APK Coverage

**Question**: Does the SeeLeveL RV 2.0 app (v1.1.1) mention or support the 708-RBT?

---

## Timeline Analysis

### 708-RBT Announcement
- **Announced**: September 18, 2025
- **Launch Date**: Beginning of December 2025
- **Status**: Not yet released

### SeeLeveL RV 2.0 App (v1.1.1)
- **APK Date**: December 31, 1979 (timestamp artifact)
- **Version**: 1.1.1
- **Analysis Date**: October 21, 2025
- **Status**: Current production version

---

## APK String Search Results

### Search for "708":
```bash
strings seelevel-app/assets/index.android.bundle | grep -i "708"
```
**Result**: ❌ No matches

### Search for "Soul":
```bash
strings seelevel-app/assets/index.android.bundle | grep -E "soul|Soul|SOUL"
```
**Result**: Only 1 match found: `BCSoul` (likely a code variable name, not product reference)

### Search for Related Terms:
- "RV-C" / "RVC": ❌ No matches
- "multiplex": ❌ No matches (only generic code terms)
- "CAN bus": ❌ No matches

### Confirmed Device Support in APK:
```
"709-BT"
"709-BTP3"
"709-BTP7"
"- This app now supports both 709-BTP3 and 709-BTP7 devices."
```
**Result**: ✅ Only 709 series found

---

## Conclusion

### ❌ **708-RBT is NOT in Current App**

The SeeLeveL RV 2.0 app (v1.1.1) does **not** mention or support the 708-RBT because:

1. **Timeline**: App version predates the 708-RBT announcement (September 2025)
2. **Product Launch**: 708-RBT launches December 2025 (future)
3. **String Analysis**: No references to "708", "Soul", "RV-C", or multiplex features

### 📋 **What This Means**

The 708-RBT news article states:
> "Setup can be completed using either the Soul Configuration Tool via Micro-USB or through Bluetooth®"

This suggests that when the 708-RBT launches in December 2025:

#### Option 1: Existing App Updated
Garnet may release an **updated version** of SeeLeveL RV 2.0 (v1.2+) that adds:
- 708-RBT device detection
- Configuration interface via Bluetooth
- Setup wizards for RV-C integration

#### Option 2: Separate Configuration Tool
Garnet may provide a **separate "Soul Configuration Tool"** for:
- OEM/installer use only
- Micro-USB connection for initial setup
- Professional configuration features

#### What Bluetooth Does for 708-RBT:
Based on the product description, Bluetooth is for:
- ✅ **Configuration**: Setting up tank types, sender configs, RV-C parameters
- ✅ **Diagnostics**: Viewing system status, troubleshooting
- ❌ **NOT for continuous monitoring**: Tank data goes via RV-C bus, not BLE

---

## Future App Updates (Prediction)

When 708-RBT launches (December 2025), the app **might** add:

### Possible New Strings (v1.2+):
```
"708-RBT"
"SeeLeveL Soul"
"RV-C Configuration"
"Multiplex Setup"
"CAN Bus Integration"
"Select 708-RBT from the list"
```

### Possible New Features:
- 708-RBT device detection (new manufacturer ID or service UUID?)
- Configuration screens for RV-C parameters
- Tank sender setup for multiplex integration
- Diagnostics dashboard for RV-C bus status

### What WON'T Change:
The app will **NOT** use BLE for continuous tank monitoring from 708-RBT because:
- Tank data flows via RV-C bus (to RV multiplex system)
- Bluetooth is only for setup/configuration
- Different protocol entirely from 709 series

---

## Impact on Our Implementation

### ✅ **Current Status: Still Complete**

Our implementation covers all devices **currently supported** by the app:
- 709-BT ✅
- 709-BTP3 ✅
- 709-BTP7 ✅

### 🔮 **Future Status: Depends on Implementation**

If Garnet adds 708-RBT to the app in December 2025, we need to determine:

#### Scenario A: Configuration-Only Bluetooth
If 708-RBT uses Bluetooth **only** for configuration:
- ✅ **No action needed** - Tank data via RV-C, not BLE scanning
- ✅ Our implementation remains complete for BLE monitoring

#### Scenario B: Dual-Mode Bluetooth
If 708-RBT **also** broadcasts tank data via BLE (unlikely):
- ❓ Would need new manufacturer ID or service UUID
- ❓ Would need new packet format
- ❓ Would require update to our implementation

**Likelihood**: Scenario A is **much more likely** based on:
- Product description emphasizes RV-C for tank data
- Bluetooth mentioned only for "configuration"
- OEM product targeting multiplex integration
- Marketing focuses on RV-C bus transmission

---

## Recommendation

### For Now (October 2025):
✅ **No action needed** - 708-RBT not yet released and not in current app

### When 708-RBT Launches (December 2025):
📋 **Monitor for**:
1. Updated SeeLeveL RV 2.0 app version (v1.2+)
2. Any mention of BLE protocol for 708-RBT
3. New manufacturer IDs or service UUIDs
4. User reports of 708-RBT detection

### If BLE Monitoring Added:
🔧 **Update implementation** if (and only if):
- 708-RBT broadcasts tank data via BLE advertisements
- New manufacturer ID or packet format identified
- Users request 708-RBT support for Victron integration

### Most Likely Outcome:
✅ **No update needed** because:
- 708-RBT will use RV-C for tank data (not BLE)
- Bluetooth is configuration-only (not monitoring)
- Different integration path (CAN bus, not BLE scanning)

---

## Summary

**Current App Status**: ❌ No 708-RBT support (product not yet released)  
**Future App Status**: 🔮 TBD (likely configuration-only, December 2025)  
**Our Implementation**: ✅ Complete for all BLE monitoring devices (709 series)  
**Action Required**: ❌ None (until 708-RBT protocol details available)

The article mentions Bluetooth for the 708-RBT, but the current app (v1.1.1) has **no references** to it. This makes sense because:
1. Product launches December 2025 (future)
2. App version predates announcement
3. Bluetooth is for configuration, not continuous monitoring

**Bottom line**: We're still good. The 708-RBT uses a different architecture (RV-C) and is not relevant to our BLE scanning implementation. ✅





