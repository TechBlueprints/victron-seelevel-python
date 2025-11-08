package no.nordicsemi.android.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Handler;
import java.util.Arrays;
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
public final class WriteRequest extends SimpleValueRequest<DataSentCallback> implements Operation {
    private static final DataSplitter MTU_SPLITTER = new DefaultMtuSplitter();
    private boolean complete;
    private int count;
    private byte[] currentChunk;
    private final byte[] data;
    private DataSplitter dataSplitter;
    private byte[] nextChunk;
    private WriteProgressCallback progressCallback;
    private final int writeType;

    WriteRequest(Request.Type type) {
        this(type, null);
    }

    WriteRequest(Request.Type type, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super(type, bluetoothGattCharacteristic);
        this.count = 0;
        this.complete = false;
        this.data = null;
        this.writeType = 0;
        this.complete = true;
    }

    WriteRequest(Request.Type type, BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i, int i2, int i3) {
        super(type, bluetoothGattCharacteristic);
        this.count = 0;
        this.complete = false;
        this.data = Bytes.copy(bArr, i, i2);
        this.writeType = i3;
    }

    WriteRequest(Request.Type type, BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i, int i2) {
        super(type, bluetoothGattCharacteristic);
        this.count = 0;
        this.complete = false;
        this.data = Bytes.copy(bArr, i, i2);
        this.writeType = 0;
    }

    WriteRequest(Request.Type type, BluetoothGattDescriptor bluetoothGattDescriptor, byte[] bArr, int i, int i2) {
        super(type, bluetoothGattDescriptor);
        this.count = 0;
        this.complete = false;
        this.data = Bytes.copy(bArr, i, i2);
        this.writeType = 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // no.nordicsemi.android.ble.Request
    public WriteRequest setRequestHandler(RequestHandler requestHandler) {
        super.setRequestHandler(requestHandler);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public WriteRequest setHandler(Handler handler) {
        super.setHandler(handler);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public WriteRequest done(SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public WriteRequest fail(FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public WriteRequest invalid(InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public WriteRequest before(BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.SimpleValueRequest
    public WriteRequest with(DataSentCallback dataSentCallback) {
        super.with((WriteRequest) dataSentCallback);
        return this;
    }

    public WriteRequest split(DataSplitter dataSplitter) {
        this.dataSplitter = dataSplitter;
        this.progressCallback = null;
        return this;
    }

    public WriteRequest split(DataSplitter dataSplitter, WriteProgressCallback writeProgressCallback) {
        this.dataSplitter = dataSplitter;
        this.progressCallback = writeProgressCallback;
        return this;
    }

    public WriteRequest split() {
        this.dataSplitter = MTU_SPLITTER;
        this.progressCallback = null;
        return this;
    }

    public WriteRequest split(WriteProgressCallback writeProgressCallback) {
        this.dataSplitter = MTU_SPLITTER;
        this.progressCallback = writeProgressCallback;
        return this;
    }

    void forceSplit() {
        if (this.dataSplitter == null) {
            split();
        }
    }

    byte[] getData(int i) {
        if (this.dataSplitter == null || this.data == null) {
            this.complete = true;
            byte[] bArr = this.data;
            this.currentChunk = bArr;
            return bArr;
        }
        int i2 = this.writeType != 4 ? i - 3 : i - 12;
        byte[] bArrChunk = this.nextChunk;
        if (bArrChunk == null) {
            bArrChunk = this.dataSplitter.chunk(this.data, this.count, i2);
        }
        if (bArrChunk != null) {
            this.nextChunk = this.dataSplitter.chunk(this.data, this.count + 1, i2);
        }
        if (this.nextChunk == null) {
            this.complete = true;
        }
        this.currentChunk = bArrChunk;
        return bArrChunk;
    }

    boolean notifyPacketSent(final BluetoothDevice bluetoothDevice, final byte[] bArr) {
        this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$WriteRequest$3-9dT9-YAUF6sJb8Zgu_ZM8t_Ew
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyPacketSent$0$WriteRequest(bluetoothDevice, bArr);
            }
        });
        this.count++;
        if (this.complete) {
            this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$WriteRequest$4bRQ2ogCIRbLKORMpi_V3KgqW-E
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notifyPacketSent$1$WriteRequest(bluetoothDevice);
                }
            });
        }
        return Arrays.equals(bArr, this.currentChunk);
    }

    public /* synthetic */ void lambda$notifyPacketSent$0$WriteRequest(BluetoothDevice bluetoothDevice, byte[] bArr) {
        WriteProgressCallback writeProgressCallback = this.progressCallback;
        if (writeProgressCallback != null) {
            writeProgressCallback.onPacketSent(bluetoothDevice, bArr, this.count);
        }
    }

    public /* synthetic */ void lambda$notifyPacketSent$1$WriteRequest(BluetoothDevice bluetoothDevice) {
        if (this.valueCallback != 0) {
            ((DataSentCallback) this.valueCallback).onDataSent(bluetoothDevice, new Data(this.data));
        }
    }

    boolean hasMore() {
        return !this.complete;
    }

    int getWriteType() {
        return this.writeType;
    }
}
