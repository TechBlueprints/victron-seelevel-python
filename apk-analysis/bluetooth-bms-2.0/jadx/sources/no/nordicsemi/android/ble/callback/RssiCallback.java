package no.nordicsemi.android.ble.callback;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface RssiCallback {
    void onRssiRead(BluetoothDevice bluetoothDevice, int i);
}
