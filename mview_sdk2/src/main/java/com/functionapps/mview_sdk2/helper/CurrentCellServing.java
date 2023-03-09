package com.functionapps.mview_sdk2.helper;

/**
 * Created by Sharad Gupta on 10/5/2016.
 */
public class CurrentCellServing {
    public long captureTime;
    public String networkType; //1 = GSm, 2 means CDMA etc
    public String LAC;
    public String node;
    public String cellId;
    public String ci;
    public String arfcn;
    public String level;
    public String qual;
    public String type;
    public String serveTime;
    public String lat;
    public String lon;

    public String ltePCI;
    public String lteTAC;
    public String lteRSRP;
    public String lteRSRQ;
    public String lteCQI;
    public String lteRSSI;
    public String lteSNR;
    public String lteSINR;
    public String lteENB;


    public CurrentCellServing()
    {
        lat="-";
        lon ="-";
        LAC = "-";
        node = "-";
        cellId = "-";
        ci="-";

        arfcn = "-";
        level = "-";
        qual="-";

        type = "-";
        level = "-";
        qual="-";
        networkType = "";

        ltePCI = "";
        lteTAC = "";
        lteRSRP = "";
        lteRSRQ = "";
        lteCQI = "";
        lteRSSI = "";
        lteSNR = "";
        lteSINR="";
        serveTime = "";
        lteENB = "";
    }
}
