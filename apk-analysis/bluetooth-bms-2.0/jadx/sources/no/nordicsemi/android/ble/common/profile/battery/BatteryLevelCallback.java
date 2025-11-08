package no.nordicsemi.android.ble.common.profile.battery;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface BatteryLevelCallback {
    void onBatteryLevelChanged(BluetoothDevice bluetoothDevice, int i);
}
