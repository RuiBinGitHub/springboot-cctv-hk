package com.springboot.entity;

import java.util.List;


public class MarkProject {

	private int id;
	private double score1;
	private double score2;
	private Project project;
	private Person person;
	private String date;
	
	private List<MarkPipe> markPipes;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<MarkPipe> getMarkPipes() {
		return markPipes;
	}
	public void setMarkPipes(List<MarkPipe> markPipes) {
		this.markPipes = markPipes;
	}
}
