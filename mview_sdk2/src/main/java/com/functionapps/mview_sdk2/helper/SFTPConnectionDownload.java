package com.functionapps.mview_sdk2.helper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.functionapps.mview_sdk2.service.ListenService;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

import org.apache.commons.net.io.CopyStreamAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;

public class SFTPConnectionDownload {
    private CopyStreamAdapter streamListener;
    private long startTime=0;
    private String startTimeN;
    private String uploadResponse;
    int status = 0;
    private Context context;
    ProgressBar pDialog;
    String host = "198.12.250.223", username = "mview_ftp",
            password = "92zbVZ";
    String type = null;
    private Dialog dialog;
    private RelativeLayout gauge_layout;
    private LinearLayout connecting_layout;
    private ProgressBar connectingProg;
    private int index;
    private String finalPath;
    private ChannelSftp sftpChannel;
    private long prevTime;

    //private CallbackContext callbacks = null;


    public int downloadTask(Context mContext, FileOutputStream f,  String finalPath) {
        type = "download";
        this.context = mContext;
        String src = "/home/mview_ftp/download/test10Mb.db";
        this.finalPath=finalPath;
        startTime = System.currentTimeMillis();
        startTimeN = Utils.getDateTime();
        status = 1;




        JSch jsch = new JSch();
        Session session = null;
        try {

            session = jsch.getSession(username, host, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();
            session.setTimeout(Constants.CONNECTION_TIMEOUT);

            Channel channel = session.openChannel("sftp");
            channel.connect();
            System.out.println("download file stream "+f);

            sftpChannel = (ChannelSftp) channel;
            sftpChannel.get(src, f, new SfProgressMonitor());
            sftpChannel.exit();
            session.disconnect();




        }


        catch (JSchException e) {
            e.printStackTrace();
            status=-1;//refused connection
        } catch (SftpException e) {
            e.printStackTrace();//file download failed

        }
        catch(ArithmeticException ae)
        {
            ae.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(session!=null && session.isConnected())
            {
                session.disconnect();
            }
            if(sftpChannel!=null && sftpChannel.isConnected())
            {
                sftpChannel.disconnect();
            }
            if(this.finalPath!=null) {
              //  Utils.deletefileFromFileManager(this.finalPath);
            }
        }
        return status;


    }


    private class SfProgressMonitor implements SftpProgressMonitor {

        private long max = 0;
        private long count = 0;
        private long percent = 0;
        private long tt = 0;
        float bandInbps =0;

        long endtime = 0;


        public void SftpProgressMonitor() {

        }


        @Override
        public void init(int op, String src, String dest, long max) {
            this.max = max;
            index=0;
            System.out.println("starting");

            startTime= System.currentTimeMillis();
            System.out.println("Download source"+src); // Origin destination
            System.out.println("Download Destination "+dest); // Destination path
            System.out.println("Download file size "+max); // Total filesize
            prevTime=startTime;

        }

        @Override
        public boolean count(long bytes) {

            this.count += bytes;
            long percentNow = this.count * 100 / max;


            //  System.out.println("type is "+type);
  if (type.equalsIgnoreCase("download")) {

                if (percentNow > this.percent) {
                    this.percent = percentNow;


                    System.out.println("download progress percent" + this.percent); // Progress 0,0


                    int val1 = (int) ((count * 100) / max);
                    long currentTime= System.currentTimeMillis();

                    long tt = System.currentTimeMillis() - startTime;
                    long newtt=currentTime-prevTime;

                    System.out.println("" +
                            "download progress count of bytes transferred till now "+this.count  +" time  "+tt +" current "+bytes); // Progress in bytes from the total

                    double bandvalue=0;
                    try {


                       bandInbps = ((this.count * 8) / ((tt) / 1000));//bits per se
                       // bandInbps = ((bytes* 8) / ((tt) / 1000));//bits per se
                        System.out.println("download progress band in bps " + bandInbps);

                       bandvalue = bandInbps / (1000 * 1000);//Mbps
                        long countInBits=count*8;


                      /*  double bytesInMb=(double)countInBits/1000000;
                        long timeInSec=tt/1000;*/
                    //    bandvalue=bytesInMb/timeInSec;
                     /*   System.out.println("download progress band in mbps " + bandvalue +" bytes in mb "+bytesInMb+" " +
                                "time "+timeInSec+" bytes "+bytes);*/
 prevTime=currentTime;
 bandvalue=4.8*bandvalue;
                    }
                    catch (ArithmeticException ae)
                    {
                        ae.printStackTrace();
                    }
                    float time = tt / 1000;
                    index++;
                    Intent sendmsg = new Intent("speed_result");
                    sendmsg.putExtra("msg", "1");
                    sendmsg.putExtra("msgshow", String.format("%.2f", bandvalue));
                    sendmsg.putExtra("index", String.valueOf(index));
                    System.out.println("index is "+index);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(sendmsg);
                }

                if (this.count == max) {
                    endtime = System.currentTimeMillis();
                    long tt = endtime - startTime;

                    long sz = max;//localFile.length();
                    bandInbps = ((sz) / ((endtime - startTime) / 1000));
                    float band = (bandInbps*8) / (1000 * 1000); //Mbps
                    float szInMB = sz / (1000 * 1000);
                    float ttInSecs = tt / 1000;
                    String downloadResponse = "/" + String.format("%.2f", ttInSecs) + "/" +
                            String.format("%.2f", szInMB) + "/" + String.format("%.2f", band) + "Mbps";


                    float time = ttInSecs;


                    mView_HealthStatus.mySpeedTest.downloadtest = mView_HealthStatus.mySpeedTest.new UploadDownload();
                    mView_HealthStatus.mySpeedTest.downloadtest.isRoaming = mView_HealthStatus.roaming;
                    mView_HealthStatus.mySpeedTest.downloadtest.lat = ListenService.gps.getLatitude();
                    mView_HealthStatus.mySpeedTest.downloadtest.lon = ListenService.gps.getLongitude();
                    mView_HealthStatus.mySpeedTest.downloadtest.networkType = mView_HealthStatus.iCurrentNetworkState;
                    mView_HealthStatus.mySpeedTest.downloadtest.sizeInBytes = max;
                    mView_HealthStatus.mySpeedTest.downloadtest.startTime = startTimeN;
                    mView_HealthStatus.mySpeedTest.downloadtest.timeTakenInMS = tt;
                    mView_HealthStatus.mySpeedTest.downloadtest.type = 2;
                    mView_HealthStatus.mySpeedTest.downloadtest.protocol = mView_HealthStatus.connectionType;
                    Intent sendmsg1 = new Intent("speed_result");
                    sendmsg1.putExtra("msg", "2");
                    sendmsg1.putExtra("msgshow", downloadResponse);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(sendmsg1);
                    status = 1;




                  /*  new WebService.Async_SendUporDownloadtestResults().execute(2);
                    new WebService.Async_SendNeighboringCellsInfo().execute();
*/

                }
            }


            return (true);

        }

        @Override
        public void end() {

            System.out.println("finished");// The process is over
            System.out.println(this.percent); // Progress
            System.out.println(max); // Total filesize
            System.out.println(this.count); // Process in bytes from the total



            try {
                JSONObject downloadJsonObj=new JSONObject();


                //download
                downloadJsonObj.put("startdatetime", mView_HealthStatus.mySpeedTest.downloadtest.startTime);
                downloadJsonObj.put("sizeInBytes", mView_HealthStatus.mySpeedTest.downloadtest.sizeInBytes);
                downloadJsonObj.put("durationTakenInMS", mView_HealthStatus.mySpeedTest.downloadtest.timeTakenInMS);


                JSONArray downloadArray=new JSONArray();
                downloadArray.put(downloadJsonObj);

                Log.i("LogFapps","Download json is "+downloadArray);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }
}
