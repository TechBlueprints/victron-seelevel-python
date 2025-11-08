package com.jkcq.base.app;

import android.content.SharedPreferences;
import androidx.exifinterface.media.ExifInterface;
import com.bumptech.glide.load.Key;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KProperty;
import no.nordicsemi.android.log.LogContract;

/* compiled from: Preference.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\b\u0018\u0000 \u001d*\u0004\b\u0000\u0010\u00012\u00020\u0002:\u0001\u001dB\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00028\u0000¢\u0006\u0002\u0010\u0006J\u001b\u0010\n\u001a\u0002H\u000b\"\u0004\b\u0001\u0010\u000b2\u0006\u0010\f\u001a\u00020\u0004H\u0002¢\u0006\u0002\u0010\rJ\u001d\u0010\u000e\u001a\u00028\u00002\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\u000fJ$\u0010\u0010\u001a\u00028\u00002\b\u0010\u0011\u001a\u0004\u0018\u00010\u00022\n\u0010\u0012\u001a\u0006\u0012\u0002\b\u00030\u0013H\u0086\u0002¢\u0006\u0002\u0010\u0014J\u001d\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00028\u0000H\u0003¢\u0006\u0002\u0010\u0006J\u001b\u0010\u0018\u001a\u00020\u0004\"\u0004\b\u0001\u0010\u000b2\u0006\u0010\u0019\u001a\u0002H\u000bH\u0002¢\u0006\u0002\u0010\u001aJ,\u0010\u001b\u001a\u00020\u00162\b\u0010\u0011\u001a\u0004\u0018\u00010\u00022\n\u0010\u0012\u001a\u0006\u0012\u0002\b\u00030\u00132\u0006\u0010\u0017\u001a\u00028\u0000H\u0086\u0002¢\u0006\u0002\u0010\u001cR\u0010\u0010\u0005\u001a\u00028\u0000X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0007R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t¨\u0006\u001e"}, d2 = {"Lcom/jkcq/base/app/Preference;", ExifInterface.GPS_DIRECTION_TRUE, "", LogContract.SessionColumns.NAME, "", "default", "(Ljava/lang/String;Ljava/lang/Object;)V", "Ljava/lang/Object;", "getName", "()Ljava/lang/String;", "deSerialization", ExifInterface.GPS_MEASUREMENT_IN_PROGRESS, "str", "(Ljava/lang/String;)Ljava/lang/Object;", "getSharedPreferences", "(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", "getValue", "thisRef", "property", "Lkotlin/reflect/KProperty;", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;)Ljava/lang/Object;", "putSharedPreferences", "", "value", "serialize", "obj", "(Ljava/lang/Object;)Ljava/lang/String;", "setValue", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;Ljava/lang/Object;)V", "Companion", "base_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class Preference<T> {
    public static final String BIKEMAC = "bikemac";
    public static final String BIKENAME = "bikename";
    public static final String BIKEVERSION = "bikeVersion";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "user_name";
    public static final String clicklaug = "clicklaug";
    public static final String selecttype = "selecttype";
    private final T default;
    private final String name;

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final String file_name = "jkcq_dgdc_preference";
    private static final Lazy prefs$delegate = LazyKt.lazy(new Function0<SharedPreferences>() { // from class: com.jkcq.base.app.Preference$Companion$prefs$2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        public final SharedPreferences invoke() {
            return BaseApp.INSTANCE.getSApplicaton().getSharedPreferences(Preference.file_name, 0);
        }
    });

    public Preference(String name, T t) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        this.name = name;
        this.default = t;
    }

    public final String getName() {
        return this.name;
    }

    /* compiled from: Preference.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082D¢\u0006\u0002\n\u0000R\u001b\u0010\u000b\u001a\u00020\f8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lcom/jkcq/base/app/Preference$Companion;", "", "()V", "BIKEMAC", "", "BIKENAME", "BIKEVERSION", "USER_ID", "USER_NAME", Preference.clicklaug, "file_name", "prefs", "Landroid/content/SharedPreferences;", "getPrefs", "()Landroid/content/SharedPreferences;", "prefs$delegate", "Lkotlin/Lazy;", Preference.selecttype, "contains", "", "key", "base_release"}, k = 1, mv = {1, 1, 16})
    public static final class Companion {
        /* JADX INFO: Access modifiers changed from: private */
        public final SharedPreferences getPrefs() {
            Lazy lazy = Preference.prefs$delegate;
            Companion companion = Preference.INSTANCE;
            return (SharedPreferences) lazy.getValue();
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final boolean contains(String key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            return getPrefs().contains(key);
        }
    }

    public final T getValue(Object thisRef, KProperty<?> property) {
        Intrinsics.checkParameterIsNotNull(property, "property");
        return getSharedPreferences(this.name, this.default);
    }

    public final void setValue(Object thisRef, KProperty<?> property, T value) {
        Intrinsics.checkParameterIsNotNull(property, "property");
        putSharedPreferences(this.name, value);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void putSharedPreferences(String name, T value) {
        SharedPreferences.Editor editorPutFloat;
        SharedPreferences.Editor editorEdit = INSTANCE.getPrefs().edit();
        if (value instanceof Long) {
            editorPutFloat = editorEdit.putLong(name, ((Number) value).longValue());
        } else if (value instanceof String) {
            editorPutFloat = editorEdit.putString(name, (String) value);
        } else if (value instanceof Integer) {
            editorPutFloat = editorEdit.putInt(name, ((Number) value).intValue());
        } else if (value instanceof Boolean) {
            editorPutFloat = editorEdit.putBoolean(name, ((Boolean) value).booleanValue());
        } else {
            editorPutFloat = value instanceof Float ? editorEdit.putFloat(name, ((Number) value).floatValue()) : editorEdit.putString(name, serialize(value));
        }
        editorPutFloat.apply();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final T getSharedPreferences(String name, T t) {
        SharedPreferences prefs = INSTANCE.getPrefs();
        if (t instanceof Long) {
            return (T) Long.valueOf(prefs.getLong(name, ((Number) t).longValue()));
        }
        if (t instanceof String) {
            T t2 = (T) prefs.getString(name, (String) t);
            if (t2 == null) {
                Intrinsics.throwNpe();
            }
            Intrinsics.checkExpressionValueIsNotNull(t2, "getString(name, default)!!");
            return t2;
        }
        if (t instanceof Integer) {
            return (T) Integer.valueOf(prefs.getInt(name, ((Number) t).intValue()));
        }
        if (t instanceof Boolean) {
            return (T) Boolean.valueOf(prefs.getBoolean(name, ((Boolean) t).booleanValue()));
        }
        if (t instanceof Float) {
            return (T) Float.valueOf(prefs.getFloat(name, ((Number) t).floatValue()));
        }
        String string = prefs.getString(name, serialize(t));
        if (string == null) {
            Intrinsics.throwNpe();
        }
        Intrinsics.checkExpressionValueIsNotNull(string, "getString(name, serialize(default))!!");
        return (T) deSerialization(string);
    }

    private final <A> String serialize(A obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        String serStr = URLEncoder.encode(byteArrayOutputStream.toString("ISO-8859-1"), Key.STRING_CHARSET_NAME);
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Intrinsics.checkExpressionValueIsNotNull(serStr, "serStr");
        return serStr;
    }

    private final <A> A deSerialization(String str) throws IOException, ClassNotFoundException {
        String redStr = URLDecoder.decode(str, Key.STRING_CHARSET_NAME);
        Intrinsics.checkExpressionValueIsNotNull(redStr, "redStr");
        Charset charsetForName = Charset.forName("ISO-8859-1");
        Intrinsics.checkExpressionValueIsNotNull(charsetForName, "Charset.forName(charsetName)");
        if (redStr != null) {
            byte[] bytes = redStr.getBytes(charsetForName);
            Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            A a = (A) objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
            return a;
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
    }
}
