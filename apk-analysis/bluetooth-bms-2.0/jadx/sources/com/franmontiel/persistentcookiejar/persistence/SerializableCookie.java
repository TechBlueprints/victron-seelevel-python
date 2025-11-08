package com.franmontiel.persistentcookiejar.persistence;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import kotlin.UByte;
import okhttp3.Cookie;

/* loaded from: classes.dex */
public class SerializableCookie implements Serializable {
    private static final long serialVersionUID = -8594045714036645534L;
    private transient Cookie cookie;
    private static final String TAG = SerializableCookie.class.getSimpleName();
    private static long NON_VALID_EXPIRES_AT = -1;

    /* JADX WARN: Removed duplicated region for block: B:32:0x0043 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String encode(okhttp3.Cookie r6) throws java.lang.Throwable {
        /*
            r5 = this;
            java.lang.String r0 = "Stream not closed in encodeCookie"
            r5.cookie = r6
            java.io.ByteArrayOutputStream r6 = new java.io.ByteArrayOutputStream
            r6.<init>()
            r1 = 0
            java.io.ObjectOutputStream r2 = new java.io.ObjectOutputStream     // Catch: java.lang.Throwable -> L27 java.io.IOException -> L29
            r2.<init>(r6)     // Catch: java.lang.Throwable -> L27 java.io.IOException -> L29
            r2.writeObject(r5)     // Catch: java.io.IOException -> L25 java.lang.Throwable -> L3f
            r2.close()     // Catch: java.io.IOException -> L16
            goto L1c
        L16:
            r1 = move-exception
            java.lang.String r2 = com.franmontiel.persistentcookiejar.persistence.SerializableCookie.TAG
            android.util.Log.d(r2, r0, r1)
        L1c:
            byte[] r6 = r6.toByteArray()
            java.lang.String r6 = byteArrayToHexString(r6)
            return r6
        L25:
            r6 = move-exception
            goto L2b
        L27:
            r6 = move-exception
            goto L41
        L29:
            r6 = move-exception
            r2 = r1
        L2b:
            java.lang.String r3 = com.franmontiel.persistentcookiejar.persistence.SerializableCookie.TAG     // Catch: java.lang.Throwable -> L3f
            java.lang.String r4 = "IOException in encodeCookie"
            android.util.Log.d(r3, r4, r6)     // Catch: java.lang.Throwable -> L3f
            if (r2 == 0) goto L3e
            r2.close()     // Catch: java.io.IOException -> L38
            goto L3e
        L38:
            r6 = move-exception
            java.lang.String r2 = com.franmontiel.persistentcookiejar.persistence.SerializableCookie.TAG
            android.util.Log.d(r2, r0, r6)
        L3e:
            return r1
        L3f:
            r6 = move-exception
            r1 = r2
        L41:
            if (r1 == 0) goto L4d
            r1.close()     // Catch: java.io.IOException -> L47
            goto L4d
        L47:
            r1 = move-exception
            java.lang.String r2 = com.franmontiel.persistentcookiejar.persistence.SerializableCookie.TAG
            android.util.Log.d(r2, r0, r1)
        L4d:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.franmontiel.persistentcookiejar.persistence.SerializableCookie.encode(okhttp3.Cookie):java.lang.String");
    }

    private static String byteArrayToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (byte b : bArr) {
            int i = b & UByte.MAX_VALUE;
            if (i < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(i));
        }
        return sb.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:34:0x004d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.io.ObjectInputStream] */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v13 */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v15 */
    /* JADX WARN: Type inference failed for: r2v16 */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:8:0x001e -> B:36:0x0049). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public okhttp3.Cookie decode(java.lang.String r6) throws java.lang.Throwable {
        /*
            r5 = this;
            java.lang.String r0 = "Stream not closed in decodeCookie"
            byte[] r6 = hexStringToByteArray(r6)
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream
            r1.<init>(r6)
            r6 = 0
            java.io.ObjectInputStream r2 = new java.io.ObjectInputStream     // Catch: java.lang.Throwable -> L28 java.lang.ClassNotFoundException -> L2c java.io.IOException -> L3b
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L28 java.lang.ClassNotFoundException -> L2c java.io.IOException -> L3b
            java.lang.Object r1 = r2.readObject()     // Catch: java.lang.ClassNotFoundException -> L24 java.io.IOException -> L26 java.lang.Throwable -> L4a
            com.franmontiel.persistentcookiejar.persistence.SerializableCookie r1 = (com.franmontiel.persistentcookiejar.persistence.SerializableCookie) r1     // Catch: java.lang.ClassNotFoundException -> L24 java.io.IOException -> L26 java.lang.Throwable -> L4a
            okhttp3.Cookie r6 = r1.cookie     // Catch: java.lang.ClassNotFoundException -> L24 java.io.IOException -> L26 java.lang.Throwable -> L4a
            r2.close()     // Catch: java.io.IOException -> L1d
            goto L49
        L1d:
            r1 = move-exception
            java.lang.String r2 = com.franmontiel.persistentcookiejar.persistence.SerializableCookie.TAG
            android.util.Log.d(r2, r0, r1)
            goto L49
        L24:
            r1 = move-exception
            goto L2e
        L26:
            r1 = move-exception
            goto L3d
        L28:
            r1 = move-exception
            r2 = r6
            r6 = r1
            goto L4b
        L2c:
            r1 = move-exception
            r2 = r6
        L2e:
            java.lang.String r3 = com.franmontiel.persistentcookiejar.persistence.SerializableCookie.TAG     // Catch: java.lang.Throwable -> L4a
            java.lang.String r4 = "ClassNotFoundException in decodeCookie"
            android.util.Log.d(r3, r4, r1)     // Catch: java.lang.Throwable -> L4a
            if (r2 == 0) goto L49
            r2.close()     // Catch: java.io.IOException -> L1d
            goto L49
        L3b:
            r1 = move-exception
            r2 = r6
        L3d:
            java.lang.String r3 = com.franmontiel.persistentcookiejar.persistence.SerializableCookie.TAG     // Catch: java.lang.Throwable -> L4a
            java.lang.String r4 = "IOException in decodeCookie"
            android.util.Log.d(r3, r4, r1)     // Catch: java.lang.Throwable -> L4a
            if (r2 == 0) goto L49
            r2.close()     // Catch: java.io.IOException -> L1d
        L49:
            return r6
        L4a:
            r6 = move-exception
        L4b:
            if (r2 == 0) goto L57
            r2.close()     // Catch: java.io.IOException -> L51
            goto L57
        L51:
            r1 = move-exception
            java.lang.String r2 = com.franmontiel.persistentcookiejar.persistence.SerializableCookie.TAG
            android.util.Log.d(r2, r0, r1)
        L57:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.franmontiel.persistentcookiejar.persistence.SerializableCookie.decode(java.lang.String):okhttp3.Cookie");
    }

    private static byte[] hexStringToByteArray(String str) {
        int length = str.length();
        byte[] bArr = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.cookie.name());
        objectOutputStream.writeObject(this.cookie.value());
        objectOutputStream.writeLong(this.cookie.persistent() ? this.cookie.expiresAt() : NON_VALID_EXPIRES_AT);
        objectOutputStream.writeObject(this.cookie.domain());
        objectOutputStream.writeObject(this.cookie.path());
        objectOutputStream.writeBoolean(this.cookie.secure());
        objectOutputStream.writeBoolean(this.cookie.httpOnly());
        objectOutputStream.writeBoolean(this.cookie.hostOnly());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        Cookie.Builder builder = new Cookie.Builder();
        builder.name((String) objectInputStream.readObject());
        builder.value((String) objectInputStream.readObject());
        long j = objectInputStream.readLong();
        if (j != NON_VALID_EXPIRES_AT) {
            builder.expiresAt(j);
        }
        String str = (String) objectInputStream.readObject();
        builder.domain(str);
        builder.path((String) objectInputStream.readObject());
        if (objectInputStream.readBoolean()) {
            builder.secure();
        }
        if (objectInputStream.readBoolean()) {
            builder.httpOnly();
        }
        if (objectInputStream.readBoolean()) {
            builder.hostOnlyDomain(str);
        }
        this.cookie = builder.build();
    }
}
