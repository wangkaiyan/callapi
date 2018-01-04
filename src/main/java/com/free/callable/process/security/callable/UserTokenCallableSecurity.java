package com.free.callable.process.security.callable;

import com.alibaba.fastjson.JSON;
import com.free.callable.annotation.HTTP;
import com.free.callable.auth.service.AuthService;
import com.free.callable.auth.struct.AuthException;
import com.free.callable.auth.struct.AuthRequest;
import com.free.callable.auth.struct.AuthResponse;
import com.free.callable.auth.struct.TokenRequest;
import com.free.callable.process.GlobalInstance;
import com.free.callable.process.print.AbstractProtocolPrint;
import com.free.framework.common.constants.HttpStatus;
import com.free.framework.common.exception.ServiceException;
import com.free.framework.common.util.QDStringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class UserTokenCallableSecurity extends CallableSecurity {

	
	public String generatorToken(UserToken tb) {
		
		Map<String, String> map = new HashMap<>();
		
		map.put("data", JSON.toJSONString(tb));
		
		TokenRequest tokenRequest = new TokenRequest(
				tb.getUserId() == null ? "" : tb.getUserId(),
				tb.getName() == null ? "" : tb.getName(), 
				tb.getSourceType() == null ? "" : tb.getSourceType().toString(), 
				String.valueOf(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000), 
				map
		);
		
		try {
			return AuthService.generateToken(tokenRequest);
		} catch (AuthException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public UserToken checkCallableSecurity(AbstractProtocolPrint print,
			Method targetMethod, HttpServletRequest request,
			HttpServletResponse response) throws ServiceException {
		
		String token = (String) GlobalInstance.getTransportSecurity().getParameter(request, "userToken");
		
		if(QDStringUtil.isEmpty(token)) {
			throw new ServiceException(HttpStatus.UNAUTHORIZED);
		}
		
		AuthRequest authRequest = new AuthRequest(token);
		
		AuthResponse authResponse;
		
		try {
			authResponse = AuthService.auth(authRequest);
		} catch (AuthException e) {
			throw new ServiceException(HttpStatus.UNAUTHORIZED);
		}
		
		if(authResponse.getIs_expire()) {
			
			HTTP http = targetMethod.getAnnotation(HTTP.class);

			if(http.isNeedReLoginWhenExpire()) {
				//重新登录
				throw new ServiceException(HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		Object data = authResponse.getAttribute("data");
		
		UserToken tokenBean = JSON.parseObject(data.toString(), UserToken.class);

		return tokenBean;
	}

}
