package no.nordicsemi.android.ble.exception;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;

/* loaded from: classes.dex */
public final class InvalidDataException extends Exception {
    private final ProfileReadResponse response;

    public InvalidDataException(ProfileReadResponse profileReadResponse) {
        this.response = profileReadResponse;
    }

    public ProfileReadResponse getResponse() {
        return this.response;
    }
}
