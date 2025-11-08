package okio.internal;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import okio.Utf8;

/* compiled from: -Utf8.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0012\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0003\u001a\u00020\u0002*\u00020\u0001¨\u0006\u0004"}, d2 = {"commonAsUtf8ToByteArray", "", "", "commonToUtf8String", "jvm"}, k = 2, mv = {1, 1, 11})
/* loaded from: classes.dex */
public final class _Utf8Kt {
    public static final String commonToUtf8String(byte[] receiver) {
        int i;
        int i2;
        int i3;
        int i4;
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        char[] cArr = new char[receiver.length];
        int length = receiver.length;
        int i5 = 0;
        int i6 = 0;
        while (i5 < length) {
            byte b = receiver[i5];
            if (b >= 0) {
                i = i6 + 1;
                cArr[i6] = (char) b;
                i5++;
                while (i5 < length && receiver[i5] >= 0) {
                    cArr[i] = (char) receiver[i5];
                    i5++;
                    i++;
                }
            } else {
                if ((b >> 5) == -2) {
                    int i7 = i5 + 1;
                    if (length <= i7) {
                        i = i6 + 1;
                        cArr[i6] = (char) Utf8.REPLACEMENT_CODE_POINT;
                    } else {
                        byte b2 = receiver[i5];
                        byte b3 = receiver[i7];
                        if ((b3 & 192) == 128) {
                            int i8 = (b3 ^ ByteCompanionObject.MIN_VALUE) ^ (b2 << 6);
                            if (i8 < 128) {
                                i = i6 + 1;
                                cArr[i6] = (char) Utf8.REPLACEMENT_CODE_POINT;
                            } else {
                                i = i6 + 1;
                                cArr[i6] = (char) i8;
                            }
                            i2 = 2;
                        } else {
                            i = i6 + 1;
                            cArr[i6] = (char) Utf8.REPLACEMENT_CODE_POINT;
                        }
                    }
                    i2 = 1;
                } else if ((b >> 4) == -2) {
                    int i9 = i5 + 2;
                    if (length <= i9) {
                        i = i6 + 1;
                        cArr[i6] = (char) Utf8.REPLACEMENT_CODE_POINT;
                        int i10 = i5 + 1;
                        if (length > i10) {
                            if ((receiver[i10] & 192) == 128) {
                                i2 = 2;
                            }
                        }
                        i2 = 1;
                    } else {
                        byte b4 = receiver[i5];
                        byte b5 = receiver[i5 + 1];
                        if ((b5 & 192) == 128) {
                            byte b6 = receiver[i9];
                            if ((b6 & 192) == 128) {
                                int i11 = ((b6 ^ ByteCompanionObject.MIN_VALUE) ^ (b5 << 6)) ^ (b4 << 12);
                                if (i11 < 2048) {
                                    i = i6 + 1;
                                    cArr[i6] = (char) Utf8.REPLACEMENT_CODE_POINT;
                                } else if (55296 <= i11 && 57343 >= i11) {
                                    i = i6 + 1;
                                    cArr[i6] = (char) Utf8.REPLACEMENT_CODE_POINT;
                                } else {
                                    i = i6 + 1;
                                    cArr[i6] = (char) i11;
                                }
                                i2 = 3;
                            } else {
                                i = i6 + 1;
                                cArr[i6] = (char) Utf8.REPLACEMENT_CODE_POINT;
                                i2 = 2;
                            }
                        } else {
                            i = i6 + 1;
                            cArr[i6] = (char) Utf8.REPLACEMENT_CODE_POINT;
                            i2 = 1;
                        }
                    }
                } else {
                    if ((b >> 3) == -2) {
                        int i12 = i5 + 3;
                        if (length <= i12) {
                            i3 = i6 + 1;
                            cArr[i6] = Utf8.REPLACEMENT_CHARACTER;
                            int i13 = i5 + 1;
                            if (length > i13) {
                                if ((receiver[i13] & 192) == 128) {
                                    int i14 = i5 + 2;
                                    if (length > i14) {
                                        if ((receiver[i14] & 192) == 128) {
                                            i4 = 3;
                                        }
                                    }
                                    i4 = 2;
                                }
                            }
                            i4 = 1;
                        } else {
                            byte b7 = receiver[i5];
                            byte b8 = receiver[i5 + 1];
                            if ((b8 & 192) == 128) {
                                byte b9 = receiver[i5 + 2];
                                if ((b9 & 192) == 128) {
                                    byte b10 = receiver[i12];
                                    if ((b10 & 192) == 128) {
                                        int i15 = (((b10 ^ ByteCompanionObject.MIN_VALUE) ^ (b9 << 6)) ^ (b8 << 12)) ^ (b7 << 18);
                                        if (i15 > 1114111) {
                                            i3 = i6 + 1;
                                            cArr[i6] = Utf8.REPLACEMENT_CHARACTER;
                                        } else if ((55296 <= i15 && 57343 >= i15) || i15 < 65536 || i15 == 65533) {
                                            i3 = i6 + 1;
                                            cArr[i6] = Utf8.REPLACEMENT_CHARACTER;
                                        } else {
                                            int i16 = i6 + 1;
                                            cArr[i6] = (char) ((i15 >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                            char c = (char) ((i15 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                            i3 = i16 + 1;
                                            cArr[i16] = c;
                                        }
                                        i4 = 4;
                                    } else {
                                        i3 = i6 + 1;
                                        cArr[i6] = Utf8.REPLACEMENT_CHARACTER;
                                        i4 = 3;
                                    }
                                } else {
                                    i3 = i6 + 1;
                                    cArr[i6] = Utf8.REPLACEMENT_CHARACTER;
                                    i4 = 2;
                                }
                            } else {
                                i3 = i6 + 1;
                                cArr[i6] = Utf8.REPLACEMENT_CHARACTER;
                                i4 = 1;
                            }
                        }
                        i5 += i4;
                    } else {
                        i3 = i6 + 1;
                        cArr[i6] = Utf8.REPLACEMENT_CHARACTER;
                        i5++;
                    }
                    i6 = i3;
                }
                i5 += i2;
            }
            i6 = i;
        }
        return new String(cArr, 0, i6);
    }

    public static final byte[] commonAsUtf8ToByteArray(String receiver) {
        int i;
        int i2;
        char cCharAt;
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        byte[] bArr = new byte[receiver.length() * 4];
        int length = receiver.length();
        int i3 = 0;
        while (i3 < length) {
            char cCharAt2 = receiver.charAt(i3);
            if (cCharAt2 >= 128) {
                int length2 = receiver.length();
                int i4 = i3;
                while (i3 < length2) {
                    char cCharAt3 = receiver.charAt(i3);
                    if (cCharAt3 < 128) {
                        int i5 = i4 + 1;
                        bArr[i4] = (byte) cCharAt3;
                        i3++;
                        while (i3 < length2 && receiver.charAt(i3) < 128) {
                            bArr[i5] = (byte) receiver.charAt(i3);
                            i3++;
                            i5++;
                        }
                        i4 = i5;
                    } else {
                        if (cCharAt3 < 2048) {
                            int i6 = i4 + 1;
                            bArr[i4] = (byte) ((cCharAt3 >> 6) | 192);
                            byte b = (byte) ((cCharAt3 & '?') | 128);
                            i = i6 + 1;
                            bArr[i6] = b;
                        } else if (55296 > cCharAt3 || 57343 < cCharAt3) {
                            int i7 = i4 + 1;
                            bArr[i4] = (byte) ((cCharAt3 >> '\f') | 224);
                            int i8 = i7 + 1;
                            bArr[i7] = (byte) (((cCharAt3 >> 6) & 63) | 128);
                            byte b2 = (byte) ((cCharAt3 & '?') | 128);
                            i = i8 + 1;
                            bArr[i8] = b2;
                        } else if (cCharAt3 > 56319 || length2 <= (i2 = i3 + 1) || 56320 > (cCharAt = receiver.charAt(i2)) || 57343 < cCharAt) {
                            i = i4 + 1;
                            bArr[i4] = Utf8.REPLACEMENT_BYTE;
                        } else {
                            int iCharAt = ((cCharAt3 << '\n') + receiver.charAt(i2)) - 56613888;
                            int i9 = i4 + 1;
                            bArr[i4] = (byte) ((iCharAt >> 18) | 240);
                            int i10 = i9 + 1;
                            bArr[i9] = (byte) (((iCharAt >> 12) & 63) | 128);
                            int i11 = i10 + 1;
                            bArr[i10] = (byte) (((iCharAt >> 6) & 63) | 128);
                            byte b3 = (byte) ((iCharAt & 63) | 128);
                            i = i11 + 1;
                            bArr[i11] = b3;
                            i3 += 2;
                            i4 = i;
                        }
                        i3++;
                        i4 = i;
                    }
                }
                byte[] bArrCopyOf = Arrays.copyOf(bArr, i4);
                Intrinsics.checkExpressionValueIsNotNull(bArrCopyOf, "java.util.Arrays.copyOf(this, newSize)");
                return bArrCopyOf;
            }
            bArr[i3] = (byte) cCharAt2;
            i3++;
        }
        byte[] bArrCopyOf2 = Arrays.copyOf(bArr, receiver.length());
        Intrinsics.checkExpressionValueIsNotNull(bArrCopyOf2, "java.util.Arrays.copyOf(this, newSize)");
        return bArrCopyOf2;
    }
}
