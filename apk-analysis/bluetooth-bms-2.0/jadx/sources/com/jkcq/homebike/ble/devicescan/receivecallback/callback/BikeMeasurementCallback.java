package com.jkcq.homebike.ble.devicescan.receivecallback.callback;

import android.bluetooth.BluetoothDevice;
import java.util.List;

/* loaded from: classes.dex */
public interface BikeMeasurementCallback {
    void onHeartRateMeasurementReceived(BluetoothDevice bluetoothDevice, int i, Boolean bool, Integer num, List<Integer> list);
}
