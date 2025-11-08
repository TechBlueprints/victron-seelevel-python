package no.nordicsemi.android.ble.common.callback.rsc;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class RunningSpeedAndCadenceMeasurementResponse extends RunningSpeedAndCadenceMeasurementDataCallback implements Parcelable {
    public static final Parcelable.Creator<RunningSpeedAndCadenceMeasurementResponse> CREATOR = new Parcelable.Creator<RunningSpeedAndCadenceMeasurementResponse>() { // from class: no.nordicsemi.android.ble.common.callback.rsc.RunningSpeedAndCadenceMeasurementResponse.1
        @Override // android.os.Parcelable.Creator
        public RunningSpeedAndCadenceMeasurementResponse createFromParcel(Parcel parcel) {
            return new RunningSpeedAndCadenceMeasurementResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public RunningSpeedAndCadenceMeasurementResponse[] newArray(int i) {
            return new RunningSpeedAndCadenceMeasurementResponse[i];
        }
    };
    private int instantaneousCadence;
    private float instantaneousSpeed;
    private boolean running;
    private Integer strideLength;
    private Long totalDistance;

    @Override // no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public RunningSpeedAndCadenceMeasurementResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.rsc.RunningSpeedAndCadenceMeasurementCallback
    public void onRSCMeasurementReceived(BluetoothDevice bluetoothDevice, boolean z, float f, int i, Integer num, Long l) {
        this.running = z;
        this.instantaneousSpeed = f;
        this.instantaneousCadence = i;
        this.strideLength = num;
        this.totalDistance = l;
    }

    public boolean isRunning() {
        return this.running;
    }

    public float getInstantaneousSpeed() {
        return this.instantaneousSpeed;
    }

    public int getInstantaneousCadence() {
        return this.instantaneousCadence;
    }

    public Integer getStrideLength() {
        return this.strideLength;
    }

    public Long getTotalDistance() {
        return this.totalDistance;
    }

    private RunningSpeedAndCadenceMeasurementResponse(Parcel parcel) {
        super(parcel);
        this.running = parcel.readByte() != 0;
        this.instantaneousSpeed = parcel.readFloat();
        this.instantaneousCadence = parcel.readInt();
        if (parcel.readByte() == 0) {
            this.strideLength = null;
        } else {
            this.strideLength = Integer.valueOf(parcel.readInt());
        }
        if (parcel.readByte() == 0) {
            this.totalDistance = null;
        } else {
            this.totalDistance = Long.valueOf(parcel.readLong());
        }
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeByte(this.running ? (byte) 1 : (byte) 0);
        parcel.writeFloat(this.instantaneousSpeed);
        parcel.writeInt(this.instantaneousCadence);
        if (this.strideLength == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(this.strideLength.intValue());
        }
        if (this.totalDistance == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(this.totalDistance.longValue());
        }
    }
}
