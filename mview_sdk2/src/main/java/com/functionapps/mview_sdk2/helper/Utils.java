package com.functionapps.mview_sdk2.helper;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

import com.functionapps.mview_sdk2.main.Mview;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

import static android.content.Context.CONNECTIVITY_SERVICE;


public class Utils {

    public static File downloaded_root = new File(Environment.getExternalStorageDirectory()+"/");
    public static String ping1(final String url, final Interfaces.PingResult pingResult, String ipType) {

        final String[] str = {""};
        final String[] pckloss = {""};
        final String[] finalVal = {""};
        final String value;


                try {
                    Process process=null;
                    Process mtrProcess=null;
                    if(Utils.checkifavailable(ipType))
                    {
                        if(ipType.equalsIgnoreCase("ipv6"))
                        {
                            process = Runtime.getRuntime().exec(
                                    "ping6 -c 5 " + url);
                            Log.i("Pinger", "Command " + "ping6 -c 5 " + url);


                        }
                        else
                        {
                            process = Runtime.getRuntime().exec(
                                    "ping -c 5 " + url);
                            Log.i("Pinger", "Command " + "ping -c 5 " + url);
                        }
                    }
                    else
                    {
                        process = Runtime.getRuntime().exec(
                                "ping -c 5 " + url);
                        Log.i("Pinger", "Command " + "ping -c 5 " + url);
                    }

                   /* Process process = Runtime.getRuntime().exec(
                            "ping -c 1 " + url);*/
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            process.getInputStream()));

                    Log.i("Pinger", "Command " + "ping -c 1 " + url);
                    //  parsePingResponse(reader);
                    if(pingResult!=null) {
                        pingResult.parsePingResult(parsePingResponse(reader,url));
                    }
                /*    String command = "mtr -n -c 5 8.8.8.8";
                    mtrProcess=Runtime.getRuntime().exec(command);
                    BufferedReader reader1 = new BufferedReader(new InputStreamReader(
                            mtrProcess.getInputStream()));
                    parseMtrResponse(reader1);*/
                    int i;
                    char[] buffer = new char[4096];
                    StringBuffer output = new StringBuffer();
                    String op[] = new String[64];
                    String delay[] = new String[8];
                    Log.i("Pinger", "Reader " + reader.read(buffer) + " buffer " + buffer);
                    while ((i = reader.read(buffer)) > 0)
                        output.append(buffer, 0, i);
                    reader.close();

                    op = output.toString().split("\n");
                   /* [PING 203.122.58.233 (203.122.58.233) 56(84)
                    bytes of data., , --- 203.122.58.233 ping statistics ---, 1 packets transmitted, 0 received, 100% packet loss, time 0ms]
*/
                    int index2 = output.indexOf("% packet loss");
                    Log.i("Pinger ", "Response " + Arrays.toString(op));
                    if (op.length > 4) {
                        Log.i("Pinger", "Ping res: " + Arrays.toString(op) + " ping is" + str[0]);
                        if (op[1] != null) {
                            delay = op[1].split("time=");
                        }

                        if (op[4] != null) {
                            String[] loss = op[4].split(",");
                            if (loss[2] != null) {
                                pckloss[0] = loss[2];
                            }
                        }

                        // String pck_loss=op[index2];


                        if (delay[1] != null) {
                            str[0] = delay[1];
                        }
                    }
                    finalVal[0] = "/" + str[0] + "/" + pckloss[0];
                    if(pingResult!=null) {
                        pingResult.onPingResultObtained(finalVal[0]);

                    }


                    System.out.println("final string " + finalVal[0]);

                } catch (IOException e) {
                    // body.append("Error\n");
                    e.printStackTrace();
                    System.out.println("exception is " + e.toString());
                } catch (ArrayIndexOutOfBoundsException ae) {
                    System.out.println("exception is " + ae.toString());
                    ae.printStackTrace();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


        return finalVal[0];
    }

    public static void appendLog(String text) {
      /*  File logFile = new File("sdcard/mView_logs.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
      /*  File logFile = new File(downloaded_root.getAbsoluteFile() + "/mView_logs.txt");
        */
        /*try {
            File root = new File(Environment.getExternalStorageDirectory(), "sdk_thanks");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, "mView_logs.txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(text);
            writer.flush();
            writer.close();
            Toast.makeText(Mview.fapps_ctx, "Text Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    public static Pinger parsePingResponse(BufferedReader stdInput1, String url)
    {
        Pinger pingData=new Pinger();
        String sudoScript="";
        String rtt_min = null, rtt_avg = null, rtt_max = null, rtt_mdev = null, rtt_dev = null, time_unit_val = null, packet_loss = null;
        try {
           /* int i;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            String op[] = new String[64];
            while ((i = stdInput1.read(buffer)) > 0)
                output.append(buffer, 0, i);
            op = output.toString().split("\n");
            Log.i("Pinger","Other response is "+ Arrays.toString(op));*/
            while ((sudoScript = stdInput1.readLine()) != null) {
                Log.i("Pinger","Pinger result is "+sudoScript);

                if (sudoScript.contains("rtt min/avg/max/mdev")) {

                    String values = sudoScript.substring(sudoScript.indexOf("=") + 1, sudoScript.length());
                    System.out.println("values for ping" + values);
                    String[] splitvalues = values.split("/");
                    rtt_min = splitvalues[0];
                    System.out.println("rtt is " + rtt_min);
                    pingData.setRttMin(rtt_min);

                    rtt_avg = splitvalues[1];
                    System.out.println("rtt_avg " + rtt_avg);

                    rtt_max = splitvalues[2];
                    System.out.println("rtt max is " + rtt_max);
                    pingData.setRrtMax(rtt_max+"");
                    rtt_mdev = splitvalues[3];
                    System.out.println("rtt_ven " + rtt_mdev);
                    String[] rtt_dev_ = rtt_mdev.split(" ");
                    rtt_dev = rtt_dev_[0];
                    time_unit_val = rtt_dev_[1];
                    pingData.setRttAvg(rtt_avg);
                    pingData.setLatency(rtt_avg);
                    pingData.setTimeUnit(rtt_dev_[1]+"");
                    pingData.setLatency(rtt_avg+"");
                    pingData.setRttDev(rtt_dev_[0]+"");

                }
                if (sudoScript.contains("(")) {



                } else if (sudoScript.contains("packet loss")) {
                    String packetloss = sudoScript.substring(sudoScript.indexOf("packet loss") - 4, sudoScript.indexOf("packet loss") - 1);
                    System.out.println("values for packet loss in second check" + packetloss);
                    String[] splitvalues = packetloss.split("/");
                    packet_loss = splitvalues[0];
                    pingData.setPacketLoss(packet_loss+"");
                    // jsonObject.put("packet_loss", packet_loss);
                    System.out.println("packet loss is " + packet_loss);
                }
                pingData.setHost(url);


            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return pingData;

    }

    public static void generateCdnIpAndPing(Interfaces.PingResult pingResult)
    {
        String str=null;
        try {

                    try {
                        String urlString = "https://www.youtube.com/watch?v=HngTaeW9KVs";
                        URL url = new URL(urlString);
                        InetAddress address = InetAddress.getByName(url.getHost());
                        String cdnIp = address.getHostAddress();
                        if(Utils.checkifavailable(cdnIp)) {
                            if (address instanceof Inet4Address) {
                                Log.i("Pinger","Ipv4");
                                ping1(cdnIp,pingResult,"ipV4");
                            } else if (address instanceof Inet6Address) {
                                Log.i("Pinger","Ipv6");
                                ping1(cdnIp,pingResult,"ipV6");
                            }
                        }




                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }



        } catch (Exception e) {
            // body.append("Error\n");
            e.printStackTrace();



        }

    }



    public static String getRoundedOffVal(String val, int roundOffUpto) {
        if (Utils.checkIfNumeric(val)) {
            if (Utils.checkifavailable(val)) {
                BigDecimal bd = new BigDecimal(val).setScale(2, RoundingMode.HALF_UP);
                return bd.doubleValue() + "";
            }
            return "0";

        }
        return val;
    }
    private static  boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID)  {

        boolean isReady = false;

        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try{

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimStateGemini = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimStateGemini.invoke(telephony, obParameter);

            if(ob_phone != null){
                int simState = Integer.parseInt(ob_phone.toString());
                if(simState == TelephonyManager.SIM_STATE_READY){
                    isReady = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isReady;
    }

    public static boolean isPhoneDualSim(Context context) {
        {
        /*TelephonyManager operator = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(context);

        boolean isDualSIM = telephonyInfo.isDualSIM();
return isDualSIM;*/

            String[] simStatusMethodNames = {"getSimStateGemini", "getSimState"};

            boolean first = false, second = false;

            for (String methodName: simStatusMethodNames) {
                // try with sim 0 first
                try {
                    first = getSIMStateBySlot(context, methodName, 0);
                    // no exception thrown, means method exists
                    second = getSIMStateBySlot(context, methodName, 1);
                    return first && second;
                } catch (Exception e) {
                    // method does not exist, nothing to do but test the next
                }
            }
            return false;

        }
    }
    public static String getPhoneImsi(Context context) {


        String imsi="NA";
        if (isPhoneDualSim(context)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1&&Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            {
                if (Utils.checkifavailable(SimInfoUtility.getSingleImsi(context)))
                {

                    imsi=SimInfoUtility.getSingleImsi(context);
                }
                else if (Utils.checkifavailable(SimInfoUtility.getSecondIMSI(context)))
                {


                    imsi=SimInfoUtility.getSecondIMSI(context);


                }

               /* else
                {


                    imsi=SimInfoUtility.getImsi(context);

                }*/
            }
        }
        else
        {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1&&Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            {
                imsi=SimInfoUtility.getImsi(context);
            }
            else {
                imsi="NA";
            }
        }
        if (!Utils.checkifavailable(imsi))
        {
            Utils.onFailure(2,"IMSI not found");
        }

        return imsi;
    }

public static String getDeviceID()
{
    try {


        return Settings.Secure.getString(
                Mview.fapps_ctx.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
    catch (Exception e)
    {
        Utils.onFailure(2,"Device ID not found");
        return "NA";

    }
}

    public static String getImsi() {
        String deviceId = " ";
        if (Build.VERSION.SDK_INT >= 29)
        {

            deviceId = Settings.Secure.getString(
                    Mview.fapps_ctx.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            System.out.println("check 1 with device id "+deviceId);
        } else {
            deviceId = getPhoneImsi(Mview.fapps_ctx);
            System.out.println("check 2 with device id "+deviceId);
        }
        if(!Utils.checkifavailable(deviceId))
        {

            deviceId = Settings.Secure.getString(
                    Mview.fapps_ctx.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            System.out.println("check 3 with device id "+deviceId);
        }

        return deviceId;
    }

    public static int convertFrequencyToChannel(int frequency) {
        if (frequency >= 2412 && frequency <= 2472) {
            return (frequency - 2412) / 5 + 1;
        } else if (frequency == 2484) {
            return 14;
        } else if (frequency >= 5170  &&  frequency <= 5825) {
            /* DFS is included. */
            return (frequency - 5170) / 5 + 34;
        }
        return -1;
    }
    public static String isAirplaneModeOn() {
        boolean state;
         state= Settings.System.getInt(Mview.fapps_ctx.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
         if (state)
         {
             return "yes";
         }
         else
         {
             return "no";
         }

    }

    private static void getPrimaryInfo(SubscriptionInfo lsuSubscriptionInfo, String imsi) {

        mView_HealthStatus.simPref = "Primary";
        mView_HealthStatus.prim_carrierName = (String) lsuSubscriptionInfo.getCarrierName();
        if (lsuSubscriptionInfo.getDataRoaming() == 1) {
            mView_HealthStatus.prim_getDataRoaming = "Yes";
        } else {
            mView_HealthStatus.prim_getDataRoaming = "No";
        }
        mView_HealthStatus.prim_mcc = lsuSubscriptionInfo.getMcc();
        mView_HealthStatus.prim_mnc = lsuSubscriptionInfo.getMnc();
        mView_HealthStatus.prim_imsi = imsi;
        mView_HealthStatus.prim_Slot=lsuSubscriptionInfo.getSimSlotIndex();
        System.out.println("simget icc 1"+lsuSubscriptionInfo.getIccId());
        System.out.println("getSim roaming"+lsuSubscriptionInfo.getDataRoaming());

    }
    public static  void onFailure(int errorCode, String errorMessage)
    {

        if (errorCode==0)
        {
            return;
        }
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentHourMin()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "HH:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static boolean checkifavailable(
            String text) {
        boolean available;
        if (text != null) {
            if (text.isEmpty() || text.equalsIgnoreCase("null")) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }


    }


    public static String getBattery(Context context) {

        try {

            Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            float bat = ((float) level / (float) scale) * 100.0f;
            return bat + "";
        }
        catch (Exception e)
        {
            Utils.onFailure(2,"Battery level can not be captured");
            return "NA";
        }

    }
    public static void deletefileFromFileManager(String path) {
        if(path!=null) {
            try {
                File fdelete = new File(path);
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        System.out.println("file Deleted :" + path);
                    } else {
                        System.out.println("file not Deleted :" + path);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    public static boolean checkIfNumeric(String value) {
        try {

            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static String  getvoc() {

        boolean na = false;
        String voc;


        if (Mview.fapps_ctx != null) {
            try {
                ConnectivityManager cm = (ConnectivityManager) Mview.fapps_ctx
                        .getSystemService(CONNECTIVITY_SERVICE);
                // test for connection
                // txt_status.setText("Internet is working");
                // txt_status.setText("Internet Connection Not Present");
                na = cm.getActiveNetworkInfo() != null
                        && cm.getActiveNetworkInfo().isAvailable()
                        && cm.getActiveNetworkInfo().isConnected();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (na) {

            return "I am able to use internet";
        }
        else {
            return "I am unable to use internet";
    }



    }

  public static   int getDefaultDataSubscriptionId()  {
      SubscriptionManager subscriptionManager = (SubscriptionManager) Mview.fapps_ctx.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= 24)  {
            int nDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();

            if (nDataSubscriptionId != SubscriptionManager.INVALID_SUBSCRIPTION_ID)  {
                return (nDataSubscriptionId);
            }
        }

        try  {
            Class<?> subscriptionClass = Class.forName(subscriptionManager.getClass().getName());
            try {
                Method getDefaultDataSubscriptionId = subscriptionClass.getMethod("getDefaultDataSubId");

                try {
                    return ((int) getDefaultDataSubscriptionId.invoke(subscriptionManager));
                }
                catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        return (SubscriptionManager.INVALID_SUBSCRIPTION_ID);
    }

   /* public String getUIText22(final TelephonyManager telephonyManager) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) Mview.fapps_ctx.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

        int nDataSubscriptionId = getDefaultDataSubscriptionId(subscriptionManager);

        if (nDataSubscriptionId != SubscriptionManager.INVALID_SUBSCRIPTION_ID) {
            SubscriptionInfo si = subscriptionManager.getActiveSubscriptionInfo(nDataSubscriptionId);

            if (si != null) {
                return (si.getCarrierName().toString());
            }
        }
    }*/
    public static int getchannel() {
        WifiManager wm = (WifiManager) Mview.fapps_ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wm.getConnectionInfo();
        int freq=wifiInfo.getFrequency();
        int channel=Utils.convertFrequencyToChannel(freq);
        return channel;
    }

    public static int getlacid() {
        int lacid = 0;

        TelephonyManager tm = (TelephonyManager) Mview.fapps_ctx.getSystemService(Context.TELEPHONY_SERVICE);
        try {

            GsmCellLocation cellLocation = (GsmCellLocation) tm.getCellLocation();
            lacid = cellLocation.getLac();

        } catch (Exception e) {
            lacid = 0;
        }

        return lacid;
    }

    public static String apnname() {
        String apn=getApnType();
        String apname;
        if (apn != null && apn.equalsIgnoreCase("Wifi"))
        {

            WifiManager wifiMgr = (WifiManager) Mview.fapps_ctx.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            apname= wifiInfo.getSSID().replaceAll("^\"(.*)\"$", "$1");

        }
        else if (apn != null && apn.equalsIgnoreCase("Mobile Data"))
        {


            TelephonyManager tm = (TelephonyManager) Mview.fapps_ctx.getSystemService(Context.TELEPHONY_SERVICE);
            apname= tm.getNetworkOperatorName();

        }
        else
            {
                apname=  " ";

            }
return  apname;


    }
    public static String getApnType()
    {
        {
            String apn = null;
            String networkname = NetworkUtil.getConnectivityStatusString(Mview.fapps_ctx);

            if (networkname.equalsIgnoreCase("Wifi enabled")) {
                apn = "Wifi";
        /*    ConstantStrings.networkname = "Wifi";
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            ConstantStrings.cnetworkname = wifiInfo.getSSID();*/
            } else if (networkname.equalsIgnoreCase("Mobile data enabled")) {
         /*   ConstantStrings.networkname = "Mobile data";
            ConstantStrings.cnetworkname = tm.getNetworkOperatorName();*/
                apn = "Mobile Data";

            }
            return apn;
        }
    }
    public static String getIP() {
        // TODO Auto-generated method stub

        String ip="NA";
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            String ethernetip=null;

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                // drop inactive
                if (!networkInterface.isUp())
                    continue;

                // smth we can explore
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();


                    if (addr instanceof Inet4Address) {
                        if (networkInterface.getName().contains("eth")) {

//		  	                	 if (!addr.isSiteLocalAddress() && !addr.isAnyLocalAddress() && !addr.isLinkLocalAddress() && !addr.isLoopbackAddress() && !addr.isMulticastAddress())
//		  	                	 {
                            if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {

                                ip = addr.getHostAddress();


                            }
                        } else if (networkInterface.getName().contains("wla")) {
//		  	                		 if (!addr.isSiteLocalAddress() && !addr.isAnyLocalAddress() && !addr.isLinkLocalAddress() && !addr.isLoopbackAddress() && !addr.isMulticastAddress()) {

                            if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                                ip = addr.getHostAddress();


                            }
                        } else if (networkInterface.getName().contains("ppp")) {
//		  	                		 if (!addr.isSiteLocalAddress() && !addr.isAnyLocalAddress() && !addr.isLinkLocalAddress() && !addr.isLoopbackAddress() && !addr.isMulticastAddress()) {

                            if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                                ip = addr.getHostAddress();


                            }
                        }


                    }


                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return ip;


    }

    public static String  getCountrycode() {
        String countryCodeValue;
        TelephonyManager tm = (TelephonyManager)Mview.fapps_ctx.getSystemService(Context.TELEPHONY_SERVICE);
         countryCodeValue = tm.getNetworkCountryIso().toUpperCase();
        return countryCodeValue;
    }

    public static boolean isMaxint(int a) {
        if (a==Integer.MAX_VALUE)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static String getIMEI() {

        try {


            TelephonyManager telephonyManager = (TelephonyManager) Mview.fapps_ctx.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getDeviceId();
        }
        catch (Exception e)
        {
            Utils.onFailure(2,"IMEI can found");
            return "NA";
        }
    }
}