package com.example.ricco.qgzone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 留言板
 */
public class MsgBoardActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public void actionStart(Context context, int friendId) {
        Intent intent = new Intent(context, MsgBoardActivity.class);
        intent.putExtra("friendId", friendId);
        startActivity(intent);
    }
}
