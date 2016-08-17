package com.example.ricco.entity;

import java.util.List;
/***
 * 
 * @author dragon
 *
 * @param <T>List对象
 * @param <K>实体对象
 * <pre>
 * 将List对象，实体对象和状态码打包
 * </pre>
 */

public class JsonModel<T,K> {

	private List<T> jsonList;
	private int state;
	private K jsonObject;

	public List<T> getJsonList() {
		return jsonList;
	}

	public void setJsonList(List<T> jsonList) {
		this.jsonList = jsonList;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public K getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(K jsonObject) {
		this.jsonObject = jsonObject;
	}
	
	public JsonModel(List<T> jsonList, int state, K jsonObject) {
		this.jsonList = jsonList;
		this.state = state;
		this.jsonObject = jsonObject;
	}
	public JsonModel( int state,List<T> jsonList) {
		this.jsonList = jsonList;
		this.state = state;
	}
	public JsonModel(int state, K jsonObject) {
		this.state = state;
		this.jsonObject = jsonObject;
	}
	public JsonModel(int state) {
		super();
		this.state = state;
	}
	
	
}
