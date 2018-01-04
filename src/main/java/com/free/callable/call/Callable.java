package com.free.callable.call;

import com.free.callable.annotation.HTTP;
import com.free.callable.annotation.Param;
import com.free.callable.exception.CallableException;
import com.free.callable.process.GlobalInstance;
import com.free.callable.process.HttpMethod;
import com.free.callable.process.InvokeCallable;
import com.free.callable.process.print.AbstractProtocolPrint;
import com.free.callable.process.security.callable.SecurityObject;
import com.free.callable.struts.Response;
import com.free.callable.struts.ResponseData;
import com.free.framework.common.api.struct.ReturnInfo;
import com.free.framework.common.api.struct.request.BaseRequest;
import com.free.framework.common.constants.HttpStatus;
import com.free.framework.common.exception.ServiceException;
import com.smart.validate.SmartValidate;
import com.smart.validate.exception.SmartValidateException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by  on 2016/6/2.
 */
public class Callable extends InvokeCallable {

    private static final Logger logger = Logger.getLogger("callable");

    @Autowired
    private Mapper mapper;

    public String call(AbstractProtocolPrint print,Method targetMethod, HttpServletRequest request,
                       HttpServletResponse response) throws Exception {

        if(isNotSupportHttpMethod(request.getMethod(), targetMethod)) {

            throw new CallableException("http method " + request.getMethod() + " not support ");
        }

        HTTP http = targetMethod.getAnnotation(HTTP.class);

        SecurityObject securityObject = null;
        String token = (String) GlobalInstance.getTransportSecurity().getParameter(request, "userToken");
        if(http.isRequireAuth() ) {
            securityObject = GlobalInstance.getCallableSecurity().checkCallableSecurity(print, targetMethod, request, response);
        }else if(StringUtils.isNotEmpty(token)){
            try {
                securityObject = GlobalInstance.getCallableSecurity().checkCallableSecurity(print, targetMethod, request, response);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        Object[] targetMethodArguments = beforeInvoke(print,targetMethod, securityObject, request, response);

        Object returnValue = invoke(targetMethod, targetMethodArguments, request, response);

        String returnString = afterInvoke( print,returnValue).toString();

        return returnString;
    }

    public boolean isNotSupportHttpMethod(String httpMethod, Method method) {
        HTTP http = method.getAnnotation(HTTP.class);

        HttpMethod[] supportMethod = http.supportMethod();
        for(HttpMethod m : supportMethod) {
            if(m.is(httpMethod)) {
                return false;
            }
        }
        return true;
    }


    @Override
    protected Object[] beforeInvoke(AbstractProtocolPrint print, Method targetMethod, Object securityObject, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Class<?>[] argumentClasses = targetMethod.getParameterTypes();
        Annotation[][] parameterAnnotations = targetMethod.getParameterAnnotations();

        Object[] arguments = new Object[argumentClasses.length];

        for(int i = 0; i < argumentClasses.length; i ++) {

            Class<?> argumentClass = argumentClasses[i];

            if(BaseRequest.class.isAssignableFrom(argumentClass)) {

                String value = (String) GlobalInstance.getTransportSecurity().getBody(request);

                if(value == null || value.trim().length() == 0) {
                    throw new CallableException("missing arguments body");
                }

                value = URLDecoder.decode(value, "utf-8");

                Object in = print.in(value, argumentClass);

                SmartValidate.validate(in);

                arguments[i] = in;
            }
            else if(HttpServletRequest.class.isAssignableFrom(argumentClass)) {
                arguments[i] = request;
            }
            else if(HttpServletResponse.class.isAssignableFrom(argumentClass)) {
                arguments[i] = response;
            }
            else if(SecurityObject.class.isAssignableFrom(argumentClass)) {
                arguments[i] = securityObject;
            }else if(parameterAnnotations[i].length>=1 && parameterAnnotations[i][0].annotationType() == Param.class){
                Param param = (Param) parameterAnnotations[i][0];
                Object obj = GlobalInstance.getTransportSecurity().getParamInBody(request,param.value());
                if(param.required() && (obj == null || (obj instanceof  String && StringUtils.isEmpty(obj.toString())))) {
                    throw new SmartValidateException( param.value() + " is empty!");
                }
                 arguments[i] =obj;


            }else{
                arguments[i] = null;
            }
        }

        return arguments;
    }



    /**
     * 获取指定方法的参数名
     *
     * @param method 要获取参数名的方法
     * @return 按参数顺序排列的参数名列表
     */
    private static String[] getMethodParameterNamesByAnnotation(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return null;
        }
        String[] parameterNames = new String[parameterAnnotations.length];
        int i = 0;
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof Param) {
                    Param param = (Param) annotation;
                    parameterNames[i++] = param.value();
                }
            }
        }
        return parameterNames;
    }



    @Override
    protected Object invoke(Method targetMethod, Object[] arguments, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object returnValue = targetMethod.invoke(this, arguments);

        return returnValue;
    }

    @Override
    protected Object afterInvoke(AbstractProtocolPrint print, Object value) {
        if(value == null) {
            return "";
        }

        if(NOSERIALIZABLE.contains(value.getClass())) {

            return value;
        }

        return print.out(value);
    }

    private static final Set<Class<?>> NOSERIALIZABLE = new HashSet<>();

    static {

        NOSERIALIZABLE.add(int.class);
        NOSERIALIZABLE.add(long.class);
        NOSERIALIZABLE.add(float.class);
        NOSERIALIZABLE.add(double.class);
        NOSERIALIZABLE.add(char.class);
        NOSERIALIZABLE.add(boolean.class);
        NOSERIALIZABLE.add(byte.class);
        NOSERIALIZABLE.add(short.class);
        NOSERIALIZABLE.add(Short.class);
        NOSERIALIZABLE.add(Byte.class);
        NOSERIALIZABLE.add(Boolean.class);
        NOSERIALIZABLE.add(Character.class);
        NOSERIALIZABLE.add(Double.class);
        NOSERIALIZABLE.add(Float.class);
        NOSERIALIZABLE.add(Long.class);
        NOSERIALIZABLE.add(Integer.class);
        NOSERIALIZABLE.add(String.class);

    }

    private ReturnInfo toReturnInfo(Exception e) {
        ReturnInfo returnInfo = null;
        if (e instanceof ServiceException) {
            ServiceException se = (ServiceException) e;
            returnInfo = se.getReturnInfo();
        } else {
            e.printStackTrace();
            returnInfo = new ReturnInfo(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return returnInfo;
    }


    public Response<ResponseData> handleException(Exception e) {

        return handleException(ResponseData.class, e);

    }

    public <T extends ResponseData> Response<T> handleException(Class<T> clazz, Exception e) {
        T data = null;
        try {
            data = clazz.newInstance();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
        return handleException(data, e);
    }

    public <T extends ResponseData> Response<T> handleException(T data, Exception e) {
        ReturnInfo returnInfo = toReturnInfo(e);
        Response<T> response = new Response<T>();
        data.setMessage(returnInfo.getMessage());
        response.setCode(returnInfo.getCode());
        response.setData(data);
        return response;
    }



    public <T> List<T> transforList(Class<T> clazz, List<?> sources) {
        List<T> list = new ArrayList<>();
        if(sources == null) {
            return list;
        }
        for(Object o : sources) {
            T t = transfor(clazz, o);
            list.add(t);
        }
        return list;
    }

    public void transfor(Object target, Object source) {
        if(source == null || target == null) {
            return;
        }
        mapper.map(source, target);
    }

    public <T> T transfor(Class<T> target, Object source) {
        if(source == null) {
            return null;
        }
        return mapper.map(source, target);
    }


}
