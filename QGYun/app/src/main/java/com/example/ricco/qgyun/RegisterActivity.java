package com.example.ricco.qgyun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ricco.entity.UserModel;
import com.example.ricco.util.CallbackListener;
import com.example.ricco.util.HttpUtil;
import com.example.ricco.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

/**
 * Created by zydx on 2016/8/1.
 * @author wenzhikang
 * 用以注册
 */
public class RegisterActivity extends Activity {
    private EditText phone = null;
    private EditText password = null;
    private Button next_step = null;
    private ImageView back_button = null;
    private final int SUCCESS = 1;
    private final int ERROR = 2;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                    break;
                case 2:
                    Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
                    password.setText("");
                    break;
                default:
                    break;
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qgyun_register_layout);
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        next_step = (Button) findViewById(R.id.button_next);
        back_button = (ImageView) findViewById(R.id.back_button);

        //返回按钮
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //绑定下一步按钮监听
        next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String str_phone = phone.getText().toString();
                final String str_password = password.getText().toString();
                if (str_phone.equals("") || str_password.equals("")) {
                    Toast.makeText(RegisterActivity.this,
                            "密码或账号不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    UserModel userModel = new UserModel(str_phone, str_password, "");
                    HttpUtil.getJson("http://192.168.1.102:8080/QGYun/UserRegister?orderJson=" +
                            JsonUtil.toJson(userModel), new CallbackListener() {
                        @Override
                        public void onFinish(Object result) {
                            Message msg = new Message();
                            String json = (String) result;
                            Gson gson = new Gson();
                            Map<String, Boolean> map = gson
                                    .fromJson(json, new TypeToken<Map<String, Boolean>>(){}.getType());
                            if (map.get("register")) {
                                msg.what = SUCCESS;
                            } else {
                                msg.what = ERROR;
                            }
                            handler.sendMessage(msg);
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }
}