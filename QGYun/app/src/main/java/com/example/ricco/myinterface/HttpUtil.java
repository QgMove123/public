package com.example.ricco.myinterface;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * @author yason
 * Get、Post工具类
 */
public class HttpUtil {

    public static void get(final String url, final CallbackListener listener){
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
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while((line = br.readLine())!=null) {
                        result.append(line);
                    }
                   if(listener != null){
                       //回调onFinish()方法
                       listener.onFinish(result.toString());
                   }
                } catch (IOException e) {
                    //回调onError()方法
                    listener.onError(e);
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

    public static void post(final String url,final String params, final CallbackListener listener){
        new Thread(){
            @Override
            public void run() {
                HttpURLConnection conn = null;
                BufferedReader br = null;
                PrintWriter pw = null;
                try {
                    URL httpUrl = new URL(url);
                    conn = (HttpURLConnection)httpUrl.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //发送请求参数
                    pw = new PrintWriter(conn.getOutputStream());
                    pw.print(params);
                     //读取URL的响应
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while((line = br.readLine())!=null) {
                        result.append(line);
                    }
                    if(listener != null){
                        //回调onFinish()方法
                        listener.onFinish(result.toString());
                    }
                } catch (IOException e) {
                    //回调onError()方法
                    listener.onError(e);
                } finally {
                    try {
                        pw.close();
                        br.close();
                        conn.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }
}
