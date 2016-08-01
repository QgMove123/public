package student;

import java.sql.*;
import java.util.Scanner;

public class Student {
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
			System.out.println("Load datebase Successfully");
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			con=DriverManager.getConnection("jdbc:mysql://127.0.0.1/student_db","root","");
			System.out.println("Link to datebase Successfully");
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
		String major;
		String name;
		String sex;
		String lesson;
		String choo;
		int exit = 0;
		int up,down;
		int cla;
		int num;
		int chinese;
		int math;
		int english;
		Student st = new Student();
		String choose = new String();
		Scanner scan;
		st.getConnection();
		System.out.println("Add         /Add a  new student");
		System.out.println("Del         /Del an old student");
		System.out.println("Show        /Show the detail of a student");
		System.out.println("Update      /Update the data of a student");
		System.out.println("ShowGrade   /Show the grade of the lesson of a student");
		System.out.println("Order       /Order the data");
		System.out.println("Search      /Search the data");
		System.out.println("Help        /Get the help menu");
		System.out.println("Exit        /Exit this system");
		System.out.println("Attention : All of the lesson's name that you input must use small letter."
				+"The lessons inclde chinese , math and english.");
		while(exit != 1){
			try{
				scan= new Scanner(System.in);
				choose = "";
				System.out.print("Please input you choose : ");
				choose = scan.nextLine();
				switch(choose){
				case "Add":{
					System.out.print("Please input major : ");
					major = scan.nextLine();
					System.out.print("Please input name : ");
					name = scan.nextLine();
					System.out.print("Please input sex(male or fema) : ");
					sex =scan.nextLine();
					System.out.print("Please input class : ");
					cla = scan.nextInt();
					System.out.print("Please input ID : ");
					num = scan.nextInt();
					System.out.print("Please input grade of Chinese /0-100: ");
					chinese = scan.nextInt();
					System.out.print("Please input grade of Math : /0-100");
					math = scan.nextInt();
					System.out.print("Please input grade of English /0-100:　");
					english = scan.nextInt();
					if((sex.equals("male")||sex.equals("fema"))
							&&(cla>=0&&chinese>=0&&math>=0&&english>=0)
							&&(chinese<=100&&math<=100&&english<=100)){
						st.add(num, major, name, sex, cla, chinese, math, english);
						System.out.println("Add Successfully!");
					}
					else System.out.println("Something must be error!");
					break;
				}
				case "Del":{
					System.out.print("Please input the ID of the student who you want to delete : ");
					num = scan.nextInt();
					st.del(num);
					System.out.println("Delete successfully!");
					break;
				}
				case "Show":{
					System.out.print("Please input the name of the student who you want to konw his/her detail : ");
					name = scan.nextLine();
					st.show(name);
					break;
				}
				case "Update":{
					System.out.print("Please input the ID of the student who you want to update : ");
					num = scan.nextInt();
					scan.nextLine();
					System.out.print("Please input major : ");
					major = scan.nextLine();
					System.out.print("Please input name : ");
					name = scan.nextLine();
					System.out.print("Please input sex(male or fema) : ");
					sex =scan.nextLine();
					System.out.print("Please input class : ");
					cla = scan.nextInt();
					System.out.print("Please input grade of Chinese /0-100: ");
					chinese = scan.nextInt();
					System.out.print("Please input grade of Math /0-100: ");
					math = scan.nextInt();
					System.out.print("Please input grade of English /0-100:　");
					english = scan.nextInt();
					if((sex.equals("male")||sex.equals("fema"))
							&&(cla>=0&&chinese>=0&&math>=0&&english>=0)
							&&(chinese<=100&&math<=100&&english<=100)){
						st.uptodata(num, major, name, sex, cla, chinese, math, english);
						System.out.println("Updata Successfully!");
					}
					else System.out.println("Something must be error!");
					break;
				}
				case "ShowGrade":{
					System.out.print("Please input the name of the student who you want to konw his/her grades : ");
					name = scan.nextLine();
					System.out.print("Please input the name of the lesson : ");
					lesson = scan.nextLine();
					st.show(name, lesson);
					break;
				}
				case "Order":{
					System.out.println("The date will order by(You can input only:num,major,name,sex,cla,chinese,math,english) : ");
					choo = scan.nextLine();
					st.order(choo);
					break;
				}
				case "Search":{
					System.out.print("please input the lesson which you want to search: ");
					choo = scan.nextLine();
					System.out.print("please input the BottomNumber : ");
					down = scan.nextInt();
					System.out.print("please input the TopNumber : ");
					up = scan.nextInt();
					st.search(choo, down, up);
					break;
				}
				case "Help":{
					System.out.println("Add         /Add a  new student");
					System.out.println("Del         /Del an old student");
					System.out.println("Show        /Show the detail of a student");
					System.out.println("Update      /Update the data of a student");
					System.out.println("ShowGrade   /Show the grade of the lesson of a student");
					System.out.println("Order       /Order the data");
					System.out.println("Search      /Search the data");
					System.out.println("Help        /Get the help menu");
					System.out.println("Exit        /Exit this system");
					System.out.println("Attention : All of the lesson's name that you input must use small letter."
							+"The lessons inclde chinese , math and english.");
					break;
				}
				case "Exit":{
					System.out.print("Bye");
					scan.close();
					exit = 1;
					break;
				}
				default :{
					System.out.println("Error! please input rignt sentence.");
				}
				}
			}catch(Exception e){
				System.out.println("Something must be error!");
				continue;
			}
		}
	}
}
