package student;

import java.sql.*;

public class Student {
	String major;
	String name;
	String sex;
	int cla;
	int num;
	int chinese;
	int math;
	int english;
	static Connection con;
	static PreparedStatement sql;
	static ResultSet res;
	class DuplicateException extends Exception{
		public DuplicateException(String ErrorMessage){
			super(ErrorMessage);
		}
	}
	public Connection getConnection(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("数据库驱动加载成功");
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			con=DriverManager.getConnection("jdbc:mysql://127.0.0.1/student_db","root","");
			System.out.println("数据库连接成功");
		}catch(SQLException e){
			e.printStackTrace();
		}
		return con;
	}
	public void add(int num,String major,String name,String sex,int cla,
			int chinese,int math,int english) throws DuplicateException{
		try{
			int flag = 0;
			sql = con.prepareStatement("select * from students");
			res = sql.executeQuery();
			while(res.next()){
				if(num == res.getInt("num"))flag = 1;
			}
			if(flag == 1){
				flag = 0;
				throw new DuplicateException("该同学已经存在");
			}
			sql = con.prepareStatement("insert into students values(?,?,?,?,?,?,?,?)");
			sql.setInt(1, num);
			sql.setString(2, major);
			sql.setString(3, name);
			sql.setString(4, sex);
			sql.setInt(5, cla);
			sql.setInt(6, chinese);
			sql.setInt(7, math);
			sql.setInt(8, english);
			sql.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public void del(int num){
		try{
			sql = con.prepareStatement("delete from students where num = ?");
			sql.setInt(1, num);
			sql.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public void uptodata(int num,String major,String name,String sex,int cla,
			int chinese,int math,int english){
		try{
			sql = con.prepareStatement("update students set major = ? where num = ?");
			sql.setString(1, major);
			sql.setInt(2, 1);
			sql.executeUpdate();
			sql = con.prepareStatement("update students set name = ? where num = ?");
			sql.setString(1, name);
			sql.setInt(2, 1);
			sql.executeUpdate();
			sql = con.prepareStatement("update students set sex = ? where num = ?");
			sql.setString(1, sex);
			sql.setInt(2, 1);
			sql.executeUpdate();
			sql = con.prepareStatement("update students set cla = ? where num = ?");
			sql.setInt(1, cla);
			sql.setInt(2, 1);
			sql.executeUpdate();
			sql = con.prepareStatement("update students set chinese = ? where num = ?");
			sql.setInt(2, chinese);
			sql.setInt(1, 1);
			sql.executeUpdate();
			sql = con.prepareStatement("update students set math = ? where num = ?");
			sql.setInt(1, math);
			sql.setInt(2, 1);
			sql.executeUpdate();
			sql = con.prepareStatement("update students set english = ? where num = ?");
			sql.setInt(2, english);
			sql.setInt(1, 1);
			sql.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public void show(){
		try{
			sql = con.prepareStatement("select * from students");
			res = sql.executeQuery();
			System.out.println("The data :");
			while(res.next()){
				System.out.print("ID : "+res.getInt(1)+" |");
				System.out.print("Major : "+res.getString("major")+" |");
				System.out.print("Name : "+res.getString("name")+" |");
				System.out.print("Sex : "+res.getString("sex")+" |");
				System.out.print("Class : "+res.getInt("cla")+" |");
				System.out.print("Chinese : "+res.getInt("chinese")+" |");
				System.out.print("Math : "+res.getInt("math")+" |");
				System.out.println("English : "+res.getInt("english"));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public void show(String name){
		try{
			sql = con.prepareStatement("select * from students where name like "+"'"+name+"'");
			res = sql.executeQuery();
			System.out.println("The data :");
			while(res.next()){
				System.out.print("ID : "+res.getInt(1)+" |");
				System.out.print("Major : "+res.getString("major")+" |");
				System.out.print("Name : "+res.getString("name")+" |");
				System.out.print("Sex : "+res.getString("sex")+" |");
				System.out.print("Class : "+res.getInt("cla")+" |");
				System.out.print("Chinese : "+res.getInt("chinese")+" |");
				System.out.print("Math : "+res.getInt("math")+" |");
				System.out.println("English : "+res.getInt("english"));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public void show(String name,String lesson){
		try{
			sql = con.prepareStatement("select "+lesson+" from students where name like "+"'"+name+"'");
			res = sql.executeQuery();
			while(res.next()){
				System.out.println(name+"-"+lesson+" : " +res.getString(lesson));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public void order(String choo){
		try{
			sql = con.prepareStatement("select * from students order by "+choo+" asc");
			res = sql.executeQuery();
			System.out.println("After order :");
			while(res.next()){
				System.out.print("ID : "+res.getInt(1)+" |");
				System.out.print("Major : "+res.getString("major")+" |");
				System.out.print("Name : "+res.getString("name")+" |");
				System.out.print("Sex : "+res.getString("sex")+" |");
				System.out.print("Class : "+res.getInt("cla")+" |");
				System.out.print("Chinese : "+res.getInt("chinese")+" |");
				System.out.print("Math : "+res.getInt("math")+" |");
				System.out.println("English : "+res.getInt("english"));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public void search(String choo,int down,int up){
		try{
			sql = con.prepareStatement("select * from students where "+choo+" between "+down+" and "+up);
			res = sql.executeQuery();
			System.out.println("After search :");
			while(res.next()){
				System.out.print("ID : "+res.getInt(1)+" |");
				System.out.print("Major : "+res.getString("major")+" |");
				System.out.print("Name : "+res.getString("name")+" |");
				System.out.print("Sex : "+res.getString("sex")+" |");
				System.out.print("Class : "+res.getInt("cla")+" |");
				System.out.print("Chinese : "+res.getInt("chinese")+" |");
				System.out.print("Math : "+res.getInt("math")+" |");
				System.out.println("English : "+res.getInt("english"));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		Student st = new Student();
		st.getConnection();
		try{
			st.del(1);
			st.del(2);
			st.add(1, "computer", "Mike", "male", 1, 89, 99, 98);
			st.add(2, "computer", "Lily", "male", 1, 88, 98, 97);
			st.add(3, "computer", "LiLei", "fema", 1, 100, 100, 100);
			st.show();
			st.del(3);
			st.show();
			st.uptodata(1, "chinese", "name", "sex", 1, 90, 100, 100);
			st.show("Mike");
			st.order("english");
			st.search("english",0,100);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
