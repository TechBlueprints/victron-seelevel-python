package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import no.nordicsemi.android.ble.common.profile.cgm.CGMTypes;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public final class CGMFeatureResponse extends CGMFeatureDataCallback implements CRCSecuredResponse, Parcelable {
    public static final Parcelable.Creator<CGMFeatureResponse> CREATOR = new Parcelable.Creator<CGMFeatureResponse>() { // from class: no.nordicsemi.android.ble.common.callback.cgm.CGMFeatureResponse.1
        @Override // android.os.Parcelable.Creator
        public CGMFeatureResponse createFromParcel(Parcel parcel) {
            return new CGMFeatureResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public CGMFeatureResponse[] newArray(int i) {
            return new CGMFeatureResponse[i];
        }
    };
    private boolean crcValid;
    private CGMTypes.CGMFeatures features;
    private int sampleLocation;
    private boolean secured;
    private int type;

    public CGMFeatureResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.cgm.CGMFeatureCallback
    public void onContinuousGlucoseMonitorFeaturesReceived(BluetoothDevice bluetoothDevice, CGMTypes.CGMFeatures cGMFeatures, int i, int i2, boolean z) {
        this.features = cGMFeatures;
        this.type = i;
        this.sampleLocation = i2;
        this.secured = z;
        this.crcValid = z;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CGMFeatureDataCallback, no.nordicsemi.android.ble.common.profile.cgm.CGMFeatureCallback
    public void onContinuousGlucoseMonitorFeaturesReceivedWithCrcError(BluetoothDevice bluetoothDevice, Data data) {
        onInvalidDataReceived(bluetoothDevice, data);
        this.secured = true;
        this.crcValid = false;
    }

    public CGMTypes.CGMFeatures getFeatures() {
        return this.features;
    }

    public int getType() {
        return this.type;
    }

    public int getSampleLocation() {
        return this.sampleLocation;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CRCSecuredResponse
    public boolean isSecured() {
        return this.secured;
    }

    @Override // no.nordicsemi.android.ble.common.callback.cgm.CRCSecuredResponse
    public boolean isCrcValid() {
        return this.crcValid;
    }

    private CGMFeatureResponse(Parcel parcel) {
        super(parcel);
        if (parcel.readByte() == 0) {
            this.features = null;
        } else {
            this.features = new CGMTypes.CGMFeatures(parcel.readInt());
        }
        this.type = parcel.readInt();
        this.sampleLocation = parcel.readInt();
        this.secured = parcel.readByte() != 0;
        this.crcValid = parcel.readByte() != 0;
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
        parcel.writeInt(this.type);
        parcel.writeInt(this.sampleLocation);
        parcel.writeByte(this.secured ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.crcValid ? (byte) 1 : (byte) 0);
    }
}
