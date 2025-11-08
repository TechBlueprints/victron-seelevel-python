package com.tencent.bugly.proguard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Pair;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.common.info.PlugInBean;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.proguard.ag;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class as {
    public static int a;
    private static final Map<Integer, Pair<String, String>> h = new HashMap<Integer, Pair<String, String>>() { // from class: com.tencent.bugly.proguard.as.1
        {
            put(3, new Pair("203", "103"));
            put(7, new Pair("208", "108"));
            put(0, new Pair("200", "100"));
            put(1, new Pair("201", "101"));
            put(2, new Pair("202", "102"));
            put(4, new Pair("204", "104"));
            put(6, new Pair("206", "106"));
            put(5, new Pair("207", "107"));
        }
    };
    private static final ArrayList<a> i = new ArrayList<a>() { // from class: com.tencent.bugly.proguard.as.2
        {
            byte b2 = 0;
            add(new b(b2));
            add(new c(b2));
            add(new d(b2));
            add(new e(b2));
            add(new h(b2));
            add(new i(b2));
            add(new f(b2));
            add(new g(b2));
        }
    };
    private static final Map<Integer, Integer> j = new HashMap<Integer, Integer>() { // from class: com.tencent.bugly.proguard.as.3
        {
            put(3, 4);
            put(7, 7);
            put(2, 1);
            put(0, 0);
            put(1, 2);
            put(4, 3);
            put(5, 5);
            put(6, 6);
        }
    };
    private static final Map<Integer, String> k = new HashMap<Integer, String>() { // from class: com.tencent.bugly.proguard.as.4
        {
            put(3, "BuglyAnrCrash");
            put(0, "BuglyJavaCrash");
            put(1, "BuglyNativeCrash");
        }
    };
    private static final Map<Integer, String> l = new HashMap<Integer, String>() { // from class: com.tencent.bugly.proguard.as.5
        {
            put(3, "BuglyAnrCrashReport");
            put(0, "BuglyJavaCrashReport");
            put(1, "BuglyNativeCrashReport");
        }
    };
    protected final Context b;
    protected final ai c;
    protected final w d;
    protected final ac e;
    protected aw f;
    protected BuglyStrategy.a g;

    public as(Context context, ai aiVar, w wVar, ac acVar, BuglyStrategy.a aVar) {
        a = 1004;
        this.b = context;
        this.c = aiVar;
        this.d = wVar;
        this.e = acVar;
        this.g = aVar;
        this.f = null;
    }

    private static List<ar> a(List<ar> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        ArrayList arrayList = new ArrayList();
        for (ar arVar : list) {
            if (arVar.d && arVar.b <= jCurrentTimeMillis - 86400000) {
                arrayList.add(arVar);
            }
        }
        return arrayList;
    }

    private static CrashDetailBean a(List<ar> list, CrashDetailBean crashDetailBean) {
        List<CrashDetailBean> listC;
        if (list.isEmpty()) {
            return crashDetailBean;
        }
        CrashDetailBean crashDetailBean2 = null;
        ArrayList arrayList = new ArrayList(10);
        for (ar arVar : list) {
            if (arVar.e) {
                arrayList.add(arVar);
            }
        }
        if (!arrayList.isEmpty() && (listC = c(arrayList)) != null && !listC.isEmpty()) {
            Collections.sort(listC);
            crashDetailBean2 = listC.get(0);
            a(crashDetailBean2, listC);
        }
        if (crashDetailBean2 == null) {
            crashDetailBean.j = true;
            crashDetailBean.t = 0;
            crashDetailBean.s = "";
            crashDetailBean2 = crashDetailBean;
        }
        b(crashDetailBean2, list);
        if (crashDetailBean2.r != crashDetailBean.r) {
            String str = crashDetailBean2.s;
            StringBuilder sb = new StringBuilder();
            sb.append(crashDetailBean.r);
            if (!str.contains(sb.toString())) {
                crashDetailBean2.t++;
                crashDetailBean2.s += crashDetailBean.r + "\n";
            }
        }
        return crashDetailBean2;
    }

    private static void a(CrashDetailBean crashDetailBean, List<CrashDetailBean> list) {
        String[] strArrSplit;
        StringBuilder sb = new StringBuilder(128);
        for (int i2 = 1; i2 < list.size(); i2++) {
            CrashDetailBean crashDetailBean2 = list.get(i2);
            if (crashDetailBean2.s != null && (strArrSplit = crashDetailBean2.s.split("\n")) != null) {
                for (String str : strArrSplit) {
                    if (!crashDetailBean.s.contains(str)) {
                        crashDetailBean.t++;
                        sb.append(str);
                        sb.append("\n");
                    }
                }
            }
        }
        crashDetailBean.s += sb.toString();
    }

    private static void b(CrashDetailBean crashDetailBean, List<ar> list) {
        StringBuilder sb = new StringBuilder(64);
        for (ar arVar : list) {
            if (!arVar.e && !arVar.d) {
                String str = crashDetailBean.s;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(arVar.b);
                if (!str.contains(sb2.toString())) {
                    crashDetailBean.t++;
                    sb.append(arVar.b);
                    sb.append("\n");
                }
            }
        }
        crashDetailBean.s += sb.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00c6  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00d5 A[Catch: all -> 0x0150, TryCatch #0 {all -> 0x0150, blocks: (B:25:0x0050, B:27:0x005e, B:28:0x0075, B:30:0x008c, B:32:0x00a2, B:37:0x00c7, B:39:0x00d5, B:44:0x00fc, B:50:0x0135, B:52:0x0139, B:54:0x0148, B:45:0x0104, B:47:0x010a, B:49:0x0127, B:48:0x010d, B:40:0x00e3, B:42:0x00e7, B:33:0x00ae, B:35:0x00b2), top: B:101:0x0050 }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00e3 A[Catch: all -> 0x0150, TryCatch #0 {all -> 0x0150, blocks: (B:25:0x0050, B:27:0x005e, B:28:0x0075, B:30:0x008c, B:32:0x00a2, B:37:0x00c7, B:39:0x00d5, B:44:0x00fc, B:50:0x0135, B:52:0x0139, B:54:0x0148, B:45:0x0104, B:47:0x010a, B:49:0x0127, B:48:0x010d, B:40:0x00e3, B:42:0x00e7, B:33:0x00ae, B:35:0x00b2), top: B:101:0x0050 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00fc A[Catch: all -> 0x0150, TryCatch #0 {all -> 0x0150, blocks: (B:25:0x0050, B:27:0x005e, B:28:0x0075, B:30:0x008c, B:32:0x00a2, B:37:0x00c7, B:39:0x00d5, B:44:0x00fc, B:50:0x0135, B:52:0x0139, B:54:0x0148, B:45:0x0104, B:47:0x010a, B:49:0x0127, B:48:0x010d, B:40:0x00e3, B:42:0x00e7, B:33:0x00ae, B:35:0x00b2), top: B:101:0x0050 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0104 A[Catch: all -> 0x0150, TryCatch #0 {all -> 0x0150, blocks: (B:25:0x0050, B:27:0x005e, B:28:0x0075, B:30:0x008c, B:32:0x00a2, B:37:0x00c7, B:39:0x00d5, B:44:0x00fc, B:50:0x0135, B:52:0x0139, B:54:0x0148, B:45:0x0104, B:47:0x010a, B:49:0x0127, B:48:0x010d, B:40:0x00e3, B:42:0x00e7, B:33:0x00ae, B:35:0x00b2), top: B:101:0x0050 }] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0139 A[Catch: all -> 0x0150, TryCatch #0 {all -> 0x0150, blocks: (B:25:0x0050, B:27:0x005e, B:28:0x0075, B:30:0x008c, B:32:0x00a2, B:37:0x00c7, B:39:0x00d5, B:44:0x00fc, B:50:0x0135, B:52:0x0139, B:54:0x0148, B:45:0x0104, B:47:0x010a, B:49:0x0127, B:48:0x010d, B:40:0x00e3, B:42:0x00e7, B:33:0x00ae, B:35:0x00b2), top: B:101:0x0050 }] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x01a5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean a(com.tencent.bugly.crashreport.crash.CrashDetailBean r20, boolean r21) {
        /*
            Method dump skipped, instructions count: 653
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.as.a(com.tencent.bugly.crashreport.crash.CrashDetailBean, boolean):boolean");
    }

    private static boolean a(String str) {
        if (at.r != null && !at.r.isEmpty()) {
            try {
                al.c("Crash regular filter for crash stack is: %s", at.r);
                if (Pattern.compile(at.r).matcher(str).find()) {
                    al.d("This crash matches the regular filter string set. It will not be record and upload.", new Object[0]);
                    return true;
                }
            } catch (Exception e2) {
                al.a(e2);
                al.d("Failed to compile " + at.r, new Object[0]);
            }
        }
        return false;
    }

    private static boolean a(CrashDetailBean crashDetailBean, List<ar> list, List<ar> list2) {
        boolean z = false;
        for (ar arVar : list) {
            if (crashDetailBean.u.equals(arVar.c)) {
                if (arVar.e) {
                    z = true;
                }
                list2.add(arVar);
            }
        }
        return z;
    }

    public static List<CrashDetailBean> a() {
        StrategyBean strategyBeanC = ac.a().c();
        if (strategyBeanC == null) {
            al.d("have not synced remote!", new Object[0]);
            return null;
        }
        if (!strategyBeanC.f) {
            al.d("Crashreport remote closed, please check your APP ID correct and Version available, then uninstall and reinstall your app.", new Object[0]);
            al.b("[init] WARNING! Crashreport closed by server, please check your APP ID correct and Version available, then uninstall and reinstall your app.", new Object[0]);
            return null;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        long jB = ap.b();
        List<ar> listB = b();
        al.c("Size of crash list loaded from DB: %s", Integer.valueOf(listB.size()));
        if (listB == null || listB.size() <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList.addAll(a(listB));
        listB.removeAll(arrayList);
        Iterator<ar> it = listB.iterator();
        while (it.hasNext()) {
            ar next = it.next();
            if (next.b < jB - at.j) {
                arrayList2.add(next);
                it.remove();
                arrayList.add(next);
            } else if (next.d) {
                if (next.b >= jCurrentTimeMillis - 86400000) {
                    it.remove();
                } else if (!next.e) {
                    it.remove();
                    arrayList.add(next);
                }
            } else if (next.f >= 3 && next.b < jCurrentTimeMillis - 86400000) {
                it.remove();
                arrayList.add(next);
            }
        }
        b(arrayList2);
        if (arrayList.size() > 0) {
            d(arrayList);
        }
        ArrayList arrayList3 = new ArrayList();
        List<CrashDetailBean> listC = c(listB);
        if (listC != null && listC.size() > 0) {
            String str = aa.b().o;
            Iterator<CrashDetailBean> it2 = listC.iterator();
            while (it2.hasNext()) {
                CrashDetailBean next2 = it2.next();
                if (!str.equals(next2.f)) {
                    it2.remove();
                    arrayList3.add(next2);
                }
            }
        }
        if (arrayList3.size() > 0) {
            e(arrayList3);
        }
        return listC;
    }

    private static void b(List<ar> list) {
        List<CrashDetailBean> listC = c(list);
        if (listC == null || listC.isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (CrashDetailBean crashDetailBean : listC) {
            String str = l.get(Integer.valueOf(crashDetailBean.b));
            if (!TextUtils.isEmpty(str)) {
                al.c("find expired data,crashId:%s eventType:%s", crashDetailBean.c, str);
                arrayList.add(new ag.c(crashDetailBean.c, str, crashDetailBean.r, false, 0L, "expired", null));
            }
        }
        ag.a.a.a(arrayList);
    }

    public final void a(CrashDetailBean crashDetailBean) {
        int i2 = crashDetailBean.b;
        if (i2 != 0) {
            if (i2 != 1) {
                if (i2 == 3 && !at.a().k()) {
                    return;
                }
            } else if (!at.a().j()) {
                return;
            }
        } else if (!at.a().j()) {
            return;
        }
        if (this.f != null) {
            al.c("Calling 'onCrashHandleEnd' of RQD crash listener.", new Object[0]);
        }
    }

    public final void b(CrashDetailBean crashDetailBean, boolean z) {
        if (at.o) {
            al.a("try to upload right now", new Object[0]);
            ArrayList arrayList = new ArrayList();
            arrayList.add(crashDetailBean);
            a(arrayList, 3000L, z, crashDetailBean.b == 7, z);
            return;
        }
        al.a("do not upload spot crash right now, crash would be uploaded when app next start", new Object[0]);
    }

    public final void a(final List<CrashDetailBean> list, long j2, final boolean z, boolean z2, boolean z3) {
        if (!aa.a(this.b).f) {
            al.d("warn: not upload process", new Object[0]);
            return;
        }
        ai aiVar = this.c;
        if (aiVar == null) {
            al.d("warn: upload manager is null", new Object[0]);
            return;
        }
        if (!z3 && !aiVar.b(at.a)) {
            al.d("warn: not crashHappen or not should upload", new Object[0]);
            return;
        }
        StrategyBean strategyBeanC = this.e.c();
        if (!strategyBeanC.f) {
            al.d("remote report is disable!", new Object[0]);
            al.b("[crash] server closed bugly in this app. please check your appid if is correct, and re-install it", new Object[0]);
            return;
        }
        if (list == null || list.size() == 0) {
            al.d("warn: crashList is null or crashList num is 0", new Object[0]);
            return;
        }
        try {
            String str = strategyBeanC.r;
            String str2 = StrategyBean.b;
            bp bpVarA = a(this.b, list, aa.b());
            if (bpVarA == null) {
                al.d("create eupPkg fail!", new Object[0]);
                return;
            }
            byte[] bArrA = ae.a((m) bpVarA);
            if (bArrA == null) {
                al.d("send encode fail!", new Object[0]);
                return;
            }
            bq bqVarA = ae.a(this.b, 830, bArrA);
            if (bqVarA == null) {
                al.d("request package is null.", new Object[0]);
                return;
            }
            final long jCurrentTimeMillis = System.currentTimeMillis();
            ah ahVar = new ah() { // from class: com.tencent.bugly.proguard.as.6
                @Override // com.tencent.bugly.proguard.ah
                public final void a(boolean z4, String str3) {
                    as.a(list, z4, System.currentTimeMillis() - jCurrentTimeMillis, z ? "realtime" : "cache", str3);
                    as.a(z4, (List<CrashDetailBean>) list);
                }
            };
            if (z) {
                this.c.a(a, bqVarA, str, str2, ahVar, j2, z2);
            } else {
                this.c.a(a, bqVarA, str, str2, ahVar, false);
            }
        } catch (Throwable th) {
            al.e("req cr error %s", th.toString());
            if (al.b(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    public static void a(boolean z, List<CrashDetailBean> list) {
        if (list != null && list.size() > 0) {
            al.c("up finish update state %b", Boolean.valueOf(z));
            for (CrashDetailBean crashDetailBean : list) {
                al.c("pre uid:%s uc:%d re:%b me:%b", crashDetailBean.c, Integer.valueOf(crashDetailBean.l), Boolean.valueOf(crashDetailBean.d), Boolean.valueOf(crashDetailBean.j));
                crashDetailBean.l++;
                crashDetailBean.d = z;
                al.c("set uid:%s uc:%d re:%b me:%b", crashDetailBean.c, Integer.valueOf(crashDetailBean.l), Boolean.valueOf(crashDetailBean.d), Boolean.valueOf(crashDetailBean.j));
            }
            Iterator<CrashDetailBean> it = list.iterator();
            while (it.hasNext()) {
                at.a().a(it.next());
            }
            al.c("update state size %d", Integer.valueOf(list.size()));
        }
        if (z) {
            return;
        }
        al.b("[crash] upload fail.", new Object[0]);
    }

    private static ContentValues c(CrashDetailBean crashDetailBean) {
        if (crashDetailBean == null) {
            return null;
        }
        try {
            ContentValues contentValues = new ContentValues();
            if (crashDetailBean.a > 0) {
                contentValues.put("_id", Long.valueOf(crashDetailBean.a));
            }
            contentValues.put("_tm", Long.valueOf(crashDetailBean.r));
            contentValues.put("_s1", crashDetailBean.u);
            int i2 = 1;
            contentValues.put("_up", Integer.valueOf(crashDetailBean.d ? 1 : 0));
            if (!crashDetailBean.j) {
                i2 = 0;
            }
            contentValues.put("_me", Integer.valueOf(i2));
            contentValues.put("_uc", Integer.valueOf(crashDetailBean.l));
            contentValues.put("_dt", ap.a(crashDetailBean));
            return contentValues;
        } catch (Throwable th) {
            if (!al.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    private static CrashDetailBean a(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            byte[] blob = cursor.getBlob(cursor.getColumnIndex("_dt"));
            if (blob == null) {
                return null;
            }
            long j2 = cursor.getLong(cursor.getColumnIndex("_id"));
            CrashDetailBean crashDetailBean = (CrashDetailBean) ap.a(blob, CrashDetailBean.CREATOR);
            if (crashDetailBean != null) {
                crashDetailBean.a = j2;
            }
            return crashDetailBean;
        } catch (Throwable th) {
            if (!al.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public final void b(CrashDetailBean crashDetailBean) {
        if (crashDetailBean == null) {
            return;
        }
        ContentValues contentValuesC = c(crashDetailBean);
        if (contentValuesC != null) {
            long jA = w.a().a("t_cr", contentValuesC, (v) null);
            if (jA >= 0) {
                al.c("insert %s success!", "t_cr");
                crashDetailBean.a = jA;
            }
        }
        if (at.l) {
            d(crashDetailBean);
        }
    }

    private static List<CrashDetailBean> c(List<ar> list) {
        Cursor cursorA;
        if (list == null || list.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("_id in (");
        Iterator<ar> it = list.iterator();
        while (it.hasNext()) {
            sb.append(it.next().a);
            sb.append(",");
        }
        if (sb.toString().contains(",")) {
            sb = new StringBuilder(sb.substring(0, sb.lastIndexOf(",")));
        }
        sb.append(")");
        String string = sb.toString();
        sb.setLength(0);
        try {
            cursorA = w.a().a("t_cr", (String[]) null, string);
            if (cursorA == null) {
                return null;
            }
            try {
                ArrayList arrayList = new ArrayList();
                sb.append("_id in (");
                int i2 = 0;
                while (cursorA.moveToNext()) {
                    CrashDetailBean crashDetailBeanA = a(cursorA);
                    if (crashDetailBeanA != null) {
                        arrayList.add(crashDetailBeanA);
                    } else {
                        try {
                            sb.append(cursorA.getLong(cursorA.getColumnIndex("_id")));
                            sb.append(",");
                            i2++;
                        } catch (Throwable unused) {
                            al.d("unknown id!", new Object[0]);
                        }
                    }
                }
                if (sb.toString().contains(",")) {
                    sb = new StringBuilder(sb.substring(0, sb.lastIndexOf(",")));
                }
                sb.append(")");
                String string2 = sb.toString();
                if (i2 > 0) {
                    al.d("deleted %s illegal data %d", "t_cr", Integer.valueOf(w.a().a("t_cr", string2)));
                }
                if (cursorA != null) {
                    cursorA.close();
                }
                return arrayList;
            } catch (Throwable th) {
                th = th;
                try {
                    if (!al.a(th)) {
                        th.printStackTrace();
                    }
                    if (cursorA != null) {
                        cursorA.close();
                    }
                    return null;
                } finally {
                    if (cursorA != null) {
                        cursorA.close();
                    }
                }
            }
        } catch (Throwable th2) {
            th = th2;
            cursorA = null;
        }
    }

    private static ar b(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            ar arVar = new ar();
            arVar.a = cursor.getLong(cursor.getColumnIndex("_id"));
            arVar.b = cursor.getLong(cursor.getColumnIndex("_tm"));
            arVar.c = cursor.getString(cursor.getColumnIndex("_s1"));
            arVar.d = cursor.getInt(cursor.getColumnIndex("_up")) == 1;
            arVar.e = cursor.getInt(cursor.getColumnIndex("_me")) == 1;
            arVar.f = cursor.getInt(cursor.getColumnIndex("_uc"));
            return arVar;
        } catch (Throwable th) {
            if (!al.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    private static List<ar> b() {
        ArrayList arrayList = new ArrayList();
        Cursor cursor = null;
        try {
            Cursor cursorA = w.a().a("t_cr", new String[]{"_id", "_tm", "_s1", "_up", "_me", "_uc"}, (String) null);
            if (cursorA == null) {
                if (cursorA != null) {
                    cursorA.close();
                }
                return null;
            }
            try {
                if (cursorA.getCount() <= 0) {
                    if (cursorA != null) {
                        cursorA.close();
                    }
                    return arrayList;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("_id in (");
                int i2 = 0;
                while (cursorA.moveToNext()) {
                    ar arVarB = b(cursorA);
                    if (arVarB != null) {
                        arrayList.add(arVarB);
                    } else {
                        try {
                            sb.append(cursorA.getLong(cursorA.getColumnIndex("_id")));
                            sb.append(",");
                            i2++;
                        } catch (Throwable unused) {
                            al.d("unknown id!", new Object[0]);
                        }
                    }
                }
                if (sb.toString().contains(",")) {
                    sb = new StringBuilder(sb.substring(0, sb.lastIndexOf(",")));
                }
                sb.append(")");
                String string = sb.toString();
                sb.setLength(0);
                if (i2 > 0) {
                    al.d("deleted %s illegal data %d", "t_cr", Integer.valueOf(w.a().a("t_cr", string)));
                }
                if (cursorA != null) {
                    cursorA.close();
                }
                return arrayList;
            } catch (Throwable th) {
                th = th;
                cursor = cursorA;
                try {
                    if (!al.a(th)) {
                        th.printStackTrace();
                    }
                    return arrayList;
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static void d(List<ar> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("_id in (");
        Iterator<ar> it = list.iterator();
        while (it.hasNext()) {
            sb.append(it.next().a);
            sb.append(",");
        }
        StringBuilder sb2 = new StringBuilder(sb.substring(0, sb.lastIndexOf(",")));
        sb2.append(")");
        String string = sb2.toString();
        sb2.setLength(0);
        try {
            al.c("deleted %s data %d", "t_cr", Integer.valueOf(w.a().a("t_cr", string)));
        } catch (Throwable th) {
            if (al.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    private static void e(List<CrashDetailBean> list) {
        try {
            if (list.size() == 0) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (CrashDetailBean crashDetailBean : list) {
                sb.append(" or _id = ");
                sb.append(crashDetailBean.a);
            }
            String string = sb.toString();
            if (string.length() > 0) {
                string = string.substring(4);
            }
            sb.setLength(0);
            al.c("deleted %s data %d", "t_cr", Integer.valueOf(w.a().a("t_cr", string)));
        } catch (Throwable th) {
            if (al.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    private static bo a(Context context, CrashDetailBean crashDetailBean, aa aaVar) {
        ArrayList<bl> arrayList = null;
        if (context == null || crashDetailBean == null || aaVar == null) {
            al.d("enExp args == null", new Object[0]);
            return null;
        }
        bo boVar = new bo();
        boVar.a = e(crashDetailBean);
        boVar.b = crashDetailBean.r;
        boVar.c = crashDetailBean.n;
        boVar.d = crashDetailBean.o;
        boVar.e = crashDetailBean.p;
        boVar.g = crashDetailBean.q;
        boVar.h = crashDetailBean.z;
        boVar.i = crashDetailBean.c;
        boVar.j = null;
        boVar.l = crashDetailBean.m;
        boVar.m = crashDetailBean.e;
        boVar.f = crashDetailBean.B;
        boVar.n = null;
        if (crashDetailBean.h != null && !crashDetailBean.h.isEmpty()) {
            arrayList = new ArrayList<>(crashDetailBean.h.size());
            for (Map.Entry<String, PlugInBean> entry : crashDetailBean.h.entrySet()) {
                bl blVar = new bl();
                blVar.a = entry.getValue().a;
                blVar.c = entry.getValue().c;
                blVar.e = entry.getValue().b;
                arrayList.add(blVar);
            }
        }
        boVar.p = arrayList;
        al.c("libInfo %s", boVar.o);
        ArrayList<bn> arrayList2 = new ArrayList<>(20);
        a(arrayList2, crashDetailBean);
        a(arrayList2, crashDetailBean.w);
        b(arrayList2, crashDetailBean.x);
        c(arrayList2, crashDetailBean.Z);
        a(arrayList2, crashDetailBean.aa, context);
        a(arrayList2, crashDetailBean.y);
        a(arrayList2, crashDetailBean, context);
        b(arrayList2, crashDetailBean, context);
        a(arrayList2, aaVar.L);
        b(arrayList2, crashDetailBean.Y);
        boVar.q = arrayList2;
        if (crashDetailBean.j) {
            boVar.k = crashDetailBean.t;
        }
        boVar.r = a(crashDetailBean, aaVar);
        boVar.s = new HashMap();
        if (crashDetailBean.S != null && crashDetailBean.S.size() > 0) {
            boVar.s.putAll(crashDetailBean.S);
            al.a("setted message size %d", Integer.valueOf(boVar.s.size()));
        }
        Map<String, String> map = boVar.s;
        al.c("pss:" + crashDetailBean.I + " vss:" + crashDetailBean.J + " javaHeap:" + crashDetailBean.K, new Object[0]);
        StringBuilder sb = new StringBuilder();
        sb.append(crashDetailBean.I);
        map.put("SDK_UPLOAD_U1", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(crashDetailBean.J);
        map.put("SDK_UPLOAD_U2", sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(crashDetailBean.K);
        map.put("SDK_UPLOAD_U3", sb3.toString());
        Object[] objArr = new Object[12];
        objArr[0] = crashDetailBean.n;
        objArr[1] = crashDetailBean.c;
        objArr[2] = aaVar.d();
        objArr[3] = Long.valueOf((crashDetailBean.r - crashDetailBean.Q) / 1000);
        objArr[4] = Boolean.valueOf(crashDetailBean.k);
        objArr[5] = Boolean.valueOf(crashDetailBean.R);
        objArr[6] = Boolean.valueOf(crashDetailBean.j);
        objArr[7] = Boolean.valueOf(crashDetailBean.b == 1);
        objArr[8] = Integer.valueOf(crashDetailBean.t);
        objArr[9] = crashDetailBean.s;
        objArr[10] = Boolean.valueOf(crashDetailBean.d);
        objArr[11] = Integer.valueOf(boVar.r.size());
        al.c("%s rid:%s sess:%s ls:%ds isR:%b isF:%b isM:%b isN:%b mc:%d ,%s ,isUp:%b ,vm:%d", objArr);
        return boVar;
    }

    private static bp a(Context context, List<CrashDetailBean> list, aa aaVar) {
        if (context == null || list == null || list.size() == 0 || aaVar == null) {
            al.d("enEXPPkg args == null!", new Object[0]);
            return null;
        }
        bp bpVar = new bp();
        bpVar.a = new ArrayList<>();
        Iterator<CrashDetailBean> it = list.iterator();
        while (it.hasNext()) {
            bpVar.a.add(a(context, it.next(), aaVar));
        }
        return bpVar;
    }

    private static bn a(String str, Context context, String str2) throws IOException {
        FileInputStream fileInputStream;
        if (str2 == null || context == null) {
            al.d("rqdp{  createZipAttachment sourcePath == null || context == null ,pls check}", new Object[0]);
            return null;
        }
        al.c("zip %s", str2);
        File file = new File(str2);
        File file2 = new File(context.getCacheDir(), str);
        if (!ap.a(file, file2)) {
            al.d("zip fail!", new Object[0]);
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            fileInputStream = new FileInputStream(file2);
            try {
                byte[] bArr = new byte[4096];
                while (true) {
                    int i2 = fileInputStream.read(bArr);
                    if (i2 <= 0) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, i2);
                    byteArrayOutputStream.flush();
                }
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                al.c("read bytes :%d", Integer.valueOf(byteArray.length));
                bn bnVar = new bn((byte) 2, file2.getName(), byteArray);
                try {
                    fileInputStream.close();
                } catch (IOException e2) {
                    if (!al.a(e2)) {
                        e2.printStackTrace();
                    }
                }
                if (file2.exists()) {
                    al.c("del tmp", new Object[0]);
                    file2.delete();
                }
                return bnVar;
            } catch (Throwable th) {
                th = th;
                try {
                    if (!al.a(th)) {
                        th.printStackTrace();
                    }
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e3) {
                            if (!al.a(e3)) {
                                e3.printStackTrace();
                            }
                        }
                    }
                    if (file2.exists()) {
                        al.c("del tmp", new Object[0]);
                        file2.delete();
                    }
                    return null;
                } catch (Throwable th2) {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e4) {
                            if (!al.a(e4)) {
                                e4.printStackTrace();
                            }
                        }
                    }
                    if (file2.exists()) {
                        al.c("del tmp", new Object[0]);
                        file2.delete();
                    }
                    throw th2;
                }
            }
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
        }
    }

    private boolean d(CrashDetailBean crashDetailBean) {
        try {
            al.c("save eup logs", new Object[0]);
            aa aaVarB = aa.b();
            String str = "#--------\npackage:" + aaVarB.e() + "\nversion:" + aaVarB.o + "\nsdk:" + aaVarB.h + "\nprocess:" + crashDetailBean.A + "\ndate:" + ap.a(new Date(crashDetailBean.r)) + "\ntype:" + crashDetailBean.n + "\nmessage:" + crashDetailBean.o + "\nstack:\n" + crashDetailBean.q + "\neupID:" + crashDetailBean.c + "\n";
            String absolutePath = null;
            if (at.m == null) {
                if (Environment.getExternalStorageState().equals("mounted")) {
                    absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Tencent/" + this.b.getPackageName();
                }
            } else {
                File file = new File(at.m);
                if (file.isFile()) {
                    file = file.getParentFile();
                }
                absolutePath = file.getAbsolutePath();
            }
            am.a(absolutePath + "/euplog.txt", str, at.n);
            return true;
        } catch (Throwable th) {
            al.d("rqdp{  save error} %s", th.toString());
            if (!al.a(th)) {
                th.printStackTrace();
            }
            return false;
        }
    }

    public static void a(String str, String str2, String str3, String str4, String str5, CrashDetailBean crashDetailBean) {
        String str6;
        aa aaVarB = aa.b();
        if (aaVarB == null) {
            return;
        }
        al.e("#++++++++++Record By Bugly++++++++++#", new Object[0]);
        al.e("# You can use Bugly(http:\\\\bugly.qq.com) to get more Crash Detail!", new Object[0]);
        al.e("# PKG NAME: %s", aaVarB.c);
        al.e("# APP VER: %s", aaVarB.o);
        al.e("# SDK VER: %s", aaVarB.h);
        al.e("# LAUNCH TIME: %s", ap.a(new Date(aa.b().a)));
        al.e("# CRASH TYPE: %s", str);
        al.e("# CRASH TIME: %s", str2);
        al.e("# CRASH PROCESS: %s", str3);
        al.e("# CRASH FOREGROUND: %s", Boolean.valueOf(aaVarB.a()));
        al.e("# CRASH THREAD: %s", str4);
        if (crashDetailBean != null) {
            al.e("# REPORT ID: %s", crashDetailBean.c);
            Object[] objArr = new Object[2];
            objArr[0] = aaVarB.h();
            objArr[1] = aaVarB.r().booleanValue() ? "ROOTED" : "UNROOT";
            al.e("# CRASH DEVICE: %s %s", objArr);
            al.e("# RUNTIME AVAIL RAM:%d ROM:%d SD:%d", Long.valueOf(crashDetailBean.C), Long.valueOf(crashDetailBean.D), Long.valueOf(crashDetailBean.E));
            al.e("# RUNTIME TOTAL RAM:%d ROM:%d SD:%d", Long.valueOf(crashDetailBean.F), Long.valueOf(crashDetailBean.G), Long.valueOf(crashDetailBean.H));
            if (!ap.b(crashDetailBean.O)) {
                al.e("# EXCEPTION FIRED BY %s %s", crashDetailBean.O, crashDetailBean.N);
            } else if (crashDetailBean.b == 3) {
                Object[] objArr2 = new Object[1];
                if (crashDetailBean.T == null) {
                    str6 = "null";
                } else {
                    str6 = crashDetailBean.T.get("BUGLY_CR_01");
                }
                objArr2[0] = str6;
                al.e("# EXCEPTION ANR MESSAGE:\n %s", objArr2);
            }
        }
        if (!ap.b(str5)) {
            al.e("# CRASH STACK: ", new Object[0]);
            al.e(str5, new Object[0]);
        }
        al.e("#++++++++++++++++++++++++++++++++++++++++++#", new Object[0]);
    }

    private static void a(CrashDetailBean crashDetailBean, Map<String, String> map) {
        String value;
        if (map == null || map.isEmpty()) {
            al.d("extra map is empty. CrashBean won't have userDatas.", new Object[0]);
            return;
        }
        crashDetailBean.S = new LinkedHashMap(map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!ap.b(entry.getKey())) {
                String key = entry.getKey();
                if (key.length() > 100) {
                    key = key.substring(0, 100);
                    al.d("setted key length is over limit %d substring to %s", 100, key);
                }
                if (!ap.b(entry.getValue()) && entry.getValue().length() > 100000) {
                    value = entry.getValue().substring(entry.getValue().length() - BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH);
                    al.d("setted %s value length is over limit %d substring", key, Integer.valueOf(BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH));
                } else {
                    value = entry.getValue();
                }
                crashDetailBean.S.put(key, value);
                al.a("add setted key %s value size:%d", key, Integer.valueOf(value.length()));
            }
        }
    }

    private static String e(CrashDetailBean crashDetailBean) {
        try {
            Pair<String, String> pair = h.get(Integer.valueOf(crashDetailBean.b));
            if (pair == null) {
                al.e("crash type error! %d", Integer.valueOf(crashDetailBean.b));
                return "";
            }
            if (crashDetailBean.j) {
                return (String) pair.first;
            }
            return (String) pair.second;
        } catch (Exception e2) {
            al.a(e2);
            return "";
        }
    }

    private static void a(ArrayList<bn> arrayList, CrashDetailBean crashDetailBean) {
        if (crashDetailBean.j && crashDetailBean.s != null && crashDetailBean.s.length() > 0) {
            try {
                arrayList.add(new bn((byte) 1, "alltimes.txt", crashDetailBean.s.getBytes("utf-8")));
            } catch (Exception e2) {
                e2.printStackTrace();
                al.a(e2);
            }
        }
    }

    private static void a(ArrayList<bn> arrayList, String str) {
        if (str != null) {
            try {
                arrayList.add(new bn((byte) 1, "log.txt", str.getBytes("utf-8")));
            } catch (Exception e2) {
                e2.printStackTrace();
                al.a(e2);
            }
        }
    }

    private static void b(ArrayList<bn> arrayList, String str) {
        if (str != null) {
            try {
                arrayList.add(new bn((byte) 1, "jniLog.txt", str.getBytes("utf-8")));
            } catch (Exception e2) {
                e2.printStackTrace();
                al.a(e2);
            }
        }
    }

    private static void c(ArrayList<bn> arrayList, String str) {
        if (ap.b(str)) {
            return;
        }
        try {
            bn bnVar = new bn((byte) 1, "crashInfos.txt", str.getBytes("utf-8"));
            al.c("attach crash infos", new Object[0]);
            arrayList.add(bnVar);
        } catch (Exception e2) {
            e2.printStackTrace();
            al.a(e2);
        }
    }

    private static void a(ArrayList<bn> arrayList, String str, Context context) {
        if (str != null) {
            try {
                bn bnVarA = a("backupRecord.zip", context, str);
                if (bnVarA != null) {
                    al.c("attach backup record", new Object[0]);
                    arrayList.add(bnVarA);
                }
            } catch (Exception e2) {
                al.a(e2);
            }
        }
    }

    private static void a(ArrayList<bn> arrayList, byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            return;
        }
        try {
            bn bnVar = new bn((byte) 2, "buglylog.zip", bArr);
            al.c("attach user log", new Object[0]);
            arrayList.add(bnVar);
        } catch (Exception e2) {
            al.a(e2);
        }
    }

    private static void a(ArrayList<bn> arrayList, CrashDetailBean crashDetailBean, Context context) {
        bn bnVarA;
        if (crashDetailBean.b != 3) {
            return;
        }
        al.c("crashBean.anrMessages:%s", crashDetailBean.T);
        try {
            if (crashDetailBean.T != null && crashDetailBean.T.containsKey("BUGLY_CR_01")) {
                if (!TextUtils.isEmpty(crashDetailBean.T.get("BUGLY_CR_01"))) {
                    arrayList.add(new bn((byte) 1, "anrMessage.txt", crashDetailBean.T.get("BUGLY_CR_01").getBytes("utf-8")));
                    al.c("attach anr message", new Object[0]);
                }
                crashDetailBean.T.remove("BUGLY_CR_01");
            }
            if (crashDetailBean.v == null || (bnVarA = a("trace.zip", context, crashDetailBean.v)) == null) {
                return;
            }
            al.c("attach traces", new Object[0]);
            arrayList.add(bnVarA);
        } catch (Exception e2) {
            e2.printStackTrace();
            al.a(e2);
        }
    }

    private static void b(ArrayList<bn> arrayList, CrashDetailBean crashDetailBean, Context context) {
        if (crashDetailBean.b == 1 && crashDetailBean.v != null) {
            try {
                bn bnVarA = a("tomb.zip", context, crashDetailBean.v);
                if (bnVarA != null) {
                    al.c("attach tombs", new Object[0]);
                    arrayList.add(bnVarA);
                }
            } catch (Exception e2) {
                al.a(e2);
            }
        }
    }

    private static void a(ArrayList<bn> arrayList, List<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
        }
        try {
            arrayList.add(new bn((byte) 1, "martianlog.txt", sb.toString().getBytes("utf-8")));
            al.c("attach pageTracingList", new Object[0]);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private static void b(ArrayList<bn> arrayList, byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            return;
        }
        try {
            arrayList.add(new bn((byte) 1, "userExtraByteData", bArr));
            al.c("attach extraData", new Object[0]);
        } catch (Exception e2) {
            al.a(e2);
        }
    }

    private static Map<String, String> a(CrashDetailBean crashDetailBean, aa aaVar) {
        HashMap map = new HashMap(30);
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(crashDetailBean.C);
            map.put("A9", sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append(crashDetailBean.D);
            map.put("A11", sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append(crashDetailBean.E);
            map.put("A10", sb3.toString());
            map.put("A23", crashDetailBean.f);
            StringBuilder sb4 = new StringBuilder();
            aaVar.getClass();
            map.put("A7", sb4.toString());
            map.put("A6", aa.n());
            map.put("A5", aaVar.m());
            map.put("A22", aaVar.g());
            StringBuilder sb5 = new StringBuilder();
            sb5.append(crashDetailBean.G);
            map.put("A2", sb5.toString());
            StringBuilder sb6 = new StringBuilder();
            sb6.append(crashDetailBean.F);
            map.put("A1", sb6.toString());
            map.put("A24", aaVar.k);
            StringBuilder sb7 = new StringBuilder();
            sb7.append(crashDetailBean.H);
            map.put("A17", sb7.toString());
            map.put("A25", aaVar.g());
            map.put("A15", aaVar.q());
            StringBuilder sb8 = new StringBuilder();
            sb8.append(aaVar.r());
            map.put("A13", sb8.toString());
            map.put("A34", crashDetailBean.A);
            if (aaVar.G != null) {
                map.put("productIdentify", aaVar.G);
            }
            map.put("A26", URLEncoder.encode(crashDetailBean.L, "utf-8"));
            boolean z = true;
            if (crashDetailBean.b == 1) {
                map.put("A27", crashDetailBean.O);
                map.put("A28", crashDetailBean.N);
                StringBuilder sb9 = new StringBuilder();
                sb9.append(crashDetailBean.k);
                map.put("A29", sb9.toString());
            }
            map.put("A30", crashDetailBean.P);
            StringBuilder sb10 = new StringBuilder();
            sb10.append(crashDetailBean.Q);
            map.put("A18", sb10.toString());
            StringBuilder sb11 = new StringBuilder();
            if (crashDetailBean.R) {
                z = false;
            }
            sb11.append(z);
            map.put("A36", sb11.toString());
            StringBuilder sb12 = new StringBuilder();
            sb12.append(aaVar.z);
            map.put("F02", sb12.toString());
            StringBuilder sb13 = new StringBuilder();
            sb13.append(aaVar.A);
            map.put("F03", sb13.toString());
            map.put("F04", aaVar.d());
            StringBuilder sb14 = new StringBuilder();
            sb14.append(aaVar.B);
            map.put("F05", sb14.toString());
            map.put("F06", aaVar.y);
            map.put("F08", aaVar.E);
            map.put("F09", aaVar.F);
            StringBuilder sb15 = new StringBuilder();
            sb15.append(aaVar.C);
            map.put("F10", sb15.toString());
            a(map, crashDetailBean);
        } catch (Exception e2) {
            e2.printStackTrace();
            al.a(e2);
        }
        return map;
    }

    private static void a(Map<String, String> map, CrashDetailBean crashDetailBean) {
        if (crashDetailBean.U >= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(crashDetailBean.U);
            map.put("C01", sb.toString());
        }
        if (crashDetailBean.V >= 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(crashDetailBean.V);
            map.put("C02", sb2.toString());
        }
        if (crashDetailBean.W != null && crashDetailBean.W.size() > 0) {
            for (Map.Entry<String, String> entry : crashDetailBean.W.entrySet()) {
                map.put("C03_" + entry.getKey(), entry.getValue());
            }
        }
        if (crashDetailBean.X == null || crashDetailBean.X.size() <= 0) {
            return;
        }
        for (Map.Entry<String, String> entry2 : crashDetailBean.X.entrySet()) {
            map.put("C04_" + entry2.getKey(), entry2.getValue());
        }
    }

    /* compiled from: BUGLY */
    static abstract class a {
        final int a;

        abstract boolean a();

        /* synthetic */ a(int i, byte b) {
            this(i);
        }

        private a(int i) {
            this.a = i;
        }
    }

    /* compiled from: BUGLY */
    static class b extends a {
        /* synthetic */ b(byte b) {
            this();
        }

        private b() {
            super(3, (byte) 0);
        }

        @Override // com.tencent.bugly.proguard.as.a
        final boolean a() {
            return at.a().k();
        }
    }

    /* compiled from: BUGLY */
    static class c extends a {
        @Override // com.tencent.bugly.proguard.as.a
        final boolean a() {
            return true;
        }

        /* synthetic */ c(byte b) {
            this();
        }

        private c() {
            super(7, (byte) 0);
        }
    }

    /* compiled from: BUGLY */
    static class d extends a {
        @Override // com.tencent.bugly.proguard.as.a
        final boolean a() {
            return true;
        }

        /* synthetic */ d(byte b) {
            this();
        }

        private d() {
            super(2, (byte) 0);
        }
    }

    /* compiled from: BUGLY */
    static class e extends a {
        /* synthetic */ e(byte b) {
            this();
        }

        /* JADX WARN: Illegal instructions before constructor call */
        private e() {
            byte b = 0;
            super(b, b);
        }

        @Override // com.tencent.bugly.proguard.as.a
        final boolean a() {
            return at.a().j();
        }
    }

    /* compiled from: BUGLY */
    static class h extends a {
        /* synthetic */ h(byte b) {
            this();
        }

        private h() {
            super(1, (byte) 0);
        }

        @Override // com.tencent.bugly.proguard.as.a
        final boolean a() {
            return at.a().j();
        }
    }

    /* compiled from: BUGLY */
    static class i extends a {
        /* synthetic */ i(byte b) {
            this();
        }

        private i() {
            super(4, (byte) 0);
        }

        @Override // com.tencent.bugly.proguard.as.a
        final boolean a() {
            return (at.a().B & 4) > 0;
        }
    }

    /* compiled from: BUGLY */
    static class f extends a {
        /* synthetic */ f(byte b) {
            this();
        }

        private f() {
            super(5, (byte) 0);
        }

        @Override // com.tencent.bugly.proguard.as.a
        final boolean a() {
            return (at.a().B & 2) > 0;
        }
    }

    /* compiled from: BUGLY */
    static class g extends a {
        /* synthetic */ g(byte b) {
            this();
        }

        private g() {
            super(6, (byte) 0);
        }

        @Override // com.tencent.bugly.proguard.as.a
        final boolean a() {
            return (at.a().B & 1) > 0;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0036, code lost:
    
        if (r0.size() >= com.tencent.bugly.proguard.at.d) goto L25;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean b(com.tencent.bugly.crashreport.crash.CrashDetailBean r9, java.util.List<com.tencent.bugly.proguard.ar> r10, java.util.List<com.tencent.bugly.proguard.ar> r11) {
        /*
            r8 = this;
            int r0 = r9.b
            r1 = 1
            r2 = 0
            if (r0 == 0) goto Lb
            if (r0 != r1) goto L9
            goto Lb
        L9:
            r3 = 0
            goto Lc
        Lb:
            r3 = 1
        Lc:
            r4 = 3
            if (r0 != r4) goto L11
            r0 = 1
            goto L12
        L11:
            r0 = 0
        L12:
            boolean r4 = com.tencent.bugly.proguard.p.c
            if (r4 != 0) goto L1f
            if (r0 != 0) goto L1c
            if (r3 != 0) goto L1c
            r0 = 1
            goto L20
        L1c:
            boolean r0 = com.tencent.bugly.proguard.at.e
            goto L20
        L1f:
            r0 = 0
        L20:
            if (r0 != 0) goto L23
            return r2
        L23:
            java.util.ArrayList r0 = new java.util.ArrayList
            r3 = 10
            r0.<init>(r3)
            boolean r10 = a(r9, r10, r0)
            if (r10 != 0) goto L38
            int r10 = r0.size()     // Catch: java.lang.Exception -> L6d
            int r3 = com.tencent.bugly.proguard.at.d     // Catch: java.lang.Exception -> L6d
            if (r10 < r3) goto L78
        L38:
            java.lang.String r10 = "same crash occur too much do merged!"
            java.lang.Object[] r3 = new java.lang.Object[r2]     // Catch: java.lang.Exception -> L6d
            com.tencent.bugly.proguard.al.a(r10, r3)     // Catch: java.lang.Exception -> L6d
            com.tencent.bugly.crashreport.crash.CrashDetailBean r9 = a(r0, r9)     // Catch: java.lang.Exception -> L6d
            java.util.Iterator r10 = r0.iterator()     // Catch: java.lang.Exception -> L6d
        L47:
            boolean r0 = r10.hasNext()     // Catch: java.lang.Exception -> L6d
            if (r0 == 0) goto L5f
            java.lang.Object r0 = r10.next()     // Catch: java.lang.Exception -> L6d
            com.tencent.bugly.proguard.ar r0 = (com.tencent.bugly.proguard.ar) r0     // Catch: java.lang.Exception -> L6d
            long r3 = r0.a     // Catch: java.lang.Exception -> L6d
            long r5 = r9.a     // Catch: java.lang.Exception -> L6d
            int r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r7 == 0) goto L47
            r11.add(r0)     // Catch: java.lang.Exception -> L6d
            goto L47
        L5f:
            r8.b(r9)     // Catch: java.lang.Exception -> L6d
            d(r11)     // Catch: java.lang.Exception -> L6d
            java.lang.String r9 = "[crash] save crash success. For this device crash many times, it will not upload crashes immediately"
            java.lang.Object[] r10 = new java.lang.Object[r2]     // Catch: java.lang.Exception -> L6d
            com.tencent.bugly.proguard.al.b(r9, r10)     // Catch: java.lang.Exception -> L6d
            return r1
        L6d:
            r9 = move-exception
            com.tencent.bugly.proguard.al.a(r9)
            java.lang.Object[] r9 = new java.lang.Object[r2]
            java.lang.String r10 = "Failed to merge crash."
            com.tencent.bugly.proguard.al.d(r10, r9)
        L78:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.as.b(com.tencent.bugly.crashreport.crash.CrashDetailBean, java.util.List, java.util.List):boolean");
    }

    static /* synthetic */ void a(List list, boolean z, long j2, String str, String str2) {
        if (list == null || list.isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            CrashDetailBean crashDetailBean = (CrashDetailBean) it.next();
            String str3 = l.get(Integer.valueOf(crashDetailBean.b));
            if (!TextUtils.isEmpty(str3)) {
                arrayList.add(new ag.c(crashDetailBean.c, str3, crashDetailBean.r, z, j2, str, str2));
            }
        }
        ag.a.a.a(arrayList);
    }
}
