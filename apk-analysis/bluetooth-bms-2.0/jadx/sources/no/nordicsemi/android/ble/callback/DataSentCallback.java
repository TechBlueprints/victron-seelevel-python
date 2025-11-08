package no.nordicsemi.android.ble.callback;

import android.bluetooth.BluetoothDevice;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public interface DataSentCallback {
    void onDataSent(BluetoothDevice bluetoothDevice, Data data);
}
