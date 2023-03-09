package com.functionapps.mview_sdk2.main;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.functionapps.mview_sdk2.helper.Pinger;
import com.functionapps.mview_sdk2.helper.SimInfoUtility;
import com.functionapps.mview_sdk2.helper.Utils;
import com.functionapps.mview_sdk2.helper.WebViewHelper;
import com.functionapps.mview_sdk2.helper.Interfaces;

import org.json.JSONException;
import org.json.JSONObject;

import static com.functionapps.mview_sdk2.main.Mview.TAG;

class Speedtest  extends AsyncTask<String, Integer, JSONObject> {
    Context fapps_ctx;
    String msisdn;
    String latitude_;
    String longitude_;
    private Pinger pingResponse;
String TAG="YoutubeTestFapps";

    public Speedtest(Context ctx, String no, String lat, String longi) {
        fapps_ctx=ctx;
        msisdn=no;
        latitude_=lat;
        longitude_=longi;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
       try {
//           new WebViewHelper(fapps_ctx, pingResponse);
            /*Utils.generateCdnIpAndPing(new Interfaces.PingResult() {
                @Override
                public void onPingResultObtained(String time) {

                }

                @Override
                public void parsePingResult(Pinger response) {
                    pingResponse = response;
                    Log.i(TAG, Utils.getDateTime() + " Ping response for webview" + pingResponse);
                    if (pingResponse != null) {

                                    Log.i(TAG, Utils.getDateTime() + " Call webview helper");
                                    new WebViewHelper(fapps_ctx, pingResponse);


                    }


                }
            });*/

        } catch (Exception exp) {
            exp.printStackTrace();


        }



        return null;
    }
    @Override
    protected void onPostExecute(JSONObject obj) {
//        Log.i(TAG, "Reached in on post execute of speed test  " + obj);
//        Mview.sendtoserver(obj, "sdk_speed", msisdn, SimInfoUtility.getImsi(fapps_ctx));


    }
}
