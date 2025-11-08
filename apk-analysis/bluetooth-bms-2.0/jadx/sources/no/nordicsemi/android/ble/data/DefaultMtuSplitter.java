package no.nordicsemi.android.ble.data;

/* loaded from: classes.dex */
public final class DefaultMtuSplitter implements DataSplitter {
    @Override // no.nordicsemi.android.ble.data.DataSplitter
    public byte[] chunk(byte[] bArr, int i, int i2) {
        int i3 = i * i2;
        int iMin = Math.min(i2, bArr.length - i3);
        if (iMin <= 0) {
            return null;
        }
        byte[] bArr2 = new byte[iMin];
        System.arraycopy(bArr, i3, bArr2, 0, iMin);
        return bArr2;
    }
}
