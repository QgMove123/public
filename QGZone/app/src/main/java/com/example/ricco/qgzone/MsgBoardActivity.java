package com.example.ricco.qgzone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ricco.others.ShuoshuoListview;

/**
 * 留言板
 */
public class MsgBoardActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShuoshuoListview.setHeader(null);
        ShuoshuoListview.setisNote(true);
        String url = getIntent().getStringExtra("friendID");
        ShuoshuoListview.setShuoshuoURL("http://192.168.1.109:8080/QGzone/NoteOfOthers?userId=10000&page=");
        setContentView(R.layout.activity_msgeboard);
        TextView to_WriteMsgBoard = (TextView) findViewById(R.id.liuyan_to_write);
        to_WriteMsgBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MsgBoardActivity.this, WriteMsgBoardActivity.class);
                startActivity(intent);
            }
        });

    }

    public static void actionStart(Context context, int friendId) {
        Intent intent = new Intent(context, MsgBoardActivity.class);
        intent.putExtra("friendId", friendId);
        context.startActivity(intent);
    }
}
