package com.wbja.stone.ydt.entity;

public class Friend {

	private String sortLetters;  //显示数据拼音的首字母
	private int  id;
	private String id_user;
	private String id_friend;
	private String tag;
	private int is_pass;
	private String displayname_friend;
	private String displayname_user;
	
	
	public int getId() {
		return id;
	}
	public void setId(int  id) {
		this.id = id;
	}
	public String getId_user() {
		return id_user;
	}
	public void setId_user(String id_user) {
		this.id_user = id_user;
	}
	public String getId_friend() {
		return id_friend;
	}
	public void setId_friend(String id_friend) {
		this.id_friend = id_friend;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getIs_pass() {
		return is_pass;
	}
	public void setIs_pass(int is_pass) {
		this.is_pass = is_pass;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	public String getDisplayname_friend() {
		return displayname_friend;
	}
	public void setDisplayname_friend(String displayname_friend) {
		this.displayname_friend = displayname_friend;
	}
	public String getDisplayname_user() {
		return displayname_user;
	}
	public void setDisplayname_user(String displayname_user) {
		this.displayname_user = displayname_user;
	}
	
	

}
