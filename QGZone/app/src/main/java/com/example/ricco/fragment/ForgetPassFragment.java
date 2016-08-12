package com.example.ricco.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ricco.qgzone.R;
import com.example.ricco.utils.DialogItem;

/**
 * 忘记密码Fragment
 * Created by chenyi on 2016/8/11.
 */
public class ForgetPassFragment extends Fragment implements View.OnClickListener {
    private Button sure;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载忘记密码Fragment的布局
        View view = inflater.inflate(R.layout.forget_pass_fragment, container, false);
        sure = (Button) view.findViewById(R.id.sure_button);
        sure.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        NewPassDialog();

    }

    //设置对话框用于输入新密码
    private void NewPassDialog() {
        //对话框的自定义样式
        final DialogItem dialogItem = new DialogItem(getActivity());
        //添加对话框
        AlertDialog.Builder show = new AlertDialog.Builder(getActivity());
        show.setTitle("请输入...");
        show.setView(dialogItem);
        show.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialogItem.getPass2().equals(dialogItem.getPass1())) {
                    Toast.makeText(getActivity(), "密码修改成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
        show.setNegativeButton("取消", null);
        show.create().show();
    }
}
