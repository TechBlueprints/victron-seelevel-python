package no.nordicsemi.android.ble.livedata;

import android.bluetooth.BluetoothDevice;
import androidx.lifecycle.LiveData;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import no.nordicsemi.android.ble.livedata.state.ConnectionState;
import no.nordicsemi.android.ble.observer.ConnectionObserver;

/* compiled from: ConnectionStateLiveData.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\b\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003B\u0005¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0018\u0010\n\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0010\u0010\r\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0018\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0010\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\u0010"}, d2 = {"Lno/nordicsemi/android/ble/livedata/ConnectionStateLiveData;", "Landroidx/lifecycle/LiveData;", "Lno/nordicsemi/android/ble/livedata/state/ConnectionState;", "Lno/nordicsemi/android/ble/observer/ConnectionObserver;", "()V", "onDeviceConnected", "", "device", "Landroid/bluetooth/BluetoothDevice;", "onDeviceConnecting", "onDeviceDisconnected", "reason", "", "onDeviceDisconnecting", "onDeviceFailedToConnect", "onDeviceReady", "ble-livedata_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class ConnectionStateLiveData extends LiveData<ConnectionState> implements ConnectionObserver {
    public ConnectionStateLiveData() {
        super(new ConnectionState.Disconnected(-1));
        setValue(new ConnectionState.Disconnected(-1));
    }

    @Override // no.nordicsemi.android.ble.observer.ConnectionObserver
    public void onDeviceConnecting(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        setValue(ConnectionState.Connecting.INSTANCE);
    }

    @Override // no.nordicsemi.android.ble.observer.ConnectionObserver
    public void onDeviceConnected(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        setValue(ConnectionState.Initializing.INSTANCE);
    }

    @Override // no.nordicsemi.android.ble.observer.ConnectionObserver
    public void onDeviceReady(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        setValue(ConnectionState.Ready.INSTANCE);
    }

    @Override // no.nordicsemi.android.ble.observer.ConnectionObserver
    public void onDeviceDisconnecting(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        setValue(ConnectionState.Disconnecting.INSTANCE);
    }

    @Override // no.nordicsemi.android.ble.observer.ConnectionObserver
    public void onDeviceDisconnected(BluetoothDevice device, int reason) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        setValue(new ConnectionState.Disconnected(reason));
    }

    @Override // no.nordicsemi.android.ble.observer.ConnectionObserver
    public void onDeviceFailedToConnect(BluetoothDevice device, int reason) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        setValue(new ConnectionState.Disconnected(reason));
    }
}
