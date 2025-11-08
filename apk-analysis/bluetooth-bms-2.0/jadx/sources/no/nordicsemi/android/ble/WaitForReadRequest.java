package no.nordicsemi.android.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Handler;
import no.nordicsemi.android.ble.Request;
import no.nordicsemi.android.ble.callback.BeforeCallback;
import no.nordicsemi.android.ble.callback.DataSentCallback;
import no.nordicsemi.android.ble.callback.FailCallback;
import no.nordicsemi.android.ble.callback.InvalidRequestCallback;
import no.nordicsemi.android.ble.callback.SuccessCallback;
import no.nordicsemi.android.ble.callback.WriteProgressCallback;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.data.DataSplitter;
import no.nordicsemi.android.ble.data.DefaultMtuSplitter;

/* loaded from: classes.dex */
public final class WaitForReadRequest extends AwaitingRequest<DataSentCallback> implements Operation {
    private static final DataSplitter MTU_SPLITTER = new DefaultMtuSplitter();
    private boolean complete;
    private int count;
    private byte[] data;
    private DataSplitter dataSplitter;
    private byte[] nextChunk;
    private WriteProgressCallback progressCallback;

    WaitForReadRequest(Request.Type type, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super(type, bluetoothGattCharacteristic);
        this.count = 0;
        this.complete = false;
        this.data = null;
        this.complete = true;
    }

    WaitForReadRequest(Request.Type type, BluetoothGattDescriptor bluetoothGattDescriptor) {
        super(type, bluetoothGattDescriptor);
        this.count = 0;
        this.complete = false;
        this.data = null;
        this.complete = true;
    }

    WaitForReadRequest(Request.Type type, BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i, int i2) {
        super(type, bluetoothGattCharacteristic);
        this.count = 0;
        this.complete = false;
        this.data = Bytes.copy(bArr, i, i2);
    }

    WaitForReadRequest(Request.Type type, BluetoothGattDescriptor bluetoothGattDescriptor, byte[] bArr, int i, int i2) {
        super(type, bluetoothGattDescriptor);
        this.count = 0;
        this.complete = false;
        this.data = Bytes.copy(bArr, i, i2);
    }

    void setDataIfNull(byte[] bArr) {
        if (this.data == null) {
            this.data = bArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // no.nordicsemi.android.ble.TimeoutableRequest, no.nordicsemi.android.ble.Request
    public WaitForReadRequest setRequestHandler(RequestHandler requestHandler) {
        super.setRequestHandler(requestHandler);
        return this;
    }

    @Override // no.nordicsemi.android.ble.TimeoutableRequest, no.nordicsemi.android.ble.Request
    public WaitForReadRequest setHandler(Handler handler) {
        super.setHandler(handler);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public WaitForReadRequest done(SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public WaitForReadRequest fail(FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public WaitForReadRequest invalid(InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public WaitForReadRequest before(BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.TimeoutableValueRequest
    public WaitForReadRequest with(DataSentCallback dataSentCallback) {
        super.with((WaitForReadRequest) dataSentCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.AwaitingRequest
    public AwaitingRequest<DataSentCallback> trigger(Operation operation) {
        super.trigger(operation);
        return this;
    }

    public WaitForReadRequest split(DataSplitter dataSplitter) {
        this.dataSplitter = dataSplitter;
        this.progressCallback = null;
        return this;
    }

    public WaitForReadRequest split(DataSplitter dataSplitter, WriteProgressCallback writeProgressCallback) {
        this.dataSplitter = dataSplitter;
        this.progressCallback = writeProgressCallback;
        return this;
    }

    public WaitForReadRequest split() {
        this.dataSplitter = MTU_SPLITTER;
        this.progressCallback = null;
        return this;
    }

    public WaitForReadRequest split(WriteProgressCallback writeProgressCallback) {
        this.dataSplitter = MTU_SPLITTER;
        this.progressCallback = writeProgressCallback;
        return this;
    }

    byte[] getData(int i) {
        byte[] bArr;
        DataSplitter dataSplitter = this.dataSplitter;
        if (dataSplitter == null || (bArr = this.data) == null) {
            this.complete = true;
            return this.data;
        }
        int i2 = i - 3;
        byte[] bArrChunk = this.nextChunk;
        if (bArrChunk == null) {
            bArrChunk = dataSplitter.chunk(bArr, this.count, i2);
        }
        if (bArrChunk != null) {
            this.nextChunk = this.dataSplitter.chunk(this.data, this.count + 1, i2);
        }
        if (this.nextChunk == null) {
            this.complete = true;
        }
        return bArrChunk;
    }

    void notifyPacketRead(final BluetoothDevice bluetoothDevice, final byte[] bArr) {
        this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$WaitForReadRequest$nk4l7MZKfDhTD4CP_klcZGFi34c
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyPacketRead$0$WaitForReadRequest(bluetoothDevice, bArr);
            }
        });
        this.count++;
    }

    public /* synthetic */ void lambda$notifyPacketRead$0$WaitForReadRequest(BluetoothDevice bluetoothDevice, byte[] bArr) {
        WriteProgressCallback writeProgressCallback = this.progressCallback;
        if (writeProgressCallback != null) {
            writeProgressCallback.onPacketSent(bluetoothDevice, bArr, this.count);
        }
    }

    @Override // no.nordicsemi.android.ble.TimeoutableRequest, no.nordicsemi.android.ble.Request
    boolean notifySuccess(final BluetoothDevice bluetoothDevice) {
        this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$WaitForReadRequest$mihFeEevJ1FfYVCbqi_4ZsmHdi0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifySuccess$1$WaitForReadRequest(bluetoothDevice);
            }
        });
        return super.notifySuccess(bluetoothDevice);
    }

    public /* synthetic */ void lambda$notifySuccess$1$WaitForReadRequest(BluetoothDevice bluetoothDevice) {
        if (this.valueCallback != 0) {
            ((DataSentCallback) this.valueCallback).onDataSent(bluetoothDevice, new Data(this.data));
        }
    }

    boolean hasMore() {
        return !this.complete;
    }
}
