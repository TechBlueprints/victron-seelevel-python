package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class TimeZoneResponse extends TimeZoneDataCallback implements Parcelable {
    public static final Parcelable.Creator<TimeZoneResponse> CREATOR = new Parcelable.Creator<TimeZoneResponse>() { // from class: no.nordicsemi.android.ble.common.callback.TimeZoneResponse.1
        @Override // android.os.Parcelable.Creator
        public TimeZoneResponse createFromParcel(Parcel parcel) {
            return new TimeZoneResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public TimeZoneResponse[] newArray(int i) {
            return new TimeZoneResponse[i];
        }
    };
    private int timeZoneOffset;
    private boolean timeZoneOffsetKnown;

    public TimeZoneResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.TimeZoneCallback
    public void onTimeZoneReceived(BluetoothDevice bluetoothDevice, int i) {
        this.timeZoneOffset = i;
        this.timeZoneOffsetKnown = true;
    }

    @Override // no.nordicsemi.android.ble.common.profile.TimeZoneCallback
    public void onUnknownTimeZoneReceived(BluetoothDevice bluetoothDevice) {
        this.timeZoneOffsetKnown = false;
    }

    public int getTimeZoneOffset() {
        return this.timeZoneOffset;
    }

    public boolean isTimeZoneOffsetKnown() {
        return this.timeZoneOffsetKnown;
    }

    private TimeZoneResponse(Parcel parcel) {
        super(parcel);
        this.timeZoneOffset = parcel.readInt();
        this.timeZoneOffsetKnown = parcel.readByte() != 0;
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.timeZoneOffset);
        parcel.writeByte(this.timeZoneOffsetKnown ? (byte) 1 : (byte) 0);
    }
}
