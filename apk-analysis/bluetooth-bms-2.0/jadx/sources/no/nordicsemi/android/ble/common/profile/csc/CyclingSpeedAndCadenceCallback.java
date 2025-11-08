package no.nordicsemi.android.ble.common.profile.csc;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface CyclingSpeedAndCadenceCallback {
    public static final float WHEEL_CIRCUMFERENCE_DEFAULT = 2340.0f;

    /* renamed from: no.nordicsemi.android.ble.common.profile.csc.CyclingSpeedAndCadenceCallback$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static float $default$getWheelCircumference(CyclingSpeedAndCadenceCallback cyclingSpeedAndCadenceCallback) {
            return 2340.0f;
        }
    }

    float getWheelCircumference();

    void onCrankDataChanged(BluetoothDevice bluetoothDevice, float f, float f2);

    void onDistanceChanged(BluetoothDevice bluetoothDevice, float f, float f2, float f3);
}
