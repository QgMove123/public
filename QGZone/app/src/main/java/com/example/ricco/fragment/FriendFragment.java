package com.example.ricco.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import com.example.ricco.entity.JsonModel;
import com.example.ricco.entity.MessageModel;
import com.example.ricco.others.ImageLoader;
import com.example.ricco.qgzone.ActivityApplyFriend;
import com.example.ricco.qgzone.ActivityCheckFriend;
import com.example.ricco.qgzone.MainActivity;
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.CircleImageVIew;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.LogUtil;
import com.example.ricco.utils.ToastUtil;
import com.example.ricco.utils.TopBar;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 好友列表
 *
 * @author Wzkang
 */
public class FriendFragment extends BaseFragment {

    private Activity mActivity = null;
    private TopBar mTopBar = null;
    private SwipeRefreshLayout mSwipe = null;
    private ListView mListview = null;
    private CircleImageVIew mCiv = null;
    private FriendAdapter mAdapter = null;
    private View headView = null;

    private List<MessageModel> mDatas = null;

    private String urlFriend = "http://192.168.3.33:8080/QGzone/MyFriends";
    private String urlDelete = "http://192.168.3.33:8080/QGzone/DeleteFriend?friendId=";
    private String urlHaveFrd = "http://192.168.3.33:8080/QGzone/HaveFriendApply";

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    ToastUtil.showShort(mActivity, "服务器异常！");
                    break;
                case 1:
                    mSwipe.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                    break;
                case 301:
                    if (mDatas.size() > 0) {
                        mDatas.clear();
                    }
                    for (MessageModel mm : (List<MessageModel>) msg.obj) {
                        mDatas.add(mm);
                    }
                    mSwipe.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                    LogUtil.d("FriendFragment", "好友列表");
                    break;
                case 302:
                    ToastUtil.showShort(mActivity, "真可怜，一个好友都没有，赶紧加一个吧~");
                    break;
                case 303:
                    ToastUtil.showShort(mActivity, "删除成功！");
                    mDatas.remove((int) msg.obj);
                    mAdapter.notifyDataSetChanged();
                    break;
                case 304:
                    ToastUtil.showShort(mActivity, "你删除失败了喔！");
                    break;
                case 305:
                    LogUtil.d("FriendFragment", "有新的好友请求");
                    break;
                case 306:
                    LogUtil.d("FriendFragment", "没有好友请求");
                    break;
                default:
                    break;
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
        refreshHaveFre();
        getDatas();


        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ActivityCheckFriend.actionStart(mActivity);
                }
                ToastUtil.showShort(mActivity, "you click " + position);
            }
        });

        mListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(mActivity)
                        .setTitle("警告")
                        .setMessage("你是否要删除此好友！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HttpUtil.get(urlDelete + mDatas.get(position - 1).getUserId(), new HttpUtil.CallBackListener() {
                                    Message msg = new Message();

                                    @Override
                                    public void OnFinish(String result) {
                                        Map<String, Integer> jsonModel =
                                                JsonUtil.toModel((String) result,
                                                        new TypeToken<Map<String, Integer>>() {
                                                        }.getType());
                                        msg.what = jsonModel.get("state").intValue() + 2;
                                        msg.obj = position - 1;
                                        mHandler.sendMessage(msg);
                                    }

                                    @Override
                                    public void OnError(Exception e) {
                                        e.printStackTrace();
                                        mHandler.sendEmptyMessage(0);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", null).create().show();
                return true;
            }
        });

        // 初始化刷新页面
        mSwipe.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_red_light);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDatas();
                mHandler.sendEmptyMessageDelayed(1, 1000);
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
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.menu_friend, null);

        // 设置按钮的点击事件
        TextView tvName = (TextView) contentView.findViewById(R.id.tv_menu_name);
        TextView tvAccount = (TextView) contentView.findViewById(R.id.tv_menu_account);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

        // 通过昵称添加
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityApplyFriend.actionStart(mActivity, true);
                popupWindow.dismiss();
            }
        });

        // 通过账号添加
        tvAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityApplyFriend.actionStart(mActivity, false);
                popupWindow.dismiss();
            }
        });

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
        mAdapter = new FriendAdapter(mActivity, R.layout.item_friend, mDatas);


        // 添加头视图
        headView = LayoutInflater.from(mActivity).inflate(R.layout.head_view_friend, null);
        mCiv = (CircleImageVIew) mActivity.findViewById(R.id.civ_check_friend);
        mListview.addHeaderView(headView);

        mListview.setAdapter(mAdapter);
        mTopBar.setLeftIsVisable(false);
    }

    // 获取数据
    private void getDatas() {
        HttpUtil.get(urlFriend, new HttpUtil.CallBackListener() {

            Message msg = new Message();

            @Override
            public void OnFinish(String result) {
                JsonModel<MessageModel, String> jsonModel = JsonUtil.toModel(result,
                        new TypeToken<JsonModel<MessageModel, String>>() {
                        }.getType());
                msg.what = jsonModel.getState();
                msg.obj = jsonModel.getJsonList();

                mHandler.sendMessage(msg);
            }

            @Override
            public void OnError(Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(0);
            }
        });
    }

    private void refreshHaveFre() {
        HttpUtil.get(urlHaveFrd, new HttpUtil.CallBackListener() {
            @Override
            public void OnFinish(String result) {
                Map<String, Integer> jsonModel =
                        JsonUtil.toModel((String) result,
                                new TypeToken<Map<String, Integer>>() {
                                }.getType());
                mHandler.sendEmptyMessage(4 + jsonModel.get("state").intValue());
            }

            @Override
            public void OnError(Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(0);
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