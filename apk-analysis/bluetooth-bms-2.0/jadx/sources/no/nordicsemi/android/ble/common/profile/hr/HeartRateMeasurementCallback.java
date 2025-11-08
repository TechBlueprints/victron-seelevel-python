package no.nordicsemi.android.ble.common.profile.hr;

import android.bluetooth.BluetoothDevice;
import java.util.List;

/* loaded from: classes.dex */
public interface HeartRateMeasurementCallback {
    void onHeartRateMeasurementReceived(BluetoothDevice bluetoothDevice, int i, Boolean bool, Integer num, List<Integer> list);
}
