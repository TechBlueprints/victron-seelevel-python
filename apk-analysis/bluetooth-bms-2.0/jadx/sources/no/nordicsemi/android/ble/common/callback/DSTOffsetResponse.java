package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import no.nordicsemi.android.ble.common.profile.DSTOffsetCallback;

/* loaded from: classes.dex */
public final class DSTOffsetResponse extends DSTOffsetDataCallback implements Parcelable {
    public static final Parcelable.Creator<DSTOffsetResponse> CREATOR = new Parcelable.Creator<DSTOffsetResponse>() { // from class: no.nordicsemi.android.ble.common.callback.DSTOffsetResponse.1
        @Override // android.os.Parcelable.Creator
        public DSTOffsetResponse createFromParcel(Parcel parcel) {
            return new DSTOffsetResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public DSTOffsetResponse[] newArray(int i) {
            return new DSTOffsetResponse[i];
        }
    };
    private DSTOffsetCallback.DSTOffset offset;

    public DSTOffsetResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.DSTOffsetCallback
    public void onDSTOffsetReceived(BluetoothDevice bluetoothDevice, DSTOffsetCallback.DSTOffset dSTOffset) {
        this.offset = dSTOffset;
    }

    public DSTOffsetCallback.DSTOffset getDSTOffset() {
        return this.offset;
    }

    private DSTOffsetResponse(Parcel parcel) {
        super(parcel);
        if (parcel.readByte() == 0) {
            this.offset = null;
        } else {
            this.offset = DSTOffsetCallback.DSTOffset.values()[parcel.readInt()];
        }
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        if (this.offset == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(this.offset.ordinal());
        }
    }
}
