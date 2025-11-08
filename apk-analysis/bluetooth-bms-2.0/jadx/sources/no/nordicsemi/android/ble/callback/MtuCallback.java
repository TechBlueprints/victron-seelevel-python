package no.nordicsemi.android.ble.callback;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface MtuCallback {
    void onMtuChanged(BluetoothDevice bluetoothDevice, int i);
}
