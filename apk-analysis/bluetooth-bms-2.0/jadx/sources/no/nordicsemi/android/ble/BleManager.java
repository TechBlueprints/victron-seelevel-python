package no.nordicsemi.android.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import java.util.UUID;
import no.nordicsemi.android.ble.ConditionalWaitRequest;
import no.nordicsemi.android.ble.callback.BeforeCallback;
import no.nordicsemi.android.ble.callback.SuccessCallback;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.observer.BondingObserver;
import no.nordicsemi.android.ble.observer.ConnectionObserver;
import no.nordicsemi.android.ble.utils.ILogger;
import no.nordicsemi.android.ble.utils.ParserUtils;

/* loaded from: classes.dex */
public abstract class BleManager implements ILogger {
    public static final int PAIRING_VARIANT_CONSENT = 3;
    public static final int PAIRING_VARIANT_DISPLAY_PASSKEY = 4;
    public static final int PAIRING_VARIANT_DISPLAY_PIN = 5;
    public static final int PAIRING_VARIANT_OOB_CONSENT = 6;
    public static final int PAIRING_VARIANT_PASSKEY = 1;
    public static final int PAIRING_VARIANT_PASSKEY_CONFIRMATION = 2;
    public static final int PAIRING_VARIANT_PIN = 0;
    BondingObserver bondingObserver;

    @Deprecated
    protected BleManagerCallbacks callbacks;
    ConnectionObserver connectionObserver;
    private final Context context;
    private final BroadcastReceiver mPairingRequestBroadcastReceiver;
    final BleManagerGattCallback requestHandler;
    private BleServerManager serverManager;
    static final UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    static final UUID BATTERY_SERVICE = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb");
    static final UUID BATTERY_LEVEL_CHARACTERISTIC = UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb");
    static final UUID GENERIC_ATTRIBUTE_SERVICE = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
    static final UUID SERVICE_CHANGED_CHARACTERISTIC = UUID.fromString("00002A05-0000-1000-8000-00805f9b34fb");

    protected abstract BleManagerGattCallback getGattCallback();

    protected int getServiceDiscoveryDelay(boolean z) {
        return z ? 1600 : 300;
    }

    @Override // no.nordicsemi.android.ble.utils.ILogger
    public void log(int i, String str) {
    }

    protected void onPairingRequestReceived(BluetoothDevice bluetoothDevice, int i) {
    }

    @Deprecated
    protected boolean shouldAutoConnect() {
        return false;
    }

    protected boolean shouldClearCacheWhenDisconnected() {
        return false;
    }

    public BleManager(Context context) {
        this(context, new Handler(Looper.getMainLooper()));
    }

    public BleManager(Context context, Handler handler) {
        this.mPairingRequestBroadcastReceiver = new BroadcastReceiver() { // from class: no.nordicsemi.android.ble.BleManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                BluetoothDevice bluetoothDevice2 = BleManager.this.requestHandler.getBluetoothDevice();
                if (bluetoothDevice2 == null || bluetoothDevice == null || !bluetoothDevice.getAddress().equals(bluetoothDevice2.getAddress())) {
                    return;
                }
                int intExtra = intent.getIntExtra("android.bluetooth.device.extra.PAIRING_VARIANT", 0);
                BleManager.this.log(3, "[Broadcast] Action received: android.bluetooth.device.action.PAIRING_REQUEST, pairing variant: " + ParserUtils.pairingVariantToString(intExtra) + " (" + intExtra + ")");
                BleManager.this.onPairingRequestReceived(bluetoothDevice, intExtra);
            }
        };
        this.context = context;
        BleManagerGattCallback gattCallback = getGattCallback();
        this.requestHandler = gattCallback;
        gattCallback.init(this, handler);
        context.registerReceiver(this.mPairingRequestBroadcastReceiver, new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST"));
    }

    public void close() {
        try {
            this.context.unregisterReceiver(this.mPairingRequestBroadcastReceiver);
        } catch (Exception unused) {
        }
        BleServerManager bleServerManager = this.serverManager;
        if (bleServerManager != null) {
            bleServerManager.removeManager(this);
        }
        this.requestHandler.close();
    }

    protected void runOnCallbackThread(Runnable runnable) {
        this.requestHandler.post(runnable);
    }

    @Deprecated
    public void setGattCallbacks(BleManagerCallbacks bleManagerCallbacks) {
        this.callbacks = bleManagerCallbacks;
    }

    public final void setConnectionObserver(ConnectionObserver connectionObserver) {
        this.connectionObserver = connectionObserver;
    }

    public final void setBondingObserver(BondingObserver bondingObserver) {
        this.bondingObserver = bondingObserver;
    }

    public final void useServer(BleServerManager bleServerManager) {
        BleServerManager bleServerManager2 = this.serverManager;
        if (bleServerManager2 != null) {
            bleServerManager2.removeManager(this);
        }
        this.serverManager = bleServerManager;
        bleServerManager.addManager(this);
        this.requestHandler.useServer(bleServerManager);
    }

    final void closeServer() {
        this.serverManager = null;
        this.requestHandler.useServer(null);
    }

    protected final Context getContext() {
        return this.context;
    }

    public BluetoothDevice getBluetoothDevice() {
        return this.requestHandler.getBluetoothDevice();
    }

    public final boolean isConnected() {
        return this.requestHandler.isConnected();
    }

    public final boolean isReady() {
        return this.requestHandler.isReady();
    }

    protected final boolean isBonded() {
        BluetoothDevice bluetoothDevice = this.requestHandler.getBluetoothDevice();
        return bluetoothDevice != null && bluetoothDevice.getBondState() == 12;
    }

    public final int getConnectionState() {
        return this.requestHandler.getConnectionState();
    }

    @Deprecated
    public final int getBatteryValue() {
        return this.requestHandler.getBatteryValue();
    }

    @Override // no.nordicsemi.android.ble.utils.ILogger
    public void log(int i, int i2, Object... objArr) {
        log(i, this.context.getString(i2, objArr));
    }

    public final ConnectRequest connect(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            throw new NullPointerException("Bluetooth device not specified");
        }
        return Request.connect(bluetoothDevice).useAutoConnect(shouldAutoConnect()).setRequestHandler((RequestHandler) this.requestHandler);
    }

    @Deprecated
    public final ConnectRequest connect(BluetoothDevice bluetoothDevice, int i) {
        if (bluetoothDevice == null) {
            throw new NullPointerException("Bluetooth device not specified");
        }
        return Request.connect(bluetoothDevice).usePreferredPhy(i).useAutoConnect(shouldAutoConnect()).setRequestHandler((RequestHandler) this.requestHandler);
    }

    public final DisconnectRequest disconnect() {
        return Request.disconnect().setRequestHandler((RequestHandler) this.requestHandler);
    }

    @Deprecated
    protected Request createBond() {
        return createBondInsecure();
    }

    protected Request createBondInsecure() {
        return Request.createBond().setRequestHandler(this.requestHandler);
    }

    protected Request ensureBond() {
        return Request.ensureBond().setRequestHandler(this.requestHandler);
    }

    protected Request removeBond() {
        return Request.removeBond().setRequestHandler(this.requestHandler);
    }

    protected ValueChangedCallback setNotificationCallback(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return this.requestHandler.getValueChangedCallback(bluetoothGattCharacteristic);
    }

    protected ValueChangedCallback setIndicationCallback(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return setNotificationCallback(bluetoothGattCharacteristic);
    }

    protected ValueChangedCallback setWriteCallback(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return this.requestHandler.getValueChangedCallback(bluetoothGattCharacteristic);
    }

    protected ValueChangedCallback setWriteCallback(BluetoothGattDescriptor bluetoothGattDescriptor) {
        return this.requestHandler.getValueChangedCallback(bluetoothGattDescriptor);
    }

    protected void removeNotificationCallback(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        this.requestHandler.removeValueChangedCallback(bluetoothGattCharacteristic);
    }

    protected void removeIndicationCallback(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        removeNotificationCallback(bluetoothGattCharacteristic);
    }

    protected void removeWriteCallback(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        this.requestHandler.removeValueChangedCallback(bluetoothGattCharacteristic);
    }

    protected void removeWriteCallback(BluetoothGattDescriptor bluetoothGattDescriptor) {
        this.requestHandler.removeValueChangedCallback(bluetoothGattDescriptor);
    }

    protected WaitForValueChangedRequest waitForNotification(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return Request.newWaitForNotificationRequest(bluetoothGattCharacteristic).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WaitForValueChangedRequest waitForIndication(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return Request.newWaitForIndicationRequest(bluetoothGattCharacteristic).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WaitForValueChangedRequest waitForWrite(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return Request.newWaitForWriteRequest(bluetoothGattCharacteristic).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WaitForValueChangedRequest waitForWrite(BluetoothGattDescriptor bluetoothGattDescriptor) {
        return Request.newWaitForWriteRequest(bluetoothGattDescriptor).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected ConditionalWaitRequest<Void> waitIf(ConditionalWaitRequest.Condition<Void> condition) {
        return Request.newConditionalWaitRequest(condition, null).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected <T> ConditionalWaitRequest<T> waitIf(T t, ConditionalWaitRequest.Condition<T> condition) {
        return Request.newConditionalWaitRequest(condition, t).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected ConditionalWaitRequest<Void> waitUntil(ConditionalWaitRequest.Condition<Void> condition) {
        return waitIf(condition).negate();
    }

    protected <T> ConditionalWaitRequest<T> waitUntil(T t, ConditionalWaitRequest.Condition<T> condition) {
        return waitIf(t, condition).negate();
    }

    protected ConditionalWaitRequest<BluetoothGattCharacteristic> waitUntilNotificationsEnabled(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return waitUntil(bluetoothGattCharacteristic, new ConditionalWaitRequest.Condition() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManager$mjZ4bGPGyigPrU0DH2s2PkAR36I
            @Override // no.nordicsemi.android.ble.ConditionalWaitRequest.Condition
            public final boolean predicate(Object obj) {
                return this.f$0.lambda$waitUntilNotificationsEnabled$0$BleManager((BluetoothGattCharacteristic) obj);
            }
        });
    }

    public /* synthetic */ boolean lambda$waitUntilNotificationsEnabled$0$BleManager(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGattDescriptor descriptor;
        byte[] descriptorValue;
        return (bluetoothGattCharacteristic == null || (descriptor = bluetoothGattCharacteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID)) == null || (descriptorValue = this.requestHandler.getDescriptorValue(descriptor)) == null || descriptorValue.length != 2 || (descriptorValue[0] & 1) != 1) ? false : true;
    }

    protected ConditionalWaitRequest<BluetoothGattCharacteristic> waitUntilIndicationsEnabled(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return waitUntil(bluetoothGattCharacteristic, new ConditionalWaitRequest.Condition() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManager$7rqwTb07tWh-L-I4J_i6I_uiAS4
            @Override // no.nordicsemi.android.ble.ConditionalWaitRequest.Condition
            public final boolean predicate(Object obj) {
                return this.f$0.lambda$waitUntilIndicationsEnabled$1$BleManager((BluetoothGattCharacteristic) obj);
            }
        });
    }

    public /* synthetic */ boolean lambda$waitUntilIndicationsEnabled$1$BleManager(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGattDescriptor descriptor;
        byte[] descriptorValue;
        return (bluetoothGattCharacteristic == null || (descriptor = bluetoothGattCharacteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID)) == null || (descriptorValue = this.requestHandler.getDescriptorValue(descriptor)) == null || descriptorValue.length != 2 || (descriptorValue[0] & 2) != 2) ? false : true;
    }

    protected WaitForReadRequest waitForRead(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return Request.newWaitForReadRequest(bluetoothGattCharacteristic).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WaitForReadRequest waitForRead(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        return Request.newWaitForReadRequest(bluetoothGattCharacteristic, bArr).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WaitForReadRequest waitForRead(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i, int i2) {
        return Request.newWaitForReadRequest(bluetoothGattCharacteristic, bArr, i, i2).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WaitForReadRequest waitForRead(BluetoothGattDescriptor bluetoothGattDescriptor) {
        return Request.newWaitForReadRequest(bluetoothGattDescriptor).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WaitForReadRequest waitForRead(BluetoothGattDescriptor bluetoothGattDescriptor, byte[] bArr) {
        return Request.newWaitForReadRequest(bluetoothGattDescriptor, bArr).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WaitForReadRequest waitForRead(BluetoothGattDescriptor bluetoothGattDescriptor, byte[] bArr, int i, int i2) {
        return Request.newWaitForReadRequest(bluetoothGattDescriptor, bArr, i, i2).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected SetValueRequest setCharacteristicValue(BluetoothGattCharacteristic bluetoothGattCharacteristic, Data data) {
        return Request.newSetValueRequest(bluetoothGattCharacteristic, data != null ? data.getValue() : null).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected SetValueRequest setCharacteristicValue(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        return Request.newSetValueRequest(bluetoothGattCharacteristic, bArr).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected SetValueRequest setCharacteristicValue(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i, int i2) {
        return Request.newSetValueRequest(bluetoothGattCharacteristic, bArr, i, i2).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected SetValueRequest setDescriptorValue(BluetoothGattDescriptor bluetoothGattDescriptor, Data data) {
        return Request.newSetValueRequest(bluetoothGattDescriptor, data != null ? data.getValue() : null).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected SetValueRequest setDescriptorValue(BluetoothGattDescriptor bluetoothGattDescriptor, byte[] bArr) {
        return Request.newSetValueRequest(bluetoothGattDescriptor, bArr).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected SetValueRequest setDescriptorValue(BluetoothGattDescriptor bluetoothGattDescriptor, byte[] bArr, int i, int i2) {
        return Request.newSetValueRequest(bluetoothGattDescriptor, bArr, i, i2).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WriteRequest enableNotifications(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return Request.newEnableNotificationsRequest(bluetoothGattCharacteristic).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WriteRequest disableNotifications(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return Request.newDisableNotificationsRequest(bluetoothGattCharacteristic).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WriteRequest enableIndications(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return Request.newEnableIndicationsRequest(bluetoothGattCharacteristic).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WriteRequest disableIndications(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return Request.newDisableIndicationsRequest(bluetoothGattCharacteristic).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected ReadRequest readCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return Request.newReadRequest(bluetoothGattCharacteristic).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WriteRequest writeCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic, Data data) {
        return Request.newWriteRequest(bluetoothGattCharacteristic, data != null ? data.getValue() : null).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WriteRequest writeCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        return Request.newWriteRequest(bluetoothGattCharacteristic, bArr).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WriteRequest writeCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i, int i2) {
        return Request.newWriteRequest(bluetoothGattCharacteristic, bArr, i, i2).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected ReadRequest readDescriptor(BluetoothGattDescriptor bluetoothGattDescriptor) {
        return Request.newReadRequest(bluetoothGattDescriptor).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WriteRequest writeDescriptor(BluetoothGattDescriptor bluetoothGattDescriptor, Data data) {
        return Request.newWriteRequest(bluetoothGattDescriptor, data != null ? data.getValue() : null).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WriteRequest writeDescriptor(BluetoothGattDescriptor bluetoothGattDescriptor, byte[] bArr) {
        return Request.newWriteRequest(bluetoothGattDescriptor, bArr).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WriteRequest writeDescriptor(BluetoothGattDescriptor bluetoothGattDescriptor, byte[] bArr, int i, int i2) {
        return Request.newWriteRequest(bluetoothGattDescriptor, bArr, i, i2).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WriteRequest sendNotification(BluetoothGattCharacteristic bluetoothGattCharacteristic, Data data) {
        return Request.newNotificationRequest(bluetoothGattCharacteristic, data != null ? data.getValue() : null).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WriteRequest sendNotification(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        return Request.newNotificationRequest(bluetoothGattCharacteristic, bArr).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WriteRequest sendNotification(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i, int i2) {
        return Request.newNotificationRequest(bluetoothGattCharacteristic, bArr, i, i2).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WriteRequest sendIndication(BluetoothGattCharacteristic bluetoothGattCharacteristic, Data data) {
        return Request.newIndicationRequest(bluetoothGattCharacteristic, data != null ? data.getValue() : null).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WriteRequest sendIndication(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        return Request.newIndicationRequest(bluetoothGattCharacteristic, bArr).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected WriteRequest sendIndication(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i, int i2) {
        return Request.newIndicationRequest(bluetoothGattCharacteristic, bArr, i, i2).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected RequestQueue beginAtomicRequestQueue() {
        return new RequestQueue().setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected ReliableWriteRequest beginReliableWrite() {
        return Request.newReliableWriteRequest().setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected final boolean isReliableWriteInProgress() {
        return this.requestHandler.isReliableWriteInProgress();
    }

    @Deprecated
    protected void readBatteryLevel() {
        Request.newReadBatteryLevelRequest().setRequestHandler((RequestHandler) this.requestHandler).with(this.requestHandler.getBatteryLevelCallback()).enqueue();
    }

    @Deprecated
    protected void enableBatteryLevelNotifications() {
        Request.newEnableBatteryLevelNotificationsRequest().setRequestHandler((RequestHandler) this.requestHandler).before(new BeforeCallback() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManager$Q5fcucTuqb47jxDYbaeYE5kksFw
            @Override // no.nordicsemi.android.ble.callback.BeforeCallback
            public final void onRequestStarted(BluetoothDevice bluetoothDevice) {
                this.f$0.lambda$enableBatteryLevelNotifications$2$BleManager(bluetoothDevice);
            }
        }).done(new SuccessCallback() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManager$Nw1Kgew0SBjy4yzqQ6SY-G0d3Nc
            @Override // no.nordicsemi.android.ble.callback.SuccessCallback
            public final void onRequestCompleted(BluetoothDevice bluetoothDevice) {
                this.f$0.lambda$enableBatteryLevelNotifications$3$BleManager(bluetoothDevice);
            }
        }).enqueue();
    }

    public /* synthetic */ void lambda$enableBatteryLevelNotifications$2$BleManager(BluetoothDevice bluetoothDevice) {
        this.requestHandler.setBatteryLevelNotificationCallback();
    }

    public /* synthetic */ void lambda$enableBatteryLevelNotifications$3$BleManager(BluetoothDevice bluetoothDevice) {
        log(4, "Battery Level notifications enabled");
    }

    @Deprecated
    protected void disableBatteryLevelNotifications() {
        Request.newDisableBatteryLevelNotificationsRequest().setRequestHandler((RequestHandler) this.requestHandler).done(new SuccessCallback() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManager$hyRHOb0JCPbFA9KMkeba5Z3Kw8A
            @Override // no.nordicsemi.android.ble.callback.SuccessCallback
            public final void onRequestCompleted(BluetoothDevice bluetoothDevice) {
                this.f$0.lambda$disableBatteryLevelNotifications$4$BleManager(bluetoothDevice);
            }
        }).enqueue();
    }

    public /* synthetic */ void lambda$disableBatteryLevelNotifications$4$BleManager(BluetoothDevice bluetoothDevice) {
        log(4, "Battery Level notifications disabled");
    }

    protected MtuRequest requestMtu(int i) {
        return Request.newMtuRequest(i).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected int getMtu() {
        return this.requestHandler.getMtu();
    }

    protected void overrideMtu(int i) {
        this.requestHandler.overrideMtu(i);
    }

    protected ConnectionPriorityRequest requestConnectionPriority(int i) {
        return Request.newConnectionPriorityRequest(i).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected PhyRequest setPreferredPhy(int i, int i2, int i3) {
        return Request.newSetPreferredPhyRequest(i, i2, i3).setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected PhyRequest readPhy() {
        return Request.newReadPhyRequest().setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected ReadRssiRequest readRssi() {
        return Request.newReadRssiRequest().setRequestHandler((RequestHandler) this.requestHandler);
    }

    protected Request refreshDeviceCache() {
        return Request.newRefreshCacheRequest().setRequestHandler(this.requestHandler);
    }

    protected SleepRequest sleep(long j) {
        return Request.newSleepRequest(j).setRequestHandler((RequestHandler) this.requestHandler);
    }

    @Deprecated
    protected final void enqueue(Request request) {
        this.requestHandler.enqueue(request);
    }

    protected final void cancelQueue() {
        this.requestHandler.cancelQueue();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static abstract class BleManagerGattCallback extends BleManagerHandler {
        protected BleManagerGattCallback() {
        }
    }
}
