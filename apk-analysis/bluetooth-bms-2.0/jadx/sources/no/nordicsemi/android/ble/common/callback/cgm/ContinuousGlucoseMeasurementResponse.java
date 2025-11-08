package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import no.nordicsemi.android.ble.common.profile.cgm.CGMTypes;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public final class ContinuousGlucoseMeasurementResponse extends ContinuousGlucoseMeasurementDataCallback implements CRCSecuredResponse, Parcelable {
    public static final Parcelable.Creator<ContinuousGlucoseMeasurementResponse> CREATOR = new Parcelable.Creator<ContinuousGlucoseMeasurementResponse>() { // from class: no.nordicsemi.android.ble.common.callback.cgm.ContinuousGlucoseMeasurementResponse.1
        @Override // android.os.Parcelable.Creator
        public ContinuousGlucoseMeasurementResponse createFromParcel(Parcel parcel) {
            return new ContinuousGlucoseMeasurementResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public ContinuousGlucoseMeasurementResponse[] newArray(int i) {
            return new ContinuousGlucoseMeasurementResponse[i];
        }
    };
    private boolean crcValid;
    private float glucoseConcentration;
    private Float quality;
    private boolean secured;
    private CGMTypes.CGMStatus status;
    private int timeOffset;
    private Float trend;

    public ContinuousGlucoseMeasurementResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.cgm.ContinuousGlucoseMeasurementCallback
    public void onContinuousGlucoseMeasurementReceived(BluetoothDevice bluetoothDevice, float f, Float f2, Float f3, CGMTypes.CGMStatus cGMStatus, int i, boolean z) {
        this.glucoseConcentration = f;
        this.trend = f2;
        this.quality = f3;
        this.status = cGMStatus;
        this.timeOffset = i;
        this.secured = z;
        this.crcValid = z;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.ContinuousGlucoseMeasurementDataCallback, no.nordicsemi.android.ble.common.profile.cgm.ContinuousGlucoseMeasurementCallback
    public void onContinuousGlucoseMeasurementReceivedWithCrcError(BluetoothDevice bluetoothDevice, Data data) {
        onInvalidDataReceived(bluetoothDevice, data);
        this.secured = true;
        this.crcValid = false;
    }

    public float getGlucoseConcentration() {
        return this.glucoseConcentration;
    }

    public Float getTrend() {
        return this.trend;
    }

    public Float getQuality() {
        return this.quality;
    }

    public CGMTypes.CGMStatus getStatus() {
        return this.status;
    }

    public int getTimeOffset() {
        return this.timeOffset;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CRCSecuredResponse
    public boolean isSecured() {
        return this.secured;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CRCSecuredResponse
    public boolean isCrcValid() {
        return this.crcValid;
    }

    private ContinuousGlucoseMeasurementResponse(Parcel parcel) {
        super(parcel);
        this.glucoseConcentration = parcel.readFloat();
        if (parcel.readByte() == 0) {
            this.trend = null;
        } else {
            this.trend = Float.valueOf(parcel.readFloat());
        }
        if (parcel.readByte() == 0) {
            this.quality = null;
        } else {
            this.quality = Float.valueOf(parcel.readFloat());
        }
        if (parcel.readByte() == 0) {
            this.status = null;
        } else {
            this.status = new CGMTypes.CGMStatus(parcel.readInt(), parcel.readInt(), parcel.readInt());
        }
        this.timeOffset = parcel.readInt();
        this.secured = parcel.readByte() != 0;
        this.crcValid = parcel.readByte() != 0;
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeFloat(this.glucoseConcentration);
        if (this.trend == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(this.trend.floatValue());
        }
        if (this.quality == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(this.quality.floatValue());
        }
        if (this.status == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(this.status.warningStatus);
            parcel.writeInt(this.status.calibrationTempStatus);
            parcel.writeInt(this.status.sensorStatus);
        }
        parcel.writeInt(this.timeOffset);
        parcel.writeByte(this.secured ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.crcValid ? (byte) 1 : (byte) 0);
    }
}
