package com.model;

public class Userbusiness implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private int id_business;//商户id
	private String name;//商户名
	private String account;//商户账号
	private String password;//商户密码
	
	public Userbusiness() {//默认构造方法
		id_business = -1;
	}
	
	public Userbusiness(String account, String password) {
		this.account = account;
		this.password = password;
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
