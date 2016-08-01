package com.qg.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.qg.db.DBUtil;
import com.qg.model.Stu;

public class StuOperate {
	
	public List<Stu> query() throws Exception{
		List<Stu> result=new ArrayList<Stu>();
		
		Connection conn=DBUtil.getConnection();
		StringBuilder sb=new StringBuilder();
		sb.append("select * from stu_info  ");
		
		PreparedStatement ptmt=conn.prepareStatement(sb.toString());
		
		ResultSet rs=ptmt.executeQuery();
		
		Stu g=null;
		while(rs.next()){
			g=new Stu();
			g.setId(rs.getInt("id"));
			g.setMajor(rs.getString("major"));
			g.setGrade(rs.getString("grade"));
			g.setName(rs.getString("name"));
			g.setSex(rs.getString("sex"));
			g.setNumber(rs.getString("number"));
			g.setChinese(rs.getDouble("Chinese"));
			g.setMaths(rs.getDouble("Maths"));
			g.setEnglish(rs.getDouble("English"));
			result.add(g);
		}
		return result;
	}
	
	public Stu get(int id) throws SQLException{
		
		//1.获取连接
		Connection conn=DBUtil.getConnection();
		String sql="" +
				" select * from stu_info " +
				" where id=? ";
		//2.预编译
		PreparedStatement ptmt=conn.prepareStatement(sql);
		//3.设置sql参数
		ptmt.setInt(1, id);
		//4.执行sql语句
		ResultSet rs=ptmt.executeQuery();
		
		Stu g=null;
		while(rs.next()){
			g=new Stu();
			g.setId(rs.getInt("id"));
			g.setMajor(rs.getString("major"));
			g.setGrade(rs.getString("grade"));
			g.setName(rs.getString("name"));
			g.setSex(rs.getString("sex"));
			g.setNumber(rs.getString("number"));
			g.setChinese(rs.getDouble("Chinese"));
			g.setMaths(rs.getDouble("Maths"));
			g.setEnglish(rs.getDouble("English"));		
		}
		return g;
	}
	
	
	public void addStu(Stu g) throws Exception{
		Connection conn=DBUtil.getConnection();
		String sql="" +
				"insert into stu_info" +
				"(major,grade,name,sex,number,Chinese,Maths,English)" +
				"values(" +
				"?,?,?,?,?,?,?,?)";
		PreparedStatement ptmt=conn.prepareStatement(sql);
		
		ptmt.setString(1, g.getMajor());
		ptmt.setString(2, g.getGrade());
		ptmt.setString(3, g.getName());
		ptmt.setString(4, g.getSex());
		ptmt.setString(5, g.getNumber());
		ptmt.setDouble(6, g.getChinese());
		ptmt.setDouble(7, g.getMaths());
		ptmt.setDouble(8, g.getEnglish());
	
		ptmt.execute();
	}
	public void updateStu(Stu g) throws SQLException{
		Connection conn=DBUtil.getConnection();
		String sql="" +
				" update stu_info " +
				" set major=?,grade=?,name=?,sex=?,number=?,Chinese=?, " +
				" Maths=?,English=? " +
				" where id=? ";
		PreparedStatement ptmt=conn.prepareStatement(sql);
	
		ptmt.setString(1, g.getMajor());
		ptmt.setString(2, g.getGrade());
		ptmt.setString(3, g.getName());
		ptmt.setString(4, g.getSex());
		ptmt.setString(5, g.getNumber());
		ptmt.setDouble(6, g.getChinese());
		ptmt.setDouble(7, g.getMaths());
		ptmt.setDouble(8, g.getEnglish());
		ptmt.setDouble(9, g.getId());
		
		ptmt.execute();
	}
	
	
	public void delStu(int id) throws SQLException{
		Connection conn=DBUtil.getConnection();
		String sql="" +
				" delete from stu_info " +
				" where id=? ";
		PreparedStatement ptmt=conn.prepareStatement(sql);
		
		ptmt.setInt(1, id);
		ptmt.execute();
	}
		
}
