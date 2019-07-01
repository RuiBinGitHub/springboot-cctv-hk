package com.springboot.entity;


public class GeomItem {

	private int id;
	private double x;  		//X坐标
	private double y;			//Y坐标
	private double grade;		//等级
	private double score; 		//分数
	private String picture;		//图片名称
	private String remarks;		//备注说明
	private Item item;			//所属记录
	private GeomPipe geomPipe;
	
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
	public double getGrade() {
		return grade;
	}
	public void setGrade(double grade) {
		this.grade = grade;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public GeomPipe getGeomPipe() {
		return geomPipe;
	}
	public void setGeomPipe(GeomPipe geomPipe) {
		this.geomPipe = geomPipe;
	}
}
