package com.springboot.entity;

public class Operator {

	private int id;
	private String title;
	private String fullname;  
	private String sure;  
	private String name;
	private String chianame;
	private String nickname;
	private String membergrades;
	private String membernumber;
	private String date;
	private Company company;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getSure() {
		return sure;
	}
	public void setSure(String sure) {
		this.sure = sure;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChianame() {
		return chianame;
	}
	public void setChianame(String chianame) {
		this.chianame = chianame;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getMembergrades() {
		return membergrades;
	}
	public void setMembergrades(String membergrades) {
		this.membergrades = membergrades;
	}
	public String getMembernumber() {
		return membernumber;
	}
	public void setMembernumber(String membernumber) {
		this.membernumber = membernumber;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
}
