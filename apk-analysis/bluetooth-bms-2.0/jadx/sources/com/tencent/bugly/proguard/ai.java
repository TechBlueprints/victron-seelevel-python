package com.tencent.bugly.proguard;

import android.content.Context;
import android.os.Process;
import android.support.v4.media.session.PlaybackStateCompat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class ai {
    private static ai b;
    public ah a;
    private final Context d;
    private long f;
    private long g;
    private Map<Integer, Long> e = new HashMap();
    private LinkedBlockingQueue<Runnable> h = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Runnable> i = new LinkedBlockingQueue<>();
    private final Object j = new Object();
    private long k = 0;
    private int l = 0;
    private final w c = w.a();

    static /* synthetic */ int b(ai aiVar) {
        int i = aiVar.l - 1;
        aiVar.l = i;
        return i;
    }

    private ai(Context context) {
        this.d = context;
    }

    public static synchronized ai a(Context context) {
        if (b == null) {
            b = new ai(context);
        }
        return b;
    }

    public static synchronized ai a() {
        return b;
    }

    public final void a(int i, bq bqVar, String str, String str2, ah ahVar, long j, boolean z) {
        try {
            try {
                a(new aj(this.d, i, bqVar.g, ae.a((Object) bqVar), str, str2, ahVar, z), true, true, j);
            } catch (Throwable th) {
                th = th;
                if (al.a(th)) {
                    return;
                }
                th.printStackTrace();
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private void a(int i, int i2, byte[] bArr, String str, String str2, ah ahVar, boolean z) {
        try {
            try {
                a(new aj(this.d, i, i2, bArr, str, str2, ahVar, 0, 0, false), z, false, 0L);
            } catch (Throwable th) {
                th = th;
                if (al.a(th)) {
                    return;
                }
                th.printStackTrace();
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public final void a(int i, bq bqVar, String str, String str2, ah ahVar, boolean z) {
        a(i, bqVar.g, ae.a((Object) bqVar), str, str2, ahVar, z);
    }

    public final long a(boolean z) {
        long jD;
        long jB = ap.b();
        int i = z ? 5 : 3;
        List<y> listA = this.c.a(i);
        if (listA != null && listA.size() > 0) {
            jD = 0;
            try {
                y yVar = listA.get(0);
                if (yVar.e >= jB) {
                    jD = ap.d(yVar.g);
                    if (i == 3) {
                        this.f = jD;
                    } else {
                        this.g = jD;
                    }
                    listA.remove(yVar);
                }
            } catch (Throwable th) {
                al.a(th);
            }
            if (listA.size() > 0) {
                this.c.a(listA);
            }
        } else {
            jD = z ? this.g : this.f;
        }
        al.c("[UploadManager] Local network consume: %d KB", Long.valueOf(jD / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID));
        return jD;
    }

    protected final synchronized void a(long j, boolean z) {
        int i = z ? 5 : 3;
        y yVar = new y();
        yVar.b = i;
        yVar.e = ap.b();
        yVar.c = "";
        yVar.d = "";
        yVar.g = ap.c(j);
        this.c.b(i);
        this.c.a(yVar);
        if (z) {
            this.g = j;
        } else {
            this.f = j;
        }
        al.c("[UploadManager] Network total consume: %d KB", Long.valueOf(j / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID));
    }

    public final synchronized void a(int i, long j) {
        if (i < 0) {
            al.e("[UploadManager] Unknown uploading ID: %d", Integer.valueOf(i));
            return;
        }
        this.e.put(Integer.valueOf(i), Long.valueOf(j));
        y yVar = new y();
        yVar.b = i;
        yVar.e = j;
        yVar.c = "";
        yVar.d = "";
        yVar.g = new byte[0];
        this.c.b(i);
        this.c.a(yVar);
        al.c("[UploadManager] Uploading(ID:%d) time: %s", Integer.valueOf(i), ap.a(j));
    }

    public final synchronized long a(int i) {
        if (i >= 0) {
            Long l = this.e.get(Integer.valueOf(i));
            if (l != null) {
                return l.longValue();
            }
        } else {
            al.e("[UploadManager] Unknown upload ID: %d", Integer.valueOf(i));
        }
        return 0L;
    }

    public final boolean b(int i) {
        if (p.c) {
            al.c("Uploading frequency will not be checked if SDK is in debug mode.", new Object[0]);
            return true;
        }
        long jCurrentTimeMillis = System.currentTimeMillis() - a(i);
        al.c("[UploadManager] Time interval is %d seconds since last uploading(ID: %d).", Long.valueOf(jCurrentTimeMillis / 1000), Integer.valueOf(i));
        if (jCurrentTimeMillis >= 30000) {
            return true;
        }
        al.a("[UploadManager] Data only be uploaded once in %d seconds.", 30L);
        return false;
    }

    private void b() {
        ak akVarA = ak.a();
        LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<>();
        final LinkedBlockingQueue linkedBlockingQueue2 = new LinkedBlockingQueue();
        synchronized (this.j) {
            al.c("[UploadManager] Try to poll all upload task need and put them into temp queue (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
            int size = this.h.size();
            final int size2 = this.i.size();
            if (size == 0 && size2 == 0) {
                al.c("[UploadManager] There is no upload task in queue.", new Object[0]);
                return;
            }
            if (akVarA == null || !akVarA.c()) {
                size2 = 0;
            }
            a(this.h, linkedBlockingQueue, size);
            a(this.i, linkedBlockingQueue2, size2);
            a(size, linkedBlockingQueue);
            if (size2 > 0) {
                al.c("[UploadManager] Execute upload tasks of queue which has %d tasks (pid=%d | tid=%d)", Integer.valueOf(size2), Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
            }
            ak akVarA2 = ak.a();
            if (akVarA2 != null) {
                akVarA2.a(new Runnable() { // from class: com.tencent.bugly.proguard.ai.2
                    @Override // java.lang.Runnable
                    public final void run() {
                        Runnable runnable;
                        for (int i = 0; i < size2 && (runnable = (Runnable) linkedBlockingQueue2.poll()) != null; i++) {
                            runnable.run();
                        }
                    }
                });
            }
        }
    }

    private static void a(LinkedBlockingQueue<Runnable> linkedBlockingQueue, LinkedBlockingQueue<Runnable> linkedBlockingQueue2, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            Runnable runnablePeek = linkedBlockingQueue.peek();
            if (runnablePeek == null) {
                return;
            }
            try {
                linkedBlockingQueue2.put(runnablePeek);
                linkedBlockingQueue.poll();
            } catch (Throwable th) {
                al.e("[UploadManager] Failed to add upload task to temp urgent queue: %s", th.getMessage());
            }
        }
    }

    private void a(int i, LinkedBlockingQueue<Runnable> linkedBlockingQueue) {
        ak akVarA = ak.a();
        if (i > 0) {
            al.c("[UploadManager] Execute urgent upload tasks of queue which has %d tasks (pid=%d | tid=%d)", Integer.valueOf(i), Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
        }
        for (int i2 = 0; i2 < i; i2++) {
            final Runnable runnablePoll = linkedBlockingQueue.poll();
            if (runnablePoll == null) {
                return;
            }
            synchronized (this.j) {
                if (this.l >= 2 && akVarA != null) {
                    akVarA.a(runnablePoll);
                } else {
                    al.a("[UploadManager] Create and start a new thread to execute a upload task: %s", "BUGLY_ASYNC_UPLOAD");
                    if (ap.a(new Runnable() { // from class: com.tencent.bugly.proguard.ai.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            runnablePoll.run();
                            synchronized (ai.this.j) {
                                ai.b(ai.this);
                            }
                        }
                    }, "BUGLY_ASYNC_UPLOAD") != null) {
                        synchronized (this.j) {
                            this.l++;
                        }
                    } else {
                        al.d("[UploadManager] Failed to start a thread to execute asynchronous upload task,will try again next time.", new Object[0]);
                        a(runnablePoll, true);
                    }
                }
            }
        }
    }

    private boolean a(Runnable runnable, boolean z) {
        if (runnable == null) {
            al.a("[UploadManager] Upload task should not be null", new Object[0]);
            return false;
        }
        try {
            al.c("[UploadManager] Add upload task to queue (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
            synchronized (this.j) {
                if (z) {
                    this.h.put(runnable);
                } else {
                    this.i.put(runnable);
                }
            }
            return true;
        } catch (Throwable th) {
            al.e("[UploadManager] Failed to add upload task to queue: %s", th.getMessage());
            return false;
        }
    }

    private void a(Runnable runnable, long j) {
        if (runnable == null) {
            al.d("[UploadManager] Upload task should not be null", new Object[0]);
            return;
        }
        al.c("[UploadManager] Execute synchronized upload task (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
        Thread threadA = ap.a(runnable, "BUGLY_SYNC_UPLOAD");
        if (threadA == null) {
            al.e("[UploadManager] Failed to start a thread to execute synchronized upload task, add it to queue.", new Object[0]);
            a(runnable, true);
            return;
        }
        try {
            threadA.join(j);
        } catch (Throwable th) {
            al.e("[UploadManager] Failed to join upload synchronized task with message: %s. Add it to queue.", th.getMessage());
            a(runnable, true);
            b();
        }
    }

    private void a(Runnable runnable, boolean z, boolean z2, long j) {
        al.c("[UploadManager] Add upload task (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
        if (z2) {
            a(runnable, j);
        } else {
            a(runnable, z);
            b();
        }
    }
}
