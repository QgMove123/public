package com.example.ricco.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ricco.constant.Constant;
import com.example.ricco.others.ShuoshuoListview;
import com.example.ricco.qgzone.MainActivity;
import com.example.ricco.qgzone.R;

/**
 * 首页的说说列表
 * Created by Ricco on 2016/8/9.
 */
public class DongTaiFragment extends BaseFragment {

    private ShuoshuoListview mShuoshuo = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View header = View.inflate(getActivity(), R.layout.dongtai_title_layout, null);
        ShuoshuoListview.setHeader(header);
        ShuoshuoListview.setisNote(false);
        ShuoshuoListview.setShuoshuoURL("http://"+Constant.host+"/QGzone/TwitterGetTest?page=");
        View layout = inflater.inflate(R.layout.fragment_dongtai, container, false);
        MainActivity.nowFragTag = Constant.FRAGMENT_FLAG_DONGTAI;
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mShuoshuo = (ShuoshuoListview) getActivity().findViewById(R.id.dongtai_fragment);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MainActivity.nowFragTag = Constant.FRAGMENT_FLAG_DONGTAI;
    }
}