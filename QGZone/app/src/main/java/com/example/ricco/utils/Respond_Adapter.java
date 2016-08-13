package com.example.ricco.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ricco.entity.TwitterCommentModel;
import com.example.ricco.entity.TwitterModel;
import com.example.ricco.qgzone.R;

import java.util.List;

/**
 * Created by Mr_Do on 2016/8/8.
 * 用于回复列表的适配器
 */
public class Respond_Adapter extends ArrayAdapter<TwitterCommentModel> {
    private  int resourceId;

    public  Respond_Adapter(Context context, int textViewResourceId, List<TwitterCommentModel> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        TwitterCommentModel respond = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView textView = (TextView) view.findViewById(R.id.respond_text);
        textView.setText(respond.getCommenterName()+" say to "+respond.getTargetName()
                +": "+respond.getComment()+respond.getTime());
        return view;
    }
}
