package com.example.ricco.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ricco.constant.Constant;
import com.example.ricco.qgzone.MainActivity;
import com.example.ricco.qgzone.R;

/**
 * 好友列表
 */
public class FriListFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.friend_layout,
                container, false);
        return layout;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        MainActivity.nowFragTag = Constant.FRAGMENT_FLAG_FRIEND;
    }
}
