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
 * Created by chenyi on 2016/8/11.
 */
public class LoginSignActivity extends Activity implements LoginFragment.LoginBtnClickListener,
        LoginFragment.ForgetPassClickListener, SignFragment.SignBtnClickListener{

    private LoginFragment loginF;
    private SignFragment signF;
    private ForgetPassFragment ForgetF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_sign_layout);

        Intent intent = getIntent();
        String way = intent.getStringExtra("way");

        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
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
    public void onSignBtnClick() {
        if (loginF == null)
        {
            loginF = new LoginFragment();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(R.id.id_content, loginF);
        tx.commit();
    }

    @Override
    public void onLoginBtnClick() {
        if (loginF == null)
        {
            loginF = new LoginFragment();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(R.id.id_content, loginF);
        tx.addToBackStack(null);
        tx.commit();
    }

    @Override
    public void onForgetPassClick() {
        if (ForgetF == null)
        {
            ForgetF = new ForgetPassFragment();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(R.id.id_content, ForgetF);
        tx.commit();
    }
}
