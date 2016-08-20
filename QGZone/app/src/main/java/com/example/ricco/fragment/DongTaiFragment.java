package com.example.ricco.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.ricco.adapter.Item_Adapter;
import com.example.ricco.constant.Constant;
import com.example.ricco.entity.JsonModel;
import com.example.ricco.entity.TwitterModel;
import com.example.ricco.others.ShuoshuoListview;
import com.example.ricco.qgzone.MainActivity;
import com.example.ricco.qgzone.MsgBoardActivity;
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.ToastUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 首页的说说列表
 * Created by Ricco on 2016/8/9.
 */
public class DongTaiFragment extends BaseFragment {

    private ShuoshuoListview mShuoshuo = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ShuoshuoListview.setHeader(null);
        ShuoshuoListview.setShuoshuoURL("http://192.168.43.172:8080/QGzone/TwitterOfOthers?userId=10000&page=");
        View layout = inflater.inflate(R.layout.fragment_dongtai, container, false);
        MainActivity.nowFragTag = Constant.FRAGMENT_FLAG_DONGTAI;;
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