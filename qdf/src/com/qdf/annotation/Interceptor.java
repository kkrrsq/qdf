package com.qdf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * 拦截器注解
 * @author xiezq
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface Interceptor {

	//实现了qdfInterceptor接口的拦截器类
	Class<?>[] value();
}
