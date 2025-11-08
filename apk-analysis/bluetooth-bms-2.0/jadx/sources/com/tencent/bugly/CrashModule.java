package com.tencent.bugly;

import android.content.Context;
import android.text.TextUtils;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.proguard.aa;
import com.tencent.bugly.proguard.al;
import com.tencent.bugly.proguard.aq;
import com.tencent.bugly.proguard.at;
import com.tencent.bugly.proguard.au;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.u;
import com.tencent.bugly.proguard.z;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class CrashModule extends o {
    public static final int MODULE_ID = 1004;
    private static int c;
    private static CrashModule e = new CrashModule();
    private long a;
    private BuglyStrategy.a b;
    private boolean d = false;

    public static CrashModule getInstance() {
        e.id = 1004;
        return e;
    }

    public synchronized boolean hasInitialized() {
        return this.d;
    }

    @Override // com.tencent.bugly.proguard.o
    public synchronized void init(Context context, boolean z, BuglyStrategy buglyStrategy) {
        if (context != null) {
            if (!this.d) {
                al.a("Initializing crash module.", new Object[0]);
                u uVarA = u.a();
                int i = c + 1;
                c = i;
                uVarA.a(i);
                this.d = true;
                CrashReport.setContext(context);
                a(context, buglyStrategy);
                at atVarA = at.a(context, z, this.b);
                atVarA.t.a();
                if (buglyStrategy != null) {
                    atVarA.B = buglyStrategy.getCallBackType();
                    atVarA.C = buglyStrategy.getCloseErrorCallback();
                    at.o = buglyStrategy.isUploadSpotCrash();
                    aa.a(context).S = buglyStrategy.isEnableRecordAnrMainStack();
                    if (!buglyStrategy.isEnableCatchAnrTrace()) {
                        atVarA.u.disableCatchAnrTrace();
                    } else {
                        atVarA.u.enableCatchAnrTrace();
                    }
                } else {
                    atVarA.u.enableCatchAnrTrace();
                }
                if (aa.b().d.equals(z.a(atVarA.c))) {
                    atVarA.u.removeEmptyNativeRecordFiles();
                }
                if (buglyStrategy == null || buglyStrategy.isEnableNativeCrashMonitor()) {
                    atVarA.e();
                } else {
                    al.a("[crash] Closed native crash monitor!", new Object[0]);
                    atVarA.d();
                }
                if (buglyStrategy == null || buglyStrategy.isEnableANRCrashMonitor()) {
                    atVarA.f();
                } else {
                    al.a("[crash] Closed ANR monitor!", new Object[0]);
                    atVarA.g();
                }
                if (buglyStrategy != null) {
                    at.e = buglyStrategy.isMerged();
                }
                atVarA.a(buglyStrategy != null ? buglyStrategy.getAppReportDelay() : 0L);
                atVarA.u.checkUploadRecordCrash();
                au.a(context);
                aq aqVarA = aq.a();
                aqVarA.a("android.net.conn.CONNECTIVITY_CHANGE");
                aqVarA.a(context);
                u uVarA2 = u.a();
                int i2 = c - 1;
                c = i2;
                uVarA2.a(i2);
            }
        }
    }

    private synchronized void a(Context context, BuglyStrategy buglyStrategy) {
        if (buglyStrategy == null) {
            return;
        }
        String libBuglySOFilePath = buglyStrategy.getLibBuglySOFilePath();
        if (!TextUtils.isEmpty(libBuglySOFilePath)) {
            aa.a(context).t = libBuglySOFilePath;
            al.a("setted libBugly.so file path :%s", libBuglySOFilePath);
        }
        if (buglyStrategy.getCrashHandleCallback() != null) {
            this.b = buglyStrategy.getCrashHandleCallback();
            al.a("setted CrashHanldeCallback", new Object[0]);
        }
        if (buglyStrategy.getAppReportDelay() > 0) {
            long appReportDelay = buglyStrategy.getAppReportDelay();
            this.a = appReportDelay;
            al.a("setted delay: %d", Long.valueOf(appReportDelay));
        }
    }

    @Override // com.tencent.bugly.proguard.o
    public void onServerStrategyChanged(StrategyBean strategyBean) {
        at atVarA;
        if (strategyBean == null || (atVarA = at.a()) == null) {
            return;
        }
        atVarA.t.a(strategyBean);
        atVarA.u.onStrategyChanged(strategyBean);
        atVarA.x.b();
    }

    @Override // com.tencent.bugly.proguard.o
    public String[] getTables() {
        return new String[]{"t_cr"};
    }
}
