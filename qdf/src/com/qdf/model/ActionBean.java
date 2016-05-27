package com.qdf.model;

import java.lang.reflect.Method;

import com.qdf.core.QdfAction;
import com.qdf.interceptor.QdfInterceptor;

/**
 * 保存Action中方法的信息,如拦截器、url等
 * ActionBean对应QdfAction中的一个方法
 * 如:UserAction.add()方法对应一个ActionBean用于保存该方法的信息
 * @author xiezq
 *
 */
public class ActionBean {

	private Class<?> qdfAction;
	private Method method;
	private QdfInterceptor []interceptors;
	private String url;
	
	public ActionBean(Class<?> qdfAction, Method method, QdfInterceptor[] interceptors, String url) {
		this.qdfAction = qdfAction;
		this.method = method;
		this.interceptors = interceptors;
		this.url = url;
	}
	
	
	public <T> Class<T> getQdfAction() {
		return (Class<T>) qdfAction;
	}


	public void setQdfAction(Class<?> qdfAction) {
		this.qdfAction = qdfAction;
	}


	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public QdfInterceptor[] getInterceptors() {
		return interceptors;
	}
	public void setInterceptors(QdfInterceptor[] interceptors) {
		this.interceptors = interceptors;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
