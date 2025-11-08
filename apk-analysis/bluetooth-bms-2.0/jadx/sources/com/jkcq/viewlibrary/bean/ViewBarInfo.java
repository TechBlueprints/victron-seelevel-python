package com.jkcq.viewlibrary.bean;

/* loaded from: classes.dex */
public class ViewBarInfo {
    int endIndex;
    int startIndex;
    int viewHeight;
    float width;

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float f) {
        this.width = f;
    }

    public ViewBarInfo() {
    }

    public ViewBarInfo(int i, int i2) {
        this.viewHeight = i;
        this.width = i2;
    }

    public int getViewHeight() {
        return this.viewHeight;
    }

    public ViewBarInfo(int i, int i2, int i3) {
        this.viewHeight = i;
        this.startIndex = i2;
        this.endIndex = i3;
    }

    public void setViewHeight(int i) {
        this.viewHeight = i;
    }

    public int getStartIndex() {
        return this.startIndex;
    }

    public void setStartIndex(int i) {
        this.startIndex = i;
    }

    public int getEndIndex() {
        return this.endIndex;
    }

    public void setEndIndex(int i) {
        this.endIndex = i;
    }

    public String toString() {
        return "ViewBarInfo{viewHeight=" + this.viewHeight + ", startIndex=" + this.startIndex + ", endIndex=" + this.endIndex + ", width=" + this.width + '}';
    }
}
