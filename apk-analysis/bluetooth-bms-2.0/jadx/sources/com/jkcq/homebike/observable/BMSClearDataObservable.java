package com.jkcq.homebike.observable;

import java.util.Observable;

/* loaded from: classes.dex */
public class BMSClearDataObservable extends Observable {
    private static BMSClearDataObservable obser;

    @Override // java.util.Observable
    public synchronized void setChanged() {
        super.setChanged();
    }

    private BMSClearDataObservable() {
    }

    public static BMSClearDataObservable getInstance() {
        if (obser == null) {
            synchronized (BMSClearDataObservable.class) {
                if (obser == null) {
                    obser = new BMSClearDataObservable();
                }
            }
        }
        return obser;
    }

    public void clearData() {
        getInstance().setChanged();
        getInstance().notifyObservers();
    }
}
