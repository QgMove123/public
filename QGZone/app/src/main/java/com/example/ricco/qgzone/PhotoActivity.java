package com.example.ricco.qgzone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import com.example.ricco.adapter.PhotoAdapter;
import com.example.ricco.constant.Constant;
import com.example.ricco.entity.AlbumModel;
import com.example.ricco.entity.JsonModel;
import com.example.ricco.others.ErrorHandling;
import com.example.ricco.others.TopBar;
import com.example.ricco.others.mPopupWindow;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.StateUtil;
import com.google.gson.reflect.TypeToken;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author yason
 *         相片列表
 *         1.显示此相册下所有相片
 *         1>GridView
 *         2>BaseAdapter
 *         1>根据url加载图片
 *         2>将图片放到视图
 *         3>DataList
 *         1>开启子线程访问网络(在相册列表页执行)
 *         2>Json数据，包含所有图片url
 *         2.点击相片显示原图
 *         1>页面跳转，显示原图
 */
public class PhotoActivity extends BaseActivity {

    private TopBar topBar;

    private GridView gridView;
    private PhotoAdapter photoAdapter;
    private List<Integer> photoList;

    private mPopupWindow popup;
    private Button popup_btn_upload;
    private Button popup_btn_edi;
    private Button popup_btn_clear;
    private Button popup_btn_delete;

    private AlbumModel albumModel;
    private int userId;

    private static ErrorHandling er = new ErrorHandling();

    private ProgressDialog progressDialog;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){
                er.handleError(PhotoActivity.this, new ErrorHandling.CallBackListener() {
                    @Override
                    public void onHandle() {
                        updateDatas();
                    }
                });
            }else{
                er.setSuccess();
                popup.dismiss();
                String jResult = (String) msg.obj;
                int what = msg.what;
                switch (what){
                    case 0x110:
                        initAdapter(jResult);
                        break;
                    case 0x111:
                        clearAlbum(jResult);
                        break;
                    case 0x112:
                        deleteAlbum(jResult);
                        break;
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_photo);

        userId = getIntent().getIntExtra("userId", -1);
        albumModel = (AlbumModel) getIntent().getSerializableExtra("albumModel");
        
        initView();

        initDatas();

        initEvent();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateDatas();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.topbar);
        if (userId != Constant.HOST_ID) {
            //隐藏按钮
            topBar.setRightIsVisable(false);
        }
        
        gridView = (GridView) findViewById(R.id.gridview);

        View root = getLayoutInflater().inflate(R.layout.popup_choose_item, null);
        popup = new mPopupWindow(PhotoActivity.this, root,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popup_btn_upload = (Button) root.findViewById(R.id.popup_btn_upload);
        popup_btn_edi = (Button) root.findViewById(R.id.popup_btn_edi);
        popup_btn_clear = (Button) root.findViewById(R.id.popup_btn_clear);
        popup_btn_delete = (Button) root.findViewById(R.id.popup_btn_delete);
        
    }

    private void initDatas() {
        if(progressDialog == null){
            progressDialog = ProgressDialog.show(this, null, "正在加载...");
        }
        int ablumState = albumModel.getAlbumState();
        String url = null;
        if(ablumState == 0){
            url = Constant.Album.checkPublicAlbum +
                    "?albumId=" + albumModel.getAlbumId();
        }else if(ablumState == 1){
            try {
                url = Constant.Album.checkPrivacyAlbum +
                        "?jsonObject=" + URLEncoder.encode(JsonUtil.toJson(albumModel),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        HttpUtil.Get(url,new HttpUtil.CallBackListener() {
            @Override
            public void OnFinish(String JsonResult) {
                Message msg = new Message();
                msg.obj = JsonResult;
                msg.what = 0x110;
                handler.sendMessage(msg);
            }

            @Override
            public void OnError(Exception e) {
                handler.sendEmptyMessage(0);
            }
        });

    }

    private void initEvent() {
        //topBar点击事件
        topBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void LeftClick(View view) {
                finish();
            }

            @Override
            public void RightClick(View view) {
                popup.showAsDropDown(view);
            }
        });

        //popup内按钮点击事件
        popup_btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpLoadPhotoActivity.actionStart(PhotoActivity.this,albumModel.getAlbumId());
            }
        });

        popup_btn_edi.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  NewAlbumActivity.actionStar(PhotoActivity.this, albumModel);
              }
        });

        popup_btn_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = Constant.Album.clearAlbum + "?albumId=" + albumModel.getAlbumId();
                    HttpUtil.Get(url, new HttpUtil.CallBackListener() {
                        @Override
                        public void OnFinish(String JsonResult) {
                            Message msg = new Message();
                            msg.obj = JsonResult;
                            msg.what = 0x111;
                            handler.sendMessage(msg);
                        }

                        @Override
                        public void OnError(Exception e) {
                            handler.sendEmptyMessage(0);
                        }
                    });
                }
        });

        popup_btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = Constant.Album.deleteAlbum + "?albumId=" + albumModel.getAlbumId();
                    HttpUtil.Get(url, new HttpUtil.CallBackListener() {
                        @Override
                        public void OnFinish(String JsonResult) {
                            Message msg = new Message();
                            msg.obj = JsonResult;
                            msg.what = 0x112;
                            handler.sendMessage(msg);
                        }

                        @Override
                        public void OnError(Exception e) {
                            handler.sendEmptyMessage(0);
                        }
                    });
                }
        });

    }

    public void updateDatas(){

        initDatas();

    }

    private void initAdapter(String jResult) {
        progressDialog.dismiss();
        JsonModel<Integer,String> jsonModel = JsonUtil.toModel
                (jResult,new TypeToken<JsonModel<Integer,String>>(){}.getType());
        int resultCode = jsonModel.getState();
        boolean flag =  StateUtil.albumState(PhotoActivity.this, resultCode);
        if(!flag) return;
        photoList = jsonModel.getJsonList();
        if(photoList == null)   return;
        if(photoAdapter == null){
            photoAdapter = new PhotoAdapter(PhotoActivity.this, photoList, albumModel.getAlbumId(), userId);
            gridView.setAdapter(photoAdapter);
        }
        else {
            photoAdapter.notifyDataUpdate(photoList);
        }
    }

    private void clearAlbum(String jResult) {
        JsonModel jsonModel = JsonUtil.toObject(jResult,JsonModel.class);
        int resultCode = jsonModel.getState();
        boolean flag = StateUtil.albumState(PhotoActivity.this, resultCode);
        if(flag){
            updateDatas();
        }
    }

    private void deleteAlbum(String jResult) {
        JsonModel jsonModel = JsonUtil.toObject(jResult,JsonModel.class);
        int resultCode = jsonModel.getState();
        boolean flag = StateUtil.albumState(PhotoActivity.this, resultCode);
        if(flag){
            finish();
        }
    }

    public static void actionStart(Context context, AlbumModel albumModel, int userId) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("albumModel", albumModel);
        context.startActivity(intent);
    }

}
