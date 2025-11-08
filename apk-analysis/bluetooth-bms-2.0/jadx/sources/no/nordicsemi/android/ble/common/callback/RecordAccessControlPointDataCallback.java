package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.RecordAccessControlPointCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class RecordAccessControlPointDataCallback extends ProfileReadResponse implements RecordAccessControlPointCallback {
    private static final int OPERATOR_NULL = 0;
    private static final int OP_CODE_NUMBER_OF_STORED_RECORDS_RESPONSE = 5;
    private static final int OP_CODE_RESPONSE_CODE = 6;
    private static final int RACP_ERROR_NO_RECORDS_FOUND = 6;
    private static final int RACP_RESPONSE_SUCCESS = 1;

    public RecordAccessControlPointDataCallback() {
    }

    protected RecordAccessControlPointDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        int iIntValue;
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() < 3) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        int iIntValue2 = data.getIntValue(17, 0).intValue();
        if (iIntValue2 != 5 && iIntValue2 != 6) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        if (data.getIntValue(17, 1).intValue() != 0) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        if (iIntValue2 == 5) {
            int size = data.size() - 2;
            if (size == 1) {
                iIntValue = data.getIntValue(17, 2).intValue();
            } else if (size == 2) {
                iIntValue = data.getIntValue(18, 2).intValue();
            } else if (size == 4) {
                iIntValue = data.getIntValue(20, 2).intValue();
            } else {
                onInvalidDataReceived(bluetoothDevice, data);
                return;
            }
            onNumberOfRecordsReceived(bluetoothDevice, iIntValue);
            return;
        }
        if (iIntValue2 != 6) {
            return;
        }
        if (data.size() != 4) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        int iIntValue3 = data.getIntValue(17, 2).intValue();
        int iIntValue4 = data.getIntValue(17, 3).intValue();
        if (iIntValue4 == 1) {
            onRecordAccessOperationCompleted(bluetoothDevice, iIntValue3);
        } else if (iIntValue4 == 6) {
            onRecordAccessOperationCompletedWithNoRecordsFound(bluetoothDevice, iIntValue3);
        } else {
            onRecordAccessOperationError(bluetoothDevice, iIntValue3, iIntValue4);
        }
    }
}
