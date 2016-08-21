package com.example.ricco.qgzone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.ricco.adapter.NoteRespond_Adapter;
import com.example.ricco.adapter.Respond_Adapter;
import com.example.ricco.constant.Constant;
import com.example.ricco.entity.NoteModel;
import com.example.ricco.entity.TwitterModel;
import com.example.ricco.others.CircleImageVIew;
import com.example.ricco.others.ImageLoader;
import com.example.ricco.others.TopBar;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.TalkPicGridView;
import com.example.ricco.utils.TalkRespondListView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenyi on 2016/8/19.
 */
public class DetailsActivity extends Activity {

    TopBar tb;
    CircleImageVIew userPic;
    TextView userName;
    TextView userTime;
    TextView userWord;
    TalkRespondListView talkRespondListView;
    TalkPicGridView talkPicGridView;
    List<Map<String, Bitmap>> listems;
    SimpleAdapter sAdapter;

    String name;
    String time;
    String word;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_about);

        //初始化控件
        initView();

        //通过传入的信息判断界面的布局
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String model = intent.getStringExtra("model");
        if (type.equals("na") || type.equals("nc")) {
            //留言板动态
            //获取从与我相关获得的留言
            NoteModel noteModel = JsonUtil.toObject(model, NoteModel.class);
            //设置留言板评论的适配器
            NoteRespond_Adapter adapter = new NoteRespond_Adapter(this,
                    R.layout.dongtai_respond_item, noteModel.getComment());
            talkRespondListView.setAdapter(adapter);
            //取得留言板的主要信息
            name = noteModel.getNoteManName();
            time = noteModel.getTime();
            word = noteModel.getNote();
            id = noteModel.getNoteManId();
        } else {
            //说说动态
            //获取从与我相关获得的说说
            TwitterModel twitterModel = JsonUtil.toObject(model, TwitterModel.class);
            //设置说说评论的适配器
            Respond_Adapter adapter = new Respond_Adapter(this,
                    R.layout.dongtai_respond_item, twitterModel.getComment());
            talkRespondListView.setAdapter(adapter);
            listems = new ArrayList<>();
            //设置说说图片的GridView
            sAdapter = new SimpleAdapter(this, listems,
                    R.layout.gridview_item_layout, new String[]{"image"}, new int[]{R.id.shuos_pic});
            talkPicGridView.setAdapter(sAdapter);
            //取得说说的主要信息
            name = twitterModel.getTalkerName();
            time = twitterModel.getTime();
            word = twitterModel.getTwitterWord();
            id = twitterModel.getTalkId();
        }

        //设置说说或留言的发送者，发送时间，内容
        userName.setText(name);
        userTime.setText(time);
        userWord.setText(word);
        ImageLoader.getInstance(1).loadImage(Constant.civUrl+id+".jpg", userPic, false);
    }

    /**
     * 初始化布局，获取控件
     */
    private void initView() {

        tb = (TopBar) findViewById(R.id.topBar);
        userPic = (CircleImageVIew) findViewById(R.id.list_personphoto);
        userName = (TextView) findViewById(R.id.dongtai_name_text);
        userTime = (TextView) findViewById(R.id.dongtai_time_text);
        userWord = (TextView) findViewById(R.id.say_textview);
        talkRespondListView = (TalkRespondListView) findViewById(R.id.respond_listview);
        talkPicGridView = (TalkPicGridView) findViewById(R.id.dongtai_shuos_gridview);

        //隐藏说说和留言板的按钮
        findViewById(R.id.icon).setVisibility(View.GONE);
        findViewById(R.id.liuyan_huifu_btn).setVisibility(View.GONE);
        findViewById(R.id.liuyan_shanchu_btn).setVisibility(View.GONE);
        findViewById(R.id.edit).setVisibility(View.GONE);

        //设置返回监听
        tb.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void LeftClick(View view) {
                finish();
            }

            @Override
            public void RightClick(View view) {

            }
        });
    }

    public void getListems(final int id, final int n) {

        //根据获取的说说获取说说的图片列表
        new Thread(new Runnable() {
            @Override
            public void run() {

                List<Map<String, Bitmap>> listems = new ArrayList<>();

                for (int i = 0; i < n; i++) {
                    Map<String, Bitmap> listem = new HashMap<>();
                    try {
                        final URL url = new URL(Constant.TalkPub.getshuoshuopic
                                                + id + "_" + (i + 1) + ".jpg");
                        Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                        listem.put("image", bitmap);
                        listems.add(listem);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Message msg = new Message();
                msg.what = 1;
                msg.obj = listems;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    listems = (List<Map<String, Bitmap>>) msg.obj;
                    sAdapter.notifyDataSetChanged();
            }
        }
    };
}
