package com.example.ricco.qgzone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 系统进入界面，可进入登录注册
 * Created by chenyi on 2016/8/11.
 */
public class FirstActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);

        Button login = (Button) findViewById(R.id.login);
        Button sign = (Button) findViewById(R.id.sign);

        login.setOnClickListener(this);
        sign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(FirstActivity.this, LoginSignActivity.class);

        //通过按钮进入登录注册界面，并通过intent传递信息显示界面
        switch (v.getId()) {
            case R.id.login :
                intent.putExtra("way", "login");
                break;
            case R.id.sign :
                intent.putExtra("way", "sign");
                break;
        }
        startActivity(intent);
    }
}
