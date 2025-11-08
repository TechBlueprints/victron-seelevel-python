package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import no.nordicsemi.android.ble.common.profile.cgm.CGMTypes;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public final class CGMStatusResponse extends CGMStatusDataCallback implements CRCSecuredResponse, Parcelable {
    public static final Parcelable.Creator<CGMStatusResponse> CREATOR = new Parcelable.Creator<CGMStatusResponse>() { // from class: no.nordicsemi.android.ble.common.callback.cgm.CGMStatusResponse.1
        @Override // android.os.Parcelable.Creator
        public CGMStatusResponse createFromParcel(Parcel parcel) {
            return new CGMStatusResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public CGMStatusResponse[] newArray(int i) {
            return new CGMStatusResponse[i];
        }
    };
    private boolean crcValid;
    private boolean secured;
    private CGMTypes.CGMStatus status;
    private int timeOffset;

    public CGMStatusResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.cgm.CGMStatusCallback
    public void onContinuousGlucoseMonitorStatusChanged(BluetoothDevice bluetoothDevice, CGMTypes.CGMStatus cGMStatus, int i, boolean z) {
        this.status = cGMStatus;
        this.timeOffset = i;
        this.secured = z;
        this.crcValid = z;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CGMStatusDataCallback, no.nordicsemi.android.ble.common.profile.cgm.CGMStatusCallback
    public void onContinuousGlucoseMonitorStatusReceivedWithCrcError(BluetoothDevice bluetoothDevice, Data data) {
        onInvalidDataReceived(bluetoothDevice, data);
        this.secured = true;
        this.crcValid = false;
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

    private CGMStatusResponse(Parcel parcel) {
        super(parcel);
        this.timeOffset = parcel.readInt();
        if (parcel.readByte() == 0) {
            this.status = null;
        } else {
            this.status = new CGMTypes.CGMStatus(parcel.readInt(), parcel.readInt(), parcel.readInt());
        }
        this.secured = parcel.readByte() != 0;
        this.crcValid = parcel.readByte() != 0;
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.timeOffset);
        if (this.status == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(this.status.warningStatus);
            parcel.writeInt(this.status.calibrationTempStatus);
            parcel.writeInt(this.status.sensorStatus);
        }
        parcel.writeByte(this.secured ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.crcValid ? (byte) 1 : (byte) 0);
    }
}
