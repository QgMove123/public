package com.example.ricco.qgzone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import com.example.ricco.constant.Constant;
import com.example.ricco.entity.JsonModel;
import com.example.ricco.others.TopBar;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.LogUtil;
import com.example.ricco.utils.ProgressDialogUtil;
import com.example.ricco.utils.ToastUtil;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

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
                view.setEnabled(false);//不允许在发表过程多次点击
                ProgressDialogUtil.showDialog("请稍等","正在加载",false,WriteMsgBoardActivity.this);
                if(!msgBoard.getText().toString().equals("")) {
                    try {
                        HttpUtil.Get(url + URLEncoder.encode(msgBoard.getText().toString(),"utf-8"), new HttpUtil.CallBackListener() {
                            @Override
                            public void OnFinish(String JsonResult) {
                                Map<String, Integer> jsonModel =
                                        JsonUtil.toModel(JsonResult,
                                                new TypeToken<Map<String, Integer>>(){}.getType());
                                if(jsonModel!=null && jsonModel.get("state").intValue() == 501) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            msgBoard.setText("");
                                            ToastUtil.showShort(WriteMsgBoardActivity.this, "发表成功");
                                        }
                                    });
                                }else {
                                    ToastUtil.showShort(WriteMsgBoardActivity.this, "网络异常了");
                                }
                                ProgressDialogUtil.deleteDialog();
                                finish();
                            }

                            @Override
                            public void OnError(Exception e) {//留言板处理失败的异常处理
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showShort(WriteMsgBoardActivity.this, "网络异常了");
                                        ProgressDialogUtil.deleteDialog();
                                        finish();
                                    }
                                });
                            }
                        });
                    } catch (UnsupportedEncodingException e) {
                        msgBoard.setText("");
                        ProgressDialogUtil.deleteDialog();
                        ToastUtil.showShort(WriteMsgBoardActivity.this,"发表失败");
                        view.setEnabled(true);
                    }
                }else{
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
                    msgBoard.setText("");
                    ProgressDialogUtil.deleteDialog();
                    ToastUtil.showShort(WriteMsgBoardActivity.this, "内容不能为空");
                    view.setEnabled(true);
//                        }
//                    });
                }
            }
        });
    }
    public static void actionStart(Context context, int targetId) {
        Intent intent = new Intent(context, WriteMsgBoardActivity.class);
        intent.putExtra("targetId", targetId);
        context.startActivity(intent);
    }
}
