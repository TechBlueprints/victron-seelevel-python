package no.nordicsemi.android.ble.common.callback.ht;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.ht.TemperatureTypeCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class TemperatureTypeDataCallback extends ProfileReadResponse implements TemperatureTypeCallback {
    public TemperatureTypeDataCallback() {
    }

    protected TemperatureTypeDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() != 1) {
            onInvalidDataReceived(bluetoothDevice, data);
        } else {
            onTemperatureTypeReceived(bluetoothDevice, data.getIntValue(17, 0).intValue());
        }
    }
}
