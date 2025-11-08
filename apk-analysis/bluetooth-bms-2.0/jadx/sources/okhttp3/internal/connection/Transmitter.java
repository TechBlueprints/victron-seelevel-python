package okhttp3.internal.connection;

import androidx.core.app.NotificationCompat;
import androidx.exifinterface.media.ExifInterface;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin._Assertions;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.CertificatePinner;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.internal.Util;
import okhttp3.internal.http.ExchangeCodec;
import okhttp3.internal.platform.Platform;
import okio.AsyncTimeout;
import okio.Timeout;

/* compiled from: Transmitter.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u007f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006*\u0001 \u0018\u00002\u00020\u0001:\u0001FB\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010#\u001a\u00020$2\u0006\u0010\n\u001a\u00020\u000bJ\u0006\u0010%\u001a\u00020$J\u0006\u0010&\u001a\u00020\tJ\u0006\u0010'\u001a\u00020$J\u0010\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020+H\u0002J\u0006\u0010,\u001a\u00020$J;\u0010-\u001a\u0002H.\"\n\b\u0000\u0010.*\u0004\u0018\u00010/2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u00100\u001a\u00020\t2\u0006\u00101\u001a\u00020\t2\u0006\u00102\u001a\u0002H.H\u0000¢\u0006\u0004\b3\u00104J\u0006\u00105\u001a\u00020\tJ)\u00106\u001a\u0002H.\"\n\b\u0000\u0010.*\u0004\u0018\u00010/2\u0006\u00102\u001a\u0002H.2\u0006\u00107\u001a\u00020\tH\u0002¢\u0006\u0002\u00108J\u001d\u00109\u001a\u00020\u00152\u0006\u0010:\u001a\u00020;2\u0006\u0010<\u001a\u00020\tH\u0000¢\u0006\u0002\b=J\u0012\u0010\u001c\u001a\u0004\u0018\u00010/2\b\u00102\u001a\u0004\u0018\u00010/J\u000e\u0010>\u001a\u00020$2\u0006\u0010\u001d\u001a\u00020\u001eJ\b\u0010?\u001a\u0004\u0018\u00010@J\u0006\u0010\u001f\u001a\u00020AJ\u0006\u0010\"\u001a\u00020$J\u0006\u0010B\u001a\u00020$J!\u0010C\u001a\u0002H.\"\n\b\u0000\u0010.*\u0004\u0018\u00010/2\u0006\u0010D\u001a\u0002H.H\u0002¢\u0006\u0002\u0010ER\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u0001X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u001a\u001a\u00020\t8F¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u000e\u0010\u001c\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001f\u001a\u00020 X\u0082\u0004¢\u0006\u0004\n\u0002\u0010!R\u000e\u0010\"\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006G"}, d2 = {"Lokhttp3/internal/connection/Transmitter;", "", "client", "Lokhttp3/OkHttpClient;", NotificationCompat.CATEGORY_CALL, "Lokhttp3/Call;", "(Lokhttp3/OkHttpClient;Lokhttp3/Call;)V", "callStackTrace", "canceled", "", "connection", "Lokhttp3/internal/connection/RealConnection;", "getConnection", "()Lokhttp3/internal/connection/RealConnection;", "setConnection", "(Lokhttp3/internal/connection/RealConnection;)V", "connectionPool", "Lokhttp3/internal/connection/RealConnectionPool;", "eventListener", "Lokhttp3/EventListener;", "exchange", "Lokhttp3/internal/connection/Exchange;", "exchangeFinder", "Lokhttp3/internal/connection/ExchangeFinder;", "exchangeRequestDone", "exchangeResponseDone", "isCanceled", "()Z", "noMoreExchanges", "request", "Lokhttp3/Request;", "timeout", "okhttp3/internal/connection/Transmitter$timeout$1", "Lokhttp3/internal/connection/Transmitter$timeout$1;", "timeoutEarlyExit", "acquireConnectionNoEvents", "", "callStart", "canRetry", "cancel", "createAddress", "Lokhttp3/Address;", "url", "Lokhttp3/HttpUrl;", "exchangeDoneDueToException", "exchangeMessageDone", ExifInterface.LONGITUDE_EAST, "Ljava/io/IOException;", "requestDone", "responseDone", "e", "exchangeMessageDone$okhttp", "(Lokhttp3/internal/connection/Exchange;ZZLjava/io/IOException;)Ljava/io/IOException;", "hasExchange", "maybeReleaseConnection", "force", "(Ljava/io/IOException;Z)Ljava/io/IOException;", "newExchange", "chain", "Lokhttp3/Interceptor$Chain;", "doExtensiveHealthChecks", "newExchange$okhttp", "prepareToConnect", "releaseConnectionNoEvents", "Ljava/net/Socket;", "Lokio/Timeout;", "timeoutEnter", "timeoutExit", "cause", "(Ljava/io/IOException;)Ljava/io/IOException;", "TransmitterReference", "okhttp"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes.dex */
public final class Transmitter {
    private final Call call;
    private Object callStackTrace;
    private boolean canceled;
    private final OkHttpClient client;
    private RealConnection connection;
    private final RealConnectionPool connectionPool;
    private final EventListener eventListener;
    private Exchange exchange;
    private ExchangeFinder exchangeFinder;
    private boolean exchangeRequestDone;
    private boolean exchangeResponseDone;
    private boolean noMoreExchanges;
    private Request request;
    private final AnonymousClass1 timeout;
    private boolean timeoutEarlyExit;

    /* JADX WARN: Type inference failed for: r3v6, types: [okhttp3.internal.connection.Transmitter$timeout$1] */
    public Transmitter(OkHttpClient client, Call call) {
        Intrinsics.checkParameterIsNotNull(client, "client");
        Intrinsics.checkParameterIsNotNull(call, "call");
        this.client = client;
        this.call = call;
        this.connectionPool = client.connectionPool().getDelegate();
        this.eventListener = this.client.eventListenerFactory().create(this.call);
        ?? r3 = new AsyncTimeout() { // from class: okhttp3.internal.connection.Transmitter.timeout.1
            @Override // okio.AsyncTimeout
            protected void timedOut() throws IOException {
                Transmitter.this.cancel();
            }
        };
        r3.timeout(this.client.callTimeoutMillis(), TimeUnit.MILLISECONDS);
        this.timeout = r3;
    }

    public final RealConnection getConnection() {
        return this.connection;
    }

    public final void setConnection(RealConnection realConnection) {
        this.connection = realConnection;
    }

    public final boolean isCanceled() {
        boolean z;
        synchronized (this.connectionPool) {
            z = this.canceled;
        }
        return z;
    }

    public final Timeout timeout() {
        return this.timeout;
    }

    public final void timeoutEnter() {
        enter();
    }

    public final void timeoutEarlyExit() {
        if (!(!this.timeoutEarlyExit)) {
            throw new IllegalStateException("Check failed.".toString());
        }
        this.timeoutEarlyExit = true;
        exit();
    }

    private final <E extends IOException> E timeoutExit(E cause) {
        if (this.timeoutEarlyExit || !exit()) {
            return cause;
        }
        InterruptedIOException interruptedIOException = new InterruptedIOException("timeout");
        if (cause != null) {
            interruptedIOException.initCause(cause);
        }
        return interruptedIOException;
    }

    public final void callStart() {
        this.callStackTrace = Platform.INSTANCE.get().getStackTraceForCloseable("response.body().close()");
        this.eventListener.callStart(this.call);
    }

    public final void prepareToConnect(Request request) throws IOException {
        Intrinsics.checkParameterIsNotNull(request, "request");
        Request request2 = this.request;
        if (request2 != null) {
            if (request2 == null) {
                Intrinsics.throwNpe();
            }
            if (Util.canReuseConnectionFor(request2.url(), request.url())) {
                ExchangeFinder exchangeFinder = this.exchangeFinder;
                if (exchangeFinder == null) {
                    Intrinsics.throwNpe();
                }
                if (exchangeFinder.hasRouteToTry()) {
                    return;
                }
            }
            if (!(this.exchange == null)) {
                throw new IllegalStateException("Check failed.".toString());
            }
            if (this.exchangeFinder != null) {
                maybeReleaseConnection(null, true);
                this.exchangeFinder = (ExchangeFinder) null;
            }
        }
        this.request = request;
        this.exchangeFinder = new ExchangeFinder(this, this.connectionPool, createAddress(request.url()), this.call, this.eventListener);
    }

    private final Address createAddress(HttpUrl url) {
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) null;
        HostnameVerifier hostnameVerifier = (HostnameVerifier) null;
        CertificatePinner certificatePinner = (CertificatePinner) null;
        if (url.getIsHttps()) {
            sslSocketFactory = this.client.sslSocketFactory();
            hostnameVerifier = this.client.hostnameVerifier();
            certificatePinner = this.client.certificatePinner();
        }
        SSLSocketFactory sSLSocketFactory = sslSocketFactory;
        HostnameVerifier hostnameVerifier2 = hostnameVerifier;
        return new Address(url.host(), url.port(), this.client.dns(), this.client.socketFactory(), sSLSocketFactory, hostnameVerifier2, certificatePinner, this.client.proxyAuthenticator(), this.client.proxy(), this.client.protocols(), this.client.connectionSpecs(), this.client.proxySelector());
    }

    public final Exchange newExchange$okhttp(Interceptor.Chain chain, boolean doExtensiveHealthChecks) {
        Intrinsics.checkParameterIsNotNull(chain, "chain");
        synchronized (this.connectionPool) {
            boolean z = true;
            if (!(!this.noMoreExchanges)) {
                throw new IllegalStateException("released".toString());
            }
            if (this.exchange != null) {
                z = false;
            }
            if (!z) {
                throw new IllegalStateException("cannot make a new request because the previous response is still open: please call response.close()".toString());
            }
            Unit unit = Unit.INSTANCE;
        }
        ExchangeFinder exchangeFinder = this.exchangeFinder;
        if (exchangeFinder == null) {
            Intrinsics.throwNpe();
        }
        ExchangeCodec exchangeCodecFind = exchangeFinder.find(this.client, chain, doExtensiveHealthChecks);
        Call call = this.call;
        EventListener eventListener = this.eventListener;
        ExchangeFinder exchangeFinder2 = this.exchangeFinder;
        if (exchangeFinder2 == null) {
            Intrinsics.throwNpe();
        }
        Exchange exchange = new Exchange(this, call, eventListener, exchangeFinder2, exchangeCodecFind);
        synchronized (this.connectionPool) {
            this.exchange = exchange;
            this.exchangeRequestDone = false;
            this.exchangeResponseDone = false;
        }
        return exchange;
    }

    public final void acquireConnectionNoEvents(RealConnection connection) {
        Intrinsics.checkParameterIsNotNull(connection, "connection");
        boolean zHoldsLock = Thread.holdsLock(this.connectionPool);
        if (_Assertions.ENABLED && !zHoldsLock) {
            throw new AssertionError("Assertion failed");
        }
        if (!(this.connection == null)) {
            throw new IllegalStateException("Check failed.".toString());
        }
        this.connection = connection;
        connection.getTransmitters().add(new TransmitterReference(this, this.callStackTrace));
    }

    public final Socket releaseConnectionNoEvents() {
        boolean zHoldsLock = Thread.holdsLock(this.connectionPool);
        if (_Assertions.ENABLED && !zHoldsLock) {
            throw new AssertionError("Assertion failed");
        }
        RealConnection realConnection = this.connection;
        if (realConnection == null) {
            Intrinsics.throwNpe();
        }
        Iterator<Reference<Transmitter>> it = realConnection.getTransmitters().iterator();
        int i = 0;
        while (true) {
            if (!it.hasNext()) {
                i = -1;
                break;
            }
            if (Intrinsics.areEqual(it.next().get(), this)) {
                break;
            }
            i++;
        }
        if (!(i != -1)) {
            throw new IllegalStateException("Check failed.".toString());
        }
        RealConnection realConnection2 = this.connection;
        if (realConnection2 == null) {
            Intrinsics.throwNpe();
        }
        realConnection2.getTransmitters().remove(i);
        this.connection = (RealConnection) null;
        if (realConnection2.getTransmitters().isEmpty()) {
            realConnection2.setIdleAtNanos$okhttp(System.nanoTime());
            if (this.connectionPool.connectionBecameIdle(realConnection2)) {
                return realConnection2.socket();
            }
        }
        return null;
    }

    public final void exchangeDoneDueToException() {
        synchronized (this.connectionPool) {
            if (!(!this.noMoreExchanges)) {
                throw new IllegalStateException("Check failed.".toString());
            }
            this.exchange = (Exchange) null;
            Unit unit = Unit.INSTANCE;
        }
    }

    public final <E extends IOException> E exchangeMessageDone$okhttp(Exchange exchange, boolean requestDone, boolean responseDone, E e) {
        boolean z;
        Intrinsics.checkParameterIsNotNull(exchange, "exchange");
        synchronized (this.connectionPool) {
            boolean z2 = true;
            if (!Intrinsics.areEqual(exchange, this.exchange)) {
                return e;
            }
            if (requestDone) {
                z = !this.exchangeRequestDone;
                this.exchangeRequestDone = true;
            } else {
                z = false;
            }
            if (responseDone) {
                if (!this.exchangeResponseDone) {
                    z = true;
                }
                this.exchangeResponseDone = true;
            }
            if (this.exchangeRequestDone && this.exchangeResponseDone && z) {
                Exchange exchange2 = this.exchange;
                if (exchange2 == null) {
                    Intrinsics.throwNpe();
                }
                RealConnection realConnectionConnection = exchange2.connection();
                if (realConnectionConnection == null) {
                    Intrinsics.throwNpe();
                }
                realConnectionConnection.setSuccessCount$okhttp(realConnectionConnection.getSuccessCount() + 1);
                this.exchange = (Exchange) null;
            } else {
                z2 = false;
            }
            Unit unit = Unit.INSTANCE;
            return z2 ? (E) maybeReleaseConnection(e, false) : e;
        }
    }

    public final IOException noMoreExchanges(IOException e) {
        synchronized (this.connectionPool) {
            this.noMoreExchanges = true;
            Unit unit = Unit.INSTANCE;
        }
        return maybeReleaseConnection(e, false);
    }

    /* JADX WARN: Type inference failed for: r4v4, types: [T, okhttp3.Connection] */
    /* JADX WARN: Type inference failed for: r5v2, types: [T, okhttp3.Connection] */
    private final <E extends IOException> E maybeReleaseConnection(E e, boolean force) throws IOException {
        boolean z;
        Socket socketReleaseConnectionNoEvents;
        boolean z2;
        Ref.ObjectRef objectRef = new Ref.ObjectRef();
        synchronized (this.connectionPool) {
            if (force) {
                try {
                    z = this.exchange == null;
                } finally {
                }
            }
            if (!z) {
                throw new IllegalStateException("cannot release connection while it is in use".toString());
            }
            objectRef.element = this.connection;
            socketReleaseConnectionNoEvents = (this.connection != null && this.exchange == null && (force || this.noMoreExchanges)) ? releaseConnectionNoEvents() : null;
            if (this.connection != null) {
                objectRef.element = (Connection) 0;
            }
            z2 = this.noMoreExchanges && this.exchange == null;
            Unit unit = Unit.INSTANCE;
        }
        if (socketReleaseConnectionNoEvents != null) {
            Util.closeQuietly(socketReleaseConnectionNoEvents);
        }
        if (((Connection) objectRef.element) != null) {
            EventListener eventListener = this.eventListener;
            Call call = this.call;
            Connection connection = (Connection) objectRef.element;
            if (connection == null) {
                Intrinsics.throwNpe();
            }
            eventListener.connectionReleased(call, connection);
        }
        if (z2) {
            boolean z3 = e != null;
            e = (E) timeoutExit(e);
            if (z3) {
                EventListener eventListener2 = this.eventListener;
                Call call2 = this.call;
                if (e == null) {
                    Intrinsics.throwNpe();
                }
                eventListener2.callFailed(call2, e);
            } else {
                this.eventListener.callEnd(this.call);
            }
        }
        return e;
    }

    public final boolean canRetry() {
        ExchangeFinder exchangeFinder = this.exchangeFinder;
        if (exchangeFinder == null) {
            Intrinsics.throwNpe();
        }
        if (exchangeFinder.hasStreamFailure()) {
            ExchangeFinder exchangeFinder2 = this.exchangeFinder;
            if (exchangeFinder2 == null) {
                Intrinsics.throwNpe();
            }
            if (exchangeFinder2.hasRouteToTry()) {
                return true;
            }
        }
        return false;
    }

    public final boolean hasExchange() {
        boolean z;
        synchronized (this.connectionPool) {
            z = this.exchange != null;
        }
        return z;
    }

    public final void cancel() throws IOException {
        Exchange exchange;
        RealConnection realConnectionConnectingConnection;
        synchronized (this.connectionPool) {
            this.canceled = true;
            exchange = this.exchange;
            ExchangeFinder exchangeFinder = this.exchangeFinder;
            if (exchangeFinder == null || (realConnectionConnectingConnection = exchangeFinder.connectingConnection()) == null) {
                realConnectionConnectingConnection = this.connection;
            }
            Unit unit = Unit.INSTANCE;
        }
        if (exchange != null) {
            exchange.cancel();
        } else if (realConnectionConnectingConnection != null) {
            realConnectionConnectingConnection.cancel();
        }
    }

    /* compiled from: Transmitter.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\b\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0017\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"Lokhttp3/internal/connection/Transmitter$TransmitterReference;", "Ljava/lang/ref/WeakReference;", "Lokhttp3/internal/connection/Transmitter;", "referent", "callStackTrace", "", "(Lokhttp3/internal/connection/Transmitter;Ljava/lang/Object;)V", "getCallStackTrace", "()Ljava/lang/Object;", "okhttp"}, k = 1, mv = {1, 1, 15})
    public static final class TransmitterReference extends WeakReference<Transmitter> {
        private final Object callStackTrace;

        public final Object getCallStackTrace() {
            return this.callStackTrace;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TransmitterReference(Transmitter referent, Object obj) {
            super(referent);
            Intrinsics.checkParameterIsNotNull(referent, "referent");
            this.callStackTrace = obj;
        }
    }
}
