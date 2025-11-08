package no.nordicsemi.android.ble;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import java.util.Deque;
import java.util.LinkedList;
import no.nordicsemi.android.ble.Request;
import no.nordicsemi.android.ble.callback.BeforeCallback;
import no.nordicsemi.android.ble.callback.FailCallback;
import no.nordicsemi.android.ble.callback.InvalidRequestCallback;
import no.nordicsemi.android.ble.callback.SuccessCallback;
import no.nordicsemi.android.ble.exception.BluetoothDisabledException;
import no.nordicsemi.android.ble.exception.DeviceDisconnectedException;
import no.nordicsemi.android.ble.exception.InvalidRequestException;
import no.nordicsemi.android.ble.exception.RequestFailedException;

/* loaded from: classes.dex */
public class RequestQueue extends Request {
    private final Deque<Request> requests;

    RequestQueue() {
        super(Request.Type.SET);
        this.requests = new LinkedList();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // no.nordicsemi.android.ble.Request
    public RequestQueue setRequestHandler(RequestHandler requestHandler) {
        super.setRequestHandler(requestHandler);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public RequestQueue setHandler(Handler handler) {
        super.setHandler(handler);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public RequestQueue done(SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public RequestQueue fail(FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public RequestQueue invalid(InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public RequestQueue before(BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public RequestQueue add(Operation operation) {
        if (operation instanceof Request) {
            Request request = (Request) operation;
            if (request.enqueued) {
                throw new IllegalStateException("Request already enqueued");
            }
            request.internalFail(new FailCallback() { // from class: no.nordicsemi.android.ble.-$$Lambda$mOnAyWC-YP6BZfO7A4oivt6F_7c
                @Override // no.nordicsemi.android.ble.callback.FailCallback
                public final void onRequestFailed(BluetoothDevice bluetoothDevice, int i) {
                    this.f$0.notifyFail(bluetoothDevice, i);
                }
            });
            this.requests.add(request);
            request.enqueued = true;
            return this;
        }
        throw new IllegalArgumentException("Operation does not extend Request");
    }

    void addFirst(Request request) {
        this.requests.addFirst(request);
    }

    public int size() {
        return this.requests.size();
    }

    public boolean isEmpty() {
        return this.requests.isEmpty();
    }

    public void cancelQueue() {
        this.requests.clear();
    }

    public final void await() throws InterruptedException, DeviceDisconnectedException, RequestFailedException, InvalidRequestException, BluetoothDisabledException {
        assertNotMainThread();
        BeforeCallback beforeCallback = this.beforeCallback;
        SuccessCallback successCallback = this.successCallback;
        FailCallback failCallback = this.failCallback;
        try {
            this.syncLock.close();
            Request.RequestCallback requestCallback = new Request.RequestCallback();
            this.beforeCallback = null;
            done((SuccessCallback) requestCallback).fail((FailCallback) requestCallback).invalid((InvalidRequestCallback) requestCallback).enqueue();
            this.syncLock.block();
            if (requestCallback.isSuccess()) {
                return;
            }
            if (requestCallback.status == -1) {
                throw new DeviceDisconnectedException();
            }
            if (requestCallback.status == -100) {
                throw new BluetoothDisabledException();
            }
            if (requestCallback.status == -5) {
                throw new InterruptedException();
            }
            if (requestCallback.status == -1000000) {
                throw new InvalidRequestException(this);
            }
            throw new RequestFailedException(this, requestCallback.status);
        } finally {
            this.beforeCallback = beforeCallback;
            this.successCallback = successCallback;
            this.failCallback = failCallback;
        }
    }

    Request getNext() {
        try {
            return this.requests.remove();
        } catch (Exception unused) {
            return null;
        }
    }

    boolean hasMore() {
        return (this.finished || this.requests.isEmpty()) ? false : true;
    }
}
