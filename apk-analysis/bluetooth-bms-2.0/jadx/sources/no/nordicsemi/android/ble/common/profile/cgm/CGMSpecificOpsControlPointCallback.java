package no.nordicsemi.android.ble.common.profile.cgm;

import android.bluetooth.BluetoothDevice;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import no.nordicsemi.android.ble.common.profile.cgm.CGMTypes;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public interface CGMSpecificOpsControlPointCallback extends CGMTypes {
    public static final int CGM_ERROR_INVALID_OPERAND = 3;
    public static final int CGM_ERROR_OP_CODE_NOT_SUPPORTED = 2;
    public static final int CGM_ERROR_PARAMETER_OUT_OF_RANGE = 5;
    public static final int CGM_ERROR_PROCEDURE_NOT_COMPLETED = 4;
    public static final int CGM_OP_CODE_RESET_DEVICE_SPECIFIC_ERROR = 25;
    public static final int CGM_OP_CODE_SET_CALIBRATION_VALUE = 4;
    public static final int CGM_OP_CODE_SET_COMMUNICATION_INTERVAL = 1;
    public static final int CGM_OP_CODE_SET_HYPER_ALERT_LEVEL = 16;
    public static final int CGM_OP_CODE_SET_HYPO_ALERT_LEVEL = 13;
    public static final int CGM_OP_CODE_SET_PATIENT_HIGH_ALERT_LEVEL = 7;
    public static final int CGM_OP_CODE_SET_PATIENT_LOW_ALERT_LEVEL = 10;
    public static final int CGM_OP_CODE_SET_RATE_OF_DECREASE_ALERT_LEVEL = 19;
    public static final int CGM_OP_CODE_SET_RATE_OF_INCREASE_ALERT_LEVEL = 22;
    public static final int CGM_OP_CODE_START_SESSION = 26;
    public static final int CGM_OP_CODE_STOP_SESSION = 27;

    /* renamed from: no.nordicsemi.android.ble.common.profile.cgm.CGMSpecificOpsControlPointCallback$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$onCGMSpecificOpsResponseReceivedWithCrcError(CGMSpecificOpsControlPointCallback cGMSpecificOpsControlPointCallback, BluetoothDevice bluetoothDevice, Data data) {
        }

        public static void $default$onContinuousGlucoseCalibrationValueReceived(CGMSpecificOpsControlPointCallback cGMSpecificOpsControlPointCallback, BluetoothDevice bluetoothDevice, float f, int i, int i2, int i3, int i4, int i5, CGMTypes.CGMCalibrationStatus cGMCalibrationStatus, boolean z) {
        }

        public static void $default$onContinuousGlucoseCommunicationIntervalReceived(CGMSpecificOpsControlPointCallback cGMSpecificOpsControlPointCallback, BluetoothDevice bluetoothDevice, int i, boolean z) {
        }

        public static void $default$onContinuousGlucoseHyperAlertReceived(CGMSpecificOpsControlPointCallback cGMSpecificOpsControlPointCallback, BluetoothDevice bluetoothDevice, float f, boolean z) {
        }

        public static void $default$onContinuousGlucoseHypoAlertReceived(CGMSpecificOpsControlPointCallback cGMSpecificOpsControlPointCallback, BluetoothDevice bluetoothDevice, float f, boolean z) {
        }

        public static void $default$onContinuousGlucosePatientHighAlertReceived(CGMSpecificOpsControlPointCallback cGMSpecificOpsControlPointCallback, BluetoothDevice bluetoothDevice, float f, boolean z) {
        }

        public static void $default$onContinuousGlucosePatientLowAlertReceived(CGMSpecificOpsControlPointCallback cGMSpecificOpsControlPointCallback, BluetoothDevice bluetoothDevice, float f, boolean z) {
        }

        public static void $default$onContinuousGlucoseRateOfDecreaseAlertReceived(CGMSpecificOpsControlPointCallback cGMSpecificOpsControlPointCallback, BluetoothDevice bluetoothDevice, float f, boolean z) {
        }

        public static void $default$onContinuousGlucoseRateOfIncreaseAlertReceived(CGMSpecificOpsControlPointCallback cGMSpecificOpsControlPointCallback, BluetoothDevice bluetoothDevice, float f, boolean z) {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CGMErrorCode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CGMOpCode {
    }

    void onCGMSpecificOpsOperationCompleted(BluetoothDevice bluetoothDevice, int i, boolean z);

    void onCGMSpecificOpsOperationError(BluetoothDevice bluetoothDevice, int i, int i2, boolean z);

    void onCGMSpecificOpsResponseReceivedWithCrcError(BluetoothDevice bluetoothDevice, Data data);

    void onContinuousGlucoseCalibrationValueReceived(BluetoothDevice bluetoothDevice, float f, int i, int i2, int i3, int i4, int i5, CGMTypes.CGMCalibrationStatus cGMCalibrationStatus, boolean z);

    void onContinuousGlucoseCommunicationIntervalReceived(BluetoothDevice bluetoothDevice, int i, boolean z);

    void onContinuousGlucoseHyperAlertReceived(BluetoothDevice bluetoothDevice, float f, boolean z);

    void onContinuousGlucoseHypoAlertReceived(BluetoothDevice bluetoothDevice, float f, boolean z);

    void onContinuousGlucosePatientHighAlertReceived(BluetoothDevice bluetoothDevice, float f, boolean z);

    void onContinuousGlucosePatientLowAlertReceived(BluetoothDevice bluetoothDevice, float f, boolean z);

    void onContinuousGlucoseRateOfDecreaseAlertReceived(BluetoothDevice bluetoothDevice, float f, boolean z);

    void onContinuousGlucoseRateOfIncreaseAlertReceived(BluetoothDevice bluetoothDevice, float f, boolean z);
}
