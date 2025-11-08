package no.nordicsemi.android.ble.common.callback.ht;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Calendar;
import no.nordicsemi.android.ble.common.profile.ht.TemperatureMeasurementCallback;

/* loaded from: classes.dex */
public final class TemperatureMeasurementResponse extends TemperatureMeasurementDataCallback implements Parcelable {
    public static final Parcelable.Creator<TemperatureMeasurementResponse> CREATOR = new Parcelable.Creator<TemperatureMeasurementResponse>() { // from class: no.nordicsemi.android.ble.common.callback.ht.TemperatureMeasurementResponse.1
        @Override // android.os.Parcelable.Creator
        public TemperatureMeasurementResponse createFromParcel(Parcel parcel) {
            return new TemperatureMeasurementResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public TemperatureMeasurementResponse[] newArray(int i) {
            return new TemperatureMeasurementResponse[i];
        }
    };
    private float temperature;
    private Calendar timestamp;
    private Integer type;
    private int unit;

    public TemperatureMeasurementResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.ht.TemperatureMeasurementCallback
    public void onTemperatureMeasurementReceived(BluetoothDevice bluetoothDevice, float f, int i, Calendar calendar, Integer num) {
        this.temperature = f;
        this.unit = i;
        this.timestamp = calendar;
        this.type = num;
    }

    public float getTemperature() {
        return this.temperature;
    }

    public float getTemperatureCelsius() {
        return TemperatureMeasurementCallback.CC.toCelsius(this.temperature, this.unit);
    }

    public float getTemperatureFahrenheit() {
        return TemperatureMeasurementCallback.CC.toFahrenheit(this.temperature, this.unit);
    }

    public int getUnit() {
        return this.unit;
    }

    public Calendar getTimestamp() {
        return this.timestamp;
    }

    public Integer getType() {
        return this.type;
    }

    private TemperatureMeasurementResponse(Parcel parcel) {
        super(parcel);
        this.temperature = parcel.readFloat();
        this.unit = parcel.readInt();
        if (parcel.readByte() == 0) {
            this.timestamp = null;
        } else {
            Calendar calendar = Calendar.getInstance();
            this.timestamp = calendar;
            calendar.setTimeInMillis(parcel.readLong());
        }
        if (parcel.readByte() == 0) {
            this.type = null;
        } else {
            this.type = Integer.valueOf(parcel.readInt());
        }
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeFloat(this.temperature);
        parcel.writeInt(this.unit);
        if (this.timestamp == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(this.timestamp.getTimeInMillis());
        }
        if (this.type == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(this.type.intValue());
        }
    }
}
