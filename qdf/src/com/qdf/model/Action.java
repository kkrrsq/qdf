package com.qdf.model;

import java.lang.reflect.Method;

import com.qdf.core.QdfAction;
import com.qdf.interceptor.QdfInterceptor;

/**
 * 保存Action的信息
 * action对应QdfAction中的一个方法
 * 如:/user/add -> UserAction.add()
 * @author xiezq
 *
 */
public class Action {

	private Class<?> qdfAction;
	private Method method;
	private QdfInterceptor []interceptors;
	private String url;
	
	public Action(Class<?> qdfAction, Method method, QdfInterceptor[] interceptors, String url) {
		this.qdfAction = qdfAction;
		this.method = method;
		this.interceptors = interceptors;
		this.url = url;
	}
	
	
	public Class<?> getQdfAction() {
		return qdfAction;
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
