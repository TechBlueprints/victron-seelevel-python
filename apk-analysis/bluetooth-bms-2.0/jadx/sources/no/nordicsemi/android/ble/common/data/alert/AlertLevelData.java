package no.nordicsemi.android.ble.common.data.alert;

import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public final class AlertLevelData {
    private static final byte[] HIGH_ALERT = {2};
    private static final byte[] MILD_ALERT = {1};
    private static final byte[] NO_ALERT = {0};

    public static Data noAlert() {
        return new Data(NO_ALERT);
    }

    public static Data mildAlert() {
        return new Data(MILD_ALERT);
    }

    public static Data highAlert() {
        return new Data(HIGH_ALERT);
    }
}
