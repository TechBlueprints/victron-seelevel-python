package no.nordicsemi.android.ble.common.profile;

import android.bluetooth.BluetoothDevice;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface RecordAccessControlPointCallback {
    public static final int RACP_EEROR_INVALID_OPERATOR = 3;
    public static final int RACP_ERROR_ABORT_UNSUCCESSFUL = 7;
    public static final int RACP_ERROR_INVALID_OPERAND = 5;
    public static final int RACP_ERROR_OPERAND_NOT_SUPPORTED = 9;
    public static final int RACP_ERROR_OPERATOR_NOT_SUPPORTED = 4;
    public static final int RACP_ERROR_OP_CODE_NOT_SUPPORTED = 2;
    public static final int RACP_ERROR_PROCEDURE_NOT_COMPLETED = 8;
    public static final int RACP_OP_CODE_ABORT_OPERATION = 3;
    public static final int RACP_OP_CODE_DELETE_STORED_RECORDS = 2;
    public static final int RACP_OP_CODE_REPORT_NUMBER_OF_RECORDS = 4;
    public static final int RACP_OP_CODE_REPORT_STORED_RECORDS = 1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface RACPErrorCode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RACPOpCode {
    }

    void onNumberOfRecordsReceived(BluetoothDevice bluetoothDevice, int i);

    void onRecordAccessOperationCompleted(BluetoothDevice bluetoothDevice, int i);

    void onRecordAccessOperationCompletedWithNoRecordsFound(BluetoothDevice bluetoothDevice, int i);

    void onRecordAccessOperationError(BluetoothDevice bluetoothDevice, int i, int i2);
}
