package no.nordicsemi.android.ble.common.profile.alert;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface AlertLevelCallback {
    public static final int ALERT_HIGH = 2;
    public static final int ALERT_MILD = 1;
    public static final int ALERT_NONE = 0;

    void onAlertLevelChanged(BluetoothDevice bluetoothDevice, int i);
}
