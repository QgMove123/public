package com.example.ricco.myinterface;


import android.os.AsyncTask;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yason
 * 提供数据源
 * 1.异步获取网络数据
 * 2.异步加载图片
 */
public class DataUtil {

    public static List<Map<String,Object>> GetDataList(){
        //1.获取Json数据
        //2.解析Json数据到List中
        GetPostUtil.get(, new CallbackListener() {
            @Override
            public void onFinish(String result) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
