package students;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Student {
	public static void show(Connection con) throws Exception{
		
		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
		Search search = null;
		Operation operation = null;
		//��ʾ���е�ѧ��
		new SearchAll().select(con);
		System.out.println("1.�鿴ĳͬѧ 2.�鿴ĳһ�ɼ� 3.������ʾ 4.���� 5.�޸� 6.��Χ���� 7.ɾ��");
		System.out.print("������ѡ��");
		int k = Integer.parseInt(read.readLine());
		//ѡ���ܽ��в���
		switch (k) {
			case 1:search = new SearchOne();
				break;
			case 2:search = new SearchGrade();
				break;
			case 3:search = new SearchSort();
				break;
			case 4:operation = new Add();
				break;
			case 5:operation = new Alter();
				break;
			case 6:search = new SearchScope();
				break;
			case 7:operation = new Delete();
				break;
		}
		//���ö�����ʵ�ֹ���
		if(search != null){
			search.select(con);
		}
		if(operation != null){
			operation.operate(con);
		}
	}  
}
//���Һ����ĸ���
class Search {
	PreparedStatement sql;
	ResultSet res;
	BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
	//ʵ�ֲ��ҹ��ܵĺ����ӿ�
	public  void select(Connection con) {
		
	}
	//������ҵ���Ϣ
	public void resPrint() throws SQLException, NumberFormatException, IOException {
		
		if(res.next()){
			System.out.println("ѧ��:" + res.getInt(1) + "  ����:" + res.getString(3) +
				 			"  �Ա�:" + res.getString(4) + "  רҵ:" + res.getString(2) + 
				 			"  �༶:" + res.getInt(5) + "��" + "  ���ĳɼ�:" + res.getInt(6) + 
				 			"  ��ѧ�ɼ�:" + res.getInt(7) + "  Ӣ��ɼ�:" + res.getInt(8));
		} else {
			//��û�в��ҳɹ�
			System.out.println("�Ҳ�����ѧ����Ϣ");
		}
	 }
}
//�������е�ѧ�������ѧ������
class SearchAll extends Search {
	@Override
	public void select(Connection con){
		try{
			sql = con.prepareStatement("select*from students");
			res = sql.executeQuery();
			while(res.next()) {
				int num = res.getInt(1);
				String name = res.getString(3);
				System.out.println("ѧ��:" + num + "  ����: " + name);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
//���ĳ��ѧ����ȫ����Ϣ
class SearchOne extends Search {
	public void select(Connection con) {
		try {
			System.out.print("�����ѯѧ����id:");
			int id = Integer.parseInt(read.readLine());
			//�����ݿ�������ת��Ϊjava����
			sql = con.prepareStatement("select*from students where num = " + id);
			res = sql.executeQuery();
			//������ҵ���Ϣ
			resPrint();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
//�鿴ĳ��ѧ��
class SearchGrade extends SearchOne {
	@Override
	public void resPrint() throws NumberFormatException, IOException, SQLException {
		System.out.println("��ѯѧ���ĳɼ�:1.���� 2.��ѧ 3.Ӣ��");
		System.out.print("������ѡ��");
		int k = Integer.parseInt(read.readLine());		
		if(res.next()){
			System.out.print("����:" + res.getString(3));
			if(k == 1){
				//�������ѧ����ĳ��ɼ�
				System.out.print("  ���ĳɼ�:" + res.getInt(6));
			}else if(k == 2){
				System.out.print("  ��ѧ�ɼ�:" + res.getInt(7));
			}else if(k == 3){
				System.out.print("  Ӣ��ɼ�:" + res.getInt(8));
			}else{
				System.out.print("û�ж�Ӧ�ɼ�");
			}
		} else {
			System.out.println("�Ҳ�����ѧ����Ϣ");
		}
	 }
}
//��Χ����
class SearchScope extends Search {
	
	public void select(Connection con) {
		String str = null;
		try {
			System.out.println("��ѯѧ���ĳɼ�:1.���� 2.��ѧ 3.Ӣ��");
			System.out.print("������ѡ��");
			int k = Integer.parseInt(read.readLine());
			if(k == 1){
				str = "Chinese";
			}else if(k == 2){
				str = "Math";
			}else if(k == 3){
				str = "English";
			}
			
			System.out.print("������ҷ�Χ");
			System.out.print("��ʼ��");
			int start = Integer.parseInt(read.readLine());
			System.out.print("������");
			int end = Integer.parseInt(read.readLine());
			
			sql = con.prepareStatement("select*from students where " + str +
									">" + start + " and " + str +  "<" + end);
			res = sql.executeQuery();
			while(res.next()) {
				resPrint();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
//����
class SearchSort extends Search {
	
	public void select(Connection con) {
		String str = null;
		
		try {
			System.out.print("�������������У�");
			str = read.readLine();
			sql = con.prepareStatement("select*from students order by " + str);
			res = sql.executeQuery();
			while(res.next()) {
				resPrint();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
//��ɾ�Ĳ����ĸ���
class Operation {
	PreparedStatement sql;
	BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
	String order = null;
	//ȡ�����������
	public void getOrder() throws Exception {
	}
	//ʵ��ѡ��Ĳ���
	public void operate(Connection con) throws Exception {
		getOrder();
		try {
			if(this.order != null){
				sql = con.prepareStatement(order);
				doIt(sql);
				sql.executeUpdate();
				System.out.println("�����ɹ�");
			} else {
				System.out.println("����ʧ��");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	//��������ݵĲ���
	public void doIt(PreparedStatement sql) throws Exception {
		
	}
}
//����ѧ��
class Add extends Operation {
	
	public void getOrder(){
		this.order = "insert into students values(?, ?, ?, ?, ?, ?, ?, ?)";
	}
	public void doIt(PreparedStatement sql) throws Exception {
		System.out.print("����ѧ��:");
		sql.setInt(1, Integer.parseInt(read.readLine()));
		System.out.print("��������:");
		sql.setString(3, read.readLine());
		System.out.print("����רҵ:");
		sql.setString(2, read.readLine());
		System.out.print("�����Ա�:");
		sql.setString(4, read.readLine());		
		System.out.print("����༶:");
		sql.setInt(5, Integer.parseInt(read.readLine()));
		System.out.print("�������ĳɼ�:");
		sql.setInt(6, Integer.parseInt(read.readLine()));
		System.out.print("������ѧ�ɼ�:");
		sql.setInt(7, Integer.parseInt(read.readLine()));
		System.out.print("����Ӣ��ɼ�:");
		sql.setInt(8, Integer.parseInt(read.readLine()));
	}
}
//ɾ��ѧ��
class Delete extends Operation {
	
	public void getOrder() throws Exception{
		System.out.print("����ɾ��ѧ����id:");
		int id = Integer.parseInt(read.readLine());
		this.order = "delete from students where num = " + id;
	}
}
//�޸�ѧ����Ϣ
class Alter extends Operation {
	int k;
	public void getOrder() throws Exception{
		String str = null;
		System.out.print("�����޸�ѧ����id:");
		int id = Integer.parseInt(read.readLine());
		System.out.println("�޸�ѧ����Ϣ:1.���� 2.��ѧ 3.Ӣ��");
		System.out.print("������ѡ��");
		k = Integer.parseInt(read.readLine());
		if(k == 1){
			str = "Chinese";
		}else if(k == 2){
			str = "Math";
		}else if(k == 3){
			str = "English";
		}		
		this.order = "update students set " + str + " = ? where num = " + id;
	}
	
	public void doIt(PreparedStatement sql) throws Exception {
		if(k == 1){
			System.out.print("�޸����ĳɼ�:");
		}else if(k == 2){
			System.out.print("������ѧ�ɼ�:");
		}else if(k == 3){
			System.out.print("����Ӣ��ɼ�:");
		}			
		sql.setInt(1, Integer.parseInt(read.readLine()));
	}
}

