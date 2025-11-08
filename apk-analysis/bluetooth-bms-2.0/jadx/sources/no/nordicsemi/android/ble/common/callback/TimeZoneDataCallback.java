package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.TimeZoneCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class TimeZoneDataCallback extends ProfileReadResponse implements TimeZoneCallback {
    public TimeZoneDataCallback() {
    }

    protected TimeZoneDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        super.onDataReceived(bluetoothDevice, data);
        Integer timeZone = readTimeZone(data, 0);
        if (timeZone == null) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        if (timeZone.intValue() == -128) {
            onUnknownTimeZoneReceived(bluetoothDevice);
        } else if (timeZone.intValue() < -48 || timeZone.intValue() > 56) {
            onInvalidDataReceived(bluetoothDevice, data);
        } else {
            onTimeZoneReceived(bluetoothDevice, timeZone.intValue() * 15);
        }
    }

    public static Integer readTimeZone(Data data, int i) {
        if (data.size() < i + 1) {
            return null;
        }
        return data.getIntValue(33, i);
    }
}
