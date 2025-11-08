package no.nordicsemi.android.ble.callback.profile;

import android.bluetooth.BluetoothDevice;
import no.nordicsemi.android.ble.callback.DataReceivedCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public interface ProfileDataCallback extends DataReceivedCallback {

    /* renamed from: no.nordicsemi.android.ble.callback.profile.ProfileDataCallback$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$onInvalidDataReceived(ProfileDataCallback profileDataCallback, BluetoothDevice bluetoothDevice, Data data) {
        }
    }

    void onInvalidDataReceived(BluetoothDevice bluetoothDevice, Data data);
}
