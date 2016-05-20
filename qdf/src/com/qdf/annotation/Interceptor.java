package com.qdf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.qdf.interceptor.QdfInterceptor;


/**
 * 拦截器注解
 * @author xiezq
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Interceptor {

	Class<?> value();
}
