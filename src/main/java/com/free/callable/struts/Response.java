package com.free.callable.struts;


import com.free.framework.common.constants.HttpStatus;

import java.io.Serializable;

/**
 * Created by  on 2016/6/2.
 */
public class Response<T extends ResponseData> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2767300673516866302L;
	
	private int code = HttpStatus.OK.getStatusCode();

	private T data;

	public Response() {

	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	public T getData() {
		return data;
	}

	@Override
	public String toString() {
		return "Response [code=" + code + ", data=" + data + "]";
	}
	
}