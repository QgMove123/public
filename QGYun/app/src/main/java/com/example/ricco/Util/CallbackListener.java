package com.example.ricco.util;

/**
 * Created by zydx on 2016/8/1.
 */
public interface CallbackListener {

    void onFinish(Object result);

    void onError(Exception e);
}
