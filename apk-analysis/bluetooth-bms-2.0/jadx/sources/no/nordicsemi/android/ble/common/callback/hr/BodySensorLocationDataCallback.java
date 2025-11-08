package no.nordicsemi.android.ble.common.callback.hr;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.hr.BodySensorLocationCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class BodySensorLocationDataCallback extends ProfileReadResponse implements BodySensorLocationCallback {
    public BodySensorLocationDataCallback() {
    }

    protected BodySensorLocationDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() < 1) {
            onInvalidDataReceived(bluetoothDevice, data);
        } else {
            onBodySensorLocationReceived(bluetoothDevice, data.getIntValue(17, 0).intValue());
        }
    }
}
