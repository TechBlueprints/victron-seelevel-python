package no.nordicsemi.android.ble.common.profile.glucose;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface GlucoseFeatureCallback {
    void onGlucoseFeaturesReceived(BluetoothDevice bluetoothDevice, GlucoseFeatures glucoseFeatures);

    public static class GlucoseFeatures {
        public final boolean generalDeviceFaultSupported;
        public final boolean lowBatteryDetectionSupported;
        public final boolean multipleBondSupported;
        public final boolean sensorMalfunctionDetectionSupported;
        public final boolean sensorReadInterruptDetectionSupported;
        public final boolean sensorResultHighLowSupported;
        public final boolean sensorSampleSizeSupported;
        public final boolean sensorStripInsertionErrorDetectionSupported;
        public final boolean sensorStripTypeErrorDetectionSupported;
        public final boolean sensorTempHighLowDetectionSupported;
        public final boolean timeFaultSupported;
        public final int value;

        public GlucoseFeatures(int i) {
            this.value = i;
            this.lowBatteryDetectionSupported = (i & 1) != 0;
            this.sensorMalfunctionDetectionSupported = (i & 2) != 0;
            this.sensorSampleSizeSupported = (i & 4) != 0;
            this.sensorStripInsertionErrorDetectionSupported = (i & 8) != 0;
            this.sensorStripTypeErrorDetectionSupported = (i & 16) != 0;
            this.sensorResultHighLowSupported = (i & 32) != 0;
            this.sensorTempHighLowDetectionSupported = (i & 64) != 0;
            this.sensorReadInterruptDetectionSupported = (i & 128) != 0;
            this.generalDeviceFaultSupported = (i & 256) != 0;
            this.timeFaultSupported = (i & 512) != 0;
            this.multipleBondSupported = (i & 1024) != 0;
        }
    }
}
