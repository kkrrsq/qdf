package com.qdf.core;

import java.lang.reflect.Method;

import com.qdf.interceptor.QdfInterceptor;
import com.qdf.model.ActionBean;
import com.qdf.servlet.IRequest;
import com.qdf.servlet.IResponse;



/**
 * 递归执行action和interceptor
 * @author xiezq
 *
 */
public class Invocation {

	private ActionBean actionBean;
	private Method method;
	private QdfInterceptor []interceptors;
	private Object[] args;
	//记录当前interceptor的下标
	private int index = 0;
	
	public Invocation(ActionBean actionBean,Method method,Object []args) {
		this.actionBean = actionBean;
		this.method = method;
		this.args = args;
		this.interceptors = actionBean.getInterceptors();
	}

	/**
	 * 递归,执行拦截器,并把自身传给拦截器,拦截器再调用invoke
	 */
	public void invoke() {
		
		if(index < interceptors.length) {
			//Interceptor还没执行完
			interceptors[index++].intercept(this);
		} else if(index++ == interceptors.length) {
			//执行action的方法
			try {
				method.invoke(actionBean.getQdfAction().newInstance(), args);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	} 
	
	public Method getMethod() {
		return method;
	}
	
	public <T> Class<T> getAction() {
		return actionBean.getQdfAction();
	}
	
	public IRequest getIRequest() {
		return (IRequest) args[0];
	}
	
	public IResponse getIResponse() {
		return (IResponse) args[1];
	}
}
