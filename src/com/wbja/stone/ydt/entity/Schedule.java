package com.wbja.stone.ydt.entity;


public class Schedule {
	private  int id;
	private int type;
	private String content;
	private String date;
	private String time;
	private String imgPath;
	private String voiPath;
	private String contel;
	private int id_user;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getContel() {
		return contel;
	}
	public void setContel(String contel) {
		this.contel = contel;
	}
	public String getVoiPath() {
		return voiPath;
	}
	public void setVoiPath(String voiPath) {
		this.voiPath = voiPath;
	}
	public int getId_user() {
		return id_user;
	}
	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	
}
