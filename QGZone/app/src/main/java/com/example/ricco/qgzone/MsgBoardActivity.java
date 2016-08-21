package com.example.ricco.qgzone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ricco.constant.Constant;
import com.example.ricco.others.ShuoshuoListview;
import com.example.ricco.others.TopBar;

/**
 * 留言板
 */
public class MsgBoardActivity extends BaseActivity{
    private int friendId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShuoshuoListview.setHeader(null);
        ShuoshuoListview.setisNote(true);
        friendId = getIntent().getIntExtra("friendId", 0);//第一次启动时获取friendId
        if (savedInstanceState != null) {//保存获取到的friendId
            friendId = savedInstanceState.getInt("friendId");
        }
        if(friendId == 0) return;
        ShuoshuoListview.setShuoshuoURL(Constant.Note.noteofothers+"?userId="+friendId+"&page=");
        setContentView(R.layout.activity_msgeboard);
        TextView to_WriteMsgBoard = (TextView) findViewById(R.id.liuyan_to_write);
        to_WriteMsgBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteMsgBoardActivity.actionStart(MsgBoardActivity.this, friendId);
            }
        });
        TopBar topBar = (TopBar) findViewById(R.id.note_topbar);
        topBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void LeftClick(View view) {
                finish();
            }

            @Override
            public void RightClick(View view) {

            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("friendId",friendId);
    }

    public static void actionStart(Context context, int friendId) {
        Intent intent = new Intent(context, MsgBoardActivity.class);
        intent.putExtra("friendId", friendId);
        context.startActivity(intent);
    }
}
