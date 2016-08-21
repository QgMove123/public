package com.example.ricco.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录功能Fragment
 * Created by chenyi on 2016/8/11.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button login;
    private TextView forget;
    private EditText account;
    private EditText password;
    private boolean isflag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        HttpUtil.sessionid = null;
        isflag = true;
        account = (EditText) view.findViewById(R.id.account);
        password = (EditText) view.findViewById(R.id.password);

        //如果是注册后跳转的则将注册的帐号填入帐号输入框
        Bundle bundle = getArguments();
        if(bundle != null) {
            String str = bundle.getString("account");
            if(str != null) {
                account.setText(str);
                //确保设置帐号后密码框仍能获取焦点输入
                password.requestFocus();
                InputMethodManager imm = (InputMethodManager) password.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        }

        login = (Button) view.findViewById(R.id.login_button);
        login.setOnClickListener(this);

        forget = (TextView) view.findViewById(R.id.action_forget_password);
        forget.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        isflag = false;
        super.onDestroyView();
    }

    /**
     * 设置登录按钮的回调，进入主页
     */
    public interface LoginBtnClickListener
    {
        void onLoginBtnClick();
    }

    /**
     * 设置忘记密码按钮的回调，进入忘记密码页面
     */
    public interface ForgetPassClickListener
    {
        void onForgetPassClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_forget_password:
                //通过回调进入忘记密码页面
                if (getActivity() instanceof ForgetPassClickListener) {
                    ((ForgetPassClickListener) getActivity()).onForgetPassClick();
                }
                break;
            case R.id.login_button:
                //登录操作以及出错处理
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
     * 登录的方法以及对错误格式的出错处理
     */
    private void attemptLogin() {

        //取得输入的值
        String acc = account.getText().toString();
        String pass = password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //检查密码是否有效
        if (!isPasswordValid(pass)) {
            password.setError("6-15位密码");
            focusView = password;
            cancel = true;
        }

        //检查帐号格式
        if (TextUtils.isEmpty(acc)) {
            account.setError("帐号不能为空");
            focusView = account;
            cancel = true;
        } else if (!isAccountValid(acc)) {
            account.setError("7位数字帐号");
            focusView = account;
            cancel = true;
        }

        if (cancel) {
            //输入框报错
            focusView.requestFocus();
        } else {
            //输入正确，传给后台确认
            Map<String, String> json = new HashMap<>();
            json.put("userId", acc);
            json.put("password", pass);

            try {
                HttpUtil.Get(Constant.Account.userSignIn+"?jsonObject="+ URLEncoder.encode(JsonUtil.toJson(json), "utf-8"), callBackListener);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private HttpUtil.CallBackListener callBackListener = new HttpUtil.CallBackListener() {
        @Override
        public void OnFinish(String result) {
            Message msg = new Message();
            try {
                //通过JSONObject取出服务器传回的状态和信息
                JSONObject dataJson = new JSONObject(result);
                Log.e("OnFinish: result", result+"");
                msg.what = Integer.valueOf(dataJson.getString("state"));
                msg.obj = dataJson.getString("user");
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                mHandler.sendMessage(msg);
            }
        }

        @Override
        public void OnError(Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(0);
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            if(isflag) {
                switch (msg.what) {
                    case 0:
                        ToastUtil.showShort(getActivity(), "服务器异常");
                        break;
                    case 111:
                        ToastUtil.showShort(getActivity(), "登录成功");
                        //通过回调跳转到主页
                        if (getActivity() instanceof LoginBtnClickListener) {
                            ((LoginBtnClickListener) getActivity()).onLoginBtnClick();
                        }
                        UserModel um = JsonUtil.toObject(msg.obj.toString(), UserModel.class);
                        Constant.HOST_ID = Integer.parseInt(um.getUserId());
                        Constant.HOST_NAME = um.getUserName();
                        break;
                    case 112:
                        Log.e("handleMessage: ", "登录失败");
                        ToastUtil.showShort(getActivity(), "登录失败");
                        //清空sessionId
                        HttpUtil.sessionid = null;
                        break;
                    default:
                        ToastUtil.showShort(getActivity(), "连接不上服务器，请查看IP");
                        break;
                }
            }
        }
    };
}
