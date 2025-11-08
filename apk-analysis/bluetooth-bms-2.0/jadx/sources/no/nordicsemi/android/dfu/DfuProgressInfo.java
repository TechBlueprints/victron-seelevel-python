package no.nordicsemi.android.dfu;

import android.os.SystemClock;

/* loaded from: classes.dex */
class DfuProgressInfo {
    private int bytesReceived;
    private int bytesSent;
    private int currentPart;
    private int imageSizeInBytes;
    private int initialBytesSent;
    private int lastBytesSent;
    private long lastProgressTime;
    private final ProgressListener mListener;
    private int maxObjectSizeInBytes;
    private int progress;
    private long timeStart;
    private int totalParts;

    interface ProgressListener {
        void updateProgressNotification();
    }

    DfuProgressInfo(ProgressListener progressListener) {
        this.mListener = progressListener;
    }

    DfuProgressInfo init(int i, int i2, int i3) {
        this.imageSizeInBytes = i;
        this.maxObjectSizeInBytes = Integer.MAX_VALUE;
        this.currentPart = i2;
        this.totalParts = i3;
        return this;
    }

    DfuProgressInfo setTotalPart(int i) {
        this.totalParts = i;
        return this;
    }

    public void setProgress(int i) {
        this.progress = i;
        this.mListener.updateProgressNotification();
    }

    void setBytesSent(int i) {
        if (this.timeStart == 0) {
            this.timeStart = SystemClock.elapsedRealtime();
            this.initialBytesSent = i;
        }
        this.bytesSent = i;
        this.progress = (int) ((i * 100.0f) / this.imageSizeInBytes);
        this.mListener.updateProgressNotification();
    }

    void addBytesSent(int i) {
        setBytesSent(this.bytesSent + i);
    }

    void setBytesReceived(int i) {
        this.bytesReceived = i;
    }

    void setMaxObjectSizeInBytes(int i) {
        this.maxObjectSizeInBytes = i;
    }

    boolean isComplete() {
        return this.bytesSent == this.imageSizeInBytes;
    }

    boolean isObjectComplete() {
        return this.bytesSent % this.maxObjectSizeInBytes == 0;
    }

    int getAvailableObjectSizeIsBytes() {
        int i = this.imageSizeInBytes;
        int i2 = this.bytesSent;
        int i3 = this.maxObjectSizeInBytes;
        return Math.min(i - i2, i3 - (i2 % i3));
    }

    int getProgress() {
        return this.progress;
    }

    int getBytesSent() {
        return this.bytesSent;
    }

    int getBytesReceived() {
        return this.bytesReceived;
    }

    int getImageSizeInBytes() {
        return this.imageSizeInBytes;
    }

    float getSpeed() {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        float f = jElapsedRealtime - this.timeStart != 0 ? (this.bytesSent - this.lastBytesSent) / (jElapsedRealtime - this.lastProgressTime) : 0.0f;
        this.lastProgressTime = jElapsedRealtime;
        this.lastBytesSent = this.bytesSent;
        return f;
    }

    float getAverageSpeed() {
        if (SystemClock.elapsedRealtime() - this.timeStart != 0) {
            return (this.bytesSent - this.initialBytesSent) / (r0 - r2);
        }
        return 0.0f;
    }

    int getCurrentPart() {
        return this.currentPart;
    }

    int getTotalParts() {
        return this.totalParts;
    }

    boolean isLastPart() {
        return this.currentPart == this.totalParts;
    }
}
