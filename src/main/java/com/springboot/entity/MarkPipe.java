package com.springboot.entity;


public class MarkPipe {

	private int id;
	private double pipescore;
	private double itemscore;
	private double score1;
	private double score2;
	private String piperemark;
	private String itemremark;
	private String remark1;
	private String remark2;

	private Pipe pipe;
	private MarkProject markProject;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPipescore() {
		return pipescore;
	}

	public void setPipescore(double pipescore) {
		this.pipescore = pipescore;
	}

	public double getItemscore() {
		return itemscore;
	}

	public void setItemscore(double itemscore) {
		this.itemscore = itemscore;
	}

	public double getScore1() {
		return score1;
	}

	public void setScore1(double score1) {
		this.score1 = score1;
	}

	public double getScore2() {
		return score2;
	}

	public void setScore2(double score2) {
		this.score2 = score2;
	}

	public Pipe getPipe() {
		return pipe;
	}

	public void setPipe(Pipe pipe) {
		this.pipe = pipe;
	}

	public MarkProject getMarkProject() {
		return markProject;
	}

	public void setMarkProject(MarkProject markProject) {
		this.markProject = markProject;
	}

	public String getPiperemark() {
		return piperemark;
	}

	public void setPiperemark(String piperemark) {
		this.piperemark = piperemark;
	}

	public String getItemremark() {
		return itemremark;
	}

	public void setItemremark(String itemremark) {
		this.itemremark = itemremark;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

}
