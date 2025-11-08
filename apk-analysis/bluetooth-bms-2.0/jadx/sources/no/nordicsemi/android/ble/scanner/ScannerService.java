package no.nordicsemi.android.ble.scanner;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import no.nordicsemi.android.ble.scanner.ScanSettings;

/* loaded from: classes.dex */
public class ScannerService extends Service {
    static final String EXTRA_FILTERS = "no.nordicsemi.android.support.v18.EXTRA_FILTERS";
    static final String EXTRA_PENDING_INTENT = "no.nordicsemi.android.support.v18.EXTRA_PENDING_INTENT";
    static final String EXTRA_SETTINGS = "no.nordicsemi.android.support.v18.EXTRA_SETTINGS";
    static final String EXTRA_START = "no.nordicsemi.android.support.v18.EXTRA_START";
    private static final String TAG = "ScannerService";
    private final Object LOCK = new Object();
    private HashMap<PendingIntent, ScanCallback> callbacks;
    private Handler handler;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.callbacks = new HashMap<>();
        this.handler = new Handler();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        boolean zContainsKey;
        boolean zIsEmpty;
        PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra(EXTRA_PENDING_INTENT);
        boolean booleanExtra = intent.getBooleanExtra(EXTRA_START, false);
        boolean z = !booleanExtra;
        if (pendingIntent == null) {
            synchronized (this.LOCK) {
                zIsEmpty = this.callbacks.isEmpty();
            }
            if (zIsEmpty) {
                stopSelf();
            }
            return 2;
        }
        synchronized (this.LOCK) {
            zContainsKey = this.callbacks.containsKey(pendingIntent);
        }
        if (booleanExtra && !zContainsKey) {
            List<ScanFilter> parcelableArrayListExtra = intent.getParcelableArrayListExtra(EXTRA_FILTERS);
            ScanSettings scanSettingsBuild = (ScanSettings) intent.getParcelableExtra(EXTRA_SETTINGS);
            if (parcelableArrayListExtra == null) {
                parcelableArrayListExtra = Collections.emptyList();
            }
            if (scanSettingsBuild == null) {
                scanSettingsBuild = new ScanSettings.Builder().build();
            }
            startScan(parcelableArrayListExtra, scanSettingsBuild, pendingIntent);
        } else if (z && zContainsKey) {
            stopScan(pendingIntent);
        }
        return 2;
    }

    @Override // android.app.Service
    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
    }

    @Override // android.app.Service
    public void onDestroy() {
        BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        Iterator<ScanCallback> it = this.callbacks.values().iterator();
        while (it.hasNext()) {
            try {
                scanner.stopScan(it.next());
            } catch (Exception unused) {
            }
        }
        this.callbacks.clear();
        this.callbacks = null;
        this.handler = null;
        super.onDestroy();
    }

    private void startScan(List<ScanFilter> list, ScanSettings scanSettings, PendingIntent pendingIntent) {
        PendingIntentExecutor pendingIntentExecutor = new PendingIntentExecutor(pendingIntent, scanSettings, this);
        synchronized (this.LOCK) {
            this.callbacks.put(pendingIntent, pendingIntentExecutor);
        }
        try {
            BluetoothLeScannerCompat.getScanner().startScanInternal(list, scanSettings, pendingIntentExecutor, this.handler);
        } catch (Exception e) {
            Log.w(TAG, "Starting scanning failed", e);
        }
    }

    private void stopScan(PendingIntent pendingIntent) {
        ScanCallback scanCallbackRemove;
        boolean zIsEmpty;
        synchronized (this.LOCK) {
            scanCallbackRemove = this.callbacks.remove(pendingIntent);
            zIsEmpty = this.callbacks.isEmpty();
        }
        if (scanCallbackRemove == null) {
            return;
        }
        try {
            BluetoothLeScannerCompat.getScanner().stopScan(scanCallbackRemove);
        } catch (Exception e) {
            Log.w(TAG, "Stopping scanning failed", e);
        }
        if (zIsEmpty) {
            stopSelf();
        }
    }
}
