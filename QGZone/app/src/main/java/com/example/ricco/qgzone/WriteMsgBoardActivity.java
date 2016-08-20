package com.example.ricco.qgzone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import com.example.ricco.constant.Constant;
import com.example.ricco.others.TopBar;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.LogUtil;
import com.example.ricco.utils.ToastUtil;

/**
 * Created by Mr_Do on 2016/8/11.
 */
public class WriteMsgBoardActivity extends BaseActivity {
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int targetId = getIntent().getIntExtra("targetId",0);

        final String url = Constant.Note.addnote + "?targetId="+ targetId + "&note=";
        setContentView(R.layout.activity_write_msg_board);
        final EditText msgBoard = (EditText) findViewById(R.id.msgboard_edittext);
        TopBar topBar = (TopBar) findViewById(R.id.msgboard_topbar);
        topBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void LeftClick(View view) {
                finish();
            }

            @Override
            public void RightClick(View view) {
                LogUtil.e("LIUYAN",url + msgBoard.getText().toString());
                HttpUtil.Get(url + msgBoard.getText().toString(), new HttpUtil.CallBackListener() {
                    @Override
                    public void OnFinish(String JsonResult) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                msgBoard.setText("");
                                ToastUtil.showShort(WriteMsgBoardActivity.this, "发表成功");
                            }
                        });
                    }

                    @Override
                    public void OnError(Exception e) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShort(WriteMsgBoardActivity.this, "发表失败");
                            }
                        });
                    }
                });
            }
        });
    }
    public static void actionStart(Context context, int targetId) {
        Intent intent = new Intent(context, WriteMsgBoardActivity.class);
        intent.putExtra("targetId", targetId);
        context.startActivity(intent);
    }
}
