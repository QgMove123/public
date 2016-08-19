package com.example.ricco.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ricco.constant.Constant;
import com.example.ricco.entity.AlbumModel;
import com.example.ricco.others.ImageLoader;
import com.example.ricco.qgzone.PhotoActivity;
import com.example.ricco.qgzone.R;
import com.example.ricco.qgzone.ViewPhotoActivity;
import com.example.ricco.utils.LogUtil;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yason
 * Photo适配器
 */
public class PhotoAdapter extends BaseAdapter {

    private List<Integer> photoList;
    private LayoutInflater inflater;
    private Context context;

    private int albumId;
    private int userId;

    public PhotoAdapter(Context context, List<Integer> photoList,int albumId,int userId){
        this.photoList = photoList;
        inflater = LayoutInflater.from(context);
        this.context = context;

        this.albumId = albumId;
        this.userId = userId;
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //1.获取item中的控件并保存至ViewHolder
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_list_photo, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.item_img = (ImageView) convertView.findViewById(R.id.item_img);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //2.状态重置
        viewHolder.item_img.setImageResource(R.color.greyViewBg);

        //3.加载图片到视图
        int photoId = photoList.get(position);
        String url = Constant.Album.showPhoto + "/" + userId + "/" + albumId + "/" + "t_" + photoId + ".jpg";
        ImageLoader.getInstance(3).loadImage(url, viewHolder.item_img,false);

        //4.设置点击监听事件
        viewHolder.item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPhotoActivity.actionStart(context,photoList.get(position),albumId,userId);
            }
        });

        return convertView;
    }

    public void notifyDataUpdate(List<Integer> photoList) {
        this.photoList = photoList;
        notifyDataSetChanged();
    }

    class ViewHolder{
        public ImageView item_img;
    }
}
