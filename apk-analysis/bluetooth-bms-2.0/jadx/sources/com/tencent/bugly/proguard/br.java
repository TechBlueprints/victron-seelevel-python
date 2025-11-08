package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class br extends m {
    static byte[] i = {0};
    static Map<String, String> j;
    public byte a = 0;
    public int b = 0;
    public byte[] c = null;
    public String d = "";
    public long e = 0;
    public String f = "";
    public String g = "";
    public Map<String, String> h = null;

    @Override // com.tencent.bugly.proguard.m
    public final void a(l lVar) throws UnsupportedEncodingException {
        lVar.a(this.a, 0);
        lVar.a(this.b, 1);
        byte[] bArr = this.c;
        if (bArr != null) {
            lVar.a(bArr, 2);
        }
        String str = this.d;
        if (str != null) {
            lVar.a(str, 3);
        }
        lVar.a(this.e, 4);
        String str2 = this.f;
        if (str2 != null) {
            lVar.a(str2, 5);
        }
        String str3 = this.g;
        if (str3 != null) {
            lVar.a(str3, 6);
        }
        Map<String, String> map = this.h;
        if (map != null) {
            lVar.a((Map) map, 7);
        }
    }

    static {
        HashMap map = new HashMap();
        j = map;
        map.put("", "");
    }

    @Override // com.tencent.bugly.proguard.m
    public final void a(k kVar) {
        this.a = kVar.a(this.a, 0, true);
        this.b = kVar.a(this.b, 1, true);
        this.c = kVar.c(2, false);
        this.d = kVar.b(3, false);
        this.e = kVar.a(this.e, 4, false);
        this.f = kVar.b(5, false);
        this.g = kVar.b(6, false);
        this.h = (Map) kVar.a((k) j, 7, false);
    }
}
