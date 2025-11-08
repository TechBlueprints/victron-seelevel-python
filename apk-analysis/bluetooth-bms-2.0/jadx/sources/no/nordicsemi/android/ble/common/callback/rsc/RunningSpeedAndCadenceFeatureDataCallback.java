package no.nordicsemi.android.ble.common.callback.rsc;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.rsc.RunningSpeedAndCadenceFeatureCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class RunningSpeedAndCadenceFeatureDataCallback extends ProfileReadResponse implements RunningSpeedAndCadenceFeatureCallback {
    public RunningSpeedAndCadenceFeatureDataCallback() {
    }

    protected RunningSpeedAndCadenceFeatureDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() != 2) {
            onInvalidDataReceived(bluetoothDevice, data);
        } else {
            onRunningSpeedAndCadenceFeaturesReceived(bluetoothDevice, new RunningSpeedAndCadenceFeatureCallback.RSCFeatures(data.getIntValue(18, 0).intValue()));
        }
    }
}
