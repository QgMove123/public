package com.example.ricco.util;



import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author yason
 * 提供数据源
 * 1.异步获取网络数据
 * 2.异步加载图片
 */
public class DataUtil {

    //1.获取Json数据
    //2.解析Json数据到List中
    //3.图片异步加载
    public static void GetDataList(final String url) {

        HttpUtil.get(url,new CallbackListener() {
            @Override
            public void onFinish(String result) {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for(int i = 0;i < jsonArray.length();i ++){

                }

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
