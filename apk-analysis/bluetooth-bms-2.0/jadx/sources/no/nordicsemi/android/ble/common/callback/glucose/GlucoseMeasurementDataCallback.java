package no.nordicsemi.android.ble.common.callback.glucose;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import java.util.Calendar;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.callback.DateTimeDataCallback;
import no.nordicsemi.android.ble.common.profile.glucose.GlucoseMeasurementCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class GlucoseMeasurementDataCallback extends ProfileReadResponse implements GlucoseMeasurementCallback {
    public GlucoseMeasurementDataCallback() {
    }

    protected GlucoseMeasurementDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        Float f;
        Integer numValueOf;
        Integer num;
        Integer numValueOf2;
        super.onDataReceived(bluetoothDevice, data);
        int i = 10;
        if (data.size() < 10) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        int iIntValue = data.getIntValue(17, 0).intValue();
        boolean z = (iIntValue & 1) != 0;
        boolean z2 = (iIntValue & 2) != 0;
        int i2 = (iIntValue & 4) != 0 ? 1 : 0;
        boolean z3 = (iIntValue & 8) != 0;
        boolean z4 = (iIntValue & 16) != 0;
        if (data.size() < (z ? 2 : 0) + 10 + (z2 ? 3 : 0) + (z3 ? 2 : 0)) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        int iIntValue2 = data.getIntValue(18, 1).intValue();
        Calendar dateTime = DateTimeDataCallback.readDateTime(data, 3);
        if (dateTime == null) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        if (z) {
            dateTime.add(12, data.getIntValue(34, 10).intValue());
            i = 12;
        }
        if (z2) {
            Float floatValue = data.getFloatValue(50, i);
            int iIntValue3 = data.getIntValue(17, i + 2).intValue();
            i += 3;
            Integer numValueOf3 = Integer.valueOf(iIntValue3 & 15);
            numValueOf2 = Integer.valueOf(iIntValue3 >> 4);
            f = floatValue;
            num = numValueOf3;
            numValueOf = Integer.valueOf(i2);
        } else {
            f = null;
            numValueOf = null;
            num = null;
            numValueOf2 = null;
        }
        onGlucoseMeasurementReceived(bluetoothDevice, iIntValue2, dateTime, f, numValueOf, num, numValueOf2, z3 ? new GlucoseMeasurementCallback.GlucoseStatus(data.getIntValue(18, i).intValue()) : null, z4);
    }
}
