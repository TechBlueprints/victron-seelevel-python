package no.nordicsemi.android.ble.common.profile.rsc;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface RunningSpeedAndCadenceMeasurementCallback {
    void onRSCMeasurementReceived(BluetoothDevice bluetoothDevice, boolean z, float f, int i, Integer num, Long l);
}
