package no.nordicsemi.android.ble.common.profile.glucose;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface GlucoseMeasurementContextCallback {
    public static final int UNIT_mg = 0;
    public static final int UNIT_ml = 1;

    void onGlucoseMeasurementContextReceived(BluetoothDevice bluetoothDevice, int i, Carbohydrate carbohydrate, Float f, Meal meal, Tester tester, Health health, Integer num, Integer num2, Medication medication, Float f2, Integer num3, Float f3);

    public enum Carbohydrate {
        RESERVED(0),
        BREAKFAST(1),
        LUNCH(2),
        DINNER(3),
        SNACK(4),
        DRINK(5),
        SUPPER(6),
        BRUNCH(7);

        public final byte value;

        Carbohydrate(int i) {
            this.value = (byte) i;
        }

        public static Carbohydrate from(int i) {
            switch (i) {
                case 1:
                    return BREAKFAST;
                case 2:
                    return LUNCH;
                case 3:
                    return DINNER;
                case 4:
                    return SNACK;
                case 5:
                    return DRINK;
                case 6:
                    return SUPPER;
                case 7:
                    return BRUNCH;
                default:
                    return RESERVED;
            }
        }
    }

    public enum Meal {
        RESERVED(0),
        PREPRANDIAL(1),
        POSTPRANDIAL(2),
        FASTING(3),
        CASUAL(4),
        BEDTIME(5);

        public final byte value;

        Meal(int i) {
            this.value = (byte) i;
        }

        public static Meal from(int i) {
            if (i == 1) {
                return PREPRANDIAL;
            }
            if (i == 2) {
                return POSTPRANDIAL;
            }
            if (i == 3) {
                return FASTING;
            }
            if (i == 4) {
                return CASUAL;
            }
            if (i == 5) {
                return BEDTIME;
            }
            return RESERVED;
        }
    }

    public enum Tester {
        RESERVED(0),
        SELF(1),
        HEALTH_CARE_PROFESSIONAL(2),
        LAB_TEST(3),
        NOT_AVAILABLE(15);

        public final byte value;

        Tester(int i) {
            this.value = (byte) i;
        }

        public static Tester from(int i) {
            if (i == 1) {
                return SELF;
            }
            if (i == 2) {
                return HEALTH_CARE_PROFESSIONAL;
            }
            if (i == 3) {
                return LAB_TEST;
            }
            if (i == 15) {
                return NOT_AVAILABLE;
            }
            return RESERVED;
        }
    }

    public enum Health {
        RESERVED(0),
        MINOR_HEALTH_ISSUES(1),
        MAJOR_HEALTH_ISSUES(2),
        DURING_MENSES(3),
        UNDER_STRESS(4),
        NO_HEALTH_ISSUES(5),
        NOT_AVAILABLE(15);

        public final byte value;

        Health(int i) {
            this.value = (byte) i;
        }

        public static Health from(int i) {
            if (i == 1) {
                return MINOR_HEALTH_ISSUES;
            }
            if (i == 2) {
                return MAJOR_HEALTH_ISSUES;
            }
            if (i == 3) {
                return DURING_MENSES;
            }
            if (i == 4) {
                return UNDER_STRESS;
            }
            if (i == 5) {
                return NO_HEALTH_ISSUES;
            }
            if (i == 15) {
                return NOT_AVAILABLE;
            }
            return RESERVED;
        }
    }

    public enum Medication {
        RESERVED(0),
        RAPID_ACTING_INSULIN(1),
        SHORT_ACTING_INSULIN(2),
        INTERMEDIATE_ACTING_INSULIN(3),
        LONG_ACTING_INSULIN(4),
        PRE_MIXED_INSULIN(5);

        public final byte value;

        Medication(int i) {
            this.value = (byte) i;
        }

        public static Medication from(int i) {
            if (i == 1) {
                return RAPID_ACTING_INSULIN;
            }
            if (i == 2) {
                return SHORT_ACTING_INSULIN;
            }
            if (i == 3) {
                return INTERMEDIATE_ACTING_INSULIN;
            }
            if (i == 4) {
                return LONG_ACTING_INSULIN;
            }
            if (i == 5) {
                return PRE_MIXED_INSULIN;
            }
            return RESERVED;
        }
    }
}
