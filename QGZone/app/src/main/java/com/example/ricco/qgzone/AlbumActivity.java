package com.example.ricco.qgzone;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.ricco.adapter.AlbumAdapter;
import com.example.ricco.constant.Constant;
import com.example.ricco.entity.AlbumModel;
import com.example.ricco.entity.JsonModel;
import com.example.ricco.others.ErrorHandling;
import com.example.ricco.others.mPopupWindow;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.StateUtil;
import com.example.ricco.others.TopBar;
import com.example.ricco.utils.ToastUtil;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    private mPopupWindow popup;
    private EditText popup_edi;
    private Button popup_btn_right;
    private ImageView popup_null;

    private int userId;

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
                switch (msg.what){
                    case 0x110:
                    er.setSuccess();
                    initAdapter();
                        break;
                    case 0x111:
                    EnterPrivacyAlbum((AlbumModel) msg.obj);
                        break;
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_album);

        userId = getIntent().getIntExtra("userId",-1);

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

        View root = getLayoutInflater().inflate(R.layout.popup_enter_password, null);
        popup = new mPopupWindow(this, root,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popup_edi = (EditText) root.findViewById(R.id.popup_edi);
        popup_btn_right = (Button) root.findViewById(R.id.popup_btn_right);
        popup_null = (ImageView) root.findViewById(R.id.popup_null);
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
            initItemClickEvent();
        } else {
            albumAdapter.notifyDataUpdate(albumList);
        }
    }

    private void initItemClickEvent() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //4.img点击事件
                ImageView item_img = (ImageView) view.findViewById(R.id.item_img);

                final AlbumModel currentAlbum = albumList.get(position);

                item_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //权限判断，对好友私密
                        if (currentAlbum.getAlbumState() == 1&&userId != Constant.HOST_ID){
                            popup.showAtLocation(findViewById(R.id.parent), Gravity.CENTER,0,0);
                        }else {
                            PhotoActivity.actionStart(AlbumActivity.this, currentAlbum, userId);
                        }
                    }
                });


                //5.popup内按钮点击事件
                popup_btn_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //1.获取密码
                        String password = getPassWord();
                        if(password != null){
                            //2.上传服务器核对
                            AlbumModel am = currentAlbum;
                            am.setAlbumPassword(password);
                            checkPassword(am);
                        }
                    }
                });

                popup_null.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup_edi.setText("");
                    }
                });
            }
        });
    }

    public String getPassWord() {
        String str = popup_edi.getText().toString();
        if (str.matches(Constant.regex)) {
            popup_edi.setText("");
            popup.dismiss();
            return str;
        }else {
            popup_edi.setText("");
            ToastUtil.showLong(this,
                    "请输入5~15位密码,可以包含字母、数字、下划线");
        }
        return null;
    }

    private void checkPassword(final AlbumModel currentAlbum) {
        String amJson = JsonUtil.toJson(currentAlbum);
        String url = null;
        try {
            url = Constant.Album.inspectAlbum + "?jsonObject=" + URLEncoder.encode(amJson,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpUtil.Get(url,new HttpUtil.CallBackListener() {
            @Override
            public void OnFinish(String JsonResult) {
                jResult = JsonResult;
                Message msg = new Message();
                msg.obj = currentAlbum;
                msg.what = 0x111;
                handler.sendMessage(msg);
            }

            @Override
            public void OnError(Exception e) {}
        });
    }

    private void EnterPrivacyAlbum(AlbumModel albumModel) {
        JsonModel<Integer, String> jsonModel = JsonUtil.toModel
                (jResult, new TypeToken<JsonModel<Integer, String>>() {
                }.getType());
        int resultCode = jsonModel.getState();
        boolean flag = StateUtil.albumState(this, resultCode);
        if (flag){
            PhotoActivity.actionStart(AlbumActivity.this, albumModel, userId);
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
