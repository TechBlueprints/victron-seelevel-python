package com.jkcq.util;

import android.util.Log;

/* loaded from: classes.dex */
public class LogUtil {
    private static final boolean IS_DEBUG = true;
    private static final String TAG = "jkcq_dgdc";

    public static void e(String str) {
        if (str != null) {
            Log.e(TAG, str);
        }
    }

    public static void e(String str, String str2) {
        if (str2 != null) {
            Log.e(str, str2);
        }
    }

    public static void w(String str) {
        if (str != null) {
            Log.w(TAG, str);
        }
    }

    public static void w(String str, String str2) {
        if (str2 != null) {
            Log.w(str, str2);
        }
    }

    public static void d(String str) {
        if (str != null) {
            Log.d(TAG, str);
        }
    }

    public static void d(String str, String str2) {
        if (str2 != null) {
            Log.d(str, str2);
        }
    }

    public static void i(String str) {
        if (str != null) {
            Log.i(TAG, str);
        }
    }

    public static void i(String str, String str2) {
        if (str2 != null) {
            Log.i(str, str2);
        }
    }

    public static void v(String str) {
        if (str != null) {
            Log.v(TAG, str);
        }
    }

    public static void v(String str, String str2) {
        if (str2 != null) {
            Log.v(str, str2);
        }
    }
}
