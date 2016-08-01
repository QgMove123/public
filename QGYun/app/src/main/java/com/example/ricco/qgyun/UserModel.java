package com.example.ricco.qgyun;

/**
 * 
 * @author fangrui
 * <p>
 *  UserModel对象，储存用户信息
 * </p>
 */

public class UserModel {
	
	private int userId;
	private String userName;
	private String userPassword;
	private String userPicture;
	
	/**
	 *  UserModel 对象构造器
	 * @param userName 用户名
	 * @param userPassword 用户密码
	 * @param userPicture 用户头像
	 */
	public UserModel( String userName, String userPassword, String userPicture){
		this.userName = userName;
		this.userPassword = userPassword;
		this.userPicture = userPicture;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserPicture() {
		return userPicture;
	}

	public void setUserPicture(String userPicture) {
		this.userPicture = userPicture;
	}

	
	
}
