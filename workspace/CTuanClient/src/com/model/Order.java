package com.model;

public class Order implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id_order;//����id
	private int id_custom;//�ͻ�id
	private int id_cuisine;//��ʽid
	private String businessname;//�̼�����
	private String name;//����
	private int num;//��������
	private String time;//�ʹ�ʱ��
	private String spot;//�ʹ�ص�
	private String feedback;//�û�����
	private int status;//����״̬
	/*
	 * 0��������
	 * 1��������
	 * 2�����ܵ�
	 * 3���Ѵ���
	 */
	public int getId_order() {//��ȡ����id
		return id_order;
	}
	public void setId_order(int id_order) {//���ö���id
		this.id_order = id_order;
	}
	public int getId_custom() {
		return id_custom;
	}
	public void setId_custom(int id_custom) {
		this.id_custom = id_custom;
	}
	public int getId_cuisine() {
		return id_cuisine;
	}
	public void setId_cuisine(int id_cuisine) {
		this.id_cuisine = id_cuisine;
	}
	public String getBusinessname() {
		return businessname;
	}
	public void setBusinessname(String businessname) {
		this.businessname = businessname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSpot() {
		return spot;
	}
	public void setSpot(String spot) {
		this.spot = spot;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
