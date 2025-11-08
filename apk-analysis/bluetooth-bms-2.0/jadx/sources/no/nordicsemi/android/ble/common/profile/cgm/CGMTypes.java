package no.nordicsemi.android.ble.common.profile.cgm;

import no.nordicsemi.android.ble.common.profile.glucose.GlucoseTypes;

/* loaded from: classes.dex */
public interface CGMTypes extends GlucoseTypes {

    public static class CGMFeatures {
        public final boolean calibrationSupported;
        public final boolean cgmQualityInfoSupported;
        public final boolean cgmTrendInfoSupported;
        public final boolean deviceSpecificAlertSupported;
        public final boolean e2eCrcSupported;
        public final boolean generalDeviceFaultSupported;
        public final boolean hyperAlertsSupported;
        public final boolean hypoAlertsSupported;
        public final boolean lowBatteryDetectionSupported;
        public final boolean multipleBondSupported;
        public final boolean multipleSessionsSupported;
        public final boolean patientHighLowAlertsSupported;
        public final boolean rateOfIncreaseDecreaseAlertsSupported;
        public final boolean sensorMalfunctionDetectionSupported;
        public final boolean sensorResultHighLowSupported;
        public final boolean sensorTempHighLowDetectionSupported;
        public final boolean sensorTypeErrorDetectionSupported;
        public final int value;

        public CGMFeatures(int i) {
            this.value = i;
            this.calibrationSupported = (i & 1) != 0;
            this.patientHighLowAlertsSupported = (i & 2) != 0;
            this.hypoAlertsSupported = (i & 4) != 0;
            this.hyperAlertsSupported = (i & 8) != 0;
            this.rateOfIncreaseDecreaseAlertsSupported = (i & 16) != 0;
            this.deviceSpecificAlertSupported = (i & 32) != 0;
            this.sensorMalfunctionDetectionSupported = (i & 64) != 0;
            this.sensorTempHighLowDetectionSupported = (i & 128) != 0;
            this.sensorResultHighLowSupported = (i & 256) != 0;
            this.lowBatteryDetectionSupported = (i & 512) != 0;
            this.sensorTypeErrorDetectionSupported = (i & 1024) != 0;
            this.generalDeviceFaultSupported = (i & 2048) != 0;
            this.e2eCrcSupported = (i & 4096) != 0;
            this.multipleBondSupported = (i & 8192) != 0;
            this.multipleSessionsSupported = (i & 16384) != 0;
            this.cgmTrendInfoSupported = (32768 & i) != 0;
            this.cgmQualityInfoSupported = (i & 65536) != 0;
        }
    }

    public static class CGMCalibrationStatus {
        public final boolean dataOutOfRange;
        public final boolean processPending;
        public final boolean rejected;
        public final int value;

        public CGMCalibrationStatus(int i) {
            this.value = i;
            this.rejected = (i & 1) != 0;
            this.dataOutOfRange = (i & 2) != 0;
            this.processPending = (i & 4) != 0;
        }
    }

    public static class CGMStatus {
        public final boolean calibrationNotAllowed;
        public final boolean calibrationRecommended;
        public final boolean calibrationRequired;
        public final int calibrationTempStatus;
        public final boolean deviceBatteryLow;
        public final boolean deviceSpecificAlert;
        public final boolean generalDeviceFault;
        public final boolean sensorMalfunction;
        public final boolean sensorRateOfDecreaseExceeded;
        public final boolean sensorRateOfIncreaseExceeded;
        public final boolean sensorResultHigherThenDeviceCanProcess;
        public final boolean sensorResultHigherThenHyperLevel;
        public final boolean sensorResultHigherThenPatientHighLevel;
        public final boolean sensorResultLowerThenDeviceCanProcess;
        public final boolean sensorResultLowerThenHypoLevel;
        public final boolean sensorResultLowerThenPatientLowLevel;
        public final int sensorStatus;
        public final boolean sensorTemperatureTooHigh;
        public final boolean sensorTemperatureTooLow;
        public final boolean sensorTypeIncorrectForDevice;
        public final boolean sessionStopped;
        public final boolean timeSyncRequired;
        public final int warningStatus;

        public CGMStatus(int i, int i2, int i3) {
            this.warningStatus = i;
            this.calibrationTempStatus = i2;
            this.sensorStatus = i3;
            this.sessionStopped = (i & 1) != 0;
            this.deviceBatteryLow = (i & 2) != 0;
            this.sensorTypeIncorrectForDevice = (i & 4) != 0;
            this.sensorMalfunction = (i & 8) != 0;
            this.deviceSpecificAlert = (i & 16) != 0;
            this.generalDeviceFault = (i & 32) != 0;
            this.timeSyncRequired = (i2 & 1) != 0;
            this.calibrationNotAllowed = (i2 & 2) != 0;
            this.calibrationRecommended = (i2 & 4) != 0;
            this.calibrationRequired = (i2 & 8) != 0;
            this.sensorTemperatureTooHigh = (i2 & 16) != 0;
            this.sensorTemperatureTooLow = (i2 & 32) != 0;
            this.sensorResultLowerThenPatientLowLevel = (i3 & 1) != 0;
            this.sensorResultHigherThenPatientHighLevel = (i3 & 2) != 0;
            this.sensorResultLowerThenHypoLevel = (i3 & 4) != 0;
            this.sensorResultHigherThenHyperLevel = (i3 & 8) != 0;
            this.sensorRateOfDecreaseExceeded = (i3 & 16) != 0;
            this.sensorRateOfIncreaseExceeded = (i3 & 32) != 0;
            this.sensorResultLowerThenDeviceCanProcess = (i3 & 64) != 0;
            this.sensorResultHigherThenDeviceCanProcess = (i3 & 128) != 0;
        }
    }
}
