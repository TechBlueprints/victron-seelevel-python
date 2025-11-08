package com.tencent.bugly.proguard;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Pair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class ag {
    private final SimpleDateFormat a;
    private final ad b;

    /* synthetic */ ag(byte b2) {
        this();
    }

    private ag() {
        this.a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.US);
        this.b = new ad();
    }

    public final void a(List<c> list) {
        if (list == null || list.isEmpty()) {
            al.d("sla batch report event is null", new Object[0]);
            return;
        }
        al.c("sla batch report event size:%s", Integer.valueOf(list.size()));
        ArrayList arrayList = new ArrayList();
        Iterator<c> it = list.iterator();
        while (it.hasNext()) {
            b bVarB = b(it.next());
            if (bVarB != null) {
                arrayList.add(bVarB);
            }
        }
        e(arrayList);
        b(arrayList);
    }

    public final void b(final List<b> list) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            ak.a().a(new Runnable() { // from class: com.tencent.bugly.proguard.ag.1
                @Override // java.lang.Runnable
                public final void run() {
                    ag.c(list);
                }
            });
        } else {
            c(list);
        }
    }

    static void c(List<b> list) {
        if (list == null || list.isEmpty()) {
            al.c("sla batch report data is empty", new Object[0]);
            return;
        }
        al.c("sla batch report list size:%s", Integer.valueOf(list.size()));
        if (list.size() > 30) {
            list = list.subList(0, 29);
        }
        ArrayList arrayList = new ArrayList();
        Iterator<b> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().c);
        }
        Pair<Integer, String> pairA = ad.a(arrayList);
        al.c("sla batch report result, rspCode:%s rspMsg:%s", pairA.first, pairA.second);
        if (((Integer) pairA.first).intValue() == 200) {
            d(list);
        }
    }

    private static void e(List<b> list) {
        for (b bVar : list) {
            al.c("sla save id:%s time:%s msg:%s", bVar.a, Long.valueOf(bVar.b), bVar.c);
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", bVar.a);
                contentValues.put("_tm", Long.valueOf(bVar.b));
                contentValues.put("_dt", bVar.c);
                w.a().a("t_sla", contentValues, (v) null);
            } catch (Throwable th) {
                al.b(th);
            }
        }
    }

    public static void d(List<b> list) {
        if (list == null || list.isEmpty()) {
            al.c("sla batch delete list is null", new Object[0]);
            return;
        }
        al.c("sla batch delete list size:%s", Integer.valueOf(list.size()));
        try {
            String str = "_id in (" + a(",", list) + ")";
            al.c("sla batch delete where:%s", str);
            w.a().a("t_sla", str);
        } catch (Throwable th) {
            al.b(th);
        }
    }

    private static String a(String str, Iterable<b> iterable) {
        Iterator<b> it = iterable.iterator();
        if (!it.hasNext()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        sb.append(it.next().a);
        sb.append("'");
        while (it.hasNext()) {
            sb.append(str);
            sb.append("'");
            sb.append(it.next().a);
            sb.append("'");
        }
        return sb.toString();
    }

    public static List<b> a() {
        Cursor cursorA = w.a().a("t_sla", new String[]{"_id", "_tm", "_dt"}, (String) null, "_tm", "30");
        if (cursorA == null) {
            return null;
        }
        if (cursorA.getCount() <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        while (cursorA.moveToNext()) {
            try {
                b bVar = new b();
                bVar.a = cursorA.getString(cursorA.getColumnIndex("_id"));
                bVar.b = cursorA.getLong(cursorA.getColumnIndex("_tm"));
                bVar.c = cursorA.getString(cursorA.getColumnIndex("_dt"));
                al.c(bVar.toString(), new Object[0]);
                arrayList.add(bVar);
            } finally {
                try {
                    return arrayList;
                } finally {
                }
            }
        }
        return arrayList;
    }

    /* compiled from: BUGLY */
    public static class c {
        String a;
        String b;
        long c;
        boolean d;
        long e;
        String f;
        String g;

        public c(String str, String str2, long j, boolean z, long j2, String str3, String str4) {
            this.a = str;
            this.b = str2;
            this.c = j;
            this.d = z;
            this.e = j2;
            this.f = str3;
            this.g = str4;
        }

        public c() {
        }
    }

    /* compiled from: BUGLY */
    public static class b {
        String a;
        public long b;
        public String c;

        public final String toString() {
            return "SLAData{uuid='" + this.a + "', time=" + this.b + ", data='" + this.c + "'}";
        }
    }

    /* compiled from: BUGLY */
    public static class a {
        private static final ag a = new ag(0);
    }

    public final void a(c cVar) {
        if (TextUtils.isEmpty(cVar.b)) {
            al.d("sla report event is null", new Object[0]);
        } else {
            al.c("sla report single event", new Object[0]);
            a(Collections.singletonList(cVar));
        }
    }

    private b b(c cVar) {
        if (cVar == null || TextUtils.isEmpty(cVar.b)) {
            al.d("sla convert event is null", new Object[0]);
            return null;
        }
        aa aaVarB = aa.b();
        if (aaVarB == null) {
            al.d("sla convert failed because ComInfoManager is null", new Object[0]);
            return null;
        }
        StringBuilder sb = new StringBuilder("&app_version=");
        sb.append(aaVarB.o);
        sb.append("&app_name=");
        sb.append(aaVarB.q);
        sb.append("&app_bundle_id=");
        sb.append(aaVarB.c);
        sb.append("&client_type=android&user_id=");
        sb.append(aaVarB.f());
        sb.append("&sdk_version=");
        sb.append(aaVarB.h);
        sb.append("&event_code=");
        sb.append(cVar.b);
        sb.append("&event_result=");
        sb.append(cVar.d ? 1 : 0);
        sb.append("&event_time=");
        sb.append(this.a.format(new Date(cVar.c)));
        sb.append("&event_cost=");
        sb.append(cVar.e);
        sb.append("&device_id=");
        sb.append(aaVarB.g());
        sb.append("&debug=");
        sb.append(aaVarB.D ? 1 : 0);
        sb.append("&param_0=");
        sb.append(cVar.f);
        sb.append("&param_1=");
        sb.append(cVar.a);
        sb.append("&param_2=");
        sb.append(aaVarB.M ? "rqd" : "ext");
        sb.append("&param_4=");
        sb.append(aaVarB.e());
        String string = sb.toString();
        if (!TextUtils.isEmpty(cVar.g)) {
            string = string + "&param_3=" + cVar.g;
        }
        al.c("sla convert eventId:%s eventType:%s, eventTime:%s success:%s cost:%s from:%s uploadMsg:", cVar.a, cVar.b, Long.valueOf(cVar.c), Boolean.valueOf(cVar.d), Long.valueOf(cVar.e), cVar.f, cVar.g);
        String str = cVar.a + "-" + cVar.b;
        b bVar = new b();
        bVar.a = str;
        bVar.b = cVar.c;
        bVar.c = string;
        return bVar;
    }
}
