package no.nordicsemi.android.log;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import no.nordicsemi.android.log.LogContract;

/* loaded from: classes.dex */
public class LocalLogSession implements ILogSession {
    private final Context context;
    private final Uri sessionUri;

    public static LocalLogSession newSession(Context context, Uri uri, String str, String str2) {
        Uri uriBuild = uri.buildUpon().appendEncodedPath(LogContract.Session.SESSION_CONTENT_DIRECTORY).appendEncodedPath("key").appendEncodedPath(str).build();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LogContract.SessionColumns.NAME, str2);
        try {
            Uri uriInsert = context.getContentResolver().insert(uriBuild, contentValues);
            if (uriInsert != null) {
                return new LocalLogSession(context, uriInsert);
            }
            return null;
        } catch (Exception e) {
            Log.e("LocalLogSession", "Error while creating a local log session.", e);
            return null;
        }
    }

    LocalLogSession(Context context, Uri uri) {
        this.context = context.getApplicationContext();
        this.sessionUri = uri;
    }

    public void delete() {
        try {
            this.context.getContentResolver().delete(this.sessionUri, null, null);
        } catch (Exception e) {
            Log.e("LocalLogSession", "Error while deleting local log session.", e);
        }
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

    @Override // no.nordicsemi.android.log.ILogSession
    public Uri getSessionContentUri() {
        return this.sessionUri.buildUpon().appendEncodedPath("log").appendEncodedPath(LogContract.Session.Content.CONTENT).build();
    }
}
