package com.qdf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * model注解
 * @author xiezq
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Model {

	/**
	 * 
	 * @return 主键名(默认为"id")
	 */
	String pk() default "id";
	
	/**
	 * 
	 * @return 表名
	 */
	String table();
}
