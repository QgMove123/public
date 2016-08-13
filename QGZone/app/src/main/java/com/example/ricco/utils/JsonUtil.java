package com.example.ricco.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class JsonUtil {
	private static Gson gson = new Gson();
	//将Object对象转化为json
	public static String toJson(Object obj){
		return gson.toJson(obj);
	}
	//将json转化为Object
	public static <T> T toObject(String json, Class<T> clazz){
		return gson.fromJson(json, clazz);
	}

	public static <T> T fromJson(String json, Type type) {
		return gson.fromJson(json, type);
	}
}
