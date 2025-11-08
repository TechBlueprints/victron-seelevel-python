package no.nordicsemi.android.ble.common.callback.ht;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import java.util.Calendar;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.callback.DateTimeDataCallback;
import no.nordicsemi.android.ble.common.profile.ht.TemperatureMeasurementCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class TemperatureMeasurementDataCallback extends ProfileReadResponse implements TemperatureMeasurementCallback {
    public TemperatureMeasurementDataCallback() {
    }

    protected TemperatureMeasurementDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        Calendar dateTime;
        super.onDataReceived(bluetoothDevice, data);
        int i = 5;
        if (data.size() < 5) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        int iIntValue = data.getIntValue(17, 0).intValue();
        int i2 = (iIntValue & 1) == 0 ? 0 : 1;
        boolean z = (iIntValue & 2) != 0;
        int i3 = (iIntValue & 4) != 0 ? 1 : 0;
        if (data.size() < (z ? 7 : 0) + 5 + i3) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        float fFloatValue = data.getFloatValue(52, 1).floatValue();
        if (z) {
            dateTime = DateTimeDataCallback.readDateTime(data, 5);
            i = 12;
        } else {
            dateTime = null;
        }
        onTemperatureMeasurementReceived(bluetoothDevice, fFloatValue, i2, dateTime, i3 != 0 ? data.getIntValue(17, i) : null);
    }
}
