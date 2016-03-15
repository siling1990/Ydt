package com.wbja.stone.ydt.entity;

public class Registration {

	private int id;
	private int  id_patient;
	private int id_doctor;
	private String name_patient;
	private String name_doctor;
	private String mobile;
	private String vcode;
	private int is_pass;
	private String clinic_time;
	private int id_clinic_time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_patient() {
		return id_patient;
	}
	public void setId_patient(int id_patient) {
		this.id_patient = id_patient;
	}
	public int getId_doctor() {
		return id_doctor;
	}
	public void setId_doctor(int id_doctor) {
		this.id_doctor = id_doctor;
	}
	public String getName_patient() {
		return name_patient;
	}
	public void setName_patient(String name_patient) {
		this.name_patient = name_patient;
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
	public String getVcode() {
		return vcode;
	}
	public void setVcode(String vcode) {
		this.vcode = vcode;
	}
	public int getIs_pass() {
		return is_pass;
	}
	public void setIs_pass(int is_pass) {
		this.is_pass = is_pass;
	}
	public String getClinic_time() {
		return clinic_time;
	}
	public void setClinic_time(String clinic_time) {
		this.clinic_time = clinic_time;
	}
	public int getId_clinic_time() {
		return id_clinic_time;
	}
	public void setId_clinic_time(int id_clinic_time) {
		this.id_clinic_time = id_clinic_time;
	}
	
	
}
