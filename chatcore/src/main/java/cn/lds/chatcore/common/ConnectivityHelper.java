package cn.lds.chatcore.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * The Class IMConnectivity.
 */
public final class ConnectivityHelper {

    /**
     * Checks if is connected.
     *
     * @param ctx the ctx
     * @return true, if is connected
     */
    public static boolean isConnected(final Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    /**
     * Checks if is wifi.(wifi 或 流量)
     *
     * @param ctx the ctx
     * @return true, if is wifi
     */
    public static boolean isWifi(final Context ctx) {
//		WifiManager wm = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
//		WifiInfo wi = wm.getConnectionInfo();
//		if (wi != null
//				&& (WifiInfo.getDetailedStateOf(wi.getSupplicantState()) == DetailedState.OBTAINING_IPADDR || WifiInfo
//						.getDetailedStateOf(wi.getSupplicantState()) == DetailedState.CONNECTED)) {
//			return false;
//		}
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        boolean isWifi = false;
        int netType = networkInfo.getType();
        int netSubtype = networkInfo.getSubtype();

        if (netType == ConnectivityManager.TYPE_WIFI) {  //WIFI
            isWifi = true;
        } else if (netType == ConnectivityManager.TYPE_MOBILE
                && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
                && !mTelephony.isNetworkRoaming()) {   //MOBILE
            isWifi = false;
        }
        return isWifi;
    }

    /**
     * Checks if is umts.
     *
     * @param ctx the ctx
     * @return true, if is umts
     */
    public static boolean isUmts(final Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkType() >= TelephonyManager.NETWORK_TYPE_UMTS;
    }

    /**
     * Checks if is edge.
     *
     * @param ctx the ctx
     * @return true, if is edge
     */
    public static boolean isEdge(final Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE;
    }

}
