package com.franmontiel.persistentcookiejar.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import okhttp3.Cookie;

/* loaded from: classes.dex */
public class SharedPrefsCookiePersistor implements CookiePersistor {
    private final SharedPreferences sharedPreferences;

    public SharedPrefsCookiePersistor(Context context) {
        this(context.getSharedPreferences("CookiePersistence", 0));
    }

    public SharedPrefsCookiePersistor(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override // com.franmontiel.persistentcookiejar.persistence.CookiePersistor
    public List<Cookie> loadAll() throws Throwable {
        ArrayList arrayList = new ArrayList(this.sharedPreferences.getAll().size());
        Iterator<Map.Entry<String, ?>> it = this.sharedPreferences.getAll().entrySet().iterator();
        while (it.hasNext()) {
            Cookie cookieDecode = new SerializableCookie().decode((String) it.next().getValue());
            if (cookieDecode != null) {
                arrayList.add(cookieDecode);
            }
        }
        return arrayList;
    }

    @Override // com.franmontiel.persistentcookiejar.persistence.CookiePersistor
    public void saveAll(Collection<Cookie> collection) {
        SharedPreferences.Editor editorEdit = this.sharedPreferences.edit();
        for (Cookie cookie : collection) {
            editorEdit.putString(createCookieKey(cookie), new SerializableCookie().encode(cookie));
        }
        editorEdit.commit();
    }

    @Override // com.franmontiel.persistentcookiejar.persistence.CookiePersistor
    public void removeAll(Collection<Cookie> collection) {
        SharedPreferences.Editor editorEdit = this.sharedPreferences.edit();
        Iterator<Cookie> it = collection.iterator();
        while (it.hasNext()) {
            editorEdit.remove(createCookieKey(it.next()));
        }
        editorEdit.commit();
    }

    private static String createCookieKey(Cookie cookie) {
        StringBuilder sb = new StringBuilder();
        sb.append(cookie.secure() ? "https" : "http");
        sb.append("://");
        sb.append(cookie.domain());
        sb.append(cookie.path());
        sb.append("|");
        sb.append(cookie.name());
        return sb.toString();
    }

    @Override // com.franmontiel.persistentcookiejar.persistence.CookiePersistor
    public void clear() {
        this.sharedPreferences.edit().clear().commit();
    }
}
