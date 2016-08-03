package com.example.ricco.qgyun;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ricco.util.Download;

import java.io.IOException;

/**
 * Created by zydx on 2016/8/1.
 */
public class ItemInfoActivity extends Activity{
    private final String url = null;
    private ImageView fileImage;
    private Button download;
    private Bitmap bitmap;

    public static void actionStart(Context context, String fileName, String id) {
        Intent intent = new Intent(context, ItemInfoActivity.class);
        intent.putExtra("name", fileName);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (bitmap != null) {
                fileImage.setImageBitmap(bitmap);// display image
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_info);
        Intent intent = getIntent();
        //Map<String, Object> map = intent.getExtras();
        fileImage = (ImageView) findViewById(R.id.file_image);
        //点击下载按钮进入下载
        download = (Button) findViewById(R.id.button_download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //新线程下载
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //处理活动的信息，提供给下载
                        String fileName = null;
                        String type = null;
                        //新建一个下载
                        Download download = new Download(url, handler);
                        try {
                            bitmap = download.saveFile(fileName, type);
                        } catch (IOException e) {
                            Toast.makeText(ItemInfoActivity.this, "无法连接网络！", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

}
