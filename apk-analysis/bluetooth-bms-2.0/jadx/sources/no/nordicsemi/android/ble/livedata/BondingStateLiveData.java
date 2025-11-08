package no.nordicsemi.android.ble.livedata;

import android.bluetooth.BluetoothDevice;
import androidx.lifecycle.LiveData;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import no.nordicsemi.android.ble.livedata.state.BondState;
import no.nordicsemi.android.ble.observer.BondingObserver;

/* compiled from: BondingStateLiveData.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003B\u0005¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\n\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\u000b"}, d2 = {"Lno/nordicsemi/android/ble/livedata/BondingStateLiveData;", "Landroidx/lifecycle/LiveData;", "Lno/nordicsemi/android/ble/livedata/state/BondState;", "Lno/nordicsemi/android/ble/observer/BondingObserver;", "()V", "onBonded", "", "device", "Landroid/bluetooth/BluetoothDevice;", "onBondingFailed", "onBondingRequired", "ble-livedata_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class BondingStateLiveData extends LiveData<BondState> implements BondingObserver {
    public BondingStateLiveData() {
        super(BondState.NotBonded.INSTANCE);
    }

    @Override // no.nordicsemi.android.ble.observer.BondingObserver
    public void onBonded(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        setValue(BondState.Bonded.INSTANCE);
    }

    @Override // no.nordicsemi.android.ble.observer.BondingObserver
    public void onBondingFailed(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        setValue(BondState.NotBonded.INSTANCE);
    }

    @Override // no.nordicsemi.android.ble.observer.BondingObserver
    public void onBondingRequired(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        setValue(BondState.Bonding.INSTANCE);
    }
}
