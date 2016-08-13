package com.example.ricco.qgzone;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.example.ricco.utils.ToastUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;


/**
 * 发表说说
 */
public class TalkPubActivity extends BaseActivity {
    public Handler handler;
    private URL url;
    private BitmapFactory.Options options = new BitmapFactory.Options();
    private int mPicNum = 0;
    private Bitmap bitmap;
    private ArrayList<HashMap<String, Object>> mPicGridViewList = new ArrayList<HashMap<String, Object>>();
    private HashMap<String, Object> mPic_Map;
    private GridView mPicGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talkpub);
        EditText talkPub = (EditText) findViewById(R.id.shuos_edittext);
        talkPub.requestFocus();
        mPicGridView = (GridView) findViewById(R.id.talkpub_gridview);
        ImageButton addPic = (ImageButton) findViewById(R.id.shuos_addpic_btn);
        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                int respondcode = 0;
                if(mPicNum<9 && isNetworkAvailable(TalkPubActivity.this)){
                    mPicNum += 1;
                    mPic_Map = new HashMap<String, Object>();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                url = new URL("http://img15.3lian.com/2015/a1/10/d/111.jpg");
                                InputStream is = url.openStream();
                                options.inSampleSize = calculateInSampleSize(options, 120, 120);
                                options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                                bitmap = BitmapFactory.decodeStream(is, null, options);
                                mPic_Map.put("image", bitmap);
                                mPicGridViewList.add(mPic_Map);
                                handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            SimpleAdapter adapter = new SimpleAdapter(TalkPubActivity.this, mPicGridViewList,
                                                    R.layout.gridview_item_layout, new String[]{"image"}, new int[]{R.id.shuos_pic});
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
                                        }
                                    });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else if(mPicNum >= 9){
                    ToastUtil.showShort(TalkPubActivity.this, "图片已经达到上限了~");
                }else if(!isNetworkAvailable(TalkPubActivity.this)){
                    ToastUtil.showShort(TalkPubActivity.this, "网络似乎不给力~");
                }
            }
        });
    }
    public int calculateInSampleSize(BitmapFactory.Options op, int reqWidth,
                                     int reqheight) {
        int originalWidth = op.outWidth;
        int originalHeight = op.outHeight;
        int inSampleSize = 1;
        if (originalWidth > reqWidth || originalHeight > reqheight) {
            int halfWidth = originalWidth / 2;
            int halfHeight = originalHeight / 2;
            while ((halfWidth / inSampleSize > reqWidth)
                    &&(halfHeight / inSampleSize > reqheight)) {
                inSampleSize *= 2;

            }
        }
        return inSampleSize;
    }


 /*
 检查当前网络是否可用
 @param context
 @return*/

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


