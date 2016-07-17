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
		//显示所有的学生
		new SearchAll().select(con);
		System.out.println("1.查看某同学 2.查看某一成绩 3.排序显示 4.增加 5.修改 6.范围查找 7.删除");
		System.out.print("请输入选择：");
		int k = Integer.parseInt(read.readLine());
		//选择功能进行操作
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
		//调用对象函数实现功能
		if(search != null){
			search.select(con);
		}
		if(operation != null){
			operation.operate(con);
		}
	}  
}
//查找函数的父类
class Search {
	PreparedStatement sql;
	ResultSet res;
	BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
	//实现查找功能的函数接口
	public  void select(Connection con) {
		
	}
	//输出查找的信息
	public void resPrint() throws SQLException, NumberFormatException, IOException {
		
		if(res.next()){
			System.out.println("学号:" + res.getInt(1) + "  姓名:" + res.getString(3) +
				 			"  性别:" + res.getString(4) + "  专业:" + res.getString(2) + 
				 			"  班级:" + res.getInt(5) + "班" + "  语文成绩:" + res.getInt(6) + 
				 			"  数学成绩:" + res.getInt(7) + "  英语成绩:" + res.getInt(8));
		} else {
			//若没有查找成功
			System.out.println("找不到该学生信息");
		}
	 }
}
//查找所有的学生，输出学号姓名
class SearchAll extends Search {
	@Override
	public void select(Connection con){
		try{
			sql = con.prepareStatement("select*from students");
			res = sql.executeQuery();
			while(res.next()) {
				int num = res.getInt(1);
				String name = res.getString(3);
				System.out.println("学号:" + num + "  姓名: " + name);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
//输出某个学生的全部信息
class SearchOne extends Search {
	public void select(Connection con) {
		try {
			System.out.print("输入查询学生的id:");
			int id = Integer.parseInt(read.readLine());
			//将数据库操作语句转换为java语言
			sql = con.prepareStatement("select*from students where num = " + id);
			res = sql.executeQuery();
			//输出查找的信息
			resPrint();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
//查看某个学生
class SearchGrade extends SearchOne {
	@Override
	public void resPrint() throws NumberFormatException, IOException, SQLException {
		System.out.println("查询学生的成绩:1.语文 2.数学 3.英语");
		System.out.print("请输入选择：");
		int k = Integer.parseInt(read.readLine());		
		if(res.next()){
			System.out.print("姓名:" + res.getString(3));
			if(k == 1){
				//输出查找学生的某项成绩
				System.out.print("  语文成绩:" + res.getInt(6));
			}else if(k == 2){
				System.out.print("  数学成绩:" + res.getInt(7));
			}else if(k == 3){
				System.out.print("  英语成绩:" + res.getInt(8));
			}else{
				System.out.print("没有对应成绩");
			}
		} else {
			System.out.println("找不到该学生信息");
		}
	 }
}
//范围查找
class SearchScope extends Search {
	
	public void select(Connection con) {
		String str = null;
		try {
			System.out.println("查询学生的成绩:1.语文 2.数学 3.英语");
			System.out.print("请输入选择：");
			int k = Integer.parseInt(read.readLine());
			if(k == 1){
				str = "Chinese";
			}else if(k == 2){
				str = "Math";
			}else if(k == 3){
				str = "English";
			}
			
			System.out.print("输入查找范围");
			System.out.print("起始：");
			int start = Integer.parseInt(read.readLine());
			System.out.print("结束：");
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
//排序
class SearchSort extends Search {
	
	public void select(Connection con) {
		String str = null;
		
		try {
			System.out.print("输入排序命令行：");
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
//增删改操作的父类
class Operation {
	PreparedStatement sql;
	BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
	String order = null;
	//取得输入的命令
	public void getOrder() throws Exception {
	}
	//实现选择的操作
	public void operate(Connection con) throws Exception {
		getOrder();
		try {
			if(this.order != null){
				sql = con.prepareStatement(order);
				doIt(sql);
				sql.executeUpdate();
				System.out.println("操作成功");
			} else {
				System.out.println("操作失败");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	//具体对数据的操作
	public void doIt(PreparedStatement sql) throws Exception {
		
	}
}
//增加学生
class Add extends Operation {
	
	public void getOrder(){
		this.order = "insert into students values(?, ?, ?, ?, ?, ?, ?, ?)";
	}
	public void doIt(PreparedStatement sql) throws Exception {
		System.out.print("输入学号:");
		sql.setInt(1, Integer.parseInt(read.readLine()));
		System.out.print("输入姓名:");
		sql.setString(3, read.readLine());
		System.out.print("输入专业:");
		sql.setString(2, read.readLine());
		System.out.print("输入性别:");
		sql.setString(4, read.readLine());		
		System.out.print("输入班级:");
		sql.setInt(5, Integer.parseInt(read.readLine()));
		System.out.print("输入语文成绩:");
		sql.setInt(6, Integer.parseInt(read.readLine()));
		System.out.print("输入数学成绩:");
		sql.setInt(7, Integer.parseInt(read.readLine()));
		System.out.print("输入英语成绩:");
		sql.setInt(8, Integer.parseInt(read.readLine()));
	}
}
//删除学生
class Delete extends Operation {
	
	public void getOrder() throws Exception{
		System.out.print("输入删除学生的id:");
		int id = Integer.parseInt(read.readLine());
		this.order = "delete from students where num = " + id;
	}
}
//修改学生信息
class Alter extends Operation {
	int k;
	public void getOrder() throws Exception{
		String str = null;
		System.out.print("输入修改学生的id:");
		int id = Integer.parseInt(read.readLine());
		System.out.println("修改学生信息:1.语文 2.数学 3.英语");
		System.out.print("请输入选择：");
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
			System.out.print("修改语文成绩:");
		}else if(k == 2){
			System.out.print("输入数学成绩:");
		}else if(k == 3){
			System.out.print("输入英语成绩:");
		}			
		sql.setInt(1, Integer.parseInt(read.readLine()));
	}
}

