package com.example.ricco.util;

import com.example.ricco.entity.ObjectModel;
import com.example.ricco.entity.ResourceModel;
import com.example.ricco.qgyun.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yason
 * 提供数据源
 * 1.异步获取网络资源
 * 2.异步加载图片
 */
public class DataUtil {

    private  List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

    //1.获取json数据
    //2.解析json数据到List中
    public  List<Map<String, Object>> getData(String url){

        HttpUtil.getJson(url, new CallbackListener() {
            @Override
            public void onFinish(Object result) {
                ObjectModel objectModel = JsonUtil.toObject((String)result, ObjectModel.class);
                List<ResourceModel> list  = objectModel.getResources();
                String Type;
                for (ResourceModel rm:list) {
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("ResourceName",(Type = rm.getResourceName()));
                    map.put("ResourceUploadTime",rm.getResourceUploadTime());
                    map.put("uploader",rm.getUploaderName());
                    map.put("ResourceId",rm.getResourceId());
                    if(Type.substring(Type.indexOf(".")+1,Type.length()).equals("zip")){
                        map.put("ResourceType", R.drawable.type_zip);
                    }else if(Type.substring(Type.indexOf(".")+1,Type.length()).equals("txt")){
                        map.put("ResourceType", R.drawable.type_txt);
                    }else if(Type.substring(Type.indexOf(".")+1,Type.length()).equals("jpg")){
                        map.put("ResourceType", R.drawable.type_jpg);
                    }else{
                        map.put("ResourceType", R.drawable.type_unknow);
                    }
                    dataList.add(map);
                }
            }
            @Override
            public void onError(Exception e) {

            }
        });
    return dataList;
    }
}
