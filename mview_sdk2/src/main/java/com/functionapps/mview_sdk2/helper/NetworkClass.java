package com.functionapps.mview_sdk2.helper;

import com.functionapps.mview_sdk2.helper.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class NetworkClass {

    public static String sendPostRequest(String message) {
        int success = 0;
        String response = "";
        String errMsg = "";
        String query = "";
        InputStream in;
        try {

            // instantiate the URL object with the target URL of the resource to
            // request
            URL url = new URL(Constants.URL);

            // instantiate the HttpURLConnection with the URL object - A new
            // connection is opened every time by calling the openConnection
            // method of the protocol handler for this URL.
            // 1. This is the point where the connection is opened.
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // set connection output to true
            connection.setDoOutput(true);
            // instead of a GET, we're going to send using method="POST"
            connection.setRequestMethod("POST");

            //by swapnil 08/23/2022
            connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
             connection.setReadTimeout(Constants.READ_TIMEOUT);


            OutputStream os = connection.getOutputStream();

            // instantiate OutputStreamWriter using the output stream, returned
            // from getOutputStream, that writes to this connection.
            // 2. This is the point where you'll know if the connection was
            // successfully established. If an I/O error occurs while creating
            // the output stream, you'll see an IOException.

            // OutputStream o = connection.getOutputStream();
            if (os != null) {

                OutputStreamWriter writer = new OutputStreamWriter(os, "utf-8");

                /*
                 * BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os,
                 * "UTF-8"));
                 */
                // BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(o,
                // "UTF-8"));
                // write data to the connection. This is data that you are sending
                // to the server
                // 3. No. Sending the data is conducted here. We established the
                // connection with getOutputStream

                writer.write("req=" + message);

                // o.write(postDataBytes);

                // Closes this output stream and releases any system resources
                // associated with this stream. At this point, we've sent all the
                // data. Only the outputStream is closed at this point, not the
                // actual connection

                writer.close();


                System.out.println(" : via sendPostRequest Message going in request is " + message);
                // if there is a response code AND that response code is 200 OK, do
                // stuff in the first if block

                System.out.println(" : via sendPostRequest ** response code**" + connection.getResponseCode());
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // OK
                    System.out.println(": via sendPostRequest response is ok from server");
                    in = connection.getInputStream();
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }

                    System.out.println(" : via sendPostRequest response is success from server and response is "+response);

                    // otherwise, if any other status code is returned, or no status
                    // code is returned, do stuff in the else block
                } else {
                    System.out.println("response is error from server");
                    // response="0";
                    // Server returned HTTP error code.
                }
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
