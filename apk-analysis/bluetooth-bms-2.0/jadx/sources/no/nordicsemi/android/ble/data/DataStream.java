package no.nordicsemi.android.ble.data;

import java.io.ByteArrayOutputStream;

/* loaded from: classes.dex */
public class DataStream {
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    public boolean write(byte[] bArr) {
        if (bArr == null) {
            return false;
        }
        return write(bArr, 0, bArr.length);
    }

    public boolean write(byte[] bArr, int i, int i2) {
        if (bArr == null || bArr.length < i) {
            return false;
        }
        this.buffer.write(bArr, i, Math.min(bArr.length - i, i2));
        return true;
    }

    public boolean write(Data data) {
        return data != null && write(data.getValue());
    }

    public int size() {
        return this.buffer.size();
    }

    public byte[] toByteArray() {
        return this.buffer.toByteArray();
    }

    public Data toData() {
        return new Data(this.buffer.toByteArray());
    }
}
