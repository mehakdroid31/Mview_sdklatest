package com.functionapps.mview_sdk2.main;

import java.util.ArrayList;

public class MySpeedTest {

	public static ArrayList<String> urlArray;
	
	public WebTest webtest;
	public VideoStreaming video;

	public UploadDownload uploadtest;
	public UploadDownload downloadtest;
	
	public MySpeedTest()
	{
		webtest = new WebTest();
		video = new VideoStreaming();
		for( int i=0; i< urlArray.size(); i++)
		{
			website w1 = new website();
			w1.url = urlArray.get(i);
			webtest.websiteArr.add(w1);
		}
	}
	
	public void initWebtest()
	{
		
	}
	
	public class WebTest {
		public ArrayList<website> websiteArr;
		public int protocol; //2g,3g,4g
		public int networkType; //wifi or mobile
		public String lat;
		public String lon;
		public String isRoaming;
		
		public WebTest()
		{
			websiteArr = new ArrayList<website>();
		}
	}
	
	public class website {
		public String url;
		public long timeTakenInMS;
		public long bytes;
	}
	
	public class UploadDownload {
		public int type; //1 means upload, 2 means download
		public long sizeInBytes;
		public String startTime;
		public long startTimeN;
		public long timeTakenInMS;
		public String protocol; //2g,3g,4g
		public int networkType; //wifi or mobile
		public double lat;
		public double lon;
		public boolean isRoaming;
		public String source;
    }
	
//	public class Download {
//		public long sizeInBytes;
//		public long startTime;
//		public long timeTakenInMS;
//		public String protocol; //2g,3g,4g
//		public int networkType; //wifi or mobile
//		public double lat;
//		public double lon;
//		public boolean isRoaming;
//	}

	public class VideoStreaming {
		public long videoDuration;
		public int noOfBuffering;
		public long totalBuferingTime;
		public long totalPlayTime;
	}
	
}//end main class
