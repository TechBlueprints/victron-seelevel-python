package no.nordicsemi.android.log.localprovider;

import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
class LogTransaction {
    private final boolean mBatch;
    private boolean mYieldFailed;
    private final List<SQLiteDatabase> mDatabasesForTransaction = new ArrayList();
    private final Map<String, SQLiteDatabase> mDatabaseTagMap = new HashMap();
    private boolean mIsDirty = false;

    LogTransaction(boolean z) {
        this.mBatch = z;
    }

    boolean isBatch() {
        return this.mBatch;
    }

    boolean isDirty() {
        return this.mIsDirty;
    }

    void markDirty() {
        this.mIsDirty = true;
    }

    void markYieldFailed() {
        this.mYieldFailed = true;
    }

    void startTransactionForDb(SQLiteDatabase sQLiteDatabase, String str) {
        if (hasDbInTransaction(str)) {
            return;
        }
        this.mDatabasesForTransaction.add(0, sQLiteDatabase);
        this.mDatabaseTagMap.put(str, sQLiteDatabase);
        sQLiteDatabase.beginTransaction();
    }

    private boolean hasDbInTransaction(String str) {
        return this.mDatabaseTagMap.containsKey(str);
    }

    SQLiteDatabase getDbForTag(String str) {
        return this.mDatabaseTagMap.get(str);
    }

    public SQLiteDatabase removeDbForTag(String str) {
        SQLiteDatabase sQLiteDatabase = this.mDatabaseTagMap.get(str);
        this.mDatabaseTagMap.remove(str);
        this.mDatabasesForTransaction.remove(sQLiteDatabase);
        return sQLiteDatabase;
    }

    void markSuccessful(boolean z) {
        if (!this.mBatch || z) {
            Iterator<SQLiteDatabase> it = this.mDatabasesForTransaction.iterator();
            while (it.hasNext()) {
                it.next().setTransactionSuccessful();
            }
        }
    }

    void finish(boolean z) {
        if (!this.mBatch || z) {
            for (SQLiteDatabase sQLiteDatabase : this.mDatabasesForTransaction) {
                if (!this.mYieldFailed || sQLiteDatabase.isDbLockedByCurrentThread()) {
                    sQLiteDatabase.endTransaction();
                }
            }
            this.mDatabasesForTransaction.clear();
            this.mDatabaseTagMap.clear();
            this.mIsDirty = false;
        }
    }
}
