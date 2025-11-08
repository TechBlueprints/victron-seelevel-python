package no.nordicsemi.android.ble.scanner;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class ScanResult implements Parcelable {
    public static final Parcelable.Creator<ScanResult> CREATOR = new Parcelable.Creator<ScanResult>() { // from class: no.nordicsemi.android.ble.scanner.ScanResult.1
        @Override // android.os.Parcelable.Creator
        public ScanResult createFromParcel(Parcel parcel) {
            return new ScanResult(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public ScanResult[] newArray(int i) {
            return new ScanResult[i];
        }
    };
    public static final int DATA_COMPLETE = 0;
    public static final int DATA_TRUNCATED = 2;
    static final int ET_CONNECTABLE_MASK = 1;
    static final int ET_LEGACY_MASK = 16;
    public static final int PERIODIC_INTERVAL_NOT_PRESENT = 0;
    public static final int PHY_UNUSED = 0;
    public static final int SID_NOT_PRESENT = 255;
    public static final int TX_POWER_NOT_PRESENT = 127;
    private int advertisingSid;
    private BluetoothDevice device;
    private int eventType;
    private int periodicAdvertisingInterval;
    private int primaryPhy;
    private int rssi;
    private ScanRecord scanRecord;
    private int secondaryPhy;
    private long timestampNanos;
    private int txPower;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public ScanResult(BluetoothDevice bluetoothDevice, ScanRecord scanRecord, int i, long j) {
        this.device = bluetoothDevice;
        this.scanRecord = scanRecord;
        this.rssi = i;
        this.timestampNanos = j;
        this.eventType = 17;
        this.primaryPhy = 1;
        this.secondaryPhy = 0;
        this.advertisingSid = 255;
        this.txPower = 127;
        this.periodicAdvertisingInterval = 0;
    }

    public ScanResult(BluetoothDevice bluetoothDevice, int i, int i2, int i3, int i4, int i5, int i6, int i7, ScanRecord scanRecord, long j) {
        this.device = bluetoothDevice;
        this.eventType = i;
        this.primaryPhy = i2;
        this.secondaryPhy = i3;
        this.advertisingSid = i4;
        this.txPower = i5;
        this.rssi = i6;
        this.periodicAdvertisingInterval = i7;
        this.scanRecord = scanRecord;
        this.timestampNanos = j;
    }

    private ScanResult(Parcel parcel) {
        readFromParcel(parcel);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        this.device.writeToParcel(parcel, i);
        if (this.scanRecord != null) {
            parcel.writeInt(1);
            parcel.writeByteArray(this.scanRecord.getBytes());
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.rssi);
        parcel.writeLong(this.timestampNanos);
        parcel.writeInt(this.eventType);
        parcel.writeInt(this.primaryPhy);
        parcel.writeInt(this.secondaryPhy);
        parcel.writeInt(this.advertisingSid);
        parcel.writeInt(this.txPower);
        parcel.writeInt(this.periodicAdvertisingInterval);
    }

    private void readFromParcel(Parcel parcel) {
        this.device = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
        if (parcel.readInt() == 1) {
            this.scanRecord = ScanRecord.parseFromBytes(parcel.createByteArray());
        }
        this.rssi = parcel.readInt();
        this.timestampNanos = parcel.readLong();
        this.eventType = parcel.readInt();
        this.primaryPhy = parcel.readInt();
        this.secondaryPhy = parcel.readInt();
        this.advertisingSid = parcel.readInt();
        this.txPower = parcel.readInt();
        this.periodicAdvertisingInterval = parcel.readInt();
    }

    public BluetoothDevice getDevice() {
        return this.device;
    }

    public ScanRecord getScanRecord() {
        return this.scanRecord;
    }

    public int getRssi() {
        return this.rssi;
    }

    public long getTimestampNanos() {
        return this.timestampNanos;
    }

    public boolean isLegacy() {
        return (this.eventType & 16) != 0;
    }

    public boolean isConnectable() {
        return (this.eventType & 1) != 0;
    }

    public int getDataStatus() {
        return (this.eventType >> 5) & 3;
    }

    public int getPrimaryPhy() {
        return this.primaryPhy;
    }

    public int getSecondaryPhy() {
        return this.secondaryPhy;
    }

    public int getAdvertisingSid() {
        return this.advertisingSid;
    }

    public int getTxPower() {
        return this.txPower;
    }

    public int getPeriodicAdvertisingInterval() {
        return this.periodicAdvertisingInterval;
    }

    public int hashCode() {
        return Objects.hash(this.device, Integer.valueOf(this.rssi), this.scanRecord, Long.valueOf(this.timestampNanos), Integer.valueOf(this.eventType), Integer.valueOf(this.primaryPhy), Integer.valueOf(this.secondaryPhy), Integer.valueOf(this.advertisingSid), Integer.valueOf(this.txPower), Integer.valueOf(this.periodicAdvertisingInterval));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ScanResult scanResult = (ScanResult) obj;
        return Objects.equals(this.device, scanResult.device) && this.rssi == scanResult.rssi && Objects.equals(this.scanRecord, scanResult.scanRecord) && this.timestampNanos == scanResult.timestampNanos && this.eventType == scanResult.eventType && this.primaryPhy == scanResult.primaryPhy && this.secondaryPhy == scanResult.secondaryPhy && this.advertisingSid == scanResult.advertisingSid && this.txPower == scanResult.txPower && this.periodicAdvertisingInterval == scanResult.periodicAdvertisingInterval;
    }

    public String toString() {
        return "ScanResult{device=" + this.device + ", scanRecord=" + Objects.toString(this.scanRecord) + ", rssi=" + this.rssi + ", timestampNanos=" + this.timestampNanos + ", eventType=" + this.eventType + ", primaryPhy=" + this.primaryPhy + ", secondaryPhy=" + this.secondaryPhy + ", advertisingSid=" + this.advertisingSid + ", txPower=" + this.txPower + ", periodicAdvertisingInterval=" + this.periodicAdvertisingInterval + '}';
    }
}
