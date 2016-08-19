package com.example.ricco.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ricco.constant.Constant;
import com.example.ricco.entity.RelationModel;
import com.example.ricco.others.ImageLoader;
import com.example.ricco.qgzone.R;
import com.example.ricco.others.CircleImageVIew;

import java.util.List;

/**
 * 与我相关的适配器
 * Created by chenyi on 2016/8/12.
 */
public class AboutAdapter extends BaseAdapter {

    private List<RelationModel> data;
    private Context context;

    public AboutAdapter(Context context, List<RelationModel> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        if(data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder = null;
        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_about_me, null);
            viewHolder = new ViewHolder();
            viewHolder.headPic = (CircleImageVIew) view.findViewById(R.id.friend_pic);
            viewHolder.name = (TextView) view.findViewById(R.id.friend_name);
            viewHolder.time = (TextView) view.findViewById(R.id.time);
            viewHolder.words = (TextView) view.findViewById(R.id.words);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        ImageLoader.getInstance(1).loadImage(Constant.civUrl+data.get(position).getSender().getUserImage(), viewHolder.headPic, false);
        viewHolder.name.setText(data.get(position).getSender().getUserName() + transformType(data.get(position).getRelationType()));
        viewHolder.time.setText(data.get(position).getRelationTime().toLocaleString());
        viewHolder.words.setText(data.get(position).getRelationContent());
        return view;
    }

    public String transformType(String str) {
        String type = "";
        switch (str) {
            case "na":
                type = "给你留言";
                break;
            case  "nc":
                type = "在你的留言板中给你评论";
                break;
            case "tc":
                type = "回复你的说说";
                break;
            case "st":
                type = "给你点赞";
                break;
            case "fa":
                type = "";
                break;
        }
        return type;
    }

    /**
     * 缓存控件实例的ViewHolder
     */
    class ViewHolder {
        CircleImageVIew headPic;
        TextView name;
        TextView time;
        TextView words;
    }
}
