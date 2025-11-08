package com.jkcq.homebike;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.jkcq.base.app.BaseApp;
import com.jkcq.base.base.BaseViewModel;
import com.jkcq.homebike.ble.scanner.ExtendedBluetoothDevice;
import com.jkcq.util.AppUtil;
import com.jkcq.util.LogUtil;
import com.jkcq.util.ktx.ToastUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import no.nordicsemi.android.ble.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.ble.scanner.ScanCallback;
import no.nordicsemi.android.ble.scanner.ScanRecord;
import no.nordicsemi.android.ble.scanner.ScanResult;
import no.nordicsemi.android.ble.scanner.ScanSettings;

/* compiled from: BMSDeviceScanViewModel.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010)\u001a\u0004\u0018\u00010\u00132\u0006\u0010*\u001a\u00020+H\u0002J\u0006\u0010,\u001a\u00020-J\u0006\u0010.\u001a\u00020-J\u0006\u0010/\u001a\u00020-J\u0014\u00100\u001a\u00020-2\f\u00101\u001a\b\u0012\u0004\u0012\u00020+0\u0017R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D¢\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u001d\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00170\u0016¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00060\u0016¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0019R\u001a\u0010\u001e\u001a\u00020\u001fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u000e\u0010$\u001a\u00020%X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010&\u001a\u00020\u0006X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b'\u0010\b\"\u0004\b(\u0010\n¨\u00062"}, d2 = {"Lcom/jkcq/homebike/BMSDeviceScanViewModel;", "Lcom/jkcq/base/base/BaseViewModel;", "()V", "SCAN_DURATION", "", "connectingPosition", "", "getConnectingPosition", "()I", "setConnectingPosition", "(I)V", "handler", "Landroid/os/Handler;", "getHandler", "()Landroid/os/Handler;", "setHandler", "(Landroid/os/Handler;)V", "listBondedValues", "Ljava/util/ArrayList;", "Lcom/jkcq/homebike/ble/scanner/ExtendedBluetoothDevice;", "listValues", "mDeviceLiveData", "Landroidx/lifecycle/MutableLiveData;", "", "getMDeviceLiveData", "()Landroidx/lifecycle/MutableLiveData;", "scanCallback", "Lno/nordicsemi/android/ble/scanner/ScanCallback;", "scanDeviceState", "getScanDeviceState", "scanning", "", "getScanning", "()Z", "setScanning", "(Z)V", "stopScanTask", "Ljava/lang/Runnable;", "sumSize", "getSumSize", "setSumSize", "findDevice", "result", "Lno/nordicsemi/android/ble/scanner/ScanResult;", "scanTimeOut", "", "startLeScan", "stopLeScan", "update", "results", "app_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class BMSDeviceScanViewModel extends BaseViewModel {
    private boolean scanning;
    private volatile int sumSize;
    private final MutableLiveData<List<ExtendedBluetoothDevice>> mDeviceLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> scanDeviceState = new MutableLiveData<>();
    private final ArrayList<ExtendedBluetoothDevice> listBondedValues = new ArrayList<>();
    private final ArrayList<ExtendedBluetoothDevice> listValues = new ArrayList<>();
    private final long SCAN_DURATION = 15000;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int connectingPosition = -1;
    private final Runnable stopScanTask = new Runnable() { // from class: com.jkcq.homebike.BMSDeviceScanViewModel$stopScanTask$1
        @Override // java.lang.Runnable
        public final void run() {
            this.this$0.scanTimeOut();
            this.this$0.stopLeScan();
        }
    };
    private final ScanCallback scanCallback = new ScanCallback() { // from class: com.jkcq.homebike.BMSDeviceScanViewModel$scanCallback$1
        @Override // no.nordicsemi.android.ble.scanner.ScanCallback
        public void onScanFailed(int errorCode) {
        }

        @Override // no.nordicsemi.android.ble.scanner.ScanCallback
        public void onScanResult(int callbackType, ScanResult result) {
            Intrinsics.checkParameterIsNotNull(result, "result");
        }

        @Override // no.nordicsemi.android.ble.scanner.ScanCallback
        public void onBatchScanResults(List<ScanResult> results) {
            Intrinsics.checkParameterIsNotNull(results, "results");
            LogUtil.e("startLeScan4");
            this.this$0.update(results);
        }
    };

    public final MutableLiveData<List<ExtendedBluetoothDevice>> getMDeviceLiveData() {
        return this.mDeviceLiveData;
    }

    public final MutableLiveData<Integer> getScanDeviceState() {
        return this.scanDeviceState;
    }

    protected final int getSumSize() {
        return this.sumSize;
    }

    protected final void setSumSize(int i) {
        this.sumSize = i;
    }

    public final Handler getHandler() {
        return this.handler;
    }

    public final void setHandler(Handler handler) {
        Intrinsics.checkParameterIsNotNull(handler, "<set-?>");
        this.handler = handler;
    }

    public final int getConnectingPosition() {
        return this.connectingPosition;
    }

    public final void setConnectingPosition(int i) {
        this.connectingPosition = i;
    }

    public final boolean getScanning() {
        return this.scanning;
    }

    public final void setScanning(boolean z) {
        this.scanning = z;
    }

    public final void scanTimeOut() {
        Log.e("scanTimeOut", "scanTimeOut");
        this.handler.post(new Runnable() { // from class: com.jkcq.homebike.BMSDeviceScanViewModel.scanTimeOut.1
            @Override // java.lang.Runnable
            public final void run() {
                BMSDeviceScanViewModel.this.getScanDeviceState().setValue(Integer.valueOf(BMSConfig.BIKE_CONN_IS_SCAN_TIMEOUT));
            }
        });
    }

    public final void stopLeScan() {
        if (this.scanning) {
            BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
            Intrinsics.checkExpressionValueIsNotNull(scanner, "BluetoothLeScannerCompat.getScanner()");
            scanner.stopScan(this.scanCallback);
            this.handler.removeCallbacks(this.stopScanTask);
            this.scanning = false;
        }
    }

    public final void startLeScan() {
        if (!AppUtil.INSTANCE.isOpenBle()) {
            ToastUtil.showTextToast(BaseApp.INSTANCE.getSApplicaton(), BaseApp.INSTANCE.getSApplicaton().getString(com.ble.vanomize12.R.string.openBle));
            return;
        }
        LogUtil.e("startLeScan");
        this.sumSize = 0;
        if (this.connectingPosition >= 0) {
            return;
        }
        if (this.scanning) {
            this.handler.removeCallbacks(this.stopScanTask);
            this.handler.postDelayed(this.stopScanTask, this.SCAN_DURATION);
            return;
        }
        this.scanDeviceState.setValue(Integer.valueOf(BMSConfig.BIKE_CONN_IS_SCAN));
        LogUtil.e("startLeScan2");
        BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        Intrinsics.checkExpressionValueIsNotNull(scanner, "BluetoothLeScannerCompat.getScanner()");
        ScanSettings scanSettingsBuild = new ScanSettings.Builder().setReportDelay(1000L).setUseHardwareBatchingIfSupported(false).setScanMode(2).build();
        Intrinsics.checkExpressionValueIsNotNull(scanSettingsBuild, "ScanSettings.Builder().s…MODE_LOW_LATENCY).build()");
        scanner.startScan(null, scanSettingsBuild, this.scanCallback);
        LogUtil.e("startLeScan3");
        this.handler.postDelayed(this.stopScanTask, this.SCAN_DURATION);
        this.scanning = true;
    }

    public final void update(List<ScanResult> results) {
        String deviceName;
        Intrinsics.checkParameterIsNotNull(results, "results");
        for (ScanResult scanResult : results) {
            ExtendedBluetoothDevice extendedBluetoothDeviceFindDevice = findDevice(scanResult);
            if (extendedBluetoothDeviceFindDevice == null) {
                BluetoothDevice device = scanResult.getDevice();
                Intrinsics.checkExpressionValueIsNotNull(device, "result.device");
                if (!TextUtils.isEmpty(device.getName())) {
                    this.listValues.add(new ExtendedBluetoothDevice(scanResult));
                }
            } else {
                if (scanResult.getScanRecord() != null) {
                    ScanRecord scanRecord = scanResult.getScanRecord();
                    if (scanRecord == null) {
                        Intrinsics.throwNpe();
                    }
                    Intrinsics.checkExpressionValueIsNotNull(scanRecord, "result.scanRecord!!");
                    deviceName = scanRecord.getDeviceName();
                } else {
                    deviceName = null;
                }
                extendedBluetoothDeviceFindDevice.name = deviceName;
                extendedBluetoothDeviceFindDevice.rssi = scanResult.getRssi();
            }
        }
        LogUtil.e("startLeScan5");
        this.handler.post(new Runnable() { // from class: com.jkcq.homebike.BMSDeviceScanViewModel.update.1
            @Override // java.lang.Runnable
            public final void run() {
                BMSDeviceScanViewModel.this.getMDeviceLiveData().setValue(BMSDeviceScanViewModel.this.listValues);
            }
        });
    }

    private final ExtendedBluetoothDevice findDevice(ScanResult result) {
        Iterator<ExtendedBluetoothDevice> it = this.listBondedValues.iterator();
        while (it.hasNext()) {
            ExtendedBluetoothDevice next = it.next();
            if (next.matches(result)) {
                return next;
            }
        }
        Iterator<ExtendedBluetoothDevice> it2 = this.listValues.iterator();
        while (it2.hasNext()) {
            ExtendedBluetoothDevice next2 = it2.next();
            if (next2.matches(result)) {
                return next2;
            }
        }
        return null;
    }
}
