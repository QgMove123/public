package com.example.ricco.utils;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.Toast;

/**
 * 状态提示
 */
public class StateUtil {

    public static boolean albumState(Context context, int resultCode) {
        switch (resultCode) {
            case 601://成功
                return true;
            case 602://失败
                ToastUtil.showLong(context, "密码不正确");
                break;
            case 603://格式错误
                ToastUtil.showLong(context, "格式错误");
                break;
            case 604 ://重名
                ToastUtil.showLong(context, "名称已存在");
                break;
            case 605 ://内容为空
                ToastUtil.showLong(context, "相册为空");
                return true;
            case 606 ://非好友关系
                ToastUtil.showLong(context, "非好友关系");
                break;
            case 607 ://没有权限
                ToastUtil.showLong(context, "没有权限");
                break;
            case 608 ://对象不存在
                ToastUtil.showLong(context, "相册不存在");
                break;
            default://未知错误
                ToastUtil.showLong(context, "发生未知错误");
        }
        return false;
    }

}
