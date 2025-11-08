package com.tencent.bugly.proguard;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.proguard.r.a;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class s {
    public static boolean a = false;
    public static r b = null;
    private static int c = 10;
    private static long d = 300000;
    private static long e = 30000;
    private static long f = 0;
    private static int g = 0;
    private static long h = 0;
    private static long i = 0;
    private static long j = 0;
    private static Application.ActivityLifecycleCallbacks k = null;
    private static Class<?> l = null;
    private static boolean m = true;

    static /* synthetic */ int g() {
        int i2 = g;
        g = i2 + 1;
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:27:0x005b A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x005c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void c(android.content.Context r12, com.tencent.bugly.BuglyStrategy r13) {
        /*
            r0 = 1
            r1 = 0
            if (r13 == 0) goto Ld
            boolean r2 = r13.recordUserInfoOnceADay()
            boolean r13 = r13.isEnableUserInfo()
            goto Lf
        Ld:
            r13 = 1
            r2 = 0
        Lf:
            if (r2 == 0) goto L5d
            com.tencent.bugly.proguard.aa r13 = com.tencent.bugly.proguard.aa.a(r12)
            java.lang.String r2 = r13.d
            java.util.List r2 = com.tencent.bugly.proguard.r.a(r2)
            if (r2 == 0) goto L58
            r3 = 0
        L1e:
            int r4 = r2.size()
            if (r3 >= r4) goto L58
            java.lang.Object r4 = r2.get(r3)
            com.tencent.bugly.crashreport.biz.UserInfoBean r4 = (com.tencent.bugly.crashreport.biz.UserInfoBean) r4
            java.lang.String r5 = r4.n
            java.lang.String r6 = r13.o
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L55
            int r5 = r4.b
            if (r5 != r0) goto L55
            long r5 = com.tencent.bugly.proguard.ap.b()
            r7 = 0
            int r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r9 <= 0) goto L58
            long r9 = r4.e
            int r11 = (r9 > r5 ? 1 : (r9 == r5 ? 0 : -1))
            if (r11 < 0) goto L55
            long r2 = r4.f
            int r13 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1))
            if (r13 > 0) goto L53
            com.tencent.bugly.proguard.r r13 = com.tencent.bugly.proguard.s.b
            r13.b()
        L53:
            r13 = 0
            goto L59
        L55:
            int r3 = r3 + 1
            goto L1e
        L58:
            r13 = 1
        L59:
            if (r13 != 0) goto L5c
            return
        L5c:
            r13 = 0
        L5d:
            com.tencent.bugly.proguard.aa r2 = com.tencent.bugly.proguard.aa.b()
            if (r2 == 0) goto L6c
            boolean r3 = com.tencent.bugly.proguard.z.a()
            if (r3 == 0) goto L6c
            r2.a(r1, r0)
        L6c:
            if (r13 == 0) goto La1
            r13 = 0
            int r2 = android.os.Build.VERSION.SDK_INT
            r3 = 14
            if (r2 < r3) goto La1
            android.content.Context r2 = r12.getApplicationContext()
            boolean r2 = r2 instanceof android.app.Application
            if (r2 == 0) goto L84
            android.content.Context r12 = r12.getApplicationContext()
            r13 = r12
            android.app.Application r13 = (android.app.Application) r13
        L84:
            if (r13 == 0) goto La1
            android.app.Application$ActivityLifecycleCallbacks r12 = com.tencent.bugly.proguard.s.k     // Catch: java.lang.Exception -> L97
            if (r12 != 0) goto L91
            com.tencent.bugly.proguard.s$a r12 = new com.tencent.bugly.proguard.s$a     // Catch: java.lang.Exception -> L97
            r12.<init>()     // Catch: java.lang.Exception -> L97
            com.tencent.bugly.proguard.s.k = r12     // Catch: java.lang.Exception -> L97
        L91:
            android.app.Application$ActivityLifecycleCallbacks r12 = com.tencent.bugly.proguard.s.k     // Catch: java.lang.Exception -> L97
            r13.registerActivityLifecycleCallbacks(r12)     // Catch: java.lang.Exception -> L97
            goto La1
        L97:
            r12 = move-exception
            boolean r13 = com.tencent.bugly.proguard.al.a(r12)
            if (r13 != 0) goto La1
            r12.printStackTrace()
        La1:
            boolean r12 = com.tencent.bugly.proguard.s.m
            if (r12 == 0) goto Lc4
            long r12 = java.lang.System.currentTimeMillis()
            com.tencent.bugly.proguard.s.i = r12
            com.tencent.bugly.proguard.r r12 = com.tencent.bugly.proguard.s.b
            r12.a(r0, r1)
            java.lang.Object[] r12 = new java.lang.Object[r1]
            java.lang.String r13 = "[session] launch app, new start"
            com.tencent.bugly.proguard.al.a(r13, r12)
            com.tencent.bugly.proguard.r r12 = com.tencent.bugly.proguard.s.b
            r12.a()
            com.tencent.bugly.proguard.r r12 = com.tencent.bugly.proguard.s.b
            r0 = 21600000(0x1499700, double:1.0671818E-316)
            r12.a(r0)
        Lc4:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.s.c(android.content.Context, com.tencent.bugly.BuglyStrategy):void");
    }

    public static void a(final Context context, final BuglyStrategy buglyStrategy) {
        long appReportDelay;
        if (a) {
            return;
        }
        m = aa.a(context).f;
        b = new r(context, m);
        a = true;
        if (buglyStrategy != null) {
            l = buglyStrategy.getUserInfoActivity();
            appReportDelay = buglyStrategy.getAppReportDelay();
        } else {
            appReportDelay = 0;
        }
        if (appReportDelay <= 0) {
            c(context, buglyStrategy);
        } else {
            ak.a().a(new Runnable() { // from class: com.tencent.bugly.proguard.s.1
                @Override // java.lang.Runnable
                public final void run() {
                    s.c(context, buglyStrategy);
                }
            }, appReportDelay);
        }
    }

    public static void a(long j2) {
        if (j2 < 0) {
            j2 = ac.a().c().p;
        }
        f = j2;
    }

    public static void a(StrategyBean strategyBean, boolean z) {
        r rVar = b;
        if (rVar != null && !z) {
            rVar.b();
        }
        if (strategyBean == null) {
            return;
        }
        if (strategyBean.p > 0) {
            e = strategyBean.p;
        }
        if (strategyBean.u > 0) {
            c = strategyBean.u;
        }
        if (strategyBean.v > 0) {
            d = strategyBean.v;
        }
    }

    public static void a() {
        r rVar = b;
        if (rVar != null) {
            rVar.a(2, false);
        }
    }

    public static void a(Context context) {
        if (!a || context == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 14) {
            Application application = context.getApplicationContext() instanceof Application ? (Application) context.getApplicationContext() : null;
            if (application != null) {
                try {
                    if (k != null) {
                        application.unregisterActivityLifecycleCallbacks(k);
                    }
                } catch (Exception e2) {
                    if (!al.a(e2)) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        a = false;
    }

    /* compiled from: BUGLY */
    static class a implements Application.ActivityLifecycleCallbacks {
        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        a() {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityStopped(Activity activity) {
            al.c(">>> %s onStop <<<", activity.getClass().getName());
            aa.b().a(activity.hashCode(), false);
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityStarted(Activity activity) {
            al.c(">>> %s onStart <<<", activity.getClass().getName());
            aa.b().a(activity.hashCode(), true);
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityResumed(Activity activity) {
            String name = activity.getClass().getName();
            if (s.l == null || s.l.getName().equals(name)) {
                al.c(">>> %s onResumed <<<", name);
                aa aaVarB = aa.b();
                if (aaVarB == null) {
                    return;
                }
                aaVarB.L.add(s.a(name, "onResumed"));
                aaVarB.y = name;
                aaVarB.z = System.currentTimeMillis();
                aaVarB.C = aaVarB.z - s.i;
                long j = aaVarB.z - s.h;
                if (j > (s.f > 0 ? s.f : s.e)) {
                    aaVarB.c();
                    s.g();
                    al.a("[session] launch app one times (app in background %d seconds and over %d seconds)", Long.valueOf(j / 1000), Long.valueOf(s.e / 1000));
                    if (s.g % s.c == 0) {
                        s.b.a(4, s.m);
                        return;
                    }
                    s.b.a(4, false);
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    if (jCurrentTimeMillis - s.j > s.d) {
                        long unused = s.j = jCurrentTimeMillis;
                        al.a("add a timer to upload hot start user info", new Object[0]);
                        if (s.m) {
                            ak.a().a(s.b.new a(null, true), s.d);
                        }
                    }
                }
            }
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityPaused(Activity activity) {
            String name = activity.getClass().getName();
            if (s.l == null || s.l.getName().equals(name)) {
                al.c(">>> %s onPaused <<<", name);
                aa aaVarB = aa.b();
                if (aaVarB == null) {
                    return;
                }
                aaVarB.L.add(s.a(name, "onPaused"));
                aaVarB.A = System.currentTimeMillis();
                aaVarB.B = aaVarB.A - aaVarB.z;
                long unused = s.h = aaVarB.A;
                if (aaVarB.B < 0) {
                    aaVarB.B = 0L;
                }
                aaVarB.y = "background";
            }
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityDestroyed(Activity activity) {
            String name = activity.getClass().getName();
            if (s.l == null || s.l.getName().equals(name)) {
                al.c(">>> %s onDestroyed <<<", name);
                aa aaVarB = aa.b();
                if (aaVarB != null) {
                    aaVarB.L.add(s.a(name, "onDestroyed"));
                }
            }
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityCreated(Activity activity, Bundle bundle) {
            String name = activity.getClass().getName();
            if (s.l == null || s.l.getName().equals(name)) {
                al.c(">>> %s onCreated <<<", name);
                aa aaVarB = aa.b();
                if (aaVarB != null) {
                    aaVarB.L.add(s.a(name, "onCreated"));
                }
            }
        }
    }

    static /* synthetic */ String a(String str, String str2) {
        return ap.a() + "  " + str + "  " + str2 + "\n";
    }
}
