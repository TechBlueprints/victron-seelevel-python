package kotlinx.coroutines.scheduling;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.LockFreeTaskQueue;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;

/* compiled from: Tasks.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0010\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0005\u001a\u00020\u0006¨\u0006\u0007"}, d2 = {"Lkotlinx/coroutines/scheduling/GlobalQueue;", "Lkotlinx/coroutines/internal/LockFreeTaskQueue;", "Lkotlinx/coroutines/scheduling/Task;", "()V", "removeFirstWithModeOrNull", "mode", "Lkotlinx/coroutines/scheduling/TaskMode;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes.dex */
public class GlobalQueue extends LockFreeTaskQueue<Task> {
    public GlobalQueue() {
        super(false);
    }

    public final Task removeFirstWithModeOrNull(TaskMode mode) {
        Object obj;
        Object obj2;
        Intrinsics.checkParameterIsNotNull(mode, "mode");
        while (true) {
            LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) this._cur$internal;
            while (true) {
                long j = lockFreeTaskQueueCore._state$internal;
                obj = null;
                if ((LockFreeTaskQueueCore.FROZEN_MASK & j) != 0) {
                    obj = LockFreeTaskQueueCore.REMOVE_FROZEN;
                    break;
                }
                LockFreeTaskQueueCore.Companion companion = LockFreeTaskQueueCore.INSTANCE;
                int i = (int) ((LockFreeTaskQueueCore.HEAD_MASK & j) >> 0);
                if ((lockFreeTaskQueueCore.mask & ((int) ((LockFreeTaskQueueCore.TAIL_MASK & j) >> 30))) == (lockFreeTaskQueueCore.mask & i)) {
                    break;
                }
                obj2 = lockFreeTaskQueueCore.array$internal.get(lockFreeTaskQueueCore.mask & i);
                if (obj2 == null) {
                    if (lockFreeTaskQueueCore.singleConsumer) {
                        break;
                    }
                } else {
                    if (obj2 instanceof LockFreeTaskQueueCore.Placeholder) {
                        break;
                    }
                    if (!(((Task) obj2).getMode() == mode)) {
                        break;
                    }
                    int i2 = (i + 1) & LockFreeTaskQueueCore.MAX_CAPACITY_MASK;
                    if (LockFreeTaskQueueCore._state$FU$internal.compareAndSet(lockFreeTaskQueueCore, j, LockFreeTaskQueueCore.INSTANCE.updateHead(j, i2))) {
                        lockFreeTaskQueueCore.array$internal.set(lockFreeTaskQueueCore.mask & i, null);
                        break;
                    }
                    if (lockFreeTaskQueueCore.singleConsumer) {
                        LockFreeTaskQueueCore lockFreeTaskQueueCoreRemoveSlowPath = lockFreeTaskQueueCore;
                        do {
                            lockFreeTaskQueueCoreRemoveSlowPath = lockFreeTaskQueueCoreRemoveSlowPath.removeSlowPath(i, i2);
                        } while (lockFreeTaskQueueCoreRemoveSlowPath != null);
                    }
                }
            }
            obj = obj2;
            if (obj != LockFreeTaskQueueCore.REMOVE_FROZEN) {
                return (Task) obj;
            }
            LockFreeTaskQueue._cur$FU$internal.compareAndSet(this, lockFreeTaskQueueCore, lockFreeTaskQueueCore.next());
        }
    }
}
