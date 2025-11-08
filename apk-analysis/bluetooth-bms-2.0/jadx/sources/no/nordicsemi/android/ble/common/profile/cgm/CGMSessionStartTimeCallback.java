package no.nordicsemi.android.ble.common.profile.cgm;

import android.bluetooth.BluetoothDevice;
import java.util.Calendar;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public interface CGMSessionStartTimeCallback {

    /* renamed from: no.nordicsemi.android.ble.common.profile.cgm.CGMSessionStartTimeCallback$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$onContinuousGlucoseMonitorSessionStartTimeReceivedWithCrcError(CGMSessionStartTimeCallback cGMSessionStartTimeCallback, BluetoothDevice bluetoothDevice, Data data) {
        }
    }

    void onContinuousGlucoseMonitorSessionStartTimeReceived(BluetoothDevice bluetoothDevice, Calendar calendar, boolean z);

    void onContinuousGlucoseMonitorSessionStartTimeReceivedWithCrcError(BluetoothDevice bluetoothDevice, Data data);
}
