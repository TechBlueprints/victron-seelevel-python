package no.nordicsemi.android.ble.callback;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface ConnectionPriorityCallback {
    void onConnectionUpdated(BluetoothDevice bluetoothDevice, int i, int i2, int i3);
}
