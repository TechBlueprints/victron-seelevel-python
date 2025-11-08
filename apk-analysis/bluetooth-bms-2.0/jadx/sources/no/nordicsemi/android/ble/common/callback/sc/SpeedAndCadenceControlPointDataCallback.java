package no.nordicsemi.android.ble.common.callback.sc;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.sc.SpeedAndCadenceControlPointCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class SpeedAndCadenceControlPointDataCallback extends ProfileReadResponse implements SpeedAndCadenceControlPointCallback {
    private static final int SC_OP_CODE_RESPONSE_CODE = 16;
    private static final int SC_RESPONSE_SUCCESS = 1;

    public SpeedAndCadenceControlPointDataCallback() {
    }

    protected SpeedAndCadenceControlPointDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() < 3) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        int iIntValue = data.getIntValue(17, 0).intValue();
        int iIntValue2 = data.getIntValue(17, 1).intValue();
        int iIntValue3 = data.getIntValue(17, 2).intValue();
        if (iIntValue != 16) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        if (iIntValue3 != 1) {
            onSCOperationError(bluetoothDevice, iIntValue2, iIntValue3);
            return;
        }
        if (iIntValue2 == 4) {
            int size = data.size() - 3;
            int[] iArr = new int[size];
            for (int i = 0; i < size; i++) {
                iArr[i] = data.getIntValue(17, i + 3).intValue();
            }
            onSupportedSensorLocationsReceived(bluetoothDevice, iArr);
            return;
        }
        onSCOperationCompleted(bluetoothDevice, iIntValue2);
    }
}
