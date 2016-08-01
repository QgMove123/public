package com.example.ricco.qgyun;

import java.io.Serializable;
import java.util.Date;
/***
 * 
 * @author dragon
 *<pre>
 *这是一个关于资料的类
 *</pre>
 */
public class ResourceModel implements Serializable{

	private int resourceId;
	private int uploadId;
	private String resourceName;
	private Date resourceUploadTime;
	private String resourcePath;
	
	public ResourceModel(){}
	public ResourceModel(int resourceId,int uploadId,String resourceName,Date resourceUploadTime,String resourcePath){
		this.resourceId=resourceId;
		this.uploadId=uploadId;
		this.resourceName=resourceName;
		this.resourcePath=resourcePath;
		this.resourceUploadTime=resourceUploadTime;
	}
	public ResourceModel(int uploadId,String resourceName,Date resourceUploadTime,String resourcePath){
		this.uploadId=uploadId;
		this.resourceName=resourceName;
		this.resourcePath=resourcePath;
		this.resourceUploadTime=resourceUploadTime;
	}
	/**
	 * 这是一个获取资源ID的的方法
	 * @return
	 */
	public int getResourceId() {
		return resourceId;
	}
	/**
	 * 这是一个获取上传者的方法
	 * @return 上传者的名字
	 */
	public int getUploadId() {
		return uploadId;
	}
	/**
	 * 这是一个获取资料名字的方法
	 * @return 文件名字
	 */
	public String getResourceName() {
		return resourceName;
	}
	/**
	 * 这是一个获取上传文件路径的方法
	 * @return 文件的路径
	 */
	public String getResourcePath() {
		return resourcePath;
	}
	/**
	 * 这是一个获取上传时间的方法
	 * @return 文件上传的时间
	 */
	public Date getResourceUploadTime(){
		return resourceUploadTime;
	}
	/**
	 * 这是一个设置文件上传者的方法
	 * @param uploader 文件上传者的名字
	 */
	public void setUploader(int uploadId) {
		this.uploadId = uploadId;
	}
	/**
	 * 这是一个设置文件名字的方法
	 * @param resourceName 文件名字
	 */
	public void setResourceName(String resourceName){
		this.resourceName = resourceName;
	}
	/**
	 * 这是一个设置文件存储路径的方法
	 * @param resourcePath 文件存储的路径
	 */
	public void setResourcePath(String resourcePath){
		this.resourcePath = resourcePath;
	}
	/**
	 * 这是一个设置文件上传时间的方法
	 * @param resourceUploadTime 文件上传的时间
	 */
	public void setResourceUploadTime(Date resourceUploadTime){
		this.resourceUploadTime=resourceUploadTime;
	}
}
