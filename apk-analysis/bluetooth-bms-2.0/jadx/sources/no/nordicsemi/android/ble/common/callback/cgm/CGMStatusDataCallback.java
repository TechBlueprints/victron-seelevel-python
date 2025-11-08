package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.cgm.CGMStatusCallback;
import no.nordicsemi.android.ble.common.profile.cgm.CGMTypes;
import no.nordicsemi.android.ble.common.util.CRC16;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class CGMStatusDataCallback extends ProfileReadResponse implements CGMStatusCallback {
    @Override // no.nordicsemi.android.ble.common.profile.cgm.CGMStatusCallback
    public /* synthetic */ void onContinuousGlucoseMonitorStatusReceivedWithCrcError(BluetoothDevice bluetoothDevice, Data data) {
        CGMStatusCallback.CC.$default$onContinuousGlucoseMonitorStatusReceivedWithCrcError(this, bluetoothDevice, data);
    }

    public CGMStatusDataCallback() {
    }

    protected CGMStatusDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() != 5 && data.size() != 7) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        int iIntValue = data.getIntValue(18, 0).intValue();
        int iIntValue2 = data.getIntValue(17, 2).intValue();
        int iIntValue3 = data.getIntValue(17, 3).intValue();
        int iIntValue4 = data.getIntValue(17, 4).intValue();
        boolean z = data.size() == 7;
        if (z && CRC16.MCRF4XX(data.getValue(), 0, 5) != data.getIntValue(18, 5).intValue()) {
            onContinuousGlucoseMonitorStatusReceivedWithCrcError(bluetoothDevice, data);
        } else {
            onContinuousGlucoseMonitorStatusChanged(bluetoothDevice, new CGMTypes.CGMStatus(iIntValue2, iIntValue3, iIntValue4), iIntValue, z);
        }
    }
}
