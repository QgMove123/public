package com.example.ricco.qgzone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ricco.adapter.ApplyFriendAdapter;
import com.example.ricco.adapter.FriendAdapter;
import com.example.ricco.entity.FriendApplyModel;
import com.example.ricco.entity.ResourceModel;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.ToastUtil;
import com.example.ricco.utils.TopBar;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ricco on 2016/8/11.
 */
public class ActivityCheckFriend extends BaseActivity {

    private TopBar mTopBar = null;
    private ListView mListView = null;
    private List<FriendApplyModel> mDatas = null;
    private ApplyFriendAdapter alyAdapter = null;

    final private String url = "http://主机号:8080/QGzone/MyFriendApply";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 有好友
                case 301:
                    break;
                // 没有好友
                case 302:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_friend);

        init();

        mListView.setAdapter(alyAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.showShort(ActivityCheckFriend.this, "" + position);
            }
        });
    }

    // 获取数据
    private void getDatas() {
        HttpUtil.Get(url, new HttpUtil.CallBackListener() {
            @Override
            public void OnFinish(Object result) {

                Message msg = new Message();
                ResourceModel resourceModel = JsonUtil.toObject((String) result, ResourceModel.class);
                msg.what = Integer.valueOf(resourceModel.getState());
                mDatas = (List<FriendApplyModel>) resourceModel.getObject();
                if (mDatas == null) {
                    mDatas = new ArrayList<FriendApplyModel>();
                }

                mHandler.sendMessage(msg);
            }

            @Override
            public void OnError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void init() {

        mTopBar = (TopBar) findViewById(R.id.tb_apply);
        mListView = (ListView) findViewById(R.id.lv_friend);
        getDatas();
        alyAdapter = new ApplyFriendAdapter(ActivityCheckFriend.this, R.layout.item_apply, mDatas);
    }

    // 方便启动
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ActivityCheckFriend.class);
    }
}
