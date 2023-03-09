package com.functionapps.mview_sdk2.helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Pinger implements Serializable {
    String latency;
    String packetLoss;
    String host;
    String rrtMax;

    public String getRttDev() {
        return rttDev;
    }

    public void setRttDev(String rttDev) {
        this.rttDev = rttDev;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    String rttMin;
    String rttDev;
    String timeUnit;

    public String getLatency() {
        return latency;
    }

    public void setLatency(String latency) {
        this.latency = latency;
    }

    public String getPacketLoss() {
        return packetLoss;
    }

    public void setPacketLoss(String packetLoss) {
        this.packetLoss = packetLoss;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRrtMax() {
        return rrtMax;
    }

    public void setRrtMax(String rrtMax) {
        this.rrtMax = rrtMax;
    }

    public String getRttMin() {
        return rttMin;
    }

    public void setRttMin(String rttMin) {
        this.rttMin = rttMin;
    }

    public String getRttAvg() {
        return rttAvg;
    }

    public void setRttAvg(String rttAvg) {
        this.rttAvg = rttAvg;
    }

    String rttAvg;

    public JSONArray videoKpiList() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bufferCount"," ");
            jsonObject.put("bufferTime1080"," ");
            jsonObject.put("bufferTime144"," ");
            jsonObject.put("bufferTime240"," ");
            jsonObject.put("bufferTime2K"," ");
            jsonObject.put("bufferTime360"," ");
            jsonObject.put("bufferTime480"," ");
            jsonObject.put("bufferTime4K"," ");
            jsonObject.put("bufferTime4KPlus"," ");
            jsonObject.put("bufferTime720"," ");
            jsonObject.put("dataUsed"," ");
            jsonObject.put("deviceHeight"," ");
            jsonObject.put("deviceWidth"," ");
            jsonObject.put("errorMessage"," ");
            jsonObject.put("latency"," ");
            jsonObject.put("packetLoss"," ");
            jsonObject.put("playTime1080"," ");
            jsonObject.put("playTime144"," ");
            jsonObject.put("playTime240"," ");
            jsonObject.put("playTime2K"," ");
            jsonObject.put("playTime480"," ");
            jsonObject.put("playTime360"," ");
            jsonObject.put("playTime4K"," ");
            jsonObject.put("playTime4KPlus"," ");
            jsonObject.put("playTime720"," ");
            jsonObject.put("requiredPlayTime"," ");
            jsonObject.put("timeout"," ");
            jsonObject.put("timestamp"," ");
            jsonObject.put("timeToFirstFrame"," ");
                                               jsonObject.put("totalBufferTime"," ");
            jsonObject.put("totalBytes"," ");
            jsonObject.put("totalPlaybackTime"," ");
            jsonObject.put("videoLength"," ");
            jsonObject.put("videoResEnum"," ");
            jsonObject.put("videoUrl"," ");




        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);
        return jsonArray;
    }
}
