package com.example.ricco.others;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ricco.qgzone.R;
import com.example.ricco.utils.ToastUtil;

/**
 * Created by Ricco on 2016/8/16.
 */
public class SearchBar extends LinearLayout {

    private ImageView mCancel = null;
    private EditText mEtext = null;
    private Context mContext = null;
    private SearchBarCallback mSearchBarCallback = null;

    //定义接口回调
    public interface SearchBarCallback{
        public void onSearchBar(String result);
    }

    //设置接口
    public void setSearchBarCallback(SearchBarCallback searchBarCalkback){
        mSearchBarCallback = searchBarCalkback;
    }

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.other_search_bar, this);
        mEtext = (EditText) findViewById(R.id.et_search);
        mCancel = (ImageView) findViewById(R.id.iv_cancel);

        initView();
    }

    public void setHint(String hint) {
        mEtext.setHint(hint);
    }

    private void initView() {

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtext.setText("");
                mCancel.setVisibility(View.GONE);
            }
        });

        mEtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    mCancel.setVisibility(View.GONE);
                } else {
                    mCancel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEtext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || keyEvent.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER) {
                    // EditText 获得的输入
                    String input = mEtext.getText().toString();
                    if (input.equals("")) {
                        ToastUtil.showShort(mContext, "嘿！你可什么东西都没输入呢！");
                    } else {
                        //回调
                        mSearchBarCallback.onSearchBar(input);
                        //隐藏软键盘
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return true;
            }
        });
    }
}
