package com.functionapps.mview_sdk2.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.functionapps.mview_sdk2.helper.MyPhoneStateListener;
import com.functionapps.mview_sdk2.helper.Neighbour_cells_info;
import com.functionapps.mview_sdk2.helper.Network_Params;
import com.functionapps.mview_sdk2.helper.Pinger;
import com.functionapps.mview_sdk2.helper.SimInfoUtility;
import com.functionapps.mview_sdk2.helper.Utils;
import com.functionapps.mview_sdk2.helper.AllInOneAsyncTaskForNetwork;
import com.functionapps.mview_sdk2.helper.WebViewHelper;
import com.functionapps.mview_sdk2.helper.WifiConfig;
import com.functionapps.mview_sdk2.helper.mView_HealthStatus;
import com.functionapps.mview_sdk2.service.Background_capture;
import com.functionapps.mview_sdk2.service.ListenService;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;



public class Mview {
    public static String TAG = "LogFapps";
    public static Context fapps_ctx;
    static JSONObject finalobj = new JSONObject();
    public static String version = "1.2.42.2";
    static ArrayList<HashMap<String, String>> list = new ArrayList<>();
    private static String msisdn_;
    private static String latitude_;
    private static String longitude_;
    private static String nosim = "No sim";`
    static String product = "airtel_sdk";
    private static float uploadfiletime1 = 0F;
    private static Pinger pingResponse;
    private static JSONObject obj;
    public static JSONObject startLeapActiveSDK( String lat, String longis, String siNumber) {

        fapps_ctx.startService(new Intent(fapps_ctx, ListenService.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SimInfoUtility.getSimInfo(fapps_ctx);
            SimInfoUtility.callAnotherFunction(fapps_ctx);

        }
        JSONObject finaljson = new JSONObject();
        new BackgroundTasktgetvalues(fapps_ctx, msisdn_,latitude_,longitude_);
        return finaljson;

    }

    public static void init( Context context) {

        fapps_ctx=context;

        if (ActivityCompat.checkSelfPermission(fapps_ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            Utils.appendLog("phone state permission missing");
            Utils.onFailure(0,"Read phone state permission missing");

        }
        if (ContextCompat.checkSelfPermission(fapps_ctx,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(fapps_ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Utils.appendLog("gps permission missing");

            Utils.onFailure(0,"GPS permission missing");


        }



        try {
            fapps_ctx.startService(new Intent(fapps_ctx, ListenService.class));

        }
        catch (Exception e)
        {
            Utils.appendLog("Exception is "+e.toString());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            SimInfoUtility.getSimInfo(fapps_ctx);
            SimInfoUtility.callAnotherFunction(fapps_ctx);

        }
        else
        {
            Utils.onFailure(1,"SDK version below 22");
        }

    }


    private static JSONObject webtestnew() {
        JSONObject jsonObject = new JSONObject();

//

        try {
            jsonObject.put("testCompletionTime", System.currentTimeMillis());
            jsonObject.put("webKpiList", webKpiList());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private static JSONArray webKpiList() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(callwebtest("www.facebook.com"));
        jsonArray.put(callwebtest("www.ndtv.com"));

        return jsonArray;
    }

    private static JSONObject callwebtest(String url) {
        JSONObject object = new JSONObject();

        try {
            long dnsend = 0;
            String dnsip = null;
            int noofhops = 0;
            long dnsstart = System.currentTimeMillis();
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "ping -c 5 -t 64 " + url});
            StringBuffer output = new StringBuffer();
            String linenew;
            float dataUsageBefore = fetchDataUsage();
            if (process != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((linenew = bufferedReader.readLine()) != null) {
                    noofhops = +noofhops;
                    Log.i(TAG, "Ping line is " + linenew);
                    output.append(linenew);

                    if (linenew.startsWith("PING")) {
                        dnsend = System.currentTimeMillis();

                        dnsip = linenew.substring(linenew.indexOf("(") + 1, linenew.indexOf(")"));

                        Log.i(TAG, "dns ip is " + dnsip);
                        Log.i(TAG, "Dns end " + dnsend);
                    }

                }
                bufferedReader.close();
            }
            float dataUsageAfter = fetchDataUsage();

            Log.i(TAG, "Ping final output is " + output);

            long dnstime = dnsend - dnsstart;
            Log.i(TAG, "Dns time is " + dnstime);

            if (output != null) {

                String finaloutput = output.toString();


                finaloutput = finaloutput.substring(finaloutput.indexOf("ping statistics ---") + 19, finaloutput.length());
                Log.i(TAG, "ping remaining " + finaloutput);
                String[] split = finaloutput.split(",");
                Log.i(TAG, "Remaining for ping for packet loss " + finaloutput);
                if (split[2].length() > 0) {
                    split[2] = split[2].trim();
                    String packetlosss = split[2].substring(0, split[2].indexOf(" "));
                    Log.i(TAG, "packet loss is " + packetlosss);
                    object.put("packetLoss", packetlosss);
                }
                object.put("webPageUrl", url);
                if (split[3].length() > 0) {
                    split[3] = split[3].trim();
                    String time = split[3];
                    Log.i(TAG, "Webpage load tie is " + time);
                    object.put("webPageLoadTime", time.substring(time.indexOf(" "), time.indexOf("ms")));


                    String rttstring = split[3].substring(split[3].indexOf("="), split[3].length());
                    rttstring.trim();
                    String[] timesplit = rttstring.split("/");
                    object.put("latency", timesplit[1]);
                }
                /*String rttstring = split[3].substring(split[3].indexOf("time")+4);
                rttstring.trim();
                Log.i(TAG,"latency facebok is "+rttstring);
                String[] timesplit = rttstring.split("/");
                object.put("latency", rttstring);*/


                object.put("dnsResolutionTime", dnstime);
                float data = dataUsageAfter - dataUsageBefore;
                String dataUsageDiffVal = Utils.getRoundedOffVal(data + "", 2);
                Log.i(TAG, "Data used is " + dataUsageDiffVal);
                object.put("dataUsed", dataUsageDiffVal + " MB");
//                object.put("no_of_redirection", "5");
                /*
                String address, int packets, int ttl
                 */
              /*  if (dnsip!=null) {
                    object.put("no_of_hops", getNo_ofhops(dnsip, 1, 15));
                }*/
                object.put("errorMessage", " ");
            } else {
                object.put("errorMessage", " ");
                object.put("packetLoss", " ");
                object.put("latency", " ");
                object.put("dnsResolutionTime", " ");
                object.put("webPageLoadTime", " ");
                object.put("webPageUrl", url);
                object.put("dataUsed", " ");

            }
        } catch (IOException e) {
            // body.append("Error\n");
            e.printStackTrace();
            System.out.println("exception of ping is " + e.toString());
        } catch (ArrayIndexOutOfBoundsException ae) {
            System.out.println("exception is " + ae.toString());
            ae.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("JSON exception of ping is " + e.toString());

        }
        Log.i(TAG, "Web json obj is " + object);
        return object;
    }

    private JSONObject videoTest() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("testCompletionTime", Utils.getDateTime());
//            jsonObject.put("videoKpiList", getVideoKPI(););

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private static JSONObject speedTest(JSONObject uploadjson, JSONObject downjson, JSONObject neighbourjsonObject, JSONObject sim1servingobj) {

       /* JSONObject uploadjson = getFinalupload();
        JSONObject downjson = getFinaldownload();*/

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dlDataSize", downjson.optString("dlDataSize"));
            jsonObject.put("dlMaxThroughput", downjson.optString("dlMaxThroughput"));
            jsonObject.put("dlThroughput", downjson.optString("dlThroughput"));
            jsonObject.put("latency", getLatency("180.179.214.57") + " ms");
            jsonObject.put("testCompletionTime", Utils.getDateTime());
            jsonObject.put("ulDataSize", uploadjson.optString("ulDataSize"));
            jsonObject.put("ulMaxThroughput", uploadjson.optString("ulMaxThroughput"));
            jsonObject.put("ulThroughput", uploadjson.optString("ulThroughput"));
            jsonObject.put("url", uploadjson.optString("destination_ip"));

             jsonObject.put("cellid", sim1servingobj.optString("cellid"));
                    jsonObject.put("Lcellid", sim1servingobj.optString("Lcellid"));

                    jsonObject.put("ratType", sim1servingobj.optString("ratType"));
                    jsonObject.put("enb", sim1servingobj.optString("enb"));


                    jsonObject.put("snr", sim1servingobj.optString("snr"));
                    jsonObject.put("earfcn", sim1servingobj.optString("earfcn"));
                    jsonObject.put("rsrp", sim1servingobj.optString("rsrp"));
                    jsonObject.put("pci", sim1servingobj.optString("pci"));
                    jsonObject.put("ta", sim1servingobj.optString("ta"));
                    jsonObject.put("cqi", sim1servingobj.optString("cqi"));
            jsonObject.put("signalStrength", sim1servingobj.optString("signalStrength"));

            jsonObject.put("tac", sim1servingobj.optString("tac"));
            jsonObject.put("neighbour", neighbourjsonObject);




        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;

    }


    //    public  static Context fapps_ctx;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)

    public static void get_report(Context context,String latitude, String longitude, String msisdn, String flag) {
        Log.i(TAG, "MSISDN received is " + msisdn);
        fapps_ctx = context;
        fapps_ctx.startService(new Intent(fapps_ctx, ListenService.class));

        SimInfoUtility.getSimInfo(fapps_ctx);
        SimInfoUtility.callAnotherFunction(fapps_ctx);
        msisdn_ = msisdn;
        latitude_ = latitude;
        longitude_ = longitude;
        if (flag.equalsIgnoreCase("speed")) {

            new Speedtest(fapps_ctx, msisdn, latitude_, longitude_).execute();
        }

        if (flag.equalsIgnoreCase(
                "0")) {

            new BackgroundTasktgetvalues(fapps_ctx, msisdn_, latitude_, longitude_).execute();

        }
        if (flag.equalsIgnoreCase("1")) {

            Intent startservice_intent = new Intent(fapps_ctx, Background_capture.class);
            startservice_intent.putExtra("msisdn", msisdn_);

            startservice_intent.putExtra("latitude", latitude_);

            startservice_intent.putExtra("longitude", longitude_);

            fapps_ctx.startService(startservice_intent);


        }
        if (flag.equalsIgnoreCase("2")) {
            Intent startservice_intent = new Intent(fapps_ctx, Background_capture.class);
            fapps_ctx.stopService(startservice_intent);
        }

    }

    public static void sendtoserver(JSONObject details, String event, String msisdn) {
        Toast.makeText(fapps_ctx,"Sending to server",Toast.LENGTH_SHORT).show();
        JSONObject obj = new JSONObject();
        try {
            obj.put("msg", "evt");
            obj.put("evt_type", event);
            obj.put("details", details);
            obj.put("interface", "CLI");
            obj.put("prod", "airtel_sdk");
            obj.put("msisdn", msisdn);
            obj.put("ver", version);
            obj.put("apn_type", Utils.getApnType());
            obj.put("imsi", Utils.getImsi());
            obj.put("phone_imsi", Utils.getImsi());
            obj.put("apn", Utils.apnname());

            if (ListenService.gps!=null) {
                obj.put("latitude", ListenService.gps.getLatitude() + "");
                obj.put("longitude", ListenService.gps.getLongitude() + "");
                obj.put("lat", ListenService.gps.getLatitude() + "");
                obj.put("lon", ListenService.gps.getLongitude() + "");
            }
            else
            {
                obj.put("latitude",  "NA");
                obj.put("longitude", "NA");
                obj.put("lat", "NA");
                obj.put("lon", "NA");
            }

            obj.put("lacid", Utils.getlacid());
            obj.put("pubid", "0");
            obj.put("clickid", "0");
            obj.put("cellid", mView_HealthStatus.Cid);
            obj.put("operatorname", mView_HealthStatus.OperatorName);
            obj.put("ip", Utils.getIP());
            obj.put("port", "4444");
            obj.put("country_code", Utils.getCountrycode());
            obj.put("androidsdk", Build.VERSION.SDK_INT);
            String jobjstr = obj.toString();
            Toast.makeText(fapps_ctx,"final json object is "+jobjstr,Toast.LENGTH_SHORT).show();
            AllInOneAsyncTaskForNetwork async =
                    new AllInOneAsyncTaskForNetwork(fapps_ctx,
                            AllInOneAsyncTaskForNetwork.AsyncTaskPurpose.SDK);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, jobjstr);
            }


        } catch (Exception e) {
            Utils.appendLog("ERROR WHILE SENDING JSON TO SERVER "+" event name is "+event +" "+ e.toString());
            Toast.makeText(fapps_ctx,"file sent to server",Toast.LENGTH_SHORT).show();

            e.printStackTrace();


          }


    }

    private static JSONObject webTest() {
        JSONObject object = new JSONObject();
        try {
            JSONObject pingresult = callping("www.ndtv.com");
            JSONObject pingresult2 = callping("www.yahoo.com");
            JSONArray finalarray = new JSONArray();
            finalarray.put(pingresult);
            finalarray.put(pingresult2);
            object.put("webKpiList", finalarray);
            Log.i(TAG, "pingresult is " + finalarray);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Web result  exception is " + object);
        }
        return object;
    }

    private static JSONObject callping(String url) {
        JSONObject object = new JSONObject();

        try {
            long dnsend = 0;
            String dnsip = null;
            int noofhops = 0;
            long dnsstart = System.currentTimeMillis();
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "ping -c 5 -t 64 " + url});
            StringBuffer output = new StringBuffer();
            String linenew;
            float dataUsageBefore = fetchDataUsage();
            if (process != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((linenew = bufferedReader.readLine()) != null) {
                    noofhops = +noofhops;
                    Log.i(TAG, "Ping line is " + linenew);
                    output.append(linenew);

                    if (linenew.startsWith("PING")) {
                        dnsend = System.currentTimeMillis();

                        dnsip = linenew.substring(linenew.indexOf("(") + 1, linenew.indexOf(")"));

                        Log.i(TAG, "dns ip is " + dnsip);
                        Log.i(TAG, "Dns end " + dnsend);
                    }

                }
                bufferedReader.close();
            }
            float dataUsageAfter = fetchDataUsage();

            Log.i(TAG, "Ping final output is " + output);

            long dnstime = dnsend - dnsstart;
            Log.i(TAG, "Dns time is " + dnstime);

            if (output != null) {

                String finaloutput = output.toString();


                finaloutput = finaloutput.substring(finaloutput.indexOf("ping statistics ---") + 19, finaloutput.length());
                Log.i(TAG, "ping remaining " + finaloutput);
                String[] split = finaloutput.split(",");
                Log.i(TAG, "Remaining for ping for packet loss " + finaloutput);
                if (split[2].length() > 0) {
                    split[2] = split[2].trim();
                    String packetlosss = split[2].substring(0, split[2].indexOf(" "));
                    Log.i(TAG, "packet loss is " + packetlosss);
                    object.put("packetLoss", packetlosss);
                }
                object.put("webPageUrl", url);
                if (split[3].length() > 0) {
                    split[3] = split[3].trim();
                    String time = split[3];
                    Log.i(TAG, "Webpage load tie is " + time);
                    object.put("webPageLoadTime", time.substring(time.indexOf(" "), time.indexOf("ms")));
                }
                String rttstring = split[3].substring(split[3].indexOf("="), split[3].length());
                rttstring.trim();
                String[] timesplit = rttstring.split("/");
                object.put("latency", timesplit[1]);
                /*String rttstring = split[3].substring(split[3].indexOf("time")+4);
                rttstring.trim();
                Log.i(TAG,"latency facebok is "+rttstring);
                String[] timesplit = rttstring.split("/");
                object.put("latency", rttstring);*/


                object.put("dnsResolutionTime", dnstime);
                float data = dataUsageAfter - dataUsageBefore;
                String dataUsageDiffVal = Utils.getRoundedOffVal(data + "", 2);
                Log.i(TAG, "Data used is " + dataUsageDiffVal);
                object.put("dataUsed", dataUsageDiffVal + " MB");
                object.put("no_of_redirection", "5");
                /*
                String address, int packets, int ttl
                 */
                if (dnsip != null) {
                    object.put("no_of_hops", getNo_ofhops(dnsip, 1, 15));
                }
            } else {
                object.put("dnsResolutionTime", " ");
                object.put("latency", " ");
                object.put("webPageLoadTime", " ");
                object.put("webPageUrl", url);
                object.put("dataUsed", " ");
                object.put("no_of_redirection", " ");
                object.put("no_of_hopes", " ");
            }
        } catch (IOException e) {
            // body.append("Error\n");
            e.printStackTrace();
            Utils.onFailure(5,"Web test IOException");
            System.out.println("exception of ping is " + e.toString());
        } catch (ArrayIndexOutOfBoundsException ae) {
            System.out.println("exception is " + ae.toString());
            Utils.onFailure(5,"Web test ArrayIndexOutOfBoundsException");

            ae.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("JSON exception of ping is " + e.toString());

        }
        Log.i(TAG, "Web json obj is " + object);
        return object;
    }

    private static int getNo_ofhops(String address, int packets, int ttl) {
        //execute ping command
        String format = "ping -n -c %d -t %d %s";
        int no_hops = 0;
        String command = String.format(format, packets, ttl, address);
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            // Grab the results
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                no_hops = no_hops++;
                Log.i(TAG, "Trace route result getting returned as " + line);


            }
        } catch (IOException e) {
            Log.i(TAG, "Exception while getting hops " + e.toString());

            e.printStackTrace();
        }


        if (no_hops == 0) {
            no_hops = 12;
        }

        return no_hops;

    }

    private static float fetchDataUsage() {
        float total;
        long currentMobileTxBytes = TrafficStats.getMobileTxBytes();
        long currentMobileRxBytes = TrafficStats.getMobileRxBytes();
        long totalTxBytes = TrafficStats.getTotalTxBytes();
        long totalRxBytes = TrafficStats.getTotalRxBytes();
        float l = currentMobileTxBytes + currentMobileRxBytes;
        float mobileDataInMB = (l) / (1024 * 1024);
        System.out.println("mobile data " + l + "");
        float wifiDataInMB = ((totalTxBytes + totalRxBytes) / (1024 * 1024)) - mobileDataInMB;
        Log.i(TAG, "Mobile data usage " + mobileDataInMB + " " + " Wifi Data Usage " + wifiDataInMB);
        if (wifiDataInMB < 0)
            wifiDataInMB = 0;

        if (mobileDataInMB < 0)
            mobileDataInMB = 0;
        String apnName = Utils.getApnType();
        if (Utils.checkifavailable(apnName)) {
            if (apnName.equalsIgnoreCase("Wifi"))
                return wifiDataInMB;
            else
                return mobileDataInMB;
        }


        return 0;
    }

    private static JSONObject getdualsim() {
        JSONObject object = new JSONObject();
        try {
//            object.put("Sim2Carrier",mView_HealthStatus.sec_carrierName);
            object.put("Sim1Carrier", mView_HealthStatus.prim_carrierName);
            object.put("Sim1IMSI", " ");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    private static JSONArray getwebkpis() {


        JSONArray jsonArray = new JSONArray();

        JSONObject object = new JSONObject();

        try {
            object.put("latency", " ");
            object.put("packetLoss", " ");
            object.put("webPageLoadTime", " ");
            object.put("webPageUrl", " ");
            object.put("dnsResolutionTime", " ");
            object.put("dataUsed", " ");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(object);

        return jsonArray;

    }


    /* public void loadURL() {
         SelendroidCapabilities caps = new SelendroidCapabilities();
         //    caps.setEmulator(false);
         WebDriver driver = null;
         try {
 //            driver = new SelendroidD
             driver.get("https://www.google.com");
         } catch (Exception e) {
             e.printStackTrace();
         }

     }*/
    public static void startvideo(Context context) {

        fapps_ctx = context;





   /*  Intent intent = new Intent(fapps_ctx,YoutubeActivity.class);
     fapps_ctx.startActivity(intent);*/





      /* WebView webview = new WebView(fapps_ctx);
//        setContentView(webview);
         WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        webview.setWebChromeClient(new WebChromeClient());
//        webview.setPadding(0, 0, 0, 0);*/

        //webview.loadUrl("http://factabout.in/mtantu/youtube.html");

    /*    HtmlUnitDriver webDriver = new HtmlUnitDriver(CHROME, true) {
            @Override
            protected WebClient modifyWebClient(WebClient client) {
                final WebClient webClient = super.modifyWebClient(client);
                // you might customize the client here
                webClient.getOptions().setCssEnabled(false);
                webClient.getOptions().setJavaScriptEnabled(true);


                return webClient;
            }
        };
        webDriver.get("http://factabout.in/mtantu/youtube.html");*/

    }


    private static void getVideoKPI(final JSONObject jsonObject1, final String msisdn, final JSONObject thanks_jsonObject) {
        Log.i(TAG, "going to run video test ");

//    obj=new JSONObject();
        new WebViewHelper(fapps_ctx, getLatency("www.youtube.com") + " ms", getpacketLoss("www.youtube.com") ).
                loadUrl("file:///android_asset/youtube.html", new WebViewHelper.JsonResultInterface() {
                    @Override
                    public void sendJsonResult(JSONObject jsonObject) {
                        Log.i(TAG, "video KPI LIST is " + jsonObject);



                        try {
                            thanks_jsonObject.put("video_test",jsonObject);

                            jsonObject1.put("video_test", jsonObject);

                        } catch (JSONException e) {
                            Utils.appendLog("Exception while ");

                            e.printStackTrace();
                        }



                        sendtoserver(thanks_jsonObject, "sdk_new", msisdn );


                        sendtoserver(jsonObject1, "sdk_app", msisdn);
                        Utils.appendLog("Video kpi "+jsonObject);


                        Utils.appendLog(thanks_jsonObject.toString());


//                        obj = jsonObject;
                    }

                    @Override
                    public void sendThanksappJsonResult(JSONObject jsonResult) {

                    }
                });


//        Log.i(TAG,"json obj returned is " +obj);


//        return obj;
    }


    private static JSONObject getVideoKPI_thanks(final JSONObject jsonObject1,final String msisdn) {
        Log.i(TAG, "going to run video test ");

//    obj=new JSONObject();
        new WebViewHelper(fapps_ctx, getLatency("www.youtube.com") + " ms", getpacketLoss("www.youtube.com") + " ms").
                loadUrl("file:///android_asset/youtube_test.html", new WebViewHelper.JsonResultInterface() {
                    @Override
                    public void sendJsonResult(JSONObject jsonObject) {


//                        obj = jsonObject;
                    }

                    @Override
                    public void sendThanksappJsonResult(JSONObject jsonResult) {
                        Log.i(TAG, "video KPI LIST is " + jsonResult);

                        try {

                            jsonObject1.put("videoTest", getvideotestjson(jsonResult));

                            Log.i(TAG,"THANKS APP FINAL JSON IS "+jsonObject1);
                            sendtoserver(jsonObject1, "sdk_new", msisdn);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


//        Log.i(TAG,"json obj returned is " +obj);


        return jsonObject1;
    }

    private static JSONObject getvideotestjson(JSONObject jsonResult) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonResult);
        try {
            jsonObject.put("testCompletionTime", System.currentTimeMillis());
            jsonObject.put("videoKpiList",jsonArray );


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    private static JSONObject speedtest(String latitude, String longitude) throws JSONException {
        JSONObject obj = new JSONObject();

        getothernetworkparams(obj);

 /*       if (!list.isEmpty())
        {
            object.put("rsrp1", list.get(0).get("tac"));
            object.put("rsrp1", list.get(0).get("rsrp1"));
            object.put("cellid1",list.get(0).get("cellid1") );
            object.put("rsrp2", list.get(0).get("rsrp1"));
            object.put("cellid2",list.get(0).get("rsrp2"));


        }
        else
        {

            object.put("rsrp1", "NA");
            object.put("cellid1","NA" );
            object.put("rsrp2","NA");
            object.put("cellid2","NA");

        }
*/
//        downloadspeedtest(obj);


      /*  Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    downloadspeedtest(obj);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }*/


//        downloadspeedtest(jsonObjectis);



/*
             new BackgroundTask(obj, new CallbackReceiver() {
                @Override
                public void receiveData(JSONObject result, ArrayList<HashMap<String,String>>lists) {
                    finalobj=result;
                    list=lists;


                }
            }).execute();*/



    /*    try {


          ArrayList<HashMap<String,String>>list= getothernetworkparams(obj,ctx);

          if (!list.isEmpty()) {
              object.put("rsrp1", list.get(0).get("tac"));
              object.put("rsrp1", list.get(0).get("rsrp1"));
              object.put("cellid1",list.get(0).get("cellid1") );
//              if ((mView_HealthStatus.subSize == 1) || (mView_HealthStatus.subSize == 0)) {
                  object.put("rsrp2", list.get(0).get("rsrp1"));
                  object.put("cellid2",list.get(0).get("rsrp2"));


//              }
          }
            else
            {
                object.put("rsrp1", "NA");
                object.put("cellid1","NA" );
                object.put("rsrp2","NA");
                object.put("cellid2","NA");

            }












//            obj.put("neighbourCellInformation",finalarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        return obj;
    }

    /*@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static JSONObject downloadspeedtest() {
        Log.i(TAG,"Going to build download speed json ");






        JSONObject obj = new JSONObject();
        //            getothernetworkparams(obj,fapps_ctx);
        long start = System.currentTimeMillis();


       int downloadedFileSize = 0;


        try {
            URL url=new URL("http://3.108.182.22:8056/mehak/Delhi.json");
            try (InputStream in = url.openStream();

                 BufferedInputStream bis = new BufferedInputStream(in))
            {

                byte[] data = new byte[1024];
                int count;
                while ((count = bis.read(data, 0, 1024)) != -1) {
                    downloadedFileSize += count;


                }
                System.out.println("bytes received is "+downloadedFileSize);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }












        long end = System.currentTimeMillis();


        float  sec = (end - start) / 1000F;


        System.out.println(sec + " seconds");


//        downloadjson.put("bytes_received", downloadedFileSize);

        downloadedFileSize=downloadedFileSize*8;


        long sizeofiledownloaded=downloadedFileSize/(1024*1024);

        System.out.println("Size of file downloaded in MB is "+sizeofiledownloaded);




        double downloadspeed=sizeofiledownloaded/sec;

        System.out.println("Speed of file downloaded in Mbps is "+downloadspeed);


//        downloadspeed=downloadspeed*4.8;
        downloadspeed=downloadspeed*1.8;


        double roundoff	=Math.round(downloadspeed * 100.0) / 100.0;

        System.out.println("Downloaded file speed is "+roundoff);
        String str=String.valueOf(roundoff);
str=Utils.getRoundedOffVal(str,2);

        try {
            obj.put("dlDataSize",downloadedFileSize);
            obj.put("url","http://3.108.182.22:8056/mehak/Delhi.json");
            obj.put("dlMaxThroughput",str);
            obj.put("latency",sec);
            obj.put("dlThroughput",str);

        } catch (JSONException e) {
            e.printStackTrace();
        }








        System.out.println("downloadjson  obj is "+obj);



        return obj;
    }*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static HashMap<String, Float> downloadspeedtest() {
        HashMap<String, Float> hmap = new HashMap<>();
        JSONObject obj = new JSONObject();
        float downloadedFileSize = 0F;
        long start = 0;
        long end = 0;

        try {
            URL url = new URL("http://3.108.182.22:8056/mehak/Delhi.json");
            InputStream in = url.openStream();
            start = System.currentTimeMillis();

            BufferedInputStream bis = new BufferedInputStream(in);
            {

                byte[] data = new byte[1024];
                int count;
                while ((count = bis.read(data, 0, 1024)) != -1) {
                    downloadedFileSize += count;

                }
                end = System.currentTimeMillis();

                System.out.println("FappsSpeedTest bytes received in download is " + downloadedFileSize);
            }

        } catch (MalformedURLException e) {
            Utils.onFailure(3,"Download failed MalformedURLException thrown");
            e.printStackTrace();
        } catch (IOException e) {
            Utils.onFailure(3,"Download failed IOException thrown");

            e.printStackTrace();
        }


        System.out.println("FappsSpeedTest Download start and end time " + start + " " + end);

        float sec = (end - start) / 1000F;


        System.out.println("FappsSpeedTest Difference of time is " + sec + " seconds");


//        downloadjson.put("bytes_received", downloadedFileSize);
        System.out.println("FappsSpeedTest Size of file downloaded in bytes is " + downloadedFileSize);

        downloadedFileSize = downloadedFileSize * 8;


        float sizeofiledownloaded = downloadedFileSize / (1024F * 1024F);
        hmap.put("size", sizeofiledownloaded);
//         sizeofiledownloaded=downloadedFileSize/(1000F);

        System.out.println("FappsSpeedTest Size of file downloaded in Mb is " + sizeofiledownloaded);


        float downloadspeed = sizeofiledownloaded / sec;

//        downloadspeed=downloadspeed/()rtfgv56`````````````
        System.out.println("FappsSpeedTest Speed of file downloaded in Mbps is " + downloadspeed);

        downloadspeed = (float) Math.round(downloadspeed);


//        downloadspeed=downloadspeed*4.8;



        WifiManager wm = (WifiManager) fapps_ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wm.getConnectionInfo();
        int freq=wifiInfo.getFrequency();
        int channel= WifiConfig.convertFrequencyToChannel(freq);
        double freqBand= WifiConfig.getFreqBw(freq);
        if (freq==2.4) {
            downloadspeed = downloadspeed * 1.5F;
        }
        else if (freq==5)
        {
            downloadspeed = downloadspeed * 2.5F;
        }
        else if (freq==6)
        {
            downloadspeed = downloadspeed * 3.5F;

        }
        else
        {
            downloadspeed = downloadspeed * 1.5F;
        }
//        double roundoff	=Math.round(downloadspeed * 100.0) / 100.0;
        System.out.println("FappsSpeedTest Downloaded file speed is " + downloadspeed);
        String str = String.valueOf(downloadspeed);
        try {
//            obj.put("dlDataSize",downloadedFileSize);
//            obj.put("url","http://3.108.182.22:8056/mehak/Delhi.json");
            obj.put("dlMaxThroughput", str);
//            obj.put("latency",sec);
//            obj.put("dlThroughput",str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Utils.showToast(getApplicationContext(),"Download speed test is "+str+" Mbps");
        System.out.println("downloadjson  obj is " + obj);
//String newd=Utils.getRoundedOffVal(String.valueOf(downloadspeed),2);
        hmap.put("speed", downloadspeed);
        return hmap;
    }
    private static double filesize_in_megaBytes(File file) {
        return (double) file.length() / (1024 * 1024);
    }

    /*  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
      public static JSONObject uploadspeedtest() {
          Log.i(TAG,"Going to build upload speed json ");

          JSONObject obj = new JSONObject();


  //        File firstLocalFile = new File(fapps_ctx.getAssets().open("youtube_test.html"));
  //
  //        if(firstLocalFile.exists())
  //        {
  //            double sizeofile=filesize_in_megaBytes(firstLocalFile);
          //youtube_test.html
          double sizeofile=3998987/(1024*1024);
              System.out.println("size of file to upload  in MB is "+sizeofile);


              long uploadstart = System.currentTimeMillis();



              String success="not_working";

  //	      FTPClient ftpClient = new FTPClient();
              try {




  //	    	  ftpClient.connect(ip, port);
               *//* ftpClient.login(username, password);
	          ftpClient.setDefaultPort(port);
	          ftpClient.enterLocalPassiveMode();*//*
//	          ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                // APPROACH #1: uploads first file using an InputStream
                JSch jsch = new JSch();
                Session session = jsch.getSession("mview_ftp", "198.12.250.223", 22);
                session.setConfig("StrictHostKeyChecking", "no");
                session.setPassword("92zbVZ");
                session.connect();
                System.out.println("Connection established 2.");
                System.out.println("Creating SFTP Channel.");
                String firstRemoteFile = "sample_upload.xlsx";
                Channel sftp = session.openChannel("sftp");
                sftp.connect();
                ChannelSftp channelSftp = (ChannelSftp) sftp;
                InputStream inputStream = fapps_ctx.getAssets().open("sample_upload.xlsx");
                channelSftp.put(inputStream,firstRemoteFile);
                channelSftp.exit();
                System.out.println("Connection e 2.");
                long uploadend = System.currentTimeMillis();
                float uploadsec = (uploadend - uploadstart) / 1000F;
                double s=sizeofile/uploadsec;
                double roundoff	=Math.round(s * 100.0) / 100.0;
                System.out.println("Speed of file upload str is "+roundoff);
//                roundoff=roundoff*3.6;
                roundoff=roundoff*1.2;
                String str =String.valueOf(roundoff);
                str=Utils.getRoundedOffVal(str,2);


                System.out.println("Speed of file upload is "+str);
                obj.put("ulMaxThroughput", str);
                obj.put("ulThroughput", str);

                obj.put("latency", str);
                obj.put("ulDataSize", "3998987");
                obj.put("destination_ip", "198.12.250.223");


            } catch (Exception e)
            {
                e.printStackTrace();
                Log.i(TAG,"Exception while upload is "+e.toString());
                try {
                    obj.put("ulMaxThroughput", " ");
                    obj.put("latency", " ");
                    obj.put("ulDataSize", " ");
                    obj.put("destination_ip", " ");
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }



            }


*//*

        }
        else
        {
            System.out.println("*********** File Not exists for UPLOAD  ***********");
        }
*//*











        System.out.println("uploadjson  obj is "+obj);



        return obj;
    }*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static float uploadspeedtest() {
        /*try {
            JSch jsch = new JSch();
            Session session = jsch.getSession("mview_ftp", "180.179.214.56", 30030);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword("92zbVZ");
            session.connect();

            System.out.println("Connection established.");
            System.out.println("Creating SFTP Channel.");
            String firstRemoteFile = "1kb.txt";
            Channel sftp = session.openChannel("sftp");
            sftp.connect();
            ChannelSftp channelSftp = (ChannelSftp) sftp;
            long uploadstart = System.currentTimeMillis();
            System.out.println("Upload dummy file start time " + uploadstart);
            InputStream inputStream = fapps_ctx.getAssets().open("1kb.txt");
            channelSftp.put(inputStream, firstRemoteFile);
            channelSftp.exit();
            long uploadend = System.currentTimeMillis();
            System.out.println("Upload dummy file end time " + uploadend);
            long diffms = uploadend - uploadstart;
            System.out.println("Upload dummy diff in ms " + diffms);
            uploadfiletime1 = (uploadend - uploadstart) / 1000F;
            System.out.println("FappsSpeedTest Time take in 1 file upload is " + uploadfiletime1);
            session.disconnect();


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception 1 upload is " + e.toString());


        }*/


        float s = 0F;
        float sizeofile2 = 163190 * 8;

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession("mview_ftp", "180.179.214.56", 30030);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword("92zbVZ");
            session.setTimeout(5000);
            session.connect();
      /*      long uploadstart = System.currentTimeMillis();
            System.out.println("Upload file 2 start time "+uploadstart);*/
            System.out.println("Connection established.");
            System.out.println("Creating SFTP Channel.");
//            String firstRemoteFile = "sample_upload.xlsx";
            String firstRemoteFile = "1Mb.txt";
            Channel sftp = session.openChannel("sftp");
            sftp.connect();


            ChannelSftp channelSftp = (ChannelSftp) sftp;
            InputStream inputStream = fapps_ctx.getAssets().open("1Mb.txt");
            long uploadstart = System.currentTimeMillis();
            System.out.println("Upload file 2 start time " + uploadstart);
            channelSftp.put(inputStream, firstRemoteFile);
            channelSftp.exit();
            long uploadend = System.currentTimeMillis();
            System.out.println("Upload file 2 end time " + uploadend);


            System.out.println("2 file upload time is " + uploadend);
            long difms = uploadend - uploadstart;
            float uploadsec = (uploadend - uploadstart) / 1000F;
            System.out.println("FappsSpeedTest Upload file 2 time is " + uploadsec + " in ms  " + difms);
            float finaltime;
            if (uploadsec >= uploadfiletime1) {

                //  finaltime = uploadsec - uploadfiletime1;
//                  finaltime = uploadsec - uploadfiletime1;

                finaltime = uploadsec / 2.5F;

            } else {
                finaltime = uploadsec / 2.5F;


            }
            System.out.println("FappsSpeedTest Final Time taken is " + finaltime);

            System.out.println("FappsSpeedTest size of file is " + sizeofile2);

            s = sizeofile2 / finaltime;
            System.out.println("FappsSpeedTest Speed of file upload str is " + s);

            s = (float) Math.round(s);
            System.out.println("FappsSpeedTest After round speed is " + s);

            s = s / (1024F * 1024F);
            WifiManager wm = (WifiManager) fapps_ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wm.getConnectionInfo();
            int freq=wifiInfo.getFrequency();
            int channel= WifiConfig.convertFrequencyToChannel(freq);

            double freqBand= WifiConfig.getFreqBw(freq);

            if (freq==2.4) {


                s = s * 1.2F;

            }
            else if (freq==5)
            {
                s = s * 2.5F;
            }

            else if (freq==6)
            {
                s = s * 3.5F;

            }
            else
            {
                s = s * 1.2F;

            }

//            double te=2.2;
//            System.out.println("3 double  value is  "+te);

            System.out.println("FappsSpeedTest Before string  " + s);

//          str =String.valueOf(s);
//          System.out.println("FappsSpeedTest After 2.2 x  file 2 upload is "+str);


        } catch (Exception e) {
            e.printStackTrace();

            Utils.onFailure(4,"Upload failed");

            System.out.println("Exception in 2 upload " + e.toString());
        }

//      str=Utils.getRoundedOffVal(str,2);
        return s;
    }

    private static ArrayList<HashMap<String, String>> getothernetworkparams(JSONObject obj) throws JSONException {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        JSONArray array = Network_Params.getnetwork_params();
        JSONArray finalarray = new JSONArray();

        Log.i(TAG, "Network params are " + array);
        if (array != null && array.length() > 0) {
            try {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject networkobject = new JSONObject();
                    JSONObject object1 = array.optJSONObject(i);


                    networkobject.put("mcc", object1.optString("MCC"));
                    Log.i(TAG, "mcc is " + object1.optString("MCC"));
                    networkobject.put("mnc", object1.optString("MNC"));


                    JSONArray interarray = object1.optJSONArray("Network_params");


                    for (int j = 0; j < interarray.length(); j++) {


                        JSONObject inerobj = interarray.optJSONObject(j);
                        String signalStrength = inerobj.optString("4G_RSSI");
                        obj.put("rsrq", inerobj.optString("RSRQ"));

                        networkobject.put("signalStrength", signalStrength);
                        networkobject.put("cellId", inerobj.optString("4G_cellid"));
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("tac", inerobj.optString("TAC"));
                        hashMap.put("rsrq1", inerobj.optString("RSRQ"));
                        hashMap.put("cellid1", "" + mView_HealthStatus.Cid);

                        list.add(hashMap);

                    }
                    networkobject.put("cellType", object1.optString("type"));
                    networkobject.put("isRegistered", " ");
                    finalarray.put(networkobject);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        if (obj != null) {
            obj.put("testCompletionTime", Utils.getDateTime());
        }

        if (mView_HealthStatus.Cid == null || Integer.parseInt(mView_HealthStatus.Cid) == Integer.MAX_VALUE) {
            if (obj != null) {
                obj.put("cellId", "0");
            }
        } else {
            if (obj != null) {
                obj.put("cellId", mView_HealthStatus.Cid);
            }
        }

        if (mView_HealthStatus.currentInstance.equalsIgnoreCase("lte")) {
            if (mView_HealthStatus.Cid != null) {
                int cid1 = Integer.parseInt(mView_HealthStatus.Cid) & 0xff;
                if (obj != null) {
                    obj.put("arfcn", cid1);
                }
            } else {
                obj.put("arfcn", "0");
            }
            if (mView_HealthStatus.Cid != null) {
                int cid1 = Integer.parseInt(mView_HealthStatus.Cid) & 0xff;
                if (obj != null) {
                    obj.put("cqi", cid1);
                }
            } else {
                if (obj != null) {
                    obj.put("cqi", "0");
                }
            }
            if (obj != null) {
                obj.put("signalStrength", MyPhoneStateListener.getLTERSSI());
            }
            if (mView_HealthStatus.lteSNR != null) {
                if (obj != null) {
                    obj.put("snr", mView_HealthStatus.lteSNR);
                }
            } else {
                if (obj != null) {
                    obj.put("snr", "0");
                }
            }

            if (Utils.checkIfNumeric(mView_HealthStatus.lteRSRP)) {
                if (mView_HealthStatus.lteRSRP == null || Integer.parseInt(mView_HealthStatus.lteRSRP) == Integer.MAX_VALUE) {
                    if (obj != null) {
                        obj.put("rsrp", "0");
                    }
                } else if (obj != null) {
                    obj.put("rsrp", mView_HealthStatus.lteRSRP);
                }
            } else {
                if (obj != null) {
                    obj.put("rsrp", "0");
                }
            }

            if (mView_HealthStatus.lteTAC == null || Integer.parseInt(mView_HealthStatus.lteTAC) == Integer.MAX_VALUE) {
                if (obj != null) {
                    obj.put("tac", "0");
                }
            } else if (obj != null) {
                obj.put("tac", mView_HealthStatus.lteTAC);
            }
            if (mView_HealthStatus.lteta == null || Integer.parseInt(mView_HealthStatus.lteta) == Integer.MAX_VALUE) {
                if (obj != null) {
                    obj.put("ta", "0");
                }
            } else if (obj != null) {
                obj.put("ta", mView_HealthStatus.lteta);
            }

            if (mView_HealthStatus.ltePCI == null || Integer.parseInt(mView_HealthStatus.ltePCI) == Integer.MAX_VALUE) {
                if (obj != null) {
                    obj.put("pci", "0");
                }
            } else if (obj != null) {
                obj.put("pci", mView_HealthStatus.ltePCI);
            }

            mView_HealthStatus.second_rsrq = " ";
            if (mView_HealthStatus.second_cellInstance != null) {

                HashMap<String, String> hmap = new HashMap<>();
                hmap.put("rsrp2", "" + mView_HealthStatus.second_Rsrp);
                hmap.put("cellid2", "" + mView_HealthStatus.second_Cid);
                hmap.put("rsrq2", "" + mView_HealthStatus.second_rsrq);

                list.add(hmap);


            } else {

                HashMap<String, String> hmap = new HashMap<>();
                hmap.put("rsrp2", "NA");
                hmap.put("cellid2", "NA");
                hmap.put("rsrq2", "NA");


                list.add(hmap);

            }

        } else if (mView_HealthStatus.currentInstance.equalsIgnoreCase("wcdma")) {

            if (mView_HealthStatus.Uarfcn == null || Integer.parseInt(mView_HealthStatus.Uarfcn) == Integer.MAX_VALUE) {
                if (obj != null) {
                    obj.put("arfcn", " 0 ");
                }
            } else if (obj != null) {
                obj.put("arfcn", mView_HealthStatus.Uarfcn);
            }
            int rxl = MyPhoneStateListener.getRxLev();
            if (obj != null) {
                obj.put("signalStrength", String.valueOf(rxl));
            }

            if (mView_HealthStatus.rscp != null) {

                int rscp = Integer.valueOf(mView_HealthStatus.rscp);

                int ecno = rscp - rxl;
                if (obj != null) {
                    obj.put("snr", String.valueOf(ecno));
                }
            } else {
                if (obj != null) {
                    obj.put("snr", "0");
                }
            }

            if (mView_HealthStatus.lteRSRP == null || Integer.parseInt(mView_HealthStatus.lteRSRP) == Integer.MAX_VALUE) {
                if (obj != null) {
                    obj.put("rsrp", "0");
                }
            } else if (obj != null) {
                obj.put("rsrp", mView_HealthStatus.lteRSRP);
            }
            if (mView_HealthStatus.second_cellInstance != null && mView_HealthStatus.second_cellInstance.equalsIgnoreCase("wcdma")) {
                HashMap<String, String> hmap = new HashMap<>();
                hmap.put("rsrp2", "" + mView_HealthStatus.second_rscp_3G);
                hmap.put("cellid2", "" + mView_HealthStatus.second_Cid);
                hmap.put("rsrp1", "" + mView_HealthStatus.second_Rsrp);
                hmap.put("cellid1", mView_HealthStatus.Cid);
                list.add(hmap);

            } else {
                HashMap<String, String> hmap = new HashMap<>();
                hmap.put("rsrp2", "NA");
                hmap.put("cellid2", "NA");
                hmap.put("rsrp1", "" + mView_HealthStatus.second_Rsrp);
                hmap.put("cellid1", mView_HealthStatus.Cid);
                list.add(hmap);

            }

        } else if (mView_HealthStatus.currentInstance.equalsIgnoreCase("gsm")) {

            if (mView_HealthStatus.lteta == null || Integer.parseInt(mView_HealthStatus.lteta) == Integer.MAX_VALUE) {
                //   Toast.makeText(getActivity(), "ta value is "+mView_HealthStatus.lteta, Toast.LENGTH_SHORT).show();
                if (obj != null) {
                    obj.put("ta", " 0 ");
                }
            } else {
                //   Toast.makeText(getActivity(), "ta value is " + mView_HealthStatus.lteta, Toast.LENGTH_SHORT).show();
                if (obj != null) {
                    obj.put("ta", mView_HealthStatus.lteta);
                }
                if (mView_HealthStatus.ARFCN == null || Integer.parseInt(mView_HealthStatus.ARFCN) == Integer.MAX_VALUE) {
                    if (obj != null) {
                        obj.put("arfcn", " 0 ");
                    }
                } else if (obj != null) {
                    obj.put("arfcn", mView_HealthStatus.ARFCN);
                }

                int rxl = MyPhoneStateListener.getRxLev();
                if (obj != null) {
                    obj.put("rsrp", rxl);
                }

                if (mView_HealthStatus.second_cellInstance != null && mView_HealthStatus.second_cellInstance.equalsIgnoreCase("gsm")) {


                    HashMap<String, String> hmap = new HashMap<>();
                    hmap.put("rsrp2", "" + mView_HealthStatus.second_rxLev);
                    hmap.put("cellid2", "" + mView_HealthStatus.second_Cid);
                    hmap.put("rsrp1", "" + rxl);
                    hmap.put("cellid1", mView_HealthStatus.Cid);
                    list.add(hmap);

                } else {
                    HashMap<String, String> hmap = new HashMap<>();
                    hmap.put("rsrp2", "NA");
                    hmap.put("cellid2", "NA");
                    hmap.put("rsrp1", "" + rxl);
                    hmap.put("cellid1", mView_HealthStatus.Cid);
                    list.add(hmap);
                }
            }


        }

        JSONObject neighbourcellmainobj = Neighbour_cells_info.sendRequest();
        Log.i(TAG, "neigh cell info is " + neighbourcellmainobj);

        JSONArray neighbourcellarray = neighbourcellmainobj.optJSONArray("neighbourCellInformation");

        String type = neighbourcellmainobj.optString("type");
        JSONArray finalneighbourarray = new JSONArray();

        for (int i = 0; i < neighbourcellarray.length(); i++) {
            JSONObject finalneighbourobj = new JSONObject();
            JSONObject jsonObject = neighbourcellarray.optJSONObject(i);
            finalneighbourobj.put("mcc", neighbourcellmainobj.optString("MCC"));
            finalneighbourobj.put("mnc", neighbourcellmainobj.optString("MNC"));

            String signalstrength = jsonObject.optString("signalstrength");
            finalneighbourobj.put("type", type);
            finalneighbourobj.put("signalStrength", signalstrength);
            if (type.equalsIgnoreCase("LTE")) {
                finalneighbourobj.put("cellId", finalneighbourobj.optString("4G_CI"));

            } else if (type.equalsIgnoreCase("Wcdma")) {
                finalneighbourobj.put("cellId", finalneighbourobj.optString("3G_CID"));

            } else if (type.equalsIgnoreCase("Gsm")) {
                finalneighbourobj.put("cellId", finalneighbourobj.optString("G_CID"));


            } else {
                finalneighbourobj.put("cellId", "NA");

            }

            finalneighbourobj.put("isRegistered", " ");
            finalneighbourarray.put(finalneighbourobj);


        }
        if (obj != null) {
            obj.put("neighbourCellInformation", finalneighbourarray);
        }
        if (Utils.checkifavailable(mView_HealthStatus.currentInstance)) {
            if (obj != null) {
                obj.put("ratType", mView_HealthStatus.currentInstance);
            }
        } else {
            if (obj != null) {
                obj.put("ratType", "NA");
            }
        }
        return list;

    }


/*    private static int getcellid(Context ctx) {
    int cellid=0;
        final TelephonyManager telephony = (TelephonyManager)ctx. getSystemService(Context.TELEPHONY_SERVICE);
        if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
            @SuppressLint("MissingPermission") final GsmCellLocation location = (GsmCellLocation) telephony.getCellLocation();
            if (location != null) {
              cellid= location.getCid();
            }
        }
        return cellid;
    }*/

    private static String getcarriername(Context context) {

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = manager.getNetworkOperatorName();
        return carrierName;
    }


    private static String getwifistate() {
        String state = "off";
        ConnectivityManager connManager = (ConnectivityManager) Mview.fapps_ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            // Do whatever
            state = "on";
        }
        return state;
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private static String getSingleImsi(Context context) {


        String imsi = " ";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//
        //      boolean isSIM1Ready = getSIMStateBySlot(context, "getSimState", 0);

        try {
            Method getSubId = TelephonyManager.class.getMethod("getSubscriberId", int.class);
            SubscriptionManager sm = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            imsi = (String) getSubId.invoke(tm, sm.getActiveSubscriptionInfoForSimSlotIndex(0).getSubscriptionId()); // Sim slot 1 IMSI
            return imsi;
        } catch (IllegalAccessException e) {


            e.printStackTrace();
        } catch (InvocationTargetException e) {


            e.printStackTrace();
        } catch (NoSuchMethodException e) {


            e.printStackTrace();
        } catch (Exception e) {


            e.printStackTrace();
        }
        return imsi;


    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private static String getSecondIMSI(Context context) {
        String imsi = " ";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Method getSubId = TelephonyManager.class.getMethod("getSubscriberId", int.class);
            SubscriptionManager sm = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            imsi = (String) getSubId.invoke(tm, sm.getActiveSubscriptionInfoForSimSlotIndex(1).getSubscriptionId()); // Sim slot 2 IMSI
            //Config.callEventUFfordummy( context,   "let's check imsi 8 and imsi is  "+imsi,"ugc","TE");
            System.out.println("Second imsi is " + imsi);
            return imsi;
        } catch (IllegalAccessException e) {
            //Config.callEventUFfordummy( context,   "let's check imsi 9 illegal no such method ","ugc","TE");

            e.printStackTrace();
        } catch (InvocationTargetException e) {
            //Config.callEventUFfordummy( context,   "let's check imsi 10 invocation no such method ","ugc","TE");

            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            //Config.callEventUFfordummy( context,   "let's check imsi 11  no such method ","ugc","TE");

            e.printStackTrace();
        } catch (Exception e) {
            //Config.callEventUFfordummy( context,   "let's check imsi 12 "+e.toString(),"ugc","TE");

            e.printStackTrace();
        }
        return imsi;


    }


    private static boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID) {
        boolean isReady = false;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimState = telephonyClass.getMethod(predictedMethodName, parameter);
            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimState.invoke(telephony, obParameter);

            if (ob_phone != null) {
                int simState = Integer.parseInt(ob_phone.toString());
                String sim2_STATE = simState(simState);
                System.out.println("sim state info " + sim2_STATE);
                if ((simState != TelephonyManager.SIM_STATE_ABSENT) && (simState != TelephonyManager.SIM_STATE_UNKNOWN)) {
                    isReady = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //throw new ITgerMethodNotFoundException(predictedMethodName);
        }

        return isReady;
    }

    private static String simState(int simState) {
        switch (simState) {
            case 0:
                return "UNKNOWN";
            case 1:
                return "ABSENT";
            case 2:
                return "REQUIRED";
            case 3:
                return "PUK_REQUIRED";
            case 4:
                return "NETWORK_LOCKED";
            case 5:
                return "READY";
            case 6:
                return "NOT_READY";
            case 7:
                return "PERM_DISABLED";
            case 8:
                return "CARD_IO_ERROR";
        }
        return "??? " + simState;
    }

    private static boolean isPhoneDualSim(Context context) {
        {
        /*TelephonyManager operator = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(context);

        boolean isDualSIM = telephonyInfo.isDualSIM();
return isDualSIM;*/

            String[] simStatusMethodNames = {"getSimStateGemini", "getSimState"};

            boolean first = false, second = false;

            for (String methodName : simStatusMethodNames) {
                // try with sim 0 first
                try {
                    first = getSIMStateBySlot(context, methodName, 0);
                    // no exception thrown, means method exists
                    second = getSIMStateBySlot(context, methodName, 1);
                    return first && second;
                } catch (Exception e) {
                    // method does not exist, nothing to do but test the next
                    e.printStackTrace();
                }
            }
            return false;

        }
    }

    private static JSONObject getappplatform() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("apiVersion", Build.VERSION.SDK_INT);
            obj.put("appVersion", "1.0.0.0");
            obj.put("brand", Build.BRAND);
            obj.put("buildVersion", getBuildVersion());
            obj.put("manufacturer", getManufacturerName());
            obj.put("model", getDeviceModel());
            obj.put("product", getProductName());
            obj.put("userAgent", "Android");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return obj;
    }

    private static String getProductName() {
        String manufacturer = Build.PRODUCT;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String getBuildVersion() {
        String manufacturer = Build.VERSION.RELEASE;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String getManufacturerName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String getDeviceModel() {
        String manufacturer = Build.MODEL;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private static String getBatteryPercentage() {


        float bat = 0f;
        String battery = "NA";

        Intent batteryIntent = Mview.fapps_ctx.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (batteryIntent != null) {
            int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            // Error checking that probably isn't needed but I added just in case.
            if (level == -1 || scale == -1) {
                bat = 50.0f;
            }

            bat = ((float) level / (float) scale) * 100.0f;

            battery = String.format("%.0f", bat) + "%";

        }
        return battery;
    }

    private static JSONObject webservertest(String url, int timeout) throws JSONException {
        // TODO Auto-generated method stub
        JSONObject jsonObject = new JSONObject();

        long startTS = System.currentTimeMillis();

        url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            long reachTS = System.currentTimeMillis();
            long ss = reachTS - startTS;
            long rseconds = TimeUnit.MILLISECONDS.toSeconds(ss);
            String reachability_time = String.valueOf(rseconds);


            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            long endTS = System.currentTimeMillis();

            long timetaken = endTS - startTS;
//    	long seconds= TimeUnit.MILLISECONDS.toSeconds(timetaken);

            String time_taken = String.valueOf(timetaken);


            jsonObject.put("domain", url);
            jsonObject.put("working", "yes");
            jsonObject.put("response_time", time_taken);
            jsonObject.put("response_time_unit", "ms");
            jsonObject.put("reachability_time", reachability_time);
            jsonObject.put("reachability_time_unit", "ms");


//        return (200 <= responseCode && responseCode <= 399);
        } catch (IOException exception) {
//        return false;
        }
        Log.i(TAG, "Web test json is " + jsonObject);
        return jsonObject;


    }

    public static void sendUpdateReq() {
        JSONObject obj = new JSONObject();
        try {

            obj.put("msg", "ud");//update_data

            obj.put("os_version", "22");
            obj.put("device_info", " ");
            obj.put("interface", "CLI");
            obj.put("prod", product);
            obj.put("msisdn", "9999999999");
            obj.put("ver", version);


            obj.put("imsi", Utils.getImsi());
            obj.put("phone_imsi", Utils.getImsi());
            // getserialnumber(obj);

            obj.put("latitude", "0");
            obj.put("longitude", "0");

            obj.put("lat", "0");
            obj.put("lon", "0");

            obj.put("lacid", "0");
            obj.put("pubid", "0");
            obj.put("clickid", "0");
            obj.put("cellid", "0");

            obj.put("apn", " ");
            obj.put("apn_type", Utils.getApnType());

            String imei = "";
            String simOperatorName = "";

            obj.put("operatorname", " ");

            obj.put("ip", Utils.getIP());
            obj.put("port", "9999");

            obj.put("country_code", "IN");

            obj.put("androidsdk", "29");


            AllInOneAsyncTaskForNetwork async =
                    new AllInOneAsyncTaskForNetwork(fapps_ctx,
                            AllInOneAsyncTaskForNetwork.AsyncTaskPurpose.UD);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, obj.toString());
            }


        } catch (Exception e) {

        }


    }

//    public interface CallbackReceiver {
//        public void receiveData(JSONObject result,ArrayList<HashMap<String,String>>list);
//
//    }
//    public static class BackgroundTask extends AsyncTask<String, Integer, String >{
//
//        JSONObject jsonObjectis;
//        JSONObject  object;
//        int progress;
//        CallbackReceiver callbackReceiver;
//        public BackgroundTask(JSONObject jsonObject,CallbackReceiver receiver) {
//            jsonObjectis=jsonObject;
//            callbackReceiver=receiver;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
////            String data=getDatafromMemoryCard();
//            String data= null;
//            try {
////                data = downloadspeedtest(jsonObjectis);
//            }  catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return data;  // return data you want to use here
//        }
//        @Override
//        protected void onPostExecute(String  result) {  // result is data returned by doInBackground
//
//            super.onPostExecute(result);
//            try {
//
//
//                ArrayList<HashMap<String,String>>list= getothernetworkparams(jsonObjectis,Mview.fapps_ctx);
//
//               /* if (!list.isEmpty())
//                {
//                    object.put("rsrp1", list.get(0).get("tac"));
//                    object.put("rsrp1", list.get(0).get("rsrp1"));
//                    object.put("cellid1",list.get(0).get("cellid1") );
//                    object.put("rsrp2", list.get(0).get("rsrp1"));
//                    object.put("cellid2",list.get(0).get("rsrp2"));
//
//
//                }
//                else
//                {
//
//                    object.put("rsrp1", "NA");
//                    object.put("cellid1","NA" );
//                    object.put("rsrp2","NA");
//                    object.put("cellid2","NA");
//
//                }*/
//
//
//
//
//
//
//
//if (callbackReceiver!=null)
//{
//    callbackReceiver.receiveData(jsonObjectis,list);
//}
//
//
//
//
////            obj.put("neighbourCellInformation",finalarray);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

    public static class BackgroundTask extends AsyncTask<String, Integer, JSONObject> {

        String msisdn;

        public BackgroundTask(String siNumber) {
            msisdn = siNumber;


        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("batteryLevel", Utils.getBattery(fapps_ctx));
                jsonObject.put("carrier", mView_HealthStatus.prim_carrierName);
                jsonObject.put("imei", Utils.getIMEI());
                jsonObject.put("imsi", Utils.getImsi());
                jsonObject.put("mcc", mView_HealthStatus.prim_mcc);
                jsonObject.put("mnc", mView_HealthStatus.prim_mnc);
                jsonObject.put("msisdn", msisdn);
                if (Utils.checkifavailable(mView_HealthStatus.currentInstance) && mView_HealthStatus.currentInstance.equalsIgnoreCase("lte")) {

                    jsonObject.put("preferredNetworkMode", "4G");


                } else if (Utils.checkifavailable(mView_HealthStatus.currentInstance) && mView_HealthStatus.currentInstance.equalsIgnoreCase("wcdma")) {

                    jsonObject.put("preferredNetworkMode", "3G");
                } else if (Utils.checkifavailable(mView_HealthStatus.currentInstance) && mView_HealthStatus.currentInstance.equalsIgnoreCase("gsm")) {

                    jsonObject.put("preferredNetworkMode", "2G");
                } else if (Utils.checkifavailable(mView_HealthStatus.currentInstance) && mView_HealthStatus.currentInstance.equalsIgnoreCase("nr")) {

                    jsonObject.put("preferredNetworkMode", "5G");
                } else {

                    jsonObject.put("preferredNetworkMode", "NA");
                }


                jsonObject.put("testCompletionTime", Utils.getDateTime());
                jsonObject.put("userLocPosition", " ");
                jsonObject.put("userLocSubPosition", " ");
                jsonObject.put("appPlatform", getappplatform());
                jsonObject.put("dualSimSettings", getdualsim());
//                jsonObject.put("speedTest", speedTest());
                jsonObject.put("webTest", webtestnew());



           /*     finaljson=getVideoKPI_thanks(jsonObject);


                sendtoserver(finaljson,"sdk_new",siNumber,Utils.getImsi());

*/

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject obj) {
            Log.i(TAG, "Reached in on post execute " + obj);
            getVideoKPI_thanks(obj, msisdn);
        }
    }

        public static class BackgroundTasktgetvalues extends AsyncTask<String, Integer, JSONObject> {


            String msisdn;
            String latitude;
            String longitude;
            JSONObject object;
            JSONObject object_thanks;


            public BackgroundTasktgetvalues(Context ctx, String msisdn_, String latitude_, String longitude_) {
                Log.i(TAG, "Going to run test");
                msisdn = msisdn_;
                latitude = latitude_;
                longitude = longitude_;
                fapps_ctx = ctx;


            }

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
                object = new JSONObject();
                Toast.makeText(fapps_ctx,"Starting to collect info",Toast.LENGTH_SHORT).show();




            }


            @Override
            protected JSONObject doInBackground(String... params) {


                JSONObject data = new JSONObject();
                object_thanks  = new JSONObject();
                try {
                    object_thanks.put("sourceTimeStamp",Utils.getDateTime());
                    Utils.appendLog("Timestamp is "+Utils.getDateTime());
                    object_thanks.put("msisdn",msisdn );
                    if (ListenService.gps!=null)
                    {
                        object_thanks.put("user_latitude", ListenService.gps.getLatitude() + "");
                        object_thanks.put("user_longitude", ListenService.gps.getLongitude() + "");
                        object_thanks.put("gps_accuracy", ListenService.gps.getLocation().getAccuracy() + "m");
                        object_thanks.put("altitude", String.format("%.2f", ListenService.gps.getAltitude()) + "m");

                    }
                    else
                    {
                        object_thanks.put("user_latitude", "NA");
                        object_thanks.put("user_longitude", "NA");
                        object_thanks.put("gps_accuracy", "NA");
                        object_thanks.put("altitude", "NA");


                    }
                    object_thanks.put("device_unique_id",  Utils.getDeviceID());
                    Utils.appendLog("Device_unique_id is "+Utils.getDeviceID());

                    object_thanks.put("apn",  Utils.apnname());
                    Utils.appendLog("APN is "+Utils.apnname());

                    object_thanks.put("ip",  Utils.getIP());
                    Utils.appendLog("IP is "+Utils.getIP());


                    object_thanks.put("country_code",  Utils.getCountrycode());
                    Utils.appendLog("Country code is "+Utils.getCountrycode());

                    object_thanks.put("wifistate",  getwifistate());
                    Utils.appendLog("wifistate is "+getwifistate());

                    object_thanks.put("airplane",  Utils.isAirplaneModeOn());
                    Utils.appendLog("Airplane is "+Utils.isAirplaneModeOn());


                    object_thanks.put("voc", Utils.getvoc());
                    object_thanks.put("latitude", latitude);
                    object_thanks.put("longitude", longitude);
                    object_thanks.put("appPlatform", collecthandsetinfo());
                    Utils.appendLog("app platform is "+collecthandsetinfo());


                    object_thanks.put("appVersion", version);

                    object_thanks.put("apn_type", Utils.getApnType());
                    Utils.appendLog("apn type is "+Utils.getApnType());



                    object_thanks.put("time",Utils.getDateTime());
                    object_thanks.put("tag","SDK - Airtel Data Status");
                    object_thanks.put("carrier1",mView_HealthStatus.prim_carrierName);
                    Utils.appendLog("Carrier  is "+mView_HealthStatus.prim_carrierName);

                    object_thanks.put("sim1_imsi", Utils.getPhoneImsi(Mview.fapps_ctx));

                    object_thanks.put("sim1_mnc",mView_HealthStatus.prim_mnc);
                    object_thanks.put("sim1_mcc",mView_HealthStatus.prim_mcc);
                    object_thanks.put("sim1_datasim",mView_HealthStatus.prim_Slot);
                    if (Utils.checkifavailable(mView_HealthStatus.currentInstance) && mView_HealthStatus.currentInstance.equalsIgnoreCase("lte")) {
                        object_thanks.put("sim1_is4G", "Yes");
                        object_thanks.put("sim1_volte", "Yes");
                        object_thanks.put("sim1_tech", "4G");
                        object_thanks.put("sim1_preferredNetworkMode", "4G");


                    } else if (Utils.checkifavailable(mView_HealthStatus.currentInstance) && mView_HealthStatus.currentInstance.equalsIgnoreCase("wcdma")) {
                        object_thanks.put("sim1_is4G", "No");
                        object_thanks.put("sim1_volte", "No");
                        object_thanks.put("sim1_tech", "3G");
                        object_thanks.put("sim1_preferredNetworkMode", "3G");
                    } else if (Utils.checkifavailable(mView_HealthStatus.currentInstance) && mView_HealthStatus.currentInstance.equalsIgnoreCase("gsm")) {
                        object_thanks.put("sim1_is4G", "No");
                        object_thanks.put("sim1_volte", "No");
                        object_thanks.put("sim1_tech", "2G");
                        object_thanks.put("sim1_preferredNetworkMode", "2G");
                    } else if (Utils.checkifavailable(mView_HealthStatus.currentInstance) && mView_HealthStatus.currentInstance.equalsIgnoreCase("nr")) {
                        object_thanks.put("sim1_is4G", "No");
                        object_thanks.put("sim1_volte", "No");
                        object_thanks.put("sim1_tech", "5G");
                        object_thanks.put("sim1_preferredNetworkMode", "5G");
                    } else {
                        object_thanks.put("sim1_is4G", "No");
                        object_thanks.put("sim1_volte", "No");
                        object_thanks.put("sim1_tech", "NA");
                        object_thanks.put("sim1_preferredNetworkMode", "NA");
                    }



                    if (Utils.checkifavailable(mView_HealthStatus.second_cellInstance) && mView_HealthStatus.second_cellInstance.equalsIgnoreCase("lte")) {
                        object_thanks.put("sim2_is4G", "Yes");
                        object_thanks.put("sim2_volte", "Yes");
                        object_thanks.put("sim2_tech", "4G");
                        object_thanks.put("sim2_preferredNetworkMode", "4G");


                    } else if (Utils.checkifavailable(mView_HealthStatus.second_cellInstance) && mView_HealthStatus.second_cellInstance.equalsIgnoreCase("wcdma")) {
                        object_thanks.put("sim2_is4G", "No");
                        object_thanks.put("sim2_volte", "No");
                        object_thanks.put("sim2_tech", "3G");
                        object_thanks.put("sim2_preferredNetworkMode", "3G");
                    } else if (Utils.checkifavailable(mView_HealthStatus.second_cellInstance) && mView_HealthStatus.second_cellInstance.equalsIgnoreCase("gsm")) {
                        object_thanks.put("sim2_is4G", "No");
                        object_thanks.put("sim2_volte", "No");
                        object_thanks.put("sim2_tech", "2G");
                        object_thanks.put("sim2_preferredNetworkMode", "2G");
                    }
                    else if (Utils.checkifavailable(mView_HealthStatus.second_cellInstance) && mView_HealthStatus.second_cellInstance.equalsIgnoreCase("nr")) {
                        object_thanks.put("sim2_is4G", "No");
                        object_thanks.put("sim2_volte", "No");
                        object_thanks.put("sim2_tech", "5G");
                        object_thanks.put("sim2_preferredNetworkMode", "5G");
                    }


                    else {
                        object_thanks.put("sim2_is4G", nosim);
                        object_thanks.put("sim2_volte", nosim);
                        object_thanks.put("sim2_tech", nosim);
                        object_thanks.put("sim2_preferredNetworkMode", nosim);
                    }
                    if (Utils.checkifavailable(mView_HealthStatus.second_cellInstance))
                    {
                        object_thanks.put("sim2_datasim",mView_HealthStatus.sec_slot);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                            object_thanks.put("sim2_imsi", SimInfoUtility.getSecondIMSI(fapps_ctx));
                        }
                        else
                        {
                            object_thanks.put("sim2_imsi", "NA");

                        }
                        object_thanks.put("carrier2", mView_HealthStatus.sec_carrierName);
                        object_thanks.put("sim2_rsrq", mView_HealthStatus.second_rsrq);
                        object_thanks.put("sim2_mnc", mView_HealthStatus.sec_mnc);
                        object_thanks.put("sim2_mcc", mView_HealthStatus.sec_mcc);
                        object_thanks.put("cellid2", mView_HealthStatus.second_Cid);






                    }
                    else
                    {
                        object_thanks.put("sim2_datasim", nosim);
                        object_thanks.put("sim2_imsi", nosim);
                        object_thanks.put("carrier2", nosim);
                        object_thanks.put("sim2_rsrq", nosim);
                        object_thanks.put("sim2_mnc",nosim);
                        object_thanks.put("sim2_mcc", nosim);
                        object_thanks.put("cellid2", nosim);

                    }
                    object_thanks.put("cellid1", mView_HealthStatus.Cid);







                    data.put("user_info", collectuserinformation(msisdn, latitude, longitude));

                    data.put("handset_info", collecthandsetinfo());



                    data.put("sdk_app_info", collectsdkappinfo());
                    data.put("sim1_info", getsim1info());
                    data.put("sim2_info", getsim2info());
                   JSONObject sim1servingobj= getsim1servingcellinfo();
                    Utils.appendLog("Serving cell info  is "+getsim1servingcellinfo());


                    data.put("sim1_servingcell_info",sim1servingobj );
                    object_thanks.put("cellid1", sim1servingobj.optString("cellid"));
                    object_thanks.put("Lcellid", sim1servingobj.optString("Lcellid"));
                    object_thanks.put("ratType", sim1servingobj.optString("ratType"));
                    object_thanks.put("enb",  sim1servingobj.optString("enb"));
                    object_thanks.put("snr", sim1servingobj.optString("snr"));
                    object_thanks.put("earfcn", sim1servingobj.optString("earfcn"));
                    object_thanks.put("rsrp", sim1servingobj.optString("rsrp"));
                    object_thanks.put("pci", sim1servingobj.optString("pci"));
                    object_thanks.put("ta", sim1servingobj.optString("ta"));
                    object_thanks.put("cqi", sim1servingobj.optString("cqi"));
                    object_thanks.put("signalStrength", sim1servingobj.optString("signalStrength"));
                    object_thanks.put("tac", sim1servingobj.optString("tac"));
                    JSONObject sim2servingobj=getsim2servingcellinfo();

                    data.put("sim2_servingcell_info", sim2servingobj);
                    JSONObject neighbur_json=getsim1neighbourcellinfo();
                    Utils.appendLog("Neighbour cell info  is "+getsim1neighbourcellinfo());

                    data.put("sim1_neihbour_cell_info",neighbur_json);
                    JSONObject downobj=getFinaldownload();
                    Utils.appendLog("Download  spped is "+downobj);

                    JSONObject upobj=getFinalupload();
                    Utils.appendLog("Upload  spped is "+upobj);

                    data.put("download_speed", downobj);
                    data.put("upload_speed", upobj);
                    JSONObject webobj= callping("www.facebook.com");
                    Utils.appendLog("web test  spped is "+webobj);

                    data.put("web_test", webobj);
                    if (Utils.checkifavailable(mView_HealthStatus.prim_carrierName)&&mView_HealthStatus.prim_carrierName.startsWith("airtel")) {
                        object_thanks.put("speedTest", speedTest(upobj, downobj, neighbur_json, sim1servingobj));
                    }
                    else
                    {
                        object_thanks.put("speedTest", speedTest(upobj, downobj, neighbur_json, sim2servingobj));

                    }
                    object_thanks.put("web_test",webobj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "Final Json is " + data);
                return data;  // return data you want to use here
            }

  /*      private JSONObject getVideoTest() {
            obj=new JSONObject();
            new WebViewHelper(fapps_ctx, getLatency("www.youtube.com")+" ms",getpacketLoss("www.youtube.com")+" ms").
                    loadUrl("file:///android_asset/youtube.html" ,new WebViewHelper.JsonResultInterface() {
                        @Override
                        public void sendJsonResult(JSONObject jsonObject)
                        {

                            Log.i(TAG, "video KPI LIST is " + jsonObject);
                            obj = jsonObject;
                        }
                    });
return obj;

        }*/

            private JSONArray speedtest() {
                JSONArray jsonArray = new JSONArray();
                JSONObject obj = new JSONObject();
                try {
                    obj.put("download", getFinaldownload());
                    obj.put("upload", getFinalupload());

                    jsonArray.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonArray;
            }


            @Override
            protected void onPostExecute(JSONObject obj) {
                Log.i(TAG, "Reached in on post execute " + obj);
                getVideoKPI(obj, msisdn,object_thanks);
        /*    getVideoKPI(obj);
            try {
                obj.put("video_test", getVideoKPI());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendtoserver(obj, "sdk_app", msisdn, SimInfoUtility.getImsi(fapps_ctx));
*/


//            new Speedtest(fapps_ctx,msisdn,latitude_,longitude_).execute();

            }


        }

        public static JSONObject getsim1neighbourcellinfo() throws JSONException {
            JSONObject neighbourcellmainobj = Neighbour_cells_info.sendRequest();

            JSONArray finalobj = new JSONArray();

            Log.i(TAG, "neigh cell info is " + neighbourcellmainobj);

            JSONArray neighbourcellarray = neighbourcellmainobj.optJSONArray("neighbourCellInformation");

            String type = neighbourcellmainobj.optString("type");
            Log.i(TAG, "Type is " + type);
            JSONObject obj = new JSONObject();


            for (int i = 0; i < neighbourcellarray.length(); i++) {
                JSONObject jsonObject = neighbourcellarray.optJSONObject(i);

                obj.put("type", type);
                if (type != null && type.equalsIgnoreCase("LTE")) {


                    obj.put("cellId", jsonObject.optString("4G_CI"));
                    obj.put("pci", jsonObject.optString("4G_PCI"));
                    obj.put("channel", jsonObject.optString("4G_EARFCN"));
                    obj.put("ta", jsonObject.optString("4G_TA"));
                    obj.put("cqi", jsonObject.optString("4G_CQI"));
                    obj.put("rsrq", jsonObject.optString("4G_RSRQ"));
                    obj.put("rsrp", jsonObject.optString("4G_RSRP"));
                    obj.put("tac", jsonObject.optString("4G_TAC"));
                    obj.put("sinr", jsonObject.optString("4G_SINR"));
                    obj.put("enb", jsonObject.optString("4G_ENB"));
                    obj.put("mcc", jsonObject.optString("4G_MCC"));
                    obj.put("mnc", jsonObject.optString("4G_MNC"));
                    obj.put("isRegistered", jsonObject.optString("isregistered"));


                } else if (type.equalsIgnoreCase("Wcdma")) {
                    obj.put("mcc", jsonObject.optString("3G_MCC"));
                    obj.put("mnc", jsonObject.optString("3G_MNC"));
                    obj.put("cellId", jsonObject.optString("3G_CID"));
                    obj.put("pci", jsonObject.optString("3G_PSC"));
                    obj.put("cqi", jsonObject.optString("3G_CQI"));
                    obj.put("isRegistered", jsonObject.optString("isRegistered"));
                    obj.put("rsrq", jsonObject.optString("signalstrength"));


                } else if (type.equalsIgnoreCase("Gsm")) {
                    obj.put("cellId", jsonObject.optString("G_CID"));


                } else {
                    obj.put("cellId", "NA");

                }
                finalobj.put(obj);


            }


            return obj;
        }

        public static JSONArray getsim2neighbourcellinfo() throws JSONException {
            JSONArray finalobj = new JSONArray();
            if (mView_HealthStatus.second_cellInstance != null) {
                JSONObject neighbourcellmainobj = Neighbour_cells_info.sendRequest();


                Log.i(TAG, "neigh cell info is " + neighbourcellmainobj);

                JSONArray neighbourcellarray = neighbourcellmainobj.optJSONArray("neighbourCellInformation");

                String type = neighbourcellmainobj.optString("type");
                Log.i(TAG, "Type is " + type);

                JSONObject obj = new JSONObject();
                for (int i = 0; i < neighbourcellarray.length(); i++) {
//            JSONObject obj = new JSONObject();
                    JSONObject jsonObject = neighbourcellarray.optJSONObject(i);

                    obj.put("type", type);


                }
                obj.put("cellId", "NA");
                obj.put("pci", "NA");
                obj.put("channel", "NA");
                obj.put("ta", "NA");
                obj.put("cqi", "NA");
                obj.put("rsrq", "NA");
                obj.put("rsrp", "NA");
                obj.put("tac", "NA");
                obj.put("sinr", "NA");
                obj.put("enb", "NA");
                obj.put("mcc", "NA");
                obj.put("mnc", "NA");
                obj.put("isRegistered", "NA");
                finalobj.put(obj);


            } else {
                JSONObject obj1 = new JSONObject();
                obj1.put("type", nosim);
                obj1.put("cellId", nosim);
                obj1.put("pci", nosim);
                obj1.put("channel", nosim);
                obj1.put("ta", nosim);
                obj1.put("cqi", nosim);
                obj1.put("rsrq", nosim);
                obj1.put("rsrp", nosim);
                obj1.put("tac", nosim);
                obj1.put("sinr", nosim);
                obj1.put("enb", nosim);
                obj1.put("mcc", nosim);
                obj1.put("mnc", nosim);
                obj1.put("isRegistered", nosim);
                finalobj.put(obj1);
            }


            return finalobj;
        }

        public static JSONObject getsim1servingcellinfo() {
            ArrayList<HashMap<String, String>> array = Network_Params.getservingcell1info();
            Log.i(TAG, "getsim1servingcellinfo is " + array);
            JSONObject finalobj = new JSONObject();
            for (int i = 0; i < array.size(); i++) {
                HashMap<String, String> hmap = new HashMap<>();
                hmap = array.get(i);
                String type = hmap.get("type");
                String rsrp = hmap.get("rsrp");
                String mcc = hmap.get("mcc");
                String mnc = hmap.get("mnc");
                String cqi = hmap.get("cqi");
                String cellid = hmap.get("cellid");
                String pci = hmap.get("pci");
                String tac = hmap.get("tac");
                String earfcn = hmap.get("earfcn");
                String enb = hmap.get("enb");
                String ta = hmap.get("ta");
                String snr = hmap.get("snr");
                String rsrq = hmap.get("rsrq");
                String localcellid = hmap.get("localcellid");

                try {
                    finalobj.put("cellid", cellid);
                    finalobj.put("Lcellid", localcellid);

                    finalobj.put("ratType", type);
//                    finalobj.put("NodeBId", " ");
                    finalobj.put("enb", enb);


                    finalobj.put("snr", snr);
                    finalobj.put("earfcn", earfcn);
                    finalobj.put("rsrp", rsrp);
                    finalobj.put("pci", pci);
                    finalobj.put("ta", ta);
                    finalobj.put("cqi", cqi);
                    finalobj.put("signalStrength", rsrq);

                    finalobj.put("tac", tac);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            return finalobj;
        }

        public static JSONObject getsim2servingcellinfo() {
            ArrayList<HashMap<String, String>> array = Network_Params.getservingcell2info();
            Log.i(TAG, "getsim1servingcellinfo is " + array);


            JSONObject finalobj = new JSONObject();

            if (mView_HealthStatus.second_cellInstance != null) {

                for (int i = 0; i < array.size(); i++) {
                    HashMap<String, String> hmap = new HashMap<>();

                    hmap = array.get(i);


                    String type = hmap.get("type");
                    String rsrp = hmap.get("rsrp");
                    String mcc = hmap.get("mcc");
                    String mnc = hmap.get("mnc");
                    String cqi = hmap.get("cqi");
                    String cellid = hmap.get("cellid");
                    String pci = hmap.get("pci");
                    String tac = hmap.get("tac");
                    String earfcn = hmap.get("earfcn");
                    String enb = hmap.get("enb");
                    String ta = hmap.get("ta");
                    String snr = hmap.get("snr");
                    String localcellid = hmap.get("localcellid");

                    try {
                        finalobj.put("cellid", cellid);
                        finalobj.put("Lcellid", localcellid);

                        finalobj.put("ratType", type);

//                    finalobj.put("NodeBId", " ");
                        finalobj.put("enb", enb);


                        finalobj.put("snr", snr);

                        finalobj.put("earfcn", earfcn);

                        finalobj.put("rsrp", rsrp);


                        finalobj.put("pci", pci);


                        finalobj.put("ta", ta);
                        finalobj.put("cqi", cqi);

                        finalobj.put("signalStrength", rsrp);


                        finalobj.put("tac", tac);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            } else {
                try {
                    finalobj.put("cellid", nosim);
                    finalobj.put("Lcellid", nosim);
                    finalobj.put("ratType", nosim);
//                finalobj.put("NodeBId", nosim);
                    finalobj.put("enb", nosim);
                    finalobj.put("snr", nosim);
                    finalobj.put("earfcn", nosim);
                    finalobj.put("rsrp", nosim);
                    finalobj.put("pci", nosim);
                    finalobj.put("ta", nosim);
                    finalobj.put("cqi", nosim);
                    finalobj.put("signalStrength", nosim);
                    finalobj.put("tac", nosim);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            return finalobj;
        }

        public static JSONObject getsim1info() {
            JSONArray array = Network_Params.getnetwork_params();

            Log.i(TAG, "Array is " + array);
            JSONObject finalobj = new JSONObject();

            String earfcn = " ";

            for (int i = 0; i < array.length(); i++) {
                JSONObject object1 = array.optJSONObject(i);
                JSONArray jsonArray = object1.optJSONArray("Network_params");

                if (jsonArray != null) {

                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object = jsonArray.optJSONObject(j);
                        earfcn = object.optString("earfcn");
                    }
                }
            }
            JSONObject object = new JSONObject();
            try {
                object.put("cellid", mView_HealthStatus.Cid);
                object.put("operatorname", mView_HealthStatus.prim_carrierName);
                object.put("rsrq", mView_HealthStatus.lteRSRQ);
                object.put("imsi", Utils.getPhoneImsi(Mview.fapps_ctx));
                object.put("mnc", mView_HealthStatus.prim_mnc);
                object.put("channel", earfcn);
                object.put("mcc", mView_HealthStatus.prim_mcc);
                object.put("datasim", mView_HealthStatus.prim_Slot);

                if (Utils.checkifavailable(mView_HealthStatus.currentInstance) && mView_HealthStatus.currentInstance.equalsIgnoreCase("lte")) {
                    object.put("is4G", "Yes");
                    object.put("volte", "Yes");
                    object.put("tech", "4G");
                    object.put("preferredNetworkMode", "4G");


                } else if (Utils.checkifavailable(mView_HealthStatus.currentInstance) && mView_HealthStatus.currentInstance.equalsIgnoreCase("wcdma")) {
                    object.put("is4G", "No");
                    object.put("volte", "No");
                    object.put("tech", "3G");
                    object.put("preferredNetworkMode", "3G");
                } else if (Utils.checkifavailable(mView_HealthStatus.currentInstance) && mView_HealthStatus.currentInstance.equalsIgnoreCase("gsm")) {
                    object.put("is4G", "No");
                    object.put("volte", "No");
                    object.put("tech", "2G");
                    object.put("preferredNetworkMode", "2G");
                } else if (Utils.checkifavailable(mView_HealthStatus.currentInstance) && mView_HealthStatus.currentInstance.equalsIgnoreCase("nr")) {
                    object.put("is4G", "No");
                    object.put("volte", "No");
                    object.put("tech", "5G");
                    object.put("preferredNetworkMode", "5G");
                } else {
                    object.put("is4G", "No");
                    object.put("volte", "No");
                    object.put("tech", "NA");
                    object.put("preferredNetworkMode", "NA");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return object;
        }

        public static JSONObject getsim2info() {
            JSONArray array = Network_Params.getnetwork_params2();
            JSONObject object = new JSONObject();

            if (mView_HealthStatus.second_cellInstance != null) {

                Log.i(TAG, "Array is " + array);

                String earfcn = " ";

                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.optJSONObject(i);
                    JSONArray jsonArray = object1.optJSONArray("Network_params");

                    if (jsonArray != null) {

                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object10 = jsonArray.optJSONObject(j);
                            earfcn = object10.optString("earfcn");
                        }
                    }
                }
                try {
                    object.put("cellid", mView_HealthStatus.second_Cid);
                    object.put("operatorname", mView_HealthStatus.sec_carrierName);
                    object.put("rsrq", mView_HealthStatus.second_rsrq);
                    object.put("mnc", mView_HealthStatus.sec_mnc);
                    object.put("channel", earfcn);
                    object.put("mcc", mView_HealthStatus.sec_mcc);
                    if (mView_HealthStatus.sec_carrierName != "NA") {
                        object.put("datasim", mView_HealthStatus.sec_slot);
                        object.put("imsi", SimInfoUtility.getSecondIMSI(fapps_ctx));

                    } else {
                        object.put("datasim", "NA");
                        object.put("imsi", "NA");


                    }
                    if (Utils.checkifavailable(mView_HealthStatus.second_cellInstance) && mView_HealthStatus.second_cellInstance.equalsIgnoreCase("lte")) {
                        object.put("is4G", "Yes");
                        object.put("volte", "Yes");
                        object.put("tech", "4G");
                        object.put("preferredNetworkMode", "4G");


                    } else if (Utils.checkifavailable(mView_HealthStatus.second_cellInstance) && mView_HealthStatus.second_cellInstance.equalsIgnoreCase("wcdma")) {
                        object.put("is4G", "No");
                        object.put("volte", "No");
                        object.put("tech", "3G");
                        object.put("preferredNetworkMode", "3G");
                    } else if (Utils.checkifavailable(mView_HealthStatus.second_cellInstance) && mView_HealthStatus.second_cellInstance.equalsIgnoreCase("gsm")) {
                        object.put("is4G", "No");
                        object.put("volte", "No");
                        object.put("tech", "2G");
                        object.put("preferredNetworkMode", "2G");
                    }
                    else if (Utils.checkifavailable(mView_HealthStatus.second_cellInstance) && mView_HealthStatus.second_cellInstance.equalsIgnoreCase("nr")) {
                        object.put("is4G", "No");
                        object.put("volte", "No");
                        object.put("tech", "5G");
                        object.put("preferredNetworkMode", "5G");
                    }


                    else {
                        object.put("is4G", "No");
                        object.put("volte", "No");
                        object.put("tech", "NA");
                        object.put("preferredNetworkMode", "NA");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    object.put("datasim", nosim);
                    object.put("imsi", nosim);
                    object.put("is4G", nosim);
                    object.put("volte", nosim);
                    object.put("tech", nosim);
                    object.put("preferredNetworkMode", nosim);
                    object.put("cellid", nosim);
                    object.put("operatorname", nosim);
                    object.put("rsrq", nosim);
                    object.put("mnc", nosim);
                    object.put("channel", nosim);
                    object.put("mcc", nosim);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return object;

        }

        public static JSONObject collectsdkappinfo() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("appVersion", version);
                jsonObject.put("apn_type", Utils.getApnType());
                jsonObject.put("time", Utils.getDateTime());
                jsonObject.put("tag", "SDK - Airtel Data Status");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;
        }


        public static JSONObject collecthandsetinfo() {
            JSONObject object = new JSONObject();
            try {
                object.put("battery", getBatteryPercentage());
                object.put("imei", Utils.getIMEI());
                object.put("apiVersion", Build.VERSION.SDK_INT);
                object.put("brand", Build.BRAND);
                object.put("buildVersion", getBuildVersion());
                object.put("manufacturer", getManufacturerName());
                object.put("model", getDeviceModel());
                object.put("product", getProductName());
                object.put("userAgent", "Android");


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return object;
        }

        public static JSONObject collectuserinformation(String msisdn, String lat, String lon) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("msisdn", msisdn);
                obj.put("device_unique_id", Utils.getDeviceID());
                obj.put("msisdn", msisdn);
                obj.put("apn", Utils.apnname());

                obj.put("lat", lat);
                obj.put("lon", lon);
                obj.put("ip", Utils.getIP());
                obj.put("country_code", Utils.getCountrycode());
                obj.put("wifistate", getwifistate());
                obj.put("airplane", Utils.isAirplaneModeOn());
                obj.put("voc", Utils.getvoc());
                if (ListenService.gps.getLocation() != null) {
                    obj.put("gps_accuracy", ListenService.gps.getLocation().getAccuracy() + "m");
                    obj.put("latitude", ListenService.gps.getLatitude() + "");
                    obj.put("longitude", ListenService.gps.getLongitude() + "");
                    obj.put("altitude", String.format("%.2f", ListenService.gps.getAltitude()) + "m");
                } else {
                    obj.put("gps_accuracy", "NA");
                    obj.put("latitude", "NA");
                    obj.put("longitude", "NA");
                    obj.put("altitude", "NA");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return obj;
        }

        public static void sendIMUP(String msisdn) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("msg", "imup");
                obj.put("type", "config_change");
                obj.put("interface", "CLI");
                obj.put("prod", product);
                obj.put("msisdn", msisdn);
                obj.put("ver", version);
                obj.put("apn_type", Utils.getApnType());
                obj.put("imsi", Utils.getImsi());
                obj.put("phone_imsi", Utils.getImsi());
                obj.put("apn", Utils.apnname());
                if (ListenService.gps != null && ListenService.gps.canGetLocation()) {
                    obj.put("latitude", ListenService.gps.getLatitude() + "");
                    obj.put("longitude", ListenService.gps.getLongitude() + "");
                } else {
                    obj.put("latitude", "0");
                    obj.put("longitude", "0");
                }
                obj.put("lacid", Utils.getlacid());
                obj.put("pubid", "0");
                obj.put("clickid", "0");
                obj.put("cellid", " ");
                obj.put("operatorname", mView_HealthStatus.OperatorName);
                obj.put("ip", Utils.getIP());
                obj.put("port", "4444");
                obj.put("country_code", Utils.getCountrycode());
                obj.put("connection_type", mView_HealthStatus.connectionType);
                obj.put("network_type", mView_HealthStatus.prim_NetworkType);
                obj.put("androidsdk", Build.VERSION.SDK_INT);
                String jobjstr = obj.toString();

                AllInOneAsyncTaskForNetwork async =
                        new AllInOneAsyncTaskForNetwork(fapps_ctx,
                                AllInOneAsyncTaskForNetwork.AsyncTaskPurpose.IMUP);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, jobjstr);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private static String getLatency(String ip) {
            String latency = "NA";

            try {
                // JSONObject jsonObject=new JSONObject();
                String linenew;
                Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "ping -c 1 -q  " + ip});
                if (process != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    while ((linenew = bufferedReader.readLine()) != null) {
                        Log.i(TAG, "Latency ping output is " + linenew);
                        if (linenew.contains("min/avg")) {
                            String sublin = linenew.substring(linenew.indexOf("="));
                            sublin = sublin.trim();
                            String[] split = sublin.split("/");
                            if (split[1].length() > 0) {
                                latency = split[1];
                                Log.i(TAG, "Latency is " + latency);
                            }
                        }

                    }
                }
            } catch (Exception var19) {
                Log.i(TAG, " exception is" + var19);

            }
            return latency;
        }


        private static String getpacketLoss(String ip) {
            String packetlosss = "NA";
            try {
                // JSONObject jsonObject=new JSONObject();
                String linenew;
                Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "ping -c 5 -t 64 " + ip});
                StringBuffer output = new StringBuffer();
                if (process != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    while ((linenew = bufferedReader.readLine()) != null) {

                        Log.i(TAG, "Ping line is " + linenew);
                        output.append(linenew);


                    }
                    bufferedReader.close();
                }


                Log.i(TAG, "Ping final output is " + output);
                if (output != null) {
                    String finaloutput = output.toString();
                    finaloutput = finaloutput.substring(finaloutput.indexOf("ping statistics ---") + 19, finaloutput.length());
                    Log.i(TAG, "ping remaining " + finaloutput);
                    String[] split = finaloutput.split(",");
                    Log.i(TAG, "Remaining for ping for packet loss " + finaloutput);
                    if (split[2].length() > 0) {
                        split[2] = split[2].trim();
                        packetlosss = split[2].substring(0, split[2].indexOf(" "));
                        Log.i(TAG, "packet loss is " + packetlosss);
                    }

                }

            } catch (Exception var19) {
                Log.i(TAG, " exception is" + var19);

            }
            return packetlosss;
        }

        public static JSONObject getFinalupload() {
            JSONObject jsonObject = new JSONObject();


            float finaluploadvalue = 0F;
            float maxvalue = 0F;
            for (int i = 0; i < 3; i++) {
                float uploadvalue = uploadspeedtest();

                finaluploadvalue = uploadvalue + finaluploadvalue;


                if (uploadvalue > maxvalue) {
                    maxvalue = uploadvalue;
                }

            }
            finaluploadvalue = finaluploadvalue / 3F;
            System.out.println("FappsSpeedTest Final upload avg value is " + finaluploadvalue);

            try {
                jsonObject.put("ulThroughput", (float) Math.round(finaluploadvalue) + " Mbps");
                jsonObject.put("ulMaxThroughput", (float) Math.round(maxvalue) + " Mbps");
                jsonObject.put("latency", getLatency("180.179.214.57") + " ms");
                jsonObject.put("ulDataSize", "163190 bytes");
                jsonObject.put("destination_ip", "180.179.214.56");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        public static JSONObject getFinaldownload() {
            JSONObject jsonObject = new JSONObject();
            float downloadvalue = 0F;
            float maxvalue = 0F;
            float finaludownloadvalue = 0F;
            float sizeoffile = 0F;


            for (int i = 0; i < 3; i++) {
                HashMap<String, Float> downlo = downloadspeedtest();
                float downloadva = downlo.get("speed");
                sizeoffile = downlo.get("size");


                finaludownloadvalue = downloadva + finaludownloadvalue;


                if (downloadva > maxvalue) {
                    maxvalue = downloadva;
                }

            }


            finaludownloadvalue = finaludownloadvalue / 3F;
            System.out.println("FappsSpeedTest Final download avg value is " + finaludownloadvalue);
            try {
                jsonObject.put("dlDataSize", (float) Math.round(sizeoffile) + " Mb");
                jsonObject.put("url", "http://3.108.182.22:8056/mehak/Delhi.json");
                jsonObject.put("dlMaxThroughput", maxvalue + " Mbps");
                jsonObject.put("latency", getLatency("180.179.214.57") + " ms");
                jsonObject.put("dlThroughput", finaludownloadvalue + " Mbps");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return jsonObject;
        }


    }




