package com.organ.model;

public class TCutLogoTemp implements java.io.Serializable {

	private static final long serialVersionUID = 769503431794709638L;
	
	private int id;
	private int userId;
	private String logoName;
	private String isDel;
	
	public TCutLogoTemp() {
		super();
	}
	
	public TCutLogoTemp( int userIdInt, String logoName) {
		super();
		this.userId = userIdInt;
		this.logoName = logoName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getLogoName() {
		return logoName;
	}
	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}
	
}