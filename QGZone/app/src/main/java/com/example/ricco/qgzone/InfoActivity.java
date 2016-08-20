package com.example.ricco.qgzone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ricco.constant.Constant;
import com.example.ricco.others.ImageLoader;
import com.example.ricco.others.CircleImageVIew;
import com.example.ricco.others.TopBar;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.InfoItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人资料
 */
public class InfoActivity extends BaseActivity {

    private List<InfoItem> infoArry = new ArrayList<InfoItem>();
    private CircleImageVIew headPic;
    private TextView name;
    private Button exit;
    private TopBar tb;
    private String url;
    private String message;

    public static void actionStart(Context context, String data1, int data2) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra("user", data1);
        intent.putExtra("id", data2);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_layout);

        tb = (TopBar) findViewById(R.id.topBar);
        headPic = (CircleImageVIew) findViewById(R.id.user_pic);
        name = (TextView) findViewById(R.id.user_name);
        exit = (Button) findViewById(R.id.exit);
        initInfo();
        Intent intent = getIntent();
        if(intent != null && intent.getStringExtra("user").equals("friend")) {
            tb.setRightIsVisable(false);
            findViewById(R.id.exit).setVisibility(View.GONE);
            url = Constant.Account.friendInfo + intent.getIntExtra("id", 0);
        } else {

            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpUtil.Get(Constant.Account.userSignOut, null);
                    Intent intent1 = new Intent(InfoActivity.this, FirstActivity.class);
                    startActivity(intent1);
                }
            });

            tb.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
                @Override
                public void LeftClick(View view) {
                    finish();
                }

                @Override
                public void RightClick(View view) {
//                    EditInfoActivity.actionStart(InfoActivity.this, message);
                }
            });
            url = Constant.Account.MessageGet;
        }

        requestInfo(url);
    }

    /**
     * 初始化个人信息的表格
     */
    public void initInfo() {
        InfoItem account = (InfoItem) findViewById(R.id.account);
        account.setTextView("帐号");
        infoArry.add(account);
        InfoItem sex = (InfoItem) findViewById(R.id.sex);
        sex.setTextView("性别");
        infoArry.add(sex);
        InfoItem birthday = (InfoItem) findViewById(R.id.birth);
        birthday.setTextView("生日");
        infoArry.add(birthday);
        InfoItem place = (InfoItem) findViewById(R.id.place);
        place.setTextView("地址");
        infoArry.add(place);
        InfoItem phone = (InfoItem) findViewById(R.id.phone);
        phone.setTextView("电话");
        infoArry.add(phone);
        InfoItem email = (InfoItem) findViewById(R.id.email);
        email.setTextView("邮箱");
        infoArry.add(email);
    }

    // 网络线程请求个人信息
    private void requestInfo(String url) {
        HttpUtil.Get(url, new HttpUtil.CallBackListener() {
            @Override
            public void OnFinish(String result) {

                Message msg = new Message();
                try {
                    //通过JSONObject取出服务器传回的状态和信息
                    JSONObject dataJson = new JSONObject(result);
                    Log.e("OnFinish: result", result);
                    msg.what = Integer.valueOf(dataJson.getString("state"));
                    msg.obj = dataJson.get("message");
                    message = dataJson.getString("message");
                    mHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 获取个人信息
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 161:
                    JSONObject map = (JSONObject) msg.obj;
                    for(InfoItem ii: infoArry) {
                        try {
                            ii.setEditText(map.getString(ii.getTextView()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        ImageLoader.getInstance(1).loadImage(Constant.civUrl+map.getString("userImage"), headPic, false);
                        name.setText(map.getString("userName"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 162:
                    Toast.makeText(InfoActivity.this, "获取个人信息失败，请查看网络连接", Toast.LENGTH_SHORT).show();
                    break;
                default:break;
            }
        }
    };
}
