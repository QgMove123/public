package com.example.ricco.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

    public static void Get(final String url, final CallBackListener listener){
        new Thread(){
            @Override
            public void run() {
                HttpURLConnection conn = null;
                BufferedReader br = null;
                try {
                    URL httpUrl = new URL(url);
                    conn = (HttpURLConnection)httpUrl.openConnection();
                    conn.setRequestMethod("GET");
                    if(sessionid != null) {
                        conn.setRequestProperty("cookie", sessionid);
                    } else {
                        String cookieval = conn.getHeaderField("set-cookie");
                        if(cookieval != null) {
                            sessionid = cookieval.substring(0, cookieval.indexOf(";"));
                        }
                    }
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    conn.connect();
                    Log.e("run: connect", conn+"");
                    Log.e("run: session", sessionid+"");
                    StringBuilder str = new StringBuilder("");
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    String line = null;
                    while((line=br.readLine())!=null){
                        str.append(line);
                    }
                    if(listener!=null){
                        //回调OnFinish()
                        listener.OnFinish(str.toString());
                        LogUtil.e("HttpUtil","Finish");
                    }
                } catch (IOException e) {
                    //回调OnError()
                    listener.OnError(e);
                    LogUtil.e("HttpUtil","Error");
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

}
