
package com.functionapps.mview_sdk2.helper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

//import com.mcpsinc.mview.application.MviewApplication;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final String TURN_ON_WIFI ="You need to turn the wifi on to create wifi heatmap!" ;
    public static final String WIFI_NOT_CONNECTED = "Not connected to any wifi" ;
    public static final String STRUCTURE ="Structure" ;
    public static final String OPENING="Openings";
    public static final String FLAT="Flat Type";
    public static final String SHAPES = "Shapes";
    public static final String LABELS ="Labels" ;
    public static final String COMPONENTS ="Components" ;
    public static final String CLOSE = "Close";
    public static final String SUCCESSFULL_HEATMAP = "Signal Strength heat map saved successfully.";
    public static final String SUCCESSFULL_HEATMAP_STEP2 = "Save link speed heatmap.";
    public static final String CREATE_WALKMAP = "STEP 2:Create your heatmap by conducting the walk test around your floor area." ;
    public static final String SUCCESSFULL_LS_HEATMAP ="Link Speed heat map saved successfully." ;
    public static final String SUCCESSFULL_LS_HEATMAP_STEP2 =" Save Signal Strength heatmap." ;
//    public static final String HEAT_MAP_MOVE_BACK_WARNING = "You have not saved link speed heatmap."+ System.lineSeparator()+"Are you sure you want to exit without saving it?";
//    public static final String LS_HEAT_MAP_MOVE_BACK_WARNING = "You have not saved signal strength heatmap."+ System.lineSeparator()+"Are you sure you want to exit without saving it?";
    public static final String CREATE_HEATMAP = "Now you can create your heatmap.";
//    public static final String IMAGE_SELECTED = "The floor plan has been selected."+ System.lineSeparator()+"Now , you may use selected floor plan to use further options.";
    public static final String BACK_PRESS_FINAL_HEATMAP = "Are you sure , you want to exit without saving any plan?";

    public static String responseOne;
    public static void resetMutexForManualScanTable()
    {
        manualScanMutex=0;
	/*	System.out.println(helper.Config.getDateTime()
				+" : via  resetMutexForUrlListTable Resetting Manual Scan Update Value "+manualScanMutex);*/
    }
    public static int manualScanMutex=0;
    // BY SWAPNIL BANSAL 10/14/2022 FOR UPLAOD SCRREN SHOT
    public static final int manualScanUploadValue=1;
    public static boolean checkIfFileExistsOrNot(String fileName)
    {
        try
        {
            if(fileName!=null)
            {
                //	System.out.println(Config.getDateTime() +" :  via checkIfFileExistsOrNot check if "+fileName +" exists or not");
                File file = new File(fileName);


                if(file.exists()) {
                    return true;
                }
            }
        }
        catch(Exception e)
        {
            System.out.print("exception is" +e.getMessage());
            //Utils.appendLog("exception in run agent method  " +e.getMessage());
            // Utils.appendLog("exception in run agent method  " +e.getStackTrace());
            e.printStackTrace();
            //System.out.println(Config.getDateTime() +" :  via checkIfFileExistsOrNot exception occured while checking file "+fileName );
            //logger.error("Exception",e);
        }
        return false;
    }
    public static void removeFile(String fileName)
    {
        try
        {
            if(fileName!=null)
            {
                File file = new File(fileName);

                if(file.exists()) {
                    if(file.delete())
                    {
                        //logger.debug(" :  via removeFile  File deleted successfully");
                    }
                    else
                    {
                       // logger.debug(" : via removeFile "+fileName +" Failed to delete the file");
                    }
                }
            }
        }
        catch(Exception e)
        {
//            System.out.println(Config.getDateTime() +" : via removeFile  exception occured while deleting file "+fileName );
            System.out.println(" Exception is"+e.getMessage());
            e.printStackTrace();
            //logger.error("Exception",e);
        }
    }

    public static void  setMutexForManualScanTable(int threadValue)
    {
        for(int i=0;i<20;i++)
        {
		/*	System.out.println(helper.Config.getDateTime()
					+" : via  setMutexForManualScanTable   Manual Scan Update Value is "+manualScanMutex +" for i "+i +" and thread  for which mutex has to be set is  "+threadValue);
		*/
            if(manualScanMutex==0)
            {

                manualScanMutex=threadValue;
                //	logger.debug(" : via  setMutexForManualScanTable  Setting Manual Scan Update Value "+manualScanMutex);
                break;

            }
            else
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                    System.out.println(" Exception is"+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        //return false;
    }
    // by swapinl for round robin part
    public static String scan_number=null;

    public static String mview = "http://198.12.250.223/webwapapi/app/mview/mView.apk";
    public static final String INCORRECT_URL ="Please check the Download Settings. The download URL is not correct." ;
    public static final String GENERIC_FAILURE = "Generic failure has occured!";
    public static final String NO_SERVICE = "No Service from your operator!!";
    public static final String ENABLE_NTWRK = "Please Enable your network.";
    public static final String NO_NTWRK ="Network is not available !" ;
    public static final String CONNECTION_ERROR="Connection failed! Please try after sometime.";
    public static final String SERVER_ERROR="No response from server, Kindly retry!!";
    public static final String CONNECTION_TIMEOUT_STRING="Connection timed out! Please try after sometime!!";
    public static final int CONNECTION_TIMEOUT=15000;
    public static final String VALIDATION_ALERT ="Mobile number verification OTP will be sent to your device and charges will apply according to your plan. "+"\n"+"\n"
            +"This app requires 3 permissions to manage Phone calls, Sms and Location. "+  "\n"+"\n"+"Network data may not be available in case location permission is denied." ;
    public static final String INCORRECT_OTP = "Please enter the correct OTP received on your device!";
    public static final String VERIFICATION_DENIED ="Sorry, we could'nt verify the phone number." ;
    public static final String NETWORK_COVERAGE_ISSUE="Network Coverage Data collected and Send to the Network Team. Thanks for Reporting the issue. " +
            "Team will analyze and will get back to you";
    public static final String DATA_BW_ISSUE="Kindly Perform UP/DL Bandwidth Test to know us better about your issue.";
    public static final String CALL_ISSUE="Kindly Perform Call/SMS Test to know us better about your issue";
    public static final String CALL_TEST_DONE="Thanks for Reporting the issue." +
            " Test data has been collected and sent to the network team. Soon we will get back to you";
    public static final String UL_DL_TEST_DONE="Thanks for Reporting. " +
            "UL/DL Data collected and sent to the network team. Soon we will get back to you";
    public static final String TEST_START_MSG="Please wait until the test completes!!";
    public static final String USER_CONFIRMATION="Are you sure you want to abort the test?";
    public static final int PERMISSIONS_REQUEST = 10;
    public static final String REGISTERATION = "registeration";
    public static final String JSON_ERROR ="Unable to load data!!";
    public static final int READ_TIMEOUT = 10000;
    public static final String NO_URL ="Kindly enter any url or ip" ;
    public static String URL = "http://198.12.250.223/mtantu/requesthandler";
    public static String USER_NUM = "";
    public static int ERROR =0 ;
    public static String BACKGROUND_AUTORESTART="autorestart";
    public static boolean PERIODIC_API_RESPONSE=true;
    public static String Rsrp="RSRP";
    public static String Snr="SNR";
    public static String LOADING="Loading...";
    public static String NO_INTERNET="Internet is not available.Kindly cross check your internet connection!";
    public static List<Intent> POWERMANAGER_INTENTS = Arrays.asList(
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.entry.FunctionActivity"))
            //.setData(android.net.Uri.parse("mobilemanager://function/entry/AutoStart"))
    );
    public static String INTERNET_PROBLEM="No Internet Connection!";
    //public static String pviewURL="";

    //public static String pviewURL="http://203.122.58.233:9030/charting_backend_new/RequestHandler?";
    public static String pviewURL="http://198.12.250.223:8080/charting_backend_new/RequestHandler?";
    public static String coordinatesURL="http://198.12.250.223/pview_idea_new/stcord/India/";
    public static String otp="OTP sent to ";
    //public static String IMSI="999999999";
    public static String IMSI;
    public static String alpha2host="203.122.58.233";
    public static String alpha3host="198.12.250.223";
    public static String gcmid;
    public static String Sim_Notification="com.services.gcmhandler";
    public static String app_name="Mview";
    public static String service_toggle="service_toggle";
    public static String autostart_msg="To identify network problems, allow mView  to collect Network parameters for next 24 hours by enabling Autostart option ";

public  static boolean service_started=true;
    public static String start_call_test="Your call will be recorded for detecting silence and other issues.Recorded call will be uploaded for further analysis!";
    public static String start_vdo_test="Your test results will be observed for further analysis."+"\n"+"You may stop at any time or wait for one minute to complete the test.";
    public static String call_end_record_alert="Your call recording has been uploaded successfully for diagnostic purpose.";
public static String map_view_test_end="If you wish to continue the network test in background,turn on the Enable Network Test option of menu at home page.";
    public static String getversionnumber(Context context) {
        String versionno = null;
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionno = manager.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionno = "0.0.0";
        }
        return versionno;
    }



}