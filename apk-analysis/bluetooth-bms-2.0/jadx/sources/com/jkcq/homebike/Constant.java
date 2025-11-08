package com.jkcq.homebike;

import android.content.Context;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Constant.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"Lcom/jkcq/homebike/Constant;", "", "()V", "mBMSContext", "Landroid/content/Context;", "getMBMSContext", "()Landroid/content/Context;", "setMBMSContext", "(Landroid/content/Context;)V", "app_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class Constant {
    public static final Constant INSTANCE = new Constant();
    public static Context mBMSContext;

    private Constant() {
    }

    public final Context getMBMSContext() {
        Context context = mBMSContext;
        if (context == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mBMSContext");
        }
        return context;
    }

    public final void setMBMSContext(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "<set-?>");
        mBMSContext = context;
    }
}
