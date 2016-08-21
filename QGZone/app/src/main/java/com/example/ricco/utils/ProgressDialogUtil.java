package com.example.ricco.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Mr_Do on 2016/8/21.
 * title 对话框的标题
 * message 对话框的内容
 * cancelable 对话框是否能取消
 * context 上下文
 */
public class ProgressDialogUtil {
    private static ProgressDialog mpdialog;

    public static void showDialog(String title, String message, boolean cancelable, Context context) {
        mpdialog = new ProgressDialog(context);
        //设置圆形进度条风格
        mpdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mpdialog.setTitle(title);                       //设置标题
        mpdialog.setMessage(message);               //设置内容
        mpdialog.setIndeterminate(false);              //设置进度条是否可以不明确
        mpdialog.setCancelable(cancelable);                  //设置进度条是否可以取消
        mpdialog.show();                            //显示进度条
    }
    public static void deleteDialog(){
        mpdialog.dismiss();
    }
}
