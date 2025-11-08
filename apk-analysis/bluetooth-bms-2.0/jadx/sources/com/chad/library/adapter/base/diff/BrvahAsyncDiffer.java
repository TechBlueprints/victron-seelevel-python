package com.chad.library.adapter.base.diff;

import android.os.Handler;
import android.os.Looper;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import com.chad.library.adapter.base.BaseQuickAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: BrvahAsyncDiffer.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0004\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0001!B%\u0012\u0010\u0010\u0003\u001a\f\u0012\u0004\u0012\u00028\u0000\u0012\u0002\b\u00030\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006¢\u0006\u0002\u0010\u0007J\u0016\u0010\u0012\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00028\u00000\nH\u0016J\u0006\u0010\u0015\u001a\u00020\u0013J(\u0010\u0016\u001a\u00020\u00132\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\t2\u0006\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0002J \u0010\u001c\u001a\u00020\u00132\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00028\u00000\u001e2\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0002J\u0014\u0010\u001f\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00028\u00000\nJ$\u0010 \u001a\u00020\u00132\u000e\u0010\u0017\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\t2\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0007R\u0018\u0010\u0003\u001a\f\u0012\u0004\u0012\u00028\u0000\u0012\u0002\b\u00030\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\n0\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\""}, d2 = {"Lcom/chad/library/adapter/base/diff/BrvahAsyncDiffer;", ExifInterface.GPS_DIRECTION_TRUE, "Lcom/chad/library/adapter/base/diff/DifferImp;", "adapter", "Lcom/chad/library/adapter/base/BaseQuickAdapter;", "config", "Lcom/chad/library/adapter/base/diff/BrvahAsyncDifferConfig;", "(Lcom/chad/library/adapter/base/BaseQuickAdapter;Lcom/chad/library/adapter/base/diff/BrvahAsyncDifferConfig;)V", "mListeners", "", "Lcom/chad/library/adapter/base/diff/ListChangeListener;", "mMainThreadExecutor", "Ljava/util/concurrent/Executor;", "mMaxScheduledGeneration", "", "mUpdateCallback", "Landroidx/recyclerview/widget/ListUpdateCallback;", "sMainThreadExecutor", "addListListener", "", "listener", "clearAllListListener", "latchList", "newList", "diffResult", "Landroidx/recyclerview/widget/DiffUtil$DiffResult;", "commitCallback", "Ljava/lang/Runnable;", "onCurrentListChanged", "previousList", "", "removeListListener", "submitList", "MainThreadExecutor", "com.github.CymChad.brvah"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class BrvahAsyncDiffer<T> implements DifferImp<T> {
    private final BaseQuickAdapter<T, ?> adapter;
    private final BrvahAsyncDifferConfig<T> config;
    private final List<ListChangeListener<T>> mListeners;
    private Executor mMainThreadExecutor;
    private int mMaxScheduledGeneration;
    private final ListUpdateCallback mUpdateCallback;
    private final Executor sMainThreadExecutor;

    public final void submitList(List<T> list) {
        submitList$default(this, list, null, 2, null);
    }

    public BrvahAsyncDiffer(BaseQuickAdapter<T, ?> adapter, BrvahAsyncDifferConfig<T> config) {
        Intrinsics.checkParameterIsNotNull(adapter, "adapter");
        Intrinsics.checkParameterIsNotNull(config, "config");
        this.adapter = adapter;
        this.config = config;
        this.mUpdateCallback = new BrvahListUpdateCallback(adapter);
        this.sMainThreadExecutor = new MainThreadExecutor();
        Executor mainThreadExecutor = this.config.getMainThreadExecutor();
        this.mMainThreadExecutor = mainThreadExecutor == null ? this.sMainThreadExecutor : mainThreadExecutor;
        this.mListeners = new CopyOnWriteArrayList();
    }

    /* compiled from: BrvahAsyncDiffer.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0000¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u000b"}, d2 = {"Lcom/chad/library/adapter/base/diff/BrvahAsyncDiffer$MainThreadExecutor;", "Ljava/util/concurrent/Executor;", "()V", "mHandler", "Landroid/os/Handler;", "getMHandler", "()Landroid/os/Handler;", "execute", "", "command", "Ljava/lang/Runnable;", "com.github.CymChad.brvah"}, k = 1, mv = {1, 1, 16})
    private static final class MainThreadExecutor implements Executor {
        private final Handler mHandler = new Handler(Looper.getMainLooper());

        public final Handler getMHandler() {
            return this.mHandler;
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable command) {
            Intrinsics.checkParameterIsNotNull(command, "command");
            this.mHandler.post(command);
        }
    }

    public static /* synthetic */ void submitList$default(BrvahAsyncDiffer brvahAsyncDiffer, List list, Runnable runnable, int i, Object obj) {
        if ((i & 2) != 0) {
            runnable = (Runnable) null;
        }
        brvahAsyncDiffer.submitList(list, runnable);
    }

    public final void submitList(List<T> newList, Runnable commitCallback) {
        int i = this.mMaxScheduledGeneration + 1;
        this.mMaxScheduledGeneration = i;
        if (newList == this.adapter.getData()) {
            if (commitCallback != null) {
                commitCallback.run();
                return;
            }
            return;
        }
        List<? extends T> data = this.adapter.getData();
        if (newList == null) {
            int size = this.adapter.getData().size();
            this.adapter.setData$com_github_CymChad_brvah(new ArrayList());
            this.mUpdateCallback.onRemoved(0, size);
            onCurrentListChanged(data, commitCallback);
            return;
        }
        if (this.adapter.getData().isEmpty()) {
            this.adapter.setData$com_github_CymChad_brvah(newList);
            this.mUpdateCallback.onInserted(0, newList.size());
            onCurrentListChanged(data, commitCallback);
            return;
        }
        this.config.getBackgroundThreadExecutor().execute(new AnonymousClass1(data, newList, i, commitCallback));
    }

    /* compiled from: BrvahAsyncDiffer.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002H\n¢\u0006\u0002\b\u0003"}, d2 = {"<anonymous>", "", ExifInterface.GPS_DIRECTION_TRUE, "run"}, k = 3, mv = {1, 1, 16})
    /* renamed from: com.chad.library.adapter.base.diff.BrvahAsyncDiffer$submitList$1, reason: invalid class name */
    static final class AnonymousClass1 implements Runnable {
        final /* synthetic */ Runnable $commitCallback;
        final /* synthetic */ List $newList;
        final /* synthetic */ List $oldList;
        final /* synthetic */ int $runGeneration;

        AnonymousClass1(List list, List list2, int i, Runnable runnable) {
            this.$oldList = list;
            this.$newList = list2;
            this.$runGeneration = i;
            this.$commitCallback = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            final DiffUtil.DiffResult diffResultCalculateDiff = DiffUtil.calculateDiff(new DiffUtil.Callback() { // from class: com.chad.library.adapter.base.diff.BrvahAsyncDiffer$submitList$1$result$1
                @Override // androidx.recyclerview.widget.DiffUtil.Callback
                public int getOldListSize() {
                    return this.this$0.$oldList.size();
                }

                @Override // androidx.recyclerview.widget.DiffUtil.Callback
                public int getNewListSize() {
                    return this.this$0.$newList.size();
                }

                @Override // androidx.recyclerview.widget.DiffUtil.Callback
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    Object obj = this.this$0.$oldList.get(oldItemPosition);
                    Object obj2 = this.this$0.$newList.get(newItemPosition);
                    if (obj == null || obj2 == null) {
                        return obj == null && obj2 == null;
                    }
                    return BrvahAsyncDiffer.this.config.getDiffCallback().areItemsTheSame(obj, obj2);
                }

                @Override // androidx.recyclerview.widget.DiffUtil.Callback
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Object obj = this.this$0.$oldList.get(oldItemPosition);
                    Object obj2 = this.this$0.$newList.get(newItemPosition);
                    if (obj != null && obj2 != null) {
                        return BrvahAsyncDiffer.this.config.getDiffCallback().areContentsTheSame(obj, obj2);
                    }
                    if (obj == null && obj2 == null) {
                        return true;
                    }
                    throw new AssertionError();
                }

                @Override // androidx.recyclerview.widget.DiffUtil.Callback
                public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                    Object obj = this.this$0.$oldList.get(oldItemPosition);
                    Object obj2 = this.this$0.$newList.get(newItemPosition);
                    if (obj != null && obj2 != null) {
                        return BrvahAsyncDiffer.this.config.getDiffCallback().getChangePayload(obj, obj2);
                    }
                    throw new AssertionError();
                }
            });
            Intrinsics.checkExpressionValueIsNotNull(diffResultCalculateDiff, "DiffUtil.calculateDiff(o…         }\n            })");
            BrvahAsyncDiffer.this.mMainThreadExecutor.execute(new Runnable() { // from class: com.chad.library.adapter.base.diff.BrvahAsyncDiffer.submitList.1.1
                @Override // java.lang.Runnable
                public final void run() {
                    if (BrvahAsyncDiffer.this.mMaxScheduledGeneration == AnonymousClass1.this.$runGeneration) {
                        BrvahAsyncDiffer.this.latchList(AnonymousClass1.this.$newList, diffResultCalculateDiff, AnonymousClass1.this.$commitCallback);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void latchList(List<T> newList, DiffUtil.DiffResult diffResult, Runnable commitCallback) {
        List<? extends T> data = this.adapter.getData();
        this.adapter.setData$com_github_CymChad_brvah(newList);
        diffResult.dispatchUpdatesTo(this.mUpdateCallback);
        onCurrentListChanged(data, commitCallback);
    }

    private final void onCurrentListChanged(List<? extends T> previousList, Runnable commitCallback) {
        Iterator<ListChangeListener<T>> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onCurrentListChanged(previousList, this.adapter.getData());
        }
        if (commitCallback != null) {
            commitCallback.run();
        }
    }

    @Override // com.chad.library.adapter.base.diff.DifferImp
    public void addListListener(ListChangeListener<T> listener) {
        Intrinsics.checkParameterIsNotNull(listener, "listener");
        this.mListeners.add(listener);
    }

    public final void removeListListener(ListChangeListener<T> listener) {
        Intrinsics.checkParameterIsNotNull(listener, "listener");
        this.mListeners.remove(listener);
    }

    public final void clearAllListListener() {
        this.mListeners.clear();
    }
}
