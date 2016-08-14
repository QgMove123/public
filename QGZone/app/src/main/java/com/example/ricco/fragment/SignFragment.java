package com.example.ricco.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ricco.constant.Constant;
import com.example.ricco.entity.UserModel;
import com.example.ricco.qgzone.LoginSignActivity;
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 注册功能Fragment
 * Created by chenyi on 2016/8/11.
 */
public class SignFragment extends Fragment implements View.OnClickListener {

    private Button sign;
    private EditText nickName;
    private EditText answer;
    private EditText password;
    private Spinner problem;
    private int id;

    /**
     * 设置注册按钮的回调接口
     */
    public interface SignBtnClickListener
    {

        void onSignBtnClick();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_fragment, container, false);

        //输入框
        nickName = (EditText) view.findViewById(R.id.nickname);
        password = (EditText) view.findViewById(R.id.password);
        answer = (EditText) view.findViewById(R.id.answer);

        //注册按钮
        sign = (Button) view.findViewById(R.id.sign_in_button);
        sign.setOnClickListener(this);

        //密保问题选择
        problem = (Spinner) view.findViewById(R.id.problem);
        problem.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg) {
                id = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                id = 0;
            }
        });

        return view;
    }

    /**
     * 注册按钮的监听事件
     * @param v
     */
    @Override
    public void onClick(View v) {

        attemptSign();

    }

    /**
     * 判断昵称和密码的格式
     * 昵称仅限中文、英文、数字、下划线
     * 密码仅限6-15位
     * @param name
     * @return
     */
    private boolean isNickNameValid(String name) {
        return name.matches("[a-z0-9A-Z\u4e00-\u9fa5]{1,15}");
    }

    private boolean isPasswordValid(String password) {
        return (password.length() >= 6 && password.length() <= 15 );
    }

    /**
     * 登录的方法以及对错误格式
     */
    private void attemptSign() {

        //取得输入的值
        String name = nickName.getText().toString();
        String pass = password.getText().toString();
        String as = answer.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //检查密码是否有效
        if (!isPasswordValid(pass)) {
            password.setError("密码长度出错");
            focusView = password;
            cancel = true;
        }

        //检查昵称格式
        if (TextUtils.isEmpty(name)) {
            nickName.setError("昵称不能为空");
            focusView = nickName;
            cancel = true;
        } else if (!isNickNameValid(name)) {
            nickName.setError("昵称格式错误");
            focusView = nickName;
            cancel = true;
        }

        //检查密保问题的格式
        if (TextUtils.isEmpty(as)) {
            answer.setError("密保答案不能为空");
            focusView = answer;
            cancel = true;
        } else if (!isNickNameValid(as)) {
            answer.setError("密保格式错误");
            focusView = answer;
            cancel = true;
        }

        if (cancel) {
            //输入出错时在输入框提示
            focusView.requestFocus();
        } else {
            //输入正常传递信息给服务器
            UserModel userModel = new UserModel();
            userModel.setUserName(name);
            userModel.setPassword(pass);
            userModel.setUserSecretId(id);
            userModel.setUserSecretAnswer(as);

            this.sendSign(JsonUtil.toJson(userModel));
        }
    }

    // 发送注册信息给服务器
    private void sendSign(String json) {
        HttpUtil.Get(Constant.Account.userSignUp+"?jsonObject="+json, new HttpUtil.CallBackListener() {
            @Override
            public void OnFinish(Object result) {

                Message msg = new Message();
                try {
                    //通过JSONObject取出服务器传回的状态和信息
                    JSONObject dataJson = new JSONObject((String) result);
                    msg.what = Integer.valueOf(dataJson.getString("state"));
                    msg.obj = dataJson.getString("userId");
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
                case 101:
                    Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                    if(getActivity() instanceof SignBtnClickListener) {
                        ((LoginSignActivity)getActivity()).setAccount((String) msg.obj);
                        ((SignBtnClickListener) getActivity()).onSignBtnClick();
                    }
                    break;
                case 102:
                    Toast.makeText(getActivity(), "注册失败", Toast.LENGTH_SHORT).show();
                    break;
                default:break;
            }
        }
    };

}
