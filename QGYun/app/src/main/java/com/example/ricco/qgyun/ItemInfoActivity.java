package com.example.ricco.qgyun;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ricco.util.CallbackListener;
import com.example.ricco.util.Download;
import com.example.ricco.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Map;

/**
 * Created by zydx on 2016/8/1.
 */
public class ItemInfoActivity extends Activity{
    private final String url = "http://192.168.1.102:8080/QGYun/";
    String fileName = null;
    String type = null;
    Intent intent = null;
    private TextView file_name;
    private ImageView fileImage;
    private Button download;
    private Button editName;
    private Button delete;
    private ImageView back;
    private Bitmap bitmap;
    private String str = null;

    /**
     * 从前一个Activity中获取文件信息
     * @param context
     * @param fileName
     * @param id
     */
    public static void actionStart(Context context, String fileName, int id) {
        Intent intent = new Intent(context, ItemInfoActivity.class);
        intent.putExtra("name", fileName);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    /**
     * 结束其他线程的返回数据，判断都在UI中显示
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if (bitmap != null) {
                    fileImage.setImageBitmap(bitmap);// display image
                    }
                    Toast.makeText(ItemInfoActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(ItemInfoActivity.this, "找不到SD卡，下载失败", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(ItemInfoActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(ItemInfoActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(ItemInfoActivity.this, "重命名成功", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(ItemInfoActivity.this, "重命名失败", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_info);
        back = (ImageView) findViewById(R.id.back_button);

        //返回按钮
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //获取文件名和类型
        intent = getIntent();
        fileName = intent.getStringExtra("name");
        type = fileName.substring(fileName.lastIndexOf(".")+1);
        Log.e("onCreate: type", type+"");
        file_name = (TextView) findViewById(R.id.file_name);
        file_name.setText(fileName);
        //设置文件的图标
        fileImage = (ImageView) findViewById(R.id.file_image);
        if(type.equals("jpg")) {
            fileImage.setImageResource(R.drawable.type_jpg);
        } else if(type.equals("zip")) {
            fileImage.setImageResource(R.drawable.type_zip);
        } else if(type.equals("txt")) {
            fileImage.setImageResource(R.drawable.type_txt);
        }
        //重命名和删除按钮
        editName = (Button) findViewById(R.id.edit_name);
        delete = (Button) findViewById(R.id.button_delete);
        //从AlertDialog的输入中获取新名字，传给服务器
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出输入框进行输入
                final EditText et = new EditText(ItemInfoActivity.this);
                final AlertDialog.Builder show = new AlertDialog.Builder(ItemInfoActivity.this);
                show.setTitle("输入新文件名");
                show.setIcon(android.R.drawable.ic_dialog_info);
                show.setView(et);
                show.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        str = et.getText().toString();
                        //Log.e("onClick:str ", str);
                        if(str == null || str.equals("")) {
                            str = fileName.substring(fileName.lastIndexOf("."));
                        }
                        Log.e("onClick:str ", str);
                        file_name.setText(str+"."+type);
                        //通过URL传递信息给服务器
                        HttpUtil.getJson(url+"ResourceRename?newSourceName="+str+"."+type+
                                "&resourceId="+ 1443, new CallbackListener() {
                            @Override
                            public void onFinish(Object result) {
                                Message msg = new Message();
                                String json = (String) result;
                                Gson gson = new Gson();
                                Map<String, Boolean> map = gson
                                        .fromJson(json, new TypeToken<Map<String, Boolean>>(){}.getType());
                                if (map.get("rename")) {
                                    msg.what = 5;
                                } else {
                                    msg.what = 6;
                                }
                                handler.sendMessage(msg);
                            }

                            @Override
                            public void onError(Exception e) {
                                e.printStackTrace();
                            }
                        });

                    }
                });
                show.setNegativeButton("取消", null);
                show.create().show();
            }
        });

        //通过url传递给服务器并删除文件
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.getJson(url+"ResourceDelete?resourceId="+
                        + intent.getIntExtra("id", 0), new CallbackListener() {
                    @Override
                    public void onFinish(Object result) {
                        Message msg = new Message();
                        String json = (String) result;
                        Gson gson = new Gson();
                        Map<String, Boolean> map = gson
                                .fromJson(json, new TypeToken<Map<String, Boolean>>(){}.getType());
                        if (map.get("delete")) {
                            msg.what = 3;
                        } else {
                            msg.what = 4;
                        }
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
        //点击下载按钮进入下载
        download = (Button) findViewById(R.id.button_download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //新线程下载
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //新建一个下载
                        Download download = new Download(url+"ResourceDownload?resourceId="
                                +intent.getIntExtra("id", 0), handler);
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
