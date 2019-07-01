package com.springboot.entity;

import java.util.List;

public class GeomProject {

	private int id;
	private String extent;   	//项目范围
	private String appertain; 	//沙井附物
	private String coveshape;  //井盖形状
	private String covesize;   //井盖大小
	private String sandshape;  	//沙井形状
	private String sanddeep;  	//沙井深度
	private String surveyer;  	//测量人员
	private Project project;	//所属项目
	
	private String center;		//中心坐标
	private List<GeomPipe> geomPipes;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getExtent() {
		return extent;
	}
	public void setExtent(String extent) {
		this.extent = extent;
	}
	public String getSandshape() {
		return sandshape;
	}
	public void setSandshape(String sandshape) {
		this.sandshape = sandshape;
	}
	public String getCoveshape() {
		return coveshape;
	}
	public void setCoveshape(String coveshape) {
		this.coveshape = coveshape;
	}
	public String getAppertain() {
		return appertain;
	}
	public void setAppertain(String appertain) {
		this.appertain = appertain;
	}
	public String getSanddeep() {
		return sanddeep;
	}
	public void setSanddeep(String sanddeep) {
		this.sanddeep = sanddeep;
	}
	public String getCovesize() {
		return covesize;
	}
	public void setCovesize(String covesize) {
		this.covesize = covesize;
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
