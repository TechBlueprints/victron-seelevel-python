package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Calendar;

/* loaded from: classes.dex */
public final class DateTimeResponse extends DateTimeDataCallback implements Parcelable {
    public static final Parcelable.Creator<DateTimeResponse> CREATOR = new Parcelable.Creator<DateTimeResponse>() { // from class: no.nordicsemi.android.ble.common.callback.DateTimeResponse.1
        @Override // android.os.Parcelable.Creator
        public DateTimeResponse createFromParcel(Parcel parcel) {
            return new DateTimeResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public DateTimeResponse[] newArray(int i) {
            return new DateTimeResponse[i];
        }
    };
    private Calendar calendar;

    public DateTimeResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.DateTimeCallback
    public void onDateTimeReceived(BluetoothDevice bluetoothDevice, Calendar calendar) {
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    private DateTimeResponse(Parcel parcel) {
        super(parcel);
        if (parcel.readByte() == 0) {
            this.calendar = null;
            return;
        }
        Calendar calendar = Calendar.getInstance();
        this.calendar = calendar;
        calendar.setTimeInMillis(parcel.readLong());
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        if (this.calendar == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(this.calendar.getTimeInMillis());
        }
    }
}
