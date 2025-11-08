package com.jkcq.util.ktx;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/* loaded from: classes.dex */
public class ToastUtil {
    private static Context context;
    private static long mLastClickTime;
    private static Toast toast;

    public static void init(Context context2) {
        context = context2;
    }

    public static void showTextToast(String str) {
        if (TextUtils.isEmpty(str) || str.contains("没有访问权限！")) {
            return;
        }
        Toast toast2 = toast;
        if (toast2 == null) {
            toast = Toast.makeText(context, str, 0);
        } else {
            toast2.setText(str);
        }
        toast.setGravity(17, 0, 0);
        toast.show();
    }

    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(1000L);
    }

    private static boolean isFastDoubleClick(long j) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - mLastClickTime < j) {
            return true;
        }
        mLastClickTime = jCurrentTimeMillis;
        return false;
    }

    public static void showTextToast(Context context2, int i) {
        Toast toast2 = toast;
        if (toast2 == null) {
            toast = Toast.makeText(context2, context2.getString(i), 0);
        } else {
            toast2.setText(i);
        }
        toast.setGravity(17, 0, 0);
        toast.show();
    }

    public void cancelToast() {
        Toast toast2 = toast;
        if (toast2 != null) {
            toast2.cancel();
        }
    }

    public static void showTextToastById(int i) {
        Toast toast2 = toast;
        if (toast2 == null) {
            Context context2 = context;
            toast = Toast.makeText(context2, context2.getResources().getString(i), 0);
        } else {
            toast2.setText(i);
        }
        toast.setGravity(17, 0, 0);
        toast.show();
    }

    public static void showTextToast(Context context2, String str) {
        if (TextUtils.isEmpty(str) || str.contains("没有访问权限！")) {
            return;
        }
        Toast toast2 = toast;
        if (toast2 == null) {
            toast = Toast.makeText(context2, str, 0);
        } else {
            toast2.setText(str);
        }
        toast.setGravity(17, 0, 0);
        toast.show();
    }

    public static void showTextToastById(Context context2, int i) {
        Toast toast2 = toast;
        if (toast2 == null) {
            toast = Toast.makeText(context2, context2.getResources().getString(i), 0);
        } else {
            toast2.setText(i);
        }
        toast.setGravity(17, 0, 0);
        toast.show();
    }
}
