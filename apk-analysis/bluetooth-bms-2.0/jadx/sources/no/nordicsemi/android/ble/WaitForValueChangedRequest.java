package no.nordicsemi.android.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Handler;
import no.nordicsemi.android.ble.Request;
import no.nordicsemi.android.ble.callback.BeforeCallback;
import no.nordicsemi.android.ble.callback.DataReceivedCallback;
import no.nordicsemi.android.ble.callback.FailCallback;
import no.nordicsemi.android.ble.callback.InvalidRequestCallback;
import no.nordicsemi.android.ble.callback.ReadProgressCallback;
import no.nordicsemi.android.ble.callback.SuccessCallback;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.data.DataFilter;
import no.nordicsemi.android.ble.data.DataMerger;
import no.nordicsemi.android.ble.data.DataStream;
import no.nordicsemi.android.ble.exception.BluetoothDisabledException;
import no.nordicsemi.android.ble.exception.DeviceDisconnectedException;
import no.nordicsemi.android.ble.exception.InvalidDataException;
import no.nordicsemi.android.ble.exception.InvalidRequestException;
import no.nordicsemi.android.ble.exception.RequestFailedException;

/* loaded from: classes.dex */
public final class WaitForValueChangedRequest extends AwaitingRequest<DataReceivedCallback> implements Operation {
    private boolean bluetoothDisabled;
    private DataStream buffer;
    private int count;
    private DataMerger dataMerger;
    private boolean deviceDisconnected;
    private DataFilter filter;
    private ReadProgressCallback progressCallback;

    WaitForValueChangedRequest(Request.Type type, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super(type, bluetoothGattCharacteristic);
        this.count = 0;
    }

    WaitForValueChangedRequest(Request.Type type, BluetoothGattDescriptor bluetoothGattDescriptor) {
        super(type, bluetoothGattDescriptor);
        this.count = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // no.nordicsemi.android.ble.TimeoutableRequest, no.nordicsemi.android.ble.Request
    public WaitForValueChangedRequest setRequestHandler(RequestHandler requestHandler) {
        super.setRequestHandler(requestHandler);
        return this;
    }

    @Override // no.nordicsemi.android.ble.TimeoutableRequest, no.nordicsemi.android.ble.Request
    public WaitForValueChangedRequest setHandler(Handler handler) {
        super.setHandler(handler);
        return this;
    }

    @Override // no.nordicsemi.android.ble.TimeoutableValueRequest, no.nordicsemi.android.ble.TimeoutableRequest
    public WaitForValueChangedRequest timeout(long j) {
        super.timeout(j);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public WaitForValueChangedRequest done(SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public WaitForValueChangedRequest fail(FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public WaitForValueChangedRequest invalid(InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public WaitForValueChangedRequest before(BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.TimeoutableValueRequest
    public WaitForValueChangedRequest with(DataReceivedCallback dataReceivedCallback) {
        super.with((WaitForValueChangedRequest) dataReceivedCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.AwaitingRequest
    public AwaitingRequest<DataReceivedCallback> trigger(Operation operation) {
        super.trigger(operation);
        return this;
    }

    public WaitForValueChangedRequest filter(DataFilter dataFilter) {
        this.filter = dataFilter;
        return this;
    }

    public WaitForValueChangedRequest merge(DataMerger dataMerger) {
        this.dataMerger = dataMerger;
        this.progressCallback = null;
        return this;
    }

    public WaitForValueChangedRequest merge(DataMerger dataMerger, ReadProgressCallback readProgressCallback) {
        this.dataMerger = dataMerger;
        this.progressCallback = readProgressCallback;
        return this;
    }

    public <E extends ProfileReadResponse> E awaitValid(E e) throws InterruptedException, InvalidDataException, DeviceDisconnectedException, RequestFailedException, InvalidRequestException, BluetoothDisabledException {
        E e2 = (E) await((WaitForValueChangedRequest) e);
        if (e2 == null || e2.isValid()) {
            return e2;
        }
        throw new InvalidDataException(e2);
    }

    public <E extends ProfileReadResponse> E awaitValid(Class<E> cls) throws InterruptedException, InvalidDataException, DeviceDisconnectedException, RequestFailedException, InvalidRequestException, BluetoothDisabledException {
        E e = (E) await((Class) cls);
        if (e == null || e.isValid()) {
            return e;
        }
        throw new InvalidDataException(e);
    }

    @Deprecated
    public <E extends ProfileReadResponse> E awaitValid(Class<E> cls, long j) throws InterruptedException, InvalidDataException, DeviceDisconnectedException, RequestFailedException, InvalidRequestException, BluetoothDisabledException {
        return (E) timeout(j).awaitValid(cls);
    }

    @Deprecated
    public <E extends ProfileReadResponse> E awaitValid(E e, long j) throws InterruptedException, InvalidDataException, DeviceDisconnectedException, RequestFailedException, InvalidRequestException, BluetoothDisabledException {
        return (E) timeout(j).awaitValid((WaitForValueChangedRequest) e);
    }

    boolean matches(byte[] bArr) {
        DataFilter dataFilter = this.filter;
        return dataFilter == null || dataFilter.filter(bArr);
    }

    void notifyValueChanged(final BluetoothDevice bluetoothDevice, final byte[] bArr) {
        final DataReceivedCallback dataReceivedCallback = (DataReceivedCallback) this.valueCallback;
        if (dataReceivedCallback == null) {
            return;
        }
        if (this.dataMerger == null) {
            final Data data = new Data(bArr);
            this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$WaitForValueChangedRequest$K1cINV5bhYmtEm38iuzf0syFLqM
                @Override // java.lang.Runnable
                public final void run() {
                    dataReceivedCallback.onDataReceived(bluetoothDevice, data);
                }
            });
            return;
        }
        final int i = this.count;
        this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$WaitForValueChangedRequest$Ih-oFX0VCjIJFo--SEv3MK5eHLE
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyValueChanged$1$WaitForValueChangedRequest(bluetoothDevice, bArr, i);
            }
        });
        if (this.buffer == null) {
            this.buffer = new DataStream();
        }
        DataMerger dataMerger = this.dataMerger;
        DataStream dataStream = this.buffer;
        int i2 = this.count;
        this.count = i2 + 1;
        if (dataMerger.merge(dataStream, bArr, i2)) {
            final Data data2 = this.buffer.toData();
            this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$WaitForValueChangedRequest$NIJSHTUe0XmUDU5V331pjaDj_2I
                @Override // java.lang.Runnable
                public final void run() {
                    dataReceivedCallback.onDataReceived(bluetoothDevice, data2);
                }
            });
            this.buffer = null;
            this.count = 0;
        }
    }

    public /* synthetic */ void lambda$notifyValueChanged$1$WaitForValueChangedRequest(BluetoothDevice bluetoothDevice, byte[] bArr, int i) {
        ReadProgressCallback readProgressCallback = this.progressCallback;
        if (readProgressCallback != null) {
            readProgressCallback.onPacketReceived(bluetoothDevice, bArr, i);
        }
    }

    boolean hasMore() {
        return this.count > 0;
    }
}
