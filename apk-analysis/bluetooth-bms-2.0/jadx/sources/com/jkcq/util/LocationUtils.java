package com.jkcq.util;

import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import androidx.core.os.EnvironmentCompat;
import com.blankj.utilcode.util.Utils;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class LocationUtils {
    private static final int TWO_MINUTES = 120000;
    private static OnLocationChangeListener mListener;
    private static LocationManager mLocationManager;
    private static MyLocationListener myLocationListener;

    public interface OnLocationChangeListener {
        void getLastKnownLocation(Location location);

        void onLocationChanged(Location location);

        void onStatusChanged(String str, int i, Bundle bundle);
    }

    private LocationUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) Utils.getApp().getSystemService("location");
        return locationManager.isProviderEnabled("network") || locationManager.isProviderEnabled("gps");
    }

    public static void openGpsSettings() {
        Utils.getApp().startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS").addFlags(268435456));
    }

    public static boolean register(long j, long j2, OnLocationChangeListener onLocationChangeListener) {
        if (onLocationChangeListener == null) {
            return false;
        }
        LocationManager locationManager = (LocationManager) Utils.getApp().getSystemService("location");
        mLocationManager = locationManager;
        if (!locationManager.isProviderEnabled("network") && !mLocationManager.isProviderEnabled("gps")) {
            Log.d("LocationUtils", "无法定位，请打开定位服务");
            return false;
        }
        mListener = onLocationChangeListener;
        String bestProvider = mLocationManager.getBestProvider(getCriteria(), true);
        Location lastKnownLocation = mLocationManager.getLastKnownLocation(bestProvider);
        if (lastKnownLocation != null) {
            onLocationChangeListener.getLastKnownLocation(lastKnownLocation);
        }
        if (myLocationListener == null) {
            myLocationListener = new MyLocationListener();
        }
        mLocationManager.requestLocationUpdates(bestProvider, j, j2, myLocationListener);
        return true;
    }

    public static void unregister() {
        LocationManager locationManager = mLocationManager;
        if (locationManager != null) {
            MyLocationListener myLocationListener2 = myLocationListener;
            if (myLocationListener2 != null) {
                locationManager.removeUpdates(myLocationListener2);
                myLocationListener = null;
            }
            mLocationManager = null;
        }
        if (mListener != null) {
            mListener = null;
        }
    }

    private static Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(1);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        criteria.setBearingRequired(false);
        criteria.setAltitudeRequired(false);
        criteria.setPowerRequirement(1);
        return criteria;
    }

    public static Address getAddress(double d, double d2) throws IOException {
        try {
            List<Address> fromLocation = new Geocoder(Utils.getApp(), Locale.getDefault()).getFromLocation(d, d2, 1);
            if (fromLocation.size() > 0) {
                return fromLocation.get(0);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCountryName(double d, double d2) throws IOException {
        Address address = getAddress(d, d2);
        return address == null ? EnvironmentCompat.MEDIA_UNKNOWN : address.getCountryName();
    }

    public static String getLocality(double d, double d2) throws IOException {
        Address address = getAddress(d, d2);
        return address == null ? EnvironmentCompat.MEDIA_UNKNOWN : address.getLocality();
    }

    public static String getStreet(double d, double d2) throws IOException {
        Address address = getAddress(d, d2);
        return address == null ? EnvironmentCompat.MEDIA_UNKNOWN : address.getAddressLine(0);
    }

    public static boolean isBetterLocation(Location location, Location location2) {
        if (location2 == null) {
            return true;
        }
        long time = location.getTime() - location2.getTime();
        boolean z = time > 120000;
        boolean z2 = time < -120000;
        boolean z3 = time > 0;
        if (z) {
            return true;
        }
        if (z2) {
            return false;
        }
        int accuracy = (int) (location.getAccuracy() - location2.getAccuracy());
        boolean z4 = accuracy > 0;
        boolean z5 = accuracy < 0;
        boolean z6 = accuracy > 200;
        boolean zIsSameProvider = isSameProvider(location.getProvider(), location2.getProvider());
        if (z5) {
            return true;
        }
        if (!z3 || z4) {
            return z3 && !z6 && zIsSameProvider;
        }
        return true;
    }

    public static boolean isSameProvider(String str, String str2) {
        if (str == null) {
            return str2 == null;
        }
        return str.equals(str2);
    }

    private static class MyLocationListener implements LocationListener {
        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
        }

        private MyLocationListener() {
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (LocationUtils.mListener != null) {
                LocationUtils.mListener.onLocationChanged(location);
            }
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
            if (LocationUtils.mListener != null) {
                LocationUtils.mListener.onStatusChanged(str, i, bundle);
            }
            if (i == 0) {
                Log.d("LocationUtils", "当前GPS状态为服务区外状态");
            } else if (i == 1) {
                Log.d("LocationUtils", "当前GPS状态为暂停服务状态");
            } else {
                if (i != 2) {
                    return;
                }
                Log.d("LocationUtils", "当前GPS状态为可见状态");
            }
        }
    }
}
