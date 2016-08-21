package com.example.ricco.qgzone;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ricco.fragment.ForgetPassFragment;
import com.example.ricco.fragment.LoginFragment;
import com.example.ricco.fragment.SignFragment;

/**
 * 登录注册的Activity，在Fragment中切换不同的功能
 * Created by chenyi on 2016/8/11.
 */
public class LoginSignActivity extends Activity implements LoginFragment.LoginBtnClickListener,
        LoginFragment.ForgetPassClickListener, SignFragment.SignBtnClickListener{

    private LoginFragment loginF;
    private SignFragment signF;
    private ForgetPassFragment ForgetF;
    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_sign_layout);

        Intent intent = getIntent();
        String way = intent.getStringExtra("way");

        //取得事务管理器
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        //通过接收的信息判断使用的功能Fragment
        if (way.equals("login")) {
            loginF = new LoginFragment();
            Log.e("onCreate: Login", "start");
            tx.add(R.id.id_content, loginF);
        } else {
            Log.e("onCreate: Sign", "start");
            signF = new SignFragment();
            tx.add(R.id.id_content,signF);
        }
        tx.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    /**
     * 继承注册Fragment的按钮接口，跳转到登录Fragment
     */
    @Override
    public void onSignBtnClick() {
        if (loginF == null) {
            loginF = new LoginFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString("account", account);
        Log.e("onSignBtnClick: 注册的帐号", account);
        //绑定碎片并且将信息传递给碎片
        loginF.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(R.id.id_content, loginF);
        tx.commit();
    }

    /**
     * 继承登录Fragment的登录按钮接口，跳转到系统首页
     */
    @Override
    public void onLoginBtnClick() {
        Intent intent = new Intent(LoginSignActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
//        InfoActivity.actionStart(LoginSignActivity.this, "me", 0);
    }

    /**
     * 继承登录Fragment的忘记密码按钮接口，跳转到忘记密码Fragment
     */
    @Override
    public void onForgetPassClick() {
        if (ForgetF == null)
        {
            ForgetF = new ForgetPassFragment();
        }
        //通过事务管理器切换不同功能Fragment
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(R.id.id_content, ForgetF);
        tx.addToBackStack(null);
        tx.commit();
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
