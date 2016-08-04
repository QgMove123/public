package com.example.ricco.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by chenyi on 2016/8/3.
 */
public class Download {
    private String strUrl;
    private Handler handler;

    public Download(String url, Handler handler) {
        this.strUrl = url;
        this.handler = handler;
    }

    /**
     * 通过url取得HttpURLConnection连接，返回输入流
     * @return
     * @throws Exception
     */
    public InputStream getStreamInput() throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        /**
         * 设置输入流为字节流，连接读取超时，GET方法
         */
        conn.setDoInput(true);
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            return conn.getInputStream();
        }
        return null;
    }

    /**
     * 将InStream写入ByteArrayOutputStream中，再一次性输出
     * @param inStream
     * @return byte[]
     * @throws Exception
     */
    public byte[] readStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        /**
         * 将文件读取到buffer数组
         */
        while( (len = inStream.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 保存文件
     * @param fileName
     * @param type
     * @throws IOException
     */
    public Bitmap saveFile(String fileName, String type) throws IOException {
        Bitmap bm = null;
        String sdcard;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdcard = Environment.getExternalStorageDirectory() + "/QGYun/";//获取跟目录
        } else {

            return null;
        }
        /**
         * 查找sd卡的目录，并创建新文件
         */
        File dirFile = new File(sdcard);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myFile = new File(sdcard, fileName);
        /**
         * 取得下载文件输入流和保存文件的输出流
         * 判断是否为图片，如果是图片先转换为图片再保存
         * 连接两个流进行保存文件
         */
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(myFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] data = readStream(getStreamInput());
        if (type.equals("jpg")) {
            bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        } else {
            bos.write(data);
        }
        bos.flush();
        bos.close();
        Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);
        return bm;
    }
}
