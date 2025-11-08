package kotlinx.coroutines.flow;

import androidx.exifinterface.media.ExifInterface;
import com.jkcq.base.constants.ConstantLanguages;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.functions.Function6;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.DelayKt;
import no.nordicsemi.android.log.LogContract;

/* compiled from: Migration.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0082\u0001\n\u0000\n\u0002\u0010\u0001\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\r\u0010\u0000\u001a\u00020\u0001H\u0002¢\u0006\u0002\b\u0002\u001a»\u0001\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0000\u0010\u0006\"\u0004\b\u0001\u0010\u0007\"\u0004\b\u0002\u0010\b\"\u0004\b\u0003\u0010\t\"\u0004\b\u0004\u0010\n\"\u0004\b\u0005\u0010\u0005*\b\u0012\u0004\u0012\u0002H\u00060\u00042\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00070\u00042\f\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\b0\u00042\f\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\t0\u00042\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\n0\u00042<\b\u0004\u0010\u000f\u001a6\b\u0001\u0012\u0004\u0012\u0002H\u0006\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u0002H\b\u0012\u0004\u0012\u0002H\t\u0012\u0004\u0012\u0002H\n\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00050\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0010H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0013\u001a¡\u0001\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0000\u0010\u0006\"\u0004\b\u0001\u0010\u0007\"\u0004\b\u0002\u0010\b\"\u0004\b\u0003\u0010\t\"\u0004\b\u0004\u0010\u0005*\b\u0012\u0004\u0012\u0002H\u00060\u00042\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00070\u00042\f\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\b0\u00042\f\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\t0\u000426\b\u0004\u0010\u000f\u001a0\b\u0001\u0012\u0004\u0012\u0002H\u0006\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u0002H\b\u0012\u0004\u0012\u0002H\t\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00050\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0014H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0015\u001a\u0087\u0001\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0000\u0010\u0006\"\u0004\b\u0001\u0010\u0007\"\u0004\b\u0002\u0010\b\"\u0004\b\u0003\u0010\u0005*\b\u0012\u0004\u0012\u0002H\u00060\u00042\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00070\u00042\f\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\b0\u000420\b\u0004\u0010\u000f\u001a*\b\u0001\u0012\u0004\u0012\u0002H\u0006\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u0002H\b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00050\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0016H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0017\u001aj\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0000\u0010\u0006\"\u0004\b\u0001\u0010\u0007\"\u0004\b\u0002\u0010\u0005*\b\u0012\u0004\u0012\u0002H\u00060\u00042\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00070\u00042(\u0010\u000f\u001a$\b\u0001\u0012\u0004\u0012\u0002H\u0006\u0012\u0004\u0012\u0002H\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00050\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0018H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0019\u001aI\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0000\u0010\u001b\"\u0004\b\u0001\u0010\u0005*\b\u0012\u0004\u0012\u0002H\u001b0\u00042#\u0010\u001c\u001a\u001f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u001b0\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00050\u00040\u001d¢\u0006\u0002\b\u001eH\u0007\u001a>\u0010\u001f\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0000\u0010\u001b\"\u0004\b\u0001\u0010\u0005*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\u0018\u0010 \u001a\u0014\u0012\u0004\u0012\u0002H\u001b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00050\u00040\u001dH\u0007\u001a+\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\u0006\u0010\"\u001a\u0002H\u001bH\u0007¢\u0006\u0002\u0010#\u001a,\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004H\u0007\u001a&\u0010$\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\u0006\u0010%\u001a\u00020&H\u0007\u001a&\u0010'\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\u0006\u0010%\u001a\u00020&H\u0007\u001aV\u0010(\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0000\u0010\u001b\"\u0004\b\u0001\u0010\u0005*\b\u0012\u0004\u0012\u0002H\u001b0\u00042(\u0010 \u001a$\b\u0001\u0012\u0004\u0012\u0002H\u001b\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00050\u00040\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00120)H\u0007ø\u0001\u0000¢\u0006\u0002\u0010*\u001a$\u0010+\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004\"\u0004\b\u0000\u0010\u001b*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u001b0\u00040\u0004H\u0007\u001aS\u0010,\u001a\u00020-\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u000421\u0010.\u001a-\b\u0001\u0012\u0013\u0012\u0011H\u001b¢\u0006\f\b/\u0012\b\b0\u0012\u0004\b\b(\"\u0012\n\u0012\b\u0012\u0004\u0012\u00020-0\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00120)H\u0007ø\u0001\u0000¢\u0006\u0002\u00101\u001a$\u00102\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004\"\u0004\b\u0000\u0010\u001b*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u001b0\u00040\u0004H\u0007\u001a&\u00103\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\u0006\u00104\u001a\u000205H\u0007\u001a,\u00106\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\f\u00107\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004H\u0007\u001a,\u00108\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\f\u00107\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004H\u0007\u001a+\u00109\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\u0006\u00107\u001a\u0002H\u001bH\u0007¢\u0006\u0002\u0010#\u001aA\u00109\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\u0006\u00107\u001a\u0002H\u001b2\u0014\b\u0002\u0010:\u001a\u000e\u0012\u0004\u0012\u00020;\u0012\u0004\u0012\u00020<0\u001dH\u0007¢\u0006\u0002\u0010=\u001a&\u0010>\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\u0006\u00104\u001a\u000205H\u0007\u001a~\u0010?\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0000\u0010\u001b\"\u0004\b\u0001\u0010\u0005*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\u0006\u0010@\u001a\u0002H\u00052H\b\u0001\u0010A\u001aB\b\u0001\u0012\u0013\u0012\u0011H\u0005¢\u0006\f\b/\u0012\b\b0\u0012\u0004\b\b(B\u0012\u0013\u0012\u0011H\u001b¢\u0006\f\b/\u0012\b\b0\u0012\u0004\b\b(\"\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00050\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0018H\u0007ø\u0001\u0000¢\u0006\u0002\u0010C\u001a&\u0010D\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\u0006\u0010E\u001a\u00020FH\u0007\u001a+\u0010G\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\u0006\u0010\"\u001a\u0002H\u001bH\u0007¢\u0006\u0002\u0010#\u001a,\u0010G\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004H\u0007\u001a\u0018\u0010H\u001a\u00020-\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u0004H\u0007\u001aD\u0010H\u001a\u00020-\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\"\u0010I\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u001b\u0012\n\u0012\b\u0012\u0004\u0012\u00020-0\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00120)H\u0007ø\u0001\u0000¢\u0006\u0002\u00101\u001ah\u0010H\u001a\u00020-\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\"\u0010I\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u001b\u0012\n\u0012\b\u0012\u0004\u0012\u00020-0\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00120)2\"\u0010J\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020;\u0012\n\u0012\b\u0012\u0004\u0012\u00020-0\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00120)H\u0007ø\u0001\u0000¢\u0006\u0002\u0010K\u001a&\u0010L\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u0004\"\u0004\b\u0000\u0010\u001b*\b\u0012\u0004\u0012\u0002H\u001b0\u00042\u0006\u00104\u001a\u000205H\u0007\u001ae\u0010M\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0000\u0010\u001b\"\u0004\b\u0001\u0010\u0005*\b\u0012\u0004\u0012\u0002H\u001b0\u000427\u0010\u000f\u001a3\b\u0001\u0012\u0013\u0012\u0011H\u001b¢\u0006\f\b/\u0012\b\b0\u0012\u0004\b\b(\"\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00050\u00040\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00120)H\u0007ø\u0001\u0000¢\u0006\u0002\u0010*\u001aL\u0010N\u001a\u00020-\"\u0004\b\u0000\u0010\u001b\"\u0004\b\u0001\u0010\u0005*\b\u0012\u0004\u0012\u0002H\u001b0O2\u0006\u00104\u001a\u0002052\u001c\u0010P\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00050\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u001dH\u0007ø\u0001\u0000¢\u0006\u0002\u0010Q\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006R"}, d2 = {"noImpl", "", "noImpl$FlowKt__MigrationKt", "combineLatest", "Lkotlinx/coroutines/flow/Flow;", "R", "T1", "T2", "T3", "T4", "T5", "other", "other2", "other3", "other4", "transform", "Lkotlin/Function6;", "Lkotlin/coroutines/Continuation;", "", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function6;)Lkotlinx/coroutines/flow/Flow;", "Lkotlin/Function5;", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function5;)Lkotlinx/coroutines/flow/Flow;", "Lkotlin/Function4;", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function4;)Lkotlinx/coroutines/flow/Flow;", "Lkotlin/Function3;", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "compose", ExifInterface.GPS_DIRECTION_TRUE, "transformer", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "concatMap", "mapper", "concatWith", "value", "(Lkotlinx/coroutines/flow/Flow;Ljava/lang/Object;)Lkotlinx/coroutines/flow/Flow;", "delayEach", "timeMillis", "", "delayFlow", "flatMap", "Lkotlin/Function2;", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/Flow;", "flatten", "forEach", "", "action", "Lkotlin/ParameterName;", LogContract.SessionColumns.NAME, "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function2;)V", "merge", "observeOn", "context", "Lkotlin/coroutines/CoroutineContext;", "onErrorResume", "fallback", "onErrorResumeNext", "onErrorReturn", "predicate", "", "", "(Lkotlinx/coroutines/flow/Flow;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Lkotlinx/coroutines/flow/Flow;", "publishOn", "scanFold", "initial", "operation", "accumulator", "(Lkotlinx/coroutines/flow/Flow;Ljava/lang/Object;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "skip", "count", "", "startWith", "subscribe", "onEach", "onError", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function2;)V", "subscribeOn", "switchMap", "withContext", "Lkotlinx/coroutines/flow/FlowCollector;", "block", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function1;)V", "kotlinx-coroutines-core"}, k = 5, mv = {1, 1, 15}, xs = "kotlinx/coroutines/flow/FlowKt")
/* loaded from: classes.dex */
final /* synthetic */ class FlowKt__MigrationKt {
    private static final Void noImpl$FlowKt__MigrationKt() {
        throw new UnsupportedOperationException("Not implemented, should not be called");
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Collect flow in the desired context instead")
    public static final <T> Flow<T> observeOn(Flow<? extends T> observeOn, CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(observeOn, "$this$observeOn");
        Intrinsics.checkParameterIsNotNull(context, "context");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Collect flow in the desired context instead")
    public static final <T> Flow<T> publishOn(Flow<? extends T> publishOn, CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(publishOn, "$this$publishOn");
        Intrinsics.checkParameterIsNotNull(context, "context");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Use flowOn instead")
    public static final <T> Flow<T> subscribeOn(Flow<? extends T> subscribeOn, CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(subscribeOn, "$this$subscribeOn");
        Intrinsics.checkParameterIsNotNull(context, "context");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'onErrorXxx' is 'catch'. Use 'catch { emitAll(fallback) }'", replaceWith = @ReplaceWith(expression = "catch { emitAll(fallback) }", imports = {}))
    public static final <T> Flow<T> onErrorResume(Flow<? extends T> onErrorResume, Flow<? extends T> fallback) {
        Intrinsics.checkParameterIsNotNull(onErrorResume, "$this$onErrorResume");
        Intrinsics.checkParameterIsNotNull(fallback, "fallback");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'onErrorXxx' is 'catch'. Use 'catch { emitAll(fallback) }'", replaceWith = @ReplaceWith(expression = "catch { emitAll(fallback) }", imports = {}))
    public static final <T> Flow<T> onErrorResumeNext(Flow<? extends T> onErrorResumeNext, Flow<? extends T> fallback) {
        Intrinsics.checkParameterIsNotNull(onErrorResumeNext, "$this$onErrorResumeNext");
        Intrinsics.checkParameterIsNotNull(fallback, "fallback");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "withContext in flow body is deprecated, use flowOn instead")
    public static final <T, R> void withContext(FlowCollector<? super T> withContext, CoroutineContext context, Function1<? super Continuation<? super R>, ? extends Object> block) {
        Intrinsics.checkParameterIsNotNull(withContext, "$this$withContext");
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(block, "block");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Use launchIn with onEach, onCompletion and catch operators instead")
    public static final <T> void subscribe(Flow<? extends T> subscribe) {
        Intrinsics.checkParameterIsNotNull(subscribe, "$this$subscribe");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Use launchIn with onEach, onCompletion and catch operators instead")
    public static final <T> void subscribe(Flow<? extends T> subscribe, Function2<? super T, ? super Continuation<? super Unit>, ? extends Object> onEach) {
        Intrinsics.checkParameterIsNotNull(subscribe, "$this$subscribe");
        Intrinsics.checkParameterIsNotNull(onEach, "onEach");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Use launchIn with onEach, onCompletion and catch operators instead")
    public static final <T> void subscribe(Flow<? extends T> subscribe, Function2<? super T, ? super Continuation<? super Unit>, ? extends Object> onEach, Function2<? super Throwable, ? super Continuation<? super Unit>, ? extends Object> onError) {
        Intrinsics.checkParameterIsNotNull(subscribe, "$this$subscribe");
        Intrinsics.checkParameterIsNotNull(onEach, "onEach");
        Intrinsics.checkParameterIsNotNull(onError, "onError");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue is named flatMapConcat", replaceWith = @ReplaceWith(expression = "flatMapConcat(mapper)", imports = {}))
    public static final <T, R> Flow<R> flatMap(Flow<? extends T> flatMap, Function2<? super T, ? super Continuation<? super Flow<? extends R>>, ? extends Object> mapper) {
        Intrinsics.checkParameterIsNotNull(flatMap, "$this$flatMap");
        Intrinsics.checkParameterIsNotNull(mapper, "mapper");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'concatMap' is 'flatMapConcat'", replaceWith = @ReplaceWith(expression = "flatMapConcat(mapper)", imports = {}))
    public static final <T, R> Flow<R> concatMap(Flow<? extends T> concatMap, Function1<? super T, ? extends Flow<? extends R>> mapper) {
        Intrinsics.checkParameterIsNotNull(concatMap, "$this$concatMap");
        Intrinsics.checkParameterIsNotNull(mapper, "mapper");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'merge' is 'flattenConcat'", replaceWith = @ReplaceWith(expression = "flattenConcat()", imports = {}))
    public static final <T> Flow<T> merge(Flow<? extends Flow<? extends T>> merge) {
        Intrinsics.checkParameterIsNotNull(merge, "$this$merge");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'flatten' is 'flattenConcat'", replaceWith = @ReplaceWith(expression = "flattenConcat()", imports = {}))
    public static final <T> Flow<T> flatten(Flow<? extends Flow<? extends T>> flatten) {
        Intrinsics.checkParameterIsNotNull(flatten, "$this$flatten");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'compose' is 'let'", replaceWith = @ReplaceWith(expression = "let(transformer)", imports = {}))
    public static final <T, R> Flow<R> compose(Flow<? extends T> compose, Function1<? super Flow<? extends T>, ? extends Flow<? extends R>> transformer) {
        Intrinsics.checkParameterIsNotNull(compose, "$this$compose");
        Intrinsics.checkParameterIsNotNull(transformer, "transformer");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'skip' is 'drop'", replaceWith = @ReplaceWith(expression = "drop(count)", imports = {}))
    public static final <T> Flow<T> skip(Flow<? extends T> skip, int i) {
        Intrinsics.checkParameterIsNotNull(skip, "$this$skip");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'forEach' is 'collect'", replaceWith = @ReplaceWith(expression = "collect(block)", imports = {}))
    public static final <T> void forEach(Flow<? extends T> forEach, Function2<? super T, ? super Continuation<? super Unit>, ? extends Object> action) {
        Intrinsics.checkParameterIsNotNull(forEach, "$this$forEach");
        Intrinsics.checkParameterIsNotNull(action, "action");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow has less verbose 'scan' shortcut", replaceWith = @ReplaceWith(expression = "scan(initial, operation)", imports = {}))
    public static final <T, R> Flow<R> scanFold(Flow<? extends T> scanFold, R r, Function3<? super R, ? super T, ? super Continuation<? super R>, ? extends Object> operation) {
        Intrinsics.checkParameterIsNotNull(scanFold, "$this$scanFold");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'onErrorXxx' is 'catch'. Use 'catch { emit(fallback) }'", replaceWith = @ReplaceWith(expression = "catch { emit(fallback) }", imports = {}))
    public static final <T> Flow<T> onErrorReturn(Flow<? extends T> onErrorReturn, T t) {
        Intrinsics.checkParameterIsNotNull(onErrorReturn, "$this$onErrorReturn");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* compiled from: Migration.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u008a@¢\u0006\u0004\b\u0006\u0010\u0007"}, d2 = {"<anonymous>", "", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlinx/coroutines/flow/FlowCollector;", "e", "", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
    @DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__MigrationKt$onErrorReturn$2", f = "Migration.kt", i = {0, 0}, l = {316}, m = "invokeSuspend", n = {"$this$catch", "e"}, s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__MigrationKt$onErrorReturn$2, reason: invalid class name */
    static final class AnonymousClass2<T> extends SuspendLambda implements Function3<FlowCollector<? super T>, Throwable, Continuation<? super Unit>, Object> {
        final /* synthetic */ Object $fallback;
        final /* synthetic */ Function1 $predicate;
        Object L$0;
        Object L$1;
        int label;
        private FlowCollector p$;
        private Throwable p$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Function1 function1, Object obj, Continuation continuation) {
            super(3, continuation);
            this.$predicate = function1;
            this.$fallback = obj;
        }

        public final Continuation<Unit> create(FlowCollector<? super T> create, Throwable e, Continuation<? super Unit> continuation) {
            Intrinsics.checkParameterIsNotNull(create, "$this$create");
            Intrinsics.checkParameterIsNotNull(e, "e");
            Intrinsics.checkParameterIsNotNull(continuation, "continuation");
            AnonymousClass2 anonymousClass2 = new AnonymousClass2(this.$predicate, this.$fallback, continuation);
            anonymousClass2.p$ = create;
            anonymousClass2.p$0 = e;
            return anonymousClass2;
        }

        @Override // kotlin.jvm.functions.Function3
        public final Object invoke(Object obj, Throwable th, Continuation<? super Unit> continuation) {
            return ((AnonymousClass2) create((FlowCollector) obj, th, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) throws Throwable {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                FlowCollector flowCollector = this.p$;
                Throwable th = this.p$0;
                if (!((Boolean) this.$predicate.invoke(th)).booleanValue()) {
                    throw th;
                }
                Object obj2 = this.$fallback;
                this.L$0 = flowCollector;
                this.L$1 = th;
                this.label = 1;
                if (flowCollector.emit(obj2, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
            }
            return Unit.INSTANCE;
        }
    }

    public static /* synthetic */ Flow onErrorReturn$default(Flow flow, Object obj, Function1 function1, int i, Object obj2) {
        if ((i & 2) != 0) {
            function1 = new Function1<Throwable, Boolean>() { // from class: kotlinx.coroutines.flow.FlowKt__MigrationKt.onErrorReturn.1
                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Boolean invoke(Throwable th) {
                    return Boolean.valueOf(invoke2(th));
                }

                /* renamed from: invoke, reason: avoid collision after fix types in other method */
                public final boolean invoke2(Throwable it) {
                    Intrinsics.checkParameterIsNotNull(it, "it");
                    return true;
                }
            };
        }
        return FlowKt.onErrorReturn(flow, obj, function1);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'onErrorXxx' is 'catch'. Use 'catch { e -> if (predicate(e)) emit(fallback) else throw e }'", replaceWith = @ReplaceWith(expression = "catch { e -> if (predicate(e)) emit(fallback) else throw e }", imports = {}))
    public static final <T> Flow<T> onErrorReturn(Flow<? extends T> onErrorReturn, T t, Function1<? super Throwable, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(onErrorReturn, "$this$onErrorReturn");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        return FlowKt.m1035catch(onErrorReturn, new AnonymousClass2(predicate, t, null));
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'startWith' is 'onStart'. Use 'onStart { emit(value) }'", replaceWith = @ReplaceWith(expression = "onStart { emit(value) }", imports = {}))
    public static final <T> Flow<T> startWith(Flow<? extends T> startWith, T t) {
        Intrinsics.checkParameterIsNotNull(startWith, "$this$startWith");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'startWith' is 'onStart'. Use 'onStart { emitAll(other) }'", replaceWith = @ReplaceWith(expression = "onStart { emitAll(other) }", imports = {}))
    public static final <T> Flow<T> startWith(Flow<? extends T> startWith, Flow<? extends T> other) {
        Intrinsics.checkParameterIsNotNull(startWith, "$this$startWith");
        Intrinsics.checkParameterIsNotNull(other, "other");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'concatWith' is 'onCompletion'. Use 'onCompletion { emit(value) }'", replaceWith = @ReplaceWith(expression = "onCompletion { emit(value) }", imports = {}))
    public static final <T> Flow<T> concatWith(Flow<? extends T> concatWith, T t) {
        Intrinsics.checkParameterIsNotNull(concatWith, "$this$concatWith");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'concatWith' is 'onCompletion'. Use 'onCompletion { emitAll(other) }'", replaceWith = @ReplaceWith(expression = "onCompletion { emitAll(other) }", imports = {}))
    public static final <T> Flow<T> concatWith(Flow<? extends T> concatWith, Flow<? extends T> other) {
        Intrinsics.checkParameterIsNotNull(concatWith, "$this$concatWith");
        Intrinsics.checkParameterIsNotNull(other, "other");
        noImpl$FlowKt__MigrationKt();
        throw null;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'combineLatest' is 'combine'", replaceWith = @ReplaceWith(expression = "this.combine(other, transform)", imports = {}))
    public static final <T1, T2, R> Flow<R> combineLatest(Flow<? extends T1> combineLatest, Flow<? extends T2> other, Function3<? super T1, ? super T2, ? super Continuation<? super R>, ? extends Object> transform) {
        Intrinsics.checkParameterIsNotNull(combineLatest, "$this$combineLatest");
        Intrinsics.checkParameterIsNotNull(other, "other");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        return FlowKt.combine(combineLatest, other, transform);
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* compiled from: Migration.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlinx/coroutines/flow/FlowCollector;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
    @DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__MigrationKt$delayFlow$1", f = "Migration.kt", i = {0}, l = {421}, m = "invokeSuspend", n = {"$this$onStart"}, s = {"L$0"})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__MigrationKt$delayFlow$1, reason: invalid class name and case insensitive filesystem */
    static final class C01891<T> extends SuspendLambda implements Function2<FlowCollector<? super T>, Continuation<? super Unit>, Object> {
        final /* synthetic */ long $timeMillis;
        Object L$0;
        int label;
        private FlowCollector p$;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C01891(long j, Continuation continuation) {
            super(2, continuation);
            this.$timeMillis = j;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> completion) {
            Intrinsics.checkParameterIsNotNull(completion, "completion");
            C01891 c01891 = new C01891(this.$timeMillis, completion);
            c01891.p$ = (FlowCollector) obj;
            return c01891;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
            return ((C01891) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                FlowCollector flowCollector = this.p$;
                long j = this.$timeMillis;
                this.L$0 = flowCollector;
                this.label = 1;
                if (DelayKt.delay(j, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
            }
            return Unit.INSTANCE;
        }
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use 'onStart { delay(timeMillis) }'", replaceWith = @ReplaceWith(expression = "onStart { delay(timeMillis) }", imports = {}))
    public static final <T> Flow<T> delayFlow(Flow<? extends T> delayFlow, long j) {
        Intrinsics.checkParameterIsNotNull(delayFlow, "$this$delayFlow");
        return FlowKt.onStart(delayFlow, new C01891(j, null));
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* compiled from: Migration.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002H\u008a@¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", ExifInterface.GPS_DIRECTION_TRUE, ConstantLanguages.ITALIAN, "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
    @DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__MigrationKt$delayEach$1", f = "Migration.kt", i = {0}, l = {433}, m = "invokeSuspend", n = {ConstantLanguages.ITALIAN}, s = {"L$0"})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__MigrationKt$delayEach$1, reason: invalid class name */
    static final class AnonymousClass1<T> extends SuspendLambda implements Function2<T, Continuation<? super Unit>, Object> {
        final /* synthetic */ long $timeMillis;
        Object L$0;
        int label;
        private Object p$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(long j, Continuation continuation) {
            super(2, continuation);
            this.$timeMillis = j;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> completion) {
            Intrinsics.checkParameterIsNotNull(completion, "completion");
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(this.$timeMillis, completion);
            anonymousClass1.p$0 = obj;
            return anonymousClass1;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                Object obj2 = this.p$0;
                long j = this.$timeMillis;
                this.L$0 = obj2;
                this.label = 1;
                if (DelayKt.delay(j, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
            }
            return Unit.INSTANCE;
        }
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use 'onEach { delay(timeMillis) }'", replaceWith = @ReplaceWith(expression = "onEach { delay(timeMillis) }", imports = {}))
    public static final <T> Flow<T> delayEach(Flow<? extends T> delayEach, long j) {
        Intrinsics.checkParameterIsNotNull(delayEach, "$this$delayEach");
        return FlowKt.onEach(delayEach, new AnonymousClass1(j, null));
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'combineLatest' is 'combine'", replaceWith = @ReplaceWith(expression = "combine(this, other, other2, transform)", imports = {}))
    public static final <T1, T2, T3, R> Flow<R> combineLatest(Flow<? extends T1> combineLatest, Flow<? extends T2> other, Flow<? extends T3> other2, Function4<? super T1, ? super T2, ? super T3, ? super Continuation<? super R>, ? extends Object> transform) {
        Intrinsics.checkParameterIsNotNull(combineLatest, "$this$combineLatest");
        Intrinsics.checkParameterIsNotNull(other, "other");
        Intrinsics.checkParameterIsNotNull(other2, "other2");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        return new FlowKt__MigrationKt$combineLatest$$inlined$combine$1(new Flow[]{combineLatest, other, other2}, transform);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'combineLatest' is 'combine'", replaceWith = @ReplaceWith(expression = "combine(this, other, other2, other3, transform)", imports = {}))
    public static final <T1, T2, T3, T4, R> Flow<R> combineLatest(Flow<? extends T1> combineLatest, Flow<? extends T2> other, Flow<? extends T3> other2, Flow<? extends T4> other3, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super Continuation<? super R>, ? extends Object> transform) {
        Intrinsics.checkParameterIsNotNull(combineLatest, "$this$combineLatest");
        Intrinsics.checkParameterIsNotNull(other, "other");
        Intrinsics.checkParameterIsNotNull(other2, "other2");
        Intrinsics.checkParameterIsNotNull(other3, "other3");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        return new FlowKt__MigrationKt$combineLatest$$inlined$combine$2(new Flow[]{combineLatest, other, other2, other3}, transform);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue of 'combineLatest' is 'combine'", replaceWith = @ReplaceWith(expression = "combine(this, other, other2, other3, transform)", imports = {}))
    public static final <T1, T2, T3, T4, T5, R> Flow<R> combineLatest(Flow<? extends T1> combineLatest, Flow<? extends T2> other, Flow<? extends T3> other2, Flow<? extends T4> other3, Flow<? extends T5> other4, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super Continuation<? super R>, ? extends Object> transform) {
        Intrinsics.checkParameterIsNotNull(combineLatest, "$this$combineLatest");
        Intrinsics.checkParameterIsNotNull(other, "other");
        Intrinsics.checkParameterIsNotNull(other2, "other2");
        Intrinsics.checkParameterIsNotNull(other3, "other3");
        Intrinsics.checkParameterIsNotNull(other4, "other4");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        return new FlowKt__MigrationKt$combineLatest$$inlined$combine$3(new Flow[]{combineLatest, other, other2, other3, other4}, transform);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogues of 'switchMap' are 'transformLatest', 'flatMapLatest' and 'mapLatest'", replaceWith = @ReplaceWith(expression = "this.flatMapLatest(transform)", imports = {}))
    public static final <T, R> Flow<R> switchMap(Flow<? extends T> switchMap, Function2<? super T, ? super Continuation<? super Flow<? extends R>>, ? extends Object> transform) {
        Intrinsics.checkParameterIsNotNull(switchMap, "$this$switchMap");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        return FlowKt.transformLatest(switchMap, new FlowKt__MigrationKt$switchMap$$inlined$flatMapLatest$1(transform, null));
    }
}
