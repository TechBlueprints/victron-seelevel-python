package no.nordicsemi.android.ble.common.profile;

import android.bluetooth.BluetoothDevice;
import java.util.Calendar;

/* loaded from: classes.dex */
public interface DateTimeCallback {
    void onDateTimeReceived(BluetoothDevice bluetoothDevice, Calendar calendar);
}
