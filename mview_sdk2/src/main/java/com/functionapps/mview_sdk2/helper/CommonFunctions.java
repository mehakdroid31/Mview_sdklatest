package com.functionapps.mview_sdk2.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Sharad Gupta on 10/11/2016.
 */
public class CommonFunctions {

    public static String chkDataConnectivity(Context c) {
        String s = "";
        final ConnectivityManager connMgr = (ConnectivityManager)
               c.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting ()) {
            s = "WiFi";
            WifiManager wm = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wm.getConnectionInfo();
            int linkSpeed = wifiInfo.getLinkSpeed();
            String ss = wifiInfo.getSSID();
            int rs = wifiInfo.getRssi();
            s = "WiFi (Link " + linkSpeed + "Mbps, " + ss + ", " + rs +"dBm)\n";
            mView_HealthStatus.connectionType = "WiFi";
            mView_HealthStatus.connectionTypeIdentifier = 1;
        } else if (mobile.isConnectedOrConnecting ()) {


            s = "Mobile data"; //getNetworkTypeString (ListenService.telMgr.getNetworkType());
            s += "\n";
            mView_HealthStatus.connectionType = "Mobile data";
            mView_HealthStatus.connectionTypeIdentifier = 2;
        } else {
            s = "Offline";
            mView_HealthStatus.connectionTypeIdentifier = 0;
            //Toast.makeText(this, "No Network ", Toast.LENGTH_LONG).show();
        }
        return s;
    }

    public static byte[] convertByteArray__p(int p_int){
        byte[] l_byte_array = new byte[4];
        int MASK_c = 0xFF;
        for (short i=0; i<=3; i++){
            l_byte_array[i] = (byte) ((p_int >> (8*i)) & MASK_c);
        }
        return l_byte_array;
    }

    public static short CID_C =1;
    public static short RNCID_C = 2;
    public static int getRNCID_or_CID__p(byte[] p_bytes, short p_which){
        int MASK_c = 0xFF;
        int l_result = 0;
        if (p_which == CID_C) {
            l_result = p_bytes[0] & MASK_c ;
            l_result = l_result + ((p_bytes[1] & MASK_c ) << 8);
        } else if (p_which == RNCID_C){
            l_result = p_bytes[2] & MASK_c ;
            l_result = l_result + ((p_bytes[3] & MASK_c ) << 8);
        } else {
            //g_FileHandler.putLog__p('E', "getRNCID_or_CID__p invalid parameter");
        }
        return l_result;
    }

}
