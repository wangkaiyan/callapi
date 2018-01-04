package com.free.callable.process.security.transport;


import com.alibaba.fastjson.JSON;
import com.free.callable.exception.CallableException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class TransportSecurity {

	public static final String requestKey = "body";

	
	/**
	 * 判断传输参数是不是经过加密的
	 * @param request
	 * @return
	 */
	public abstract boolean isSecueTransport(HttpServletRequest request);

	
	/**
	 * 凡是经过安全传输的参数都是从Attribute中获取， 否则就是从Parameter中获取
	 * @param request
	 * @param key
	 * @return
	 */
	public abstract Object getParameter(HttpServletRequest request, String key);
	
	public Object getBody(HttpServletRequest request) {
		
		return getParameter(request, requestKey);
		
	}

	public Object getParamInBody(HttpServletRequest request ,String key) {
		Object object = getParameter(request, requestKey);
		Object param =JSON.parseObject(object.toString()).get(key);
		//Object param = JSONObject.parseObject(JSON.toJSONString(object)).get(key);
		return param;

	}
	
	public abstract String response(
			HttpServletRequest request,
			HttpServletResponse response,
			String source) throws CallableException;
	
	
	public abstract void request(HttpServletRequest request) throws CallableException;
}
