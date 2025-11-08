package com.blankj.utilcode.util;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.NotificationManagerCompat;
import com.blankj.utilcode.util.Utils;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
public final class ToastUtils {
    private static final int COLOR_DEFAULT = -16777217;
    private static final String NULL = "null";
    private static IToast iToast = null;
    private static int sBgColor = -16777217;
    private static int sBgResource = -1;
    private static int sGravity = -1;
    private static int sMsgColor = -16777217;
    private static int sMsgTextSize = -1;
    private static int sXOffset = -1;
    private static int sYOffset = -1;

    interface IToast {
        void cancel();

        View getView();

        void setDuration(int i);

        void setGravity(int i, int i2, int i3);

        void setText(int i);

        void setText(CharSequence charSequence);

        void setView(View view);

        void show();
    }

    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void setGravity(int i, int i2, int i3) {
        sGravity = i;
        sXOffset = i2;
        sYOffset = i3;
    }

    public static void setBgColor(int i) {
        sBgColor = i;
    }

    public static void setBgResource(int i) {
        sBgResource = i;
    }

    public static void setMsgColor(int i) {
        sMsgColor = i;
    }

    public static void setMsgTextSize(int i) {
        sMsgTextSize = i;
    }

    public static void showShort(CharSequence charSequence) {
        if (charSequence == null) {
            charSequence = NULL;
        }
        show(charSequence, 0);
    }

    public static void showShort(int i) throws Resources.NotFoundException {
        show(i, 0);
    }

    public static void showShort(int i, Object... objArr) throws Resources.NotFoundException {
        show(i, 0, objArr);
    }

    public static void showShort(String str, Object... objArr) {
        show(str, 0, objArr);
    }

    public static void showLong(CharSequence charSequence) {
        if (charSequence == null) {
            charSequence = NULL;
        }
        show(charSequence, 1);
    }

    public static void showLong(int i) throws Resources.NotFoundException {
        show(i, 1);
    }

    public static void showLong(int i, Object... objArr) throws Resources.NotFoundException {
        show(i, 1, objArr);
    }

    public static void showLong(String str, Object... objArr) {
        show(str, 1, objArr);
    }

    public static View showCustomShort(int i) {
        return showCustomShort(getView(i));
    }

    public static View showCustomShort(View view) {
        show(view, 0);
        return view;
    }

    public static View showCustomLong(int i) {
        return showCustomLong(getView(i));
    }

    public static View showCustomLong(View view) {
        show(view, 1);
        return view;
    }

    public static void cancel() {
        IToast iToast2 = iToast;
        if (iToast2 != null) {
            iToast2.cancel();
        }
    }

    private static void show(int i, int i2) throws Resources.NotFoundException {
        show(i, i2, null);
    }

    private static void show(int i, int i2, Object... objArr) throws Resources.NotFoundException {
        try {
            CharSequence text = Utils.getApp().getResources().getText(i);
            if (objArr != null && objArr.length > 0) {
                text = String.format(text.toString(), objArr);
            }
            show(text, i2);
        } catch (Exception unused) {
            show(String.valueOf(i), i2);
        }
    }

    private static void show(String str, int i, Object... objArr) {
        if (str == null) {
            str = NULL;
        } else if (objArr != null && objArr.length > 0) {
            str = String.format(str, objArr);
        }
        show(str, i);
    }

    private static void show(final CharSequence charSequence, final int i) {
        UtilsBridge.runOnUiThread(new Runnable() { // from class: com.blankj.utilcode.util.ToastUtils.1
            @Override // java.lang.Runnable
            public void run() {
                ToastUtils.cancel();
                IToast unused = ToastUtils.iToast = ToastFactory.makeToast(Utils.getApp(), charSequence, i);
                View view = ToastUtils.iToast.getView();
                if (view == null) {
                    return;
                }
                TextView textView = (TextView) view.findViewById(R.id.message);
                if (ToastUtils.sMsgColor != ToastUtils.COLOR_DEFAULT) {
                    textView.setTextColor(ToastUtils.sMsgColor);
                }
                if (ToastUtils.sMsgTextSize != -1) {
                    textView.setTextSize(ToastUtils.sMsgTextSize);
                }
                if (ToastUtils.sGravity != -1 || ToastUtils.sXOffset != -1 || ToastUtils.sYOffset != -1) {
                    ToastUtils.iToast.setGravity(ToastUtils.sGravity, ToastUtils.sXOffset, ToastUtils.sYOffset);
                }
                ToastUtils.setBg(textView);
                ToastUtils.iToast.show();
            }
        });
    }

    private static void show(final View view, final int i) {
        UtilsBridge.runOnUiThread(new Runnable() { // from class: com.blankj.utilcode.util.ToastUtils.2
            @Override // java.lang.Runnable
            public void run() {
                ToastUtils.cancel();
                IToast unused = ToastUtils.iToast = ToastFactory.newToast(Utils.getApp());
                ToastUtils.iToast.setView(view);
                ToastUtils.iToast.setDuration(i);
                if (ToastUtils.sGravity != -1 || ToastUtils.sXOffset != -1 || ToastUtils.sYOffset != -1) {
                    ToastUtils.iToast.setGravity(ToastUtils.sGravity, ToastUtils.sXOffset, ToastUtils.sYOffset);
                }
                ToastUtils.setBg();
                ToastUtils.iToast.show();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setBg() {
        if (sBgResource != -1) {
            iToast.getView().setBackgroundResource(sBgResource);
            return;
        }
        if (sBgColor != COLOR_DEFAULT) {
            View view = iToast.getView();
            Drawable background = view.getBackground();
            if (background != null) {
                background.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));
            } else if (Build.VERSION.SDK_INT >= 16) {
                view.setBackground(new ColorDrawable(sBgColor));
            } else {
                view.setBackgroundDrawable(new ColorDrawable(sBgColor));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setBg(TextView textView) {
        if (sBgResource != -1) {
            iToast.getView().setBackgroundResource(sBgResource);
            textView.setBackgroundColor(0);
            return;
        }
        if (sBgColor != COLOR_DEFAULT) {
            View view = iToast.getView();
            Drawable background = view.getBackground();
            Drawable background2 = textView.getBackground();
            if (background != null && background2 != null) {
                background.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));
                textView.setBackgroundColor(0);
            } else if (background != null) {
                background.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));
            } else if (background2 != null) {
                background2.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));
            } else {
                view.setBackgroundColor(sBgColor);
            }
        }
    }

    private static View getView(int i) {
        return ((LayoutInflater) Utils.getApp().getSystemService("layout_inflater")).inflate(i, (ViewGroup) null);
    }

    static class ToastFactory {
        ToastFactory() {
        }

        static IToast makeToast(Context context, CharSequence charSequence, int i) {
            if (NotificationManagerCompat.from(context).areNotificationsEnabled() && Build.VERSION.SDK_INT >= 23 && !UtilsBridge.isGrantedDrawOverlays()) {
                return new SystemToast(makeNormalToast(context, charSequence, i));
            }
            return new ToastWithoutNotification(makeNormalToast(context, charSequence, i));
        }

        static IToast newToast(Context context) {
            if (NotificationManagerCompat.from(context).areNotificationsEnabled() && Build.VERSION.SDK_INT >= 23 && !UtilsBridge.isGrantedDrawOverlays()) {
                return new SystemToast(new Toast(context));
            }
            return new ToastWithoutNotification(new Toast(context));
        }

        private static Toast makeNormalToast(Context context, CharSequence charSequence, int i) {
            Toast toastMakeText = Toast.makeText(context, "", i);
            toastMakeText.setText(charSequence);
            return toastMakeText;
        }
    }

    static class SystemToast extends AbsToast {
        SystemToast(Toast toast) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
            super(toast);
            if (Build.VERSION.SDK_INT == 25) {
                try {
                    Field declaredField = Toast.class.getDeclaredField("mTN");
                    declaredField.setAccessible(true);
                    Object obj = declaredField.get(toast);
                    Field declaredField2 = declaredField.getType().getDeclaredField("mHandler");
                    declaredField2.setAccessible(true);
                    declaredField2.set(obj, new SafeHandler((Handler) declaredField2.get(obj)));
                } catch (Exception unused) {
                }
            }
        }

        @Override // com.blankj.utilcode.util.ToastUtils.IToast
        public void show() {
            this.mToast.show();
        }

        @Override // com.blankj.utilcode.util.ToastUtils.IToast
        public void cancel() {
            this.mToast.cancel();
        }

        static class SafeHandler extends Handler {
            private Handler impl;

            SafeHandler(Handler handler) {
                this.impl = handler;
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                this.impl.handleMessage(message);
            }

            @Override // android.os.Handler
            public void dispatchMessage(Message message) {
                try {
                    this.impl.dispatchMessage(message);
                } catch (Exception e) {
                    Log.e("ToastUtils", e.toString());
                }
            }
        }
    }

    static class ToastWithoutNotification extends AbsToast {
        private WindowManager.LayoutParams mParams;
        private View mView;
        private WindowManager mWM;

        ToastWithoutNotification(Toast toast) {
            super(toast);
            this.mParams = new WindowManager.LayoutParams();
        }

        @Override // com.blankj.utilcode.util.ToastUtils.IToast
        public void show() {
            UtilsBridge.runOnUiThreadDelayed(new Runnable() { // from class: com.blankj.utilcode.util.ToastUtils.ToastWithoutNotification.1
                @Override // java.lang.Runnable
                public void run() {
                    ToastWithoutNotification.this.realShow();
                }
            }, 300L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void realShow() {
            if (this.mToast == null) {
                return;
            }
            View view = this.mToast.getView();
            this.mView = view;
            if (view == null) {
                return;
            }
            Context context = this.mToast.getView().getContext();
            if (Build.VERSION.SDK_INT < 25) {
                this.mWM = (WindowManager) context.getSystemService("window");
                this.mParams.type = 2005;
            } else if (UtilsBridge.isGrantedDrawOverlays()) {
                this.mWM = (WindowManager) context.getSystemService("window");
                if (Build.VERSION.SDK_INT >= 26) {
                    this.mParams.type = 2038;
                } else {
                    this.mParams.type = 2002;
                }
            } else {
                Context topActivityOrApp = UtilsBridge.getTopActivityOrApp();
                if (!(topActivityOrApp instanceof Activity)) {
                    Log.w("ToastUtils", "Couldn't get top Activity.");
                    new SystemToast(this.mToast).show();
                    return;
                }
                Activity activity = (Activity) topActivityOrApp;
                if (activity.isFinishing() || activity.isDestroyed()) {
                    Log.w("ToastUtils", activity + " is useless");
                    new SystemToast(this.mToast).show();
                    return;
                }
                this.mWM = activity.getWindowManager();
                this.mParams.type = 99;
                UtilsBridge.addActivityLifecycleCallbacks(activity, getActivityLifecycleCallbacks());
            }
            setToastParams();
            try {
                if (this.mWM != null) {
                    this.mWM.addView(this.mView, this.mParams);
                }
            } catch (Exception unused) {
            }
            UtilsBridge.runOnUiThreadDelayed(new Runnable() { // from class: com.blankj.utilcode.util.ToastUtils.ToastWithoutNotification.2
                @Override // java.lang.Runnable
                public void run() {
                    ToastWithoutNotification.this.cancel();
                }
            }, this.mToast.getDuration() == 0 ? 2000L : 3500L);
        }

        private void setToastParams() {
            this.mParams.height = -2;
            this.mParams.width = -2;
            this.mParams.format = -3;
            this.mParams.windowAnimations = R.style.Animation.Toast;
            this.mParams.setTitle("ToastWithoutNotification");
            this.mParams.flags = 152;
            this.mParams.packageName = Utils.getApp().getPackageName();
            this.mParams.gravity = this.mToast.getGravity();
            if ((this.mParams.gravity & 7) == 7) {
                this.mParams.horizontalWeight = 1.0f;
            }
            if ((this.mParams.gravity & 112) == 112) {
                this.mParams.verticalWeight = 1.0f;
            }
            this.mParams.x = this.mToast.getXOffset();
            this.mParams.y = this.mToast.getYOffset();
            this.mParams.horizontalMargin = this.mToast.getHorizontalMargin();
            this.mParams.verticalMargin = this.mToast.getVerticalMargin();
        }

        private Utils.ActivityLifecycleCallbacks getActivityLifecycleCallbacks() {
            return new Utils.ActivityLifecycleCallbacks() { // from class: com.blankj.utilcode.util.ToastUtils.ToastWithoutNotification.3
                @Override // com.blankj.utilcode.util.Utils.ActivityLifecycleCallbacks
                public void onActivityDestroyed(Activity activity) {
                    if (activity != null) {
                        if (ToastUtils.iToast == null) {
                            return;
                        }
                        activity.getWindow().getDecorView().setVisibility(8);
                        ToastUtils.iToast.cancel();
                        return;
                    }
                    throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
                }
            };
        }

        @Override // com.blankj.utilcode.util.ToastUtils.IToast
        public void cancel() {
            try {
                if (this.mWM != null) {
                    this.mWM.removeViewImmediate(this.mView);
                }
            } catch (Exception unused) {
            }
            this.mView = null;
            this.mWM = null;
            this.mToast = null;
        }
    }

    static abstract class AbsToast implements IToast {
        Toast mToast;

        AbsToast(Toast toast) {
            this.mToast = toast;
        }

        @Override // com.blankj.utilcode.util.ToastUtils.IToast
        public void setView(View view) {
            this.mToast.setView(view);
        }

        @Override // com.blankj.utilcode.util.ToastUtils.IToast
        public View getView() {
            return this.mToast.getView();
        }

        @Override // com.blankj.utilcode.util.ToastUtils.IToast
        public void setDuration(int i) {
            this.mToast.setDuration(i);
        }

        @Override // com.blankj.utilcode.util.ToastUtils.IToast
        public void setGravity(int i, int i2, int i3) {
            this.mToast.setGravity(i, i2, i3);
        }

        @Override // com.blankj.utilcode.util.ToastUtils.IToast
        public void setText(int i) {
            this.mToast.setText(i);
        }

        @Override // com.blankj.utilcode.util.ToastUtils.IToast
        public void setText(CharSequence charSequence) {
            this.mToast.setText(charSequence);
        }
    }
}
