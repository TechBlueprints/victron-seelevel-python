package no.nordicsemi.android.log;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import no.nordicsemi.android.log.LogContract;

/* loaded from: classes.dex */
public class LogSession implements ILogSession {
    private final Context context;
    private final Uri sessionUri;

    LogSession(Context context, Uri uri) {
        this.context = context.getApplicationContext();
        this.sessionUri = uri;
    }

    @Override // no.nordicsemi.android.log.ILogSession
    public Context getContext() {
        return this.context;
    }

    @Override // no.nordicsemi.android.log.ILogSession
    public Uri getSessionUri() {
        return this.sessionUri;
    }

    @Override // no.nordicsemi.android.log.ILogSession
    public Uri getSessionEntriesUri() {
        return this.sessionUri.buildUpon().appendEncodedPath("log").build();
    }

    public Uri getSessionsUri() {
        try {
            Cursor cursorQuery = this.context.getContentResolver().query(this.sessionUri, new String[]{LogContract.SessionColumns.APPLICATION_ID}, null, null, null);
            try {
                if (cursorQuery.moveToNext()) {
                    return LogContract.Session.createSessionsUri(cursorQuery.getLong(0));
                }
                return null;
            } finally {
                cursorQuery.close();
            }
        } catch (Exception unused) {
            return null;
        }
    }

    @Override // no.nordicsemi.android.log.ILogSession
    public Uri getSessionContentUri() {
        return this.sessionUri.buildUpon().appendEncodedPath("log").appendEncodedPath(LogContract.Session.Content.CONTENT).build();
    }

    public String toString() {
        return this.sessionUri.toString();
    }
}
