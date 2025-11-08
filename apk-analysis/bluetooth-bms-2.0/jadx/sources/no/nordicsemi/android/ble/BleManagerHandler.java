package no.nordicsemi.android.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import no.nordicsemi.android.ble.Request;
import no.nordicsemi.android.ble.callback.DataReceivedCallback;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.error.GattError;
import no.nordicsemi.android.ble.observer.BondingObserver;
import no.nordicsemi.android.ble.observer.ConnectionObserver;
import no.nordicsemi.android.ble.utils.ParserUtils;

/* loaded from: classes.dex */
abstract class BleManagerHandler extends RequestHandler {
    private static final long CONNECTION_TIMEOUT_THRESHOLD = 20000;
    private static final String ERROR_AUTH_ERROR_WHILE_BONDED = "Phone has lost bonding information";
    private static final String ERROR_CONNECTION_PRIORITY_REQUEST = "Error on connection priority request";
    private static final String ERROR_CONNECTION_STATE_CHANGE = "Error on connection state change";
    private static final String ERROR_DISCOVERY_SERVICE = "Error on discovering services";
    private static final String ERROR_MTU_REQUEST = "Error on mtu request";
    private static final String ERROR_NOTIFY = "Error on sending notification/indication";
    private static final String ERROR_PHY_UPDATE = "Error on PHY update";
    private static final String ERROR_READ_CHARACTERISTIC = "Error on reading characteristic";
    private static final String ERROR_READ_DESCRIPTOR = "Error on reading descriptor";
    private static final String ERROR_READ_PHY = "Error on PHY read";
    private static final String ERROR_READ_RSSI = "Error on RSSI read";
    private static final String ERROR_RELIABLE_WRITE = "Error on Execute Reliable Write";
    private static final String ERROR_WRITE_CHARACTERISTIC = "Error on writing characteristic";
    private static final String ERROR_WRITE_DESCRIPTOR = "Error on writing descriptor";
    private static final String TAG = "BleManager";
    private AwaitingRequest<?> awaitingRequest;

    @Deprecated
    private ValueChangedCallback batteryLevelNotificationCallback;
    private BluetoothDevice bluetoothDevice;
    private BluetoothGatt bluetoothGatt;
    private Map<BluetoothGattCharacteristic, byte[]> characteristicValues;
    private ConnectRequest connectRequest;
    private boolean connected;
    private long connectionTime;
    private Map<BluetoothGattDescriptor, byte[]> descriptorValues;
    private boolean deviceNotSupported;
    private Handler handler;
    private boolean initInProgress;
    private Deque<Request> initQueue;
    private boolean initialConnection;
    private BleManager manager;
    private boolean operationInProgress;
    private int prepareError;
    private Deque<Pair<Object, byte[]>> preparedValues;
    private boolean ready;
    private boolean reliableWriteInProgress;
    private Request request;
    private RequestQueue requestQueue;
    private BleServerManager serverManager;
    private boolean serviceDiscoveryRequested;
    private boolean servicesDiscovered;
    private boolean userDisconnected;
    private final Object LOCK = new Object();
    private final Deque<Request> taskQueue = new LinkedBlockingDeque();
    private int connectionCount = 0;
    private int connectionState = 0;
    private boolean connectionPriorityOperationInProgress = false;
    private int mtu = 23;

    @Deprecated
    private int batteryValue = -1;
    private final HashMap<Object, ValueChangedCallback> valueChangedCallbacks = new HashMap<>();
    private final BroadcastReceiver bluetoothStateBroadcastReceiver = new BroadcastReceiver() { // from class: no.nordicsemi.android.ble.BleManagerHandler.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 10);
            int intExtra2 = intent.getIntExtra("android.bluetooth.adapter.extra.PREVIOUS_STATE", 10);
            BleManagerHandler.this.log(3, "[Broadcast] Action received: android.bluetooth.adapter.action.STATE_CHANGED, state changed to " + state2String(intExtra));
            if (intExtra == 10 || intExtra == 13) {
                if (intExtra2 != 13 && intExtra2 != 10) {
                    BleManagerHandler.this.operationInProgress = true;
                    BleManagerHandler.this.taskQueue.clear();
                    BleManagerHandler.this.initQueue = null;
                    BluetoothDevice bluetoothDevice = BleManagerHandler.this.bluetoothDevice;
                    if (bluetoothDevice != null) {
                        if (BleManagerHandler.this.request != null && BleManagerHandler.this.request.type != Request.Type.DISCONNECT) {
                            BleManagerHandler.this.request.notifyFail(bluetoothDevice, -100);
                            BleManagerHandler.this.request = null;
                        }
                        if (BleManagerHandler.this.awaitingRequest != null) {
                            BleManagerHandler.this.awaitingRequest.notifyFail(bluetoothDevice, -100);
                            BleManagerHandler.this.awaitingRequest = null;
                        }
                        if (BleManagerHandler.this.connectRequest != null) {
                            BleManagerHandler.this.connectRequest.notifyFail(bluetoothDevice, -100);
                            BleManagerHandler.this.connectRequest = null;
                        }
                    }
                    BleManagerHandler.this.userDisconnected = true;
                    BleManagerHandler.this.operationInProgress = false;
                    if (bluetoothDevice != null) {
                        BleManagerHandler.this.notifyDeviceDisconnected(bluetoothDevice, 1);
                        return;
                    }
                    return;
                }
                BleManagerHandler.this.close();
            }
        }

        private String state2String(int i) {
            switch (i) {
                case 10:
                    return "OFF";
                case 11:
                    return "TURNING ON";
                case 12:
                    return "ON";
                case 13:
                    return "TURNING OFF";
                default:
                    return "UNKNOWN (" + i + ")";
            }
        }
    };
    private final BroadcastReceiver mBondingBroadcastReceiver = new AnonymousClass2();
    private final BluetoothGattCallback gattCallback = new AnonymousClass3();

    /* JADX INFO: Access modifiers changed from: private */
    interface BondingObserverRunnable {
        void run(BondingObserver bondingObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Deprecated
    interface CallbackRunnable {
        void run(BleManagerCallbacks bleManagerCallbacks);
    }

    /* JADX INFO: Access modifiers changed from: private */
    interface ConnectionObserverRunnable {
        void run(ConnectionObserver connectionObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int mapDisconnectStatusToReason(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 8) {
            return 10;
        }
        if (i != 19) {
            return i != 22 ? -1 : 1;
        }
        return 2;
    }

    @Deprecated
    protected Deque<Request> initGatt(BluetoothGatt bluetoothGatt) {
        return null;
    }

    protected void initialize() {
    }

    protected boolean isOptionalServiceSupported(BluetoothGatt bluetoothGatt) {
        return false;
    }

    protected abstract boolean isRequiredServiceSupported(BluetoothGatt bluetoothGatt);

    @Deprecated
    protected void onBatteryValueReceived(BluetoothGatt bluetoothGatt, int i) {
    }

    @Deprecated
    protected void onCharacteristicIndicated(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    @Deprecated
    protected void onCharacteristicNotified(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    @Deprecated
    protected void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    @Deprecated
    protected void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    @Deprecated
    protected void onConnectionUpdated(BluetoothGatt bluetoothGatt, int i, int i2, int i3) {
    }

    @Deprecated
    protected void onDescriptorRead(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor) {
    }

    @Deprecated
    protected void onDescriptorWrite(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor) {
    }

    protected abstract void onDeviceDisconnected();

    protected void onDeviceReady() {
    }

    protected void onManagerReady() {
    }

    @Deprecated
    protected void onMtuChanged(BluetoothGatt bluetoothGatt, int i) {
    }

    protected void onServerReady(BluetoothGattServer bluetoothGattServer) {
    }

    BleManagerHandler() {
    }

    static /* synthetic */ int access$2204(BleManagerHandler bleManagerHandler) {
        int i = bleManagerHandler.connectionCount + 1;
        bleManagerHandler.connectionCount = i;
        return i;
    }

    /* renamed from: no.nordicsemi.android.ble.BleManagerHandler$2, reason: invalid class name */
    class AnonymousClass2 extends BroadcastReceiver {
        AnonymousClass2() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            final BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            int intExtra = intent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", -1);
            int intExtra2 = intent.getIntExtra("android.bluetooth.device.extra.PREVIOUS_BOND_STATE", -1);
            if (BleManagerHandler.this.bluetoothDevice == null || bluetoothDevice == null || !bluetoothDevice.getAddress().equals(BleManagerHandler.this.bluetoothDevice.getAddress())) {
                return;
            }
            BleManagerHandler.this.log(3, "[Broadcast] Action received: android.bluetooth.device.action.BOND_STATE_CHANGED, bond state changed to: " + ParserUtils.bondStateToString(intExtra) + " (" + intExtra + ")");
            switch (intExtra) {
                case 10:
                    if (intExtra2 != 11) {
                        if (intExtra2 == 12) {
                            if (BleManagerHandler.this.request != null && BleManagerHandler.this.request.type == Request.Type.REMOVE_BOND) {
                                BleManagerHandler.this.log(4, "Bond information removed");
                                BleManagerHandler.this.request.notifySuccess(bluetoothDevice);
                                BleManagerHandler.this.request = null;
                            }
                            BleManagerHandler.this.close();
                            break;
                        }
                    } else {
                        BleManagerHandler.this.postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$2$GrPDiqsuxaY8NDRxigKOnHodZnU
                            @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                            public final void run(BleManagerCallbacks bleManagerCallbacks) {
                                bleManagerCallbacks.onBondingFailed(bluetoothDevice);
                            }
                        });
                        BleManagerHandler.this.postBondingStateChange(new BondingObserverRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$2$LrpMNvnTBVyRR6elWABpGyUBOYc
                            @Override // no.nordicsemi.android.ble.BleManagerHandler.BondingObserverRunnable
                            public final void run(BondingObserver bondingObserver) {
                                bondingObserver.onBondingFailed(bluetoothDevice);
                            }
                        });
                        BleManagerHandler.this.log(5, "Bonding failed");
                        if (BleManagerHandler.this.request != null) {
                            BleManagerHandler.this.request.notifyFail(bluetoothDevice, -4);
                            BleManagerHandler.this.request = null;
                            break;
                        }
                    }
                    break;
                case 11:
                    BleManagerHandler.this.postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$2$xe5AHoK5gkt4BvBW8sfKDssEbuQ
                        @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                        public final void run(BleManagerCallbacks bleManagerCallbacks) {
                            bleManagerCallbacks.onBondingRequired(bluetoothDevice);
                        }
                    });
                    BleManagerHandler.this.postBondingStateChange(new BondingObserverRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$2$kau-7hevqyVc0338wVDRp_whL9k
                        @Override // no.nordicsemi.android.ble.BleManagerHandler.BondingObserverRunnable
                        public final void run(BondingObserver bondingObserver) {
                            bondingObserver.onBondingRequired(bluetoothDevice);
                        }
                    });
                    return;
                case 12:
                    BleManagerHandler.this.log(4, "Device bonded");
                    BleManagerHandler.this.postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$2$CjdfqwDL_KwY_joA8UfMhkdSzaQ
                        @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                        public final void run(BleManagerCallbacks bleManagerCallbacks) {
                            bleManagerCallbacks.onBonded(bluetoothDevice);
                        }
                    });
                    BleManagerHandler.this.postBondingStateChange(new BondingObserverRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$2$7gdgzXLoRBzqZNaFEN7tUtwKMSM
                        @Override // no.nordicsemi.android.ble.BleManagerHandler.BondingObserverRunnable
                        public final void run(BondingObserver bondingObserver) {
                            bondingObserver.onBonded(bluetoothDevice);
                        }
                    });
                    if (BleManagerHandler.this.request == null || BleManagerHandler.this.request.type != Request.Type.CREATE_BOND) {
                        if (!BleManagerHandler.this.servicesDiscovered && !BleManagerHandler.this.serviceDiscoveryRequested) {
                            BleManagerHandler.this.serviceDiscoveryRequested = true;
                            BleManagerHandler.this.post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$2$Ro3S4ZmJQ8yWhDiR0hSU5V2NxHY
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$onReceive$6$BleManagerHandler$2();
                                }
                            });
                            return;
                        } else if (Build.VERSION.SDK_INT < 26 && BleManagerHandler.this.request != null) {
                            BleManagerHandler bleManagerHandler = BleManagerHandler.this;
                            bleManagerHandler.enqueueFirst(bleManagerHandler.request);
                            break;
                        } else {
                            return;
                        }
                    } else {
                        BleManagerHandler.this.request.notifySuccess(bluetoothDevice);
                        BleManagerHandler.this.request = null;
                        break;
                    }
                    break;
            }
            BleManagerHandler.this.nextRequest(true);
        }

        public /* synthetic */ void lambda$onReceive$6$BleManagerHandler$2() {
            BleManagerHandler.this.log(2, "Discovering services...");
            BleManagerHandler.this.log(3, "gatt.discoverServices()");
            BleManagerHandler.this.bluetoothGatt.discoverServices();
        }
    }

    void init(BleManager bleManager, Handler handler) {
        this.manager = bleManager;
        this.handler = handler;
    }

    void useServer(BleServerManager bleServerManager) {
        this.serverManager = bleServerManager;
    }

    void close() {
        try {
            Context context = this.manager.getContext();
            context.unregisterReceiver(this.bluetoothStateBroadcastReceiver);
            context.unregisterReceiver(this.mBondingBroadcastReceiver);
        } catch (Exception unused) {
        }
        synchronized (this.LOCK) {
            if (this.bluetoothGatt != null) {
                if (this.manager.shouldClearCacheWhenDisconnected()) {
                    if (internalRefreshDeviceCache()) {
                        log(4, "Cache refreshed");
                    } else {
                        log(5, "Refreshing failed");
                    }
                }
                log(3, "gatt.close()");
                try {
                    this.bluetoothGatt.close();
                } catch (Throwable unused2) {
                }
                this.bluetoothGatt = null;
            }
            this.reliableWriteInProgress = false;
            this.initialConnection = false;
            this.valueChangedCallbacks.clear();
            this.taskQueue.clear();
            this.initQueue = null;
            this.bluetoothDevice = null;
        }
    }

    public BluetoothDevice getBluetoothDevice() {
        return this.bluetoothDevice;
    }

    public final byte[] getCharacteristicValue(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        Map<BluetoothGattCharacteristic, byte[]> map = this.characteristicValues;
        if (map != null && map.containsKey(bluetoothGattCharacteristic)) {
            return this.characteristicValues.get(bluetoothGattCharacteristic);
        }
        return bluetoothGattCharacteristic.getValue();
    }

    public final byte[] getDescriptorValue(BluetoothGattDescriptor bluetoothGattDescriptor) {
        Map<BluetoothGattDescriptor, byte[]> map = this.descriptorValues;
        if (map != null && map.containsKey(bluetoothGattDescriptor)) {
            return this.descriptorValues.get(bluetoothGattDescriptor);
        }
        return bluetoothGattDescriptor.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean internalConnect(final BluetoothDevice bluetoothDevice, ConnectRequest connectRequest) {
        boolean zIsEnabled = BluetoothAdapter.getDefaultAdapter().isEnabled();
        if (this.connected || !zIsEnabled) {
            BluetoothDevice bluetoothDevice2 = this.bluetoothDevice;
            if (zIsEnabled && bluetoothDevice2 != null && bluetoothDevice2.equals(bluetoothDevice)) {
                this.connectRequest.notifySuccess(bluetoothDevice);
            } else {
                ConnectRequest connectRequest2 = this.connectRequest;
                if (connectRequest2 != null) {
                    connectRequest2.notifyFail(bluetoothDevice, zIsEnabled ? -4 : -100);
                }
            }
            this.connectRequest = null;
            nextRequest(true);
            return true;
        }
        Context context = this.manager.getContext();
        synchronized (this.LOCK) {
            if (this.bluetoothGatt != null) {
                if (!this.initialConnection) {
                    log(3, "gatt.close()");
                    try {
                        this.bluetoothGatt.close();
                    } catch (Throwable unused) {
                    }
                    this.bluetoothGatt = null;
                    try {
                        log(3, "wait(200)");
                        Thread.sleep(200L);
                    } catch (InterruptedException unused2) {
                    }
                } else {
                    this.initialConnection = false;
                    this.connectionTime = 0L;
                    this.connectionState = 1;
                    log(2, "Connecting...");
                    postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$Jf4ep4P2gYeHtqb1moyxUk69j48
                        @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                        public final void run(BleManagerCallbacks bleManagerCallbacks) {
                            bleManagerCallbacks.onDeviceConnecting(bluetoothDevice);
                        }
                    });
                    postConnectionStateChange(new ConnectionObserverRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$xsAn3VnTmy-wWr_8GjRepwUa0oo
                        @Override // no.nordicsemi.android.ble.BleManagerHandler.ConnectionObserverRunnable
                        public final void run(ConnectionObserver connectionObserver) {
                            connectionObserver.onDeviceConnecting(bluetoothDevice);
                        }
                    });
                    log(3, "gatt.connect()");
                    this.bluetoothGatt.connect();
                    return true;
                }
            } else {
                context.registerReceiver(this.bluetoothStateBroadcastReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
                context.registerReceiver(this.mBondingBroadcastReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
            }
            if (connectRequest == null) {
                return false;
            }
            boolean zShouldAutoConnect = connectRequest.shouldAutoConnect();
            this.userDisconnected = !zShouldAutoConnect;
            if (zShouldAutoConnect) {
                this.initialConnection = true;
            }
            this.bluetoothDevice = bluetoothDevice;
            log(2, connectRequest.isFirstAttempt() ? "Connecting..." : "Retrying...");
            this.connectionState = 1;
            postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$ACKCnBiL3Novo_t94euqReAL7rw
                @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                public final void run(BleManagerCallbacks bleManagerCallbacks) {
                    bleManagerCallbacks.onDeviceConnecting(bluetoothDevice);
                }
            });
            postConnectionStateChange(new ConnectionObserverRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$E4otc7EAFoByH8kKtHWepM1ZBCU
                @Override // no.nordicsemi.android.ble.BleManagerHandler.ConnectionObserverRunnable
                public final void run(ConnectionObserver connectionObserver) {
                    connectionObserver.onDeviceConnecting(bluetoothDevice);
                }
            });
            this.connectionTime = SystemClock.elapsedRealtime();
            if (Build.VERSION.SDK_INT >= 26) {
                int preferredPhy = connectRequest.getPreferredPhy();
                log(3, "gatt = device.connectGatt(autoConnect = false, TRANSPORT_LE, " + ParserUtils.phyMaskToString(preferredPhy) + ")");
                this.bluetoothGatt = bluetoothDevice.connectGatt(context, false, this.gattCallback, 2, preferredPhy);
            } else if (Build.VERSION.SDK_INT >= 23) {
                log(3, "gatt = device.connectGatt(autoConnect = false, TRANSPORT_LE)");
                this.bluetoothGatt = bluetoothDevice.connectGatt(context, false, this.gattCallback, 2);
            } else {
                log(3, "gatt = device.connectGatt(autoConnect = false)");
                this.bluetoothGatt = bluetoothDevice.connectGatt(context, false, this.gattCallback);
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean internalDisconnect() {
        this.userDisconnected = true;
        this.initialConnection = false;
        this.ready = false;
        if (this.bluetoothGatt != null) {
            this.connectionState = 3;
            log(2, this.connected ? "Disconnecting..." : "Cancelling connection...");
            final BluetoothDevice device = this.bluetoothGatt.getDevice();
            if (this.connected) {
                postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$6YkrBFRivM1XdafAE2ojISIXkmo
                    @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                    public final void run(BleManagerCallbacks bleManagerCallbacks) {
                        bleManagerCallbacks.onDeviceDisconnecting(device);
                    }
                });
                postConnectionStateChange(new ConnectionObserverRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$P59ypFooIx2weu89KOH_QPgAEhM
                    @Override // no.nordicsemi.android.ble.BleManagerHandler.ConnectionObserverRunnable
                    public final void run(ConnectionObserver connectionObserver) {
                        connectionObserver.onDeviceDisconnecting(device);
                    }
                });
            }
            log(3, "gatt.disconnect()");
            this.bluetoothGatt.disconnect();
            if (this.connected) {
                return true;
            }
            this.connectionState = 0;
            log(4, "Disconnected");
            postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$nlmiobmnXip6ebJqhKDeJmeNKe0
                @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                public final void run(BleManagerCallbacks bleManagerCallbacks) {
                    bleManagerCallbacks.onDeviceDisconnected(device);
                }
            });
            postConnectionStateChange(new ConnectionObserverRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$c6AzsZxpcT6nLvAfBCl7kAE8b7E
                @Override // no.nordicsemi.android.ble.BleManagerHandler.ConnectionObserverRunnable
                public final void run(ConnectionObserver connectionObserver) {
                    connectionObserver.onDeviceDisconnected(device, 0);
                }
            });
        }
        Request request = this.request;
        if (request != null && request.type == Request.Type.DISCONNECT) {
            BluetoothDevice bluetoothDevice = this.bluetoothDevice;
            if (bluetoothDevice != null) {
                this.request.notifySuccess(bluetoothDevice);
            } else {
                this.request.notifyInvalidRequest();
            }
        }
        nextRequest(true);
        return true;
    }

    private boolean internalCreateBond(boolean z) throws NoSuchMethodException, SecurityException {
        BluetoothDevice bluetoothDevice = this.bluetoothDevice;
        if (bluetoothDevice == null) {
            return false;
        }
        if (z) {
            log(2, "Ensuring bonding...");
        } else {
            log(2, "Starting bonding...");
        }
        if (!z && bluetoothDevice.getBondState() == 12) {
            log(5, "Bond information present on client, skipping bonding");
            this.request.notifySuccess(bluetoothDevice);
            nextRequest(true);
            return true;
        }
        boolean zCreateBond = createBond(bluetoothDevice);
        if (!z || zCreateBond) {
            return zCreateBond;
        }
        Request requestHandler = Request.createBond().setRequestHandler(this);
        requestHandler.successCallback = this.request.successCallback;
        requestHandler.invalidRequestCallback = this.request.invalidRequestCallback;
        requestHandler.failCallback = this.request.failCallback;
        requestHandler.internalSuccessCallback = this.request.internalSuccessCallback;
        requestHandler.internalFailCallback = this.request.internalFailCallback;
        this.request.successCallback = null;
        this.request.invalidRequestCallback = null;
        this.request.failCallback = null;
        this.request.internalSuccessCallback = null;
        this.request.internalFailCallback = null;
        enqueueFirst(requestHandler);
        enqueueFirst(Request.removeBond().setRequestHandler(this));
        nextRequest(true);
        return true;
    }

    private boolean createBond(BluetoothDevice bluetoothDevice) throws NoSuchMethodException, SecurityException {
        if (Build.VERSION.SDK_INT >= 19) {
            log(3, "device.createBond()");
            return bluetoothDevice.createBond();
        }
        try {
            Method method = bluetoothDevice.getClass().getMethod("createBond", new Class[0]);
            log(3, "device.createBond() (hidden)");
            return ((Boolean) method.invoke(bluetoothDevice, new Object[0])).booleanValue();
        } catch (Exception e) {
            Log.w(TAG, "An exception occurred while creating bond", e);
            return false;
        }
    }

    private boolean internalRemoveBond() throws NoSuchMethodException, SecurityException {
        BluetoothDevice bluetoothDevice = this.bluetoothDevice;
        if (bluetoothDevice == null) {
            return false;
        }
        log(2, "Removing bond information...");
        if (bluetoothDevice.getBondState() == 10) {
            log(5, "Device is not bonded");
            this.request.notifySuccess(bluetoothDevice);
            nextRequest(true);
            return true;
        }
        try {
            Method method = bluetoothDevice.getClass().getMethod("removeBond", new Class[0]);
            log(3, "device.removeBond() (hidden)");
            return ((Boolean) method.invoke(bluetoothDevice, new Object[0])).booleanValue();
        } catch (Exception e) {
            Log.w(TAG, "An exception occurred while removing bond", e);
            return false;
        }
    }

    private boolean ensureServiceChangedEnabled() {
        BluetoothGattService service;
        BluetoothGattCharacteristic characteristic;
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || !this.connected || bluetoothGatt.getDevice().getBondState() != 12 || (service = bluetoothGatt.getService(BleManager.GENERIC_ATTRIBUTE_SERVICE)) == null || (characteristic = service.getCharacteristic(BleManager.SERVICE_CHANGED_CHARACTERISTIC)) == null) {
            return false;
        }
        log(4, "Service Changed characteristic found on a bonded device");
        return internalEnableIndications(characteristic);
    }

    private boolean internalEnableNotifications(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGattDescriptor cccd;
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || bluetoothGattCharacteristic == null || !this.connected || (cccd = getCccd(bluetoothGattCharacteristic, 16)) == null) {
            return false;
        }
        log(3, "gatt.setCharacteristicNotification(" + bluetoothGattCharacteristic.getUuid() + ", true)");
        bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, true);
        cccd.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        log(2, "Enabling notifications for " + bluetoothGattCharacteristic.getUuid());
        log(3, "gatt.writeDescriptor(" + BleManager.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID + ", value=0x01-00)");
        return internalWriteDescriptorWorkaround(cccd);
    }

    private boolean internalDisableNotifications(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGattDescriptor cccd;
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || bluetoothGattCharacteristic == null || !this.connected || (cccd = getCccd(bluetoothGattCharacteristic, 16)) == null) {
            return false;
        }
        log(3, "gatt.setCharacteristicNotification(" + bluetoothGattCharacteristic.getUuid() + ", false)");
        bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, false);
        cccd.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        log(2, "Disabling notifications and indications for " + bluetoothGattCharacteristic.getUuid());
        log(3, "gatt.writeDescriptor(" + BleManager.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID + ", value=0x00-00)");
        return internalWriteDescriptorWorkaround(cccd);
    }

    private boolean internalEnableIndications(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGattDescriptor cccd;
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || bluetoothGattCharacteristic == null || !this.connected || (cccd = getCccd(bluetoothGattCharacteristic, 32)) == null) {
            return false;
        }
        log(3, "gatt.setCharacteristicNotification(" + bluetoothGattCharacteristic.getUuid() + ", true)");
        bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, true);
        cccd.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
        log(2, "Enabling indications for " + bluetoothGattCharacteristic.getUuid());
        log(3, "gatt.writeDescriptor(" + BleManager.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID + ", value=0x02-00)");
        return internalWriteDescriptorWorkaround(cccd);
    }

    private boolean internalDisableIndications(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return internalDisableNotifications(bluetoothGattCharacteristic);
    }

    private boolean internalSendNotification(BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z) {
        BluetoothGattDescriptor descriptor;
        BleServerManager bleServerManager = this.serverManager;
        if (bleServerManager == null || bleServerManager.getServer() == null || bluetoothGattCharacteristic == null) {
            return false;
        }
        if (((z ? 32 : 16) & bluetoothGattCharacteristic.getProperties()) == 0 || (descriptor = bluetoothGattCharacteristic.getDescriptor(BleManager.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID)) == null) {
            return false;
        }
        byte[] value = this.descriptorValues.containsKey(descriptor) ? this.descriptorValues.get(descriptor) : descriptor.getValue();
        if (value != null && value.length == 2 && value[0] != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("[Server] Sending ");
            sb.append(z ? "indication" : "notification");
            sb.append(" to ");
            sb.append(bluetoothGattCharacteristic.getUuid());
            log(2, sb.toString());
            log(3, "server.notifyCharacteristicChanged(device, " + bluetoothGattCharacteristic.getUuid() + ", " + z + ")");
            boolean zNotifyCharacteristicChanged = this.serverManager.getServer().notifyCharacteristicChanged(this.bluetoothDevice, bluetoothGattCharacteristic, z);
            if (zNotifyCharacteristicChanged && Build.VERSION.SDK_INT < 21) {
                post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$4z4z8HRfuKD3il8pHjX6UvACPTQ
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$internalSendNotification$8$BleManagerHandler();
                    }
                });
            }
            return zNotifyCharacteristicChanged;
        }
        nextRequest(true);
        return true;
    }

    public /* synthetic */ void lambda$internalSendNotification$8$BleManagerHandler() {
        notifyNotificationSent(this.bluetoothDevice);
        nextRequest(true);
    }

    private static BluetoothGattDescriptor getCccd(BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        if (bluetoothGattCharacteristic == null || (i & bluetoothGattCharacteristic.getProperties()) == 0) {
            return null;
        }
        return bluetoothGattCharacteristic.getDescriptor(BleManager.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
    }

    private boolean internalReadCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || bluetoothGattCharacteristic == null || !this.connected || (bluetoothGattCharacteristic.getProperties() & 2) == 0) {
            return false;
        }
        log(2, "Reading characteristic " + bluetoothGattCharacteristic.getUuid());
        log(3, "gatt.readCharacteristic(" + bluetoothGattCharacteristic.getUuid() + ")");
        return bluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
    }

    private boolean internalWriteCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || bluetoothGattCharacteristic == null || !this.connected || (bluetoothGattCharacteristic.getProperties() & 12) == 0) {
            return false;
        }
        log(2, "Writing characteristic " + bluetoothGattCharacteristic.getUuid() + " (" + ParserUtils.writeTypeToString(bluetoothGattCharacteristic.getWriteType()) + ")");
        StringBuilder sb = new StringBuilder();
        sb.append("gatt.writeCharacteristic(");
        sb.append(bluetoothGattCharacteristic.getUuid());
        sb.append(")");
        log(3, sb.toString());
        return bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
    }

    private boolean internalReadDescriptor(BluetoothGattDescriptor bluetoothGattDescriptor) {
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || bluetoothGattDescriptor == null || !this.connected) {
            return false;
        }
        log(2, "Reading descriptor " + bluetoothGattDescriptor.getUuid());
        log(3, "gatt.readDescriptor(" + bluetoothGattDescriptor.getUuid() + ")");
        return bluetoothGatt.readDescriptor(bluetoothGattDescriptor);
    }

    private boolean internalWriteDescriptor(BluetoothGattDescriptor bluetoothGattDescriptor) {
        if (this.bluetoothGatt == null || bluetoothGattDescriptor == null || !this.connected) {
            return false;
        }
        log(2, "Writing descriptor " + bluetoothGattDescriptor.getUuid());
        log(3, "gatt.writeDescriptor(" + bluetoothGattDescriptor.getUuid() + ")");
        return internalWriteDescriptorWorkaround(bluetoothGattDescriptor);
    }

    private boolean internalWriteDescriptorWorkaround(BluetoothGattDescriptor bluetoothGattDescriptor) {
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || bluetoothGattDescriptor == null || !this.connected) {
            return false;
        }
        BluetoothGattCharacteristic characteristic = bluetoothGattDescriptor.getCharacteristic();
        int writeType = characteristic.getWriteType();
        characteristic.setWriteType(2);
        boolean zWriteDescriptor = bluetoothGatt.writeDescriptor(bluetoothGattDescriptor);
        characteristic.setWriteType(writeType);
        return zWriteDescriptor;
    }

    private boolean internalBeginReliableWrite() {
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || !this.connected) {
            return false;
        }
        if (this.reliableWriteInProgress) {
            return true;
        }
        log(2, "Beginning reliable write...");
        log(3, "gatt.beginReliableWrite()");
        boolean zBeginReliableWrite = bluetoothGatt.beginReliableWrite();
        this.reliableWriteInProgress = zBeginReliableWrite;
        return zBeginReliableWrite;
    }

    private boolean internalExecuteReliableWrite() {
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || !this.connected || !this.reliableWriteInProgress) {
            return false;
        }
        log(2, "Executing reliable write...");
        log(3, "gatt.executeReliableWrite()");
        return bluetoothGatt.executeReliableWrite();
    }

    private boolean internalAbortReliableWrite() {
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || !this.connected || !this.reliableWriteInProgress) {
            return false;
        }
        log(2, "Aborting reliable write...");
        if (Build.VERSION.SDK_INT >= 19) {
            log(3, "gatt.abortReliableWrite()");
            bluetoothGatt.abortReliableWrite();
            return true;
        }
        log(3, "gatt.abortReliableWrite(device)");
        bluetoothGatt.abortReliableWrite(bluetoothGatt.getDevice());
        return true;
    }

    @Deprecated
    private boolean internalReadBatteryLevel() {
        BluetoothGattService service;
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || !this.connected || (service = bluetoothGatt.getService(BleManager.BATTERY_SERVICE)) == null) {
            return false;
        }
        return internalReadCharacteristic(service.getCharacteristic(BleManager.BATTERY_LEVEL_CHARACTERISTIC));
    }

    @Deprecated
    private boolean internalSetBatteryNotifications(boolean z) {
        BluetoothGattService service;
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || !this.connected || (service = bluetoothGatt.getService(BleManager.BATTERY_SERVICE)) == null) {
            return false;
        }
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(BleManager.BATTERY_LEVEL_CHARACTERISTIC);
        if (z) {
            return internalEnableNotifications(characteristic);
        }
        return internalDisableNotifications(characteristic);
    }

    private boolean internalRequestMtu(int i) {
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || !this.connected) {
            return false;
        }
        log(2, "Requesting new MTU...");
        log(3, "gatt.requestMtu(" + i + ")");
        return bluetoothGatt.requestMtu(i);
    }

    private boolean internalRequestConnectionPriority(int i) {
        String str;
        String str2;
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || !this.connected) {
            return false;
        }
        if (i == 1) {
            str = Build.VERSION.SDK_INT >= 23 ? "HIGH (11.25–15ms, 0, 20s)" : "HIGH (7.5–10ms, 0, 20s)";
            str2 = "HIGH";
        } else if (i != 2) {
            str = "BALANCED (30–50ms, 0, 20s)";
            str2 = "BALANCED";
        } else {
            str = "LOW POWER (100–125ms, 2, 20s)";
            str2 = "LOW POWER";
        }
        log(2, "Requesting connection priority: " + str + "...");
        log(3, "gatt.requestConnectionPriority(" + str2 + ")");
        return bluetoothGatt.requestConnectionPriority(i);
    }

    private boolean internalSetPreferredPhy(int i, int i2, int i3) {
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || !this.connected) {
            return false;
        }
        log(2, "Requesting preferred PHYs...");
        log(3, "gatt.setPreferredPhy(" + ParserUtils.phyMaskToString(i) + ", " + ParserUtils.phyMaskToString(i2) + ", coding option = " + ParserUtils.phyCodedOptionToString(i3) + ")");
        bluetoothGatt.setPreferredPhy(i, i2, i3);
        return true;
    }

    private boolean internalReadPhy() {
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || !this.connected) {
            return false;
        }
        log(2, "Reading PHY...");
        log(3, "gatt.readPhy()");
        bluetoothGatt.readPhy();
        return true;
    }

    private boolean internalReadRssi() {
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null || !this.connected) {
            return false;
        }
        log(2, "Reading remote RSSI...");
        log(3, "gatt.readRemoteRssi()");
        return bluetoothGatt.readRemoteRssi();
    }

    ValueChangedCallback getValueChangedCallback(Object obj) {
        ValueChangedCallback valueChangedCallback = this.valueChangedCallbacks.get(obj);
        if (valueChangedCallback == null) {
            valueChangedCallback = new ValueChangedCallback(this);
            if (obj != null) {
                this.valueChangedCallbacks.put(obj, valueChangedCallback);
            }
        }
        return valueChangedCallback.free();
    }

    void removeValueChangedCallback(Object obj) {
        this.valueChangedCallbacks.remove(obj);
    }

    @Deprecated
    DataReceivedCallback getBatteryLevelCallback() {
        return new DataReceivedCallback() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$dLueSSPIWZDfLLIm4yf9ESnYKV4
            @Override // no.nordicsemi.android.ble.callback.DataReceivedCallback
            public final void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
                this.f$0.lambda$getBatteryLevelCallback$10$BleManagerHandler(bluetoothDevice, data);
            }
        };
    }

    public /* synthetic */ void lambda$getBatteryLevelCallback$10$BleManagerHandler(final BluetoothDevice bluetoothDevice, Data data) {
        if (data.size() == 1) {
            final int iIntValue = data.getIntValue(17, 0).intValue();
            log(4, "Battery Level received: " + iIntValue + "%");
            this.batteryValue = iIntValue;
            onBatteryValueReceived(this.bluetoothGatt, iIntValue);
            postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$x4q-5UQYCD641mnkdNQR2jT-d3w
                @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                public final void run(BleManagerCallbacks bleManagerCallbacks) {
                    bleManagerCallbacks.onBatteryValueReceived(bluetoothDevice, iIntValue);
                }
            });
        }
    }

    @Deprecated
    void setBatteryLevelNotificationCallback() {
        if (this.batteryLevelNotificationCallback == null) {
            this.batteryLevelNotificationCallback = new ValueChangedCallback(this).with(new DataReceivedCallback() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$DgeAXrbQnFNm3WWyoxLENNlnSt8
                @Override // no.nordicsemi.android.ble.callback.DataReceivedCallback
                public final void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
                    this.f$0.lambda$setBatteryLevelNotificationCallback$12$BleManagerHandler(bluetoothDevice, data);
                }
            });
        }
    }

    public /* synthetic */ void lambda$setBatteryLevelNotificationCallback$12$BleManagerHandler(final BluetoothDevice bluetoothDevice, Data data) {
        if (data.size() == 1) {
            final int iIntValue = data.getIntValue(17, 0).intValue();
            this.batteryValue = iIntValue;
            onBatteryValueReceived(this.bluetoothGatt, iIntValue);
            postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$bAyCbw0valRb5w3ntpZFJsn_wwE
                @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                public final void run(BleManagerCallbacks bleManagerCallbacks) {
                    bleManagerCallbacks.onBatteryValueReceived(bluetoothDevice, iIntValue);
                }
            });
        }
    }

    private boolean internalRefreshDeviceCache() {
        BluetoothGatt bluetoothGatt = this.bluetoothGatt;
        if (bluetoothGatt == null) {
            return false;
        }
        log(2, "Refreshing device cache...");
        log(3, "gatt.refresh() (hidden)");
        try {
            return ((Boolean) bluetoothGatt.getClass().getMethod("refresh", new Class[0]).invoke(bluetoothGatt, new Object[0])).booleanValue();
        } catch (Exception e) {
            Log.w(TAG, "An exception occurred while refreshing device", e);
            log(5, "gatt.refresh() method not found");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enqueueFirst(Request request) {
        RequestQueue requestQueue = this.requestQueue;
        if (requestQueue == null) {
            (this.initInProgress ? this.initQueue : this.taskQueue).addFirst(request);
        } else {
            requestQueue.addFirst(request);
        }
        request.enqueued = true;
        this.operationInProgress = false;
    }

    @Override // no.nordicsemi.android.ble.RequestHandler
    final void enqueue(Request request) {
        (this.initInProgress ? this.initQueue : this.taskQueue).add(request);
        request.enqueued = true;
        nextRequest(false);
    }

    @Override // no.nordicsemi.android.ble.RequestHandler
    final void cancelQueue() {
        this.taskQueue.clear();
        this.initQueue = null;
        AwaitingRequest<?> awaitingRequest = this.awaitingRequest;
        if (awaitingRequest != null) {
            awaitingRequest.notifyFail(this.bluetoothDevice, -7);
        }
        Request request = this.request;
        if (request != null && this.awaitingRequest != request) {
            request.notifyFail(this.bluetoothDevice, -7);
            this.request = null;
        }
        this.awaitingRequest = null;
        RequestQueue requestQueue = this.requestQueue;
        if (requestQueue != null) {
            requestQueue.notifyFail(this.bluetoothDevice, -7);
            this.requestQueue = null;
        }
        ConnectRequest connectRequest = this.connectRequest;
        if (connectRequest != null) {
            connectRequest.notifyFail(this.bluetoothDevice, -7);
            this.connectRequest = null;
            internalDisconnect();
            return;
        }
        nextRequest(true);
    }

    @Override // no.nordicsemi.android.ble.RequestHandler
    final void onRequestTimeout(TimeoutableRequest timeoutableRequest) {
        this.request = null;
        this.awaitingRequest = null;
        if (timeoutableRequest.type == Request.Type.CONNECT) {
            this.connectRequest = null;
            internalDisconnect();
        } else if (timeoutableRequest.type == Request.Type.DISCONNECT) {
            close();
        } else {
            nextRequest(true);
        }
    }

    @Override // no.nordicsemi.android.ble.CallbackHandler
    public void post(Runnable runnable) {
        this.handler.post(runnable);
    }

    @Override // no.nordicsemi.android.ble.CallbackHandler
    public void postDelayed(Runnable runnable, long j) {
        this.handler.postDelayed(runnable, j);
    }

    @Override // no.nordicsemi.android.ble.CallbackHandler
    public void removeCallbacks(Runnable runnable) {
        this.handler.removeCallbacks(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Deprecated
    public void postCallback(final CallbackRunnable callbackRunnable) {
        final BleManagerCallbacks bleManagerCallbacks = this.manager.callbacks;
        if (bleManagerCallbacks != null) {
            post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$p4akY9gSYDZH13QD6317hrpjD1U
                @Override // java.lang.Runnable
                public final void run() {
                    callbackRunnable.run(bleManagerCallbacks);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postBondingStateChange(final BondingObserverRunnable bondingObserverRunnable) {
        final BondingObserver bondingObserver = this.manager.bondingObserver;
        if (bondingObserver != null) {
            post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$dcwAZUe8fCfUr4hGwBKGsBeHDKk
                @Override // java.lang.Runnable
                public final void run() {
                    bondingObserverRunnable.run(bondingObserver);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postConnectionStateChange(final ConnectionObserverRunnable connectionObserverRunnable) {
        final ConnectionObserver connectionObserver = this.manager.connectionObserver;
        if (connectionObserver != null) {
            post(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$4r-nMh00pvleAsXLi5_Uq8GIr38
                @Override // java.lang.Runnable
                public final void run() {
                    connectionObserverRunnable.run(connectionObserver);
                }
            });
        }
    }

    final int getConnectionState() {
        return this.connectionState;
    }

    final boolean isConnected() {
        return this.connected;
    }

    @Deprecated
    final int getBatteryValue() {
        return this.batteryValue;
    }

    final boolean isReady() {
        return this.ready;
    }

    final boolean isReliableWriteInProgress() {
        return this.reliableWriteInProgress;
    }

    final int getMtu() {
        return this.mtu;
    }

    final void overrideMtu(int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.mtu = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDeviceDisconnected(final BluetoothDevice bluetoothDevice, final int i) {
        boolean z = this.connected;
        this.connected = false;
        this.servicesDiscovered = false;
        this.serviceDiscoveryRequested = false;
        this.deviceNotSupported = false;
        this.initInProgress = false;
        this.connectionState = 0;
        checkCondition();
        if (!z) {
            log(5, "Connection attempt timed out");
            close();
            postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$GMLIUNMfb5GpcWSB_N70isSbWRg
                @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                public final void run(BleManagerCallbacks bleManagerCallbacks) {
                    bleManagerCallbacks.onDeviceDisconnected(bluetoothDevice);
                }
            });
            postConnectionStateChange(new ConnectionObserverRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$dRQ0SI18dkhqQ1Tl3hQ1aCStZLA
                @Override // no.nordicsemi.android.ble.BleManagerHandler.ConnectionObserverRunnable
                public final void run(ConnectionObserver connectionObserver) {
                    connectionObserver.onDeviceFailedToConnect(bluetoothDevice, i);
                }
            });
        } else if (this.userDisconnected) {
            log(4, "Disconnected");
            close();
            postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$qGvninRkzxQ2taaJYZZJi9lxJZs
                @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                public final void run(BleManagerCallbacks bleManagerCallbacks) {
                    bleManagerCallbacks.onDeviceDisconnected(bluetoothDevice);
                }
            });
            postConnectionStateChange(new ConnectionObserverRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$guXJ2j28OUdhOwA3ci3Sy6Rbp5w
                @Override // no.nordicsemi.android.ble.BleManagerHandler.ConnectionObserverRunnable
                public final void run(ConnectionObserver connectionObserver) {
                    connectionObserver.onDeviceDisconnected(bluetoothDevice, i);
                }
            });
            Request request = this.request;
            if (request != null && request.type == Request.Type.DISCONNECT) {
                request.notifySuccess(bluetoothDevice);
            }
        } else {
            log(5, "Connection lost");
            postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$ttiCMYVCTx9BKMxFumt87yJCFV4
                @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                public final void run(BleManagerCallbacks bleManagerCallbacks) {
                    bleManagerCallbacks.onLinkLossOccurred(bluetoothDevice);
                }
            });
            postConnectionStateChange(new ConnectionObserverRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$SfU5_l6WgoTFsf5r8Kj75Cmmvok
                @Override // no.nordicsemi.android.ble.BleManagerHandler.ConnectionObserverRunnable
                public final void run(ConnectionObserver connectionObserver) {
                    connectionObserver.onDeviceDisconnected(bluetoothDevice, 3);
                }
            });
        }
        onDeviceDisconnected();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onError(final BluetoothDevice bluetoothDevice, final String str, final int i) {
        log(6, "Error (0x" + Integer.toHexString(i) + "): " + GattError.parse(i));
        postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$Vn_C1gCfUoWdmjXTk4Fy63j6fl4
            @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
            public final void run(BleManagerCallbacks bleManagerCallbacks) {
                bleManagerCallbacks.onError(bluetoothDevice, str, i);
            }
        });
    }

    /* renamed from: no.nordicsemi.android.ble.BleManagerHandler$3, reason: invalid class name */
    class AnonymousClass3 extends BluetoothGattCallback {
        AnonymousClass3() {
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public final void onConnectionStateChange(final BluetoothGatt bluetoothGatt, final int i, int i2) {
            BleManagerHandler.this.log(3, "[Callback] Connection state changed with status: " + i + " and new state: " + i2 + " (" + ParserUtils.stateToString(i2) + ")");
            int iMapDisconnectStatusToReason = 4;
            if (i != 0 || i2 != 2) {
                if (i2 == 0) {
                    long jElapsedRealtime = SystemClock.elapsedRealtime();
                    boolean z = BleManagerHandler.this.connectionTime > 0;
                    boolean z2 = z && jElapsedRealtime > BleManagerHandler.this.connectionTime + BleManagerHandler.CONNECTION_TIMEOUT_THRESHOLD;
                    if (i != 0) {
                        BleManagerHandler.this.log(5, "Error: (0x" + Integer.toHexString(i) + "): " + GattError.parseConnectionError(i));
                    }
                    if (i == 0 || !z || z2 || BleManagerHandler.this.connectRequest == null || !BleManagerHandler.this.connectRequest.canRetry()) {
                        BleManagerHandler.this.operationInProgress = true;
                        BleManagerHandler.this.taskQueue.clear();
                        BleManagerHandler.this.initQueue = null;
                        BleManagerHandler.this.ready = false;
                        boolean z3 = BleManagerHandler.this.connected;
                        boolean z4 = BleManagerHandler.this.deviceNotSupported;
                        BleManagerHandler bleManagerHandler = BleManagerHandler.this;
                        BluetoothDevice device = bluetoothGatt.getDevice();
                        if (z2) {
                            iMapDisconnectStatusToReason = 10;
                        } else if (!z4) {
                            iMapDisconnectStatusToReason = BleManagerHandler.this.mapDisconnectStatusToReason(i);
                        }
                        bleManagerHandler.notifyDeviceDisconnected(device, iMapDisconnectStatusToReason);
                        int i3 = -1;
                        if (BleManagerHandler.this.request != null && BleManagerHandler.this.request.type != Request.Type.DISCONNECT && BleManagerHandler.this.request.type != Request.Type.REMOVE_BOND) {
                            BleManagerHandler.this.request.notifyFail(bluetoothGatt.getDevice(), i == 0 ? -1 : i);
                            BleManagerHandler.this.request = null;
                        }
                        if (BleManagerHandler.this.awaitingRequest != null) {
                            BleManagerHandler.this.awaitingRequest.notifyFail(bluetoothGatt.getDevice(), -1);
                            BleManagerHandler.this.awaitingRequest = null;
                        }
                        if (BleManagerHandler.this.connectRequest != null) {
                            if (z4) {
                                i3 = -2;
                            } else if (i != 0) {
                                i3 = (i == 133 && z2) ? -5 : i;
                            }
                            BleManagerHandler.this.connectRequest.notifyFail(bluetoothGatt.getDevice(), i3);
                            BleManagerHandler.this.connectRequest = null;
                        }
                        BleManagerHandler.this.operationInProgress = false;
                        if (!z3 || !BleManagerHandler.this.initialConnection) {
                            BleManagerHandler.this.initialConnection = false;
                            BleManagerHandler.this.nextRequest(false);
                        } else {
                            BleManagerHandler.this.internalConnect(bluetoothGatt.getDevice(), null);
                        }
                        if (z3 || i == 0) {
                            return;
                        }
                    } else {
                        int retryDelay = BleManagerHandler.this.connectRequest.getRetryDelay();
                        if (retryDelay > 0) {
                            BleManagerHandler.this.log(3, "wait(" + retryDelay + ")");
                        }
                        BleManagerHandler.this.postDelayed(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$3$TSZCZyN40bh4eGrJ7spPGY0UiTw
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$onConnectionStateChange$3$BleManagerHandler$3(bluetoothGatt);
                            }
                        }, retryDelay);
                        return;
                    }
                } else if (i != 0) {
                    BleManagerHandler.this.log(6, "Error (0x" + Integer.toHexString(i) + "): " + GattError.parseConnectionError(i));
                }
                BleManagerHandler.this.postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$3$Fl0KURS2UGmPOS-U51YWAP8D710
                    @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                    public final void run(BleManagerCallbacks bleManagerCallbacks) {
                        bleManagerCallbacks.onError(bluetoothGatt.getDevice(), BleManagerHandler.ERROR_CONNECTION_STATE_CHANGE, i);
                    }
                });
                return;
            }
            if (BleManagerHandler.this.bluetoothDevice == null) {
                Log.e(BleManagerHandler.TAG, "Device received notification after disconnection.");
                BleManagerHandler.this.log(3, "gatt.close()");
                try {
                    bluetoothGatt.close();
                    return;
                } catch (Throwable unused) {
                    return;
                }
            }
            BleManagerHandler.this.log(4, "Connected to " + bluetoothGatt.getDevice().getAddress());
            BleManagerHandler.this.connected = true;
            BleManagerHandler.this.connectionTime = 0L;
            BleManagerHandler.this.connectionState = 2;
            BleManagerHandler.this.postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$3$w-sWcqS36MfkcSfM_54gIcjFJIo
                @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                public final void run(BleManagerCallbacks bleManagerCallbacks) {
                    bleManagerCallbacks.onDeviceConnected(bluetoothGatt.getDevice());
                }
            });
            BleManagerHandler.this.postConnectionStateChange(new ConnectionObserverRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$3$Sng7HGfrEIzvc-Dnm35TkepW0M0
                @Override // no.nordicsemi.android.ble.BleManagerHandler.ConnectionObserverRunnable
                public final void run(ConnectionObserver connectionObserver) {
                    connectionObserver.onDeviceConnected(bluetoothGatt.getDevice());
                }
            });
            if (BleManagerHandler.this.serviceDiscoveryRequested) {
                return;
            }
            int serviceDiscoveryDelay = BleManagerHandler.this.manager.getServiceDiscoveryDelay(bluetoothGatt.getDevice().getBondState() == 12);
            if (serviceDiscoveryDelay > 0) {
                BleManagerHandler.this.log(3, "wait(" + serviceDiscoveryDelay + ")");
            }
            final int iAccess$2204 = BleManagerHandler.access$2204(BleManagerHandler.this);
            BleManagerHandler.this.postDelayed(new Runnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$3$V_CHLH3akwHf8WmdslGEyIBILA4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onConnectionStateChange$2$BleManagerHandler$3(iAccess$2204, bluetoothGatt);
                }
            }, serviceDiscoveryDelay);
        }

        public /* synthetic */ void lambda$onConnectionStateChange$2$BleManagerHandler$3(int i, BluetoothGatt bluetoothGatt) {
            if (i == BleManagerHandler.this.connectionCount && BleManagerHandler.this.connected && bluetoothGatt.getDevice().getBondState() != 11) {
                BleManagerHandler.this.serviceDiscoveryRequested = true;
                BleManagerHandler.this.log(2, "Discovering services...");
                BleManagerHandler.this.log(3, "gatt.discoverServices()");
                bluetoothGatt.discoverServices();
            }
        }

        public /* synthetic */ void lambda$onConnectionStateChange$3$BleManagerHandler$3(BluetoothGatt bluetoothGatt) {
            BleManagerHandler.this.internalConnect(bluetoothGatt.getDevice(), BleManagerHandler.this.connectRequest);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public final void onServicesDiscovered(final BluetoothGatt bluetoothGatt, int i) {
            BluetoothGattServer server;
            BleManagerHandler.this.serviceDiscoveryRequested = false;
            if (i == 0) {
                BleManagerHandler.this.log(4, "Services discovered");
                BleManagerHandler.this.servicesDiscovered = true;
                if (BleManagerHandler.this.isRequiredServiceSupported(bluetoothGatt)) {
                    BleManagerHandler.this.log(2, "Primary service found");
                    BleManagerHandler.this.deviceNotSupported = false;
                    final boolean zIsOptionalServiceSupported = BleManagerHandler.this.isOptionalServiceSupported(bluetoothGatt);
                    if (zIsOptionalServiceSupported) {
                        BleManagerHandler.this.log(2, "Secondary service found");
                    }
                    BleManagerHandler.this.postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$3$0rxA9HsWuQUlBjUfieyGLC4X2nc
                        @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                        public final void run(BleManagerCallbacks bleManagerCallbacks) {
                            bleManagerCallbacks.onServicesDiscovered(bluetoothGatt.getDevice(), zIsOptionalServiceSupported);
                        }
                    });
                    if (BleManagerHandler.this.serverManager != null && (server = BleManagerHandler.this.serverManager.getServer()) != null) {
                        Iterator<BluetoothGattService> it = server.getServices().iterator();
                        while (it.hasNext()) {
                            for (BluetoothGattCharacteristic bluetoothGattCharacteristic : it.next().getCharacteristics()) {
                                if (!BleManagerHandler.this.serverManager.isShared(bluetoothGattCharacteristic)) {
                                    if (BleManagerHandler.this.characteristicValues == null) {
                                        BleManagerHandler.this.characteristicValues = new HashMap();
                                    }
                                    BleManagerHandler.this.characteristicValues.put(bluetoothGattCharacteristic, bluetoothGattCharacteristic.getValue());
                                }
                                for (BluetoothGattDescriptor bluetoothGattDescriptor : bluetoothGattCharacteristic.getDescriptors()) {
                                    if (!BleManagerHandler.this.serverManager.isShared(bluetoothGattDescriptor)) {
                                        if (BleManagerHandler.this.descriptorValues == null) {
                                            BleManagerHandler.this.descriptorValues = new HashMap();
                                        }
                                        BleManagerHandler.this.descriptorValues.put(bluetoothGattDescriptor, bluetoothGattDescriptor.getValue());
                                    }
                                }
                            }
                        }
                        BleManagerHandler.this.onServerReady(server);
                    }
                    BleManagerHandler.this.initInProgress = true;
                    BleManagerHandler.this.operationInProgress = true;
                    BleManagerHandler bleManagerHandler = BleManagerHandler.this;
                    bleManagerHandler.initQueue = bleManagerHandler.initGatt(bluetoothGatt);
                    boolean z = BleManagerHandler.this.initQueue != null;
                    if (z) {
                        Iterator it2 = BleManagerHandler.this.initQueue.iterator();
                        while (it2.hasNext()) {
                            ((Request) it2.next()).enqueued = true;
                        }
                    }
                    if (BleManagerHandler.this.initQueue == null) {
                        BleManagerHandler.this.initQueue = new LinkedBlockingDeque();
                    }
                    if (Build.VERSION.SDK_INT < 23 || Build.VERSION.SDK_INT == 26 || Build.VERSION.SDK_INT == 27 || Build.VERSION.SDK_INT == 28) {
                        BleManagerHandler.this.enqueueFirst(Request.newEnableServiceChangedIndicationsRequest().setRequestHandler((RequestHandler) BleManagerHandler.this));
                    }
                    if (z) {
                        BleManagerHandler.this.manager.readBatteryLevel();
                        if (BleManagerHandler.this.manager.callbacks != null && BleManagerHandler.this.manager.callbacks.shouldEnableBatteryLevelNotifications(bluetoothGatt.getDevice())) {
                            BleManagerHandler.this.manager.enableBatteryLevelNotifications();
                        }
                    }
                    BleManagerHandler.this.initialize();
                    BleManagerHandler.this.initInProgress = false;
                    BleManagerHandler.this.nextRequest(true);
                    return;
                }
                BleManagerHandler.this.log(5, "Device is not supported");
                BleManagerHandler.this.deviceNotSupported = true;
                BleManagerHandler.this.postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$3$5iOoHcFpBckHBto1RyjKg3D2BX8
                    @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                    public final void run(BleManagerCallbacks bleManagerCallbacks) {
                        bleManagerCallbacks.onDeviceNotSupported(bluetoothGatt.getDevice());
                    }
                });
                BleManagerHandler.this.internalDisconnect();
                return;
            }
            Log.e(BleManagerHandler.TAG, "onServicesDiscovered error " + i);
            BleManagerHandler.this.onError(bluetoothGatt.getDevice(), BleManagerHandler.ERROR_DISCOVERY_SERVICE, i);
            if (BleManagerHandler.this.connectRequest != null) {
                BleManagerHandler.this.connectRequest.notifyFail(bluetoothGatt.getDevice(), -4);
                BleManagerHandler.this.connectRequest = null;
            }
            BleManagerHandler.this.internalDisconnect();
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicRead(final BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, final int i) {
            byte[] value = bluetoothGattCharacteristic.getValue();
            if (i == 0) {
                BleManagerHandler.this.log(4, "Read Response received from " + bluetoothGattCharacteristic.getUuid() + ", value: " + ParserUtils.parse(value));
                BleManagerHandler.this.onCharacteristicRead(bluetoothGatt, bluetoothGattCharacteristic);
                if (BleManagerHandler.this.request instanceof ReadRequest) {
                    ReadRequest readRequest = (ReadRequest) BleManagerHandler.this.request;
                    boolean zMatches = readRequest.matches(value);
                    if (zMatches) {
                        readRequest.notifyValueChanged(bluetoothGatt.getDevice(), value);
                    }
                    if (!zMatches || readRequest.hasMore()) {
                        BleManagerHandler.this.enqueueFirst(readRequest);
                    } else {
                        readRequest.notifySuccess(bluetoothGatt.getDevice());
                    }
                }
            } else {
                if (i == 5 || i == 8 || i == 137) {
                    BleManagerHandler.this.log(5, "Authentication required (" + i + ")");
                    if (bluetoothGatt.getDevice().getBondState() != 10) {
                        Log.w(BleManagerHandler.TAG, BleManagerHandler.ERROR_AUTH_ERROR_WHILE_BONDED);
                        BleManagerHandler.this.postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$3$R5WWSwdguL9SsbgVTYXzZhjDK3Y
                            @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                            public final void run(BleManagerCallbacks bleManagerCallbacks) {
                                bleManagerCallbacks.onError(bluetoothGatt.getDevice(), BleManagerHandler.ERROR_AUTH_ERROR_WHILE_BONDED, i);
                            }
                        });
                        return;
                    }
                    return;
                }
                Log.e(BleManagerHandler.TAG, "onCharacteristicRead error " + i);
                if (BleManagerHandler.this.request instanceof ReadRequest) {
                    BleManagerHandler.this.request.notifyFail(bluetoothGatt.getDevice(), i);
                }
                BleManagerHandler.this.awaitingRequest = null;
                BleManagerHandler.this.onError(bluetoothGatt.getDevice(), BleManagerHandler.ERROR_READ_CHARACTERISTIC, i);
            }
            BleManagerHandler.this.checkCondition();
            BleManagerHandler.this.nextRequest(true);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicWrite(final BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, final int i) {
            byte[] value = bluetoothGattCharacteristic.getValue();
            if (i == 0) {
                BleManagerHandler.this.log(4, "Data written to " + bluetoothGattCharacteristic.getUuid() + ", value: " + ParserUtils.parse(value));
                BleManagerHandler.this.onCharacteristicWrite(bluetoothGatt, bluetoothGattCharacteristic);
                if (BleManagerHandler.this.request instanceof WriteRequest) {
                    WriteRequest writeRequest = (WriteRequest) BleManagerHandler.this.request;
                    if (!writeRequest.notifyPacketSent(bluetoothGatt.getDevice(), value) && (BleManagerHandler.this.requestQueue instanceof ReliableWriteRequest)) {
                        writeRequest.notifyFail(bluetoothGatt.getDevice(), -6);
                        BleManagerHandler.this.requestQueue.cancelQueue();
                    } else if (writeRequest.hasMore()) {
                        BleManagerHandler.this.enqueueFirst(writeRequest);
                    } else {
                        writeRequest.notifySuccess(bluetoothGatt.getDevice());
                    }
                }
            } else {
                if (i == 5 || i == 8 || i == 137) {
                    BleManagerHandler.this.log(5, "Authentication required (" + i + ")");
                    if (bluetoothGatt.getDevice().getBondState() != 10) {
                        Log.w(BleManagerHandler.TAG, BleManagerHandler.ERROR_AUTH_ERROR_WHILE_BONDED);
                        BleManagerHandler.this.postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$3$93cGlXOs0Nu6m8qZHxTph_mwlb8
                            @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                            public final void run(BleManagerCallbacks bleManagerCallbacks) {
                                bleManagerCallbacks.onError(bluetoothGatt.getDevice(), BleManagerHandler.ERROR_AUTH_ERROR_WHILE_BONDED, i);
                            }
                        });
                        return;
                    }
                    return;
                }
                Log.e(BleManagerHandler.TAG, "onCharacteristicWrite error " + i);
                if (BleManagerHandler.this.request instanceof WriteRequest) {
                    BleManagerHandler.this.request.notifyFail(bluetoothGatt.getDevice(), i);
                    if (BleManagerHandler.this.requestQueue instanceof ReliableWriteRequest) {
                        BleManagerHandler.this.requestQueue.cancelQueue();
                    }
                }
                BleManagerHandler.this.awaitingRequest = null;
                BleManagerHandler.this.onError(bluetoothGatt.getDevice(), BleManagerHandler.ERROR_WRITE_CHARACTERISTIC, i);
            }
            BleManagerHandler.this.checkCondition();
            BleManagerHandler.this.nextRequest(true);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public final void onReliableWriteCompleted(BluetoothGatt bluetoothGatt, int i) {
            boolean z = BleManagerHandler.this.request.type == Request.Type.EXECUTE_RELIABLE_WRITE;
            BleManagerHandler.this.reliableWriteInProgress = false;
            if (i != 0) {
                Log.e(BleManagerHandler.TAG, "onReliableWriteCompleted execute " + z + ", error " + i);
                BleManagerHandler.this.request.notifyFail(bluetoothGatt.getDevice(), i);
                BleManagerHandler.this.onError(bluetoothGatt.getDevice(), BleManagerHandler.ERROR_RELIABLE_WRITE, i);
            } else if (z) {
                BleManagerHandler.this.log(4, "Reliable Write executed");
                BleManagerHandler.this.request.notifySuccess(bluetoothGatt.getDevice());
            } else {
                BleManagerHandler.this.log(5, "Reliable Write aborted");
                BleManagerHandler.this.request.notifySuccess(bluetoothGatt.getDevice());
                BleManagerHandler.this.requestQueue.notifyFail(bluetoothGatt.getDevice(), -4);
            }
            BleManagerHandler.this.checkCondition();
            BleManagerHandler.this.nextRequest(true);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onDescriptorRead(final BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, final int i) {
            byte[] value = bluetoothGattDescriptor.getValue();
            if (i == 0) {
                BleManagerHandler.this.log(4, "Read Response received from descr. " + bluetoothGattDescriptor.getUuid() + ", value: " + ParserUtils.parse(value));
                BleManagerHandler.this.onDescriptorRead(bluetoothGatt, bluetoothGattDescriptor);
                if (BleManagerHandler.this.request instanceof ReadRequest) {
                    ReadRequest readRequest = (ReadRequest) BleManagerHandler.this.request;
                    readRequest.notifyValueChanged(bluetoothGatt.getDevice(), value);
                    if (readRequest.hasMore()) {
                        BleManagerHandler.this.enqueueFirst(readRequest);
                    } else {
                        readRequest.notifySuccess(bluetoothGatt.getDevice());
                    }
                }
            } else {
                if (i == 5 || i == 8 || i == 137) {
                    BleManagerHandler.this.log(5, "Authentication required (" + i + ")");
                    if (bluetoothGatt.getDevice().getBondState() != 10) {
                        Log.w(BleManagerHandler.TAG, BleManagerHandler.ERROR_AUTH_ERROR_WHILE_BONDED);
                        BleManagerHandler.this.postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$3$kTg0pekgTQSqNrap5vDDAoSOgiw
                            @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                            public final void run(BleManagerCallbacks bleManagerCallbacks) {
                                bleManagerCallbacks.onError(bluetoothGatt.getDevice(), BleManagerHandler.ERROR_AUTH_ERROR_WHILE_BONDED, i);
                            }
                        });
                        return;
                    }
                    return;
                }
                Log.e(BleManagerHandler.TAG, "onDescriptorRead error " + i);
                if (BleManagerHandler.this.request instanceof ReadRequest) {
                    BleManagerHandler.this.request.notifyFail(bluetoothGatt.getDevice(), i);
                }
                BleManagerHandler.this.awaitingRequest = null;
                BleManagerHandler.this.onError(bluetoothGatt.getDevice(), BleManagerHandler.ERROR_READ_DESCRIPTOR, i);
            }
            BleManagerHandler.this.checkCondition();
            BleManagerHandler.this.nextRequest(true);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onDescriptorWrite(final BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, final int i) {
            byte[] value = bluetoothGattDescriptor.getValue();
            if (i == 0) {
                BleManagerHandler.this.log(4, "Data written to descr. " + bluetoothGattDescriptor.getUuid() + ", value: " + ParserUtils.parse(value));
                if (BleManagerHandler.this.isServiceChangedCCCD(bluetoothGattDescriptor)) {
                    BleManagerHandler.this.log(4, "Service Changed notifications enabled");
                } else if (BleManagerHandler.this.isCCCD(bluetoothGattDescriptor)) {
                    if (value != null && value.length == 2 && value[1] == 0) {
                        byte b = value[0];
                        if (b == 0) {
                            BleManagerHandler.this.log(4, "Notifications and indications disabled");
                        } else if (b == 1) {
                            BleManagerHandler.this.log(4, "Notifications enabled");
                        } else if (b == 2) {
                            BleManagerHandler.this.log(4, "Indications enabled");
                        }
                        BleManagerHandler.this.onDescriptorWrite(bluetoothGatt, bluetoothGattDescriptor);
                    }
                } else {
                    BleManagerHandler.this.onDescriptorWrite(bluetoothGatt, bluetoothGattDescriptor);
                }
                if (BleManagerHandler.this.request instanceof WriteRequest) {
                    WriteRequest writeRequest = (WriteRequest) BleManagerHandler.this.request;
                    if (!writeRequest.notifyPacketSent(bluetoothGatt.getDevice(), value) && (BleManagerHandler.this.requestQueue instanceof ReliableWriteRequest)) {
                        writeRequest.notifyFail(bluetoothGatt.getDevice(), -6);
                        BleManagerHandler.this.requestQueue.cancelQueue();
                    } else if (writeRequest.hasMore()) {
                        BleManagerHandler.this.enqueueFirst(writeRequest);
                    } else {
                        writeRequest.notifySuccess(bluetoothGatt.getDevice());
                    }
                }
            } else {
                if (i == 5 || i == 8 || i == 137) {
                    BleManagerHandler.this.log(5, "Authentication required (" + i + ")");
                    if (bluetoothGatt.getDevice().getBondState() != 10) {
                        Log.w(BleManagerHandler.TAG, BleManagerHandler.ERROR_AUTH_ERROR_WHILE_BONDED);
                        BleManagerHandler.this.postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$3$Uc4bdQCeBkXou1lhXO77rk3sXIY
                            @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                            public final void run(BleManagerCallbacks bleManagerCallbacks) {
                                bleManagerCallbacks.onError(bluetoothGatt.getDevice(), BleManagerHandler.ERROR_AUTH_ERROR_WHILE_BONDED, i);
                            }
                        });
                        return;
                    }
                    return;
                }
                Log.e(BleManagerHandler.TAG, "onDescriptorWrite error " + i);
                if (BleManagerHandler.this.request instanceof WriteRequest) {
                    BleManagerHandler.this.request.notifyFail(bluetoothGatt.getDevice(), i);
                    if (BleManagerHandler.this.requestQueue instanceof ReliableWriteRequest) {
                        BleManagerHandler.this.requestQueue.cancelQueue();
                    }
                }
                BleManagerHandler.this.awaitingRequest = null;
                BleManagerHandler.this.onError(bluetoothGatt.getDevice(), BleManagerHandler.ERROR_WRITE_DESCRIPTOR, i);
            }
            BleManagerHandler.this.checkCondition();
            BleManagerHandler.this.nextRequest(true);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            byte[] value = bluetoothGattCharacteristic.getValue();
            if (BleManagerHandler.this.isServiceChangedCharacteristic(bluetoothGattCharacteristic)) {
                BleManagerHandler.this.operationInProgress = true;
                BleManagerHandler.this.taskQueue.clear();
                BleManagerHandler.this.initQueue = null;
                BleManagerHandler.this.log(4, "Service Changed indication received");
                BleManagerHandler.this.log(2, "Discovering Services...");
                BleManagerHandler.this.log(3, "gatt.discoverServices()");
                bluetoothGatt.discoverServices();
                return;
            }
            BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(BleManager.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
            boolean z = descriptor == null || descriptor.getValue() == null || descriptor.getValue().length != 2 || descriptor.getValue()[0] == 1;
            String str = ParserUtils.parse(value);
            if (z) {
                BleManagerHandler.this.log(4, "Notification received from " + bluetoothGattCharacteristic.getUuid() + ", value: " + str);
                BleManagerHandler.this.onCharacteristicNotified(bluetoothGatt, bluetoothGattCharacteristic);
            } else {
                BleManagerHandler.this.log(4, "Indication received from " + bluetoothGattCharacteristic.getUuid() + ", value: " + str);
                BleManagerHandler.this.onCharacteristicIndicated(bluetoothGatt, bluetoothGattCharacteristic);
            }
            if (BleManagerHandler.this.batteryLevelNotificationCallback != null && BleManagerHandler.this.isBatteryLevelCharacteristic(bluetoothGattCharacteristic)) {
                BleManagerHandler.this.batteryLevelNotificationCallback.notifyValueChanged(bluetoothGatt.getDevice(), value);
            }
            ValueChangedCallback valueChangedCallback = (ValueChangedCallback) BleManagerHandler.this.valueChangedCallbacks.get(bluetoothGattCharacteristic);
            if (valueChangedCallback != null && valueChangedCallback.matches(value)) {
                valueChangedCallback.notifyValueChanged(bluetoothGatt.getDevice(), value);
            }
            if ((BleManagerHandler.this.awaitingRequest instanceof WaitForValueChangedRequest) && BleManagerHandler.this.awaitingRequest.characteristic == bluetoothGattCharacteristic && !BleManagerHandler.this.awaitingRequest.isTriggerPending()) {
                WaitForValueChangedRequest waitForValueChangedRequest = (WaitForValueChangedRequest) BleManagerHandler.this.awaitingRequest;
                if (waitForValueChangedRequest.matches(value)) {
                    waitForValueChangedRequest.notifyValueChanged(bluetoothGatt.getDevice(), value);
                    if (!waitForValueChangedRequest.hasMore()) {
                        waitForValueChangedRequest.notifySuccess(bluetoothGatt.getDevice());
                        BleManagerHandler.this.awaitingRequest = null;
                        if (waitForValueChangedRequest.isTriggerCompleteOrNull()) {
                            BleManagerHandler.this.nextRequest(true);
                        }
                    }
                }
            }
            if (BleManagerHandler.this.checkCondition()) {
                BleManagerHandler.this.nextRequest(true);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public final void onMtuChanged(BluetoothGatt bluetoothGatt, int i, int i2) {
            if (i2 == 0) {
                BleManagerHandler.this.log(4, "MTU changed to: " + i);
                BleManagerHandler.this.mtu = i;
                BleManagerHandler.this.onMtuChanged(bluetoothGatt, i);
                if (BleManagerHandler.this.request instanceof MtuRequest) {
                    ((MtuRequest) BleManagerHandler.this.request).notifyMtuChanged(bluetoothGatt.getDevice(), i);
                    BleManagerHandler.this.request.notifySuccess(bluetoothGatt.getDevice());
                }
            } else {
                Log.e(BleManagerHandler.TAG, "onMtuChanged error: " + i2 + ", mtu: " + i);
                if (BleManagerHandler.this.request instanceof MtuRequest) {
                    BleManagerHandler.this.request.notifyFail(bluetoothGatt.getDevice(), i2);
                    BleManagerHandler.this.awaitingRequest = null;
                }
                BleManagerHandler.this.onError(bluetoothGatt.getDevice(), BleManagerHandler.ERROR_MTU_REQUEST, i2);
            }
            BleManagerHandler.this.checkCondition();
            BleManagerHandler.this.nextRequest(true);
        }

        public final void onConnectionUpdated(final BluetoothGatt bluetoothGatt, int i, int i2, int i3, final int i4) {
            if (i4 == 0) {
                BleManagerHandler.this.log(4, "Connection parameters updated (interval: " + (i * 1.25d) + "ms, latency: " + i2 + ", timeout: " + (i3 * 10) + "ms)");
                BleManagerHandler.this.onConnectionUpdated(bluetoothGatt, i, i2, i3);
                if (BleManagerHandler.this.request instanceof ConnectionPriorityRequest) {
                    ((ConnectionPriorityRequest) BleManagerHandler.this.request).notifyConnectionPriorityChanged(bluetoothGatt.getDevice(), i, i2, i3);
                    BleManagerHandler.this.request.notifySuccess(bluetoothGatt.getDevice());
                }
            } else if (i4 == 59) {
                Log.e(BleManagerHandler.TAG, "onConnectionUpdated received status: Unacceptable connection interval, interval: " + i + ", latency: " + i2 + ", timeout: " + i3);
                BleManagerHandler.this.log(5, "Connection parameters update failed with status: UNACCEPT CONN INTERVAL (0x3b) (interval: " + (((double) i) * 1.25d) + "ms, latency: " + i2 + ", timeout: " + (i3 * 10) + "ms)");
                if (BleManagerHandler.this.request instanceof ConnectionPriorityRequest) {
                    BleManagerHandler.this.request.notifyFail(bluetoothGatt.getDevice(), i4);
                    BleManagerHandler.this.awaitingRequest = null;
                }
            } else {
                Log.e(BleManagerHandler.TAG, "onConnectionUpdated received status: " + i4 + ", interval: " + i + ", latency: " + i2 + ", timeout: " + i3);
                BleManagerHandler.this.log(5, "Connection parameters update failed with status " + i4 + " (interval: " + (((double) i) * 1.25d) + "ms, latency: " + i2 + ", timeout: " + (i3 * 10) + "ms)");
                if (BleManagerHandler.this.request instanceof ConnectionPriorityRequest) {
                    BleManagerHandler.this.request.notifyFail(bluetoothGatt.getDevice(), i4);
                    BleManagerHandler.this.awaitingRequest = null;
                }
                BleManagerHandler.this.postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$3$DglJlty-DP4w47h5sjjGR1R07iE
                    @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                    public final void run(BleManagerCallbacks bleManagerCallbacks) {
                        bleManagerCallbacks.onError(bluetoothGatt.getDevice(), BleManagerHandler.ERROR_CONNECTION_PRIORITY_REQUEST, i4);
                    }
                });
            }
            if (BleManagerHandler.this.connectionPriorityOperationInProgress) {
                BleManagerHandler.this.connectionPriorityOperationInProgress = false;
                BleManagerHandler.this.checkCondition();
                BleManagerHandler.this.nextRequest(true);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public final void onPhyUpdate(final BluetoothGatt bluetoothGatt, int i, int i2, final int i3) {
            if (i3 == 0) {
                BleManagerHandler.this.log(4, "PHY updated (TX: " + ParserUtils.phyToString(i) + ", RX: " + ParserUtils.phyToString(i2) + ")");
                if (BleManagerHandler.this.request instanceof PhyRequest) {
                    ((PhyRequest) BleManagerHandler.this.request).notifyPhyChanged(bluetoothGatt.getDevice(), i, i2);
                    BleManagerHandler.this.request.notifySuccess(bluetoothGatt.getDevice());
                }
            } else {
                BleManagerHandler.this.log(5, "PHY updated failed with status " + i3);
                if (BleManagerHandler.this.request instanceof PhyRequest) {
                    BleManagerHandler.this.request.notifyFail(bluetoothGatt.getDevice(), i3);
                    BleManagerHandler.this.awaitingRequest = null;
                }
                BleManagerHandler.this.postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$3$1BNenqhgoemcS1aMgujRAYqICFM
                    @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                    public final void run(BleManagerCallbacks bleManagerCallbacks) {
                        bleManagerCallbacks.onError(bluetoothGatt.getDevice(), BleManagerHandler.ERROR_PHY_UPDATE, i3);
                    }
                });
            }
            if (BleManagerHandler.this.checkCondition() || (BleManagerHandler.this.request instanceof PhyRequest)) {
                BleManagerHandler.this.nextRequest(true);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public final void onPhyRead(final BluetoothGatt bluetoothGatt, int i, int i2, final int i3) {
            if (i3 == 0) {
                BleManagerHandler.this.log(4, "PHY read (TX: " + ParserUtils.phyToString(i) + ", RX: " + ParserUtils.phyToString(i2) + ")");
                if (BleManagerHandler.this.request instanceof PhyRequest) {
                    ((PhyRequest) BleManagerHandler.this.request).notifyPhyChanged(bluetoothGatt.getDevice(), i, i2);
                    BleManagerHandler.this.request.notifySuccess(bluetoothGatt.getDevice());
                }
            } else {
                BleManagerHandler.this.log(5, "PHY read failed with status " + i3);
                if (BleManagerHandler.this.request instanceof PhyRequest) {
                    BleManagerHandler.this.request.notifyFail(bluetoothGatt.getDevice(), i3);
                }
                BleManagerHandler.this.awaitingRequest = null;
                BleManagerHandler.this.postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$3$LDprO46QhYdTRKr48wNdXgOH97w
                    @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                    public final void run(BleManagerCallbacks bleManagerCallbacks) {
                        bleManagerCallbacks.onError(bluetoothGatt.getDevice(), BleManagerHandler.ERROR_READ_PHY, i3);
                    }
                });
            }
            BleManagerHandler.this.checkCondition();
            BleManagerHandler.this.nextRequest(true);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public final void onReadRemoteRssi(final BluetoothGatt bluetoothGatt, int i, final int i2) {
            if (i2 == 0) {
                BleManagerHandler.this.log(4, "Remote RSSI received: " + i + " dBm");
                if (BleManagerHandler.this.request instanceof ReadRssiRequest) {
                    ((ReadRssiRequest) BleManagerHandler.this.request).notifyRssiRead(bluetoothGatt.getDevice(), i);
                    BleManagerHandler.this.request.notifySuccess(bluetoothGatt.getDevice());
                }
            } else {
                BleManagerHandler.this.log(5, "Reading remote RSSI failed with status " + i2);
                if (BleManagerHandler.this.request instanceof ReadRssiRequest) {
                    BleManagerHandler.this.request.notifyFail(bluetoothGatt.getDevice(), i2);
                }
                BleManagerHandler.this.awaitingRequest = null;
                BleManagerHandler.this.postCallback(new CallbackRunnable() { // from class: no.nordicsemi.android.ble.-$$Lambda$BleManagerHandler$3$HEyDe0L_pr-_GsrfaIausw4UfKE
                    @Override // no.nordicsemi.android.ble.BleManagerHandler.CallbackRunnable
                    public final void run(BleManagerCallbacks bleManagerCallbacks) {
                        bleManagerCallbacks.onError(bluetoothGatt.getDevice(), BleManagerHandler.ERROR_READ_RSSI, i2);
                    }
                });
            }
            BleManagerHandler.this.checkCondition();
            BleManagerHandler.this.nextRequest(true);
        }
    }

    final void onCharacteristicReadRequest(BluetoothGattServer bluetoothGattServer, BluetoothDevice bluetoothDevice, int i, int i2, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        WaitForReadRequest waitForReadRequest;
        log(3, "[Server callback] Read request for characteristic " + bluetoothGattCharacteristic.getUuid() + " (requestId=" + i + ", offset: " + i2 + ")");
        if (i2 == 0) {
            log(4, "[Server] READ request for characteristic " + bluetoothGattCharacteristic.getUuid() + " received");
        }
        Map<BluetoothGattCharacteristic, byte[]> map = this.characteristicValues;
        byte[] value = (map == null || !map.containsKey(bluetoothGattCharacteristic)) ? bluetoothGattCharacteristic.getValue() : this.characteristicValues.get(bluetoothGattCharacteristic);
        AwaitingRequest<?> awaitingRequest = this.awaitingRequest;
        if ((awaitingRequest instanceof WaitForReadRequest) && awaitingRequest.characteristic == bluetoothGattCharacteristic && !this.awaitingRequest.isTriggerPending()) {
            WaitForReadRequest waitForReadRequest2 = (WaitForReadRequest) this.awaitingRequest;
            waitForReadRequest2.setDataIfNull(value);
            value = waitForReadRequest2.getData(this.mtu);
            waitForReadRequest = waitForReadRequest2;
        } else {
            waitForReadRequest = null;
        }
        if (value != null) {
            int length = value.length;
            int i3 = this.mtu;
            if (length > i3 - 1) {
                value = Bytes.copy(value, i2, i3 - 1);
            }
        }
        byte[] bArr = value;
        sendResponse(bluetoothGattServer, bluetoothDevice, 0, i, i2, bArr);
        if (waitForReadRequest != null) {
            waitForReadRequest.notifyPacketRead(bluetoothDevice, bArr);
            if (waitForReadRequest.hasMore()) {
                return;
            }
            if (bArr == null || bArr.length < this.mtu - 1) {
                waitForReadRequest.notifySuccess(bluetoothDevice);
                this.awaitingRequest = null;
                nextRequest(true);
                return;
            }
            return;
        }
        if (checkCondition()) {
            nextRequest(true);
        }
    }

    final void onCharacteristicWriteRequest(BluetoothGattServer bluetoothGattServer, BluetoothDevice bluetoothDevice, int i, BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z, boolean z2, int i2, byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("[Server callback] Write ");
        sb.append(z2 ? "request" : "command");
        sb.append(" to characteristic ");
        sb.append(bluetoothGattCharacteristic.getUuid());
        sb.append(" (requestId=");
        sb.append(i);
        sb.append(", prepareWrite=");
        sb.append(z);
        sb.append(", responseNeeded=");
        sb.append(z2);
        sb.append(", offset: ");
        sb.append(i2);
        sb.append(", value=");
        sb.append(ParserUtils.parseDebug(bArr));
        sb.append(")");
        log(3, sb.toString());
        if (i2 == 0) {
            String str = z2 ? "WRITE REQUEST" : "WRITE COMMAND";
            log(4, "[Server] " + (z ? "Prepare " : "") + str + " for characteristic " + bluetoothGattCharacteristic.getUuid() + " received, value: " + ParserUtils.parse(bArr));
        }
        if (z2) {
            sendResponse(bluetoothGattServer, bluetoothDevice, 0, i, i2, bArr);
        }
        if (z) {
            if (this.preparedValues == null) {
                this.preparedValues = new LinkedList();
            }
            if (i2 == 0) {
                this.preparedValues.offer(new Pair<>(bluetoothGattCharacteristic, bArr));
                return;
            }
            Pair<Object, byte[]> pairPeekLast = this.preparedValues.peekLast();
            if (pairPeekLast != null && bluetoothGattCharacteristic.equals(pairPeekLast.first)) {
                this.preparedValues.pollLast();
                this.preparedValues.offer(new Pair<>(bluetoothGattCharacteristic, Bytes.concat((byte[]) pairPeekLast.second, bArr, i2)));
                return;
            } else {
                this.prepareError = 7;
                return;
            }
        }
        if (assignAndNotify(bluetoothDevice, bluetoothGattCharacteristic, bArr) || checkCondition()) {
            nextRequest(true);
        }
    }

    final void onDescriptorReadRequest(BluetoothGattServer bluetoothGattServer, BluetoothDevice bluetoothDevice, int i, int i2, BluetoothGattDescriptor bluetoothGattDescriptor) {
        WaitForReadRequest waitForReadRequest;
        log(3, "[Server callback] Read request for descriptor " + bluetoothGattDescriptor.getUuid() + " (requestId=" + i + ", offset: " + i2 + ")");
        if (i2 == 0) {
            log(4, "[Server] READ request for descriptor " + bluetoothGattDescriptor.getUuid() + " received");
        }
        Map<BluetoothGattDescriptor, byte[]> map = this.descriptorValues;
        byte[] value = (map == null || !map.containsKey(bluetoothGattDescriptor)) ? bluetoothGattDescriptor.getValue() : this.descriptorValues.get(bluetoothGattDescriptor);
        AwaitingRequest<?> awaitingRequest = this.awaitingRequest;
        if ((awaitingRequest instanceof WaitForReadRequest) && awaitingRequest.descriptor == bluetoothGattDescriptor && !this.awaitingRequest.isTriggerPending()) {
            WaitForReadRequest waitForReadRequest2 = (WaitForReadRequest) this.awaitingRequest;
            waitForReadRequest2.setDataIfNull(value);
            value = waitForReadRequest2.getData(this.mtu);
            waitForReadRequest = waitForReadRequest2;
        } else {
            waitForReadRequest = null;
        }
        if (value != null) {
            int length = value.length;
            int i3 = this.mtu;
            if (length > i3 - 1) {
                value = Bytes.copy(value, i2, i3 - 1);
            }
        }
        byte[] bArr = value;
        sendResponse(bluetoothGattServer, bluetoothDevice, 0, i, i2, bArr);
        if (waitForReadRequest != null) {
            waitForReadRequest.notifyPacketRead(bluetoothDevice, bArr);
            if (waitForReadRequest.hasMore()) {
                return;
            }
            if (bArr == null || bArr.length < this.mtu - 1) {
                waitForReadRequest.notifySuccess(bluetoothDevice);
                this.awaitingRequest = null;
                nextRequest(true);
                return;
            }
            return;
        }
        if (checkCondition()) {
            nextRequest(true);
        }
    }

    final void onDescriptorWriteRequest(BluetoothGattServer bluetoothGattServer, BluetoothDevice bluetoothDevice, int i, BluetoothGattDescriptor bluetoothGattDescriptor, boolean z, boolean z2, int i2, byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("[Server callback] Write ");
        sb.append(z2 ? "request" : "command");
        sb.append(" to descriptor ");
        sb.append(bluetoothGattDescriptor.getUuid());
        sb.append(" (requestId=");
        sb.append(i);
        sb.append(", prepareWrite=");
        sb.append(z);
        sb.append(", responseNeeded=");
        sb.append(z2);
        sb.append(", offset: ");
        sb.append(i2);
        sb.append(", value=");
        sb.append(ParserUtils.parseDebug(bArr));
        sb.append(")");
        log(3, sb.toString());
        if (i2 == 0) {
            String str = z2 ? "WRITE REQUEST" : "WRITE COMMAND";
            log(4, "[Server] " + (z ? "Prepare " : "") + str + " request for descriptor " + bluetoothGattDescriptor.getUuid() + " received, value: " + ParserUtils.parse(bArr));
        }
        if (z2) {
            sendResponse(bluetoothGattServer, bluetoothDevice, 0, i, i2, bArr);
        }
        if (z) {
            if (this.preparedValues == null) {
                this.preparedValues = new LinkedList();
            }
            if (i2 == 0) {
                this.preparedValues.offer(new Pair<>(bluetoothGattDescriptor, bArr));
                return;
            }
            Pair<Object, byte[]> pairPeekLast = this.preparedValues.peekLast();
            if (pairPeekLast != null && bluetoothGattDescriptor.equals(pairPeekLast.first)) {
                this.preparedValues.pollLast();
                this.preparedValues.offer(new Pair<>(bluetoothGattDescriptor, Bytes.concat((byte[]) pairPeekLast.second, bArr, i2)));
                return;
            } else {
                this.prepareError = 7;
                return;
            }
        }
        if (assignAndNotify(bluetoothDevice, bluetoothGattDescriptor, bArr) || checkCondition()) {
            nextRequest(true);
        }
    }

    final void onExecuteWrite(BluetoothGattServer bluetoothGattServer, BluetoothDevice bluetoothDevice, int i, boolean z) {
        boolean z2;
        log(3, "[Server callback] Execute write request (requestId=" + i + ", execute=" + z + ")");
        if (z) {
            Deque<Pair<Object, byte[]>> deque = this.preparedValues;
            log(4, "[Server] Execute write request received");
            this.preparedValues = null;
            int i2 = this.prepareError;
            if (i2 != 0) {
                sendResponse(bluetoothGattServer, bluetoothDevice, i2, i, 0, null);
                this.prepareError = 0;
                return;
            }
            sendResponse(bluetoothGattServer, bluetoothDevice, 0, i, 0, null);
            if (deque == null || deque.isEmpty()) {
                return;
            }
            loop0: while (true) {
                z2 = false;
                for (Pair<Object, byte[]> pair : deque) {
                    if (pair.first instanceof BluetoothGattCharacteristic) {
                        if (!assignAndNotify(bluetoothDevice, (BluetoothGattCharacteristic) pair.first, (byte[]) pair.second) && !z2) {
                            break;
                        }
                        z2 = true;
                    } else if (pair.first instanceof BluetoothGattDescriptor) {
                        if (!assignAndNotify(bluetoothDevice, (BluetoothGattDescriptor) pair.first, (byte[]) pair.second) && !z2) {
                            break;
                        }
                        z2 = true;
                    } else {
                        continue;
                    }
                }
            }
            if (checkCondition() || z2) {
                nextRequest(true);
                return;
            }
            return;
        }
        log(4, "[Server] Cancel write request received");
        this.preparedValues = null;
        sendResponse(bluetoothGattServer, bluetoothDevice, 0, i, 0, null);
    }

    final void onNotificationSent(BluetoothGattServer bluetoothGattServer, BluetoothDevice bluetoothDevice, int i) {
        log(3, "[Server callback] Notification sent (status=" + i + ")");
        if (i == 0) {
            notifyNotificationSent(bluetoothDevice);
        } else {
            Log.e(TAG, "onNotificationSent error " + i);
            Request request = this.request;
            if (request instanceof WriteRequest) {
                request.notifyFail(bluetoothDevice, i);
            }
            this.awaitingRequest = null;
            onError(bluetoothDevice, ERROR_NOTIFY, i);
        }
        checkCondition();
        nextRequest(true);
    }

    final void onMtuChanged(BluetoothGattServer bluetoothGattServer, BluetoothDevice bluetoothDevice, int i) {
        log(4, "[Server] MTU changed to: " + i);
        this.mtu = i;
        checkCondition();
        nextRequest(false);
    }

    private void notifyNotificationSent(BluetoothDevice bluetoothDevice) {
        Request request = this.request;
        if (request instanceof WriteRequest) {
            WriteRequest writeRequest = (WriteRequest) request;
            int i = AnonymousClass4.$SwitchMap$no$nordicsemi$android$ble$Request$Type[writeRequest.type.ordinal()];
            if (i == 1) {
                log(4, "[Server] Notification sent");
            } else if (i == 2) {
                log(4, "[Server] Indication sent");
            }
            writeRequest.notifyPacketSent(bluetoothDevice, writeRequest.characteristic.getValue());
            if (writeRequest.hasMore()) {
                enqueueFirst(writeRequest);
            } else {
                writeRequest.notifySuccess(bluetoothDevice);
            }
        }
    }

    /* renamed from: no.nordicsemi.android.ble.BleManagerHandler$4, reason: invalid class name */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$no$nordicsemi$android$ble$Request$Type;

        static {
            int[] iArr = new int[Request.Type.values().length];
            $SwitchMap$no$nordicsemi$android$ble$Request$Type = iArr;
            try {
                iArr[Request.Type.NOTIFY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.INDICATE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.WAIT_FOR_NOTIFICATION.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.WAIT_FOR_INDICATION.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.WAIT_FOR_READ.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.WAIT_FOR_WRITE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.CONNECT.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.DISCONNECT.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.ENSURE_BOND.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.CREATE_BOND.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.REMOVE_BOND.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.SET.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.READ.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.WRITE.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.READ_DESCRIPTOR.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.WRITE_DESCRIPTOR.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.SET_VALUE.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.SET_DESCRIPTOR_VALUE.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.BEGIN_RELIABLE_WRITE.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.EXECUTE_RELIABLE_WRITE.ordinal()] = 20;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.ABORT_RELIABLE_WRITE.ordinal()] = 21;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.ENABLE_NOTIFICATIONS.ordinal()] = 22;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.ENABLE_INDICATIONS.ordinal()] = 23;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.DISABLE_NOTIFICATIONS.ordinal()] = 24;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.DISABLE_INDICATIONS.ordinal()] = 25;
            } catch (NoSuchFieldError unused25) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.READ_BATTERY_LEVEL.ordinal()] = 26;
            } catch (NoSuchFieldError unused26) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.ENABLE_BATTERY_LEVEL_NOTIFICATIONS.ordinal()] = 27;
            } catch (NoSuchFieldError unused27) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.DISABLE_BATTERY_LEVEL_NOTIFICATIONS.ordinal()] = 28;
            } catch (NoSuchFieldError unused28) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.ENABLE_SERVICE_CHANGED_INDICATIONS.ordinal()] = 29;
            } catch (NoSuchFieldError unused29) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.REQUEST_MTU.ordinal()] = 30;
            } catch (NoSuchFieldError unused30) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.REQUEST_CONNECTION_PRIORITY.ordinal()] = 31;
            } catch (NoSuchFieldError unused31) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.SET_PREFERRED_PHY.ordinal()] = 32;
            } catch (NoSuchFieldError unused32) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.READ_PHY.ordinal()] = 33;
            } catch (NoSuchFieldError unused33) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.READ_RSSI.ordinal()] = 34;
            } catch (NoSuchFieldError unused34) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.REFRESH_CACHE.ordinal()] = 35;
            } catch (NoSuchFieldError unused35) {
            }
            try {
                $SwitchMap$no$nordicsemi$android$ble$Request$Type[Request.Type.SLEEP.ordinal()] = 36;
            } catch (NoSuchFieldError unused36) {
            }
        }
    }

    private boolean assignAndNotify(BluetoothDevice bluetoothDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        Map<BluetoothGattCharacteristic, byte[]> map = this.characteristicValues;
        if (map == null || !map.containsKey(bluetoothGattCharacteristic)) {
            bluetoothGattCharacteristic.setValue(bArr);
        } else {
            this.characteristicValues.put(bluetoothGattCharacteristic, bArr);
        }
        ValueChangedCallback valueChangedCallback = this.valueChangedCallbacks.get(bluetoothGattCharacteristic);
        if (valueChangedCallback != null) {
            valueChangedCallback.notifyValueChanged(bluetoothDevice, bArr);
        }
        AwaitingRequest<?> awaitingRequest = this.awaitingRequest;
        if ((awaitingRequest instanceof WaitForValueChangedRequest) && awaitingRequest.characteristic == bluetoothGattCharacteristic && !this.awaitingRequest.isTriggerPending()) {
            WaitForValueChangedRequest waitForValueChangedRequest = (WaitForValueChangedRequest) this.awaitingRequest;
            if (waitForValueChangedRequest.matches(bArr)) {
                waitForValueChangedRequest.notifyValueChanged(bluetoothDevice, bArr);
                if (!waitForValueChangedRequest.hasMore()) {
                    waitForValueChangedRequest.notifySuccess(bluetoothDevice);
                    this.awaitingRequest = null;
                    return waitForValueChangedRequest.isTriggerCompleteOrNull();
                }
            }
        }
        return false;
    }

    private boolean assignAndNotify(BluetoothDevice bluetoothDevice, BluetoothGattDescriptor bluetoothGattDescriptor, byte[] bArr) {
        Map<BluetoothGattDescriptor, byte[]> map = this.descriptorValues;
        if (map == null || !map.containsKey(bluetoothGattDescriptor)) {
            bluetoothGattDescriptor.setValue(bArr);
        } else {
            this.descriptorValues.put(bluetoothGattDescriptor, bArr);
        }
        ValueChangedCallback valueChangedCallback = this.valueChangedCallbacks.get(bluetoothGattDescriptor);
        if (valueChangedCallback != null) {
            valueChangedCallback.notifyValueChanged(bluetoothDevice, bArr);
        }
        AwaitingRequest<?> awaitingRequest = this.awaitingRequest;
        if ((awaitingRequest instanceof WaitForValueChangedRequest) && awaitingRequest.descriptor == bluetoothGattDescriptor && !this.awaitingRequest.isTriggerPending()) {
            WaitForValueChangedRequest waitForValueChangedRequest = (WaitForValueChangedRequest) this.awaitingRequest;
            if (waitForValueChangedRequest.matches(bArr)) {
                waitForValueChangedRequest.notifyValueChanged(bluetoothDevice, bArr);
                if (!waitForValueChangedRequest.hasMore()) {
                    waitForValueChangedRequest.notifySuccess(bluetoothDevice);
                    this.awaitingRequest = null;
                    return waitForValueChangedRequest.isTriggerCompleteOrNull();
                }
            }
        }
        return false;
    }

    private void sendResponse(BluetoothGattServer bluetoothGattServer, BluetoothDevice bluetoothDevice, int i, int i2, int i3, byte[] bArr) {
        String str;
        if (i == 0) {
            str = "GATT_SUCCESS";
        } else if (i == 6) {
            str = "GATT_REQUEST_NOT_SUPPORTED";
        } else {
            if (i != 7) {
                throw new InvalidParameterException();
            }
            str = "GATT_INVALID_OFFSET";
        }
        log(3, "server.sendResponse(" + str + ", offset=" + i3 + ", value=" + ParserUtils.parseDebug(bArr) + ")");
        bluetoothGattServer.sendResponse(bluetoothDevice, i2, i, i3, bArr);
        log(2, "[Server] Response sent");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkCondition() {
        AwaitingRequest<?> awaitingRequest = this.awaitingRequest;
        if (!(awaitingRequest instanceof ConditionalWaitRequest)) {
            return false;
        }
        ConditionalWaitRequest conditionalWaitRequest = (ConditionalWaitRequest) awaitingRequest;
        if (!conditionalWaitRequest.isFulfilled()) {
            return false;
        }
        conditionalWaitRequest.notifySuccess(this.bluetoothDevice);
        this.awaitingRequest = null;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:188:0x033e A[Catch: all -> 0x03a2, TryCatch #0 {, blocks: (B:5:0x0005, B:7:0x0009, B:11:0x0010, B:12:0x0012, B:14:0x0016, B:17:0x001c, B:19:0x001f, B:21:0x0023, B:23:0x002b, B:27:0x0040, B:29:0x0044, B:33:0x0050, B:35:0x0054, B:37:0x005f, B:38:0x006f, B:40:0x0073, B:41:0x0080, B:43:0x0089, B:46:0x0092, B:48:0x009c, B:61:0x00c0, B:64:0x00c6, B:66:0x00ca, B:71:0x00d8, B:73:0x00dc, B:75:0x00e5, B:78:0x00f0, B:80:0x00f8, B:82:0x0103, B:84:0x0109, B:87:0x0119, B:91:0x012d, B:201:0x0374, B:208:0x038a, B:204:0x037c, B:93:0x0132, B:94:0x015f, B:96:0x0165, B:97:0x016f, B:99:0x0175, B:100:0x0181, B:102:0x0187, B:103:0x018d, B:105:0x0191, B:108:0x019c, B:110:0x01a2, B:111:0x01b4, B:113:0x01b8, B:116:0x01c3, B:120:0x01cc, B:122:0x01d2, B:124:0x01dc, B:125:0x01e6, B:126:0x01ea, B:128:0x01f4, B:130:0x01f8, B:131:0x0202, B:133:0x0206, B:136:0x0213, B:137:0x0219, B:138:0x021f, B:139:0x0225, B:140:0x022b, B:141:0x0233, B:142:0x023b, B:143:0x0243, B:144:0x024b, B:145:0x0251, B:146:0x0257, B:148:0x025d, B:151:0x0267, B:153:0x026d, B:155:0x0271, B:157:0x027b, B:159:0x0294, B:158:0x0289, B:160:0x029b, B:162:0x02a1, B:164:0x02a5, B:166:0x02af, B:168:0x02c8, B:167:0x02bd, B:170:0x02d1, B:172:0x02d8, B:173:0x02e1, B:174:0x02e7, B:175:0x02ef, B:177:0x02f6, B:178:0x0306, B:179:0x030b, B:180:0x0312, B:183:0x031b, B:184:0x0320, B:185:0x0325, B:186:0x032a, B:187:0x032f, B:188:0x033e, B:190:0x0345, B:192:0x0352, B:194:0x035a, B:195:0x0363, B:199:0x036e, B:86:0x0116, B:211:0x0396, B:24:0x0036), top: B:220:0x0005, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0345 A[Catch: all -> 0x03a2, TryCatch #0 {, blocks: (B:5:0x0005, B:7:0x0009, B:11:0x0010, B:12:0x0012, B:14:0x0016, B:17:0x001c, B:19:0x001f, B:21:0x0023, B:23:0x002b, B:27:0x0040, B:29:0x0044, B:33:0x0050, B:35:0x0054, B:37:0x005f, B:38:0x006f, B:40:0x0073, B:41:0x0080, B:43:0x0089, B:46:0x0092, B:48:0x009c, B:61:0x00c0, B:64:0x00c6, B:66:0x00ca, B:71:0x00d8, B:73:0x00dc, B:75:0x00e5, B:78:0x00f0, B:80:0x00f8, B:82:0x0103, B:84:0x0109, B:87:0x0119, B:91:0x012d, B:201:0x0374, B:208:0x038a, B:204:0x037c, B:93:0x0132, B:94:0x015f, B:96:0x0165, B:97:0x016f, B:99:0x0175, B:100:0x0181, B:102:0x0187, B:103:0x018d, B:105:0x0191, B:108:0x019c, B:110:0x01a2, B:111:0x01b4, B:113:0x01b8, B:116:0x01c3, B:120:0x01cc, B:122:0x01d2, B:124:0x01dc, B:125:0x01e6, B:126:0x01ea, B:128:0x01f4, B:130:0x01f8, B:131:0x0202, B:133:0x0206, B:136:0x0213, B:137:0x0219, B:138:0x021f, B:139:0x0225, B:140:0x022b, B:141:0x0233, B:142:0x023b, B:143:0x0243, B:144:0x024b, B:145:0x0251, B:146:0x0257, B:148:0x025d, B:151:0x0267, B:153:0x026d, B:155:0x0271, B:157:0x027b, B:159:0x0294, B:158:0x0289, B:160:0x029b, B:162:0x02a1, B:164:0x02a5, B:166:0x02af, B:168:0x02c8, B:167:0x02bd, B:170:0x02d1, B:172:0x02d8, B:173:0x02e1, B:174:0x02e7, B:175:0x02ef, B:177:0x02f6, B:178:0x0306, B:179:0x030b, B:180:0x0312, B:183:0x031b, B:184:0x0320, B:185:0x0325, B:186:0x032a, B:187:0x032f, B:188:0x033e, B:190:0x0345, B:192:0x0352, B:194:0x035a, B:195:0x0363, B:199:0x036e, B:86:0x0116, B:211:0x0396, B:24:0x0036), top: B:220:0x0005, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:197:0x036b  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x036d  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x0374 A[Catch: all -> 0x03a2, TryCatch #0 {, blocks: (B:5:0x0005, B:7:0x0009, B:11:0x0010, B:12:0x0012, B:14:0x0016, B:17:0x001c, B:19:0x001f, B:21:0x0023, B:23:0x002b, B:27:0x0040, B:29:0x0044, B:33:0x0050, B:35:0x0054, B:37:0x005f, B:38:0x006f, B:40:0x0073, B:41:0x0080, B:43:0x0089, B:46:0x0092, B:48:0x009c, B:61:0x00c0, B:64:0x00c6, B:66:0x00ca, B:71:0x00d8, B:73:0x00dc, B:75:0x00e5, B:78:0x00f0, B:80:0x00f8, B:82:0x0103, B:84:0x0109, B:87:0x0119, B:91:0x012d, B:201:0x0374, B:208:0x038a, B:204:0x037c, B:93:0x0132, B:94:0x015f, B:96:0x0165, B:97:0x016f, B:99:0x0175, B:100:0x0181, B:102:0x0187, B:103:0x018d, B:105:0x0191, B:108:0x019c, B:110:0x01a2, B:111:0x01b4, B:113:0x01b8, B:116:0x01c3, B:120:0x01cc, B:122:0x01d2, B:124:0x01dc, B:125:0x01e6, B:126:0x01ea, B:128:0x01f4, B:130:0x01f8, B:131:0x0202, B:133:0x0206, B:136:0x0213, B:137:0x0219, B:138:0x021f, B:139:0x0225, B:140:0x022b, B:141:0x0233, B:142:0x023b, B:143:0x0243, B:144:0x024b, B:145:0x0251, B:146:0x0257, B:148:0x025d, B:151:0x0267, B:153:0x026d, B:155:0x0271, B:157:0x027b, B:159:0x0294, B:158:0x0289, B:160:0x029b, B:162:0x02a1, B:164:0x02a5, B:166:0x02af, B:168:0x02c8, B:167:0x02bd, B:170:0x02d1, B:172:0x02d8, B:173:0x02e1, B:174:0x02e7, B:175:0x02ef, B:177:0x02f6, B:178:0x0306, B:179:0x030b, B:180:0x0312, B:183:0x031b, B:184:0x0320, B:185:0x0325, B:186:0x032a, B:187:0x032f, B:188:0x033e, B:190:0x0345, B:192:0x0352, B:194:0x035a, B:195:0x0363, B:199:0x036e, B:86:0x0116, B:211:0x0396, B:24:0x0036), top: B:220:0x0005, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0023 A[Catch: Exception -> 0x004d, all -> 0x03a2, TryCatch #0 {, blocks: (B:5:0x0005, B:7:0x0009, B:11:0x0010, B:12:0x0012, B:14:0x0016, B:17:0x001c, B:19:0x001f, B:21:0x0023, B:23:0x002b, B:27:0x0040, B:29:0x0044, B:33:0x0050, B:35:0x0054, B:37:0x005f, B:38:0x006f, B:40:0x0073, B:41:0x0080, B:43:0x0089, B:46:0x0092, B:48:0x009c, B:61:0x00c0, B:64:0x00c6, B:66:0x00ca, B:71:0x00d8, B:73:0x00dc, B:75:0x00e5, B:78:0x00f0, B:80:0x00f8, B:82:0x0103, B:84:0x0109, B:87:0x0119, B:91:0x012d, B:201:0x0374, B:208:0x038a, B:204:0x037c, B:93:0x0132, B:94:0x015f, B:96:0x0165, B:97:0x016f, B:99:0x0175, B:100:0x0181, B:102:0x0187, B:103:0x018d, B:105:0x0191, B:108:0x019c, B:110:0x01a2, B:111:0x01b4, B:113:0x01b8, B:116:0x01c3, B:120:0x01cc, B:122:0x01d2, B:124:0x01dc, B:125:0x01e6, B:126:0x01ea, B:128:0x01f4, B:130:0x01f8, B:131:0x0202, B:133:0x0206, B:136:0x0213, B:137:0x0219, B:138:0x021f, B:139:0x0225, B:140:0x022b, B:141:0x0233, B:142:0x023b, B:143:0x0243, B:144:0x024b, B:145:0x0251, B:146:0x0257, B:148:0x025d, B:151:0x0267, B:153:0x026d, B:155:0x0271, B:157:0x027b, B:159:0x0294, B:158:0x0289, B:160:0x029b, B:162:0x02a1, B:164:0x02a5, B:166:0x02af, B:168:0x02c8, B:167:0x02bd, B:170:0x02d1, B:172:0x02d8, B:173:0x02e1, B:174:0x02e7, B:175:0x02ef, B:177:0x02f6, B:178:0x0306, B:179:0x030b, B:180:0x0312, B:183:0x031b, B:184:0x0320, B:185:0x0325, B:186:0x032a, B:187:0x032f, B:188:0x033e, B:190:0x0345, B:192:0x0352, B:194:0x035a, B:195:0x0363, B:199:0x036e, B:86:0x0116, B:211:0x0396, B:24:0x0036), top: B:220:0x0005, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x003d  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0040 A[Catch: Exception -> 0x004d, all -> 0x03a2, TryCatch #0 {, blocks: (B:5:0x0005, B:7:0x0009, B:11:0x0010, B:12:0x0012, B:14:0x0016, B:17:0x001c, B:19:0x001f, B:21:0x0023, B:23:0x002b, B:27:0x0040, B:29:0x0044, B:33:0x0050, B:35:0x0054, B:37:0x005f, B:38:0x006f, B:40:0x0073, B:41:0x0080, B:43:0x0089, B:46:0x0092, B:48:0x009c, B:61:0x00c0, B:64:0x00c6, B:66:0x00ca, B:71:0x00d8, B:73:0x00dc, B:75:0x00e5, B:78:0x00f0, B:80:0x00f8, B:82:0x0103, B:84:0x0109, B:87:0x0119, B:91:0x012d, B:201:0x0374, B:208:0x038a, B:204:0x037c, B:93:0x0132, B:94:0x015f, B:96:0x0165, B:97:0x016f, B:99:0x0175, B:100:0x0181, B:102:0x0187, B:103:0x018d, B:105:0x0191, B:108:0x019c, B:110:0x01a2, B:111:0x01b4, B:113:0x01b8, B:116:0x01c3, B:120:0x01cc, B:122:0x01d2, B:124:0x01dc, B:125:0x01e6, B:126:0x01ea, B:128:0x01f4, B:130:0x01f8, B:131:0x0202, B:133:0x0206, B:136:0x0213, B:137:0x0219, B:138:0x021f, B:139:0x0225, B:140:0x022b, B:141:0x0233, B:142:0x023b, B:143:0x0243, B:144:0x024b, B:145:0x0251, B:146:0x0257, B:148:0x025d, B:151:0x0267, B:153:0x026d, B:155:0x0271, B:157:0x027b, B:159:0x0294, B:158:0x0289, B:160:0x029b, B:162:0x02a1, B:164:0x02a5, B:166:0x02af, B:168:0x02c8, B:167:0x02bd, B:170:0x02d1, B:172:0x02d8, B:173:0x02e1, B:174:0x02e7, B:175:0x02ef, B:177:0x02f6, B:178:0x0306, B:179:0x030b, B:180:0x0312, B:183:0x031b, B:184:0x0320, B:185:0x0325, B:186:0x032a, B:187:0x032f, B:188:0x033e, B:190:0x0345, B:192:0x0352, B:194:0x035a, B:195:0x0363, B:199:0x036e, B:86:0x0116, B:211:0x0396, B:24:0x0036), top: B:220:0x0005, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0050 A[Catch: all -> 0x03a2, TRY_ENTER, TryCatch #0 {, blocks: (B:5:0x0005, B:7:0x0009, B:11:0x0010, B:12:0x0012, B:14:0x0016, B:17:0x001c, B:19:0x001f, B:21:0x0023, B:23:0x002b, B:27:0x0040, B:29:0x0044, B:33:0x0050, B:35:0x0054, B:37:0x005f, B:38:0x006f, B:40:0x0073, B:41:0x0080, B:43:0x0089, B:46:0x0092, B:48:0x009c, B:61:0x00c0, B:64:0x00c6, B:66:0x00ca, B:71:0x00d8, B:73:0x00dc, B:75:0x00e5, B:78:0x00f0, B:80:0x00f8, B:82:0x0103, B:84:0x0109, B:87:0x0119, B:91:0x012d, B:201:0x0374, B:208:0x038a, B:204:0x037c, B:93:0x0132, B:94:0x015f, B:96:0x0165, B:97:0x016f, B:99:0x0175, B:100:0x0181, B:102:0x0187, B:103:0x018d, B:105:0x0191, B:108:0x019c, B:110:0x01a2, B:111:0x01b4, B:113:0x01b8, B:116:0x01c3, B:120:0x01cc, B:122:0x01d2, B:124:0x01dc, B:125:0x01e6, B:126:0x01ea, B:128:0x01f4, B:130:0x01f8, B:131:0x0202, B:133:0x0206, B:136:0x0213, B:137:0x0219, B:138:0x021f, B:139:0x0225, B:140:0x022b, B:141:0x0233, B:142:0x023b, B:143:0x0243, B:144:0x024b, B:145:0x0251, B:146:0x0257, B:148:0x025d, B:151:0x0267, B:153:0x026d, B:155:0x0271, B:157:0x027b, B:159:0x0294, B:158:0x0289, B:160:0x029b, B:162:0x02a1, B:164:0x02a5, B:166:0x02af, B:168:0x02c8, B:167:0x02bd, B:170:0x02d1, B:172:0x02d8, B:173:0x02e1, B:174:0x02e7, B:175:0x02ef, B:177:0x02f6, B:178:0x0306, B:179:0x030b, B:180:0x0312, B:183:0x031b, B:184:0x0320, B:185:0x0325, B:186:0x032a, B:187:0x032f, B:188:0x033e, B:190:0x0345, B:192:0x0352, B:194:0x035a, B:195:0x0363, B:199:0x036e, B:86:0x0116, B:211:0x0396, B:24:0x0036), top: B:220:0x0005, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x009c A[Catch: all -> 0x03a2, TryCatch #0 {, blocks: (B:5:0x0005, B:7:0x0009, B:11:0x0010, B:12:0x0012, B:14:0x0016, B:17:0x001c, B:19:0x001f, B:21:0x0023, B:23:0x002b, B:27:0x0040, B:29:0x0044, B:33:0x0050, B:35:0x0054, B:37:0x005f, B:38:0x006f, B:40:0x0073, B:41:0x0080, B:43:0x0089, B:46:0x0092, B:48:0x009c, B:61:0x00c0, B:64:0x00c6, B:66:0x00ca, B:71:0x00d8, B:73:0x00dc, B:75:0x00e5, B:78:0x00f0, B:80:0x00f8, B:82:0x0103, B:84:0x0109, B:87:0x0119, B:91:0x012d, B:201:0x0374, B:208:0x038a, B:204:0x037c, B:93:0x0132, B:94:0x015f, B:96:0x0165, B:97:0x016f, B:99:0x0175, B:100:0x0181, B:102:0x0187, B:103:0x018d, B:105:0x0191, B:108:0x019c, B:110:0x01a2, B:111:0x01b4, B:113:0x01b8, B:116:0x01c3, B:120:0x01cc, B:122:0x01d2, B:124:0x01dc, B:125:0x01e6, B:126:0x01ea, B:128:0x01f4, B:130:0x01f8, B:131:0x0202, B:133:0x0206, B:136:0x0213, B:137:0x0219, B:138:0x021f, B:139:0x0225, B:140:0x022b, B:141:0x0233, B:142:0x023b, B:143:0x0243, B:144:0x024b, B:145:0x0251, B:146:0x0257, B:148:0x025d, B:151:0x0267, B:153:0x026d, B:155:0x0271, B:157:0x027b, B:159:0x0294, B:158:0x0289, B:160:0x029b, B:162:0x02a1, B:164:0x02a5, B:166:0x02af, B:168:0x02c8, B:167:0x02bd, B:170:0x02d1, B:172:0x02d8, B:173:0x02e1, B:174:0x02e7, B:175:0x02ef, B:177:0x02f6, B:178:0x0306, B:179:0x030b, B:180:0x0312, B:183:0x031b, B:184:0x0320, B:185:0x0325, B:186:0x032a, B:187:0x032f, B:188:0x033e, B:190:0x0345, B:192:0x0352, B:194:0x035a, B:195:0x0363, B:199:0x036e, B:86:0x0116, B:211:0x0396, B:24:0x0036), top: B:220:0x0005, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0109 A[Catch: all -> 0x03a2, TryCatch #0 {, blocks: (B:5:0x0005, B:7:0x0009, B:11:0x0010, B:12:0x0012, B:14:0x0016, B:17:0x001c, B:19:0x001f, B:21:0x0023, B:23:0x002b, B:27:0x0040, B:29:0x0044, B:33:0x0050, B:35:0x0054, B:37:0x005f, B:38:0x006f, B:40:0x0073, B:41:0x0080, B:43:0x0089, B:46:0x0092, B:48:0x009c, B:61:0x00c0, B:64:0x00c6, B:66:0x00ca, B:71:0x00d8, B:73:0x00dc, B:75:0x00e5, B:78:0x00f0, B:80:0x00f8, B:82:0x0103, B:84:0x0109, B:87:0x0119, B:91:0x012d, B:201:0x0374, B:208:0x038a, B:204:0x037c, B:93:0x0132, B:94:0x015f, B:96:0x0165, B:97:0x016f, B:99:0x0175, B:100:0x0181, B:102:0x0187, B:103:0x018d, B:105:0x0191, B:108:0x019c, B:110:0x01a2, B:111:0x01b4, B:113:0x01b8, B:116:0x01c3, B:120:0x01cc, B:122:0x01d2, B:124:0x01dc, B:125:0x01e6, B:126:0x01ea, B:128:0x01f4, B:130:0x01f8, B:131:0x0202, B:133:0x0206, B:136:0x0213, B:137:0x0219, B:138:0x021f, B:139:0x0225, B:140:0x022b, B:141:0x0233, B:142:0x023b, B:143:0x0243, B:144:0x024b, B:145:0x0251, B:146:0x0257, B:148:0x025d, B:151:0x0267, B:153:0x026d, B:155:0x0271, B:157:0x027b, B:159:0x0294, B:158:0x0289, B:160:0x029b, B:162:0x02a1, B:164:0x02a5, B:166:0x02af, B:168:0x02c8, B:167:0x02bd, B:170:0x02d1, B:172:0x02d8, B:173:0x02e1, B:174:0x02e7, B:175:0x02ef, B:177:0x02f6, B:178:0x0306, B:179:0x030b, B:180:0x0312, B:183:0x031b, B:184:0x0320, B:185:0x0325, B:186:0x032a, B:187:0x032f, B:188:0x033e, B:190:0x0345, B:192:0x0352, B:194:0x035a, B:195:0x0363, B:199:0x036e, B:86:0x0116, B:211:0x0396, B:24:0x0036), top: B:220:0x0005, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0125 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void nextRequest(boolean r12) {
        /*
            Method dump skipped, instructions count: 998
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: no.nordicsemi.android.ble.BleManagerHandler.nextRequest(boolean):void");
    }

    public /* synthetic */ void lambda$nextRequest$25$BleManagerHandler(ConnectionPriorityRequest connectionPriorityRequest, BluetoothDevice bluetoothDevice) {
        if (connectionPriorityRequest.notifySuccess(bluetoothDevice)) {
            this.connectionPriorityOperationInProgress = false;
            nextRequest(true);
        }
    }

    public /* synthetic */ void lambda$nextRequest$26$BleManagerHandler(Request request, BluetoothDevice bluetoothDevice) {
        if (this.request == request) {
            request.notifyFail(bluetoothDevice, -5);
            nextRequest(true);
        }
    }

    public /* synthetic */ void lambda$nextRequest$27$BleManagerHandler(Request request, BluetoothDevice bluetoothDevice) {
        log(4, "Cache refreshed");
        request.notifySuccess(bluetoothDevice);
        this.request = null;
        AwaitingRequest<?> awaitingRequest = this.awaitingRequest;
        if (awaitingRequest != null) {
            awaitingRequest.notifyFail(bluetoothDevice, -3);
            this.awaitingRequest = null;
        }
        this.taskQueue.clear();
        this.initQueue = null;
        if (this.connected) {
            onDeviceDisconnected();
            log(2, "Discovering Services...");
            log(3, "gatt.discoverServices()");
            this.bluetoothGatt.discoverServices();
        }
    }

    public /* synthetic */ void lambda$nextRequest$28$BleManagerHandler(SleepRequest sleepRequest, BluetoothDevice bluetoothDevice) {
        sleepRequest.notifySuccess(bluetoothDevice);
        nextRequest(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isServiceChangedCCCD(BluetoothGattDescriptor bluetoothGattDescriptor) {
        return bluetoothGattDescriptor != null && BleManager.SERVICE_CHANGED_CHARACTERISTIC.equals(bluetoothGattDescriptor.getCharacteristic().getUuid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isServiceChangedCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return bluetoothGattCharacteristic != null && BleManager.SERVICE_CHANGED_CHARACTERISTIC.equals(bluetoothGattCharacteristic.getUuid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Deprecated
    public boolean isBatteryLevelCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return bluetoothGattCharacteristic != null && BleManager.BATTERY_LEVEL_CHARACTERISTIC.equals(bluetoothGattCharacteristic.getUuid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCCCD(BluetoothGattDescriptor bluetoothGattDescriptor) {
        return bluetoothGattDescriptor != null && BleManager.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID.equals(bluetoothGattDescriptor.getUuid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(int i, String str) {
        this.manager.log(i, str);
    }
}
