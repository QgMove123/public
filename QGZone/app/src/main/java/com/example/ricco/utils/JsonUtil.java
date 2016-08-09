package com.example.ricco.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class JsonUtil {
	//将Object对象转化为json
	public static String toJson(Object obj){
		GsonBuilder gb  = new GsonBuilder();
		Gson gson = gb.create();
		return gson.toJson(obj);
	}
	//将json转化为Object
	// T代表任意一种类型，<T>是一种形式告诉系统用的是泛型编程
	public static <T> T toObject(String json, Class<T> clazz){
		Gson gson = new Gson();
		return gson.fromJson(json, clazz);
	}
	//返回Map<String, String>型键值对
	public static Map<String, String> toMap(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, new TypeToken<Map<String, String>>(){}.getType());
	}

}
