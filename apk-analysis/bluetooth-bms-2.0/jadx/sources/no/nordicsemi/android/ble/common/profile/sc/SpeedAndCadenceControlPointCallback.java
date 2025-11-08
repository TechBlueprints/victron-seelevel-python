package no.nordicsemi.android.ble.common.profile.sc;

import android.bluetooth.BluetoothDevice;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface SpeedAndCadenceControlPointCallback extends SensorLocationTypes {
    public static final int SC_ERROR_INVALID_PARAMETER = 3;
    public static final int SC_ERROR_OPERATION_FAILED = 4;
    public static final int SC_ERROR_OP_CODE_NOT_SUPPORTED = 2;
    public static final int SC_OP_CODE_REQUEST_SUPPORTED_SENSOR_LOCATIONS = 4;
    public static final int SC_OP_CODE_SET_CUMULATIVE_VALUE = 1;
    public static final int SC_OP_CODE_START_SENSOR_CALIBRATION = 2;
    public static final int SC_OP_CODE_UPDATE_SENSOR_LOCATION = 3;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SCErrorCode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SCOpCode {
    }

    void onSCOperationCompleted(BluetoothDevice bluetoothDevice, int i);

    void onSCOperationError(BluetoothDevice bluetoothDevice, int i, int i2);

    void onSupportedSensorLocationsReceived(BluetoothDevice bluetoothDevice, int[] iArr);
}
