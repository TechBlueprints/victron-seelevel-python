package com.alibaba.android.arouter.facade.callback;

import com.alibaba.android.arouter.facade.Postcard;

/* loaded from: classes.dex */
public interface InterceptorCallback {
    void onContinue(Postcard postcard);

    void onInterrupt(Throwable th);
}
