# Garnet Instruments News Analysis (September 2025)

**Source**: [Garnet Instruments News - September 18, 2025](https://www.garnetinstruments.com/2025/09/18/)  
**Date Analyzed**: October 21, 2025  
**Relevance**: Future Product Roadmap

---

## Key Announcements

### 1. SeeLeveL Soul™ 708-RBT (NEW - December 2025)

**Product Type**: OEM RV-C multiplex system module  
**Target Market**: RV manufacturers (OEMs)  
**Launch Date**: Beginning of December 2025

#### Features:
- ✅ **Bluetooth connectivity** (new)
- ✅ **SeeLeveL RV 2.0 app** compatible (same app as 709-BT/BTP7)
- ✅ **RV-C bus integration** (CAN-based multiplex)
- ✅ **Display-free module** (concealed installation)
- ✅ **Supports up to 7 tanks**
- ✅ **Dual configuration**: Micro-USB or Bluetooth

#### Configuration Methods:
1. **Soul Configuration Tool** (via Micro-USB)
2. **SeeLeveL RV 2.0 mobile app** (via Bluetooth)

---

## Impact on Our Implementation

### ❓ **Should We Support 708-RBT?**

#### Analysis:

**Product Type**: 
- The 708-RBT is an **OEM module** for RV-C networks
- It transmits data over the **RV-C bus** (CAN protocol)
- It is **not a standalone Bluetooth tank monitor** like 709-BT/BTP7

**Integration Path**:
- **Primary**: RV-C bus → RV multiplex system → Victron Cerbo GX (via CAN)
- **Secondary**: Bluetooth → SeeLeveL RV 2.0 app (configuration/diagnostics only)

**Bluetooth Role**:
- The 708-RBT uses Bluetooth **for configuration**, not continuous monitoring
- Tank data is transmitted via **RV-C bus**, not Bluetooth advertisements
- The SeeLeveL RV 2.0 app connects for **setup/diagnostics**, not live data

### 🔍 **Key Differences from 709-BT/BTP7**

| Feature | 709-BT/BTP7 | 708-RBT |
|---------|-------------|---------|
| **Primary Interface** | Bluetooth BLE (continuous) | RV-C bus (CAN) |
| **Bluetooth Use** | Live tank monitoring | Configuration only |
| **Target User** | Aftermarket/DIY | OEM integration |
| **Installation** | Visible display | Concealed module |
| **Victron Integration** | Direct BLE scanning | Via RV-C/CAN bridge |
| **Our Implementation** | ✅ Supported | ❌ Not applicable |

---

## Conclusions

### ✅ **No Action Required**

The 708-RBT is **not relevant** to our BLE-based Victron integration because:

1. **Different Protocol**: Uses RV-C (CAN) for tank data transmission, not BLE
2. **Different Market**: OEM module for manufacturer integration, not end-user product
3. **Different Use Case**: Bluetooth is for configuration/diagnostics, not live monitoring
4. **Different Integration Path**: Would integrate via Victron's CAN interface, not BLE scanning

### 📝 **What This Means**

Our implementation correctly focuses on:
- ✅ **709-BT** - Original Bluetooth tank monitor
- ✅ **709-BTP3** - Updated Bluetooth tank monitor (ASCII format)
- ✅ **709-BTP7** - Latest Bluetooth tank monitor (binary format)

The **708-RBT** is a different product line entirely (RV-C multiplex module), and would require:
- Victron CAN bus integration (not BLE)
- RV-C protocol parsing (not SeeLevel BLE protocol)
- CAN interface on Cerbo GX (different from BLE scanning)

---

## Additional Notes from News Page

### Dealer Program Announcement

**Key Points**:
- Garnet expanding dealer network in North America
- Focus on RV dealers offering aftermarket upgrades
- Emphasis on Bluetooth® and CAN integration features
- Target: replacing "antiquated systems" with digital monitoring

**Relevance**: Confirms that Bluetooth-based products (709 series) are mainstream, and our implementation addresses a real market need.

### Product Ecosystem

From the news page, Garnet's product lines are:

1. **Liquid Transport** (commercial trucks, septic, fuel, etc.)
   - Not relevant to RV applications

2. **Holding Tanks** (RV market)
   - **709-BT/BTP3/BTP7**: Standalone Bluetooth monitors (our focus) ✅
   - **708-RBT**: OEM RV-C module (different protocol) ❌
   - **SeeLeveL Soul series**: Multiplex/CAN integration ❌

---

## Recommendation

### ✅ **Current Implementation Status: COMPLETE**

Our implementation covers all **Bluetooth-based standalone tank monitors**:
- 709-BT (original)
- 709-BTP3 (ASCII format, manufacturer ID 0x0131)
- 709-BTP7 (binary format, manufacturer ID 0x0CC0)

### ❌ **708-RBT: NOT APPLICABLE**

The 708-RBT is a **different product category** (RV-C multiplex module) and:
- Does not use BLE for tank data transmission
- Integrates via CAN bus, not BLE scanning
- Would require completely different Victron integration approach
- Is targeted at OEMs, not aftermarket/DIY users

### 📋 **Documentation Update**

Consider adding a note to the README clarifying:
```markdown
## Supported Devices

This implementation supports Garnet's **standalone Bluetooth tank monitors**:
- SeeLeveL 709-BT (original)
- SeeLeveL 709-BTP3 (updated, ASCII format)
- SeeLeveL 709-BTP7 (latest, binary format)

**Not Supported**:
- SeeLeveL Soul 708-RBT (RV-C multiplex module - uses CAN bus, not BLE for tank data)
```

---

## Future Considerations

If Garnet releases a **new Bluetooth-based standalone monitor** (e.g., 709-BTP8 or similar), we should:

1. **Monitor for**:
   - New manufacturer IDs
   - New packet formats
   - New sensor types

2. **Update if**:
   - Product uses BLE advertisements for tank data
   - Product is marketed to aftermarket/DIY users
   - Product is compatible with SeeLeveL RV 2.0 app

3. **Ignore if**:
   - Product uses CAN/RV-C for tank data
   - Product is OEM-only
   - Product requires physical display connection

---

## Summary

### ✅ **Nothing Missed - Implementation Complete**

The news about the 708-RBT does not affect our implementation because:

1. **Different protocol**: RV-C (CAN) vs BLE
2. **Different market**: OEM vs aftermarket
3. **Different integration**: CAN bus vs BLE scanning
4. **Different Bluetooth use**: Configuration vs continuous monitoring

Our implementation remains **100% complete** for all **Bluetooth-based standalone SeeLevel tank monitors** (709 series).

The 708-RBT would be a **separate project** requiring Victron CAN/RV-C integration, not BLE scanning.

---

**Conclusion**: No code changes, no missing features. Our implementation correctly targets the 709 series Bluetooth monitors. ✅





