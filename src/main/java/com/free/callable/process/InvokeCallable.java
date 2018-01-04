package com.free.callable.process;

import com.free.callable.process.print.AbstractProtocolPrint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by  on 2016/6/2.
 */
public abstract class InvokeCallable {

    protected abstract Object[] beforeInvoke(AbstractProtocolPrint print, Method targetMethod, Object securityObject, HttpServletRequest request, HttpServletResponse response) throws Exception;

    protected abstract Object invoke(Method targetMethod, Object[] targetMethodArguments, HttpServletRequest request, HttpServletResponse response) throws Exception;

    protected abstract Object afterInvoke(AbstractProtocolPrint print, Object value);
}
