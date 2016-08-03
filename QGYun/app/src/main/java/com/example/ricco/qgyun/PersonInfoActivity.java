package com.example.ricco.qgyun;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ricco.util.CallbackListener;
import com.example.ricco.util.HttpUtil;

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
    private final int PICTURE_HEAD = 2;
    private final int EXIT = 3;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
               switch (msg.what) {
                   case 1:
                       iv_head.setImageBitmap((Bitmap) msg.obj);
                       break;
                   case 2:
                       break;
                   case 3:
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
        ini();

        //选择头像
        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                HttpUtil.getJson("url", new CallbackListener() {
                    @Override
                    public void onFinish(Object result) {
                        Message msg = new Message();
                        msg.what = EXIT;
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

    //初始化信息
    private void ini() {
        btn_back = (ImageView) findViewById(R.id.back_button);
        btn_exit = (Button) findViewById(R.id.button_exit);
        btn_modefy = (Button) findViewById(R.id.button_modify);
        tv_info = (TextView) findViewById(R.id.info_account);
        iv_head = (ImageView) findViewById(R.id.info_head);
        //初始化账号信息
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        tv_info.setText(pref.getString("userName", ""));
        //初始化头像
        HttpUtil.getPic("http://192.168.1.125:8080/QGYun/com/qg/servlet/DownloadPictureServlet?user_name="
                + tv_info.getText().toString(),iv_head.getWidth(), iv_head.getHeight(), new CallbackListener() {
            @Override
            public void onFinish(Object result) {
                Bitmap bitmap = (Bitmap) result;
                if (bitmap == null) {
                    return;
                } else {
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
