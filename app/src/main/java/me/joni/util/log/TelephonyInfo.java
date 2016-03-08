package me.joni.util.log;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public final class TelephonyInfo {
    private TelephonyManager mTelephonyManager;
    private WifiManager mWifiManager;

    public TelephonyInfo(Context context) {
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public static String getNetworState(Context appContext) {
        String networkState = "?";
        if (appContext != null) {
            WifiManager mWifiManager = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
            if (mWifiManager.isWifiEnabled()) {
                networkState = "WiFi";
            } else {
                networkState = getTeleState(appContext);
            }
        }
        return networkState;
    }

    private static String getTeleState(Context appContext) {
        String networkState = "?";

        TelephonyManager mTelephonyManager = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
        int state = mTelephonyManager.getNetworkType();
        switch (state) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                networkState = "GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                networkState = "EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                networkState = "UMTS";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                networkState = "HSDPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                networkState = "HSUPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                networkState = "HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                networkState = "CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                networkState = "CDMA - EvDo rev. 0";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                networkState = "CDMA - EvDo rev. A";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                networkState = "CDMA - EvDo rev. B";
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                networkState = "CDMA - 1xRTT";
                break;
            default:
                networkState = "?";
        }
        return networkState;
    }

    // Get the cell network type
    public String getNetworkState() {
        if (mWifiManager.isWifiEnabled()) {
            // If WIFI is enabled, none of others will be using
            return "WIFI";
        }

        int state = mTelephonyManager.getNetworkType();
        switch (state) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "CDMA - EvDo rev. 0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "CDMA - EvDo rev. A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "CDMA - EvDo rev. B";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "CDMA - 1xRTT";
            default:
                return "UNKNOWN";
        }
    }
}
