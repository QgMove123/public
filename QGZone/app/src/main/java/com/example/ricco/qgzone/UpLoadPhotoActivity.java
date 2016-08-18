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
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.example.ricco.adapter.ImageAdapter;
import com.example.ricco.others.TopBar;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private Set<String> selectedImg;
    private String[] imgPath = new String[9];//用于返回给动态页的Path
    private ImageAdapter imgAdapter;

    private ProgressDialog progressDialog;

    private Intent mIntent;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            imgAdapter = new ImageAdapter(UpLoadPhotoActivity.this, imgList);
            gridView.setAdapter(imgAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        initView();

        initDatas();

        initEvent();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        topBar = (TopBar) findViewById(R.id.topbar);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        gridView = (GridView) findViewById(R.id.gridview);
    }

    /**
     * 准备数据源
     */
    private void initDatas() {
        progressDialog = ProgressDialog.show(this, null, "正在加载...");
        new Thread() {
            @Override
            public void run() {

                Uri imgUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(imgUri, null, null, null, null);
                imgList = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    imgList.add(path);
                }
                cursor.close();
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    /**
     * 设置事件监听
     */
    private void initEvent() {
        //topBar点击事件
        topBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void LeftClick(View view) {}

            @Override
            public void RightClick(View view) {
                finish();
            }
        });
        //上传图片到服务器
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.获取选中图片
                selectedImg = imgAdapter.getSelectedImg();
                //保存图片
                for(int i=0; i<9&&i<selectedImg.toArray().length&&selectedImg.toArray()[i]!=null; i++)
                    imgPath[i] = (String) selectedImg.toArray()[i];
                //2.上传到服务器或返回给说说发表页
                if((mIntent=getIntent())!=null){
                    if(mIntent.getStringExtra("Call").equals("DongTai")){
                        mIntent = new Intent(UpLoadPhotoActivity.this, TalkPubActivity.class);
                        mIntent.putExtra("Path",imgPath);
                        startActivity(mIntent);
                    }
                }
            }
        });
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, UpLoadPhotoActivity.class);
        context.startActivity(intent);
    }
}
