package com.example.ricco.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ricco.entity.NoteCommentModel;
import com.example.ricco.entity.NoteModel;
import com.example.ricco.qgzone.R;

import java.util.List;

/**
 * Created by Mr_Do on 2016/8/13.
 */
public class NoteRespond_Adapter extends ArrayAdapter<NoteCommentModel>{
    private  int resourceId;
    public NoteRespond_Adapter(Context context, int textViewResourceId, List<NoteCommentModel> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        NoteCommentModel respond = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView textView = (TextView) view.findViewById(R.id.respond_text);
        textView.setText(respond.getCommenterName()+" say to "+respond.getTargetName()
                +": "+respond.getComment()+"\r\n            ---"+respond.getTime());
        return view;
    }




}
