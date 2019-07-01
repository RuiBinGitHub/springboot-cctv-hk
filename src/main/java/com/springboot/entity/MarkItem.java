package com.springboot.entity;

public class MarkItem {

	private int id;
	private double score;
	private String remark;
	private Item item;
	private MarkPipe markPipe;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public MarkPipe getMarkPipe() {
		return markPipe;
	}
	public void setMarkPipe(MarkPipe markPipe) {
		this.markPipe = markPipe;
	}
}
