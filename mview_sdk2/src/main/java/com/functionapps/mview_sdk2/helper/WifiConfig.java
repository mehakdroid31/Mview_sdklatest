package com.functionapps.mview_sdk2.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;


import com.functionapps.mview_sdk2.R;
import com.functionapps.mview_sdk2.main.Mview;

import java.util.Objects;
import java.util.Random;

public class WifiConfig {
    private static final String TAG ="WifiConfig" ;

    /**
     * 2.4 GHz band first channel number
     * @hide
     */
    public static final int BAND_24_GHZ_FIRST_CH_NUM = 1;
    /**
     * 2.4 GHz band last channel number
     * @hide
     */
    public static final int BAND_24_GHZ_LAST_CH_NUM = 14;
    /**
     * 2.4 GHz band frequency of first channel in MHz
     * @hide
     */
    public static final int BAND_24_GHZ_START_FREQ_MHZ = 2412;
    /**
     * 2.4 GHz band frequency of last channel in MHz
     * @hide
     */
    public static final int BAND_24_GHZ_END_FREQ_MHZ = 2484;

    /**
     * 5 GHz band first channel number
     * @hide
     */
    public static final int BAND_5_GHZ_FIRST_CH_NUM = 32;
    /**
     * 5 GHz band last channel number
     * @hide
     */
    public static final int BAND_5_GHZ_LAST_CH_NUM = 173;
    /**
     * 5 GHz band frequency of first channel in MHz
     * @hide
     */
    public static final int BAND_5_GHZ_START_FREQ_MHZ = 5160;
    /**
     * 5 GHz band frequency of last channel in MHz
     * @hide
     */
    public static final int BAND_5_GHZ_END_FREQ_MHZ = 5865;

    /**
     * 6 GHz band first channel number
     *
     */
    public static final int BAND_6_GHZ_FIRST_CH_NUM = 1;
    /**
     * 6 GHz band last channel number
     *
     */
    public static final int BAND_6_GHZ_LAST_CH_NUM = 233;
    /**
     * 6 GHz band frequency of first channel in MHz
     *
     */
    public static final int BAND_6_GHZ_START_FREQ_MHZ = 5945;
    /**
     * 6 GHz band frequency of last channel in MHz
     *
     */
    public static final int BAND_6_GHZ_END_FREQ_MHZ = 7105;

    public static double getFreqBw(int freq)
    {
      if(is5GHz(freq))
          return 5;
      else if(is6GHz(freq))
          return 6;
      else if (is24GHz(freq))
          return 2.4;
        return 0;
    }



    /**
     * Utility function to check if a frequency within 2.4 GHz band
     * @param freqMhz frequency in MHz
     * @return true if within 2.4GHz, false otherwise
     *
     * @hide
     */
    public static boolean is24GHz(int freqMhz) {
        return freqMhz >= BAND_24_GHZ_START_FREQ_MHZ && freqMhz <= BAND_24_GHZ_END_FREQ_MHZ;
    }

    /**
     * Utility function to check if a frequency within 5 GHz band
     * @param freqMhz frequency in MHz
     * @return true if within 5GHz, false otherwise
     *
     * @hide
     */
    public static boolean is5GHz(int freqMhz) {
        return freqMhz >=  BAND_5_GHZ_START_FREQ_MHZ && freqMhz <= BAND_5_GHZ_END_FREQ_MHZ;
    }

    /**
     * Utility function to check if a frequency within 6 GHz band
     * @param freqMhz
     * @return true if within 6GHz, false otherwise
     *
     * @hide
     */
    public static boolean is6GHz(int freqMhz) {
        return freqMhz >= BAND_6_GHZ_START_FREQ_MHZ && freqMhz <= BAND_6_GHZ_END_FREQ_MHZ;
    }
  /*  public static int convertFrequencyToChannel(int freq) {
        if (freq >= 2412 && freq <= 2484) {
            return (freq - 2412) / 5 + 1;
        } else if (freq >= 5170 && freq <= 5825) {
            return (freq - 5170) / 5 + 34;
        } else {
            return -1;
        }
    }*/
    /* Random number generator used for AP channel selection. */
    private static final Random sRandom = new Random();
    /**
     * Convert frequency to channel.
     * @param frequency frequency to convert
     * @return channel number associated with given frequency, -1 if no match
     */
    public static int convertFrequencyToChannel(int frequency) {
        if (frequency >= 2412 && frequency <= 2472) {
            return (frequency - 2412) / 5 + 1;
        } else if (frequency == 2484) {
            return 14;
        } else if (frequency >= 5170  &&  frequency <= 5825) {
            /* DFS is included. */
            return (frequency - 5170) / 5 + 34;
        }
        return -1;
    }
    /**
     * Return a channel number for AP setup based on the frequency band.
     * @param apBand 0 for 2GHz, 1 for 5GHz
     * @param allowed2GChannels list of allowed 2GHz channels
     * @param allowed5GFreqList list of allowed 5GHz frequencies
     * @return a valid channel number on success, -1 on failure.
     */
/*
    public static int chooseApChannel(int apBand,
                                      ArrayList<Integer> allowed2GChannels,
                                      int[] allowed5GFreqList) {
        if (apBand != WifiConfiguration.AP_BAND_2GHZ
                && apBand != WifiConfiguration.AP_BAND_5GHZ) {
            Log.e(TAG, "Invalid band: " + apBand);
            return -1;
        }
        if (apBand == WifiConfiguration.AP_BAND_2GHZ)  {
            */
/* Select a channel from 2GHz band. *//*

            if (allowed2GChannels == null || allowed2GChannels.size() == 0) {
                Log.d(TAG, "2GHz allowed channel list not specified");
                */
/* Use default channel. *//*

                return DEFAULT_AP_CHANNEL;
            }
            */
/* Pick a random channel. *//*

            int index = sRandom.nextInt(allowed2GChannels.size());
            return allowed2GChannels.get(index).intValue();
        }
        */
/* 5G without DFS. *//*

        if (allowed5GFreqList != null && allowed5GFreqList.length > 0) {
            */
/* Pick a random channel from the list of supported channels. *//*

            return convertFrequencyToChannel(
                    allowed5GFreqList[sRandom.nextInt(allowed5GFreqList.length)]);
        }
        Log.e(TAG, "No available channels on 5GHz band");
        return -1;
    }
*/

   /* public static boolean ifDualBandIsSupported(WifiManager wifiManager)
    {
        if(wifiManager.is5GHzBandSupported())
    }*/

}
