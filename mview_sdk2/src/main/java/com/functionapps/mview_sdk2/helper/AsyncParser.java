package com.functionapps.mview_sdk2.helper;

import android.content.Context;
import android.util.Log;

import com.functionapps.mview_sdk2.main.Mview;

import org.json.JSONException;
import org.json.JSONObject;

public class AsyncParser {
    private  Context context;

    public AsyncParser(Context context) {
        this.context=context;
        //  db_handler=new DB_handler(this.context);
    }

    /*
    {"msg":"ud","interface":"CLI","status":0,"message":"Check Agent Details.","agent_details":[{"status":"Edit","jar_file_name":"sdk_app","jar_path":"http:\/\/factabout.in\/urlAirtel\/","class_name":null,"method_name":null,"jar_file_size":null,"jar_file_checksum":null,"schedule_start_time":"2023-02-01 19:34:38","schedule_end_time":"2030-02-01 19:34:38","number_of_iteration":"0","device_tcp_ip":null,"device_tcp_port":null,"upload_request_time":"2023-02-02 21:44:20","upload_complete_time":"0000-00-00 00:00:00","sendStatus":"0","userID":null,"agent_version":null,"type":"schedule","frequency":"5000","period":null,"priority":"0","task_type":null,"network":null,"url_count":"0"}]}
     */
    public static void parseimup(String response) {

        if (response!=null) {
            try {
                JSONObject obj = new JSONObject(response);
                String status = obj.optString("status");
                if (status.equalsIgnoreCase("0")) {
                    String updateFlag = obj.optString("update_flag");
                    if (updateFlag.equalsIgnoreCase("1")) {
                        Mview.sendUpdateReq();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Log.i(Mview.TAG,"Mo response of imup");
        }
    }
}
