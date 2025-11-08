package com.tencent.bugly.proguard;

import android.content.Context;
import android.util.Log;
import com.tencent.bugly.BuglyStrategy;
import java.util.ArrayList;
import java.util.List;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class p {
    public static boolean a = true;
    public static List<o> b = new ArrayList();
    public static boolean c;
    private static w d;
    private static boolean e;

    private static boolean a(aa aaVar) {
        List<String> list = aaVar.v;
        aaVar.getClass();
        return list != null && list.contains("bugly");
    }

    public static synchronized void a(Context context) {
        a(context, null);
    }

    public static synchronized void a(Context context, BuglyStrategy buglyStrategy) {
        if (e) {
            al.d("[init] initial Multi-times, ignore this.", new Object[0]);
            return;
        }
        if (context == null) {
            Log.w(al.b, "[init] context of init() is null, check it.");
            return;
        }
        aa aaVarA = aa.a(context);
        if (a(aaVarA)) {
            a = false;
            return;
        }
        String strE = aaVarA.e();
        if (strE == null) {
            Log.e(al.b, "[init] meta data of BUGLY_APPID in AndroidManifest.xml should be set.");
        } else {
            a(context, strE, aaVarA.D, buglyStrategy);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:83:0x021a A[Catch: all -> 0x0238, TryCatch #2 {, blocks: (B:4:0x0003, B:6:0x0008, B:10:0x0013, B:14:0x001e, B:18:0x0028, B:20:0x002c, B:21:0x0068, B:23:0x00a9, B:26:0x00ad, B:28:0x00bf, B:30:0x00cd, B:32:0x00d3, B:33:0x00e9, B:34:0x00f8, B:36:0x0100, B:38:0x010a, B:40:0x0110, B:41:0x0126, B:47:0x014e, B:53:0x0162, B:55:0x016c, B:57:0x0172, B:58:0x0188, B:59:0x0197, B:61:0x019d, B:63:0x01a3, B:64:0x01b9, B:65:0x01c5, B:67:0x01cb, B:68:0x01d7, B:42:0x0134, B:44:0x013d, B:46:0x0147, B:50:0x015b, B:52:0x015f, B:70:0x01e4, B:80:0x0212, B:81:0x0215, B:83:0x021a, B:85:0x0221, B:77:0x0209, B:79:0x020f, B:72:0x01ec, B:74:0x01fc), top: B:95:0x0003, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x021f  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x01ec A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static synchronized void a(android.content.Context r12, java.lang.String r13, boolean r14, com.tencent.bugly.BuglyStrategy r15) {
        /*
            Method dump skipped, instructions count: 571
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.p.a(android.content.Context, java.lang.String, boolean, com.tencent.bugly.BuglyStrategy):void");
    }

    public static synchronized void a(o oVar) {
        if (!b.contains(oVar)) {
            b.add(oVar);
        }
    }
}
