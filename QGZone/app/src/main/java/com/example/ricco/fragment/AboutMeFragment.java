package com.example.ricco.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ricco.constant.Constant;
import com.example.ricco.qgzone.MainActivity;
import com.example.ricco.qgzone.R;
import com.example.ricco.adapter.AboutAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 与我相关
 */
public class AboutMeFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aboutme, container, false);

        AboutAdapter aa = new AboutAdapter(getActivity(), getData());
        ListView aboutList = (ListView) view.findViewById(R.id.list_about_me);
        aboutList.setAdapter(aa);
        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        MainActivity.nowFragTag = Constant.FRAGMENT_FLAG_ABOUTME;
    }

    /**
     * 获取与我相关的数据源
     * @return
     */
    public List<Map<String, Object>> getData(){
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();

        return list;
    }
}