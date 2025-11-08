package no.nordicsemi.android.ble.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanSettings;

/* loaded from: classes.dex */
class BluetoothLeScannerImplMarshmallow extends BluetoothLeScannerImplLollipop {
    BluetoothLeScannerImplMarshmallow() {
    }

    @Override // no.nordicsemi.android.ble.scanner.BluetoothLeScannerImplLollipop
    android.bluetooth.le.ScanSettings toNativeScanSettings(BluetoothAdapter bluetoothAdapter, ScanSettings scanSettings, boolean z) {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        if (z || (bluetoothAdapter.isOffloadedScanBatchingSupported() && scanSettings.getUseHardwareBatchingIfSupported())) {
            builder.setReportDelay(scanSettings.getReportDelayMillis());
        }
        if (z || scanSettings.getUseHardwareCallbackTypesIfSupported()) {
            builder.setCallbackType(scanSettings.getCallbackType()).setMatchMode(scanSettings.getMatchMode()).setNumOfMatches(scanSettings.getNumOfMatches());
        }
        builder.setScanMode(scanSettings.getScanMode());
        return builder.build();
    }
}
