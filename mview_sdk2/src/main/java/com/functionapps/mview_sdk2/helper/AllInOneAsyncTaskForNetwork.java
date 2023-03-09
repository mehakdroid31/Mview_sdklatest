package com.functionapps.mview_sdk2.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;


public class AllInOneAsyncTaskForNetwork extends AsyncTask<String, String, String> {
    private final AsyncTaskPurpose asyncTaskPurpose;
    private String map_Id;
    private String response;
    public Context context;

    public AllInOneAsyncTaskForNetwork(Context ctx, AsyncTaskPurpose asyncTaskPurpose) {
        this.asyncTaskPurpose=asyncTaskPurpose;
        this.context = ctx;

    }




    public enum AsyncTaskPurpose {

        SDK,IMUP,UD;


    }


    @Override
    protected String doInBackground(String... data) {
       String response= NetworkClass.sendPostRequest(data[0]);
        return response;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        response = s;
        System.out.println("Result is" + s);
        if (response != null && asyncTaskPurpose == AsyncTaskPurpose.IMUP)

            {
                AsyncParser jsonParser = new AsyncParser(context);
                jsonParser.parseimup(response);
            }


    }
}
