package no.nordicsemi.android.log;

import android.content.Context;
import android.net.Uri;

/* loaded from: classes.dex */
public interface ILogSession {
    Context getContext();

    Uri getSessionContentUri();

    Uri getSessionEntriesUri();

    Uri getSessionUri();
}
