package no.nordicsemi.android.log.localprovider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import no.nordicsemi.android.log.LogContract;
import no.nordicsemi.android.log.localprovider.LocalLogDatabaseHelper;

/* loaded from: classes.dex */
public abstract class LocalLogContentProvider extends ContentProvider {
    private static final int BULK_INSERTS_PER_YIELD_POINT = 50;
    private static final String DB_TAG = "local_log_db";
    private static final int MAX_OPERATIONS_PER_YIELD_POINT = 500;
    private static final int SESSION = 1020;
    private static final int SESSION_ID = 1021;
    private static final int SESSION_ID_LOG = 1022;
    private static final int SESSION_ID_LOG_CONTENT = 1023;
    private static final int SESSION_KEY = 1024;
    protected static final int SLEEP_AFTER_YIELD_DELAY = 4000;
    private static final String TAG = "LocalLogContentProvider";
    private LocalLogDatabaseHelper mDatabaseHelper;
    private String mSerializeDbTag;
    private SQLiteOpenHelper mSerializeOnDbHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(-1);
    private static final ProjectionMap sSessionColumns = ProjectionMap.builder().add("_id").add("key").add(LogContract.SessionColumns.NAME).add(LogContract.SessionColumns.CREATED_AT).build();
    private static final ProjectionMap sLogColumns = ProjectionMap.builder().add("_id").add(LogContract.LogColumns.SESSION_ID).add(LogContract.LogColumns.LEVEL).add(LogContract.LogColumns.TIME).add(LogContract.LogColumns.DATA).build();
    private static final ProjectionMap sCountProjectionMap = ProjectionMap.builder().add("_count", "COUNT(*)").build();
    private final ThreadLocal<LogTransaction> mTransactionHolder = new ThreadLocal<>();
    private final ThreadLocal<LocalLogDatabaseHelper> mLocalDatabaseHelper = new ThreadLocal<>();
    private final ContentValues mValues = new ContentValues();
    private final String[] mSelectionArgs1 = new String[1];

    private char getLevelAsChar(int i) {
        if (i == 1) {
            return 'V';
        }
        if (i == 5) {
            return 'I';
        }
        if (i == 10) {
            return 'A';
        }
        if (i != 15) {
            return i != 20 ? 'D' : 'E';
        }
        return 'W';
    }

    protected abstract Uri getAuthorityUri();

    protected LocalLogDatabaseHelper getDatabaseHelper(Context context) {
        return LocalLogDatabaseHelper.getInstance(context);
    }

    public void setDbHelperToSerializeOn(SQLiteOpenHelper sQLiteOpenHelper, String str) {
        this.mSerializeOnDbHelper = sQLiteOpenHelper;
        this.mSerializeDbTag = str;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        try {
            return initialize();
        } catch (RuntimeException e) {
            Log.e(TAG, "Cannot start provider", e);
            return false;
        }
    }

    private boolean initialize() {
        LocalLogDatabaseHelper databaseHelper = getDatabaseHelper(getContext());
        this.mDatabaseHelper = databaseHelper;
        this.mLocalDatabaseHelper.set(databaseHelper);
        setDbHelperToSerializeOn(this.mDatabaseHelper, DB_TAG);
        String authority = getAuthorityUri().getAuthority();
        UriMatcher uriMatcher = sUriMatcher;
        uriMatcher.addURI(authority, LogContract.Session.SESSION_CONTENT_DIRECTORY, 1020);
        uriMatcher.addURI(authority, "session/#", 1021);
        uriMatcher.addURI(authority, "session/#/log", SESSION_ID_LOG);
        uriMatcher.addURI(authority, "session/#/log/content", SESSION_ID_LOG_CONTENT);
        uriMatcher.addURI(authority, "session/key/*", 1024);
        return true;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        int iMatch = sUriMatcher.match(uri);
        if (iMatch == 1021) {
            return LogContract.Session.CONTENT_ITEM_TYPE;
        }
        if (iMatch != SESSION_ID_LOG) {
            return null;
        }
        return LogContract.Log.CONTENT_TYPE;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        String[] strArrAppendSelectionArgs = strArr2;
        this.mLocalDatabaseHelper.set(this.mDatabaseHelper);
        SQLiteDatabase readableDatabase = this.mLocalDatabaseHelper.get().getReadableDatabase();
        SQLiteQueryBuilder sQLiteQueryBuilder = new SQLiteQueryBuilder();
        int iMatch = sUriMatcher.match(uri);
        switch (iMatch) {
            case 1021:
                String lastPathSegment = uri.getLastPathSegment();
                sQLiteQueryBuilder.setTables(LocalLogDatabaseHelper.Tables.LOG_SESSIONS);
                sQLiteQueryBuilder.setProjectionMap(sSessionColumns);
                sQLiteQueryBuilder.appendWhere("_id=?");
                strArrAppendSelectionArgs = appendSelectionArgs(strArrAppendSelectionArgs, lastPathSegment);
                break;
            case SESSION_ID_LOG /* 1022 */:
            case SESSION_ID_LOG_CONTENT /* 1023 */:
                String str3 = uri.getPathSegments().get(1);
                sQLiteQueryBuilder.setTables("log");
                sQLiteQueryBuilder.setProjectionMap(sLogColumns);
                sQLiteQueryBuilder.appendWhere("session_id=?");
                strArrAppendSelectionArgs = appendSelectionArgs(strArrAppendSelectionArgs, str3);
                if (iMatch == SESSION_ID_LOG_CONTENT) {
                    StringBuilder sb = new StringBuilder();
                    String[] strArr3 = {"key", LogContract.SessionColumns.NAME, LogContract.SessionColumns.CREATED_AT};
                    String[] strArr4 = this.mSelectionArgs1;
                    strArr4[0] = str3;
                    Cursor cursorQuery = readableDatabase.query(LocalLogDatabaseHelper.Tables.LOG_SESSIONS, strArr3, "_id=?", strArr4, null, null, null);
                    try {
                        if (cursorQuery.moveToNext()) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(cursorQuery.getLong(2));
                            sb.append(String.format("%s, %tF\n", getContext().getApplicationInfo().loadLabel(getContext().getPackageManager()).toString(), calendar));
                            String string = cursorQuery.getString(1);
                            Object[] objArr = new Object[2];
                            if (string == null) {
                                string = "No name";
                            }
                            objArr[0] = string;
                            objArr[1] = cursorQuery.getString(0);
                            sb.append(String.format("%s (%s)\n", objArr));
                        }
                        cursorQuery.close();
                        Cursor cursorQuery2 = query(uri, readableDatabase, sQLiteQueryBuilder, new String[]{LogContract.LogColumns.TIME, LogContract.LogColumns.LEVEL, LogContract.LogColumns.DATA}, str, strArrAppendSelectionArgs, "time ASC");
                        try {
                            Calendar calendar2 = Calendar.getInstance();
                            while (cursorQuery2.moveToNext()) {
                                sb.append(getLevelAsChar(cursorQuery2.getInt(1)));
                                calendar2.setTimeInMillis(cursorQuery2.getLong(0));
                                sb.append(String.format("\t%1$tR:%1$tS.%1$tL\t%2$s\n", calendar2, cursorQuery2.getString(2)));
                            }
                            cursorQuery2.close();
                            MatrixCursor matrixCursor = new MatrixCursor(new String[]{LogContract.Session.Content.CONTENT});
                            matrixCursor.addRow(new String[]{sb.toString()});
                            return matrixCursor;
                        } catch (Throwable th) {
                            cursorQuery2.close();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        cursorQuery.close();
                        throw th2;
                    }
                }
                break;
        }
        return query(uri, readableDatabase, sQLiteQueryBuilder, strArr, str, strArrAppendSelectionArgs, str2);
    }

    @Override // android.content.ContentProvider
    public int bulkInsert(Uri uri, ContentValues[] contentValuesArr) {
        LogTransaction logTransactionStartTransaction = startTransaction(true);
        int length = contentValuesArr.length;
        try {
            boolean z = false;
            int i = 0;
            for (ContentValues contentValues : contentValuesArr) {
                if (contentValues != null) {
                    if (insertInTransaction(uri, contentValues) != null) {
                        z = true;
                    }
                    i++;
                    if (i >= 50) {
                        try {
                            yield(logTransactionStartTransaction);
                            i = 0;
                        } catch (RuntimeException e) {
                            logTransactionStartTransaction.markYieldFailed();
                            throw e;
                        }
                    } else {
                        continue;
                    }
                }
            }
            if (z) {
                logTransactionStartTransaction.markDirty();
            }
            logTransactionStartTransaction.markSuccessful(true);
            return length;
        } finally {
            endTransaction(uri, true);
        }
    }

    @Override // android.content.ContentProvider
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> arrayList) throws OperationApplicationException {
        LogTransaction logTransactionStartTransaction = startTransaction(true);
        try {
            int size = arrayList.size();
            ContentProviderResult[] contentProviderResultArr = new ContentProviderResult[size];
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < size; i3++) {
                i++;
                if (i >= MAX_OPERATIONS_PER_YIELD_POINT) {
                    throw new OperationApplicationException("Too many content provider operations between yield points. The maximum number of operations per yield point is 500", i2);
                }
                ContentProviderOperation contentProviderOperation = arrayList.get(i3);
                if (i3 > 0 && contentProviderOperation.isYieldAllowed()) {
                    try {
                        if (yield(logTransactionStartTransaction)) {
                            i2++;
                        }
                        i = 0;
                    } catch (RuntimeException e) {
                        logTransactionStartTransaction.markYieldFailed();
                        throw e;
                    }
                }
                contentProviderResultArr[i3] = contentProviderOperation.apply(this, contentProviderResultArr, i3);
            }
            logTransactionStartTransaction.markSuccessful(true);
            return contentProviderResultArr;
        } finally {
            endTransaction(LogContract.Session.CONTENT_URI, true);
        }
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        LogTransaction logTransactionStartTransaction = startTransaction(false);
        try {
            Uri uriInsertInTransaction = insertInTransaction(uri, contentValues);
            if (uriInsertInTransaction != null) {
                logTransactionStartTransaction.markDirty();
            }
            logTransactionStartTransaction.markSuccessful(false);
            return uriInsertInTransaction;
        } finally {
            endTransaction(uri, false);
        }
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        LogTransaction logTransactionStartTransaction = startTransaction(false);
        try {
            int iDeleteInTransaction = deleteInTransaction(uri, str, strArr);
            if (iDeleteInTransaction > 0) {
                logTransactionStartTransaction.markDirty();
            }
            logTransactionStartTransaction.markSuccessful(false);
            return iDeleteInTransaction;
        } finally {
            endTransaction(uri, false);
        }
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        LogTransaction logTransactionStartTransaction = startTransaction(false);
        try {
            int iUpdateInTransaction = updateInTransaction(uri, contentValues, str, strArr);
            if (iUpdateInTransaction > 0) {
                logTransactionStartTransaction.markDirty();
            }
            logTransactionStartTransaction.markSuccessful(false);
            return iUpdateInTransaction;
        } finally {
            endTransaction(uri, false);
        }
    }

    private Cursor query(Uri uri, SQLiteDatabase sQLiteDatabase, SQLiteQueryBuilder sQLiteQueryBuilder, String[] strArr, String str, String[] strArr2, String str2) {
        if (strArr != null && strArr.length == 1 && "_count".equals(strArr[0])) {
            sQLiteQueryBuilder.setProjectionMap(sCountProjectionMap);
        }
        Cursor cursorQuery = sQLiteQueryBuilder.query(sQLiteDatabase, strArr, str, strArr2, null, null, str2);
        if (cursorQuery != null) {
            cursorQuery.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursorQuery;
    }

    protected Uri insertInTransaction(Uri uri, ContentValues contentValues) throws NumberFormatException {
        long jInsertLog;
        this.mLocalDatabaseHelper.set(this.mDatabaseHelper);
        int iMatch = sUriMatcher.match(uri);
        if (iMatch != 1020) {
            if (iMatch == SESSION_ID_LOG) {
                long j = Long.parseLong(uri.getPathSegments().get(1));
                if (!contentValues.containsKey(LogContract.LogColumns.TIME)) {
                    contentValues.put(LogContract.LogColumns.TIME, Long.valueOf(System.currentTimeMillis()));
                }
                contentValues.put(LogContract.LogColumns.SESSION_ID, Long.valueOf(j));
                jInsertLog = insertLog(uri, contentValues);
            } else if (iMatch != 1024) {
                jInsertLog = 0;
            }
            if (jInsertLog < 0) {
                return null;
            }
            return ContentUris.withAppendedId(uri, jInsertLog);
        }
        if (contentValues.getAsString("key") == null) {
            return null;
        }
        contentValues.put(LogContract.SessionColumns.CREATED_AT, Long.valueOf(System.currentTimeMillis()));
        if (insertSession(uri, contentValues) < 0) {
            return null;
        }
        String lastPathSegment = uri.getLastPathSegment();
        long jCurrentTimeMillis = System.currentTimeMillis();
        contentValues.put("key", lastPathSegment);
        contentValues.put(LogContract.SessionColumns.CREATED_AT, Long.valueOf(jCurrentTimeMillis));
        long jInsertSession = insertSession(uri, contentValues);
        if (jInsertSession < 0) {
            return null;
        }
        return ContentUris.withAppendedId(Uri.withAppendedPath(getAuthorityUri(), LogContract.Session.SESSION_CONTENT_DIRECTORY), jInsertSession);
    }

    private long insertSession(Uri uri, ContentValues contentValues) {
        this.mValues.clear();
        this.mValues.putAll(contentValues);
        return this.mLocalDatabaseHelper.get().getWritableDatabase().insert(LocalLogDatabaseHelper.Tables.LOG_SESSIONS, null, this.mValues);
    }

    private long insertLog(Uri uri, ContentValues contentValues) {
        this.mValues.clear();
        this.mValues.putAll(contentValues);
        return this.mLocalDatabaseHelper.get().getWritableDatabase().insert("log", null, this.mValues);
    }

    private int deleteInTransaction(Uri uri, String str, String[] strArr) {
        this.mLocalDatabaseHelper.set(this.mDatabaseHelper);
        int iMatch = sUriMatcher.match(uri);
        if (iMatch == 1020) {
            return deleteSessions();
        }
        if (iMatch != 1021) {
            return 0;
        }
        return deleteSession(ContentUris.parseId(uri));
    }

    private int deleteSessions() {
        SQLiteDatabase writableDatabase = this.mLocalDatabaseHelper.get().getWritableDatabase();
        writableDatabase.delete("log", null, null);
        return writableDatabase.delete(LocalLogDatabaseHelper.Tables.LOG_SESSIONS, null, null);
    }

    private int deleteSession(long j) {
        SQLiteDatabase writableDatabase = this.mLocalDatabaseHelper.get().getWritableDatabase();
        String[] strArr = this.mSelectionArgs1;
        strArr[0] = String.valueOf(j);
        writableDatabase.delete("log", "session_id=?", strArr);
        return writableDatabase.delete(LocalLogDatabaseHelper.Tables.LOG_SESSIONS, "_id=?", strArr);
    }

    private int updateInTransaction(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        throw new UnsupportedOperationException("Updating log is not supported. You can not change the history.");
    }

    private LogTransaction startTransaction(boolean z) {
        LogTransaction logTransaction = this.mTransactionHolder.get();
        if (logTransaction == null) {
            logTransaction = new LogTransaction(z);
            SQLiteOpenHelper sQLiteOpenHelper = this.mSerializeOnDbHelper;
            if (sQLiteOpenHelper != null) {
                logTransaction.startTransactionForDb(sQLiteOpenHelper.getWritableDatabase(), this.mSerializeDbTag);
            }
            this.mTransactionHolder.set(logTransaction);
        }
        return logTransaction;
    }

    private void endTransaction(Uri uri, boolean z) {
        LogTransaction logTransaction = this.mTransactionHolder.get();
        if (logTransaction != null) {
            if (!logTransaction.isBatch() || z) {
                try {
                    if (logTransaction.isDirty()) {
                        notifyChange(Uri.withAppendedPath(getAuthorityUri(), LogContract.Session.SESSION_CONTENT_DIRECTORY));
                    }
                    logTransaction.finish(z);
                } finally {
                    this.mTransactionHolder.set(null);
                }
            }
        }
    }

    protected boolean yield(LogTransaction logTransaction) {
        SQLiteDatabase dbForTag = logTransaction.getDbForTag(DB_TAG);
        return dbForTag != null && dbForTag.yieldIfContendedSafely(4000L);
    }

    protected void notifyChange(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, (ContentObserver) null, false);
    }

    private String[] appendSelectionArgs(String[] strArr, String... strArr2) {
        if (strArr == null) {
            return strArr2;
        }
        String[] strArr3 = new String[strArr.length + strArr2.length];
        System.arraycopy(strArr2, 0, strArr3, 0, strArr2.length);
        System.arraycopy(strArr, 0, strArr3, strArr2.length, strArr.length);
        return strArr3;
    }
}
