package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.cgm.CGMSessionRunTimeCallback;
import no.nordicsemi.android.ble.common.util.CRC16;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class CGMSessionRunTimeDataCallback extends ProfileReadResponse implements CGMSessionRunTimeCallback {
    @Override // no.nordicsemi.android.ble.common.profile.cgm.CGMSessionRunTimeCallback
    public /* synthetic */ void onContinuousGlucoseMonitorSessionRunTimeReceivedWithCrcError(BluetoothDevice bluetoothDevice, Data data) {
        CGMSessionRunTimeCallback.CC.$default$onContinuousGlucoseMonitorSessionRunTimeReceivedWithCrcError(this, bluetoothDevice, data);
    }

    public CGMSessionRunTimeDataCallback() {
    }

    protected CGMSessionRunTimeDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() != 2 && data.size() != 4) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        int iIntValue = data.getIntValue(18, 0).intValue();
        boolean z = data.size() == 4;
        if (z && CRC16.MCRF4XX(data.getValue(), 0, 2) != data.getIntValue(18, 2).intValue()) {
            onContinuousGlucoseMonitorSessionRunTimeReceivedWithCrcError(bluetoothDevice, data);
        } else {
            onContinuousGlucoseMonitorSessionRunTimeReceived(bluetoothDevice, iIntValue, z);
        }
    }
}
