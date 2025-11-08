package no.nordicsemi.android.ble.common.profile.csc;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface CyclingSpeedAndCadenceMeasurementCallback {
    void onCrankMeasurementReceived(BluetoothDevice bluetoothDevice, int i, int i2);

    void onWheelMeasurementReceived(BluetoothDevice bluetoothDevice, long j, int i);
}
