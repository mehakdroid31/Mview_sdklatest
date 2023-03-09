package com.functionapps.mview_sdk2.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.functionapps.mview_sdk2.main.Mview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkUtil {
    private static final String TAG = "NetworkUtil";
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;

        if (conn == NetworkUtil.TYPE_WIFI) {
            status = "Wifi enabled";


        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = "Mobile data enabled";


        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }
    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static JSONObject getSecondSimGSMNetworkParams() throws JSONException {
        JSONObject gsmObject=new JSONObject();
//1
        gsmObject.put("pci", 0);
        gsmObject.put("earfcn",0);
        gsmObject.put("4G_ta", 0);
        gsmObject.put("4G_CQI", 0);
        gsmObject.put("4G_RSSI", 0);
        gsmObject.put("RSRP", 0);
        gsmObject.put("sinr", 0);
        gsmObject.put("TAC",0);
        gsmObject.put("enb", 0);
        gsmObject.put("4G_cellid",0);
        gsmObject.put("RSRQ",0);

//12
        gsmObject.put("psc",0);
        gsmObject.put("uarfcn",0);
        gsmObject.put("3G_CQI",0);
        gsmObject.put("3G_RSSI",0);
        gsmObject.put("RSCP",0);
        gsmObject.put("ecno",0);
        gsmObject.put("3G_lac",0);
        gsmObject.put("NodeBId",0);
        gsmObject.put("3G_cellid",0);
        //21
      //  if(mView_HealthStatus.ARFCN!=null)
            gsmObject.put("arfcn", mView_HealthStatus.second_arfcn);
       /* else
            gsmObject.put("arfcn",0);*/
        //22
      //  if(mView_HealthStatus.lteta!=null)
            gsmObject.put("2G_ta",mView_HealthStatus.secong_gsmTa);
       /* else
            gsmObject.put("2G_ta",0);*/
        //23
        gsmObject.put("rxlev", mView_HealthStatus.second_rxLev);
        //24
      //  if(mView_HealthStatus.rxqualfor2g!=null)
            gsmObject.put("rxqual",mView_HealthStatus.second_rxqual);
       /* else
            gsmObject.put("rxqual",0);*/
        //25
     //   if(mView_HealthStatus.Lac!=null)
            gsmObject.put("2G_lac",mView_HealthStatus.second_gsmLac);
       /* else
            gsmObject.put("2G_lac",0);*/

        byte[] l_byte_array = new byte[4];
        l_byte_array = CommonFunctions.convertByteArray__p(mView_HealthStatus.second_gsmCid);
        int l_RNC_ID = CommonFunctions.getRNCID_or_CID__p(l_byte_array, CommonFunctions.RNCID_C);
        int l_real_CID = CommonFunctions.getRNCID_or_CID__p(l_byte_array, CommonFunctions.CID_C);
        //26
        gsmObject.put("siteid",l_RNC_ID);
        //27
        gsmObject.put("2G_cellid",l_real_CID);
        //returning 27 params
        return  gsmObject;
    }

    public static JSONObject getGSMNetworkParams() throws JSONException {
        JSONObject gsmObject=new JSONObject();
//1
        gsmObject.put("pci", 0);
        gsmObject.put("earfcn",0);
        gsmObject.put("4G_ta", 0);
        gsmObject.put("4G_CQI", 0);
        gsmObject.put("4G_RSSI", 0);
        gsmObject.put("RSRP", 0);
        gsmObject.put("sinr", 0);
        gsmObject.put("TAC",0);
        gsmObject.put("enb", 0);
        gsmObject.put("4G_cellid",0);
        gsmObject.put("RSRQ",0);

//12
        gsmObject.put("psc",0);
        gsmObject.put("uarfcn",0);
        gsmObject.put("3G_CQI",0);
        gsmObject.put("3G_RSSI",0);
        gsmObject.put("RSCP",0);
        gsmObject.put("ecno",0);
        gsmObject.put("3G_lac",0);
        gsmObject.put("NodeBId",0);
        gsmObject.put("3G_cellid",0);
        //21
        if(mView_HealthStatus.ARFCN!=null)
            gsmObject.put("arfcn",mView_HealthStatus.ARFCN);
        else
            gsmObject.put("arfcn",0);
        //22
        if(mView_HealthStatus.lteta!=null)
            gsmObject.put("2G_ta",mView_HealthStatus.lteta);
        else
            gsmObject.put("2G_ta",0);
        //23
        gsmObject.put("rxlev", MyPhoneStateListener.getRxLev());
        //24
        if(mView_HealthStatus.rxqualfor2g!=null)
            gsmObject.put("rxqual",mView_HealthStatus.rxqualfor2g);
        else
            gsmObject.put("rxqual",0);
        //25
        if(mView_HealthStatus.Lac!=null)
            gsmObject.put("2G_lac",mView_HealthStatus.Lac);
        else
            gsmObject.put("2G_lac",0);

        byte[] l_byte_array = new byte[4];
        l_byte_array = CommonFunctions.convertByteArray__p(Integer.parseInt(mView_HealthStatus.Cid));
        int l_RNC_ID = CommonFunctions.getRNCID_or_CID__p(l_byte_array, CommonFunctions.RNCID_C);
        int l_real_CID = CommonFunctions.getRNCID_or_CID__p(l_byte_array, CommonFunctions.CID_C);
        //26
        gsmObject.put("siteid",l_RNC_ID);
        //27
        gsmObject.put("2G_cellid",l_real_CID);
        //returning 27 params
        return  gsmObject;
    }
    public static JSONObject getSecondSimThreeGNetworkParams() throws JSONException {
        int ecno = 0;
        JSONObject threeGObject=new JSONObject();
//1
        threeGObject.put("pci", 0);
        threeGObject.put("earfcn",0);
        threeGObject.put("4G_ta", 0);
        threeGObject.put("4G_CQI", 0);
        threeGObject.put("4G_RSSI", 0);
        threeGObject.put("RSRP", 0);
        threeGObject.put("sinr", 0);
        threeGObject.put("TAC",0);
        threeGObject.put("enb", 0);
        threeGObject.put("4G_cellid",0);
        threeGObject.put("RSRQ",0);
        //12
        if(mView_HealthStatus.Wcdma_Psc!=null)
            threeGObject.put("psc",mView_HealthStatus.Wcdma_Psc);
        else
            threeGObject.put("psc",0);


        //13
      //  if(mView_HealthStatus.Uarfcn!=null)
            threeGObject.put("uarfcn",mView_HealthStatus.second_uarfcn_3G);
      /*  else
            threeGObject.put("uarfcn",0);*/
//14
     //   if(MyPhoneStateListener.getCQI()!=null)
            threeGObject.put("3G_CQI",mView_HealthStatus.second_Cqi);
       /* else
            threeGObject.put("3G_CQI",0);*/
        //15
        threeGObject.put("3G_RSSI",MyPhoneStateListener.getRxLev());
        //16
      //  if (mView_HealthStatus.rscp!=null)
            threeGObject.put("RSCP",mView_HealthStatus.second_rscp_3G);
      /*  else
            threeGObject.put("RSCP",0);*/
        /*if(mView_HealthStatus.rscp!=null) {
            int rscp = Integer.parseInt(mView_HealthStatus.rscp);
            ecno = rscp - MyPhoneStateListener.getRxLev();
        }*/

        //17
         ecno = mView_HealthStatus.second_rscp_3G - MyPhoneStateListener.getRxLev();
        threeGObject.put("ecno",ecno);
        //18
       // if(mView_HealthStatus.Lac!=null)
            threeGObject.put("3G_lac",mView_HealthStatus.second_lac_3G);
       /* else
            threeGObject.put("3G_lac",0);*/
        //19
    //    if(mView_HealthStatus.nodeb_id!=null)
            threeGObject.put("NodeBId",mView_HealthStatus.second_NodeBID_3G);
      /*  else
            threeGObject.put("NodeBId",0);*/
        //20
        byte[] l_byte_array = new byte[4];
        threeGObject.put("3G_cellid", mView_HealthStatus.second_cid_3G);
       /* if(mView_HealthStatus.Cid!=null) {
            l_byte_array = CommonFunctions.convertByteArray__p(Integer.parseInt(mView_HealthStatus.Cid));
            int l_real_CID = CommonFunctions.getRNCID_or_CID__p(l_byte_array, CommonFunctions.CID_C);
            threeGObject.put("3G_cellid", mView_HealthStatus.second_cid_3G);
        }
        else
        {
            threeGObject.put("3G_cellid", 0);
        }*/

       /* l_byte_array = CommonFunctions.convertByteArray__p(Integer.parseInt(mView_HealthStatus.Cid));
        int l_real_CID = CommonFunctions.getRNCID_or_CID__p(l_byte_array, CommonFunctions.CID_C);
        threeGObject.put("3G_cellid",l_real_CID);*/
        //21
        threeGObject.put("arfcn",0);
        threeGObject.put("2G_ta",0);
        threeGObject.put("rxlev",0);
        threeGObject.put("rxqual",0);
        threeGObject.put("2G_lac",0);
        threeGObject.put("siteid",0);
        threeGObject.put("2G_cellid",0);
//27params
        return threeGObject;
    }

    public static JSONObject getThreeGNetworkParams() throws JSONException {
        int ecno = 0;
        JSONObject threeGObject=new JSONObject();
//1
        threeGObject.put("pci", 0);
        threeGObject.put("earfcn",0);
        threeGObject.put("4G_ta", 0);
        threeGObject.put("4G_CQI", 0);
        threeGObject.put("4G_RSSI", 0);
        threeGObject.put("RSRP", 0);
        threeGObject.put("sinr", 0);
        threeGObject.put("TAC",0);
        threeGObject.put("enb", 0);
        threeGObject.put("4G_cellid",0);
        threeGObject.put("RSRQ",0);
        //12
        if(mView_HealthStatus.Wcdma_Psc!=null)
            threeGObject.put("psc",mView_HealthStatus.Wcdma_Psc);
        else
            threeGObject.put("psc",0);


        //13
        if(mView_HealthStatus.Uarfcn!=null)
            threeGObject.put("uarfcn",mView_HealthStatus.Uarfcn);
        else
            threeGObject.put("uarfcn",0);
//14
        if(MyPhoneStateListener.getCQI()!=null)
            threeGObject.put("3G_CQI",MyPhoneStateListener.getCQI());
        else
            threeGObject.put("3G_CQI",0);
        //15
        threeGObject.put("3G_RSSI",MyPhoneStateListener.getRxLev());
        //16
        if (mView_HealthStatus.rscp!=null)
            threeGObject.put("RSCP",mView_HealthStatus.rscp);
        else
            threeGObject.put("RSCP",0);
        if(mView_HealthStatus.rscp!=null) {
            int rscp = Integer.parseInt(mView_HealthStatus.rscp);
            ecno = rscp - MyPhoneStateListener.getRxLev();
        }

        //17

        threeGObject.put("ecno",ecno);
        //18
        if(mView_HealthStatus.Lac!=null)
            threeGObject.put("3G_lac",mView_HealthStatus.Lac);
        else
            threeGObject.put("3G_lac",0);
        //19
        if(mView_HealthStatus.nodeb_id!=null)
            threeGObject.put("NodeBId",mView_HealthStatus.nodeb_id);
        else
            threeGObject.put("NodeBId",0);
        //20
        byte[] l_byte_array = new byte[4];

        if(mView_HealthStatus.Cid!=null) {
            l_byte_array = CommonFunctions.convertByteArray__p(Integer.parseInt(mView_HealthStatus.Cid));
            int l_real_CID = CommonFunctions.getRNCID_or_CID__p(l_byte_array, CommonFunctions.CID_C);
            threeGObject.put("3G_cellid", l_real_CID);
        }
        else
        {
            threeGObject.put("3G_cellid", 0);
        }

       /* l_byte_array = CommonFunctions.convertByteArray__p(Integer.parseInt(mView_HealthStatus.Cid));
        int l_real_CID = CommonFunctions.getRNCID_or_CID__p(l_byte_array, CommonFunctions.CID_C);
        threeGObject.put("3G_cellid",l_real_CID);*/
        //21
        threeGObject.put("arfcn",0);
        threeGObject.put("2G_ta",0);
        threeGObject.put("rxlev",0);
        threeGObject.put("rxqual",0);
        threeGObject.put("2G_lac",0);
        threeGObject.put("siteid",0);
        threeGObject.put("2G_cellid",0);
//27params
        return threeGObject;
    }

    public static JSONObject getSecondaryLTENetworkParams() throws JSONException {

        JSONObject lteObject=new JSONObject();
        System.out.println("periodic lte "+"pci "+mView_HealthStatus.second_pci
                +"earfcn "+mView_HealthStatus.second_arfcn);
        if(mView_HealthStatus.ltePCI!=null)

            lteObject.put("pci", mView_HealthStatus.second_pci);
        else
            lteObject.put("pci", 0);
            lteObject.put("earfcn",mView_HealthStatus.second_earfcn);

            lteObject.put("4G_CQI", mView_HealthStatus.second_Cqi);

        if(MyPhoneStateListener.getLTERSSI()!=null)

            lteObject.put("4G_RSSI", MyPhoneStateListener.getLTERSSI());

        else
            lteObject.put("4G_RSSI", 0);

            lteObject.put("RSRP", mView_HealthStatus.second_Rsrp);

            lteObject.put("sinr", mView_HealthStatus.second_snr);

            lteObject.put("TAC", mView_HealthStatus.second_tac);

            lteObject.put("enb", mView_HealthStatus.second_ENB);

            lteObject.put("4G_cellid",mView_HealthStatus.second_Cid);

            lteObject.put("RSRQ", 0);//check

        lteObject.put("psc",0);
        lteObject.put("uarfcn",0);
        lteObject.put("3G_CQI",0);
        lteObject.put("3G_RSSI",0);
        lteObject.put("RSCP",0);
        lteObject.put("ecno",0);
        lteObject.put("3G_lac",0);
        lteObject.put("NodeBId",0);
        lteObject.put("3G_cellid",0);//20
        //21
        lteObject.put("arfcn",0);
        lteObject.put("2G_ta",0);
        lteObject.put("rxlev",0);
        lteObject.put("rxqual",0);
        lteObject.put("2G_lac",0);
        lteObject.put("siteid",0);
        lteObject.put("2G_cellid",0);//27


//returning 27 vals
        return lteObject;

    }



    public static JSONObject getLTENetworkParams() throws JSONException {

        JSONObject lteObject=new JSONObject();
        System.out.println("periodic lte "+"pci "+mView_HealthStatus.ltePCI +"earfcn "+mView_HealthStatus.lteArfcn);
        //1
        if(mView_HealthStatus.ltePCI!=null)
            lteObject.put("pci", mView_HealthStatus.ltePCI);
        else
            lteObject.put("pci", 0);
        //2
        if(mView_HealthStatus.lteArfcn!=null)
            lteObject.put("earfcn",mView_HealthStatus.lteArfcn);
        else
            lteObject.put("earfcn",0);
        //3
        if(mView_HealthStatus.lteta!=null)
            lteObject.put("4G_ta", mView_HealthStatus.lteta);
        else
            lteObject.put("4G_ta", 0);

        //4
        if(MyPhoneStateListener.getCQI()!=null)
            lteObject.put("4G_CQI", MyPhoneStateListener.getCQI());
        else
            lteObject.put("4G_CQI",0);
        //5
        if(MyPhoneStateListener.getLTERSSI()!=null)
            lteObject.put("4G_RSSI", MyPhoneStateListener.getLTERSSI());
        else
            lteObject.put("4G_RSSI", 0);
        //6
        if(mView_HealthStatus.lteRSRP!=null)
            lteObject.put("RSRP", mView_HealthStatus.lteRSRP);
        else
            lteObject.put("RSRP",0);
        //7
        if(MyPhoneStateListener.getSNR()!=null)
            lteObject.put("sinr", MyPhoneStateListener.getSNR());
        else
            lteObject.put("sinr", 0);
        //8
        if(mView_HealthStatus.lteTAC!=null)
            lteObject.put("TAC", mView_HealthStatus.lteTAC);
        else
            lteObject.put("TAC", 0);
//9
        if(mView_HealthStatus.lteENB!=null)
            lteObject.put("enb", mView_HealthStatus.lteENB);
        else
            lteObject.put("enb", 0);
        //10
        if(mView_HealthStatus.Cid!=null) {
           /* int cid1 = Integer.parseInt(mView_HealthStatus.Cid) & 0xff;*/

            String cellidHex = DecToHex(Integer.parseInt(mView_HealthStatus.Cid));

            try {


                if (cellidHex.length() > 2) {
                    String localcellid = cellidHex.substring(cellidHex.length() - 2);
                    int cid1 = Integer.parseInt(localcellid);

//            int cid1=getLocalcellId(Integer.parseInt(mView_HealthStatus.Cid));

                    Log.i(Mview.TAG, "Cid is " + mView_HealthStatus.Cid + " and local id is " + cid1);
                    lteObject.put("4G_cellid", cid1);
                } else {
                    lteObject.put("4G_cellid", "NA");
                }
            }
            catch (NumberFormatException e)
            {
                lteObject.put("4G_cellid", "NA");
                e.printStackTrace();
            }
        }

        else
        {
            lteObject.put("4G_cellid",0);

        }
        //11
        if(mView_HealthStatus.lteRSRQ!=null)

            lteObject.put("RSRQ", mView_HealthStatus.lteRSRQ);
        else
            lteObject.put("RSRQ", 0);
        //12
        lteObject.put("psc",0);
        lteObject.put("uarfcn",0);
        lteObject.put("3G_CQI",0);
        lteObject.put("3G_RSSI",0);
        lteObject.put("RSCP",0);
        lteObject.put("ecno",0);
        lteObject.put("3G_lac",0);
        lteObject.put("NodeBId",0);
        lteObject.put("3G_cellid",0);//20
        //21
        lteObject.put("arfcn",0);
        lteObject.put("2G_ta",0);
        lteObject.put("rxlev",0);
        lteObject.put("rxqual",0);
        lteObject.put("2G_lac",0);
        lteObject.put("siteid",0);
        lteObject.put("2G_cellid",0);//27


//returning 27 vals
        return lteObject;

    }
    public static  String DecToHex(int dec) {
        //& 0xff

        return Integer.toHexString(dec);



    }
    public static JSONObject getLTEParams() throws JSONException {

        JSONObject lteObject=new JSONObject();
        lteObject.put("pci", 0);
        lteObject.put("earfcn",0);
        lteObject.put("4G_ta", 0);
        lteObject.put("4G_CQI", 0);
        lteObject.put("4G_RSSI", 0);
        lteObject.put("RSRP", 0);
        lteObject.put("sinr", 0);
        lteObject.put("TAC",0);
        lteObject.put("enb", 0);
        lteObject.put("4G_cellid",0);
        lteObject.put("RSRQ",0);//11
        //
        lteObject.put("psc",0);
        lteObject.put("uarfcn",0);
        lteObject.put("3G_CQI",0);
        lteObject.put("3G_RSSI",0);
        lteObject.put("RSCP",0);
        lteObject.put("ecno",0);
        lteObject.put("3G_lac",0);
        lteObject.put("NodeBId",0);
        lteObject.put("3G_cellid",0);//9
        //
        lteObject.put("arfcn",0);
        lteObject.put("2G_ta",0);
        lteObject.put("rxlev",0);
        lteObject.put("rxqual",0);
        lteObject.put("2G_lac",0);
        lteObject.put("siteid",0);
        lteObject.put("2G_cellid",0);//7
        return lteObject;

    }

    public static String getSecondSimSignalStrength() {
        String sec_strength="NA";
        Log.i(TAG,"Second sim signal strength instance "+mView_HealthStatus.second_cellInstance);
        if (mView_HealthStatus.second_cellInstance != null) {

            if (mView_HealthStatus.second_cellInstance.equalsIgnoreCase("lte")) {
                {

                    if (mView_HealthStatus.second_Rsrp < 0 && mView_HealthStatus.second_Rsrp > -75) {


                        sec_strength = mView_HealthStatus.second_Rsrp +"dbm";
                    } else if (mView_HealthStatus.second_Rsrp <= -75 && mView_HealthStatus.second_Rsrp > -95) {

                        sec_strength = mView_HealthStatus.second_Rsrp + "dbm";
                    } else if (mView_HealthStatus.second_Rsrp <= -95 /*&& mView_HealthStatus.second_Rsrp > -115*/) {

                        sec_strength = mView_HealthStatus.second_Rsrp + "dbm";
                    }

                }


            } else if (mView_HealthStatus.second_cellInstance.equalsIgnoreCase("wcdma")) {

                if (mView_HealthStatus.second_rscp_3G < 0 && mView_HealthStatus.second_rscp_3G > -75) {

                    sec_strength = mView_HealthStatus.second_rscp_3G + "dbm";
                } else if (mView_HealthStatus.second_rscp_3G <= -75 && mView_HealthStatus.second_rscp_3G > -95) {

                    sec_strength = mView_HealthStatus.second_rscp_3G + "dbm";
                } else if (mView_HealthStatus.second_rscp_3G <= -95 /*&& mView_HealthStatus.second_rscp_3G > -115*/) {

                    sec_strength = mView_HealthStatus.second_rscp_3G + "dbm";
                }


            } else if (mView_HealthStatus.second_cellInstance.equalsIgnoreCase("gsm")) {


                if (mView_HealthStatus.second_rxLev < 0 && mView_HealthStatus.second_rxLev > -75) {

                    sec_strength = mView_HealthStatus.second_rxLev + "dbm";
                } else if (mView_HealthStatus.second_rxLev <= -75 && mView_HealthStatus.second_rxLev > -95) {

                    sec_strength = mView_HealthStatus.second_rxLev + "dbm";
                } else if (mView_HealthStatus.second_rxLev <= -95 /*&& mView_HealthStatus.second_rxLev > -115*/) {

                    sec_strength = mView_HealthStatus.second_rxLev + "dbm";
                }

            }
        }

        return sec_strength;
    }
    public static JSONArray getNeighboringCellsInfoForGSM() {


        {
            JSONArray jsonArray=new JSONArray();
            if(NeighboringCellsInfo.gsm_neighboringCellList!=null &&NeighboringCellsInfo.gsm_neighboringCellList.size()>0) {
                for (int i = 0; i < 1; i++) {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("4G_PCI", 0);
                        object.put("4G_EARFCN", 0);
                        object.put("4G_TA", 0);
                        object.put("4G_CQI", 0);
                        object.put("4G_RSRQ", 0);
                        object.put("4G_RSRP", 0);
                        object.put("4G_TAC", 0);
                        object.put("4G_SINR", 0);
                        object.put("4G_CI", 0);
                        object.put("4G_ENB", 0);//10

                        //wcdma

                        object.put("3G_CID", 0);
                        object.put("3G_LAC", 0);
                        object.put("3G_PSC", 0);
                        object.put("3G_UARFCN", 0);
                        object.put("3G_RSCP", 0);
                        object.put("3G_RSSI", 0);
                        object.put("3G_CQI", 0);
                        object.put("3G_NODE_BID", 0);//8


                        //gsm
                        for (int j = 0; j < NeighboringCellsInfo.gsm_neighboringCellList.get(0).size(); j++) {
                            object.put(NeighboringCellsInfo.gsmParams.get(j),
                                    NeighboringCellsInfo.gsm_neighboringCellList.get(i).get(j));
                        }

                        jsonArray.put(object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                getNeighboringCellsInfo();
            }
            return jsonArray;
        }
    }

    public static JSONArray getNeighboringCellsInfoForWcdma() {
        JSONArray jsonArray=new JSONArray();
        if(NeighboringCellsInfo.wcdma_neighboringCellList!=null && NeighboringCellsInfo.wcdma_neighboringCellList.size()>0) {
            for (int i = 0; i < 1; i++) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("4G_PCI", 0);
                    object.put("4G_EARFCN", 0);
                    object.put("4G_TA", 0);
                    object.put("4G_CQI", 0);
                    object.put("4G_RSRQ", 0);
                    object.put("4G_RSRP", 0);
                    object.put("4G_TAC", 0);
                    object.put("4G_SINR", 0);
                    object.put("4G_CI", 0);
                    object.put("4G_ENB", 0);//10

                    //wcdma

                    for (int j = 0; j < NeighboringCellsInfo.wcdma_neighboringCellList.get(0).size(); j++) {
                        object.put(NeighboringCellsInfo.wcdmaParams.get(j), NeighboringCellsInfo.wcdma_neighboringCellList.get(i).get(j));//8
                    }


                    //gsm
                    object.put("2G_LAC", 0);
                    object.put("2G_CID", 0);
                    object.put("2G_ARFCN", 0);
                    object.put("2G_BSIC", 0);
                    object.put("2G_PSC", 0);
                    object.put("2G_RX_LEVEL", 0);
                    object.put("2G_TA", 0);
                    object.put("2G_SITE_ID", 0);//8
                    jsonArray.put(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            getNeighboringCellsInfo();
        }

        return jsonArray;
    }

    public static JSONArray getNeighboringCellsInfoForLte() {
        JSONArray jsonArray=new JSONArray();

        try {
            if(NeighboringCellsInfo.neighboringCellList!=null && NeighboringCellsInfo.neighboringCellList.size()>0) {
                for (int i = 0; i < 1 ; i++) {
                    JSONObject lteObject = new JSONObject();
                    for (int j = 0; j < NeighboringCellsInfo.neighboringCellList.get(0).size(); j++)
                    {
                        lteObject.put(NeighboringCellsInfo.lteParams.get(j), NeighboringCellsInfo.neighboringCellList.get(i).get(j));
                    }
                    Log.i(Mview.TAG,"getNeighboringCellsInfoForLte metod returtned "+lteObject);
                    jsonArray.put(lteObject);
                    lteObject.put("3G_CID", 0);
                    lteObject.put("3G_LAC", 0);
                    lteObject.put("3G_PSC", 0);
                    lteObject.put("3G_UARFCN", 0);
                    lteObject.put("3G_RSCP", 0);
                    lteObject.put("3G_RSSI", 0);
                    lteObject.put("3G_CQI", 0);
                    lteObject.put("3G_NODE_BID", 0);//8

                    //gsm
                    lteObject.put("2G_LAC", 0);
                    lteObject.put("2G_CID", 0);
                    lteObject.put("2G_ARFCN", 0);
                    lteObject.put("2G_BSIC", 0);
                    lteObject.put("2G_PSC", 0);
                    lteObject.put("2G_RX_LEVEL", 0);
                    lteObject.put("2G_TA", 0);
                    lteObject.put("2G_SITE_ID", 0);//8*/

                }
            }
            else
            {
                getNeighboringCellsInfo();
            }
        }

        catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("json array size finally is "+jsonArray.length());
        return jsonArray;



    }

    public static JSONArray getNeighboringCellsInfoForNR() {
        JSONArray jsonArray=new JSONArray();

        try {
            if(NeighboringCellsInfo.nr_neighboringCellList!=null && NeighboringCellsInfo.nr_neighboringCellList.size()>0) {
                for (int i = 0; i < 1; i++) {
                    JSONObject lteObject = new JSONObject();
                    for (int j = 0; j < NeighboringCellsInfo.nr_neighboringCellList.get(0).size(); j++)
                    {
                        lteObject.put(NeighboringCellsInfo.nrParams.get(j), NeighboringCellsInfo.nr_neighboringCellList.get(i).get(j));
                    }
                    Log.i(Mview.TAG,"getNeighboringCellsInfoForLte metod returtned "+lteObject);
                    jsonArray.put(lteObject);
                    lteObject.put("3G_CID", 0);
                    lteObject.put("3G_LAC", 0);
                    lteObject.put("3G_PSC", 0);
                    lteObject.put("3G_UARFCN", 0);
                    lteObject.put("3G_RSCP", 0);
                    lteObject.put("3G_RSSI", 0);
                    lteObject.put("3G_CQI", 0);
                    lteObject.put("3G_NODE_BID", 0);//8

                    //gsm
                    lteObject.put("2G_LAC", 0);
                    lteObject.put("2G_CID", 0);
                    lteObject.put("2G_ARFCN", 0);
                    lteObject.put("2G_BSIC", 0);
                    lteObject.put("2G_PSC", 0);
                    lteObject.put("2G_RX_LEVEL", 0);
                    lteObject.put("2G_TA", 0);
                    lteObject.put("2G_SITE_ID", 0);//8*/

                }
            }
            else
            {
                getNeighboringCellsInfo();
            }
        }

        catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("json array size finally is "+jsonArray.length());
        return jsonArray;



    }

    public static JSONArray getNeighboringCellsInfo() {

        JSONObject object=new JSONObject();
        JSONArray jsonArray=new JSONArray();

        try {

            object.put("4G_PCI",0);
            object.put("4G_EARFCN",0);
            object.put("4G_TA",0);
            object.put("4G_CQI",0);
            object.put("4G_RSRQ",0);
            object.put("4G_RSRP",0);
            object.put("4G_TAC",0);
            object.put("4G_SINR",0);
            object.put("4G_CI",0);
            object.put("4G_ENB",0);//10


            object.put("3G_CID", 0);
            object.put("3G_LAC", 0);
            object.put("3G_PSC", 0);
            object.put("3G_UARFCN", 0);
            object.put("3G_RSCP", 0);
            object.put("3G_RSSI", 0);
            object.put("3G_CQI", 0);
            object.put("3G_NODE_BID", 0);//8

            //gsm
            object.put("2G_LAC", 0);
            object.put("2G_CID", 0);
            object.put("2G_ARFCN", 0);
            object.put("2G_BSIC", 0);
            object.put("2G_PSC", 0);
            object.put("2G_RX_LEVEL", 0);
            object.put("2G_TA", 0);
            object.put("2G_SITE_ID", 0);
            object.put("signalstrength",0);//9
            jsonArray.put(object);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;

    }


}
