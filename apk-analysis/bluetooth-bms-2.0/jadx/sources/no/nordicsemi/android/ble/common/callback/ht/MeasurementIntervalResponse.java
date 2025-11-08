package no.nordicsemi.android.ble.common.callback.ht;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class MeasurementIntervalResponse extends MeasurementIntervalDataCallback implements Parcelable {
    public static final Parcelable.Creator<MeasurementIntervalResponse> CREATOR = new Parcelable.Creator<MeasurementIntervalResponse>() { // from class: no.nordicsemi.android.ble.common.callback.ht.MeasurementIntervalResponse.1
        @Override // android.os.Parcelable.Creator
        public MeasurementIntervalResponse createFromParcel(Parcel parcel) {
            return new MeasurementIntervalResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public MeasurementIntervalResponse[] newArray(int i) {
            return new MeasurementIntervalResponse[i];
        }
    };
    private int interval;

    public MeasurementIntervalResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.ht.MeasurementIntervalCallback
    public void onMeasurementIntervalReceived(BluetoothDevice bluetoothDevice, int i) {
        this.interval = i;
    }

    public int getInterval() {
        return this.interval;
    }

    private MeasurementIntervalResponse(Parcel parcel) {
        super(parcel);
        this.interval = parcel.readInt();
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.interval);
    }
}
