package com.generate.deviceinfolib;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import static com.generate.deviceinfolib.Common.checkPermission;

/**
 * @author TAO
 * @desc
 * @since 2020/9/23
 */
public class NetworkInfoUtil {

    private Context context;

    public NetworkInfoUtil(Context context) {
        this.context = context;
    }

    public String getNetWorkModel() {
        if (isWifiAvailable(context)) {
            return "NETWORK_WIFI";
        } else if (isNetwork4G(context)) {
            return "NETWORK_4G";
        } else if (isNetwork3G(context)) {
            return "NETWORK_3G";
        } else if (isNetwork2G(context)) {
            return "NETWORK_2G";
        } else {
            return "NETWORK_UNKNOWN";
        }
    }

    private boolean isNetwork2G(Context context) {
        int subType = getMobileNetworkType(context);
        return (subType == TelephonyManager.NETWORK_TYPE_CDMA
                || subType == TelephonyManager.NETWORK_TYPE_EDGE
                || subType == TelephonyManager.NETWORK_TYPE_GPRS
                || subType == TelephonyManager.NETWORK_TYPE_1xRTT || subType == TelephonyManager.NETWORK_TYPE_IDEN);
    }

    private boolean isNetwork3G(Context context) {
        int subType = getMobileNetworkType(context);

        boolean ret = (subType == TelephonyManager.NETWORK_TYPE_UMTS
                || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                || subType == TelephonyManager.NETWORK_TYPE_EVDO_A
                || subType == TelephonyManager.NETWORK_TYPE_EVDO_B
                || subType == TelephonyManager.NETWORK_TYPE_HSPA
                || subType == TelephonyManager.NETWORK_TYPE_EHRPD || subType == TelephonyManager.NETWORK_TYPE_HSUPA); // TODO:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            ret = ret || subType == TelephonyManager.NETWORK_TYPE_HSPAP;
        }
        return ret;
    }

    private boolean isWifiAvailable(Context ctx) {
        if (checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)) {
            ConnectivityManager conMan = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conMan == null) {
                return false;
            }
            NetworkInfo wifiInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return (wifiInfo != null && wifiInfo.getState() == NetworkInfo.State.CONNECTED);
        }
        return false;
    }

    private boolean isNetwork4G(Context context) {
        int subType = getMobileNetworkType(context);
        return subType == TelephonyManager.NETWORK_TYPE_LTE;
    }


    private int getMobileNetworkType(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
            return info.getSubtype();
        }
        return -1;
    }

}
