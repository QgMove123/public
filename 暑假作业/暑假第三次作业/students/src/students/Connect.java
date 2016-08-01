package students;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Connect {
	private String jdbcUrl = "jdbc:mysql://localhost:3306/cy_stu";
	private String username = "root";
	private String password = "";
	static Connection con;

	public Connection getConnect() {
		Connection conn = null;
		try {
			//加载数据库驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("数据库驱动加载成功");
		} catch(ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		try {
			//连接数据库
			conn = DriverManager.getConnection(jdbcUrl, username, password);
			System.out.println("数据库连接成功");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void main(String[] args) throws Exception {
		//连接数据库
		Connect c = new Connect();
		con = c.getConnect();
		//对数据库中的学生进行操作
		Student stu = new Student();
		stu.show(con);
	}
	
}
