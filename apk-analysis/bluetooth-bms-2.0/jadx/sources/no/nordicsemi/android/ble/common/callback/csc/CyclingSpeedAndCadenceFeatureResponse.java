package no.nordicsemi.android.ble.common.callback.csc;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import no.nordicsemi.android.ble.common.profile.csc.CyclingSpeedAndCadenceFeatureCallback;

/* loaded from: classes.dex */
public final class CyclingSpeedAndCadenceFeatureResponse extends CyclingSpeedAndCadenceFeatureDataCallback implements Parcelable {
    public static final Parcelable.Creator<CyclingSpeedAndCadenceFeatureResponse> CREATOR = new Parcelable.Creator<CyclingSpeedAndCadenceFeatureResponse>() { // from class: no.nordicsemi.android.ble.common.callback.csc.CyclingSpeedAndCadenceFeatureResponse.1
        @Override // android.os.Parcelable.Creator
        public CyclingSpeedAndCadenceFeatureResponse createFromParcel(Parcel parcel) {
            return new CyclingSpeedAndCadenceFeatureResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public CyclingSpeedAndCadenceFeatureResponse[] newArray(int i) {
            return new CyclingSpeedAndCadenceFeatureResponse[i];
        }
    };
    private CyclingSpeedAndCadenceFeatureCallback.CSCFeatures features;

    public CyclingSpeedAndCadenceFeatureResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.csc.CyclingSpeedAndCadenceFeatureCallback
    public void onCyclingSpeedAndCadenceFeaturesReceived(BluetoothDevice bluetoothDevice, CyclingSpeedAndCadenceFeatureCallback.CSCFeatures cSCFeatures) {
        this.features = cSCFeatures;
    }

    public CyclingSpeedAndCadenceFeatureCallback.CSCFeatures getFeatures() {
        return this.features;
    }

    private CyclingSpeedAndCadenceFeatureResponse(Parcel parcel) {
        super(parcel);
        if (parcel.readByte() == 0) {
            this.features = null;
        } else {
            this.features = new CyclingSpeedAndCadenceFeatureCallback.CSCFeatures(parcel.readInt());
        }
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        if (this.features == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(this.features.value);
        }
    }
}
