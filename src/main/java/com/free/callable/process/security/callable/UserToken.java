package com.free.callable.process.security.callable;

import java.io.Serializable;

public class UserToken extends SecurityObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 495004200586315217L;

	private String userId;
	
	private String name;
	
	private Integer sourceType;
	
	public UserToken() {

	}


	public UserToken(String userId, String name,
					 Integer sourceType) {
		super();
		this.userId = userId;
		this.name = name;
		this.sourceType = sourceType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Integer getSourceType() {
		return sourceType;
	}


	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}


	@Override
	public String toString() {
		return "UserToken [ userId=" + userId
				+ ", name=" + name + ", sourceType=" + sourceType + "]";
	}
}
