package no.nordicsemi.android.ble.response;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import no.nordicsemi.android.ble.callback.DataSentCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public class WriteResponse implements DataSentCallback, Parcelable {
    public static final Parcelable.Creator<WriteResponse> CREATOR = new Parcelable.Creator<WriteResponse>() { // from class: no.nordicsemi.android.ble.response.WriteResponse.1
        @Override // android.os.Parcelable.Creator
        public WriteResponse createFromParcel(Parcel parcel) {
            return new WriteResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public WriteResponse[] newArray(int i) {
            return new WriteResponse[i];
        }
    };
    private Data data;
    private BluetoothDevice device;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // no.nordicsemi.android.ble.callback.DataSentCallback
    public void onDataSent(BluetoothDevice bluetoothDevice, Data data) {
        this.device = bluetoothDevice;
        this.data = data;
    }

    public BluetoothDevice getBluetoothDevice() {
        return this.device;
    }

    public Data getRawData() {
        return this.data;
    }

    protected WriteResponse(Parcel parcel) {
        this.device = (BluetoothDevice) parcel.readParcelable(BluetoothDevice.class.getClassLoader());
        this.data = (Data) parcel.readParcelable(Data.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.device, i);
        parcel.writeParcelable(this.data, i);
    }
}
