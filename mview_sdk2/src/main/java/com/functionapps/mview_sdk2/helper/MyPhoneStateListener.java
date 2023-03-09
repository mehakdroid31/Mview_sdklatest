package com.functionapps.mview_sdk2.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import androidx.annotation.RequiresApi;


import com.functionapps.mview_sdk2.main.Mview;
import com.functionapps.mview_sdk2.main.MyCall;
import com.functionapps.mview_sdk2.service.ListenService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyPhoneStateListener extends PhoneStateListener {
    private static final String TAG = "MyPhoneStateListener";
    public static String LOG_TAG = "mViewPhoneStateListener";
    public static Context mContext;
    static String ss;
    private static CellInfoNr cellInfoNr;

    private final SubscriptionManager mSubMgr;
    ArrayList<MyCall> myCallArray;
    boolean incomingCallStatus;
    boolean outgoingCallStatus;
    MyCall currentCallObject;
    ArrayList<RecordedCellLocation> last5CellLocationArr;
    ArrayList<RecordedServiceState> last5CellServiceStateArr;
    boolean bCallDropDanger = false;
    int currLocationIndex;
    int currServiceStateIndex;
    int maxLocationsToRecord = 5;
    int maxServiceStatesToRecord = 5;
    public static CellLocation lastCellLocation;
    public static ServiceState lastServiceState;
    public static SignalStrength lastSignalStrength;
    int currentSignalStrength;
    static CurrentCellServing currentCellServing;
    public static TelephonyManager telMgr;
    private static long resulttime;
    private LteParams.Paramslist obj;
    private static LteParams lteParams;
    public MyPhoneStateListener myPhoneStateListener = null;
    private String type;
    private TelephonyManager telephonyManager;
    private ArrayList<String> finalOp_Nameslte;
    private ArrayList<String> finalOpNames;
    private static CellIdentityLte cellIdentityLte;
    private static CellInfoLte cellInfoLte;
    private ArrayList<String> finalOp_Nameswcdma;
    private ArrayList<String> finalOp_Namesgsm;
    private ArrayList<String> finalOp_Names;
    static CellIdentityNr cellIdentityNr;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public MyPhoneStateListener(Context context, TelephonyManager tel) {
        Log.i("LogFapps", "Reaced");
        mContext = context;
        telMgr = tel;
        mSubMgr = SubscriptionManager.from(context);
       /* System.out.printf("for slot 1 %s%n", mSubMgr.getActiveSubscriptionInfoForSimSlotIndex(1));
       System.out.println("for slot 1 "+ Arrays.toString(mSubMgr.getSubscriptionIds(1)));
       System.out.println("for slot 2 "+ Arrays.toString(mSubMgr.getSubscriptionIds(2)));*/
        myCallArray = new ArrayList<MyCall>();
        currLocationIndex = -1;
        currServiceStateIndex = -1;
        last5CellLocationArr = new ArrayList<RecordedCellLocation>();
        last5CellServiceStateArr = new ArrayList<RecordedServiceState>();
        last5CellServiceStateArr.ensureCapacity(maxServiceStatesToRecord);
        last5CellLocationArr.ensureCapacity(maxLocationsToRecord);


    }

    @SuppressLint("MissingPermission")
    public static int getNetworkType() {

        mContext = Mview.fapps_ctx;
        TelephonyManager teleMan = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

        int networkType = teleMan.getNetworkType();
        Utils.appendLog("network typ recieved frm getNetworkType 113 is "+networkType);
        System.out.println("network type " + networkType);
        //check with Sir once
        if (networkType == 18) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return checkNetworkTypeAccToVoice(teleMan.getVoiceNetworkType());
            } else {
                mView_HealthStatus.onlyCurrentNetworkState = "NS";
                mView_HealthStatus.iCurrentNetworkState = 0;
                return mView_HealthStatus.iCurrentNetworkState;
            }
        } else {
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    mView_HealthStatus.onlyCurrentNetworkState = "2G";
                    mView_HealthStatus.iCurrentNetworkState = 2;
                    break;
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    mView_HealthStatus.onlyCurrentNetworkState = "2G";
                    mView_HealthStatus.iCurrentNetworkState = 2;
                    break;
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    mView_HealthStatus.onlyCurrentNetworkState = "2G";
                    mView_HealthStatus.iCurrentNetworkState = 2;
                    break;
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    mView_HealthStatus.onlyCurrentNetworkState = "2G";
                    mView_HealthStatus.iCurrentNetworkState = 2;
                    break;
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    mView_HealthStatus.onlyCurrentNetworkState = "2G";
                    mView_HealthStatus.iCurrentNetworkState = 2;
                    break;

                case TelephonyManager.NETWORK_TYPE_UMTS:
                    mView_HealthStatus.onlyCurrentNetworkState = "3G";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    mView_HealthStatus.onlyCurrentNetworkState = "3G";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    mView_HealthStatus.onlyCurrentNetworkState = "3G";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    mView_HealthStatus.onlyCurrentNetworkState = "3G";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    mView_HealthStatus.onlyCurrentNetworkState = "3G";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    mView_HealthStatus.onlyCurrentNetworkState = "3G";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    mView_HealthStatus.onlyCurrentNetworkState = "3G";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    mView_HealthStatus.onlyCurrentNetworkState = "3G";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    mView_HealthStatus.onlyCurrentNetworkState = "3G";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:

                    mView_HealthStatus.onlyCurrentNetworkState = "4G";
                    mView_HealthStatus.iCurrentNetworkState = 4;
                    break;
                case TelephonyManager.NETWORK_TYPE_GSM:
                    mView_HealthStatus.onlyCurrentNetworkState = "2G";
                    mView_HealthStatus.iCurrentNetworkState = 2;
                    break;
                case TelephonyManager.NETWORK_TYPE_NR:
                    mView_HealthStatus.onlyCurrentNetworkState = "5G";
                    mView_HealthStatus.currentInstance="nr";
                    mView_HealthStatus.iCurrentNetworkState = 5;
                    break;

                default:

                    mView_HealthStatus.onlyCurrentNetworkState = "NS";
                    mView_HealthStatus.iCurrentNetworkState = 0;
                    break;
            }
            return mView_HealthStatus.iCurrentNetworkState;
        }
        //return mView_HealthStatus.iCurrentNetworkState;
    /*}
        return mView_HealthStatus.iCurrentNetworkState;*/

    }

    private static String checkStringNetworkTypeAccToVoice(int voiceNetworkType) {

        String proto = "";
        String proto1 = "";
        Log.i(TAG, "Network type acc to voice " + voiceNetworkType);
        Utils.appendLog("network typ recieved frm checkStringNetworkTypeAccToVoice 219 is "+voiceNetworkType);

        switch (voiceNetworkType) {

            case TelephonyManager.NETWORK_TYPE_GPRS:
                proto = "2G (GPRS)";
                proto1 = "GPRS";
                mView_HealthStatus.iCurrentNetworkState = 2;
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                proto = "2G (EDGE)";
                proto1 = "EDGE";
                mView_HealthStatus.iCurrentNetworkState = 2;
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                proto = "2G (CDMA)";
                proto1 = "CDMA";
                mView_HealthStatus.iCurrentNetworkState = 2;
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                proto = "2G (1xRTT)";
                proto1 = "1xRTT";
                mView_HealthStatus.iCurrentNetworkState = 2;
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                proto = "2G (IDEN)";
                proto1 = "IDEN";
                mView_HealthStatus.iCurrentNetworkState = 2;
                break;

            case TelephonyManager.NETWORK_TYPE_UMTS:
                proto = "3G (UMTS)";
                proto1 = "UMTS";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                proto = "3G (EVDO_0)";
                proto1 = "EVDO_0";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                proto = "3G (EVDO_A)";
                proto1 = "EVDO_A";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                proto = "3G (HSDPA)";
                proto1 = "HSDPA";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                proto = "3G (HSUPA)";
                proto1 = "HSUPA";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                proto = "3G (HSPA)";
                proto1 = "HSPA";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                proto = "3G (EVDO_B)";
                proto1 = "EVDO_B";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                proto = "3G (EHRPD)";
                proto1 = "EHRPD";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                proto = "3G (HSPAP)";
                proto1 = "HSPAP";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                proto = "4G(LTE)";
                proto1 = "LTE";
                mView_HealthStatus.iCurrentNetworkState = 4;
                break;
            case TelephonyManager.NETWORK_TYPE_GSM:
                proto = "2G (GSM)";
                proto1 = "GSM";
                mView_HealthStatus.iCurrentNetworkState = 2;

                break;
            case TelephonyManager.NETWORK_TYPE_NR:
                proto = "5G";
                proto1 = "NR";
                mView_HealthStatus.iCurrentNetworkState = 5;

                break;
            default:
                proto = "NS";
                proto1 = "NS";
                mView_HealthStatus.iCurrentNetworkState = 0;
        }
        Log.i(TAG, "Returning value of current state network " + mView_HealthStatus.iCurrentNetworkState + " network is " + mView_HealthStatus.onlyCurrentNetworkState);
        mView_HealthStatus.strCurrentNetworkState = proto;
        mView_HealthStatus.strCurrentNetworkProtocol = proto1;
        return proto;
    }

    private static int checkNetworkTypeAccToVoice(int voiceNetworkType) {
        Log.i(TAG, "Network type acc to voice " + voiceNetworkType);
        Utils.appendLog("network typ recieved frm checkNetworkTypeAccToVoice 328 is "+voiceNetworkType);

        switch (voiceNetworkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:

                mView_HealthStatus.onlyCurrentNetworkState = "2G";
                mView_HealthStatus.iCurrentNetworkState = 2;
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:

                mView_HealthStatus.onlyCurrentNetworkState = "2G";
                mView_HealthStatus.iCurrentNetworkState = 2;
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:

                mView_HealthStatus.onlyCurrentNetworkState = "2G";
                mView_HealthStatus.iCurrentNetworkState = 2;
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:

                mView_HealthStatus.onlyCurrentNetworkState = "2G";
                mView_HealthStatus.iCurrentNetworkState = 2;
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN:

                mView_HealthStatus.onlyCurrentNetworkState = "2G";
                mView_HealthStatus.iCurrentNetworkState = 2;
                break;

            case TelephonyManager.NETWORK_TYPE_UMTS:
                mView_HealthStatus.onlyCurrentNetworkState = "3G";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                mView_HealthStatus.onlyCurrentNetworkState = "3G";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                mView_HealthStatus.onlyCurrentNetworkState = "3G";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                mView_HealthStatus.onlyCurrentNetworkState = "3G";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                mView_HealthStatus.onlyCurrentNetworkState = "3G";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                mView_HealthStatus.onlyCurrentNetworkState = "3G";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                mView_HealthStatus.onlyCurrentNetworkState = "3G";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                mView_HealthStatus.onlyCurrentNetworkState = "3G";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                mView_HealthStatus.onlyCurrentNetworkState = "3G";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:

                mView_HealthStatus.onlyCurrentNetworkState = "4G";
                mView_HealthStatus.iCurrentNetworkState = 4;
                break;

            case TelephonyManager.NETWORK_TYPE_NR:

                mView_HealthStatus.onlyCurrentNetworkState = "5G";
                mView_HealthStatus.iCurrentNetworkState = 5;
                break;

            default:

                mView_HealthStatus.onlyCurrentNetworkState = "NS";
                mView_HealthStatus.iCurrentNetworkState = 0;
                break;
        }
        Log.i(TAG, "Returning value of current state network " + mView_HealthStatus.iCurrentNetworkState + " network is " + mView_HealthStatus.onlyCurrentNetworkState);
        //  mView_HealthStatus.onlyCurrentNetworkState=  mView_HealthStatus.iCurrentNetworkState;
        mView_HealthStatus.strCurrentNetworkProtocol = mView_HealthStatus.strCurrentNetworkState;
        return mView_HealthStatus.iCurrentNetworkState;

    }

    public static String getSignalStrengthForSec() {
        String sec_strength = null;
        if (mView_HealthStatus.second_cellInstance != null) {

            if (mView_HealthStatus.second_cellInstance.equalsIgnoreCase("lte")) {
                {
                    if (Integer.parseInt(mView_HealthStatus.lteRSRP) < 0 && Integer.parseInt(mView_HealthStatus.lteRSRP) > -75) {
                        sec_strength = mView_HealthStatus.second_Rsrp + "dbm ( Good )";
                    } else if (Integer.parseInt(mView_HealthStatus.lteRSRP) <= -75 && Integer.parseInt(mView_HealthStatus.lteRSRP) > -95) {
                        sec_strength = mView_HealthStatus.second_Rsrp + "dbm ( Fine )";
                    } else if (Integer.parseInt(mView_HealthStatus.lteRSRP) <= -95 && Integer.parseInt(mView_HealthStatus.lteRSRP) > -115) {
                        sec_strength = mView_HealthStatus.second_Rsrp + "dbm ( Poor )";
                    }
                }


            } else if (mView_HealthStatus.second_cellInstance.equalsIgnoreCase("wcdma")) {
                if (mView_HealthStatus.second_rscp_3G < 0 && mView_HealthStatus.second_rscp_3G > -75) {
                    sec_strength = mView_HealthStatus.second_rscp_3G + "  dbm (Good)";
                } else if (mView_HealthStatus.second_rscp_3G <= -75 && mView_HealthStatus.second_rscp_3G > -95) {
                    sec_strength = mView_HealthStatus.second_rscp_3G + "  dbm (Fine)";
                } else if (mView_HealthStatus.second_rscp_3G <= -95 && mView_HealthStatus.second_rscp_3G > -115) {
                    sec_strength = mView_HealthStatus.second_rscp_3G + " dbm (Poor) ";
                }


            } else if (mView_HealthStatus.second_cellInstance.equalsIgnoreCase("gsm")) {
                if (mView_HealthStatus.second_rxLev < 0 && mView_HealthStatus.second_rxLev > -75) {
                    sec_strength = mView_HealthStatus.second_rxLev + " dbm (Good)";
                } else if (mView_HealthStatus.second_rxLev <= -75 && mView_HealthStatus.second_rxLev > -95) {
                    sec_strength = mView_HealthStatus.second_rxLev + "  dbm (Fine)";
                } else if (mView_HealthStatus.second_rxLev <= -95 && mView_HealthStatus.second_rxLev > -115) {
                    sec_strength = mView_HealthStatus.second_rxLev + "dbm (Poor)";
                }
            }
        }
        return sec_strength;

    }

    public static String getSignalStrengthForPrim() {
        String strength = null;
        if (mView_HealthStatus.currentInstance.equalsIgnoreCase("lte")) {
            {
                if (mView_HealthStatus.lteRSRP != null) {
                    if (Integer.parseInt(mView_HealthStatus.lteRSRP) < 0 && Integer.parseInt(mView_HealthStatus.lteRSRP) > -75) {
                        strength = mView_HealthStatus.lteRSRP + "dbm ( Good )";
                    } else if (Integer.parseInt(mView_HealthStatus.lteRSRP) <= -75 && Integer.parseInt(mView_HealthStatus.lteRSRP) > -95) {
                        strength = mView_HealthStatus.lteRSRP + "dbm ( Fine )";
                    } else if (Integer.parseInt(mView_HealthStatus.lteRSRP) <= -95 && Integer.parseInt(mView_HealthStatus.lteRSRP) > -115) {
                        strength = mView_HealthStatus.lteRSRP + "dbm ( Poor )";
                    }
                }
            }
        } else if (mView_HealthStatus.currentInstance.equalsIgnoreCase("wcdma")) {
            if (mView_HealthStatus.rscp != null) {
                if (Integer.parseInt(mView_HealthStatus.rscp) < 0 && Integer.parseInt(mView_HealthStatus.rscp) > -75) {
                    strength = mView_HealthStatus.rscp + "  dbm (Good)";
                } else if (Integer.parseInt(mView_HealthStatus.rscp) <= -75 && Integer.parseInt(mView_HealthStatus.rscp) > -95) {
                    strength = mView_HealthStatus.rscp + "  dbm (Fine)";
                } else if (Integer.parseInt(mView_HealthStatus.rscp) <= -95 && Integer.parseInt(mView_HealthStatus.rscp) > -115) {
                    strength = mView_HealthStatus.rscp + " dbm (Poor) ";
                }
            }
        } else if (mView_HealthStatus.currentInstance.equalsIgnoreCase("gsm")) {
            if (mView_HealthStatus.gsmSignalStrength < 0 && mView_HealthStatus.gsmSignalStrength > -75) {
                strength = mView_HealthStatus.gsmSignalStrength + " dbm (Good)";
            } else if (mView_HealthStatus.gsmSignalStrength <= -75 && mView_HealthStatus.gsmSignalStrength > -95) {
                strength = mView_HealthStatus.gsmSignalStrength + "  dbm (Fine)";
            } else if (mView_HealthStatus.gsmSignalStrength <= -95 && mView_HealthStatus.gsmSignalStrength > -115) {
                strength = mView_HealthStatus.gsmSignalStrength + "dbm (Poor)";
            }

        }


        return strength;
    }

    public static void sendcontext(Context ctx) {
        mContext = ctx;
    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCellInfoChanged(List<CellInfo> cellInfo) {


        super.onCellInfoChanged(cellInfo);
        //  Utils.showToast(mContext,"cell info changed called");

        Log.i("LogFapps", "onCellInfoChanged");

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        long curr = System.currentTimeMillis();

        String displaydate = sdf.format(curr);
        //System.out.println("current time in celllinfo"+displaydate);
        try {
            if (telMgr != null) {
                if (mContext != null) {


                      /*  cellInfo = ListenService.telMgr.getAllCellInfo();
                        if ((cellInfo != null) && cellInfo.size() > 0) {
                            fetchCellsInfo(cellInfo);
                        }
*/

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //    Log.i(LOG_TAG, "onCellInfoChanged: " + cellInfo);
       /* if (cellInfo != null) {
            for (CellInfo m : cellInfo) {
                if (m instanceof CellInfoLte) {
                    CellInfoLte cellInfoLte = (CellInfoLte) m;
                    cellInfoLte.getCellIdentity().getPci();
                    //	Log.d("onCellInfoChanged", "CellInfoLte--" + m);
                }
            }
        }*/
    }

    private void writeLogToFile() {
        call_checks();
        String log = "";
        boolean b = mView_HealthStatus.writeCallLogs; //isExternalStorageWritable();
        if (b) {
            System.out.println("in call if condition");
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            File file = new File(path, "mview");
            if (!file.exists()) {
                file.mkdirs();
            }
            //  file = new File(path, "mview" + "/" + "calllog.txt");
            Utils.deletefileFromFileManager(path + "/mview" + "/" + "calllog.txt");
            FileWriter out;
//            try {
//                out = new FileWriter(file, true);
//                ///////////
//                if (currentCallObject.calltype == 1)
//                    out.append("Incoming Phone Number " + currentCallObject.callerPhoneNumber + "\n");
//                else
//                    out.append("Outgoing Phone Number " + currentCallObject.callerPhoneNumber + "\n");
//
//                out.append("Call Start " + currentCallObject.timeofCall.toString() + "\n");
//                out.append("Call End " + currentCallObject.endTime.toString() + "\n");
//                String callTaken = "No";
//                if (currentCallObject.isCallTaken)
//                    callTaken = "Yes";
//                out.append("Call Answered = " + callTaken + "\n");
//                out.append(currentCallObject.myLat + " ");
//                out.append(currentCallObject.myLon + "\n");
//                out.append("Call dropped = " + currentCallObject.isDroppedCall + "\n");
//
//                if (currentCallObject.cellLocationArr.size() == 0) {
//                    if (last5CellLocationArr.size() == 0) {
//                        if (lastCellLocation != null) {
//                            out.append("##Last Cell Location\n");
//                            if (lastCellLocation instanceof GsmCellLocation) {
//                                GsmCellLocation gcLoc = (GsmCellLocation) lastCellLocation;
//                                out.append(gcLoc.toString() + "\n");
//                            }
//                        }
//                    } else {
//                        out.append("##Total Cell Locations Captured = " + last5CellLocationArr.size() + "\n");
//                        for (int i = 0; i < last5CellLocationArr.size(); i++) {
//                            if (last5CellLocationArr.get(i).loc instanceof GsmCellLocation) {
//                                GsmCellLocation gcLoc = (GsmCellLocation) last5CellLocationArr.get(i).loc;
//                                out.append(gcLoc.toString() + "\n" + last5CellLocationArr.get(i).myLat + " " + last5CellLocationArr.get(i).myLon + " " + last5CellLocationArr.get(i).dt + "\n");
//                            }
//                        }
//                    }
//                }
//                for (int i = 0; i < currentCallObject.cellLocationArr.size(); i++) {
//                    if (i == 0)
//                        out.append("##Cell Location Info\n");
//
//                    if (currentCallObject.cellLocationArr.get(i) instanceof GsmCellLocation) {
//                        GsmCellLocation gcLoc = (GsmCellLocation) currentCallObject.cellLocationArr.get(i);
//                        out.append(gcLoc.toString() + "\n");
//                        /*Log.i(LOG_TAG,
//                                "onCellLocationChanged: GsmCellLocation "
//										+ gcLoc.toString());
//						Log.i(LOG_TAG, "onCellLocationChanged: GsmCellLocation getCid "
//								+ gcLoc.getCid());
//						Log.i(LOG_TAG, "onCellLocationChanged: GsmCellLocation getLac "
//								+ gcLoc.getLac());
//						Log.i(LOG_TAG, "onCellLocationChanged: GsmCellLocation getPsc"
//								+ gcLoc.getPsc()); // Requires min API 9*/
//                    }
//
//                }
//                if (last5CellServiceStateArr.size() > 0) {
//                    out.append("@@Total Service State captured = " + last5CellServiceStateArr.size() + "\n");
//                    for (int i = 0; i < last5CellServiceStateArr.size(); i++) {
//                        String state = "";
//                        switch (last5CellServiceStateArr.get(i).service.getState()) {
//                            case ServiceState.STATE_IN_SERVICE:
//                                state = "STATE_IN_SERVICE";
//                                break;
//                            case ServiceState.STATE_OUT_OF_SERVICE:
//                                state = "STATE_OUT_OF_SERVICE";
//                                break;
//                            case ServiceState.STATE_EMERGENCY_ONLY:
//                                state = "STATE_EMERGENCY_ONLY";
//
//                                break;
//                            case ServiceState.STATE_POWER_OFF:
//                                state = "STATE_POWER_OFF";
//
//                                break;
//                        }
//                        out.append("-> " + state + " " + last5CellServiceStateArr.get(i).service.toString() + "\n" + last5CellServiceStateArr.get(i).myLat + " " + last5CellServiceStateArr.get(i).myLon + " " + last5CellServiceStateArr.get(i).dt + "\n");
//                    }
//                }
//
//                for (int i = 0; i < currentCallObject.serviceStateArr.size(); i++) {
//                    if (i == 0)
//                        out.append("@@Service State Info\n");
//
//                    String state = "";
//                    switch (currentCallObject.serviceStateArr.get(i).getState()) {
//                        case ServiceState.STATE_IN_SERVICE:
//                            state = "STATE_IN_SERVICE";
//                            break;
//                        case ServiceState.STATE_OUT_OF_SERVICE:
//                            state = "STATE_OUT_OF_SERVICE";
//
//                            break;
//                        case ServiceState.STATE_EMERGENCY_ONLY:
//                            state = "STATE_EMERGENCY_ONLY";
//                            break;
//                        case ServiceState.STATE_POWER_OFF:
//                            state = "STATE_POWER_OFF";
//                            break;
//                    }
//                    out.append("-> " + state + " " + currentCallObject.serviceStateArr.get(i).toString() + "\n");
//                }
//                out.append("##Total Signal Strength Info captured = " + currentCallObject.signalStrengthArr.size() + "\n");
//                for (int ii = 0; ii < currentCallObject.signalStrengthArr.size(); ii++) {
//                    int getGsmBitErrorRate = 0;
//                    int getGsmSignalStrength = 0;
//                    if (currentCallObject.signalStrengthArr.get(ii).isGsm()) {
//                        getGsmBitErrorRate = currentCallObject.signalStrengthArr.get(ii).getGsmBitErrorRate();
//                        getGsmSignalStrength = currentCallObject.signalStrengthArr.get(ii).getGsmSignalStrength();
//                    }
//
//                    out.append(getGsmSignalStrength + " " + currentCallObject.signalStrengthArr.get(ii).toString() + "\n");
//                }
//
//                //JSONArray jarr = WebService.getCallRecordJSON(currentCallObject);
//                //String s = jarr.toString();
//                //out.append("______________________________________________"+"\n");
//                //out.append(s);
//                out.append("______________________________________________" + "\n");
//                //////////////
//                //out.append(log);
//                out.flush();
//                out.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }

        }

        try {
            //System.out.println("about to call web api ");
            //Toast.makeText(mContext, "about to call web api", Toast.LENGTH_SHORT).show();
            JSONObject callJson = new JSONObject();
            System.out.println("calling curr call obj value " + currentCallObject.myLon + " " + currentCallObject.myLat);
            String temp = "M";
            long dura = 0;
            if (currentCallObject.isCallTaken) {
                temp = "S";
            }
            if (currentCallObject.isDroppedCall)
                temp = "F";

            dura = currentCallObject.endTimeInMS - currentCallObject.timeofcallInMS;
            callJson.put("CALLType", temp);
            callJson.put("InorOut", currentCallObject.calltype);//1 means incoming, 2 means outgoing
            if (currentCallObject.operator == null && telMgr != null) {
                currentCallObject.operator = telMgr.getNetworkOperatorName();
            }

            callJson.put("operatorname", currentCallObject.operator);
            if (currentCallObject.callerPhoneNumber != null)
                callJson.put("Incoming", currentCallObject.callerPhoneNumber);//for outgoing as well
            else
                callJson.put("Incoming", 0);

            callJson.put("speed", currentCallObject.speed);
            callJson.put("datetime", currentCallObject.timeofcallInMSNew);
            callJson.put("duration", dura);

            callJson.put("disconnectcause", currentCallObject.disconnectCause);
            if (currentCallObject.isRoaming)
                callJson.put("roaming", "1");
            else
                callJson.put("roaming", "0");


            JSONArray detailsArray = new JSONArray();
            detailsArray.put(callJson);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void call_checks() {
        //System.out.println("called call_checks");
        for (int i = 0; i < last5CellServiceStateArr.size(); i++) {
            String state = "";
            switch (last5CellServiceStateArr.get(i).service.getState()) {
                case ServiceState.STATE_IN_SERVICE:
                    state = "STATE_IN_SERVICE";
                    break;
                case ServiceState.STATE_OUT_OF_SERVICE:
                    state = "STATE_OUT_OF_SERVICE";
                    if (i == currServiceStateIndex) {
                        if (currentCallObject.isCallTaken) {
                            mView_HealthStatus.iCall_Success--;

                            //			Toast.makeText(mContext, "decreasing call success", Toast.LENGTH_SHORT).show();
                        } else {
                            mView_HealthStatus.iCall_Missed--;
                        }
                        //Toast.makeText(mContext, "missed calls in outofservice "+mView_HealthStatus.iCall_Missed, //Toast.LENGTH_SHORT).show();

                        mView_HealthStatus.iCall_Failed++;
                        currentCallObject.isDroppedCall = true;
                        currentCallObject.disconnectCause = "STATE_OUT_OF_SERVICE";
                    }

                    break;
                case ServiceState.STATE_EMERGENCY_ONLY:
                    state = "STATE_EMERGENCY_ONLY";
                    if (i == currServiceStateIndex) {
                        mView_HealthStatus.iCall_Failed++;
                        if (currentCallObject.isCallTaken) {
                            mView_HealthStatus.iCall_Success--;
                            //	Toast.makeText(mContext, "decreasing call success", Toast.LENGTH_SHORT).show();
                        } else {
                            mView_HealthStatus.iCall_Missed--;

                        }
                        //Toast.makeText(mContext, "missed calls in emergency "+mView_HealthStatus.iCall_Missed, //Toast.LENGTH_SHORT).show();
                        currentCallObject.isDroppedCall = true;
                        currentCallObject.disconnectCause = "STATE_EMERGENCY_ONLY";
                    }
                    break;
                case ServiceState.STATE_POWER_OFF:
                    state = "STATE_POWER_OFF";
                    if (i == currServiceStateIndex) {
                        mView_HealthStatus.iCall_Failed++;


                        if (currentCallObject.isCallTaken) {
                            mView_HealthStatus.iCall_Success--;

                            //	Toast.makeText(mContext, "decreasing call success", Toast.LENGTH_SHORT).show();
                        } else {
                            mView_HealthStatus.iCall_Missed--;

                        }
                        //Toast.makeText(mContext, "missed calls in poweroff "+mView_HealthStatus.iCall_Missed, //Toast.LENGTH_SHORT).show();
                        currentCallObject.isDroppedCall = true;
                        currentCallObject.disconnectCause = "STATE_POWER_OFF";
                    }
                    break;
            }
            //out.append("-> " + state + " " + last5CellServiceStateArr.get(i).service.toString()+ "\n" + last5CellServiceStateArr.get(i).myLat + " " + last5CellServiceStateArr.get(i).myLon + " " + last5CellServiceStateArr.get(i).dt + "\n");
        }//end for loop
    }//end function

    @Override
    public void onDataConnectionStateChanged(int state, int networkType) {
        super.onDataConnectionStateChanged(state, networkType);
        int oldstate = -1;
        if (mView_HealthStatus.iCurrentNetworkState != -1) {
            if (mView_HealthStatus.starTimeInCurrentState == 0) {
                mView_HealthStatus.starTimeInCurrentState = System.currentTimeMillis();
            }

            if (mView_HealthStatus.iCurrentNetworkState == 4) {
                mView_HealthStatus.timein4G += (System.currentTimeMillis() - mView_HealthStatus.starTimeInCurrentState);
            } else if (mView_HealthStatus.iCurrentNetworkState == 3) {

                mView_HealthStatus.timein3G += (System.currentTimeMillis() - mView_HealthStatus.starTimeInCurrentState);
            } else if (mView_HealthStatus.iCurrentNetworkState == 2) {
                mView_HealthStatus.timein2G += (System.currentTimeMillis() - mView_HealthStatus.starTimeInCurrentState);
            } else if (mView_HealthStatus.iCurrentNetworkState == 0) {
                mView_HealthStatus.timeinNS += (System.currentTimeMillis() - mView_HealthStatus.starTimeInCurrentState);
            }
            oldstate = mView_HealthStatus.iCurrentNetworkState;
        }
        mView_HealthStatus.starTimeInCurrentState = System.currentTimeMillis();
        String proto = "";
        mView_HealthStatus.iCurrentNetworkState = 2;
        mView_HealthStatus.onlyCurrentNetworkState = "3G";
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                proto = "2G (GPRS)";
                mView_HealthStatus.onlyCurrentNetworkState = "2G";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                proto = "2G (EDGE)";
                mView_HealthStatus.onlyCurrentNetworkState = "2G";
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                proto = "2G (CDMA)";
                mView_HealthStatus.onlyCurrentNetworkState = "2G";
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                proto = "2G (1xRTT)";
                mView_HealthStatus.onlyCurrentNetworkState = "2G";
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                proto = "2G (IDEN)";
                mView_HealthStatus.onlyCurrentNetworkState = "2G";
                break;

            case TelephonyManager.NETWORK_TYPE_UMTS:
                proto = "3G (UMTS)";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                proto = "3G (EVDO_0)";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                proto = "3G (EVDO_A)";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                proto = "3G (HSDPA)";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                proto = "3G (HSUPA)";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                proto = "3G (HSPA)";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                proto = "3G (EVDO_B)";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                proto = "3G (EHRPD)";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                proto = "3G (HSPAP)";
                mView_HealthStatus.iCurrentNetworkState = 3;
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                proto = "4G";
                mView_HealthStatus.onlyCurrentNetworkState = "4G";
                mView_HealthStatus.iCurrentNetworkState = 4;
                break;
            default:
                proto = "NS";
                mView_HealthStatus.onlyCurrentNetworkState = "NS";
                mView_HealthStatus.iCurrentNetworkState = 0;
                break;
        }
        mView_HealthStatus.strCurrentNetworkState = proto;
        if (oldstate == -1) {
            if (mView_HealthStatus.iCurrentNetworkState == 4) {
                mView_HealthStatus.timein4G += (System.currentTimeMillis() - mView_HealthStatus.starTimeInCurrentState) + 100;
            } else if (mView_HealthStatus.iCurrentNetworkState == 3) {
                mView_HealthStatus.timein3G += (System.currentTimeMillis() - mView_HealthStatus.starTimeInCurrentState) + 100;
            } else if (mView_HealthStatus.iCurrentNetworkState == 2) {
                mView_HealthStatus.timein2G += (System.currentTimeMillis() - mView_HealthStatus.starTimeInCurrentState) + 100;
            } else if (mView_HealthStatus.iCurrentNetworkState == 0) {
                mView_HealthStatus.timein3G = 0;
                mView_HealthStatus.timeinNS += (System.currentTimeMillis() - mView_HealthStatus.starTimeInCurrentState) + 100;
            }
        }
    }

    //  CODE ############################
    public static String getLTERSSI() {
        try {
//            if (this.NetworkType != 13) {
//                return "-";
//            }
            if (Integer.parseInt(mView_HealthStatus.lteRSSI) == 99 || Integer.parseInt(mView_HealthStatus.lteRSSI) == -1) {
                return "0";

            }
            if (Integer.parseInt(mView_HealthStatus.lteRSSI) >= 0) {
                return String.valueOf(Integer.parseInt(mView_HealthStatus.lteRSSI) - 94);
            }
            return String.valueOf((-1 * Integer.parseInt(mView_HealthStatus.lteRSSI)) - 94);
        } catch (Exception e) {
            return "0";
        }
    }

    public static int NetworkType;

    public static String getSignalStrength() {

        if (mView_HealthStatus.iCurrentNetworkState == 4) {
            if (mView_HealthStatus.lteRSRP != null)
                ss = mView_HealthStatus.lteRSRP + "dbm";

        } else if (mView_HealthStatus.iCurrentNetworkState == 3) {
            if (mView_HealthStatus.rscp != null)
                ss = mView_HealthStatus.rscp + "dbm";


        } else if (mView_HealthStatus.iCurrentNetworkState == 2) {
            ss = MyPhoneStateListener.getRxLev() + "dbm";
        }
        return ss;
    }

    @SuppressLint("MissingPermission")
    public static int getRxLev() {
        if (telMgr != null) {

            NetworkType = telMgr.getNetworkType();
            try {
                if (NetworkType == 1 || NetworkType == 2) {
                    if (mView_HealthStatus.RxLev == 99 || mView_HealthStatus.RxLev == 9999) {
                        return -200;
                    }
                    if (mView_HealthStatus.RxLev >= 0) {
                        return (mView_HealthStatus.RxLev * 2) - 113;
                    }
                    return mView_HealthStatus.RxLev;
                } else if (NetworkType == 0) {
//                if (!MainActivity.discardnetworktypenull.booleanValue()) {
//                    return -200;
//                }
                    if (mView_HealthStatus.iCurrentNetworkState == 2) {//this.nettech.equals("2G")) {
                        if (mView_HealthStatus.RxLev == 99 || mView_HealthStatus.RxLev == 9999) {
                            return -200;
                        }
                        if (mView_HealthStatus.RxLev >= 0) {
                            return (mView_HealthStatus.RxLev * 2) - 113;
                        }
                        return mView_HealthStatus.RxLev;
                    } else if (mView_HealthStatus.iCurrentNetworkState == 3) { //this.nettech.equals("3G")) {
                        if (mView_HealthStatus.CDMARSSI > -120 && mView_HealthStatus.CDMARSSI <= -32) {
                            return mView_HealthStatus.CDMARSSI;
                        }
                        if (mView_HealthStatus.RxLev == 99 || mView_HealthStatus.RxLev == 9999) {
                            return -200;
                        }
                        if (mView_HealthStatus.RxLev >= 0) {
                            return (mView_HealthStatus.RxLev * 2) - 113;

                        }
                        return mView_HealthStatus.RxLev;
                    } else if (mView_HealthStatus.iCurrentNetworkState == 4) { //this.nettech.equals("4G")) {
                        if (Integer.parseInt(mView_HealthStatus.lteRSRP) < -2 && Integer.parseInt(mView_HealthStatus.lteRSRP) > -200) {
                            return Integer.parseInt(mView_HealthStatus.lteRSRP);
                        }
                        if (mView_HealthStatus.RxLev == 99 || mView_HealthStatus.RxLev == 9999) {
                            return -200;
                        }
                        return mView_HealthStatus.RxLev - 140;
                    } else if (mView_HealthStatus.tddphone.booleanValue()) {
                        return mView_HealthStatus.CDMARSSI;
                    } else {
                        return -200;
                    }
                } else if (NetworkType == 3 || NetworkType == 8 || NetworkType == 9 || NetworkType == 10 || NetworkType == 15) {
                    if (mView_HealthStatus.CDMARSSI > -120 && mView_HealthStatus.CDMARSSI <= -32) {
                        System.out.println("signal val 2" + mView_HealthStatus.CDMARSSI);
                        return mView_HealthStatus.CDMARSSI;
                    }
                    if (mView_HealthStatus.RxLev == 99 || mView_HealthStatus.RxLev == 9999) {
                        System.out.println("signal val 3" + mView_HealthStatus.CDMARSSI);
                        return -200;
                    }
                    if (mView_HealthStatus.RxLev >= 0) {
                        System.out.println("signal val 4" + mView_HealthStatus.CDMARSSI);
                        return (mView_HealthStatus.RxLev * 2) - 113;
                    }
                    return mView_HealthStatus.RxLev;
                } else if (NetworkType == 4 || NetworkType == 7) {
                    if (mView_HealthStatus.CDMARSSI != -1 && mView_HealthStatus.CDMARSSI != -120) {
                        return mView_HealthStatus.CDMARSSI;
                    }
                    if (mView_HealthStatus.RxLev == 99 || mView_HealthStatus.RxLev == 9999) {
                        return -200;
                    }
                    return (mView_HealthStatus.RxLev * 2) - 113;
                } else if (NetworkType == 5 || NetworkType == 6 || NetworkType == 12 || NetworkType == 14) {
                    if (mView_HealthStatus.EVDORSSI != -1 && mView_HealthStatus.EVDORSSI != -120) {
                        return mView_HealthStatus.EVDORSSI;
                    }
                    if (mView_HealthStatus.RxLev == 99 || mView_HealthStatus.RxLev == 9999) {
                        return -200;
                    }
                    return (mView_HealthStatus.RxLev * 2) - 113;
                } else if (NetworkType != 13) {
                    return -200;
                } else {
                    if (Integer.parseInt(mView_HealthStatus.lteRSRP) < -2 && Integer.parseInt(mView_HealthStatus.lteRSRP) > -200) {
                        return Integer.parseInt(mView_HealthStatus.lteRSRP);
                    }
                    if (mView_HealthStatus.RxLev == 99 || mView_HealthStatus.RxLev == 9999) {
                        return -200;
                    }
                    return mView_HealthStatus.RxLev - 140;
                }
            } catch (Exception e) {
                return -200;
            }
        } else {
            return -200;
        }
    }

    @SuppressLint("MissingPermission")
    public static String getCQI() {
        if (telMgr != null) {
            NetworkType = telMgr.getNetworkType();
            try {
                if (NetworkType == 0 || NetworkType == 1 || NetworkType == 2) {
                    return "0";
                }
                if (NetworkType == 3 || NetworkType == 8 || NetworkType == 9 || NetworkType == 10 || NetworkType == 15) {
                    return "0";
                }
                if (NetworkType == 4 || NetworkType == 7) {
                    return "0";
                }
                if (NetworkType == 5 || NetworkType == 6 || NetworkType == 12 || NetworkType == 14) {
                    return "0";
                }
                if (NetworkType != 13) {
                    return "0";
                }
                if (Integer.parseInt(mView_HealthStatus.lteCQI) == -1 || Integer.parseInt(mView_HealthStatus.lteCQI) >= 100
                        || Integer.parseInt(mView_HealthStatus.lteCQI) < 0) {
                    return "0";
                }
                return mView_HealthStatus.lteCQI; //String.valueOf(this.LTECQI);
            } catch (Exception e) {
                return "0";
            }
        }
        return "0";
    }

    @SuppressLint("MissingPermission")
    public static String getSNR() {
        if (telMgr != null) {
            /*
             /*
             * @see #NETWORK_TYPE_UNKNOWN
             * @see #NETWORK_TYPE_GPRS
             * @see #NETWORK_TYPE_EDGE
             * @see #NETWORK_TYPE_UMTS
             * @see #NETWORK_TYPE_HSDPA
             * @see #NETWORK_TYPE_HSUPA
             * @see #NETWORK_TYPE_HSPA
             * @see #NETWORK_TYPE_CDMA
             * @see #NETWORK_TYPE_EVDO_0
             * @see #NETWORK_TYPE_EVDO_A
             * @see #NETWORK_TYPE_EVDO_B
             * @see #NETWORK_TYPE_1xRTT
             * @see #NETWORK_TYPE_IDEN
             * @see #NETWORK_TYPE_LTE
             * @see #NETWORK_TYPE_EHRPD
             * @see #NETWORK_TYPE_HSPAP
             * @see #NETWORK_TYPE_NR
             */

            NetworkType = telMgr.getNetworkType();
            try {
                if (NetworkType == 0 || NetworkType == 1 || NetworkType == 2) {
                    return "0";
                }
                if (NetworkType == 3 || NetworkType == 8 || NetworkType == 9 || NetworkType == 10 || NetworkType == 15) {
                    return "0";
                }
                if (NetworkType == 4 || NetworkType == 7) {
                    return "0";
                }
                if (NetworkType == 5 || NetworkType == 6 || NetworkType == 12 || NetworkType == 14) {
                    if (mView_HealthStatus.EVDOSNR <= -1 || mView_HealthStatus.EVDOSNR >= 50) {
                        return "0";
                    }
                    return String.valueOf(mView_HealthStatus.EVDOSNR);
                } else if (NetworkType != 13) {
                    return "0";
                } else {
                    if (Integer.parseInt(mView_HealthStatus.lteSNR) == -99.0d || Integer.parseInt(mView_HealthStatus.lteSNR) < -50.0d || Integer.parseInt(mView_HealthStatus.lteSNR) >= 80.0d) {
                        return "0";
                    }
                    return mView_HealthStatus.lteSNR; //String.valueOf(mView_HealthStatus.LTESNR);
                }
            } catch (Exception e) {
                return "0";
            }

        }
        return "0";
    }

    public void decodesignalstrength(SignalStrength signalStrength) {
        String SignalString = signalStrength.toString().replace("SignalStrength:", "");

        String[] signal = signalStrength.toString().split(" ");
        System.out.println("4g signal " + Arrays.toString(signal));

        System.out.println("cdma dbm " + signalStrength.getCdmaDbm() + "evdo dbm " + signalStrength.getEvdoDbm());

        if (mView_HealthStatus.phonetype.equals("CDMA") || mView_HealthStatus.phonetype.equals("")) {
            try {
                mView_HealthStatus.CDMARSSI = Integer.parseInt(signal[3]);
            } catch (Exception e) {
            }
            try {
                mView_HealthStatus.CDMAECIO = Integer.parseInt(signal[4]) / 10;
            } catch (Exception e2) {
            }
            try {
                mView_HealthStatus.EVDORSSI = Integer.parseInt(signal[5]);
            } catch (Exception e3) {
            }
            try {
                mView_HealthStatus.EVDOECIO = Integer.parseInt(signal[6]) / 10;
            } catch (Exception e4) {
            }
            try {
                mView_HealthStatus.EVDOSNR = Integer.parseInt(signal[7]);
            } catch (Exception e5) {
            }
        } else {
            mView_HealthStatus.RxLev = signalStrength.getGsmSignalStrength();
            mView_HealthStatus.BER = signalStrength.getGsmBitErrorRate();
            mView_HealthStatus.CDMARSSI = signalStrength.getCdmaDbm();
            mView_HealthStatus.CDMAECIO = signalStrength.getCdmaEcio();
            mView_HealthStatus.EVDORSSI = signalStrength.getEvdoDbm();
            mView_HealthStatus.EVDOECIO = signalStrength.getEvdoEcio() / 10;
            mView_HealthStatus.evdoecio = signalStrength.getEvdoEcio();
            mView_HealthStatus.EVDOSNR = signalStrength.getEvdoSnr();
            //lteParams.paramslist.Ecno=String.valueOf(mView_HealthStatus.EVDOECIO);

        }
        try {
            if (signalStrength.toString().contains("gw")) {
                mView_HealthStatus.tddphone = Boolean.valueOf(true);
                try {
                    mView_HealthStatus.CDMARSSI = Integer.parseInt(signal[3]);
                } catch (Exception e6) {
                }
                try {
                    mView_HealthStatus.CDMAECIO = Integer.parseInt(signal[4]);
                } catch (Exception e7) {
                }
                try {
                    mView_HealthStatus.lteRSSI = signal[10];
                    System.out.println("signal vals rssi from 1" + signal[10]);
                } catch (Exception e8) {
                }
                try {
					/*mView_HealthStatus.lteRSRP = signal[11];
					if (mView_HealthStatus.iCurrentNetworkState == 4) {
						lteParams.paramslist.Rsrp = signal[11];
					}*/
                    //System.out.println("rsrp.. 11 "+mView_HealthStatus.lteRSRP);
                } catch (Exception e9) {
                }
                try {
//                    mView_HealthStatus.lteRSRQ = signal[12];
                } catch (Exception e10) {
                }
                try {
                    if (signal[13].equals("INVALID_SNR")) {
                        mView_HealthStatus.lteSNR = -99.0d + "";
                    } else if (Integer.parseInt(signal[13]) < 10000) {
                        mView_HealthStatus.lteSNR = signal[13];
                    } else {
                        mView_HealthStatus.lteSNR = -99.0d + "";
                    }
                    lteParams.paramslist.Snr = mView_HealthStatus.lteSNR;
                    //System.out.println("SNR val "+lteParams.paramslist.Snr);

                } catch (Exception e11) {
                }
                try {
                    mView_HealthStatus.lteCQI = signal[14];
                    //System.out.println("mview signal 14  "+signal[14]);
                    return;
                } catch (Exception e12) {
                    return;
                }
            }

            try {
                System.out.println("signal values " + Arrays.toString(signal));
                mView_HealthStatus.lteRSSI = signal[8];
                System.out.println("signal val rssi " + signal[8]);
            }
            catch (Exception e13) {
            }


            try {
                if (signal[11].equals("INVALID_SNR")) {
                    mView_HealthStatus.lteSNR = -99.0d + "";
                } else if (Integer.parseInt(signal[11]) < 10000)
                {
                    mView_HealthStatus.lteSNR = (Double.parseDouble(signal[11]) / 10.0d) + "";
                } else
                    {
                    mView_HealthStatus.lteSNR = -99.0d + "";
                }
                if (mView_HealthStatus.iCurrentNetworkState == 4)
                {
                    lteParams.paramslist.Snr = mView_HealthStatus.lteSNR;
                }
            } catch (Exception e16) {
            }
            System.out.println("4G rssi " + signal[8] + " rsrp " + signal[9] + " snr " + mView_HealthStatus.lteSNR);
            try {

                mView_HealthStatus.lteCQI = signal[12];
                System.out.println("mView_HealthStatus.lteCQI is   "+mView_HealthStatus.lteCQI);
            } catch (Exception e17) {
            }
        } catch (Exception e18) {
        }
    }


    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        Log.i("LogFapps", "onSignalStrengthsChanged");


        boolean callInProgress = false;
        if (incomingCallStatus && currentCallObject != null) {
            currentCallObject.signalStrengthArr.add(signalStrength);
            callInProgress = true;
        }
        lastSignalStrength = signalStrength;

        @SuppressLint("MissingPermission")
        int networkType = telMgr.getNetworkType();
        mView_HealthStatus.strCurrentNetworkState = MyPhoneStateListener.getNetworkClass(networkType, mContext);

        if (signalStrength.isGsm()) {
            currentSignalStrength = signalStrength.getGsmSignalStrength();
            if (callInProgress) {
                if (currentSignalStrength < mView_HealthStatus.MIN_GSM_SIGNAL_STRENGTH_FOR_CALL_DROP) {
                    //sharad
                    bCallDropDanger = true;
                } else {
                    bCallDropDanger = false;
                }
            }
         /*   Log.i(LOG_TAG, "onSignalStrengthsChanged: getGsmBitErrorRate "
                    + signalStrength.getGsmBitErrorRate());
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getGsmSignalStrength "
*/                  /*  + signalStrength.getGsmSignalStrength());*/
        } else if (signalStrength.getCdmaDbm() > 0) {
          /*  Log.i(LOG_TAG, "onSignalStrengthsChanged: getCdmaDbm "
                    + signalStrength.getCdmaDbm());
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getCdmaEcio "
                    + signalStrength.getCdmaEcio());*/
        } else {
         /*   Log.i(LOG_TAG, "onSignalStrengthsChanged: getEvdoDbm "
                    + signalStrength.getEvdoDbm());
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getEvdoEcio "
                    + signalStrength.getEvdoEcio());
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getEvdoSnr "
                    + signalStrength.getEvdoSnr());*/
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        resulttime = System.currentTimeMillis();
        String displaydate = sdf.format(resulttime);
        lteParams = new LteParams();

        lteParams.paramslist = new LteParams().new Paramslist();


        decodesignalstrength(signalStrength);
        lteParams.paramslist.currenttime = displaydate;
        lteParams.paramslist.RxLevel = String.valueOf(getRxLev());
//	//Toast.makeText(mContext, "calling signalstrength changed", //Toast.LENGTH_SHORT).show();
        int currSize = mView_HealthStatus.lteparams.size();

        if (currSize == mView_HealthStatus.MaxPeriodicDataToSaveInDB && currSize >= 1) {
            mView_HealthStatus.lteparams.remove(0);
        }

        mView_HealthStatus.lteparams.add(lteParams.paramslist);
        for (int i = 0; i < mView_HealthStatus.lteparams.size(); i++) {
            ////System.out.println("captured data listener " + mView_HealthStatus.lteparams.get(i));
            //System.out.println("current time listener" + mView_HealthStatus.lteparams.get(i).currenttime + " rsrp" +
            //	mView_HealthStatus.lteparams.get(i).Rsrp);
        }


        mView_HealthStatus.rxqualfor2g = signalStrength.getGsmBitErrorRate() + "";
        lteParams.paramslist.RXQual = String.valueOf(signalStrength.getGsmBitErrorRate());

        int rxl = MyPhoneStateListener.getRxLev();
        if (currentCellServing != null) {

            currentCellServing.level = rxl + "";
        }


        try {
            if (telMgr != null) {

                final List<CellInfo> cellInfo;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    cellInfo = telMgr.getAllCellInfo();
                    System.out.println("cell info 1 " + cellInfo);
                    if (cellInfo != null && cellInfo.size() > 0) {
                        fetchCellsInfo(cellInfo);
                    }
                }

            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* if (true)
            return;*/

        // Reflection code starts from here
        try {
            Method[] methods = SignalStrength.class
                    .getMethods();
            for (Method mthd : methods) {
                if (mthd.getName().equals("getLteSignalStrength")) {
                    //mView_HealthStatus.lteRSSI = mthd.invoke(signalStrength) + "";
                } else if (mthd.getName().equals("getLteRsrp")) {
                    //mView_HealthStatus.lteRSRP = mthd.invoke(signalStrength) + "";
                } else if (mthd.getName().equals("getLteRsrq")) {
                    //mView_HealthStatus.lteRSRQ = mthd.invoke(signalStrength) + "";
                } else if (mthd.getName().equals("getLteRssnr")) {
                    // mView_HealthStatus.lteSNR = mthd.invoke(signalStrength) + "";
                } else if (mthd.getName().equals("getLteCqi")) {
                    mView_HealthStatus.lteCQI = mthd.invoke(signalStrength) + "";
                } else if (mthd.getName().equals("getLteCqi")) {
                    mView_HealthStatus.lteSINR = mthd.invoke(signalStrength) + "";
                }

                if (mthd.getName().equals("getLteSignalStrength")
                        || mthd.getName().equals("getLteRsrp")
                        || mthd.getName().equals("getLteRsrq")
                        || mthd.getName().equals("getLteRssnr")
                        || mthd.getName().equals("getLteCqi")) {

                    Log.i(LOG_TAG,
                            "onSignalStrengthsChanged LTE: " + mthd.getName() + " "
                                    + mthd.invoke(signalStrength));
                }


            }

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void fetchCellsInfo(List<CellInfo> cellInfo) {

        final List<CellInfo> registeredCellInfo = new ArrayList<>();
        final List<CellInfo> neighborCelllInfo = new ArrayList<>();

        NeighboringCellsInfo.neighboringCellList = new ArrayList<>();
        NeighboringCellsInfo.wcdma_neighboringCellList = new ArrayList<>();
        NeighboringCellsInfo.gsm_neighboringCellList = new ArrayList<>();

        NeighboringCellsInfo.nr_neighboringCellList = new ArrayList<>();

        CellInfo m;

        for (int i = 0; i < cellInfo.size(); i++) {
            m = cellInfo.get(i);


//            Log.i("LogFapps","Now Cell info is" + cellInfo);
            if (m.isRegistered()) {
                registeredCellInfo.add(m);
//                neighborCelllInfo.add(m);
            } else {
                neighborCelllInfo.add(m);
            }
        }


        if (registeredCellInfo != null && registeredCellInfo.size() > 0) {
            if (registeredCellInfo.get(0) != null)
                addServingCellInfo1(registeredCellInfo.get(0));


            if (registeredCellInfo.size() > 1) {
                if (registeredCellInfo.get(1) != null)
                    addServingCellInfo2(registeredCellInfo.get(1));
            }
        }


        if (neighborCelllInfo != null && neighborCelllInfo.size() > 0) {
            Log.i("LogFapps", "addNeighboringCellsInfo called");
            addNeighboringCellsInfo(neighborCelllInfo);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void addNeighboringCellsInfo(List<CellInfo> cellInfo) {
        for (int i = 0; i < cellInfo.size(); i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (cellInfo.get(i) instanceof CellInfoGsm) {
                    CellInfoGsm cellInfogsm = (CellInfoGsm) cellInfo.get(i);
                    CellIdentityGsm c = cellInfogsm.getCellIdentity();
                    CellSignalStrengthGsm ss = cellInfogsm.getCellSignalStrength();
                    getNeighboringCellsInfoForGSM(ss, c, cellInfo.get(i).isRegistered());


                } else if (cellInfo.get(i) instanceof CellInfoLte) {

                    cellInfoLte = (CellInfoLte) cellInfo.get(i);


                    cellIdentityLte = cellInfoLte.getCellIdentity();


                    CellSignalStrengthLte ss = cellInfoLte.getCellSignalStrength();
                    Log.i(Mview.TAG, "CellSignalstrenth info LTE " + ss);

                    getNeighboringCellsInfoForLte(ss, cellIdentityLte, cellInfo.get(i).isRegistered());
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (cellInfo.get(i) instanceof CellInfoWcdma) {

                        CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo.get(i);
                        CellIdentityWcdma cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
                        CellSignalStrengthWcdma ss = cellInfoWcdma.getCellSignalStrength();
                        getNeighboringCellsInfoForWcdma(ss, cellIdentityWcdma, cellInfo.get(i).isRegistered());

                    }
                }

                else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (cellInfo.get(i)  instanceof CellInfoNr)
                    {
                        mView_HealthStatus.currentInstance = "nr";
                        cellInfoNr = (CellInfoNr) cellInfo.get(i);
                        cellIdentityNr = (CellIdentityNr) cellInfoNr.getCellIdentity();
                        CellSignalStrengthNr ss = (CellSignalStrengthNr) cellInfoNr.getCellSignalStrength();
                        try {
                            getNeighboringCellsInfoForNR(ss, cellIdentityNr, cellInfo.get(i).isRegistered());

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        //  getLteCellInfoForFirstSim(ss);
                    }
                    }

                }
            }

        }
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void addServingCellInfo2(CellInfo m) {
        if (m != null) {


            if (m instanceof CellInfoGsm) {
                CellInfoGsm cellInfogsm = (CellInfoGsm) m;
                CellIdentityGsm c = cellInfogsm.getCellIdentity();
                CellSignalStrengthGsm ss = cellInfogsm.getCellSignalStrength();
                String[] info = m.toString().split(" ");

                String[] ber;
                ber = info[17].split("ber=");
                if (ber[1] != null) {
                    mView_HealthStatus.second_rxqual = Integer.parseInt(ber[1]);
                }


                getGsmCellInfoForSecondSim(ss, c, cellInfogsm);
            } else if (m instanceof CellInfoLte) {

                cellInfoLte = (CellInfoLte) m;
                cellIdentityLte = cellInfoLte.getCellIdentity();
                CellSignalStrengthLte ss = cellInfoLte.getCellSignalStrength();
                getLteCellInfoForAnotherSim(ss);

            } else if (m instanceof CellInfoWcdma) {

                CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) m;
                CellIdentityWcdma cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
                CellSignalStrengthWcdma ss = cellInfoWcdma.getCellSignalStrength();
                getWcdmaCellInfoForAnotherSim(ss, cellIdentityWcdma);
            } else if (m instanceof CellInfoNr) {
                mView_HealthStatus.second_cellInstance = "nr";
                cellInfoNr = (CellInfoNr) m;
                cellIdentityNr = (CellIdentityNr) cellInfoNr.getCellIdentity();
                CellSignalStrengthNr ss = (CellSignalStrengthNr) cellInfoNr.getCellSignalStrength();
                try {
                    getCellInfoForSecondSim5G(ss, cellIdentityNr);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //  getLteCellInfoForFirstSim(ss);
            }
        }


    }

    private static void getCellInfoForSecondSim5G(CellSignalStrengthNr ss, CellIdentityNr cellIdentityNr) {
        HashMap<String, String> hmap = new HashMap<>();
        hmap.put("type", "nr");
        hmap.put("ta", "NA");
        hmap.put("cqi", "NA");


        String simOperator = ListenService.telMgr.getSimOperatorName();
        mView_HealthStatus.sec_carrierName = simOperator;
        String mcc = MyPhoneStateListener.cellIdentityNr.getMccString();
        String mnc = MyPhoneStateListener.cellIdentityNr.getMncString();
        hmap.put("mcc", "" + mcc);
        hmap.put("mnc", "" + mnc);


        hmap.put("ta", "NA");

        int SsRsrp = ss.getSsRsrp();
        if (!Utils.isMaxint(SsRsrp)) {
            hmap.put("rsrp", SsRsrp + "");
        } else {
            hmap.put("rsrp", "NA");

        }
        int SsRsrq = ss.getSsRsrq();
        int CsiSinr = ss.getCsiSinr();


        int SsSinr = ss.getSsSinr();

        if (!Utils.isMaxint(CsiSinr)) {
            hmap.put("snr", "" + CsiSinr);
        } else {
            hmap.put("snr", "NA");

        }

        int pci = MyPhoneStateListener.cellIdentityNr.getPci();


        if (!Utils.isMaxint(pci)) {
            hmap.put("pci", "" + pci);
        } else {
            hmap.put("pci", "NA");

        }
        int CsiRsrp = ss.getCsiRsrp();

        if (!Utils.isMaxint(CsiRsrp)) {
            hmap.put("rsrp", CsiRsrp + "");
        } else {
            hmap.put("rsrp", "NA");

        }
        int CsiRsrq = ss.getCsiRsrq();
        if (!Utils.isMaxint(CsiRsrq)) {

            hmap.put("rsrq", "" + CsiRsrq);
        } else {
            hmap.put("rsrq", "NA");
        }
        int[] Band = MyPhoneStateListener.cellIdentityNr.getBands();
        hmap.put("bands",""+ Band);



        int arfcn = MyPhoneStateListener.cellIdentityNr.getNrarfcn();

        if (!Utils.isMaxint(arfcn)) {
            hmap.put("earfcn", arfcn + "");
        } else {
            hmap.put("earfcn", "NA");

        }

        long cid = MyPhoneStateListener.cellIdentityNr.getNci();
        try {


            hmap.put("enb", String.valueOf(getenb((int) cid)));
        } catch (Exception e) {
            e.printStackTrace();
            hmap.put("enb", "NA");

        }

        hmap.put("cellid", String.valueOf(cid));

        try {


            String cellidHex = DecToHex((int) cid);
            String localcellid = cellidHex.substring(cellidHex.length() - 2);
            int cid1 = Integer.parseInt(localcellid);

            hmap.put("localcellid", cid1 + "");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            hmap.put("localcellid", " NA");

        }
        int dbm = ss.getDbm();
        int tac = cellIdentityNr.getTac();

        if (!Utils.isMaxint(tac)) {
            hmap.put("tac", "" + tac);
        } else {
            hmap.put("tac", "NA");

        }
        mView_HealthStatus.servingcell2info.add(hmap);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static void getWcdmaCellInfoForAnotherSim(CellSignalStrengthWcdma ss, CellIdentityWcdma cellIdentityWcdma) {

        {
            HashMap<String, String> hmap = new HashMap<>();
            hmap.put("type", "3G");
            hmap.put("ta", "NA");
                hmap.put("rsrq", mView_HealthStatus.second_rsrq);
            mView_HealthStatus.second_cellInstance = "wcdma";
            int rscp = ss.getDbm();
            int uarfcn = 0;
            int lac = cellIdentityWcdma.getLac();

            int cid = cellIdentityWcdma.getCid();


            hmap.put("cellid", String.valueOf(cid));

            try {


                String cellidHex = DecToHex(cid);
                String localcellid = cellidHex.substring(cellidHex.length() - 2);
                int cid1 = Integer.parseInt(localcellid);

                hmap.put("localcellid", cid1 + "");
            } catch (NumberFormatException e) {
                e.printStackTrace();
                hmap.put("localcellid", " NA");

            }
            int psc = cellIdentityWcdma.getPsc();
            int mcc = cellIdentityWcdma.getMcc();
            int mnc = cellIdentityWcdma.getMnc();
            hmap.put("mcc", "" + mcc);
            hmap.put("mnc", "" + mnc);
            mView_HealthStatus.second_rsrq = " ";
            int cqi = mView_HealthStatus.second_Cqi;

            String snr = "NA";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                snr = getSNR();

            }
            hmap.put("snr", "" + snr);
            if (cqi != Integer.MAX_VALUE || cqi != 0) {
                hmap.put("cqi", String.valueOf(cqi));
            } else {
                hmap.put("cqi", "NA");

            }

            hmap.put("pci", "" + psc);


            if (rscp != Integer.MAX_VALUE) {

                hmap.put("rsrp", rscp + "");


            } else {
                hmap.put("rsrp", "NA");

            }
            hmap.put("tac", "NA");


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                uarfcn = cellIdentityWcdma.getUarfcn();
                hmap.put("earfcn", uarfcn + "");

            } else {
                hmap.put("earfcn", "NA");

            }

            if (rscp != Integer.MAX_VALUE) {
                mView_HealthStatus.second_rscp_3G = rscp;
            }
            if (uarfcn != Integer.MAX_VALUE) {
                mView_HealthStatus.second_uarfcn_3G = uarfcn;
            }
            if (lac != Integer.MAX_VALUE) {
                mView_HealthStatus.second_lac_3G = lac;
            }
            if (cid != Integer.MAX_VALUE) {
                mView_HealthStatus.second_cid_3G = cid;
            }
            if (psc != Integer.MAX_VALUE) {
                mView_HealthStatus.second_Psc_3g = psc;
            }
            if (mcc != Integer.MAX_VALUE) {
                mView_HealthStatus.second_Mcc = mcc;
            }
            if (mnc != Integer.MAX_VALUE) {
                mView_HealthStatus.second_Mnc = mnc;
            }


            String ecno = String.valueOf(mView_HealthStatus.second_rscp_3G - getRxLev());
            if (ecno != null) {
                mView_HealthStatus.second_ecno_3G = ecno;
            } else {
                mView_HealthStatus.second_ecno_3G = "0";
            }

            hmap.put("rsrq", "NA");

            mView_HealthStatus.second_NodeBID_3G = String.valueOf(getenb(cid));
            mView_HealthStatus.servingcell2info.add(hmap);

        }
    }

    private void getLteCellInfoForSecondSim(CellSignalStrengthLte ss) {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void getGsmCellInfoForSecondSim(CellSignalStrengthGsm ss, CellIdentityGsm c, CellInfoGsm cellInfogsm) {
        mView_HealthStatus.second_cellInstance = "gsm";
        HashMap<String, String> hmap = new HashMap<>();
        hmap.put("type", "gsm");


        hmap.put("rsrq",   mView_HealthStatus.second_rsrq);

        int dbm = cellInfogsm.getCellSignalStrength().getDbm();

        System.out.println("ss in gsm " + dbm);
        int mcc = c.getMcc();
        int mnc = c.getMnc();
        hmap.put("mcc", cellIdentityLte.getMcc() + "");
        hmap.put("mnc", cellIdentityLte.getMnc() + "");
        if (!Utils.isMaxint(mView_HealthStatus.second_Cqi)) {
            hmap.put("cqi", "" + mView_HealthStatus.second_Cqi);
        }
        else {
            hmap.put("cqi", "NA");

        }


        int snr = mView_HealthStatus.second_snr;

        hmap.put("snr", "" + snr);
        if (mcc != Integer.MAX_VALUE) {
            mView_HealthStatus.second_Mcc = mcc;
        }
        if (mnc != Integer.MAX_VALUE) {
            mView_HealthStatus.second_Mnc = mnc;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int ta = ss.getTimingAdvance();
            if (ta != Integer.MAX_VALUE) {

                mView_HealthStatus.secong_gsmTa = ta;
                hmap.put("ta", "" + ta);

            }
            else
            {
                hmap.put("ta", "NA");

            }
        }
        else
        {
            hmap.put("ta", "NA");

        }



        mView_HealthStatus.second_rxLev = dbm;


        int cid = c.getCid();
        if (cid != Integer.MAX_VALUE) {
            mView_HealthStatus.second_Cid = cid;
            hmap.put("cellid", String.valueOf(cid));
            String cellidHex = DecToHex(cid);
            String localcellid = cellidHex.substring(cellidHex.length() - 2);
            int cid1 = Integer.parseInt(localcellid);

            hmap.put("localcellid", cid1 + "");

        } else {
            hmap.put("cellid", "NA");
            hmap.put("localcellid", "NA");


        }
        hmap.put("enb", String.valueOf(getenb(cid)));

        int lac = c.getLac();
        int psc = c.getPsc();



        if (psc != Integer.MAX_VALUE) {
            mView_HealthStatus.secong_gsmPsc = psc;
            hmap.put("pci", psc + "");

        }
        else {
            hmap.put("pci",  "NA");

        }
        System.out.println("cid is " + cid);
        if (cid != Integer.MAX_VALUE) {
            mView_HealthStatus.second_gsmCid = cid;

        }
        if (lac != Integer.MAX_VALUE) {
            mView_HealthStatus.second_gsmLac = lac;
        }


//                            if (c.getMcc() < 2147483647) {
        if (Build.VERSION.SDK_INT >= 24) {
            int arfcn = c.getArfcn();
            if (arfcn != Integer.MAX_VALUE) {
                mView_HealthStatus.second_arfcn = arfcn;
                hmap.put("earfcn",""+ arfcn);

            }
            else
            {
                hmap.put("earfcn","NA");

            }

        }
        else

        {
            hmap.put("earfcn","NA");

        }
        hmap.put("tac", "NA");
        mView_HealthStatus.servingcell2info.add(hmap);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void addServingCellInfo1(CellInfo m) {
        if (m != null) {

            if (m instanceof CellInfoGsm) {
                mView_HealthStatus.currentInstance = "gsm";
                CellInfoGsm cellInfogsm = (CellInfoGsm) m;
                CellIdentityGsm c = cellInfogsm.getCellIdentity();
                CellSignalStrengthGsm ss = cellInfogsm.getCellSignalStrength();
                getGsmCellInfoForFirstSim(ss, c, cellInfogsm);
            } else if (m instanceof CellInfoLte) {
                mView_HealthStatus.currentInstance = "lte";
                cellInfoLte = (CellInfoLte) m;
                cellIdentityLte = cellInfoLte.getCellIdentity();
                CellSignalStrengthLte ss = cellInfoLte.getCellSignalStrength();
                getLteCellInfoForFirstSim(ss);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                if (m instanceof CellInfoWcdma) {
                    mView_HealthStatus.currentInstance = "wcdma";
                    CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) m;
                    CellIdentityWcdma cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
                    CellSignalStrengthWcdma ss = cellInfoWcdma.getCellSignalStrength();
                    getWcdmaCellInfoForFirstSim(ss, cellIdentityWcdma);
                }

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (m instanceof CellInfoNr) {
                    mView_HealthStatus.currentInstance = "nr";
                    cellInfoNr = (CellInfoNr) m;
                    cellIdentityNr = (CellIdentityNr) cellInfoNr.getCellIdentity();
                    CellSignalStrengthNr ss = (CellSignalStrengthNr) cellInfoNr.getCellSignalStrength();
                    try {
                        getCellInfoForFirstSim5G(ss);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //  getLteCellInfoForFirstSim(ss);
                }
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static void getCellInfoForFirstSim5G(CellSignalStrengthNr ss) {

        HashMap<String, String> hmap = new HashMap<>();
        hmap.put("type", "nr");
        hmap.put("ta", "NA");
        hmap.put("cqi", "NA");


        String simOperator = ListenService.telMgr.getSimOperatorName();
        mView_HealthStatus.sec_carrierName = simOperator;
        String mcc = MyPhoneStateListener.cellIdentityNr.getMccString();
        String mnc = MyPhoneStateListener.cellIdentityNr.getMncString();
        hmap.put("mcc", "" + mcc);
        hmap.put("mnc", "" + mnc);


        hmap.put("ta", "NA");

        int SsRsrp = ss.getSsRsrp();
        if (!Utils.isMaxint(SsRsrp)) {
            hmap.put("rsrp", SsRsrp + "");
        } else {
            hmap.put("rsrp", "NA");

        }
        int SsRsrq = ss.getSsRsrq();
        int CsiSinr = ss.getCsiSinr();


        int SsSinr = ss.getSsSinr();

        if (!Utils.isMaxint(CsiSinr)) {
            hmap.put("snr", "" + CsiSinr);
        } else {
            hmap.put("snr", "NA");

        }

        int pci = MyPhoneStateListener.cellIdentityNr.getPci();


        if (!Utils.isMaxint(pci)) {
            hmap.put("pci", "" + pci);
        } else {
            hmap.put("pci", "NA");

        }
        int CsiRsrp = ss.getCsiRsrp();

        if (!Utils.isMaxint(CsiRsrp)) {
            hmap.put("rsrp", CsiRsrp + "");
        } else {
            hmap.put("rsrp", "NA");

        }
        int CsiRsrq = ss.getCsiRsrq();
        if (!Utils.isMaxint(CsiRsrq)) {

            hmap.put("rsrq", "" + CsiRsrq);
        } else {
            hmap.put("rsrq", "NA");
        }
        int[] Band = MyPhoneStateListener.cellIdentityNr.getBands();
        hmap.put("bands",""+ Band);



        int arfcn = MyPhoneStateListener.cellIdentityNr.getNrarfcn();

        if (!Utils.isMaxint(arfcn)) {
            hmap.put("earfcn", arfcn + "");
        } else {
            hmap.put("earfcn", "NA");

        }

        long cid = MyPhoneStateListener.cellIdentityNr.getNci();
        try {


            hmap.put("enb", String.valueOf(getenb((int) cid)));
        } catch (Exception e) {
            e.printStackTrace();
            hmap.put("enb", "NA");

        }

        hmap.put("cellid", String.valueOf(cid));

        try {


            String cellidHex = DecToHex((int) cid);
            String localcellid = cellidHex.substring(cellidHex.length() - 2);
            int cid1 = Integer.parseInt(localcellid);

            hmap.put("localcellid", cid1 + "");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            hmap.put("localcellid", " NA");

        }
        int dbm = ss.getDbm();
        int tac = cellIdentityNr.getTac();

        if (!Utils.isMaxint(tac)) {
            hmap.put("tac", "" + tac);
        } else {
            hmap.put("tac", "NA");

        }
        mView_HealthStatus.servingcell1info.add(hmap);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void getGsmCellInfoForFirstSim(CellSignalStrengthGsm ss, CellIdentityGsm c, CellInfoGsm cellInfogsm) {


        {
            mView_HealthStatus.servingcell1info = new ArrayList<>();
            HashMap<String, String> hmap = new HashMap<>();

            hmap.put("mcc", "" + cellInfogsm.getCellIdentity().getMcc());
            hmap.put("mnc", "" + cellInfogsm.getCellIdentity().getMnc());

            hmap.put("rsrq",""+mView_HealthStatus.lteRSRQ);
            hmap.put("type", "2G");

            int asus = cellInfogsm.getCellSignalStrength().getAsuLevel();
            int dbm = cellInfogsm.getCellSignalStrength().getDbm();
            if (!Utils.isMaxint(dbm)) {
                hmap.put("rsrp", "" + dbm);
            } else {
                hmap.put("rsrp", "NA");

            }
            int level = cellInfogsm.getCellSignalStrength().getLevel();
            mView_HealthStatus.gsmSignalStrength = cellInfogsm.getCellSignalStrength().getDbm();
            System.out.println("ss in gsm " + dbm);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String ta = String.valueOf(ss.getTimingAdvance());
                hmap.put("ta", ta);


                //System.out.println("ta is "+ta);
                //	//Toast.makeText(mContext, "ta is "+ta, //Toast.LENGTH_SHORT).show();
                mView_HealthStatus.lteta = ta;
            } else {
                hmap.put("ta", "NA");

                mView_HealthStatus.lteta = "0";
            }

            mView_HealthStatus.lteasus = asus + "";
            mView_HealthStatus.ltedbm = dbm + "";
            mView_HealthStatus.ltelevel = level + "";


            int cid = c.getCid();
            hmap.put("cellid", "" + cid);
            try {


                String cellidHex = DecToHex(cid);
                String localcellid = cellidHex.substring(cellidHex.length() - 2);
                int cid1 = Integer.parseInt(localcellid);

                hmap.put("localcellid", "" + cid1);
            } catch (NumberFormatException e) {

                hmap.put("localcellid", "NA");
                e.printStackTrace();
            }


            int lac = c.getLac();
            int psc = c.getPsc();
            if (!Utils.isMaxint(psc)) {
                hmap.put("pci", "" + psc);
            } else {
                hmap.put("pci", "NA");

            }

            hmap.put("cqi", mView_HealthStatus.lteCQI);


            mView_HealthStatus.Psc = String.valueOf(psc);
//            short cid_short = (short) cid;
            mView_HealthStatus.Cid = String.valueOf(cid);


            mView_HealthStatus.Lac = String.valueOf(lac);


            hmap.put("tac", "NA");
            hmap.put("enb", "" + getenb(cid));
            hmap.put("snr", mView_HealthStatus.lteSNR);


//                            if (c.getMcc() < 2147483647) {
            if (Build.VERSION.SDK_INT >= 24) {
                int arfcn = c.getArfcn();
                if (!Utils.isMaxint(arfcn)) {
                    hmap.put("earfcn", "" + arfcn);
                } else {
                    hmap.put("earfcn", "NA");

                }
                int bsic = c.getBsic();
                //System.out.println("arfcn.. cell   " + c.getArfcn());
                ///
                mView_HealthStatus.ARFCN = arfcn + "";
                //just in case for LTE if it comes here
                if (mView_HealthStatus.iCurrentNetworkState != 4) {
                    mView_HealthStatus.ltePCI = bsic + "";
                    currentCellServing.arfcn = arfcn + "";
                    //System.out.println("arfcn.. cell info  " + c.getArfcn());
                }
            } else {
                hmap.put("earfcn", "NA");

                mView_HealthStatus.ltePCI = "0";
                mView_HealthStatus.lteArfcn = "0";
            }
            mView_HealthStatus.servingcell1info.add(hmap);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static void getWcdmaCellInfoForFirstSim(CellSignalStrengthWcdma ss, CellIdentityWcdma cellIdentityWcdma) {

        {
            mView_HealthStatus.servingcell1info = new ArrayList<>();
            mView_HealthStatus.mcc_first = cellIdentityWcdma.getMcc();

            HashMap<String, String> hmap = new HashMap<>();
            hmap.put("mcc", "" + cellIdentityWcdma.getMcc());
            mView_HealthStatus.mnc_first = cellIdentityWcdma.getMnc();
            hmap.put("mnc", "" + cellIdentityWcdma.getMnc());
            hmap.put("type", "3G");

            hmap.put("ta", "NA");
            mView_HealthStatus.currentInstance = "wcdma";


            mView_HealthStatus.rscp = String.valueOf(ss.getDbm());
            if (ss.getDbm() != Integer.MAX_VALUE) {
                hmap.put("rsrp", "" + ss.getDbm());
            } else {
                hmap.put("rsrp", "NA");

            }
            hmap.put("cqi", "NA");

            lteParams.paramslist.Rscp = mView_HealthStatus.rscp;
            System.out.println("wcdma asu is " + ss.getAsuLevel());
            if (mView_HealthStatus.rscp != null) {

                String ecno = String.valueOf(Integer.parseInt(mView_HealthStatus.rscp) - getRxLev());
                lteParams.paramslist.Ecno = ecno;
            }
            String signalstrength = ss.toString().replace("CellSignalStrengthWcdma:", "");
            String[] signal = signalstrength.toString().split(" ");
            hmap.put("rsrq",signalstrength);

            int psc = cellIdentityWcdma.getPsc();


            hmap.put("pci", "" + psc);
            int uarfcn = 0;
            //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uarfcn = cellIdentityWcdma.getUarfcn();
            }
            //}
            //	int Ec=cellIdentityWcdma.get

            if (uarfcn != Integer.MAX_VALUE) {
                hmap.put("earfcn", "" + uarfcn);
            } else {
                hmap.put("earfcn", "NA");

            }
            mView_HealthStatus.Wcdma_Psc = String.valueOf(psc);
            //	mView_HealthStatus.lteArfcn=String.valueOf(uarfcn);
            mView_HealthStatus.Uarfcn = String.valueOf(uarfcn);

            int longCid = cellIdentityWcdma.getCid();
            hmap.put("cellid", "" + longCid);
            try {


                String cellidHex = DecToHex(longCid);
                String localcellid = cellidHex.substring(cellidHex.length() - 2);
                int cid1 = Integer.parseInt(localcellid);

                hmap.put("localcellid", "" + cid1);
            } catch (NumberFormatException e) {

                hmap.put("localcellid", "NA");
                e.printStackTrace();
            }

            hmap.put("tac", "NA");
            hmap.put("enb", "" + getenb(longCid));
            hmap.put("snr", mView_HealthStatus.lteSNR);


//            short cid_short = (short) cid;
            mView_HealthStatus.nodeb_id = String.valueOf(getenb(longCid));
            lteParams.paramslist.NodeBid = mView_HealthStatus.nodeb_id;
//						mView_HealthStatus.lteENB=String.valueOf(getenb(longCid));

            mView_HealthStatus.servingcell1info.add(hmap);


        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void getLteCellInfoForFirstSim(CellSignalStrengthLte ss) {

        {
            HashMap<String, String> hmap = new HashMap<>();
            System.out.println("lte cell info 1 " + cellIdentityLte);
            mView_HealthStatus.currentInstance = "lte";
            hmap.put("type", "lte");
            int level = ss.getLevel();
            int ta = ss.getTimingAdvance();

            int cid = cellIdentityLte.getCi();
            int pci = cellIdentityLte.getPci();
            int tac = cellIdentityLte.getTac();
            int dbm = ss.getDbm();

            if (!Utils.isMaxint(dbm)) {
                mView_HealthStatus.lteRSRP = dbm + "";
                hmap.put("rsrp", dbm + "");
                lteParams.paramslist.Rsrp = dbm + "";
            } else {
                hmap.put("rsrp", "NA");
            }
            System.out.println("signal rsrp " + dbm + "");
            int asus = ss.getAsuLevel();
            mView_HealthStatus.mcc_first = cellIdentityLte.getMcc();
            mView_HealthStatus.mnc_first = cellIdentityLte.getMnc();
            hmap.put("mcc", cellIdentityLte.getMcc() + "");
            hmap.put("mnc", cellIdentityLte.getMnc() + "");
//            mView_HealthStatus.lteCQI = String.valueOf(cqi);
            Log.i("LogFapps", "lteCQI is " + mView_HealthStatus.lteCQI);

            mView_HealthStatus.Cid = String.valueOf(cid);
            if (!Utils.isMaxint(cid)) {
                hmap.put("cellid", String.valueOf(cid));
            } else {
                hmap.put("cellid", "NA");


                /*
						getting cid which is ci in LTE
                         */
            }
            if (currentCellServing != null) {
                currentCellServing.ci = String.valueOf(cid);
                String cellidHex = DecToHex(cid);
                try {
                    String localcellid = cellidHex.substring(cellidHex.length() - 2);

                    int cid1 = Integer.parseInt(localcellid);
                    hmap.put("localcellid", cid1 + "");

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                        /*
                        getting PCI
                        * */
                currentCellServing.ltePCI = pci + "";
            }

            mView_HealthStatus.ltePCI = pci + "";
            if (!Utils.isMaxint(pci)) {
                hmap.put("pci", pci + "");
            } else {
                hmap.put("pci", "NA");

            }


                        /*
                        getting tac
                         */
            if (!Utils.isMaxint(tac)) {
                currentCellServing.lteTAC = tac + "";
                mView_HealthStatus.lteTAC = tac + "";
                hmap.put("tac", tac + "");


            } else {
                hmap.put("tac", "NA");

            }
            Log.i("LogFapps", "lteTAC is " + mView_HealthStatus.lteTAC);
                      /*
                      Getting earfcn which is arfcn in gsm
                       */
            int arfcn = 0; //requires 24
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                arfcn = cellIdentityLte.getEarfcn();
            }
            if (!Utils.isMaxint(arfcn)) {
                hmap.put("earfcn", arfcn + "");
            } else {
                hmap.put("earfcn", "NA");

            }

            currentCellServing.arfcn = arfcn + "";
            mView_HealthStatus.lteArfcn = String.valueOf(arfcn);


            mView_HealthStatus.lteasus = asus + "";

			/*mView_HealthStatus.lteRSRP = dbm + "";

			lteParams.paramslist.Rsrp=mView_HealthStatus.lteRSRP;*/

            /*Getting Level

             */

            mView_HealthStatus.ltelevel = level + "";


            mView_HealthStatus.lteENB = String.valueOf(getenb(cid));
            hmap.put("enb", String.valueOf(getenb(cid)));

            currentCellServing.lteENB = String.valueOf(getenb(cid));
            Log.i("LogFapps", "lteENB is " + mView_HealthStatus.lteENB);
            //System.out.println("enb..." + getenb(cid));

            /*Getting TA

             */
            if (!Utils.isMaxint(ta)) {
                mView_HealthStatus.lteta = ta + "";

                hmap.put("ta", ta + "");
            } else {
                hmap.put("ta", "NA");
            }
            int snr = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                snr = ss.getRssnr();
            }


            System.out.println("SNR is "+snr+" from mobilecomm "+mView_HealthStatus.lteSNR);

          if (!Utils.isMaxint(snr))
            {
                System.out.println("Goig in if condition for snr");
                hmap.put("snr", "" + snr);
            }
            else
            {
                System.out.println("Goig in else condition for snr");
                hmap.put("snr", mView_HealthStatus.lteSNR);
            }
            int cqi = getvalueofcqilte(mView_HealthStatus.lteSNR);
           /* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                cqi = ss.getCqi();
            }*/
            mView_HealthStatus.lteCQI = String.valueOf(cqi);
            Log.i(Mview.TAG,"cqi is "+cqi);

            if (!Utils.isMaxint(cqi)) {
                hmap.put("cqi", String.valueOf(cqi));
            } else {
                hmap.put("cqi", "NA");

            }


            int rsrq = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                rsrq = ss.getRsrq();
            }

            if (!Utils.isMaxint(rsrq)) {
                hmap.put("rsrq", rsrq + "");
            } else {
                hmap.put("rsrq", "NA");

            }
            mView_HealthStatus.lteRSRQ = rsrq + "";

            //	}

            mView_HealthStatus.servingcell1info.add(hmap);
        }
    }

    private static int getvalueofcqilte(String lte) {
        System.out.println("STRING SNR IS "+lte);
        int lteSNR=0;
        try {
            float f1 = Float.parseFloat(lte);
             lteSNR=(int)f1;
            System.out.println("STRING SNR IS "+lte+" "+" integer is "+lteSNR);

        }
        catch (Exception e)
        {
            System.out.println("exception while converting snr "+e.toString());

            e.printStackTrace();
        }

        int cqi;
        if(lteSNR<19.8290&&lteSNR>=17.8410)
        {
            cqi=15;
        }
        else if (lteSNR<17.8410&&lteSNR>=15.8880)
        {
            cqi=14;
        }
        else if (lteSNR<15.8880&&lteSNR>=14.1730)
        {
            cqi=13;
        }
        else if (lteSNR<14.1730&&lteSNR>=12.2890)
        {
            cqi=12;
        }
        else if (lteSNR<12.2890&&lteSNR>=10.3660)
        {
            cqi=11;
        }
        else if (lteSNR<10.3660&&lteSNR>=8.5730)
        {
            cqi=10;
        }
        else if (lteSNR<8.5730&&lteSNR>=6.5250)
        {
            cqi=9;
        }
        else if (lteSNR<6.250&&lteSNR>=4.6940)
        {

            cqi=8;


        }
        else if(lteSNR<4.6940&&lteSNR>=2.6990)
        {
            cqi=7;
        }
        else if(lteSNR<2.6990&&lteSNR>=0.7610)
        {
            cqi=6;
        }
        else if (lteSNR<0.7610&&lteSNR>=-1.2530)
        {
            cqi=5;
        }
        else if (lteSNR<-1.2530&&lteSNR>=-3.1800)
        {
            cqi=4;
        }
        else if (lteSNR<-3.1800&&lteSNR>=-5.1470)
        {
            cqi=3;
        }
        else if(lteSNR<-5.1470&&lteSNR>=-6.9360)
        {
            cqi=2;
        }
        else if (lteSNR<-6.9360)
        {
            cqi=1;
        }

        else
        {
            cqi=-1;
        }





        return cqi;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void getLteCellInfoForAnotherSim(CellSignalStrengthLte ss) {

        {


            HashMap<String, String> hmap = new HashMap<>();
            hmap.put("type", "lte");

            System.out.println("lte cell info 2 " + cellIdentityLte);
            mView_HealthStatus.second_cellInstance = "lte";
            int level = ss.getLevel();

            int ta = ss.getTimingAdvance();
            if (ta != Integer.MAX_VALUE) {
                hmap.put("ta", "" + ta);

            } else {
                hmap.put("ta", "NA");

            }
            int cqi = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                cqi = ss.getCqi();
            }

            if (cqi != Integer.MAX_VALUE || cqi != 0) {
                hmap.put("cqi", String.valueOf(cqi));
            } else {
                hmap.put("cqi", "NA");

            }

            int cid = cellIdentityLte.getCi();
            int pci = cellIdentityLte.getPci();
            hmap.put("pci", pci + "");

            int snr = mView_HealthStatus.second_snr;

            hmap.put("snr", "" + snr);
            int rsrq = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                rsrq = ss.getRsrq();
            }
            hmap.put("rsrq", rsrq + "");
            int tac = cellIdentityLte.getTac();
            int mcc = cellIdentityLte.getMcc();
            int mnc = cellIdentityLte.getMnc();
            hmap.put("mcc", cellIdentityLte.getMcc() + "");
            hmap.put("mnc", cellIdentityLte.getMnc() + "");
            hmap.put("enb", String.valueOf(getenb(cid)));

            int dbm = ss.getDbm();

            if (dbm != Integer.MAX_VALUE) {
                mView_HealthStatus.lteRSRP = dbm + "";
                hmap.put("rsrp", dbm + "");

                lteParams.paramslist.Rsrp = dbm + "";
            } else {
                hmap.put("rsrp", "NA");

            }

            int aba = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                aba = ss.getRsrq();
            }

            if (aba != Integer.MAX_VALUE) {
                mView_HealthStatus.second_rsrq = "" + aba;
                hmap.put("rsrq",""+aba);

            } else {
                hmap.put("rsrq","NA");
                mView_HealthStatus.second_rsrq = "NA";


            }



            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                snr = ss.getRssnr();

            }

            int rsrp = ss.getDbm();


            mView_HealthStatus.second_Mcc = mcc;
            mView_HealthStatus.second_Mnc = mnc;
            if (rsrp != Integer.MAX_VALUE) {
                mView_HealthStatus.second_Rsrp = rsrp;
            }
            if (snr != Integer.MAX_VALUE) {
                mView_HealthStatus.second_snr = snr;
            }

            if (cqi != Integer.MAX_VALUE) {
                mView_HealthStatus.second_Cqi = cqi;
            }

            if (cid != Integer.MAX_VALUE) {
                mView_HealthStatus.second_Cid = cid;
                hmap.put("cellid", String.valueOf(cid));
                String cellidHex = DecToHex(cid);
                String localcellid = cellidHex.substring(cellidHex.length() - 2);
                int cid1 = Integer.parseInt(localcellid);

                hmap.put("localcellid", cid1 + "");

            } else {
                hmap.put("cellid", "NA");
                hmap.put("localcellid", "NA");


            }
            if (pci != Integer.MAX_VALUE) {
                mView_HealthStatus.second_pci = pci;
            }

            if (tac != Integer.MAX_VALUE) {

                mView_HealthStatus.second_tac = tac;
            }
            if (tac != Integer.MAX_VALUE) {
                hmap.put("tac", tac + "");


            } else {
                hmap.put("tac", "NA");

            }

            if (Build.VERSION.SDK_INT >= 24) {
                int arfcn = cellIdentityLte.getEarfcn();
                mView_HealthStatus.second_earfcn = arfcn;
                hmap.put("earfcn", arfcn + "");


            }


            mView_HealthStatus.second_ENB = String.valueOf(getenb(cid));
            if (ta != Integer.MAX_VALUE) {
                mView_HealthStatus.second_ta = ta;
            }
            mView_HealthStatus.servingcell2info.add(hmap);


        }

    }

    private void getCellInfoForAnotherSim() {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void getNeighboringCellsInfoForGSM(CellSignalStrengthGsm ss, CellIdentityGsm cellIdentityGsm, boolean isRegistered) {

		/*CellInfoGsm:{mRegistered=NO mTimeStampType=oem_ril mTimeStamp=15712607914940ns mCellConnectionStatus=0
			CellIdentityGsm:{ mLac=1014 mCid=48161 mArfcn=661 mBsic=0x15 mMcc=404 mMnc=11
		mAlphaLong=404 11 mAlphaShort=404 11} CellSignalStrengthGsm: ss=8 ber=99 mTa=2147483647},*/
        try {
            if (NeighboringCellsInfo.gsm_neighboringCellList != null) {
                HashMap<Integer, Integer> hp = new HashMap<>();
                NeighboringCellsInfo.gsmParams = new ArrayList<>();
                NeighboringCellsInfo.gsmParams.add("2G_LAC");


                hp.put(0, cellIdentityGsm.getLac());


                NeighboringCellsInfo.gsmParams.add("2G_CID");
                hp.put(1, cellIdentityGsm.getCid());

                NeighboringCellsInfo.gsmParams.add("2G_ARFCN");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    hp.put(2, cellIdentityGsm.getArfcn());
                } else {
                    hp.put(2, 0);
                }

                NeighboringCellsInfo.gsmParams.add("2G_BSIC");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    hp.put(3, cellIdentityGsm.getBsic());
                } else {
                    hp.put(3, 0);
                }


                NeighboringCellsInfo.gsmParams.add("2G_PSC");
                hp.put(4, cellIdentityGsm.getPsc());

                NeighboringCellsInfo.gsmParams.add("2G_RX_LEVEL");
                hp.put(5, ss.getDbm());
                NeighboringCellsInfo.gsm_neighnor_ss = ss.getDbm();

                NeighboringCellsInfo.gsmParams.add("2G_TA");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    hp.put(6, ss.getTimingAdvance());
                } else {
                    hp.put(6, 0);
                }
                byte[] l_byte_array = CommonFunctions.convertByteArray__p(cellIdentityGsm.getCid());
                int l_RNC_ID = CommonFunctions.getRNCID_or_CID__p(l_byte_array, CommonFunctions.RNCID_C);
                NeighboringCellsInfo.gsmParams.add("2G_SITE_ID");
                hp.put(7, l_RNC_ID);
                NeighboringCellsInfo.gsmParams.add("signalstrength");
                hp.put(8, ss.getDbm());

                NeighboringCellsInfo.gsm_neighboringCellList.add(hp);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static void getNeighboringCellsInfoForWcdma(CellSignalStrengthWcdma ss, CellIdentityWcdma cellIdentityWcdma, boolean isRegistered) {

		/*CellInfoWcdma:{mRegistered=NO mTimeStampType=oem_ril mTimeStamp=10537808770612ns mCellConnectionStatus=0 CellIdentityWcdma:
		{ mLac=2147483647 mCid=2147483647 mPsc=260
		mUarfcn=10757 mMcc=null mMnc=null mAlphaLong= mAlphaShort=} CellSignalStrengthWcdma: ss=6 ber=99}*/
        try {
            if (NeighboringCellsInfo.wcdma_neighboringCellList != null) {
                NeighboringCellsInfo.wcdma_neighborCells = new HashMap<>();
                NeighboringCellsInfo.wcdmaParams = new ArrayList<>();


                NeighboringCellsInfo.wcdmaParams.add("3G_CID");
                if (Utils.isMaxint(cellIdentityWcdma.getCid()))
                {
                    NeighboringCellsInfo.wcdma_neighborCells.put(0, "NA" );

                }
                else {
                    NeighboringCellsInfo.wcdma_neighborCells.put(0, "" + cellIdentityWcdma.getCid());
                }

                NeighboringCellsInfo.wcdmaParams.add("3G_LAC");
                NeighboringCellsInfo.wcdma_neighborCells.put(1, "" + cellIdentityWcdma.getLac());


                NeighboringCellsInfo.wcdmaParams.add("3G_PSC");
                NeighboringCellsInfo.wcdma_neighborCells.put(2, "" + cellIdentityWcdma.getPsc());

                NeighboringCellsInfo.wcdmaParams.add("3G_UARFCN");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    NeighboringCellsInfo.wcdma_neighborCells.put(3, "" + cellIdentityWcdma.getUarfcn());
                } else {
                    NeighboringCellsInfo.wcdma_neighborCells.put(3, "0");

                }

                NeighboringCellsInfo.wcdmaParams.add("3G_RSCP");
                NeighboringCellsInfo.wcdma_neighborCells.put(4, "" + ss.getDbm());
                NeighboringCellsInfo.threeG_neighbor_ss = ss.getDbm();


                NeighboringCellsInfo.wcdmaParams.add("3G_RSSI");
                NeighboringCellsInfo.wcdma_neighborCells.put(5, "0");

                NeighboringCellsInfo.wcdmaParams.add("3G_CQI");
                NeighboringCellsInfo.wcdma_neighborCells.put(6, "0");

                NeighboringCellsInfo.wcdmaParams.add("3G_NODE_BID");
                NeighboringCellsInfo.wcdma_neighborCells.put(7, "" + getenb(cellIdentityWcdma.getCid()));

                NeighboringCellsInfo.wcdmaParams.add("signalstrength");

//
                String signalstrength = ss.toString().replace("CellSignalStrengthWcdma:", "");
                String[] signal = signalstrength.toString().split(" ");
                NeighboringCellsInfo.wcdma_neighborCells.put(8, "" + signalstrength);
//
//
//
//                NeighboringCellsInfo.wcdma_neighboringCellList.add(NeighboringCellsInfo.wcdma_neighborCells);
                NeighboringCellsInfo.wcdmaParams.add("isRegistered");
                NeighboringCellsInfo.wcdma_neighborCells.put(9, "" + isRegistered);
                NeighboringCellsInfo.wcdmaParams.add("3G_MCC");

                if (cellIdentityWcdma.getMcc() != 0) {

                    if (cellIdentityWcdma.getMcc() != Integer.MAX_VALUE) {

                        NeighboringCellsInfo.wcdma_neighborCells.put(11, "" + cellIdentityWcdma.getMcc());//mcc
                    } else {
                        NeighboringCellsInfo.wcdma_neighborCells.put(11, "" + mView_HealthStatus.prim_mcc);//mcc


                    }
                } else {
                    NeighboringCellsInfo.lte_neighborCells.put(11, "" + mView_HealthStatus.prim_mcc);//mcc


                }
                NeighboringCellsInfo.wcdmaParams.add("3G_MNC");

                if (cellIdentityWcdma.getMnc() != 0) {
                    if (cellIdentityWcdma.getMnc() != Integer.MAX_VALUE) {
                        NeighboringCellsInfo.wcdma_neighborCells.put(12, "" + cellIdentityWcdma.getMnc());//mnc
                    } else {
                        NeighboringCellsInfo.wcdma_neighborCells.put(12, "" + mView_HealthStatus.mnc);//mnc

                    }

                } else {
                    NeighboringCellsInfo.wcdma_neighborCells.put(12, "" + mView_HealthStatus.mnc);//mnc

                }


                NeighboringCellsInfo.wcdma_neighboringCellList.add(NeighboringCellsInfo.wcdma_neighborCells);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static void getNeighboringCellsInfoForNR(CellSignalStrengthNr ss, CellIdentityNr cellIdentitynr, boolean isRegistered) {

        try {
            if (NeighboringCellsInfo.nr_neighboringCellList != null) {
                NeighboringCellsInfo.nr_neighborCells = new HashMap<>();
                NeighboringCellsInfo.nrParams = new ArrayList<>();

                NeighboringCellsInfo.nrParams.add("4G_PCI");

                if (cellIdentityNr.getPci() != Integer.MAX_VALUE) {
                    Log.i(Mview.TAG, "PCI is " + cellIdentityNr.getPci());
                    NeighboringCellsInfo.nr_neighborCells.put(0, "" + cellIdentityNr.getPci());//pci
                } else {
                    NeighboringCellsInfo.nr_neighborCells.put(0, "NA");//pci
                }


                NeighboringCellsInfo.nrParams.add("4G_EARFCN");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (cellIdentityNr.getNrarfcn() != Integer.MAX_VALUE) {

                        NeighboringCellsInfo.nr_neighborCells.put(1, "" + cellIdentityNr.getNrarfcn());
                    } else {
                        NeighboringCellsInfo.nr_neighborCells.put(1, "NA");

                    }
                } else {
                    NeighboringCellsInfo.nr_neighborCells.put(1, "NA");
                }
                Log.i(Mview.TAG, "Earfcn is " + cellIdentitynr.getNrarfcn());


                NeighboringCellsInfo.nrParams.add("4G_TA");

                    NeighboringCellsInfo.lte_neighborCells.put(2, "NA");//ta




                NeighboringCellsInfo.nrParams.add("4G_CQI");

                    NeighboringCellsInfo.nr_neighborCells.put(3, "");//cqi


                NeighboringCellsInfo.nrParams.add("4G_RSRQ");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    Log.i(Mview.TAG, "rsrq is " + ss.getSsRsrq());

                    if (ss.getSsRsrq() != Integer.MAX_VALUE) {

                        NeighboringCellsInfo.nr_neighborCells.put(4, "" + ss.getSsRsrq());//rsrq
                    } else {
                        NeighboringCellsInfo.nr_neighborCells.put(4, "NA");//cqi

                    }
                } else {
                    NeighboringCellsInfo.nr_neighborCells.put(4, "NA");//rsrq
                }


                NeighboringCellsInfo.nrParams.add("4G_RSRP");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.i(Mview.TAG, "rsrp is " + ss.getSsRsrp());

                    if (ss.getSsRsrp() != Integer.MAX_VALUE) {

                        NeighboringCellsInfo.nr_neighborCells.put(5, "" + ss.getSsRsrp());//rsrp
                    } else {
                        NeighboringCellsInfo.nr_neighborCells.put(5, "NA");//rsrq

                    }
                } else {
                    NeighboringCellsInfo.nr_neighborCells.put(5, "");//rsrp
//                    NeighboringCellsInfo.nr_neighborCells = 0;
                }


                NeighboringCellsInfo.nrParams.add("4G_TAC");

                if (cellIdentitynr.getTac() != Integer.MAX_VALUE) {

                    NeighboringCellsInfo.nr_neighborCells.put(6, "" + cellIdentitynr.getTac());//tac

                    Log.i(Mview.TAG, "tac is " + cellIdentitynr.getTac());
                } else {
                    NeighboringCellsInfo.nr_neighborCells.put(6, "NA");//tac

                }

                NeighboringCellsInfo.nrParams.add("4G_SINR");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (ss.getCsiSinr() != Integer.MAX_VALUE) {
                        NeighboringCellsInfo.nr_neighborCells.put(7, "" + ss.getCsiSinr());//rssnr
                    } else {
                        NeighboringCellsInfo.nr_neighborCells.put(7, "NA");//rssnr

                    }
                } else {
                    NeighboringCellsInfo.nr_neighborCells.put(7, "");//rssnr

                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.i(Mview.TAG, "sinr is " + ss.getCsiSinr());
                } else {

                }


                NeighboringCellsInfo.nrParams.add("4G_CI");


                if (cellIdentitynr.getNci() != Integer.MAX_VALUE) {
                    NeighboringCellsInfo.nr_neighborCells.put(8, "" + cellIdentitynr.getNci());//cid
                } else {
                    NeighboringCellsInfo.nr_neighborCells.put(8, "NA");//cid

                }


                NeighboringCellsInfo.nrParams.add("4G_ENB");

                NeighboringCellsInfo.nr_neighborCells.put(9, "" + getenb((int) cellIdentitynr.getNci()));//cid


//                NeighboringCellsInfo.nr_neighboringCellList.add("signalstrength");


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (ss.getCsiRsrp() != Integer.MAX_VALUE) {
                        NeighboringCellsInfo.nr_neighborCells.put(10, "" + ss.getCsiRsrp());//rsrp
                    } else {
                        NeighboringCellsInfo.nr_neighborCells.put(10, "NA");//rsrp

                    }
                } else {
                    NeighboringCellsInfo.nr_neighborCells.put(10, "0");//rsrp

                }


                NeighboringCellsInfo.nrParams.add("4G_MCC");

                if (cellIdentitynr.getMccString() != null) {


                        NeighboringCellsInfo.lte_neighborCells.put(11, "" + cellIdentitynr.getMccString());//mcc

                } else {
                    NeighboringCellsInfo.lte_neighborCells.put(11, "" + mView_HealthStatus.prim_mcc);//mcc


                }
                NeighboringCellsInfo. nrParams.add("4G_MNC");

                if (cellIdentitynr.getMncString() !=  null) {
                        NeighboringCellsInfo.nr_neighborCells.put(12, "" + cellIdentitynr.getMncString());//mnc


                } else {
                    NeighboringCellsInfo.nr_neighborCells.put(12, "" + mView_HealthStatus.mnc);//mnc

                }


                NeighboringCellsInfo.nrParams.add("4G_Network");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                    NeighboringCellsInfo.nr_neighborCells.put(13, ListenService.telMgr.getSimOperatorName());//mnc
                } else {
                    NeighboringCellsInfo.nr_neighborCells.put(13, " ");//operator

                }


                NeighboringCellsInfo.nrParams.add("isregistered");
                NeighboringCellsInfo.nr_neighborCells.put(14, "" + isRegistered);//isregisterd


                NeighboringCellsInfo.nr_neighboringCellList.add(NeighboringCellsInfo.nr_neighborCells);
                Log.i(Mview.TAG, "Neighbour cell list " + NeighboringCellsInfo.neighboringCellList);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void getNeighboringCellsInfoForLte(CellSignalStrengthLte ss, CellIdentityLte cellIdentityLte, boolean isRegistered) {
        try {

            if (NeighboringCellsInfo.neighboringCellList != null) {
                NeighboringCellsInfo.lte_neighborCells = new HashMap<>();
                NeighboringCellsInfo.lteParams = new ArrayList<>();

                NeighboringCellsInfo.lteParams.add("4G_PCI");

                if (cellIdentityLte.getPci() != Integer.MAX_VALUE) {
                    Log.i(Mview.TAG, "PCI is " + cellIdentityLte.getPci());
                    NeighboringCellsInfo.lte_neighborCells.put(0, "" + cellIdentityLte.getPci());//pci
                } else {
                    NeighboringCellsInfo.lte_neighborCells.put(0, "NA");//pci
                }


                NeighboringCellsInfo.lteParams.add("4G_EARFCN");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (cellIdentityLte.getEarfcn() != Integer.MAX_VALUE) {

                        NeighboringCellsInfo.lte_neighborCells.put(1, "" + cellIdentityLte.getEarfcn());
                    } else {
                        NeighboringCellsInfo.lte_neighborCells.put(1, "NA");

                    }
                } else {
                    NeighboringCellsInfo.lte_neighborCells.put(1, "NA");
                }
                Log.i(Mview.TAG, "Earfcn is " + cellIdentityLte.getEarfcn());


                NeighboringCellsInfo.lteParams.add("4G_TA");
                if (ss.getTimingAdvance() != Integer.MAX_VALUE) {

                    NeighboringCellsInfo.lte_neighborCells.put(2, "" + ss.getTimingAdvance());//ta
                } else {
                    NeighboringCellsInfo.lte_neighborCells.put(2, "NA");//ta

                }
                Log.i(Mview.TAG, "TA is " + ss.getTimingAdvance());


                NeighboringCellsInfo.lteParams.add("4G_CQI");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (ss.getCqi() != Integer.MAX_VALUE) {

                        NeighboringCellsInfo.lte_neighborCells.put(3, "" + ss.getCqi());//cqi
                    } else {
                        NeighboringCellsInfo.lte_neighborCells.put(3, "NA");//ta

                    }
                } else {
                    NeighboringCellsInfo.lte_neighborCells.put(3, "");//cqi
                }
                Log.i(Mview.TAG, "CQI is " + ss.getCqi());


                NeighboringCellsInfo.lteParams.add("4G_RSRQ");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.i(Mview.TAG, "rsrq is " + ss.getRsrq());

                    if (ss.getRsrq() != Integer.MAX_VALUE) {

                        NeighboringCellsInfo.lte_neighborCells.put(4, "" + ss.getRsrq());//rsrq
                    } else {
                        NeighboringCellsInfo.lte_neighborCells.put(4, "NA");//cqi

                    }
                } else {
                    NeighboringCellsInfo.lte_neighborCells.put(4, "NA");//rsrq
                }


                NeighboringCellsInfo.lteParams.add("4G_RSRP");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.i(Mview.TAG, "rsrp is " + ss.getRsrp());

                    if (ss.getRsrp() != Integer.MAX_VALUE) {

                        NeighboringCellsInfo.lte_neighborCells.put(5, "" + ss.getRsrp());//rsrp
                    } else {
                        NeighboringCellsInfo.lte_neighborCells.put(5, "NA");//rsrq

                    }
                    NeighboringCellsInfo.lte_neighbor_ss = ss.getRsrp();
                } else {
                    NeighboringCellsInfo.lte_neighborCells.put(5, "");//rsrp
                    NeighboringCellsInfo.lte_neighbor_ss = 0;
                }


                NeighboringCellsInfo.lteParams.add("4G_TAC");

                if (cellIdentityLte.getTac() != Integer.MAX_VALUE) {

                    NeighboringCellsInfo.lte_neighborCells.put(6, "" + cellIdentityLte.getTac());//tac

                    Log.i(Mview.TAG, "tac is " + cellIdentityLte.getTac());
                } else {
                    NeighboringCellsInfo.lte_neighborCells.put(6, "NA");//tac

                }

                NeighboringCellsInfo.lteParams.add("4G_SINR");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (ss.getRssnr() != Integer.MAX_VALUE) {
                        NeighboringCellsInfo.lte_neighborCells.put(7, "" + ss.getRssnr());//rssnr
                    } else {
                        NeighboringCellsInfo.lte_neighborCells.put(7, "NA");//rssnr

                    }
                } else {
                    NeighboringCellsInfo.lte_neighborCells.put(7, "");//rssnr

                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.i(Mview.TAG, "sinr is " + ss.getRssnr());
                } else {

                }


                NeighboringCellsInfo.lteParams.add("4G_CI");


                if (cellIdentityLte.getCi() != Integer.MAX_VALUE) {
                    NeighboringCellsInfo.lte_neighborCells.put(8, "" + cellIdentityLte.getCi());//cid
                } else {
                    NeighboringCellsInfo.lte_neighborCells.put(8, "NA");//cid

                }


                NeighboringCellsInfo.lteParams.add("4G_ENB");

                NeighboringCellsInfo.lte_neighborCells.put(9, "" + getenb(cellIdentityLte.getCi()));//cid


                NeighboringCellsInfo.lteParams.add("signalstrength");


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (ss.getRsrp() != Integer.MAX_VALUE) {
                        NeighboringCellsInfo.lte_neighborCells.put(10, "" + ss.getRsrp());//rsrp
                    } else {
                        NeighboringCellsInfo.lte_neighborCells.put(10, "NA");//rsrp

                    }
                } else {
                    NeighboringCellsInfo.lte_neighborCells.put(10, "0");//rsrp

                }


                NeighboringCellsInfo.lteParams.add("4G_MCC");

                if (cellIdentityLte.getMcc() != 0) {

                    if (cellIdentityLte.getMcc() != Integer.MAX_VALUE) {

                        NeighboringCellsInfo.lte_neighborCells.put(11, "" + cellIdentityLte.getMcc());//mcc
                    } else {
                        NeighboringCellsInfo.lte_neighborCells.put(11, "" + mView_HealthStatus.prim_mcc);//mcc


                    }
                } else {
                    NeighboringCellsInfo.lte_neighborCells.put(11, "" + mView_HealthStatus.prim_mcc);//mcc


                }
                NeighboringCellsInfo.lteParams.add("4G_MNC");

                if (cellIdentityLte.getMnc() != 0) {
                    if (cellIdentityLte.getMnc() != Integer.MAX_VALUE) {
                        NeighboringCellsInfo.lte_neighborCells.put(12, "" + cellIdentityLte.getMnc());//mnc
                    } else {
                        NeighboringCellsInfo.lte_neighborCells.put(12, "" + mView_HealthStatus.mnc);//mnc

                    }

                } else {
                    NeighboringCellsInfo.lte_neighborCells.put(12, "" + mView_HealthStatus.mnc);//mnc

                }


                NeighboringCellsInfo.lteParams.add("4G_Network");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                    NeighboringCellsInfo.lte_neighborCells.put(13, cellIdentityLte.getMobileNetworkOperator());//mnc
                } else {
                    NeighboringCellsInfo.lte_neighborCells.put(13, " ");//operator

                }


                NeighboringCellsInfo.lteParams.add("isregistered");
                NeighboringCellsInfo.lte_neighborCells.put(14, "" + isRegistered);//isregisterd


                NeighboringCellsInfo.neighboringCellList.add(NeighboringCellsInfo.lte_neighborCells);
                Log.i(Mview.TAG, "Neighbour cell list " + NeighboringCellsInfo.neighboringCellList);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getenb(int cid) {
           /*
                        Getting enb
                         */
        int eNB = 0;
        if (cid != Integer.MAX_VALUE) {
            String cellidHex = DecToHex(cid);
            if (cellidHex != null) {
                //System.out.println("cellidhex..  "+cellidHex);//66
                if (cellidHex.length() > 2) {

                    String eNBHex = cellidHex.substring(0, cellidHex.length() - 2);//last 2 digits represent local cellid
                    //System.out.println("enBhex" + eNBHex);

                    try {
                        eNB = HexToDec(eNBHex);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //System.out.println("exception here  " + e.toString());
                    }
                }


            }
        }
        return eNB;
    }


    // @Override
    public void onDataActivity(int direction) {
        super.onDataActivity(direction);
        switch (direction) {
            case TelephonyManager.DATA_ACTIVITY_NONE:
                Log.i(LOG_TAG, "onDataActivity: DATA_ACTIVITY_NONE");
                break;
            case TelephonyManager.DATA_ACTIVITY_IN:
                Log.i(LOG_TAG, "onDataActivity: DATA_ACTIVITY_IN");
                break;
            case TelephonyManager.DATA_ACTIVITY_OUT:
                Log.i(LOG_TAG, "onDataActivity: DATA_ACTIVITY_OUT");
                break;
            case TelephonyManager.DATA_ACTIVITY_INOUT:
                Log.i(LOG_TAG, "onDataActivity: DATA_ACTIVITY_INOUT");
                break;
            case TelephonyManager.DATA_ACTIVITY_DORMANT:
                Log.i(LOG_TAG, "onDataActivity: DATA_ACTIVITY_DORMANT");
                break;
            default:
                Log.w(LOG_TAG, "onDataActivity: UNKNOWN " + direction);
                break;
        }
    }

    @Override
    public void onServiceStateChanged(ServiceState serviceState) {
        super.onServiceStateChanged(serviceState);


        if (incomingCallStatus && currentCallObject != null) {
            currentCallObject.serviceStateArr.add(serviceState);
        }
        lastServiceState = serviceState;

        String lat = "";
        String lon = "";
       /* if (ListenService.gps.canGetLocation()) {
            lat = ListenService.gps.getLatitude() + "";
            lon = ListenService.gps.getLongitude() + "";
        }*/

        if (telMgr != null) {
            mView_HealthStatus.simOperatorName = telMgr.getSimOperatorName();
            mView_HealthStatus.OperatorName = telMgr.getNetworkOperatorName();
        }

        int ind = currServiceStateIndex + 1;
        currServiceStateIndex = (ind) % maxServiceStatesToRecord;
        //currServiceStateIndex++;

        RecordedServiceState r = new RecordedServiceState(lat, lon, serviceState, new Date());
        int sz = last5CellServiceStateArr.size();
        try {
            RecordedServiceState r1 = last5CellServiceStateArr.get(currServiceStateIndex);
            last5CellServiceStateArr.set(currServiceStateIndex, r);
        } catch (Exception e) {
            last5CellServiceStateArr.add(currServiceStateIndex, r);
        }

        //last5CellServiceStateArr.add(currServiceStateIndex, r);
        //currServiceStateIndex++;
        mView_HealthStatus.carrier_selection = serviceState.getIsManualSelection();

        Log.i(LOG_TAG, "onServiceStateChanged: " + serviceState.toString());
        Log.i(LOG_TAG, "onServiceStateChanged: getOperatorAlphaLong "
                + serviceState.getOperatorAlphaLong());
        Log.i(LOG_TAG, "onServiceStateChanged: getOperatorAlphaShort "
                + serviceState.getOperatorAlphaShort());
        Log.i(LOG_TAG, "onServiceStateChanged: getOperatorNumeric "
                + serviceState.getOperatorNumeric());
        Log.i(LOG_TAG, "onServiceStateChanged: getIsManualSelection "
                + serviceState.getIsManualSelection());
        Log.i(LOG_TAG,
                "onServiceStateChanged: getRoaming "
                        + serviceState.getRoaming());
        mView_HealthStatus.roaming = serviceState.getRoaming();
        switch (serviceState.getState()) {
            case ServiceState.STATE_IN_SERVICE:
                Log.i(LOG_TAG, "onServiceStateChanged: STATE_IN_SERVICE");
                break;
            case ServiceState.STATE_OUT_OF_SERVICE:
                Log.i(LOG_TAG, "onServiceStateChanged: STATE_OUT_OF_SERVICE");
                break;
            case ServiceState.STATE_EMERGENCY_ONLY:
                Log.i(LOG_TAG, "onServiceStateChanged: STATE_EMERGENCY_ONLY");
                break;
            case ServiceState.STATE_POWER_OFF:
                Log.i(LOG_TAG, "onServiceStateChanged: STATE_POWER_OFF");
                break;
        }
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        System.out.println("calling incoming num1 " + incomingNumber + "call status " + state);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:

                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_IDLE");
                break;
            case TelephonyManager.CALL_STATE_RINGING:

                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_RINGING");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:


                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_OFFHOOK");
                break;
            default:
                incomingCallStatus = false;
                outgoingCallStatus = false;
                Log.i(LOG_TAG, "UNKNOWN_STATE: " + state);
                break;
        }
    }

    public MyCall createCallObject(String incomingNumber) {

        MyCall obj = new MyCall();
        obj.callerPhoneNumber = incomingNumber;
        obj.timeofCall = new Date();
        obj.timeofcallInMS = System.currentTimeMillis();
        obj.timeofcallInMSNew = Utils.getDateTime();
        obj.operator = telMgr.getNetworkOperatorName();
        obj.isRoaming = lastServiceState.getRoaming();
//        obj.speed = ListenService.gps.getSpeed();

        myCallArray.add(obj);

//        if (ListenService.gps.canGetLocation()) {
//            obj.myLat = ListenService.gps.getLatitude() + "";
//            obj.myLon = ListenService.gps.getLongitude() + "";
//        }
//        System.out.println("calling incoming num " + incomingNumber + Utils.getDateTime() + " " + "lat " + obj.myLat + "long  " + obj.myLon);
        return obj;
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCellLocationChanged(CellLocation location) {

        super.onCellLocationChanged(location);
        Log.i("LogFapps", "onCellLocationChanged ");
        try {
            ArrayList<CellLocation> list = new ArrayList<>();
            list.add(location);
            if (incomingCallStatus && currentCallObject != null) {
                currentCallObject.cellLocationArr.add(location);
            }
            lastCellLocation = location;
//            String lat = "";
//            String lon = "";
//            if (ListenService.gps.canGetLocation()) {
//                lat = ListenService.gps.getLatitude() + "";
//                lon = ListenService.gps.getLongitude() + "";
//            }

            int ind = (currLocationIndex + 1) % maxLocationsToRecord;
            currLocationIndex = ind;
            RecordedCellLocation r = new RecordedCellLocation("0", "0", location, new Date());

            try {
                RecordedCellLocation r1 = last5CellLocationArr.get(currLocationIndex);
                last5CellLocationArr.set(currLocationIndex, r);
            } catch (Exception e) {
                last5CellLocationArr.add(currLocationIndex, r);
            }
            //System.out.println("context  "+ context);
            if (mView_HealthStatus.timeSeriesServingCellDataArray != null) {
                //System.out.println("size of array  "+mView_HealthStatus.timeSeriesServingCellDataArray.size());
            } else {
                //System.out.println("array null ");
            }

            if (mView_HealthStatus.timeSeriesServingCellDataArray == null /*&& MainActivity.context != null*/) {
                mView_HealthStatus.timeSeriesServingCellDataArray = new ArrayList<CurrentCellServing>();
                //System.out.println("array value"+mView_HealthStatus.timeSeriesServingCellDataArray);


            }
            if (mView_HealthStatus.lteparams == null) {
                ArrayList<LteParams.Paramslist> lteparams = new ArrayList<>();

            }

            if (mView_HealthStatus.timeSeriesServingCellDataArray != null && mView_HealthStatus.timeSeriesServingCellDataArray.size() > 0) {
                currentCellServing.serveTime = ((System.currentTimeMillis() - currentCellServing.captureTime) / 1000) + "";
            }

            currentCellServing = new CurrentCellServing();
            if (mView_HealthStatus.timeSeriesServingCellDataArray != null)
                mView_HealthStatus.timeSeriesServingCellDataArray.add(currentCellServing);
            currentCellServing.captureTime = System.currentTimeMillis();
//            currentCellServing.lat = ListenService.gps.getLatitude() + "";
//            currentCellServing.lon = ListenService.gps.getLongitude() + "";
            int networkType = telMgr.getNetworkType();
            mView_HealthStatus.strCurrentNetworkState = MyPhoneStateListener.getNetworkClass(networkType, mContext);

            if (mView_HealthStatus.iCurrentNetworkState != 0)
                currentCellServing.networkType = mView_HealthStatus.iCurrentNetworkState + "G";
            else
                currentCellServing.networkType = "NS";


            if (location instanceof GsmCellLocation) {


                GsmCellLocation gcLoc = (GsmCellLocation) location;
                mView_HealthStatus.cellLocationType = "GSM";
                mView_HealthStatus.Lac = gcLoc.getLac() + "";

                //hack
                if (mView_HealthStatus.iCurrentNetworkState == 4)
                    mView_HealthStatus.lteTAC = gcLoc.getLac() + "";

                mView_HealthStatus.Psc = gcLoc.getPsc() + "";
                mView_HealthStatus.Cid = gcLoc.getCid() + "";


                currentCellServing.LAC = mView_HealthStatus.Lac;
                currentCellServing.cellId = mView_HealthStatus.Cid;
                currentCellServing.ci = mView_HealthStatus.Psc;


                mView_HealthStatus.lteENB = String.valueOf(getenb(gcLoc.getCid()));

                currentCellServing.lteENB = String.valueOf(getenb(gcLoc.getCid()));


            } else if (location instanceof CdmaCellLocation) {
                CdmaCellLocation ccLoc = (CdmaCellLocation) location;

            } else {
            }


            try {
                if (telMgr != null) {
                    List<CellInfo> cellInfo = telMgr.getAllCellInfo();
                    System.out.println("cell info 2 " + cellInfo);
                    if ((cellInfo != null) && cellInfo.size() > 0) {
                        fetchCellsInfo(cellInfo);
                    }
                }
            }


		/*{
			List<CellInfo> cellInfo = ListenService.telMgr.getAllCellInfo();

			NeighboringCellsInfo.neighboringCellList = new ArrayList<>();
			NeighboringCellsInfo.wcdma_neighboringCellList = new ArrayList<>();
			NeighboringCellsInfo.gsm_neighboringCellList = new ArrayList<>();
			ArrayList<String> op_nameslte=new ArrayList<>();
			ArrayList<String> op_nameswcdma=new ArrayList<>();
			ArrayList<String> op_namesgsm=new ArrayList<>();




				if (cellInfo != null)
			{

				for(int c=0;c<cellInfo.size();c++) {
					CellInfo cellInfo1 = cellInfo.get(c);
					if (cellInfo1 != null)
					{

						if (cellInfo1.isRegistered()) {
							if (cellInfo1 instanceof CellInfoLte) {

								String[] info = cellInfo1.toString().split(" ");
								String alphaName = info[12] + info[13];

								op_nameslte.add(alphaName);
							} else if (cellInfo1 instanceof CellInfoWcdma) {
								String[] info = cellInfo1.toString().split(" ");
								String alphaName = info[11] + info[12];

								op_nameswcdma.add(alphaName);
							} else if (cellInfo1 instanceof CellInfoGsm) {

							}
						}
					}
				}





				for (int i = 0; i < cellInfo.size(); i++) {
				CellInfo m = cellInfo.get(i);
				System.out.println("cell info index " + i);

				if (m instanceof CellInfoLte)
				{

					cellInfoLte = (CellInfoLte) m;
					cellIdentityLte = cellInfoLte.getCellIdentity();
					CellSignalStrengthLte ss = cellInfoLte.getCellSignalStrength();
					if (m.isRegistered()) {
						String[] info = m.toString().split(" ");
						String alphaName = info[12] + info[13];
						if (finalOp_Nameslte != null && finalOp_Nameslte.size() > 0) {

							for (int j = 0; j < finalOp_Nameslte.size(); j++)
							{


								if (finalOp_Nameslte.get(j).equalsIgnoreCase(alphaName))
								{
									getLteCellInfoForFirstSim(ss);

								}
								else
								{
									getLteCellInfoForAnotherSim(ss);

								}

							}
						}
					}else {
							*//*{mRegistered=YES mTimeStampType=oem_ril mTimeStamp=73643362238166ns CellIdentityLte:{ mMcc=405 mMnc=872 mCi=119577 mPci=261 mTac=7}
							CellSignalStrengthLte: ss=31 rsrp=-80 rsrq=-12 rssnr=2147483647 cqi=2147483647 ta=2147483647}*//*
						getNeighboringCellsInfoForLte(ss, cellIdentityLte);


					}

				}

				else if (m instanceof CellInfoGsm) {

					//	Utils.showToast(context,"instance of gsm ");

					CellInfoGsm cellInfogsm = (CellInfoGsm) m;
					CellIdentityGsm c = cellInfogsm.getCellIdentity();
					CellSignalStrengthGsm ss = cellInfogsm.getCellSignalStrength();
					if (m.isRegistered()) {


						try {
							int asus = cellInfogsm.getCellSignalStrength().getAsuLevel();
							int dbm = cellInfogsm.getCellSignalStrength().getDbm();
							int level = cellInfogsm.getCellSignalStrength().getLevel();
							int cc = cellInfogsm.getCellSignalStrength().describeContents();
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
								String ta = String.valueOf(ss.getTimingAdvance());
								//System.out.println("ta is " + ta);
								//	//Toast.makeText(mContext, "ta is "+ta, //Toast.LENGTH_SHORT).show();
								mView_HealthStatus.lteta = ta;
							}
							mView_HealthStatus.lteasus = asus + "";
							mView_HealthStatus.ltedbm = dbm + "";
							mView_HealthStatus.ltelevel = level + "";
							//mView_HealthStatus.gsmSignalStrength=cellInfogsm.getCellSignalStrength().getDbm();


							int cid = c.getCid();
							int lac = c.getLac();
							int psc = c.getPsc();


							mView_HealthStatus.Psc = String.valueOf(psc);
							mView_HealthStatus.Cid = String.valueOf(cid);
							mView_HealthStatus.Lac = String.valueOf(lac);


//                            if (c.getMcc() < 2147483647) {
							///////check!!!!
							*//*if (Build.VERSION.SDK_INT >= 24) {
								int arfcn = c.getArfcn();
								int bsic = c.getBsic();
								mView_HealthStatus.lteArfcn = arfcn + "";
								//just in case for LTE if it comes here
								if (mView_HealthStatus.iCurrentNetworkState != 4)
									mView_HealthStatus.ltePCI = bsic + "";
								currentCellServing.arfcn = arfcn + "";
							}*//*


						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						getNeighboringCellsInfoForGSM(ss, c);
					}

				} else if (m instanceof CellInfoWcdma) {

					mView_HealthStatus.currentInstance = "wcdma";
					CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) m;
					CellIdentityWcdma cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
					CellSignalStrengthWcdma ss = cellInfoWcdma.getCellSignalStrength();
					if (m.isRegistered()) {

					} else {
						getNeighboringCellsInfoForWcdma(ss, cellIdentityWcdma);
					}
				}


			}
		}

		}*/ catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCallForwardingIndicatorChanged(boolean cfi) {
        super.onCallForwardingIndicatorChanged(cfi);
        Log.i(LOG_TAG, "onCallForwardingIndicatorChanged: " + cfi);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMessageWaitingIndicatorChanged(boolean mwi) {
        super.onMessageWaitingIndicatorChanged(mwi);
        Log.i(LOG_TAG, "onMessageWaitingIndicatorChanged: " + mwi);
    }

    public void onCarrierNetworkChange(boolean active) {

    }

    public static String getSignalStrength(SignalStrength signalStrength) {
        String ss = "";

        if (signalStrength.isGsm()) {
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getGsmBitErrorRate "
                    + signalStrength.getGsmBitErrorRate());
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getGsmSignalStrength "
                    + signalStrength.getGsmSignalStrength());
            ss = signalStrength.getGsmSignalStrength() + "dBm";
        } else if (signalStrength.getCdmaDbm() > 0) {
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getCdmaDbm "
                    + signalStrength.getCdmaDbm());
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getCdmaEcio "
                    + signalStrength.getCdmaEcio());
            ss = signalStrength.getCdmaDbm() + "dBm";
        } else {
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getEvdoDbm "
                    + signalStrength.getEvdoDbm());
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getEvdoEcio "
                    + signalStrength.getEvdoEcio());
            Log.i(LOG_TAG, "onSignalStrengthsChanged: getEvdoSnr "
                    + signalStrength.getEvdoSnr());
        }

        // Reflection code starts from here
        try {
            Method[] methods = SignalStrength.class
                    .getMethods();
            for (Method mthd : methods) {
                if (mthd.getName().equals("getLteSignalStrength")
                /* || mthd.getName().equals("getLteRsrp")
                 || mthd.getName().equals("getLteRsrq")
				 || mthd.getName().equals("getLteRssnr")
				 || mthd.getName().equals("getLteCqi")*/
                ) {
                    Log.i(LOG_TAG,
                            "onSignalStrengthsChanged LTE: " + mthd.getName() + " "
                                    + mthd.invoke(signalStrength));
                    String ss1 = mthd.invoke(signalStrength) + "";
                    if (!ss1.equals("99"))
                        ss = "LTE " + ss1 + "dBm";

                }//end if
            }//end for
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return ss;
    }

    @SuppressLint("MissingPermission")
    public static String getNetworkClass(int networkType, Context ctx) {
        mContext = ctx;

        String proto = "";
        String proto1 = "";

        TelephonyManager teleMan = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

        System.out.println("network type " + networkType);
        //check with Sir once
        if (networkType == 18) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return checkStringNetworkTypeAccToVoice(teleMan.getVoiceNetworkType()) + "";
            } else {

                mView_HealthStatus.iCurrentNetworkState = 0;
                //return mView_HealthStatus.iCurrentNetworkState; }
            }
        } else {
            switch (networkType) {

                case TelephonyManager.NETWORK_TYPE_GPRS:
                    proto = "2G (GPRS)";
                    proto1 = "GPRS";
                    mView_HealthStatus.iCurrentNetworkState = 2;
                    break;
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    proto = "2G (EDGE)";
                    proto1 = "EDGE";
                    mView_HealthStatus.iCurrentNetworkState = 2;
                    break;
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    proto = "2G (CDMA)";
                    proto1 = "CDMA";
                    mView_HealthStatus.iCurrentNetworkState = 2;
                    break;
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    proto = "2G (1xRTT)";
                    proto1 = "1xRTT";
                    mView_HealthStatus.iCurrentNetworkState = 2;
                    break;
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    proto = "2G (IDEN)";
                    proto1 = "IDEN";
                    mView_HealthStatus.iCurrentNetworkState = 2;
                    break;

                case TelephonyManager.NETWORK_TYPE_UMTS:
                    proto = "3G (UMTS)";
                    proto1 = "UMTS";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    proto = "3G (EVDO_0)";
                    proto1 = "EVDO_0";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    proto = "3G (EVDO_A)";
                    proto1 = "EVDO_A";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    proto = "3G (HSDPA)";
                    proto1 = "HSDPA";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    proto = "3G (HSUPA)";
                    proto1 = "HSUPA";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    proto = "3G (HSPA)";
                    proto1 = "HSPA";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    proto = "3G (EVDO_B)";
                    proto1 = "EVDO_B";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    proto = "3G (EHRPD)";
                    proto1 = "EHRPD";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    proto = "3G (HSPAP)";
                    proto1 = "HSPAP";
                    mView_HealthStatus.iCurrentNetworkState = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    proto = "4G(LTE)";
                    proto1 = "LTE";
                    mView_HealthStatus.iCurrentNetworkState = 4;
                    break;

                default:
                    proto = "NS";
                    proto1 = "NS";
                    mView_HealthStatus.iCurrentNetworkState = 0;
            }

            //	Toast.makeText(mContext, "current ntwrk state "+mView_HealthStatus.iCurrentNetworkState +networkType, Toast.LENGTH_SHORT).show();
            mView_HealthStatus.strCurrentNetworkState = proto;
            mView_HealthStatus.strCurrentNetworkProtocol = proto1;
            return proto;


        }
        return proto;
    }


    public class RecordedCellLocation {
        public String myLat;
        public String myLon;
        public CellLocation loc;
        public Date dt;

        public RecordedCellLocation(String lat, String lon, CellLocation loc1, Date dt1) {
            myLat = lat;
            myLon = lon;
            loc = loc1;
            dt = dt1;
        }
    }

    public class RecordedServiceState {
        public String myLat;
        public String myLon;
        public ServiceState service;
        public Date dt;

        public RecordedServiceState(String lat, String lon, ServiceState ser, Date dt1) {
            myLat = lat;
            myLon = lon;
            service = ser;
            dt = dt1;
        }
    }

    public static String DecToHex(int dec) {
//        return String.format("%x", dec);
        return Integer.toHexString(dec);

    }

    // hex -> decimal
    public static int HexToDec(String hex) {
        return Integer.parseInt(hex, 16);

    }


}
