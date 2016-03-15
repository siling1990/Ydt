package com.wbja.stone.ydt.entity;

public class Patient {
	private int id;
	private int id_fav;
	private int id_user;
	private int usertype;
	private String name_doctor;
	private String mobile;
	
	private int id_local;
	private String sortLetters;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_fav() {
		return id_fav;
	}
	public void setId_fav(int id_fav) {
		this.id_fav = id_fav;
	}
	public int getId_user() {
		return id_user;
	}
	public void setId_user(int id_user) {
		this.id_user = id_user;
	}
	public int getUsertype() {
		return usertype;
	}
	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}
	public String getName_doctor() {
		return name_doctor;
	}
	public void setName_doctor(String name_doctor) {
		this.name_doctor = name_doctor;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getId_local() {
		return id_local;
	}
	public void setId_local(int id_local) {
		this.id_local = id_local;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	
	

}
