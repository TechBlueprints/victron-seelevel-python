package no.nordicsemi.android.ble;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import no.nordicsemi.android.ble.Request;
import no.nordicsemi.android.ble.callback.BeforeCallback;
import no.nordicsemi.android.ble.callback.FailCallback;
import no.nordicsemi.android.ble.callback.InvalidRequestCallback;
import no.nordicsemi.android.ble.callback.PhyCallback;
import no.nordicsemi.android.ble.callback.SuccessCallback;

/* loaded from: classes.dex */
public final class PhyRequest extends SimpleValueRequest<PhyCallback> implements Operation {
    public static final int PHY_LE_1M_MASK = 1;
    public static final int PHY_LE_2M_MASK = 2;
    public static final int PHY_LE_CODED_MASK = 4;
    public static final int PHY_OPTION_NO_PREFERRED = 0;
    public static final int PHY_OPTION_S2 = 1;
    public static final int PHY_OPTION_S8 = 2;
    private final int phyOptions;
    private final int rxPhy;
    private final int txPhy;

    PhyRequest(Request.Type type) {
        super(type);
        this.txPhy = 0;
        this.rxPhy = 0;
        this.phyOptions = 0;
    }

    PhyRequest(Request.Type type, int i, int i2, int i3) {
        super(type);
        i = (i & (-8)) > 0 ? 1 : i;
        i2 = (i2 & (-8)) > 0 ? 1 : i2;
        i3 = (i3 < 0 || i3 > 2) ? 0 : i3;
        this.txPhy = i;
        this.rxPhy = i2;
        this.phyOptions = i3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // no.nordicsemi.android.ble.Request
    public PhyRequest setRequestHandler(RequestHandler requestHandler) {
        super.setRequestHandler(requestHandler);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public PhyRequest setHandler(Handler handler) {
        super.setHandler(handler);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public PhyRequest done(SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public PhyRequest fail(FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public PhyRequest invalid(InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public PhyRequest before(BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.SimpleValueRequest
    public PhyRequest with(PhyCallback phyCallback) {
        super.with((PhyRequest) phyCallback);
        return this;
    }

    void notifyPhyChanged(final BluetoothDevice bluetoothDevice, final int i, final int i2) {
        this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$PhyRequest$Et679__ths69ewkKmJfIYnZm71c
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyPhyChanged$0$PhyRequest(bluetoothDevice, i, i2);
            }
        });
    }

    public /* synthetic */ void lambda$notifyPhyChanged$0$PhyRequest(BluetoothDevice bluetoothDevice, int i, int i2) {
        if (this.valueCallback != 0) {
            ((PhyCallback) this.valueCallback).onPhyChanged(bluetoothDevice, i, i2);
        }
    }

    void notifyLegacyPhy(final BluetoothDevice bluetoothDevice) {
        this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$PhyRequest$1LBxeuGGEY-MGIkxeU-NhD-GX0k
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyLegacyPhy$1$PhyRequest(bluetoothDevice);
            }
        });
    }

    public /* synthetic */ void lambda$notifyLegacyPhy$1$PhyRequest(BluetoothDevice bluetoothDevice) {
        if (this.valueCallback != 0) {
            ((PhyCallback) this.valueCallback).onPhyChanged(bluetoothDevice, 1, 1);
        }
    }

    int getPreferredTxPhy() {
        return this.txPhy;
    }

    int getPreferredRxPhy() {
        return this.rxPhy;
    }

    int getPreferredPhyOptions() {
        return this.phyOptions;
    }
}
