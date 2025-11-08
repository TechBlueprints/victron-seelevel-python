package no.nordicsemi.android.ble.common.callback.glucose;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import no.nordicsemi.android.ble.common.profile.glucose.GlucoseFeatureCallback;

/* loaded from: classes.dex */
public final class GlucoseFeatureResponse extends GlucoseFeatureDataCallback implements Parcelable {
    public static final Parcelable.Creator<GlucoseFeatureResponse> CREATOR = new Parcelable.Creator<GlucoseFeatureResponse>() { // from class: no.nordicsemi.android.ble.common.callback.glucose.GlucoseFeatureResponse.1
        @Override // android.os.Parcelable.Creator
        public GlucoseFeatureResponse createFromParcel(Parcel parcel) {
            return new GlucoseFeatureResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GlucoseFeatureResponse[] newArray(int i) {
            return new GlucoseFeatureResponse[i];
        }
    };
    private GlucoseFeatureCallback.GlucoseFeatures features;

    public GlucoseFeatureResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.glucose.GlucoseFeatureCallback
    public void onGlucoseFeaturesReceived(BluetoothDevice bluetoothDevice, GlucoseFeatureCallback.GlucoseFeatures glucoseFeatures) {
        this.features = glucoseFeatures;
    }

    public GlucoseFeatureCallback.GlucoseFeatures getFeatures() {
        return this.features;
    }

    private GlucoseFeatureResponse(Parcel parcel) {
        super(parcel);
        if (parcel.readByte() == 0) {
            this.features = null;
        } else {
            this.features = new GlucoseFeatureCallback.GlucoseFeatures(parcel.readInt());
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
