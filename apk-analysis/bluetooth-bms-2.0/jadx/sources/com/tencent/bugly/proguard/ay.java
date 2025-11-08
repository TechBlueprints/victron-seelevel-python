package com.tencent.bugly.proguard;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.FileObserver;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.crashreport.crash.anr.TraceFileHelper;
import com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class ay {
    public static ay f;
    public final ActivityManager b;
    final aa c;
    final ak d;
    String e;
    private final Context g;
    private final ac h;
    private final as i;
    private FileObserver k;
    private bg m;
    private int n;
    public final AtomicBoolean a = new AtomicBoolean(false);
    private final Object j = new Object();
    private boolean l = true;
    private long o = 0;

    public static synchronized ay a() {
        return f;
    }

    public ay(Context context, ac acVar, aa aaVar, ak akVar, as asVar) {
        Context contextA = ap.a(context);
        this.g = contextA;
        this.b = (ActivityManager) contextA.getSystemService("activity");
        if (ap.b(NativeCrashHandler.getDumpFilePath())) {
            this.e = context.getDir("bugly", 0).getAbsolutePath();
        } else {
            this.e = NativeCrashHandler.getDumpFilePath();
        }
        this.c = aaVar;
        this.d = akVar;
        this.h = acVar;
        this.i = asVar;
    }

    private CrashDetailBean a(ax axVar) {
        CrashDetailBean crashDetailBean = new CrashDetailBean();
        try {
            crashDetailBean.C = ab.j();
            crashDetailBean.D = ab.f();
            crashDetailBean.E = ab.l();
            crashDetailBean.F = this.c.k();
            crashDetailBean.G = this.c.j();
            crashDetailBean.H = this.c.l();
            crashDetailBean.I = ab.b(this.g);
            crashDetailBean.J = ab.g();
            crashDetailBean.K = ab.h();
            crashDetailBean.b = 3;
            crashDetailBean.e = this.c.g();
            crashDetailBean.f = this.c.o;
            crashDetailBean.g = this.c.q();
            crashDetailBean.m = this.c.f();
            crashDetailBean.n = "ANR_EXCEPTION";
            crashDetailBean.o = axVar.f;
            crashDetailBean.q = axVar.g;
            crashDetailBean.T = new HashMap();
            crashDetailBean.T.put("BUGLY_CR_01", axVar.e);
            int iIndexOf = crashDetailBean.q != null ? crashDetailBean.q.indexOf("\n") : -1;
            crashDetailBean.p = iIndexOf > 0 ? crashDetailBean.q.substring(0, iIndexOf) : "GET_FAIL";
            crashDetailBean.r = axVar.c;
            if (crashDetailBean.q != null) {
                crashDetailBean.u = ap.c(crashDetailBean.q.getBytes());
            }
            crashDetailBean.z = axVar.b;
            crashDetailBean.A = axVar.a;
            crashDetailBean.B = "main(1)";
            crashDetailBean.L = this.c.s();
            crashDetailBean.h = this.c.p();
            crashDetailBean.i = this.c.A();
            crashDetailBean.v = axVar.d;
            crashDetailBean.P = this.c.u;
            crashDetailBean.Q = this.c.a;
            crashDetailBean.R = this.c.a();
            crashDetailBean.U = this.c.z();
            crashDetailBean.V = this.c.x;
            crashDetailBean.W = this.c.t();
            crashDetailBean.X = this.c.y();
            crashDetailBean.y = ao.a();
        } catch (Throwable th) {
            if (!al.a(th)) {
                th.printStackTrace();
            }
        }
        return crashDetailBean;
    }

    private static boolean a(String str, String str2, String str3) throws Throwable {
        TraceFileHelper.a targetDumpInfo = TraceFileHelper.readTargetDumpInfo(str3, str, true);
        if (targetDumpInfo == null || targetDumpInfo.d == null || targetDumpInfo.d.isEmpty()) {
            al.e("not found trace dump for %s", str3);
            return false;
        }
        StringBuilder sb = new StringBuilder(1024);
        String[] strArr = targetDumpInfo.d.get("main");
        if (strArr != null && strArr.length >= 3) {
            sb.append("\"main\" tid=");
            sb.append(strArr[2]);
            sb.append(" :\n");
            sb.append(strArr[0]);
            sb.append("\n");
            sb.append(strArr[1]);
            sb.append("\n\n");
        }
        for (Map.Entry<String, String[]> entry : targetDumpInfo.d.entrySet()) {
            if (!entry.getKey().equals("main") && entry.getValue() != null && entry.getValue().length >= 3) {
                sb.append("\"");
                sb.append(entry.getKey());
                sb.append("\" tid=");
                sb.append(entry.getValue()[2]);
                sb.append(" :\n");
                sb.append(entry.getValue()[0]);
                sb.append("\n");
                sb.append(entry.getValue()[1]);
                sb.append("\n\n");
            }
        }
        return am.a(str2, sb.toString(), sb.length() * 2);
    }

    private static String a(List<ba> list, long j) {
        if (list == null || list.isEmpty()) {
            return "main thread stack not enable";
        }
        StringBuilder sb = new StringBuilder(4096);
        sb.append("\n>>>>> 以下为anr过程中主线程堆栈记录，可根据堆栈出现次数推测在该堆栈阻塞的时间，出现次数越多对anr贡献越大，越可能是造成anr的原因 >>>>>\n");
        sb.append("\n>>>>> Thread Stack Traces Records Start >>>>>\n");
        for (int i = 0; i < list.size(); i++) {
            ba baVar = list.get(i);
            sb.append("Thread name:");
            sb.append(baVar.a);
            sb.append("\n");
            long j2 = baVar.b - j;
            String str = j2 <= 0 ? "before " : "after ";
            sb.append("Got ");
            sb.append(str);
            sb.append("anr:");
            sb.append(Math.abs(j2));
            sb.append("ms\n");
            sb.append(baVar.c);
            sb.append("\n");
            if (sb.length() * 2 >= 101376) {
                break;
            }
        }
        sb.append("\n<<<<< Thread Stack Traces Records End <<<<<\n");
        return sb.toString();
    }

    public final boolean a(boolean z) {
        boolean zCompareAndSet = this.a.compareAndSet(!z, z);
        al.c("tryChangeAnrState to %s, success:%s", Boolean.valueOf(z), Boolean.valueOf(zCompareAndSet));
        return zCompareAndSet;
    }

    private synchronized void c() {
        if (e()) {
            al.d("start when started!", new Object[0]);
            return;
        }
        FileObserver fileObserver = new FileObserver("/data/anr/") { // from class: com.tencent.bugly.proguard.ay.1
            @Override // android.os.FileObserver
            public final void onEvent(int i, String str) {
                if (str == null) {
                    return;
                }
                final String strConcat = "/data/anr/".concat(String.valueOf(str));
                al.d("watching file %s", strConcat);
                if (!strConcat.contains("trace")) {
                    al.d("not anr file %s", strConcat);
                } else {
                    ay.this.d.a(new Runnable() { // from class: com.tencent.bugly.proguard.ay.1.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            ay ayVar = ay.this;
                            String str2 = strConcat;
                            if (ayVar.a(true)) {
                                try {
                                    al.c("read trace first dump for create time!", new Object[0]);
                                    TraceFileHelper.a firstDumpInfo = TraceFileHelper.readFirstDumpInfo(str2, false);
                                    long jCurrentTimeMillis = firstDumpInfo != null ? firstDumpInfo.c : -1L;
                                    if (jCurrentTimeMillis == -1) {
                                        al.d("trace dump fail could not get time!", new Object[0]);
                                        jCurrentTimeMillis = System.currentTimeMillis();
                                    }
                                    if (ayVar.a(jCurrentTimeMillis)) {
                                        return;
                                    }
                                    ayVar.a(jCurrentTimeMillis, str2);
                                } catch (Throwable th) {
                                    if (!al.a(th)) {
                                        th.printStackTrace();
                                    }
                                    al.e("handle anr error %s", th.getClass().toString());
                                }
                            }
                        }
                    });
                }
            }
        };
        this.k = fileObserver;
        try {
            fileObserver.startWatching();
            al.a("start anr monitor!", new Object[0]);
            this.d.a(new Runnable() { // from class: com.tencent.bugly.proguard.ay.2
                @Override // java.lang.Runnable
                public final void run() {
                    ay.a(ay.this);
                }
            });
        } catch (Throwable th) {
            this.k = null;
            al.d("start anr monitor failed!", new Object[0]);
            if (al.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    private synchronized void d() {
        if (!e()) {
            al.d("close when closed!", new Object[0]);
            return;
        }
        try {
            this.k.stopWatching();
            this.k = null;
            al.d("close anr monitor!", new Object[0]);
        } catch (Throwable th) {
            al.d("stop anr monitor failed!", new Object[0]);
            if (al.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    private synchronized boolean e() {
        return this.k != null;
    }

    private synchronized void c(boolean z) {
        if (Build.VERSION.SDK_INT <= 19) {
            if (z) {
                c();
                return;
            } else {
                d();
                return;
            }
        }
        if (z) {
            g();
        } else {
            h();
        }
    }

    private synchronized boolean f() {
        return this.l;
    }

    private synchronized void d(boolean z) {
        if (this.l != z) {
            al.a("user change anr %b", Boolean.valueOf(z));
            this.l = z;
        }
    }

    public final void b(boolean z) {
        d(z);
        boolean zF = f();
        ac acVarA = ac.a();
        if (acVarA != null) {
            zF = zF && acVarA.c().f;
        }
        if (zF != e()) {
            al.a("anr changed to %b", Boolean.valueOf(zF));
            c(zF);
        }
    }

    public final synchronized void b() {
        al.d("customer decides whether to open or close.", new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a(long j, String str) {
        ActivityManager.ProcessErrorStateInfo processErrorStateInfoA;
        String strA;
        List<ba> listC;
        try {
            al.c("anr time:%s", Long.valueOf(j));
            synchronized (this.j) {
                if (this.m != null) {
                    al.c("Disable record main stack trace.", new Object[0]);
                    this.m.c();
                }
            }
            String strA2 = ap.a(Looper.getMainLooper().getThread());
            Map<String, String> mapA = ap.a(this.c.R, at.h);
            if (!this.c.a()) {
                processErrorStateInfoA = az.a(this.b, 0L);
            } else {
                boolean z = ab.o() || ab.p();
                al.c("isAnrCrashDevice:%s", Boolean.valueOf(z));
                if (z) {
                    processErrorStateInfoA = az.a(this.b, 0L);
                } else {
                    processErrorStateInfoA = az.a(this.b, 21000L);
                }
            }
            if (processErrorStateInfoA == null) {
                al.c("proc state is invisible or not my proc!", new Object[0]);
                return;
            }
            ax axVar = new ax();
            axVar.c = j;
            if (processErrorStateInfoA != null) {
                strA = processErrorStateInfoA.processName;
            } else {
                strA = z.a(Process.myPid());
            }
            axVar.a = strA;
            axVar.f = processErrorStateInfoA != null ? processErrorStateInfoA.shortMsg : "";
            axVar.e = processErrorStateInfoA != null ? processErrorStateInfoA.longMsg : "";
            axVar.b = mapA;
            axVar.g = strA2;
            if (TextUtils.isEmpty(axVar.g)) {
                axVar.g = "main stack is null , some error may be encountered.";
            }
            Object[] objArr = new Object[7];
            objArr[0] = Long.valueOf(axVar.c);
            objArr[1] = axVar.d;
            objArr[2] = axVar.a;
            objArr[3] = axVar.g;
            objArr[4] = axVar.f;
            objArr[5] = axVar.e;
            objArr[6] = Integer.valueOf(axVar.b == null ? 0 : axVar.b.size());
            al.c("anr time:%d\ntrace file:%s\nproc:%s\nmain stack:%s\nshort msg:%s\nlong msg:%s\n threads:%d", objArr);
            al.a("found visible anr , start to upload!", new Object[0]);
            al.c("trace file:%s", str);
            if (TextUtils.isEmpty(str) || !new File(str).exists()) {
                al.c("trace file is null or not exists, just ignore", new Object[0]);
            } else {
                File file = new File(this.e, "bugly_trace_" + j + ".txt");
                al.c("trace file exists", new Object[0]);
                if (str.startsWith("/data/anr/")) {
                    al.a("backup trace isOK:%s", Boolean.valueOf(a(str, file.getAbsolutePath(), axVar.a)));
                } else {
                    al.a("trace file rename :%s", Boolean.valueOf(new File(str).renameTo(file)));
                }
                synchronized (this.j) {
                    listC = this.m != null ? this.m.a.c() : null;
                }
                if (listC != null) {
                    String strA3 = a(listC, j);
                    al.c("save main stack trace", new Object[0]);
                    am.a(file, strA3, 2147483647L, true);
                }
                axVar.d = file.getAbsolutePath();
            }
            CrashDetailBean crashDetailBeanA = a(axVar);
            at.a().a(crashDetailBeanA);
            if (crashDetailBeanA.a >= 0) {
                al.a("backup anr record success!", new Object[0]);
            } else {
                al.d("backup anr record fail!", new Object[0]);
            }
            as.a("ANR", ap.a(j), axVar.a, "main", axVar.g, crashDetailBeanA);
            if (!this.i.a(crashDetailBeanA, !ab.r())) {
                this.i.b(crashDetailBeanA, true);
            }
            this.i.a(crashDetailBeanA);
            synchronized (this.j) {
                if (this.m != null) {
                    al.c("Finish anr process.", new Object[0]);
                    this.m.d();
                }
            }
        } catch (Throwable th) {
            try {
                al.b(th);
            } finally {
                a(false);
            }
        }
    }

    private synchronized void g() {
        if (e()) {
            al.d("start when started!", new Object[0]);
            return;
        }
        if (TextUtils.isEmpty(this.e)) {
            return;
        }
        synchronized (this.j) {
            if (this.m == null || !this.m.isAlive()) {
                bg bgVar = new bg();
                this.m = bgVar;
                boolean z = this.c.S;
                bgVar.b = z;
                al.c("set record stack trace enable:".concat(String.valueOf(z)), new Object[0]);
                bg bgVar2 = this.m;
                StringBuilder sb = new StringBuilder("Bugly-ThreadMonitor");
                int i = this.n;
                this.n = i + 1;
                sb.append(i);
                bgVar2.setName(sb.toString());
                this.m.b();
            }
        }
        FileObserver fileObserver = new FileObserver(this.e) { // from class: com.tencent.bugly.proguard.ay.3
            @Override // android.os.FileObserver
            public final void onEvent(int i2, String str) {
                if (str == null) {
                    return;
                }
                al.d("observe file, dir:%s fileName:%s", ay.this.e, str);
                if (!(str.startsWith("manual_bugly_trace_") && str.endsWith(".txt"))) {
                    al.c("not manual trace file, ignore.", new Object[0]);
                    return;
                }
                if (!ay.this.a.get()) {
                    al.c("proc is not in anr, just ignore", new Object[0]);
                    return;
                }
                if (ay.this.c.a()) {
                    al.c("Found foreground anr, resend sigquit immediately.", new Object[0]);
                    NativeCrashHandler.getInstance().resendSigquit();
                    long jA = am.a(str, "manual_bugly_trace_", ".txt");
                    ay.this.a(jA, ay.this.e + "/" + str);
                    al.c("Finish handling one anr.", new Object[0]);
                    return;
                }
                al.c("Found background anr, resend sigquit later.", new Object[0]);
                long jA2 = am.a(str, "manual_bugly_trace_", ".txt");
                ay.this.a(jA2, ay.this.e + "/" + str);
                al.c("Finish handling one anr, now resend sigquit.", new Object[0]);
                NativeCrashHandler.getInstance().resendSigquit();
            }
        };
        this.k = fileObserver;
        try {
            fileObserver.startWatching();
            al.a("startWatchingPrivateAnrDir! dumFilePath is %s", this.e);
            this.d.a(new Runnable() { // from class: com.tencent.bugly.proguard.ay.4
                @Override // java.lang.Runnable
                public final void run() {
                    ay.a(ay.this);
                }
            });
        } catch (Throwable th) {
            this.k = null;
            al.d("startWatchingPrivateAnrDir failed!", new Object[0]);
            if (al.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    public final boolean a(long j) {
        if (Math.abs(j - this.o) < 10000) {
            al.d("should not process ANR too Fre in %dms", 10000);
            return true;
        }
        this.o = j;
        return false;
    }

    private synchronized void h() {
        if (!e()) {
            al.d("close when closed!", new Object[0]);
            return;
        }
        synchronized (this.j) {
            if (this.m != null) {
                this.m.a();
                this.m = null;
            }
        }
        al.a("stopWatchingPrivateAnrDir", new Object[0]);
        try {
            this.k.stopWatching();
            this.k = null;
            al.d("close anr monitor!", new Object[0]);
        } catch (Throwable th) {
            al.d("stop anr monitor failed!", new Object[0]);
            if (al.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    static /* synthetic */ void a(ay ayVar) {
        long jCurrentTimeMillis = (at.j + System.currentTimeMillis()) - ap.b();
        am.a(ayVar.e, "bugly_trace_", ".txt", jCurrentTimeMillis);
        am.a(ayVar.e, "manual_bugly_trace_", ".txt", jCurrentTimeMillis);
        am.a(ayVar.e, "main_stack_record_", ".txt", jCurrentTimeMillis);
        am.a(ayVar.e, "main_stack_record_", ".txt.merged", jCurrentTimeMillis);
    }
}
