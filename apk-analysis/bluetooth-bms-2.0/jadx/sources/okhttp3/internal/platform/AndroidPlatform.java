package okhttp3.internal.platform;

import android.os.Build;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.cert.Certificate;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import no.nordicsemi.android.log.LogContract;
import okhttp3.Protocol;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.TrustRootIndex;

/* compiled from: AndroidPlatform.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0084\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 82\u00020\u0001:\u00046789B=\u0012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003\u0012\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0006\u0012\u0006\u0010\t\u001a\u00020\u0006¢\u0006\u0002\u0010\nJ$\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\n\u0010\u0011\u001a\u0006\u0012\u0002\b\u00030\u00032\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J$\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\n\u0010\u0011\u001a\u0006\u0012\u0002\b\u00030\u00032\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J(\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020!0 H\u0016J \u0010\"\u001a\u00020\u001c2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020(H\u0016J\u0012\u0010)\u001a\u0004\u0018\u00010\u00102\u0006\u0010#\u001a\u00020\u001eH\u0016J\u0012\u0010*\u001a\u0004\u0018\u00010\u00132\u0006\u0010+\u001a\u00020\u0010H\u0016J\u0010\u0010,\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\"\u0010-\u001a\u00020\u001c2\u0006\u0010.\u001a\u00020(2\u0006\u0010/\u001a\u00020\u00102\b\u00100\u001a\u0004\u0018\u000101H\u0016J\u001a\u00102\u001a\u00020\u001c2\u0006\u0010/\u001a\u00020\u00102\b\u00103\u001a\u0004\u0018\u00010\u0013H\u0016J\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u00104\u001a\u000205H\u0014R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006:"}, d2 = {"Lokhttp3/internal/platform/AndroidPlatform;", "Lokhttp3/internal/platform/Platform;", "sslParametersClass", "Ljava/lang/Class;", "sslSocketClass", "setUseSessionTickets", "Ljava/lang/reflect/Method;", "setHostname", "getAlpnSelectedProtocol", "setAlpnProtocols", "(Ljava/lang/Class;Ljava/lang/Class;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;)V", "closeGuard", "Lokhttp3/internal/platform/AndroidPlatform$CloseGuard;", "api23IsCleartextTrafficPermitted", "", "hostname", "", "networkPolicyClass", "networkSecurityPolicy", "", "api24IsCleartextTrafficPermitted", "buildCertificateChainCleaner", "Lokhttp3/internal/tls/CertificateChainCleaner;", "trustManager", "Ljavax/net/ssl/X509TrustManager;", "buildTrustRootIndex", "Lokhttp3/internal/tls/TrustRootIndex;", "configureTlsExtensions", "", "sslSocket", "Ljavax/net/ssl/SSLSocket;", "protocols", "", "Lokhttp3/Protocol;", "connectSocket", "socket", "Ljava/net/Socket;", "address", "Ljava/net/InetSocketAddress;", "connectTimeout", "", "getSelectedProtocol", "getStackTraceForCloseable", "closer", "isCleartextTrafficPermitted", "log", LogContract.LogColumns.LEVEL, "message", "t", "", "logCloseableLeak", "stackTrace", "sslSocketFactory", "Ljavax/net/ssl/SSLSocketFactory;", "AndroidCertificateChainCleaner", "CloseGuard", "Companion", "CustomTrustRootIndex", "okhttp"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes.dex */
public final class AndroidPlatform extends Platform {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final int MAX_LOG_LENGTH = 4000;
    private final CloseGuard closeGuard;
    private final Method getAlpnSelectedProtocol;
    private final Method setAlpnProtocols;
    private final Method setHostname;
    private final Method setUseSessionTickets;
    private final Class<?> sslParametersClass;
    private final Class<?> sslSocketClass;

    public AndroidPlatform(Class<?> sslParametersClass, Class<?> sslSocketClass, Method setUseSessionTickets, Method setHostname, Method getAlpnSelectedProtocol, Method setAlpnProtocols) {
        Intrinsics.checkParameterIsNotNull(sslParametersClass, "sslParametersClass");
        Intrinsics.checkParameterIsNotNull(sslSocketClass, "sslSocketClass");
        Intrinsics.checkParameterIsNotNull(setUseSessionTickets, "setUseSessionTickets");
        Intrinsics.checkParameterIsNotNull(setHostname, "setHostname");
        Intrinsics.checkParameterIsNotNull(getAlpnSelectedProtocol, "getAlpnSelectedProtocol");
        Intrinsics.checkParameterIsNotNull(setAlpnProtocols, "setAlpnProtocols");
        this.sslParametersClass = sslParametersClass;
        this.sslSocketClass = sslSocketClass;
        this.setUseSessionTickets = setUseSessionTickets;
        this.setHostname = setHostname;
        this.getAlpnSelectedProtocol = getAlpnSelectedProtocol;
        this.setAlpnProtocols = setAlpnProtocols;
        this.closeGuard = CloseGuard.INSTANCE.get();
    }

    @Override // okhttp3.internal.platform.Platform
    public void connectSocket(Socket socket, InetSocketAddress address, int connectTimeout) throws IOException {
        Intrinsics.checkParameterIsNotNull(socket, "socket");
        Intrinsics.checkParameterIsNotNull(address, "address");
        try {
            socket.connect(address, connectTimeout);
        } catch (ClassCastException e) {
            if (Build.VERSION.SDK_INT == 26) {
                throw new IOException("Exception in connect", e);
            }
            throw e;
        }
    }

    @Override // okhttp3.internal.platform.Platform
    protected X509TrustManager trustManager(SSLSocketFactory sslSocketFactory) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IllegalArgumentException {
        Intrinsics.checkParameterIsNotNull(sslSocketFactory, "sslSocketFactory");
        Object fieldOrNull = Platform.INSTANCE.readFieldOrNull(sslSocketFactory, this.sslParametersClass, "sslParameters");
        if (fieldOrNull == null) {
            try {
                Class<?> gmsSslParametersClass = Class.forName("com.google.android.gms.org.conscrypt.SSLParametersImpl", false, sslSocketFactory.getClass().getClassLoader());
                Platform.Companion companion = Platform.INSTANCE;
                Intrinsics.checkExpressionValueIsNotNull(gmsSslParametersClass, "gmsSslParametersClass");
                fieldOrNull = companion.readFieldOrNull(sslSocketFactory, gmsSslParametersClass, "sslParameters");
            } catch (ClassNotFoundException unused) {
                return super.trustManager(sslSocketFactory);
            }
        }
        Platform.Companion companion2 = Platform.INSTANCE;
        if (fieldOrNull == null) {
            Intrinsics.throwNpe();
        }
        X509TrustManager x509TrustManager = (X509TrustManager) companion2.readFieldOrNull(fieldOrNull, X509TrustManager.class, "x509TrustManager");
        return x509TrustManager != null ? x509TrustManager : (X509TrustManager) Platform.INSTANCE.readFieldOrNull(fieldOrNull, X509TrustManager.class, "trustManager");
    }

    @Override // okhttp3.internal.platform.Platform
    public void configureTlsExtensions(SSLSocket sslSocket, String hostname, List<? extends Protocol> protocols) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Intrinsics.checkParameterIsNotNull(sslSocket, "sslSocket");
        Intrinsics.checkParameterIsNotNull(protocols, "protocols");
        if (this.sslSocketClass.isInstance(sslSocket)) {
            if (hostname != null) {
                try {
                    this.setUseSessionTickets.invoke(sslSocket, true);
                    this.setHostname.invoke(sslSocket, hostname);
                } catch (IllegalAccessException e) {
                    throw new AssertionError(e);
                } catch (InvocationTargetException e2) {
                    throw new AssertionError(e2);
                }
            }
            this.setAlpnProtocols.invoke(sslSocket, Platform.INSTANCE.concatLengthPrefixed(protocols));
        }
    }

    @Override // okhttp3.internal.platform.Platform
    public String getSelectedProtocol(SSLSocket socket) {
        Intrinsics.checkParameterIsNotNull(socket, "socket");
        if (!this.sslSocketClass.isInstance(socket)) {
            return null;
        }
        try {
            byte[] bArr = (byte[]) this.getAlpnSelectedProtocol.invoke(socket, new Object[0]);
            if (bArr == null) {
                return null;
            }
            Charset UTF_8 = StandardCharsets.UTF_8;
            Intrinsics.checkExpressionValueIsNotNull(UTF_8, "UTF_8");
            return new String(bArr, UTF_8);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e2) {
            throw new AssertionError(e2);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0055, code lost:
    
        r8 = r2 + 1;
     */
    @Override // okhttp3.internal.platform.Platform
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void log(int r8, java.lang.String r9, java.lang.Throwable r10) {
        /*
            r7 = this;
            java.lang.String r0 = "message"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r9, r0)
            r0 = 5
            if (r8 != r0) goto L9
            goto La
        L9:
            r0 = 3
        La:
            if (r10 == 0) goto L24
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append(r9)
            java.lang.String r9 = "\n"
            r8.append(r9)
            java.lang.String r9 = android.util.Log.getStackTraceString(r10)
            r8.append(r9)
            java.lang.String r9 = r8.toString()
        L24:
            r8 = 0
            int r10 = r9.length()
        L29:
            if (r8 >= r10) goto L62
            r1 = r9
            java.lang.CharSequence r1 = (java.lang.CharSequence) r1
            r2 = 10
            r4 = 0
            r5 = 4
            r6 = 0
            r3 = r8
            int r1 = kotlin.text.StringsKt.indexOf$default(r1, r2, r3, r4, r5, r6)
            r2 = -1
            if (r1 == r2) goto L3c
            goto L3d
        L3c:
            r1 = r10
        L3d:
            int r2 = r8 + 4000
            int r2 = java.lang.Math.min(r1, r2)
            if (r9 == 0) goto L5a
            java.lang.String r8 = r9.substring(r8, r2)
            java.lang.String r3 = "(this as java.lang.Strin…ing(startIndex, endIndex)"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r8, r3)
            java.lang.String r3 = "OkHttp"
            android.util.Log.println(r0, r3, r8)
            if (r2 < r1) goto L58
            int r8 = r2 + 1
            goto L29
        L58:
            r8 = r2
            goto L3d
        L5a:
            kotlin.TypeCastException r8 = new kotlin.TypeCastException
            java.lang.String r9 = "null cannot be cast to non-null type java.lang.String"
            r8.<init>(r9)
            throw r8
        L62:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.platform.AndroidPlatform.log(int, java.lang.String, java.lang.Throwable):void");
    }

    @Override // okhttp3.internal.platform.Platform
    public Object getStackTraceForCloseable(String closer) {
        Intrinsics.checkParameterIsNotNull(closer, "closer");
        return this.closeGuard.createAndOpen(closer);
    }

    @Override // okhttp3.internal.platform.Platform
    public void logCloseableLeak(String message, Object stackTrace) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        if (this.closeGuard.warnIfOpen(stackTrace)) {
            return;
        }
        log(5, message, null);
    }

    @Override // okhttp3.internal.platform.Platform
    public boolean isCleartextTrafficPermitted(String hostname) throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        Intrinsics.checkParameterIsNotNull(hostname, "hostname");
        try {
            Class<?> networkPolicyClass = Class.forName("android.security.NetworkSecurityPolicy");
            Object networkSecurityPolicy = networkPolicyClass.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
            Intrinsics.checkExpressionValueIsNotNull(networkPolicyClass, "networkPolicyClass");
            Intrinsics.checkExpressionValueIsNotNull(networkSecurityPolicy, "networkSecurityPolicy");
            return api24IsCleartextTrafficPermitted(hostname, networkPolicyClass, networkSecurityPolicy);
        } catch (ClassNotFoundException unused) {
            return super.isCleartextTrafficPermitted(hostname);
        } catch (IllegalAccessException e) {
            throw new AssertionError("unable to determine cleartext support", e);
        } catch (IllegalArgumentException e2) {
            throw new AssertionError("unable to determine cleartext support", e2);
        } catch (NoSuchMethodException unused2) {
            return super.isCleartextTrafficPermitted(hostname);
        } catch (InvocationTargetException e3) {
            throw new AssertionError("unable to determine cleartext support", e3);
        }
    }

    private final boolean api24IsCleartextTrafficPermitted(String hostname, Class<?> networkPolicyClass, Object networkSecurityPolicy) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            Object objInvoke = networkPolicyClass.getMethod("isCleartextTrafficPermitted", String.class).invoke(networkSecurityPolicy, hostname);
            if (objInvoke != null) {
                return ((Boolean) objInvoke).booleanValue();
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Boolean");
        } catch (NoSuchMethodException unused) {
            return api23IsCleartextTrafficPermitted(hostname, networkPolicyClass, networkSecurityPolicy);
        }
    }

    private final boolean api23IsCleartextTrafficPermitted(String hostname, Class<?> networkPolicyClass, Object networkSecurityPolicy) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            Object objInvoke = networkPolicyClass.getMethod("isCleartextTrafficPermitted", new Class[0]).invoke(networkSecurityPolicy, new Object[0]);
            if (objInvoke != null) {
                return ((Boolean) objInvoke).booleanValue();
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Boolean");
        } catch (NoSuchMethodException unused) {
            return super.isCleartextTrafficPermitted(hostname);
        }
    }

    @Override // okhttp3.internal.platform.Platform
    public CertificateChainCleaner buildCertificateChainCleaner(X509TrustManager trustManager) throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Intrinsics.checkParameterIsNotNull(trustManager, "trustManager");
        try {
            Class<?> cls = Class.forName("android.net.http.X509TrustManagerExtensions");
            Object extensions = cls.getConstructor(X509TrustManager.class).newInstance(trustManager);
            Method checkServerTrusted = cls.getMethod("checkServerTrusted", X509Certificate[].class, String.class, String.class);
            Intrinsics.checkExpressionValueIsNotNull(extensions, "extensions");
            Intrinsics.checkExpressionValueIsNotNull(checkServerTrusted, "checkServerTrusted");
            return new AndroidCertificateChainCleaner(extensions, checkServerTrusted);
        } catch (Exception unused) {
            return super.buildCertificateChainCleaner(trustManager);
        }
    }

    @Override // okhttp3.internal.platform.Platform
    public TrustRootIndex buildTrustRootIndex(X509TrustManager trustManager) throws NoSuchMethodException, SecurityException {
        Intrinsics.checkParameterIsNotNull(trustManager, "trustManager");
        try {
            Method method = trustManager.getClass().getDeclaredMethod("findTrustAnchorByIssuerAndSignature", X509Certificate.class);
            Intrinsics.checkExpressionValueIsNotNull(method, "method");
            method.setAccessible(true);
            return new CustomTrustRootIndex(trustManager, method);
        } catch (NoSuchMethodException unused) {
            return super.buildTrustRootIndex(trustManager);
        }
    }

    /* compiled from: AndroidPlatform.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J$\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0003H\u0096\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lokhttp3/internal/platform/AndroidPlatform$AndroidCertificateChainCleaner;", "Lokhttp3/internal/tls/CertificateChainCleaner;", "x509TrustManagerExtensions", "", "checkServerTrusted", "Ljava/lang/reflect/Method;", "(Ljava/lang/Object;Ljava/lang/reflect/Method;)V", "clean", "", "Ljava/security/cert/Certificate;", "chain", "hostname", "", "equals", "", "other", "hashCode", "", "okhttp"}, k = 1, mv = {1, 1, 15})
    public static final class AndroidCertificateChainCleaner extends CertificateChainCleaner {
        private final Method checkServerTrusted;
        private final Object x509TrustManagerExtensions;

        public int hashCode() {
            return 0;
        }

        public AndroidCertificateChainCleaner(Object x509TrustManagerExtensions, Method checkServerTrusted) {
            Intrinsics.checkParameterIsNotNull(x509TrustManagerExtensions, "x509TrustManagerExtensions");
            Intrinsics.checkParameterIsNotNull(checkServerTrusted, "checkServerTrusted");
            this.x509TrustManagerExtensions = x509TrustManagerExtensions;
            this.checkServerTrusted = checkServerTrusted;
        }

        @Override // okhttp3.internal.tls.CertificateChainCleaner
        public List<Certificate> clean(List<? extends Certificate> chain, String hostname) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SSLPeerUnverifiedException {
            Intrinsics.checkParameterIsNotNull(chain, "chain");
            Intrinsics.checkParameterIsNotNull(hostname, "hostname");
            try {
                Object[] array = chain.toArray(new X509Certificate[0]);
                if (array != null) {
                    Object objInvoke = this.checkServerTrusted.invoke(this.x509TrustManagerExtensions, (X509Certificate[]) array, "RSA", hostname);
                    if (objInvoke != null) {
                        return (List) objInvoke;
                    }
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.List<java.security.cert.Certificate>");
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            } catch (InvocationTargetException e2) {
                SSLPeerUnverifiedException sSLPeerUnverifiedException = new SSLPeerUnverifiedException(e2.getMessage());
                sSLPeerUnverifiedException.initCause(e2);
                throw sSLPeerUnverifiedException;
            }
        }

        public boolean equals(Object other) {
            return other instanceof AndroidCertificateChainCleaner;
        }
    }

    /* compiled from: AndroidPlatform.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0000\u0018\u0000 \r2\u00020\u0001:\u0001\rB#\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u00012\u0006\u0010\b\u001a\u00020\tJ\u0010\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u0001R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lokhttp3/internal/platform/AndroidPlatform$CloseGuard;", "", "getMethod", "Ljava/lang/reflect/Method;", "openMethod", "warnIfOpenMethod", "(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;)V", "createAndOpen", "closer", "", "warnIfOpen", "", "closeGuardInstance", "Companion", "okhttp"}, k = 1, mv = {1, 1, 15})
    public static final class CloseGuard {

        /* renamed from: Companion, reason: from kotlin metadata */
        public static final Companion INSTANCE = new Companion(null);
        private final Method getMethod;
        private final Method openMethod;
        private final Method warnIfOpenMethod;

        public CloseGuard(Method method, Method method2, Method method3) {
            this.getMethod = method;
            this.openMethod = method2;
            this.warnIfOpenMethod = method3;
        }

        public final Object createAndOpen(String closer) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            Intrinsics.checkParameterIsNotNull(closer, "closer");
            Method method = this.getMethod;
            if (method != null) {
                try {
                    Object objInvoke = method.invoke(null, new Object[0]);
                    Method method2 = this.openMethod;
                    if (method2 == null) {
                        Intrinsics.throwNpe();
                    }
                    method2.invoke(objInvoke, closer);
                    return objInvoke;
                } catch (Exception unused) {
                }
            }
            return null;
        }

        public final boolean warnIfOpen(Object closeGuardInstance) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (closeGuardInstance == null) {
                return false;
            }
            try {
                Method method = this.warnIfOpenMethod;
                if (method == null) {
                    Intrinsics.throwNpe();
                }
                method.invoke(closeGuardInstance, new Object[0]);
                return true;
            } catch (Exception unused) {
                return false;
            }
        }

        /* compiled from: AndroidPlatform.kt */
        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004¨\u0006\u0005"}, d2 = {"Lokhttp3/internal/platform/AndroidPlatform$CloseGuard$Companion;", "", "()V", "get", "Lokhttp3/internal/platform/AndroidPlatform$CloseGuard;", "okhttp"}, k = 1, mv = {1, 1, 15})
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public final CloseGuard get() throws NoSuchMethodException, ClassNotFoundException, SecurityException {
                Method method;
                Method method2;
                Method method3;
                try {
                    Class<?> cls = Class.forName("dalvik.system.CloseGuard");
                    method = cls.getMethod("get", new Class[0]);
                    method3 = cls.getMethod("open", String.class);
                    method2 = cls.getMethod("warnIfOpen", new Class[0]);
                } catch (Exception unused) {
                    method = (Method) null;
                    method2 = method;
                    method3 = method2;
                }
                return new CloseGuard(method, method3, method2);
            }
        }
    }

    /* compiled from: AndroidPlatform.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0080\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u0007\u001a\u00020\u0003HÂ\u0003J\t\u0010\b\u001a\u00020\u0005HÂ\u0003J\u001d\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rHÖ\u0003J\u0012\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lokhttp3/internal/platform/AndroidPlatform$CustomTrustRootIndex;", "Lokhttp3/internal/tls/TrustRootIndex;", "trustManager", "Ljavax/net/ssl/X509TrustManager;", "findByIssuerAndSignatureMethod", "Ljava/lang/reflect/Method;", "(Ljavax/net/ssl/X509TrustManager;Ljava/lang/reflect/Method;)V", "component1", "component2", "copy", "equals", "", "other", "", "findByIssuerAndSignature", "Ljava/security/cert/X509Certificate;", "cert", "hashCode", "", "toString", "", "okhttp"}, k = 1, mv = {1, 1, 15})
    public static final /* data */ class CustomTrustRootIndex implements TrustRootIndex {
        private final Method findByIssuerAndSignatureMethod;
        private final X509TrustManager trustManager;

        /* renamed from: component1, reason: from getter */
        private final X509TrustManager getTrustManager() {
            return this.trustManager;
        }

        /* renamed from: component2, reason: from getter */
        private final Method getFindByIssuerAndSignatureMethod() {
            return this.findByIssuerAndSignatureMethod;
        }

        public static /* synthetic */ CustomTrustRootIndex copy$default(CustomTrustRootIndex customTrustRootIndex, X509TrustManager x509TrustManager, Method method, int i, Object obj) {
            if ((i & 1) != 0) {
                x509TrustManager = customTrustRootIndex.trustManager;
            }
            if ((i & 2) != 0) {
                method = customTrustRootIndex.findByIssuerAndSignatureMethod;
            }
            return customTrustRootIndex.copy(x509TrustManager, method);
        }

        public final CustomTrustRootIndex copy(X509TrustManager trustManager, Method findByIssuerAndSignatureMethod) {
            Intrinsics.checkParameterIsNotNull(trustManager, "trustManager");
            Intrinsics.checkParameterIsNotNull(findByIssuerAndSignatureMethod, "findByIssuerAndSignatureMethod");
            return new CustomTrustRootIndex(trustManager, findByIssuerAndSignatureMethod);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CustomTrustRootIndex)) {
                return false;
            }
            CustomTrustRootIndex customTrustRootIndex = (CustomTrustRootIndex) other;
            return Intrinsics.areEqual(this.trustManager, customTrustRootIndex.trustManager) && Intrinsics.areEqual(this.findByIssuerAndSignatureMethod, customTrustRootIndex.findByIssuerAndSignatureMethod);
        }

        public int hashCode() {
            X509TrustManager x509TrustManager = this.trustManager;
            int iHashCode = (x509TrustManager != null ? x509TrustManager.hashCode() : 0) * 31;
            Method method = this.findByIssuerAndSignatureMethod;
            return iHashCode + (method != null ? method.hashCode() : 0);
        }

        public String toString() {
            return "CustomTrustRootIndex(trustManager=" + this.trustManager + ", findByIssuerAndSignatureMethod=" + this.findByIssuerAndSignatureMethod + ")";
        }

        public CustomTrustRootIndex(X509TrustManager trustManager, Method findByIssuerAndSignatureMethod) {
            Intrinsics.checkParameterIsNotNull(trustManager, "trustManager");
            Intrinsics.checkParameterIsNotNull(findByIssuerAndSignatureMethod, "findByIssuerAndSignatureMethod");
            this.trustManager = trustManager;
            this.findByIssuerAndSignatureMethod = findByIssuerAndSignatureMethod;
        }

        @Override // okhttp3.internal.tls.TrustRootIndex
        public X509Certificate findByIssuerAndSignature(X509Certificate cert) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            Intrinsics.checkParameterIsNotNull(cert, "cert");
            try {
                Object objInvoke = this.findByIssuerAndSignatureMethod.invoke(this.trustManager, cert);
                if (objInvoke == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.security.cert.TrustAnchor");
                }
                return ((TrustAnchor) objInvoke).getTrustedCert();
            } catch (IllegalAccessException e) {
                throw new AssertionError("unable to get issues and signature", e);
            } catch (InvocationTargetException unused) {
                return null;
            }
        }
    }

    /* compiled from: AndroidPlatform.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lokhttp3/internal/platform/AndroidPlatform$Companion;", "", "()V", "MAX_LOG_LENGTH", "", "buildIfSupported", "Lokhttp3/internal/platform/Platform;", "okhttp"}, k = 1, mv = {1, 1, 15})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Platform buildIfSupported() {
            try {
                Class<?> cls = Class.forName("com.android.org.conscrypt.SSLParametersImpl");
                Intrinsics.checkExpressionValueIsNotNull(cls, "Class.forName(\"com.andro…crypt.SSLParametersImpl\")");
                Class<?> cls2 = Class.forName("com.android.org.conscrypt.OpenSSLSocketImpl");
                Intrinsics.checkExpressionValueIsNotNull(cls2, "Class.forName(\"com.andro…crypt.OpenSSLSocketImpl\")");
                if (Build.VERSION.SDK_INT >= 21) {
                    try {
                        Method setUseSessionTickets = cls2.getDeclaredMethod("setUseSessionTickets", Boolean.TYPE);
                        Method setHostname = cls2.getMethod("setHostname", String.class);
                        Method getAlpnSelectedProtocol = cls2.getMethod("getAlpnSelectedProtocol", new Class[0]);
                        Method setAlpnProtocols = cls2.getMethod("setAlpnProtocols", byte[].class);
                        Intrinsics.checkExpressionValueIsNotNull(setUseSessionTickets, "setUseSessionTickets");
                        Intrinsics.checkExpressionValueIsNotNull(setHostname, "setHostname");
                        Intrinsics.checkExpressionValueIsNotNull(getAlpnSelectedProtocol, "getAlpnSelectedProtocol");
                        Intrinsics.checkExpressionValueIsNotNull(setAlpnProtocols, "setAlpnProtocols");
                        return new AndroidPlatform(cls, cls2, setUseSessionTickets, setHostname, getAlpnSelectedProtocol, setAlpnProtocols);
                    } catch (NoSuchMethodException unused) {
                    }
                }
                throw new IllegalStateException("Expected Android API level 21+ but was " + Build.VERSION.SDK_INT);
            } catch (ClassNotFoundException unused2) {
                return null;
            }
        }
    }
}
