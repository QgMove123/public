package com.example.ricco.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
	public static String toJson(Object obj){
		GsonBuilder gb  = new GsonBuilder();
		Gson gson = gb.create();
		return gson.toJson(obj);
	}
	
	public static <T> T toObject(String json, Class<T> clazz){
		GsonBuilder gb = new GsonBuilder();
		Gson gson = gb.create();
		return gson.fromJson(json, clazz);
	}
}
