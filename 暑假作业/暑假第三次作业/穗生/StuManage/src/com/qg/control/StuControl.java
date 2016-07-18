package com.qg.control;

import java.sql.SQLException;
import java.util.List;

import com.qg.model.Stu;
import com.qg.model.StuOperate;

public class StuControl {
	
	public Stu get(int id) throws SQLException{
		StuOperate soperate=new StuOperate();
		return soperate.get(id);
	}
	
	public void add(Stu g) throws Exception{
		StuOperate soperate=new StuOperate();
		soperate.addStu(g);
	}
	
	public void update(Stu g) throws Exception{
		StuOperate soperate=new StuOperate();
		soperate.updateStu(g);
	}
	
	public void del(int id) throws SQLException{
		StuOperate soperate=new StuOperate();
		soperate.delStu(id);
	}

	public List<Stu> query() throws Exception {
		StuOperate soperate=new StuOperate();
		return soperate.query();
	}
	
}
