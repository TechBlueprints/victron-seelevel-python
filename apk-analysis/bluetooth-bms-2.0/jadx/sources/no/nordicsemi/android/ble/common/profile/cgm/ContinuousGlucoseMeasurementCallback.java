package no.nordicsemi.android.ble.common.profile.cgm;

import android.bluetooth.BluetoothDevice;
import no.nordicsemi.android.ble.common.profile.cgm.CGMTypes;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public interface ContinuousGlucoseMeasurementCallback extends CGMTypes {

    /* renamed from: no.nordicsemi.android.ble.common.profile.cgm.ContinuousGlucoseMeasurementCallback$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$onContinuousGlucoseMeasurementReceivedWithCrcError(ContinuousGlucoseMeasurementCallback continuousGlucoseMeasurementCallback, BluetoothDevice bluetoothDevice, Data data) {
        }

        public static float toMgPerDecilitre(float f) {
            return f * 18.2f;
        }
    }

    void onContinuousGlucoseMeasurementReceived(BluetoothDevice bluetoothDevice, float f, Float f2, Float f3, CGMTypes.CGMStatus cGMStatus, int i, boolean z);

    void onContinuousGlucoseMeasurementReceivedWithCrcError(BluetoothDevice bluetoothDevice, Data data);
}
