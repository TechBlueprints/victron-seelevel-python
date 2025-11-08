package no.nordicsemi.android.ble.common.profile.glucose;

import android.bluetooth.BluetoothDevice;
import java.util.Calendar;

/* loaded from: classes.dex */
public interface GlucoseMeasurementCallback extends GlucoseTypes {
    public static final int UNIT_kg_L = 0;
    public static final int UNIT_mol_L = 1;

    /* renamed from: no.nordicsemi.android.ble.common.profile.glucose.GlucoseMeasurementCallback$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static float toKgPerL(float f, int i) {
            return i == 0 ? f : (f * 18.2f) / 100.0f;
        }

        public static float toMgPerDecilitre(float f, int i) {
            float f2;
            if (i == 0) {
                f2 = 100000.0f;
            } else {
                f *= 18.2f;
                f2 = 1000.0f;
            }
            return f * f2;
        }

        public static float toMmolPerL(float f, int i) {
            return i == 1 ? f * 1000.0f : (f * 100000.0f) / 18.2f;
        }

        public static float toMolPerL(float f, int i) {
            return i == 1 ? f : (f * 100.0f) / 18.2f;
        }
    }

    void onGlucoseMeasurementReceived(BluetoothDevice bluetoothDevice, int i, Calendar calendar, Float f, Integer num, Integer num2, Integer num3, GlucoseStatus glucoseStatus, boolean z);

    public static class GlucoseStatus {
        public final boolean deviceBatteryLow;
        public final boolean generalDeviceFault;
        public final boolean sampleSizeInsufficient;
        public final boolean sensorMalfunction;
        public final boolean sensorReadInterrupted;
        public final boolean sensorResultHigherThenDeviceCanProcess;
        public final boolean sensorResultLowerThenDeviceCanProcess;
        public final boolean sensorTemperatureTooHigh;
        public final boolean sensorTemperatureTooLow;
        public final boolean stripInsertionError;
        public final boolean stripTypeIncorrect;
        public final boolean timeFault;
        public final int value;

        public GlucoseStatus(int i) {
            this.value = i;
            this.deviceBatteryLow = (i & 1) != 0;
            this.sensorMalfunction = (i & 2) != 0;
            this.sampleSizeInsufficient = (i & 4) != 0;
            this.stripInsertionError = (i & 8) != 0;
            this.stripTypeIncorrect = (i & 16) != 0;
            this.sensorResultLowerThenDeviceCanProcess = (i & 32) != 0;
            this.sensorResultHigherThenDeviceCanProcess = (i & 64) != 0;
            this.sensorTemperatureTooHigh = (i & 128) != 0;
            this.sensorTemperatureTooLow = (i & 256) != 0;
            this.sensorReadInterrupted = (i & 512) != 0;
            this.generalDeviceFault = (i & 1024) != 0;
            this.timeFault = (i & 2048) != 0;
        }
    }
}
