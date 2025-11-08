package no.nordicsemi.android.ble;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Handler;
import no.nordicsemi.android.ble.Request;
import no.nordicsemi.android.ble.callback.BeforeCallback;
import no.nordicsemi.android.ble.callback.FailCallback;
import no.nordicsemi.android.ble.callback.InvalidRequestCallback;
import no.nordicsemi.android.ble.callback.SuccessCallback;

/* loaded from: classes.dex */
public final class SetValueRequest extends SimpleRequest {
    private final byte[] data;
    private boolean longReadSupported;

    SetValueRequest(Request.Type type, BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i, int i2) {
        super(type, bluetoothGattCharacteristic);
        this.longReadSupported = true;
        this.data = Bytes.copy(bArr, i, i2);
    }

    SetValueRequest(Request.Type type, BluetoothGattDescriptor bluetoothGattDescriptor, byte[] bArr, int i, int i2) {
        super(type, bluetoothGattDescriptor);
        this.longReadSupported = true;
        this.data = Bytes.copy(bArr, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // no.nordicsemi.android.ble.Request
    public SetValueRequest setRequestHandler(RequestHandler requestHandler) {
        super.setRequestHandler(requestHandler);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public SetValueRequest setHandler(Handler handler) {
        super.setHandler(handler);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public SetValueRequest done(SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public SetValueRequest fail(FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public SetValueRequest invalid(InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // no.nordicsemi.android.ble.Request
    public SetValueRequest before(BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    public SetValueRequest allowLongRead(boolean z) {
        this.longReadSupported = z;
        return this;
    }

    byte[] getData(int i) {
        int i2 = this.longReadSupported ? 512 : i - 3;
        byte[] bArr = this.data;
        return bArr.length < i2 ? bArr : Bytes.copy(bArr, 0, i2);
    }
}
