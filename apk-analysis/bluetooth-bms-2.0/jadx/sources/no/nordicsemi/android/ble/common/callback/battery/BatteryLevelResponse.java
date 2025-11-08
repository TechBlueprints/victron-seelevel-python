package no.nordicsemi.android.ble.common.callback.battery;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class BatteryLevelResponse extends BatteryLevelDataCallback implements Parcelable {
    public static final Parcelable.Creator<BatteryLevelResponse> CREATOR = new Parcelable.Creator<BatteryLevelResponse>() { // from class: no.nordicsemi.android.ble.common.callback.battery.BatteryLevelResponse.1
        @Override // android.os.Parcelable.Creator
        public BatteryLevelResponse createFromParcel(Parcel parcel) {
            return new BatteryLevelResponse(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public BatteryLevelResponse[] newArray(int i) {
            return new BatteryLevelResponse[i];
        }
    };
    private int mBatteryLevel;

    public BatteryLevelResponse() {
    }

    @Override // no.nordicsemi.android.ble.common.profile.battery.BatteryLevelCallback
    public void onBatteryLevelChanged(BluetoothDevice bluetoothDevice, int i) {
        this.mBatteryLevel = i;
    }

    public int getBatteryLevel() {
        return this.mBatteryLevel;
    }

    private BatteryLevelResponse(Parcel parcel) {
        super(parcel);
        this.mBatteryLevel = parcel.readInt();
    }

    @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.mBatteryLevel);
    }
}
