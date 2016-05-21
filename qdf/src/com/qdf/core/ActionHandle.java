package com.qdf.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qdf.annotation.Interceptor;
import com.qdf.interceptor.QdfInterceptor;
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

	public void handle(ActionBean actionBean,String methodName,IRequest request,
			IResponse response) {
		try {

			QdfAction action = (QdfAction) actionBean.getQdfAction().newInstance();
			
			
			Method method = actionBean.getMethod();
			
			Object [] objects = new Object[]{request,response};
			
			new Invocation(actionBean, method, objects).invoke();
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
}
