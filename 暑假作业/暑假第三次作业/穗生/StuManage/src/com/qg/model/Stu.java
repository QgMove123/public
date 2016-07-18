package com.qg.model;

public class Stu {

	private int id;
	private String major;
	private String grade;
	private String name;
	private String sex;
	private String number;
	private double Chinese;
	private double Maths;
	private double English;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public double getChinese() {
		return Chinese;
	}

	public void setChinese(double chinese) {
		Chinese = chinese;
	}

	public double getMaths() {
		return Maths;
	}

	public void setMaths(double maths) {
		Maths = maths;
	}

	public double getEnglish() {
		return English;
	}

	public void setEnglish(double english) {
		English = english;
	}

	@Override
	public String toString() {
		return "Stu [编号=" + id + ", 专业=" + major + ", 班级="
				+ grade + ", 姓名=" + name + ", 性别=" + sex + ", 学号=" + number + 
				", 语文=" + Chinese + ", 数学=" + Maths + ", 英语=" + English + "]" ;
	}
}
