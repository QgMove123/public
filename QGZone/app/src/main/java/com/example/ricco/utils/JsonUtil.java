package com.example.ricco.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * toJson 用于将object对象转化为json字符串
 * toObject 其中Class<T>传入实体类类名
 * toModel 另外一个运用反射获取type，规范new TypeToken<这里放入你的类型>(){}.getType();
 * @author Wzkang
 * @// TODO: 2016/8/14
 */
public class JsonUtil {
	private static Gson gson = new Gson();
	// 将Object对象转化为json
	public static String toJson(Object obj){
		return gson.toJson(obj);
	}
	// 将json转化为Object
	public static <T> T toObject(String json, Class<T> clazz){
		return gson.fromJson(json, clazz);
	}
	// 将json转化为泛型类，方便定制
	public static <T> T toModel(String json, Type type) {
		return gson.fromJson(json, type);
	}
}
