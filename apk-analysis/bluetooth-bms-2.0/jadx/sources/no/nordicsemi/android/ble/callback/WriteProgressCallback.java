package no.nordicsemi.android.ble.callback;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface WriteProgressCallback {
    void onPacketSent(BluetoothDevice bluetoothDevice, byte[] bArr, int i);
}
