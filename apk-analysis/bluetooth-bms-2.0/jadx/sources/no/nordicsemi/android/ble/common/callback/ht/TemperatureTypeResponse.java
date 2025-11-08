package no.nordicsemi.android.ble.common.callback.ht;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class TemperatureTypeResponse extends TemperatureTypeDataCallback implements Parcelable {
    public static final Parcelable.Creator<TemperatureTypeResponse> CREATOR = new Parcelable.Creator<TemperatureTypeResponse>() { // from class: no.nordicsemi.android.ble.common.callback.ht.TemperatureTypeResponse.1
        @Override // android.os.Parcelable.Creator
        public TemperatureTypeResponse createFromParcel(Parcel parcel) {
            return new TemperatureTypeResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public TemperatureTypeResponse[] newArray(int i) {
            return new TemperatureTypeResponse[i];
        }
    };
    private int type;

    public TemperatureTypeResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.ht.TemperatureTypeCallback
    public void onTemperatureTypeReceived(BluetoothDevice bluetoothDevice, int i) {
        this.type = i;
    }

    public int getType() {
        return this.type;
    }

    private TemperatureTypeResponse(Parcel parcel) {
        super(parcel);
        this.type = parcel.readInt();
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.type);
    }
}
