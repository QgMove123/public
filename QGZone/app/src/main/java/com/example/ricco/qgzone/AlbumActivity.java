package com.example.ricco.qgzone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.ricco.utils.SipAdapter;
import com.example.ricco.others.TopBar;

import java.util.List;
import java.util.Map;

/**
 * @author yason
 * 相册列表
 * 1.topBar
 * 2.recyclerView
 */
public class AlbumActivity extends BaseActivity{

    private TopBar topBar;
    private RecyclerView recyclerView;
    private List<Map<String,Object>> dataList;
    private SipAdapter sip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        //初始化
        topBar = (TopBar) findViewById(R.id.topBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //topBar事件监听
        topBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void LeftClick(View view) {
                finish();
            }

            @Override
            public void RightClick(View view) {
                Intent intent = new Intent(AlbumActivity.this, AddAlbumActivity.class);
                startActivity(intent);
            }
        });
        //显示相册列表
        //1.获取数据源

        //2.适配器加载数据源
        sip = new SipAdapter(this, dataList);
        //3.视图加载适配器
        recyclerView.setAdapter(sip);
    }
}
