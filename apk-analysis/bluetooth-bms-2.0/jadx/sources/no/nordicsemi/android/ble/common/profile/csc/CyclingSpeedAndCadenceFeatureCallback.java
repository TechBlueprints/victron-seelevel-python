package no.nordicsemi.android.ble.common.profile.csc;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface CyclingSpeedAndCadenceFeatureCallback {
    void onCyclingSpeedAndCadenceFeaturesReceived(BluetoothDevice bluetoothDevice, CSCFeatures cSCFeatures);

    public static class CSCFeatures {
        public final boolean crankRevolutionDataSupported;
        public final boolean multipleSensorDataSupported;
        public final int value;
        public final boolean wheelRevolutionDataSupported;

        public CSCFeatures(int i) {
            this.value = i;
            this.wheelRevolutionDataSupported = (i & 1) != 0;
            this.crankRevolutionDataSupported = (i & 2) != 0;
            this.multipleSensorDataSupported = (i & 4) != 0;
        }
    }
}
