package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.*;

public class Dao {
	//�����������
	protected static String dbClassName = "com.mysql.jdbc.Driver";
	//���ݿ�URL
	protected static String dbUrl = "jdbc:mysql://localhost:3306/takeout_db"
			+ "?characterEncoding=utf8&useSSL=false";
	protected static String dbUser = "root";//���ݿ��û���
	protected static String dbPwd = "123321123++";//���ݿ�����
	protected static Connection conn = null;//���ݿ����Ӷ���
	
	static {//�ھ�̬������г�ʼ��Dao�࣬ʵ�����ݿ�����Ӻ������ļ���
		try {
			if (conn == null) {
				Class.forName(dbClassName);
				System.out.println("���ݿ���سɹ�");
				conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
				System.out.println("���ݿ����ӳɹ�");
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private Dao() {//��չ��췽������ֹ����Dao���ʵ������
	}
	
	//��ȡ�û�����
	public static Usercustom getCustomInfo(String account) {
		String where = "where account= '" + account + "'";
		Usercustom info = new Usercustom();
		ResultSet set = findForResultSet("select * from user_custom " + where);
		try {
			if (set.next() != false) {
				info.setId_custom(set.getInt("id_custom"));
				info.setName(set.getString("name"));
				info.setAccount(set.getString("account"));
				info.setPassword(set.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return info;//������ģ�ͷ���
	}
	//��ȡ�̻�����
	public static Userbusiness getBusinessInfo(String account) {
		String where = "where account= '" + account + "'";
		Userbusiness info = new Userbusiness();
		ResultSet set = findForResultSet("select * from user_business " + where);
		try {
			if (set.next() != false) {
				info.setId_business(set.getInt("id_business"));
				info.setName(set.getString("name"));
				info.setAccount(set.getString("account"));
				info.setPassword(set.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return info;//������ģ�ͷ���
	}
	//��ȡ��������
	public static Order getOrderInfo(int id_order) {
		String where = "id_order= '" + id_order + "'";
		Order info = new Order();
		ResultSet set = findForResultSet("select * from takeoutorder where " + where);
		try {
			if (set.next() != false) {
				ResultSet set2 = findForResultSet("select cuisine.name, user_business.name "
						+ "from cuisine, user_business "
						+ "where cuisine.id_business = user_business.id_business "
						+ "and id_cuisine= '" + set.getInt("id_cuisine") + "'");
				set2.next();//��һ���ſ�ʼ
				info.setFeedback(set.getString("feedback"));
				info.setId_cuisine(set.getInt("id_cuisine"));
				info.setId_custom(set.getInt("id_custom"));
				info.setId_order(set.getInt("id_order"));
				info.setName(set2.getString("cuisine.name"));
				info.setBusinessname(set2.getString("user_business.name"));
				info.setNum(set.getInt("num"));
				info.setSpot(set.getString("spot"));
				info.setStatus(set.getInt("status"));
				info.setTime(set.getString("time"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return info;
	}
	//��ȡ�˵��˵�
	public static Cuisine getCuisineInfo(int id_cuisine) {
		String where = "id_cuisine= '" + id_cuisine + "'";
		Cuisine info = new Cuisine();
		ResultSet set1 = findForResultSet("select * from cuisine where " + where);//��ѯ�˵�
		ResultSet set2 = findForResultSet("select feedback from takeoutorder where " + where);//�����ѯ
		
		List<String> feedback = new ArrayList<String>();
		try {
			while (set2.next()) {//�����ѯ�Ľ�����ܼ�����
				feedback.add(set2.getString("feedback"));
			}
			
			if (set1.next() != false) {//�в˵�����һ���ж���
				info.setDescribe(set1.getString("miaoshu"));
				info.setId_business(set1.getInt("id_business"));
				info.setId_cuisine(set1.getInt("id_cuisine"));
				info.setName(set1.getString("name"));
				info.setPrice(set1.getFloat("price"));
				info.setSale(set1.getInt("sales"));
				info.setStar(set1.getInt("star"));
				info.setFeedback(feedback);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return info;
	}
	//�жϵ�½
	public static boolean checkCustomLogin (String account, String password) {
		ResultSet set = findForResultSet("select * from user_custom where account= '"
				+ account + "' and password= '" + password + "'");
		try {
			return set.next();//����ܲ�ѯ�õ����ͻ᷵��true
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public static boolean checkBusinessLogin (String account, String password) {
		ResultSet set = findForResultSet("select * from user_business where account= '"
				+ account + "' and password= '" + password + "'");
		try {
			return set.next();//����ܲ�ѯ�õ����ͻ᷵��true
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	//���뷽��
	public static int insert(String sql) {
		return update(sql);
	}
	//ɾ������
	public static int delete(String sql) {
		return update(sql);
	}
	//���·���
	public static int update(String sql) {
		int result = 0;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	//��ѯ����
	public static ResultSet findForResultSet(String sql) {
		if (conn == null)//�����Ӳ��ɹ��򷵻ؿ�
			return null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
}