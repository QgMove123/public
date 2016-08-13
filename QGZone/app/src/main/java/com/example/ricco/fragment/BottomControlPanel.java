package com.example.ricco.fragment;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.ricco.constant.Constant;
import com.example.ricco.qgzone.R;

/**
 * Created by Ricco on 2016/8/9.
 */

public class BottomControlPanel extends LinearLayout implements View.OnClickListener {
    private ImageText mDtaiBtn = null;
    private ImageText mAmeBtn = null;
    private ImageText mSendBtn = null;
    private ImageText mZoneBtn = null;
    private ImageText mFriendBtn = null;
    private int DEFALUT_BACKGROUND_COLOR = Color.rgb(243, 243, 243); //Color.rgb(192, 192, 192)
    private BottomPanelCallback mBottomCallback = null;

    public BottomControlPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    //定义接口回调
    public interface BottomPanelCallback{
        public void onBottomPanelClick(int itemId);
    }

    //设置接口
    public void setBottomCallback(BottomPanelCallback bottomCallback){
        mBottomCallback = bottomCallback;
    }

    //当加载完布局的时候，进行下面的操作并显示自定义控件
    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();
        mDtaiBtn = (ImageText) findViewById(R.id.btn_dongtai);
        mAmeBtn = (ImageText) findViewById(R.id.btn_aboutme);
        mSendBtn = (ImageText) findViewById(R.id.btn_send);
        mZoneBtn = (ImageText) findViewById(R.id.btn_zone);
        mFriendBtn = (ImageText) findViewById(R.id.btn_friend);
        setBackgroundColor(DEFALUT_BACKGROUND_COLOR);
    }

    public void initBottomPanel(){
        if(mDtaiBtn != null){
//            mDtaiBtn.setImage(R.drawable.message_unselected);
            mDtaiBtn.setImage(R.drawable.other_login_weibo);
            mDtaiBtn.setText("好友动态");
        }
        if(mAmeBtn != null){
            mAmeBtn.setImage(R.drawable.other_login_weibo);
            mAmeBtn.setText("与我相关");
        }
        if(mSendBtn != null){
            mSendBtn.setImage(R.drawable.other_login_weibo);
            //用于标识隐藏
            mSendBtn.setText("HIDE");
        }
        if(mZoneBtn != null){
            mZoneBtn.setImage(R.drawable.other_login_weibo);
            mZoneBtn.setText("个人空间");
        }
        if(mFriendBtn != null){
            mFriendBtn.setImage(R.drawable.other_login_weibo);
            mFriendBtn.setText("好友列表");
        }
        setBtnListener();
    }

    //绑定监听
    private void setBtnListener(){

        int num = this.getChildCount();
        for(int i = 0; i < num; i++){
            View v = this.getChildAt(i);
            if(v != null){
                v.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        initBottomPanel();
        int index = -1;
        switch(v.getId()){
            case R.id.btn_dongtai:
                index = Constant.BTN_FLAG_DONGTAI;
                mDtaiBtn.setChecked(index);
                break;
            case R.id.btn_aboutme:
                index = Constant.BTN_FLAG_ABOUTME;
                mAmeBtn.setChecked(index);
                break;
            case R.id.btn_send:
                index = Constant.BTN_FLAG_SEND;
                mSendBtn.setChecked(index);
                break;
            case R.id.btn_zone:
                index = Constant.BTN_FLAG_ZONE;
                mZoneBtn.setChecked(index);
                break;
            case R.id.btn_friend:
                index = Constant.BTN_FLAG_FRIEND;
                mFriendBtn.setChecked(index);
            default:break;
        }
        if(mBottomCallback != null){
            mBottomCallback.onBottomPanelClick(index);
        }
    }

    //设置一开始的默认选择
    public void defaultBtnChecked(){
        if(mDtaiBtn != null){
            mDtaiBtn.setChecked(Constant.BTN_FLAG_DONGTAI);
        }
    }
}
