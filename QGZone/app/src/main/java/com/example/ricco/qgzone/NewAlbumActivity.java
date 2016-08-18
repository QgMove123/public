package com.example.ricco.qgzone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.ricco.constant.Constant;
import com.example.ricco.entity.AlbumModel;
import com.example.ricco.entity.JsonModel;
import com.example.ricco.others.ErrorHandling;
import com.example.ricco.others.mPopupWindow;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.LogUtil;
import com.example.ricco.utils.StateUtil;
import com.example.ricco.utils.ToastUtil;
import com.example.ricco.others.TopBar;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author yason
 * 新建相册&编辑相册
 * 1.填写名称、设置权限
 * 2.将相册信息上传服务器核对
 * 3.返回相册列表页并刷新(broadcastReceiver)
 */
public class NewAlbumActivity extends BaseActivity{

    private TopBar topBar;
    private EditText edi_name;

    private RadioGroup radioGroup;
    private RadioButton rb_private;
    private RadioButton rb_public;

    private mPopupWindow popup;
    private EditText popup_edi;
    private Button popup_btn_right;
    private Button popup_btn_cancel;

    //新建相册名、密码、权限
    private String name;
    private String permit = "";
    private int albumState = 0;
    private int oldAlbumId;

    private String jResult;

    private boolean ediMode = false;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            createAlbum();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_album);

        initView();

        initEvent();

        AlbumModel oldAlbum = (AlbumModel) getIntent().
                getSerializableExtra("oldAlbum");
        if (oldAlbum != null) {
            ediMode = true;
            editAlbum(oldAlbum);
        }

    }

    private void editAlbum(AlbumModel oldAlbum) {
        topBar.setTitle("编辑相册");
        topBar.setLeftText("取消");
        topBar.setRightText("保存");
        oldAlbumId = oldAlbum.getAlbumId();
        edi_name.setText(oldAlbum.getAlbumName());
        if(oldAlbum.getAlbumState() == 1){
            rb_private.toggle();
            popup_edi.setText(oldAlbum.getAlbumPassword());
        }
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.topbar);
        edi_name = (EditText) findViewById(R.id.edi_name);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        rb_private = (RadioButton) radioGroup.findViewById(R.id.rb_private);
        rb_public = (RadioButton) radioGroup.findViewById(R.id.rb_public);

        View root = getLayoutInflater().inflate(R.layout.popup_enter_password, null);
        popup = new mPopupWindow(this, root,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popup_edi = (EditText) root.findViewById(R.id.popup_edi);
        popup_btn_right = (Button) root.findViewById(R.id.popup_btn_right);
        popup_btn_cancel = (Button) root.findViewById(R.id.popup_btn_cancel);
    }

    private void initEvent() {
        //1.topbar点击事件
        topBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void LeftClick(View view) {
                finish();
            }

            @Override
            public void RightClick(View view) {
                name = edi_name.getText().toString();
                AlbumModel albumModel = new AlbumModel();
                albumModel.setAlbumName(name);
                albumModel.setAlbumPassword(permit);
                albumModel.setAlbumState(albumState);
                if(ediMode){
                    albumModel.setAlbumId(oldAlbumId);
                }
                String amJson = null;
                try {
                    amJson = URLEncoder.encode(JsonUtil.toJson(albumModel),"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String url;
                if(ediMode){
                    url = Constant.Album.alterAlbumInfo + "?jsonObject=" + amJson;
                }else{
                    url = Constant.Album.createAlbum + "?album=" + amJson;
                }
                HttpUtil.Get(url, new HttpUtil.CallBackListener() {
                    @Override
                    public void OnFinish(String JsonResult) {
                        jResult = JsonResult;
                        handler.sendEmptyMessage(0x110);
                    }

                    @Override
                    public void OnError(Exception e) {
                    }
                });
            }
        });

        //2.radioGroup点击事件
        rb_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permit = "";
            }
        });

        rb_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0, 0);
            }
        });

        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if(permit == null||permit == ""){
                    rb_public.toggle();
                }
                Window window = NewAlbumActivity.this.getWindow();
                WindowManager.LayoutParams Ip = window.getAttributes();
                Ip.alpha = 1.0f;
                window.setAttributes(Ip);
            }
        });

        //3.popup内按钮点击事件
        popup_btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = popup_edi.getText().toString();
                if (str.matches(Constant.regex)) {
                    albumState = 1;
                    permit = str;
                    popup.dismiss();
                } else {
                    popup_edi.setText("");
                    ToastUtil.showLong(NewAlbumActivity.this,
                            "请输入5~15位密码,可以包含字母、数字、下划线");
                }
            }
        });

        popup_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                //权限还原为公开
                if(popup_edi.getText().toString().equals("")){
                    rb_public.toggle();
                }
            }
        });

    }


    private void createAlbum() {
        JsonModel<String,AlbumModel> jsonModel = JsonUtil.toModel
                (jResult,new TypeToken<JsonModel<String,AlbumModel>>(){}.getType());
        int resultCode = jsonModel.getState();
        boolean flag = StateUtil.albumState(NewAlbumActivity.this, resultCode);
        if(flag) AlbumActivity.actionStart(this,Constant.HOST_ID);
    }

    public static void actionStar(Context context,AlbumModel oldAlbum) {
        Intent intent = new Intent(context, NewAlbumActivity.class);
        intent.putExtra("oldAlbum", oldAlbum);
        context.startActivity(intent);
    }

}
