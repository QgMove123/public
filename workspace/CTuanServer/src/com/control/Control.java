package com.control;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dao.Dao;
import com.model.*;
/*
 * ���ڽ����ݿⷵ�ص���Ϣ����ɹ������������ڴ���
 */
public class Control {
		
	//��½������½ʧ�ܣ�ԭ������
	public Common login(Common com) {
		if (com.getFlag() == 1) {//�̻�
			Userbusiness ub = (Userbusiness) com.getObject();
			if (Dao.checkBusinessLogin(ub.getAccount(), ub.getPassword()) == true) {
				//ͨ��Ψһ���˺�����ѯ�ٽ��и�ֵ
				ub.setId_business(Dao.getBusinessInfo(ub.getAccount()).getId_business());
				ub.setName(Dao.getBusinessInfo(ub.getAccount()).getName());
				return com;
			} 
		} else {//�û�
			Usercustom uc = (Usercustom) com.getObject();
			if (Dao.checkCustomLogin(uc.getAccount(), uc.getPassword()) == true) {
				//ͨ��Ψһ���˺�����ѯ�ٽ��и�ֵ
				uc.setId_custom(Dao.getCustomInfo(uc.getAccount()).getId_custom());
				uc.setName(Dao.getCustomInfo(uc.getAccount()).getName());
				return com;
			} 
		}
		return com;
	}

	//ע��
	public Common register(Common com) {
		if (com.getFlag() == 1) {//�̻�
			Userbusiness ub = (Userbusiness) com.getObject();
			Dao.insert("insert user_business values(NULL, '" + ub.getName() + "','"
					+ ub.getAccount() + "','" + ub.getPassword() + "')");
			ub.setId_business(Dao.getBusinessInfo(ub.getAccount()).getId_business());
			return com;
		} else {//�û�
			Usercustom uc = (Usercustom) com.getObject();
			Dao.insert("insert user_custom values(NULL, '" + uc.getName() + "','"
					+ uc.getAccount() + "','" + uc.getPassword() + "')");
			uc.setId_custom(Dao.getCustomInfo(uc.getAccount()).getId_custom());
			return com;
		}
	}
	
	//���Ӳ˵�����������Ϣ
	public void addCuisine(Common com) {
		Cuisine cs = (Cuisine)com.getObject();
		Dao.insert("insert into cuisine values(NULL, '" + cs.getId_business() + "', '"
				+ cs.getName() + "', '" + cs.getDescribe() + "', '" + cs.getPrice()  + "', '0', '0')");
	}
	
	//ɾ���˵�
	public void deleteCuisine(Common com) {
		Dao.delete("delete from cuisine where id_cuisine= '" + com.getFlag() + "'");
	}
	
	//  ��ʾ�˵�
	public Common showCuisine(Common com) {
		@SuppressWarnings("unchecked")
		List<Cuisine> cs = (ArrayList<Cuisine>) com.getObject();
		String condition = "";
		/* 1 ��ʾȫ��
		 * 2 ��������
		 * 3 ��������
		 * 4 �۸�����
		 */
		if (com.getFlag() == 1)
			condition = "";
		else if (com.getFlag() == 2)
			condition = " order by sales desc";
		else if (com.getFlag() == 3)
			condition = " order by price desc";
		else if (com.getFlag() == 4)
			condition = " order by star desc";
		ResultSet set = Dao.findForResultSet("select id_cuisine from cuisine" + condition);
		
		try {
			while (set.next()) {
				cs.add(Dao.getCuisineInfo(set.getInt("id_cuisine")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return com;
	}
	
	//��ʾ����
	public Common showOrder(Common com) {
		@SuppressWarnings("unchecked")
		List<Order> od = (ArrayList<Order>)com.getObject();
		
		ResultSet set = Dao.findForResultSet("select id_order from takeoutorder");
		
		try {
			while (set.next()) {
				od.add(Dao.getOrderInfo(set.getInt("id_order")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return com;
	}
	//ȡ������
	public void deleteOrder(Common com) {
		Order od = (Order)com.getObject();
		Dao.delete("delete from takeoutorder where id_order= '" + od.getId_order() + "'");
	}
	
	//���Ӷ���
	public void addOrder(Common com) {
		Order od = (Order)com.getObject();
		Dao.insert("insert into takeoutorder values(NULL, '" + od.getId_custom() + "', '"
		+ od.getId_cuisine() + "', '" + od.getNum() + "', '" + od.getTime() + "', '"
		+ od.getSpot() + "', '" + od.getStatus() + "', NULL)");
	}
	
	//�޸Ĳ˵�
	public void modefyCuisine(Common com) {
		Cuisine cs = (Cuisine)com.getObject();
		Dao.update("update cuisine set name  = '" + cs.getName() + "', miaoshu= '"
		+ cs.getDescribe() + "', price= '" + cs.getPrice() + "' where id_cuisine= '" + cs.getId_cuisine() + "'");
	}
	//�޸Ķ���
	public Common modefyOrder(Common com) {
		Order od = (Order)com.getObject();
		Dao.update("update takeoutorder set status= '" + od.getStatus() + "', feedback= '" + od.getFeedback() + "' where id_order = '" + od.getId_order() + "'");
		return com;
	}
	
	//������־
	public Common downloadLog(Common com) {
		@SuppressWarnings("unchecked")
		List<String> log = (ArrayList<String>)com.getObject();
		
		ResultSet set = Dao.findForResultSet("select * from dailylog");
		
		try {
			while (set.next()) {
				log.add("" + set.getString("time") + "	" + set.getString("thing"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return com;
	}
	
	//��־
	public void recordDaily(int i) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//�������ڸ�ʽ
		String nowtime = fmt.format(new Date());
		String thing = null;
		switch (i) {
		case 1:
			thing = "�û�ע��";
			break;
		case 2:
			thing = "�û���¼";
			break;
		case 3:
			thing = "��ò˵�";
			break;
		case 4:
			thing = "��������";
			break;
		case 5:
			thing = "���Ӷ���";
			break;
		case 6:
			thing = "������־";
			break;
		case 7:
			thing = "�ϼܲ�ʽ";
			break;
		case 8:
			thing = "�¼ܲ�ʽ";
			break;
		case 9:
			thing = "�޸Ĳ�ʽ";
			break;
		case 10:
			thing = "�鿴����";
		default:
			break;
		}
		Dao.insert("insert into dailylog values('" + nowtime + "', '" + thing + "')");
	}
}