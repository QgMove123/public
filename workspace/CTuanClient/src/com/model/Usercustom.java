package com.model;

public class Usercustom implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private int id_custom;//�û�id
	private String name;//�û���
	private String account;//�û��˺�
	private String password;//�û�����
	
	public Usercustom() {//Ĭ�Ϲ��췽��
		id_custom = -1;
	}
	
	public Usercustom(String account, String password) {
		this.account = account;
		this.password = password;
	}

	public int getId_custom() {
		return id_custom;
	}
	
	public void setId_custom(int id_custom) {
		this.id_custom = id_custom;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
