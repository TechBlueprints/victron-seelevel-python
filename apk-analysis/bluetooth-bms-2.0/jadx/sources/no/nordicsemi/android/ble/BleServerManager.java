package no.nordicsemi.android.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.UUID;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.observer.ServerObserver;
import no.nordicsemi.android.ble.utils.ILogger;

/* loaded from: classes.dex */
public abstract class BleServerManager implements ILogger {
    private final Context context;
    private BluetoothGattServer server;
    private ServerObserver serverObserver;
    private Queue<BluetoothGattService> serverServices;
    private List<BluetoothGattCharacteristic> sharedCharacteristics;
    private List<BluetoothGattDescriptor> sharedDescriptors;
    private static final UUID CHARACTERISTIC_EXTENDED_PROPERTIES_DESCRIPTOR_UUID = UUID.fromString("00002900-0000-1000-8000-00805f9b34fb");
    private static final UUID CLIENT_USER_DESCRIPTION_DESCRIPTOR_UUID = UUID.fromString("00002901-0000-1000-8000-00805f9b34fb");
    private static final UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private final List<BleManager> managers = new ArrayList();
    private final BluetoothGattServerCallback gattServerCallback = new BluetoothGattServerCallback() { // from class: no.nordicsemi.android.ble.BleServerManager.1
        @Override // android.bluetooth.BluetoothGattServerCallback
        public void onServiceAdded(int i, BluetoothGattService bluetoothGattService) {
            if (i == 0) {
                try {
                    BleServerManager.this.server.addService((BluetoothGattService) BleServerManager.this.serverServices.remove());
                    return;
                } catch (Exception unused) {
                    BleServerManager.this.log(4, "[Server] All services added successfully");
                    if (BleServerManager.this.serverObserver != null) {
                        BleServerManager.this.serverObserver.onServerReady();
                    }
                    BleServerManager.this.serverServices = null;
                    return;
                }
            }
            BleServerManager.this.log(6, "[Server] Adding service failed with error " + i);
        }

        @Override // android.bluetooth.BluetoothGattServerCallback
        public void onConnectionStateChange(BluetoothDevice bluetoothDevice, int i, int i2) {
            if (i != 0 || i2 != 2) {
                if (i == 0) {
                    BleServerManager.this.log(4, "[Server] " + bluetoothDevice.getAddress() + " is disconnected");
                } else {
                    BleServerManager.this.log(5, "[Server] " + bluetoothDevice.getAddress() + " has disconnected connected with status: " + i);
                }
                if (BleServerManager.this.serverObserver != null) {
                    BleServerManager.this.serverObserver.onDeviceDisconnectedFromServer(bluetoothDevice);
                    return;
                }
                return;
            }
            BleServerManager.this.log(4, "[Server] " + bluetoothDevice.getAddress() + " is now connected");
            if (BleServerManager.this.serverObserver != null) {
                BleServerManager.this.serverObserver.onDeviceConnectedToServer(bluetoothDevice);
            }
        }

        @Override // android.bluetooth.BluetoothGattServerCallback
        public void onCharacteristicReadRequest(BluetoothDevice bluetoothDevice, int i, int i2, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            BleManagerHandler requestHandler = BleServerManager.this.getRequestHandler(bluetoothDevice);
            if (requestHandler != null) {
                requestHandler.onCharacteristicReadRequest(BleServerManager.this.server, bluetoothDevice, i, i2, bluetoothGattCharacteristic);
            }
        }

        @Override // android.bluetooth.BluetoothGattServerCallback
        public void onCharacteristicWriteRequest(BluetoothDevice bluetoothDevice, int i, BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z, boolean z2, int i2, byte[] bArr) {
            BleManagerHandler requestHandler = BleServerManager.this.getRequestHandler(bluetoothDevice);
            if (requestHandler != null) {
                requestHandler.onCharacteristicWriteRequest(BleServerManager.this.server, bluetoothDevice, i, bluetoothGattCharacteristic, z, z2, i2, bArr);
            }
        }

        @Override // android.bluetooth.BluetoothGattServerCallback
        public void onDescriptorReadRequest(BluetoothDevice bluetoothDevice, int i, int i2, BluetoothGattDescriptor bluetoothGattDescriptor) {
            BleManagerHandler requestHandler = BleServerManager.this.getRequestHandler(bluetoothDevice);
            if (requestHandler != null) {
                requestHandler.onDescriptorReadRequest(BleServerManager.this.server, bluetoothDevice, i, i2, bluetoothGattDescriptor);
            }
        }

        @Override // android.bluetooth.BluetoothGattServerCallback
        public void onDescriptorWriteRequest(BluetoothDevice bluetoothDevice, int i, BluetoothGattDescriptor bluetoothGattDescriptor, boolean z, boolean z2, int i2, byte[] bArr) {
            BleManagerHandler requestHandler = BleServerManager.this.getRequestHandler(bluetoothDevice);
            if (requestHandler != null) {
                requestHandler.onDescriptorWriteRequest(BleServerManager.this.server, bluetoothDevice, i, bluetoothGattDescriptor, z, z2, i2, bArr);
            }
        }

        @Override // android.bluetooth.BluetoothGattServerCallback
        public void onExecuteWrite(BluetoothDevice bluetoothDevice, int i, boolean z) {
            BleManagerHandler requestHandler = BleServerManager.this.getRequestHandler(bluetoothDevice);
            if (requestHandler != null) {
                requestHandler.onExecuteWrite(BleServerManager.this.server, bluetoothDevice, i, z);
            }
        }

        @Override // android.bluetooth.BluetoothGattServerCallback
        public void onNotificationSent(BluetoothDevice bluetoothDevice, int i) {
            BleManagerHandler requestHandler = BleServerManager.this.getRequestHandler(bluetoothDevice);
            if (requestHandler != null) {
                requestHandler.onNotificationSent(BleServerManager.this.server, bluetoothDevice, i);
            }
        }

        @Override // android.bluetooth.BluetoothGattServerCallback
        public void onMtuChanged(BluetoothDevice bluetoothDevice, int i) {
            BleManagerHandler requestHandler = BleServerManager.this.getRequestHandler(bluetoothDevice);
            if (requestHandler != null) {
                requestHandler.onMtuChanged(BleServerManager.this.server, bluetoothDevice, i);
            }
        }
    };

    protected abstract List<BluetoothGattService> initializeServer();

    @Override // no.nordicsemi.android.ble.utils.ILogger
    public void log(int i, String str) {
    }

    public BleServerManager(Context context) {
        this.context = context;
    }

    public final boolean open() {
        if (this.server != null) {
            return true;
        }
        this.serverServices = new LinkedList(initializeServer());
        BluetoothManager bluetoothManager = (BluetoothManager) this.context.getSystemService("bluetooth");
        if (bluetoothManager != null) {
            this.server = bluetoothManager.openGattServer(this.context, this.gattServerCallback);
        }
        if (this.server != null) {
            log(4, "[Server] Server started successfully");
            try {
                this.server.addService(this.serverServices.remove());
            } catch (NoSuchElementException unused) {
                ServerObserver serverObserver = this.serverObserver;
                if (serverObserver != null) {
                    serverObserver.onServerReady();
                }
            } catch (Exception unused2) {
                close();
                return false;
            }
            return true;
        }
        log(5, "GATT server initialization failed");
        this.serverServices = null;
        return false;
    }

    public final void close() {
        BluetoothGattServer bluetoothGattServer = this.server;
        if (bluetoothGattServer != null) {
            bluetoothGattServer.close();
            this.server = null;
        }
        this.serverServices = null;
        for (BleManager bleManager : this.managers) {
            bleManager.closeServer();
            bleManager.close();
        }
        this.managers.clear();
    }

    public final void setServerObserver(ServerObserver serverObserver) {
        this.serverObserver = serverObserver;
    }

    final BluetoothGattServer getServer() {
        return this.server;
    }

    final void addManager(BleManager bleManager) {
        if (this.managers.contains(bleManager)) {
            return;
        }
        this.managers.add(bleManager);
    }

    final void removeManager(BleManager bleManager) {
        this.managers.remove(bleManager);
    }

    final boolean isShared(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        List<BluetoothGattCharacteristic> list = this.sharedCharacteristics;
        return list != null && list.contains(bluetoothGattCharacteristic);
    }

    final boolean isShared(BluetoothGattDescriptor bluetoothGattDescriptor) {
        List<BluetoothGattDescriptor> list = this.sharedDescriptors;
        return list != null && list.contains(bluetoothGattDescriptor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BleManagerHandler getRequestHandler(BluetoothDevice bluetoothDevice) {
        for (BleManager bleManager : this.managers) {
            if (bluetoothDevice.equals(bleManager.getBluetoothDevice())) {
                return bleManager.requestHandler;
            }
        }
        return null;
    }

    @Override // no.nordicsemi.android.ble.utils.ILogger
    public void log(int i, int i2, Object... objArr) {
        log(i, this.context.getString(i2, objArr));
    }

    protected final BluetoothGattService service(UUID uuid, BluetoothGattCharacteristic... bluetoothGattCharacteristicArr) {
        BluetoothGattService bluetoothGattService = new BluetoothGattService(uuid, 0);
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattCharacteristicArr) {
            bluetoothGattService.addCharacteristic(bluetoothGattCharacteristic);
        }
        return bluetoothGattService;
    }

    protected final BluetoothGattCharacteristic characteristic(UUID uuid, int i, int i2, byte[] bArr, BluetoothGattDescriptor... bluetoothGattDescriptorArr) {
        int i3 = i;
        BluetoothGattDescriptor bluetoothGattDescriptor = null;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        for (BluetoothGattDescriptor bluetoothGattDescriptor2 : bluetoothGattDescriptorArr) {
            if (CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID.equals(bluetoothGattDescriptor2.getUuid())) {
                z2 = true;
            } else if (CLIENT_USER_DESCRIPTION_DESCRIPTOR_UUID.equals(bluetoothGattDescriptor2.getUuid()) && (bluetoothGattDescriptor2.getPermissions() & 112) != 0) {
                z = true;
            } else if (CHARACTERISTIC_EXTENDED_PROPERTIES_DESCRIPTOR_UUID.equals(bluetoothGattDescriptor2.getUuid())) {
                bluetoothGattDescriptor = bluetoothGattDescriptor2;
                z3 = true;
            }
        }
        if (z) {
            if (bluetoothGattDescriptor == null) {
                bluetoothGattDescriptor = new BluetoothGattDescriptor(CHARACTERISTIC_EXTENDED_PROPERTIES_DESCRIPTOR_UUID, 1);
                bluetoothGattDescriptor.setValue(new byte[]{2, 0});
            } else if (bluetoothGattDescriptor.getValue() == null || bluetoothGattDescriptor.getValue().length != 2) {
                bluetoothGattDescriptor.setValue(new byte[]{2, 0});
            } else {
                byte[] value = bluetoothGattDescriptor.getValue();
                value[0] = (byte) (value[0] | 2);
            }
        }
        boolean z4 = (i3 & 48) != 0;
        boolean z5 = (bluetoothGattDescriptor == null || bluetoothGattDescriptor.getValue() == null || bluetoothGattDescriptor.getValue().length != 2 || (bluetoothGattDescriptor.getValue()[0] & 1) == 0) ? false : true;
        if (z || z5) {
            i3 |= 128;
        }
        if ((i3 & 128) != 0 && bluetoothGattDescriptor == null) {
            bluetoothGattDescriptor = new BluetoothGattDescriptor(CHARACTERISTIC_EXTENDED_PROPERTIES_DESCRIPTOR_UUID, 1);
            bluetoothGattDescriptor.setValue(new byte[]{0, 0});
        }
        BluetoothGattCharacteristic bluetoothGattCharacteristic = new BluetoothGattCharacteristic(uuid, i3, i2);
        if (z4 && !z2) {
            bluetoothGattCharacteristic.addDescriptor(cccd());
        }
        for (BluetoothGattDescriptor bluetoothGattDescriptor3 : bluetoothGattDescriptorArr) {
            bluetoothGattCharacteristic.addDescriptor(bluetoothGattDescriptor3);
        }
        if (bluetoothGattDescriptor != null && !z3) {
            bluetoothGattCharacteristic.addDescriptor(bluetoothGattDescriptor);
        }
        bluetoothGattCharacteristic.setValue(bArr);
        return bluetoothGattCharacteristic;
    }

    protected final BluetoothGattCharacteristic characteristic(UUID uuid, int i, int i2, Data data, BluetoothGattDescriptor... bluetoothGattDescriptorArr) {
        return characteristic(uuid, i, i2, data != null ? data.getValue() : null, bluetoothGattDescriptorArr);
    }

    protected final BluetoothGattCharacteristic characteristic(UUID uuid, int i, int i2, BluetoothGattDescriptor... bluetoothGattDescriptorArr) {
        return characteristic(uuid, i, i2, (byte[]) null, bluetoothGattDescriptorArr);
    }

    protected final BluetoothGattCharacteristic sharedCharacteristic(UUID uuid, int i, int i2, byte[] bArr, BluetoothGattDescriptor... bluetoothGattDescriptorArr) {
        BluetoothGattCharacteristic bluetoothGattCharacteristicCharacteristic = characteristic(uuid, i, i2, bArr, bluetoothGattDescriptorArr);
        if (this.sharedCharacteristics == null) {
            this.sharedCharacteristics = new ArrayList();
        }
        this.sharedCharacteristics.add(bluetoothGattCharacteristicCharacteristic);
        return bluetoothGattCharacteristicCharacteristic;
    }

    protected final BluetoothGattCharacteristic sharedCharacteristic(UUID uuid, int i, int i2, Data data, BluetoothGattDescriptor... bluetoothGattDescriptorArr) {
        return sharedCharacteristic(uuid, i, i2, data != null ? data.getValue() : null, bluetoothGattDescriptorArr);
    }

    protected final BluetoothGattCharacteristic sharedCharacteristic(UUID uuid, int i, int i2, BluetoothGattDescriptor... bluetoothGattDescriptorArr) {
        return sharedCharacteristic(uuid, i, i2, (byte[]) null, bluetoothGattDescriptorArr);
    }

    protected final BluetoothGattDescriptor descriptor(UUID uuid, int i, byte[] bArr) {
        BluetoothGattDescriptor bluetoothGattDescriptor = new BluetoothGattDescriptor(uuid, i);
        bluetoothGattDescriptor.setValue(bArr);
        return bluetoothGattDescriptor;
    }

    protected final BluetoothGattDescriptor descriptor(UUID uuid, int i, Data data) {
        return descriptor(uuid, i, data != null ? data.getValue() : null);
    }

    protected final BluetoothGattDescriptor sharedDescriptor(UUID uuid, int i, byte[] bArr) {
        BluetoothGattDescriptor bluetoothGattDescriptorDescriptor = descriptor(uuid, i, bArr);
        if (this.sharedDescriptors == null) {
            this.sharedDescriptors = new ArrayList();
        }
        this.sharedDescriptors.add(bluetoothGattDescriptorDescriptor);
        return bluetoothGattDescriptorDescriptor;
    }

    protected final BluetoothGattDescriptor sharedDescriptor(UUID uuid, int i, Data data) {
        return sharedDescriptor(uuid, i, data != null ? data.getValue() : null);
    }

    protected final BluetoothGattDescriptor cccd() {
        return descriptor(CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID, 17, new byte[]{0, 0});
    }

    protected final BluetoothGattDescriptor sharedCccd() {
        return sharedDescriptor(CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID, 17, new byte[]{0, 0});
    }

    protected final BluetoothGattDescriptor reliableWrite() {
        return sharedDescriptor(CHARACTERISTIC_EXTENDED_PROPERTIES_DESCRIPTOR_UUID, 1, new byte[]{1, 0});
    }

    protected final BluetoothGattDescriptor description(String str, boolean z) {
        BluetoothGattDescriptor bluetoothGattDescriptorDescriptor = descriptor(CLIENT_USER_DESCRIPTION_DESCRIPTOR_UUID, (z ? 16 : 0) | 1, str != null ? str.getBytes() : null);
        if (!z) {
            if (this.sharedDescriptors == null) {
                this.sharedDescriptors = new ArrayList();
            }
            this.sharedDescriptors.add(bluetoothGattDescriptorDescriptor);
        }
        return bluetoothGattDescriptorDescriptor;
    }
}
