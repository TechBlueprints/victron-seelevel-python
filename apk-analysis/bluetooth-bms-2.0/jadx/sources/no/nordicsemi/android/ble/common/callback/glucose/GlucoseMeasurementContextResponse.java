package no.nordicsemi.android.ble.common.callback.glucose;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import no.nordicsemi.android.ble.common.profile.glucose.GlucoseMeasurementContextCallback;

/* loaded from: classes.dex */
public final class GlucoseMeasurementContextResponse extends GlucoseMeasurementContextDataCallback implements Parcelable {
    public static final Parcelable.Creator<GlucoseMeasurementContextResponse> CREATOR = new Parcelable.Creator<GlucoseMeasurementContextResponse>() { // from class: no.nordicsemi.android.ble.common.callback.glucose.GlucoseMeasurementContextResponse.1
        @Override // android.os.Parcelable.Creator
        public GlucoseMeasurementContextResponse createFromParcel(Parcel parcel) {
            return new GlucoseMeasurementContextResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GlucoseMeasurementContextResponse[] newArray(int i) {
            return new GlucoseMeasurementContextResponse[i];
        }
    };
    private Float HbA1c;
    private GlucoseMeasurementContextCallback.Carbohydrate carbohydrate;
    private Float carbohydrateAmount;
    private Integer exerciseDuration;
    private Integer exerciseIntensity;
    private GlucoseMeasurementContextCallback.Health health;
    private GlucoseMeasurementContextCallback.Meal meal;
    private GlucoseMeasurementContextCallback.Medication medication;
    private Float medicationAmount;
    private Integer medicationUnit;
    private int sequenceNumber;
    private GlucoseMeasurementContextCallback.Tester tester;

    public GlucoseMeasurementContextResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.glucose.GlucoseMeasurementContextCallback
    public void onGlucoseMeasurementContextReceived(BluetoothDevice bluetoothDevice, int i, GlucoseMeasurementContextCallback.Carbohydrate carbohydrate, Float f, GlucoseMeasurementContextCallback.Meal meal, GlucoseMeasurementContextCallback.Tester tester, GlucoseMeasurementContextCallback.Health health, Integer num, Integer num2, GlucoseMeasurementContextCallback.Medication medication, Float f2, Integer num3, Float f3) {
        this.sequenceNumber = i;
        this.carbohydrate = carbohydrate;
        this.carbohydrateAmount = f;
        this.meal = meal;
        this.tester = tester;
        this.health = health;
        this.exerciseDuration = num;
        this.exerciseIntensity = num2;
        this.medication = medication;
        this.medicationAmount = f2;
        this.medicationUnit = num3;
        this.HbA1c = f3;
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    public GlucoseMeasurementContextCallback.Carbohydrate getCarbohydrate() {
        return this.carbohydrate;
    }

    public Float getCarbohydrateAmount() {
        return this.carbohydrateAmount;
    }

    public GlucoseMeasurementContextCallback.Meal getMeal() {
        return this.meal;
    }

    public GlucoseMeasurementContextCallback.Tester getTester() {
        return this.tester;
    }

    public GlucoseMeasurementContextCallback.Health getHealth() {
        return this.health;
    }

    public Integer getExerciseDuration() {
        return this.exerciseDuration;
    }

    public Integer getExerciseIntensity() {
        return this.exerciseIntensity;
    }

    public GlucoseMeasurementContextCallback.Medication getMedication() {
        return this.medication;
    }

    public Float getMedicationAmount() {
        return this.medicationAmount;
    }

    public Integer getMedicationUnit() {
        return this.medicationUnit;
    }

    public Float getHbA1c() {
        return this.HbA1c;
    }

    private GlucoseMeasurementContextResponse(Parcel parcel) {
        super(parcel);
        this.sequenceNumber = parcel.readInt();
        if (parcel.readByte() == 0) {
            this.carbohydrateAmount = null;
        } else {
            this.carbohydrateAmount = Float.valueOf(parcel.readFloat());
        }
        if (parcel.readByte() == 0) {
            this.exerciseDuration = null;
        } else {
            this.exerciseDuration = Integer.valueOf(parcel.readInt());
        }
        if (parcel.readByte() == 0) {
            this.exerciseIntensity = null;
        } else {
            this.exerciseIntensity = Integer.valueOf(parcel.readInt());
        }
        if (parcel.readByte() == 0) {
            this.medicationAmount = null;
        } else {
            this.medicationAmount = Float.valueOf(parcel.readFloat());
        }
        if (parcel.readByte() == 0) {
            this.medicationUnit = null;
        } else {
            this.medicationUnit = Integer.valueOf(parcel.readInt());
        }
        if (parcel.readByte() == 0) {
            this.HbA1c = null;
        } else {
            this.HbA1c = Float.valueOf(parcel.readFloat());
        }
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.sequenceNumber);
        if (this.carbohydrateAmount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(this.carbohydrateAmount.floatValue());
        }
        if (this.exerciseDuration == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(this.exerciseDuration.intValue());
        }
        if (this.exerciseIntensity == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(this.exerciseIntensity.intValue());
        }
        if (this.medicationAmount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(this.medicationAmount.floatValue());
        }
        if (this.medicationUnit == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(this.medicationUnit.intValue());
        }
        if (this.HbA1c == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(this.HbA1c.floatValue());
        }
    }
}
