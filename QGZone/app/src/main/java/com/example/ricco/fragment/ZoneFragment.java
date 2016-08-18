package com.example.ricco.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.ricco.constant.Constant;
import com.example.ricco.others.ImageLoader;
import com.example.ricco.others.ShuoshuoListview;
import com.example.ricco.others.TopBar;
import com.example.ricco.qgzone.AlbumActivity;
import com.example.ricco.qgzone.FriendTalkPubActivity;
import com.example.ricco.qgzone.InfoActivity;
import com.example.ricco.qgzone.MainActivity;
import com.example.ricco.qgzone.MsgBoardActivity;
import com.example.ricco.qgzone.R;
import com.example.ricco.others.CircleImageVIew;
import com.example.ricco.utils.ToastUtil;

/**
 * Created by Ricco on 2016/8/9.
 * 个人空间
 */
public class ZoneFragment extends BaseFragment implements View.OnClickListener{

    private View mHeadView = null;
    private Activity mContext = null;
    private TopBar mTopBar = null;
    private CircleImageVIew mCiv = null;
    private LinearLayout mTalkPub = null;
    private LinearLayout mAlbum = null;
    private LinearLayout mMsgBoard = null;
    private String mUrl = Constant.TalkPub.individuallity + Constant.HOST_ID + "&";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        // 初始化头视图
        initHeadView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ShuoshuoListview.setHeader(mHeadView);
        ShuoshuoListview.setShuoshuoURL(mUrl);
        View layout = inflater.inflate(R.layout.fragment_zone,
                container, false);

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTopBar = (TopBar) mContext.findViewById(R.id.tb_zone);
        mTopBar.setLeftIsVisable(false);
        mTopBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void LeftClick(View view) {
            }

            @Override
            public void RightClick(View view) {
                InfoActivity.actionStart(mContext, "host", Constant.HOST_ID);
            }
        });
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        MainActivity.nowFragTag = Constant.FRAGMENT_FLAG_ZONE;
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

        //拿头像
        ImageLoader.getInstance(1)
                .loadImage(Constant.civUrl + Constant.HOST_ID + ".jpg", mCiv, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_talk_pub:
                FriendTalkPubActivity.actionStart(mContext, Constant.HOST_ID, false);
                break;
            case R.id.ll_album:
                AlbumActivity.actionStart(mContext, Constant.HOST_ID);
                break;
            case R.id.ll_msg_board:
                MsgBoardActivity.actionStart(mContext, Constant.HOST_ID);
                break;
            default:break;
        }
    }
}
