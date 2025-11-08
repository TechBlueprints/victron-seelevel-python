package no.nordicsemi.android.ble.common.callback.glucose;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.glucose.GlucoseMeasurementContextCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class GlucoseMeasurementContextDataCallback extends ProfileReadResponse implements GlucoseMeasurementContextCallback {
    public GlucoseMeasurementContextDataCallback() {
    }

    protected GlucoseMeasurementContextDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        GlucoseMeasurementContextCallback.Carbohydrate carbohydrate;
        Float f;
        GlucoseMeasurementContextCallback.Meal meal;
        GlucoseMeasurementContextCallback.Tester tester;
        GlucoseMeasurementContextCallback.Health healthFrom;
        Integer num;
        Integer num2;
        GlucoseMeasurementContextCallback.Medication medication;
        Float f2;
        Integer numValueOf;
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() < 3) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        int iIntValue = data.getIntValue(17, 0).intValue();
        boolean z = (iIntValue & 1) != 0;
        int i = (iIntValue & 2) != 0 ? 1 : 0;
        int i2 = (iIntValue & 4) != 0 ? 1 : 0;
        boolean z2 = (iIntValue & 8) != 0;
        boolean z3 = (iIntValue & 16) != 0;
        int i3 = (iIntValue & 32) != 0 ? 1 : 0;
        boolean z4 = (iIntValue & 64) != 0;
        int i4 = (iIntValue & 128) != 0 ? 1 : 0;
        if (data.size() < (z ? 3 : 0) + 3 + i + i2 + (z2 ? 3 : 0) + (z3 ? 3 : 0) + (z4 ? 2 : 0) + i4) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        int iIntValue2 = data.getIntValue(18, 1).intValue();
        int i5 = i4 != 0 ? 4 : 3;
        if (z) {
            GlucoseMeasurementContextCallback.Carbohydrate carbohydrateFrom = GlucoseMeasurementContextCallback.Carbohydrate.from(data.getIntValue(17, i5).intValue());
            Float floatValue = data.getFloatValue(50, i5 + 1);
            i5 += 3;
            carbohydrate = carbohydrateFrom;
            f = floatValue;
        } else {
            carbohydrate = null;
            f = null;
        }
        if (i != 0) {
            GlucoseMeasurementContextCallback.Meal mealFrom = GlucoseMeasurementContextCallback.Meal.from(data.getIntValue(17, i5).intValue());
            i5++;
            meal = mealFrom;
        } else {
            meal = null;
        }
        if (i2 != 0) {
            int iIntValue3 = data.getIntValue(17, i5).intValue();
            GlucoseMeasurementContextCallback.Tester testerFrom = GlucoseMeasurementContextCallback.Tester.from(iIntValue3 & 15);
            i5++;
            healthFrom = GlucoseMeasurementContextCallback.Health.from(iIntValue3 >> 4);
            tester = testerFrom;
        } else {
            tester = null;
            healthFrom = null;
        }
        if (z2) {
            Integer intValue = data.getIntValue(18, i5);
            Integer intValue2 = data.getIntValue(17, i5 + 2);
            i5 += 3;
            num = intValue;
            num2 = intValue2;
        } else {
            num = null;
            num2 = null;
        }
        if (z3) {
            GlucoseMeasurementContextCallback.Medication medicationFrom = GlucoseMeasurementContextCallback.Medication.from(data.getIntValue(17, i5).intValue());
            Float floatValue2 = data.getFloatValue(50, i5 + 1);
            i5 += 3;
            medication = medicationFrom;
            f2 = floatValue2;
            numValueOf = Integer.valueOf(i3);
        } else {
            medication = null;
            f2 = null;
            numValueOf = null;
        }
        onGlucoseMeasurementContextReceived(bluetoothDevice, iIntValue2, carbohydrate, f, meal, tester, healthFrom, num, num2, medication, f2, numValueOf, z4 ? data.getFloatValue(50, i5) : null);
    }
}
