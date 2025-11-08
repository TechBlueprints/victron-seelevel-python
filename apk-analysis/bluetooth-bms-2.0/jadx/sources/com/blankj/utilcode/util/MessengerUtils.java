package com.blankj.utilcode.util;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class MessengerUtils {
    private static final String KEY_STRING = "MESSENGER_UTILS";
    private static final int WHAT_SEND = 2;
    private static final int WHAT_SUBSCRIBE = 0;
    private static final int WHAT_UNSUBSCRIBE = 1;
    private static Client sLocalClient;
    private static ConcurrentHashMap<String, MessageCallback> subscribers = new ConcurrentHashMap<>();
    private static Map<String, Client> sClientMap = new HashMap();

    public interface MessageCallback {
        void messageCall(Bundle bundle);
    }

    public static void register() {
        if (UtilsBridge.isMainProcess()) {
            if (UtilsBridge.isServiceRunning(ServerService.class.getName())) {
                Log.i("MessengerUtils", "Server service is running.");
                return;
            } else {
                Utils.getApp().startService(new Intent(Utils.getApp(), (Class<?>) ServerService.class));
                return;
            }
        }
        if (sLocalClient == null) {
            Client client = new Client(null);
            if (client.bind()) {
                sLocalClient = client;
                return;
            } else {
                Log.e("MessengerUtils", "Bind service failed.");
                return;
            }
        }
        Log.i("MessengerUtils", "The client have been bind.");
    }

    public static void unregister() throws RemoteException {
        if (UtilsBridge.isMainProcess()) {
            if (!UtilsBridge.isServiceRunning(ServerService.class.getName())) {
                Log.i("MessengerUtils", "Server service isn't running.");
                return;
            } else {
                Utils.getApp().stopService(new Intent(Utils.getApp(), (Class<?>) ServerService.class));
            }
        }
        Client client = sLocalClient;
        if (client != null) {
            client.unbind();
        }
    }

    public static void register(String str) {
        if (sClientMap.containsKey(str)) {
            Log.i("MessengerUtils", "register: client registered: " + str);
            return;
        }
        Client client = new Client(str);
        if (client.bind()) {
            sClientMap.put(str, client);
            return;
        }
        Log.e("MessengerUtils", "register: client bind failed: " + str);
    }

    public static void unregister(String str) throws RemoteException {
        if (sClientMap.containsKey(str)) {
            Client client = sClientMap.get(str);
            sClientMap.remove(str);
            client.unbind();
        } else {
            Log.i("MessengerUtils", "unregister: client didn't register: " + str);
        }
    }

    public static void subscribe(String str, MessageCallback messageCallback) {
        if (str == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (messageCallback == null) {
            throw new NullPointerException("Argument 'callback' of type MessageCallback (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        subscribers.put(str, messageCallback);
    }

    public static void unsubscribe(String str) {
        if (str == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        subscribers.remove(str);
    }

    public static void post(String str, Bundle bundle) {
        if (str == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (bundle == null) {
            throw new NullPointerException("Argument 'data' of type Bundle (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        bundle.putString(KEY_STRING, str);
        Client client = sLocalClient;
        if (client != null) {
            client.sendMsg2Server(bundle);
        } else {
            Intent intent = new Intent(Utils.getApp(), (Class<?>) ServerService.class);
            intent.putExtras(bundle);
            Utils.getApp().startService(intent);
        }
        Iterator<Client> it = sClientMap.values().iterator();
        while (it.hasNext()) {
            it.next().sendMsg2Server(bundle);
        }
    }

    static class Client {
        String mPkgName;
        Messenger mServer;
        LinkedList<Bundle> mCached = new LinkedList<>();
        Handler mReceiveServeMsgHandler = new Handler() { // from class: com.blankj.utilcode.util.MessengerUtils.Client.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                String string;
                MessageCallback messageCallback;
                Bundle data = message.getData();
                if (data == null || (string = data.getString(MessengerUtils.KEY_STRING)) == null || (messageCallback = (MessageCallback) MessengerUtils.subscribers.get(string)) == null) {
                    return;
                }
                messageCallback.messageCall(data);
            }
        };
        Messenger mClient = new Messenger(this.mReceiveServeMsgHandler);
        ServiceConnection mConn = new ServiceConnection() { // from class: com.blankj.utilcode.util.MessengerUtils.Client.2
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) throws RemoteException {
                Log.d("MessengerUtils", "client service connected " + componentName);
                Client.this.mServer = new Messenger(iBinder);
                Message messageObtain = Message.obtain(Client.this.mReceiveServeMsgHandler, 0, UtilsBridge.getCurrentProcessName().hashCode(), 0);
                messageObtain.replyTo = Client.this.mClient;
                try {
                    Client.this.mServer.send(messageObtain);
                } catch (RemoteException e) {
                    Log.e("MessengerUtils", "onServiceConnected: ", e);
                }
                Client.this.sendCachedMsg2Server();
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                Log.w("MessengerUtils", "client service disconnected:" + componentName);
                Client.this.mServer = null;
                if (Client.this.bind()) {
                    return;
                }
                Log.e("MessengerUtils", "client service rebind failed: " + componentName);
            }
        };

        Client(String str) {
            this.mPkgName = str;
        }

        boolean bind() {
            if (TextUtils.isEmpty(this.mPkgName)) {
                return Utils.getApp().bindService(new Intent(Utils.getApp(), (Class<?>) ServerService.class), this.mConn, 1);
            }
            if (UtilsBridge.isAppInstalled(this.mPkgName)) {
                if (UtilsBridge.isAppRunning(this.mPkgName)) {
                    Intent intent = new Intent(this.mPkgName + ".messenger");
                    intent.setPackage(this.mPkgName);
                    return Utils.getApp().bindService(intent, this.mConn, 1);
                }
                Log.e("MessengerUtils", "bind: the app is not running -> " + this.mPkgName);
                return false;
            }
            Log.e("MessengerUtils", "bind: the app is not installed -> " + this.mPkgName);
            return false;
        }

        void unbind() throws RemoteException {
            Message messageObtain = Message.obtain(this.mReceiveServeMsgHandler, 1, UtilsBridge.getCurrentProcessName().hashCode(), 0);
            messageObtain.replyTo = this.mClient;
            try {
                this.mServer.send(messageObtain);
            } catch (RemoteException e) {
                Log.e("MessengerUtils", "unbind: ", e);
            }
            try {
                Utils.getApp().unbindService(this.mConn);
            } catch (Exception unused) {
            }
        }

        void sendMsg2Server(Bundle bundle) {
            if (this.mServer == null) {
                this.mCached.addFirst(bundle);
                Log.i("MessengerUtils", "save the bundle " + bundle);
                return;
            }
            sendCachedMsg2Server();
            if (send2Server(bundle)) {
                return;
            }
            this.mCached.addFirst(bundle);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sendCachedMsg2Server() {
            if (this.mCached.isEmpty()) {
                return;
            }
            for (int size = this.mCached.size() - 1; size >= 0; size--) {
                if (send2Server(this.mCached.get(size))) {
                    this.mCached.remove(size);
                }
            }
        }

        private boolean send2Server(Bundle bundle) throws RemoteException {
            Message messageObtain = Message.obtain(this.mReceiveServeMsgHandler, 2);
            messageObtain.setData(bundle);
            messageObtain.replyTo = this.mClient;
            try {
                this.mServer.send(messageObtain);
                return true;
            } catch (RemoteException e) {
                Log.e("MessengerUtils", "send2Server: ", e);
                return false;
            }
        }
    }

    public static class ServerService extends Service {
        private final ConcurrentHashMap<Integer, Messenger> mClientMap = new ConcurrentHashMap<>();
        private final Handler mReceiveClientMsgHandler = new Handler() { // from class: com.blankj.utilcode.util.MessengerUtils.ServerService.1
            @Override // android.os.Handler
            public void handleMessage(Message message) throws RemoteException {
                int i = message.what;
                if (i == 0) {
                    ServerService.this.mClientMap.put(Integer.valueOf(message.arg1), message.replyTo);
                    return;
                }
                if (i == 1) {
                    ServerService.this.mClientMap.remove(Integer.valueOf(message.arg1));
                } else if (i == 2) {
                    ServerService.this.sendMsg2Client(message);
                    ServerService.this.consumeServerProcessCallback(message);
                } else {
                    super.handleMessage(message);
                }
            }
        };
        private final Messenger messenger = new Messenger(this.mReceiveClientMsgHandler);

        @Override // android.app.Service
        public IBinder onBind(Intent intent) {
            return this.messenger.getBinder();
        }

        @Override // android.app.Service
        public int onStartCommand(Intent intent, int i, int i2) throws RemoteException {
            Bundle extras;
            if (intent != null && (extras = intent.getExtras()) != null) {
                Message messageObtain = Message.obtain(this.mReceiveClientMsgHandler, 2);
                messageObtain.replyTo = this.messenger;
                messageObtain.setData(extras);
                sendMsg2Client(messageObtain);
                consumeServerProcessCallback(messageObtain);
            }
            return 2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sendMsg2Client(Message message) throws RemoteException {
            for (Messenger messenger : this.mClientMap.values()) {
                if (messenger != null) {
                    try {
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void consumeServerProcessCallback(Message message) {
            String string;
            MessageCallback messageCallback;
            Bundle data = message.getData();
            if (data == null || (string = data.getString(MessengerUtils.KEY_STRING)) == null || (messageCallback = (MessageCallback) MessengerUtils.subscribers.get(string)) == null) {
                return;
            }
            messageCallback.messageCall(data);
        }
    }
}
