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
			//�������ݿ���������
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("���ݿ��������سɹ�");
		} catch(ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		try {
			//�������ݿ�
			conn = DriverManager.getConnection(jdbcUrl, username, password);
			System.out.println("���ݿ����ӳɹ�");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void main(String[] args) throws Exception {
		//�������ݿ�
		Connect c = new Connect();
		con = c.getConnect();
		//�����ݿ��е�ѧ�����в���
		Student stu = new Student();
		stu.show(con);
	}
	
}
