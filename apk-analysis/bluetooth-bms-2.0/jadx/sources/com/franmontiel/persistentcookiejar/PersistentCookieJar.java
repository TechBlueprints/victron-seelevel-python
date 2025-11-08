package com.franmontiel.persistentcookiejar;

import com.franmontiel.persistentcookiejar.cache.CookieCache;
import com.franmontiel.persistentcookiejar.persistence.CookiePersistor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

/* loaded from: classes.dex */
public class PersistentCookieJar implements ClearableCookieJar {
    private CookieCache cache;
    private CookiePersistor persistor;

    public PersistentCookieJar(CookieCache cookieCache, CookiePersistor cookiePersistor) {
        this.cache = cookieCache;
        this.persistor = cookiePersistor;
        cookieCache.addAll(cookiePersistor.loadAll());
    }

    @Override // okhttp3.CookieJar
    public synchronized void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        this.cache.addAll(list);
        this.persistor.saveAll(filterPersistentCookies(list));
    }

    private static List<Cookie> filterPersistentCookies(List<Cookie> list) {
        ArrayList arrayList = new ArrayList();
        for (Cookie cookie : list) {
            if (cookie.persistent()) {
                arrayList.add(cookie);
            }
        }
        return arrayList;
    }

    @Override // okhttp3.CookieJar
    public synchronized List<Cookie> loadForRequest(HttpUrl httpUrl) {
        ArrayList arrayList;
        ArrayList arrayList2 = new ArrayList();
        arrayList = new ArrayList();
        Iterator<Cookie> it = this.cache.iterator();
        while (it.hasNext()) {
            Cookie next = it.next();
            if (isCookieExpired(next)) {
                arrayList2.add(next);
                it.remove();
            } else if (next.matches(httpUrl)) {
                arrayList.add(next);
            }
        }
        this.persistor.removeAll(arrayList2);
        return arrayList;
    }

    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    @Override // com.franmontiel.persistentcookiejar.ClearableCookieJar
    public synchronized void clearSession() {
        this.cache.clear();
        this.cache.addAll(this.persistor.loadAll());
    }

    @Override // com.franmontiel.persistentcookiejar.ClearableCookieJar
    public synchronized void clear() {
        this.cache.clear();
        this.persistor.clear();
    }
}
