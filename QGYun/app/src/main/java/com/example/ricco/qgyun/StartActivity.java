package com.example.ricco.qgyun;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ricco.entity.UserModel;
import com.example.ricco.util.CallbackListener;
import com.example.ricco.util.HttpUtil;
import com.example.ricco.util.JsonUtil;

public class StartActivity extends Activity{

    private Button login = null;
    private Button register = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);
        login = (Button) findViewById(R.id.button_login);
        register = (Button) findViewById(R.id.button_register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        UserModel userModel = new UserModel(pref.getString("userName", ""), "", "");
        HttpUtil.getJson("http://192.168.1.102:8080/QGYun/UserExit?orderJson=" +
                JsonUtil.toJson(userModel), new CallbackListener() {
            @Override
            public void onFinish(Object result) {

            }
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        UserModel userModel = new UserModel(pref.getString("userName", ""), "", "");
        HttpUtil.getJson("http://192.168.1.102:8080/QGYun/UserExit?orderJson=" +
                JsonUtil.toJson(userModel), new CallbackListener() {
            @Override
            public void onFinish(Object result) {

            }
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
