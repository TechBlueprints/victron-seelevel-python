package no.nordicsemi.android.ble.common.callback.hr;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class BodySensorLocationResponse extends BodySensorLocationDataCallback implements Parcelable {
    public static final Parcelable.Creator<BodySensorLocationResponse> CREATOR = new Parcelable.Creator<BodySensorLocationResponse>() { // from class: no.nordicsemi.android.ble.common.callback.hr.BodySensorLocationResponse.1
        @Override // android.os.Parcelable.Creator
        public BodySensorLocationResponse createFromParcel(Parcel parcel) {
            return new BodySensorLocationResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public BodySensorLocationResponse[] newArray(int i) {
            return new BodySensorLocationResponse[i];
        }
    };
    private int sensorLocation;

    public BodySensorLocationResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.hr.BodySensorLocationCallback
    public void onBodySensorLocationReceived(BluetoothDevice bluetoothDevice, int i) {
        this.sensorLocation = i;
    }

    public int getSensorLocation() {
        return this.sensorLocation;
    }

    private BodySensorLocationResponse(Parcel parcel) {
        super(parcel);
        this.sensorLocation = parcel.readInt();
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.sensorLocation);
    }
}
