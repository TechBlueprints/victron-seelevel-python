package no.nordicsemi.android.ble;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import no.nordicsemi.android.ble.Request;
import no.nordicsemi.android.ble.exception.BluetoothDisabledException;
import no.nordicsemi.android.ble.exception.DeviceDisconnectedException;
import no.nordicsemi.android.ble.exception.InvalidRequestException;
import no.nordicsemi.android.ble.exception.RequestFailedException;

/* loaded from: classes.dex */
public abstract class TimeoutableValueRequest<T> extends TimeoutableRequest {
    T valueCallback;

    TimeoutableValueRequest(Request.Type type) {
        super(type);
    }

    TimeoutableValueRequest(Request.Type type, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super(type, bluetoothGattCharacteristic);
    }

    TimeoutableValueRequest(Request.Type type, BluetoothGattDescriptor bluetoothGattDescriptor) {
        super(type, bluetoothGattDescriptor);
    }

    @Override // no.nordicsemi.android.ble.TimeoutableRequest
    public TimeoutableValueRequest<T> timeout(long j) {
        super.timeout(j);
        return this;
    }

    public TimeoutableValueRequest<T> with(T t) {
        this.valueCallback = t;
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <E extends T> E await(E e) throws InterruptedException, DeviceDisconnectedException, RequestFailedException, InvalidRequestException, BluetoothDisabledException {
        assertNotMainThread();
        T t = this.valueCallback;
        try {
            with(e).await();
            return e;
        } finally {
            this.valueCallback = t;
        }
    }

    public <E extends T> E await(Class<E> cls) throws InterruptedException, DeviceDisconnectedException, RequestFailedException, InvalidRequestException, BluetoothDisabledException {
        assertNotMainThread();
        try {
            return (E) await((TimeoutableValueRequest<T>) cls.newInstance());
        } catch (IllegalAccessException unused) {
            throw new IllegalArgumentException("Couldn't instantiate " + cls.getCanonicalName() + " class. Is the default constructor accessible?");
        } catch (InstantiationException unused2) {
            throw new IllegalArgumentException("Couldn't instantiate " + cls.getCanonicalName() + " class. Does it have a default constructor with no arguments?");
        }
    }

    @Deprecated
    public <E extends T> E await(Class<E> cls, long j) throws InterruptedException, DeviceDisconnectedException, RequestFailedException, InvalidRequestException, BluetoothDisabledException {
        return (E) timeout(j).await((Class) cls);
    }

    @Deprecated
    public <E extends T> E await(E e, long j) throws InterruptedException, DeviceDisconnectedException, RequestFailedException, InvalidRequestException, BluetoothDisabledException {
        return (E) timeout(j).await((TimeoutableValueRequest<T>) e);
    }
}
