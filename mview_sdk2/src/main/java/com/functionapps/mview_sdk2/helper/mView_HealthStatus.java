package com.functionapps.mview_sdk2.helper;



import com.functionapps.mview_sdk2.main.MySpeedTest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sharad Gupta on 9/23/2016.
 */
public class mView_HealthStatus {

    public static int MaxPeriodicDataToSaveInDB = 10;
    public static String installedSince;

    public static MySpeedTest mySpeedTest;

    //shared prefrences - Main App Settings
    public static int periodicRefreshFrequencyInSeconds = 300; //5 mins
    public static int updateDashboardUIIntervalInSeconds = 5;
 //   public static String youtubeurl = "https://www.youtube.com/watch?v=Sg64rEtDd4s";
   public static String youtubeurl =" www.youtube.com/embed/HngTaeW9KVs";
//   public static  String  youtubeurl="https//nwexp.airtel.com/leap/leapYoutubeTest.html";
    public static boolean writeCallLogs = true;
    public static int MIN_GSM_SIGNAL_STRENGTH_FOR_CALL_DROP = 12;
    public static boolean startbackgroundservice = true;

    public static String userid = "1";

    public static String OperatorName = "";
    public static String simOperatorName="";
    public static int mcc;
    public static int mnc;

    public static String cellLocationType; //GSM or CDMA
    public static String Lac;
    public static String Cid;
    public static String Psc;

    public static String phonetype = "NA";
    public static boolean roaming;
    public static ArrayList<HashMap<String, String>> servingcell1info=new ArrayList<>();
    public static ArrayList<HashMap<String, String>> servingcell2info=new ArrayList<>();


    //Netowrk State
    public static int iCurrentNetworkState = -1;
    public static String strCurrentNetworkState = "NS";
    public static String strCurrentNetworkProtocol = "NS";
    public static String onlyCurrentNetworkState = "3G";
    public static String currentInstance="NS";

    public static long starTimeInCurrentState;
    public static long timein4G;
    public static long timein3G = 100;
    public static long timein2G;
    public static long timeinNS;

    public static float per4g, per3g, per2g, perNS;

    public static int iTotal_Calls, iCall_Success, iCall_Failed, iCall_Missed;

//    public static MySpeedTest mySpeedTest;

    public static int connectionTypeIdentifier; //1 means wifi, 2 means dataplan, 3 means 0
    public static String connectionType = "";

    public static String batteryLevel = "0";

//    public static ArrayList<CapturedPhoneState.BasicPhoneState> timeSeriesCapturedData;

    public static ArrayList<String> callRecordsTobeSentToServerArray;

    public static ArrayList<CurrentCellServing> timeSeriesServingCellDataArray;

    public static String ltePCI;
    public static String lteTAC;
    public static String lteRSRP;
    public static String lteRSRQ;
    public static String lteCQI;
    public static String lteRSSI;
    public static String lteSNR;
    public static String lteSINR;
    public static String lteArfcn;
    public static String lteENB;

    public static String lteasus;
    public static String ltedbm;
    public static String ltelevel;
    public static String lteta;
    public static int gsmSignalStrength;


    public static int CDMARSSI,CDMAECIO,EVDORSSI,EVDOECIO,EVDOSNR;

    public static int RxLev,BER;
    public static Boolean tddphone;
    public static String Wcdma_Psc;

    public static String rxqualfor2g;

    public static String Uarfcn;
          public static String ARFCN;
    public static String nodeb_id;
    public static String rscp;

    public static int evdoecio;
    public static String cellid;
    public static ArrayList<LteParams.Paramslist> lteparams=new ArrayList<>();
    public static int iCall_Answered;
    public static int iNoAnswered_Calls;
    public static int iTotal_OutgoingCalls;
    public static int iOutgoing_Success;
    public static boolean carrier_selection;
    public static String simPref="Primary";
    public static String prim_carrierName="NA";
    public static String prim_getDataRoaming="No";
    public static int prim_mcc,prim_mnc;
    public static String sec_imsi="NA";
    public static String sec_carrierName="NA";
    public static String sec_getDataRoaming="No";
    public static int sec_mcc,sec_mnc;

    public static String prim_imsi;
    public static String second_cellInstance=null;
    public static int second_Cqi;
    public static int second_Cid;
    public static int second_pci;
    public static int second_tac;
    public static int second_earfcn;
    public static String second_ENB;
    public static int second_ta;
    public static int second_Mnc;
    public static int second_Mcc;
    public static int second_Rsrp;
    public static int second_snr;
    public static String second_SimOperator;
    public static int second_rscp_3G;
    public static int second_uarfcn_3G;
    public static int second_lac_3G;
    public static int second_cid_3G;
    public static int second_Psc_3g;
    public static String second_NodeBID_3G;
    public static String second_ecno_3G;
    public static int mnc_first;
    public static int mcc_first;
    public static int secong_gsmTa;
    public static int second_rxLev;
    public static int secong_gsmPsc;
    public static int second_gsmCid;
    public static int second_gsmLac;
    public static int second_arfcn;
    public static int second_rxqual;
    public static String prim_ss;
    public static String sec_ss;
    public static int prim_carrierMode;
    public static int sec_carrierMode;
    public static int sec_slot;
    public static int prim_Slot;
    public static int subSize;
    public static String prim_NetworkType;
    public static String sec_NetworkType;
    public static int periodicFrequencyForAllServices=300;//5 min
   public static int newPeriodicFrequencyForAllService=1800;//30 min
   public static int firstTriggerForAllServices=60000;
    public static String second_rsrq;
}
