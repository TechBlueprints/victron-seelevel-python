package com.tencent.bugly.proguard;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class bg extends Thread {
    public bf a;
    private a g;
    private boolean c = false;
    private boolean d = true;
    private boolean e = false;
    private int f = 1;
    public boolean b = true;

    /* compiled from: BUGLY */
    public interface a {
    }

    public final boolean a() {
        this.c = true;
        if (!isAlive()) {
            return false;
        }
        try {
            interrupt();
        } catch (Exception e) {
            al.b(e);
        }
        al.d("MainHandlerChecker is reset to null.", new Object[0]);
        this.a = null;
        return true;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        boolean z;
        long jCurrentTimeMillis = System.currentTimeMillis();
        while (!this.c) {
            try {
                z = false;
            } catch (Exception e) {
                al.b(e);
            } catch (OutOfMemoryError e2) {
                al.b(e2);
            }
            if (this.a == null) {
                al.c("Main handler checker is null. Stop thread monitor.", new Object[0]);
                return;
            }
            bf bfVar = this.a;
            if (bfVar.c) {
                bfVar.c = false;
                bfVar.d = SystemClock.uptimeMillis();
                bfVar.a.post(bfVar);
            }
            a(bfVar);
            boolean z2 = true;
            if (this.b && this.d) {
                long jB = bfVar.b();
                if (jB > 1510 && jB < 199990) {
                    if (jB <= 5010) {
                        this.f = 1;
                        al.c("timeSinceMsgSent in [2s, 5s], record stack", new Object[0]);
                        z = true;
                    } else {
                        int i = this.f + 1;
                        this.f = i;
                        if ((i & (i - 1)) != 0) {
                            z2 = false;
                        }
                        if (z2) {
                            al.c("timeSinceMsgSent in (5s, 200s), should record stack:true", new Object[0]);
                        }
                        z = z2;
                    }
                }
            }
            if (z) {
                bfVar.d();
            }
            if (this.g != null && this.d) {
                bfVar.a();
                bfVar.b();
            }
            ap.b(500 - ((System.currentTimeMillis() - jCurrentTimeMillis) % 500));
        }
    }

    private synchronized void a(bf bfVar) {
        if (this.d) {
            return;
        }
        if (this.e && !bfVar.a()) {
            al.c("Restart getting main stack trace.", new Object[0]);
            this.d = true;
            this.e = false;
        }
    }

    public final synchronized void c() {
        this.d = false;
        al.c("Record stack trace is disabled.", new Object[0]);
    }

    public final synchronized void d() {
        this.e = true;
    }

    public final boolean b() {
        Handler handler = new Handler(Looper.getMainLooper());
        bf bfVar = this.a;
        if (bfVar == null) {
            this.a = new bf(handler, handler.getLooper().getThread().getName());
        } else {
            bfVar.b = 5000L;
        }
        if (isAlive()) {
            return false;
        }
        try {
            start();
            return true;
        } catch (Exception e) {
            al.b(e);
            return false;
        }
    }
}
