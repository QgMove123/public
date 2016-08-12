package com.example.ricco.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.ricco.entity.FriendApplyModel;
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.CircleImageVIew;
import com.example.ricco.utils.LogUtil;
import com.example.ricco.utils.ToastUtil;

import java.util.List;

/**
 * Created by Ricco on 2016/8/11.
 * @author Wzkang
 * 添加好友、审核好友的适配器
 */
public class ApplyFriendAdapter extends BaseAdapter {

    private Context mContext = null;
    private int mResource = 0;
    private List<FriendApplyModel> mData = null;
    private LayoutInflater mInflater = null;

    public ApplyFriendAdapter(Context context, int resource,
                         List<FriendApplyModel> FriendApplyModel) {
        mContext = context;
        mResource = resource;
        mData = FriendApplyModel;
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

        viewHolder.civ_head.setImageResource(R.mipmap.ic_launcher);
        viewHolder.tv_item_name.setText(mData.get(position).getRequesterName());
        LogUtil.d("state","" + position + ":" + mData.get(position).getApplyState() + "");
        if (mData.get(position).getApplyState() == 1) {
            viewHolder.btn_apply.setClickable(false);
            viewHolder.btn_apply.setText("已添加");
        } else {
            viewHolder.btn_apply.setText("添加");
            viewHolder.btn_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showShort(mContext, position + "");
                    new AlertDialog.Builder(mContext)
                            .setTitle("添加请求")
                            .setMessage("请问你是否要添加其为好友")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ToastUtil.showShort(mContext, "You click the positive!");
                                            mData.get(position).setApplyState(1);
                                            notifyDataSetChanged();
                                            LogUtil.d("state2", "" + position);
                                        }
                                    })
                            .setNegativeButton("取消", null).create().show();
                }
            });
        }
    }

    public class ViewHolder {
        private CircleImageVIew civ_head;
        private TextView tv_item_name;
        private Button btn_apply;
    }
}
