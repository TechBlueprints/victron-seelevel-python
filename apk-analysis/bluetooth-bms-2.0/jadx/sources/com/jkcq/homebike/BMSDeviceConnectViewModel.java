package com.jkcq.homebike;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.jkcq.base.app.BaseApp;
import com.jkcq.base.app.Preference;
import com.jkcq.base.base.BaseViewModel;
import com.jkcq.homebike.ble.battery.BatteryManagerCallbacks;
import com.jkcq.homebike.ble.devicescan.BMSBleManager;
import com.jkcq.homebike.ble.devicescan.receivecallback.BMSRealDataCallback;
import com.jkcq.util.ktx.ToastUtil;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import no.nordicsemi.android.ble.BleManagerCallbacks;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.log.LogContract;

/* compiled from: BMSDeviceConnectViewModel.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004B\u0005¢\u0006\u0002\u0010\u0005J\u000e\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#J\u0006\u0010$\u001a\u00020!J\u0018\u0010%\u001a\u00020!2\u0006\u0010\"\u001a\u00020#2\u0006\u0010&\u001a\u00020\u000eH\u0016J\u0010\u0010'\u001a\u00020!2\u0006\u0010\"\u001a\u00020#H\u0016J\u0010\u0010(\u001a\u00020!2\u0006\u0010\"\u001a\u00020#H\u0016J\u0010\u0010)\u001a\u00020!2\u0006\u0010\"\u001a\u00020#H\u0016J\u0018\u0010*\u001a\u00020!2\u0006\u0010\"\u001a\u00020#2\u0006\u0010+\u001a\u00020,H\u0016J\u0010\u0010-\u001a\u00020!2\u0006\u0010\"\u001a\u00020#H\u0016J\u0010\u0010.\u001a\u00020!2\u0006\u0010\"\u001a\u00020#H\u0016J\u0010\u0010/\u001a\u00020!2\u0006\u0010\"\u001a\u00020#H\u0016J\u0010\u00100\u001a\u00020!2\u0006\u0010\"\u001a\u00020#H\u0016J\u0010\u00101\u001a\u00020!2\u0006\u0010\"\u001a\u00020#H\u0016J\u0010\u00102\u001a\u00020!2\u0006\u0010\"\u001a\u00020#H\u0016J \u00103\u001a\u00020!2\u0006\u0010\"\u001a\u00020#2\u0006\u00104\u001a\u00020\u00122\u0006\u00105\u001a\u00020\u000eH\u0016J\u0010\u00106\u001a\u00020!2\u0006\u0010\"\u001a\u00020#H\u0016J\u0018\u00107\u001a\u00020!2\u0006\u0010\"\u001a\u00020#2\u0006\u00108\u001a\u000209H\u0016J\u0006\u0010:\u001a\u00020!J\u000e\u0010;\u001a\u00020!2\u0006\u0010<\u001a\u00020=J\u0006\u0010>\u001a\u00020!R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\r¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0010R+\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00128F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\u001a\u0010\u001b\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R+\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00128F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\u001f\u0010\u001b\u001a\u0004\b\u001d\u0010\u0017\"\u0004\b\u001e\u0010\u0019¨\u0006?"}, d2 = {"Lcom/jkcq/homebike/BMSDeviceConnectViewModel;", "Lcom/jkcq/base/base/BaseViewModel;", "Lno/nordicsemi/android/ble/BleManagerCallbacks;", "Lcom/jkcq/homebike/ble/battery/BatteryManagerCallbacks;", "Lcom/jkcq/homebike/ble/devicescan/receivecallback/BMSRealDataCallback;", "()V", "BMSManager", "Lcom/jkcq/homebike/ble/devicescan/BMSBleManager;", "getBMSManager", "()Lcom/jkcq/homebike/ble/devicescan/BMSBleManager;", "setBMSManager", "(Lcom/jkcq/homebike/ble/devicescan/BMSBleManager;)V", "mDeviceConnState", "Landroidx/lifecycle/MutableLiveData;", "", "getMDeviceConnState", "()Landroidx/lifecycle/MutableLiveData;", "mDeviceConnStateTips", "", "getMDeviceConnStateTips", "<set-?>", "mDeviceMac", "getMDeviceMac", "()Ljava/lang/String;", "setMDeviceMac", "(Ljava/lang/String;)V", "mDeviceMac$delegate", "Lcom/jkcq/base/app/Preference;", "mDeviceName", "getMDeviceName", "setMDeviceName", "mDeviceName$delegate", "conectBikeDevice", "", "device", "Landroid/bluetooth/BluetoothDevice;", "disconectDevice", "onBatteryLevelChanged", "batteryLevel", "onBonded", "onBondingFailed", "onBondingRequired", "onDataReceived", LogContract.LogColumns.DATA, "Lno/nordicsemi/android/ble/data/Data;", "onDeviceConnected", "onDeviceConnecting", "onDeviceDisconnected", "onDeviceDisconnecting", "onDeviceNotSupported", "onDeviceReady", "onError", "message", "errorCode", "onLinkLossOccurred", "onServicesDiscovered", "optionalServicesFound", "", "sendQuitData", "setCallBack", "context", "Landroid/content/Context;", "unBindeDevice", "app_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class BMSDeviceConnectViewModel extends BaseViewModel implements BleManagerCallbacks, BatteryManagerCallbacks, BMSRealDataCallback {
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(BMSDeviceConnectViewModel.class), "mDeviceName", "getMDeviceName()Ljava/lang/String;")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(BMSDeviceConnectViewModel.class), "mDeviceMac", "getMDeviceMac()Ljava/lang/String;"))};
    public BMSBleManager BMSManager;

    /* renamed from: mDeviceName$delegate, reason: from kotlin metadata */
    private final Preference mDeviceName = new Preference(Preference.BIKENAME, "");

    /* renamed from: mDeviceMac$delegate, reason: from kotlin metadata */
    private final Preference mDeviceMac = new Preference(Preference.BIKEMAC, "");
    private final MutableLiveData<Integer> mDeviceConnState = new MutableLiveData<>();
    private final MutableLiveData<String> mDeviceConnStateTips = new MutableLiveData<>();

    public final String getMDeviceMac() {
        return (String) this.mDeviceMac.getValue(this, $$delegatedProperties[1]);
    }

    public final String getMDeviceName() {
        return (String) this.mDeviceName.getValue(this, $$delegatedProperties[0]);
    }

    @Override // no.nordicsemi.android.ble.BleManagerCallbacks
    @Deprecated
    public /* synthetic */ void onBatteryValueReceived(BluetoothDevice bluetoothDevice, int i) {
        BleManagerCallbacks.CC.$default$onBatteryValueReceived(this, bluetoothDevice, i);
    }

    public final void setMDeviceMac(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.mDeviceMac.setValue(this, $$delegatedProperties[1], str);
    }

    public final void setMDeviceName(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.mDeviceName.setValue(this, $$delegatedProperties[0], str);
    }

    @Override // no.nordicsemi.android.ble.BleManagerCallbacks
    @Deprecated
    public /* synthetic */ boolean shouldEnableBatteryLevelNotifications(BluetoothDevice bluetoothDevice) {
        return BleManagerCallbacks.CC.$default$shouldEnableBatteryLevelNotifications(this, bluetoothDevice);
    }

    public final BMSBleManager getBMSManager() {
        BMSBleManager bMSBleManager = this.BMSManager;
        if (bMSBleManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("BMSManager");
        }
        return bMSBleManager;
    }

    public final void setBMSManager(BMSBleManager bMSBleManager) {
        Intrinsics.checkParameterIsNotNull(bMSBleManager, "<set-?>");
        this.BMSManager = bMSBleManager;
    }

    public final MutableLiveData<Integer> getMDeviceConnState() {
        return this.mDeviceConnState;
    }

    public final MutableLiveData<String> getMDeviceConnStateTips() {
        return this.mDeviceConnStateTips;
    }

    public final void setCallBack(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        BMSBleManager bMSBleManager = BMSBleManager.getInstance(context);
        Intrinsics.checkExpressionValueIsNotNull(bMSBleManager, "BMSBleManager.getInstanc…    context\n            )");
        this.BMSManager = bMSBleManager;
        if (bMSBleManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("BMSManager");
        }
        bMSBleManager.setGattCallbacks(this);
    }

    public final void sendQuitData() {
        BMSBleManager bMSBleManager = this.BMSManager;
        if (bMSBleManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("BMSManager");
        }
        if (bMSBleManager.isConnected()) {
            BMSBleManager bMSBleManager2 = this.BMSManager;
            if (bMSBleManager2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("BMSManager");
            }
            bMSBleManager2.sendData();
        }
    }

    public final void conectBikeDevice(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        BMSConfig.device = device;
        BMSBleManager bMSBleManager = this.BMSManager;
        if (bMSBleManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("BMSManager");
        }
        bMSBleManager.connect(device).useAutoConnect(true).retry(3, 100).enqueue();
    }

    public final void unBindeDevice() {
        BMSBleManager bMSBleManager = this.BMSManager;
        if (bMSBleManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("BMSManager");
        }
        if (bMSBleManager.isConnected()) {
            disconectDevice();
        }
        setMDeviceName("");
        setMDeviceMac("");
        ToastUtil.showTextToast(BaseApp.INSTANCE.getSApplicaton(), com.ble.vanomize12.R.string.device_unbind_success);
    }

    public final void disconectDevice() {
        BMSBleManager bMSBleManager = this.BMSManager;
        if (bMSBleManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("BMSManager");
        }
        if (bMSBleManager != null) {
            BMSBleManager bMSBleManager2 = this.BMSManager;
            if (bMSBleManager2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("BMSManager");
            }
            if (bMSBleManager2.isConnected()) {
                BMSBleManager bMSBleManager3 = this.BMSManager;
                if (bMSBleManager3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("BMSManager");
                }
                bMSBleManager3.disconnect().enqueue();
            }
        }
    }

    @Override // no.nordicsemi.android.ble.BleManagerCallbacks
    public void onDeviceDisconnecting(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        BMSConfig.BikeConnState = BMSConfig.BIKE_CONN_DISCONNECTING;
    }

    @Override // no.nordicsemi.android.ble.BleManagerCallbacks
    public void onDeviceDisconnected(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        Log.e("DeviceConnectViewModel", "onDeviceDisconnected" + device.getName());
        this.mDeviceConnStateTips.setValue(BaseApp.INSTANCE.getSApplicaton().getString(com.ble.vanomize12.R.string.dviceDisconnected));
        BMSConfig.BikeConnState = BMSConfig.BIKE_CONN_DISCONN;
        this.mDeviceConnState.setValue(Integer.valueOf(BMSConfig.BIKE_CONN_DISCONN));
    }

    @Override // no.nordicsemi.android.ble.common.profile.battery.BatteryLevelCallback
    public void onBatteryLevelChanged(BluetoothDevice device, int batteryLevel) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        Log.e("BikeDeviceConnect", "onBatteryLevelChanged" + device.getName() + "batteryLevel" + batteryLevel);
    }

    @Override // no.nordicsemi.android.ble.BleManagerCallbacks
    public void onDeviceConnected(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        if (ToastUtil.isFastDoubleClick()) {
            return;
        }
        Log.e("BikeDeviceConnect", "onDeviceConnected" + device.getName());
        this.mDeviceConnStateTips.setValue(BaseApp.INSTANCE.getSApplicaton().getString(com.ble.vanomize12.R.string.bluetooth_connection_successful));
        String name = device.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "device.name");
        setMDeviceName(name);
        String address = device.getAddress();
        Intrinsics.checkExpressionValueIsNotNull(address, "device.address");
        setMDeviceMac(address);
        BMSConfig.BikeConnState = BMSConfig.BIKE_CONN_SUCCESS;
    }

    @Override // no.nordicsemi.android.ble.BleManagerCallbacks
    public void onDeviceNotSupported(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        Log.e("BikeDeviceConnect", "onDeviceNotSupported" + device.getName());
    }

    @Override // no.nordicsemi.android.ble.BleManagerCallbacks
    public void onBondingFailed(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        Log.e("BikeDeviceConnect", "onBondingFailed" + device.getName());
    }

    @Override // no.nordicsemi.android.ble.BleManagerCallbacks
    public void onServicesDiscovered(BluetoothDevice device, boolean optionalServicesFound) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        Log.e("BikeDeviceConnect", "onServicesDiscovered" + device.getName());
    }

    @Override // no.nordicsemi.android.ble.BleManagerCallbacks
    public void onBondingRequired(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        Log.e("BikeDeviceConnect", "onBondingRequired" + device.getName());
    }

    @Override // no.nordicsemi.android.ble.BleManagerCallbacks
    public void onLinkLossOccurred(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        this.mDeviceConnStateTips.setValue(BaseApp.INSTANCE.getSApplicaton().getString(com.ble.vanomize12.R.string.dviceDisconnected));
        BMSConfig.BikeConnState = BMSConfig.BIKE_CONN_DISCONN;
        this.mDeviceConnState.setValue(Integer.valueOf(BMSConfig.BIKE_CONN_DISCONN));
        Log.e("BikeDeviceConnect", "onLinkLossOccurred" + device.getName());
    }

    @Override // no.nordicsemi.android.ble.BleManagerCallbacks
    public void onBonded(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        Log.e("BikeDeviceConnect", "onBonded" + device.getName());
    }

    @Override // no.nordicsemi.android.ble.BleManagerCallbacks
    public void onDeviceReady(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        Log.e("BikeDeviceConnect", "onDeviceReady" + device.getName());
        this.mDeviceConnStateTips.setValue(BaseApp.INSTANCE.getSApplicaton().getString(com.ble.vanomize12.R.string.bluetooth_connection_successful));
        this.mDeviceConnState.setValue(Integer.valueOf(BMSConfig.BIKE_CONN_SUCCESS));
        BMSBleManager bMSBleManager = this.BMSManager;
        if (bMSBleManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("BMSManager");
        }
        if (bMSBleManager.isConnected()) {
            BMSBleManager bMSBleManager2 = this.BMSManager;
            if (bMSBleManager2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("BMSManager");
            }
            bMSBleManager2.readVersionCharacteristic();
        }
    }

    @Override // no.nordicsemi.android.ble.BleManagerCallbacks
    public void onError(BluetoothDevice device, String message, int errorCode) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        Intrinsics.checkParameterIsNotNull(message, "message");
        Log.e("BikeDeviceConnect", "onError" + device.getName() + ",message=" + message + "errorCode=" + errorCode);
    }

    @Override // no.nordicsemi.android.ble.BleManagerCallbacks
    public void onDeviceConnecting(BluetoothDevice device) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        Log.e("DeviceConnectViewModel", "onDeviceConnecting" + device.getName());
        this.mDeviceConnStateTips.setValue(BaseApp.INSTANCE.getSApplicaton().getString(com.ble.vanomize12.R.string.device_Connecting));
        this.mDeviceConnState.setValue(Integer.valueOf(BMSConfig.BIKE_CONN_CONNECTING));
    }

    @Override // no.nordicsemi.android.ble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice device, Data data) {
        Intrinsics.checkParameterIsNotNull(device, "device");
        Intrinsics.checkParameterIsNotNull(data, "data");
        Log.e("onDataReceived", "onDataReceived------------------------");
    }
}
