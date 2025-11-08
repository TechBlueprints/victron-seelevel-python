package no.nordicsemi.android.ble.common.callback.glucose;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.glucose.GlucoseFeatureCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class GlucoseFeatureDataCallback extends ProfileReadResponse implements GlucoseFeatureCallback {
    public GlucoseFeatureDataCallback() {
    }

    protected GlucoseFeatureDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() != 2) {
            onInvalidDataReceived(bluetoothDevice, data);
        } else {
            onGlucoseFeaturesReceived(bluetoothDevice, new GlucoseFeatureCallback.GlucoseFeatures(data.getIntValue(18, 0).intValue()));
        }
    }
}
