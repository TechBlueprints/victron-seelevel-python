package no.nordicsemi.android.ble.common.callback.bps;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Calendar;
import no.nordicsemi.android.ble.common.profile.bp.BloodPressureTypes;

/* loaded from: classes.dex */
public final class IntermediateCuffPressureResponse extends IntermediateCuffPressureDataCallback implements Parcelable {
    public static final Parcelable.Creator<IntermediateCuffPressureResponse> CREATOR = new Parcelable.Creator<IntermediateCuffPressureResponse>() { // from class: no.nordicsemi.android.ble.common.callback.bps.IntermediateCuffPressureResponse.1
        @Override // android.os.Parcelable.Creator
        public IntermediateCuffPressureResponse createFromParcel(Parcel parcel) {
            return new IntermediateCuffPressureResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public IntermediateCuffPressureResponse[] newArray(int i) {
            return new IntermediateCuffPressureResponse[i];
        }
    };
    private Calendar calendar;
    private float cuffPressure;
    private Float pulseRate;
    private BloodPressureTypes.BPMStatus status;
    private int unit;
    private Integer userID;

    public IntermediateCuffPressureResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.bp.IntermediateCuffPressureCallback
    public void onIntermediateCuffPressureReceived(BluetoothDevice bluetoothDevice, float f, int i, Float f2, Integer num, BloodPressureTypes.BPMStatus bPMStatus, Calendar calendar) {
        this.cuffPressure = f;
        this.unit = i;
        this.pulseRate = f2;
        this.userID = num;
        this.status = bPMStatus;
        this.calendar = calendar;
    }

    public float getCuffPressure() {
        return this.cuffPressure;
    }

    public int getUnit() {
        return this.unit;
    }

    public Float getPulseRate() {
        return this.pulseRate;
    }

    public Integer getUserID() {
        return this.userID;
    }

    public BloodPressureTypes.BPMStatus getStatus() {
        return this.status;
    }

    public Calendar getTimestamp() {
        return this.calendar;
    }

    private IntermediateCuffPressureResponse(Parcel parcel) {
        super(parcel);
        this.cuffPressure = parcel.readFloat();
        this.unit = parcel.readInt();
        if (parcel.readByte() == 0) {
            this.pulseRate = null;
        } else {
            this.pulseRate = Float.valueOf(parcel.readFloat());
        }
        if (parcel.readByte() == 0) {
            this.userID = null;
        } else {
            this.userID = Integer.valueOf(parcel.readInt());
        }
        if (parcel.readByte() == 0) {
            this.status = null;
        } else {
            this.status = new BloodPressureTypes.BPMStatus(parcel.readInt());
        }
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
        parcel.writeFloat(this.cuffPressure);
        parcel.writeInt(this.unit);
        if (this.pulseRate == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(this.pulseRate.floatValue());
        }
        if (this.userID == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(this.userID.intValue());
        }
        if (this.status == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(this.status.value);
        }
        if (this.calendar == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(this.calendar.getTimeInMillis());
        }
    }
}
