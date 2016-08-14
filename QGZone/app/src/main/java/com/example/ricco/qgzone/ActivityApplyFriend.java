package com.example.ricco.qgzone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ricco.adapter.ApplyFriendAdapter;
import com.example.ricco.entity.JsonModel;
import com.example.ricco.entity.MessageModel;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.LogUtil;
import com.example.ricco.utils.ToastUtil;
import com.example.ricco.utils.TopBar;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加好友
 * @author Wzkang
 * Created by Ricco on 2016/8/12.
 */
public class ActivityApplyFriend extends BaseActivity {

    // 通过昵称搜索
    final String firstUrl = "http://192.168.3.33:8080/QGzone/SearchByUserName?searchName=";
    // 通过账号搜索
    final String secondUrl = "http://192.168.3.33:8080/QGzone/SearchByUserId?searchId=";
    private TopBar mTopBar = null;
    private EditText mEtext = null;
    private Button mButton = null;
    private ListView mListv = null;
    private List<MessageModel> mDatas = null;
    private boolean mChoice = true;
    private ApplyFriendAdapter mAdapter = null;
    private String mUrl = null;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtil.showShort(getApplicationContext(), "服务器错误！");
                    break;
                case 301:
                    LogUtil.d("ActivityApplyFriend", "检索到好友！");
                    if (msg.obj != null) {
                        if (mDatas.size() > 0) {
                            mDatas.clear();
                        }
                        for (MessageModel mm : (List<MessageModel>) msg.obj) {
                            mDatas.add(mm);
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                    }
                case 302:
                    ToastUtil.showShort(getApplicationContext(), "检索不到好友！");
                    break;
                case 303:
                    ToastUtil.showShort(getApplicationContext(), "成功发送请求！");
                    break;
                case 304:
                    ToastUtil.showShort(getApplicationContext(), "发送请求失败！");
                    break;
                case 305:
                    ToastUtil.showShort(getApplicationContext(), "该用户不存在！");
                    break;
                case 306:
                    ToastUtil.showShort(getApplicationContext(), "不能给自己发送申请喔");
                    break;
                case 307:
                    ToastUtil.showShort(getApplicationContext(), "你们已经是好友啦！");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_friend);
        init();

        mTopBar.setRightIsVisable(false);
        mTopBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void LeftClick(View view) {
                finish();
            }

            @Override
            public void RightClick(View view) {

            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEtext.getText().toString();
                if (input.equals("")) {
                    ToastUtil.showShort(getApplicationContext(), "嘿！你可什么东西都没输入呢！");
                } else {
                    if (mChoice) {
                        if (input.matches("[a-z0-9A-Z\\u4e00-\\u9fa5]{1,15}")) {
                            getDatas(input);
                        } else {
                            ToastUtil.showShort(getApplicationContext(), "输入格式错误！");
                        }
                    } else {
                        if (input.matches("[1-9][0-9]{4,10}")) {
                            getDatas(input);
                        } else {
                            ToastUtil.showShort(getApplicationContext(), "输入格式错误！");
                        }
                    }
                }
            }
        });
    }

    private void init() {

        mTopBar = (TopBar) findViewById(R.id.tb_apply);
        mEtext = (EditText) findViewById(R.id.et_search);
        mButton = (Button) findViewById(R.id.btn_search);
        mListv = (ListView) findViewById(R.id.lv_friend);
        mDatas = new ArrayList<MessageModel>();
        mChoice = getIntent().getBooleanExtra("choice", true);
        mUrl = mChoice ? firstUrl : secondUrl;
        if (mChoice) {
            mEtext.setHint("昵称搜索");
        } else {
            mEtext.setHint("账号搜索");
        }
        mAdapter = new ApplyFriendAdapter(ActivityApplyFriend.this, R.layout.item_apply, mHandler, mDatas);
        mListv.setAdapter(mAdapter);
    }

    private void getDatas(String identifier) {

        HttpUtil.get(mUrl + identifier, new HttpUtil.CallBackListener() {
            Message msg = new Message();

            @Override
            public void OnFinish(String result) {
                JsonModel<MessageModel, String> jsonModel = JsonUtil.toModel(result,
                        new TypeToken<JsonModel<MessageModel, String>>() {
                        }.getType());
                msg.what = jsonModel.getState();
                msg.obj = jsonModel.getJsonList();

                mHandler.sendMessage(msg);
            }

            @Override
            public void OnError(Exception e) {
                e.printStackTrace();
                msg.what = 0;
                mHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 快速启动
     *
     * @param context
     * @param choice  true代表按照昵称，false代表账号
     */
    public static void actionStart(Context context, boolean choice) {
        Intent intent = new Intent(context, ActivityApplyFriend.class);
        intent.putExtra("choice", choice);
        context.startActivity(intent);
    }
}