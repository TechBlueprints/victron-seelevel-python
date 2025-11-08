package com.jkcq.homebike;

import android.util.Log;
import androidx.exifinterface.media.ExifInterface;
import com.jkcq.base.app.BaseApp;
import com.jkcq.homebike.observable.BMSRealDataObservable;
import java.util.ArrayList;
import java.util.Arrays;
import no.nordicsemi.android.ble.error.GattError;

/* loaded from: classes.dex */
public class BMSParseUtil {
    private static final int BMS_FOOTER = 126;
    private static final byte BMS_HEADER = 58;
    private static final int BMS_rspDataLen = 140;
    private static final String TAG = BMSParseUtil.class.getSimpleName();
    private static final ArrayList<Byte> bms_packet = new ArrayList<>();

    public static void main(String[] strArr) throws Exception {
        System.out.println(parsePacket(":008231008C000000000000000CCF0CD20CD80CCF0000000000000000000000000000000000000000000000000000000041282828F0000000000001000056035203E803E8F7~".getBytes(), ":008231008C000000000000000CCF0CD20CD80CCF0000000000000000000000000000000000000000000000000000000041282828F0000000000001000056035203E803E8F7~"));
    }

    public static void parseData(byte[] bArr) {
        int length = bArr.length;
        for (int i = 0; i < length; i++) {
            if (bArr[i] == 58 && (bms_packet.size() == 0 || bms_packet.size() == 140)) {
                bms_packet.clear();
            }
            if (bms_packet.size() < 140) {
                bms_packet.add(Byte.valueOf(bArr[i]));
            }
            if (bms_packet.size() == 140 && bms_packet.get(GattError.GATT_INVALID_CFG).byteValue() == 126) {
                byte[] bArr2 = new byte[140];
                for (int i2 = 0; i2 < 140; i2++) {
                    bArr2[i2] = bms_packet.get(i2).byteValue();
                }
                BMSDataBean packet = parsePacket(bArr2, new String(bArr2));
                BMSRealDataObservable.getInstance().setRealData(packet);
                Log.e(TAG, "parseData " + packet.toString());
            }
        }
    }

    public static BMSDataBean parsePacket(byte[] bArr, String str) {
        BMSDataBean bMSDataBean = new BMSDataBean();
        try {
            String strSubstring = str.substring(25, 89);
            Log.e(TAG, "parsePacket strVoltage:" + strSubstring + ",len:" + strSubstring.length());
            ArrayList<Integer> arrayList = new ArrayList<>();
            int[] iArrHex2IntArray = hex2IntArray(strSubstring);
            Log.e(TAG, "parsePacket voltageArr:" + Arrays.toString(iArrHex2IntArray));
            int i = 0;
            for (int i2 = 0; i2 < 32; i2 += 2) {
                int i3 = (iArrHex2IntArray[i2] * 256) + iArrHex2IntArray[i2 + 1];
                Log.e(TAG, "parsePacket value:" + i3);
                i += i3;
                arrayList.add(Integer.valueOf(i3));
            }
            Log.e(TAG, "parsePacket voltage:" + i);
            bMSDataBean.setVoltage(i);
            bMSDataBean.setvArray(arrayList);
            int iHex2Int = hex2Int(str.substring(97, 99)) + (-40);
            if (iHex2Int > 120) {
                iHex2Int = 120;
            }
            bMSDataBean.setTem(iHex2Int);
            String upperCase = str.substring(105, 109).toUpperCase();
            BaseApp.INSTANCE.getSApplicaton();
            if (upperCase.startsWith(ExifInterface.GPS_MEASUREMENT_IN_PROGRESS)) {
                bMSDataBean.setStatus(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.charging));
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.charging));
                bMSDataBean.setAlarm(false);
            } else if (upperCase.startsWith("5")) {
                bMSDataBean.setStatus(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.discharge));
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.discharge));
                bMSDataBean.setAlarm(false);
            } else if (upperCase.startsWith("F")) {
                bMSDataBean.setStatus(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.standby));
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.standby));
                bMSDataBean.setAlarm(false);
            } else {
                bMSDataBean.setStatus(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.standby));
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.standby));
                bMSDataBean.setAlarm(false);
            }
            Log.e(TAG, "parsePacket strState:" + upperCase);
            if (upperCase.equals("5800")) {
                bMSDataBean.setAlarm(true);
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.low_teperature_Discharging));
            } else if (upperCase.equals("5400")) {
                bMSDataBean.setAlarm(true);
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.high_teperature_Discharging));
            } else if (upperCase.equals("A200")) {
                bMSDataBean.setAlarm(true);
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.low_teperature_Charging));
            } else if (upperCase.equals("A100")) {
                bMSDataBean.setAlarm(true);
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.highteperature_Charging));
            } else if (upperCase.equals("5080")) {
                bMSDataBean.setAlarm(true);
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.Over_Discharging));
            } else if (upperCase.equals("5020")) {
                bMSDataBean.setAlarm(true);
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.short_circuit));
            } else if (upperCase.equals("A010")) {
                bMSDataBean.setAlarm(true);
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.Over_Charging));
            } else if (upperCase.equals("5008")) {
                bMSDataBean.setAlarm(true);
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.Over_Protection));
            } else if (upperCase.equals("A004")) {
                bMSDataBean.setAlarm(true);
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.Over_Voltage_Protection));
            } else if (upperCase.equals("A006")) {
                bMSDataBean.setAlarm(true);
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.heating));
            } else if (upperCase.equals("A005")) {
                bMSDataBean.setAlarm(true);
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.balancing));
            } else if (upperCase.equals("5300")) {
                bMSDataBean.setAlarm(true);
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.over_temperature_of_fet));
            } else if (upperCase.equals("CA00")) {
                bMSDataBean.setAlarm(true);
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.low_temperture_of_charge_and_dischange));
            } else if (upperCase.equals("C500")) {
                bMSDataBean.setAlarm(true);
                bMSDataBean.setWrokStateDetail(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.high_temperture_of_charge_and_dischange));
            }
            int[] iArrHex2IntArray2 = hex2IntArray(str.substring(89, 97).toUpperCase());
            int i4 = ((iArrHex2IntArray2[0] * 256) + iArrHex2IntArray2[1]) * 10;
            int iAbs = Math.abs(((iArrHex2IntArray2[2] * 256) + iArrHex2IntArray2[3]) * 10);
            if (i4 == 0 && iAbs == 0) {
                bMSDataBean.setStatus(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.standby));
                bMSDataBean.setElectric(0);
            }
            if (i4 > 0) {
                bMSDataBean.setStatus(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.charge));
                bMSDataBean.setElectric(i4);
            }
            if (iAbs > 0) {
                bMSDataBean.setStatus(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.discharge));
                bMSDataBean.setElectric(-iAbs);
            }
            if (!bMSDataBean.getAlarm()) {
                bMSDataBean.setWrokStateDetail(bMSDataBean.getStatus());
            }
            int[] iArrHex2IntArray3 = hex2IntArray(str.substring(109, 119));
            if (iArrHex2IntArray3[0] == 0) {
                bMSDataBean.setHotBtn(false);
            } else {
                bMSDataBean.setHotBtn(true);
            }
            bMSDataBean.setAddElectric(((iArrHex2IntArray3[1] * 256) + iArrHex2IntArray3[2]) * 10);
            bMSDataBean.setTimes((iArrHex2IntArray3[3] * 256) + iArrHex2IntArray3[4]);
            int iHex2Int2 = hex2Int(str.substring(123, 125));
            bMSDataBean.setSoc(iHex2Int2);
            if (iHex2Int2 >= 80) {
                bMSDataBean.setHealth(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.perfect));
            } else if (iHex2Int2 >= 60) {
                bMSDataBean.setHealth(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.good));
            } else {
                bMSDataBean.setHealth(Constant.mBMSContext.getString(com.ble.vanomize12.R.string.bad));
            }
            int[] iArrHex2IntArray4 = hex2IntArray(str.substring(GattError.GATT_ERROR, GattError.GATT_AUTH_FAIL));
            int i5 = ((iArrHex2IntArray4[0] * 256) + iArrHex2IntArray4[1]) / 10;
            if (i5 > 1000) {
                i5 = 1000;
            }
            bMSDataBean.setCapacity(i5);
        } catch (Exception unused) {
        }
        return bMSDataBean;
    }

    private static int hex2Int(String str) {
        try {
            return Integer.valueOf(str, 16).intValue();
        } catch (Exception unused) {
            return 0;
        }
    }

    private static int[] hex2IntArray(String str) {
        int[] iArr = null;
        if (str != null && !"".equals(str) && str.length() % 2 == 0) {
            try {
                int length = str.length() / 2;
                iArr = new int[length];
                for (int i = 0; i < length; i++) {
                    int i2 = i * 2;
                    iArr[i] = hex2Int(str.substring(i2, i2 + 2));
                }
            } catch (Exception unused) {
            }
        }
        return iArr;
    }
}
