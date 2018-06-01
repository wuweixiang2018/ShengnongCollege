package com.education.shengnongcollege.network.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by wuweixiang on 16/12/15.
 * 网络工具类
 */

public class NetworkUtil {
    // 适配低版本手机 这些事Android原生常量，一般不是专业的人是看不懂的，嘿嘿^-^,反正我是看不懂。
    /**
     * Network type is unknown
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     */
    public static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    public static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    public static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    public static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     */
    public static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    public static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    public static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     */
    public static final int NETWORK_TYPE_HSPAP = 15;
    /** */
    public static final int NETWORK_TYPE_UNAVAILABLE = -1;

    /**
     * 是否网络可用
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    /**
     * 获取到网络状态详情wifi、2G、3G、4G
     *
     * @return
     */
    public static String getNetState(Context dcontext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) dcontext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 网络开关都没有打开 返回没有打开
        if (connectivityManager == null) {
            return "NetWorkUnavailable-NoPermission";
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            // 打开了网络，但是不可用。
            if (networkInfo == null || !networkInfo.isAvailable()) {
                return "NetWorkUnavailable-DisConnected-All";
            } else { // 可能联网
                int networkInfoType = networkInfo.getType();
                if (networkInfoType == ConnectivityManager.TYPE_MOBILE) {
                    TelephonyManager telephonyManager = (TelephonyManager) dcontext.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                    int networkType = telephonyManager.getNetworkType();
                    return getNetworkClassByType(networkType);
                } else if (networkInfoType == ConnectivityManager.TYPE_WIFI) {
                    return "wifi";
                } else {
                    return "UnknowNetWork";
                }
            }
        }
    }

    // 根据networkType来判断当前网络是2G、3G、4G
    private static String getNetworkClassByType(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_UNAVAILABLE:
                return "NetWorkUnavailable-DisConnected-Phone";
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return "2G";
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return "3G";
            case NETWORK_TYPE_LTE:
                return "4G";
            default:
                return "UnknowNetWork";
        }
    }

    public static String getJidToUsername(String jid) {
        return jid.split("@")[0];
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }
}
