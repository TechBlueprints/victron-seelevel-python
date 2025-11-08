package no.nordicsemi.android.ble.scanner;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.nordicsemi.android.ble.scanner.ScanSettings;

/* loaded from: classes.dex */
public abstract class BluetoothLeScannerCompat {
    public static final String EXTRA_CALLBACK_TYPE = "android.bluetooth.le.extra.CALLBACK_TYPE";
    public static final String EXTRA_ERROR_CODE = "android.bluetooth.le.extra.ERROR_CODE";
    public static final String EXTRA_LIST_SCAN_RESULT = "android.bluetooth.le.extra.LIST_SCAN_RESULT";
    private static BluetoothLeScannerCompat instance;

    public abstract void flushPendingScanResults(ScanCallback scanCallback);

    abstract void startScanInternal(List<ScanFilter> list, ScanSettings scanSettings, Context context, PendingIntent pendingIntent);

    abstract void startScanInternal(List<ScanFilter> list, ScanSettings scanSettings, ScanCallback scanCallback, Handler handler);

    abstract void stopScanInternal(Context context, PendingIntent pendingIntent);

    abstract void stopScanInternal(ScanCallback scanCallback);

    public static synchronized BluetoothLeScannerCompat getScanner() {
        if (instance != null) {
            return instance;
        }
        if (Build.VERSION.SDK_INT >= 26) {
            BluetoothLeScannerImplOreo bluetoothLeScannerImplOreo = new BluetoothLeScannerImplOreo();
            instance = bluetoothLeScannerImplOreo;
            return bluetoothLeScannerImplOreo;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            BluetoothLeScannerImplMarshmallow bluetoothLeScannerImplMarshmallow = new BluetoothLeScannerImplMarshmallow();
            instance = bluetoothLeScannerImplMarshmallow;
            return bluetoothLeScannerImplMarshmallow;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            BluetoothLeScannerImplLollipop bluetoothLeScannerImplLollipop = new BluetoothLeScannerImplLollipop();
            instance = bluetoothLeScannerImplLollipop;
            return bluetoothLeScannerImplLollipop;
        }
        BluetoothLeScannerImplJB bluetoothLeScannerImplJB = new BluetoothLeScannerImplJB();
        instance = bluetoothLeScannerImplJB;
        return bluetoothLeScannerImplJB;
    }

    BluetoothLeScannerCompat() {
    }

    public final void startScan(ScanCallback scanCallback) {
        if (scanCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        startScanInternal(Collections.emptyList(), new ScanSettings.Builder().build(), scanCallback, new Handler(Looper.getMainLooper()));
    }

    public final void startScan(List<ScanFilter> list, ScanSettings scanSettings, ScanCallback scanCallback) {
        if (scanCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        Handler handler = new Handler(Looper.getMainLooper());
        if (list == null) {
            list = Collections.emptyList();
        }
        if (scanSettings == null) {
            scanSettings = new ScanSettings.Builder().build();
        }
        startScanInternal(list, scanSettings, scanCallback, handler);
    }

    public final void startScan(List<ScanFilter> list, ScanSettings scanSettings, ScanCallback scanCallback, Handler handler) {
        if (scanCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        if (list == null) {
            list = Collections.emptyList();
        }
        if (scanSettings == null) {
            scanSettings = new ScanSettings.Builder().build();
        }
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        startScanInternal(list, scanSettings, scanCallback, handler);
    }

    public final void stopScan(ScanCallback scanCallback) {
        if (scanCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        stopScanInternal(scanCallback);
    }

    public final void startScan(List<ScanFilter> list, ScanSettings scanSettings, Context context, PendingIntent pendingIntent) {
        if (pendingIntent == null) {
            throw new IllegalArgumentException("callbackIntent is null");
        }
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }
        if (list == null) {
            list = Collections.emptyList();
        }
        if (scanSettings == null) {
            scanSettings = new ScanSettings.Builder().build();
        }
        startScanInternal(list, scanSettings, context, pendingIntent);
    }

    public final void stopScan(Context context, PendingIntent pendingIntent) {
        if (pendingIntent == null) {
            throw new IllegalArgumentException("callbackIntent is null");
        }
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }
        stopScanInternal(context, pendingIntent);
    }

    static class ScanCallbackWrapper {
        private final boolean emulateBatching;
        private final boolean emulateFiltering;
        private final boolean emulateFoundOrLostCallbackType;
        final List<ScanFilter> filters;
        final Handler handler;
        final ScanCallback scanCallback;
        final ScanSettings scanSettings;
        private final Object LOCK = new Object();
        private final List<ScanResult> scanResults = new ArrayList();
        private final Set<String> devicesInBatch = new HashSet();
        private final Map<String, ScanResult> devicesInRange = new HashMap();
        private final Runnable flushPendingScanResultsTask = new Runnable() { // from class: no.nordicsemi.android.ble.scanner.BluetoothLeScannerCompat.ScanCallbackWrapper.1
            @Override // java.lang.Runnable
            public void run() {
                if (ScanCallbackWrapper.this.scanningStopped) {
                    return;
                }
                ScanCallbackWrapper.this.flushPendingScanResults();
                ScanCallbackWrapper.this.handler.postDelayed(this, ScanCallbackWrapper.this.scanSettings.getReportDelayMillis());
            }
        };
        private final Runnable matchLostNotifierTask = new Runnable() { // from class: no.nordicsemi.android.ble.scanner.BluetoothLeScannerCompat.ScanCallbackWrapper.2
            @Override // java.lang.Runnable
            public void run() {
                long jElapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
                synchronized (ScanCallbackWrapper.this.LOCK) {
                    Iterator it = ScanCallbackWrapper.this.devicesInRange.values().iterator();
                    while (it.hasNext()) {
                        final ScanResult scanResult = (ScanResult) it.next();
                        if (scanResult.getTimestampNanos() < jElapsedRealtimeNanos - ScanCallbackWrapper.this.scanSettings.getMatchLostDeviceTimeout()) {
                            it.remove();
                            ScanCallbackWrapper.this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.scanner.BluetoothLeScannerCompat.ScanCallbackWrapper.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    ScanCallbackWrapper.this.scanCallback.onScanResult(4, scanResult);
                                }
                            });
                        }
                    }
                    if (!ScanCallbackWrapper.this.devicesInRange.isEmpty()) {
                        ScanCallbackWrapper.this.handler.postDelayed(this, ScanCallbackWrapper.this.scanSettings.getMatchLostTaskInterval());
                    }
                }
            }
        };
        private boolean scanningStopped = false;

        ScanCallbackWrapper(boolean z, boolean z2, List<ScanFilter> list, ScanSettings scanSettings, ScanCallback scanCallback, Handler handler) {
            this.filters = Collections.unmodifiableList(list);
            this.scanSettings = scanSettings;
            this.scanCallback = scanCallback;
            this.handler = handler;
            boolean z3 = false;
            this.emulateFoundOrLostCallbackType = (scanSettings.getCallbackType() == 1 || ((Build.VERSION.SDK_INT >= 23) && scanSettings.getUseHardwareCallbackTypesIfSupported())) ? false : true;
            this.emulateFiltering = (list.isEmpty() || (z2 && scanSettings.getUseHardwareFilteringIfSupported())) ? false : true;
            long reportDelayMillis = scanSettings.getReportDelayMillis();
            if (reportDelayMillis > 0 && (!z || !scanSettings.getUseHardwareBatchingIfSupported())) {
                z3 = true;
            }
            this.emulateBatching = z3;
            if (z3) {
                handler.postDelayed(this.flushPendingScanResultsTask, reportDelayMillis);
            }
        }

        void close() {
            this.scanningStopped = true;
            this.handler.removeCallbacksAndMessages(null);
            synchronized (this.LOCK) {
                this.devicesInRange.clear();
                this.devicesInBatch.clear();
                this.scanResults.clear();
            }
        }

        void flushPendingScanResults() {
            if (!this.emulateBatching || this.scanningStopped) {
                return;
            }
            synchronized (this.LOCK) {
                this.scanCallback.onBatchScanResults(new ArrayList(this.scanResults));
                this.scanResults.clear();
                this.devicesInBatch.clear();
            }
        }

        void handleScanResult(int i, ScanResult scanResult) {
            boolean zIsEmpty;
            ScanResult scanResultPut;
            if (this.scanningStopped) {
                return;
            }
            if (this.filters.isEmpty() || matches(scanResult)) {
                String address = scanResult.getDevice().getAddress();
                if (this.emulateFoundOrLostCallbackType) {
                    synchronized (this.devicesInRange) {
                        zIsEmpty = this.devicesInRange.isEmpty();
                        scanResultPut = this.devicesInRange.put(address, scanResult);
                    }
                    if (scanResultPut == null && (this.scanSettings.getCallbackType() & 2) > 0) {
                        this.scanCallback.onScanResult(2, scanResult);
                    }
                    if (!zIsEmpty || (this.scanSettings.getCallbackType() & 4) <= 0) {
                        return;
                    }
                    this.handler.removeCallbacks(this.matchLostNotifierTask);
                    this.handler.postDelayed(this.matchLostNotifierTask, this.scanSettings.getMatchLostTaskInterval());
                    return;
                }
                if (this.emulateBatching) {
                    synchronized (this.LOCK) {
                        if (!this.devicesInBatch.contains(address)) {
                            this.scanResults.add(scanResult);
                            this.devicesInBatch.add(address);
                        }
                    }
                    return;
                }
                this.scanCallback.onScanResult(i, scanResult);
            }
        }

        void handleScanResults(List<ScanResult> list) {
            if (this.scanningStopped) {
                return;
            }
            if (this.emulateFiltering) {
                ArrayList arrayList = new ArrayList();
                for (ScanResult scanResult : list) {
                    if (matches(scanResult)) {
                        arrayList.add(scanResult);
                    }
                }
                list = arrayList;
            }
            this.scanCallback.onBatchScanResults(list);
        }

        void handleScanError(int i) {
            this.scanCallback.onScanFailed(i);
        }

        private boolean matches(ScanResult scanResult) {
            Iterator<ScanFilter> it = this.filters.iterator();
            while (it.hasNext()) {
                if (it.next().matches(scanResult)) {
                    return true;
                }
            }
            return false;
        }
    }
}
