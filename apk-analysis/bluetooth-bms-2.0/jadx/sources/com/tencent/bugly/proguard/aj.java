package com.tencent.bugly.proguard;

import android.content.Context;
import android.os.Process;
import android.util.Pair;
import com.bumptech.glide.load.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class aj implements Runnable {
    protected int a;
    protected long b;
    protected long c;
    private int d;
    private int e;
    private final Context f;
    private final int g;
    private final byte[] h;
    private final aa i;
    private final ac j;
    private final af k;
    private final ai l;
    private final int m;
    private final ah n;
    private final ah o;
    private String p;
    private final String q;
    private final Map<String, String> r;
    private boolean s;

    public aj(Context context, int i, int i2, byte[] bArr, String str, String str2, ah ahVar, boolean z) {
        this(context, i, i2, bArr, str, str2, ahVar, 2, 30000, z);
    }

    public aj(Context context, int i, int i2, byte[] bArr, String str, String str2, ah ahVar, int i3, int i4, boolean z) {
        this.d = 2;
        this.e = 30000;
        this.p = null;
        this.a = 0;
        this.b = 0L;
        this.c = 0L;
        this.s = false;
        this.f = context;
        this.i = aa.a(context);
        this.h = bArr;
        this.j = ac.a();
        if (af.a == null) {
            af.a = new af(context);
        }
        this.k = af.a;
        ai aiVarA = ai.a();
        this.l = aiVarA;
        this.m = i;
        this.p = str;
        this.q = str2;
        this.n = ahVar;
        this.o = aiVarA.a;
        this.g = i2;
        if (i3 > 0) {
            this.d = i3;
        }
        if (i4 > 0) {
            this.e = i4;
        }
        this.s = z;
        this.r = null;
    }

    private static void a(String str) {
        al.e("[Upload] Failed to upload(%d): %s", 1, str);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0017  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x001b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void a(boolean r5, int r6, java.lang.String r7) {
        /*
            r4 = this;
            int r0 = r4.g
            r1 = 630(0x276, float:8.83E-43)
            if (r0 == r1) goto L1b
            r1 = 640(0x280, float:8.97E-43)
            if (r0 == r1) goto L17
            r1 = 830(0x33e, float:1.163E-42)
            if (r0 == r1) goto L1b
            r1 = 840(0x348, float:1.177E-42)
            if (r0 == r1) goto L17
            java.lang.String r0 = java.lang.String.valueOf(r0)
            goto L1d
        L17:
            java.lang.String r0 = "userinfo"
            goto L1d
        L1b:
            java.lang.String r0 = "crash"
        L1d:
            r1 = 1
            r2 = 0
            if (r5 == 0) goto L2b
            java.lang.Object[] r6 = new java.lang.Object[r1]
            r6[r2] = r0
            java.lang.String r0 = "[Upload] Success: %s"
            com.tencent.bugly.proguard.al.a(r0, r6)
            goto L3e
        L2b:
            r3 = 3
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)
            r3[r2] = r6
            r3[r1] = r0
            r6 = 2
            r3[r6] = r7
            java.lang.String r6 = "[Upload] Failed to upload(%d) %s: %s"
            com.tencent.bugly.proguard.al.e(r6, r3)
        L3e:
            long r0 = r4.b
            long r2 = r4.c
            long r0 = r0 + r2
            r2 = 0
            int r6 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r6 <= 0) goto L5e
            com.tencent.bugly.proguard.ai r6 = r4.l
            boolean r0 = r4.s
            long r0 = r6.a(r0)
            long r2 = r4.b
            long r0 = r0 + r2
            long r2 = r4.c
            long r0 = r0 + r2
            com.tencent.bugly.proguard.ai r6 = r4.l
            boolean r2 = r4.s
            r6.a(r0, r2)
        L5e:
            com.tencent.bugly.proguard.ah r6 = r4.n
            if (r6 == 0) goto L65
            r6.a(r5, r7)
        L65:
            com.tencent.bugly.proguard.ah r6 = r4.o
            if (r6 == 0) goto L6c
            r6.a(r5, r7)
        L6c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.aj.a(boolean, int, java.lang.String):void");
    }

    private static boolean a(br brVar, aa aaVar, ac acVar) throws NumberFormatException {
        if (brVar == null) {
            al.d("resp == null!", new Object[0]);
            return false;
        }
        if (brVar.a != 0) {
            al.e("resp result error %d", Byte.valueOf(brVar.a));
            return false;
        }
        try {
            if (!ap.b(brVar.g) && !aa.b().i().equals(brVar.g)) {
                w.a().a(ac.a, "device", brVar.g.getBytes(Key.STRING_CHARSET_NAME), true);
                aaVar.d(brVar.g);
            }
        } catch (Throwable th) {
            al.a(th);
        }
        aaVar.m = brVar.e;
        if (brVar.b == 510) {
            if (brVar.c == null) {
                al.e("[Upload] Strategy data is null. Response cmd: %d", Integer.valueOf(brVar.b));
                return false;
            }
            bt btVar = (bt) ae.a(brVar.c, bt.class);
            if (btVar == null) {
                al.e("[Upload] Failed to decode strategy from server. Response cmd: %d", Integer.valueOf(brVar.b));
                return false;
            }
            acVar.a(btVar);
        }
        return true;
    }

    @Override // java.lang.Runnable
    public final void run() {
        String str;
        Pair pair;
        boolean zBooleanValue;
        try {
            this.a = 0;
            this.b = 0L;
            this.c = 0L;
            if (ab.c(this.f) == null) {
                str = "network is not available";
            } else if (this.h == null || this.h.length == 0) {
                str = "request package is empty!";
            } else if (this.f == null || this.i == null || this.j == null || this.k == null) {
                str = "illegal access error";
            } else {
                str = this.j.c() == null ? "illegal local strategy" : null;
            }
            if (str != null) {
                a(false, 0, str);
                return;
            }
            byte[] bArrA = ap.a(this.h);
            if (bArrA == null) {
                a(false, 0, "failed to zip request body");
                return;
            }
            HashMap map = new HashMap(10);
            map.put("tls", "1");
            map.put("prodId", this.i.e());
            map.put("bundleId", this.i.c);
            map.put("appVer", this.i.o);
            if (this.r != null) {
                map.putAll(this.r);
            }
            map.put("cmd", Integer.toString(this.g));
            map.put("platformId", Byte.toString((byte) 1));
            map.put("sdkVer", this.i.h);
            map.put("strategylastUpdateTime", Long.toString(this.j.c().o));
            this.l.a(this.m, System.currentTimeMillis());
            String strB = this.p;
            this.j.c();
            int i = 0;
            int i2 = 0;
            while (true) {
                int i3 = i + 1;
                if (i < this.d) {
                    if (i3 > 1) {
                        al.d("[Upload] Failed to upload last time, wait and try(%d) again.", Integer.valueOf(i3));
                        ap.b(this.e);
                        if (i3 == this.d) {
                            al.d("[Upload] Use the back-up url at the last time: %s", this.q);
                            strB = this.q;
                        }
                    }
                    al.c("[Upload] Send %d bytes", Integer.valueOf(bArrA.length));
                    strB = b(strB);
                    al.c("[Upload] Upload to %s with cmd %d (pid=%d | tid=%d).", strB, Integer.valueOf(this.g), Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                    byte[] bArrA2 = this.k.a(strB, bArrA, this, map);
                    Map<String, String> map2 = this.k.c;
                    Pair<Boolean, Boolean> pairA = a(bArrA2, map2);
                    if (!((Boolean) pairA.first).booleanValue()) {
                        zBooleanValue = ((Boolean) pairA.second).booleanValue();
                    } else {
                        Pair<Boolean, Boolean> pairA2 = a(map2);
                        if (!((Boolean) pairA2.first).booleanValue()) {
                            zBooleanValue = ((Boolean) pairA2.second).booleanValue();
                        } else {
                            byte[] bArrB = ap.b(bArrA2);
                            if (bArrB != null) {
                                bArrA2 = bArrB;
                            }
                            br brVarA = ae.a(bArrA2);
                            if (brVarA == null) {
                                a(false, 1, "failed to decode response package");
                                Boolean bool = Boolean.FALSE;
                                pair = new Pair(bool, bool);
                            } else {
                                Object[] objArr = new Object[2];
                                objArr[0] = Integer.valueOf(brVarA.b);
                                objArr[1] = Integer.valueOf(brVarA.c == null ? 0 : brVarA.c.length);
                                al.c("[Upload] Response cmd is: %d, length of sBuffer is: %d", objArr);
                                if (!a(brVarA, this.i, this.j)) {
                                    a(false, 2, "failed to process response package");
                                    Boolean bool2 = Boolean.FALSE;
                                    pair = new Pair(bool2, bool2);
                                } else {
                                    a(true, 2, "successfully uploaded");
                                    Boolean bool3 = Boolean.TRUE;
                                    pair = new Pair(bool3, bool3);
                                }
                            }
                            zBooleanValue = !((Boolean) pair.first).booleanValue() ? ((Boolean) pair.second).booleanValue() : false;
                        }
                    }
                    if (!zBooleanValue) {
                        return;
                    }
                    i = i3;
                    i2 = 1;
                } else {
                    a(false, i2, "failed after many attempts");
                    return;
                }
            }
        } catch (Throwable th) {
            if (al.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    private Pair<Boolean, Boolean> a(byte[] bArr, Map<String, String> map) {
        if (bArr == null) {
            a("Failed to upload for no response!");
            return new Pair<>(Boolean.FALSE, Boolean.TRUE);
        }
        al.c("[Upload] Received %d bytes", Integer.valueOf(bArr.length));
        if (bArr.length == 0) {
            a(false, 1, "response data from server is empty");
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    al.c("[Upload] HTTP headers from server: key = %s, value = %s", entry.getKey(), entry.getValue());
                }
            }
            Boolean bool = Boolean.FALSE;
            return new Pair<>(bool, bool);
        }
        Boolean bool2 = Boolean.TRUE;
        return new Pair<>(bool2, bool2);
    }

    public final void a(long j) {
        this.a++;
        this.b += j;
    }

    public final void b(long j) {
        this.c += j;
    }

    private static String b(String str) {
        if (ap.b(str)) {
            return str;
        }
        try {
            return String.format("%s?aid=%s", str, UUID.randomUUID().toString());
        } catch (Throwable th) {
            al.a(th);
            return str;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00ba  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.util.Pair<java.lang.Boolean, java.lang.Boolean> a(java.util.Map<java.lang.String, java.lang.String> r8) {
        /*
            Method dump skipped, instructions count: 293
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.aj.a(java.util.Map):android.util.Pair");
    }
}
