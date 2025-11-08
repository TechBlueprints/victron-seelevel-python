package no.nordicsemi.android.ble.common.callback.bps;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Calendar;
import no.nordicsemi.android.ble.common.profile.bp.BloodPressureTypes;

/* loaded from: classes.dex */
public final class BloodPressureMeasurementResponse extends BloodPressureMeasurementDataCallback implements Parcelable {
    public static final Parcelable.Creator<BloodPressureMeasurementResponse> CREATOR = new Parcelable.Creator<BloodPressureMeasurementResponse>() { // from class: no.nordicsemi.android.ble.common.callback.bps.BloodPressureMeasurementResponse.1
        @Override // android.os.Parcelable.Creator
        public BloodPressureMeasurementResponse createFromParcel(Parcel parcel) {
            return new BloodPressureMeasurementResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public BloodPressureMeasurementResponse[] newArray(int i) {
            return new BloodPressureMeasurementResponse[i];
        }
    };
    private Calendar calendar;
    private float diastolic;
    private float meanArterialPressure;
    private Float pulseRate;
    private BloodPressureTypes.BPMStatus status;
    private float systolic;
    private int unit;
    private Integer userID;

    public BloodPressureMeasurementResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.bp.BloodPressureMeasurementCallback
    public void onBloodPressureMeasurementReceived(BluetoothDevice bluetoothDevice, float f, float f2, float f3, int i, Float f4, Integer num, BloodPressureTypes.BPMStatus bPMStatus, Calendar calendar) {
        this.systolic = f;
        this.diastolic = f2;
        this.meanArterialPressure = f3;
        this.unit = i;
        this.pulseRate = f4;
        this.userID = num;
        this.status = bPMStatus;
        this.calendar = calendar;
    }

    public float getSystolic() {
        return this.systolic;
    }

    public float getDiastolic() {
        return this.diastolic;
    }

    public float getMeanArterialPressure() {
        return this.meanArterialPressure;
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

    private BloodPressureMeasurementResponse(Parcel parcel) {
        super(parcel);
        this.systolic = parcel.readFloat();
        this.diastolic = parcel.readFloat();
        this.meanArterialPressure = parcel.readFloat();
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
        parcel.writeFloat(this.systolic);
        parcel.writeFloat(this.diastolic);
        parcel.writeFloat(this.meanArterialPressure);
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
