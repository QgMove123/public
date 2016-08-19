package com.example.ricco.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.ricco.constant.Constant;
import com.example.ricco.entity.MessageModel;
import com.example.ricco.others.ImageLoader;
import com.example.ricco.qgzone.R;
import com.example.ricco.others.CircleImageVIew;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.LogUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * 添加好友适配器
 * Created by Ricco on 2016/8/11.
 * @author Wzkang
 */
public class ApplyFriendAdapter extends BaseAdapter {

    private Context mContext = null;
    private int mResource = 0;
    private List<MessageModel> mData = null;
    private LayoutInflater mInflater = null;
    private String mUrl = Constant.Friend.addFriend;
    private Handler mHandler = null;

    public ApplyFriendAdapter(Context context, int resource,
                                Handler handler, List<MessageModel> messageList) {
        mContext = context;
        mResource = resource;
        mHandler = handler;
        mData = messageList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(mInflater, position, convertView, mResource);
    }

    private View createViewFromResource(LayoutInflater inflater, int position,
                                        View convertView, int resource) {
        View v;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            v = inflater.inflate(resource, null);
            viewHolder.civ_head = (CircleImageVIew) v.findViewById(R.id.civ_item_head);
            viewHolder.tv_item_name = (TextView) v.findViewById(R.id.tv_item_name);
            viewHolder.tv_item_time = (TextView) v.findViewById(R.id.tv_item_time);
            viewHolder.btn_apply = (Button) v.findViewById(R.id.btn_apply);
            v.setTag(viewHolder);
        } else {
            v = convertView;
            viewHolder = (ViewHolder) v.getTag();
        }

        bindView(position, viewHolder);
        return v;
    }

    private void bindView(final int position, ViewHolder viewHolder) {

        ImageLoader.getInstance(1).loadImage(Constant.civUrl + mData.get(position).getUserImage(),
                viewHolder.civ_head, false);
        viewHolder.tv_item_name.setText(mData.get(position).getUserName());
        viewHolder.tv_item_time.setVisibility(View.GONE);
        viewHolder.btn_apply.setText("添加");

        viewHolder.btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("ApplyAdapter", "" + position);
                new AlertDialog.Builder(mContext)
                        .setTitle("添加请求")
                        .setMessage("请问你是否要添加其为好友")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HttpUtil.Get(mUrl + mData.get(position).getUserId(), new HttpUtil.CallBackListener() {
                                    Message msg = new Message();
                                    @Override
                                    public void OnFinish(String result) {
                                        Map<String, Integer> jsonModel =
                                                JsonUtil.toModel(result,
                                                        new TypeToken<Map<String, Integer>>(){}.getType());
                                        msg.what = 2 + jsonModel.get("state").intValue();
                                        mHandler.sendMessage(msg);
                                    }

                                    @Override
                                    public void OnError(Exception e) {
                                        msg.what = 0;
                                        mHandler.sendMessage(msg);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", null).create().show();
            }
        });
    }

    public class ViewHolder {
        private CircleImageVIew civ_head;
        private TextView tv_item_name;
        private TextView tv_item_time;
        private Button btn_apply;
    }
}
