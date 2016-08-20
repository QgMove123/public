package com.example.ricco.others;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.ricco.adapter.ShuoshuoAdapter;
import com.example.ricco.constant.Constant;
import com.example.ricco.entity.JsonModel;
import com.example.ricco.entity.NoteModel;
import com.example.ricco.entity.TwitterModel;
import com.example.ricco.qgzone.R;
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
    private ShuoshuoAdapter itemAdapter;
    private boolean mFlag_Load = true;
    private ListView lv;
    private int mPage = 1;
    private int mItemPosition = 0;
    private SwipeRefreshLayout mSwipe = null;
    private ArrayList<TwitterModel> itemList = new ArrayList<TwitterModel>();
    private ArrayList<NoteModel> noteList = new ArrayList<NoteModel>();
    private ArrayList<ArrayList<HashMap<String, Object>>> mPicGridViewList = new ArrayList<ArrayList<HashMap<String, Object>>>();
    private static boolean isNote;
    private static String shuoshuoURL;
    private static View header = null;

    public ShuoshuoListview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.other_shuoshuo_layout, this);
        // 初始化
        mSwipe = (SwipeRefreshLayout) findViewById(R.id.dongtai_swipe);
        lv = (ListView) findViewById(R.id.lv);
        if (itemList != null && itemList.size() > 0)
            itemList.clear();
        else if (noteList != null && noteList.size() > 0)
            noteList.clear();
        if (itemAdapter != null && !itemAdapter.isEmpty()) {
            itemAdapter.notifyDataSetChanged();
        }
        initView();
        loadData();//加载数据
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
                    mpdialog.setCancelable(true);                  //设置进度条是否可以取消
                    mpdialog.show();                            //显示进度条
                    break;
                }
                case 2: {
                    mPage = 1;
                    mItemPosition = 1;
                    if (itemList.size() > 0) itemList.clear();
                    else if (noteList.size() > 0) noteList.clear();
                    loadData();
                    break;
                }
                case 3: {
                    mItemPosition -= 1;//删除说说或留言
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
                case 7: {
                    int position = msg.getData().getInt("position");
                    if (itemList != null && itemList.size() > 0) itemList.remove(position);
                    else noteList.remove(position);
                    itemAdapter.notifyDataSetChanged();
                    handler.sendEmptyMessage(3);
                    handler.sendEmptyMessage(5);
                    break;
                }
                case 8: {
                    itemAdapter.notifyDataSetChanged();
                    break;
                }
                case 9:
                    lv.setAdapter(itemAdapter);
                    ToastUtil.showShort(mContext, "没有可以显示的说说呢~");
                    break;
                case 10:
                    ToastUtil.showShort(mContext,"网络异常了");
                    break;
                default:break;
            }
        }
    };


    public static void setShuoshuoURL(String url) {
        shuoshuoURL = url;
    }

    public static void setHeader(View headerview) {
        header = headerview;
    }

    public static void setisNote(boolean bool) {
        if (bool) isNote = true;
        else isNote = false;
    }

    private void initView() {
        if (header != null && lv.getHeaderViewsCount() == 0) lv.addHeaderView(header);
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {//上拉下载
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1 && mFlag_Load) {
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
                if (itemList != null && itemList.size() > 0) itemList.clear();
                else if (noteList != null && noteList.size() > 0) noteList.clear();
                if (itemAdapter != null && !itemAdapter.isEmpty()) {
                    itemAdapter.notifyDataSetChanged();
                }
                if (header != null && lv.getHeaderViewsCount() == 0) lv.addHeaderView(header);
                mPicGridViewList.clear();
                mItemPosition = 0;
                mPage = 1;
                loadData();
            }
        });
    }


    private void loadData() {

        handler.sendEmptyMessage(1);
        Log.e("URL-2", shuoshuoURL + mPage);
        HttpUtil.Get(shuoshuoURL + mPage, new HttpUtil.CallBackListener() {
            @Override
            public void OnFinish(String result) {
                if (!isNote) {
                    final JsonModel<TwitterModel, TwitterModel> jsonModel = JsonUtil.toModel((String) result, new TypeToken<JsonModel<TwitterModel, TwitterModel>>() {
                    }.getType());
                    if (jsonModel.getState() == 201 && jsonModel.getJsonList().size() != 0) {
                        Log.e("Page", mPage + "");
                        itemList.addAll(jsonModel.getJsonList());
                        for (; mItemPosition < itemList.size(); mItemPosition++) {

                            ArrayList<HashMap<String, Object>> pics = new ArrayList<HashMap<String, Object>>();
                            for (int j = 0; j < itemList.get(mItemPosition).getTwitterPicture(); j++) {
                                try {
                                    Log.e("twitterpicture", itemList.get(mItemPosition).getTwitterPicture() + "  " + (mItemPosition - 12 * (mPage - 1)) + "  " + mItemPosition + "  " + (mPage - 1));
                                    URL url = new URL(Constant.TalkPub.getshuoshuopic + jsonModel.getJsonList().get(mItemPosition - 12 * (mPage - 1)).getTwitterId() + "_" + (j + 1) + ".jpg");//说说图片
                                    Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                                    Log.e("fileSize", bitmap.getByteCount() + "");
                                    if (bitmap != null) {
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
                            if (pics != null) mPicGridViewList.add(pics);
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mPage == 1) {
                                    itemAdapter = new ShuoshuoAdapter(itemList, mContext, mPicGridViewList, handler);
//                                    ListView lv = (ListView) findViewById(R.id.lv);
                                    lv.setAdapter(itemAdapter);
                                    handler.sendEmptyMessage(0);
                                    handler.sendEmptyMessage(5);
                                } else {
                                    itemAdapter.notifyDataSetChanged();
                                    handler.sendEmptyMessage(5);
                                }

                                mPage += 1;//页数加一
                                mFlag_Load = true;//滑动可监听
                            }
                        });
                    } else if (jsonModel.getState() == 201 && jsonModel.getJsonList().size() == 0) {

                        if (itemList.size() == 0) {
                            itemAdapter = new ShuoshuoAdapter(itemList, mContext, mPicGridViewList, handler);
                            handler.sendEmptyMessage(9);
                        } else {
                            handler.sendEmptyMessage(4);
                        }
                        //结束progress dialog
                        handler.sendEmptyMessage(5);
                    }
                } else {
                    final JsonModel<NoteModel, NoteModel> jsonModel = JsonUtil.toModel((String) result, new TypeToken<JsonModel<NoteModel, NoteModel>>() {
                    }.getType());
                    if (jsonModel.getState() == 501 && jsonModel.getJsonList().size() != 0) {
                        Log.e("Page", mPage + "");
                        noteList.addAll(jsonModel.getJsonList());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mPage == 1) {
                                    Log.e("BOOL", "TRUE");
                                    itemAdapter = new ShuoshuoAdapter(noteList, mContext, handler);
                                    // ListView lv = (ListView) findViewById(R.id.lv);
                                    lv.setAdapter(itemAdapter);
                                    handler.sendEmptyMessage(5);
                                } else {
                                    itemAdapter.notifyDataSetChanged();
                                    handler.sendEmptyMessage(5);
                                }
                                mPage += 1;//页数加一
                                mFlag_Load = true;//滑动可监听
                                handler.sendEmptyMessage(0);
                            }
                        });
                    } else if (jsonModel.getState() == 501 && jsonModel.getJsonList().size() == 0) {
                        handler.sendEmptyMessage(5);
                        handler.sendEmptyMessage(4);
                        handler.sendEmptyMessage(0);
                    } else if (jsonModel.getState() == 506) handler.sendEmptyMessage(5);
                }
            }

            @Override
            public void OnError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
