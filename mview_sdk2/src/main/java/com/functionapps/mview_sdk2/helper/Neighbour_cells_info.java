package com.functionapps.mview_sdk2.helper;

import org.json.JSONException;
import org.json.JSONObject;

import static com.functionapps.mview_sdk2.helper.NetworkUtil.getNeighboringCellsInfo;
import static com.functionapps.mview_sdk2.helper.NetworkUtil.getNeighboringCellsInfoForGSM;
import static com.functionapps.mview_sdk2.helper.NetworkUtil.getNeighboringCellsInfoForLte;
import static com.functionapps.mview_sdk2.helper.NetworkUtil.getNeighboringCellsInfoForNR;
import static com.functionapps.mview_sdk2.helper.NetworkUtil.getNeighboringCellsInfoForWcdma;

public class Neighbour_cells_info {

    public static  JSONObject sendRequest() {
        JSONObject neighboringJsonObj = new JSONObject();

        try {
            if (MyPhoneStateListener.getNetworkType() == 4) {
                neighboringJsonObj.put("type", "LTE");
                neighboringJsonObj.put("neighbourCellInformation", getNeighboringCellsInfoForLte());

            }
            else if (MyPhoneStateListener.getNetworkType() == 3)
            {
                neighboringJsonObj.put("type", "Wcdma");
                neighboringJsonObj.put("neighbourCellInformation", getNeighboringCellsInfoForWcdma());


            } else if (MyPhoneStateListener.getNetworkType() == 2)
            {
                neighboringJsonObj.put("type", "Gsm");

                neighboringJsonObj.put("neighbourCellInformation", getNeighboringCellsInfoForGSM());

            }
            else if (MyPhoneStateListener.getNetworkType() == 5)
            {
                neighboringJsonObj.put("type", "NR");

                neighboringJsonObj.put("neighbourCellInformation", getNeighboringCellsInfoForNR());
            }
            else
                {
                neighboringJsonObj.put("type", "none");

                neighboringJsonObj.put("neighbourCellInformation", getNeighboringCellsInfo());//27

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return neighboringJsonObj;
    }

}
