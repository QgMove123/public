package com.example.ricco.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ricco.constant.Constant;
import com.example.ricco.qgzone.MainActivity;
import com.example.ricco.qgzone.R;

/**
 * 与我相关
 */
public class AboutMeFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Layout = inflater.inflate(R.layout.fragment_aboutme,
                container, false);
        return Layout;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        MainActivity.nowFragTag = Constant.FRAGMENT_FLAG_ABOUTME;
    }
}
