package com.tencent.bugly.proguard;

import androidx.core.app.NotificationCompat;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class g extends m {
    public byte[] g;
    public Map<String, String> i;
    public Map<String, String> j;
    static final /* synthetic */ boolean m = !g.class.desiredAssertionStatus();
    static byte[] k = null;
    static Map<String, String> l = null;
    public short a = 0;
    public byte b = 0;
    public int c = 0;
    public int d = 0;
    public String e = null;
    public String f = null;
    public int h = 0;

    public final boolean equals(Object obj) {
        g gVar = (g) obj;
        return n.a(1, (int) gVar.a) && n.a(1, (int) gVar.b) && n.a(1, gVar.c) && n.a(1, gVar.d) && n.a((Object) 1, (Object) gVar.e) && n.a((Object) 1, (Object) gVar.f) && n.a((Object) 1, (Object) gVar.g) && n.a(1, gVar.h) && n.a((Object) 1, (Object) gVar.i) && n.a((Object) 1, (Object) gVar.j);
    }

    public final Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException unused) {
            if (m) {
                return null;
            }
            throw new AssertionError();
        }
    }

    @Override // com.tencent.bugly.proguard.m
    public final void a(l lVar) throws UnsupportedEncodingException {
        lVar.a(this.a, 1);
        lVar.a(this.b, 2);
        lVar.a(this.c, 3);
        lVar.a(this.d, 4);
        lVar.a(this.e, 5);
        lVar.a(this.f, 6);
        lVar.a(this.g, 7);
        lVar.a(this.h, 8);
        lVar.a((Map) this.i, 9);
        lVar.a((Map) this.j, 10);
    }

    @Override // com.tencent.bugly.proguard.m
    public final void a(k kVar) {
        try {
            this.a = kVar.a(this.a, 1, true);
            this.b = kVar.a(this.b, 2, true);
            this.c = kVar.a(this.c, 3, true);
            this.d = kVar.a(this.d, 4, true);
            this.e = kVar.b(5, true);
            this.f = kVar.b(6, true);
            if (k == null) {
                k = new byte[]{0};
            }
            this.g = kVar.c(7, true);
            this.h = kVar.a(this.h, 8, true);
            if (l == null) {
                HashMap map = new HashMap();
                l = map;
                map.put("", "");
            }
            this.i = (Map) kVar.a((k) l, 9, true);
            if (l == null) {
                HashMap map2 = new HashMap();
                l = map2;
                map2.put("", "");
            }
            this.j = (Map) kVar.a((k) l, 10, true);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("RequestPacket decode error " + f.a(this.g));
            throw new RuntimeException(e);
        }
    }

    @Override // com.tencent.bugly.proguard.m
    public final void a(StringBuilder sb, int i) {
        i iVar = new i(sb, i);
        iVar.a(this.a, "iVersion");
        iVar.a(this.b, "cPacketType");
        iVar.a(this.c, "iMessageType");
        iVar.a(this.d, "iRequestId");
        iVar.a(this.e, "sServantName");
        iVar.a(this.f, "sFuncName");
        iVar.a(this.g, "sBuffer");
        iVar.a(this.h, "iTimeout");
        iVar.a((Map) this.i, "context");
        iVar.a((Map) this.j, NotificationCompat.CATEGORY_STATUS);
    }
}
