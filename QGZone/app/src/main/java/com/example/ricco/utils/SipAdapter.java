package com.example.ricco.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ricco.qgzone.R;

import java.util.List;
import java.util.Map;

/**
 * 适配器
 * 1.List<Map<String,Object>>
 * 2.ViewHolder
 */
public class SipAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context mContext;
    private List<Map<String,Object>> mDatas;
    private LayoutInflater mInflater;
    //1.获取数据源
    public SipAdapter(Context context, List<Map<String,Object>> datas){
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }
    //2.创建ViewHolder存放布局View下的所有子控件
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_album, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }
    //3.进行数据项和控件的绑定
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    public ImageView imgItem;
    public TextView tvItem;

    public MyViewHolder(View itemView) {
        super(itemView);
        imgItem = (ImageView) itemView.findViewById(R.id.item_img);
        tvItem = (TextView) itemView.findViewById(R.id.item_tv);
    }
}