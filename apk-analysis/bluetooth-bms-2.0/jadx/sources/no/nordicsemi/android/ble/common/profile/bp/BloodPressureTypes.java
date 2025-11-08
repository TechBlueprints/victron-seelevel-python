package no.nordicsemi.android.ble.common.profile.bp;

/* loaded from: classes.dex */
public interface BloodPressureTypes {
    public static final int UNIT_kPa = 1;
    public static final int UNIT_mmHg = 0;

    /* renamed from: no.nordicsemi.android.ble.common.profile.bp.BloodPressureTypes$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static float toKPa(float f, int i) {
            return i == 1 ? f : f * 0.13332239f;
        }

        public static float toMmHg(float f, int i) {
            return i == 0 ? f : f / 0.13332239f;
        }
    }

    public static class BPMStatus {
        public final boolean bodyMovementDetected;
        public final boolean cuffTooLose;
        public final boolean improperMeasurementPosition;
        public final boolean irregularPulseDetected;
        public final boolean pulseRateExceedsUpperLimit;
        public final boolean pulseRateInRange;
        public final boolean pulseRateIsLessThenLowerLimit;
        public final int value;

        public BPMStatus(int i) {
            this.value = i;
            this.bodyMovementDetected = (i & 1) != 0;
            this.cuffTooLose = (i & 2) != 0;
            this.irregularPulseDetected = (i & 4) != 0;
            int i2 = (i & 24) >> 3;
            this.pulseRateInRange = i2 == 0;
            this.pulseRateExceedsUpperLimit = i2 == 1;
            this.pulseRateIsLessThenLowerLimit = i2 == 2;
            this.improperMeasurementPosition = (i & 32) != 0;
        }
    }
}
