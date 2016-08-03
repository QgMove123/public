package com.example.ricco.qgyun;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ricco.entity.UserModel;
import com.example.ricco.util.CallbackListener;
import com.example.ricco.util.HttpUtil;
import com.example.ricco.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zydx on 2016/8/1.
 */
public class PasswordActivity extends Activity {

    private EditText et_oldPassword = null;
    private EditText et_newPassword = null;
    private EditText getEt_againPassword = null;
    private Button btn_modefy = null;
    private String account = null;
    private String password = null;
    private final int SUCCESS = 1;
    private final int ERROR = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(PasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 2:
                    Toast.makeText(PasswordActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    public void onCreate(Bundle savedInstancestates) {
        super.onCreate(savedInstancestates);
        setContentView(R.layout.password_layout);
        init();

        btn_modefy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toast = null;
                if (!et_oldPassword.getText().toString().equals(password)) {
                    toast = "旧密码错误！";
                } else {
                    String nstr = et_newPassword.getText().toString();
                    final String astr = getEt_againPassword.getText().toString();
                    if (nstr.equals("") || astr.equals("")) {
                        toast = "新密码不能为空！";
                    } else if (!nstr.equals(astr)) {
                        toast = "输入的密码不相同！";
                    } else {
                        UserModel userModel = new UserModel(account, astr, "");
                        HttpUtil.getJson("http://192.168.1.112:8080/QGYun/UserAlterInformation?orderJson=" +
                                JsonUtil.toJson(userModel), new CallbackListener() {
                            @Override
                            public void onFinish(Object result) {
                                Message msg = new Message();
                                String json = (String) result;
                                Gson gson = new Gson();
                                Map<String, Boolean> map = gson
                                        .fromJson(json, new TypeToken<HashMap<String, Boolean>>(){}.getType());
                                if (map.get("login")) {
                                    msg.what = SUCCESS;
                                    //保存数据到手机
                                    SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                                    editor.putString("userPassword", astr);
                                    editor.apply();
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
                Toast.makeText(PasswordActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        et_oldPassword = (EditText) findViewById(R.id.edit_account);
        et_newPassword = (EditText) findViewById(R.id.edit_password);
        getEt_againPassword = (EditText) findViewById(R.id.edit_password_again);
        btn_modefy = (Button) findViewById(R.id.button_modify);
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        account = pref.getString("userName", "");
        password = pref.getString("userPassword", "");
    }
}
