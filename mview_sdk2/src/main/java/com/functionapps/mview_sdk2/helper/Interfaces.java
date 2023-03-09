package com.functionapps.mview_sdk2.helper;

public class Interfaces {
    public interface PingResult
    {
        public void onPingResultObtained(final String time);
        public void parsePingResult(Pinger response);

    }
}
