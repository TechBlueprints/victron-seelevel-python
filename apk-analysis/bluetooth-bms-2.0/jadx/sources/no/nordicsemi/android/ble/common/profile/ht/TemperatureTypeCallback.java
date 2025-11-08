package no.nordicsemi.android.ble.common.profile.ht;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface TemperatureTypeCallback extends HealthThermometerTypes {
    void onTemperatureTypeReceived(BluetoothDevice bluetoothDevice, int i);
}
