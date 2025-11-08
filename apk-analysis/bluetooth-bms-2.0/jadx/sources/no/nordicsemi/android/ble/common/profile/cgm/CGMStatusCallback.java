package no.nordicsemi.android.ble.common.profile.cgm;

import android.bluetooth.BluetoothDevice;
import no.nordicsemi.android.ble.common.profile.cgm.CGMTypes;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public interface CGMStatusCallback extends CGMTypes {

    /* renamed from: no.nordicsemi.android.ble.common.profile.cgm.CGMStatusCallback$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$onContinuousGlucoseMonitorStatusReceivedWithCrcError(CGMStatusCallback cGMStatusCallback, BluetoothDevice bluetoothDevice, Data data) {
        }
    }

    void onContinuousGlucoseMonitorStatusChanged(BluetoothDevice bluetoothDevice, CGMTypes.CGMStatus cGMStatus, int i, boolean z);

    void onContinuousGlucoseMonitorStatusReceivedWithCrcError(BluetoothDevice bluetoothDevice, Data data);
}
