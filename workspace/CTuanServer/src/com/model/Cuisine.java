package com.model;

import java.util.List;

public class Cuisine implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id_cuisine;//�˵�id
	private int id_business;//�̻�id
	private String name;//����
	private String describe;//��������
	private float price;//�۸�
	private int sale;//����
	private int star;//����
	private List<String> feedback;//����
	
	public int getId_cuisine() {//��ȡ�˵�id
		return id_cuisine;
	}
	public void setId_cuisine(int id_cuisine) {//���ò˵�id
		this.id_cuisine = id_cuisine;
	}
	public int getId_business() {
		return id_business;
	}
	public void setId_business(int id_business) {
		this.id_business = id_business;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getSale() {
		return sale;
	}
	public void setSale(int sale) {
		this.sale = sale;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public List<String> getFeedback() {
		return feedback;
	}
	public void setFeedback(List<String> feedback) {
		this.feedback = feedback;
	}
}