package no.nordicsemi.android.ble.common.callback.csc;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.csc.CyclingSpeedAndCadenceCallback;
import no.nordicsemi.android.ble.common.profile.csc.CyclingSpeedAndCadenceMeasurementCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class CyclingSpeedAndCadenceMeasurementDataCallback extends ProfileReadResponse implements CyclingSpeedAndCadenceMeasurementCallback, CyclingSpeedAndCadenceCallback {
    private long mInitialWheelRevolutions;
    private int mLastCrankEventTime;
    private int mLastCrankRevolutions;
    private int mLastWheelEventTime;
    private long mLastWheelRevolutions;
    private float mWheelCadence;

    @Override // no.nordicsemi.android.ble.common.profile.csc.CyclingSpeedAndCadenceCallback
    public /* synthetic */ float getWheelCircumference() {
        return CyclingSpeedAndCadenceCallback.CC.$default$getWheelCircumference(this);
    }

    public CyclingSpeedAndCadenceMeasurementDataCallback() {
        this.mInitialWheelRevolutions = -1L;
        this.mLastWheelRevolutions = -1L;
        this.mLastWheelEventTime = -1;
        this.mLastCrankRevolutions = -1;
        this.mLastCrankEventTime = -1;
        this.mWheelCadence = -1.0f;
    }

    protected CyclingSpeedAndCadenceMeasurementDataCallback(Parcel parcel) {
        super(parcel);
        this.mInitialWheelRevolutions = -1L;
        this.mLastWheelRevolutions = -1L;
        this.mLastWheelEventTime = -1;
        this.mLastCrankRevolutions = -1;
        this.mLastCrankEventTime = -1;
        this.mWheelCadence = -1.0f;
    }

    @Override // no.nordicsemi.android.ble.response.ReadResponse, no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        super.onDataReceived(bluetoothDevice, data);
        int i = 1;
        if (data.size() < 1) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        byte bByteValue = data.getByte(0).byteValue();
        boolean z = (bByteValue & 1) != 0;
        boolean z2 = (bByteValue & 2) != 0;
        if (data.size() < (z ? 6 : 0) + 1 + (z2 ? 4 : 0)) {
            onInvalidDataReceived(bluetoothDevice, data);
            return;
        }
        if (z) {
            long jIntValue = data.getIntValue(20, 1).intValue() & 4294967295L;
            int iIntValue = data.getIntValue(18, 5).intValue();
            if (this.mInitialWheelRevolutions < 0) {
                this.mInitialWheelRevolutions = jIntValue;
            }
            onWheelMeasurementReceived(bluetoothDevice, jIntValue, iIntValue);
            i = 7;
        }
        if (z2) {
            onCrankMeasurementReceived(bluetoothDevice, data.getIntValue(18, i).intValue(), data.getIntValue(18, i + 2).intValue());
        }
    }

    @Override // no.nordicsemi.android.ble.common.profile.csc.CyclingSpeedAndCadenceMeasurementCallback
    public void onWheelMeasurementReceived(BluetoothDevice bluetoothDevice, long j, int i) {
        if (this.mLastWheelEventTime == i) {
            return;
        }
        if (this.mLastWheelRevolutions >= 0) {
            float wheelCircumference = getWheelCircumference();
            float f = (i < this.mLastWheelEventTime ? (65535 + i) - r1 : i - r1) / 1024.0f;
            long j2 = this.mLastWheelRevolutions;
            this.mWheelCadence = ((j - j2) * 60.0f) / f;
            onDistanceChanged(bluetoothDevice, (j * wheelCircumference) / 1000.0f, ((j - this.mInitialWheelRevolutions) * wheelCircumference) / 1000.0f, (((j - j2) * wheelCircumference) / 1000.0f) / f);
        }
        this.mLastWheelRevolutions = j;
        this.mLastWheelEventTime = i;
    }

    @Override // no.nordicsemi.android.ble.common.profile.csc.CyclingSpeedAndCadenceMeasurementCallback
    public void onCrankMeasurementReceived(BluetoothDevice bluetoothDevice, int i, int i2) {
        int i3 = this.mLastCrankEventTime;
        if (i3 == i2) {
            return;
        }
        if (this.mLastCrankRevolutions >= 0) {
            float f = ((i - this.mLastCrankRevolutions) * 60.0f) / ((i2 < i3 ? (65535 + i2) - i3 : i2 - i3) / 1024.0f);
            if (f > 0.0f) {
                float f2 = this.mWheelCadence;
                onCrankDataChanged(bluetoothDevice, f, f2 >= 0.0f ? f2 / f : 0.0f);
            }
        }
        this.mLastCrankRevolutions = i;
        this.mLastCrankEventTime = i2;
    }
}
