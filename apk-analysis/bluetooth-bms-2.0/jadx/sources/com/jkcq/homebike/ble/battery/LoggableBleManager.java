package com.jkcq.homebike.ble.battery;

import android.content.Context;
import android.util.Log;
import no.nordicsemi.android.ble.BleManagerCallbacks;
import no.nordicsemi.android.ble.LegacyBleManager;
import no.nordicsemi.android.log.ILogSession;
import no.nordicsemi.android.log.LogContract;
import no.nordicsemi.android.log.Logger;

/* loaded from: classes.dex */
public abstract class LoggableBleManager<T extends BleManagerCallbacks> extends LegacyBleManager<T> {
    private ILogSession logSession;

    public LoggableBleManager(Context context) {
        super(context);
    }

    public void setLogger(ILogSession iLogSession) {
        this.logSession = iLogSession;
    }

    @Override // no.nordicsemi.android.ble.BleManager, no.nordicsemi.android.ble.utils.ILogger
    public void log(int i, String str) {
        Logger.log(this.logSession, LogContract.Log.Level.fromPriority(i), str);
        Log.println(i, "BleManager", str);
    }
}
