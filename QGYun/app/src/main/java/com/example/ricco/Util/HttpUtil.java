package com.example.ricco.util;


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
 * @author yason
 * Get、Post工具类
 */
public class HttpUtil {

    public static void getJson(final String url,final CallbackListener listener){
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
                    Log.e("tag",conn.getResponseCode()+"");
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    Log.e("tag","ok2");
                    StringBuilder result = new StringBuilder();
                    String line;
                    while((line = br.readLine())!=null) {
                        result.append(line);
                    }
                   if(listener != null){
                       //回调onFinish()方法
                       listener.onFinish(result.toString());
                       Log.e("tag","Finish");
                   }
                } catch (IOException e) {
                    //回调onError()方法
                    listener.onError(e);
                    Log.e("tag","Error");
                } finally {
                    try {
                        br.close();
                        conn.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    public static void getPic(final String url,final int requestWidth ,final int requestHeight ,final CallbackListener listener){

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
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(bis, null, opt);
                    int width = opt.outWidth;
                    int height = opt.outHeight;
                    opt.inSampleSize = 1;
                    if(width>requestWidth||height>requestHeight){
                        int wRatio = (int) width / requestWidth;
                        int hRatio = (int) height / requestHeight;
                        opt.inSampleSize = wRatio>hRatio?wRatio:hRatio;
                    }
                    opt.inJustDecodeBounds = false;
                    Bitmap result = BitmapFactory.decodeStream(bis,null,opt);
                    
                    if(listener != null){
                        //回调onFinish()方法
                        listener.onFinish(result);
                    }
                } catch (IOException e) {
                    //回调onError()方法
                    listener.onError(e);
                } finally {
                    try {
                        bis.close();
                        conn.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }
}
