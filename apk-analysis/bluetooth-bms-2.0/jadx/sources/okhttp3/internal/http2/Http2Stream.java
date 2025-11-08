package okhttp3.internal.http2;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.ArrayDeque;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin._Assertions;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Headers;
import okhttp3.internal.Util;
import okio.AsyncTimeout;
import okio.Buffer;
import okio.BufferedSource;
import okio.Sink;
import okio.Source;
import okio.Timeout;

/* compiled from: Http2Stream.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u008a\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 Y2\u00020\u0001:\u0004YZ[\\B1\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\u0002\u0010\u000bJ\u000e\u0010:\u001a\u00020;2\u0006\u0010<\u001a\u00020\rJ\r\u0010=\u001a\u00020;H\u0000¢\u0006\u0002\b>J\r\u0010?\u001a\u00020;H\u0000¢\u0006\u0002\b@J\u0018\u0010A\u001a\u00020;2\u0006\u0010B\u001a\u00020\u00162\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cJ\u001a\u0010C\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0002J\u000e\u0010D\u001a\u00020;2\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010E\u001a\u00020;2\u0006\u0010F\u001a\u00020\nJ\u0006\u0010G\u001a\u00020HJ\u0006\u0010I\u001a\u00020JJ\u0006\u0010)\u001a\u00020KJ\u0016\u0010L\u001a\u00020;2\u0006\u00101\u001a\u00020M2\u0006\u0010N\u001a\u00020\u0003J\u0016\u0010O\u001a\u00020;2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\b\u001a\u00020\u0007J\u000e\u0010P\u001a\u00020;2\u0006\u0010\u0015\u001a\u00020\u0016J\u0006\u0010Q\u001a\u00020\nJ\u0006\u0010F\u001a\u00020\nJ\r\u0010R\u001a\u00020;H\u0000¢\u0006\u0002\bSJ$\u0010T\u001a\u00020;2\f\u0010U\u001a\b\u0012\u0004\u0012\u00020W0V2\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010X\u001a\u00020\u0007J\u0006\u00108\u001a\u00020KR$\u0010\u000e\u001a\u00020\r2\u0006\u0010\f\u001a\u00020\r@@X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u001e\u0010\u0015\u001a\u0004\u0018\u00010\u00168@X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001c\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u000e\u0010!\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\"\u001a\b\u0012\u0004\u0012\u00020\n0#X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0011\u0010&\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b&\u0010'R\u0011\u0010(\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b(\u0010'R\u0018\u0010)\u001a\u00060*R\u00020\u0000X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u0018\u0010-\u001a\u00060.R\u00020\u0000X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b/\u00100R\u0018\u00101\u001a\u000602R\u00020\u0000X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b3\u00104R$\u00105\u001a\u00020\r2\u0006\u0010\f\u001a\u00020\r@@X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b6\u0010\u0010\"\u0004\b7\u0010\u0012R\u0018\u00108\u001a\u00060*R\u00020\u0000X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b9\u0010,¨\u0006]"}, d2 = {"Lokhttp3/internal/http2/Http2Stream;", "", "id", "", "connection", "Lokhttp3/internal/http2/Http2Connection;", "outFinished", "", "inFinished", "headers", "Lokhttp3/Headers;", "(ILokhttp3/internal/http2/Http2Connection;ZZLokhttp3/Headers;)V", "<set-?>", "", "bytesLeftInWriteWindow", "getBytesLeftInWriteWindow", "()J", "setBytesLeftInWriteWindow$okhttp", "(J)V", "getConnection", "()Lokhttp3/internal/http2/Http2Connection;", "errorCode", "Lokhttp3/internal/http2/ErrorCode;", "getErrorCode$okhttp", "()Lokhttp3/internal/http2/ErrorCode;", "setErrorCode$okhttp", "(Lokhttp3/internal/http2/ErrorCode;)V", "errorException", "Ljava/io/IOException;", "getErrorException$okhttp", "()Ljava/io/IOException;", "setErrorException$okhttp", "(Ljava/io/IOException;)V", "hasResponseHeaders", "headersQueue", "Ljava/util/ArrayDeque;", "getId", "()I", "isLocallyInitiated", "()Z", "isOpen", "readTimeout", "Lokhttp3/internal/http2/Http2Stream$StreamTimeout;", "getReadTimeout$okhttp", "()Lokhttp3/internal/http2/Http2Stream$StreamTimeout;", "sink", "Lokhttp3/internal/http2/Http2Stream$FramingSink;", "getSink$okhttp", "()Lokhttp3/internal/http2/Http2Stream$FramingSink;", "source", "Lokhttp3/internal/http2/Http2Stream$FramingSource;", "getSource$okhttp", "()Lokhttp3/internal/http2/Http2Stream$FramingSource;", "unacknowledgedBytesRead", "getUnacknowledgedBytesRead", "setUnacknowledgedBytesRead$okhttp", "writeTimeout", "getWriteTimeout$okhttp", "addBytesToWriteWindow", "", "delta", "cancelStreamIfNecessary", "cancelStreamIfNecessary$okhttp", "checkOutNotClosed", "checkOutNotClosed$okhttp", "close", "rstStatusCode", "closeInternal", "closeLater", "enqueueTrailers", "trailers", "getSink", "Lokio/Sink;", "getSource", "Lokio/Source;", "Lokio/Timeout;", "receiveData", "Lokio/BufferedSource;", "length", "receiveHeaders", "receiveRstStream", "takeHeaders", "waitForIo", "waitForIo$okhttp", "writeHeaders", "responseHeaders", "", "Lokhttp3/internal/http2/Header;", "flushHeaders", "Companion", "FramingSink", "FramingSource", "StreamTimeout", "okhttp"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes.dex */
public final class Http2Stream {
    public static final long EMIT_BUFFER_SIZE = 16384;
    private long bytesLeftInWriteWindow;
    private final Http2Connection connection;
    private ErrorCode errorCode;
    private IOException errorException;
    private boolean hasResponseHeaders;
    private final ArrayDeque<Headers> headersQueue;
    private final int id;
    private final StreamTimeout readTimeout;
    private final FramingSink sink;
    private final FramingSource source;
    private long unacknowledgedBytesRead;
    private final StreamTimeout writeTimeout;

    public Http2Stream(int i, Http2Connection connection, boolean z, boolean z2, Headers headers) {
        Intrinsics.checkParameterIsNotNull(connection, "connection");
        this.id = i;
        this.connection = connection;
        this.bytesLeftInWriteWindow = connection.getPeerSettings().getInitialWindowSize();
        this.headersQueue = new ArrayDeque<>();
        this.source = new FramingSource(this.connection.getOkHttpSettings().getInitialWindowSize(), z2);
        this.sink = new FramingSink(z);
        this.readTimeout = new StreamTimeout();
        this.writeTimeout = new StreamTimeout();
        if (headers != null) {
            if (!(!isLocallyInitiated())) {
                throw new IllegalStateException("locally-initiated streams shouldn't have headers yet".toString());
            }
            this.headersQueue.add(headers);
        } else if (!isLocallyInitiated()) {
            throw new IllegalStateException("remotely-initiated streams should have headers".toString());
        }
    }

    public final int getId() {
        return this.id;
    }

    public final Http2Connection getConnection() {
        return this.connection;
    }

    public final long getUnacknowledgedBytesRead() {
        return this.unacknowledgedBytesRead;
    }

    public final void setUnacknowledgedBytesRead$okhttp(long j) {
        this.unacknowledgedBytesRead = j;
    }

    public final long getBytesLeftInWriteWindow() {
        return this.bytesLeftInWriteWindow;
    }

    public final void setBytesLeftInWriteWindow$okhttp(long j) {
        this.bytesLeftInWriteWindow = j;
    }

    /* renamed from: getSource$okhttp, reason: from getter */
    public final FramingSource getSource() {
        return this.source;
    }

    /* renamed from: getSink$okhttp, reason: from getter */
    public final FramingSink getSink() {
        return this.sink;
    }

    /* renamed from: getReadTimeout$okhttp, reason: from getter */
    public final StreamTimeout getReadTimeout() {
        return this.readTimeout;
    }

    /* renamed from: getWriteTimeout$okhttp, reason: from getter */
    public final StreamTimeout getWriteTimeout() {
        return this.writeTimeout;
    }

    public final synchronized ErrorCode getErrorCode$okhttp() {
        return this.errorCode;
    }

    public final void setErrorCode$okhttp(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    /* renamed from: getErrorException$okhttp, reason: from getter */
    public final IOException getErrorException() {
        return this.errorException;
    }

    public final void setErrorException$okhttp(IOException iOException) {
        this.errorException = iOException;
    }

    public final synchronized boolean isOpen() {
        if (this.errorCode != null) {
            return false;
        }
        if ((this.source.getFinished() || this.source.getClosed()) && (this.sink.getFinished() || this.sink.getClosed())) {
            if (this.hasResponseHeaders) {
                return false;
            }
        }
        return true;
    }

    public final boolean isLocallyInitiated() {
        return this.connection.getClient() == ((this.id & 1) == 1);
    }

    public final synchronized Headers takeHeaders() throws IOException {
        Headers headersRemoveFirst;
        this.readTimeout.enter();
        while (this.headersQueue.isEmpty() && this.errorCode == null) {
            try {
                waitForIo$okhttp();
            } catch (Throwable th) {
                this.readTimeout.exitAndThrowIfTimedOut();
                throw th;
            }
        }
        this.readTimeout.exitAndThrowIfTimedOut();
        if (!this.headersQueue.isEmpty()) {
            headersRemoveFirst = this.headersQueue.removeFirst();
            Intrinsics.checkExpressionValueIsNotNull(headersRemoveFirst, "headersQueue.removeFirst()");
        } else {
            Throwable streamResetException = this.errorException;
            if (streamResetException == null) {
                ErrorCode errorCode = this.errorCode;
                if (errorCode == null) {
                    Intrinsics.throwNpe();
                }
                streamResetException = new StreamResetException(errorCode);
            }
            throw streamResetException;
        }
        return headersRemoveFirst;
    }

    public final synchronized Headers trailers() throws IOException {
        Headers trailers;
        if (this.errorCode != null) {
            Throwable streamResetException = this.errorException;
            if (streamResetException == null) {
                ErrorCode errorCode = this.errorCode;
                if (errorCode == null) {
                    Intrinsics.throwNpe();
                }
                streamResetException = new StreamResetException(errorCode);
            }
            throw streamResetException;
        }
        if (!(this.source.getFinished() && this.source.getReceiveBuffer().exhausted() && this.source.getReadBuffer().exhausted())) {
            throw new IllegalStateException("too early; can't read the trailers yet".toString());
        }
        trailers = this.source.getTrailers();
        if (trailers == null) {
            trailers = Util.EMPTY_HEADERS;
        }
        return trailers;
    }

    public final void writeHeaders(List<Header> responseHeaders, boolean outFinished, boolean flushHeaders) throws IOException {
        boolean z;
        Intrinsics.checkParameterIsNotNull(responseHeaders, "responseHeaders");
        boolean z2 = !Thread.holdsLock(this);
        if (_Assertions.ENABLED && !z2) {
            throw new AssertionError("Assertion failed");
        }
        synchronized (this) {
            this.hasResponseHeaders = true;
            if (outFinished) {
                this.sink.setFinished(true);
            }
            Unit unit = Unit.INSTANCE;
        }
        if (!flushHeaders) {
            synchronized (this.connection) {
                z = this.connection.getBytesLeftInWriteWindow() == 0;
                Unit unit2 = Unit.INSTANCE;
            }
            flushHeaders = z;
        }
        this.connection.writeHeaders$okhttp(this.id, outFinished, responseHeaders);
        if (flushHeaders) {
            this.connection.flush();
        }
    }

    public final void enqueueTrailers(Headers trailers) {
        Intrinsics.checkParameterIsNotNull(trailers, "trailers");
        synchronized (this) {
            boolean z = true;
            if (!(!this.sink.getFinished())) {
                throw new IllegalStateException("already finished".toString());
            }
            if (trailers.size() == 0) {
                z = false;
            }
            if (!z) {
                throw new IllegalArgumentException("trailers.size() == 0".toString());
            }
            this.sink.setTrailers(trailers);
            Unit unit = Unit.INSTANCE;
        }
    }

    public final Timeout readTimeout() {
        return this.readTimeout;
    }

    public final Timeout writeTimeout() {
        return this.writeTimeout;
    }

    public final Source getSource() {
        return this.source;
    }

    public final Sink getSink() {
        synchronized (this) {
            if (!(this.hasResponseHeaders || isLocallyInitiated())) {
                throw new IllegalStateException("reply before requesting the sink".toString());
            }
            Unit unit = Unit.INSTANCE;
        }
        return this.sink;
    }

    public final void close(ErrorCode rstStatusCode, IOException errorException) throws IOException {
        Intrinsics.checkParameterIsNotNull(rstStatusCode, "rstStatusCode");
        if (closeInternal(rstStatusCode, errorException)) {
            this.connection.writeSynReset$okhttp(this.id, rstStatusCode);
        }
    }

    public final void closeLater(ErrorCode errorCode) {
        Intrinsics.checkParameterIsNotNull(errorCode, "errorCode");
        if (closeInternal(errorCode, null)) {
            this.connection.writeSynResetLater$okhttp(this.id, errorCode);
        }
    }

    private final boolean closeInternal(ErrorCode errorCode, IOException errorException) {
        boolean z = !Thread.holdsLock(this);
        if (_Assertions.ENABLED && !z) {
            throw new AssertionError("Assertion failed");
        }
        synchronized (this) {
            if (this.errorCode != null) {
                return false;
            }
            if (this.source.getFinished() && this.sink.getFinished()) {
                return false;
            }
            this.errorCode = errorCode;
            this.errorException = errorException;
            notifyAll();
            Unit unit = Unit.INSTANCE;
            this.connection.removeStream$okhttp(this.id);
            return true;
        }
    }

    public final void receiveData(BufferedSource source, int length) throws IOException {
        Intrinsics.checkParameterIsNotNull(source, "source");
        boolean z = !Thread.holdsLock(this);
        if (_Assertions.ENABLED && !z) {
            throw new AssertionError("Assertion failed");
        }
        this.source.receive$okhttp(source, length);
    }

    public final void receiveHeaders(Headers headers, boolean inFinished) {
        boolean zIsOpen;
        Intrinsics.checkParameterIsNotNull(headers, "headers");
        boolean z = !Thread.holdsLock(this);
        if (_Assertions.ENABLED && !z) {
            throw new AssertionError("Assertion failed");
        }
        synchronized (this) {
            if (!this.hasResponseHeaders || !inFinished) {
                this.hasResponseHeaders = true;
                this.headersQueue.add(headers);
            } else {
                this.source.setTrailers(headers);
            }
            if (inFinished) {
                this.source.setFinished$okhttp(true);
            }
            zIsOpen = isOpen();
            notifyAll();
            Unit unit = Unit.INSTANCE;
        }
        if (zIsOpen) {
            return;
        }
        this.connection.removeStream$okhttp(this.id);
    }

    public final synchronized void receiveRstStream(ErrorCode errorCode) {
        Intrinsics.checkParameterIsNotNull(errorCode, "errorCode");
        if (this.errorCode == null) {
            this.errorCode = errorCode;
            notifyAll();
        }
    }

    /* compiled from: Http2Stream.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0017\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\u0018\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\u0003H\u0016J\u001d\u0010\u001f\u001a\u00020\u001b2\u0006\u0010 \u001a\u00020!2\u0006\u0010\u001e\u001a\u00020\u0003H\u0000¢\u0006\u0002\b\"J\b\u0010#\u001a\u00020$H\u0016J\u0010\u0010%\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0003H\u0002R\u001a\u0010\u0007\u001a\u00020\u0005X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001a\u0010\u0004\u001a\u00020\u0005X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\t\"\u0004\b\r\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u000e\u001a\u00020\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0012\u001a\u00020\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u001c\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019¨\u0006&"}, d2 = {"Lokhttp3/internal/http2/Http2Stream$FramingSource;", "Lokio/Source;", "maxByteCount", "", "finished", "", "(Lokhttp3/internal/http2/Http2Stream;JZ)V", "closed", "getClosed$okhttp", "()Z", "setClosed$okhttp", "(Z)V", "getFinished$okhttp", "setFinished$okhttp", "readBuffer", "Lokio/Buffer;", "getReadBuffer", "()Lokio/Buffer;", "receiveBuffer", "getReceiveBuffer", "trailers", "Lokhttp3/Headers;", "getTrailers", "()Lokhttp3/Headers;", "setTrailers", "(Lokhttp3/Headers;)V", "close", "", "read", "sink", "byteCount", "receive", "source", "Lokio/BufferedSource;", "receive$okhttp", "timeout", "Lokio/Timeout;", "updateConnectionFlowControl", "okhttp"}, k = 1, mv = {1, 1, 15})
    public final class FramingSource implements Source {
        private boolean closed;
        private boolean finished;
        private final long maxByteCount;
        private Headers trailers;
        private final Buffer receiveBuffer = new Buffer();
        private final Buffer readBuffer = new Buffer();

        public FramingSource(long j, boolean z) {
            this.maxByteCount = j;
            this.finished = z;
        }

        /* renamed from: getFinished$okhttp, reason: from getter */
        public final boolean getFinished() {
            return this.finished;
        }

        public final void setFinished$okhttp(boolean z) {
            this.finished = z;
        }

        public final Buffer getReceiveBuffer() {
            return this.receiveBuffer;
        }

        public final Buffer getReadBuffer() {
            return this.readBuffer;
        }

        public final Headers getTrailers() {
            return this.trailers;
        }

        public final void setTrailers(Headers headers) {
            this.trailers = headers;
        }

        /* renamed from: getClosed$okhttp, reason: from getter */
        public final boolean getClosed() {
            return this.closed;
        }

        public final void setClosed$okhttp(boolean z) {
            this.closed = z;
        }

        @Override // okio.Source
        public long read(Buffer sink, long byteCount) throws IOException {
            StreamResetException errorException;
            long j;
            boolean z;
            Intrinsics.checkParameterIsNotNull(sink, "sink");
            if (!(byteCount >= 0)) {
                throw new IllegalArgumentException(("byteCount < 0: " + byteCount).toString());
            }
            do {
                errorException = (IOException) null;
                synchronized (Http2Stream.this) {
                    Http2Stream.this.getReadTimeout().enter();
                    try {
                        if (Http2Stream.this.getErrorCode$okhttp() != null && (errorException = Http2Stream.this.getErrorException()) == null) {
                            ErrorCode errorCode$okhttp = Http2Stream.this.getErrorCode$okhttp();
                            if (errorCode$okhttp == null) {
                                Intrinsics.throwNpe();
                            }
                            errorException = new StreamResetException(errorCode$okhttp);
                        }
                        if (this.closed) {
                            throw new IOException("stream closed");
                        }
                        if (this.readBuffer.size() > 0) {
                            j = this.readBuffer.read(sink, Math.min(byteCount, this.readBuffer.size()));
                            Http2Stream http2Stream = Http2Stream.this;
                            http2Stream.setUnacknowledgedBytesRead$okhttp(http2Stream.getUnacknowledgedBytesRead() + j);
                            if (errorException == null && Http2Stream.this.getUnacknowledgedBytesRead() >= Http2Stream.this.getConnection().getOkHttpSettings().getInitialWindowSize() / 2) {
                                Http2Stream.this.getConnection().writeWindowUpdateLater$okhttp(Http2Stream.this.getId(), Http2Stream.this.getUnacknowledgedBytesRead());
                                Http2Stream.this.setUnacknowledgedBytesRead$okhttp(0L);
                            }
                        } else if (this.finished || errorException != null) {
                            j = -1;
                        } else {
                            Http2Stream.this.waitForIo$okhttp();
                            j = -1;
                            z = true;
                            Http2Stream.this.getReadTimeout().exitAndThrowIfTimedOut();
                            Unit unit = Unit.INSTANCE;
                        }
                        z = false;
                        Http2Stream.this.getReadTimeout().exitAndThrowIfTimedOut();
                        Unit unit2 = Unit.INSTANCE;
                    } catch (Throwable th) {
                        Http2Stream.this.getReadTimeout().exitAndThrowIfTimedOut();
                        throw th;
                    }
                }
            } while (z);
            if (j != -1) {
                updateConnectionFlowControl(j);
                return j;
            }
            if (errorException == null) {
                return -1L;
            }
            if (errorException == null) {
                Intrinsics.throwNpe();
            }
            throw errorException;
        }

        private final void updateConnectionFlowControl(long read) {
            boolean z = !Thread.holdsLock(Http2Stream.this);
            if (_Assertions.ENABLED && !z) {
                throw new AssertionError("Assertion failed");
            }
            Http2Stream.this.getConnection().updateConnectionFlowControl$okhttp(read);
        }

        public final void receive$okhttp(BufferedSource source, long byteCount) throws IOException {
            boolean z;
            boolean z2;
            Intrinsics.checkParameterIsNotNull(source, "source");
            boolean z3 = !Thread.holdsLock(Http2Stream.this);
            if (_Assertions.ENABLED && !z3) {
                throw new AssertionError("Assertion failed");
            }
            while (byteCount > 0) {
                synchronized (Http2Stream.this) {
                    z = this.finished;
                    z2 = this.readBuffer.size() + byteCount > this.maxByteCount;
                    Unit unit = Unit.INSTANCE;
                }
                if (z2) {
                    source.skip(byteCount);
                    Http2Stream.this.closeLater(ErrorCode.FLOW_CONTROL_ERROR);
                    return;
                }
                if (z) {
                    source.skip(byteCount);
                    return;
                }
                long j = source.read(this.receiveBuffer, byteCount);
                if (j == -1) {
                    throw new EOFException();
                }
                byteCount -= j;
                synchronized (Http2Stream.this) {
                    boolean z4 = this.readBuffer.size() == 0;
                    this.readBuffer.writeAll(this.receiveBuffer);
                    if (z4) {
                        Http2Stream http2Stream = Http2Stream.this;
                        if (http2Stream == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
                        }
                        http2Stream.notifyAll();
                    }
                    Unit unit2 = Unit.INSTANCE;
                }
            }
        }

        @Override // okio.Source
        /* renamed from: timeout */
        public Timeout getTimeout() {
            return Http2Stream.this.getReadTimeout();
        }

        @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            long size;
            synchronized (Http2Stream.this) {
                this.closed = true;
                size = this.readBuffer.size();
                this.readBuffer.clear();
                Http2Stream http2Stream = Http2Stream.this;
                if (http2Stream != null) {
                    http2Stream.notifyAll();
                    Unit unit = Unit.INSTANCE;
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
                }
            }
            if (size > 0) {
                updateConnectionFlowControl(size);
            }
            Http2Stream.this.cancelStreamIfNecessary$okhttp();
        }
    }

    public final void cancelStreamIfNecessary$okhttp() throws IOException {
        boolean zIsOpen;
        boolean z = true;
        boolean z2 = !Thread.holdsLock(this);
        if (_Assertions.ENABLED && !z2) {
            throw new AssertionError("Assertion failed");
        }
        synchronized (this) {
            if (this.source.getFinished() || !this.source.getClosed() || (!this.sink.getFinished() && !this.sink.getClosed())) {
                z = false;
            }
            zIsOpen = isOpen();
            Unit unit = Unit.INSTANCE;
        }
        if (z) {
            close(ErrorCode.CANCEL, null);
        } else {
            if (zIsOpen) {
                return;
            }
            this.connection.removeStream$okhttp(this.id);
        }
    }

    /* compiled from: Http2Stream.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\b\u0080\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\u0010\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0003H\u0002J\b\u0010\u0018\u001a\u00020\u0015H\u0016J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\u0018\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\u001eH\u0016R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0007\"\u0004\b\u000b\u0010\tR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013¨\u0006\u001f"}, d2 = {"Lokhttp3/internal/http2/Http2Stream$FramingSink;", "Lokio/Sink;", "finished", "", "(Lokhttp3/internal/http2/Http2Stream;Z)V", "closed", "getClosed", "()Z", "setClosed", "(Z)V", "getFinished", "setFinished", "sendBuffer", "Lokio/Buffer;", "trailers", "Lokhttp3/Headers;", "getTrailers", "()Lokhttp3/Headers;", "setTrailers", "(Lokhttp3/Headers;)V", "close", "", "emitFrame", "outFinishedOnLastFrame", "flush", "timeout", "Lokio/Timeout;", "write", "source", "byteCount", "", "okhttp"}, k = 1, mv = {1, 1, 15})
    public final class FramingSink implements Sink {
        private boolean closed;
        private boolean finished;
        private final Buffer sendBuffer;
        private Headers trailers;

        public FramingSink(boolean z) {
            this.finished = z;
            this.sendBuffer = new Buffer();
        }

        public /* synthetic */ FramingSink(Http2Stream http2Stream, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this((i & 1) != 0 ? false : z);
        }

        public final boolean getFinished() {
            return this.finished;
        }

        public final void setFinished(boolean z) {
            this.finished = z;
        }

        public final Headers getTrailers() {
            return this.trailers;
        }

        public final void setTrailers(Headers headers) {
            this.trailers = headers;
        }

        public final boolean getClosed() {
            return this.closed;
        }

        public final void setClosed(boolean z) {
            this.closed = z;
        }

        @Override // okio.Sink
        public void write(Buffer source, long byteCount) throws IOException {
            Intrinsics.checkParameterIsNotNull(source, "source");
            boolean z = !Thread.holdsLock(Http2Stream.this);
            if (_Assertions.ENABLED && !z) {
                throw new AssertionError("Assertion failed");
            }
            this.sendBuffer.write(source, byteCount);
            while (this.sendBuffer.size() >= 16384) {
                emitFrame(false);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:24:0x0073  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private final void emitFrame(boolean r12) throws java.io.IOException {
            /*
                r11 = this;
                okhttp3.internal.http2.Http2Stream r0 = okhttp3.internal.http2.Http2Stream.this
                monitor-enter(r0)
                okhttp3.internal.http2.Http2Stream r1 = okhttp3.internal.http2.Http2Stream.this     // Catch: java.lang.Throwable -> La5
                okhttp3.internal.http2.Http2Stream$StreamTimeout r1 = r1.getWriteTimeout()     // Catch: java.lang.Throwable -> La5
                r1.enter()     // Catch: java.lang.Throwable -> La5
            Lc:
                okhttp3.internal.http2.Http2Stream r1 = okhttp3.internal.http2.Http2Stream.this     // Catch: java.lang.Throwable -> L9a
                long r1 = r1.getBytesLeftInWriteWindow()     // Catch: java.lang.Throwable -> L9a
                r3 = 0
                int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
                if (r5 > 0) goto L2e
                boolean r1 = r11.finished     // Catch: java.lang.Throwable -> L9a
                if (r1 != 0) goto L2e
                boolean r1 = r11.closed     // Catch: java.lang.Throwable -> L9a
                if (r1 != 0) goto L2e
                okhttp3.internal.http2.Http2Stream r1 = okhttp3.internal.http2.Http2Stream.this     // Catch: java.lang.Throwable -> L9a
                okhttp3.internal.http2.ErrorCode r1 = r1.getErrorCode$okhttp()     // Catch: java.lang.Throwable -> L9a
                if (r1 != 0) goto L2e
                okhttp3.internal.http2.Http2Stream r1 = okhttp3.internal.http2.Http2Stream.this     // Catch: java.lang.Throwable -> L9a
                r1.waitForIo$okhttp()     // Catch: java.lang.Throwable -> L9a
                goto Lc
            L2e:
                okhttp3.internal.http2.Http2Stream r1 = okhttp3.internal.http2.Http2Stream.this     // Catch: java.lang.Throwable -> La5
                okhttp3.internal.http2.Http2Stream$StreamTimeout r1 = r1.getWriteTimeout()     // Catch: java.lang.Throwable -> La5
                r1.exitAndThrowIfTimedOut()     // Catch: java.lang.Throwable -> La5
                okhttp3.internal.http2.Http2Stream r1 = okhttp3.internal.http2.Http2Stream.this     // Catch: java.lang.Throwable -> La5
                r1.checkOutNotClosed$okhttp()     // Catch: java.lang.Throwable -> La5
                okhttp3.internal.http2.Http2Stream r1 = okhttp3.internal.http2.Http2Stream.this     // Catch: java.lang.Throwable -> La5
                long r1 = r1.getBytesLeftInWriteWindow()     // Catch: java.lang.Throwable -> La5
                okio.Buffer r3 = r11.sendBuffer     // Catch: java.lang.Throwable -> La5
                long r3 = r3.size()     // Catch: java.lang.Throwable -> La5
                long r9 = java.lang.Math.min(r1, r3)     // Catch: java.lang.Throwable -> La5
                okhttp3.internal.http2.Http2Stream r1 = okhttp3.internal.http2.Http2Stream.this     // Catch: java.lang.Throwable -> La5
                long r2 = r1.getBytesLeftInWriteWindow()     // Catch: java.lang.Throwable -> La5
                long r2 = r2 - r9
                r1.setBytesLeftInWriteWindow$okhttp(r2)     // Catch: java.lang.Throwable -> La5
                kotlin.Unit r1 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> La5
                monitor-exit(r0)
                okhttp3.internal.http2.Http2Stream r0 = okhttp3.internal.http2.Http2Stream.this
                okhttp3.internal.http2.Http2Stream$StreamTimeout r0 = r0.getWriteTimeout()
                r0.enter()
                if (r12 == 0) goto L73
                okio.Buffer r12 = r11.sendBuffer     // Catch: java.lang.Throwable -> L71
                long r0 = r12.size()     // Catch: java.lang.Throwable -> L71
                int r12 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
                if (r12 != 0) goto L73
                r12 = 1
                r7 = 1
                goto L75
            L71:
                r12 = move-exception
                goto L90
            L73:
                r12 = 0
                r7 = 0
            L75:
                okhttp3.internal.http2.Http2Stream r12 = okhttp3.internal.http2.Http2Stream.this     // Catch: java.lang.Throwable -> L71
                okhttp3.internal.http2.Http2Connection r5 = r12.getConnection()     // Catch: java.lang.Throwable -> L71
                okhttp3.internal.http2.Http2Stream r12 = okhttp3.internal.http2.Http2Stream.this     // Catch: java.lang.Throwable -> L71
                int r6 = r12.getId()     // Catch: java.lang.Throwable -> L71
                okio.Buffer r8 = r11.sendBuffer     // Catch: java.lang.Throwable -> L71
                r5.writeData(r6, r7, r8, r9)     // Catch: java.lang.Throwable -> L71
                okhttp3.internal.http2.Http2Stream r12 = okhttp3.internal.http2.Http2Stream.this
                okhttp3.internal.http2.Http2Stream$StreamTimeout r12 = r12.getWriteTimeout()
                r12.exitAndThrowIfTimedOut()
                return
            L90:
                okhttp3.internal.http2.Http2Stream r0 = okhttp3.internal.http2.Http2Stream.this
                okhttp3.internal.http2.Http2Stream$StreamTimeout r0 = r0.getWriteTimeout()
                r0.exitAndThrowIfTimedOut()
                throw r12
            L9a:
                r12 = move-exception
                okhttp3.internal.http2.Http2Stream r1 = okhttp3.internal.http2.Http2Stream.this     // Catch: java.lang.Throwable -> La5
                okhttp3.internal.http2.Http2Stream$StreamTimeout r1 = r1.getWriteTimeout()     // Catch: java.lang.Throwable -> La5
                r1.exitAndThrowIfTimedOut()     // Catch: java.lang.Throwable -> La5
                throw r12     // Catch: java.lang.Throwable -> La5
            La5:
                r12 = move-exception
                monitor-exit(r0)
                throw r12
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Stream.FramingSink.emitFrame(boolean):void");
        }

        @Override // okio.Sink, java.io.Flushable
        public void flush() throws IOException {
            boolean z = !Thread.holdsLock(Http2Stream.this);
            if (_Assertions.ENABLED && !z) {
                throw new AssertionError("Assertion failed");
            }
            synchronized (Http2Stream.this) {
                Http2Stream.this.checkOutNotClosed$okhttp();
                Unit unit = Unit.INSTANCE;
            }
            while (this.sendBuffer.size() > 0) {
                emitFrame(false);
                Http2Stream.this.getConnection().flush();
            }
        }

        @Override // okio.Sink
        /* renamed from: timeout */
        public Timeout getTimeout() {
            return Http2Stream.this.getWriteTimeout();
        }

        @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            boolean z = !Thread.holdsLock(Http2Stream.this);
            if (_Assertions.ENABLED && !z) {
                throw new AssertionError("Assertion failed");
            }
            synchronized (Http2Stream.this) {
                if (this.closed) {
                    return;
                }
                Unit unit = Unit.INSTANCE;
                if (!Http2Stream.this.getSink().finished) {
                    boolean z2 = this.sendBuffer.size() > 0;
                    if (this.trailers != null) {
                        while (this.sendBuffer.size() > 0) {
                            emitFrame(false);
                        }
                        Http2Connection connection = Http2Stream.this.getConnection();
                        int id = Http2Stream.this.getId();
                        Headers headers = this.trailers;
                        if (headers == null) {
                            Intrinsics.throwNpe();
                        }
                        connection.writeHeaders$okhttp(id, true, Util.toHeaderList(headers));
                    } else if (z2) {
                        while (this.sendBuffer.size() > 0) {
                            emitFrame(true);
                        }
                    } else {
                        Http2Stream.this.getConnection().writeData(Http2Stream.this.getId(), true, null, 0L);
                    }
                }
                synchronized (Http2Stream.this) {
                    this.closed = true;
                    Unit unit2 = Unit.INSTANCE;
                }
                Http2Stream.this.getConnection().flush();
                Http2Stream.this.cancelStreamIfNecessary$okhttp();
            }
        }
    }

    public final void addBytesToWriteWindow(long delta) {
        this.bytesLeftInWriteWindow += delta;
        if (delta > 0) {
            notifyAll();
        }
    }

    public final void checkOutNotClosed$okhttp() throws Throwable {
        if (this.sink.getClosed()) {
            throw new IOException("stream closed");
        }
        if (this.sink.getFinished()) {
            throw new IOException("stream finished");
        }
        if (this.errorCode != null) {
            Throwable streamResetException = this.errorException;
            if (streamResetException == null) {
                ErrorCode errorCode = this.errorCode;
                if (errorCode == null) {
                    Intrinsics.throwNpe();
                }
                streamResetException = new StreamResetException(errorCode);
            }
            throw streamResetException;
        }
    }

    /* compiled from: Http2Stream.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0080\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004J\u0012\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0014J\b\u0010\b\u001a\u00020\u0004H\u0014¨\u0006\t"}, d2 = {"Lokhttp3/internal/http2/Http2Stream$StreamTimeout;", "Lokio/AsyncTimeout;", "(Lokhttp3/internal/http2/Http2Stream;)V", "exitAndThrowIfTimedOut", "", "newTimeoutException", "Ljava/io/IOException;", "cause", "timedOut", "okhttp"}, k = 1, mv = {1, 1, 15})
    public final class StreamTimeout extends AsyncTimeout {
        public StreamTimeout() {
        }

        @Override // okio.AsyncTimeout
        protected void timedOut() {
            Http2Stream.this.closeLater(ErrorCode.CANCEL);
        }

        @Override // okio.AsyncTimeout
        protected IOException newTimeoutException(IOException cause) {
            SocketTimeoutException socketTimeoutException = new SocketTimeoutException("timeout");
            if (cause != null) {
                socketTimeoutException.initCause(cause);
            }
            return socketTimeoutException;
        }

        public final void exitAndThrowIfTimedOut() throws IOException {
            if (exit()) {
                throw newTimeoutException(null);
            }
        }
    }

    public final void waitForIo$okhttp() throws InterruptedException, InterruptedIOException {
        try {
            wait();
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException();
        }
    }
}
