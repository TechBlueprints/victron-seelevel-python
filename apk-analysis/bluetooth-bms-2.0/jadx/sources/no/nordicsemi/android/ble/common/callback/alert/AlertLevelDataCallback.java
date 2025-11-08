package no.nordicsemi.android.ble.common.callback.alert;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.alert.AlertLevelCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class AlertLevelDataCallback extends ProfileReadResponse implements AlertLevelCallback {
    public AlertLevelDataCallback() {
    }

    protected AlertLevelDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        Integer intValue;
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() == 1 && (intValue = data.getIntValue(17, 0)) != null && intValue.intValue() <= 2) {
            onAlertLevelChanged(bluetoothDevice, intValue.intValue());
        } else {
            onInvalidDataReceived(bluetoothDevice, data);
        }
    }
}
