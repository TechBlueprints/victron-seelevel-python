package no.nordicsemi.android.ble.common.profile.sc;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface SensorLocationCallback extends SensorLocationTypes {
    void onSensorLocationReceived(BluetoothDevice bluetoothDevice, int i);
}
