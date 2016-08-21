package com.example.ricco.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import com.example.ricco.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
    private boolean isflag;
    //记录选择的密保问题
    private int id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_fragment, container, false);

        isflag = true;

        //输入框
        nickName = (EditText) view.findViewById(R.id.nickname);
        password = (EditText) view.findViewById(R.id.password);
        answer = (EditText) view.findViewById(R.id.answer);

        //注册按钮
        sign = (Button) view.findViewById(R.id.sign_in_button);
        sign.setOnClickListener(this);

        //密保问题选择
        problem = (Spinner) view.findViewById(R.id.problem);
        //监听密保问题的选项
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

    @Override
    public void onDestroyView() {
        isflag = false;
        super.onDestroyView();
    }

    /**
     * 设置注册按钮的回调接口，跳转到登录的界面
     */
    public interface SignBtnClickListener {
        void onSignBtnClick();
    }

    /**
     * 注册按钮的监听事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        //注册操作，设置出错处理
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
            password.setError("6-15位密码");
            focusView = password;
            cancel = true;
        }

        //检查昵称格式
        if (TextUtils.isEmpty(name)) {
            nickName.setError("昵称不能为空，");
            focusView = nickName;
            cancel = true;
        } else if (!isNickNameValid(name)) {
            nickName.setError("昵称仅限中文、英文、数字、下划线");
            focusView = nickName;
            cancel = true;
        }

        //检查密保问题的格式
        if (TextUtils.isEmpty(as)) {
            answer.setError("密保答案不能为空");
            focusView = answer;
            cancel = true;
        } else if (!isNickNameValid(as)) {
            answer.setError("密保仅限中文、英文、数字、下划线");
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

            try {
                HttpUtil.Get(Constant.Account.userSignUp+"?jsonObject="+ URLEncoder.encode(JsonUtil.toJson(userModel), "utf-8"),
                            callBackListener);
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
                msg.what = Integer.valueOf(dataJson.getString("state"));
                msg.obj = dataJson.getString("userId");
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                mHandler.sendMessage(msg);
            }
        }

        @Override
        public void OnError(Exception e) {
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
                    case 101:
                        Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                        final String account = (String) msg.obj;
                        //弹出对话框显示新帐号
                        AlertDialog.Builder show = new AlertDialog.Builder(getActivity());
                        show.setTitle("请输入...");
                        show.setMessage("你的帐号为:"+account);

                        show.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //调用接口回调，跳转页面
                                if(getActivity() instanceof SignBtnClickListener) {
                                    //保存注册后的帐号
                                    ((LoginSignActivity)getActivity()).setAccount(account);
                                    ((SignBtnClickListener) getActivity()).onSignBtnClick();
                                }
                            }
                        });
                        show.create().show();
                        break;
                    case 102:
                        Toast.makeText(getActivity(), "注册失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        ToastUtil.showShort(getActivity(), "连接不上服务器，请查看IP");
                        break;
                }
            }

        }
    };

}
