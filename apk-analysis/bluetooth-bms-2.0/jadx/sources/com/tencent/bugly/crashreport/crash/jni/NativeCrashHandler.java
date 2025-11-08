package com.tencent.bugly.crashreport.crash.jni;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.proguard.aa;
import com.tencent.bugly.proguard.ab;
import com.tencent.bugly.proguard.ac;
import com.tencent.bugly.proguard.ak;
import com.tencent.bugly.proguard.al;
import com.tencent.bugly.proguard.ap;
import com.tencent.bugly.proguard.as;
import com.tencent.bugly.proguard.at;
import com.tencent.bugly.proguard.bd;
import com.tencent.bugly.proguard.be;
import com.tencent.bugly.proguard.q;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class NativeCrashHandler implements q {
    static String a = null;
    private static NativeCrashHandler b = null;
    private static int c = 1;
    private static boolean n = true;
    private final Context d;
    private final aa e;
    private final ak f;
    private NativeExceptionHandler g;
    private final boolean h;
    private boolean i = false;
    private boolean j = false;
    private boolean k = false;
    private boolean l = false;
    private as m;

    private native String getProperties(String str);

    private native String getSoCpuAbi();

    protected native boolean appendNativeLog(String str, String str2, String str3);

    protected native boolean appendWholeNativeLog(String str);

    protected native String getNativeKeyValueList();

    protected native String getNativeLog();

    protected native boolean putNativeKeyValue(String str, String str2);

    protected native String regist(String str, boolean z, int i);

    protected native String removeNativeKeyValue(String str);

    protected native void setNativeInfo(int i, String str);

    protected native void testCrash();

    protected native String unregist();

    static /* synthetic */ boolean a(NativeCrashHandler nativeCrashHandler, String str) {
        return nativeCrashHandler.a(999, str);
    }

    private NativeCrashHandler(Context context, aa aaVar, as asVar, ak akVar, boolean z, String str) {
        this.d = ap.a(context);
        if (ap.b(a)) {
            try {
                if (ap.b(str)) {
                    str = context.getDir("bugly", 0).getAbsolutePath();
                }
            } catch (Throwable unused) {
                str = "/data/data/" + aa.a(context).c + "/app_bugly";
            }
            a = str;
        }
        this.m = asVar;
        this.e = aaVar;
        this.f = akVar;
        this.h = z;
        this.g = new bd(context, aaVar, asVar, ac.a());
    }

    public static synchronized NativeCrashHandler getInstance(Context context, aa aaVar, as asVar, ac acVar, ak akVar, boolean z, String str) {
        if (b == null) {
            b = new NativeCrashHandler(context, aaVar, asVar, akVar, z, str);
        }
        return b;
    }

    public static synchronized NativeCrashHandler getInstance() {
        return b;
    }

    public static synchronized String getDumpFilePath() {
        return a;
    }

    public static synchronized void setDumpFilePath(String str) {
        a = str;
    }

    public static void setShouldHandleInJava(boolean z) {
        n = z;
        NativeCrashHandler nativeCrashHandler = b;
        if (nativeCrashHandler != null) {
            nativeCrashHandler.a(999, String.valueOf(z));
        }
    }

    public static boolean isShouldHandleInJava() {
        return n;
    }

    public String getRunningCpuAbi() {
        try {
            return getSoCpuAbi();
        } catch (Throwable unused) {
            al.d("get so cpu abi failed，please upgrade bugly so version", new Object[0]);
            return "";
        }
    }

    private synchronized void a(boolean z) {
        if (this.k) {
            al.d("[Native] Native crash report has already registered.", new Object[0]);
            return;
        }
        if (this.j) {
            try {
                String strRegist = regist(a, z, c);
                if (strRegist != null) {
                    al.a("[Native] Native Crash Report enable.", new Object[0]);
                    this.e.u = strRegist;
                    String strConcat = "-".concat(this.e.u);
                    if (!at.b && !this.e.h.contains(strConcat)) {
                        this.e.h = this.e.h.concat("-").concat(this.e.u);
                    }
                    al.a("comInfo.sdkVersion %s", this.e.h);
                    this.k = true;
                    String runningCpuAbi = getRunningCpuAbi();
                    if (!TextUtils.isEmpty(runningCpuAbi)) {
                        this.e.e(runningCpuAbi);
                    }
                    return;
                }
            } catch (Throwable unused) {
                al.c("[Native] Failed to load Bugly SO file.", new Object[0]);
            }
        } else if (this.i) {
            try {
                Class[] clsArr = {String.class, String.class, Integer.TYPE, Integer.TYPE};
                Object[] objArr = new Object[4];
                objArr[0] = a;
                objArr[1] = ab.d();
                objArr[2] = Integer.valueOf(z ? 1 : 5);
                objArr[3] = 1;
                String str = (String) ap.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "registNativeExceptionHandler2", clsArr, objArr);
                if (str == null) {
                    Class[] clsArr2 = {String.class, String.class, Integer.TYPE};
                    aa.b();
                    str = (String) ap.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "registNativeExceptionHandler", clsArr2, new Object[]{a, ab.d(), Integer.valueOf(aa.B())});
                }
                if (str != null) {
                    this.k = true;
                    this.e.u = str;
                    ap.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "enableHandler", new Class[]{Boolean.TYPE}, new Object[]{Boolean.TRUE});
                    ap.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "setLogMode", new Class[]{Integer.TYPE}, new Object[]{Integer.valueOf(z ? 1 : 5)});
                    String runningCpuAbi2 = getRunningCpuAbi();
                    if (!TextUtils.isEmpty(runningCpuAbi2)) {
                        this.e.e(runningCpuAbi2);
                    }
                    return;
                }
            } catch (Throwable unused2) {
            }
        }
        this.j = false;
        this.i = false;
    }

    public synchronized void startNativeMonitor() {
        if (!this.j && !this.i) {
            boolean z = !ap.b(this.e.t);
            if (at.b) {
                boolean zA = a(z ? this.e.t : "Bugly_Native", z);
                this.j = zA;
                if (!zA && !z) {
                    this.i = a("NativeRQD", false);
                }
            } else {
                String str = "Bugly_Native";
                String str2 = this.e.t;
                if (z) {
                    str = str2;
                } else {
                    this.e.getClass();
                }
                this.j = a(str, z);
            }
            if (this.j || this.i) {
                a(this.h);
                setNativeAppVersion(this.e.o);
                setNativeAppChannel(this.e.s);
                setNativeAppPackage(this.e.c);
                setNativeUserId(this.e.f());
                setNativeIsAppForeground(this.e.a());
                setNativeLaunchTime(this.e.a);
                return;
            }
            return;
        }
        a(this.h);
    }

    public void checkUploadRecordCrash() {
        this.f.a(new Runnable() { // from class: com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler.1
            @Override // java.lang.Runnable
            public final void run() {
                int i;
                if (ap.a(NativeCrashHandler.this.d, "native_record_lock")) {
                    if (!NativeCrashHandler.n) {
                        NativeCrashHandler.a(NativeCrashHandler.this, "false");
                    }
                    CrashDetailBean crashDetailBeanA = be.a(NativeCrashHandler.this.d, NativeCrashHandler.a, NativeCrashHandler.this.g);
                    if (crashDetailBeanA != null) {
                        al.a("[Native] Get crash from native record.", new Object[0]);
                        if (!NativeCrashHandler.this.m.a(crashDetailBeanA, true)) {
                            NativeCrashHandler.this.m.b(crashDetailBeanA, false);
                        }
                        be.a(false, NativeCrashHandler.a);
                    }
                    final NativeCrashHandler nativeCrashHandler = NativeCrashHandler.this;
                    long jB = ap.b() - at.j;
                    long jB2 = ap.b() + 86400000;
                    File file = new File(NativeCrashHandler.a);
                    if (file.exists() && file.isDirectory()) {
                        try {
                            File[] fileArrListFiles = file.listFiles();
                            if (fileArrListFiles != null && fileArrListFiles.length != 0) {
                                Arrays.sort(fileArrListFiles, new Comparator<File>() { // from class: com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler.2
                                    @Override // java.util.Comparator
                                    public final /* synthetic */ int compare(File file2, File file3) {
                                        return Long.compare(file3.lastModified(), file2.lastModified());
                                    }
                                });
                                long length = 0;
                                int length2 = fileArrListFiles.length;
                                int i2 = 0;
                                int i3 = 0;
                                int i4 = 0;
                                while (i2 < length2) {
                                    File file2 = fileArrListFiles[i2];
                                    long jLastModified = file2.lastModified();
                                    length += file2.length();
                                    if (jLastModified < jB || jLastModified >= jB2 || length >= at.i) {
                                        i = length2;
                                        al.a("[Native] Delete record file: %s", file2.getAbsolutePath());
                                        i3++;
                                        if (file2.delete()) {
                                            i4++;
                                        }
                                    } else {
                                        i = length2;
                                    }
                                    i2++;
                                    length2 = i;
                                }
                                al.c("[Native] Number of record files overdue: %d, has deleted: %d", Integer.valueOf(i3), Integer.valueOf(i4));
                            }
                        } catch (Throwable th) {
                            al.a(th);
                        }
                    }
                    ap.b(NativeCrashHandler.this.d, "native_record_lock");
                    return;
                }
                al.a("[Native] Failed to lock file for handling native crash record.", new Object[0]);
            }
        });
    }

    private static boolean a(String str, boolean z) {
        boolean z2;
        try {
            al.a("[Native] Trying to load so: %s", str);
            if (z) {
                System.load(str);
            } else {
                System.loadLibrary(str);
            }
        } catch (Throwable th) {
            th = th;
            z2 = false;
        }
        try {
            al.a("[Native] Successfully loaded SO: %s", str);
            return true;
        } catch (Throwable th2) {
            th = th2;
            z2 = true;
            al.d(th.getMessage(), new Object[0]);
            al.d("[Native] Failed to load so: %s", str);
            return z2;
        }
    }

    private synchronized void c() {
        if (!this.k) {
            al.d("[Native] Native crash report has already unregistered.", new Object[0]);
            return;
        }
        try {
        } catch (Throwable unused) {
            al.c("[Native] Failed to close native crash report.", new Object[0]);
        }
        if (unregist() != null) {
            al.a("[Native] Successfully closed native crash report.", new Object[0]);
            this.k = false;
            return;
        }
        try {
            ap.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "enableHandler", new Class[]{Boolean.TYPE}, new Object[]{Boolean.FALSE});
            this.k = false;
            al.a("[Native] Successfully closed native crash report.", new Object[0]);
            return;
        } catch (Throwable unused2) {
            al.c("[Native] Failed to close native crash report.", new Object[0]);
            this.j = false;
            this.i = false;
            return;
        }
    }

    public void testNativeCrash() {
        if (!this.j) {
            al.d("[Native] Bugly SO file has not been load.", new Object[0]);
        } else {
            testCrash();
        }
    }

    public void testNativeCrash(boolean z, boolean z2, boolean z3) {
        a(16, String.valueOf(z));
        a(17, String.valueOf(z2));
        a(18, String.valueOf(z3));
        testNativeCrash();
    }

    public void dumpAnrNativeStack() {
        a(19, "1");
    }

    public void resendSigquit() {
        a(20, "");
    }

    public void unBlockSigquit(boolean z) {
        if (z) {
            a(21, "true");
        } else {
            a(21, "false");
        }
    }

    public NativeExceptionHandler getNativeExceptionHandler() {
        return this.g;
    }

    public void removeEmptyNativeRecordFiles() {
        be.c(a);
    }

    private synchronized void b(boolean z) {
        if (z) {
            startNativeMonitor();
        } else {
            c();
        }
    }

    public synchronized boolean isUserOpened() {
        return this.l;
    }

    private synchronized void c(boolean z) {
        if (this.l != z) {
            al.a("user change native %b", Boolean.valueOf(z));
            this.l = z;
        }
    }

    public synchronized void setUserOpened(boolean z) {
        c(z);
        boolean zIsUserOpened = isUserOpened();
        ac acVarA = ac.a();
        if (acVarA != null) {
            zIsUserOpened = zIsUserOpened && acVarA.c().f;
        }
        if (zIsUserOpened != this.k) {
            al.a("native changed to %b", Boolean.valueOf(zIsUserOpened));
            b(zIsUserOpened);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x002c  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0031 A[Catch: all -> 0x0043, TRY_LEAVE, TryCatch #0 {, blocks: (B:5:0x0005, B:7:0x000b, B:8:0x001a, B:10:0x0026, B:14:0x002d, B:16:0x0031), top: B:22:0x0005 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void onStrategyChanged(com.tencent.bugly.crashreport.common.strategy.StrategyBean r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            r0 = 1
            r1 = 0
            if (r5 == 0) goto L1a
            boolean r2 = r5.f     // Catch: java.lang.Throwable -> L43
            boolean r3 = r4.k     // Catch: java.lang.Throwable -> L43
            if (r2 == r3) goto L1a
            java.lang.String r2 = "server native changed to %b"
            java.lang.Object[] r3 = new java.lang.Object[r0]     // Catch: java.lang.Throwable -> L43
            boolean r5 = r5.f     // Catch: java.lang.Throwable -> L43
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)     // Catch: java.lang.Throwable -> L43
            r3[r1] = r5     // Catch: java.lang.Throwable -> L43
            com.tencent.bugly.proguard.al.d(r2, r3)     // Catch: java.lang.Throwable -> L43
        L1a:
            com.tencent.bugly.proguard.ac r5 = com.tencent.bugly.proguard.ac.a()     // Catch: java.lang.Throwable -> L43
            com.tencent.bugly.crashreport.common.strategy.StrategyBean r5 = r5.c()     // Catch: java.lang.Throwable -> L43
            boolean r5 = r5.f     // Catch: java.lang.Throwable -> L43
            if (r5 == 0) goto L2c
            boolean r5 = r4.l     // Catch: java.lang.Throwable -> L43
            if (r5 == 0) goto L2c
            r5 = 1
            goto L2d
        L2c:
            r5 = 0
        L2d:
            boolean r2 = r4.k     // Catch: java.lang.Throwable -> L43
            if (r5 == r2) goto L41
            java.lang.String r2 = "native changed to %b"
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch: java.lang.Throwable -> L43
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r5)     // Catch: java.lang.Throwable -> L43
            r0[r1] = r3     // Catch: java.lang.Throwable -> L43
            com.tencent.bugly.proguard.al.a(r2, r0)     // Catch: java.lang.Throwable -> L43
            r4.b(r5)     // Catch: java.lang.Throwable -> L43
        L41:
            monitor-exit(r4)
            return
        L43:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler.onStrategyChanged(com.tencent.bugly.crashreport.common.strategy.StrategyBean):void");
    }

    @Override // com.tencent.bugly.proguard.q
    public boolean appendLogToNative(String str, String str2, String str3) {
        if ((this.i || this.j) && str != null && str2 != null && str3 != null) {
            try {
                if (this.j) {
                    return appendNativeLog(str, str2, str3);
                }
                Boolean bool = (Boolean) ap.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "appendNativeLog", new Class[]{String.class, String.class, String.class}, new Object[]{str, str2, str3});
                if (bool != null) {
                    return bool.booleanValue();
                }
                return false;
            } catch (UnsatisfiedLinkError unused) {
            } catch (Throwable th) {
                if (!al.a(th)) {
                    th.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override // com.tencent.bugly.proguard.q
    public String getLogFromNative() {
        if (!this.i && !this.j) {
            return null;
        }
        try {
            if (this.j) {
                return getNativeLog();
            }
            return (String) ap.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "getNativeLog", null, null);
        } catch (UnsatisfiedLinkError unused) {
            return null;
        } catch (Throwable th) {
            if (!al.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public boolean putKeyValueToNative(String str, String str2) {
        if ((this.i || this.j) && str != null && str2 != null) {
            try {
                if (this.j) {
                    return putNativeKeyValue(str, str2);
                }
                Boolean bool = (Boolean) ap.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "putNativeKeyValue", new Class[]{String.class, String.class}, new Object[]{str, str2});
                if (bool != null) {
                    return bool.booleanValue();
                }
                return false;
            } catch (UnsatisfiedLinkError unused) {
            } catch (Throwable th) {
                if (!al.a(th)) {
                    th.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean a(int i, String str) {
        if (!this.j) {
            return false;
        }
        try {
            setNativeInfo(i, str);
            return true;
        } catch (UnsatisfiedLinkError unused) {
            return false;
        } catch (Throwable th) {
            if (!al.a(th)) {
                th.printStackTrace();
            }
            return false;
        }
    }

    public boolean filterSigabrtSysLog() {
        return a(998, "true");
    }

    public boolean setNativeAppVersion(String str) {
        return a(10, str);
    }

    public boolean setNativeAppChannel(String str) {
        return a(12, str);
    }

    public boolean setNativeAppPackage(String str) {
        return a(13, str);
    }

    public boolean setNativeUserId(String str) {
        return a(11, str);
    }

    @Override // com.tencent.bugly.proguard.q
    public boolean setNativeIsAppForeground(boolean z) {
        return a(14, z ? "true" : "false");
    }

    public boolean setNativeLaunchTime(long j) {
        try {
            return a(15, String.valueOf(j));
        } catch (NumberFormatException e) {
            if (al.a(e)) {
                return false;
            }
            e.printStackTrace();
            return false;
        }
    }

    public void enableCatchAnrTrace() {
        if (Build.VERSION.SDK_INT > 19) {
            c |= 2;
        }
    }

    public void disableCatchAnrTrace() {
        if (Build.VERSION.SDK_INT > 19) {
            c = 1;
        }
    }

    public boolean isEnableCatchAnrTrace() {
        return (c & 2) == 2;
    }

    public String getSystemProperty(String str) {
        return (this.j || this.i) ? getProperties(str) : "fail";
    }
}
