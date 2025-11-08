package no.nordicsemi.android.ble.common.profile.bp;

import android.bluetooth.BluetoothDevice;
import java.util.Calendar;
import no.nordicsemi.android.ble.common.profile.bp.BloodPressureTypes;

/* loaded from: classes.dex */
public interface IntermediateCuffPressureCallback extends BloodPressureTypes {
    void onIntermediateCuffPressureReceived(BluetoothDevice bluetoothDevice, float f, int i, Float f2, Integer num, BloodPressureTypes.BPMStatus bPMStatus, Calendar calendar);
}
