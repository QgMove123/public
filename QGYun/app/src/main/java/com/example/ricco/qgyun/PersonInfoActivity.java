package com.example.ricco.qgyun;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ricco.entity.UserModel;
import com.example.ricco.util.CallbackListener;
import com.example.ricco.util.HttpPost;
import com.example.ricco.util.HttpUtil;
import com.example.ricco.util.JsonUtil;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by zydx on 2016/8/1.
 */
public class PersonInfoActivity extends Activity {

    private ImageView btn_back = null;
    private Button btn_exit = null;
    private Button btn_modefy = null;
    private TextView tv_info = null;
    private ImageView iv_head = null;
    private final int PICTURE_DEFAULT = 1;
    private final int EXIT_AAL = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
               switch (msg.what) {
                   case 1:
                       iv_head.setImageBitmap((Bitmap) msg.obj);
                       break;
                   case 2:
                       startActivity(new Intent(PersonInfoActivity.this, StartActivity.class));
                       break;
                   default:
                       break;
               }
        }
    };

    public void onCreate(Bundle savedInstancestates) {
        super.onCreate(savedInstancestates);
        setContentView(R.layout.person_info_layout);
        btn_exit = (Button) findViewById(R.id.button_exit);
        btn_back = (ImageView) findViewById(R.id.back_button);
        btn_modefy = (Button) findViewById(R.id.button_modefy);
        tv_info = (TextView) findViewById(R.id.info_account);
        iv_head = (ImageView) findViewById(R.id.info_head);
        ini();

        //选择头像
        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,0);
            }
        });

        //修改密码

        btn_modefy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonInfoActivity.this, PasswordActivity.class));
            }
        });

        //返回上一页面
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //注销
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel userModel = new UserModel(tv_info.getText().toString(), "", "");
                HttpUtil.getJson("http://192.168.1.113:8080/QGYun/UserExit?orderJson=" +
                        JsonUtil.toJson(userModel), new CallbackListener() {
                    @Override
                    public void onFinish(Object result) {
                        Message msg = new Message();
                        msg.what = EXIT_AAL;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    @Override
    //获取返回的图片进行处理
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //1.获取图片path
        Uri uri = data.getData();
        //获得路径并上传图片
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = this.getContentResolver().query(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String path = actualimagecursor.getString(actual_image_column_index);
        new HttpPost("http://192.168.1.113:8080/QGYun/UploadPicture",
                tv_info.getText().toString(), path.substring(path.lastIndexOf("/") + 1), path).start();
        //设置图片
        FileDescriptor fileDescriptor;
        try {
            ParcelFileDescriptor r = getContentResolver().openFileDescriptor(uri, "r");
            fileDescriptor = r.getFileDescriptor();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fileDescriptor = null;
        }
//        new FileInputStream(fileDescriptor);
        //2.压缩图片后解码获得bitmap
        //2.1 获取原始图片长宽
        BitmapFactory.Options ops = new BitmapFactory.Options();
        ops.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, ops);
        int height = ops.outHeight;
        int width = ops.outWidth;
        //2.2 设置目标图片长宽并压缩图片
        int reHeight = iv_head.getHeight();
        int reWidth = iv_head.getWidth();
        ops.inSampleSize = 1;
        if(height>reHeight||width>reWidth){
            int hRatio = (int)height/reHeight;
            int wRatio = (int)width/reWidth;
            ops.inSampleSize = hRatio>wRatio?hRatio:wRatio;
        }
        //2.3解码取bitmap
        ops.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor,null, ops);
        //3.给imgView设置图片
        iv_head.setImageBitmap(bitmap);
    }

    //初始化信息
    private void ini() {
        //初始化账号信息
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        tv_info.setText(pref.getString("userName", ""));
        //初始化头像
        HttpUtil.getPic("http://192.168.1.113:8080/QGYun/DownloadPicture?user_name="
                + tv_info.getText().toString(), iv_head.getWidth(),
                iv_head.getHeight(), new CallbackListener() {
            @Override
            public void onFinish(Object result) {
                Bitmap bitmap = (Bitmap) result;
                if (bitmap != null) {
                    Message msg = new Message();
                    msg.what = PICTURE_DEFAULT;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
