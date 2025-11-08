package no.nordicsemi.android.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Looper;
import no.nordicsemi.android.ble.ConditionalWaitRequest;
import no.nordicsemi.android.ble.callback.BeforeCallback;
import no.nordicsemi.android.ble.callback.FailCallback;
import no.nordicsemi.android.ble.callback.InvalidRequestCallback;
import no.nordicsemi.android.ble.callback.SuccessCallback;

/* loaded from: classes.dex */
public abstract class Request {
    BeforeCallback beforeCallback;
    final BluetoothGattCharacteristic characteristic;
    final BluetoothGattDescriptor descriptor;
    boolean enqueued;
    FailCallback failCallback;
    boolean finished;
    protected CallbackHandler handler;
    BeforeCallback internalBeforeCallback;
    FailCallback internalFailCallback;
    SuccessCallback internalSuccessCallback;
    InvalidRequestCallback invalidRequestCallback;
    protected RequestHandler requestHandler;
    boolean started;
    SuccessCallback successCallback;
    final ConditionVariable syncLock;
    final Type type;

    enum Type {
        SET,
        CONNECT,
        DISCONNECT,
        CREATE_BOND,
        ENSURE_BOND,
        REMOVE_BOND,
        WRITE,
        NOTIFY,
        INDICATE,
        READ,
        WRITE_DESCRIPTOR,
        READ_DESCRIPTOR,
        BEGIN_RELIABLE_WRITE,
        EXECUTE_RELIABLE_WRITE,
        ABORT_RELIABLE_WRITE,
        ENABLE_NOTIFICATIONS,
        ENABLE_INDICATIONS,
        DISABLE_NOTIFICATIONS,
        DISABLE_INDICATIONS,
        WAIT_FOR_NOTIFICATION,
        WAIT_FOR_INDICATION,
        WAIT_FOR_READ,
        WAIT_FOR_WRITE,
        WAIT_FOR_CONDITION,
        SET_VALUE,
        SET_DESCRIPTOR_VALUE,
        READ_BATTERY_LEVEL,
        ENABLE_BATTERY_LEVEL_NOTIFICATIONS,
        DISABLE_BATTERY_LEVEL_NOTIFICATIONS,
        ENABLE_SERVICE_CHANGED_INDICATIONS,
        REQUEST_MTU,
        REQUEST_CONNECTION_PRIORITY,
        SET_PREFERRED_PHY,
        READ_PHY,
        READ_RSSI,
        REFRESH_CACHE,
        SLEEP
    }

    Request(Type type) {
        this.type = type;
        this.characteristic = null;
        this.descriptor = null;
        this.syncLock = new ConditionVariable(true);
    }

    Request(Type type, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        this.type = type;
        this.characteristic = bluetoothGattCharacteristic;
        this.descriptor = null;
        this.syncLock = new ConditionVariable(true);
    }

    Request(Type type, BluetoothGattDescriptor bluetoothGattDescriptor) {
        this.type = type;
        this.characteristic = null;
        this.descriptor = bluetoothGattDescriptor;
        this.syncLock = new ConditionVariable(true);
    }

    Request setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
        if (this.handler == null) {
            this.handler = requestHandler;
        }
        return this;
    }

    public Request setHandler(final Handler handler) {
        this.handler = new CallbackHandler() { // from class: no.nordicsemi.android.ble.Request.1
            @Override // no.nordicsemi.android.ble.CallbackHandler
            public void post(Runnable runnable) {
                handler.post(runnable);
            }

            @Override // no.nordicsemi.android.ble.CallbackHandler
            public void postDelayed(Runnable runnable, long j) {
                handler.postDelayed(runnable, j);
            }

            @Override // no.nordicsemi.android.ble.CallbackHandler
            public void removeCallbacks(Runnable runnable) {
                handler.removeCallbacks(runnable);
            }
        };
        return this;
    }

    static ConnectRequest connect(BluetoothDevice bluetoothDevice) {
        return new ConnectRequest(Type.CONNECT, bluetoothDevice);
    }

    static DisconnectRequest disconnect() {
        return new DisconnectRequest(Type.DISCONNECT);
    }

    @Deprecated
    public static SimpleRequest createBond() {
        return new SimpleRequest(Type.CREATE_BOND);
    }

    static SimpleRequest ensureBond() {
        return new SimpleRequest(Type.ENSURE_BOND);
    }

    @Deprecated
    public static SimpleRequest removeBond() {
        return new SimpleRequest(Type.REMOVE_BOND);
    }

    @Deprecated
    public static ReadRequest newReadRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new ReadRequest(Type.READ, bluetoothGattCharacteristic);
    }

    @Deprecated
    public static WriteRequest newWriteRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        return new WriteRequest(Type.WRITE, bluetoothGattCharacteristic, bArr, 0, bArr != null ? bArr.length : 0, bluetoothGattCharacteristic != null ? bluetoothGattCharacteristic.getWriteType() : 2);
    }

    @Deprecated
    public static WriteRequest newWriteRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i) {
        return new WriteRequest(Type.WRITE, bluetoothGattCharacteristic, bArr, 0, bArr != null ? bArr.length : 0, i);
    }

    @Deprecated
    public static WriteRequest newWriteRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i, int i2) {
        return new WriteRequest(Type.WRITE, bluetoothGattCharacteristic, bArr, i, i2, bluetoothGattCharacteristic != null ? bluetoothGattCharacteristic.getWriteType() : 2);
    }

    @Deprecated
    public static WriteRequest newWriteRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i, int i2, int i3) {
        return new WriteRequest(Type.WRITE, bluetoothGattCharacteristic, bArr, i, i2, i3);
    }

    @Deprecated
    public static ReadRequest newReadRequest(BluetoothGattDescriptor bluetoothGattDescriptor) {
        return new ReadRequest(Type.READ_DESCRIPTOR, bluetoothGattDescriptor);
    }

    @Deprecated
    public static WriteRequest newWriteRequest(BluetoothGattDescriptor bluetoothGattDescriptor, byte[] bArr) {
        return new WriteRequest(Type.WRITE_DESCRIPTOR, bluetoothGattDescriptor, bArr, 0, bArr != null ? bArr.length : 0);
    }

    @Deprecated
    public static WriteRequest newWriteRequest(BluetoothGattDescriptor bluetoothGattDescriptor, byte[] bArr, int i, int i2) {
        return new WriteRequest(Type.WRITE_DESCRIPTOR, bluetoothGattDescriptor, bArr, i, i2);
    }

    static ReliableWriteRequest newReliableWriteRequest() {
        return new ReliableWriteRequest();
    }

    static SimpleRequest newBeginReliableWriteRequest() {
        return new SimpleRequest(Type.BEGIN_RELIABLE_WRITE);
    }

    static SimpleRequest newExecuteReliableWriteRequest() {
        return new SimpleRequest(Type.EXECUTE_RELIABLE_WRITE);
    }

    static SimpleRequest newAbortReliableWriteRequest() {
        return new SimpleRequest(Type.ABORT_RELIABLE_WRITE);
    }

    static WriteRequest newNotificationRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        return new WriteRequest(Type.NOTIFY, bluetoothGattCharacteristic, bArr, 0, bArr != null ? bArr.length : 0);
    }

    static WriteRequest newNotificationRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i, int i2) {
        return new WriteRequest(Type.NOTIFY, bluetoothGattCharacteristic, bArr, i, i2);
    }

    static WriteRequest newIndicationRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        return new WriteRequest(Type.INDICATE, bluetoothGattCharacteristic, bArr, 0, bArr != null ? bArr.length : 0);
    }

    static WriteRequest newIndicationRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i, int i2) {
        return new WriteRequest(Type.INDICATE, bluetoothGattCharacteristic, bArr, i, i2);
    }

    @Deprecated
    public static WriteRequest newEnableNotificationsRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new WriteRequest(Type.ENABLE_NOTIFICATIONS, bluetoothGattCharacteristic);
    }

    @Deprecated
    public static WriteRequest newDisableNotificationsRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new WriteRequest(Type.DISABLE_NOTIFICATIONS, bluetoothGattCharacteristic);
    }

    @Deprecated
    public static WriteRequest newEnableIndicationsRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new WriteRequest(Type.ENABLE_INDICATIONS, bluetoothGattCharacteristic);
    }

    @Deprecated
    public static WriteRequest newDisableIndicationsRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new WriteRequest(Type.DISABLE_INDICATIONS, bluetoothGattCharacteristic);
    }

    @Deprecated
    public static WaitForValueChangedRequest newWaitForNotificationRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new WaitForValueChangedRequest(Type.WAIT_FOR_NOTIFICATION, bluetoothGattCharacteristic);
    }

    @Deprecated
    public static WaitForValueChangedRequest newWaitForIndicationRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new WaitForValueChangedRequest(Type.WAIT_FOR_INDICATION, bluetoothGattCharacteristic);
    }

    static WaitForValueChangedRequest newWaitForWriteRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new WaitForValueChangedRequest(Type.WAIT_FOR_WRITE, bluetoothGattCharacteristic);
    }

    static WaitForValueChangedRequest newWaitForWriteRequest(BluetoothGattDescriptor bluetoothGattDescriptor) {
        return new WaitForValueChangedRequest(Type.WAIT_FOR_WRITE, bluetoothGattDescriptor);
    }

    static WaitForReadRequest newWaitForReadRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new WaitForReadRequest(Type.WAIT_FOR_READ, bluetoothGattCharacteristic);
    }

    static WaitForReadRequest newWaitForReadRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        return new WaitForReadRequest(Type.WAIT_FOR_READ, bluetoothGattCharacteristic, bArr, 0, bArr != null ? bArr.length : 0);
    }

    static WaitForReadRequest newWaitForReadRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i, int i2) {
        return new WaitForReadRequest(Type.WAIT_FOR_READ, bluetoothGattCharacteristic, bArr, i, i2);
    }

    static WaitForReadRequest newWaitForReadRequest(BluetoothGattDescriptor bluetoothGattDescriptor) {
        return new WaitForReadRequest(Type.WAIT_FOR_READ, bluetoothGattDescriptor);
    }

    static WaitForReadRequest newWaitForReadRequest(BluetoothGattDescriptor bluetoothGattDescriptor, byte[] bArr) {
        return new WaitForReadRequest(Type.WAIT_FOR_READ, bluetoothGattDescriptor, bArr, 0, bArr != null ? bArr.length : 0);
    }

    static WaitForReadRequest newWaitForReadRequest(BluetoothGattDescriptor bluetoothGattDescriptor, byte[] bArr, int i, int i2) {
        return new WaitForReadRequest(Type.WAIT_FOR_READ, bluetoothGattDescriptor, bArr, i, i2);
    }

    static <T> ConditionalWaitRequest<T> newConditionalWaitRequest(ConditionalWaitRequest.Condition<T> condition, T t) {
        return new ConditionalWaitRequest<>(Type.WAIT_FOR_CONDITION, condition, t);
    }

    static SetValueRequest newSetValueRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        return new SetValueRequest(Type.SET_VALUE, bluetoothGattCharacteristic, bArr, 0, bArr != null ? bArr.length : 0);
    }

    static SetValueRequest newSetValueRequest(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i, int i2) {
        return new SetValueRequest(Type.SET_VALUE, bluetoothGattCharacteristic, bArr, i, i2);
    }

    static SetValueRequest newSetValueRequest(BluetoothGattDescriptor bluetoothGattDescriptor, byte[] bArr) {
        return new SetValueRequest(Type.SET_DESCRIPTOR_VALUE, bluetoothGattDescriptor, bArr, 0, bArr != null ? bArr.length : 0);
    }

    static SetValueRequest newSetValueRequest(BluetoothGattDescriptor bluetoothGattDescriptor, byte[] bArr, int i, int i2) {
        return new SetValueRequest(Type.SET_DESCRIPTOR_VALUE, bluetoothGattDescriptor, bArr, i, i2);
    }

    @Deprecated
    public static ReadRequest newReadBatteryLevelRequest() {
        return new ReadRequest(Type.READ_BATTERY_LEVEL);
    }

    @Deprecated
    public static WriteRequest newEnableBatteryLevelNotificationsRequest() {
        return new WriteRequest(Type.ENABLE_BATTERY_LEVEL_NOTIFICATIONS);
    }

    @Deprecated
    public static WriteRequest newDisableBatteryLevelNotificationsRequest() {
        return new WriteRequest(Type.DISABLE_BATTERY_LEVEL_NOTIFICATIONS);
    }

    static WriteRequest newEnableServiceChangedIndicationsRequest() {
        return new WriteRequest(Type.ENABLE_SERVICE_CHANGED_INDICATIONS);
    }

    @Deprecated
    public static MtuRequest newMtuRequest(int i) {
        return new MtuRequest(Type.REQUEST_MTU, i);
    }

    @Deprecated
    public static ConnectionPriorityRequest newConnectionPriorityRequest(int i) {
        return new ConnectionPriorityRequest(Type.REQUEST_CONNECTION_PRIORITY, i);
    }

    @Deprecated
    public static PhyRequest newSetPreferredPhyRequest(int i, int i2, int i3) {
        return new PhyRequest(Type.SET_PREFERRED_PHY, i, i2, i3);
    }

    @Deprecated
    public static PhyRequest newReadPhyRequest() {
        return new PhyRequest(Type.READ_PHY);
    }

    @Deprecated
    public static ReadRssiRequest newReadRssiRequest() {
        return new ReadRssiRequest(Type.READ_RSSI);
    }

    @Deprecated
    public static SimpleRequest newRefreshCacheRequest() {
        return new SimpleRequest(Type.REFRESH_CACHE);
    }

    @Deprecated
    public static SleepRequest newSleepRequest(long j) {
        return new SleepRequest(Type.SLEEP, j);
    }

    public Request done(SuccessCallback successCallback) {
        this.successCallback = successCallback;
        return this;
    }

    public Request fail(FailCallback failCallback) {
        this.failCallback = failCallback;
        return this;
    }

    void internalBefore(BeforeCallback beforeCallback) {
        this.internalBeforeCallback = beforeCallback;
    }

    void internalSuccess(SuccessCallback successCallback) {
        this.internalSuccessCallback = successCallback;
    }

    void internalFail(FailCallback failCallback) {
        this.internalFailCallback = failCallback;
    }

    public Request invalid(InvalidRequestCallback invalidRequestCallback) {
        this.invalidRequestCallback = invalidRequestCallback;
        return this;
    }

    public Request before(BeforeCallback beforeCallback) {
        this.beforeCallback = beforeCallback;
        return this;
    }

    public void enqueue() {
        this.requestHandler.enqueue(this);
    }

    void notifyStarted(final BluetoothDevice bluetoothDevice) {
        if (this.started) {
            return;
        }
        this.started = true;
        BeforeCallback beforeCallback = this.internalBeforeCallback;
        if (beforeCallback != null) {
            beforeCallback.onRequestStarted(bluetoothDevice);
        }
        this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$Request$iGoHDgsdsRBKxRdgJdmCHZvtNv4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyStarted$0$Request(bluetoothDevice);
            }
        });
    }

    public /* synthetic */ void lambda$notifyStarted$0$Request(BluetoothDevice bluetoothDevice) {
        BeforeCallback beforeCallback = this.beforeCallback;
        if (beforeCallback != null) {
            beforeCallback.onRequestStarted(bluetoothDevice);
        }
    }

    boolean notifySuccess(final BluetoothDevice bluetoothDevice) {
        if (this.finished) {
            return false;
        }
        this.finished = true;
        SuccessCallback successCallback = this.internalSuccessCallback;
        if (successCallback != null) {
            successCallback.onRequestCompleted(bluetoothDevice);
        }
        this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$Request$b53xfrphHtm2A9q9pSy2hUJQMgo
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifySuccess$1$Request(bluetoothDevice);
            }
        });
        return true;
    }

    public /* synthetic */ void lambda$notifySuccess$1$Request(BluetoothDevice bluetoothDevice) {
        SuccessCallback successCallback = this.successCallback;
        if (successCallback != null) {
            successCallback.onRequestCompleted(bluetoothDevice);
        }
    }

    void notifyFail(final BluetoothDevice bluetoothDevice, final int i) {
        if (this.finished) {
            return;
        }
        this.finished = true;
        FailCallback failCallback = this.internalFailCallback;
        if (failCallback != null) {
            failCallback.onRequestFailed(bluetoothDevice, i);
        }
        this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$Request$EUIJZwpY6U4LQ-0FNXV1TUigki0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyFail$2$Request(bluetoothDevice, i);
            }
        });
    }

    public /* synthetic */ void lambda$notifyFail$2$Request(BluetoothDevice bluetoothDevice, int i) {
        FailCallback failCallback = this.failCallback;
        if (failCallback != null) {
            failCallback.onRequestFailed(bluetoothDevice, i);
        }
    }

    void notifyInvalidRequest() {
        if (this.finished) {
            return;
        }
        this.finished = true;
        this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$Request$uAQObLhn9Aak6274o65kuA3ZH7s
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyInvalidRequest$3$Request();
            }
        });
    }

    public /* synthetic */ void lambda$notifyInvalidRequest$3$Request() {
        InvalidRequestCallback invalidRequestCallback = this.invalidRequestCallback;
        if (invalidRequestCallback != null) {
            invalidRequestCallback.onInvalidRequest();
        }
    }

    static void assertNotMainThread() throws IllegalStateException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Cannot execute synchronous operation from the UI thread.");
        }
    }

    final class RequestCallback implements SuccessCallback, FailCallback, InvalidRequestCallback {
        static final int REASON_REQUEST_INVALID = -1000000;
        int status = 0;

        RequestCallback() {
        }

        @Override // no.nordicsemi.android.ble.callback.SuccessCallback
        public void onRequestCompleted(BluetoothDevice bluetoothDevice) {
            Request.this.syncLock.open();
        }

        @Override // no.nordicsemi.android.ble.callback.FailCallback
        public void onRequestFailed(BluetoothDevice bluetoothDevice, int i) {
            this.status = i;
            Request.this.syncLock.open();
        }

        @Override // no.nordicsemi.android.ble.callback.InvalidRequestCallback
        public void onInvalidRequest() {
            this.status = REASON_REQUEST_INVALID;
            Request.this.syncLock.open();
        }

        boolean isSuccess() {
            return this.status == 0;
        }
    }
}
