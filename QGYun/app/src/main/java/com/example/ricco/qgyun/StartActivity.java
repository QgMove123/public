package com.example.ricco.qgyun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.ricco.myinterface.*;

public class StartActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void login(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public void register(){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}
