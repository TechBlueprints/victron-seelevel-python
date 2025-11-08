package com.jkcq.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.MotionEvent;

/* loaded from: classes.dex */
public class BaseDialog extends Dialog {
    long lastClickTime;

    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int i) {
        super(context, i);
    }

    protected BaseDialog(Context context, boolean z, DialogInterface.OnCancelListener onCancelListener) {
        super(context, z, onCancelListener);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            Log.i("dialogTouch", "dispatchTouchEvent dialogTouch");
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.app.Dialog
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0 && isFastDoubleClick()) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    public boolean isFastDoubleClick() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = jCurrentTimeMillis - this.lastClickTime;
        this.lastClickTime = jCurrentTimeMillis;
        return j <= 1000;
    }
}
