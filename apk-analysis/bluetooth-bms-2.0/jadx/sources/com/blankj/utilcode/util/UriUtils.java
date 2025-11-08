package com.blankj.utilcode.util;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import no.nordicsemi.android.log.LogContract;

/* loaded from: classes.dex */
public final class UriUtils {
    private UriUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static Uri res2Uri(String str) {
        return Uri.parse("android.resource://" + Utils.getApp().getPackageName() + "/" + str);
    }

    public static Uri file2Uri(File file) {
        if (file == null) {
            throw new NullPointerException("Argument 'file' of type File (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(Utils.getApp(), Utils.getApp().getPackageName() + ".utilcode.provider", file);
        }
        return Uri.fromFile(file);
    }

    public static File uri2File(Uri uri) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, SecurityException, ArrayIndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException {
        if (uri == null) {
            throw new NullPointerException("Argument 'uri' of type Uri (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        File fileUri2FileReal = uri2FileReal(uri);
        return fileUri2FileReal != null ? fileUri2FileReal : copyUri2Cache(uri);
    }

    private static File uri2FileReal(Uri uri) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, SecurityException, ArrayIndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException {
        Uri uri2;
        File fileFromUri;
        String str;
        File file;
        if (uri == null) {
            throw new NullPointerException("Argument 'uri' of type Uri (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Log.d("UriUtils", uri.toString());
        String authority = uri.getAuthority();
        String scheme = uri.getScheme();
        String path = uri.getPath();
        if (Build.VERSION.SDK_INT >= 24 && path != null) {
            String[] strArr = {"/external/", "/external_path/"};
            for (int i = 0; i < 2; i++) {
                String str2 = strArr[i];
                if (path.startsWith(str2)) {
                    File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + path.replace(str2, "/"));
                    if (file2.exists()) {
                        Log.d("UriUtils", uri.toString() + " -> " + str2);
                        return file2;
                    }
                }
            }
            if (path.startsWith("/files_path/")) {
                file = new File(Utils.getApp().getFilesDir().getAbsolutePath() + path.replace("/files_path/", "/"));
            } else if (path.startsWith("/cache_path/")) {
                file = new File(Utils.getApp().getCacheDir().getAbsolutePath() + path.replace("/cache_path/", "/"));
            } else if (path.startsWith("/external_files_path/")) {
                file = new File(Utils.getApp().getExternalFilesDir(null).getAbsolutePath() + path.replace("/external_files_path/", "/"));
            } else if (path.startsWith("/external_cache_path/")) {
                file = new File(Utils.getApp().getExternalCacheDir().getAbsolutePath() + path.replace("/external_cache_path/", "/"));
            } else {
                file = null;
            }
            if (file != null && file.exists()) {
                Log.d("UriUtils", uri.toString() + " -> " + path);
                return file;
            }
        }
        if ("file".equals(scheme)) {
            if (path != null) {
                return new File(path);
            }
            Log.d("UriUtils", uri.toString() + " parse failed. -> 0");
            return null;
        }
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(Utils.getApp(), uri)) {
            if ("com.android.externalstorage.documents".equals(authority)) {
                String[] strArrSplit = DocumentsContract.getDocumentId(uri).split(":");
                String str3 = strArrSplit[0];
                if ("primary".equalsIgnoreCase(str3)) {
                    return new File(Environment.getExternalStorageDirectory() + "/" + strArrSplit[1]);
                }
                StorageManager storageManager = (StorageManager) Utils.getApp().getSystemService("storage");
                try {
                    Class<?> cls = Class.forName("android.os.storage.StorageVolume");
                    Method method = storageManager.getClass().getMethod("getVolumeList", new Class[0]);
                    Method method2 = cls.getMethod("getUuid", new Class[0]);
                    Method method3 = cls.getMethod("getState", new Class[0]);
                    Method method4 = cls.getMethod("getPath", new Class[0]);
                    Method method5 = cls.getMethod("isPrimary", new Class[0]);
                    Method method6 = cls.getMethod("isEmulated", new Class[0]);
                    Object objInvoke = method.invoke(storageManager, new Object[0]);
                    int length = Array.getLength(objInvoke);
                    int i2 = 0;
                    while (i2 < length) {
                        Object obj = Array.get(objInvoke, i2);
                        Object obj2 = objInvoke;
                        if (("mounted".equals(method3.invoke(obj, new Object[0])) || "mounted_ro".equals(method3.invoke(obj, new Object[0]))) && ((!((Boolean) method5.invoke(obj, new Object[0])).booleanValue() || !((Boolean) method6.invoke(obj, new Object[0])).booleanValue()) && (str = (String) method2.invoke(obj, new Object[0])) != null && str.equals(str3))) {
                            return new File(method4.invoke(obj, new Object[0]) + "/" + strArrSplit[1]);
                        }
                        i2++;
                        objInvoke = obj2;
                    }
                } catch (Exception e) {
                    Log.d("UriUtils", uri.toString() + " parse failed. " + e.toString() + " -> 1_0");
                }
                Log.d("UriUtils", uri.toString() + " parse failed. -> 1_0");
                return null;
            }
            if ("com.android.providers.downloads.documents".equals(authority)) {
                String documentId = DocumentsContract.getDocumentId(uri);
                if (TextUtils.isEmpty(documentId)) {
                    Log.d("UriUtils", uri.toString() + " parse failed(id is null). -> 1_1");
                    return null;
                }
                if (documentId.startsWith("raw:")) {
                    return new File(documentId.substring(4));
                }
                String[] strArr2 = {"content://downloads/public_downloads", "content://downloads/all_downloads", "content://downloads/my_downloads"};
                for (int i3 = 0; i3 < 3; i3++) {
                    try {
                        fileFromUri = getFileFromUri(ContentUris.withAppendedId(Uri.parse(strArr2[i3]), Long.valueOf(documentId).longValue()), "1_1");
                    } catch (Exception unused) {
                    }
                    if (fileFromUri != null) {
                        return fileFromUri;
                    }
                }
                Log.d("UriUtils", uri.toString() + " parse failed. -> 1_1");
                return null;
            }
            if ("com.android.providers.media.documents".equals(authority)) {
                String[] strArrSplit2 = DocumentsContract.getDocumentId(uri).split(":");
                String str4 = strArrSplit2[0];
                if ("image".equals(str4)) {
                    uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(str4)) {
                    uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else {
                    if (!"audio".equals(str4)) {
                        Log.d("UriUtils", uri.toString() + " parse failed. -> 1_2");
                        return null;
                    }
                    uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                return getFileFromUri(uri2, "_id=?", new String[]{strArrSplit2[1]}, "1_2");
            }
            if (LogContract.Session.Content.CONTENT.equals(scheme)) {
                return getFileFromUri(uri, "1_3");
            }
            Log.d("UriUtils", uri.toString() + " parse failed. -> 1_4");
            return null;
        }
        if (LogContract.Session.Content.CONTENT.equals(scheme)) {
            return getFileFromUri(uri, ExifInterface.GPS_MEASUREMENT_2D);
        }
        Log.d("UriUtils", uri.toString() + " parse failed. -> 3");
        return null;
    }

    private static File getFileFromUri(Uri uri, String str) {
        return getFileFromUri(uri, null, null, str);
    }

    private static File getFileFromUri(Uri uri, String str, String[] strArr, String str2) {
        if ("com.google.android.apps.photos.content".equals(uri.getAuthority())) {
            if (!TextUtils.isEmpty(uri.getLastPathSegment())) {
                return new File(uri.getLastPathSegment());
            }
        } else if ("com.tencent.mtt.fileprovider".equals(uri.getAuthority())) {
            String path = uri.getPath();
            if (!TextUtils.isEmpty(path)) {
                return new File(Environment.getExternalStorageDirectory(), path.substring(10, path.length()));
            }
        } else if ("com.huawei.hidisk.fileprovider".equals(uri.getAuthority())) {
            String path2 = uri.getPath();
            if (!TextUtils.isEmpty(path2)) {
                return new File(path2.replace("/root", ""));
            }
        }
        Cursor cursorQuery = Utils.getApp().getContentResolver().query(uri, new String[]{"_data"}, str, strArr, null);
        try {
            if (cursorQuery == null) {
                Log.d("UriUtils", uri.toString() + " parse failed(cursor is null). -> " + str2);
                return null;
            }
            if (!cursorQuery.moveToFirst()) {
                Log.d("UriUtils", uri.toString() + " parse failed(moveToFirst return false). -> " + str2);
                return null;
            }
            int columnIndex = cursorQuery.getColumnIndex("_data");
            if (columnIndex > -1) {
                return new File(cursorQuery.getString(columnIndex));
            }
            Log.d("UriUtils", uri.toString() + " parse failed(columnIndex: " + columnIndex + " is wrong). -> " + str2);
            return null;
        } catch (Exception unused) {
            Log.d("UriUtils", uri.toString() + " parse failed. -> " + str2);
            return null;
        } finally {
            cursorQuery.close();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x0062 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.io.File copyUri2Cache(android.net.Uri r7) throws java.lang.Throwable {
        /*
            java.lang.String r0 = "UriUtils"
            java.lang.String r1 = "copyUri2Cache() called"
            android.util.Log.d(r0, r1)
            r0 = 0
            android.app.Application r1 = com.blankj.utilcode.util.Utils.getApp()     // Catch: java.lang.Throwable -> L4a java.io.FileNotFoundException -> L4f
            android.content.ContentResolver r1 = r1.getContentResolver()     // Catch: java.lang.Throwable -> L4a java.io.FileNotFoundException -> L4f
            java.io.InputStream r7 = r1.openInputStream(r7)     // Catch: java.lang.Throwable -> L4a java.io.FileNotFoundException -> L4f
            java.io.File r1 = new java.io.File     // Catch: java.io.FileNotFoundException -> L48 java.lang.Throwable -> L5f
            android.app.Application r2 = com.blankj.utilcode.util.Utils.getApp()     // Catch: java.io.FileNotFoundException -> L48 java.lang.Throwable -> L5f
            java.io.File r2 = r2.getCacheDir()     // Catch: java.io.FileNotFoundException -> L48 java.lang.Throwable -> L5f
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.io.FileNotFoundException -> L48 java.lang.Throwable -> L5f
            r3.<init>()     // Catch: java.io.FileNotFoundException -> L48 java.lang.Throwable -> L5f
            java.lang.String r4 = ""
            r3.append(r4)     // Catch: java.io.FileNotFoundException -> L48 java.lang.Throwable -> L5f
            long r4 = java.lang.System.currentTimeMillis()     // Catch: java.io.FileNotFoundException -> L48 java.lang.Throwable -> L5f
            r3.append(r4)     // Catch: java.io.FileNotFoundException -> L48 java.lang.Throwable -> L5f
            java.lang.String r3 = r3.toString()     // Catch: java.io.FileNotFoundException -> L48 java.lang.Throwable -> L5f
            r1.<init>(r2, r3)     // Catch: java.io.FileNotFoundException -> L48 java.lang.Throwable -> L5f
            java.lang.String r2 = r1.getAbsolutePath()     // Catch: java.io.FileNotFoundException -> L48 java.lang.Throwable -> L5f
            com.blankj.utilcode.util.UtilsBridge.writeFileFromIS(r2, r7)     // Catch: java.io.FileNotFoundException -> L48 java.lang.Throwable -> L5f
            if (r7 == 0) goto L47
            r7.close()     // Catch: java.io.IOException -> L43
            goto L47
        L43:
            r7 = move-exception
            r7.printStackTrace()
        L47:
            return r1
        L48:
            r1 = move-exception
            goto L51
        L4a:
            r7 = move-exception
            r6 = r0
            r0 = r7
            r7 = r6
            goto L60
        L4f:
            r1 = move-exception
            r7 = r0
        L51:
            r1.printStackTrace()     // Catch: java.lang.Throwable -> L5f
            if (r7 == 0) goto L5e
            r7.close()     // Catch: java.io.IOException -> L5a
            goto L5e
        L5a:
            r7 = move-exception
            r7.printStackTrace()
        L5e:
            return r0
        L5f:
            r0 = move-exception
        L60:
            if (r7 == 0) goto L6a
            r7.close()     // Catch: java.io.IOException -> L66
            goto L6a
        L66:
            r7 = move-exception
            r7.printStackTrace()
        L6a:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blankj.utilcode.util.UriUtils.copyUri2Cache(android.net.Uri):java.io.File");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:35:0x003e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r4v0, types: [android.net.Uri] */
    /* JADX WARN: Type inference failed for: r4v2 */
    /* JADX WARN: Type inference failed for: r4v4, types: [java.io.InputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static byte[] uri2Bytes(android.net.Uri r4) throws java.lang.Throwable {
        /*
            r0 = 0
            android.app.Application r1 = com.blankj.utilcode.util.Utils.getApp()     // Catch: java.lang.Throwable -> L1e java.io.FileNotFoundException -> L23
            android.content.ContentResolver r1 = r1.getContentResolver()     // Catch: java.lang.Throwable -> L1e java.io.FileNotFoundException -> L23
            java.io.InputStream r4 = r1.openInputStream(r4)     // Catch: java.lang.Throwable -> L1e java.io.FileNotFoundException -> L23
            byte[] r0 = com.blankj.utilcode.util.UtilsBridge.inputStream2Bytes(r4)     // Catch: java.io.FileNotFoundException -> L1c java.lang.Throwable -> L3b
            if (r4 == 0) goto L1b
            r4.close()     // Catch: java.io.IOException -> L17
            goto L1b
        L17:
            r4 = move-exception
            r4.printStackTrace()
        L1b:
            return r0
        L1c:
            r1 = move-exception
            goto L25
        L1e:
            r4 = move-exception
            r3 = r0
            r0 = r4
            r4 = r3
            goto L3c
        L23:
            r1 = move-exception
            r4 = r0
        L25:
            r1.printStackTrace()     // Catch: java.lang.Throwable -> L3b
            java.lang.String r1 = "UriUtils"
            java.lang.String r2 = "uri to bytes failed."
            android.util.Log.d(r1, r2)     // Catch: java.lang.Throwable -> L3b
            if (r4 == 0) goto L3a
            r4.close()     // Catch: java.io.IOException -> L36
            goto L3a
        L36:
            r4 = move-exception
            r4.printStackTrace()
        L3a:
            return r0
        L3b:
            r0 = move-exception
        L3c:
            if (r4 == 0) goto L46
            r4.close()     // Catch: java.io.IOException -> L42
            goto L46
        L42:
            r4 = move-exception
            r4.printStackTrace()
        L46:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blankj.utilcode.util.UriUtils.uri2Bytes(android.net.Uri):byte[]");
    }
}
