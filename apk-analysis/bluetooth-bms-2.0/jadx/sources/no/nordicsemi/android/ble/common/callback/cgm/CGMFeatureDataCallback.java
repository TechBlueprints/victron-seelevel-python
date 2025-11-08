package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.cgm.CGMFeatureCallback;
import no.nordicsemi.android.ble.common.profile.cgm.CGMTypes;
import no.nordicsemi.android.ble.common.util.CRC16;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class CGMFeatureDataCallback extends ProfileReadResponse implements CGMFeatureCallback {
    @Override // no.nordicsemi.android.ble.common.profile.cgm.CGMFeatureCallback
    public /* synthetic */ void onContinuousGlucoseMonitorFeaturesReceivedWithCrcError(BluetoothDevice bluetoothDevice, Data data) {
        CGMFeatureCallback.CC.$default$onContinuousGlucoseMonitorFeaturesReceivedWithCrcError(this, bluetoothDevice, data);
    }

    public CGMFeatureDataCallback() {
    }

    protected CGMFeatureDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() != 6) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        int iIntValue = data.getIntValue(19, 0).intValue();
        int iIntValue2 = data.getIntValue(17, 3).intValue();
        int iIntValue3 = data.getIntValue(18, 4).intValue();
        CGMTypes.CGMFeatures cGMFeatures = new CGMTypes.CGMFeatures(iIntValue);
        if (cGMFeatures.e2eCrcSupported) {
            if (CRC16.MCRF4XX(data.getValue(), 0, 4) != iIntValue3) {
                onContinuousGlucoseMonitorFeaturesReceivedWithCrcError(bluetoothDevice, data);
                return;
            }
        } else if (iIntValue3 != 65535) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        onContinuousGlucoseMonitorFeaturesReceived(bluetoothDevice, cGMFeatures, iIntValue2 & 15, iIntValue2 >> 4, cGMFeatures.e2eCrcSupported);
    }
}
