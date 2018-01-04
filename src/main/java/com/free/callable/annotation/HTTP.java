package com.free.callable.annotation;


import com.free.callable.process.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by  on 2016/6/2.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HTTP {
    /**
     * 调用方法别名
     * @return
     */
    String alias();


    /**
     * 支持的http请求方式
     * @return
     */
    HttpMethod[] supportMethod() default {HttpMethod.GET, HttpMethod.POST};

    /**
     * 是否需要接口认证
     * @return
     */
    boolean isRequireAuth() default false;

    /**
     * 认证过期时 是否需要重新登录
     * @return
     */
    boolean isNeedReLoginWhenExpire() default false;
}
