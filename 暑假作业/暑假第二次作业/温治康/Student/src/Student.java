import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Student {
	//绑定输入流
	static BufferedReader myin = new BufferedReader(new InputStreamReader(System.in));
	static Connection con;
	static PreparedStatement sql;
	static ResultSet res;
	
	public static void main(String args[]) {
		
		short num = 0;
		Student student = new Student();
		con = Connect.getConnection();
		do { 
			System.out.print("search(all):1	serarch(part):2\n"
					+ "searchAll(condition):3	addStu:4\n"
					+ "modefyStu:5	deleteStudent:6\n"
					+ "searchByArea:7	exit:0	input:");
			
			try {
				num = Short.parseShort(myin.readLine());
			} catch (NumberFormatException | IOException e1) {
				// TODO Auto-generated catch block
				return;
			}// 读取字符串并且转换为short
			switch (num) {
			case 1:
				student.searchAll();//查看某同学的全部信息（基本信息+学科成绩）
				break;
			case 2:
				student.searchPart();//查看某同学某一科目成绩
				break;
			case 3:
				student.searchByCon();//按学科成绩、性别等条件进行排序显示
				break;
			case 4:
				try {
					student.addStu();//增加某一同学信息
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 5:
				student.modefyStu();//修改同学信息
				break;
			case 6:
				student.deleteStu();//删除某一同学信息
				break;
			case 7:
				student.seatchStuByArea();
			default:
				break;
			}
		} while (num != 0);
		
		Connect.closeConnexction(con);//关闭
	}
	
	public void searchAll() {
		String line = null;
		try {
			sql = con.prepareStatement("select * from student where stu_num = ?");
			System.out.print("请输入学号(若不知道请直接回车)：");
			line = myin.readLine();
			if (line.equals("") == true) {//如果为空串
				sql.setInt(1, 88888);
			} else {
				sql.setInt(1, Integer.parseInt(line));
			}
			
			res = sql.executeQuery();
			if (res.next() == false) {
				System.out.println("不存在当前行！");
			} else {
				System.out.println("学号：" + res.getString("stu_num") + "|姓名：" + res.getString("name")
				+ "|性别：" + res.getString("sex") + "|班级：" + res.getString("class") + "|专业：" + res.getString("major")
				+ "|语文：" + res.getFloat("chinese") + "|数学：" + res.getFloat("math") + "|英语：" + res.getFloat("english"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void searchPart() {
		String line = null;
		try {
			System.out.print("请输入你要查询的科目：");
			String major = myin.readLine();
			sql = con.prepareStatement("select * from student where stu_num = ?");
			System.out.print("请输入id(若不知道请直接回车)：");
			line = myin.readLine();
			if (line.equals("") == true) {//如果为空串
				sql.setInt(1, 88888);
			} else {
				sql.setInt(1, Integer.parseInt(line));
			}
			
			res = sql.executeQuery();
			if (res.next() == false) {
				System.out.println("不存在当前行！");
			} else {
				System.out.println("学号：" + res.getString("stu_num") + "|姓名：" + res.getString("name")
				+ "|" + major + "：" + res.getFloat(major));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void searchByCon() {
		try {
			System.out.print("请输入你要排序的项目：");
			String order = myin.readLine();
			sql = con.prepareStatement("select * from student order by " + order);
		
			res = sql.executeQuery();
			if (res.next() == false) {
				System.out.println("不存在当前行！");
			} else {
				do {
				System.out.println("学号：" + res.getString("stu_num") + "|姓名：" + res.getString("name") + "|性别：" + res.getString("sex")
				+ "|班级：" + res.getString("class") + "|专业：" + res.getString("major") + "|语文：" + res.getFloat("chinese")
				+ "|数学：" + res.getFloat("math") + "|英语：" + res.getFloat("english"));
				} while (res.next() == true);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addStu() throws SQLException, IOException {
		sql = con.prepareStatement("insert into student values(NULL, ?, ?, ?, ?, ?, ?, ?)");
		
		System.out.print("请输入姓名：");
		sql.setString(1, myin.readLine());
		System.out.print("请输入性别：");
		sql.setString(2, myin.readLine());
		System.out.print("请输入班级：");
		sql.setString(3, myin.readLine());
		System.out.print("请输入专业：");
		sql.setString(4, myin.readLine());
		System.out.print("请输入语文成绩：");
		sql.setFloat(5, Float.parseFloat(myin.readLine()));
		System.out.print("请输入数学成绩：");
		sql.setFloat(6, Float.parseFloat(myin.readLine()));
		System.out.print("请输入英语成绩：");
		sql.setFloat(7, Float.parseFloat(myin.readLine()));
		
		sql.executeUpdate();
		System.out.println("插入成功！");
		
	    /*查询全部*/
		sql = con.prepareStatement("select * from student");
		res = sql.executeQuery();
		while (res.next()) {
			System.out.println("学号：" + res.getString("stu_num") + "|姓名：" + res.getString("name") + "|性别：" + res.getString("sex")
			+ "|班级：" + res.getString("class") + "|专业：" + res.getString("major") + "|语文：" + res.getFloat("chinese")
			+ "|数学：" + res.getFloat("math") + "|英语：" + res.getFloat("english"));
		}
	}

	public void modefyStu() {
		String line = null;
		try {
			String modefy = null;
			String text = null;
			System.out.print("请输入要修改信息的学员的学号：");
			line = myin.readLine();
			System.out.print("请问想修改什么（英文表示）？");
			modefy = myin.readLine();
			System.out.print("请输入新值：");
			text = myin.readLine();
			
			try {
				Float.parseFloat(text);//如果text不是字符串格式就处理catch语句
				sql = con.prepareStatement("update student set " + modefy + " = " + text + " where stu_num = ?");
			} catch (NumberFormatException e) {
				sql = con.prepareStatement("update student set " + modefy + " = \"" + text + "\" where stu_num = ?");
			}
			
			if (line.equals("") == true) {//如果为空串
				System.out.println("修改失败！");
				return;
			} else {
				sql.setInt(1, Integer.parseInt(line));
			}
			
			if (sql.executeUpdate() != 0) {
				System.out.println("修改成功!");
			}
			sql = con.prepareStatement("select * from student");
			res = sql.executeQuery();
			while (res.next()) {
				System.out.println("学号：" + res.getString("stu_num") + "|姓名：" + res.getString("name") + "|性别：" + res.getString("sex")
				+ "|班级：" + res.getString("class") + "|专业：" + res.getString("major") + "|语文：" + res.getFloat("chinese")
				+ "|数学：" + res.getFloat("math") + "|英语：" + res.getFloat("english"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteStu() {

		String line = null;
		try {
			sql = con.prepareStatement("delete from student where stu_num = ?");
			System.out.print("请输入要删除学员的学号：");
			line = myin.readLine();
			if (line.equals("") == true) {//如果为空串
				System.out.println("删除失败！");
				return;
			} else {
				sql.setInt(1, Integer.parseInt(line));
			}
			
			if (sql.executeUpdate() != 0) {
				System.out.println("删除成功!");
			}
			sql = con.prepareStatement("select * from student");
			res = sql.executeQuery();
			while (res.next()) {
				System.out.println("学号：" + res.getString("stu_num") + "|姓名：" + res.getString("name") + "|性别：" + res.getString("sex")
				+ "|班级：" + res.getString("class") + "|专业：" + res.getString("major") + "|语文：" + res.getFloat("chinese")
				+ "|数学：" + res.getFloat("math") + "|英语：" + res.getFloat("english"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void seatchStuByArea() {
		try {
			System.out.print("请输入范围：");
			String area = myin.readLine();
			sql = con.prepareStatement("select * from student where " + area);
		
			res = sql.executeQuery();
			if (res.next() == false) {
				System.out.println("不存在当前行！");
			} else {
				do {
				System.out.println("学号：" + res.getString("stu_num") + "|姓名：" + res.getString("name") + "|性别：" + res.getString("sex")
				+ "|班级：" + res.getString("class") + "|专业：" + res.getString("major") + "|语文：" + res.getFloat("chinese")
				+ "|数学：" + res.getFloat("math") + "|英语：" + res.getFloat("english"));
				} while (res.next() == true);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}