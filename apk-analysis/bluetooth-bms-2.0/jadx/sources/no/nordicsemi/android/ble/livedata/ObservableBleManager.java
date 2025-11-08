package no.nordicsemi.android.ble.livedata;

import android.content.Context;
import android.os.Handler;
import androidx.lifecycle.LiveData;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.livedata.state.BondState;
import no.nordicsemi.android.ble.livedata.state.ConnectionState;

/* compiled from: ObservableBleManager.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\t¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\f¨\u0006\u0010"}, d2 = {"Lno/nordicsemi/android/ble/livedata/ObservableBleManager;", "Lno/nordicsemi/android/ble/BleManager;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "handler", "Landroid/os/Handler;", "(Landroid/content/Context;Landroid/os/Handler;)V", "bondingState", "Landroidx/lifecycle/LiveData;", "Lno/nordicsemi/android/ble/livedata/state/BondState;", "getBondingState", "()Landroidx/lifecycle/LiveData;", "state", "Lno/nordicsemi/android/ble/livedata/state/ConnectionState;", "getState", "ble-livedata_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public abstract class ObservableBleManager extends BleManager {
    private final LiveData<BondState> bondingState;
    private final LiveData<ConnectionState> state;

    public final LiveData<ConnectionState> getState() {
        return this.state;
    }

    public final LiveData<BondState> getBondingState() {
        return this.bondingState;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ObservableBleManager(Context context) {
        super(context);
        Intrinsics.checkParameterIsNotNull(context, "context");
        this.state = new ConnectionStateLiveData();
        this.bondingState = new BondingStateLiveData();
        LiveData<ConnectionState> liveData = this.state;
        if (liveData == null) {
            throw new TypeCastException("null cannot be cast to non-null type no.nordicsemi.android.ble.livedata.ConnectionStateLiveData");
        }
        setConnectionObserver((ConnectionStateLiveData) liveData);
        LiveData<BondState> liveData2 = this.bondingState;
        if (liveData2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type no.nordicsemi.android.ble.livedata.BondingStateLiveData");
        }
        setBondingObserver((BondingStateLiveData) liveData2);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ObservableBleManager(Context context, Handler handler) {
        super(context, handler);
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        this.state = new ConnectionStateLiveData();
        this.bondingState = new BondingStateLiveData();
        LiveData<ConnectionState> liveData = this.state;
        if (liveData == null) {
            throw new TypeCastException("null cannot be cast to non-null type no.nordicsemi.android.ble.livedata.ConnectionStateLiveData");
        }
        setConnectionObserver((ConnectionStateLiveData) liveData);
        LiveData<BondState> liveData2 = this.bondingState;
        if (liveData2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type no.nordicsemi.android.ble.livedata.BondingStateLiveData");
        }
        setBondingObserver((BondingStateLiveData) liveData2);
    }
}
