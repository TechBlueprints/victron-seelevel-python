package com.tencent.bugly.proguard;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler;
import com.tencent.bugly.proguard.ag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class at {
    private static at D = null;
    public static int a = 0;
    public static boolean b = false;
    public static int d = 2;
    public static boolean e = false;
    public static int f = 20480;
    public static int g = 3000;
    public static int h = 20480;
    public static long i = 209715200;
    public static long j = 604800000;
    public static String k = null;
    public static boolean l = false;
    public static String m = null;
    public static int n = 5000;
    public static boolean o = true;
    public static boolean p = false;
    public static String q;
    public static String r;
    public Boolean A;
    public int B = 31;
    public boolean C = false;
    public final Context c;
    public final as s;
    public final av t;
    public final NativeCrashHandler u;
    public final ac v;
    public final ak w;
    public final ay x;
    public BuglyStrategy.a y;
    public aw z;

    private at(Context context, ak akVar, boolean z, BuglyStrategy.a aVar) {
        a = 1004;
        Context contextA = ap.a(context);
        this.c = contextA;
        this.v = ac.a();
        this.w = akVar;
        this.y = aVar;
        this.z = null;
        this.s = new as(contextA, ai.a(), w.a(), this.v, aVar);
        aa aaVarA = aa.a(contextA);
        this.t = new av(contextA, this.s, this.v, aaVarA);
        NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance(contextA, aaVarA, this.s, this.v, akVar, z, null);
        this.u = nativeCrashHandler;
        aaVarA.N = nativeCrashHandler;
        ac acVar = this.v;
        as asVar = this.s;
        if (ay.f == null) {
            ay.f = new ay(contextA, acVar, aaVarA, akVar, asVar);
        }
        this.x = ay.f;
    }

    public static synchronized at a(Context context, boolean z, BuglyStrategy.a aVar) {
        if (D == null) {
            D = new at(context, ak.a(), z, aVar);
        }
        return D;
    }

    public static synchronized at a() {
        return D;
    }

    public final void d() {
        this.u.setUserOpened(false);
    }

    public final void e() {
        this.u.setUserOpened(true);
    }

    public final void f() {
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.tencent.bugly.proguard.at.1
            @Override // java.lang.Runnable
            public final void run() {
                NativeCrashHandler.getInstance().unBlockSigquit(true);
            }
        });
        this.x.b(true);
    }

    public final void g() {
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.tencent.bugly.proguard.at.2
            @Override // java.lang.Runnable
            public final void run() {
                NativeCrashHandler.getInstance().unBlockSigquit(false);
            }
        });
        this.x.b(false);
    }

    public final synchronized void a(boolean z, boolean z2, boolean z3) {
        this.u.testNativeCrash(z, z2, z3);
    }

    public final boolean i() {
        return this.x.a.get();
    }

    public final void a(CrashDetailBean crashDetailBean) {
        this.s.b(crashDetailBean);
    }

    public final void a(long j2) {
        ak.a().a(new Thread() { // from class: com.tencent.bugly.proguard.at.4
            @Override // java.lang.Thread, java.lang.Runnable
            public final void run() {
                List<CrashDetailBean> list;
                if (!ap.a(at.this.c, "local_crash_lock")) {
                    al.c("Failed to lock file for uploading local crash.", new Object[0]);
                    return;
                }
                ag agVar = ag.a.a;
                List<ag.b> listA = ag.a();
                if (listA == null || listA.isEmpty()) {
                    al.c("sla local data is null", new Object[0]);
                } else {
                    al.c("sla load local data list size:%s", Integer.valueOf(listA.size()));
                    Iterator<ag.b> it = listA.iterator();
                    ArrayList arrayList = new ArrayList();
                    while (it.hasNext()) {
                        ag.b next = it.next();
                        if (next.b < ap.b() - 604800000) {
                            al.c("sla local data is expired:%s", next.c);
                            arrayList.add(next);
                            it.remove();
                        }
                    }
                    ag.d(arrayList);
                    agVar.b(listA);
                }
                List<CrashDetailBean> listA2 = as.a();
                if (listA2 != null && listA2.size() > 0) {
                    al.c("Size of crash list: %s", Integer.valueOf(listA2.size()));
                    int size = listA2.size();
                    if (size > 20) {
                        ArrayList arrayList2 = new ArrayList();
                        Collections.sort(listA2);
                        for (int i2 = 0; i2 < 20; i2++) {
                            arrayList2.add(listA2.get((size - 1) - i2));
                        }
                        list = arrayList2;
                    } else {
                        list = listA2;
                    }
                    at.this.s.a(list, 0L, false, false, false);
                } else {
                    al.c("no crash need to be uploaded at this start", new Object[0]);
                }
                ap.b(at.this.c, "local_crash_lock");
            }
        }, j2);
    }

    public final boolean j() {
        return (this.B & 16) > 0;
    }

    public final boolean k() {
        return (this.B & 8) > 0;
    }

    public final synchronized void b() {
        this.t.a();
        e();
        f();
    }

    public final synchronized void c() {
        this.t.b();
        d();
        g();
    }

    public final synchronized void h() {
        int i2 = 0;
        while (true) {
            int i3 = i2 + 1;
            if (i2 < 30) {
                try {
                    al.a("try main sleep for make a test anr! try:%d/30 , kill it if you don't want to wait!", Integer.valueOf(i3));
                    ap.b(5000L);
                    i2 = i3;
                } catch (Throwable th) {
                    if (al.a(th)) {
                        return;
                    }
                    th.printStackTrace();
                    return;
                }
            }
        }
    }
}
