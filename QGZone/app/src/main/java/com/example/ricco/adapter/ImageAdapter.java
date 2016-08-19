package com.example.ricco.adapter;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.ricco.others.ImageLoader;
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yason
 * UpLoadPhoto适配器
 */
public class ImageAdapter extends BaseAdapter {

    private  static List<String> selectedImg = new ArrayList<>();
    private List<String> imgList;
    private LayoutInflater inflater;
    private int screenWidth;

    public ImageAdapter(Context context, List<String> imgList){
        this.imgList = imgList;
        inflater = LayoutInflater.from(context);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public Object getItem(int position) {
        return imgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //1.获取item中的控件并保存至ViewHolder
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_upload_photo, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.item_img = (ImageView) convertView.findViewById(R.id.item_img);
            viewHolder.item_btn = (ImageButton) convertView.findViewById(R.id.item_btn);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //2.状态重置
        viewHolder.item_img.setImageResource(R.color.lightBlue);
        viewHolder.item_btn.setImageResource(R.color.lightBlue);
        viewHolder.item_img.setColorFilter(null);

        viewHolder.item_img.setMaxWidth(screenWidth/3);

        //3.加载图片到视图
        ImageLoader.getInstance(3).loadImage(
                imgList.get(position), viewHolder.item_img, true);

        //4.设置点击监听事件
        final String filePath = imgList.get(position);
        viewHolder.item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //已经被选择
                if(selectedImg.contains(filePath)){
                    selectedImg.remove(filePath);
                    viewHolder.item_img.setColorFilter(null);
                    viewHolder.item_btn.setImageResource(R.mipmap.ic_launcher);
                }
                //未被选择
                else{
                    selectedImg.add(filePath);
                    viewHolder.item_img.setColorFilter(Color.parseColor("#77000000"));
                    viewHolder.item_btn.setImageResource(R.mipmap.ic_launcher);
                }

            }
        });

        return convertView;
    }

    public  List<String> getSelectedImg() {
        return selectedImg;
    }

    class ViewHolder{
        public ImageView item_img;
        public ImageButton item_btn;
    }
}
