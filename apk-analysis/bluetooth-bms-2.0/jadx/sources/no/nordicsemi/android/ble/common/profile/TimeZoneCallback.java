package no.nordicsemi.android.ble.common.profile;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface TimeZoneCallback {
    void onTimeZoneReceived(BluetoothDevice bluetoothDevice, int i);

    void onUnknownTimeZoneReceived(BluetoothDevice bluetoothDevice);
}
