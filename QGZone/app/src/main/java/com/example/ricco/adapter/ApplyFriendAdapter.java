package com.example.ricco.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ricco.entity.FriendApplyModel;
import com.example.ricco.entity.MessageModel;
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.CircleImageVIew;
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
        viewHolder.tv_item_name.setText(mData.get(position).getRequesterName() + position);
        viewHolder.btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, position + "");
            }
        });
    }

    public class ViewHolder {
        private CircleImageVIew civ_head;
        private TextView tv_item_name;
        private Button btn_apply;
    }
}
