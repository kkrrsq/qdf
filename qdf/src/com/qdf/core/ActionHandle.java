package com.qdf.core;

import java.lang.reflect.Method;

import com.qdf.model.ActionBean;
import com.qdf.servlet.IRequest;
import com.qdf.servlet.IResponse;

/**
 * Action代理类
 * 执行Action中的方法
 * @author xiezq
 *
 */
public class ActionHandle {

	/**
	 * 执行Action中的方法
	 * @param actionBean ActionBean
	 * @param methodName 方法名
	 * @param request	IRequest
	 * @param response	IResponse
	 */
	public void handle(ActionBean actionBean,String methodName,IRequest request,
			IResponse response) {
		try {

			Class<QdfAction> qdfClazz = actionBean.getQdfAction();
			
			QdfAction action = qdfClazz.newInstance();
			
			
			Method method = actionBean.getMethod();
			
			Object [] objects = new Object[]{request,response};
			
			new Invocation(actionBean, method, objects).invoke();
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
}
