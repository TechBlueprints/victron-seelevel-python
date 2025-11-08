package okio;

import java.util.zip.Inflater;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: InflaterSource.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0086\b¨\u0006\u0005"}, d2 = {"inflate", "Lokio/InflaterSource;", "Lokio/Source;", "inflater", "Ljava/util/zip/Inflater;", "jvm"}, k = 2, mv = {1, 1, 11})
/* renamed from: okio.-InflaterSourceExtensions, reason: invalid class name */
/* loaded from: classes.dex */
public final class InflaterSourceExtensions {
    public static /* bridge */ /* synthetic */ InflaterSource inflate$default(Source receiver, Inflater inflater, int i, Object obj) {
        if ((i & 1) != 0) {
            inflater = new Inflater();
        }
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(inflater, "inflater");
        return new InflaterSource(receiver, inflater);
    }

    public static final InflaterSource inflate(Source receiver, Inflater inflater) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(inflater, "inflater");
        return new InflaterSource(receiver, inflater);
    }
}
