package com.example.ricco.qgzone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.example.ricco.adapter.ApplyFriendAdapter;
import com.example.ricco.entity.FriendApplyModel;
import com.example.ricco.entity.ResourceModel;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
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

    private String url = null;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_friend);

        init();
        mListView.setAdapter(alyAdapter);

    }

    // 获取数据
    private void getDatas() {
        HttpUtil.Get(url, new HttpUtil.CallBackListener() {
            @Override
            public void OnFinish(Object result) {

                Message msg = new Message();
                ResourceModel resourceModel = JsonUtil.toObject((String) result, ResourceModel.class);
                msg.what = Integer.valueOf(resourceModel.getState());
                msg.obj = resourceModel.getObject();

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
        mDatas = new ArrayList<FriendApplyModel>();
        FriendApplyModel friendApplyModel = new FriendApplyModel();
        friendApplyModel.setRequesterName("wzkang!");
        mTopBar.setRightIsVisable(false);
        for (int i=0; i<10 ;i++) {
            mDatas.add(friendApplyModel);
        }

        alyAdapter = new ApplyFriendAdapter(getApplicationContext(), R.layout.item_apply, mDatas);
    }

    // 方便启动
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ActivityCheckFriend.class);
    }
}
