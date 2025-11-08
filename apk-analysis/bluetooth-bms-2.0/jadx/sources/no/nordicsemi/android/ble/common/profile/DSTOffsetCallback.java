package no.nordicsemi.android.ble.common.profile;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface DSTOffsetCallback {
    void onDSTOffsetReceived(BluetoothDevice bluetoothDevice, DSTOffset dSTOffset);

    public enum DSTOffset {
        STANDARD_TIME(0),
        HALF_AN_HOUR_DAYLIGHT_TIME(2),
        DAYLIGHT_TIME(4),
        DOUBLE_DAYLIGHT_TIME(8),
        UNKNOWN(255);

        public final int offset;

        DSTOffset(int i) {
            if (i != 255) {
                this.offset = i * 15;
            } else {
                this.offset = 0;
            }
        }

        public static DSTOffset from(int i) {
            if (i == 0) {
                return STANDARD_TIME;
            }
            if (i == 2) {
                return HALF_AN_HOUR_DAYLIGHT_TIME;
            }
            if (i == 4) {
                return DAYLIGHT_TIME;
            }
            if (i == 8) {
                return DOUBLE_DAYLIGHT_TIME;
            }
            if (i != 255) {
                return null;
            }
            return UNKNOWN;
        }
    }
}
