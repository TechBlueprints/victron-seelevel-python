package com.tencent.bugly.crashreport;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.core.os.EnvironmentCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.CrashModule;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.crash.h5.H5JavaScriptInterface;
import com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler;
import com.tencent.bugly.proguard.aa;
import com.tencent.bugly.proguard.ac;
import com.tencent.bugly.proguard.ak;
import com.tencent.bugly.proguard.al;
import com.tencent.bugly.proguard.an;
import com.tencent.bugly.proguard.ap;
import com.tencent.bugly.proguard.aq;
import com.tencent.bugly.proguard.at;
import com.tencent.bugly.proguard.au;
import com.tencent.bugly.proguard.bc;
import com.tencent.bugly.proguard.p;
import com.tencent.bugly.proguard.s;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.y;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class CrashReport {
    private static Context a;

    /* compiled from: BUGLY */
    public static class CrashHandleCallback extends BuglyStrategy.a {
    }

    /* compiled from: BUGLY */
    public interface a {
        String a();

        void a(H5JavaScriptInterface h5JavaScriptInterface, String str);

        void a(String str);

        void b();

        CharSequence c();
    }

    public static void enableBugly(boolean z) {
        p.a = z;
    }

    public static void initCrashReport(Context context) {
        if (context == null) {
            return;
        }
        a = context;
        p.a(CrashModule.getInstance());
        p.a(context);
    }

    public static void initCrashReport(Context context, UserStrategy userStrategy) {
        if (context == null) {
            return;
        }
        a = context;
        p.a(CrashModule.getInstance());
        p.a(context, userStrategy);
    }

    public static void initCrashReport(Context context, String str, boolean z) {
        initCrashReport(context, str, z, null);
    }

    public static void initCrashReport(Context context, String str, boolean z, UserStrategy userStrategy) {
        if (context == null) {
            return;
        }
        a = context;
        p.a(CrashModule.getInstance());
        p.a(context, str, z, userStrategy);
    }

    public static Context getContext() {
        return a;
    }

    public static String getBuglyVersion(Context context) {
        if (context == null) {
            al.d("Please call with context.", new Object[0]);
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        return aa.a(context).h;
    }

    public static void testJavaCrash() {
        int i;
        if (!p.a) {
            Log.w(al.b, "Can not test Java crash because bugly is disable.");
            return;
        }
        if (!CrashModule.getInstance().hasInitialized()) {
            Log.e(al.b, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return;
        }
        aa aaVarB = aa.b();
        if (aaVarB != null && (i = aaVarB.x) != 24096) {
            aaVarB.x = 24096;
            al.a("server scene tag %d changed to tag %d", Integer.valueOf(i), Integer.valueOf(aaVarB.x));
        }
        throw new RuntimeException("This Crash create for Test! You can go to Bugly see more detail!");
    }

    public static void testNativeCrash() {
        testNativeCrash(true, true, false);
    }

    public static void testNativeCrash(boolean z, boolean z2, boolean z3) {
        if (!p.a) {
            Log.w(al.b, "Can not test native crash because bugly is disable.");
        } else if (!CrashModule.getInstance().hasInitialized()) {
            Log.e(al.b, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            al.a("start to create a native crash for test!", new Object[0]);
            at.a().a(z, z2, z3);
        }
    }

    public static void testANRCrash() {
        if (!p.a) {
            Log.w(al.b, "Can not test ANR crash because bugly is disable.");
        } else if (!CrashModule.getInstance().hasInitialized()) {
            Log.e(al.b, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            al.a("start to create a anr crash for test!", new Object[0]);
            at.a().h();
        }
    }

    public static void postException(Thread thread, int i, String str, String str2, String str3, Map<String, String> map) {
        if (!p.a) {
            Log.w(al.b, "Can not post crash caught because bugly is disable.");
        } else if (!CrashModule.getInstance().hasInitialized()) {
            Log.e(al.b, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            au.a(thread, i, str, str2, str3, map);
        }
    }

    public static void postException(int i, String str, String str2, String str3, Map<String, String> map) {
        postException(Thread.currentThread(), i, str, str2, str3, map);
    }

    public static void postCatchedException(Throwable th) {
        postCatchedException(th, Thread.currentThread());
    }

    public static void postCatchedException(Throwable th, Thread thread) {
        postCatchedException(th, thread, false);
    }

    public static void postCatchedException(final Throwable th, final Thread thread, final boolean z) {
        if (!p.a) {
            Log.w(al.b, "Can not post crash caught because bugly is disable.");
            return;
        }
        if (!CrashModule.getInstance().hasInitialized()) {
            Log.e(al.b, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return;
        }
        if (th == null) {
            al.d("throwable is null, just return", new Object[0]);
            return;
        }
        if (thread == null) {
            thread = Thread.currentThread();
        }
        final at atVarA = at.a();
        atVarA.w.a(new Runnable() { // from class: com.tencent.bugly.proguard.at.3
            final /* synthetic */ boolean a = false;
            final /* synthetic */ String d = null;
            final /* synthetic */ byte[] e = null;
            final /* synthetic */ boolean f = true;

            @Override // java.lang.Runnable
            public final void run() {
                try {
                    al.c("post a throwable %b", Boolean.valueOf(this.a));
                    at.this.t.a(thread, th, false, this.d, this.e, this.f);
                    if (z) {
                        al.a("clear user datas", new Object[0]);
                        aa.a(at.this.c).u();
                    }
                } catch (Throwable th2) {
                    if (!al.b(th2)) {
                        th2.printStackTrace();
                    }
                    al.e("java catch error: %s", th.toString());
                }
            }
        });
    }

    public static void setAllThreadStackEnable(Context context, boolean z, boolean z2) {
        aa aaVarA = aa.a(context);
        aaVarA.Q = z;
        aaVarA.R = z2;
    }

    public static void closeNativeReport() {
        if (!p.a) {
            Log.w(al.b, "Can not close native report because bugly is disable.");
        } else if (!CrashModule.getInstance().hasInitialized()) {
            Log.e(al.b, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            at.a().d();
        }
    }

    public static void startCrashReport() {
        if (!p.a) {
            Log.w(al.b, "Can not start crash report because bugly is disable.");
        } else if (!CrashModule.getInstance().hasInitialized()) {
            Log.w(al.b, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            at.a().b();
        }
    }

    public static void closeCrashReport() {
        if (!p.a) {
            Log.w(al.b, "Can not close crash report because bugly is disable.");
        } else if (!CrashModule.getInstance().hasInitialized()) {
            Log.w(al.b, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            at.a().c();
        }
    }

    public static void closeBugly() {
        if (!p.a) {
            Log.w(al.b, "Can not close bugly because bugly is disable.");
            return;
        }
        if (!CrashModule.getInstance().hasInitialized()) {
            Log.w(al.b, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return;
        }
        if (a == null) {
            return;
        }
        aq aqVarA = aq.a();
        if (aqVarA != null) {
            aqVarA.b(a);
        }
        closeCrashReport();
        s.a(a);
        ak akVarA = ak.a();
        if (akVarA != null) {
            akVarA.b();
        }
    }

    public static void setUserSceneTag(Context context, int i) {
        if (!p.a) {
            Log.w(al.b, "Can not set tag caught because bugly is disable.");
            return;
        }
        if (context == null) {
            Log.e(al.b, "setTag args context should not be null");
            return;
        }
        if (i <= 0) {
            al.d("setTag args tagId should > 0", new Object[0]);
        }
        aa aaVarA = aa.a(context);
        synchronized (aaVarA.U) {
            int i2 = aaVarA.w;
            if (i2 != i) {
                aaVarA.w = i;
                al.a("user scene tag %d changed to tag %d", Integer.valueOf(i2), Integer.valueOf(aaVarA.w));
            }
        }
        al.b("[param] set user scene tag: %d", Integer.valueOf(i));
    }

    public static int getUserSceneTagId(Context context) {
        if (!p.a) {
            Log.w(al.b, "Can not get user scene tag because bugly is disable.");
            return -1;
        }
        if (context == null) {
            Log.e(al.b, "getUserSceneTagId args context should not be null");
            return -1;
        }
        return aa.a(context).z();
    }

    public static String getUserData(Context context, String str) {
        if (!p.a) {
            Log.w(al.b, "Can not get user data because bugly is disable.");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (context == null) {
            Log.e(al.b, "getUserDataValue args context should not be null");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (ap.b(str)) {
            return null;
        }
        return aa.a(context).g(str);
    }

    public static void putUserData(Context context, String str, String str2) {
        if (!p.a) {
            Log.w(al.b, "Can not put user data because bugly is disable.");
            return;
        }
        if (context == null) {
            Log.w(al.b, "putUserData args context should not be null");
            return;
        }
        if (str == null) {
            new StringBuilder().append(str);
            al.d("putUserData args key should not be null or empty", new Object[0]);
            return;
        }
        if (str2 == null) {
            new StringBuilder().append(str2);
            al.d("putUserData args value should not be null", new Object[0]);
            return;
        }
        if (str2.length() > 200) {
            al.d("user data value length over limit %d, it will be cutted!", Integer.valueOf(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION));
            str2 = str2.substring(0, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        }
        aa aaVarA = aa.a(context);
        if (aaVarA.w().contains(str)) {
            NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
            if (nativeCrashHandler != null) {
                nativeCrashHandler.putKeyValueToNative(str, str2);
            }
            aa.a(context).a(str, str2);
            al.c("replace KV %s %s", str, str2);
            return;
        }
        if (aaVarA.v() >= 50) {
            al.d("user data size is over limit %d, it will be cutted!", 50);
            return;
        }
        if (str.length() > 50) {
            al.d("user data key length over limit %d , will drop this new key %s", 50, str);
            str = str.substring(0, 50);
        }
        NativeCrashHandler nativeCrashHandler2 = NativeCrashHandler.getInstance();
        if (nativeCrashHandler2 != null) {
            nativeCrashHandler2.putKeyValueToNative(str, str2);
        }
        aa.a(context).a(str, str2);
        al.b("[param] set user data: %s - %s", str, str2);
    }

    public static String removeUserData(Context context, String str) {
        if (!p.a) {
            Log.w(al.b, "Can not remove user data because bugly is disable.");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (context == null) {
            Log.e(al.b, "removeUserData args context should not be null");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (ap.b(str)) {
            return null;
        }
        al.b("[param] remove user data: %s", str);
        return aa.a(context).f(str);
    }

    public static Set<String> getAllUserDataKeys(Context context) {
        if (!p.a) {
            Log.w(al.b, "Can not get all keys of user data because bugly is disable.");
            return new HashSet();
        }
        if (context == null) {
            Log.e(al.b, "getAllUserDataKeys args context should not be null");
            return new HashSet();
        }
        return aa.a(context).w();
    }

    public static int getUserDatasSize(Context context) {
        if (!p.a) {
            Log.w(al.b, "Can not get size of user data because bugly is disable.");
            return -1;
        }
        if (context == null) {
            Log.e(al.b, "getUserDatasSize args context should not be null");
            return -1;
        }
        return aa.a(context).v();
    }

    public static String getAppID() {
        if (!p.a) {
            Log.w(al.b, "Can not get App ID because bugly is disable.");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (!CrashModule.getInstance().hasInitialized()) {
            Log.e(al.b, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        return aa.a(a).e();
    }

    public static void setUserId(String str) {
        if (!p.a) {
            Log.w(al.b, "Can not set user ID because bugly is disable.");
        } else if (!CrashModule.getInstance().hasInitialized()) {
            Log.e(al.b, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            setUserId(a, str);
        }
    }

    public static void setUserId(Context context, String str) {
        if (!p.a) {
            Log.w(al.b, "Can not set user ID because bugly is disable.");
            return;
        }
        if (context == null) {
            Log.e(al.b, "Context should not be null when bugly has not been initialed!");
            return;
        }
        if (TextUtils.isEmpty(str)) {
            al.d("userId should not be null", new Object[0]);
            return;
        }
        if (str.length() > 100) {
            String strSubstring = str.substring(0, 100);
            al.d("userId %s length is over limit %d substring to %s", str, 100, strSubstring);
            str = strSubstring;
        }
        if (str.equals(aa.a(context).f())) {
            return;
        }
        aa aaVarA = aa.a(context);
        synchronized (aaVarA.V) {
            aaVarA.l = String.valueOf(str == null ? "10000" : str);
        }
        al.b("[user] set userId : %s", str);
        NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
        if (nativeCrashHandler != null) {
            nativeCrashHandler.setNativeUserId(str);
        }
        if (CrashModule.getInstance().hasInitialized()) {
            s.a();
        }
    }

    public static String getUserId() {
        if (!p.a) {
            Log.w(al.b, "Can not get user ID because bugly is disable.");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (!CrashModule.getInstance().hasInitialized()) {
            Log.e(al.b, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        return aa.a(a).f();
    }

    public static void setDeviceId(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return;
        }
        aa.a(context).a(str);
    }

    public static void setDeviceModel(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return;
        }
        aa.a(context).b(str);
    }

    public static String getDeviceID(Context context) {
        return aa.a(context).g();
    }

    public static String getAppVer() {
        if (!p.a) {
            Log.w(al.b, "Can not get app version because bugly is disable.");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (!CrashModule.getInstance().hasInitialized()) {
            Log.e(al.b, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        return aa.a(a).o;
    }

    public static String getAppChannel() {
        if (!p.a) {
            Log.w(al.b, "Can not get App channel because bugly is disable.");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (!CrashModule.getInstance().hasInitialized()) {
            Log.e(al.b, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        return aa.a(a).s;
    }

    public static void setContext(Context context) {
        a = context;
    }

    public static boolean isLastSessionCrash() {
        if (!p.a) {
            Log.w(al.b, "The info 'isLastSessionCrash' is not accurate because bugly is disable.");
            return false;
        }
        if (!CrashModule.getInstance().hasInitialized()) {
            Log.e(al.b, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return false;
        }
        at atVarA = at.a();
        Boolean bool = atVarA.A;
        if (bool != null) {
            return bool.booleanValue();
        }
        String str = aa.b().d;
        List<y> listA = w.a().a(1);
        ArrayList arrayList = new ArrayList();
        if (listA != null && listA.size() > 0) {
            for (y yVar : listA) {
                if (str.equals(yVar.c)) {
                    atVarA.A = Boolean.TRUE;
                    arrayList.add(yVar);
                }
            }
            if (arrayList.size() > 0) {
                w.a().a(arrayList);
            }
            return true;
        }
        atVarA.A = Boolean.FALSE;
        return false;
    }

    public static void setSdkExtraData(Context context, String str, String str2) {
        if (!p.a) {
            Log.w(al.b, "Can not put SDK extra data because bugly is disable.");
            return;
        }
        if (context == null || ap.b(str) || ap.b(str2)) {
            return;
        }
        aa aaVarA = aa.a(context);
        if (str == null || str2 == null) {
            return;
        }
        synchronized (aaVarA.T) {
            aaVarA.K.put(str, str2);
        }
    }

    public static Map<String, String> getSdkExtraData() {
        if (!p.a) {
            Log.w(al.b, "Can not get SDK extra data because bugly is disable.");
            return new HashMap();
        }
        if (!CrashModule.getInstance().hasInitialized()) {
            Log.e(al.b, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return null;
        }
        return aa.a(a).K;
    }

    public static Map<String, String> getSdkExtraData(Context context) {
        if (!p.a) {
            Log.w(al.b, "Can not get SDK extra data because bugly is disable.");
            return new HashMap();
        }
        if (context == null) {
            al.d("Context should not be null.", new Object[0]);
            return null;
        }
        return aa.a(context).K;
    }

    private static void putSdkData(Context context, String str, String str2) {
        if (context == null || ap.b(str) || ap.b(str2)) {
            return;
        }
        String strReplace = str.replace("[a-zA-Z[0-9]]+", "");
        if (strReplace.length() > 100) {
            Log.w(al.b, String.format("putSdkData key length over limit %d, will be cutted.", 50));
            strReplace = strReplace.substring(0, 50);
        }
        if (str2.length() > 500) {
            Log.w(al.b, String.format("putSdkData value length over limit %d, will be cutted!", Integer.valueOf(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION)));
            str2 = str2.substring(0, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        }
        aa.a(context).b(strReplace, str2);
        al.b(String.format("[param] putSdkData data: %s - %s", strReplace, str2), new Object[0]);
    }

    @Deprecated
    public static void setIsAppForeground(Context context, boolean z) {
        al.a("App fore and back status are no longer supported", new Object[0]);
    }

    public static void setIsDevelopmentDevice(Context context, boolean z) {
        if (!p.a) {
            Log.w(al.b, "Can not set 'isDevelopmentDevice' because bugly is disable.");
            return;
        }
        if (context == null) {
            al.d("Context should not be null.", new Object[0]);
            return;
        }
        if (z) {
            al.c("This is a development device.", new Object[0]);
        } else {
            al.c("This is not a development device.", new Object[0]);
        }
        aa.a(context).I = z;
    }

    public static void setSessionIntervalMills(long j) {
        if (!p.a) {
            Log.w(al.b, "Can not set 'SessionIntervalMills' because bugly is disable.");
        } else {
            s.a(j);
        }
    }

    public static void setDumpFilePath(Context context, String str) {
        if (!p.a) {
            Log.w(al.b, "Can not set App version because bugly is disable.");
            return;
        }
        if (context == null) {
            Log.w(al.b, "setTombPath args context should not be null");
        } else if (str == null) {
            Log.w(al.b, "tombstone path is null, will not set");
        } else {
            Log.i(al.b, "user set tombstone path: ".concat(String.valueOf(str)));
            NativeCrashHandler.setDumpFilePath(str);
        }
    }

    public static void setAppVersion(Context context, String str) {
        if (!p.a) {
            Log.w(al.b, "Can not set App version because bugly is disable.");
            return;
        }
        if (context == null) {
            Log.w(al.b, "setAppVersion args context should not be null");
            return;
        }
        if (str == null) {
            Log.w(al.b, "App version is null, will not set");
            return;
        }
        aa.a(context).o = str;
        NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
        if (nativeCrashHandler != null) {
            nativeCrashHandler.setNativeAppVersion(str);
        }
    }

    public static void setAppChannel(Context context, String str) {
        if (!p.a) {
            Log.w(al.b, "Can not set App channel because Bugly is disable.");
            return;
        }
        if (context == null) {
            Log.w(al.b, "setAppChannel args context should not be null");
            return;
        }
        if (str == null) {
            Log.w(al.b, "App channel is null, will not set");
            return;
        }
        aa.a(context).s = str;
        NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
        if (nativeCrashHandler != null) {
            nativeCrashHandler.setNativeAppChannel(str);
        }
    }

    public static void setAppPackage(Context context, String str) {
        if (!p.a) {
            Log.w(al.b, "Can not set App package because bugly is disable.");
            return;
        }
        if (context == null) {
            Log.w(al.b, "setAppPackage args context should not be null");
            return;
        }
        if (str == null) {
            Log.w(al.b, "App package is null, will not set");
            return;
        }
        aa.a(context).c = str;
        NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
        if (nativeCrashHandler != null) {
            nativeCrashHandler.setNativeAppPackage(str);
        }
    }

    public static void setCrashFilter(String str) {
        if (!p.a) {
            Log.w(al.b, "Can not set App package because bugly is disable.");
        } else {
            Log.i(al.b, "Set crash stack filter: ".concat(String.valueOf(str)));
            at.q = str;
        }
    }

    public static void setCrashRegularFilter(String str) {
        if (!p.a) {
            Log.w(al.b, "Can not set App package because bugly is disable.");
        } else {
            Log.i(al.b, "Set crash stack filter: ".concat(String.valueOf(str)));
            at.r = str;
        }
    }

    public static void setHandleNativeCrashInJava(boolean z) {
        if (!p.a) {
            Log.w(al.b, "Can not set App package because bugly is disable.");
        } else {
            Log.i(al.b, "Should handle native crash in Java profile after handled in native profile: ".concat(String.valueOf(z)));
            NativeCrashHandler.setShouldHandleInJava(z);
        }
    }

    public static void setBuglyDbName(String str) {
        if (!p.a) {
            Log.w(al.b, "Can not set DB name because bugly is disable.");
        } else {
            Log.i(al.b, "Set Bugly DB name: ".concat(String.valueOf(str)));
            x.a = str;
        }
    }

    public static void enableObtainId(Context context, boolean z) {
        setCollectPrivacyInfo(context, z);
    }

    public static void setCollectPrivacyInfo(Context context, boolean z) {
        if (!p.a) {
            Log.w(al.b, "Can not set collect privacy info enable because bugly is disable.");
        } else if (context == null) {
            Log.w(al.b, "setCollectPrivacyInfo args context should not be null");
        } else {
            Log.i(al.b, "setCollectPrivacyInfo: ".concat(String.valueOf(z)));
            aa.a(context).n = z;
        }
    }

    public static void setServerUrl(String str) {
        if (ap.b(str) || !ap.d(str)) {
            Log.i(al.b, "URL is invalid.");
            return;
        }
        ac.a(str);
        StrategyBean.a = str;
        StrategyBean.b = str;
    }

    public static void uploadUserInfo() {
        if (!p.a) {
            Log.w(al.b, "Can not upload user info because bugly is disable.");
        } else if (s.b == null) {
            Log.w(al.b, "Can not upload user info because bugly is not init.");
        } else {
            s.b.b();
        }
    }

    public static boolean setJavascriptMonitor(WebView webView, boolean z) {
        return setJavascriptMonitor(webView, z, false);
    }

    public static boolean setJavascriptMonitor(final WebView webView, boolean z, boolean z2) {
        if (webView == null) {
            Log.w(al.b, "WebView is null.");
            return false;
        }
        webView.getSettings().setSavePassword(false);
        webView.getSettings().setAllowFileAccess(false);
        return setJavascriptMonitor(new a() { // from class: com.tencent.bugly.crashreport.CrashReport.1
            @Override // com.tencent.bugly.crashreport.CrashReport.a
            public final String a() {
                return webView.getUrl();
            }

            @Override // com.tencent.bugly.crashreport.CrashReport.a
            public final void b() {
                WebSettings settings = webView.getSettings();
                if (settings.getJavaScriptEnabled()) {
                    return;
                }
                settings.setJavaScriptEnabled(true);
            }

            @Override // com.tencent.bugly.crashreport.CrashReport.a
            public final void a(String str) {
                webView.loadUrl(str);
            }

            @Override // com.tencent.bugly.crashreport.CrashReport.a
            public final void a(H5JavaScriptInterface h5JavaScriptInterface, String str) {
                webView.addJavascriptInterface(h5JavaScriptInterface, str);
            }

            @Override // com.tencent.bugly.crashreport.CrashReport.a
            public final CharSequence c() {
                return webView.getContentDescription();
            }
        }, z, z2);
    }

    public static boolean setJavascriptMonitor(a aVar, boolean z) {
        return setJavascriptMonitor(aVar, z, false);
    }

    public static boolean setJavascriptMonitor(a aVar, boolean z, boolean z2) {
        if (aVar == null) {
            Log.w(al.b, "WebViewInterface is null.");
            return false;
        }
        if (!CrashModule.getInstance().hasInitialized()) {
            al.e("CrashReport has not been initialed! please to call method 'initCrashReport' first!", new Object[0]);
            return false;
        }
        al.a("Set Javascript exception monitor of webview.", new Object[0]);
        if (!p.a) {
            Log.w(al.b, "Can not set JavaScript monitor because bugly is disable.");
            return false;
        }
        al.c("URL of webview is %s", aVar.a());
        if (!z2 && Build.VERSION.SDK_INT < 19) {
            al.e("This interface is only available for Android 4.4 or later.", new Object[0]);
            return false;
        }
        al.a("Enable the javascript needed by webview monitor.", new Object[0]);
        aVar.b();
        H5JavaScriptInterface h5JavaScriptInterface = H5JavaScriptInterface.getInstance(aVar);
        if (h5JavaScriptInterface != null) {
            al.a("Add a secure javascript interface to the webview.", new Object[0]);
            aVar.a(h5JavaScriptInterface, "exceptionUploader");
        }
        if (z) {
            al.a("Inject bugly.js(v%s) to the webview.", bc.b());
            String strA = bc.a();
            if (strA == null) {
                al.e("Failed to inject Bugly.js.", bc.b());
                return false;
            }
            aVar.a("javascript:".concat(String.valueOf(strA)));
        }
        return true;
    }

    /* compiled from: BUGLY */
    public static class UserStrategy extends BuglyStrategy {
        CrashHandleCallback c;

        public UserStrategy(Context context) {
        }

        @Override // com.tencent.bugly.BuglyStrategy
        public synchronized void setCallBackType(int i) {
            this.a = i;
        }

        @Override // com.tencent.bugly.BuglyStrategy
        public synchronized int getCallBackType() {
            return this.a;
        }

        @Override // com.tencent.bugly.BuglyStrategy
        public synchronized void setCloseErrorCallback(boolean z) {
            this.b = z;
        }

        @Override // com.tencent.bugly.BuglyStrategy
        public synchronized boolean getCloseErrorCallback() {
            return this.b;
        }

        @Override // com.tencent.bugly.BuglyStrategy
        public synchronized CrashHandleCallback getCrashHandleCallback() {
            return this.c;
        }

        public synchronized void setCrashHandleCallback(CrashHandleCallback crashHandleCallback) {
            this.c = crashHandleCallback;
        }
    }

    public static void setHttpProxy(String str, int i) {
        if (TextUtils.isEmpty(str)) {
            an.a = null;
        } else {
            an.a = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(str, i));
        }
    }

    public static void setHttpProxy(InetAddress inetAddress, int i) {
        if (inetAddress == null) {
            an.a = null;
        } else {
            an.a = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(inetAddress, i));
        }
    }

    public static Proxy getHttpProxy() {
        return an.a;
    }
}
