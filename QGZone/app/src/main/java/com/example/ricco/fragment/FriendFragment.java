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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.ricco.adapter.FriendAdapter;
import com.example.ricco.constant.Constant;
import com.example.ricco.entity.ResourceModel;
import com.example.ricco.entity.MessageModel;
import com.example.ricco.qgzone.MainActivity;
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.ToastUtil;
import com.example.ricco.utils.TopBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wzkang
 *
 * 好友列表
 */
public class FriendFragment extends BaseFragment {

    private Activity mActivity = null;
    private TopBar mTopBar = null;
    private SwipeRefreshLayout mSwipe = null;
    private ListView mListview = null;
    private FriendAdapter mAdapter = null;
    private View headView = null;

    private List<MessageModel> mDatas;

    private String urlFriend = "http://主机号:8080/QGzone/MyFriends";

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    mDatas = (List<MessageModel>) msg.obj;
                    mAdapter.notifyDataSetChanged();
                    mSwipe.setRefreshing(false);
                    break;
                case 2:
                    ToastUtil.showShort(mActivity, "真可怜，一个好友都没有，赶紧加一个吧~");
                    break;
                case 3:
                    ToastUtil.showShort(mActivity, "服务器异常！");
                    break;
                case 4:
                    //刷新处理
                    getDatas();
                    break;
                default:break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_friend, container, false);

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 初始化
        init();

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.showShort(mActivity, "you click " + position);
            }
        });

        mListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.showShort(mActivity, "you long click " + position);
                return false;
            }
        });

        // 初始化刷新页面
        mSwipe.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_red_light);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(0, 1000);
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

    // 菜单方法
    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mActivity).inflate(
                R.layout.menu_friend, null);

        // 设置按钮的点击事件
        TextView tvName = (TextView) contentView.findViewById(R.id.tv_menu_name);
        TextView tvAccount = (TextView) contentView.findViewById(R.id.tv_menu_account);

        // 通过昵称添加
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mActivity, "You click the name!");
            }
        });

        // 通过账号添加
        tvAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mActivity, "You click the account!");
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

    private void init() {
        // 获取当前活动context
        mActivity = getActivity();

        mTopBar = (TopBar) mActivity.findViewById(R.id.tb_friend);
        mListview = (ListView) mActivity.findViewById(R.id.lv_friend);
        mSwipe = (SwipeRefreshLayout) mActivity.findViewById(R.id.swipe_container);
        mDatas = new ArrayList<MessageModel>();

        getDatas();

        mAdapter = new FriendAdapter(mActivity, R.layout.item_friend, mDatas);
        headView = LayoutInflater.from(mActivity).inflate(R.layout.head_view_friend, null);

        mListview.addHeaderView(headView);
        mListview.setAdapter(mAdapter);

        mTopBar.setLeftIsVisable(false);
    }

    // 获取数据
    private void getDatas() {
        HttpUtil.Get(urlFriend, new HttpUtil.CallBackListener() {
            @Override
            public void OnFinish(Object result) {

                Message msg = new Message();
                ResourceModel resourceModel = JsonUtil.toObject((String) result, ResourceModel.class);
                msg.what = Integer.valueOf(resourceModel.getState());
                msg.obj = resourceModel.getObject();

                mHandler.sendMessage(msg);
            }

            @Override
            public void OnError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        MainActivity.nowFragTag = Constant.FRAGMENT_FLAG_FRIEND;
    }
}
