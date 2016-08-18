package com.example.ricco.qgzone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.ricco.constant.Constant;
import com.example.ricco.entity.JsonModel;
import com.example.ricco.entity.PhotoModel;
import com.example.ricco.others.ErrorHandling;
import com.example.ricco.others.ImageLoader;
import com.example.ricco.others.mPopupWindow;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.LogUtil;
import com.example.ricco.utils.StateUtil;

/**
 * @author yason
 *         显示原图
 *         1.加载原图到imageV
 */
public class ViewPhotoActivity extends BaseActivity {

    private ImageView viewImg;
    private ImageButton viewBtn;

    private PopupWindow popup;
    private Button popup_btn_delete;
    private Button popup_btn_cancel;

    private int userId;
    private int albumId;
    private int photoId;

    private String jResult;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            deletePhoto();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        userId = getIntent().getIntExtra("userId", -1);
        albumId = getIntent().getIntExtra("albumId", -1);
        photoId = getIntent().getIntExtra("photoId", -1);

        initView();

        initDatas();

        initEvent();


    }

    private void initView() {
        View root = getLayoutInflater().inflate(R.layout.popup_delete_photo, null);
        popup = new mPopupWindow(ViewPhotoActivity.this, root,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popup_btn_delete = (Button) root.findViewById(R.id.popup_btn_delete);
        popup_btn_cancel = (Button) root.findViewById(R.id.popup_btn_cancel);

        viewImg = (ImageView) findViewById(R.id.view_img);

        viewBtn = (ImageButton) findViewById(R.id.view_btn);
        if (userId != Constant.MY_ID) {
            //隐藏按钮
            viewBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void initDatas() {
        String url = Constant.Album.showPhoto + "/" + userId + "/" + albumId + "/" + photoId + ".jpg";
        ImageLoader.getInstance(1).loadImage(url, viewImg, false);
    }

    private void initEvent() {
        viewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.showAtLocation(findViewById(R.id.parent), Gravity.BOTTOM, 0, 0);
            }

        });

        popup_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoModel photoModel = new PhotoModel();
                photoModel.setAlbumId(albumId);
                photoModel.setPhotoId(photoId);
                String pmJson = JsonUtil.toJson(photoModel);
                String url = Constant.Album.deletePhoto + "?photo=" + pmJson;
                LogUtil.e("tag",url);
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
        });

        popup_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }

    private void deletePhoto() {
        popup.dismiss();
        JsonModel jsonModel = JsonUtil.
                toObject(jResult, JsonModel.class);
        int resultCode = jsonModel.getState();
        boolean flag = StateUtil.albumState(ViewPhotoActivity.this, resultCode);
        if (flag) {
            finish();
        }
    }

    public static void actionStart(Context context, int photoId, int albumId, int userId) {
        Intent intent = new Intent(context, ViewPhotoActivity.class);
        intent.putExtra("photoId", photoId);
        intent.putExtra("albumId", albumId);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }
}
