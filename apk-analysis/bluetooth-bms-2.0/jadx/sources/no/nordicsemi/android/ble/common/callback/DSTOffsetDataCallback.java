package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.DSTOffsetCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class DSTOffsetDataCallback extends ProfileReadResponse implements DSTOffsetCallback {
    public DSTOffsetDataCallback() {
    }

    protected DSTOffsetDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        super.onDataReceived(bluetoothDevice, data);
        DSTOffsetCallback.DSTOffset dSTOffset = readDSTOffset(data, 0);
        if (dSTOffset == null) {
            onInvalidDataReceived(bluetoothDevice, data);
        } else {
            onDSTOffsetReceived(bluetoothDevice, dSTOffset);
        }
    }

    public static DSTOffsetCallback.DSTOffset readDSTOffset(Data data, int i) {
        if (data.size() < i + 1) {
            return null;
        }
        return DSTOffsetCallback.DSTOffset.from(data.getIntValue(17, i).intValue());
    }
}
