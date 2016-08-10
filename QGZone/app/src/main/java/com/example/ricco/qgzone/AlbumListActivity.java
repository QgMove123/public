package com.example.ricco.qgzone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.ricco.utils.Topbar;

/**
 * 相册列表
 * 1.topBar
 * 2.recyclerView
 */
public class AlbumListActivity extends BaseActivity{

    private Topbar topBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albumlist);
        topBar = (Topbar) findViewById(R.id.topbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

//        topBar.setOnTopBarClickListener(new Topbar.TopBarClickListener() {
//            @Override
//            public void LeftClick() {
//                finish();
//            }
//
//            @Override
//            public void RightClick() {
//                Intent intent = new Intent(AlbumListActivity.this,AddAlbumActivity.class);
//                startActivity(intent);
//            }
//        });


    }
}
