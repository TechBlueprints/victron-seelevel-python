package no.nordicsemi.android.ble.common.callback.hr;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class HeartRateMeasurementResponse extends HeartRateMeasurementDataCallback implements Parcelable {
    public static final Parcelable.Creator<HeartRateMeasurementResponse> CREATOR = new Parcelable.Creator<HeartRateMeasurementResponse>() { // from class: no.nordicsemi.android.ble.common.callback.hr.HeartRateMeasurementResponse.1
        @Override // android.os.Parcelable.Creator
        public HeartRateMeasurementResponse createFromParcel(Parcel parcel) {
            return new HeartRateMeasurementResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public HeartRateMeasurementResponse[] newArray(int i) {
            return new HeartRateMeasurementResponse[i];
        }
    };
    private Boolean contactDetected;
    private Integer energyExpanded;
    private int heartRate;
    private List<Integer> rrIntervals;

    public HeartRateMeasurementResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.hr.HeartRateMeasurementCallback
    public void onHeartRateMeasurementReceived(BluetoothDevice bluetoothDevice, int i, Boolean bool, Integer num, List<Integer> list) {
        this.heartRate = i;
        this.contactDetected = bool;
        this.energyExpanded = num;
        this.rrIntervals = list;
    }

    public int getHeartRate() {
        return this.heartRate;
    }

    public Boolean isSensorContactSupported() {
        if (this.heartRate > 0) {
            return Boolean.valueOf(this.contactDetected != null);
        }
        return null;
    }

    public Boolean isSensorContactDetected() {
        return this.contactDetected;
    }

    public Integer getEnergyExpanded() {
        return this.energyExpanded;
    }

    public List<Integer> getRrIntervals() {
        return this.rrIntervals;
    }

    private HeartRateMeasurementResponse(Parcel parcel) {
        Boolean boolValueOf;
        super(parcel);
        this.heartRate = parcel.readInt();
        byte b = parcel.readByte();
        if (b == 0) {
            boolValueOf = null;
        } else {
            boolValueOf = Boolean.valueOf(b == 1);
        }
        this.contactDetected = boolValueOf;
        if (parcel.readByte() == 0) {
            this.energyExpanded = null;
        } else {
            this.energyExpanded = Integer.valueOf(parcel.readInt());
        }
        int i = parcel.readInt();
        if (i == 0) {
            this.rrIntervals = null;
            return;
        }
        ArrayList arrayList = new ArrayList(i);
        parcel.readList(arrayList, Integer.class.getClassLoader());
        this.rrIntervals = Collections.unmodifiableList(arrayList);
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.heartRate);
        Boolean bool = this.contactDetected;
        parcel.writeByte((byte) (bool == null ? 0 : bool.booleanValue() ? 1 : 2));
        if (this.energyExpanded == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(this.energyExpanded.intValue());
        }
        List<Integer> list = this.rrIntervals;
        if (list == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(list.size());
            parcel.writeList(this.rrIntervals);
        }
    }
}
