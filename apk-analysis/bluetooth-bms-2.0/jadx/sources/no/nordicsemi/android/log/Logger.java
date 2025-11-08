package no.nordicsemi.android.log;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import java.util.ArrayList;
import java.util.List;
import no.nordicsemi.android.log.LogContract;

/* loaded from: classes.dex */
public class Logger {
    public static final int MARK_CLEAR = 0;
    public static final int MARK_FLAG_BLUE = 5;
    public static final int MARK_FLAG_RED = 6;
    public static final int MARK_FLAG_YELLOW = 4;
    public static final int MARK_STAR_BLUE = 2;
    public static final int MARK_STAR_RED = 3;
    public static final int MARK_STAR_YELLOW = 1;
    private static final int SESSION_ID = 100;
    private static final int SESSION_ID_LOG = 101;
    private static final int SESSION_KEY_NUMBER = 102;
    private static final int SESSION_KEY_NUMBER_LOG = 103;
    private static final UriMatcher mUriMatcher = new UriMatcher(-1);
    private static final ContentValues values = new ContentValues();

    static {
        UriMatcher uriMatcher = mUriMatcher;
        uriMatcher.addURI("no.nordicsemi.android.log", "session/#", 100);
        uriMatcher.addURI("no.nordicsemi.android.log", "session/#/log", 101);
        uriMatcher.addURI("no.nordicsemi.android.log", "session/key/*/#", 102);
        uriMatcher.addURI("no.nordicsemi.android.log", "session/key/*/#/log", 103);
    }

    public static LogSession newSession(Context context, String str, String str2) {
        return newSession(context, null, str, str2);
    }

    public static LogSession newSession(Context context, String str, String str2, String str3) {
        ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
        ContentProviderOperation.Builder builderNewInsert = ContentProviderOperation.newInsert(LogContract.Application.CONTENT_URI);
        String string = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        if (str != null) {
            builderNewInsert.withValue("application", string + " " + str);
        } else {
            builderNewInsert.withValue("application", string);
        }
        arrayList.add(builderNewInsert.build());
        arrayList.add(ContentProviderOperation.newInsert(LogContract.Session.CONTENT_URI.buildUpon().appendEncodedPath("key").appendEncodedPath(str2).build()).withValueBackReference(LogContract.SessionColumns.APPLICATION_ID, 0).withValue(LogContract.SessionColumns.NAME, str3).build());
        try {
            return new LogSession(context, context.getContentResolver().applyBatch("no.nordicsemi.android.log", arrayList)[1].uri);
        } catch (Exception unused) {
            return null;
        }
    }

    public static ILogSession openSession(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        switch (mUriMatcher.match(uri)) {
            case 100:
            case 102:
                return new LogSession(context, uri);
            case 101:
            case 103:
                Uri.Builder builderBuildUpon = LogContract.Session.CONTENT_URI.buildUpon();
                List<String> pathSegments = uri.getPathSegments();
                for (int i = 1; i < pathSegments.size() - 1; i++) {
                    builderBuildUpon.appendEncodedPath(pathSegments.get(i));
                }
                return new LogSession(context, builderBuildUpon.build());
            default:
                return new LocalLogSession(context, uri);
        }
    }

    public static void setSessionDescription(LogSession logSession, String str) {
        if (logSession == null) {
            return;
        }
        synchronized (values) {
            values.clear();
            values.put(LogContract.SessionColumns.DESCRIPTION, str);
            try {
                logSession.getContext().getContentResolver().update(logSession.getSessionUri(), values, null, null);
            } catch (Exception unused) {
            }
        }
    }

    public static void setSessionMark(LogSession logSession, int i) {
        if (logSession == null) {
            return;
        }
        synchronized (values) {
            values.clear();
            values.put(LogContract.SessionColumns.MARK, Integer.valueOf(i));
            try {
                logSession.getContext().getContentResolver().update(logSession.getSessionUri(), values, null, null);
            } catch (Exception unused) {
            }
        }
    }

    public static void d(ILogSession iLogSession, String str) {
        log(iLogSession, 0, str);
    }

    public static void v(ILogSession iLogSession, String str) {
        log(iLogSession, 1, str);
    }

    public static void i(ILogSession iLogSession, String str) {
        log(iLogSession, 5, str);
    }

    public static void a(ILogSession iLogSession, String str) {
        log(iLogSession, 10, str);
    }

    public static void w(ILogSession iLogSession, String str) {
        log(iLogSession, 15, str);
    }

    public static void e(ILogSession iLogSession, String str) {
        log(iLogSession, 20, str);
    }

    public static void log(ILogSession iLogSession, int i, String str) {
        if (iLogSession == null) {
            return;
        }
        synchronized (values) {
            values.clear();
            values.put(LogContract.LogColumns.LEVEL, Integer.valueOf(i));
            values.put(LogContract.LogColumns.DATA, str);
            try {
                iLogSession.getContext().getContentResolver().insert(iLogSession.getSessionEntriesUri(), values);
            } catch (Exception unused) {
            }
        }
    }

    public static ContentValues logEntry(ILogSession iLogSession, int i, String str) {
        if (iLogSession == null) {
            return null;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(LogContract.LogColumns.TIME, Long.valueOf(System.currentTimeMillis()));
        contentValues.put(LogContract.LogColumns.LEVEL, Integer.valueOf(i));
        contentValues.put(LogContract.LogColumns.DATA, str);
        return contentValues;
    }

    public static void d(ILogSession iLogSession, int i, Object... objArr) {
        log(iLogSession, 0, i, objArr);
    }

    public static void v(ILogSession iLogSession, int i, Object... objArr) {
        log(iLogSession, 1, i, objArr);
    }

    public static void i(ILogSession iLogSession, int i, Object... objArr) {
        log(iLogSession, 5, i, objArr);
    }

    public static void a(ILogSession iLogSession, int i, Object... objArr) {
        log(iLogSession, 10, i, objArr);
    }

    public static void w(ILogSession iLogSession, int i, Object... objArr) {
        log(iLogSession, 15, i, objArr);
    }

    public static void e(ILogSession iLogSession, int i, Object... objArr) {
        log(iLogSession, 20, i, objArr);
    }

    public static void log(ILogSession iLogSession, int i, int i2, Object... objArr) {
        if (iLogSession == null) {
            return;
        }
        values.clear();
        values.put(LogContract.LogColumns.LEVEL, Integer.valueOf(i));
        values.put(LogContract.LogColumns.DATA, iLogSession.getContext().getString(i2, objArr));
        try {
            iLogSession.getContext().getContentResolver().insert(iLogSession.getSessionEntriesUri(), values);
        } catch (Exception unused) {
        }
    }

    public static ContentValues logEntry(ILogSession iLogSession, int i, int i2, Object... objArr) {
        if (iLogSession == null) {
            return null;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(LogContract.LogColumns.TIME, Long.valueOf(System.currentTimeMillis()));
        contentValues.put(LogContract.LogColumns.LEVEL, Integer.valueOf(i));
        contentValues.put(LogContract.LogColumns.DATA, iLogSession.getContext().getString(i2, objArr));
        return contentValues;
    }

    public static void log(ILogSession iLogSession, ContentValues[] contentValuesArr) {
        if (iLogSession == null || contentValuesArr == null || contentValuesArr.length == 0) {
            return;
        }
        try {
            iLogSession.getContext().getContentResolver().bulkInsert(iLogSession.getSessionEntriesUri(), contentValuesArr);
        } catch (Exception unused) {
        }
    }

    public static void log(ILogSession iLogSession, List<ContentValues> list) {
        if (iLogSession == null || list == null || list.isEmpty()) {
            return;
        }
        try {
            iLogSession.getContext().getContentResolver().bulkInsert(iLogSession.getSessionEntriesUri(), (ContentValues[]) list.toArray(new ContentValues[0]));
        } catch (Exception unused) {
        }
    }
}
