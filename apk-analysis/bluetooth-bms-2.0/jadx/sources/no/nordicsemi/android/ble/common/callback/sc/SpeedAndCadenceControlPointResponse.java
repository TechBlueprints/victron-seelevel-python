package no.nordicsemi.android.ble.common.callback.sc;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class SpeedAndCadenceControlPointResponse extends SpeedAndCadenceControlPointDataCallback implements Parcelable {
    public static final Parcelable.Creator<SpeedAndCadenceControlPointResponse> CREATOR = new Parcelable.Creator<SpeedAndCadenceControlPointResponse>() { // from class: no.nordicsemi.android.ble.common.callback.sc.SpeedAndCadenceControlPointResponse.1
        @Override // android.os.Parcelable.Creator
        public SpeedAndCadenceControlPointResponse createFromParcel(Parcel parcel) {
            return new SpeedAndCadenceControlPointResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SpeedAndCadenceControlPointResponse[] newArray(int i) {
            return new SpeedAndCadenceControlPointResponse[i];
        }
    };
    private int errorCode;
    private int[] locations;
    private boolean operationCompleted;
    private int requestCode;

    public SpeedAndCadenceControlPointResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.sc.SpeedAndCadenceControlPointCallback
    public void onSCOperationCompleted(BluetoothDevice bluetoothDevice, int i) {
        this.operationCompleted = true;
        this.requestCode = i;
    }

    @Override // no.nordicsemi.android.ble.common.profile.sc.SpeedAndCadenceControlPointCallback
    public void onSCOperationError(BluetoothDevice bluetoothDevice, int i, int i2) {
        this.operationCompleted = false;
        this.requestCode = i;
        this.errorCode = i2;
    }

    @Override // no.nordicsemi.android.ble.common.profile.sc.SpeedAndCadenceControlPointCallback
    public void onSupportedSensorLocationsReceived(BluetoothDevice bluetoothDevice, int[] iArr) {
        this.operationCompleted = true;
        this.requestCode = 4;
        this.locations = iArr;
    }

    public boolean isOperationCompleted() {
        return this.operationCompleted;
    }

    public int getRequestCode() {
        return this.requestCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public int[] getSupportedSensorLocations() {
        return this.locations;
    }

    private SpeedAndCadenceControlPointResponse(Parcel parcel) {
        super(parcel);
        this.operationCompleted = parcel.readByte() != 0;
        this.requestCode = parcel.readInt();
        this.errorCode = parcel.readInt();
        this.locations = parcel.createIntArray();
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeByte(this.operationCompleted ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.requestCode);
        parcel.writeInt(this.errorCode);
        parcel.writeIntArray(this.locations);
    }
}
