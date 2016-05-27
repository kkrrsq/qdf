package com.qdf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存名称注解
 * 用于指定缓存名称
 * @author xiezq
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CacheName {

	/**
	 * 
	 * @return 缓存名(需要在cache.xml配置)
	 */
	String value();
	
}
