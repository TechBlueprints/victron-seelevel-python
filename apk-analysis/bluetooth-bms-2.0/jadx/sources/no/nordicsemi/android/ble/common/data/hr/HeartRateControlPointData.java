package no.nordicsemi.android.ble.common.data.hr;

import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public final class HeartRateControlPointData {
    private static final byte[] RESET = {1};

    public static Data reset() {
        return new Data(RESET);
    }
}
