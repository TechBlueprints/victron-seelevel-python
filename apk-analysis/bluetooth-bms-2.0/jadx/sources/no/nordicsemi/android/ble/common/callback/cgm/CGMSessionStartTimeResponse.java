package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Calendar;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public final class CGMSessionStartTimeResponse extends CGMSessionStartTimeDataCallback implements CRCSecuredResponse, Parcelable {
    public static final Parcelable.Creator<CGMSessionStartTimeResponse> CREATOR = new Parcelable.Creator<CGMSessionStartTimeResponse>() { // from class: no.nordicsemi.android.ble.common.callback.cgm.CGMSessionStartTimeResponse.1
        @Override // android.os.Parcelable.Creator
        public CGMSessionStartTimeResponse createFromParcel(Parcel parcel) {
            return new CGMSessionStartTimeResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public CGMSessionStartTimeResponse[] newArray(int i) {
            return new CGMSessionStartTimeResponse[i];
        }
    };
    private boolean crcValid;
    private boolean secured;
    private Calendar startTime;

    public CGMSessionStartTimeResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.cgm.CGMSessionStartTimeCallback
    public void onContinuousGlucoseMonitorSessionStartTimeReceived(BluetoothDevice bluetoothDevice, Calendar calendar, boolean z) {
        this.startTime = calendar;
        this.secured = z;
        this.crcValid = z;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CGMSessionStartTimeDataCallback, no.nordicsemi.android.ble.common.profile.cgm.CGMSessionStartTimeCallback
    public void onContinuousGlucoseMonitorSessionStartTimeReceivedWithCrcError(BluetoothDevice bluetoothDevice, Data data) {
        onInvalidDataReceived(bluetoothDevice, data);
        this.secured = true;
        this.crcValid = false;
    }

    public Calendar getStartTime() {
        return this.startTime;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CRCSecuredResponse
    public boolean isSecured() {
        return this.secured;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CRCSecuredResponse
    public boolean isCrcValid() {
        return this.crcValid;
    }

    private CGMSessionStartTimeResponse(Parcel parcel) {
        super(parcel);
        if (parcel.readByte() == 0) {
            this.startTime = null;
        } else {
            Calendar calendar = Calendar.getInstance();
            this.startTime = calendar;
            calendar.setTimeInMillis(parcel.readLong());
        }
        this.secured = parcel.readByte() != 0;
        this.crcValid = parcel.readByte() != 0;
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        if (this.startTime == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(this.startTime.getTimeInMillis());
        }
        parcel.writeByte(this.secured ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.crcValid ? (byte) 1 : (byte) 0);
    }
}
