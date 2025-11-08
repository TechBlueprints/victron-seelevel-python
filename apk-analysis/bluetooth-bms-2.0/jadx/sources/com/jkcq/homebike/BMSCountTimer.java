package com.jkcq.homebike;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* loaded from: classes.dex */
public class BMSCountTimer {
    private int HANDLER_COUNT_TIMER = 0;
    private long delayedTime;
    private long intervalTime;
    private Handler mHandler;
    private long millisecond;

    public interface OnCountTimerListener {
        void onCountTimerChanged(long j);
    }

    public BMSCountTimer(long j, OnCountTimerListener onCountTimerListener) {
        init(-1L, j, onCountTimerListener);
    }

    public BMSCountTimer(long j, long j2, OnCountTimerListener onCountTimerListener) {
        init(j, j2, onCountTimerListener);
    }

    private void init(long j, final long j2, final OnCountTimerListener onCountTimerListener) {
        this.delayedTime = j;
        this.intervalTime = j2;
        this.mHandler = new Handler(Looper.getMainLooper()) { // from class: com.jkcq.homebike.BMSCountTimer.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                BMSCountTimer.this.millisecond += j2;
                BMSCountTimer.this.mHandler.removeMessages(BMSCountTimer.this.HANDLER_COUNT_TIMER);
                BMSCountTimer.this.mHandler.sendEmptyMessageDelayed(BMSCountTimer.this.HANDLER_COUNT_TIMER, BMSCountTimer.this.intervalTime);
                if (onCountTimerListener != null) {
                    if (BMSCountTimer.this.delayedTime != -1) {
                        onCountTimerListener.onCountTimerChanged(BMSCountTimer.this.delayedTime + BMSCountTimer.this.millisecond);
                    } else {
                        onCountTimerListener.onCountTimerChanged(BMSCountTimer.this.millisecond);
                    }
                }
            }
        };
    }

    public void reStart(long j) {
        this.millisecond = j;
        this.mHandler.removeMessages(this.HANDLER_COUNT_TIMER);
        long j2 = this.delayedTime;
        if (j2 == -1) {
            this.mHandler.sendEmptyMessage(this.HANDLER_COUNT_TIMER);
        } else {
            this.mHandler.sendEmptyMessageDelayed(this.HANDLER_COUNT_TIMER, j2);
        }
    }

    public void start() {
        this.millisecond = 0L;
        this.mHandler.removeMessages(this.HANDLER_COUNT_TIMER);
        long j = this.delayedTime;
        if (j == -1) {
            this.mHandler.sendEmptyMessage(this.HANDLER_COUNT_TIMER);
        } else {
            this.mHandler.sendEmptyMessageDelayed(this.HANDLER_COUNT_TIMER, j);
        }
    }

    public void stop() {
        this.mHandler.removeMessages(this.HANDLER_COUNT_TIMER);
    }
}
