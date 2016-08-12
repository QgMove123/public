package com.example.ricco.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ricco.qgzone.R;

/**
 * 注册功能Fragment
 * Created by chenyi on 2016/8/11.
 */
public class SignFragment extends Fragment implements View.OnClickListener {

    private Button sign;

    /**
     * 设置注册按钮的回调
     */
    public interface SignBtnClickListener
    {
        void onSignBtnClick();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_fragment, container, false);
        sign = (Button) view.findViewById(R.id.sign_in_button);
        sign.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(getActivity() instanceof SignBtnClickListener) {
            ((SignBtnClickListener) getActivity()).onSignBtnClick();
        }

    }
}
