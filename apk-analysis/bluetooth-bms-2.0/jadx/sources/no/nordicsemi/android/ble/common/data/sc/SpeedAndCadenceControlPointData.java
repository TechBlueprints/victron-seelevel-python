package no.nordicsemi.android.ble.common.data.sc;

import no.nordicsemi.android.ble.common.profile.sc.SensorLocationTypes;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.data.MutableData;

/* loaded from: classes.dex */
public final class SpeedAndCadenceControlPointData implements SensorLocationTypes {
    private static final byte SC_OP_CODE_REQUEST_SUPPORTED_SENSOR_LOCATIONS = 4;
    private static final byte SC_OP_CODE_SET_CUMULATIVE_VALUE = 1;
    private static final byte SC_OP_CODE_START_SENSOR_CALIBRATION = 2;
    private static final byte SC_OP_CODE_UPDATE_SENSOR_LOCATION = 3;

    public static Data setCumulativeValue(long j) {
        MutableData mutableData = new MutableData(new byte[5]);
        mutableData.setByte(1, 0);
        mutableData.setValue(j, 20, 1);
        return mutableData;
    }

    public static Data startSensorCalibration() {
        return new MutableData(new byte[]{SC_OP_CODE_START_SENSOR_CALIBRATION});
    }

    public static Data updateSensorLocation(int i) {
        MutableData mutableData = new MutableData(new byte[2]);
        mutableData.setByte(3, 0);
        mutableData.setValue(i, 17, 1);
        return mutableData;
    }

    public static Data requestSupportedSensorLocations() {
        return new MutableData(new byte[]{SC_OP_CODE_REQUEST_SUPPORTED_SENSOR_LOCATIONS});
    }
}
