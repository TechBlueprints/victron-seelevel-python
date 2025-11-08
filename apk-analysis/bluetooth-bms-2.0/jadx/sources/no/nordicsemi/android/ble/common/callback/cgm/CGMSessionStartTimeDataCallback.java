package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import com.blankj.utilcode.constant.TimeConstants;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.callback.DSTOffsetDataCallback;
import no.nordicsemi.android.ble.common.callback.DateTimeDataCallback;
import no.nordicsemi.android.ble.common.callback.TimeZoneDataCallback;
import no.nordicsemi.android.ble.common.profile.DSTOffsetCallback;
import no.nordicsemi.android.ble.common.profile.cgm.CGMSessionStartTimeCallback;
import no.nordicsemi.android.ble.common.util.CRC16;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class CGMSessionStartTimeDataCallback extends ProfileReadResponse implements CGMSessionStartTimeCallback {
    @Override // no.nordicsemi.android.ble.common.profile.cgm.CGMSessionStartTimeCallback
    public /* synthetic */ void onContinuousGlucoseMonitorSessionStartTimeReceivedWithCrcError(BluetoothDevice bluetoothDevice, Data data) {
        CGMSessionStartTimeCallback.CC.$default$onContinuousGlucoseMonitorSessionStartTimeReceivedWithCrcError(this, bluetoothDevice, data);
    }

    public CGMSessionStartTimeDataCallback() {
    }

    protected CGMSessionStartTimeDataCallback(Parcel parcel) {
        super(parcel);
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        super.onDataReceived(bluetoothDevice, data);
        if (data.size() != 9 && data.size() != 11) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        boolean z = data.size() == 11;
        if (z && CRC16.MCRF4XX(data.getValue(), 0, 9) != data.getIntValue(18, 9).intValue()) {
            onContinuousGlucoseMonitorSessionStartTimeReceivedWithCrcError(bluetoothDevice, data);
            return;
        }
        Calendar dateTime = DateTimeDataCallback.readDateTime(data, 0);
        final Integer timeZone = TimeZoneDataCallback.readTimeZone(data, 7);
        final DSTOffsetCallback.DSTOffset dSTOffset = DSTOffsetDataCallback.readDSTOffset(data, 8);
        if (dateTime == null || timeZone == null || dSTOffset == null) {
            onInvalidDataReceived(bluetoothDevice, data);
        } else {
            dateTime.setTimeZone(new TimeZone() { // from class: no.nordicsemi.android.ble.common.callback.cgm.CGMSessionStartTimeDataCallback.1
                @Override // java.util.TimeZone
                public boolean useDaylightTime() {
                    return true;
                }

                @Override // java.util.TimeZone
                public int getOffset(int i, int i2, int i3, int i4, int i5, int i6) {
                    return (timeZone.intValue() + dSTOffset.offset) * TimeConstants.MIN;
                }

                @Override // java.util.TimeZone
                public void setRawOffset(int i) {
                    throw new UnsupportedOperationException("Can't set raw offset for this TimeZone");
                }

                @Override // java.util.TimeZone
                public int getRawOffset() {
                    return timeZone.intValue() * TimeConstants.MIN;
                }

                @Override // java.util.TimeZone
                public boolean inDaylightTime(Date date) {
                    return dSTOffset.offset > 0;
                }

                @Override // java.util.TimeZone
                public int getDSTSavings() {
                    return dSTOffset.offset * TimeConstants.MIN;
                }
            });
            onContinuousGlucoseMonitorSessionStartTimeReceived(bluetoothDevice, dateTime, z);
        }
    }
}
