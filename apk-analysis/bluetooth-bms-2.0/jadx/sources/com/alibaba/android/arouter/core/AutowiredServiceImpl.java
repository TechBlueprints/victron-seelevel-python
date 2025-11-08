package com.alibaba.android.arouter.core;

import android.content.Context;
import android.util.LruCache;
import com.alibaba.android.arouter.facade.service.AutowiredService;
import com.alibaba.android.arouter.facade.template.ISyringe;
import com.alibaba.android.arouter.utils.Consts;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class AutowiredServiceImpl implements AutowiredService {
    private List<String> blackList;
    private LruCache<String, ISyringe> classCache;

    @Override // com.alibaba.android.arouter.facade.template.IProvider
    public void init(Context context) {
        this.classCache = new LruCache<>(66);
        this.blackList = new ArrayList();
    }

    @Override // com.alibaba.android.arouter.facade.service.AutowiredService
    public void autowire(Object obj) {
        String name = obj.getClass().getName();
        try {
            if (this.blackList.contains(name)) {
                return;
            }
            ISyringe iSyringe = this.classCache.get(name);
            if (iSyringe == null) {
                iSyringe = (ISyringe) Class.forName(obj.getClass().getName() + Consts.SUFFIX_AUTOWIRED).getConstructor(new Class[0]).newInstance(new Object[0]);
            }
            iSyringe.inject(obj);
            this.classCache.put(name, iSyringe);
        } catch (Exception unused) {
            this.blackList.add(name);
        }
    }
}
