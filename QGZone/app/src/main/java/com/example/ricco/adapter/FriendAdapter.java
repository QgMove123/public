package com.example.ricco.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ricco.constant.Constant;
import com.example.ricco.entity.MessageModel;
import com.example.ricco.others.ImageLoader;
import com.example.ricco.qgzone.InfoActivity;
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.CircleImageVIew;
import com.example.ricco.utils.ToastUtil;

import java.util.List;

/**
 * @author Wzkang
 * Created by Ricco on 2016/8/11.
 *
 * 适配好友列表
 */
public class FriendAdapter extends BaseAdapter {

    private int mResource = 0;
    private Context mContext = null;
    private List<MessageModel> mData = null;
    private LayoutInflater mInflater = null;

    public FriendAdapter(Context context, int resource,
                         List<MessageModel> messageModels) {
        mResource = resource;
        mContext = context;
        mData = messageModels;
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
            viewHolder.tv_name = (TextView) v.findViewById(R.id.tv_item_name);
            v.setTag(viewHolder);
        } else {
            v = convertView;
            viewHolder = (ViewHolder) v.getTag();
        }

        bindView(position, viewHolder);
        return v;
    }

    private void bindView(final int position, ViewHolder viewHolder) {

        ImageLoader.getInstance(1).loadImage(Constant.civUrl + mData.get(position).getUserImage() ,
                viewHolder.civ_head, false);
        viewHolder.tv_name.setText(mData.get(position).getUserName());
        viewHolder.civ_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoActivity.actionStart(mContext, "friend", mData.get(position).getUserId());
            }
        });
    }

    public class ViewHolder {
        public CircleImageVIew civ_head;
        public TextView tv_name;
    }

    public void notifyChanged(List<MessageModel> messageModels) {
        mData = null;
        mData = messageModels;
        notifyDataSetChanged();
    }
}
