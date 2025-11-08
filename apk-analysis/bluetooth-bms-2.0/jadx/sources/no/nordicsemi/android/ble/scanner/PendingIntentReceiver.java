package no.nordicsemi.android.ble.scanner;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;
import no.nordicsemi.android.ble.scanner.BluetoothLeScannerImplOreo;

/* loaded from: classes.dex */
public class PendingIntentReceiver extends BroadcastReceiver {
    static final String ACTION = "no.nordicsemi.android.support.v18.ACTION_FOUND";
    static final String EXTRA_FILTERS = "no.nordicsemi.android.support.v18.EXTRA_FILTERS";
    static final String EXTRA_MATCH_LOST_INTERVAL = "no.nordicsemi.android.support.v18.EXTRA_MATCH_LOST_INTERVAL";
    static final String EXTRA_MATCH_LOST_TIMEOUT = "no.nordicsemi.android.support.v18.EXTRA_MATCH_LOST_TIMEOUT";
    static final String EXTRA_MATCH_MODE = "no.nordicsemi.android.support.v18.EXTRA_MATCH_MODE";
    static final String EXTRA_NUM_OF_MATCHES = "no.nordicsemi.android.support.v18.EXTRA_NUM_OF_MATCHES";
    static final String EXTRA_PENDING_INTENT = "no.nordicsemi.android.support.v18.EXTRA_PENDING_INTENT";
    static final String EXTRA_SETTINGS = "no.nordicsemi.android.support.v18.EXTRA_SETTINGS";
    static final String EXTRA_USE_HARDWARE_BATCHING = "no.nordicsemi.android.support.v18.EXTRA_USE_HARDWARE_BATCHING";
    static final String EXTRA_USE_HARDWARE_CALLBACK_TYPES = "no.nordicsemi.android.support.v18.EXTRA_USE_HARDWARE_CALLBACK_TYPES";
    static final String EXTRA_USE_HARDWARE_FILTERING = "no.nordicsemi.android.support.v18.EXTRA_USE_HARDWARE_FILTERING";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        PendingIntent pendingIntent;
        BluetoothLeScannerImplOreo.PendingIntentExecutorWrapper wrapper;
        BluetoothLeScannerImplOreo bluetoothLeScannerImplOreo;
        if (context == null || intent == null || (pendingIntent = (PendingIntent) intent.getParcelableExtra(EXTRA_PENDING_INTENT)) == null) {
            return;
        }
        ArrayList parcelableArrayListExtra = intent.getParcelableArrayListExtra(EXTRA_FILTERS);
        android.bluetooth.le.ScanSettings scanSettings = (android.bluetooth.le.ScanSettings) intent.getParcelableExtra(EXTRA_SETTINGS);
        if (parcelableArrayListExtra == null || scanSettings == null) {
            return;
        }
        boolean booleanExtra = intent.getBooleanExtra(EXTRA_USE_HARDWARE_BATCHING, true);
        boolean booleanExtra2 = intent.getBooleanExtra(EXTRA_USE_HARDWARE_FILTERING, true);
        boolean booleanExtra3 = intent.getBooleanExtra(EXTRA_USE_HARDWARE_CALLBACK_TYPES, true);
        long longExtra = intent.getLongExtra(EXTRA_MATCH_LOST_TIMEOUT, 10000L);
        long longExtra2 = intent.getLongExtra(EXTRA_MATCH_LOST_INTERVAL, 10000L);
        int intExtra = intent.getIntExtra(EXTRA_MATCH_MODE, 1);
        int intExtra2 = intent.getIntExtra(EXTRA_NUM_OF_MATCHES, 3);
        BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        BluetoothLeScannerImplOreo bluetoothLeScannerImplOreo2 = (BluetoothLeScannerImplOreo) scanner;
        ArrayList<ScanFilter> arrayListFromNativeScanFilters = bluetoothLeScannerImplOreo2.fromNativeScanFilters(parcelableArrayListExtra);
        ScanSettings scanSettingsFromNativeScanSettings = bluetoothLeScannerImplOreo2.fromNativeScanSettings(scanSettings, booleanExtra, booleanExtra2, booleanExtra3, longExtra, longExtra2, intExtra, intExtra2);
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean zIsOffloadedScanBatchingSupported = defaultAdapter.isOffloadedScanBatchingSupported();
        boolean zIsOffloadedFilteringSupported = defaultAdapter.isOffloadedFilteringSupported();
        synchronized (scanner) {
            try {
                wrapper = bluetoothLeScannerImplOreo2.getWrapper(pendingIntent);
                if (wrapper == null) {
                    bluetoothLeScannerImplOreo = bluetoothLeScannerImplOreo2;
                    BluetoothLeScannerImplOreo.PendingIntentExecutorWrapper pendingIntentExecutorWrapper = new BluetoothLeScannerImplOreo.PendingIntentExecutorWrapper(zIsOffloadedScanBatchingSupported, zIsOffloadedFilteringSupported, arrayListFromNativeScanFilters, scanSettingsFromNativeScanSettings, pendingIntent);
                    bluetoothLeScannerImplOreo.addWrapper(pendingIntent, pendingIntentExecutorWrapper);
                    wrapper = pendingIntentExecutorWrapper;
                } else {
                    bluetoothLeScannerImplOreo = bluetoothLeScannerImplOreo2;
                }
            } catch (IllegalStateException unused) {
                return;
            }
        }
        wrapper.executor.setTemporaryContext(context);
        ArrayList parcelableArrayListExtra2 = intent.getParcelableArrayListExtra(BluetoothLeScannerCompat.EXTRA_LIST_SCAN_RESULT);
        if (parcelableArrayListExtra2 != null) {
            ArrayList<ScanResult> arrayListFromNativeScanResults = bluetoothLeScannerImplOreo.fromNativeScanResults(parcelableArrayListExtra2);
            if (scanSettingsFromNativeScanSettings.getReportDelayMillis() > 0) {
                wrapper.handleScanResults(arrayListFromNativeScanResults);
            } else if (!arrayListFromNativeScanResults.isEmpty()) {
                wrapper.handleScanResult(intent.getIntExtra(BluetoothLeScannerCompat.EXTRA_CALLBACK_TYPE, 1), arrayListFromNativeScanResults.get(0));
            }
        } else {
            int intExtra3 = intent.getIntExtra(BluetoothLeScannerCompat.EXTRA_ERROR_CODE, 0);
            if (intExtra3 != 0) {
                wrapper.handleScanError(intExtra3);
            }
        }
        wrapper.executor.setTemporaryContext(null);
    }
}
