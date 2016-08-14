package com.example.ricco.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ricco.constant.Constant;
import com.example.ricco.entity.UserModel;
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录功能Fragment
 * Created by chenyi on 2016/8/11.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button login;
    private TextView forget;
    private EditText account;
    private EditText password;

    /**
     * 设置登录按钮的回调
     */
    public interface LoginBtnClickListener
    {


        void onLoginBtnClick();
    }
    /**
     * 设置忘记密码按钮的回调
     */
    public interface ForgetPassClickListener
    {

        void onForgetPassClick();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        account = (EditText) view.findViewById(R.id.account);
        password = (EditText) view.findViewById(R.id.password);
        Bundle bundle = getArguments();
        if(bundle != null) {
            String str = bundle.getString("account");
            if(str != null) {
                account.setText(str);
            }
        }
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
                attemptLogin();
                break;
            default:
                break;
        }
    }

    /**
     * 判断帐号和密码的格式
     * @param account
     * @return
     */
    private boolean isAccountValid(String account) {
        return account.length() == 7;
    }

    private boolean isPasswordValid(String password) {
        return (password.length() >= 6 && password.length() <= 15 );
    }

    /**
     * 登录的方法以及对错误格式
     */
    private void attemptLogin() {

        //取得输入的值
        String acc = account.getText().toString();
        String pass = password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //检查密码是否有效
        if (!isPasswordValid(pass)) {
            password.setError("密码长度出错");
            focusView = password;
            cancel = true;
        }

        //检查帐号格式
        if (TextUtils.isEmpty(acc)) {
            account.setError("帐号不能为空");
            focusView = account;
            cancel = true;
        } else if (!isAccountValid(acc)) {
            account.setError("帐号应为7位数字");
            focusView = account;
            cancel = true;
        }

        if (cancel) {
            //输入框报错
            focusView.requestFocus();
        } else {
            //输入正确，传给后台确认
            UserModel userModel = new UserModel();
            userModel.setUserId(acc);
            userModel.setPassword(pass);

            this.sendLogin(JsonUtil.toJson(userModel));
        }
    }

    // 发送注册信息给服务器
    private void sendLogin(String json) {
        HttpUtil.Get(Constant.Account.userSignIn+"?jsonObject="+json, new HttpUtil.CallBackListener() {
            @Override
            public void OnFinish(Object result) {

                Message msg = new Message();
                try {
                    //通过JSONObject取出服务器传回的状态和信息
                    JSONObject dataJson = new JSONObject((String) result);
                    Log.e("OnFinish: result", result+"");
                    msg.what = Integer.valueOf(dataJson.getString("state"));
                    msg.obj = dataJson.get("user");
                    mHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 111:
                    ToastUtil.showShort(getActivity(), "登录成功");
                    if (getActivity() instanceof LoginBtnClickListener) {
                        ((LoginBtnClickListener) getActivity()).onLoginBtnClick();
                    }
                    break;
                case 112:
                    ToastUtil.showShort(getActivity(), "登录失败");
                    break;
                default:break;
            }
        }
    };
}
