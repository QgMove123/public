package com.example.ricco.qgzone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ricco.utils.CircleImageVIew;
import com.example.ricco.utils.InfoItem;
import com.example.ricco.utils.TopBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人资料
 */
public class InfoActivity extends BaseActivity{

    private List<InfoItem> infoArry = new ArrayList<InfoItem>();
    private CircleImageVIew headPic;
    private TextView name;
    private Button exit;
    private TopBar tb;

    public static void actionStart(Context context, String data1, int data2) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra("user", data1);
        intent.putExtra("id", data2);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_layout);

        Intent intent = getIntent();
        if(intent.getStringExtra("user").equals("friend")) {
            ((TopBar)findViewById(R.id.topBar)).setRightIsVisable(false);
            findViewById(R.id.exit).setVisibility(View.GONE);
        } else {
            tb = (TopBar) findViewById(R.id.topBar);
            exit = (Button) findViewById(R.id.exit);
        }



    }

    public void initInfo() {
        InfoItem account = (InfoItem) findViewById(R.id.account);
        account.setTextView("帐号");
        infoArry.add(account);
        InfoItem sex = (InfoItem) findViewById(R.id.sex);
        sex.setTextView("性别");
        infoArry.add(sex);
        InfoItem birthday = (InfoItem) findViewById(R.id.birth);
        birthday.setTextView("生日");
        infoArry.add(birthday);
        InfoItem place = (InfoItem) findViewById(R.id.place);
        place.setTextView("所在地");
        infoArry.add(place);
        InfoItem phone = (InfoItem) findViewById(R.id.phone);
        phone.setTextView("电话");
        infoArry.add(phone);
        InfoItem email = (InfoItem) findViewById(R.id.email);
        email.setTextView("邮箱");
        infoArry.add(email);
        InfoItem problem = (InfoItem) findViewById(R.id.problem);
        problem.setTextView("密保问题");
        infoArry.add(problem);
        InfoItem password = (InfoItem) findViewById(R.id.password);
        password.setTextView("密码");
        infoArry.add(password);
    }


}
