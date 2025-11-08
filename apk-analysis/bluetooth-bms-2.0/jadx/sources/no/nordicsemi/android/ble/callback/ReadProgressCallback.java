package no.nordicsemi.android.ble.callback;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface ReadProgressCallback {
    void onPacketReceived(BluetoothDevice bluetoothDevice, byte[] bArr, int i);
}
