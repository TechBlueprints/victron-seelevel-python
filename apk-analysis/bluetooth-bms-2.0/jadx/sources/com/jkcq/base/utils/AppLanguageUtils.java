package com.jkcq.base.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.Log;
import com.jkcq.base.constants.ConstantLanguages;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

/* loaded from: classes.dex */
public class AppLanguageUtils {
    public static HashMap<String, Locale> mAllLanguages = new HashMap<String, Locale>(7) { // from class: com.jkcq.base.utils.AppLanguageUtils.1
        {
            put(ConstantLanguages.ENGLISH, Locale.ENGLISH);
            put(ConstantLanguages.SIMPLIFIED_CHINESE, Locale.SIMPLIFIED_CHINESE);
            put(ConstantLanguages.TRADITIONAL_CHINESE, Locale.TRADITIONAL_CHINESE);
            put(ConstantLanguages.FRANCE, Locale.FRANCE);
            put(ConstantLanguages.GERMAN, Locale.GERMANY);
            put(ConstantLanguages.HINDI, new Locale(ConstantLanguages.HINDI, "IN"));
            put(ConstantLanguages.ITALIAN, Locale.ITALY);
        }
    };

    public static void changeAppLanguage(Context context, String str) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale localeByLanguage = getLocaleByLanguage(str);
        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(localeByLanguage);
        } else {
            configuration.locale = localeByLanguage;
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    private static boolean isSupportLanguage(String str) {
        return mAllLanguages.containsKey(str);
    }

    public static String getSupportLanguage(String str) {
        return isSupportLanguage(str) ? str : ConstantLanguages.ENGLISH;
    }

    public static Locale getLocaleByLanguage(String str) {
        if (isSupportLanguage(str)) {
            return mAllLanguages.get(str);
        }
        Locale locale = Locale.getDefault();
        for (String str2 : mAllLanguages.keySet()) {
            Log.e("getLocaleByLanguage", "-----" + mAllLanguages.get(str2).getLanguage());
            if (TextUtils.equals(mAllLanguages.get(str2).getLanguage(), locale.getLanguage())) {
                return locale;
            }
        }
        return Locale.ENGLISH;
    }

    public static String getLocaleByLanguage() {
        Locale locale = Locale.getDefault();
        Iterator<String> it = mAllLanguages.keySet().iterator();
        while (it.hasNext()) {
            if (TextUtils.equals(mAllLanguages.get(it.next()).getLanguage(), locale.getLanguage())) {
                return locale.getLanguage();
            }
        }
        return ConstantLanguages.ENGLISH;
    }

    public static Context attachBaseContext(Context context, String str) {
        if (!TextUtils.isEmpty(str) && Build.VERSION.SDK_INT >= 24) {
            return updateResources(context, str);
        }
        changeAppLanguage(context, str);
        return context;
    }

    private static Context updateResources(Context context, String str) {
        Resources resources = context.getResources();
        Locale localeByLanguage = getLocaleByLanguage(str);
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(localeByLanguage);
        configuration.setLocales(new LocaleList(localeByLanguage));
        return context.createConfigurationContext(configuration);
    }
}
