package no.nordicsemi.android.ble.common.callback.rsc;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.rsc.RunningSpeedAndCadenceMeasurementCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class RunningSpeedAndCadenceMeasurementDataCallback extends ProfileReadResponse implements RunningSpeedAndCadenceMeasurementCallback {
    public RunningSpeedAndCadenceMeasurementDataCallback() {
    }

    protected RunningSpeedAndCadenceMeasurementDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        Integer intValue;
        super.onDataReceived(bluetoothDevice, data);
        int i = 4;
        if (data.size() < 4) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        int iIntValue = data.getIntValue(17, 0).intValue();
        boolean z = (iIntValue & 1) != 0;
        boolean z2 = (iIntValue & 2) != 0;
        boolean z3 = (iIntValue & 4) != 0;
        float fIntValue = data.getIntValue(18, 1).intValue() / 256.0f;
        int iIntValue2 = data.getIntValue(17, 3).intValue();
        if (data.size() < (z ? 2 : 0) + 4 + (z2 ? 4 : 0)) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        if (z) {
            intValue = data.getIntValue(18, 4);
            i = 6;
        } else {
            intValue = null;
        }
        onRSCMeasurementReceived(bluetoothDevice, z3, fIntValue, iIntValue2, intValue, z2 ? data.getLongValue(20, i) : null);
    }
}
