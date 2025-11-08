package no.nordicsemi.android.ble.common.callback.glucose;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Calendar;
import no.nordicsemi.android.ble.common.profile.glucose.GlucoseMeasurementCallback;

/* loaded from: classes.dex */
public final class GlucoseMeasurementResponse extends GlucoseMeasurementDataCallback implements Parcelable {
    public static final Parcelable.Creator<GlucoseMeasurementResponse> CREATOR = new Parcelable.Creator<GlucoseMeasurementResponse>() { // from class: no.nordicsemi.android.ble.common.callback.glucose.GlucoseMeasurementResponse.1
        @Override // android.os.Parcelable.Creator
        public GlucoseMeasurementResponse createFromParcel(Parcel parcel) {
            return new GlucoseMeasurementResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GlucoseMeasurementResponse[] newArray(int i) {
            return new GlucoseMeasurementResponse[i];
        }
    };
    private boolean contextInformationFollows;
    private Float glucoseConcentration;
    private Integer sampleLocation;
    private int sequenceNumber;
    private GlucoseMeasurementCallback.GlucoseStatus status;
    private Calendar time;
    private Integer type;
    private Integer unit;

    @Override // no.nordicsemi.android.ble.common.profile.glucose.GlucoseMeasurementCallback
    public void onGlucoseMeasurementReceived(BluetoothDevice bluetoothDevice, int i, Calendar calendar, Float f, Integer num, Integer num2, Integer num3, GlucoseMeasurementCallback.GlucoseStatus glucoseStatus, boolean z) {
        this.sequenceNumber = i;
        this.time = calendar;
        this.glucoseConcentration = f;
        this.unit = num;
        this.type = num2;
        this.sampleLocation = num3;
        this.status = glucoseStatus;
        this.contextInformationFollows = z;
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    public Calendar getTime() {
        return this.time;
    }

    public Float getGlucoseConcentration() {
        return this.glucoseConcentration;
    }

    public Integer getUnit() {
        return this.unit;
    }

    public Integer getType() {
        return this.type;
    }

    public Integer getSampleLocation() {
        return this.sampleLocation;
    }

    public GlucoseMeasurementCallback.GlucoseStatus getStatus() {
        return this.status;
    }

    public boolean contextInformationFollows() {
        return this.contextInformationFollows;
    }

    private GlucoseMeasurementResponse(Parcel parcel) {
        super(parcel);
        this.sequenceNumber = parcel.readInt();
        if (parcel.readByte() == 0) {
            this.time = null;
        } else {
            Calendar calendar = Calendar.getInstance();
            this.time = calendar;
            calendar.setTimeInMillis(parcel.readLong());
        }
        if (parcel.readByte() == 0) {
            this.glucoseConcentration = null;
        } else {
            this.glucoseConcentration = Float.valueOf(parcel.readFloat());
        }
        if (parcel.readByte() == 0) {
            this.unit = null;
        } else {
            this.unit = Integer.valueOf(parcel.readInt());
        }
        if (parcel.readByte() == 0) {
            this.type = null;
        } else {
            this.type = Integer.valueOf(parcel.readInt());
        }
        if (parcel.readByte() == 0) {
            this.sampleLocation = null;
        } else {
            this.sampleLocation = Integer.valueOf(parcel.readInt());
        }
        if (parcel.readByte() == 0) {
            this.status = null;
        } else {
            this.status = new GlucoseMeasurementCallback.GlucoseStatus(parcel.readInt());
        }
        this.contextInformationFollows = parcel.readByte() != 0;
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.sequenceNumber);
        if (this.time == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(this.time.getTimeInMillis());
        }
        if (this.glucoseConcentration == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(this.glucoseConcentration.floatValue());
        }
        if (this.unit == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(this.unit.intValue());
        }
        if (this.type == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(this.type.intValue());
        }
        if (this.sampleLocation == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(this.sampleLocation.intValue());
        }
        super.writeToParcel(parcel, i);
        if (this.status == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(this.status.value);
        }
        parcel.writeByte(this.contextInformationFollows ? (byte) 1 : (byte) 0);
    }
}
