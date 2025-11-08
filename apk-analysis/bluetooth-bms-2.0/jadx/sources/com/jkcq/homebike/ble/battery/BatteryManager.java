package com.jkcq.homebike.ble.battery;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import com.jkcq.homebike.ble.battery.BatteryManagerCallbacks;
import java.util.UUID;
import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.callback.DataReceivedCallback;
import no.nordicsemi.android.ble.callback.FailCallback;
import no.nordicsemi.android.ble.callback.SuccessCallback;
import no.nordicsemi.android.ble.common.callback.battery.BatteryLevelDataCallback;
import no.nordicsemi.android.ble.data.Data;

/* loaded from: classes.dex */
public abstract class BatteryManager<T extends BatteryManagerCallbacks> extends LoggableBleManager<T> {
    private Integer batteryLevel;
    private BluetoothGattCharacteristic batteryLevelCharacteristic;
    private DataReceivedCallback batteryLevelDataCallback;
    private static final UUID BATTERY_SERVICE_UUID = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb");
    private static final UUID BATTERY_LEVEL_CHARACTERISTIC_UUID = UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb");

    public BatteryManager(Context context) {
        super(context);
        this.batteryLevelDataCallback = new BatteryLevelDataCallback() { // from class: com.jkcq.homebike.ble.battery.BatteryManager.1
            public void onRealData(BluetoothDevice bluetoothDevice, int i, int i2) {
            }

            @Override // no.nordicsemi.android.ble.common.profile.battery.BatteryLevelCallback
            public void onBatteryLevelChanged(BluetoothDevice bluetoothDevice, int i) {
                BatteryManager.this.log(10, "Battery Level received: " + i + "%");
                BatteryManager.this.batteryLevel = Integer.valueOf(i);
                if (BatteryManager.this.mCallbacks != null) {
                    ((BatteryManagerCallbacks) BatteryManager.this.mCallbacks).onBatteryLevelChanged(bluetoothDevice, i);
                }
            }

            @Override // no.nordicsemi.android.ble.callback.profile.ProfileReadResponse, no.nordicsemi.android.ble.callback.profile.ProfileDataCallback
            public void onInvalidDataReceived(BluetoothDevice bluetoothDevice, Data data) {
                BatteryManager.this.log(5, "Invalid Battery Level data received: " + data);
            }
        };
    }

    public void readBatteryLevelCharacteristic() {
        if (isConnected()) {
            readCharacteristic(this.batteryLevelCharacteristic).with(this.batteryLevelDataCallback).fail(new FailCallback() { // from class: com.jkcq.homebike.ble.battery.-$$Lambda$BatteryManager$bJX-ynnBqa9rnrQy2kzITTNNRPY
                @Override // no.nordicsemi.android.ble.callback.FailCallback
                public final void onRequestFailed(BluetoothDevice bluetoothDevice, int i) {
                    this.f$0.lambda$readBatteryLevelCharacteristic$0$BatteryManager(bluetoothDevice, i);
                }
            }).enqueue();
        }
    }

    public /* synthetic */ void lambda$readBatteryLevelCharacteristic$0$BatteryManager(BluetoothDevice bluetoothDevice, int i) {
        log(5, "Battery Level characteristic not found");
    }

    public void enableBatteryLevelCharacteristicNotifications() {
        if (isConnected()) {
            setNotificationCallback(this.batteryLevelCharacteristic).with(this.batteryLevelDataCallback);
            enableNotifications(this.batteryLevelCharacteristic).done(new SuccessCallback() { // from class: com.jkcq.homebike.ble.battery.-$$Lambda$BatteryManager$ruOzjkVZJrlcM59Q3PotmJkjWz4
                @Override // no.nordicsemi.android.ble.callback.SuccessCallback
                public final void onRequestCompleted(BluetoothDevice bluetoothDevice) {
                    this.f$0.lambda$enableBatteryLevelCharacteristicNotifications$1$BatteryManager(bluetoothDevice);
                }
            }).fail(new FailCallback() { // from class: com.jkcq.homebike.ble.battery.-$$Lambda$BatteryManager$lhMSFz_r0x-99DXOwG65IfeHGKQ
                @Override // no.nordicsemi.android.ble.callback.FailCallback
                public final void onRequestFailed(BluetoothDevice bluetoothDevice, int i) {
                    this.f$0.lambda$enableBatteryLevelCharacteristicNotifications$2$BatteryManager(bluetoothDevice, i);
                }
            }).enqueue();
        }
    }

    public /* synthetic */ void lambda$enableBatteryLevelCharacteristicNotifications$1$BatteryManager(BluetoothDevice bluetoothDevice) {
        log(4, "Battery Level notifications enabled");
    }

    public /* synthetic */ void lambda$enableBatteryLevelCharacteristicNotifications$2$BatteryManager(BluetoothDevice bluetoothDevice, int i) {
        log(5, "Battery Level characteristic not found");
    }

    public void disableBatteryLevelCharacteristicNotifications() {
        if (isConnected()) {
            disableNotifications(this.batteryLevelCharacteristic).done(new SuccessCallback() { // from class: com.jkcq.homebike.ble.battery.-$$Lambda$BatteryManager$H5Ps30It9rIQVBmPIvUMZvW9G4c
                @Override // no.nordicsemi.android.ble.callback.SuccessCallback
                public final void onRequestCompleted(BluetoothDevice bluetoothDevice) {
                    this.f$0.lambda$disableBatteryLevelCharacteristicNotifications$3$BatteryManager(bluetoothDevice);
                }
            }).enqueue();
        }
    }

    public /* synthetic */ void lambda$disableBatteryLevelCharacteristicNotifications$3$BatteryManager(BluetoothDevice bluetoothDevice) {
        log(4, "Battery Level notifications disabled");
    }

    public Integer getBatteryLevel() {
        return this.batteryLevel;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract class BatteryManagerGattCallback extends BleManager.BleManagerGattCallback {
        protected BatteryManagerGattCallback() {
        }

        @Override // no.nordicsemi.android.ble.BleManagerHandler
        protected void initialize() {
            BatteryManager.this.readBatteryLevelCharacteristic();
            BatteryManager.this.enableBatteryLevelCharacteristicNotifications();
        }

        @Override // no.nordicsemi.android.ble.BleManagerHandler
        protected boolean isOptionalServiceSupported(BluetoothGatt bluetoothGatt) {
            BluetoothGattService service = bluetoothGatt.getService(BatteryManager.BATTERY_SERVICE_UUID);
            if (service != null) {
                BatteryManager.this.batteryLevelCharacteristic = service.getCharacteristic(BatteryManager.BATTERY_LEVEL_CHARACTERISTIC_UUID);
            }
            return BatteryManager.this.batteryLevelCharacteristic != null;
        }

        @Override // no.nordicsemi.android.ble.BleManagerHandler
        protected void onDeviceDisconnected() {
            BatteryManager.this.batteryLevelCharacteristic = null;
            BatteryManager.this.batteryLevel = null;
        }
    }
}
