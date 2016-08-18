package com.example.ricco.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class JsonUtil {
	//将Object对象转化为json
	public static String toJson(Object obj){
		GsonBuilder gb  = new GsonBuilder();
		Gson gson = gb.create();
		return gson.toJson(obj);
	}
	//将json转化为Object
	public static <T> T toObject(String json, Class<T> clazz){
		Gson gson = new Gson();
		return gson.fromJson(json, clazz);
	}
	//将json转化为Model
	public static <T> T toModel(String json, Type type){
		Gson gson = new Gson();
		return gson.fromJson(json, type);
	}
	//返回Map<String, String>型键值对
	public static Map<String, String> toMap(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json,
				new TypeToken<Map<String, String>>(){}.getType());
	}

}
