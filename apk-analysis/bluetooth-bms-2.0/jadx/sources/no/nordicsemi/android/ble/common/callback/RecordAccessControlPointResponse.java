package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class RecordAccessControlPointResponse extends RecordAccessControlPointDataCallback implements Parcelable {
    public static final Parcelable.Creator<RecordAccessControlPointResponse> CREATOR = new Parcelable.Creator<RecordAccessControlPointResponse>() { // from class: no.nordicsemi.android.ble.common.callback.RecordAccessControlPointResponse.1
        @Override // android.os.Parcelable.Creator
        public RecordAccessControlPointResponse createFromParcel(Parcel parcel) {
            return new RecordAccessControlPointResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public RecordAccessControlPointResponse[] newArray(int i) {
            return new RecordAccessControlPointResponse[i];
        }
    };
    private int errorCode;
    private int numberOfRecords;
    private boolean operationCompleted;
    private boolean recordsFound;
    private int requestCode;

    public RecordAccessControlPointResponse() {
        this.numberOfRecords = -1;
    }

    @Override // no.nordicsemi.android.ble.common.profile.RecordAccessControlPointCallback
    public void onRecordAccessOperationCompleted(BluetoothDevice bluetoothDevice, int i) {
        this.operationCompleted = true;
        this.recordsFound = true;
        this.requestCode = i;
    }

    @Override // no.nordicsemi.android.ble.common.profile.RecordAccessControlPointCallback
    public void onRecordAccessOperationCompletedWithNoRecordsFound(BluetoothDevice bluetoothDevice, int i) {
        this.operationCompleted = true;
        this.numberOfRecords = 0;
        this.recordsFound = false;
        this.requestCode = i;
    }

    @Override // no.nordicsemi.android.ble.common.profile.RecordAccessControlPointCallback
    public void onNumberOfRecordsReceived(BluetoothDevice bluetoothDevice, int i) {
        this.operationCompleted = true;
        this.numberOfRecords = i;
        this.recordsFound = i > 0;
        this.requestCode = 4;
    }

    @Override // no.nordicsemi.android.ble.common.profile.RecordAccessControlPointCallback
    public void onRecordAccessOperationError(BluetoothDevice bluetoothDevice, int i, int i2) {
        this.operationCompleted = false;
        this.errorCode = i2;
        this.requestCode = i;
    }

    public int getRequestCode() {
        return this.requestCode;
    }

    public boolean isOperationCompleted() {
        return this.operationCompleted;
    }

    public boolean wereRecordsFound() {
        return this.recordsFound;
    }

    public int getNumberOfRecords() {
        return this.numberOfRecords;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    private RecordAccessControlPointResponse(Parcel parcel) {
        super(parcel);
        this.numberOfRecords = -1;
        this.operationCompleted = parcel.readByte() != 0;
        this.recordsFound = parcel.readByte() != 0;
        this.numberOfRecords = parcel.readInt();
        this.errorCode = parcel.readInt();
        this.requestCode = parcel.readInt();
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeByte(this.operationCompleted ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.recordsFound ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.numberOfRecords);
        parcel.writeInt(this.errorCode);
        parcel.writeInt(this.requestCode);
    }
}
