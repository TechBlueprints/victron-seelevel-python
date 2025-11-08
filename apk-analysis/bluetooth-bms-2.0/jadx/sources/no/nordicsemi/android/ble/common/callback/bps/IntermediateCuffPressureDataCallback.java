package no.nordicsemi.android.ble.common.callback.bps;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import java.util.Calendar;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.callback.DateTimeDataCallback;
import no.nordicsemi.android.ble.common.profile.bp.BloodPressureTypes;
import no.nordicsemi.android.ble.common.profile.bp.IntermediateCuffPressureCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class IntermediateCuffPressureDataCallback extends ProfileReadResponse implements IntermediateCuffPressureCallback {
    public IntermediateCuffPressureDataCallback() {
    }

    protected IntermediateCuffPressureDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        Calendar dateTime;
        Float f;
        Integer num;
        super.onDataReceived(bluetoothDevice, data);
        int i = 7;
        if (data.size() < 7) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        int iIntValue = data.getIntValue(17, 0).intValue();
        int i2 = (iIntValue & 1) == 0 ? 0 : 1;
        boolean z = (iIntValue & 2) != 0;
        boolean z2 = (iIntValue & 4) != 0;
        int i3 = (iIntValue & 8) != 0 ? 1 : 0;
        boolean z3 = (iIntValue & 16) != 0;
        if (data.size() < (z ? 7 : 0) + 7 + (z2 ? 2 : 0) + i3 + (z3 ? 2 : 0)) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        float fFloatValue = data.getFloatValue(50, 1).floatValue();
        if (z) {
            dateTime = DateTimeDataCallback.readDateTime(data, 7);
            i = 14;
        } else {
            dateTime = null;
        }
        if (z2) {
            Float floatValue = data.getFloatValue(50, i);
            i += 2;
            f = floatValue;
        } else {
            f = null;
        }
        if (i3 != 0) {
            Integer intValue = data.getIntValue(17, i);
            i++;
            num = intValue;
        } else {
            num = null;
        }
        onIntermediateCuffPressureReceived(bluetoothDevice, fFloatValue, i2, f, num, z3 ? new BloodPressureTypes.BPMStatus(data.getIntValue(18, i).intValue()) : null, dateTime);
    }
}
