package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import java.util.Calendar;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.DateTimeCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class DateTimeDataCallback extends ProfileReadResponse implements DateTimeCallback {
    public DateTimeDataCallback() {
    }

    protected DateTimeDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        super.onDataReceived(bluetoothDevice, data);
        Calendar dateTime = readDateTime(data, 0);
        if (dateTime == null) {
            onInvalidDataReceived(bluetoothDevice, data);
        } else {
            onDateTimeReceived(bluetoothDevice, dateTime);
        }
    }

    public static Calendar readDateTime(Data data, int i) {
        if (data.size() < i + 7) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        int iIntValue = data.getIntValue(18, i).intValue();
        int iIntValue2 = data.getIntValue(17, i + 2).intValue();
        int iIntValue3 = data.getIntValue(17, i + 3).intValue();
        if (iIntValue > 0) {
            calendar.set(1, iIntValue);
        } else {
            calendar.clear(1);
        }
        if (iIntValue2 > 0) {
            calendar.set(2, iIntValue2 - 1);
        } else {
            calendar.clear(2);
        }
        if (iIntValue3 > 0) {
            calendar.set(5, iIntValue3);
        } else {
            calendar.clear(5);
        }
        calendar.set(11, data.getIntValue(17, i + 4).intValue());
        calendar.set(12, data.getIntValue(17, i + 5).intValue());
        calendar.set(13, data.getIntValue(17, i + 6).intValue());
        calendar.set(14, 0);
        return calendar;
    }
}
