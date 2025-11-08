package okhttp3.internal.http2;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin._Assertions;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import no.nordicsemi.android.log.LogContract;
import okhttp3.internal.Util;
import okhttp3.internal.http2.Http2Reader;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;

/* compiled from: Http2Connection.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000®\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010#\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u001b\n\u0002\u0018\u0002\n\u0002\b\u0013\u0018\u0000 \u008e\u00012\u00020\u0001:\b\u008d\u0001\u008e\u0001\u008f\u0001\u0090\u0001B\u000f\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010I\u001a\u00020JJ\b\u0010K\u001a\u00020JH\u0016J'\u0010K\u001a\u00020J2\u0006\u0010L\u001a\u00020M2\u0006\u0010N\u001a\u00020M2\b\u0010O\u001a\u0004\u0018\u00010PH\u0000¢\u0006\u0002\bQJ\u0012\u0010R\u001a\u00020J2\b\u0010S\u001a\u0004\u0018\u00010PH\u0002J\u0006\u0010T\u001a\u00020JJ\u0010\u0010U\u001a\u0004\u0018\u00010>2\u0006\u0010V\u001a\u00020\u0017J\u0006\u0010W\u001a\u00020\u0017J&\u0010X\u001a\u00020>2\u0006\u0010Y\u001a\u00020\u00172\f\u0010Z\u001a\b\u0012\u0004\u0012\u00020\\0[2\u0006\u0010]\u001a\u00020\u0006H\u0002J\u001c\u0010X\u001a\u00020>2\f\u0010Z\u001a\b\u0012\u0004\u0012\u00020\\0[2\u0006\u0010]\u001a\u00020\u0006J\u0006\u0010^\u001a\u00020\u0017J-\u0010_\u001a\u00020J2\u0006\u0010`\u001a\u00020\u00172\u0006\u0010a\u001a\u00020b2\u0006\u0010c\u001a\u00020\u00172\u0006\u0010d\u001a\u00020\u0006H\u0000¢\u0006\u0002\beJ+\u0010f\u001a\u00020J2\u0006\u0010`\u001a\u00020\u00172\f\u0010Z\u001a\b\u0012\u0004\u0012\u00020\\0[2\u0006\u0010d\u001a\u00020\u0006H\u0000¢\u0006\u0002\bgJ#\u0010h\u001a\u00020J2\u0006\u0010`\u001a\u00020\u00172\f\u0010Z\u001a\b\u0012\u0004\u0012\u00020\\0[H\u0000¢\u0006\u0002\biJ\u001d\u0010j\u001a\u00020J2\u0006\u0010`\u001a\u00020\u00172\u0006\u0010k\u001a\u00020MH\u0000¢\u0006\u0002\blJ$\u0010m\u001a\u00020>2\u0006\u0010Y\u001a\u00020\u00172\f\u0010Z\u001a\b\u0012\u0004\u0012\u00020\\0[2\u0006\u0010]\u001a\u00020\u0006J\u0015\u0010n\u001a\u00020\u00062\u0006\u0010`\u001a\u00020\u0017H\u0000¢\u0006\u0002\boJ\u0017\u0010p\u001a\u0004\u0018\u00010>2\u0006\u0010`\u001a\u00020\u0017H\u0000¢\u0006\u0002\bqJ\u000e\u0010r\u001a\u00020J2\u0006\u0010s\u001a\u00020(J\u000e\u0010t\u001a\u00020J2\u0006\u0010u\u001a\u00020MJ\u0012\u0010v\u001a\u00020J2\b\b\u0002\u0010w\u001a\u00020\u0006H\u0007J\u0015\u0010x\u001a\u00020J2\u0006\u0010y\u001a\u00020\bH\u0000¢\u0006\u0002\bzJ(\u0010{\u001a\u00020J2\u0006\u0010`\u001a\u00020\u00172\u0006\u0010|\u001a\u00020\u00062\b\u0010}\u001a\u0004\u0018\u00010~2\u0006\u0010c\u001a\u00020\bJ-\u0010\u007f\u001a\u00020J2\u0006\u0010`\u001a\u00020\u00172\u0006\u0010|\u001a\u00020\u00062\r\u0010\u0080\u0001\u001a\b\u0012\u0004\u0012\u00020\\0[H\u0000¢\u0006\u0003\b\u0081\u0001J\"\u0010\u0082\u0001\u001a\u00020J2\u0007\u0010\u0083\u0001\u001a\u00020\u00062\u0007\u0010\u0084\u0001\u001a\u00020\u00172\u0007\u0010\u0085\u0001\u001a\u00020\u0017J\u0007\u0010\u0086\u0001\u001a\u00020JJ\u001f\u0010\u0087\u0001\u001a\u00020J2\u0006\u0010`\u001a\u00020\u00172\u0006\u0010u\u001a\u00020MH\u0000¢\u0006\u0003\b\u0088\u0001J\u001f\u0010\u0089\u0001\u001a\u00020J2\u0006\u0010`\u001a\u00020\u00172\u0006\u0010k\u001a\u00020MH\u0000¢\u0006\u0003\b\u008a\u0001J\u001f\u0010\u008b\u0001\u001a\u00020J2\u0006\u0010`\u001a\u00020\u00172\u0006\u0010A\u001a\u00020\bH\u0000¢\u0006\u0003\b\u008c\u0001R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R$\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b@@X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0014\u0010\u000e\u001a\u00020\u0006X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0011\u001a\u00020\u0012X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R&\u0010\u0018\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00068F@@X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0010\"\u0004\b\u0019\u0010\u001aR\u001a\u0010\u001b\u001a\u00020\u0017X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u0014\u0010 \u001a\u00020!X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u001a\u0010$\u001a\u00020\u0017X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u001d\"\u0004\b&\u0010\u001fR\u0011\u0010'\u001a\u00020(¢\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0011\u0010+\u001a\u00020(¢\u0006\b\n\u0000\u001a\u0004\b,\u0010*R\u000e\u0010-\u001a\u00020.X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u000200X\u0082\u0004¢\u0006\u0002\n\u0000R\u0015\u00101\u001a\u000602R\u00020\u0000¢\u0006\b\n\u0000\u001a\u0004\b3\u00104R\u001a\u00105\u001a\u00020\u0006X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b6\u0010\u0010\"\u0004\b7\u0010\u001aR\u0014\u00108\u001a\u000209X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b:\u0010;R \u0010<\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020>0=X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b?\u0010@R\u001e\u0010A\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\bB\u0010\u000bR\u0011\u0010C\u001a\u00020D¢\u0006\b\n\u0000\u001a\u0004\bE\u0010FR\u000e\u0010G\u001a\u00020HX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0091\u0001"}, d2 = {"Lokhttp3/internal/http2/Http2Connection;", "Ljava/io/Closeable;", "builder", "Lokhttp3/internal/http2/Http2Connection$Builder;", "(Lokhttp3/internal/http2/Http2Connection$Builder;)V", "awaitingPong", "", "<set-?>", "", "bytesLeftInWriteWindow", "getBytesLeftInWriteWindow", "()J", "setBytesLeftInWriteWindow$okhttp", "(J)V", "client", "getClient$okhttp", "()Z", "connectionName", "", "getConnectionName$okhttp", "()Ljava/lang/String;", "currentPushRequests", "", "", "isShutdown", "setShutdown$okhttp", "(Z)V", "lastGoodStreamId", "getLastGoodStreamId$okhttp", "()I", "setLastGoodStreamId$okhttp", "(I)V", "listener", "Lokhttp3/internal/http2/Http2Connection$Listener;", "getListener$okhttp", "()Lokhttp3/internal/http2/Http2Connection$Listener;", "nextStreamId", "getNextStreamId$okhttp", "setNextStreamId$okhttp", "okHttpSettings", "Lokhttp3/internal/http2/Settings;", "getOkHttpSettings", "()Lokhttp3/internal/http2/Settings;", "peerSettings", "getPeerSettings", "pushExecutor", "Ljava/util/concurrent/ThreadPoolExecutor;", "pushObserver", "Lokhttp3/internal/http2/PushObserver;", "readerRunnable", "Lokhttp3/internal/http2/Http2Connection$ReaderRunnable;", "getReaderRunnable", "()Lokhttp3/internal/http2/Http2Connection$ReaderRunnable;", "receivedInitialPeerSettings", "getReceivedInitialPeerSettings$okhttp", "setReceivedInitialPeerSettings$okhttp", "socket", "Ljava/net/Socket;", "getSocket$okhttp", "()Ljava/net/Socket;", "streams", "", "Lokhttp3/internal/http2/Http2Stream;", "getStreams$okhttp", "()Ljava/util/Map;", "unacknowledgedBytesRead", "getUnacknowledgedBytesRead", "writer", "Lokhttp3/internal/http2/Http2Writer;", "getWriter", "()Lokhttp3/internal/http2/Http2Writer;", "writerExecutor", "Ljava/util/concurrent/ScheduledThreadPoolExecutor;", "awaitPong", "", "close", "connectionCode", "Lokhttp3/internal/http2/ErrorCode;", "streamCode", "cause", "Ljava/io/IOException;", "close$okhttp", "failConnection", "e", "flush", "getStream", "id", "maxConcurrentStreams", "newStream", "associatedStreamId", "requestHeaders", "", "Lokhttp3/internal/http2/Header;", "out", "openStreamCount", "pushDataLater", "streamId", "source", "Lokio/BufferedSource;", "byteCount", "inFinished", "pushDataLater$okhttp", "pushHeadersLater", "pushHeadersLater$okhttp", "pushRequestLater", "pushRequestLater$okhttp", "pushResetLater", "errorCode", "pushResetLater$okhttp", "pushStream", "pushedStream", "pushedStream$okhttp", "removeStream", "removeStream$okhttp", "setSettings", "settings", "shutdown", "statusCode", "start", "sendConnectionPreface", "updateConnectionFlowControl", "read", "updateConnectionFlowControl$okhttp", "writeData", "outFinished", "buffer", "Lokio/Buffer;", "writeHeaders", "alternating", "writeHeaders$okhttp", "writePing", "reply", "payload1", "payload2", "writePingAndAwaitPong", "writeSynReset", "writeSynReset$okhttp", "writeSynResetLater", "writeSynResetLater$okhttp", "writeWindowUpdateLater", "writeWindowUpdateLater$okhttp", "Builder", "Companion", "Listener", "ReaderRunnable", "okhttp"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes.dex */
public final class Http2Connection implements Closeable {
    public static final int OKHTTP_CLIENT_WINDOW_SIZE = 16777216;
    private boolean awaitingPong;
    private long bytesLeftInWriteWindow;
    private final boolean client;
    private final String connectionName;
    private final Set<Integer> currentPushRequests;
    private boolean isShutdown;
    private int lastGoodStreamId;
    private final Listener listener;
    private int nextStreamId;
    private final Settings okHttpSettings;
    private final Settings peerSettings;
    private final ThreadPoolExecutor pushExecutor;
    private final PushObserver pushObserver;
    private final ReaderRunnable readerRunnable;
    private boolean receivedInitialPeerSettings;
    private final Socket socket;
    private final Map<Integer, Http2Stream> streams;
    private long unacknowledgedBytesRead;
    private final Http2Writer writer;
    private final ScheduledThreadPoolExecutor writerExecutor;
    private static final ThreadPoolExecutor listenerExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp Http2Connection", true));

    /* compiled from: Http2Connection.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u0000 \n2\u00020\u0001:\u0001\nB\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH&¨\u0006\u000b"}, d2 = {"Lokhttp3/internal/http2/Http2Connection$Listener;", "", "()V", "onSettings", "", "connection", "Lokhttp3/internal/http2/Http2Connection;", "onStream", "stream", "Lokhttp3/internal/http2/Http2Stream;", "Companion", "okhttp"}, k = 1, mv = {1, 1, 15})
    public static abstract class Listener {
        public static final Listener REFUSE_INCOMING_STREAMS = new Listener() { // from class: okhttp3.internal.http2.Http2Connection$Listener$Companion$REFUSE_INCOMING_STREAMS$1
            @Override // okhttp3.internal.http2.Http2Connection.Listener
            public void onStream(Http2Stream stream) throws IOException {
                Intrinsics.checkParameterIsNotNull(stream, "stream");
                stream.close(ErrorCode.REFUSED_STREAM, null);
            }
        };

        public void onSettings(Http2Connection connection) {
            Intrinsics.checkParameterIsNotNull(connection, "connection");
        }

        public abstract void onStream(Http2Stream stream) throws IOException;
    }

    public final boolean pushedStream$okhttp(int streamId) {
        return streamId != 0 && (streamId & 1) == 0;
    }

    public final void start() throws IOException {
        start$default(this, false, 1, null);
    }

    public Http2Connection(Builder builder) {
        Intrinsics.checkParameterIsNotNull(builder, "builder");
        this.client = builder.getClient();
        this.listener = builder.getListener();
        this.streams = new LinkedHashMap();
        this.connectionName = builder.getConnectionName$okhttp();
        this.nextStreamId = builder.getClient() ? 3 : 2;
        this.writerExecutor = new ScheduledThreadPoolExecutor(1, Util.threadFactory(Util.format("OkHttp %s Writer", this.connectionName), false));
        this.pushExecutor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory(Util.format("OkHttp %s Push Observer", this.connectionName), true));
        this.pushObserver = builder.getPushObserver();
        Settings settings = new Settings();
        if (builder.getClient()) {
            settings.set(7, 16777216);
        }
        this.okHttpSettings = settings;
        Settings settings2 = new Settings();
        settings2.set(7, 65535);
        settings2.set(5, 16384);
        this.peerSettings = settings2;
        this.bytesLeftInWriteWindow = settings2.getInitialWindowSize();
        this.socket = builder.getSocket$okhttp();
        this.writer = new Http2Writer(builder.getSink$okhttp(), this.client);
        this.readerRunnable = new ReaderRunnable(this, new Http2Reader(builder.getSource$okhttp(), this.client));
        this.currentPushRequests = new LinkedHashSet();
        if (builder.getPingIntervalMillis() != 0) {
            this.writerExecutor.scheduleAtFixedRate(new Runnable() { // from class: okhttp3.internal.http2.Http2Connection.1
                @Override // java.lang.Runnable
                public final void run() {
                    String str = "OkHttp " + Http2Connection.this.getConnectionName() + " ping";
                    Thread currentThread = Thread.currentThread();
                    Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
                    String name = currentThread.getName();
                    currentThread.setName(str);
                    try {
                        Http2Connection.this.writePing(false, 0, 0);
                    } finally {
                        currentThread.setName(name);
                    }
                }
            }, builder.getPingIntervalMillis(), builder.getPingIntervalMillis(), TimeUnit.MILLISECONDS);
        }
    }

    /* renamed from: getClient$okhttp, reason: from getter */
    public final boolean getClient() {
        return this.client;
    }

    /* renamed from: getListener$okhttp, reason: from getter */
    public final Listener getListener() {
        return this.listener;
    }

    public final Map<Integer, Http2Stream> getStreams$okhttp() {
        return this.streams;
    }

    /* renamed from: getConnectionName$okhttp, reason: from getter */
    public final String getConnectionName() {
        return this.connectionName;
    }

    /* renamed from: getLastGoodStreamId$okhttp, reason: from getter */
    public final int getLastGoodStreamId() {
        return this.lastGoodStreamId;
    }

    public final void setLastGoodStreamId$okhttp(int i) {
        this.lastGoodStreamId = i;
    }

    /* renamed from: getNextStreamId$okhttp, reason: from getter */
    public final int getNextStreamId() {
        return this.nextStreamId;
    }

    public final void setNextStreamId$okhttp(int i) {
        this.nextStreamId = i;
    }

    public final synchronized boolean isShutdown() {
        return this.isShutdown;
    }

    public final void setShutdown$okhttp(boolean z) {
        this.isShutdown = z;
    }

    public final Settings getOkHttpSettings() {
        return this.okHttpSettings;
    }

    public final Settings getPeerSettings() {
        return this.peerSettings;
    }

    public final long getUnacknowledgedBytesRead() {
        return this.unacknowledgedBytesRead;
    }

    public final long getBytesLeftInWriteWindow() {
        return this.bytesLeftInWriteWindow;
    }

    public final void setBytesLeftInWriteWindow$okhttp(long j) {
        this.bytesLeftInWriteWindow = j;
    }

    /* renamed from: getReceivedInitialPeerSettings$okhttp, reason: from getter */
    public final boolean getReceivedInitialPeerSettings() {
        return this.receivedInitialPeerSettings;
    }

    public final void setReceivedInitialPeerSettings$okhttp(boolean z) {
        this.receivedInitialPeerSettings = z;
    }

    /* renamed from: getSocket$okhttp, reason: from getter */
    public final Socket getSocket() {
        return this.socket;
    }

    public final Http2Writer getWriter() {
        return this.writer;
    }

    public final ReaderRunnable getReaderRunnable() {
        return this.readerRunnable;
    }

    public final synchronized int openStreamCount() {
        return this.streams.size();
    }

    public final synchronized Http2Stream getStream(int id) {
        return this.streams.get(Integer.valueOf(id));
    }

    public final synchronized Http2Stream removeStream$okhttp(int streamId) {
        Http2Stream http2StreamRemove;
        http2StreamRemove = this.streams.remove(Integer.valueOf(streamId));
        notifyAll();
        return http2StreamRemove;
    }

    public final synchronized int maxConcurrentStreams() {
        return this.peerSettings.getMaxConcurrentStreams(Integer.MAX_VALUE);
    }

    public final synchronized void updateConnectionFlowControl$okhttp(long read) {
        long j = this.unacknowledgedBytesRead + read;
        this.unacknowledgedBytesRead = j;
        if (j >= this.okHttpSettings.getInitialWindowSize() / 2) {
            writeWindowUpdateLater$okhttp(0, this.unacknowledgedBytesRead);
            this.unacknowledgedBytesRead = 0L;
        }
    }

    public final Http2Stream pushStream(int associatedStreamId, List<Header> requestHeaders, boolean out) throws IOException {
        Intrinsics.checkParameterIsNotNull(requestHeaders, "requestHeaders");
        if (!(!this.client)) {
            throw new IllegalStateException("Client cannot push requests.".toString());
        }
        return newStream(associatedStreamId, requestHeaders, out);
    }

    public final Http2Stream newStream(List<Header> requestHeaders, boolean out) throws IOException {
        Intrinsics.checkParameterIsNotNull(requestHeaders, "requestHeaders");
        return newStream(0, requestHeaders, out);
    }

    private final Http2Stream newStream(int associatedStreamId, List<Header> requestHeaders, boolean out) throws IOException {
        int i;
        Http2Stream http2Stream;
        boolean z;
        boolean z2 = !out;
        synchronized (this.writer) {
            synchronized (this) {
                if (this.nextStreamId > 1073741823) {
                    shutdown(ErrorCode.REFUSED_STREAM);
                }
                if (this.isShutdown) {
                    throw new ConnectionShutdownException();
                }
                i = this.nextStreamId;
                this.nextStreamId += 2;
                http2Stream = new Http2Stream(i, this, z2, false, null);
                z = !out || this.bytesLeftInWriteWindow == 0 || http2Stream.getBytesLeftInWriteWindow() == 0;
                if (http2Stream.isOpen()) {
                    this.streams.put(Integer.valueOf(i), http2Stream);
                }
                Unit unit = Unit.INSTANCE;
            }
            if (associatedStreamId == 0) {
                this.writer.headers(z2, i, requestHeaders);
            } else {
                if (!(true ^ this.client)) {
                    throw new IllegalArgumentException("client streams shouldn't have associated stream IDs".toString());
                }
                this.writer.pushPromise(associatedStreamId, i, requestHeaders);
            }
            Unit unit2 = Unit.INSTANCE;
        }
        if (z) {
            this.writer.flush();
        }
        return http2Stream;
    }

    public final void writeHeaders$okhttp(int streamId, boolean outFinished, List<Header> alternating) throws IOException {
        Intrinsics.checkParameterIsNotNull(alternating, "alternating");
        this.writer.headers(outFinished, streamId, alternating);
    }

    public final void writeData(int streamId, boolean outFinished, Buffer buffer, long byteCount) throws IOException {
        if (byteCount == 0) {
            this.writer.data(outFinished, streamId, buffer, 0);
            return;
        }
        while (byteCount > 0) {
            Ref.IntRef intRef = new Ref.IntRef();
            synchronized (this) {
                while (this.bytesLeftInWriteWindow <= 0) {
                    try {
                        if (!this.streams.containsKey(Integer.valueOf(streamId))) {
                            throw new IOException("stream closed");
                        }
                        wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                        throw new InterruptedIOException();
                    }
                }
                intRef.element = (int) Math.min(byteCount, this.bytesLeftInWriteWindow);
                intRef.element = Math.min(intRef.element, this.writer.getMaxFrameSize());
                this.bytesLeftInWriteWindow -= intRef.element;
                Unit unit = Unit.INSTANCE;
            }
            byteCount -= intRef.element;
            this.writer.data(outFinished && byteCount == 0, streamId, buffer, intRef.element);
        }
    }

    public final void writeSynResetLater$okhttp(final int streamId, final ErrorCode errorCode) {
        Intrinsics.checkParameterIsNotNull(errorCode, "errorCode");
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = this.writerExecutor;
        final String str = "OkHttp " + this.connectionName + " stream " + streamId;
        try {
            scheduledThreadPoolExecutor.execute(new Runnable() { // from class: okhttp3.internal.http2.Http2Connection$writeSynResetLater$$inlined$tryExecute$1
                @Override // java.lang.Runnable
                public final void run() {
                    String str2 = str;
                    Thread currentThread = Thread.currentThread();
                    Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
                    String name = currentThread.getName();
                    currentThread.setName(str2);
                    try {
                        try {
                            this.writeSynReset$okhttp(streamId, errorCode);
                        } catch (IOException e) {
                            this.failConnection(e);
                        }
                    } finally {
                        currentThread.setName(name);
                    }
                }
            });
        } catch (RejectedExecutionException unused) {
        }
    }

    public final void writeSynReset$okhttp(int streamId, ErrorCode statusCode) throws IOException {
        Intrinsics.checkParameterIsNotNull(statusCode, "statusCode");
        this.writer.rstStream(streamId, statusCode);
    }

    public final void writeWindowUpdateLater$okhttp(final int streamId, final long unacknowledgedBytesRead) {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = this.writerExecutor;
        final String str = "OkHttp Window Update " + this.connectionName + " stream " + streamId;
        try {
            scheduledThreadPoolExecutor.execute(new Runnable() { // from class: okhttp3.internal.http2.Http2Connection$writeWindowUpdateLater$$inlined$tryExecute$1
                @Override // java.lang.Runnable
                public final void run() {
                    String str2 = str;
                    Thread currentThread = Thread.currentThread();
                    Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
                    String name = currentThread.getName();
                    currentThread.setName(str2);
                    try {
                        try {
                            this.getWriter().windowUpdate(streamId, unacknowledgedBytesRead);
                        } catch (IOException e) {
                            this.failConnection(e);
                        }
                    } finally {
                        currentThread.setName(name);
                    }
                }
            });
        } catch (RejectedExecutionException unused) {
        }
    }

    public final void writePing(boolean reply, int payload1, int payload2) throws IOException {
        boolean z;
        if (!reply) {
            synchronized (this) {
                z = this.awaitingPong;
                this.awaitingPong = true;
                Unit unit = Unit.INSTANCE;
            }
            if (z) {
                failConnection(null);
                return;
            }
        }
        try {
            this.writer.ping(reply, payload1, payload2);
        } catch (IOException e) {
            failConnection(e);
        }
    }

    public final void writePingAndAwaitPong() throws InterruptedException, IOException {
        writePing(false, 1330343787, -257978967);
        awaitPong();
    }

    public final synchronized void awaitPong() throws InterruptedException {
        while (this.awaitingPong) {
            wait();
        }
    }

    public final void flush() throws IOException {
        this.writer.flush();
    }

    public final void shutdown(ErrorCode statusCode) throws IOException {
        Intrinsics.checkParameterIsNotNull(statusCode, "statusCode");
        synchronized (this.writer) {
            synchronized (this) {
                if (this.isShutdown) {
                    return;
                }
                this.isShutdown = true;
                int i = this.lastGoodStreamId;
                Unit unit = Unit.INSTANCE;
                this.writer.goAway(i, statusCode, Util.EMPTY_BYTE_ARRAY);
                Unit unit2 = Unit.INSTANCE;
            }
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        close$okhttp(ErrorCode.NO_ERROR, ErrorCode.CANCEL, null);
    }

    public final void close$okhttp(ErrorCode connectionCode, ErrorCode streamCode, IOException cause) throws IOException {
        int i;
        Intrinsics.checkParameterIsNotNull(connectionCode, "connectionCode");
        Intrinsics.checkParameterIsNotNull(streamCode, "streamCode");
        boolean z = !Thread.holdsLock(this);
        if (_Assertions.ENABLED && !z) {
            throw new AssertionError("Assertion failed");
        }
        try {
            shutdown(connectionCode);
        } catch (IOException unused) {
        }
        Http2Stream[] http2StreamArr = (Http2Stream[]) null;
        synchronized (this) {
            if (!this.streams.isEmpty()) {
                Collection<Http2Stream> collectionValues = this.streams.values();
                if (collectionValues == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.util.Collection<T>");
                }
                Object[] array = collectionValues.toArray(new Http2Stream[0]);
                if (array != null) {
                    http2StreamArr = (Http2Stream[]) array;
                    this.streams.clear();
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                }
            }
            Unit unit = Unit.INSTANCE;
        }
        if (http2StreamArr != null) {
            for (Http2Stream http2Stream : http2StreamArr) {
                try {
                    http2Stream.close(streamCode, cause);
                } catch (IOException unused2) {
                }
            }
        }
        try {
            this.writer.close();
        } catch (IOException unused3) {
        }
        try {
            this.socket.close();
        } catch (IOException unused4) {
        }
        this.writerExecutor.shutdown();
        this.pushExecutor.shutdown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void failConnection(IOException e) throws IOException {
        close$okhttp(ErrorCode.PROTOCOL_ERROR, ErrorCode.PROTOCOL_ERROR, e);
    }

    public static /* synthetic */ void start$default(Http2Connection http2Connection, boolean z, int i, Object obj) throws IOException {
        if ((i & 1) != 0) {
            z = true;
        }
        http2Connection.start(z);
    }

    public final void start(boolean sendConnectionPreface) throws IOException {
        if (sendConnectionPreface) {
            this.writer.connectionPreface();
            this.writer.settings(this.okHttpSettings);
            if (this.okHttpSettings.getInitialWindowSize() != 65535) {
                this.writer.windowUpdate(0, r6 - 65535);
            }
        }
        new Thread(this.readerRunnable, "OkHttp " + this.connectionName).start();
    }

    public final void setSettings(Settings settings) throws IOException {
        Intrinsics.checkParameterIsNotNull(settings, "settings");
        synchronized (this.writer) {
            synchronized (this) {
                if (this.isShutdown) {
                    throw new ConnectionShutdownException();
                }
                this.okHttpSettings.merge(settings);
                Unit unit = Unit.INSTANCE;
            }
            this.writer.settings(settings);
            Unit unit2 = Unit.INSTANCE;
        }
    }

    /* compiled from: Http2Connection.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u00102\u001a\u000203J\u000e\u0010\u000e\u001a\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0014\u001a\u00020\u00002\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u0010\u001a\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\u001bJ.\u0010&\u001a\u00020\u00002\u0006\u0010&\u001a\u00020'2\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010,\u001a\u00020-2\b\b\u0002\u0010 \u001a\u00020!H\u0007R\u001a\u0010\u0002\u001a\u00020\u0003X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004R\u001a\u0010\b\u001a\u00020\tX\u0080.¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u00020\u000fX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0014\u001a\u00020\u0015X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\u001a\u001a\u00020\u001bX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u001a\u0010 \u001a\u00020!X\u0080.¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R\u001a\u0010&\u001a\u00020'X\u0080.¢\u0006\u000e\n\u0000\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R\u001a\u0010,\u001a\u00020-X\u0080.¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010/\"\u0004\b0\u00101¨\u00064"}, d2 = {"Lokhttp3/internal/http2/Http2Connection$Builder;", "", "client", "", "(Z)V", "getClient$okhttp", "()Z", "setClient$okhttp", "connectionName", "", "getConnectionName$okhttp", "()Ljava/lang/String;", "setConnectionName$okhttp", "(Ljava/lang/String;)V", "listener", "Lokhttp3/internal/http2/Http2Connection$Listener;", "getListener$okhttp", "()Lokhttp3/internal/http2/Http2Connection$Listener;", "setListener$okhttp", "(Lokhttp3/internal/http2/Http2Connection$Listener;)V", "pingIntervalMillis", "", "getPingIntervalMillis$okhttp", "()I", "setPingIntervalMillis$okhttp", "(I)V", "pushObserver", "Lokhttp3/internal/http2/PushObserver;", "getPushObserver$okhttp", "()Lokhttp3/internal/http2/PushObserver;", "setPushObserver$okhttp", "(Lokhttp3/internal/http2/PushObserver;)V", "sink", "Lokio/BufferedSink;", "getSink$okhttp", "()Lokio/BufferedSink;", "setSink$okhttp", "(Lokio/BufferedSink;)V", "socket", "Ljava/net/Socket;", "getSocket$okhttp", "()Ljava/net/Socket;", "setSocket$okhttp", "(Ljava/net/Socket;)V", "source", "Lokio/BufferedSource;", "getSource$okhttp", "()Lokio/BufferedSource;", "setSource$okhttp", "(Lokio/BufferedSource;)V", "build", "Lokhttp3/internal/http2/Http2Connection;", "okhttp"}, k = 1, mv = {1, 1, 15})
    public static final class Builder {
        private boolean client;
        public String connectionName;
        private int pingIntervalMillis;
        public BufferedSink sink;
        public Socket socket;
        public BufferedSource source;
        private Listener listener = Listener.REFUSE_INCOMING_STREAMS;
        private PushObserver pushObserver = PushObserver.CANCEL;

        public final Builder socket(Socket socket) throws IOException {
            return socket$default(this, socket, null, null, null, 14, null);
        }

        public final Builder socket(Socket socket, String str) throws IOException {
            return socket$default(this, socket, str, null, null, 12, null);
        }

        public final Builder socket(Socket socket, String str, BufferedSource bufferedSource) throws IOException {
            return socket$default(this, socket, str, bufferedSource, null, 8, null);
        }

        public Builder(boolean z) {
            this.client = z;
        }

        /* renamed from: getClient$okhttp, reason: from getter */
        public final boolean getClient() {
            return this.client;
        }

        public final void setClient$okhttp(boolean z) {
            this.client = z;
        }

        public final Socket getSocket$okhttp() {
            Socket socket = this.socket;
            if (socket == null) {
                Intrinsics.throwUninitializedPropertyAccessException("socket");
            }
            return socket;
        }

        public final void setSocket$okhttp(Socket socket) {
            Intrinsics.checkParameterIsNotNull(socket, "<set-?>");
            this.socket = socket;
        }

        public final String getConnectionName$okhttp() {
            String str = this.connectionName;
            if (str == null) {
                Intrinsics.throwUninitializedPropertyAccessException("connectionName");
            }
            return str;
        }

        public final void setConnectionName$okhttp(String str) {
            Intrinsics.checkParameterIsNotNull(str, "<set-?>");
            this.connectionName = str;
        }

        public final BufferedSource getSource$okhttp() {
            BufferedSource bufferedSource = this.source;
            if (bufferedSource == null) {
                Intrinsics.throwUninitializedPropertyAccessException("source");
            }
            return bufferedSource;
        }

        public final void setSource$okhttp(BufferedSource bufferedSource) {
            Intrinsics.checkParameterIsNotNull(bufferedSource, "<set-?>");
            this.source = bufferedSource;
        }

        public final BufferedSink getSink$okhttp() {
            BufferedSink bufferedSink = this.sink;
            if (bufferedSink == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sink");
            }
            return bufferedSink;
        }

        public final void setSink$okhttp(BufferedSink bufferedSink) {
            Intrinsics.checkParameterIsNotNull(bufferedSink, "<set-?>");
            this.sink = bufferedSink;
        }

        /* renamed from: getListener$okhttp, reason: from getter */
        public final Listener getListener() {
            return this.listener;
        }

        public final void setListener$okhttp(Listener listener) {
            Intrinsics.checkParameterIsNotNull(listener, "<set-?>");
            this.listener = listener;
        }

        /* renamed from: getPushObserver$okhttp, reason: from getter */
        public final PushObserver getPushObserver() {
            return this.pushObserver;
        }

        public final void setPushObserver$okhttp(PushObserver pushObserver) {
            Intrinsics.checkParameterIsNotNull(pushObserver, "<set-?>");
            this.pushObserver = pushObserver;
        }

        /* renamed from: getPingIntervalMillis$okhttp, reason: from getter */
        public final int getPingIntervalMillis() {
            return this.pingIntervalMillis;
        }

        public final void setPingIntervalMillis$okhttp(int i) {
            this.pingIntervalMillis = i;
        }

        public static /* synthetic */ Builder socket$default(Builder builder, Socket socket, String str, BufferedSource bufferedSource, BufferedSink bufferedSink, int i, Object obj) throws IOException {
            if ((i & 2) != 0) {
                str = Util.connectionName(socket);
            }
            if ((i & 4) != 0) {
                bufferedSource = Okio.buffer(Okio.source(socket));
            }
            if ((i & 8) != 0) {
                bufferedSink = Okio.buffer(Okio.sink(socket));
            }
            return builder.socket(socket, str, bufferedSource, bufferedSink);
        }

        public final Builder socket(Socket socket, String connectionName, BufferedSource source, BufferedSink sink) throws IOException {
            Intrinsics.checkParameterIsNotNull(socket, "socket");
            Intrinsics.checkParameterIsNotNull(connectionName, "connectionName");
            Intrinsics.checkParameterIsNotNull(source, "source");
            Intrinsics.checkParameterIsNotNull(sink, "sink");
            Builder builder = this;
            builder.socket = socket;
            builder.connectionName = connectionName;
            builder.source = source;
            builder.sink = sink;
            return builder;
        }

        public final Builder listener(Listener listener) {
            Intrinsics.checkParameterIsNotNull(listener, "listener");
            Builder builder = this;
            builder.listener = listener;
            return builder;
        }

        public final Builder pushObserver(PushObserver pushObserver) {
            Intrinsics.checkParameterIsNotNull(pushObserver, "pushObserver");
            Builder builder = this;
            builder.pushObserver = pushObserver;
            return builder;
        }

        public final Builder pingIntervalMillis(int pingIntervalMillis) {
            Builder builder = this;
            builder.pingIntervalMillis = pingIntervalMillis;
            return builder;
        }

        public final Http2Connection build() {
            return new Http2Connection(this);
        }
    }

    /* compiled from: Http2Connection.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0012\b\u0086\u0004\u0018\u00002\u00020\u00012\u00020\u0002B\u000f\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\b\u001a\u00020\tH\u0016J8\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u0010\u0010\u0015\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J(\u0010\u0018\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\fH\u0016J \u0010\u001e\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020\f2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u0010H\u0016J.\u0010#\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010$\u001a\u00020\f2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020'0&H\u0016J \u0010(\u001a\u00020\t2\u0006\u0010)\u001a\u00020\u001a2\u0006\u0010*\u001a\u00020\f2\u0006\u0010+\u001a\u00020\fH\u0016J(\u0010,\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010-\u001a\u00020\f2\u0006\u0010.\u001a\u00020\f2\u0006\u0010/\u001a\u00020\u001aH\u0016J&\u00100\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u00101\u001a\u00020\f2\f\u00102\u001a\b\u0012\u0004\u0012\u00020'0&H\u0016J\u0018\u00103\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010 \u001a\u00020!H\u0016J\b\u00104\u001a\u00020\tH\u0016J\u0018\u00105\u001a\u00020\t2\u0006\u00106\u001a\u00020\u001a2\u0006\u00105\u001a\u00020\u0017H\u0016J\u0018\u00107\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u00108\u001a\u00020\u0014H\u0016R\u0014\u0010\u0003\u001a\u00020\u0004X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u00069"}, d2 = {"Lokhttp3/internal/http2/Http2Connection$ReaderRunnable;", "Ljava/lang/Runnable;", "Lokhttp3/internal/http2/Http2Reader$Handler;", "reader", "Lokhttp3/internal/http2/Http2Reader;", "(Lokhttp3/internal/http2/Http2Connection;Lokhttp3/internal/http2/Http2Reader;)V", "getReader$okhttp", "()Lokhttp3/internal/http2/Http2Reader;", "ackSettings", "", "alternateService", "streamId", "", "origin", "", "protocol", "Lokio/ByteString;", "host", "port", "maxAge", "", "applyAndAckSettings", "peerSettings", "Lokhttp3/internal/http2/Settings;", LogContract.LogColumns.DATA, "inFinished", "", "source", "Lokio/BufferedSource;", "length", "goAway", "lastGoodStreamId", "errorCode", "Lokhttp3/internal/http2/ErrorCode;", "debugData", "headers", "associatedStreamId", "headerBlock", "", "Lokhttp3/internal/http2/Header;", "ping", "reply", "payload1", "payload2", "priority", "streamDependency", "weight", "exclusive", "pushPromise", "promisedStreamId", "requestHeaders", "rstStream", "run", "settings", "clearPrevious", "windowUpdate", "windowSizeIncrement", "okhttp"}, k = 1, mv = {1, 1, 15})
    public final class ReaderRunnable implements Runnable, Http2Reader.Handler {
        private final Http2Reader reader;
        final /* synthetic */ Http2Connection this$0;

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void ackSettings() {
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void alternateService(int streamId, String origin, ByteString protocol, String host, int port, long maxAge) {
            Intrinsics.checkParameterIsNotNull(origin, "origin");
            Intrinsics.checkParameterIsNotNull(protocol, "protocol");
            Intrinsics.checkParameterIsNotNull(host, "host");
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void priority(int streamId, int streamDependency, int weight, boolean exclusive) {
        }

        public ReaderRunnable(Http2Connection http2Connection, Http2Reader reader) {
            Intrinsics.checkParameterIsNotNull(reader, "reader");
            this.this$0 = http2Connection;
            this.reader = reader;
        }

        /* renamed from: getReader$okhttp, reason: from getter */
        public final Http2Reader getReader() {
            return this.reader;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v0, types: [okhttp3.internal.http2.ErrorCode] */
        /* JADX WARN: Type inference failed for: r0v7, types: [java.io.Closeable] */
        /* JADX WARN: Type inference failed for: r0v8, types: [okhttp3.internal.http2.ErrorCode] */
        @Override // java.lang.Runnable
        public void run() throws IOException {
            ErrorCode errorCode;
            Http2Reader http2Reader = ErrorCode.INTERNAL_ERROR;
            ErrorCode errorCode2 = ErrorCode.INTERNAL_ERROR;
            IOException e = (IOException) null;
            try {
                try {
                    this.reader.readConnectionPreface(this);
                    while (this.reader.nextFrame(false, this)) {
                    }
                    http2Reader = ErrorCode.NO_ERROR;
                    errorCode2 = ErrorCode.CANCEL;
                    errorCode = http2Reader;
                } catch (IOException e2) {
                    e = e2;
                    ErrorCode errorCode3 = ErrorCode.PROTOCOL_ERROR;
                    errorCode2 = ErrorCode.PROTOCOL_ERROR;
                    errorCode = errorCode3;
                }
            } finally {
                this.this$0.close$okhttp(http2Reader, errorCode2, e);
                Util.closeQuietly(this.reader);
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void data(boolean inFinished, int streamId, BufferedSource source, int length) throws IOException {
            Intrinsics.checkParameterIsNotNull(source, "source");
            if (this.this$0.pushedStream$okhttp(streamId)) {
                this.this$0.pushDataLater$okhttp(streamId, source, length, inFinished);
                return;
            }
            Http2Stream stream = this.this$0.getStream(streamId);
            if (stream == null) {
                this.this$0.writeSynResetLater$okhttp(streamId, ErrorCode.PROTOCOL_ERROR);
                long j = length;
                this.this$0.updateConnectionFlowControl$okhttp(j);
                source.skip(j);
                return;
            }
            stream.receiveData(source, length);
            if (inFinished) {
                stream.receiveHeaders(Util.EMPTY_HEADERS, true);
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void headers(final boolean inFinished, final int streamId, int associatedStreamId, final List<Header> headerBlock) {
            Intrinsics.checkParameterIsNotNull(headerBlock, "headerBlock");
            if (this.this$0.pushedStream$okhttp(streamId)) {
                this.this$0.pushHeadersLater$okhttp(streamId, headerBlock, inFinished);
                return;
            }
            synchronized (this.this$0) {
                final Http2Stream stream = this.this$0.getStream(streamId);
                if (stream == null) {
                    if (this.this$0.isShutdown()) {
                        return;
                    }
                    if (streamId <= this.this$0.getLastGoodStreamId()) {
                        return;
                    }
                    if (streamId % 2 == this.this$0.getNextStreamId() % 2) {
                        return;
                    }
                    final Http2Stream http2Stream = new Http2Stream(streamId, this.this$0, false, inFinished, Util.toHeaders(headerBlock));
                    this.this$0.setLastGoodStreamId$okhttp(streamId);
                    this.this$0.getStreams$okhttp().put(Integer.valueOf(streamId), http2Stream);
                    ThreadPoolExecutor threadPoolExecutor = Http2Connection.listenerExecutor;
                    final String str = "OkHttp " + this.this$0.getConnectionName() + " stream " + streamId;
                    threadPoolExecutor.execute(new Runnable() { // from class: okhttp3.internal.http2.Http2Connection$ReaderRunnable$headers$$inlined$synchronized$lambda$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            String str2 = str;
                            Thread currentThread = Thread.currentThread();
                            Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
                            String name = currentThread.getName();
                            currentThread.setName(str2);
                            try {
                                try {
                                    this.this$0.getListener().onStream(http2Stream);
                                } catch (IOException e) {
                                    Platform.INSTANCE.get().log(4, "Http2Connection.Listener failure for " + this.this$0.getConnectionName(), e);
                                    try {
                                        http2Stream.close(ErrorCode.PROTOCOL_ERROR, e);
                                    } catch (IOException unused) {
                                    }
                                }
                            } finally {
                                currentThread.setName(name);
                            }
                        }
                    });
                    return;
                }
                Unit unit = Unit.INSTANCE;
                stream.receiveHeaders(Util.toHeaders(headerBlock), inFinished);
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void rstStream(int streamId, ErrorCode errorCode) {
            Intrinsics.checkParameterIsNotNull(errorCode, "errorCode");
            if (this.this$0.pushedStream$okhttp(streamId)) {
                this.this$0.pushResetLater$okhttp(streamId, errorCode);
                return;
            }
            Http2Stream http2StreamRemoveStream$okhttp = this.this$0.removeStream$okhttp(streamId);
            if (http2StreamRemoveStream$okhttp != null) {
                http2StreamRemoveStream$okhttp.receiveRstStream(errorCode);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v2, types: [T, okhttp3.internal.http2.Http2Stream[]] */
        /* JADX WARN: Type inference failed for: r1v23, types: [T, okhttp3.internal.http2.Http2Stream[]] */
        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void settings(final boolean clearPrevious, final Settings settings) {
            int i;
            Intrinsics.checkParameterIsNotNull(settings, "settings");
            final Ref.LongRef longRef = new Ref.LongRef();
            longRef.element = 0L;
            final Ref.ObjectRef objectRef = new Ref.ObjectRef();
            objectRef.element = (Http2Stream[]) 0;
            synchronized (this.this$0) {
                int initialWindowSize = this.this$0.getPeerSettings().getInitialWindowSize();
                if (clearPrevious) {
                    this.this$0.getPeerSettings().clear();
                }
                this.this$0.getPeerSettings().merge(settings);
                applyAndAckSettings(settings);
                int initialWindowSize2 = this.this$0.getPeerSettings().getInitialWindowSize();
                if (initialWindowSize2 != -1 && initialWindowSize2 != initialWindowSize) {
                    longRef.element = initialWindowSize2 - initialWindowSize;
                    if (!this.this$0.getReceivedInitialPeerSettings()) {
                        this.this$0.setReceivedInitialPeerSettings$okhttp(true);
                    }
                    if (!this.this$0.getStreams$okhttp().isEmpty()) {
                        Collection<Http2Stream> collectionValues = this.this$0.getStreams$okhttp().values();
                        if (collectionValues == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.util.Collection<T>");
                        }
                        Object[] array = collectionValues.toArray(new Http2Stream[0]);
                        if (array == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                        }
                        objectRef.element = (Http2Stream[]) array;
                    }
                }
                ThreadPoolExecutor threadPoolExecutor = Http2Connection.listenerExecutor;
                final String str = "OkHttp " + this.this$0.getConnectionName() + " settings";
                threadPoolExecutor.execute(new Runnable() { // from class: okhttp3.internal.http2.Http2Connection$ReaderRunnable$settings$$inlined$synchronized$lambda$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        String str2 = str;
                        Thread currentThread = Thread.currentThread();
                        Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
                        String name = currentThread.getName();
                        currentThread.setName(str2);
                        try {
                            this.this$0.getListener().onSettings(this.this$0);
                        } finally {
                            currentThread.setName(name);
                        }
                    }
                });
                Unit unit = Unit.INSTANCE;
            }
            if (((Http2Stream[]) objectRef.element) == null || longRef.element == 0) {
                return;
            }
            Http2Stream[] http2StreamArr = (Http2Stream[]) objectRef.element;
            if (http2StreamArr == null) {
                Intrinsics.throwNpe();
            }
            for (Http2Stream http2Stream : http2StreamArr) {
                synchronized (http2Stream) {
                    http2Stream.addBytesToWriteWindow(longRef.element);
                    Unit unit2 = Unit.INSTANCE;
                }
            }
        }

        private final void applyAndAckSettings(final Settings peerSettings) {
            ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = this.this$0.writerExecutor;
            final String str = "OkHttp " + this.this$0.getConnectionName() + " ACK Settings";
            try {
                scheduledThreadPoolExecutor.execute(new Runnable() { // from class: okhttp3.internal.http2.Http2Connection$ReaderRunnable$applyAndAckSettings$$inlined$tryExecute$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        String str2 = str;
                        Thread currentThread = Thread.currentThread();
                        Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
                        String name = currentThread.getName();
                        currentThread.setName(str2);
                        try {
                            try {
                                this.this$0.getWriter().applyAndAckSettings(peerSettings);
                            } catch (IOException e) {
                                this.this$0.failConnection(e);
                            }
                        } finally {
                            currentThread.setName(name);
                        }
                    }
                });
            } catch (RejectedExecutionException unused) {
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void ping(boolean reply, final int payload1, final int payload2) {
            if (!reply) {
                ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = this.this$0.writerExecutor;
                final String str = "OkHttp " + this.this$0.getConnectionName() + " ping";
                try {
                    scheduledThreadPoolExecutor.execute(new Runnable() { // from class: okhttp3.internal.http2.Http2Connection$ReaderRunnable$ping$$inlined$tryExecute$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            String str2 = str;
                            Thread currentThread = Thread.currentThread();
                            Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
                            String name = currentThread.getName();
                            currentThread.setName(str2);
                            try {
                                this.this$0.writePing(true, payload1, payload2);
                            } finally {
                                currentThread.setName(name);
                            }
                        }
                    });
                    return;
                } catch (RejectedExecutionException unused) {
                    return;
                }
            }
            synchronized (this.this$0) {
                this.this$0.awaitingPong = false;
                Http2Connection http2Connection = this.this$0;
                if (http2Connection != null) {
                    http2Connection.notifyAll();
                    Unit unit = Unit.INSTANCE;
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
                }
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void goAway(int lastGoodStreamId, ErrorCode errorCode, ByteString debugData) {
            int i;
            Http2Stream[] http2StreamArr;
            Intrinsics.checkParameterIsNotNull(errorCode, "errorCode");
            Intrinsics.checkParameterIsNotNull(debugData, "debugData");
            debugData.size();
            synchronized (this.this$0) {
                Collection<Http2Stream> collectionValues = this.this$0.getStreams$okhttp().values();
                if (collectionValues == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.util.Collection<T>");
                }
                Object[] array = collectionValues.toArray(new Http2Stream[0]);
                if (array != null) {
                    http2StreamArr = (Http2Stream[]) array;
                    this.this$0.setShutdown$okhttp(true);
                    Unit unit = Unit.INSTANCE;
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                }
            }
            for (Http2Stream http2Stream : http2StreamArr) {
                if (http2Stream.getId() > lastGoodStreamId && http2Stream.isLocallyInitiated()) {
                    http2Stream.receiveRstStream(ErrorCode.REFUSED_STREAM);
                    this.this$0.removeStream$okhttp(http2Stream.getId());
                }
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void windowUpdate(int streamId, long windowSizeIncrement) {
            if (streamId == 0) {
                synchronized (this.this$0) {
                    Http2Connection http2Connection = this.this$0;
                    http2Connection.setBytesLeftInWriteWindow$okhttp(http2Connection.getBytesLeftInWriteWindow() + windowSizeIncrement);
                    Http2Connection http2Connection2 = this.this$0;
                    if (http2Connection2 != null) {
                        http2Connection2.notifyAll();
                        Unit unit = Unit.INSTANCE;
                    } else {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
                    }
                }
                return;
            }
            Http2Stream stream = this.this$0.getStream(streamId);
            if (stream != null) {
                synchronized (stream) {
                    stream.addBytesToWriteWindow(windowSizeIncrement);
                    Unit unit2 = Unit.INSTANCE;
                }
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void pushPromise(int streamId, int promisedStreamId, List<Header> requestHeaders) {
            Intrinsics.checkParameterIsNotNull(requestHeaders, "requestHeaders");
            this.this$0.pushRequestLater$okhttp(promisedStreamId, requestHeaders);
        }
    }

    public final void pushRequestLater$okhttp(final int streamId, final List<Header> requestHeaders) {
        Intrinsics.checkParameterIsNotNull(requestHeaders, "requestHeaders");
        synchronized (this) {
            if (this.currentPushRequests.contains(Integer.valueOf(streamId))) {
                writeSynResetLater$okhttp(streamId, ErrorCode.PROTOCOL_ERROR);
                return;
            }
            this.currentPushRequests.add(Integer.valueOf(streamId));
            if (this.isShutdown) {
                return;
            }
            ThreadPoolExecutor threadPoolExecutor = this.pushExecutor;
            final String str = "OkHttp " + this.connectionName + " Push Request[" + streamId + ']';
            try {
                threadPoolExecutor.execute(new Runnable() { // from class: okhttp3.internal.http2.Http2Connection$pushRequestLater$$inlined$tryExecute$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        String str2 = str;
                        Thread currentThread = Thread.currentThread();
                        Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
                        String name = currentThread.getName();
                        currentThread.setName(str2);
                        try {
                            if (this.pushObserver.onRequest(streamId, requestHeaders)) {
                                try {
                                    this.getWriter().rstStream(streamId, ErrorCode.CANCEL);
                                    synchronized (this) {
                                        this.currentPushRequests.remove(Integer.valueOf(streamId));
                                    }
                                } catch (IOException unused) {
                                }
                            }
                        } finally {
                            currentThread.setName(name);
                        }
                    }
                });
            } catch (RejectedExecutionException unused) {
            }
        }
    }

    public final void pushHeadersLater$okhttp(final int streamId, final List<Header> requestHeaders, final boolean inFinished) {
        Intrinsics.checkParameterIsNotNull(requestHeaders, "requestHeaders");
        if (this.isShutdown) {
            return;
        }
        ThreadPoolExecutor threadPoolExecutor = this.pushExecutor;
        final String str = "OkHttp " + this.connectionName + " Push Headers[" + streamId + ']';
        try {
            threadPoolExecutor.execute(new Runnable() { // from class: okhttp3.internal.http2.Http2Connection$pushHeadersLater$$inlined$tryExecute$1
                /* JADX WARN: Removed duplicated region for block: B:24:0x003a A[EXC_TOP_SPLITTER, SYNTHETIC] */
                /* JADX WARN: Removed duplicated region for block: B:9:0x0037 A[Catch: IOException -> 0x004e, all -> 0x0052, TryCatch #0 {all -> 0x0052, blocks: (B:3:0x0012, B:5:0x0024, B:7:0x0033, B:9:0x0037, B:10:0x0039, B:12:0x0049, B:15:0x004c, B:16:0x004d), top: B:23:0x0012 }] */
                @Override // java.lang.Runnable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final void run() {
                    /*
                        r6 = this;
                        java.lang.String r0 = r1
                        java.lang.Thread r1 = java.lang.Thread.currentThread()
                        java.lang.String r2 = "currentThread"
                        kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r1, r2)
                        java.lang.String r2 = r1.getName()
                        r1.setName(r0)
                        okhttp3.internal.http2.Http2Connection r0 = r2     // Catch: java.lang.Throwable -> L52
                        okhttp3.internal.http2.PushObserver r0 = okhttp3.internal.http2.Http2Connection.access$getPushObserver$p(r0)     // Catch: java.lang.Throwable -> L52
                        int r3 = r3     // Catch: java.lang.Throwable -> L52
                        java.util.List r4 = r4     // Catch: java.lang.Throwable -> L52
                        boolean r5 = r5     // Catch: java.lang.Throwable -> L52
                        boolean r0 = r0.onHeaders(r3, r4, r5)     // Catch: java.lang.Throwable -> L52
                        if (r0 == 0) goto L31
                        okhttp3.internal.http2.Http2Connection r3 = r2     // Catch: java.io.IOException -> L4e java.lang.Throwable -> L52
                        okhttp3.internal.http2.Http2Writer r3 = r3.getWriter()     // Catch: java.io.IOException -> L4e java.lang.Throwable -> L52
                        int r4 = r3     // Catch: java.io.IOException -> L4e java.lang.Throwable -> L52
                        okhttp3.internal.http2.ErrorCode r5 = okhttp3.internal.http2.ErrorCode.CANCEL     // Catch: java.io.IOException -> L4e java.lang.Throwable -> L52
                        r3.rstStream(r4, r5)     // Catch: java.io.IOException -> L4e java.lang.Throwable -> L52
                    L31:
                        if (r0 != 0) goto L37
                        boolean r0 = r5     // Catch: java.io.IOException -> L4e java.lang.Throwable -> L52
                        if (r0 == 0) goto L4e
                    L37:
                        okhttp3.internal.http2.Http2Connection r0 = r2     // Catch: java.io.IOException -> L4e java.lang.Throwable -> L52
                        monitor-enter(r0)     // Catch: java.io.IOException -> L4e java.lang.Throwable -> L52
                        okhttp3.internal.http2.Http2Connection r3 = r2     // Catch: java.lang.Throwable -> L4b
                        java.util.Set r3 = okhttp3.internal.http2.Http2Connection.access$getCurrentPushRequests$p(r3)     // Catch: java.lang.Throwable -> L4b
                        int r4 = r3     // Catch: java.lang.Throwable -> L4b
                        java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch: java.lang.Throwable -> L4b
                        r3.remove(r4)     // Catch: java.lang.Throwable -> L4b
                        monitor-exit(r0)     // Catch: java.io.IOException -> L4e java.lang.Throwable -> L52
                        goto L4e
                    L4b:
                        r3 = move-exception
                        monitor-exit(r0)     // Catch: java.io.IOException -> L4e java.lang.Throwable -> L52
                        throw r3     // Catch: java.io.IOException -> L4e java.lang.Throwable -> L52
                    L4e:
                        r1.setName(r2)
                        return
                    L52:
                        r0 = move-exception
                        r1.setName(r2)
                        throw r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection$pushHeadersLater$$inlined$tryExecute$1.run():void");
                }
            });
        } catch (RejectedExecutionException unused) {
        }
    }

    public final void pushDataLater$okhttp(final int streamId, BufferedSource source, final int byteCount, final boolean inFinished) throws IOException {
        Intrinsics.checkParameterIsNotNull(source, "source");
        final Buffer buffer = new Buffer();
        long j = byteCount;
        source.require(j);
        source.read(buffer, j);
        if (this.isShutdown) {
            return;
        }
        ThreadPoolExecutor threadPoolExecutor = this.pushExecutor;
        final String str = "OkHttp " + this.connectionName + " Push Data[" + streamId + ']';
        threadPoolExecutor.execute(new Runnable() { // from class: okhttp3.internal.http2.Http2Connection$pushDataLater$$inlined$execute$1
            @Override // java.lang.Runnable
            public final void run() {
                String str2 = str;
                Thread currentThread = Thread.currentThread();
                Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
                String name = currentThread.getName();
                currentThread.setName(str2);
                try {
                    boolean zOnData = this.pushObserver.onData(streamId, buffer, byteCount, inFinished);
                    if (zOnData) {
                        this.getWriter().rstStream(streamId, ErrorCode.CANCEL);
                    }
                    if (zOnData || inFinished) {
                        synchronized (this) {
                            this.currentPushRequests.remove(Integer.valueOf(streamId));
                        }
                    }
                } catch (IOException unused) {
                } catch (Throwable th) {
                    currentThread.setName(name);
                    throw th;
                }
                currentThread.setName(name);
            }
        });
    }

    public final void pushResetLater$okhttp(final int streamId, final ErrorCode errorCode) {
        Intrinsics.checkParameterIsNotNull(errorCode, "errorCode");
        if (this.isShutdown) {
            return;
        }
        ThreadPoolExecutor threadPoolExecutor = this.pushExecutor;
        final String str = "OkHttp " + this.connectionName + " Push Reset[" + streamId + ']';
        threadPoolExecutor.execute(new Runnable() { // from class: okhttp3.internal.http2.Http2Connection$pushResetLater$$inlined$execute$1
            @Override // java.lang.Runnable
            public final void run() {
                String str2 = str;
                Thread currentThread = Thread.currentThread();
                Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
                String name = currentThread.getName();
                currentThread.setName(str2);
                try {
                    this.pushObserver.onReset(streamId, errorCode);
                    synchronized (this) {
                        this.currentPushRequests.remove(Integer.valueOf(streamId));
                    }
                } finally {
                    currentThread.setName(name);
                }
            }
        });
    }
}
