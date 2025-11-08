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
public final class ReadRequest extends SimpleValueRequest<DataReceivedCallback> implements Operation {
    private DataStream buffer;
    private int count;
    private DataMerger dataMerger;
    private DataFilter filter;
    private ReadProgressCallback progressCallback;

    ReadRequest(Request.Type type) {
        super(type);
        this.count = 0;
    }

    ReadRequest(Request.Type type, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super(type, bluetoothGattCharacteristic);
        this.count = 0;
    }

    ReadRequest(Request.Type type, BluetoothGattDescriptor bluetoothGattDescriptor) {
        super(type, bluetoothGattDescriptor);
        this.count = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // no.nordicsemi.android.ble.Request
    public ReadRequest setRequestHandler(RequestHandler requestHandler) {
        super.setRequestHandler(requestHandler);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public ReadRequest setHandler(Handler handler) {
        super.setHandler(handler);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public ReadRequest done(SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public ReadRequest fail(FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public ReadRequest invalid(InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public ReadRequest before(BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.SimpleValueRequest
    public ReadRequest with(DataReceivedCallback dataReceivedCallback) {
        super.with((ReadRequest) dataReceivedCallback);
        return this;
    }

    public ReadRequest filter(DataFilter dataFilter) {
        this.filter = dataFilter;
        return this;
    }

    public ReadRequest merge(DataMerger dataMerger) {
        this.dataMerger = dataMerger;
        this.progressCallback = null;
        return this;
    }

    public ReadRequest merge(DataMerger dataMerger, ReadProgressCallback readProgressCallback) {
        this.dataMerger = dataMerger;
        this.progressCallback = readProgressCallback;
        return this;
    }

    public <E extends ProfileReadResponse> E awaitValid(Class<E> cls) throws InvalidDataException, DeviceDisconnectedException, RequestFailedException, InvalidRequestException, BluetoothDisabledException {
        E e = (E) await((Class) cls);
        if (e.isValid()) {
            return e;
        }
        throw new InvalidDataException(e);
    }

    public <E extends ProfileReadResponse> E awaitValid(E e) throws InvalidDataException, DeviceDisconnectedException, RequestFailedException, InvalidRequestException, BluetoothDisabledException {
        await((ReadRequest) e);
        if (e.isValid()) {
            return e;
        }
        throw new InvalidDataException(e);
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
            this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$ReadRequest$406mAhxV10eMfmbBI4l07-ac_9c
                @Override // java.lang.Runnable
                public final void run() {
                    dataReceivedCallback.onDataReceived(bluetoothDevice, data);
                }
            });
            return;
        }
        this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$ReadRequest$yMu30kyrT2tdHMA29DQddHhPbuY
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyValueChanged$1$ReadRequest(bluetoothDevice, bArr);
            }
        });
        if (this.buffer == null) {
            this.buffer = new DataStream();
        }
        DataMerger dataMerger = this.dataMerger;
        DataStream dataStream = this.buffer;
        int i = this.count;
        this.count = i + 1;
        if (dataMerger.merge(dataStream, bArr, i)) {
            final Data data2 = this.buffer.toData();
            this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$ReadRequest$Z0-tDOdHPeuyJGdUIeSAltGO_qc
                @Override // java.lang.Runnable
                public final void run() {
                    dataReceivedCallback.onDataReceived(bluetoothDevice, data2);
                }
            });
            this.buffer = null;
            this.count = 0;
        }
    }

    public /* synthetic */ void lambda$notifyValueChanged$1$ReadRequest(BluetoothDevice bluetoothDevice, byte[] bArr) {
        ReadProgressCallback readProgressCallback = this.progressCallback;
        if (readProgressCallback != null) {
            readProgressCallback.onPacketReceived(bluetoothDevice, bArr, this.count);
        }
    }

    boolean hasMore() {
        return this.count > 0;
    }
}
