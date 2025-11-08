package com.tencent.bugly.crashreport.crash.h5;

import android.webkit.JavascriptInterface;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.inner.InnerApi;
import com.tencent.bugly.proguard.al;
import com.tencent.bugly.proguard.ap;
import com.tencent.bugly.proguard.bb;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import no.nordicsemi.android.log.LogContract;
import org.json.JSONObject;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class H5JavaScriptInterface {
    private static HashSet<Integer> a = new HashSet<>();
    private String b = null;
    private Thread c = null;
    private String d = null;
    private Map<String, String> e = null;

    private H5JavaScriptInterface() {
    }

    public static H5JavaScriptInterface getInstance(CrashReport.a aVar) {
        String string = null;
        if (aVar == null || a.contains(Integer.valueOf(aVar.hashCode()))) {
            return null;
        }
        H5JavaScriptInterface h5JavaScriptInterface = new H5JavaScriptInterface();
        a.add(Integer.valueOf(aVar.hashCode()));
        Thread threadCurrentThread = Thread.currentThread();
        h5JavaScriptInterface.c = threadCurrentThread;
        if (threadCurrentThread != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            for (int i = 2; i < threadCurrentThread.getStackTrace().length; i++) {
                StackTraceElement stackTraceElement = threadCurrentThread.getStackTrace()[i];
                if (!stackTraceElement.toString().contains("crashreport")) {
                    sb.append(stackTraceElement.toString());
                    sb.append("\n");
                }
            }
            string = sb.toString();
        }
        h5JavaScriptInterface.d = string;
        HashMap map = new HashMap();
        StringBuilder sb2 = new StringBuilder();
        sb2.append((Object) aVar.c());
        map.put("[WebView] ContentDescription", sb2.toString());
        h5JavaScriptInterface.e = map;
        return h5JavaScriptInterface;
    }

    private static bb a(String str) {
        String string;
        if (str != null && str.length() > 0) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                bb bbVar = new bb();
                bbVar.a = jSONObject.getString("projectRoot");
                if (bbVar.a == null) {
                    return null;
                }
                bbVar.b = jSONObject.getString("context");
                if (bbVar.b == null) {
                    return null;
                }
                bbVar.c = jSONObject.getString("url");
                if (bbVar.c == null) {
                    return null;
                }
                bbVar.d = jSONObject.getString("userAgent");
                if (bbVar.d == null) {
                    return null;
                }
                bbVar.e = jSONObject.getString("language");
                if (bbVar.e == null) {
                    return null;
                }
                bbVar.f = jSONObject.getString(LogContract.SessionColumns.NAME);
                if (bbVar.f == null || bbVar.f.equals("null") || (string = jSONObject.getString("stacktrace")) == null) {
                    return null;
                }
                int iIndexOf = string.indexOf("\n");
                if (iIndexOf < 0) {
                    al.d("H5 crash stack's format is wrong!", new Object[0]);
                    return null;
                }
                bbVar.h = string.substring(iIndexOf + 1);
                bbVar.g = string.substring(0, iIndexOf);
                int iIndexOf2 = bbVar.g.indexOf(":");
                if (iIndexOf2 > 0) {
                    bbVar.g = bbVar.g.substring(iIndexOf2 + 1);
                }
                bbVar.i = jSONObject.getString("file");
                if (bbVar.f == null) {
                    return null;
                }
                bbVar.j = jSONObject.getLong("lineNumber");
                if (bbVar.j < 0) {
                    return null;
                }
                bbVar.k = jSONObject.getLong("columnNumber");
                if (bbVar.k < 0) {
                    return null;
                }
                al.a("H5 crash information is following: ", new Object[0]);
                al.a("[projectRoot]: " + bbVar.a, new Object[0]);
                al.a("[context]: " + bbVar.b, new Object[0]);
                al.a("[url]: " + bbVar.c, new Object[0]);
                al.a("[userAgent]: " + bbVar.d, new Object[0]);
                al.a("[language]: " + bbVar.e, new Object[0]);
                al.a("[name]: " + bbVar.f, new Object[0]);
                al.a("[message]: " + bbVar.g, new Object[0]);
                al.a("[stacktrace]: \n" + bbVar.h, new Object[0]);
                al.a("[file]: " + bbVar.i, new Object[0]);
                al.a("[lineNumber]: " + bbVar.j, new Object[0]);
                al.a("[columnNumber]: " + bbVar.k, new Object[0]);
                return bbVar;
            } catch (Throwable th) {
                if (!al.a(th)) {
                    th.printStackTrace();
                }
            }
        }
        return null;
    }

    @JavascriptInterface
    public void printLog(String str) {
        al.d("Log from js: %s", str);
    }

    @JavascriptInterface
    public void reportJSException(String str) {
        if (str == null) {
            al.d("Payload from JS is null.", new Object[0]);
            return;
        }
        String strC = ap.c(str.getBytes());
        String str2 = this.b;
        if (str2 != null && str2.equals(strC)) {
            al.d("Same payload from js. Please check whether you've injected bugly.js more than one times.", new Object[0]);
            return;
        }
        this.b = strC;
        al.d("Handling JS exception ...", new Object[0]);
        bb bbVarA = a(str);
        if (bbVarA == null) {
            al.d("Failed to parse payload.", new Object[0]);
            return;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        if (bbVarA.a != null) {
            linkedHashMap2.put("[JS] projectRoot", bbVarA.a);
        }
        if (bbVarA.b != null) {
            linkedHashMap2.put("[JS] context", bbVarA.b);
        }
        if (bbVarA.c != null) {
            linkedHashMap2.put("[JS] url", bbVarA.c);
        }
        if (bbVarA.d != null) {
            linkedHashMap2.put("[JS] userAgent", bbVarA.d);
        }
        if (bbVarA.i != null) {
            linkedHashMap2.put("[JS] file", bbVarA.i);
        }
        if (bbVarA.j != 0) {
            linkedHashMap2.put("[JS] lineNumber", Long.toString(bbVarA.j));
        }
        linkedHashMap.putAll(linkedHashMap2);
        linkedHashMap.putAll(this.e);
        linkedHashMap.put("Java Stack", this.d);
        Thread thread = this.c;
        if (bbVarA != null) {
            InnerApi.postH5CrashAsync(thread, bbVarA.f, bbVarA.g, bbVarA.h, linkedHashMap);
        }
    }
}
