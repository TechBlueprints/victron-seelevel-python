package okio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* compiled from: Okio.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000R\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\r\u0010\u0005\u001a\u00020\u0006H\u0007¢\u0006\u0002\b\u0007\u001a\n\u0010\b\u001a\u00020\u0006*\u00020\t\u001a\n\u0010\n\u001a\u00020\u000b*\u00020\u0006\u001a\n\u0010\n\u001a\u00020\f*\u00020\r\u001a\u0016\u0010\u000e\u001a\u00020\u0006*\u00020\t2\b\b\u0002\u0010\u000f\u001a\u00020\u0001H\u0007\u001a\n\u0010\u000e\u001a\u00020\u0006*\u00020\u0010\u001a\n\u0010\u000e\u001a\u00020\u0006*\u00020\u0011\u001a%\u0010\u000e\u001a\u00020\u0006*\u00020\u00122\u0012\u0010\u0013\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00150\u0014\"\u00020\u0015H\u0007¢\u0006\u0002\u0010\u0016\u001a\n\u0010\u0017\u001a\u00020\r*\u00020\t\u001a\n\u0010\u0017\u001a\u00020\r*\u00020\u0018\u001a\n\u0010\u0017\u001a\u00020\r*\u00020\u0011\u001a%\u0010\u0017\u001a\u00020\r*\u00020\u00122\u0012\u0010\u0013\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00150\u0014\"\u00020\u0015H\u0007¢\u0006\u0002\u0010\u0019\"\u001c\u0010\u0000\u001a\u00020\u0001*\u00060\u0002j\u0002`\u00038@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0000\u0010\u0004¨\u0006\u001a"}, d2 = {"isAndroidGetsocknameError", "", "Ljava/lang/AssertionError;", "Lkotlin/AssertionError;", "(Ljava/lang/AssertionError;)Z", "blackholeSink", "Lokio/Sink;", "blackhole", "appendingSink", "Ljava/io/File;", "buffer", "Lokio/BufferedSink;", "Lokio/BufferedSource;", "Lokio/Source;", "sink", "append", "Ljava/io/OutputStream;", "Ljava/net/Socket;", "Ljava/nio/file/Path;", "options", "", "Ljava/nio/file/OpenOption;", "(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Lokio/Sink;", "source", "Ljava/io/InputStream;", "(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Lokio/Source;", "jvm"}, k = 2, mv = {1, 1, 11})
/* loaded from: classes.dex */
public final class Okio {
    public static final Sink sink(File file) throws FileNotFoundException {
        return sink$default(file, false, 1, null);
    }

    public static final BufferedSource buffer(Source receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return new buffer(receiver);
    }

    public static final BufferedSink buffer(Sink receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return new buffer(receiver);
    }

    public static final Sink sink(OutputStream receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return new sink(receiver, new Timeout());
    }

    public static final Source source(InputStream receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return new source(receiver, new Timeout());
    }

    public static final Sink blackhole() {
        return new BlackholeSink();
    }

    public static final Sink sink(Socket receiver) throws IOException {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        SocketAsyncTimeout socketAsyncTimeout = new SocketAsyncTimeout(receiver);
        OutputStream outputStream = receiver.getOutputStream();
        Intrinsics.checkExpressionValueIsNotNull(outputStream, "getOutputStream()");
        return socketAsyncTimeout.sink(new sink(outputStream, socketAsyncTimeout));
    }

    public static final Source source(Socket receiver) throws IOException {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        SocketAsyncTimeout socketAsyncTimeout = new SocketAsyncTimeout(receiver);
        InputStream inputStream = receiver.getInputStream();
        Intrinsics.checkExpressionValueIsNotNull(inputStream, "getInputStream()");
        return socketAsyncTimeout.source(new source(inputStream, socketAsyncTimeout));
    }

    public static final Sink sink(File receiver, boolean z) throws FileNotFoundException {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return sink(new FileOutputStream(receiver, z));
    }

    public static /* bridge */ /* synthetic */ Sink sink$default(File file, boolean z, int i, Object obj) throws FileNotFoundException {
        if ((i & 1) != 0) {
            z = false;
        }
        return sink(file, z);
    }

    public static final Sink appendingSink(File receiver) throws FileNotFoundException {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return sink(new FileOutputStream(receiver, true));
    }

    public static final Source source(File receiver) throws FileNotFoundException {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return source(new FileInputStream(receiver));
    }

    public static final Sink sink(Path receiver, OpenOption... options) throws IOException {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(options, "options");
        OutputStream outputStreamNewOutputStream = Files.newOutputStream(receiver, (OpenOption[]) Arrays.copyOf(options, options.length));
        Intrinsics.checkExpressionValueIsNotNull(outputStreamNewOutputStream, "Files.newOutputStream(this, *options)");
        return sink(outputStreamNewOutputStream);
    }

    public static final Source source(Path receiver, OpenOption... options) throws IOException {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(options, "options");
        InputStream inputStreamNewInputStream = Files.newInputStream(receiver, (OpenOption[]) Arrays.copyOf(options, options.length));
        Intrinsics.checkExpressionValueIsNotNull(inputStreamNewInputStream, "Files.newInputStream(this, *options)");
        return source(inputStreamNewInputStream);
    }

    public static final boolean isAndroidGetsocknameError(AssertionError receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (receiver.getCause() == null) {
            return false;
        }
        String message = receiver.getMessage();
        return message != null ? StringsKt.contains$default((CharSequence) message, (CharSequence) "getsockname failed", false, 2, (Object) null) : false;
    }
}
