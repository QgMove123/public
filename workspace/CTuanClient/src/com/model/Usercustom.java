package com.model;

public class Usercustom implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private int id_custom;//用户id
	private String name;//用户名
	private String account;//用户账号
	private String password;//用户密码
	
	public Usercustom() {//默认构造方法
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
