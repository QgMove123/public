package com.example.ricco.utils;

/**
 * 接口，用于回调
 */
public interface CallBackListener {
    public void OnFinish(String JsonResult);
    public void OnError(Exception e);
}
