package com.example.ricco.qgzone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.ricco.utils.ImageLoader;

public class ImageDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ImageView imagedetail = (ImageView) findViewById(R.id.imagedetail);
        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");
        ImageLoader.getInstance(1).loadImage(url,imagedetail,false);
        imagedetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
