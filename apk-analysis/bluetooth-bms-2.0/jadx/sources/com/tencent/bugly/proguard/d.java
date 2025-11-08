package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class d extends c {
    protected HashMap<String, byte[]> e = null;
    private HashMap<String, Object> g = new HashMap<>();
    k f = new k();

    @Override // com.tencent.bugly.proguard.c
    public final /* bridge */ /* synthetic */ void a(String str) {
        super.a(str);
    }

    public void b() {
        this.e = new HashMap<>();
    }

    @Override // com.tencent.bugly.proguard.c
    public <T> void a(String str, T t) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, UnsupportedEncodingException {
        if (this.e == null) {
            super.a(str, (String) t);
            return;
        }
        if (str == null) {
            throw new IllegalArgumentException("put key can not is null");
        }
        if (t == null) {
            throw new IllegalArgumentException("put value can not is null");
        }
        if (t instanceof Set) {
            throw new IllegalArgumentException("can not support Set");
        }
        l lVar = new l();
        lVar.a(this.c);
        lVar.a(t, 0);
        this.e.put(str, n.a(lVar.a));
    }

    public final <T> T b(String str, T t) throws b {
        HashMap<String, byte[]> map = this.e;
        if (map != null) {
            if (!map.containsKey(str)) {
                return null;
            }
            if (this.g.containsKey(str)) {
                return (T) this.g.get(str);
            }
            try {
                this.f.a(this.e.get(str));
                this.f.a(this.c);
                T t2 = (T) this.f.a((k) t, 0, true);
                if (t2 != null) {
                    c(str, t2);
                }
                return t2;
            } catch (Exception e) {
                throw new b(e);
            }
        }
        if (!this.a.containsKey(str)) {
            return null;
        }
        if (this.g.containsKey(str)) {
            return (T) this.g.get(str);
        }
        byte[] value = new byte[0];
        Iterator<Map.Entry<String, byte[]>> it = this.a.get(str).entrySet().iterator();
        if (it.hasNext()) {
            Map.Entry<String, byte[]> next = it.next();
            next.getKey();
            value = next.getValue();
        }
        try {
            this.f.a(value);
            this.f.a(this.c);
            T t3 = (T) this.f.a((k) t, 0, true);
            c(str, t3);
            return t3;
        } catch (Exception e2) {
            throw new b(e2);
        }
    }

    private void c(String str, Object obj) {
        this.g.put(str, obj);
    }

    @Override // com.tencent.bugly.proguard.c
    public byte[] a() throws UnsupportedEncodingException {
        if (this.e != null) {
            l lVar = new l(0);
            lVar.a(this.c);
            lVar.a((Map) this.e, 0);
            return n.a(lVar.a);
        }
        return super.a();
    }

    @Override // com.tencent.bugly.proguard.c
    public void a(byte[] bArr) {
        try {
            super.a(bArr);
        } catch (Exception unused) {
            this.f.a(bArr);
            this.f.a(this.c);
            HashMap map = new HashMap(1);
            map.put("", new byte[0]);
            this.e = this.f.a((Map) map, 0, false);
        }
    }
}
