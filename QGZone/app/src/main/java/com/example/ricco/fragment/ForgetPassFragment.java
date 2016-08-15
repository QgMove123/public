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
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.DialogItem;
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
        sure = (Button) view.findViewById(R.id.sure_button);
        sure.setOnClickListener(this);

        account = (EditText) view.findViewById(R.id.account);
        answer = (EditText) view.findViewById(R.id.answer);
        problem = (Spinner) view.findViewById(R.id.problem);

        //密保问题
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
            //输入正常传递信息给服务器
            jsonObjiect.put("userId", acc);
            jsonObjiect.put("oldSecretId", id+"");
            jsonObjiect.put("oldAnswer", as);

            this.sendProblem(Constant.Account.userCheckSecret+"?jsonObject=" + JsonUtil.toJson(jsonObjiect));
        }
    }

    //设置对话框用于输入新密码
    private void NewPassDialog() {
        //对话框的自定义样式
        final DialogItem dialogItem = new DialogItem(getActivity());
        dialogItem.setPass1("新密码");
        dialogItem.setPass1("确认新密码");
        //添加对话框
        AlertDialog.Builder show = new AlertDialog.Builder(getActivity());
        show.setTitle("请输入...");
        show.setView(dialogItem);
        show.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = dialogItem.getPass1();
                if (dialogItem.getPass2().equals(password) && !password.isEmpty()) {
                    canCloseDialog(dialog, true);
                    jsonObjiect.put("newPassword" , password);
                    sendProblem(Constant.Account.userForgetPassword+"?jsonObject="+JsonUtil.toJson(jsonObjiect));
                } else {
                    canCloseDialog(dialog, false);
                    dialogItem.getEditText().setError("两次输入的密码不一致");
                    View focusView = dialogItem.getEditText();
                    focusView.requestFocus();
                }
            }
        });
        show.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                canCloseDialog(dialog, true);
            }
        });
        show.create().show();
    }

    private void canCloseDialog(DialogInterface dialogInterface, boolean close) {
        try {
            Field field = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialogInterface, close);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 发送注册信息给服务器
    private void sendProblem(String url) {
        HttpUtil.Get(url, new HttpUtil.CallBackListener() {
            @Override
            public void OnFinish(String result) {

                Message msg = new Message();
                try {
                    //通过JSONObject取出服务器传回的状态和信息
                    JSONObject dataJson = new JSONObject(result);
                    Log.e("OnFinish: ", result+"");
                    msg.what = Integer.valueOf(dataJson.getString("state"));
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
                case 1211:
                    NewPassDialog();
                    break;
                case 1212:
                    Toast.makeText(getActivity(), "找不到帐号或密保答案错误", Toast.LENGTH_SHORT).show();
                    break;
                case 121:
                    jsonObjiect.remove("newPassword");
                    Toast.makeText(getActivity(), "密码修改成功", Toast.LENGTH_SHORT).show();
                    break;
                case 122:
                    Toast.makeText(getActivity(), "密码修改失败", Toast.LENGTH_SHORT).show();
                    break;
                default:break;
            }
        }
    };
}
