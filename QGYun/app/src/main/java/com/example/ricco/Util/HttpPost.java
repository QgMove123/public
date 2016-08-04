package com.example.ricco.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Mr_Do on 2016/8/3.
 * 用于文件的上传
 */
public class HttpPost extends Thread {
    private String url;
    private String userName;
    private String fileName;
    private String filePath;
    public HttpPost(String url, String userName, String fileName, String filePath) {
        this.url = url;
        this.userName = userName;
        this.fileName = fileName;
        this.filePath = filePath;
    }
    @Override
    public void run() {
        try {
            String boundary = "---------------------------7de2c25201d48";
            String prefix = "--";
            String end = "\r\n";
            URL httpUrl = new URL(url+"?userName="+userName);
            Log.e("con","go");
            HttpURLConnection con = (HttpURLConnection) httpUrl.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setDoOutput(true);
            con.setDoInput(true);
            Log.e("con3",con+"");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            Log.e("connect","connect-ok");
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(prefix+boundary+end);
            out.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""+fileName+"\"" + end);
            out.writeBytes(end);
            File file = new File(filePath);
            BufferedInputStream fromFile = new BufferedInputStream(new FileInputStream(file));
            byte[] bytes = new byte[1024*8];
            int len;
            while((len = fromFile.read(bytes))>0){
                out.write(bytes,0,len);
            }
            out.writeBytes(end);
            out.writeBytes(prefix+boundary+prefix+end);
            Log.e("code",con.getResponseCode()+"");
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}