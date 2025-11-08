package no.nordicsemi.android.ble;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import no.nordicsemi.android.ble.callback.DataReceivedCallback;
import no.nordicsemi.android.ble.callback.ReadProgressCallback;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.data.DataFilter;
import no.nordicsemi.android.ble.data.DataMerger;
import no.nordicsemi.android.ble.data.DataStream;

/* loaded from: classes.dex */
public class ValueChangedCallback {
    private DataStream buffer;
    private int count = 0;
    private DataMerger dataMerger;
    private DataFilter filter;
    private CallbackHandler handler;
    private ReadProgressCallback progressCallback;
    private DataReceivedCallback valueCallback;

    ValueChangedCallback(CallbackHandler callbackHandler) {
        this.handler = callbackHandler;
    }

    public ValueChangedCallback setHandler(final Handler handler) {
        this.handler = new CallbackHandler() { // from class: no.nordicsemi.android.ble.ValueChangedCallback.1
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

    public ValueChangedCallback with(DataReceivedCallback dataReceivedCallback) {
        this.valueCallback = dataReceivedCallback;
        return this;
    }

    public ValueChangedCallback filter(DataFilter dataFilter) {
        this.filter = dataFilter;
        return this;
    }

    public ValueChangedCallback merge(DataMerger dataMerger) {
        this.dataMerger = dataMerger;
        this.progressCallback = null;
        return this;
    }

    public ValueChangedCallback merge(DataMerger dataMerger, ReadProgressCallback readProgressCallback) {
        this.dataMerger = dataMerger;
        this.progressCallback = readProgressCallback;
        return this;
    }

    ValueChangedCallback free() {
        this.valueCallback = null;
        this.dataMerger = null;
        this.progressCallback = null;
        this.buffer = null;
        return this;
    }

    boolean matches(byte[] bArr) {
        DataFilter dataFilter = this.filter;
        return dataFilter == null || dataFilter.filter(bArr);
    }

    void notifyValueChanged(final BluetoothDevice bluetoothDevice, final byte[] bArr) {
        final DataReceivedCallback dataReceivedCallback = this.valueCallback;
        if (dataReceivedCallback == null) {
            return;
        }
        if (this.dataMerger == null) {
            final Data data = new Data(bArr);
            this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$ValueChangedCallback$4rcq0bbOhcTVqpLmftoy8pUEJ-Q
                @Override // java.lang.Runnable
                public final void run() {
                    dataReceivedCallback.onDataReceived(bluetoothDevice, data);
                }
            });
            return;
        }
        this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$ValueChangedCallback$hrkXYcC4bGNz18A7TyoYUTBk430
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyValueChanged$1$ValueChangedCallback(bluetoothDevice, bArr);
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
            this.handler.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$ValueChangedCallback$-WyEri9uRn7LWDhLhNsuhDQ89xM
                @Override // java.lang.Runnable
                public final void run() {
                    dataReceivedCallback.onDataReceived(bluetoothDevice, data2);
                }
            });
            this.buffer = null;
            this.count = 0;
        }
    }

    public /* synthetic */ void lambda$notifyValueChanged$1$ValueChangedCallback(BluetoothDevice bluetoothDevice, byte[] bArr) {
        ReadProgressCallback readProgressCallback = this.progressCallback;
        if (readProgressCallback != null) {
            readProgressCallback.onPacketReceived(bluetoothDevice, bArr, this.count);
        }
    }
}
