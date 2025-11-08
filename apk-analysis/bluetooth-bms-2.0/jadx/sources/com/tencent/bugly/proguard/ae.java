package com.tencent.bugly.proguard;

import android.content.Context;
import android.text.TextUtils;
import com.tencent.bugly.crashreport.biz.UserInfoBean;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class ae {
    public static bu a(UserInfoBean userInfoBean) {
        if (userInfoBean == null) {
            return null;
        }
        bu buVar = new bu();
        buVar.a = userInfoBean.e;
        buVar.e = userInfoBean.j;
        buVar.d = userInfoBean.c;
        buVar.c = userInfoBean.d;
        buVar.h = userInfoBean.o == 1;
        int i = userInfoBean.b;
        if (i == 1) {
            buVar.b = (byte) 1;
        } else if (i == 2) {
            buVar.b = (byte) 4;
        } else if (i == 3) {
            buVar.b = (byte) 2;
        } else if (i == 4) {
            buVar.b = (byte) 3;
        } else if (i == 8) {
            buVar.b = (byte) 8;
        } else {
            if (userInfoBean.b < 10 || userInfoBean.b >= 20) {
                al.e("unknown uinfo type %d ", Integer.valueOf(userInfoBean.b));
                return null;
            }
            buVar.b = (byte) userInfoBean.b;
        }
        buVar.f = new HashMap();
        if (userInfoBean.p >= 0) {
            Map<String, String> map = buVar.f;
            StringBuilder sb = new StringBuilder();
            sb.append(userInfoBean.p);
            map.put("C01", sb.toString());
        }
        if (userInfoBean.q >= 0) {
            Map<String, String> map2 = buVar.f;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(userInfoBean.q);
            map2.put("C02", sb2.toString());
        }
        if (userInfoBean.r != null && userInfoBean.r.size() > 0) {
            for (Map.Entry<String, String> entry : userInfoBean.r.entrySet()) {
                buVar.f.put("C03_" + entry.getKey(), entry.getValue());
            }
        }
        if (userInfoBean.s != null && userInfoBean.s.size() > 0) {
            for (Map.Entry<String, String> entry2 : userInfoBean.s.entrySet()) {
                buVar.f.put("C04_" + entry2.getKey(), entry2.getValue());
            }
        }
        Map<String, String> map3 = buVar.f;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(!userInfoBean.l);
        map3.put("A36", sb3.toString());
        Map<String, String> map4 = buVar.f;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(userInfoBean.g);
        map4.put("F02", sb4.toString());
        Map<String, String> map5 = buVar.f;
        StringBuilder sb5 = new StringBuilder();
        sb5.append(userInfoBean.h);
        map5.put("F03", sb5.toString());
        buVar.f.put("F04", userInfoBean.j);
        Map<String, String> map6 = buVar.f;
        StringBuilder sb6 = new StringBuilder();
        sb6.append(userInfoBean.i);
        map6.put("F05", sb6.toString());
        buVar.f.put("F06", userInfoBean.m);
        Map<String, String> map7 = buVar.f;
        StringBuilder sb7 = new StringBuilder();
        sb7.append(userInfoBean.k);
        map7.put("F10", sb7.toString());
        al.c("summary type %d vm:%d", Byte.valueOf(buVar.b), Integer.valueOf(buVar.f.size()));
        return buVar;
    }

    public static <T extends m> T a(byte[] bArr, Class<T> cls) {
        if (bArr != null && bArr.length > 0) {
            try {
                T tNewInstance = cls.newInstance();
                k kVar = new k(bArr);
                kVar.a("utf-8");
                tNewInstance.a(kVar);
                return tNewInstance;
            } catch (Throwable th) {
                if (!al.b(th)) {
                    th.printStackTrace();
                }
            }
        }
        return null;
    }

    public static bq a(Context context, int i, byte[] bArr) {
        aa aaVarB = aa.b();
        StrategyBean strategyBeanC = ac.a().c();
        if (aaVarB == null || strategyBeanC == null) {
            al.e("Can not create request pkg for parameters is invalid.", new Object[0]);
            return null;
        }
        try {
            bq bqVar = new bq();
            synchronized (aaVarB) {
                bqVar.a = aaVarB.b;
                bqVar.b = aaVarB.e();
                bqVar.c = aaVarB.c;
                bqVar.d = aaVarB.o;
                bqVar.e = aaVarB.s;
                bqVar.f = aaVarB.h;
                bqVar.g = i;
                if (bArr == null) {
                    bArr = "".getBytes();
                }
                bqVar.h = bArr;
                bqVar.i = aaVarB.h();
                bqVar.j = aaVarB.k;
                bqVar.k = new HashMap();
                bqVar.l = aaVarB.d();
                bqVar.m = strategyBeanC.o;
                bqVar.o = aaVarB.g();
                bqVar.p = ab.c(context);
                bqVar.q = System.currentTimeMillis();
                bqVar.s = aaVarB.i();
                bqVar.v = aaVarB.g();
                bqVar.w = bqVar.p;
                aaVarB.getClass();
                bqVar.n = "com.tencent.bugly";
                bqVar.k.put("A26", aaVarB.s());
                Map<String, String> map = bqVar.k;
                StringBuilder sb = new StringBuilder();
                sb.append(aa.C());
                map.put("A62", sb.toString());
                Map<String, String> map2 = bqVar.k;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(aa.D());
                map2.put("A63", sb2.toString());
                Map<String, String> map3 = bqVar.k;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(aaVarB.J);
                map3.put("F11", sb3.toString());
                Map<String, String> map4 = bqVar.k;
                StringBuilder sb4 = new StringBuilder();
                sb4.append(aaVarB.I);
                map4.put("F12", sb4.toString());
                bqVar.k.put("D3", aaVarB.q);
                if (p.b != null) {
                    for (o oVar : p.b) {
                        if (oVar.versionKey != null && oVar.version != null) {
                            bqVar.k.put(oVar.versionKey, oVar.version);
                        }
                    }
                }
                bqVar.k.put("G15", ap.d("G15", ""));
                bqVar.k.put("G10", ap.d("G10", ""));
                bqVar.k.put("D4", ap.d("D4", "0"));
            }
            Map<String, String> mapX = aaVarB.x();
            if (mapX != null) {
                for (Map.Entry<String, String> entry : mapX.entrySet()) {
                    if (!TextUtils.isEmpty(entry.getValue())) {
                        bqVar.k.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            return bqVar;
        } catch (Throwable th) {
            if (!al.b(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public static byte[] a(Object obj) {
        try {
            e eVar = new e();
            eVar.b();
            eVar.a("utf-8");
            eVar.c();
            eVar.b("RqdServer");
            eVar.c("sync");
            eVar.a("detail", (String) obj);
            return eVar.a();
        } catch (Throwable th) {
            if (al.b(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    public static br a(byte[] bArr) {
        if (bArr != null) {
            try {
                e eVar = new e();
                eVar.b();
                eVar.a("utf-8");
                eVar.a(bArr);
                Object objB = eVar.b("detail", new br());
                if (br.class.isInstance(objB)) {
                    return (br) br.class.cast(objB);
                }
                return null;
            } catch (Throwable th) {
                if (!al.b(th)) {
                    th.printStackTrace();
                }
            }
        }
        return null;
    }

    public static byte[] a(m mVar) {
        try {
            l lVar = new l();
            lVar.a("utf-8");
            mVar.a(lVar);
            byte[] bArr = new byte[lVar.a.position()];
            System.arraycopy(lVar.a.array(), 0, bArr, 0, lVar.a.position());
            return bArr;
        } catch (Throwable th) {
            if (al.b(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }
}
