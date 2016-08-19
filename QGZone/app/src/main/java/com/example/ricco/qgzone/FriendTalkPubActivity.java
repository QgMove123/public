package com.example.ricco.qgzone;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.ricco.constant.Constant;
import com.example.ricco.others.ImageLoader;
import com.example.ricco.others.ShuoshuoListview;
import com.example.ricco.others.TopBar;
import com.example.ricco.others.CircleImageVIew;
import com.example.ricco.utils.LogUtil;
import com.example.ricco.utils.ToastUtil;


/**
 * 好友主页
 * Created by Ricco on 2016/8/18.
 */
public class FriendTalkPubActivity extends BaseActivity implements View.OnClickListener {

    private View mHeadView = null;
    private Context mContext = FriendTalkPubActivity.this;
    private TopBar mTopBar = null;
    // headview 视图
    private CircleImageVIew mCiv = null;
    private LinearLayout mTalkPub = null;
    private LinearLayout mAlbum = null;
    private LinearLayout mMsgBoard = null;
    private int friendId;
    private String mUrl = Constant.TalkPub.individuallity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendId = getIntent().getIntExtra("friendId", 0);
        LogUtil.e("friendId", friendId + "");
        // 初始化头视图
        initHeadView();
        ShuoshuoListview.setHeader(mHeadView);

        // 设置shuoShuoListView url
        ShuoshuoListview.setShuoshuoURL(mUrl + friendId + "&page=");

        // 加载主视图
        setContentView(R.layout.fragment_zone);

        mTopBar = (TopBar) findViewById(R.id.tb_zone);
        mTopBar.setTitle("好友主页");

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

    private void initHeadView() {
        mHeadView = LayoutInflater.from(mContext).inflate(R.layout.head_view_zone, null);
        mCiv = (CircleImageVIew) mHeadView.findViewById(R.id.civ_zone);
        mTalkPub = (LinearLayout) mHeadView.findViewById(R.id.ll_talk_pub);
        mAlbum = (LinearLayout) mHeadView.findViewById(R.id.ll_album);
        mMsgBoard = (LinearLayout) mHeadView.findViewById(R.id.ll_msg_board);

        // 绑定监听
        mTalkPub.setOnClickListener(this);
        mAlbum.setOnClickListener(this);
        mMsgBoard.setOnClickListener(this);

        // 拿头像
        ImageLoader.getInstance(1).loadImage(Constant.civUrl + friendId + ".jpg", mCiv, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_talk_pub:
                PerTalkActivity.actionStart(mContext, friendId);
                break;
            case R.id.ll_album:
                AlbumActivity.actionStart(mContext, friendId);
                break;
            case R.id.ll_msg_board:
                MsgBoardActivity.actionStart(mContext, friendId);
                break;
            default:
                break;
        }
    }

    // 快速启动
    public static void actionStart(Context context, int friendId) {
        Intent intent = new Intent(context, FriendTalkPubActivity.class);
        intent.putExtra("friendId", friendId);
        context.startActivity(intent);
    }
}
