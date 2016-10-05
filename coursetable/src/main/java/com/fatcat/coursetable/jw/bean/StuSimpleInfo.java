package com.fatcat.coursetable.jw.bean;

import java.io.Serializable;

/**
 * 学生概要信息
 * @author EsauLu
 *
 */
public class StuSimpleInfo implements Serializable {
	private String name;
	private String id;
	private String department;
	private String major;
	private String classNum;
	public StuSimpleInfo() {
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getClassNum() {
		return classNum;
	}
	public void setClassNum(String classNum) {
		this.classNum = classNum;
	}

}
