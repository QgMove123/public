package com.example.ricco.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ricco.constant.Constant;
import com.example.ricco.others.CircleImageVIew;
import com.example.ricco.others.ImageLoader;
import com.example.ricco.others.ShuoshuoListview;
import com.example.ricco.qgzone.MainActivity;
import com.example.ricco.qgzone.R;

/**
 * 首页的说说列表
 * Created by Ricco on 2016/8/9.
 */
public class DongTaiFragment extends BaseFragment {

    private ShuoshuoListview mShuoshuo = null;
    private View mHeadView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 初始化头视图
        initHeadView();
        ShuoshuoListview.setHeader(mHeadView);
        ShuoshuoListview.setisNote(false);
        ShuoshuoListview.setShuoshuoURL(Constant.TalkPub.dongtaitalkpub);

        View layout = inflater.inflate(R.layout.fragment_dongtai, container, false);
        MainActivity.nowFragTag = Constant.FRAGMENT_FLAG_DONGTAI;
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mShuoshuo = (ShuoshuoListview) getActivity().findViewById(R.id.dongtai_fragment);
    }

    private void initHeadView() {
        mHeadView = View.inflate(getActivity(), R.layout.dongtai_title_layout, null);
        CircleImageVIew civ = (CircleImageVIew) mHeadView.findViewById(R.id.talk_list_personphoto);

        //拿头像
        ImageLoader.getInstance(1)
                .loadImage(Constant.civUrl + Constant.HOST_ID + ".jpg", civ, false);
    }

    @Override 
    public void onStart(){
        // TODO: 2016/8/22 避免被新的Activity遮盖后导致数据改动
        super.onStart();
        ShuoshuoListview.setHeader(mHeadView);
        ShuoshuoListview.setisNote(false);
        ShuoshuoListview.setShuoshuoURL(Constant.TalkPub.dongtaitalkpub);
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MainActivity.nowFragTag = Constant.FRAGMENT_FLAG_DONGTAI;
    }
}