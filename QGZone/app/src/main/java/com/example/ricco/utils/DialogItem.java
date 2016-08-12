package com.example.ricco.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.ricco.qgzone.R;

/**
 * 输入新密码对话框的自定义View
 * Created by chenyi on 2016/8/11.
 */
public class DialogItem extends LinearLayout{

    private EditText pass1;
    private EditText pass2;

    public DialogItem(Context context) {
        super(context);
        //加载新控件的布局
        LayoutInflater.from(context).inflate(R.layout.item_dialog, this);
        pass1 = (EditText) findViewById(R.id.new_pass);
        pass2 = (EditText) findViewById(R.id.sure_pass);
    }

    /**
     * 取得输入的新密码
     */
    public String getPass1() {
        return pass1.getText().toString();
    }

    public String getPass2() {
        return pass2.getText().toString();
    }
}
