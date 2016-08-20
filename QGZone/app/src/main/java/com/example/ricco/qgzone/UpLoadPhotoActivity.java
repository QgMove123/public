package com.example.ricco.qgzone;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import com.example.ricco.adapter.ImageAdapter;
import com.example.ricco.constant.Constant;
import com.example.ricco.entity.JsonModel;
import com.example.ricco.others.TopBar;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.LogUtil;
import com.example.ricco.utils.StateUtil;
import com.example.ricco.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yason
 * 图片上传
 * 1.显示所有图库图片
 *  1>GridView
 *  2>BaseAdapter
 *      2.1>根据路径加载图片
 *      2.2>将图片放到视图中
 *  3>DataList(路径)
 *      3.1>开启子线程遍历图库
 *      3.2>通知UI线程
 *      3.3>适配器加载数据
 * 2.选择图片上传
 *      1>选中item变暗，角标变亮
 *      2>保存选中图片路径
 *      3>上传到服务器
 * 3.通知图片列表更新数据
 */
public class UpLoadPhotoActivity extends BaseActivity {
    private TopBar topBar;
    private Button btn_upload;

    private GridView gridView;
    private List<String> imgList;
    private ImageAdapter imgAdapter;

    private int albumId;

    private String jResult;

    private ProgressDialog progressDialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0x110:
                    initAdapter();
                    break;
                case 0x111:
                    upLoadPhoto();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        albumId = getIntent().
                getIntExtra("albumId",-1);

        initView();

        initDatas();

        initEvent();

    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.topbar);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        gridView = (GridView) findViewById(R.id.gridview);
    }

    private void initDatas() {
        imgList = new ArrayList<>();
        new Thread() {
            @Override
            public void run() {
                Uri imgUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(imgUri, null, null, null, null);
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.
                            getColumnIndex(MediaStore.Images.Media.DATA));
                    imgList.add(path);
                }
                cursor.close();
                handler.sendEmptyMessage(0x110);
            }
        }.start();
    }

    private void initEvent() {
        //topBar点击事件
        topBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void LeftClick(View view) {}

            @Override
            public void RightClick(View view) {
                if (getIntent().getStringExtra("CallForPath").equals("DongTai")) {
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        });
        //上传图片到服务器
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //1.获取选中图片
                List<String> selectedImg = imgAdapter.getSelectedImg();
                //2.1 返回路径
                if (getIntent().getStringExtra("CallForPath").equals("DongTai")){

                    Intent intent = new Intent(UpLoadPhotoActivity.this , TalkPubActivity.class);
                    String allPath = new String("");
                    if(selectedImg.size()<=9) {
                        for (int i = 0; i < selectedImg.size(); i++) {
                            allPath += selectedImg.get(i) + "@";
                        }
                        intent.putExtra("Path", allPath);
                        setResult(RESULT_OK, intent);
                        finish();
                    }else{
                        ToastUtil.showShort(UpLoadPhotoActivity.this,"不能超过九张");
                        return;
                    }
                }
                if (albumId == -1) return;
                //2.2 上传到服务器
                progressDialog = ProgressDialog.
                        show(UpLoadPhotoActivity.this, null, "正在上传");
                String url = Constant.Album.uploadPhoto + "?albumId=" + albumId;
                HttpUtil.Post(url, null, selectedImg, new HttpUtil.CallBackListener() {
                    @Override
                    public void OnFinish(String JsonResult) {
                        jResult = JsonResult;
                        handler.sendEmptyMessage(0x111);
                    }

                    @Override
                    public void OnError(Exception e) {
                    }
                });
            }
        });
    }


    private void initAdapter() {
        imgAdapter = new ImageAdapter(UpLoadPhotoActivity.this, imgList);
        gridView.setAdapter(imgAdapter);
    }

    private void upLoadPhoto() {
        progressDialog.dismiss();
        JsonModel jsonModel = JsonUtil.toObject(jResult, JsonModel.class);
        int state = jsonModel.getState();
        boolean flag = StateUtil.albumState(UpLoadPhotoActivity.this, state);
        if (flag) {
            finish();
        }
    }

    public static void actionStart(Context context,int albumId){
        Intent intent = new Intent(context, UpLoadPhotoActivity.class);
        intent.putExtra("albumId", albumId);
        intent.putExtra("CallForPath", "Error");
        context.startActivity(intent);
    }

}
