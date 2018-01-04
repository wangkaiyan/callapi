package com.free.callable.process.security.callable;


import com.free.callable.process.print.AbstractProtocolPrint;
import com.free.framework.common.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public abstract class CallableSecurity {

	public abstract SecurityObject checkCallableSecurity(AbstractProtocolPrint print,
			Method targetMethod, HttpServletRequest request,
			HttpServletResponse response) throws ServiceException;
	
}
