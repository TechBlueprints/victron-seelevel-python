package no.nordicsemi.android.ble.common.data;

import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.data.MutableData;

/* loaded from: classes.dex */
public final class RecordAccessControlPointData {
    private static final byte OPERATOR_ALL_RECORDS = 1;
    private static final byte OPERATOR_FIRST_RECORD = 5;
    private static final byte OPERATOR_GREATER_THEN_OR_EQUAL = 3;
    private static final byte OPERATOR_LAST_RECORD = 6;
    private static final byte OPERATOR_LESS_THEN_OR_EQUAL = 2;
    private static final byte OPERATOR_NULL = 0;
    private static final byte OPERATOR_WITHING_RANGE = 4;
    private static final byte OP_CODE_ABORT_OPERATION = 3;
    private static final byte OP_CODE_DELETE_STORED_RECORDS = 2;
    private static final byte OP_CODE_NUMBER_OF_STORED_RECORDS_RESPONSE = 5;
    private static final byte OP_CODE_REPORT_NUMBER_OF_RECORDS = 4;
    private static final byte OP_CODE_REPORT_STORED_RECORDS = 1;
    private static final byte OP_CODE_RESPONSE_CODE = 6;

    public enum FilterType {
        TIME_OFFSET(1),
        SEQUENCE_NUMBER(1),
        USER_FACING_TIME(2);

        final byte type;

        FilterType(int i) {
            this.type = (byte) i;
        }
    }

    private RecordAccessControlPointData() {
    }

    public static Data reportAllStoredRecords() {
        return create((byte) 1, (byte) 1);
    }

    public static Data reportFirstStoredRecord() {
        return create((byte) 1, (byte) 5);
    }

    public static Data reportLastStoredRecord() {
        return create((byte) 1, (byte) 6);
    }

    public static Data reportStoredRecordsLessThenOrEqualTo(FilterType filterType, int i, int i2) {
        return create((byte) 1, (byte) 2, filterType, i, i2);
    }

    public static Data reportStoredRecordsGreaterThenOrEqualTo(FilterType filterType, int i, int i2) {
        return create((byte) 1, (byte) 3, filterType, i, i2);
    }

    public static Data reportStoredRecordsFromRange(FilterType filterType, int i, int i2, int i3) {
        return create((byte) 1, (byte) 4, filterType, i, i2, i3);
    }

    public static Data reportStoredRecordsLessThenOrEqualTo(int i) {
        return create((byte) 1, (byte) 2, FilterType.SEQUENCE_NUMBER, 18, i);
    }

    public static Data reportStoredRecordsGreaterThenOrEqualTo(int i) {
        return create((byte) 1, (byte) 3, FilterType.SEQUENCE_NUMBER, 18, i);
    }

    public static Data reportStoredRecordsFromRange(int i, int i2) {
        return create((byte) 1, (byte) 4, FilterType.SEQUENCE_NUMBER, 18, i, i2);
    }

    public static Data deleteAllStoredRecords() {
        return create((byte) 2, (byte) 1);
    }

    public static Data deleteFirstStoredRecord() {
        return create((byte) 2, (byte) 5);
    }

    public static Data deleteLastStoredRecord() {
        return create((byte) 2, (byte) 6);
    }

    public static Data deleteStoredRecordsLessThenOrEqualTo(FilterType filterType, int i, int i2) {
        return create((byte) 2, (byte) 2, filterType, i, i2);
    }

    public static Data deleteStoredRecordsGreaterThenOrEqualTo(FilterType filterType, int i, int i2) {
        return create((byte) 2, (byte) 3, filterType, i, i2);
    }

    public static Data deleteStoredRecordsFromRange(FilterType filterType, int i, int i2, int i3) {
        return create((byte) 2, (byte) 4, filterType, i, i2, i3);
    }

    public static Data deleteStoredRecordsLessThenOrEqualTo(int i) {
        return create((byte) 2, (byte) 2, FilterType.SEQUENCE_NUMBER, 18, i);
    }

    public static Data deleteStoredRecordsGreaterThenOrEqualTo(int i) {
        return create((byte) 2, (byte) 3, FilterType.SEQUENCE_NUMBER, 18, i);
    }

    public static Data deleteStoredRecordsFromRange(int i, int i2) {
        return create((byte) 2, (byte) 4, FilterType.SEQUENCE_NUMBER, 18, i, i2);
    }

    public static Data reportNumberOfAllStoredRecords() {
        return create((byte) 4, (byte) 1);
    }

    public static Data reportNumberOfStoredRecordsLessThenOrEqualTo(FilterType filterType, int i, int i2) {
        return create((byte) 4, (byte) 2, filterType, i, i2);
    }

    public static Data reportNumberOfStoredRecordsGreaterThenOrEqualTo(FilterType filterType, int i, int i2) {
        return create((byte) 4, (byte) 3, filterType, i, i2);
    }

    public static Data reportNumberOfStoredRecordsFromRange(FilterType filterType, int i, int i2, int i3) {
        return create((byte) 4, (byte) 4, filterType, i, i2, i3);
    }

    public static Data reportNumberOfStoredRecordsLessThenOrEqualTo(int i) {
        return create((byte) 4, (byte) 2, FilterType.SEQUENCE_NUMBER, 18, i);
    }

    public static Data reportNumberOfStoredRecordsGreaterThenOrEqualTo(int i) {
        return create((byte) 4, (byte) 3, FilterType.SEQUENCE_NUMBER, 18, i);
    }

    public static Data reportNumberOfStoredRecordsFromRange(int i, int i2) {
        return create((byte) 4, (byte) 4, FilterType.SEQUENCE_NUMBER, 18, i, i2);
    }

    public static Data abortOperation() {
        return create((byte) 3, (byte) 0);
    }

    private static Data create(byte b, byte b2) {
        return Data.opCode(b, b2);
    }

    private static Data create(byte b, byte b2, FilterType filterType, int i, int... iArr) {
        int i2 = i & 15;
        MutableData mutableData = new MutableData(new byte[(iArr.length * i2) + 3]);
        mutableData.setByte(b, 0);
        mutableData.setByte(b2, 1);
        if (iArr.length > 0) {
            mutableData.setByte(filterType.type, 2);
            mutableData.setValue(iArr[0], i, 3);
        }
        if (iArr.length == 2) {
            mutableData.setValue(iArr[1], i, i2 + 3);
        }
        return mutableData;
    }
}
