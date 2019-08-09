package com.springboot.entity;

public class GeomPipe {

	private int id;
	private String smhNo; // 管道起始井号
	private String fmhNo; // 管道终止井号

	private double smhX; // 管道起始X坐标
	private double smhY; // 管道起始Y坐标
	private double smhH; // 管道起始Z坐标
	private double fmhX; // 管道终止X坐标
	private double fmhY; // 管道终止Y坐标
	private double fmhH; // 管道终止Z坐标
	private double actualX1; // 实际坐标x1
	private double actualY1; // 实际坐标y1
	private double actualX2; // 实际坐标x2
	private double actualY2; // 实际坐标y2
	private String smhGradeA;
	private String smhGradeB;
	private String fmhGradeA;
	private String fmhGradeB;

	private double grade; // 管道等级
	private double score; // 管道评分
	private Pipe pipe;
	private GeomProject geomProject;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSmhNo() {
		return smhNo;
	}

	public void setSmhNo(String smhNo) {
		this.smhNo = smhNo;
	}

	public String getFmhNo() {
		return fmhNo;
	}

	public void setFmhNo(String fmhNo) {
		this.fmhNo = fmhNo;
	}

	public double getSmhX() {
		return smhX;
	}

	public void setSmhX(double smhX) {
		this.smhX = smhX;
	}

	public double getSmhY() {
		return smhY;
	}

	public void setSmhY(double smhY) {
		this.smhY = smhY;
	}

	public double getSmhH() {
		return smhH;
	}

	public void setSmhH(double smhH) {
		this.smhH = smhH;
	}

	public double getFmhX() {
		return fmhX;
	}

	public void setFmhX(double fmhX) {
		this.fmhX = fmhX;
	}

	public double getFmhY() {
		return fmhY;
	}

	public void setFmhY(double fmhY) {
		this.fmhY = fmhY;
	}

	public double getFmhH() {
		return fmhH;
	}

	public void setFmhH(double fmhH) {
		this.fmhH = fmhH;
	}

	public double getActualX1() {
		return actualX1;
	}

	public void setActualX1(double actualX1) {
		this.actualX1 = actualX1;
	}

	public double getActualY1() {
		return actualY1;
	}

	public void setActualY1(double actualY1) {
		this.actualY1 = actualY1;
	}

	public double getActualX2() {
		return actualX2;
	}

	public void setActualX2(double actualX2) {
		this.actualX2 = actualX2;
	}

	public double getActualY2() {
		return actualY2;
	}

	public void setActualY2(double actualY2) {
		this.actualY2 = actualY2;
	}

	public String getSmhGradeA() {
		return smhGradeA;
	}

	public void setSmhGradeA(String smhGradeA) {
		this.smhGradeA = smhGradeA;
	}

	public String getSmhGradeB() {
		return smhGradeB;
	}

	public void setSmhGradeB(String smhGradeB) {
		this.smhGradeB = smhGradeB;
	}

	public String getFmhGradeA() {
		return fmhGradeA;
	}

	public void setFmhGradeA(String fmhGradeA) {
		this.fmhGradeA = fmhGradeA;
	}

	public String getFmhGradeB() {
		return fmhGradeB;
	}

	public void setFmhGradeB(String fmhGradeB) {
		this.fmhGradeB = fmhGradeB;
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

	public Pipe getPipe() {
		return pipe;
	}

	public void setPipe(Pipe pipe) {
		this.pipe = pipe;
	}

	public GeomProject getGeomProject() {
		return geomProject;
	}

	public void setGeomProject(GeomProject geomProject) {
		this.geomProject = geomProject;
	}
}
