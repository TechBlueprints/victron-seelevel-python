package com.jkcq.homebike.ble.devicescan;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import com.jkcq.homebike.BMSParseUtil;
import com.jkcq.homebike.ble.battery.BatteryManager;
import com.jkcq.homebike.ble.devicescan.receivecallback.BMSRealDataCallback;
import java.nio.ByteBuffer;
import java.util.UUID;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.data.MutableData;
import no.nordicsemi.android.ble.error.GattError;

/* loaded from: classes.dex */
public class BMSBleManager extends BatteryManager<BMSBleManagerCallbacks> {
    ByteBuffer buffer;
    private BluetoothGattCharacteristic responseNotifyCharacteristic;
    private BluetoothGattCharacteristic sendWriteCharacteristic;
    public static final UUID BIKE_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9E");
    public static final UUID BIKE_SEND_WRITE_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
    public static final UUID BIKE_RESPONCE_NOTIFY_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");
    private static BMSBleManager managerInstance = null;

    public void readVersionCharacteristic() {
    }

    public static synchronized BMSBleManager getInstance(Context context) {
        if (managerInstance == null) {
            managerInstance = new BMSBleManager(context);
        }
        return managerInstance;
    }

    private BMSBleManager(Context context) {
        super(context);
        this.buffer = ByteBuffer.allocate(GattError.GATT_SERVICE_STARTED);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // no.nordicsemi.android.ble.BleManager
    public BatteryManager<BMSBleManagerCallbacks>.BatteryManagerGattCallback getGattCallback() {
        return new HeartRateManagerCallback();
    }

    private final class HeartRateManagerCallback extends BatteryManager<BMSBleManagerCallbacks>.BatteryManagerGattCallback {
        private HeartRateManagerCallback() {
            super();
        }

        @Override // com.jkcq.homebike.ble.battery.BatteryManager.BatteryManagerGattCallback, no.nordicsemi.android.ble.BleManagerHandler
        protected void initialize() {
            super.initialize();
            Log.e("BikeBleManager", "initialize");
            BMSBleManager bMSBleManager = BMSBleManager.this;
            bMSBleManager.setNotificationCallback(bMSBleManager.responseNotifyCharacteristic).with(new BMSRealDataCallback() { // from class: com.jkcq.homebike.ble.devicescan.BMSBleManager.HeartRateManagerCallback.1
                @Override // no.nordicsemi.android.ble.callback.DataReceivedCallback
                public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
                    Log.e("onDataReceived", "responseNotifyCharacteristic" + data.toString());
                    BMSParseUtil.parseData(data.getValue());
                }
            });
            BMSBleManager bMSBleManager2 = BMSBleManager.this;
            bMSBleManager2.enableNotifications(bMSBleManager2.responseNotifyCharacteristic).enqueue();
        }

        @Override // no.nordicsemi.android.ble.BleManagerHandler
        protected boolean isRequiredServiceSupported(BluetoothGatt bluetoothGatt) {
            BluetoothGattService service = bluetoothGatt.getService(BMSBleManager.BIKE_SERVICE_UUID);
            if (service == null) {
                return true;
            }
            BMSBleManager.this.sendWriteCharacteristic = service.getCharacteristic(BMSBleManager.BIKE_SEND_WRITE_UUID);
            BMSBleManager.this.responseNotifyCharacteristic = service.getCharacteristic(BMSBleManager.BIKE_RESPONCE_NOTIFY_UUID);
            return true;
        }

        @Override // com.jkcq.homebike.ble.battery.BatteryManager.BatteryManagerGattCallback, no.nordicsemi.android.ble.BleManagerHandler
        protected boolean isOptionalServiceSupported(BluetoothGatt bluetoothGatt) {
            super.isOptionalServiceSupported(bluetoothGatt);
            return true;
        }

        @Override // com.jkcq.homebike.ble.battery.BatteryManager.BatteryManagerGattCallback, no.nordicsemi.android.ble.BleManagerHandler
        protected void onDeviceDisconnected() {
            super.onDeviceDisconnected();
            BMSBleManager.this.cancelQueue();
            BMSBleManager.this.sendWriteCharacteristic = null;
            BMSBleManager.this.responseNotifyCharacteristic = null;
        }
    }

    public void sendData() {
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.sendWriteCharacteristic;
        if (bluetoothGattCharacteristic != null) {
            writeCharacteristic(bluetoothGattCharacteristic, createQuickData()).enqueue();
        }
    }

    private static Data createQuickData() {
        byte[] bytes = ":000250000E03~".getBytes();
        MutableData mutableData = new MutableData(new byte[bytes.length]);
        mutableData.setValue(bytes);
        return mutableData;
    }
}
