package com.functionapps.mview_sdk2.main;

import android.app.Application;
import android.content.Context;

public class AirtelFapps extends Application {
    Context ctx;
    public void onCreate() {
        super.onCreate();
        ctx = getApplicationContext();
        Mview.fapps_ctx=ctx;
    }
}
