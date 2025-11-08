package com.alibaba.android.arouter.facade.template;

import java.util.Map;

/* loaded from: classes.dex */
public interface IInterceptorGroup {
    void loadInto(Map<Integer, Class<? extends IInterceptor>> map);
}
