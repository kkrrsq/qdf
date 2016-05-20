package com.qdf.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.qdf.interceptor.QdfInterceptor;
import com.qdf.model.Action;
import com.qdf.servlet.IRequest;
import com.qdf.servlet.IResponse;



/**
 * 执行action和interceptor
 * @author xiezq
 *
 */
public class Invocation {

	private Action action;
	private Method method;
	private QdfInterceptor []interceptors;
	private Object[] args;
	//记录当前interceptor的下标
	private int index = 0;
	
	public Invocation(Action action,Method method,Object []args) {
		this.action = action;
		this.method = method;
		this.args = args;
		this.interceptors = action.getInterceptors();
	}

	public void invoke() {
		
		if(index < interceptors.length) {
			//Interceptor还没执行完
			interceptors[index++].intercept(this);
		} else if(index++ == interceptors.length) {
			//执行action的方法
			try {
				method.invoke(action.getQdfAction().newInstance(), args);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	} 
	
}
