package no.nordicsemi.android.ble;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import no.nordicsemi.android.ble.Request;
import no.nordicsemi.android.ble.callback.BeforeCallback;
import no.nordicsemi.android.ble.callback.FailCallback;
import no.nordicsemi.android.ble.callback.InvalidRequestCallback;
import no.nordicsemi.android.ble.callback.MtuCallback;
import no.nordicsemi.android.ble.callback.SuccessCallback;

/* loaded from: classes.dex */
public final class MtuRequest extends SimpleValueRequest<MtuCallback> implements Operation {
    private final int value;

    MtuRequest(Request.Type type, int i) {
        super(type);
        i = i < 23 ? 23 : i;
        this.value = i > 517 ? 517 : i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // no.nordicsemi.android.ble.Request
    public MtuRequest setRequestHandler(RequestHandler requestHandler) {
        super.setRequestHandler(requestHandler);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public MtuRequest setHandler(Handler handler) {
        super.setHandler(handler);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public MtuRequest done(SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public MtuRequest fail(FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public MtuRequest invalid(InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public MtuRequest before(BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.SimpleValueRequest
    public MtuRequest with(MtuCallback mtuCallback) {
        super.with((MtuRequest) mtuCallback);
        return this;
    }

    void notifyMtuChanged(final BluetoothDevice bluetoothDevice, final int i) {
        this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$MtuRequest$vw_Q0tJBUeAO2PiOpfDKoYxTr5w
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyMtuChanged$0$MtuRequest(bluetoothDevice, i);
            }
        });
    }

    public /* synthetic */ void lambda$notifyMtuChanged$0$MtuRequest(BluetoothDevice bluetoothDevice, int i) {
        if (this.valueCallback != 0) {
            ((MtuCallback) this.valueCallback).onMtuChanged(bluetoothDevice, i);
        }
    }

    int getRequiredMtu() {
        return this.value;
    }
}
