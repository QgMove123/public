package com.example.ricco.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.GpsStatus;
import android.text.Html;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Get、Post请求
 */
public class HttpUtil {

    public static String sessionid = null;

    /**
     * 接口，用于回调
     */
    public interface CallBackListener {
        public void OnFinish(String result);
        public void OnError(Exception e);
    }

    public static void Get(final String url, final CallBackListener listener) {
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                BufferedReader br = null;
                try {
                    URL httpUrl = new URL(url);
                    conn = (HttpURLConnection) httpUrl.openConnection();
                    conn.setRequestMethod("GET");
                    if (sessionid != null) {
                        conn.setRequestProperty("cookie", sessionid);
                    } else {
                        String cookieval = conn.getHeaderField("set-cookie");
                        if (cookieval != null) {
                            sessionid = cookieval.substring(0, cookieval.indexOf(";"));
                        }
                    }
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    ;
                    conn.connect();
                    Log.e("run: connect", conn + "");
                    Log.e("run: session", sessionid + "");
                    StringBuilder str = new StringBuilder("");
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        str.append(line);
                    }
                    if (listener != null) {
                        //回调OnFinish()
                        listener.OnFinish(str.toString());
                        LogUtil.e("HttpUtil", "Finish");
                    }
                } catch (IOException e) {
                    //回调OnError()
                    listener.OnError(e);
                    LogUtil.e("HttpUtil", "Error");
                } finally {
                    try {
                        br.close();
                        conn.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    public static void Post(final String url, final HashMap<String,String> hashMap,
                            final ArrayList<Bitmap> bitmaps, final CallBackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msg = new String();
                String boundary = "---------------------------7de2c25201d48";
                String prefix = "--";
                String end = "\r\n";
                try {
                    URL httpUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
//                    if(sessionid != null) {
//                        conn.setRequestProperty("cookie", sessionid);
//                    }else {
//                        String cookieval = conn.getHeaderField("set-cookie");
//                        if (cookieval != null) {
//                            sessionid = cookieval.substring(0, cookieval.indexOf(";"));
//                            Log.e("Tag",sessionid+"get");
//                        }
//                    }

                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    DataOutputStream dos =new DataOutputStream (conn.getOutputStream());


                    for(Map.Entry<String,String> entry : hashMap.entrySet()){
                        String strkey = entry.getKey();
                        String strval = entry.getValue();
                        dos.writeBytes(prefix+boundary+end);
                        dos.writeBytes("Content-Disposition: form-data; name=\""+strkey+"\"" + end);
                        dos.writeBytes(end);
                        dos.writeUTF(strval);
                        dos.writeBytes(end);
                    }




                    for (int i=0;bitmaps!=null&&i<bitmaps.size()&&bitmaps.get(i)!=null&&bitmaps.size()<=9;i++) {
                        dos.writeBytes(prefix+boundary+end);
                        dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=aaa.jpg" + end);
                        dos.writeBytes(end);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmaps.get(i).compress(Bitmap.CompressFormat.PNG,100,baos);
                        byte[] bytes = baos.toByteArray();
                        dos.write(bytes);
                        if(bytes.length!=0)Log.e("Tag",bytes.length+"");
                        dos.writeBytes(end);
                    }
                    dos.writeBytes(prefix+boundary+prefix+end);


                    listener.OnFinish(conn.getResponseCode()+"");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    listener.OnError(e);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
