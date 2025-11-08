package no.nordicsemi.android.ble.common.callback.alert;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class AlertLevelResponse extends AlertLevelDataCallback implements Parcelable {
    public static final Parcelable.Creator<AlertLevelResponse> CREATOR = new Parcelable.Creator<AlertLevelResponse>() { // from class: no.nordicsemi.android.ble.common.callback.alert.AlertLevelResponse.1
        @Override // android.os.Parcelable.Creator
        public AlertLevelResponse createFromParcel(Parcel parcel) {
            return new AlertLevelResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public AlertLevelResponse[] newArray(int i) {
            return new AlertLevelResponse[i];
        }
    };
    private int level;

    public AlertLevelResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.alert.AlertLevelCallback
    public void onAlertLevelChanged(BluetoothDevice bluetoothDevice, int i) {
        this.level = i;
    }

    public int getAlertLevel() {
        return this.level;
    }

    private AlertLevelResponse(Parcel parcel) {
        super(parcel);
        this.level = parcel.readInt();
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.level);
    }
}
