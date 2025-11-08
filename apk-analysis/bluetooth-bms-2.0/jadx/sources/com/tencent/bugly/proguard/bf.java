package com.tencent.bugly.proguard;

import android.os.Handler;
import android.os.SystemClock;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class bf implements Runnable {
    final Handler a;
    long d;
    private final String e;
    private final List<ba> f = new LinkedList();
    long b = 5000;
    private final long g = 5000;
    boolean c = true;

    bf(Handler handler, String str) {
        this.a = handler;
        this.e = str;
    }

    public final boolean a() {
        return !this.c && SystemClock.uptimeMillis() >= this.d + this.b;
    }

    private Thread e() {
        return this.a.getLooper().getThread();
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.c = true;
        this.b = this.g;
    }

    public final long b() {
        return SystemClock.uptimeMillis() - this.d;
    }

    public final List<ba> c() {
        ArrayList arrayList;
        long jCurrentTimeMillis = System.currentTimeMillis();
        synchronized (this.f) {
            arrayList = new ArrayList(this.f.size());
            for (int i = 0; i < this.f.size(); i++) {
                ba baVar = this.f.get(i);
                if (!baVar.e && jCurrentTimeMillis - baVar.b < 200000) {
                    arrayList.add(baVar);
                    baVar.e = true;
                }
            }
        }
        return arrayList;
    }

    public final void d() {
        StringBuilder sb = new StringBuilder(1024);
        long jNanoTime = System.nanoTime();
        try {
            StackTraceElement[] stackTrace = e().getStackTrace();
            if (stackTrace.length == 0) {
                sb.append("Thread does not have stack trace.\n");
            } else {
                for (StackTraceElement stackTraceElement : stackTrace) {
                    sb.append(stackTraceElement);
                    sb.append("\n");
                }
            }
        } catch (SecurityException e) {
            sb.append("getStackTrace() encountered:\n");
            sb.append(e.getMessage());
            sb.append("\n");
            al.a(e);
        }
        long jNanoTime2 = System.nanoTime();
        ba baVar = new ba(sb.toString(), System.currentTimeMillis());
        baVar.d = jNanoTime2 - jNanoTime;
        String name = e().getName();
        if (name == null) {
            name = "";
        }
        baVar.a = name;
        synchronized (this.f) {
            while (this.f.size() >= 32) {
                this.f.remove(0);
            }
            this.f.add(baVar);
        }
    }
}
