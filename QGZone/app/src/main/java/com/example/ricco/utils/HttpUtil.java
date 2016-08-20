package com.example.ricco.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
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
                    if (sessionid != null) {
                        conn.setRequestProperty("cookie", sessionid);
                    } else {
                        String cookieval = conn.getHeaderField("set-cookie");
                        if (cookieval != null) {
                            sessionid = cookieval.substring(0, cookieval.indexOf(";"));
                        }
                    }
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
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
                    if(sessionid != null) {
                        conn.setRequestProperty("cookie", sessionid);
                    } else {
                        String cookieval = conn.getHeaderField("set-cookie");
                        if(cookieval != null) {
                            sessionid = cookieval.substring(0, cookieval.indexOf(";"));
                        }
                    }

                    conn.setConnectTimeout(8000);
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
                    //输出图片
                    int size = 0;
                    if (imgPaths != null && (size = imgPaths.size())!= 0) {
                        for (int i = 0; i < size; i++) {
                            dos.writeBytes(prefix+boundary+end);
                            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=image.jpg" + end);
                            dos.writeBytes(end);
                            FileInputStream is = new FileInputStream(imgPaths.get(i));
                            BufferedInputStream bis = new BufferedInputStream(is);
                            byte[] buffer = new byte[1024 * 8];
                            int len;
                            while ((len = bis.read(buffer)) != -1) {
                                dos.write(buffer, 0, len);
                            }
                            dos.writeBytes(end);
                        }
                    }
                    dos.writeBytes(prefix+boundary+prefix+end);
                    dos.flush();
                    //读入
                    StringBuilder str = new StringBuilder("");
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    String line = null;
                    while((line=br.readLine())!=null){
                        str.append(line);
                    }
                    if(listener!=null){
                        //回调OnFinish()
                        listener.OnFinish(str.toString());
                        LogUtil.e("HttpUtilPost","Finish");
                    }


                    listener.OnFinish(conn.getResponseCode()+"");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    listener.OnError(e);
                } catch (IOException e) {
                    listener.OnError(e);
                    LogUtil.e("HttpUtilGet","Error");
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }
}
