package no.nordicsemi.android.ble.common.callback.sc;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.sc.SensorLocationCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class SensorLocationDataCallback extends ProfileReadResponse implements SensorLocationCallback {
    public SensorLocationDataCallback() {
    }

    protected SensorLocationDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() != 1) {
            onInvalidDataReceived(bluetoothDevice, data);
        } else {
            onSensorLocationReceived(bluetoothDevice, data.getIntValue(17, 0).intValue());
        }
    }
}
