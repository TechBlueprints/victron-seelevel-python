package no.nordicsemi.android.ble.common.data.cgm;

import no.nordicsemi.android.ble.common.profile.cgm.CGMTypes;
import no.nordicsemi.android.ble.common.util.CRC16;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.data.MutableData;

/* loaded from: classes.dex */
public final class CGMSpecificOpsControlPointData implements CGMTypes {
    private static final byte OP_CODE_GET_CALIBRATION_VALUE = 5;
    private static final byte OP_CODE_GET_COMMUNICATION_INTERVAL = 2;
    private static final byte OP_CODE_GET_HYPER_ALERT_LEVEL = 17;
    private static final byte OP_CODE_GET_HYPO_ALERT_LEVEL = 14;
    private static final byte OP_CODE_GET_PATIENT_HIGH_ALERT_LEVEL = 8;
    private static final byte OP_CODE_GET_PATIENT_LOW_ALERT_LEVEL = 11;
    private static final byte OP_CODE_GET_RATE_OF_DECREASE_ALERT_LEVEL = 20;
    private static final byte OP_CODE_GET_RATE_OF_INCREASE_ALERT_LEVEL = 23;
    private static final byte OP_CODE_RESET_DEVICE_SPECIFIC_ERROR = 25;
    private static final byte OP_CODE_SET_CALIBRATION_VALUE = 4;
    private static final byte OP_CODE_SET_COMMUNICATION_INTERVAL = 1;
    private static final byte OP_CODE_SET_HYPER_ALERT_LEVEL = 16;
    private static final byte OP_CODE_SET_HYPO_ALERT_LEVEL = 13;
    private static final byte OP_CODE_SET_PATIENT_HIGH_ALERT_LEVEL = 7;
    private static final byte OP_CODE_SET_PATIENT_LOW_ALERT_LEVEL = 10;
    private static final byte OP_CODE_SET_RATE_OF_DECREASE_ALERT_LEVEL = 19;
    private static final byte OP_CODE_SET_RATE_OF_INCREASE_ALERT_LEVEL = 22;
    private static final byte OP_CODE_START_SESSION = 26;
    private static final byte OP_CODE_STOP_SESSION = 27;

    private CGMSpecificOpsControlPointData() {
    }

    public static Data startSession(boolean z) {
        return create(OP_CODE_START_SESSION, z);
    }

    public static Data stopSession(boolean z) {
        return create(OP_CODE_STOP_SESSION, z);
    }

    public static Data resetDeviceSpecificAlert(boolean z) {
        return create(OP_CODE_RESET_DEVICE_SPECIFIC_ERROR, z);
    }

    public static Data setCommunicationInterval(int i, boolean z) {
        return create(OP_CODE_SET_COMMUNICATION_INTERVAL, i, 17, z);
    }

    public static Data setCommunicationIntervalToFastestSupported(boolean z) {
        return create(OP_CODE_SET_COMMUNICATION_INTERVAL, 255, 17, z);
    }

    public static Data disablePeriodicCommunication(boolean z) {
        return create(OP_CODE_SET_COMMUNICATION_INTERVAL, 255, 17, z);
    }

    public static Data getCommunicationInterval(boolean z) {
        return create(OP_CODE_GET_COMMUNICATION_INTERVAL, z);
    }

    public static Data setCalibrationValue(float f, int i, int i2, int i3, int i4, boolean z) {
        MutableData mutableData = new MutableData(new byte[(z ? 2 : 0) + 11]);
        mutableData.setByte(4, 0);
        mutableData.setValue(f, 50, 1);
        mutableData.setValue(i3, 18, 3);
        mutableData.setValue(((i2 & 15) << 8) | (i & 15), 17, 5);
        mutableData.setValue(i4, 18, 6);
        mutableData.setValue(0, 18, 8);
        mutableData.setValue(0, 17, 10);
        return appendCrc(mutableData, z);
    }

    public static Data getCalibrationValue(int i, boolean z) {
        return create(OP_CODE_GET_CALIBRATION_VALUE, i, 18, z);
    }

    public static Data getLastCalibrationValue(boolean z) {
        return create(OP_CODE_GET_CALIBRATION_VALUE, 65535, 18, z);
    }

    public static Data setPatientHighAlertLevel(float f, boolean z) {
        return create(OP_CODE_SET_PATIENT_HIGH_ALERT_LEVEL, f, z);
    }

    public static Data getPatientHighAlertLevel(boolean z) {
        return create(OP_CODE_GET_PATIENT_HIGH_ALERT_LEVEL, z);
    }

    public static Data setPatientLowAlertLevel(float f, boolean z) {
        return create(OP_CODE_SET_PATIENT_LOW_ALERT_LEVEL, f, z);
    }

    public static Data getPatientLowAlertLevel(boolean z) {
        return create(OP_CODE_GET_PATIENT_LOW_ALERT_LEVEL, z);
    }

    public static Data setHypoAlertLevel(float f, boolean z) {
        return create(OP_CODE_SET_HYPO_ALERT_LEVEL, f, z);
    }

    public static Data getHypoAlertLevel(boolean z) {
        return create(OP_CODE_GET_HYPO_ALERT_LEVEL, z);
    }

    public static Data setHyperAlertLevel(float f, boolean z) {
        return create(OP_CODE_SET_HYPER_ALERT_LEVEL, f, z);
    }

    public static Data getHyperAlertLevel(boolean z) {
        return create(OP_CODE_GET_HYPER_ALERT_LEVEL, z);
    }

    public static Data setRateOfDecreaseAlertLevel(float f, boolean z) {
        return create(OP_CODE_SET_RATE_OF_DECREASE_ALERT_LEVEL, f, z);
    }

    public static Data getRateOfDecreaseAlertLevel(boolean z) {
        return create(OP_CODE_GET_RATE_OF_DECREASE_ALERT_LEVEL, z);
    }

    public static Data setRateOfIncreaseAlertLevel(float f, boolean z) {
        return create(OP_CODE_SET_RATE_OF_INCREASE_ALERT_LEVEL, f, z);
    }

    public static Data getRateOfIncreaseAlertLevel(boolean z) {
        return create(OP_CODE_GET_RATE_OF_INCREASE_ALERT_LEVEL, z);
    }

    private static Data create(byte b, boolean z) {
        MutableData mutableData = new MutableData(new byte[(z ? 2 : 0) + 1]);
        mutableData.setByte(b, 0);
        return appendCrc(mutableData, z);
    }

    private static Data create(byte b, int i, int i2, boolean z) {
        MutableData mutableData = new MutableData(new byte[(i2 & 15) + 1 + (z ? 2 : 0)]);
        mutableData.setByte(b, 0);
        mutableData.setValue(i, i2, 1);
        return appendCrc(mutableData, z);
    }

    private static Data create(byte b, float f, boolean z) {
        MutableData mutableData = new MutableData(new byte[(z ? 2 : 0) + 3]);
        mutableData.setByte(b, 0);
        mutableData.setValue(f, 50, 1);
        return appendCrc(mutableData, z);
    }

    private static Data appendCrc(MutableData mutableData, boolean z) {
        if (z) {
            int size = mutableData.size() - 2;
            mutableData.setValue(CRC16.MCRF4XX(mutableData.getValue(), 0, size), 18, size);
        }
        return mutableData;
    }
}
