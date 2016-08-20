package com.example.ricco.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ricco.constant.Constant;
import com.example.ricco.entity.AlbumModel;
import com.example.ricco.entity.JsonModel;
import com.example.ricco.others.mPopupWindow;
import com.example.ricco.qgzone.PhotoActivity;
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.LogUtil;
import com.example.ricco.utils.StateUtil;
import com.example.ricco.utils.ToastUtil;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author yason
 * Album适配器
 */
public class AlbumAdapter extends BaseAdapter {

    private List<AlbumModel> albumList;
    private LayoutInflater inflater;
    private Context context;



    private int userId;

    private String jRseult;



    public AlbumAdapter(Context context, List<AlbumModel> albumList,int userId){
        this.albumList = albumList;
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.userId = userId;

    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int position) {
        return albumList.get(position);
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
            convertView = inflater.inflate(R.layout.item_list_album, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.item_img = (ImageView) convertView.findViewById(R.id.item_img);
            viewHolder.item_tv = (TextView) convertView.findViewById(R.id.item_tv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //2.状态重置
        viewHolder.item_img.setImageResource(R.color.greyViewBg);
        viewHolder.item_tv.setText("未命名相册");

        //3.加载封面、文件名到视图
        int albumState = albumList.get(position).getAlbumState();
        viewHolder.item_tv.setText(albumList.get(position).getAlbumName());
        if(albumState == 0){//公开
            viewHolder.item_img.setImageResource(R.mipmap.list_album_public);
        }else if(albumState == 1){//私密
            viewHolder.item_img.setImageResource(R.mipmap.list_album_privacy);
        }




        return convertView;
    }



    public void notifyDataUpdate(List<AlbumModel> albumList) {
        this.albumList = albumList;
        notifyDataSetChanged();
    }





    class ViewHolder{
        public ImageView item_img;
        public TextView item_tv;
    }

}
