package no.nordicsemi.android.ble.common.callback.csc;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.csc.CyclingSpeedAndCadenceFeatureCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class CyclingSpeedAndCadenceFeatureDataCallback extends ProfileReadResponse implements CyclingSpeedAndCadenceFeatureCallback {
    public CyclingSpeedAndCadenceFeatureDataCallback() {
    }

    protected CyclingSpeedAndCadenceFeatureDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() != 2) {
            onInvalidDataReceived(bluetoothDevice, data);
        } else {
            onCyclingSpeedAndCadenceFeaturesReceived(bluetoothDevice, new CyclingSpeedAndCadenceFeatureCallback.CSCFeatures(data.getIntValue(18, 0).intValue()));
        }
    }
}
