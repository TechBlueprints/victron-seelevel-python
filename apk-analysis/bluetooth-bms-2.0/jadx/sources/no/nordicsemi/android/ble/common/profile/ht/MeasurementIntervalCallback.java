package no.nordicsemi.android.ble.common.profile.ht;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface MeasurementIntervalCallback {
    void onMeasurementIntervalReceived(BluetoothDevice bluetoothDevice, int i);
}
