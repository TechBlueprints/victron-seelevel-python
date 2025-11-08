package no.nordicsemi.android.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import no.nordicsemi.android.ble.Request;
import no.nordicsemi.android.ble.callback.BeforeCallback;
import no.nordicsemi.android.ble.callback.FailCallback;
import no.nordicsemi.android.ble.callback.SuccessCallback;
import no.nordicsemi.android.ble.exception.BluetoothDisabledException;
import no.nordicsemi.android.ble.exception.DeviceDisconnectedException;
import no.nordicsemi.android.ble.exception.InvalidRequestException;
import no.nordicsemi.android.ble.exception.RequestFailedException;

/* loaded from: classes.dex */
public abstract class AwaitingRequest<T> extends TimeoutableValueRequest<T> {
    private static final int NOT_STARTED = -123456;
    private static final int STARTED = -123455;
    private Request trigger;
    private int triggerStatus;

    AwaitingRequest(Request.Type type) {
        super(type);
        this.triggerStatus = 0;
    }

    AwaitingRequest(Request.Type type, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super(type, bluetoothGattCharacteristic);
        this.triggerStatus = 0;
    }

    AwaitingRequest(Request.Type type, BluetoothGattDescriptor bluetoothGattDescriptor) {
        super(type, bluetoothGattDescriptor);
        this.triggerStatus = 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public AwaitingRequest<T> trigger(Operation operation) {
        if (operation instanceof Request) {
            Request request = (Request) operation;
            this.trigger = request;
            this.triggerStatus = NOT_STARTED;
            request.internalBefore(new BeforeCallback() { // from class: no.nordicsemi.android.ble.-$$Lambda$AwaitingRequest$PkqeSebXgWSZLn3TcUM1lpyPSrk
                @Override // no.nordicsemi.android.ble.callback.BeforeCallback
                public final void onRequestStarted(BluetoothDevice bluetoothDevice) {
                    this.f$0.lambda$trigger$0$AwaitingRequest(bluetoothDevice);
                }
            });
            this.trigger.internalSuccess(new SuccessCallback() { // from class: no.nordicsemi.android.ble.-$$Lambda$AwaitingRequest$eP7MbJIJ11f2aiDtikTSjAdYVdA
                @Override // no.nordicsemi.android.ble.callback.SuccessCallback
                public final void onRequestCompleted(BluetoothDevice bluetoothDevice) {
                    this.f$0.lambda$trigger$1$AwaitingRequest(bluetoothDevice);
                }
            });
            this.trigger.internalFail(new FailCallback() { // from class: no.nordicsemi.android.ble.-$$Lambda$AwaitingRequest$PTaobeLH-1lzafC3JD7t4PoozWY
                @Override // no.nordicsemi.android.ble.callback.FailCallback
                public final void onRequestFailed(BluetoothDevice bluetoothDevice, int i) {
                    this.f$0.lambda$trigger$2$AwaitingRequest(bluetoothDevice, i);
                }
            });
        }
        return this;
    }

    public /* synthetic */ void lambda$trigger$0$AwaitingRequest(BluetoothDevice bluetoothDevice) {
        this.triggerStatus = STARTED;
    }

    public /* synthetic */ void lambda$trigger$1$AwaitingRequest(BluetoothDevice bluetoothDevice) {
        this.triggerStatus = 0;
    }

    public /* synthetic */ void lambda$trigger$2$AwaitingRequest(BluetoothDevice bluetoothDevice, int i) {
        this.triggerStatus = i;
        this.syncLock.open();
        notifyFail(bluetoothDevice, i);
    }

    @Override // no.nordicsemi.android.ble.TimeoutableValueRequest
    public <E extends T> E await(E e) throws InterruptedException, DeviceDisconnectedException, RequestFailedException, InvalidRequestException, BluetoothDisabledException {
        assertNotMainThread();
        try {
            if (this.trigger != null && this.trigger.enqueued) {
                throw new IllegalStateException("Trigger request already enqueued");
            }
            super.await((AwaitingRequest<T>) e);
            return e;
        } catch (RequestFailedException e2) {
            if (this.triggerStatus != 0) {
                throw new RequestFailedException(this.trigger, this.triggerStatus);
            }
            throw e2;
        }
    }

    Request getTrigger() {
        return this.trigger;
    }

    boolean isTriggerPending() {
        return this.triggerStatus == NOT_STARTED;
    }

    boolean isTriggerCompleteOrNull() {
        return this.triggerStatus != STARTED;
    }
}
