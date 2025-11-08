package no.nordicsemi.android.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Handler;
import no.nordicsemi.android.ble.Request;
import no.nordicsemi.android.ble.callback.FailCallback;
import no.nordicsemi.android.ble.callback.SuccessCallback;
import no.nordicsemi.android.ble.exception.BluetoothDisabledException;
import no.nordicsemi.android.ble.exception.DeviceDisconnectedException;
import no.nordicsemi.android.ble.exception.InvalidRequestException;
import no.nordicsemi.android.ble.exception.RequestFailedException;

/* loaded from: classes.dex */
public abstract class TimeoutableRequest extends Request {
    protected long timeout;
    private Runnable timeoutCallback;

    TimeoutableRequest(Request.Type type) {
        super(type);
    }

    TimeoutableRequest(Request.Type type, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super(type, bluetoothGattCharacteristic);
    }

    TimeoutableRequest(Request.Type type, BluetoothGattDescriptor bluetoothGattDescriptor) {
        super(type, bluetoothGattDescriptor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // no.nordicsemi.android.ble.Request
    public TimeoutableRequest setRequestHandler(RequestHandler requestHandler) {
        super.setRequestHandler(requestHandler);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public TimeoutableRequest setHandler(Handler handler) {
        super.setHandler(handler);
        return this;
    }

    public TimeoutableRequest timeout(long j) {
        if (this.timeoutCallback != null) {
            throw new IllegalStateException("Request already started");
        }
        this.timeout = j;
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public final void enqueue() {
        super.enqueue();
    }

    @Deprecated
    public final void enqueue(long j) {
        timeout(j).enqueue();
    }

    public final void await() throws InterruptedException, DeviceDisconnectedException, RequestFailedException, InvalidRequestException, BluetoothDisabledException {
        assertNotMainThread();
        SuccessCallback successCallback = this.successCallback;
        FailCallback failCallback = this.failCallback;
        try {
            this.syncLock.close();
            Request.RequestCallback requestCallback = new Request.RequestCallback();
            done(requestCallback).fail(requestCallback).invalid(requestCallback).enqueue();
            if (!this.syncLock.block(this.timeout)) {
                throw new InterruptedException();
            }
            if (requestCallback.isSuccess()) {
                return;
            }
            if (requestCallback.status == -1) {
                throw new DeviceDisconnectedException();
            }
            if (requestCallback.status == -100) {
                throw new BluetoothDisabledException();
            }
            if (requestCallback.status == -1000000) {
                throw new InvalidRequestException(this);
            }
            throw new RequestFailedException(this, requestCallback.status);
        } finally {
            this.successCallback = successCallback;
            this.failCallback = failCallback;
        }
    }

    @Deprecated
    public final void await(long j) throws InterruptedException, DeviceDisconnectedException, RequestFailedException, InvalidRequestException, BluetoothDisabledException {
        timeout(j).await();
    }

    @Override // no.nordicsemi.android.ble.Request
    void notifyStarted(final BluetoothDevice bluetoothDevice) {
        if (this.timeout > 0) {
            this.timeoutCallback = new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$TimeoutableRequest$LRW3H3z9m1CuZHgzj67Qxd_-s8Y
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notifyStarted$0$TimeoutableRequest(bluetoothDevice);
                }
            };
            this.handler.postDelayed(this.timeoutCallback, this.timeout);
        }
        super.notifyStarted(bluetoothDevice);
    }

    public /* synthetic */ void lambda$notifyStarted$0$TimeoutableRequest(BluetoothDevice bluetoothDevice) {
        this.timeoutCallback = null;
        if (this.finished) {
            return;
        }
        notifyFail(bluetoothDevice, -5);
        this.requestHandler.onRequestTimeout(this);
    }

    @Override // no.nordicsemi.android.ble.Request
    boolean notifySuccess(BluetoothDevice bluetoothDevice) {
        if (!this.finished) {
            this.handler.removeCallbacks(this.timeoutCallback);
            this.timeoutCallback = null;
        }
        return super.notifySuccess(bluetoothDevice);
    }

    @Override // no.nordicsemi.android.ble.Request
    void notifyFail(BluetoothDevice bluetoothDevice, int i) {
        if (!this.finished) {
            this.handler.removeCallbacks(this.timeoutCallback);
            this.timeoutCallback = null;
        }
        super.notifyFail(bluetoothDevice, i);
    }

    @Override // no.nordicsemi.android.ble.Request
    void notifyInvalidRequest() {
        if (!this.finished) {
            this.handler.removeCallbacks(this.timeoutCallback);
            this.timeoutCallback = null;
        }
        super.notifyInvalidRequest();
    }
}
