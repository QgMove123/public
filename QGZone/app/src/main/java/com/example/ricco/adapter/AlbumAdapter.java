package com.example.ricco.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ricco.constant.Constant;
import com.example.ricco.entity.AlbumModel;
import com.example.ricco.entity.JsonModel;
import com.example.ricco.others.mPopupWindow;
import com.example.ricco.qgzone.PhotoActivity;
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.StateUtil;
import com.example.ricco.utils.ToastUtil;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author yason
 * Album适配器
 */
public class AlbumAdapter extends BaseAdapter {

    private List<AlbumModel> albumList;
    private LayoutInflater inflater;
    private Context context;

    private mPopupWindow popup;
    private EditText popup_edi;
    private Button popup_btn_right;
    private Button popup_btn_cancel;

    private int userId;

    private String jRseult;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            EnterPrivacyAlbum((AlbumModel) msg.obj);
        }
    };

    public AlbumAdapter(Context context, List<AlbumModel> albumList,int userId){
        this.albumList = albumList;
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.userId = userId;

        View root = inflater.inflate(R.layout.popup_enter_password, null);
        popup = new mPopupWindow((Activity) context, root,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popup_edi = (EditText) root.findViewById(R.id.popup_edi);
        popup_btn_right = (Button) root.findViewById(R.id.popup_btn_right);
        popup_btn_cancel = (Button) root.findViewById(R.id.popup_btn_cancel);
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int position) {
        return albumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //1.获取item中的控件并保存至ViewHolder
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_list_album, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.item_img = (ImageView) convertView.findViewById(R.id.item_img);
            viewHolder.item_tv = (TextView) convertView.findViewById(R.id.item_tv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //2.状态重置
        viewHolder.item_img.setImageResource(R.mipmap.ic_launcher);
        viewHolder.item_tv.setText("未命名相册");

        //3.加载封面、文件名到视图
        final int albumState = albumList.get(position).getAlbumState();
        viewHolder.item_tv.setText(albumList.get(position).getAlbumName());
        if(albumState == 0){//公开
            viewHolder.item_img.setImageResource(R.mipmap.list_album_public);
        }else if(albumState == 1){//私密
            viewHolder.item_img.setImageResource(R.mipmap.list_album_privacy);
        }


        //4.img点击事件
        final AlbumModel currentAlbum = albumList.get(position);
        viewHolder.item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //权限判断，对好友私密
                if (albumState == 1){
//                    if (albumState == 1&&userId != Constant.myId){
                    Activity act = (Activity)context;
                    popup.showAtLocation(act.findViewById(R.id.parent),Gravity.CENTER,0,0);
                    }else {
                        PhotoActivity.actionStart(context, currentAlbum, userId);
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
                    currentAlbum.setAlbumPassword(password);
                    checkPassword(currentAlbum);
                }
            }
        });

        popup_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_edi.setText("");
                popup.dismiss();
            }
        });

        return convertView;
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
                jRseult = JsonResult;
                Message msg = new Message();
                msg.obj = currentAlbum;
                handler.sendMessage(msg);
            }

            @Override
            public void OnError(Exception e) {}
        });
    }

    public void notifyDataUpdate(List<AlbumModel> albumList) {
        this.albumList = albumList;
        notifyDataSetChanged();
    }

    public String getPassWord() {
        String str = popup_edi.getText().toString();
        if (str.matches(Constant.regex)) {
            popup_edi.setText("");
            popup.dismiss();
            return str;
        }else {
            popup_edi.setText("");
            ToastUtil.showLong(context,
                    "请输入5~15位密码,可以包含字母、数字、下划线");
        }
        return null;
    }

    private void EnterPrivacyAlbum(AlbumModel albumModel) {
        JsonModel<AlbumModel, String> jsonModel = JsonUtil.toModel
                (jRseult, new TypeToken<JsonModel<Integer, String>>() {
                }.getType());
        int resultCode = jsonModel.getState();
        boolean flag = StateUtil.albumState(context, resultCode);
        if (flag){
            PhotoActivity.actionStart(context, albumModel, userId);
        }
    }

    class ViewHolder{
        public ImageView item_img;
        public TextView item_tv;
    }

}
