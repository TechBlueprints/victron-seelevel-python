package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public final class CGMSessionRunTimeResponse extends CGMSessionRunTimeDataCallback implements CRCSecuredResponse, Parcelable {
    public static final Parcelable.Creator<CGMSessionRunTimeResponse> CREATOR = new Parcelable.Creator<CGMSessionRunTimeResponse>() { // from class: no.nordicsemi.android.ble.common.callback.cgm.CGMSessionRunTimeResponse.1
        @Override // android.os.Parcelable.Creator
        public CGMSessionRunTimeResponse createFromParcel(Parcel parcel) {
            return new CGMSessionRunTimeResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public CGMSessionRunTimeResponse[] newArray(int i) {
            return new CGMSessionRunTimeResponse[i];
        }
    };
    private boolean crcValid;
    private boolean secured;
    private int sessionRunTime;

    public CGMSessionRunTimeResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.cgm.CGMSessionRunTimeCallback
    public void onContinuousGlucoseMonitorSessionRunTimeReceived(BluetoothDevice bluetoothDevice, int i, boolean z) {
        this.sessionRunTime = i;
        this.secured = z;
        this.crcValid = z;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CGMSessionRunTimeDataCallback, no.nordicsemi.android.ble.common.profile.cgm.CGMSessionRunTimeCallback
    public void onContinuousGlucoseMonitorSessionRunTimeReceivedWithCrcError(BluetoothDevice bluetoothDevice, Data data) {
        onInvalidDataReceived(bluetoothDevice, data);
        this.secured = true;
        this.crcValid = false;
    }

    public int getSessionRunTime() {
        return this.sessionRunTime;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CRCSecuredResponse
    public boolean isSecured() {
        return this.secured;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CRCSecuredResponse
    public boolean isCrcValid() {
        return this.crcValid;
    }

    private CGMSessionRunTimeResponse(Parcel parcel) {
        super(parcel);
        this.sessionRunTime = parcel.readInt();
        this.secured = parcel.readByte() != 0;
        this.crcValid = parcel.readByte() != 0;
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.sessionRunTime);
        parcel.writeByte(this.secured ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.crcValid ? (byte) 1 : (byte) 0);
    }
}
