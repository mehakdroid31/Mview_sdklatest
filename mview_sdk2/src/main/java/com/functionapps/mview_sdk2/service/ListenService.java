package com.functionapps.mview_sdk2.service;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.functionapps.mview_sdk2.helper.MyPhoneStateListener;
import com.functionapps.mview_sdk2.helper.GPSTracker;
import com.functionapps.mview_sdk2.helper.Utils;
import com.functionapps.mview_sdk2.helper.mView_HealthStatus;

import java.util.Calendar;

public class ListenService extends Service {
    public static GPSTracker gps;
    public static TelephonyManager telMgr;
    MyPhoneStateListener myPhoneStateListener = null;
    private TelephonyManager telephonyManager;
    private PendingIntent pendingIntent;
    private AlarmManager alarmMgr;
    private Context context;

    public TelephonyManager getInstance() {
        telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telMgr;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        gps = new GPSTracker((ListenService.this));
        mView_HealthStatus.OperatorName = telMgr.getNetworkOperatorName();
        mView_HealthStatus.simOperatorName = telMgr.getSimOperatorName();
        int cc = telMgr.getPhoneType();
        if (cc == telMgr.PHONE_TYPE_GSM) {
            mView_HealthStatus.phonetype = "GSM";
        } else if (cc == telMgr.PHONE_TYPE_CDMA) {
            mView_HealthStatus.phonetype = "CDMA";
        } else if (cc == telMgr.PHONE_TYPE_NONE) {
        } else {
            mView_HealthStatus.phonetype = "LTE";
        }


        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = tel.getNetworkOperator();


        if (TextUtils.isEmpty(networkOperator) == false) {
            mView_HealthStatus.mcc = Integer.parseInt(networkOperator.substring(0, 3));
            mView_HealthStatus.mnc = Integer.parseInt(networkOperator.substring(3));
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        int networkType = telMgr.getNetworkType();

        mView_HealthStatus.strCurrentNetworkState = MyPhoneStateListener.getNetworkClass(networkType,context);

    }



    boolean runthread = true;
    int periodicRefreshFrequencyInSeconds = 300;

    private void setUpAlarm() {
        //  Utils.showToast(this,"alarm called in on start of lsiten service");
//        alarmMgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
       /* Intent intent = new Intent(getApplicationContext(), AlarmRxr.class);
        intent.putExtra("alarm", 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }
        else
        {
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        }*/
      /*  Calendar updateTime = Calendar.getInstance();
        updateTime.set(Calendar.SECOND, mView_HealthStatus.periodicFrequencyForAllServices);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(), mView_HealthStatus.periodicFrequencyForAllServices * 1000, pendingIntent);*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Toast.makeText(this, "listen service started.. and myphonestate listener object is  "+myPhoneStateListener, Toast.LENGTH_SHORT).show();
        //   runAsForeground();
        setUpAlarm();
        System.out.println("in on start of listen service..");
        if (myPhoneStateListener == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                myPhoneStateListener = new MyPhoneStateListener(ListenService.this, telMgr);
            }
            else
            {
                Utils.onFailure(1,"SDK version below 22");
            }
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                telMgr.listen(myPhoneStateListener,
                        PhoneStateListener.LISTEN_CALL_STATE
                                | PhoneStateListener.LISTEN_CELL_INFO // Requires API 17
                                | PhoneStateListener.LISTEN_CELL_LOCATION
                                | PhoneStateListener.LISTEN_DATA_ACTIVITY
                                | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
                                | PhoneStateListener.LISTEN_SERVICE_STATE
                                | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
                                | PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR
                                | PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR);
            } else {
                telMgr.listen(myPhoneStateListener,
                        PhoneStateListener.LISTEN_CALL_STATE
                                // Requires API 17
                                | PhoneStateListener.LISTEN_DATA_ACTIVITY
                                | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
                                | PhoneStateListener.LISTEN_SERVICE_STATE
                                | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
                                | PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR
                                | PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR);
            }
        }



        return START_STICKY;
    }



    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                //Print Toast or open dialog

            }
            return false;
        }
    });



    @Override
    public IBinder onBind(Intent arg0) {// TODO Auto-generated method stub
        return null;
    }


    private boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service already", "running");
                return true;
            }
        }
        Log.i("Service not", "running");
        return false;
    }

    @Override
    public void onLowMemory() {
        // Toast.makeText(this, "on low memory", Toast.LENGTH_SHORT).show();
        super.onLowMemory();
    }
}
