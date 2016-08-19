package com.example.ricco.qgzone;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.ricco.constant.Constant;
import com.example.ricco.fragment.BaseFragment;
import com.example.ricco.others.BottomControlPanel;
import com.example.ricco.utils.HttpUtil;

public class MainActivity extends BaseActivity implements BottomControlPanel.BottomPanelCallback {

    BottomControlPanel bottomPanel = null;

    private FragmentManager fragmentManager = null;
    private FragmentTransaction fragmentTransaction = null;

    public static String nowFragTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        fragmentManager = getFragmentManager();
        setDefaultFirstFragment(Constant.FRAGMENT_FLAG_DONGTAI);
    }


    @Override
    protected void onDestroy() {
        HttpUtil.Get(Constant.Account.userSignOut, null);
        super.onDestroy();
    }

    //初始化UI界面并设置接口回调
    private void initUI() {
        bottomPanel = (BottomControlPanel) findViewById(R.id.bottom_layout);
        if (bottomPanel != null) {
            bottomPanel.initBottomPanel();
            bottomPanel.setBottomCallback(this);
        }
    }

    /*
     *处理BottomControlPanel的回调
     */
    @Override
    public void onBottomPanelClick(int itemId) {
        // TODO Auto-generated method stub
        String tag = "";
        switch (itemId) {
            case Constant.BTN_FLAG_DONGTAI:
                tag = Constant.FRAGMENT_FLAG_DONGTAI;
                break;
            case Constant.BTN_FLAG_ABOUTME:
                tag = Constant.FRAGMENT_FLAG_ABOUTME;
                break;
            case Constant.BTN_FLAG_SEND:
                tag = Constant.FRAGMENT_FLAG_SEND;
                break;
            case Constant.BTN_FLAG_ZONE:
                tag = Constant.FRAGMENT_FLAG_ZONE;
                break;
            case Constant.BTN_FLAG_FRIEND:
                tag = Constant.FRAGMENT_FLAG_FRIEND;
            default:
                break;
        }

        if (tag.equals(Constant.FRAGMENT_FLAG_SEND)) {
            //发表说说和其他控件不同
            startActivity(new Intent(MainActivity.this, TalkPubActivity.class));
        } else {
            //切换Fragment
            setTabSelection(tag);
        }
    }

    private void setDefaultFirstFragment(String tag) {
        //设置默认选择fragment
        setTabSelection(tag);
        //设置默认选择Tab
        bottomPanel.defaultBtnChecked();
    }

    //提交事务
    private void commitTransactions(String tag) {
        if (fragmentTransaction != null && !fragmentTransaction.isEmpty()) {
            fragmentTransaction.commit();
            nowFragTag = tag;
            //释放事务
            fragmentTransaction = null;
        }
    }

    //保证事务创建成功
    private FragmentTransaction ensureTransaction() {
        if (fragmentTransaction == null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }

        return fragmentTransaction;
    }

    private void attachFragment(int layout, Fragment f, String tag) {
        if (f != null) {
            if (f.isDetached()) {
                ensureTransaction();
                fragmentTransaction.attach(f);

            } else if (!f.isAdded()) {
                ensureTransaction();
                fragmentTransaction.add(layout, f, tag);
            }
        }
    }

    private Fragment getFragment(String tag) {

        Fragment f = fragmentManager.findFragmentByTag(tag);

        if (f == null) {
            f = BaseFragment.newInstance(getApplicationContext(), tag);
        }
        return f;

    }

    private void detachFragment(Fragment f) {

        if (f != null && !f.isDetached()) {
            ensureTransaction();
            fragmentTransaction.detach(f);
        }
    }

    /**
     * 切换fragment
     *
     * @param tag
     */
    private void switchFragment(String tag) {
        //如果重复点击同一个标签
        if (tag.equals(nowFragTag)) {
            return;
        }
        //把上一个fragment detach掉
        if (nowFragTag != null && !nowFragTag.equals("")) {
            detachFragment(getFragment(nowFragTag));
        }
        attachFragment(R.id.fragment_content, getFragment(tag), tag);
        commitTransactions(tag);
    }

    /**
     * 设置选中的Tag
     *
     * @param tag
     */
    public void setTabSelection(String tag) {
        // 开启一个Fragment事务
        fragmentTransaction = fragmentManager.beginTransaction();
        switchFragment(tag);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        nowFragTag = "";
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
    }

   //以下代码用于实现点击编辑框外部隐藏软键盘效果
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                if(hideInputMethod(this, v)) {

                    //return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    public static Boolean hideInputMethod(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
    }
}