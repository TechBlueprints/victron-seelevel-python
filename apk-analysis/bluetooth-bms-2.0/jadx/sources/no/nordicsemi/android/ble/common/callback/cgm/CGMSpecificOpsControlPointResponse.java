package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import no.nordicsemi.android.ble.common.profile.cgm.CGMTypes;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public final class CGMSpecificOpsControlPointResponse extends CGMSpecificOpsControlPointDataCallback implements CRCSecuredResponse, Parcelable {
    public static final Parcelable.Creator<CGMSpecificOpsControlPointResponse> CREATOR = new Parcelable.Creator<CGMSpecificOpsControlPointResponse>() { // from class: no.nordicsemi.android.ble.common.callback.cgm.CGMSpecificOpsControlPointResponse.1
        @Override // android.os.Parcelable.Creator
        public CGMSpecificOpsControlPointResponse createFromParcel(Parcel parcel) {
            return new CGMSpecificOpsControlPointResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public CGMSpecificOpsControlPointResponse[] newArray(int i) {
            return new CGMSpecificOpsControlPointResponse[i];
        }
    };
    private float alertLevel;
    private int calibrationDataRecordNumber;
    private CGMTypes.CGMCalibrationStatus calibrationStatus;
    private int calibrationTime;
    private boolean crcValid;
    private int errorCode;
    private int glucoseCommunicationInterval;
    private float glucoseConcentrationOfCalibration;
    private int nextCalibrationTime;
    private boolean operationCompleted;
    private int requestCode;
    private int sampleLocation;
    private boolean secured;
    private int type;

    public CGMSpecificOpsControlPointResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.cgm.CGMSpecificOpsControlPointCallback
    public void onCGMSpecificOpsOperationCompleted(BluetoothDevice bluetoothDevice, int i, boolean z) {
        this.operationCompleted = true;
        this.requestCode = i;
        this.secured = z;
        this.crcValid = z;
    }

    @Override // no.nordicsemi.android.ble.common.profile.cgm.CGMSpecificOpsControlPointCallback
    public void onCGMSpecificOpsOperationError(BluetoothDevice bluetoothDevice, int i, int i2, boolean z) {
        this.operationCompleted = false;
        this.requestCode = i;
        this.errorCode = i2;
        this.secured = z;
        this.crcValid = z;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CGMSpecificOpsControlPointDataCallback, no.nordicsemi.android.ble.common.profile.cgm.CGMSpecificOpsControlPointCallback
    public void onCGMSpecificOpsResponseReceivedWithCrcError(BluetoothDevice bluetoothDevice, Data data) {
        onInvalidDataReceived(bluetoothDevice, data);
        this.operationCompleted = false;
        this.secured = true;
        this.crcValid = false;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CGMSpecificOpsControlPointDataCallback, no.nordicsemi.android.ble.common.profile.cgm.CGMSpecificOpsControlPointCallback
    public void onContinuousGlucoseCommunicationIntervalReceived(BluetoothDevice bluetoothDevice, int i, boolean z) {
        this.operationCompleted = true;
        this.requestCode = 1;
        this.glucoseCommunicationInterval = i;
        this.secured = z;
        this.crcValid = z;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CGMSpecificOpsControlPointDataCallback, no.nordicsemi.android.ble.common.profile.cgm.CGMSpecificOpsControlPointCallback
    public void onContinuousGlucoseCalibrationValueReceived(BluetoothDevice bluetoothDevice, float f, int i, int i2, int i3, int i4, int i5, CGMTypes.CGMCalibrationStatus cGMCalibrationStatus, boolean z) {
        this.operationCompleted = true;
        this.requestCode = 4;
        this.glucoseConcentrationOfCalibration = f;
        this.calibrationTime = i;
        this.nextCalibrationTime = i2;
        this.type = i3;
        this.sampleLocation = i4;
        this.calibrationDataRecordNumber = i5;
        this.calibrationStatus = cGMCalibrationStatus;
        this.secured = z;
        this.crcValid = z;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CGMSpecificOpsControlPointDataCallback, no.nordicsemi.android.ble.common.profile.cgm.CGMSpecificOpsControlPointCallback
    public void onContinuousGlucosePatientHighAlertReceived(BluetoothDevice bluetoothDevice, float f, boolean z) {
        this.operationCompleted = true;
        this.requestCode = 7;
        this.alertLevel = f;
        this.secured = z;
        this.crcValid = z;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CGMSpecificOpsControlPointDataCallback, no.nordicsemi.android.ble.common.profile.cgm.CGMSpecificOpsControlPointCallback
    public void onContinuousGlucosePatientLowAlertReceived(BluetoothDevice bluetoothDevice, float f, boolean z) {
        this.operationCompleted = true;
        this.requestCode = 10;
        this.alertLevel = f;
        this.secured = z;
        this.crcValid = z;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CGMSpecificOpsControlPointDataCallback, no.nordicsemi.android.ble.common.profile.cgm.CGMSpecificOpsControlPointCallback
    public void onContinuousGlucoseHypoAlertReceived(BluetoothDevice bluetoothDevice, float f, boolean z) {
        this.operationCompleted = true;
        this.requestCode = 13;
        this.alertLevel = f;
        this.secured = z;
        this.crcValid = z;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CGMSpecificOpsControlPointDataCallback, no.nordicsemi.android.ble.common.profile.cgm.CGMSpecificOpsControlPointCallback
    public void onContinuousGlucoseHyperAlertReceived(BluetoothDevice bluetoothDevice, float f, boolean z) {
        this.operationCompleted = true;
        this.requestCode = 16;
        this.alertLevel = f;
        this.secured = z;
        this.crcValid = z;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CGMSpecificOpsControlPointDataCallback, no.nordicsemi.android.ble.common.profile.cgm.CGMSpecificOpsControlPointCallback
    public void onContinuousGlucoseRateOfDecreaseAlertReceived(BluetoothDevice bluetoothDevice, float f, boolean z) {
        this.operationCompleted = true;
        this.requestCode = 19;
        this.alertLevel = f;
        this.secured = z;
        this.crcValid = z;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CGMSpecificOpsControlPointDataCallback, no.nordicsemi.android.ble.common.profile.cgm.CGMSpecificOpsControlPointCallback
    public void onContinuousGlucoseRateOfIncreaseAlertReceived(BluetoothDevice bluetoothDevice, float f, boolean z) {
        this.operationCompleted = true;
        this.requestCode = 22;
        this.alertLevel = f;
        this.secured = z;
        this.crcValid = z;
    }

    public boolean isOperationCompleted() {
        return this.operationCompleted;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CRCSecuredResponse
    public boolean isSecured() {
        return this.secured;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CRCSecuredResponse
    public boolean isCrcValid() {
        return this.crcValid;
    }

    public int getRequestCode() {
        return this.requestCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public int getGlucoseCommunicationInterval() {
        return this.glucoseCommunicationInterval;
    }

    public float getGlucoseConcentrationOfCalibration() {
        return this.glucoseConcentrationOfCalibration;
    }

    public int getCalibrationTime() {
        return this.calibrationTime;
    }

    public int getNextCalibrationTime() {
        return this.nextCalibrationTime;
    }

    public int getType() {
        return this.type;
    }

    public int getSampleLocation() {
        return this.sampleLocation;
    }

    public int getCalibrationDataRecordNumber() {
        return this.calibrationDataRecordNumber;
    }

    public CGMTypes.CGMCalibrationStatus getCalibrationStatus() {
        return this.calibrationStatus;
    }

    public float getAlertLevel() {
        return this.alertLevel;
    }

    private CGMSpecificOpsControlPointResponse(Parcel parcel) {
        super(parcel);
        this.operationCompleted = parcel.readByte() != 0;
        this.secured = parcel.readByte() != 0;
        this.crcValid = parcel.readByte() != 0;
        this.requestCode = parcel.readInt();
        this.errorCode = parcel.readInt();
        this.glucoseCommunicationInterval = parcel.readInt();
        this.glucoseConcentrationOfCalibration = parcel.readFloat();
        this.calibrationTime = parcel.readInt();
        this.nextCalibrationTime = parcel.readInt();
        this.type = parcel.readInt();
        this.sampleLocation = parcel.readInt();
        this.calibrationDataRecordNumber = parcel.readInt();
        if (parcel.readByte() == 0) {
            this.calibrationStatus = null;
        } else {
            this.calibrationStatus = new CGMTypes.CGMCalibrationStatus(parcel.readInt());
        }
        this.alertLevel = parcel.readFloat();
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeByte(this.operationCompleted ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.secured ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.crcValid ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.requestCode);
        parcel.writeInt(this.errorCode);
        parcel.writeInt(this.glucoseCommunicationInterval);
        parcel.writeFloat(this.glucoseConcentrationOfCalibration);
        parcel.writeInt(this.calibrationTime);
        parcel.writeInt(this.nextCalibrationTime);
        parcel.writeInt(this.type);
        parcel.writeInt(this.sampleLocation);
        parcel.writeInt(this.calibrationDataRecordNumber);
        if (this.calibrationStatus == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(this.calibrationStatus.value);
        }
        parcel.writeFloat(this.alertLevel);
    }
}
