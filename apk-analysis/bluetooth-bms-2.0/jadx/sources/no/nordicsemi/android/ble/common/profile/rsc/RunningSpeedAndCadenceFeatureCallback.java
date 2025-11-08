package no.nordicsemi.android.ble.common.profile.rsc;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface RunningSpeedAndCadenceFeatureCallback {
    void onRunningSpeedAndCadenceFeaturesReceived(BluetoothDevice bluetoothDevice, RSCFeatures rSCFeatures);

    public static class RSCFeatures {
        public final boolean calibrationProcedureSupported;
        public final boolean instantaneousStrideLengthMeasurementSupported;
        public final boolean multipleSensorLocationsSupported;
        public final boolean totalDistanceMeasurementSupported;
        public final int value;
        public final boolean walkingOrRunningStatusSupported;

        public RSCFeatures(int i) {
            this.value = i;
            this.instantaneousStrideLengthMeasurementSupported = (i & 1) != 0;
            this.totalDistanceMeasurementSupported = (i & 2) != 0;
            this.walkingOrRunningStatusSupported = (i & 4) != 0;
            this.calibrationProcedureSupported = (i & 8) != 0;
            this.multipleSensorLocationsSupported = (i & 16) != 0;
        }
    }
}
