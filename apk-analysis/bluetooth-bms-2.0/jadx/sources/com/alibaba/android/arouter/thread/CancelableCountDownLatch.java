package com.alibaba.android.arouter.thread;

import java.util.concurrent.CountDownLatch;

/* loaded from: classes.dex */
public class CancelableCountDownLatch extends CountDownLatch {
    public CancelableCountDownLatch(int i) {
        super(i);
    }

    public void cancel() {
        while (getCount() > 0) {
            countDown();
        }
    }
}
