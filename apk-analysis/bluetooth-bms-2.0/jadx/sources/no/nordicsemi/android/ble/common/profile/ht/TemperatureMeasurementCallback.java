package no.nordicsemi.android.ble.common.profile.ht;

import android.bluetooth.BluetoothDevice;
import java.util.Calendar;

/* loaded from: classes.dex */
public interface TemperatureMeasurementCallback extends HealthThermometerTypes {
    public static final int UNIT_C = 0;
    public static final int UNIT_F = 1;

    /* renamed from: no.nordicsemi.android.ble.common.profile.ht.TemperatureMeasurementCallback$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static float toCelsius(float f, int i) {
            return i == 0 ? f : (f - 32.0f) / 1.8f;
        }

        public static float toFahrenheit(float f, int i) {
            return i == 1 ? f : (f * 1.8f) + 32.0f;
        }
    }

    void onTemperatureMeasurementReceived(BluetoothDevice bluetoothDevice, float f, int i, Calendar calendar, Integer num);
}
