package com.jkcq.homebike.observable;

import android.util.Log;
import com.jkcq.homebike.BMSDataBean;
import java.util.Observable;

/* loaded from: classes.dex */
public class BMSRealDataObservable extends Observable {
    private static BMSRealDataObservable obser;

    @Override // java.util.Observable
    public synchronized void setChanged() {
        super.setChanged();
    }

    private BMSRealDataObservable() {
    }

    public static BMSRealDataObservable getInstance() {
        if (obser == null) {
            synchronized (BMSRealDataObservable.class) {
                if (obser == null) {
                    obser = new BMSRealDataObservable();
                }
            }
        }
        return obser;
    }

    public void setRealData(BMSDataBean bMSDataBean) {
        getInstance().setChanged();
        getInstance().notifyObservers(bMSDataBean);
    }

    public void sendHrValue(Integer num) {
        Log.e("sendHrValue", num + "");
        getInstance().setChanged();
        getInstance().notifyObservers(num);
    }
}
