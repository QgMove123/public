package com.example.ricco.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ricco.adapter.AboutAdapter;
import com.example.ricco.constant.Constant;
import com.example.ricco.entity.RelationModel;
import com.example.ricco.qgzone.MainActivity;
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 与我相关
 */
public class AboutMeFragment extends BaseFragment {

    private ListView aboutList;
    private int page = 1;
    private static int leight = 0;
    private boolean flag = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aboutme, container, false);

        aboutList = (ListView) view.findViewById(R.id.list_about_me);
        sendRelation(Constant.Account.RelationGet+"?jsonObject={\"page\"=\""+(page++)+"\"}");

        aboutList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1 && flag) {
                        sendRelation(Constant.Account.RelationGet+"?page="+(page++));
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        MainActivity.nowFragTag = Constant.FRAGMENT_FLAG_ABOUTME;
    }

    // 发送注册信息给服务器
    private void sendRelation(String url) {
        HttpUtil.Get(url, new HttpUtil.CallBackListener() {
            @Override
            public void OnFinish(Object result) {

                Message msg = new Message();
                Log.e("OnFinish: result", result+"");

                AboutJsonModel object = JsonUtil.toObject(result.toString(), AboutJsonModel.class);
                msg.what = object.state;
                msg.obj = object.relations;

                mHandler.sendMessage(msg);
            }

            @Override
            public void OnError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 401:
                    List<RelationModel> data = new ArrayList<RelationModel>();
                    data.addAll((Collection<? extends RelationModel>) msg.obj);
                    if(leight == data.size()) {
                        flag = false;
                    } else {
                        leight = data.size();
                    }
                    AboutAdapter aa = new AboutAdapter(getActivity(), data);
                    aboutList.setAdapter(aa);
                    break;
                case 402:
                    Toast.makeText(getActivity(), "查看失败，请查看网络连接", Toast.LENGTH_SHORT).show();
                    break;
                default:break;
            }
        }
    };

    class AboutJsonModel {
        int state;
        List<RelationModel> relations;
    }

}