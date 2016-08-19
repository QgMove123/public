package com.example.ricco.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ricco.constant.Constant;
import com.example.ricco.others.PassItem;
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 忘记密码Fragment
 * Created by chenyi on 2016/8/11.
 */
public class ForgetPassFragment extends Fragment implements View.OnClickListener {

    private Button sure;
    private EditText account;
    private EditText answer;
    private Spinner problem;
    private int id;
    private Map<String, Object> jsonObjiect = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载忘记密码Fragment的布局
        View view = inflater.inflate(R.layout.forget_pass_fragment, container, false);

        //确认修改密码的按钮
        sure = (Button) view.findViewById(R.id.sure_button);
        sure.setOnClickListener(this);

        //输入帐号密保答案
        account = (EditText) view.findViewById(R.id.account);
        answer = (EditText) view.findViewById(R.id.answer);

        //密保问题
        problem = (Spinner) view.findViewById(R.id.problem);
        problem.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg) {
                //获取修改密保问题的序号
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
     * 忘记密码的操作以及输入的出错处理
     * @param v
     */
    @Override
    public void onClick(View v) {

        String acc = account.getText().toString();
        String as = answer.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //检查帐号是否有效
        if (acc.length() != 7) {
            account.setError("帐号为7为数字");
            focusView = account;
            cancel = true;
        }

        //检查密保答案的格式
        if (TextUtils.isEmpty(as)) {
            answer.setError("密保答案不能为空");
            focusView = answer;
            cancel = true;
        } else if (!as.matches("[a-z0-9A-Z\u4e00-\u9fa5]{1,15}")) {
            answer.setError("密保答案限定中文、数字、字母");
            focusView = answer;
            cancel = true;
        }

        if (cancel) {
            //输入出错时在输入框提示
            focusView.requestFocus();
        } else {
            //传递正常信息给服务器
            jsonObjiect.put("userId", acc);
            jsonObjiect.put("oldSecretId", id+"");
            jsonObjiect.put("oldAnswer", as);

            HttpUtil.Get(Constant.Account.userCheckSecret+"?jsonObject="+JsonUtil.toJson(jsonObjiect),
                        callBackListener);
        }
    }

    /**
     * 设置对话框用于输入新密码
     */
    private void NewPassDialog() {
        //对话框的自定义样式
        final PassItem passItem = new PassItem(getActivity());
        passItem.setPass1("新密码");
        passItem.setPass2("确认新密码");
        //添加对话框
        AlertDialog.Builder show = new AlertDialog.Builder(getActivity());
        show.setTitle("请输入...");
        show.setView(passItem);

        show.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = passItem.getPass1();
                //判断两次输入密码是否相同
                if (passItem.getPass2().equals(password) && !password.isEmpty()) {
                    canCloseDialog(dialog, true);
                    jsonObjiect.put("newPassword" , password);
                    //发送修改密码的信息
                    HttpUtil.Get(Constant.Account.userForgetPassword+"?jsonObject="+JsonUtil.toJson(jsonObjiect),
                                callBackListener);
                } else {
                    //对话框不关闭，并且提示错误
                    canCloseDialog(dialog, false);
                    passItem.getEditText().setError("两次输入的密码不一致");
                    View focusView = passItem.getEditText();
                    focusView.requestFocus();
                }
            }
        });
        show.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消对话框
                canCloseDialog(dialog, true);
            }
        });
        show.create().show();
    }

    /**
     * 输入框能否关闭
     * @param dialogInterface
     * @param close
     */
    private void canCloseDialog(DialogInterface dialogInterface, boolean close) {
        try {
            Field field = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialogInterface, close);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * HttpUtil的回调，接收返回的状态码和信息
     */
    private HttpUtil.CallBackListener callBackListener = new HttpUtil.CallBackListener() {
        @Override
        public void OnFinish(String result) {
            Message msg = new Message();
            try {
                //通过JSONObject取出服务器传回的状态和信息
                JSONObject dataJson = new JSONObject(result);
                Log.e("OnFinish: ", result+"");
                msg.what = Integer.valueOf(dataJson.getString("state"));
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                mHandler.sendMessage(msg);
            }
        }

        @Override
        public void OnError(Exception e) {
            e.printStackTrace();
        }
    };

    /**
     * 对接收的状态码进行判断，更新UI
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1211:
                    NewPassDialog();
                    break;
                case 1212:
                    Toast.makeText(getActivity(), "找不到帐号或密保答案错误", Toast.LENGTH_SHORT).show();
                    break;
                case 121:
                    Toast.makeText(getActivity(), "密码修改成功", Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                    break;
                case 122:
                    Toast.makeText(getActivity(), "密码修改失败", Toast.LENGTH_SHORT).show();
                    break;
                default:break;
            }
        }
    };
}
