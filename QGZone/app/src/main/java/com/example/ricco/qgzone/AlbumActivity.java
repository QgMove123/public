package com.example.ricco.qgzone;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import com.example.ricco.adapter.AlbumAdapter;
import com.example.ricco.constant.Constant;
import com.example.ricco.entity.AlbumModel;
import com.example.ricco.entity.JsonModel;
import com.example.ricco.others.ErrorHandling;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.StateUtil;
import com.example.ricco.others.TopBar;
import com.google.gson.reflect.TypeToken;
import java.util.List;

/**
 * @author yason
 *         相册列表
 *         1.显示所有相册
 *         1>GridView图文混排
 *         2>BaseAdapter
 *         2.1>根据url加载封面
 *         2.2>将相册名、封面放到视图
 *         3>DataList
 *         3.1>开启子线程访问网络
 *         3.2>获取Json数据，包含相册名、封面url、权限
 *         2.选择相册进入
 *         1>跳转到对应相片列表
 *         3.添加相册
 *         1>跳转到添加相册页面
 *         2>添加成功返回时刷新数据源
 */
public class AlbumActivity extends BaseActivity {

    private TopBar topBar;

    private GridView gridView;
    private List<AlbumModel> albumList;
    private AlbumAdapter albumAdapter;

    private int userId = 1;

    private String jResult;

    private static ErrorHandling er = new ErrorHandling();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                er.handleError(AlbumActivity.this,
                        new ErrorHandling.CallBackListener() {
                    @Override
                    public void onHandle() {
                        updateDatas();
                    }
                });
            } else {
                er.setSuccess();
                initAdapter();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_album);

//        userId = getIntent().getIntExtra("userId",-1);

        initView();

        initDatas();

        initEvent();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateDatas();
    }

    /**
     * 初始化
     */
    private void initView() {
        topBar = (TopBar) findViewById(R.id.topbar);
        if (userId != Constant.HOST_ID) {
            //隐藏按钮
            topBar.setRightIsVisable(false);
        }

        gridView = (GridView) findViewById(R.id.gridview);
    }

    /**
     * 获取数据源
     */
    private void initDatas() {
        String url = Constant.Album.showAlbum + "?userId=" + userId;
        HttpUtil.Get(url, new HttpUtil.CallBackListener() {
            @Override
            public void OnFinish(String JsonResult) {
                jResult = JsonResult;
                handler.sendEmptyMessage(0x110);
            }

            @Override
            public void OnError(Exception e) {
                handler.sendEmptyMessage(0);
            }
        });

    }

    /**
     * 设置点击事件
     */
    private void initEvent() {
        //topBar点击事件
        topBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void LeftClick(View view) {
                finish();
            }

            @Override
            public void RightClick(View view) {
                NewAlbumActivity.actionStar(AlbumActivity.this, null);
            }
        });
    }

    /**
     * 适配器加载数据
     */
    private void initAdapter() {
        JsonModel<AlbumModel, String> jsonModel = JsonUtil.toModel
                (jResult, new TypeToken<JsonModel<AlbumModel, String>>() {
                }.getType());
        int resultCode = jsonModel.getState();
        boolean flag = StateUtil.albumState(AlbumActivity.this, resultCode);
        if (!flag) return;
        albumList = jsonModel.getJsonList();
        if (albumList == null) return;
        if (albumAdapter == null) {
            albumAdapter = new AlbumAdapter(AlbumActivity.this, albumList, userId);
            gridView.setAdapter(albumAdapter);
        } else {
            albumAdapter.notifyDataUpdate(albumList);
        }
    }

    /**
     * 刷新数据
     */
    private void updateDatas() {

        initDatas();

    }

    /**
     * 启动该Activity的方法
     */
    public static void actionStart(Context context, int userId) {
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

}
