package com.jkcq.util;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import com.jkcq.base.constants.ConstantLanguages;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* compiled from: AppUtil.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\u000f\u001a\u00020\nJ\b\u0010\u0010\u001a\u00020\u0011H\u0002J\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013J\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\nJ\u0006\u0010\u0017\u001a\u00020\u0018J\u0006\u0010\u0019\u001a\u00020\u0018J\u0006\u0010\u001a\u001a\u00020\u0018J\u0006\u0010\u001b\u001a\u00020\u0018J\u000e\u0010\u001c\u001a\u00020\u00182\u0006\u0010\u001d\u001a\u00020\u001eR\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e¨\u0006\u001f"}, d2 = {"Lcom/jkcq/util/AppUtil;", "", "()V", "bleAdapter", "Landroid/bluetooth/BluetoothAdapter;", "getBleAdapter", "()Landroid/bluetooth/BluetoothAdapter;", "setBleAdapter", "(Landroid/bluetooth/BluetoothAdapter;)V", "mApp", "Landroid/app/Application;", "getMApp", "()Landroid/app/Application;", "setMApp", "(Landroid/app/Application;)V", "getApp", "getLocale", "Ljava/util/Locale;", "getModel", "", "init", "", "app", "isCN", "", "isES", "isOpenBle", "isUSA", "isZh", "context", "Landroid/content/Context;", "util_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class AppUtil {
    public static final AppUtil INSTANCE = new AppUtil();
    private static BluetoothAdapter bleAdapter;
    public static Application mApp;

    private AppUtil() {
    }

    public final Application getMApp() {
        Application application = mApp;
        if (application == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mApp");
        }
        return application;
    }

    public final void setMApp(Application application) {
        Intrinsics.checkParameterIsNotNull(application, "<set-?>");
        mApp = application;
    }

    public final void init(Application app) {
        Intrinsics.checkParameterIsNotNull(app, "app");
        mApp = app;
    }

    public final Application getApp() {
        Application application = mApp;
        if (application == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mApp");
        }
        if (application == null) {
            throw new NullPointerException("u should init first");
        }
        Application application2 = mApp;
        if (application2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mApp");
        }
        return application2;
    }

    public final boolean isUSA() {
        return !isCN();
    }

    public final boolean isES() {
        return Intrinsics.areEqual(getLocale().getLanguage(), "es");
    }

    public final boolean isCN() {
        return Intrinsics.areEqual(getLocale().getLanguage(), ConstantLanguages.SIMPLIFIED_CHINESE);
    }

    private final Locale getLocale() {
        if (Build.VERSION.SDK_INT >= 24) {
            Locale locale = LocaleList.getDefault().get(0);
            Intrinsics.checkExpressionValueIsNotNull(locale, "LocaleList.getDefault()[0]");
            return locale;
        }
        Locale locale2 = Locale.getDefault();
        Intrinsics.checkExpressionValueIsNotNull(locale2, "Locale.getDefault()");
        return locale2;
    }

    public final BluetoothAdapter getBleAdapter() {
        return bleAdapter;
    }

    public final void setBleAdapter(BluetoothAdapter bluetoothAdapter) {
        bleAdapter = bluetoothAdapter;
    }

    public final boolean isOpenBle() {
        if (bleAdapter == null) {
            bleAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        BluetoothAdapter bluetoothAdapter = bleAdapter;
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter == null) {
                Intrinsics.throwNpe();
            }
            if (bluetoothAdapter.isEnabled()) {
                return true;
            }
        }
        return false;
    }

    public final boolean isZh(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Resources resources = context.getResources();
        Intrinsics.checkExpressionValueIsNotNull(resources, "context.resources");
        Locale locale = resources.getConfiguration().locale;
        Intrinsics.checkExpressionValueIsNotNull(locale, "locale");
        String language = locale.getLanguage();
        Intrinsics.checkExpressionValueIsNotNull(language, "language");
        return StringsKt.contains$default((CharSequence) language, (CharSequence) ConstantLanguages.SIMPLIFIED_CHINESE, false, 2, (Object) null);
    }

    public final String getModel() {
        String str = Build.MODEL;
        if (str == null) {
            return "";
        }
        String str2 = str;
        int length = str2.length() - 1;
        int i = 0;
        boolean z = false;
        while (i <= length) {
            boolean z2 = str2.charAt(!z ? i : length) <= ' ';
            if (z) {
                if (!z2) {
                    break;
                }
                length--;
            } else if (z2) {
                i++;
            } else {
                z = true;
            }
        }
        String string = str2.subSequence(i, length + 1).toString();
        if (string == null) {
            return "";
        }
        String strReplace = new Regex("\\s*").replace(string, "");
        return strReplace != null ? strReplace : "";
    }
}
