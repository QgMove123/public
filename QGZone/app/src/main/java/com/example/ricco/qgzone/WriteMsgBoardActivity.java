package com.example.ricco.qgzone;

import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by Mr_Do on 2016/8/11.
 */
public class WriteMsgBoardActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_msg_board);
        EditText msgBoard = (EditText) findViewById(R.id.msgboard_edittext);
        msgBoard.requestFocus();
    }
}
