package com.jkcq.homebike;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public class BMSConfig {
    public static int BIKE_CONN_CONNECTING = 2;
    public static int BIKE_CONN_DISCONN = 0;
    public static int BIKE_CONN_DISCONNECTING = 3;
    public static int BIKE_CONN_IS_SCAN = 4;
    public static int BIKE_CONN_IS_SCAN_TIMEOUT = 5;
    public static int BIKE_CONN_SUCCESS = 1;
    public static int BikeConnState = 0;
    public static BluetoothDevice device = null;
    public static boolean isBikeScanPage = false;
}
