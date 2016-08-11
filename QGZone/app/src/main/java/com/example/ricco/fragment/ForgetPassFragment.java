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
 * Created by chenyi on 2016/8/11.
 */
public class ForgetPassFragment extends Fragment implements View.OnClickListener {
    private Button sure;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forget_pass_fragment, container, false);
        sure = (Button) view.findViewById(R.id.sure_button);
        sure.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        NewPassDialog();

    }

    private void NewPassDialog() {
        final DialogItem dialogItem = new DialogItem(getActivity());
        AlertDialog.Builder show = new AlertDialog.Builder(getActivity());
        show.setTitle("输入新密码");
        show.setIcon(android.R.drawable.ic_dialog_info);
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
