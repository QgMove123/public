package com.example.ricco.others;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.ricco.adapter.Item_Adapter;
import com.example.ricco.entity.JsonModel;
import com.example.ricco.entity.TwitterModel;
import com.example.ricco.qgzone.MsgBoardActivity;
import com.example.ricco.qgzone.R;
import com.example.ricco.qgzone.TalkPubActivity;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.ToastUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mr_Do on 2016/8/17.
 */
public class ShuoshuoListview extends RelativeLayout {

    private Context mContext;
    private ProgressDialog mpdialog;
    public static Item_Adapter itemAdapter;
    private boolean mFlag_Load = true;
    private ListView lv;
    private int id = 10000;
    private int mPage=1;
    private int mItemPosition = 0;
    private SwipeRefreshLayout mSwipe = null;
    public static ArrayList<TwitterModel> itemList = new ArrayList<TwitterModel>();
    public static ArrayList<TwitterModel> itemListCompare = new ArrayList<TwitterModel>();
    private ArrayList<ArrayList<HashMap<String, Object>>> mPicGridViewList = new ArrayList<ArrayList<HashMap<String, Object>>>();
    private static String shuoshuoURL;
    private static View header = null;

    public ShuoshuoListview(Context context, AttributeSet attrs) {
        super(context,attrs);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.other_shuoshuo_layout,this);
        // 初始化
        mSwipe = (SwipeRefreshLayout) findViewById(R.id.dongtai_swipe);
        lv = (ListView) findViewById(R.id.lv);
        initView();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    mSwipe.setRefreshing(false);
                    break;
                }
                case 1: {
                    mpdialog = new ProgressDialog(mContext);
                    //设置圆形进度条风格
                    mpdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mpdialog.setTitle("请稍等");                       //设置标题
                    mpdialog.setMessage("正在加载...");               //设置内容
                    mpdialog.setIndeterminate(false);              //设置进度条是否可以不明确
                    mpdialog.setCancelable(false);                  //设置进度条是否可以取消
                    mpdialog.show();                            //显示进度条
                    break;
                }
                case 2: {
                    mPage = 0;
                    mItemPosition = 1;
                    itemList.clear();
                    loadData();
                    break;
                }
                case 3: {
                    mItemPosition -= 1;//删除说说
                    break;
                }
                case 4: {
                    ToastUtil.showShort(mContext, "已经是最后一条啦～");
                    break;
                }
                case 5: {
                    mpdialog.dismiss();
                    break;
                }
                case 6: {
                    ToastUtil.showShort(mContext, "这条回复不是你的哈～");
                    break;
                }
            }
        }
    };


    public static void setShuoshuoURL(String url){
        shuoshuoURL = url;
    }

    public static void setHeader(View headerview){
        header = headerview;
    }


    private void initView()
    {
        if(header!=null) lv.addHeaderView(header);

        loadData();//加载数据
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {//上拉下载
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() -1  && mFlag_Load) {
                        loadData();
                        mFlag_Load = false;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        // 下拉刷新
        mSwipe.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_red_light);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemList.clear();
                mPicGridViewList.clear();
                mItemPosition = 0;
                mPage = 1;
                loadData();
            }
        });
    }



    private void loadData(){

        handler.sendEmptyMessage(1);
        Log.e("URL-2", shuoshuoURL+mPage);
        HttpUtil.Get(shuoshuoURL+mPage, new HttpUtil.CallBackListener() {
            @Override
            public void OnFinish(String result) {
                final JsonModel<TwitterModel,TwitterModel> jsonModel = JsonUtil.toModel((String) result,new TypeToken<JsonModel<TwitterModel,TwitterModel>>(){}.getType());
                if(jsonModel.getState() == 201 && jsonModel.getJsonList().size()!=0){
                    Log.e("Page",mPage+"");
                    itemList.addAll(jsonModel.getJsonList());
                    for(;mItemPosition<itemList.size();mItemPosition++){

                        ArrayList<HashMap<String, Object>> pics = new ArrayList<HashMap<String, Object>>();
                        for(int j=0;j<itemList.get(mItemPosition).getTwitterPicture();j++){
                            try {
                                Log.e("twitterpicture",itemList.get(mItemPosition).getTwitterPicture()+"  "+(mItemPosition-12*(mPage-1))+"  "+mItemPosition+"  "+(mPage-1));
                                URL url = new URL("http://192.168.3.16:8080/QGzone/twitterPhotos/_"+jsonModel.getJsonList().get(mItemPosition-12*(mPage-1)).getTwitterId()+"_"+(j+1)+".jpg");
                                Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                                Log.e("fileSize",bitmap.getByteCount()+"");
                                if(bitmap!=null) {
                                    HashMap<String, Object> pic_Map = new HashMap<String, Object>();
                                    pic_Map.put("image", bitmap);
                                    pics.add(pic_Map);
                                }
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if(pics!=null)mPicGridViewList.add(pics);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(lv.getAdapter() == null) {
                                itemAdapter = new Item_Adapter(itemList, mContext, mPicGridViewList,id,handler);
                                ListView lv = (ListView) findViewById(R.id.lv);
                                lv.setAdapter(itemAdapter);
                                handler.sendEmptyMessage(5);
                            }else{
                                itemAdapter.notifyDataSetChanged();
                                handler.sendEmptyMessage(5);
                            }

                            mPage+=1;//页数加一
                            mFlag_Load=true;//滑动可监听
                            handler.sendEmptyMessage(0);

                        }
                    });
                }else if(jsonModel.getState() == 201 && jsonModel.getJsonList().size()==0){
                    handler.sendEmptyMessage(5);
                    handler.sendEmptyMessage(4);
                }
            }
            @Override
            public void OnError(Exception e) {
                e.printStackTrace();
            }
        });
    }

}
