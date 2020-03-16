package com.springboot.entity;

import java.util.List;

public class GeomProject {

	private int id;
	private double x;
	private double y;
	private String extent; // 项目范围
	private String surveyer; // 测量人员
	private Project project; // 所属项目

	private String center; // 中心坐标
	private List<GeomPipe> geomPipes;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String getExtent() {
		return extent;
	}

	public void setExtent(String extent) {
		this.extent = extent;
	}

	public String getSurveyer() {
		return surveyer;
	}

	public void setSurveyer(String surveyer) {
		this.surveyer = surveyer;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public List<GeomPipe> getGeomPipes() {
		return geomPipes;
	}

	public void setGeomPipes(List<GeomPipe> geomPipes) {
		this.geomPipes = geomPipes;
	}
}
