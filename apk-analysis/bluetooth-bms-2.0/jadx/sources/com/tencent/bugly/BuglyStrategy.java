package com.tencent.bugly;

import com.tencent.bugly.proguard.aa;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class BuglyStrategy {
    private String c;
    private String d;
    private String e;
    private long f;
    private String g;
    private String h;
    private String i;
    private a u;
    private boolean j = true;
    private boolean k = true;
    private boolean l = true;
    private boolean m = false;
    private boolean n = true;
    private Class<?> o = null;
    private boolean p = true;
    private boolean q = true;
    private boolean r = true;
    private boolean s = true;
    private boolean t = false;
    protected int a = 31;
    protected boolean b = false;
    private boolean v = false;

    public synchronized BuglyStrategy setBuglyLogUpload(boolean z) {
        this.p = z;
        return this;
    }

    public synchronized BuglyStrategy setRecordUserInfoOnceADay(boolean z) {
        this.t = z;
        return this;
    }

    public synchronized BuglyStrategy setUploadProcess(boolean z) {
        this.r = z;
        return this;
    }

    public synchronized boolean isUploadProcess() {
        return this.r;
    }

    public synchronized boolean isBuglyLogUpload() {
        return this.p;
    }

    public synchronized boolean recordUserInfoOnceADay() {
        return this.t;
    }

    public boolean isReplaceOldChannel() {
        return this.q;
    }

    public void setReplaceOldChannel(boolean z) {
        this.q = z;
    }

    public synchronized String getAppVersion() {
        if (this.c == null) {
            return aa.b().o;
        }
        return this.c;
    }

    public synchronized BuglyStrategy setAppVersion(String str) {
        this.c = str;
        return this;
    }

    public synchronized BuglyStrategy setUserInfoActivity(Class<?> cls) {
        this.o = cls;
        return this;
    }

    public synchronized Class<?> getUserInfoActivity() {
        return this.o;
    }

    public synchronized String getAppChannel() {
        if (this.d == null) {
            return aa.b().s;
        }
        return this.d;
    }

    public synchronized BuglyStrategy setAppChannel(String str) {
        this.d = str;
        return this;
    }

    public synchronized String getAppPackageName() {
        if (this.e == null) {
            return aa.b().c;
        }
        return this.e;
    }

    public synchronized BuglyStrategy setAppPackageName(String str) {
        this.e = str;
        return this;
    }

    public synchronized long getAppReportDelay() {
        return this.f;
    }

    public synchronized BuglyStrategy setAppReportDelay(long j) {
        this.f = j;
        return this;
    }

    public synchronized String getLibBuglySOFilePath() {
        return this.g;
    }

    public synchronized BuglyStrategy setLibBuglySOFilePath(String str) {
        this.g = str;
        return this;
    }

    public synchronized String getDeviceID() {
        return this.h;
    }

    public synchronized BuglyStrategy setDeviceID(String str) {
        this.h = str;
        return this;
    }

    public synchronized String getDeviceModel() {
        return this.i;
    }

    public synchronized BuglyStrategy setDeviceModel(String str) {
        this.i = str;
        return this;
    }

    public synchronized boolean isEnableNativeCrashMonitor() {
        return this.j;
    }

    public synchronized BuglyStrategy setEnableNativeCrashMonitor(boolean z) {
        this.j = z;
        return this;
    }

    public synchronized BuglyStrategy setEnableUserInfo(boolean z) {
        this.n = z;
        return this;
    }

    public synchronized boolean isEnableUserInfo() {
        return this.n;
    }

    public synchronized boolean isEnableCatchAnrTrace() {
        return this.l;
    }

    public void setEnableCatchAnrTrace(boolean z) {
        this.l = z;
    }

    public void setEnableRecordAnrMainStack(boolean z) {
        this.m = z;
    }

    public boolean isEnableRecordAnrMainStack() {
        return this.m;
    }

    public synchronized boolean isEnableANRCrashMonitor() {
        return this.k;
    }

    public synchronized BuglyStrategy setEnableANRCrashMonitor(boolean z) {
        this.k = z;
        return this;
    }

    public synchronized a getCrashHandleCallback() {
        return this.u;
    }

    public synchronized BuglyStrategy setCrashHandleCallback(a aVar) {
        this.u = aVar;
        return this;
    }

    public synchronized void setCallBackType(int i) {
        this.a = i;
    }

    public synchronized int getCallBackType() {
        return this.a;
    }

    public synchronized void setCloseErrorCallback(boolean z) {
        this.b = z;
    }

    public synchronized boolean getCloseErrorCallback() {
        return this.b;
    }

    public boolean isMerged() {
        return this.v;
    }

    @Deprecated
    public void setMerged(boolean z) {
        this.v = z;
    }

    public synchronized void setUploadSpotCrash(boolean z) {
        this.s = z;
    }

    public synchronized boolean isUploadSpotCrash() {
        return this.s;
    }

    /* compiled from: BUGLY */
    public static class a {
        public static final int CRASHTYPE_ANR = 4;
        public static final int CRASHTYPE_BLOCK = 7;
        public static final int CRASHTYPE_COCOS2DX_JS = 5;
        public static final int CRASHTYPE_COCOS2DX_LUA = 6;
        public static final int CRASHTYPE_JAVA_CATCH = 1;
        public static final int CRASHTYPE_JAVA_CRASH = 0;
        public static final int CRASHTYPE_NATIVE = 2;
        public static final int CRASHTYPE_U3D = 3;
        public static final int MAX_USERDATA_KEY_LENGTH = 100;
        public static final int MAX_USERDATA_VALUE_LENGTH = 100000;

        public synchronized Map<String, String> onCrashHandleStart(int i, String str, String str2, String str3) {
            return null;
        }

        public synchronized byte[] onCrashHandleStart2GetExtraDatas(int i, String str, String str2, String str3) {
            return null;
        }
    }
}
