package com.example.ricco.qgzone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.example.ricco.entity.TwitterModel;
import com.example.ricco.others.TopBar;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.ImageLoader;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.ToastUtil;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;


/**
 * 发表说说
 */
public class TalkPubActivity extends BaseActivity {
    private String[] imgPath = new String[9];
    private Intent mIntent;
    private TwitterModel twitterModel;
    public Handler handler;
    private URL url;
    private int mPicNum = 0;
    private Bitmap bitmap;
    private ArrayList<Bitmap> bitmaps;
    private ArrayList<HashMap<String, Object>> mPicGridViewList = new ArrayList<HashMap<String, Object>>();
    private GridView mPicGridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talkpub);
        final EditText talkPub = (EditText) findViewById(R.id.shuos_edittext);
        talkPub.requestFocus();
        mPicGridView = (GridView) findViewById(R.id.talkpub_gridview);
        ImageButton addPic = (ImageButton) findViewById(R.id.shuos_addpic_btn);
        bitmaps = new ArrayList<Bitmap>();





        /*打开图片选择器*/
        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                mIntent = new Intent(TalkPubActivity.this, UpLoadPhotoActivity.class);
                mIntent.putExtra("Call","DongTai");
                startActivity(mIntent);
            }
        });





        /*获取图片数据并保存*/
        if((mIntent=getIntent())!=null){
            if(mIntent.getStringArrayExtra("Path")!=null) {
                imgPath = mIntent.getStringArrayExtra("Path");
                for (int i = 0; i < 9 && imgPath[i] != null; i++) {
                    bitmap = BitmapFactory.decodeFile(imgPath[i]);
                    bitmaps.add(bitmap);
                    mPicNum ++;
                }
            }
        }









        /*加载图片到GridView*/

        //1.绑定数据
        SimpleAdapter adapter = new SimpleAdapter(TalkPubActivity.this, mPicGridViewList,
                R.layout.gridview_item_layout, new String[]{"image"}, new int[]{R.id.shuos_pic}){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView = (ImageView) convertView.findViewById(R.id.dongtai_picture);
                ImageLoader.getInstance(1).loadImage(imgPath[position],imageView,true);
                return convertView;
            }
        };
        //2.重新计算GridView的高度
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView iv = (ImageView) view;
                    iv.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        mPicGridView.setAdapter(adapter);






        /*设置标题栏的监听事件*/
        TopBar shousTitleBar = (TopBar) findViewById(R.id.shuos_title_bar);

        shousTitleBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {

            @Override
            public void LeftClick(View view) {
                finish();
            }

            @Override
            public void RightClick(View view) {//发表说说
                if(isNetworkAvailable(TalkPubActivity.this)) {
                    EditText shuosEditText = (EditText) findViewById(R.id.shuos_edittext);
                    twitterModel = new TwitterModel();
                    twitterModel.setTwitterWord(shuosEditText.getText().toString());
                    shuosEditText.setText("");
                    shuosEditText.setEnabled(true);
                    twitterModel.setTime(new Date().toString());
                    twitterModel.setTwitterPicture(mPicNum);
                    twitterModel.setTalkerName("aaa");
                    twitterModel.setTalkId(122222);
                    HashMap<String,String> stringHashMap = new HashMap<String, String>();
                    stringHashMap.put("twitterWord",twitterModel.getTwitterWord());
                    HttpUtil.Post("http://192.168.3.16:8080/QGzone/TwitterAdd", stringHashMap, bitmaps, new HttpUtil.CallBackListener() {
                        @Override
                        public void OnFinish(String result) {
                            Log.e("Conn", (String) result);
                            finish();
                        }

                        @Override
                        public void OnError(Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });

    }





    /*检查当前网络是否可用 */
    public boolean isNetworkAvailable(Activity activity) {
        boolean flag = false;
        //得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        return flag;
    }

}


