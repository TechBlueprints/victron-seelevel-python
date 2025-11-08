package kotlin.collections;

import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UIntArray;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.UnsignedKt;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: UArraySorting.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0012\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0007\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\t\u0010\n\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\f\u0010\r\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0010\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0013\u0010\u0014\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0019\u0010\u001a\u001a\u001a\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u0003H\u0001ø\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u001d\u001a\u001a\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\bH\u0001ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001f\u001a\u001a\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000bH\u0001ø\u0001\u0000¢\u0006\u0004\b \u0010!\u001a\u001a\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000eH\u0001ø\u0001\u0000¢\u0006\u0004\b\"\u0010#\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006$"}, d2 = {"partition", "", "array", "Lkotlin/UByteArray;", "left", "right", "partition-4UcCI2c", "([BII)I", "Lkotlin/UIntArray;", "partition-oBK06Vg", "([III)I", "Lkotlin/ULongArray;", "partition--nroSd4", "([JII)I", "Lkotlin/UShortArray;", "partition-Aa5vz7o", "([SII)I", "quickSort", "", "quickSort-4UcCI2c", "([BII)V", "quickSort-oBK06Vg", "([III)V", "quickSort--nroSd4", "([JII)V", "quickSort-Aa5vz7o", "([SII)V", "sortArray", "sortArray-GBYM_sE", "([B)V", "sortArray--ajY-9A", "([I)V", "sortArray-QwZRm1k", "([J)V", "sortArray-rL5Bavg", "([S)V", "kotlin-stdlib"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class UArraySortingKt {
    /* renamed from: partition-4UcCI2c, reason: not valid java name */
    private static final int m333partition4UcCI2c(byte[] bArr, int i, int i2) {
        int i3;
        byte bM80getimpl = UByteArray.m80getimpl(bArr, (i + i2) / 2);
        while (i <= i2) {
            while (true) {
                int iM80getimpl = UByteArray.m80getimpl(bArr, i) & UByte.MAX_VALUE;
                i3 = bM80getimpl & UByte.MAX_VALUE;
                if (Intrinsics.compare(iM80getimpl, i3) >= 0) {
                    break;
                }
                i++;
            }
            while (Intrinsics.compare(UByteArray.m80getimpl(bArr, i2) & UByte.MAX_VALUE, i3) > 0) {
                i2--;
            }
            if (i <= i2) {
                byte bM80getimpl2 = UByteArray.m80getimpl(bArr, i);
                UByteArray.m85setVurrAj0(bArr, i, UByteArray.m80getimpl(bArr, i2));
                UByteArray.m85setVurrAj0(bArr, i2, bM80getimpl2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* renamed from: quickSort-4UcCI2c, reason: not valid java name */
    private static final void m337quickSort4UcCI2c(byte[] bArr, int i, int i2) {
        int iM333partition4UcCI2c = m333partition4UcCI2c(bArr, i, i2);
        int i3 = iM333partition4UcCI2c - 1;
        if (i < i3) {
            m337quickSort4UcCI2c(bArr, i, i3);
        }
        if (iM333partition4UcCI2c < i2) {
            m337quickSort4UcCI2c(bArr, iM333partition4UcCI2c, i2);
        }
    }

    /* renamed from: partition-Aa5vz7o, reason: not valid java name */
    private static final int m334partitionAa5vz7o(short[] sArr, int i, int i2) {
        int i3;
        short sM313getimpl = UShortArray.m313getimpl(sArr, (i + i2) / 2);
        while (i <= i2) {
            while (true) {
                int iM313getimpl = UShortArray.m313getimpl(sArr, i) & UShort.MAX_VALUE;
                i3 = sM313getimpl & UShort.MAX_VALUE;
                if (Intrinsics.compare(iM313getimpl, i3) >= 0) {
                    break;
                }
                i++;
            }
            while (Intrinsics.compare(UShortArray.m313getimpl(sArr, i2) & UShort.MAX_VALUE, i3) > 0) {
                i2--;
            }
            if (i <= i2) {
                short sM313getimpl2 = UShortArray.m313getimpl(sArr, i);
                UShortArray.m318set01HTLdE(sArr, i, UShortArray.m313getimpl(sArr, i2));
                UShortArray.m318set01HTLdE(sArr, i2, sM313getimpl2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* renamed from: quickSort-Aa5vz7o, reason: not valid java name */
    private static final void m338quickSortAa5vz7o(short[] sArr, int i, int i2) {
        int iM334partitionAa5vz7o = m334partitionAa5vz7o(sArr, i, i2);
        int i3 = iM334partitionAa5vz7o - 1;
        if (i < i3) {
            m338quickSortAa5vz7o(sArr, i, i3);
        }
        if (iM334partitionAa5vz7o < i2) {
            m338quickSortAa5vz7o(sArr, iM334partitionAa5vz7o, i2);
        }
    }

    /* renamed from: partition-oBK06Vg, reason: not valid java name */
    private static final int m335partitionoBK06Vg(int[] iArr, int i, int i2) {
        int iM149getimpl = UIntArray.m149getimpl(iArr, (i + i2) / 2);
        while (i <= i2) {
            while (UnsignedKt.uintCompare(UIntArray.m149getimpl(iArr, i), iM149getimpl) < 0) {
                i++;
            }
            while (UnsignedKt.uintCompare(UIntArray.m149getimpl(iArr, i2), iM149getimpl) > 0) {
                i2--;
            }
            if (i <= i2) {
                int iM149getimpl2 = UIntArray.m149getimpl(iArr, i);
                UIntArray.m154setVXSXFK8(iArr, i, UIntArray.m149getimpl(iArr, i2));
                UIntArray.m154setVXSXFK8(iArr, i2, iM149getimpl2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* renamed from: quickSort-oBK06Vg, reason: not valid java name */
    private static final void m339quickSortoBK06Vg(int[] iArr, int i, int i2) {
        int iM335partitionoBK06Vg = m335partitionoBK06Vg(iArr, i, i2);
        int i3 = iM335partitionoBK06Vg - 1;
        if (i < i3) {
            m339quickSortoBK06Vg(iArr, i, i3);
        }
        if (iM335partitionoBK06Vg < i2) {
            m339quickSortoBK06Vg(iArr, iM335partitionoBK06Vg, i2);
        }
    }

    /* renamed from: partition--nroSd4, reason: not valid java name */
    private static final int m332partitionnroSd4(long[] jArr, int i, int i2) {
        long jM218getimpl = ULongArray.m218getimpl(jArr, (i + i2) / 2);
        while (i <= i2) {
            while (UnsignedKt.ulongCompare(ULongArray.m218getimpl(jArr, i), jM218getimpl) < 0) {
                i++;
            }
            while (UnsignedKt.ulongCompare(ULongArray.m218getimpl(jArr, i2), jM218getimpl) > 0) {
                i2--;
            }
            if (i <= i2) {
                long jM218getimpl2 = ULongArray.m218getimpl(jArr, i);
                ULongArray.m223setk8EXiF4(jArr, i, ULongArray.m218getimpl(jArr, i2));
                ULongArray.m223setk8EXiF4(jArr, i2, jM218getimpl2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* renamed from: quickSort--nroSd4, reason: not valid java name */
    private static final void m336quickSortnroSd4(long[] jArr, int i, int i2) {
        int iM332partitionnroSd4 = m332partitionnroSd4(jArr, i, i2);
        int i3 = iM332partitionnroSd4 - 1;
        if (i < i3) {
            m336quickSortnroSd4(jArr, i, i3);
        }
        if (iM332partitionnroSd4 < i2) {
            m336quickSortnroSd4(jArr, iM332partitionnroSd4, i2);
        }
    }

    /* renamed from: sortArray-GBYM_sE, reason: not valid java name */
    public static final void m341sortArrayGBYM_sE(byte[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        m337quickSort4UcCI2c(array, 0, UByteArray.m81getSizeimpl(array) - 1);
    }

    /* renamed from: sortArray-rL5Bavg, reason: not valid java name */
    public static final void m343sortArrayrL5Bavg(short[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        m338quickSortAa5vz7o(array, 0, UShortArray.m314getSizeimpl(array) - 1);
    }

    /* renamed from: sortArray--ajY-9A, reason: not valid java name */
    public static final void m340sortArrayajY9A(int[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        m339quickSortoBK06Vg(array, 0, UIntArray.m150getSizeimpl(array) - 1);
    }

    /* renamed from: sortArray-QwZRm1k, reason: not valid java name */
    public static final void m342sortArrayQwZRm1k(long[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        m336quickSortnroSd4(array, 0, ULongArray.m219getSizeimpl(array) - 1);
    }
}
