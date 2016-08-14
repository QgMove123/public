package com.example.ricco.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Get、Post请求
 */
public class HttpUtil {

    public static String sessionid;

    /**
     * 接口，用于回调
     */
    public interface CallBackListener {
        public void OnFinish(Object result);
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
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    Log.e("run: url",conn+"");
                    if(sessionid != null) {
                        conn.setRequestProperty("cookie", sessionid);
                    } else {
                        String cookieval = conn.getHeaderField("set-cookie");
                        if(cookieval != null) {
                            sessionid = cookieval.substring(0, cookieval.indexOf(";"));
                        }
                    }
                    Log.e("run: session", sessionid+"");
                    conn.connect();
                    LogUtil.e("HttpUtil",conn.getResponseCode()+"");
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
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public static void getPic(final String url, final int requestWidth,
                              final int requestHeight , final CallBackListener listener){

        new Thread() {
            @Override
            public void run() {
                BufferedInputStream bis = null;
                HttpURLConnection conn = null;
                try {
                    URL httpUrl = new URL(url);
                    conn = (HttpURLConnection) httpUrl.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    bis = new BufferedInputStream(conn.getInputStream());
                    //图片压缩
//                    BitmapFactory.Options opt = new BitmapFactory.Options();
//                    opt.inJustDecodeBounds = true;
//                    BitmapFactory.decodeStream(bis, null, opt);
//                    int width = opt.outWidth;
//                    int height = opt.outHeight;
//                    opt.inSampleSize = 1;
//                    if(width>requestWidth||height>requestHeight){
//                        int wRatio = (int) width / requestWidth;
//                        int hRatio = (int) height / requestHeight;
//                        opt.inSampleSize = wRatio>hRatio?wRatio:hRatio;
//                    }
//                    opt.inJustDecodeBounds = false;
//                    Bitmap result = BitmapFactory.decodeStream(bis,null,opt);
                    Bitmap result = BitmapFactory.decodeStream(bis);

                    if(listener != null){
                        //回调onFinish()方法
                        listener.OnFinish(result);
                    }
                } catch (IOException e ) {
                    //回调onError()方法
                    listener.OnError(e);
                } catch (ArithmeticException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        bis.close();
                        conn.disconnect();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public static void Post(final String url, final CallBackListener listener){

    }
}
