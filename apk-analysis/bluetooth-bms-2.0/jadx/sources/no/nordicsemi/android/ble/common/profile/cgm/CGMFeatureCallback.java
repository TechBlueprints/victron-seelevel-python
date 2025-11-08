package no.nordicsemi.android.ble.common.profile.cgm;

import android.bluetooth.BluetoothDevice;
import no.nordicsemi.android.ble.common.profile.cgm.CGMTypes;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public interface CGMFeatureCallback extends CGMTypes {

    /* renamed from: no.nordicsemi.android.ble.common.profile.cgm.CGMFeatureCallback$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$onContinuousGlucoseMonitorFeaturesReceivedWithCrcError(CGMFeatureCallback cGMFeatureCallback, BluetoothDevice bluetoothDevice, Data data) {
        }
    }

    void onContinuousGlucoseMonitorFeaturesReceived(BluetoothDevice bluetoothDevice, CGMTypes.CGMFeatures cGMFeatures, int i, int i2, boolean z);

    void onContinuousGlucoseMonitorFeaturesReceivedWithCrcError(BluetoothDevice bluetoothDevice, Data data);
}
