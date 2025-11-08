package no.nordicsemi.android.ble.scanner;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface ScanResultsCallBack {
    void onBatchScanResults(BluetoothDevice bluetoothDevice, int i, byte[] bArr);

    void onScanFailed(int i);

    void onScanFinished();
}
