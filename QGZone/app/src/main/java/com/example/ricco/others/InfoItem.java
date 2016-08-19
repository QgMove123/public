package com.example.ricco.others;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ricco.qgzone.R;

/**
 * 自定义的个人信息框
 * Created by chenyi on 2016/8/10.
 */
public class InfoItem extends LinearLayout{

    private TextView textView;
    private TextView editText;

    public InfoItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_info, this);
        textView = (TextView) findViewById(R.id.info_name);
        editText = (TextView) findViewById(R.id.user_info);
    }

    public String getTextView() {
        String str = null;
        switch (textView.getText().toString()) {
            case "帐号":
                str = "userId";break;
            case "昵称":
                str = "userName";break;
            case "性别":
                str = "userSex";break;
            case "邮箱":
                str = "userEmail";break;
            case "生日":
                str = "userBirthday";break;
            case "电话":
                str = "userPhone";break;
            case "地址":
                str = "userAddress";break;
            default:
                break;
        }
        return str;
    }

    public void setTextView(String str) {
        this.textView.setText(str);
    }

    public String getEditText() {
        return editText.getText().toString();
    }

    public void setEditText(String str) {
        this.editText.setText(str);
    }
}
