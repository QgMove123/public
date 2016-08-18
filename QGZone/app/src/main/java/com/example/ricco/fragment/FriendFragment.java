package com.example.ricco.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.ricco.constant.Constant;
import com.example.ricco.qgzone.MainActivity;
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.ToastUtil;
import com.example.ricco.others.TopBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 好友列表
 */
public class FriendFragment extends BaseFragment {

    private Activity mActivity = null;
    private TopBar mTopBar = null;
    private SwipeRefreshLayout mSwipelayout = null;
    private ListView mListview = null;
    private ArrayAdapter<String> mAdapter = null;
    private List<String> mDatas = new ArrayList<String>(Arrays.asList("1", "2", "3",
            "4", "5", "6", "7", "8"));
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            mDatas.addAll(Arrays.asList("10", "10", "10", "10", "10", "10"));
            mAdapter.notifyDataSetChanged();
            mSwipelayout.setRefreshing(false);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_friend,
                container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();
        mTopBar = (TopBar) mActivity.findViewById(R.id.tb_friend);
        mListview = (ListView) mActivity.findViewById(R.id.lv_friend);
        mSwipelayout = (SwipeRefreshLayout) mActivity.findViewById(R.id.swipe_container);
        mAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, mDatas);
        mListview.setAdapter(mAdapter);

        mTopBar.setLeftIsVisable(false);
        mSwipelayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_red_light);

        mSwipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(0, 2000);
            }
        });

        mTopBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void LeftClick(View view) {

            }

            @Override
            public void RightClick(View view) {

                showPopupWindow(view);
            }
        });
    }

    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mActivity).inflate(
                R.layout.menu_friend, null);
        // 设置按钮的点击事件
        Button button = (Button) contentView.findViewById(R.id.button_3);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mActivity, "button is pressed!");
            }
        });

        final PopupWindow popupWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 显示 默认显示View的正下方，可以使用重载方法设置偏移量来调整位置
        popupWindow.showAsDropDown(view);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        MainActivity.nowFragTag = Constant.FRAGMENT_FLAG_FRIEND;
    }
}
