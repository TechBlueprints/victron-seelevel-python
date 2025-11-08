package no.nordicsemi.android.ble.common.callback.sc;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class SensorLocationResponse extends SensorLocationDataCallback implements Parcelable {
    public static final Parcelable.Creator<SensorLocationResponse> CREATOR = new Parcelable.Creator<SensorLocationResponse>() { // from class: no.nordicsemi.android.ble.common.callback.sc.SensorLocationResponse.1
        @Override // android.os.Parcelable.Creator
        public SensorLocationResponse createFromParcel(Parcel parcel) {
            return new SensorLocationResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SensorLocationResponse[] newArray(int i) {
            return new SensorLocationResponse[i];
        }
    };
    private int location;

    public SensorLocationResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.sc.SensorLocationCallback
    public void onSensorLocationReceived(BluetoothDevice bluetoothDevice, int i) {
        this.location = i;
    }

    public int getLocation() {
        return this.location;
    }

    private SensorLocationResponse(Parcel parcel) {
        super(parcel);
        this.location = parcel.readInt();
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.location);
    }
}
