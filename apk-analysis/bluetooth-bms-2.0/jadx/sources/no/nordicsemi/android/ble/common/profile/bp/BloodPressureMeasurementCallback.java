package no.nordicsemi.android.ble.common.profile.bp;

import android.bluetooth.BluetoothDevice;
import java.util.Calendar;
import no.nordicsemi.android.ble.common.profile.bp.BloodPressureTypes;

/* loaded from: classes.dex */
public interface BloodPressureMeasurementCallback extends BloodPressureTypes {
    void onBloodPressureMeasurementReceived(BluetoothDevice bluetoothDevice, float f, float f2, float f3, int i, Float f4, Integer num, BloodPressureTypes.BPMStatus bPMStatus, Calendar calendar);
}
