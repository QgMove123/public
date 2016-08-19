package com.example.ricco.qgzone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ricco.adapter.CheckedFriendAdapter;
import com.example.ricco.constant.Constant;
import com.example.ricco.entity.FriendApplyModel;
import com.example.ricco.entity.JsonModel;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.ToastUtil;
import com.example.ricco.others.TopBar;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 查看请求添加好友列表
 * @author Wzkang
 * Created by Ricco on 2016/8/11.
 */
public class CheckFriendActivity extends BaseActivity {

    private TopBar mTopBar = null;
    private ListView mListView = null;
    private List<FriendApplyModel> mDatas = null;
    private CheckedFriendAdapter alyAdapter = null;

    private String urlCheck = Constant.Friend.friendRequest;
    private String urlDelete = Constant.Friend.deleteRequest;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtil.showShort(getApplicationContext(), "服务器异常！");
                    break;
                // 有好友
                case 301:
                    if (msg.obj != null) {
                        for (FriendApplyModel fm :(List<FriendApplyModel>) msg.obj) {
                            mDatas.add(fm);
                        }
                        alyAdapter.notifyDataSetChanged();
                    }
                    break;
                // 没有好友
                case 302:
                    ToastUtil.showShort(getApplicationContext(), "你并没有好友请求呢！");
                    break;
                case 303:
                    ToastUtil.showShort(getApplicationContext(), "删除好友请求成功！");
                    mDatas.remove((int) msg.obj);
                    alyAdapter.notifyDataSetChanged();
                    break;
                case 304:
                    ToastUtil.showShort(getApplicationContext(), "删除失败！");
                    break;
                case 305:
                    mDatas.get((int) msg.obj).setApplyState(1);
                    ToastUtil.showShort(getApplicationContext(), "添加成功！");
                    alyAdapter.notifyDataSetChanged();
                    break;
                case 306:
                    ToastUtil.showShort(getApplicationContext(), "添加失败！");
                    break;
                case 307:
                    ToastUtil.showShort(getApplicationContext(), "已经处理！");
                    break;
                case 308:
                    ToastUtil.showShort(getApplicationContext(), "申请不存在！");
                    break;
                default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_friend);

        init();
        getDatas();

        mTopBar.setRightIsVisable(false);
        mTopBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void LeftClick(View view) {
                finish();
            }

            @Override
            public void RightClick(View view) {

            }
        });

        mListView.setAdapter(alyAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.showShort(CheckFriendActivity.this, "outside:" + position);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(CheckFriendActivity.this)
                        .setTitle("警告")
                        .setMessage("你是否要删除此好友请求！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HttpUtil.Get(urlDelete + mDatas.get(position).getFriendApplyId(), new HttpUtil.CallBackListener() {
                                    @Override
                                    public void OnFinish(String result) {
                                        Message msg = new Message();
                                        Map<String, Integer> jsonModel =
                                                JsonUtil.toModel(result,
                                                        new TypeToken<Map<String, Integer>>(){}.getType());
                                        msg.what = 2 + jsonModel.get("state").intValue();
                                        msg.obj = position;
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
    }

    // 获取数据
    private void getDatas() {
        HttpUtil.Get(urlCheck, new HttpUtil.CallBackListener() {
            Message msg = new Message();
            @Override
            public void OnFinish(String result) {

                JsonModel<FriendApplyModel, String> resourceModel =
                        JsonUtil.toModel(result,
                                new TypeToken<JsonModel<FriendApplyModel, String>>(){}.getType());
                msg.what = resourceModel.getState();
                msg.obj = resourceModel.getJsonList();

                mHandler.sendMessage(msg);
            }

            @Override
            public void OnError(Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(0);
            }
        });
    }

    private void init() {

        mTopBar = (TopBar) findViewById(R.id.tb_apply);
        mListView = (ListView) findViewById(R.id.lv_friend);
        mDatas = new ArrayList<FriendApplyModel>();
        alyAdapter = new CheckedFriendAdapter(CheckFriendActivity.this, R.layout.item_apply,
                mHandler, mDatas);
    }

    // 方便启动
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, CheckFriendActivity.class);
        context.startActivity(intent);
    }
}
