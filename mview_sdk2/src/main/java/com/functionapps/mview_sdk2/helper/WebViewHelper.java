package com.functionapps.mview_sdk2.helper;


import android.annotation.TargetApi;
import android.content.Context;
import android.net.TrafficStats;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;


import com.functionapps.mview_sdk2.main.Mview;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class WebViewHelper {
    private static final String TAG = "WebViewHelper";
    private static final long COUNT_DOWN_TIMER =40000 ;
    private  Pinger pingResponse;
    private  Context context;
    private WebView webView;
    private static final String PLAYER_WIDTH="640";
    private static final String PLAYER_HEIGHT="390";
    private static final String PLAY_TIME_REQUIRED="25000";//ms
    private float dataUsageBefore;
    private float dataUsageAfter;
    private float dataUsageDiff;
    private String dataUsageDiffVal;
    private int progress=0;
    private boolean timeOutOccured=false;
    private String eventObtained;
    private boolean resultObtained;
    private long time_between_stalls=0;
    String latency;
    private String packet_loss;
    private long timetofistframe;
    private long videostarttime=0;
    long startplay_time=0;
    private long totalPlaying_time=0;

    public interface JsonResultInterface
    {
        public void   sendJsonResult(JSONObject jsonResult);
        public void   sendThanksappJsonResult(JSONObject jsonResult);


    }


     JsonResultInterface sendResult;
    public  WebViewHelper(Context cont,String late , String loss) {
        Log.i(TAG,"webview helper class called" );

        webView = new WebView(cont);
        context=cont;
        packet_loss=loss;
        latency=late;
//        pingResponse=pingRespo;
         WebSettings settings = webView.getSettings();
        webView.getSettings().setJavaScriptEnabled(true);
        // Add a JavaScript interface to the WebView
        webView.addJavascriptInterface(new JavaScriptInterface(), "Android");
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);

        //webView.getSettings().setTimeout(3000);

        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //     settings.setPluginState(WebSettings.PluginState.ON);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        webView.setPadding(0, 0, 0, 0);

        /*webView.setLayoutParams(new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT));

        // webView.loadUrl("javascript:document.getElementById('player').stopVideo();");
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        windowManager.addView(webView,webView.getLayoutParams());
*/
//        webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.i(TAG,"Error with code "+errorCode +" desc "+description);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String title = webView.getTitle();
                // webView.
                Log.d(TAG, "Title: " + title);
            }


           /* @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //view.loadUrl();
                return true;

            }*/

        });




       /* webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.d(TAG, "Title: " + title);
            }
        });*/


        webView.setWebChromeClient(new WebChromeClient(){
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
               /* context.runOnUiThread(() -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        String[] PERMISSIONS = {
                                PermissionRequest.RESOURCE_AUDIO_CAPTURE,
                                PermissionRequest.RESOURCE_VIDEO_CAPTURE
                        };
                        request.grant(PERMISSIONS);
                    }
                });*/
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.d(TAG, "Progress is : " + newProgress);
                progress=newProgress;
                // Utils.showLongToast(context,"Video Page loaded upto "+newProgress +"%");
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.i(TAG,"Message is "+message +" result is "+result +" for url "+url);
                return super.onJsAlert(view, url, message, result);

            }
        });
    }

    public void loadUrl(String url, JsonResultInterface sendResult) {
        dataUsageBefore= fetchDataUsage();
        final String mimeType = "text/html";
        final String encoding = "UTF-8";

        this.sendResult=sendResult;

         videostarttime=System.currentTimeMillis();
        //String html = getHTML();
        // webView.loadDataWithBaseURL(url, url, mimeType, encoding, "");
        setCoundownTimer(url);


    }

    private void setCoundownTimer( String url) {
        new CountDownTimer(COUNT_DOWN_TIMER, 1000) {

            public CountDownTimer init(){

                webView.loadUrl("file:///android_asset/youtube_test.html");
                return this;
            }
            public void onTick(long millisUntilFinished) {
                Log.i(TAG,"seconds remaining: " + millisUntilFinished / 1000);
                Log.i(TAG,"Event obtained on finishing "+eventObtained);
                if(Utils.checkifavailable(eventObtained)) {
                    if (eventObtained.equalsIgnoreCase("Playing"))
                    {
                        cancel();
                    }
                }

                // textView.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                //Sending Final Report even if we don't receive any state or specifically Playing state
                Log.i(TAG,"Event obtained on finishing "+eventObtained);
                if(Utils.checkifavailable(eventObtained))
                {
                    if(!eventObtained.equalsIgnoreCase("Playing"))
                    {
                        Utils.onFailure(6,"Video Test timeout");
                        sendEventAfterTimeOut();

                        webView.stopLoading();
                    }
                }
                else
                {
                    webView.stopLoading();
                    sendEventAfterTimeOut();
                }


            }
        }.init().start();

    }

   /* private float fetchDataUsage() {
        float total;
        long currentMobileTxBytes = TrafficStats.getMobileTxBytes();
        long currentMobileRxBytes = TrafficStats.getMobileRxBytes();
        long totalTxBytes = TrafficStats.getTotalTxBytes();
        long totalRxBytes = TrafficStats.getTotalRxBytes();

        float l = currentMobileTxBytes + currentMobileRxBytes;
        float mobileDataInMB = (l) / (1024 * 1024);
        System.out.println("mobile data "+l +"");
        float wifiDataInMB = ((totalTxBytes + totalRxBytes)/(1024*1024)) - mobileDataInMB;
        Log.i(TAG,"Mobile data usage "+mobileDataInMB +" "+" Wifi Data Usage "+wifiDataInMB);
        if(wifiDataInMB < 0 )
            wifiDataInMB = 0;

        if(mobileDataInMB<0)
            mobileDataInMB=0;
        String apnName=Utils.getApnType();
        if(Utils.checkifavailable(apnName))
        {
            if(apnName.equalsIgnoreCase("Wifi"))
                return wifiDataInMB;
            else
                return mobileDataInMB;
        }


        return 0;
    }*/

    private float fetchDataUsage() {
        float total;
        //long currentMobileTxBytes = TrafficStats.getMobileTxBytes();
        long currentMobileRxBytes = TrafficStats.getMobileRxBytes();
        //   long totalTxBytes = TrafficStats.getTotalTxBytes();
        long totalRxBytes = TrafficStats.getTotalRxBytes();

        //  float l = currentMobileTxBytes + currentMobileRxBytes;
        float mobileDataInMB = (currentMobileRxBytes) / (1024 * 1024);
        System.out.println("mobile data "+currentMobileRxBytes +"");
        float wifiDataInMB = ((totalRxBytes)/(1024*1024)) - mobileDataInMB;
        Log.i(TAG,"Mobile data usage "+mobileDataInMB +" "+" Wifi Data Usage "+wifiDataInMB);
        if(wifiDataInMB < 0 )
            wifiDataInMB = 0;

        if(mobileDataInMB<0)
            mobileDataInMB=0;
        String apnName=Utils.getApnType();
        if(Utils.checkifavailable(apnName)){
            if(apnName.equalsIgnoreCase("Wifi"))
                return wifiDataInMB;
            else
                return mobileDataInMB;
        }


        return 0;
    }


    private void sendEventAfterTimeOut() {
        timeOutOccured=true;
        dataUsageAfter= fetchDataUsage();
        dataUsageDiff=dataUsageAfter-dataUsageBefore;
        dataUsageDiffVal= Utils.getRoundedOffVal(dataUsageDiff+"",2);
            // Utils.showToast(context,"Video Test Ended...");
            if(sendResult!=null)
            {
                Log.i(TAG,"going to send to interface with zero values");
                sendResult.sendJsonResult((addParamsForVideoKpi(0, 0, 0, 0)));
                sendResult.sendThanksappJsonResult(addParamsForVideoKpiThanks(0,0,0,0));

            }
           /* RequestResponse.sendEvent
                    (addParamsForVideoKpi(0, 0, 0, 0), "video_test");*/

    }
    private  JSONObject addParamsForVideoKpi(int bufferingCount, int duration, long totalBufferingTime, long totalPlayingTime) {
        JSONObject object =new JSONObject();
        try {


            object.put("bufferCount",bufferingCount+"");
            object.put("bufferTime1080"," ");
            object.put("bufferTime144"," ");
            object.put("bufferTime240"," ");
            object.put("bufferTime2K"," ");
            object.put("bufferTime360"," ");
            object.put("bufferTime480"," ");
            object.put("bufferTime4K"," ");
            object.put("bufferTime4KPlus"," ");
            object.put("bufferTime720",totalBufferingTime+" ms");
            object.put("dataUsed",dataUsageDiffVal+"MB");
            object.put("deviceHeight",PLAYER_HEIGHT);
            object.put("deviceWidth",PLAYER_WIDTH);
           object.put("latency",latency);
            object.put("packetLoss",packet_loss);
            object.put("playTime1080"," ");
            object.put("playTime144"," ");
            object.put("playTime240"," ");
            object.put("playTime2K"," ");
            object.put("playTime360"," ");
            object.put("playTime480"," ");
            object.put("playTime4K"," ");
            object.put("playTime4KPlus"," ");
            object.put("playTime720",totalPlayingTime+" ms");
            object.put("requiredPlayTime",PLAY_TIME_REQUIRED+"");
            object.put("timestamp",Utils.getDateTime());
            object.put("timeout",COUNT_DOWN_TIMER+" ms");
            object.put("totalBufferTime",totalBufferingTime+" ms");
            object.put("totalBytes",dataUsageDiffVal+"MB");
            object.put("totalPlaybackTime",totalPlayingTime+" ms");
            object.put("videoLength",duration+" sec");
            object.put("videoResEnum"," ");
            object.put("videoUrl","https://www.youtube.com/watch?v=HngTaeW9KVs");
            try {


                long stallratio = totalBufferingTime / totalPlayingTime;
                object.put("stall_ratio",stallratio);


            }
            catch (Exception e)
            {
                object.put("stall_ratio"," ");

            }
            if (videostarttime<=startplay_time) {
                timetofistframe =  startplay_time-videostarttime;
                object.put("video_playback_rate", timetofistframe+" ms");
            }
            else
            {
                object.put("video_playback_rate", " ");

            }
            if (totalPlayingTime>=totalBufferingTime) {
                object.put("time_video_stalls", totalPlayingTime - totalBufferingTime+" ms");
            }
            else
            {
                object.put("time_video_stalls", " ");

            }
        /*    object.put("bufferCount",bufferingCount+"");
            object.put("bufferTime1080"," ");
            object.put("bufferTime144"," ");
            object.put("bufferTime240"," ");
            object.put("bufferTime2K"," ");
            object.put("bufferTime360"," ");
            object.put("bufferTime480"," ");
            object.put("bufferTime4K"," ");
            object.put("bufferTime4KPlus"," ");
            object.put("bufferTime720",totalBufferingTime+"");
            object.put("dataUsed",dataUsageDiffVal+"MB");
            object.put("deviceHeight",PLAYER_HEIGHT);
            object.put("deviceWidth",PLAYER_WIDTH);
            object.put("latency",pingResponse.getLatency());
            object.put("packetLoss",pingResponse.getPacketLoss());
            object.put("playTime1080"," ");
            object.put("playTime144"," ");
            object.put("playTime240"," ");
            object.put("playTime2K"," ");
            object.put("playTime360"," ");
            object.put("playTime480"," ");
            object.put("playTime4K"," ");
            object.put("playTime4KPlus"," ");
            object.put("playTime720",totalPlayingTime+"");
            object.put("requiredPlayTime",PLAY_TIME_REQUIRED+"");
            object.put("timestamp", Utils.getDateTime());
            object.put("timeout",COUNT_DOWN_TIMER+"");
            object.put("totalBufferTime",totalBufferingTime+"");
            object.put("totalBytes",dataUsageDiffVal+"MB");
            object.put("totalPlaybackTime",totalPlayingTime+"");
            object.put("videoLength",duration+"");
            object.put("videoResEnum"," ");
            object.put("videoUrl","https://www.youtube.com/watch?v=HngTaeW9KVs");
            object.put("pageLoadPercentage",progress+"%");
            object.put("cdnIpSource",pingResponse.getHost()+"");
            try {


                long stallratio = totalBufferingTime / totalPlayingTime;
                object.put("stall_ratio",stallratio);


            }
            catch (Exception e)
            {
                object.put("stall_ratio"," ");

            }
            object.put("video_playback_rate"," ");
            object.put("time_video_stalls",time_between_stalls);

            if(timeOutOccured)
                object.put("timeOutOccured","Yes");
            else
                object.put("timeOutOccured","No");
*/



        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"Video Test Json "+object.toString());
        return object;
    }









    private  JSONObject addParamsForVideoKpiThanks(int bufferingCount, int duration, long totalBufferingTime, long totalPlayingTime) {
        JSONObject object =new JSONObject();
        try {


            object.put("bufferCount",bufferingCount+"");
            object.put("bufferTime1080"," ");
            object.put("bufferTime144"," ");
            object.put("bufferTime240"," ");
            object.put("bufferTime2K"," ");
            object.put("bufferTime360"," ");
            object.put("bufferTime480"," ");
            object.put("bufferTime4K"," ");
            object.put("bufferTime4KPlus"," ");

            object.put("bufferTime720",totalBufferingTime+"");
            object.put("dataUsed",dataUsageDiffVal+"MB");
            object.put("deviceHeight",PLAYER_HEIGHT);
            object.put("deviceWidth",PLAYER_WIDTH);
            object.put("errorMessage"," ");


            object.put("latency",latency);
            object.put("packetLoss",packet_loss);
            object.put("playTime1080"," ");
            object.put("playTime144"," ");
            object.put("playTime240"," ");
            object.put("playTime2K"," ");
            object.put("playTime360"," ");
            object.put("playTime480"," ");
            object.put("playTime4K"," ");
            object.put("playTime4KPlus"," ");
            Log.i(TAG,"TOtal play time is "+totalPlayingTime);
            object.put("playTime720",totalPlayingTime+"");
            object.put("requiredPlayTime",PLAY_TIME_REQUIRED+"");
            object.put("timestamp",Utils.getDateTime());
            object.put("timeout",COUNT_DOWN_TIMER+"");
            Log.i(TAG,"Video start time is "+videostarttime+ "Play start time is "+startplay_time);

            if(videostarttime<=startplay_time) {
                object.put("timeToFirstFrame",  startplay_time-videostarttime);
            }
            else {
                object.put("timeToFirstFrame", " ");

            }



            object.put("totalBufferTime",totalBufferingTime+"");
            object.put("totalBytes",dataUsageDiffVal+"MB");
            object.put("totalPlaybackTime",totalPlayingTime+"");
            object.put("videoLength",duration+" sec");
            object.put("videoResEnum"," ");
            object.put("videoUrl","https://www.youtube.com/watch?v=HngTaeW9KVs");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"Video Test Json "+object.toString());
        return object;
    }




    // Create a JavaScript interface
    class JavaScriptInterface {
        private int bufferingCount;
        private long totalBufferingTime;
        private int duration;


        @JavascriptInterface
        public void onTimeOutOccured(boolean timeOut)

        {
            timeOutOccured=true;
        }
        @JavascriptInterface
        public void onPlayerStateChanged(String state)
        {
            Log.i(TAG,"State is "+state);
            if(Utils.checkifavailable(state))
            {
                if(state.equalsIgnoreCase("Playing"))
                    eventObtained=state;
               /* if(state.equalsIgnoreCase("buffering"))
                {*/

                //  Utils.showToast(context,state);
                // }
            }
        }
        @JavascriptInterface
        public void onObtainResult(final int duration, final int bufferingCount,final long totalBufferingTime,long totalPlayingTime,long startplaytime) {
            Log.i(TAG,"Duration is "+duration +" Buffering count is "+bufferingCount +" total playing time "+totalPlayingTime+" start play time is "+startplaytime);


            this.bufferingCount=bufferingCount;
            this.totalBufferingTime=totalBufferingTime;
            startplay_time=startplaytime;
            this.duration=duration;
            resultObtained=true;
            // Handle the play time, buffering time, and buffering count
           /* dataUsageAfter= fetchDataUsage();
            dataUsageDiff=dataUsageAfter-dataUsageBefore;
             dataUsageDiffVal= Utils.getRoundedOffVal(dataUsageDiff+"",2);
             Utils.showAlert("Video Ended",context,"","Ok");
            // Utils.showToast(context,"Video Test Ended...");

           RequestResponse.sendEvent(addParamsForVideoKpi(bufferingCount,duration,totalBufferingTime,totalPlayingTime),"video_test");*/
          /*  if(sendResult!=null) {
                Log.i(TAG, "going to send to interface with actual values");

                sendResult.sendJsonResult((addParamsForVideoKpi(bufferingCount, duration, totalBufferingTime, totalPlayingTime)));
                sendResult.sendThanksappJsonResult(addParamsForVideoKpiThanks(bufferingCount, duration, totalBufferingTime, totalPlayingTime));

//                sendResult.sendJsonResult(addParamsForVideoKpi(bufferingCount,duration,totalBufferingTime,totalPlayingTime),"video_test"));
            }*/

        }
        @JavascriptInterface
        public void sendPlayingTime(final long totalPlayingTime)
        {
            Log.i(TAG,"Total Play time is "+totalPlayingTime);
Log.i(TAG,"resultObtained value is "+resultObtained);




//            totalPlaying_time=totalPlayingTime;
            if(resultObtained) {
                dataUsageAfter = fetchDataUsage();
                dataUsageDiff = dataUsageAfter - dataUsageBefore;
                dataUsageDiffVal = Utils.getRoundedOffVal(dataUsageDiff + "", 2);
                //   Utils.showAlert("Video Ended", context, "", "Ok");
                // Utils.showToast(context,"Video Test Ended...");

                if(sendResult!=null) {
                    Log.i(TAG, "going to send to interface with actual values");

                    sendResult.sendJsonResult((addParamsForVideoKpi(bufferingCount, duration, totalBufferingTime, totalPlayingTime)));
                    sendResult.sendThanksappJsonResult(addParamsForVideoKpiThanks(bufferingCount, duration, totalBufferingTime, totalPlayingTime));

//                sendResult.sendJsonResult(addParamsForVideoKpi(bufferingCount,duration,totalBufferingTime,totalPlayingTime),"video_test"));
                }

//                RequestResponse.sendEvent(addParamsForVideoKpi(bufferingCount, duration, totalBufferingTime, totalPlayingTime), "video_test");
                resultObtained=false;
            }
        }


       /*  "small" typically corresponds to a resolution of 240p
"medium" typically corresponds to a resolution of 360p
"large" typically corresponds to a resolution of 480p
"hd720" corresponds to a resolution of 720p
"hd1080" corresponds to a resolution of 1080p
"highres" corresponds to a resolution of 1080p or higher*/

    }


}
