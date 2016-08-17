package com.example.ricco.utils;

import android.util.Log;

/**
 * Created by Ricco on 2016/8/9.
 */
public class LogUtil {
    private LogUtil() {} // 封闭构造方法

    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg)
    {
        if (isDebug)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg)
    {
        if (isDebug)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg)
    {
        if (isDebug)
            Log.v(tag, msg);
    }
}
