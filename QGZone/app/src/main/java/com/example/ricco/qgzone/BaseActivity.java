package com.example.ricco.qgzone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.ricco.utils.ActivityCollector;

/**
 * Created by Ricco on 2016/8/8.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
