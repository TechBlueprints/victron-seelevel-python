package no.nordicsemi.android.ble.common.util;

import androidx.exifinterface.media.ExifInterface;

/* loaded from: classes.dex */
public final class CRC16 {
    private CRC16() {
    }

    public static int CCITT_Kermit(byte[] bArr, int i, int i2) {
        return CRC(4129, 0, bArr, i, i2, true, true, 0);
    }

    public static int CCITT_FALSE(byte[] bArr, int i, int i2) {
        return CRC(4129, 65535, bArr, i, i2, false, false, 0);
    }

    public static int MCRF4XX(byte[] bArr, int i, int i2) {
        return CRC(4129, 65535, bArr, i, i2, true, true, 0);
    }

    public static int AUG_CCITT(byte[] bArr, int i, int i2) {
        return CRC(4129, 7439, bArr, i, i2, false, false, 0);
    }

    public static int ARC(byte[] bArr, int i, int i2) {
        return CRC(ExifInterface.DATA_PACK_BITS_COMPRESSED, 0, bArr, i, i2, true, true, 0);
    }

    public static int MAXIM(byte[] bArr, int i, int i2) {
        return CRC(ExifInterface.DATA_PACK_BITS_COMPRESSED, 0, bArr, i, i2, true, true, 65535);
    }

    public static int CRC(int i, int i2, byte[] bArr, int i3, int i4, boolean z, boolean z2, int i5) {
        for (int i6 = i3; i6 < i3 + i4 && i6 < bArr.length; i6++) {
            byte b = bArr[i6];
            for (int i7 = 0; i7 < 8; i7++) {
                boolean z3 = ((b >> (7 - (z ? 7 - i7 : i7))) & 1) == 1;
                boolean z4 = ((i2 >> 15) & 1) == 1;
                i2 <<= 1;
                if (z3 ^ z4) {
                    i2 ^= i;
                }
            }
        }
        return z2 ? (Integer.reverse(i2) >>> 16) ^ i5 : (i2 ^ i5) & 65535;
    }
}
