package no.nordicsemi.android.ble.common.callback.battery;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.battery.BatteryLevelCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class BatteryLevelDataCallback extends ProfileReadResponse implements BatteryLevelCallback {
    public BatteryLevelDataCallback() {
    }

    protected BatteryLevelDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        int iIntValue;
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() == 1 && (iIntValue = data.getIntValue(17, 0).intValue()) >= 0 && iIntValue <= 100) {
            onBatteryLevelChanged(bluetoothDevice, iIntValue);
        } else {
            onInvalidDataReceived(bluetoothDevice, data);
        }
    }
}
