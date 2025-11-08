package no.nordicsemi.android.ble.common.profile.cgm;

import android.bluetooth.BluetoothDevice;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public interface CGMSessionRunTimeCallback {

    /* renamed from: no.nordicsemi.android.ble.common.profile.cgm.CGMSessionRunTimeCallback$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$onContinuousGlucoseMonitorSessionRunTimeReceivedWithCrcError(CGMSessionRunTimeCallback cGMSessionRunTimeCallback, BluetoothDevice bluetoothDevice, Data data) {
        }
    }

    void onContinuousGlucoseMonitorSessionRunTimeReceived(BluetoothDevice bluetoothDevice, int i, boolean z);

    void onContinuousGlucoseMonitorSessionRunTimeReceivedWithCrcError(BluetoothDevice bluetoothDevice, Data data);
}
