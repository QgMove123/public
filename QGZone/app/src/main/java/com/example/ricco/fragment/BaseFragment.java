package com.example.ricco.fragment;

import android.app.Fragment;
import android.content.Context;

import com.example.ricco.constant.Constant;

/**
 * Created by Ricco on 2016/8/9.
 */

public class BaseFragment extends Fragment {

    public static BaseFragment newInstance(Context context,String tag){
        BaseFragment baseFragment =  null;

        switch (tag) {
            case Constant.FRAGMENT_FLAG_DONGTAI:
                baseFragment = new DynamicFragment();
                break;
            case Constant.FRAGMENT_FLAG_ABOUTME:
                baseFragment = new RelatedFragment();
                break;
            case Constant.FRAGMENT_FLAG_ZONE:
                baseFragment = new ZoneFragment();
                break;
            case Constant.FRAGMENT_FLAG_FRIEND:
                baseFragment = new FriListFragment();
                break;
            default:break;
        }

        return baseFragment;
    }
}
