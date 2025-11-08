package com.jkcq.homebike.ble.devicescan.receivecallback.callback;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.hr.HeartRateMeasurementCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class BikeDataMeasurementDataCallback extends ProfileReadResponse implements HeartRateMeasurementCallback {
    public BikeDataMeasurementDataCallback() {
    }

    protected BikeDataMeasurementDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        Integer num;
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() < 2) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        int iIntValue = data.getIntValue(17, 0).intValue();
        int i = (iIntValue & 1) != 0 ? 18 : 17;
        int i2 = (iIntValue & 6) >> 1;
        boolean z = i2 == 2 || i2 == 3;
        boolean z2 = i2 == 3;
        boolean z3 = (iIntValue & 8) != 0;
        boolean z4 = (iIntValue & 16) != 0;
        int i3 = i & 15;
        if (data.size() < i3 + 1 + (z3 ? 2 : 0) + (z4 ? 2 : 0)) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        List<Integer> listUnmodifiableList = null;
        Boolean boolValueOf = z ? Boolean.valueOf(z2) : null;
        int iIntValue2 = data.getIntValue(i, 1).intValue();
        int i4 = 1 + i3;
        if (z3) {
            Integer intValue = data.getIntValue(18, i4);
            i4 += 2;
            num = intValue;
        } else {
            num = null;
        }
        if (z4) {
            int size = (data.size() - i4) / 2;
            ArrayList arrayList = new ArrayList(size);
            for (int i5 = 0; i5 < size; i5++) {
                arrayList.add(data.getIntValue(18, i4));
                i4 += 2;
            }
            listUnmodifiableList = Collections.unmodifiableList(arrayList);
        }
        onHeartRateMeasurementReceived(bluetoothDevice, iIntValue2, boolValueOf, num, listUnmodifiableList);
    }
}
