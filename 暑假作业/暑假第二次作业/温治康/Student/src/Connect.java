import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
	/*加载数据库驱动并连接*/
	public static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
            // or:
            // com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();
            // or：
            // new com.mysql.jdbc.Driver();
			System.out.println("数据库驱动加载成功");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wzk_db?characterEncoding=utf8&useSSL=false", "root", "123321123++");
			// MySQL的JDBC URL编写方式：jdbc:mysql://主机名称:连接端口/数据库的名称，账户名，密码
	        // 执行数据库操作之前要在数据库管理系统上创建一个数据库
			System.out.println("数据库连接成功");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return con;//返回连接对象
	}
	/*断开与数据库的连接*/
	public static void closeConnexction(Connection con) {
		try {
			con.close();//关闭与数据库的连接，会释放此对象的数据库和jdbc资源
			System.out.println("数据库断开成功");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
