package no.nordicsemi.android.ble.response;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import no.nordicsemi.android.ble.callback.PhyCallback;

/* loaded from: classes.dex */
public class PhyResult implements PhyCallback, Parcelable {
    public static final Parcelable.Creator<PhyResult> CREATOR = new Parcelable.Creator<PhyResult>() { // from class: no.nordicsemi.android.ble.response.PhyResult.1
        @Override // android.os.Parcelable.Creator
        public PhyResult createFromParcel(Parcel parcel) {
            return new PhyResult(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public PhyResult[] newArray(int i) {
            return new PhyResult[i];
        }
    };
    private BluetoothDevice device;
    private int rxPhy;
    private int txPhy;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // no.nordicsemi.android.ble.callback.PhyCallback
    public void onPhyChanged(BluetoothDevice bluetoothDevice, int i, int i2) {
        this.device = bluetoothDevice;
        this.txPhy = i;
        this.rxPhy = i2;
    }

    public BluetoothDevice getBluetoothDevice() {
        return this.device;
    }

    public int getTxPhy() {
        return this.txPhy;
    }

    public int getRxPhy() {
        return this.rxPhy;
    }

    protected PhyResult(Parcel parcel) {
        this.device = (BluetoothDevice) parcel.readParcelable(BluetoothDevice.class.getClassLoader());
        this.txPhy = parcel.readInt();
        this.rxPhy = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.device, i);
        parcel.writeInt(this.txPhy);
        parcel.writeInt(this.rxPhy);
    }
}
