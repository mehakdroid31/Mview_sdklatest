package com.functionapps.mview_sdk2.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsManager;
import android.telephony.ims.ImsMmTelManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;


import com.functionapps.mview_sdk2.main.Mview;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;
import static android.content.Context.TELEPHONY_SUBSCRIPTION_SERVICE;
import static com.functionapps.mview_sdk2.service.ListenService.telMgr;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
public class SimInfoUtility {
    private static final String TAG = "SimInfoUtility";

    /* private ImsMmTelManager getImsMmTelManager(int subId) {
         if (!SubscriptionManager.isUsableSubscriptionId(subId)) {
             return null;
         }
         ImsManager imsMgr = mContext.getSystemService(ImsManager.class);
         return (imsMgr == null) ? null : imsMgr.getImsMmTelManager(subId);
     }
 */
    public static void getWifiCallingStatus(Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PRECISE_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.i(TAG, "Permission not given");
            return;
        }
        List<SubscriptionInfo> subscriptionInfos1
                = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
        for (int i = 0; i < subscriptionInfos1.size(); i++) {
            SubscriptionInfo lsuSubscriptionInfo = subscriptionInfos1.get(i);
            if (lsuSubscriptionInfo != null) {
                int subId = lsuSubscriptionInfo.getSubscriptionId();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    ImsManager imsMgr = context.getSystemService(ImsManager.class);
                    ImsMmTelManager imsMmTelManager = imsMgr.getImsMmTelManager(subId);

                } else {
                    Log.i(TAG, "sdk version down ");
                }
            }
            // ImsMmTelManager imsMmTelManager = ImsManager.getImsMmTelManager()

        }
    }

    public static void getSimInfo(Context context) {
        getWifiCallingStatus(context);
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    List<SubscriptionInfo> subscriptionInfos1
                            = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
                    mView_HealthStatus.subSize = subscriptionInfos1.size();

                    for (int i = 0; i < subscriptionInfos1.size(); i++) {
                        SubscriptionInfo lsuSubscriptionInfo = subscriptionInfos1.get(i);
                        if (lsuSubscriptionInfo != null) {

                            if (i == 0) {
                                mView_HealthStatus.prim_imsi = Utils.getImsi();
                                mView_HealthStatus.prim_NetworkType = mView_HealthStatus.strCurrentNetworkProtocol;
                                mView_HealthStatus.prim_carrierName = lsuSubscriptionInfo.getCarrierName() + "";
                                if (lsuSubscriptionInfo.getDataRoaming() == 1) {
                                    mView_HealthStatus.prim_getDataRoaming = "Yes";
                                } else {
                                    mView_HealthStatus.prim_getDataRoaming = "No";
                                }
                                mView_HealthStatus.prim_mcc = lsuSubscriptionInfo.getMcc();
                                mView_HealthStatus.prim_mnc = lsuSubscriptionInfo.getMnc();
                                mView_HealthStatus.prim_Slot = lsuSubscriptionInfo.getSimSlotIndex();
                            } else if (i == 1) {
                                mView_HealthStatus.sec_NetworkType = mView_HealthStatus.strCurrentNetworkProtocol;
                                mView_HealthStatus.sec_slot = lsuSubscriptionInfo.getSimSlotIndex();
                                mView_HealthStatus.sec_carrierName = lsuSubscriptionInfo.getCarrierName() + "";
                                if (lsuSubscriptionInfo.getDataRoaming() == 1) {
                                    mView_HealthStatus.sec_getDataRoaming = "Yes";
                                } else {
                                    mView_HealthStatus.sec_getDataRoaming = "No";
                                }

                                mView_HealthStatus.second_Mcc = lsuSubscriptionInfo.getMcc();
                                mView_HealthStatus.second_Mnc = lsuSubscriptionInfo.getMnc();
                            }

                            Log.i(TAG, "For sim index " + 0);
                            Log.d(TAG, "New_getNumber " + lsuSubscriptionInfo.getNumber());
                            Log.d(TAG, "New_network name : " + lsuSubscriptionInfo.getCarrierName());
                            Log.d(TAG, "New_getCountryIso " + lsuSubscriptionInfo.getCountryIso());
                            Log.d(TAG, "New_getDataRoaming " + lsuSubscriptionInfo.getDataRoaming());
                            Log.d(TAG, "New_getSubId " + lsuSubscriptionInfo.getSubscriptionId());
                            Log.d(TAG, "New_getDisplayName " + lsuSubscriptionInfo.getDisplayName());
                            Log.d(TAG, "New_getMCC " + lsuSubscriptionInfo.getMcc());
                            Log.d(TAG, "New_getMNC " + lsuSubscriptionInfo.getMnc());
                            Log.d(TAG, "New_getSimSlot " + lsuSubscriptionInfo.getSimSlotIndex());
                        }

                    }

//callAnotherFunction(context);
                }
            } else {
                mView_HealthStatus.simPref = "Primary";
                mView_HealthStatus.prim_carrierName = telMgr.getSimOperator();
                if (MyPhoneStateListener.lastServiceState.getRoaming()) {
                    mView_HealthStatus.prim_getDataRoaming = "Yes";
                } else {
                    mView_HealthStatus.prim_getDataRoaming = "No";
                }
                mView_HealthStatus.prim_imsi = Constants.IMSI;
                mView_HealthStatus.prim_Slot = 0;
                if (mView_HealthStatus.carrier_selection) {
                    mView_HealthStatus.prim_carrierMode = 1;
                } else {
                    mView_HealthStatus.prim_carrierMode = 0;
                }
                mView_HealthStatus.prim_NetworkType = mView_HealthStatus.strCurrentNetworkProtocol;
                // getOutput(context,"getCarrierName",0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    @SuppressLint("MissingPermission")
    public static void callAnotherFunction(Context context) {
        System.out.println("simget imsi is +" + Utils.getImsi());
        System.out.println("Sim permission");
        List<SubscriptionInfo> subscriptionInfos = null;
        SubscriptionManager subManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        subscriptionInfos = SubscriptionManager.from(context).getActiveSubscriptionInfoList();

        if (subscriptionInfos != null && !subscriptionInfos.isEmpty()) {
            for (int i = 0; i < subscriptionInfos.size(); i++) {
                SubscriptionInfo subInfo = subscriptionInfos.get(i);
                mView_HealthStatus.subSize = subscriptionInfos.size();
                System.out.println("sub size " + mView_HealthStatus.subSize);
                SubscriptionInfo lsuSubscriptionInfo = subscriptionInfos.get(i);
                System.out.println("SimgetSlot " + lsuSubscriptionInfo.getSimSlotIndex());
                String imsi = getImsiSlotWise(lsuSubscriptionInfo.getSimSlotIndex(), context);
                System.out.println("SimgetImsi " + imsi);
                if (mView_HealthStatus.subSize > 1) {
                    if (Constants.IMSI != null) {
                        System.out.println("imsi on comparing is " + getImsi(context));
                        if (getImsi(context).equals(getImsiSlotWise(0, context))) {
                            mView_HealthStatus.sec_carrierMode = 2;
                            if (mView_HealthStatus.carrier_selection) {
                                mView_HealthStatus.prim_carrierMode = 1;
                            } else {
                                mView_HealthStatus.prim_carrierMode = 0;

                            }
                            mView_HealthStatus.prim_NetworkType = mView_HealthStatus.strCurrentNetworkProtocol;
                            System.out.println("nettwork type " + mView_HealthStatus.strCurrentNetworkProtocol);

                        } else {
                            mView_HealthStatus.prim_carrierMode = 2;
                            if (mView_HealthStatus.carrier_selection) {
                                mView_HealthStatus.sec_carrierMode = 1;

                            } else {
                                mView_HealthStatus.sec_carrierMode = 0;

                            }
                            mView_HealthStatus.sec_NetworkType = mView_HealthStatus.strCurrentNetworkProtocol;
                            System.out.println("nettwork type " + mView_HealthStatus.strCurrentNetworkProtocol);
                        }
                        System.out.println("carrier prime " + mView_HealthStatus.prim_carrierMode);
                        System.out.println("carrier sec " + mView_HealthStatus.sec_carrierMode);
                        // {
                        if (i == 0) {
                            getPrimaryInfo(lsuSubscriptionInfo, imsi, subscriptionInfos.get(i));

                            //mView_HealthStatus.prim_ss= MyPhoneStateListener.getSignalStrengthForPrim();
                            //telephonyManager = telMgr.createForSubscriptionId(lsuSubscriptionInfo.getSubscriptionId());

                        } else {

                            mView_HealthStatus.simPref = "Secondary";
                            mView_HealthStatus.sec_carrierName = (String) lsuSubscriptionInfo.getCarrierName();
                            if (lsuSubscriptionInfo.getDataRoaming() == 1) {
                                mView_HealthStatus.sec_getDataRoaming = "Yes";
                            } else {
                                mView_HealthStatus.sec_getDataRoaming = "No";
                            }
                            mView_HealthStatus.sec_mcc = lsuSubscriptionInfo.getMcc();
                            mView_HealthStatus.sec_mnc = lsuSubscriptionInfo.getMnc();
                            mView_HealthStatus.sec_imsi = imsi;
                            mView_HealthStatus.sec_slot = lsuSubscriptionInfo.getSimSlotIndex();
                            // mView_HealthStatus.sec_NetworkType=getNetworkTypeName(subscriptionInfos.get(i).getSubscriptionId());
                            System.out.println("simget icc " + lsuSubscriptionInfo.getIccId());
                            System.out.println("getSim1 roaming" + lsuSubscriptionInfo.getDataRoaming());
                        }
                    }
                } else {
                    mView_HealthStatus.sec_carrierMode = 2;
                    if (mView_HealthStatus.carrier_selection) {
                        mView_HealthStatus.prim_carrierMode = 1;
                    } else {
                        mView_HealthStatus.prim_carrierMode = 0;
                    }
                    mView_HealthStatus.prim_NetworkType = mView_HealthStatus.strCurrentNetworkProtocol;
                    getPrimaryInfo(lsuSubscriptionInfo, imsi, subscriptionInfos.get(0));
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static String getSecondIMSI(Context context) {
        String imsi = "NA";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1&&Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            try {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                Method getSubId = TelephonyManager.class.getMethod("getSubscriberId", int.class);
                SubscriptionManager sm = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                if (ActivityCompat.checkSelfPermission(Mview.fapps_ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return "NA";
                }
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
        }
        return imsi;


    }

    private static void getPrimaryInfo(SubscriptionInfo lsuSubscriptionInfo, String imsi, SubscriptionInfo subscriptionInfo) {
        mView_HealthStatus.simPref = "Primary";
        mView_HealthStatus.prim_carrierName = (String) lsuSubscriptionInfo.getCarrierName();
        if (lsuSubscriptionInfo.getDataRoaming() == 1) {
            mView_HealthStatus.prim_getDataRoaming = "Yes";
        } else {
            mView_HealthStatus.prim_getDataRoaming = "No";
        }
        mView_HealthStatus.prim_mcc = lsuSubscriptionInfo.getMcc();
        mView_HealthStatus.prim_mnc = lsuSubscriptionInfo.getMnc();

        Log.i(TAG, "Imsi here is  " + imsi);

        if (imsi == null)
            mView_HealthStatus.prim_imsi = Constants.IMSI;
        else if (imsi.equalsIgnoreCase("NA")) {

            mView_HealthStatus.prim_imsi = Constants.IMSI;
        } else
            mView_HealthStatus.prim_imsi = imsi;


        mView_HealthStatus.prim_Slot = lsuSubscriptionInfo.getSimSlotIndex();
        // mView_HealthStatus.prim_NetworkType=getNetworkTypeName(subscriptionInfo.getSubscriptionId());
        getIsManualSelection(subscriptionInfo.getSubscriptionId());
        System.out.println("sim get icc 1" + lsuSubscriptionInfo.getIccId());
        System.out.println("getSim roaming" + lsuSubscriptionInfo.getDataRoaming());

    }

    public static String getImsi(Context context) {
        String IMSI = "NA";
        if (context != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

                TelephonyManager operator = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    IMSI = operator.getSubscriberId();
                    if (IMSI == null)
                        IMSI = "";
                    return IMSI;
                }
            }
        }
        return IMSI;
    }

    private static void getIsManualSelection(int subscriptionId) {
    }

   /* public boolean getIsManualSelection() {
        return mIsManualNetworkSelection;
    }*/

    public static String getImsiSlotWise(int slot, Context context) {
        String imsi = "NA";
        imsi = Utils.getImsi();
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1&&Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            {
                Method getSubId = TelephonyManager.class.getMethod("getSubscriberId", int.class);

                SubscriptionManager sm = (SubscriptionManager) context.getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                        == PackageManager.PERMISSION_GRANTED) {

                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    imsi = (String) getSubId.invoke(tm, sm.getActiveSubscriptionInfoForSimSlotIndex(slot).getSubscriptionId());
                    Log.i(TAG, "Imsi fro slot " + slot + " is " + imsi);
                    return imsi;
                }

            }
            return imsi;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return imsi;
    }


    public static String getSingleImsi(Context context) {


        String imsi = " ";
//
        //      boolean isSIM1Ready = getSIMStateBySlot(context, "getSimState", 0);

        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            Method getSubId = TelephonyManager.class.getMethod("getSubscriberId", int.class);
            SubscriptionManager sm = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            if (ActivityCompat.checkSelfPermission(Mview.fapps_ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "NA";
            }
            imsi = (String) getSubId.invoke(tm, sm.getActiveSubscriptionInfoForSimSlotIndex(0).getSubscriptionId()); // Sim slot 1 IMSI


            return imsi;
        } catch (IllegalAccessException e) {


            e.printStackTrace();
        } catch (InvocationTargetException e) {


            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            

            e.printStackTrace();
        }
        catch (Exception e)
        {


            e.printStackTrace();
        }
        return imsi;



    }

}
