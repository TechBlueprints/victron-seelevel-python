package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.cgm.CGMTypes;
import no.nordicsemi.android.ble.common.profile.cgm.ContinuousGlucoseMeasurementCallback;
import no.nordicsemi.android.ble.common.util.CRC16;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class ContinuousGlucoseMeasurementDataCallback extends ProfileReadResponse implements ContinuousGlucoseMeasurementCallback {
    @Override // no.nordicsemi.android.ble.common.profile.cgm.ContinuousGlucoseMeasurementCallback
    public /* synthetic */ void onContinuousGlucoseMeasurementReceivedWithCrcError(BluetoothDevice bluetoothDevice, Data data) {
        ContinuousGlucoseMeasurementCallback.CC.$default$onContinuousGlucoseMeasurementReceivedWithCrcError(this, bluetoothDevice, data);
    }

    public ContinuousGlucoseMeasurementDataCallback() {
    }

    protected ContinuousGlucoseMeasurementDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        int iIntValue;
        int iIntValue2;
        int iIntValue3;
        Float f;
        Float f2;
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() < 1) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        int i = 0;
        while (i < data.size()) {
            int iIntValue4 = data.getIntValue(17, i).intValue();
            if (iIntValue4 < 6 || i + iIntValue4 > data.size()) {
                onInvalidDataReceived(bluetoothDevice, data);
                return;
            }
            int iIntValue5 = data.getIntValue(17, i + 1).intValue();
            boolean z = (iIntValue5 & 1) != 0;
            boolean z2 = (iIntValue5 & 2) != 0;
            int i2 = (iIntValue5 & 32) != 0 ? 1 : 0;
            int i3 = (iIntValue5 & 64) != 0 ? 1 : 0;
            int i4 = (iIntValue5 & 128) != 0 ? 1 : 0;
            int i5 = (z ? 2 : 0) + 6 + (z2 ? 2 : 0) + i2 + i3 + i4;
            if (iIntValue4 != i5 && iIntValue4 != i5 + 2) {
                onInvalidDataReceived(bluetoothDevice, data);
                return;
            }
            boolean z3 = iIntValue4 == i5 + 2;
            if (z3 && data.getIntValue(18, i + i5).intValue() != CRC16.MCRF4XX(data.getValue(), i, i5)) {
                onContinuousGlucoseMeasurementReceivedWithCrcError(bluetoothDevice, data);
                return;
            }
            int i6 = i + 2;
            float fFloatValue = data.getFloatValue(50, i6).floatValue();
            int i7 = i6 + 2;
            int iIntValue6 = data.getIntValue(18, i7).intValue();
            i = i7 + 2;
            if (i2 != 0) {
                iIntValue = data.getIntValue(17, i).intValue();
                i++;
            } else {
                iIntValue = 0;
            }
            if (i3 != 0) {
                iIntValue2 = data.getIntValue(17, i).intValue();
                i++;
            } else {
                iIntValue2 = 0;
            }
            if (i4 != 0) {
                iIntValue3 = data.getIntValue(17, i).intValue();
                i++;
            } else {
                iIntValue3 = 0;
            }
            CGMTypes.CGMStatus cGMStatus = (i2 == 0 && i3 == 0 && i4 == 0) ? null : new CGMTypes.CGMStatus(iIntValue, iIntValue2, iIntValue3);
            if (z) {
                Float floatValue = data.getFloatValue(50, i);
                i += 2;
                f = floatValue;
            } else {
                f = null;
            }
            if (z2) {
                Float floatValue2 = data.getFloatValue(50, i);
                i += 2;
                f2 = floatValue2;
            } else {
                f2 = null;
            }
            if (z3) {
                i += 2;
            }
            onContinuousGlucoseMeasurementReceived(bluetoothDevice, fFloatValue, f, f2, cGMStatus, iIntValue6, z3);
        }
    }
}
