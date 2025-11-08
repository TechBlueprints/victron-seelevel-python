package no.nordicsemi.android.ble.common.callback.ht;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.ht.MeasurementIntervalCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class MeasurementIntervalDataCallback extends ProfileReadResponse implements MeasurementIntervalCallback {
    public MeasurementIntervalDataCallback() {
    }

    protected MeasurementIntervalDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() != 2) {
            onInvalidDataReceived(bluetoothDevice, data);
        } else {
            onMeasurementIntervalReceived(bluetoothDevice, data.getIntValue(18, 0).intValue());
        }
    }
}
