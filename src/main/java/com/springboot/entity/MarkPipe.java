package com.springboot.entity;


public class MarkPipe {

	private int id;
	private double pipescore;
	private double itemscore;
	private double score1;
	private double score2;
	private String remark;

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
