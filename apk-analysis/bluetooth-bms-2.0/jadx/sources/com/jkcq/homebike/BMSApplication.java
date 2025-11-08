package com.jkcq.homebike;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jkcq.base.app.BaseApp;
import com.tencent.bugly.crashreport.CrashReport;
import kotlin.Metadata;

/* compiled from: BMSApplication.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016¨\u0006\u0005"}, d2 = {"Lcom/jkcq/homebike/BMSApplication;", "Lcom/jkcq/base/app/BaseApp;", "()V", "onCreate", "", "app_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class BMSApplication extends BaseApp {
    @Override // com.jkcq.base.app.BaseApp, android.app.Application
    public void onCreate() {
        super.onCreate();
        ARouter.openLog();
        ARouter.openDebug();
        ARouter.init(this);
        CrashReport.initCrashReport(getApplicationContext(), "a0a2afe900", true);
    }
}
