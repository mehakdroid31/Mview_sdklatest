package com.functionapps.mview_sdk2.helper;


import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.functionapps.mview_sdk2.main.Mview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.functionapps.mview_sdk2.helper.NetworkUtil.getGSMNetworkParams;
import static com.functionapps.mview_sdk2.helper.NetworkUtil.getLTENetworkParams;
import static com.functionapps.mview_sdk2.helper.NetworkUtil.getLTEParams;
import static com.functionapps.mview_sdk2.helper.NetworkUtil.getSecondSimThreeGNetworkParams;
import static com.functionapps.mview_sdk2.helper.NetworkUtil.getSecondaryLTENetworkParams;
import static com.functionapps.mview_sdk2.helper.NetworkUtil.getThreeGNetworkParams;

public class Network_Params {

    static Context ctx;
    private static TelephonyManager teleMan;
    private static PhoneStateListener myPhoneStateListener;
//    private static ListenService listenService;

    public static JSONArray getnetwork_params()


    {




       return getvales(Mview.fapps_ctx);

}
    public static JSONArray getnetwork_params2()



    {




        return getvalues2(Mview.fapps_ctx);

    }

    private static JSONArray getvales(Context ctx) {


        try {


            JSONArray jsonArray = new JSONArray();


            if (MyPhoneStateListener.getNetworkType() == 4) {
                jsonArray.put(getLTENetworkParams());
                // jsonArray.put(getThreeGParams());
                //jsonArray.put(getGSMParams());
            } else if (MyPhoneStateListener.getNetworkType() == 3) {

                jsonArray.put(getThreeGNetworkParams());

            } else if (MyPhoneStateListener.getNetworkType() == 2) {
                jsonArray.put(getGSMNetworkParams());
            }
            else if (MyPhoneStateListener.getNetworkType() == 5)
            {
                jsonArray.put(getFiveGparams());

            }
            else {
                jsonArray.put(getLTEParams());

            }
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("MCC", mView_HealthStatus.prim_mcc+"");
            jsonObject.put("MNC",mView_HealthStatus.prim_mnc+"");
            jsonObject.put("type",mView_HealthStatus.strCurrentNetworkProtocol+"");


            jsonObject.put("Network_params",jsonArray);
            JSONArray jsonArray1=new JSONArray();
            jsonArray1.put(jsonObject);

            return jsonArray1;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }



    }

    private static JSONObject getFiveGparams() {
        JSONObject obj=new JSONObject();
        int arfcn=MyPhoneStateListener.cellIdentityNr.getNrarfcn();


        try {
            obj.put("earfcn",arfcn);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

    private static JSONArray getvalues2(Context ctx) {


        try {


            JSONArray jsonArray = new JSONArray();


            if (MyPhoneStateListener.getNetworkType() == 4) {
                jsonArray.put(getSecondaryLTENetworkParams());
                // jsonArray.put(getThreeGParams());
                //jsonArray.put(getGSMParams());
            } else if (MyPhoneStateListener.getNetworkType() == 3) {

                jsonArray.put(getSecondSimThreeGNetworkParams());

            } else if (MyPhoneStateListener.getNetworkType() == 2) {
                jsonArray.put(getGSMNetworkParams());
            }
            else if (MyPhoneStateListener.getNetworkType()==5)
            {
                jsonArray.put(getFiveGparams());

            }
            else {
                jsonArray.put(getSecondaryLTENetworkParams());

            }
            JSONObject jsonObject=new JSONObject();

            jsonObject.put("Network_params",jsonArray);
            JSONArray jsonArray1=new JSONArray();
            jsonArray1.put(jsonObject);

            return jsonArray1;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }



    }


    public static void addcontext(Context context) {

        ctx=context;
        Log.i("LogFapps","enabling class");

    }

    public static ArrayList<HashMap<String ,String>> getservingcell1info() {
        ArrayList<HashMap<String ,String >>list= new ArrayList<>();

        list=mView_HealthStatus.servingcell1info;


        return list;
    }
    public static ArrayList<HashMap<String ,String>> getservingcell2info() {
        ArrayList<HashMap<String ,String >>list= new ArrayList<>();

        list=mView_HealthStatus.servingcell2info;


        return list;
    }
}
