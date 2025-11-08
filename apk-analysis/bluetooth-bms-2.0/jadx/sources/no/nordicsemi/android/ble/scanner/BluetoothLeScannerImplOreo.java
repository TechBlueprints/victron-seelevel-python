package no.nordicsemi.android.ble.scanner;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import no.nordicsemi.android.ble.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.ble.scanner.ScanFilter;
import no.nordicsemi.android.ble.scanner.ScanSettings;

/* loaded from: classes.dex */
class BluetoothLeScannerImplOreo extends BluetoothLeScannerImplMarshmallow {
    private final HashMap<PendingIntent, PendingIntentExecutorWrapper> wrappers = new HashMap<>();

    BluetoothLeScannerImplOreo() {
    }

    PendingIntentExecutorWrapper getWrapper(PendingIntent pendingIntent) {
        synchronized (this.wrappers) {
            if (!this.wrappers.containsKey(pendingIntent)) {
                return null;
            }
            PendingIntentExecutorWrapper pendingIntentExecutorWrapper = this.wrappers.get(pendingIntent);
            if (pendingIntentExecutorWrapper != null) {
                return pendingIntentExecutorWrapper;
            }
            throw new IllegalStateException("Scanning has been stopped");
        }
    }

    void addWrapper(PendingIntent pendingIntent, PendingIntentExecutorWrapper pendingIntentExecutorWrapper) {
        synchronized (this.wrappers) {
            this.wrappers.put(pendingIntent, pendingIntentExecutorWrapper);
        }
    }

    @Override // no.nordicsemi.android.ble.scanner.BluetoothLeScannerImplLollipop, no.nordicsemi.android.ble.scanner.BluetoothLeScannerCompat
    void startScanInternal(List<ScanFilter> list, ScanSettings scanSettings, Context context, PendingIntent pendingIntent) {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeScanner bluetoothLeScanner = defaultAdapter.getBluetoothLeScanner();
        if (bluetoothLeScanner == null) {
            throw new IllegalStateException("BT le scanner not available");
        }
        if (scanSettings == null) {
            scanSettings = new ScanSettings.Builder().build();
        }
        List<ScanFilter> listEmptyList = list != null ? list : Collections.emptyList();
        android.bluetooth.le.ScanSettings nativeScanSettings = toNativeScanSettings(defaultAdapter, scanSettings, false);
        ArrayList<android.bluetooth.le.ScanFilter> nativeScanFilters = null;
        if (list != null && defaultAdapter.isOffloadedFilteringSupported() && scanSettings.getUseHardwareFilteringIfSupported()) {
            nativeScanFilters = toNativeScanFilters(list);
        }
        synchronized (this.wrappers) {
            this.wrappers.remove(pendingIntent);
        }
        bluetoothLeScanner.startScan(nativeScanFilters, nativeScanSettings, createStartingPendingIntent(listEmptyList, scanSettings, context, pendingIntent));
    }

    @Override // no.nordicsemi.android.ble.scanner.BluetoothLeScannerImplLollipop, no.nordicsemi.android.ble.scanner.BluetoothLeScannerCompat
    void stopScanInternal(Context context, PendingIntent pendingIntent) {
        BluetoothLeScanner bluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        if (bluetoothLeScanner == null) {
            throw new IllegalStateException("BT le scanner not available");
        }
        bluetoothLeScanner.stopScan(createStoppingPendingIntent(context, pendingIntent));
        synchronized (this.wrappers) {
            this.wrappers.put(pendingIntent, null);
        }
    }

    private PendingIntent createStartingPendingIntent(List<ScanFilter> list, ScanSettings scanSettings, Context context, PendingIntent pendingIntent) {
        int iHashCode = pendingIntent.hashCode();
        Intent intent = new Intent(context, (Class<?>) PendingIntentReceiver.class);
        intent.setAction("no.nordicsemi.android.support.v18.ACTION_FOUND");
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_PENDING_INTENT", pendingIntent);
        intent.putParcelableArrayListExtra("no.nordicsemi.android.support.v18.EXTRA_FILTERS", toNativeScanFilters(list));
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_SETTINGS", toNativeScanSettings(defaultAdapter, scanSettings, true));
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_USE_HARDWARE_BATCHING", scanSettings.getUseHardwareBatchingIfSupported());
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_USE_HARDWARE_FILTERING", scanSettings.getUseHardwareFilteringIfSupported());
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_USE_HARDWARE_CALLBACK_TYPES", scanSettings.getUseHardwareCallbackTypesIfSupported());
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_MATCH_MODE", scanSettings.getMatchMode());
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_NUM_OF_MATCHES", scanSettings.getNumOfMatches());
        return PendingIntent.getBroadcast(context, iHashCode, intent, 134217728);
    }

    private PendingIntent createStoppingPendingIntent(Context context, PendingIntent pendingIntent) {
        int iHashCode = pendingIntent.hashCode();
        Intent intent = new Intent(context, (Class<?>) PendingIntentReceiver.class);
        intent.setAction("no.nordicsemi.android.support.v18.ACTION_FOUND");
        return PendingIntent.getBroadcast(context, iHashCode, intent, 134217728);
    }

    @Override // no.nordicsemi.android.ble.scanner.BluetoothLeScannerImplMarshmallow, no.nordicsemi.android.ble.scanner.BluetoothLeScannerImplLollipop
    android.bluetooth.le.ScanSettings toNativeScanSettings(BluetoothAdapter bluetoothAdapter, ScanSettings scanSettings, boolean z) {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        if (z || (bluetoothAdapter.isOffloadedScanBatchingSupported() && scanSettings.getUseHardwareBatchingIfSupported())) {
            builder.setReportDelay(scanSettings.getReportDelayMillis());
        }
        if (z || scanSettings.getUseHardwareCallbackTypesIfSupported()) {
            builder.setCallbackType(scanSettings.getCallbackType()).setMatchMode(scanSettings.getMatchMode()).setNumOfMatches(scanSettings.getNumOfMatches());
        }
        builder.setScanMode(scanSettings.getScanMode()).setLegacy(scanSettings.getLegacy()).setPhy(scanSettings.getPhy());
        return builder.build();
    }

    ScanSettings fromNativeScanSettings(android.bluetooth.le.ScanSettings scanSettings, boolean z, boolean z2, boolean z3, long j, long j2, int i, int i2) {
        return new ScanSettings.Builder().setLegacy(scanSettings.getLegacy()).setPhy(scanSettings.getPhy()).setCallbackType(scanSettings.getCallbackType()).setScanMode(scanSettings.getScanMode()).setReportDelay(scanSettings.getReportDelayMillis()).setUseHardwareBatchingIfSupported(z).setUseHardwareFilteringIfSupported(z2).setUseHardwareCallbackTypesIfSupported(z3).setMatchOptions(j, j2).setMatchMode(i).setNumOfMatches(i2).build();
    }

    ArrayList<ScanFilter> fromNativeScanFilters(List<android.bluetooth.le.ScanFilter> list) {
        ArrayList<ScanFilter> arrayList = new ArrayList<>();
        Iterator<android.bluetooth.le.ScanFilter> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(fromNativeScanFilter(it.next()));
        }
        return arrayList;
    }

    ScanFilter fromNativeScanFilter(android.bluetooth.le.ScanFilter scanFilter) {
        ScanFilter.Builder builder = new ScanFilter.Builder();
        builder.setDeviceAddress(scanFilter.getDeviceAddress()).setDeviceName(scanFilter.getDeviceName()).setServiceUuid(scanFilter.getServiceUuid(), scanFilter.getServiceUuidMask()).setManufacturerData(scanFilter.getManufacturerId(), scanFilter.getManufacturerData(), scanFilter.getManufacturerDataMask());
        if (scanFilter.getServiceDataUuid() != null) {
            builder.setServiceData(scanFilter.getServiceDataUuid(), scanFilter.getServiceData(), scanFilter.getServiceDataMask());
        }
        return builder.build();
    }

    @Override // no.nordicsemi.android.ble.scanner.BluetoothLeScannerImplLollipop
    ScanResult fromNativeScanResult(android.bluetooth.le.ScanResult scanResult) {
        return new ScanResult(scanResult.getDevice(), (scanResult.getDataStatus() << 5) | (scanResult.isLegacy() ? 16 : 0) | scanResult.isConnectable(), scanResult.getPrimaryPhy(), scanResult.getSecondaryPhy(), scanResult.getAdvertisingSid(), scanResult.getTxPower(), scanResult.getRssi(), scanResult.getPeriodicAdvertisingInterval(), ScanRecord.parseFromBytes(scanResult.getScanRecord() != null ? scanResult.getScanRecord().getBytes() : null), scanResult.getTimestampNanos());
    }

    static class PendingIntentExecutorWrapper extends BluetoothLeScannerCompat.ScanCallbackWrapper {
        final PendingIntentExecutor executor;

        PendingIntentExecutorWrapper(boolean z, boolean z2, List<ScanFilter> list, ScanSettings scanSettings, PendingIntent pendingIntent) {
            super(z, z2, list, scanSettings, new PendingIntentExecutor(pendingIntent, scanSettings), new Handler());
            this.executor = (PendingIntentExecutor) this.scanCallback;
        }
    }
}
