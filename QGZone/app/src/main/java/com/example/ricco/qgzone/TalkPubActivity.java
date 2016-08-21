package com.example.ricco.qgzone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.example.ricco.adapter.ImageAdapter;
import com.example.ricco.constant.Constant;
import com.example.ricco.entity.TwitterModel;
import com.example.ricco.others.ImageLoader;
import com.example.ricco.others.TopBar;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.LogUtil;
import com.example.ricco.utils.ProgressDialogUtil;
import com.example.ricco.utils.ToastUtil;
import com.google.gson.reflect.TypeToken;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 发表说说
 */
public class TalkPubActivity extends BaseActivity {
    private String[] imgPath;
    private TwitterModel twitterModel;
    public Handler handler;
    private String url = Constant.TalkPub.sendshuoshuo;
    private int mPicNum = 0;
    private ArrayList<Bitmap> bitmaps;
    private ArrayList<HashMap<String, String>> mPicGridViewList = new ArrayList<HashMap<String, String>>();
    private GridView mPicGridView;
    private ImageButton addPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talkpub);
        final EditText talkPub = (EditText) findViewById(R.id.shuos_edittext);
        mPicGridView = (GridView) findViewById(R.id.talkpub_gridview);
        addPic = (ImageButton) findViewById(R.id.shuos_addpic_btn);


        /*打开图片选择器*/
        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startChoosePicIntent = new Intent(TalkPubActivity.this, UpLoadPhotoActivity.class);
                startChoosePicIntent.putExtra("CallForPath", "DongTai");
                startActivityForResult(startChoosePicIntent, 1);
            }
        });

        /*设置标题栏的监听事件*/
        TopBar shousTitleBar = (TopBar) findViewById(R.id.shuos_title_bar);

        shousTitleBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {

            @Override
            public void LeftClick(View view) {
                finish();
            }

            @Override
            public void RightClick(View view) {//发表说说
                view.setEnabled(false);
                ProgressDialogUtil.showDialog("请稍等","正在加载...",false,TalkPubActivity.this);
                final EditText shuosEditText = (EditText) findViewById(R.id.shuos_edittext);
                ArrayList<String> pathList = new ArrayList<String>();
                for (int i = 0; imgPath != null && i < imgPath.length && imgPath[i] != null; i++)
                    pathList.add(imgPath[i]);
                if(!shuosEditText.getText().toString().equals("") || (imgPath!=null && imgPath.length>0)){
                    HttpUtil.Post(url, shuosEditText.getText().toString(), pathList, new HttpUtil.CallBackListener() {
                        @Override
                        public void OnFinish(String result) {
                            Map<String, Integer> jsonModel =
                                    JsonUtil.toModel(result,
                                            new TypeToken<Map<String, Integer>>(){}.getType());
                            LogUtil.e("SendDongtai", result);
                            if(jsonModel!=null && jsonModel.get("state").intValue() == 201) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showShort(TalkPubActivity.this, "发送成功");
                                        ProgressDialogUtil.deleteDialog();
                                        shuosEditText.setText("");
                                        finish();
                                    }
                                });
                            }else{
                                ToastUtil.showShort(TalkPubActivity.this, "网络异常");
                                ProgressDialogUtil.deleteDialog();
                                shuosEditText.setText("");
                                finish();
                            }
                        }

                        @Override
                        public void OnError(Exception e) {
                            ProgressDialogUtil.deleteDialog();
                            ToastUtil.showShort(TalkPubActivity.this, "网络异常");
                        }
                    });
                }else {
                    ProgressDialogUtil.deleteDialog();
                    view.setEnabled(true);
                    ToastUtil.showShort(TalkPubActivity.this, "内容不能为空");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            //// TODO: 2016/8/20 强制用户点击，待下次优化
            //addPic.setClickable(false);
            /*加载图片到GridView*/
            //1.获取路径
            if(imgPath!=null)for(int i=0 ; i<imgPath.length; i++) imgPath[i] = null;
            if(mPicGridViewList!=null)mPicGridViewList.clear();//// TODO: 2016/8/21  
            if (data.getStringExtra("Path") != null) {
                LogUtil.e("Len", data.getStringExtra("Path"));
                imgPath = data.getStringExtra("Path").split("@");
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; imgPath != null && i < imgPath.length && imgPath[i] != null; i++) {
                    map.put("image", imgPath[i]);
                    mPicGridViewList.add(map);
                }
            } else {
                Log.e(">>>", "false");
            }
            //  2.绑定到GridView
            SimpleAdapter adapter = new SimpleAdapter(TalkPubActivity.this, mPicGridViewList,
                    R.layout.gridview_item_layout, new String[]{"image"}, new int[]{R.id.shuos_pic}) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    convertView = LayoutInflater.from(TalkPubActivity.this).inflate(R.layout.gridview_item_layout, null);
                    ImageView imageView = (ImageView) convertView.findViewById(R.id.shuos_pic);
                    ImageLoader.getInstance(1).loadImage(imgPath[position], imageView, true);
                    return convertView;
                }
            };
            mPicGridView.setAdapter(adapter);
        }

    }
}


