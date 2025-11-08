package no.nordicsemi.android.ble.common.callback.csc;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class CyclingSpeedAndCadenceMeasurementResponse extends CyclingSpeedAndCadenceMeasurementDataCallback implements Parcelable {
    public static final Parcelable.Creator<CyclingSpeedAndCadenceMeasurementResponse> CREATOR = new Parcelable.Creator<CyclingSpeedAndCadenceMeasurementResponse>() { // from class: no.nordicsemi.android.ble.common.callback.csc.CyclingSpeedAndCadenceMeasurementResponse.1
        @Override // android.os.Parcelable.Creator
        public CyclingSpeedAndCadenceMeasurementResponse createFromParcel(Parcel parcel) {
            return new CyclingSpeedAndCadenceMeasurementResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public CyclingSpeedAndCadenceMeasurementResponse[] newArray(int i) {
            return new CyclingSpeedAndCadenceMeasurementResponse[i];
        }
    };
    private long crankRevolutions;
    private int lastCrankEventTime;
    private int lastWheelEventTime;
    private long wheelRevolutions;

    @Override // no.nordicsemi.android.ble.common.profile.csc.CyclingSpeedAndCadenceCallback
    public void onCrankDataChanged(BluetoothDevice bluetoothDevice, float f, float f2) {
    }

    @Override // no.nordicsemi.android.ble.common.profile.csc.CyclingSpeedAndCadenceCallback
    public void onDistanceChanged(BluetoothDevice bluetoothDevice, float f, float f2, float f3) {
    }

    public CyclingSpeedAndCadenceMeasurementResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.callback.csc.CyclingSpeedAndCadenceMeasurementDataCallback, no.nordicsemi.android.ble.common.profile.csc.CyclingSpeedAndCadenceMeasurementCallback
    public void onWheelMeasurementReceived(BluetoothDevice bluetoothDevice, long j, int i) {
        this.wheelRevolutions = j;
        this.lastWheelEventTime = i;
    }

    @Override // no.nordicsemi.android.ble.common.callback.csc.CyclingSpeedAndCadenceMeasurementDataCallback, no.nordicsemi.android.ble.common.profile.csc.CyclingSpeedAndCadenceMeasurementCallback
    public void onCrankMeasurementReceived(BluetoothDevice bluetoothDevice, int i, int i2) {
        this.crankRevolutions = i;
        this.lastCrankEventTime = i2;
    }

    public long getWheelRevolutions() {
        return this.wheelRevolutions;
    }

    public long getCrankRevolutions() {
        return this.crankRevolutions;
    }

    public long getLastWheelEventTime() {
        return this.lastWheelEventTime;
    }

    public long getLastCrankEventTime() {
        return this.lastCrankEventTime;
    }

    public float getTotalDistance(float f) {
        return (this.wheelRevolutions * f) / 1000.0f;
    }

    public float getDistance(float f, CyclingSpeedAndCadenceMeasurementResponse cyclingSpeedAndCadenceMeasurementResponse) {
        return ((this.wheelRevolutions - cyclingSpeedAndCadenceMeasurementResponse.wheelRevolutions) * f) / 1000.0f;
    }

    public float getSpeed(float f, CyclingSpeedAndCadenceMeasurementResponse cyclingSpeedAndCadenceMeasurementResponse) {
        int i = this.lastWheelEventTime;
        if (i < cyclingSpeedAndCadenceMeasurementResponse.lastWheelEventTime) {
            i += 65535;
        }
        return getDistance(f, cyclingSpeedAndCadenceMeasurementResponse) / ((i - r1) / 1024.0f);
    }

    public float getWheelCadence(CyclingSpeedAndCadenceMeasurementResponse cyclingSpeedAndCadenceMeasurementResponse) {
        int i = this.lastWheelEventTime;
        if (i < cyclingSpeedAndCadenceMeasurementResponse.lastWheelEventTime) {
            i += 65535;
        }
        float f = (i - r1) / 1024.0f;
        if (f == 0.0f) {
            return 0.0f;
        }
        return ((this.wheelRevolutions - cyclingSpeedAndCadenceMeasurementResponse.wheelRevolutions) * 60.0f) / f;
    }

    public float getCrankCadence(CyclingSpeedAndCadenceMeasurementResponse cyclingSpeedAndCadenceMeasurementResponse) {
        int i = this.lastCrankEventTime;
        if (i < cyclingSpeedAndCadenceMeasurementResponse.lastCrankEventTime) {
            i += 65535;
        }
        float f = (i - r1) / 1024.0f;
        if (f == 0.0f) {
            return 0.0f;
        }
        return ((this.crankRevolutions - cyclingSpeedAndCadenceMeasurementResponse.crankRevolutions) * 60.0f) / f;
    }

    public float getGearRatio(CyclingSpeedAndCadenceMeasurementResponse cyclingSpeedAndCadenceMeasurementResponse) {
        float crankCadence = getCrankCadence(cyclingSpeedAndCadenceMeasurementResponse);
        if (crankCadence > 0.0f) {
            return getWheelCadence(cyclingSpeedAndCadenceMeasurementResponse) / crankCadence;
        }
        return 0.0f;
    }

    private CyclingSpeedAndCadenceMeasurementResponse(Parcel parcel) {
        super(parcel);
        this.wheelRevolutions = parcel.readLong();
        this.crankRevolutions = parcel.readLong();
        this.lastWheelEventTime = parcel.readInt();
        this.lastCrankEventTime = parcel.readInt();
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeLong(this.wheelRevolutions);
        parcel.writeLong(this.crankRevolutions);
        parcel.writeInt(this.lastWheelEventTime);
        parcel.writeInt(this.lastCrankEventTime);
    }
}
