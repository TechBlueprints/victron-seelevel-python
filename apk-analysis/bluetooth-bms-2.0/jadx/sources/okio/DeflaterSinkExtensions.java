package okio;

import java.util.zip.Deflater;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: DeflaterSink.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0086\b¨\u0006\u0005"}, d2 = {"deflate", "Lokio/DeflaterSink;", "Lokio/Sink;", "deflater", "Ljava/util/zip/Deflater;", "jvm"}, k = 2, mv = {1, 1, 11})
/* renamed from: okio.-DeflaterSinkExtensions, reason: invalid class name */
/* loaded from: classes.dex */
public final class DeflaterSinkExtensions {
    public static /* bridge */ /* synthetic */ DeflaterSink deflate$default(Sink receiver, Deflater deflater, int i, Object obj) {
        if ((i & 1) != 0) {
            deflater = new Deflater();
        }
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(deflater, "deflater");
        return new DeflaterSink(receiver, deflater);
    }

    public static final DeflaterSink deflate(Sink receiver, Deflater deflater) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(deflater, "deflater");
        return new DeflaterSink(receiver, deflater);
    }
}
