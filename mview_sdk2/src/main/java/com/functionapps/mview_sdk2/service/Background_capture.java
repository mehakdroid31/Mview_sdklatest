package com.functionapps.mview_sdk2.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.functionapps.mview_sdk2.main.Mview;

import java.util.Timer;
import java.util.TimerTask;

public class Background_capture extends Service {


    private Timer timer;
    private String msisdn;
    private String latitude;
    private String longitude;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {


        return null;
    }
    @Override
    public void onCreate() {

        timer = new Timer();
        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                // your code here...
                try {
                    new Mview.BackgroundTasktgetvalues(Background_capture.this,msisdn,latitude,longitude).execute();

                } catch (Exception exp) {
                    exp.printStackTrace();


                }
            }
        };

        timer.schedule(hourlyTask, 0l, 1000*5*60);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        if (intent!=null)
        {
            msisdn=intent.getStringExtra("msisdn");
            latitude=intent.getStringExtra("latitude");
            longitude=intent.getStringExtra("longitude");

        }
        return  START_STICKY;
    }
}

