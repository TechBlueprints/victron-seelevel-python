package com.jkcq.homebike.ble.scanner;

import android.bluetooth.BluetoothDevice;
import no.nordicsemi.android.ble.scanner.ScanResult;

/* loaded from: classes.dex */
public class ExtendedBluetoothDevice implements Comparable {
    static final int NO_RSSI = -1000;
    public final BluetoothDevice device;
    public boolean isBonded;
    public boolean isConnect;
    public String name;
    public int rssi;

    public ExtendedBluetoothDevice(ScanResult scanResult) {
        this.device = scanResult.getDevice();
        this.name = scanResult.getScanRecord() != null ? scanResult.getScanRecord().getDeviceName() : null;
        this.rssi = scanResult.getRssi();
        this.isBonded = false;
    }

    public ExtendedBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.device = bluetoothDevice;
        this.name = bluetoothDevice.getName();
        this.rssi = -1000;
        this.isBonded = true;
    }

    public ExtendedBluetoothDevice(BluetoothDevice bluetoothDevice, boolean z) {
        this.isConnect = z;
        this.device = bluetoothDevice;
        this.name = bluetoothDevice.getName();
        this.rssi = -1000;
        this.isBonded = true;
    }

    public boolean matches(ScanResult scanResult) {
        return this.device.getAddress().equals(scanResult.getDevice().getAddress());
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        return this.rssi > ((ExtendedBluetoothDevice) obj).rssi ? -1 : 1;
    }
}
