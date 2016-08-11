package com.example.ricco.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ricco.qgzone.R;

/**
 * Created by chenyi on 2016/8/11.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button login;
    private TextView forget;

    /**
     * 设置登录按钮的回调
     */
    public interface LoginBtnClickListener
    {
        void onLoginBtnClick();
    }

    /**
     * 设置登录按钮的回调
     */
    public interface ForgetPassClickListener
    {
        void onForgetPassClick();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        login = (Button) view.findViewById(R.id.login_button);
        login.setOnClickListener(this);

        forget = (TextView) view.findViewById(R.id.action_forget_password);
        forget.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_forget_password:
                if (getActivity() instanceof ForgetPassClickListener) {
                    ((ForgetPassClickListener) getActivity()).onForgetPassClick();
                }
                break;
            case R.id.login_button:
                if (getActivity() instanceof LoginBtnClickListener) {
                    ((LoginBtnClickListener) getActivity()).onLoginBtnClick();
                }
                break;
            default:
                break;
        }
    }
}
