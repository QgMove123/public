package com.example.ricco.qgzone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.ricco.utils.TopBar;

/**
 * 相册列表
 * 1.topBar
 * 2.recyclerView
 */
public class AlbumListActivity extends BaseActivity{

    private TopBar topBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        topBar = (TopBar) findViewById(R.id.topbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        topBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void LeftClick() {
                finish();
            }

            @Override
            public void RightClick() {
                Intent intent = new Intent(AlbumListActivity.this,AddAlbumActivity.class);


            }
        });

    }
}
