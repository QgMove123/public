package com.example.ricco.qgzone;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ricco.constant.Constant;
import com.example.ricco.others.ShuoshuoListview;
import com.example.ricco.others.TopBar;


/**
 * 好友主页
 * Created by Ricco on 2016/8/18.
 */
public class PerTalkActivity extends BaseActivity {

    private View mHeadView = null;
    private Context mContext = PerTalkActivity.this;
    private TopBar mTopBar = null;
    private int friendId;
    private String mUrl = Constant.TalkPub.individuallity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendId = getIntent().getIntExtra("friendId", 0);

        ShuoshuoListview.setHeader(null);
        // 设置shuoShuoListView url
        ShuoshuoListview.setShuoshuoURL(mUrl + friendId + "&");

        // 加载主视图
        setContentView(R.layout.fragment_zone);

        mTopBar = (TopBar) findViewById(R.id.tb_zone);
        mTopBar.setTitle("说说");

        mTopBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void LeftClick(View view) {
                finish();
            }

            @Override
            public void RightClick(View view) {
                InfoActivity.actionStart(mContext, "friend", friendId);
            }
        });
    }

    // 快速启动
    public static void actionStart(Context context, int friendId) {
        Intent intent = new Intent(context, PerTalkActivity.class);
        intent.putExtra("friendId", friendId);
        context.startActivity(intent);
    }
}
