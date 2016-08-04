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

    public void run() {
        try {
            String boundary = "---------------------------";
            String prefix = "--";
            String end = "\r\n";
            URL httpurl = new URL(url+"?userName="+userName+"&fileName="+fileName);
            HttpURLConnection con = (HttpURLConnection) httpurl.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            //con.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            //out.writeBytes(prefix+boundary+end);
            //out.writeBytes("Content-Disposition:form-data;"+
                    //"name=\"orderJson\""+end);
            File file = new File(filePath);
            BufferedInputStream fromFile = new BufferedInputStream(new FileInputStream(file));
            byte[] bytes = new byte[1024];
            int len;
            while((len = fromFile.read(bytes))>0){
                out.write(bytes,0,len);
            }
            //out.writeBytes(end);
            //out.writeBytes(prefix+boundary+prefix+end);

            fromFile.close();
            out.close();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}