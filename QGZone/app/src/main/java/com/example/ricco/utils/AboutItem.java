package com.example.ricco.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ricco.others.CircleImageVIew;
import com.example.ricco.qgzone.R;

/**
 * Created by chenyi on 2016/8/12.
 */
public class AboutItem extends LinearLayout  {

    private CircleImageVIew headPic;
    private TextView name;
    private TextView time;
    private TextView words;

    public AboutItem(Context context) {
        super(context);

        //加载新控件的布局
        LayoutInflater.from(context).inflate(R.layout.item_about_me, this);
        headPic = (CircleImageVIew) findViewById(R.id.friend_pic);
        name = (TextView) findViewById(R.id.friend_name);
        time = (TextView) findViewById(R.id.time);
        words = (TextView) findViewById(R.id.words);
    }


    public void setHeadPic(Bitmap picture) {
        this.headPic.setImageBitmap(picture);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setTime(String time) {
        this.time.setText(time);
    }

    public void setWords(String words) {
        this.words.setText(words);
    }
}
