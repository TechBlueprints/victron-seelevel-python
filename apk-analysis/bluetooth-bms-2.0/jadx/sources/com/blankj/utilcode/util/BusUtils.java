package com.blankj.utilcode.util;

import android.util.Log;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/* loaded from: classes.dex */
public final class BusUtils {
    private static final Object NULL = "nULl";
    private static final String TAG = "BusUtils";
    private final Map<String, Set<Object>> mClassName_BusesMap;
    private final Map<String, Map<String, Object>> mClassName_Tag_Arg4StickyMap;
    private final Map<String, List<String>> mClassName_TagsMap;
    private final Map<String, List<BusInfo>> mTag_BusInfoListMap;

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.CLASS)
    public @interface Bus {
        int priority() default 0;

        boolean sticky() default false;

        String tag();

        ThreadMode threadMode() default ThreadMode.POSTING;
    }

    public enum ThreadMode {
        MAIN,
        IO,
        CPU,
        CACHED,
        SINGLE,
        POSTING
    }

    private void init() {
    }

    private BusUtils() {
        this.mTag_BusInfoListMap = new HashMap();
        this.mClassName_BusesMap = new ConcurrentHashMap();
        this.mClassName_TagsMap = new ConcurrentHashMap();
        this.mClassName_Tag_Arg4StickyMap = new ConcurrentHashMap();
        init();
    }

    private void registerBus(String str, String str2, String str3, String str4, String str5, boolean z, String str6) {
        registerBus(str, str2, str3, str4, str5, z, str6, 0);
    }

    private void registerBus(String str, String str2, String str3, String str4, String str5, boolean z, String str6, int i) {
        List<BusInfo> arrayList = this.mTag_BusInfoListMap.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            this.mTag_BusInfoListMap.put(str, arrayList);
        }
        arrayList.add(new BusInfo(str2, str3, str4, str5, z, str6, i));
    }

    public static void register(Object obj) {
        getInstance().registerInner(obj);
    }

    public static void unregister(Object obj) {
        getInstance().unregisterInner(obj);
    }

    public static void post(String str) {
        post(str, NULL);
    }

    public static void post(String str, Object obj) {
        getInstance().postInner(str, obj);
    }

    public static void postSticky(String str) {
        postSticky(str, NULL);
    }

    public static void postSticky(String str, Object obj) {
        getInstance().postStickyInner(str, obj);
    }

    public static void removeSticky(String str) {
        getInstance().removeStickyInner(str);
    }

    public static String toString_() {
        return getInstance().toString();
    }

    public String toString() {
        return "BusUtils: " + this.mTag_BusInfoListMap;
    }

    private static BusUtils getInstance() {
        return LazyHolder.INSTANCE;
    }

    private void registerInner(Object obj) {
        if (obj == null) {
            return;
        }
        Class<?> cls = obj.getClass();
        String name = cls.getName();
        synchronized (this.mClassName_BusesMap) {
            Set<Object> copyOnWriteArraySet = this.mClassName_BusesMap.get(name);
            if (copyOnWriteArraySet == null) {
                copyOnWriteArraySet = new CopyOnWriteArraySet<>();
                this.mClassName_BusesMap.put(name, copyOnWriteArraySet);
            }
            copyOnWriteArraySet.add(obj);
        }
        if (this.mClassName_TagsMap.get(name) == null) {
            synchronized (this.mClassName_TagsMap) {
                if (this.mClassName_TagsMap.get(name) == null) {
                    CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
                    for (Map.Entry<String, List<BusInfo>> entry : this.mTag_BusInfoListMap.entrySet()) {
                        for (BusInfo busInfo : entry.getValue()) {
                            try {
                                if (Class.forName(busInfo.className).isAssignableFrom(cls)) {
                                    copyOnWriteArrayList.add(entry.getKey());
                                    busInfo.subClassNames.add(name);
                                }
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    this.mClassName_TagsMap.put(name, copyOnWriteArrayList);
                }
            }
        }
        processSticky(obj);
    }

    private void processSticky(Object obj) {
        Map<String, Object> map = this.mClassName_Tag_Arg4StickyMap.get(obj.getClass().getName());
        if (map == null) {
            return;
        }
        synchronized (this.mClassName_Tag_Arg4StickyMap) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                postStickyInner(entry.getKey(), entry.getValue(), true);
            }
        }
    }

    private void unregisterInner(Object obj) {
        if (obj == null) {
            return;
        }
        String name = obj.getClass().getName();
        synchronized (this.mClassName_BusesMap) {
            Set<Object> set = this.mClassName_BusesMap.get(name);
            if (set != null && set.contains(obj)) {
                set.remove(obj);
                return;
            }
            Log.e(TAG, "The bus of <" + obj + "> was not registered before.");
        }
    }

    private void postInner(String str, Object obj) {
        postInner(str, obj, false);
    }

    private void postInner(String str, Object obj, boolean z) {
        List<BusInfo> list = this.mTag_BusInfoListMap.get(str);
        if (list == null) {
            Log.e(TAG, "The bus of tag <" + str + "> is not exists.");
            return;
        }
        Iterator<BusInfo> it = list.iterator();
        while (it.hasNext()) {
            invokeBus(str, obj, it.next(), z);
        }
    }

    private void invokeBus(String str, Object obj, BusInfo busInfo, boolean z) {
        if (busInfo.method == null) {
            Method methodByBusInfo = getMethodByBusInfo(busInfo);
            if (methodByBusInfo == null) {
                return;
            } else {
                busInfo.method = methodByBusInfo;
            }
        }
        invokeMethod(str, obj, busInfo, z);
    }

    private Method getMethodByBusInfo(BusInfo busInfo) {
        try {
            return "".equals(busInfo.paramType) ? Class.forName(busInfo.className).getDeclaredMethod(busInfo.funName, new Class[0]) : Class.forName(busInfo.className).getDeclaredMethod(busInfo.funName, getClassName(busInfo.paramType));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0058  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.Class getClassName(java.lang.String r2) throws java.lang.ClassNotFoundException {
        /*
            r1 = this;
            int r0 = r2.hashCode()
            switch(r0) {
                case -1325958191: goto L4e;
                case 104431: goto L44;
                case 3039496: goto L3a;
                case 3052374: goto L30;
                case 3327612: goto L26;
                case 64711720: goto L1c;
                case 97526364: goto L12;
                case 109413500: goto L8;
                default: goto L7;
            }
        L7:
            goto L58
        L8:
            java.lang.String r0 = "short"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L58
            r0 = 3
            goto L59
        L12:
            java.lang.String r0 = "float"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L58
            r0 = 6
            goto L59
        L1c:
            java.lang.String r0 = "boolean"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L58
            r0 = 0
            goto L59
        L26:
            java.lang.String r0 = "long"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L58
            r0 = 2
            goto L59
        L30:
            java.lang.String r0 = "char"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L58
            r0 = 7
            goto L59
        L3a:
            java.lang.String r0 = "byte"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L58
            r0 = 4
            goto L59
        L44:
            java.lang.String r0 = "int"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L58
            r0 = 1
            goto L59
        L4e:
            java.lang.String r0 = "double"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L58
            r0 = 5
            goto L59
        L58:
            r0 = -1
        L59:
            switch(r0) {
                case 0: goto L76;
                case 1: goto L73;
                case 2: goto L70;
                case 3: goto L6d;
                case 4: goto L6a;
                case 5: goto L67;
                case 6: goto L64;
                case 7: goto L61;
                default: goto L5c;
            }
        L5c:
            java.lang.Class r2 = java.lang.Class.forName(r2)
            return r2
        L61:
            java.lang.Class r2 = java.lang.Character.TYPE
            return r2
        L64:
            java.lang.Class r2 = java.lang.Float.TYPE
            return r2
        L67:
            java.lang.Class r2 = java.lang.Double.TYPE
            return r2
        L6a:
            java.lang.Class r2 = java.lang.Byte.TYPE
            return r2
        L6d:
            java.lang.Class r2 = java.lang.Short.TYPE
            return r2
        L70:
            java.lang.Class r2 = java.lang.Long.TYPE
            return r2
        L73:
            java.lang.Class r2 = java.lang.Integer.TYPE
            return r2
        L76:
            java.lang.Class r2 = java.lang.Boolean.TYPE
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blankj.utilcode.util.BusUtils.getClassName(java.lang.String):java.lang.Class");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:20:0x004b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void invokeMethod(final java.lang.String r8, final java.lang.Object r9, final com.blankj.utilcode.util.BusUtils.BusInfo r10, final boolean r11) {
        /*
            r7 = this;
            com.blankj.utilcode.util.BusUtils$1 r6 = new com.blankj.utilcode.util.BusUtils$1
            r0 = r6
            r1 = r7
            r2 = r8
            r3 = r9
            r4 = r10
            r5 = r11
            r0.<init>()
            java.lang.String r8 = r10.threadMode
            int r9 = r8.hashCode()
            r10 = 4
            r11 = 3
            r0 = 2
            r1 = 1
            switch(r9) {
                case -1848936376: goto L41;
                case 2342: goto L37;
                case 66952: goto L2d;
                case 2358713: goto L23;
                case 1980249378: goto L19;
                default: goto L18;
            }
        L18:
            goto L4b
        L19:
            java.lang.String r9 = "CACHED"
            boolean r8 = r8.equals(r9)
            if (r8 == 0) goto L4b
            r8 = 3
            goto L4c
        L23:
            java.lang.String r9 = "MAIN"
            boolean r8 = r8.equals(r9)
            if (r8 == 0) goto L4b
            r8 = 0
            goto L4c
        L2d:
            java.lang.String r9 = "CPU"
            boolean r8 = r8.equals(r9)
            if (r8 == 0) goto L4b
            r8 = 2
            goto L4c
        L37:
            java.lang.String r9 = "IO"
            boolean r8 = r8.equals(r9)
            if (r8 == 0) goto L4b
            r8 = 1
            goto L4c
        L41:
            java.lang.String r9 = "SINGLE"
            boolean r8 = r8.equals(r9)
            if (r8 == 0) goto L4b
            r8 = 4
            goto L4c
        L4b:
            r8 = -1
        L4c:
            if (r8 == 0) goto L7a
            if (r8 == r1) goto L72
            if (r8 == r0) goto L6a
            if (r8 == r11) goto L62
            if (r8 == r10) goto L5a
            r6.run()
            return
        L5a:
            java.util.concurrent.ExecutorService r8 = com.blankj.utilcode.util.ThreadUtils.getSinglePool()
            r8.execute(r6)
            return
        L62:
            java.util.concurrent.ExecutorService r8 = com.blankj.utilcode.util.ThreadUtils.getCachedPool()
            r8.execute(r6)
            return
        L6a:
            java.util.concurrent.ExecutorService r8 = com.blankj.utilcode.util.ThreadUtils.getCpuPool()
            r8.execute(r6)
            return
        L72:
            java.util.concurrent.ExecutorService r8 = com.blankj.utilcode.util.ThreadUtils.getIoPool()
            r8.execute(r6)
            return
        L7a:
            com.blankj.utilcode.util.ThreadUtils.runOnUiThread(r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blankj.utilcode.util.BusUtils.invokeMethod(java.lang.String, java.lang.Object, com.blankj.utilcode.util.BusUtils$BusInfo, boolean):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void realInvokeMethod(String str, Object obj, BusInfo busInfo, boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        HashSet hashSet = new HashSet();
        Iterator<String> it = busInfo.subClassNames.iterator();
        while (it.hasNext()) {
            Set<Object> set = this.mClassName_BusesMap.get(it.next());
            if (set != null && !set.isEmpty()) {
                hashSet.addAll(set);
            }
        }
        if (hashSet.size() == 0) {
            if (z) {
                return;
            }
            Log.e(TAG, "The bus of tag <" + str + "> was not registered before.");
            return;
        }
        try {
            if (obj == NULL) {
                Iterator it2 = hashSet.iterator();
                while (it2.hasNext()) {
                    busInfo.method.invoke(it2.next(), new Object[0]);
                }
            } else {
                Iterator it3 = hashSet.iterator();
                while (it3.hasNext()) {
                    busInfo.method.invoke(it3.next(), obj);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        }
    }

    private void postStickyInner(String str, Object obj) {
        postStickyInner(str, obj, false);
    }

    private void postStickyInner(String str, Object obj, boolean z) {
        List<BusInfo> list = this.mTag_BusInfoListMap.get(str);
        if (list == null) {
            Log.e(TAG, "The bus of tag <" + str + "> is not exists.");
            return;
        }
        for (BusInfo busInfo : list) {
            if (busInfo.sticky) {
                synchronized (this.mClassName_Tag_Arg4StickyMap) {
                    Map<String, Object> map = this.mClassName_Tag_Arg4StickyMap.get(busInfo.className);
                    if (map == null) {
                        map = new HashMap<>();
                        this.mClassName_Tag_Arg4StickyMap.put(busInfo.className, map);
                    }
                    map.put(str, obj);
                }
                invokeBus(str, obj, busInfo, true);
            } else if (!z) {
                invokeBus(str, obj, busInfo, false);
            }
        }
    }

    private void removeStickyInner(String str) {
        List<BusInfo> list = this.mTag_BusInfoListMap.get(str);
        if (list == null) {
            Log.e(TAG, "The bus of tag <" + str + "> is not exists.");
            return;
        }
        for (BusInfo busInfo : list) {
            if (!busInfo.sticky) {
                Log.e(TAG, "The bus of tag <" + str + "> is not sticky.");
                return;
            }
            synchronized (this.mClassName_Tag_Arg4StickyMap) {
                Map<String, Object> map = this.mClassName_Tag_Arg4StickyMap.get(busInfo.className);
                if (map != null && map.containsKey(str)) {
                    map.remove(str);
                }
                Log.e(TAG, "The sticky bus of tag <" + str + "> didn't post.");
                return;
            }
        }
    }

    static void registerBus4Test(String str, String str2, String str3, String str4, String str5, boolean z, String str6, int i) {
        getInstance().registerBus(str, str2, str3, str4, str5, z, str6, i);
    }

    private static final class BusInfo {
        String className;
        String funName;
        Method method;
        String paramName;
        String paramType;
        int priority;
        boolean sticky;
        List<String> subClassNames = new CopyOnWriteArrayList();
        String threadMode;

        BusInfo(String str, String str2, String str3, String str4, boolean z, String str5, int i) {
            this.className = str;
            this.funName = str2;
            this.paramType = str3;
            this.paramName = str4;
            this.sticky = z;
            this.threadMode = str5;
            this.priority = i;
        }

        public String toString() {
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("BusInfo { desc: ");
            sb.append(this.className);
            sb.append("#");
            sb.append(this.funName);
            if ("".equals(this.paramType)) {
                str = "()";
            } else {
                str = "(" + this.paramType + " " + this.paramName + ")";
            }
            sb.append(str);
            sb.append(", sticky: ");
            sb.append(this.sticky);
            sb.append(", threadMode: ");
            sb.append(this.threadMode);
            sb.append(", method: ");
            sb.append(this.method);
            sb.append(", priority: ");
            sb.append(this.priority);
            sb.append(" }");
            return sb.toString();
        }
    }

    private static class LazyHolder {
        private static final BusUtils INSTANCE = new BusUtils();

        private LazyHolder() {
        }
    }
}
