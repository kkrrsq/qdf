package com.qdf.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qdf.annotation.Interceptor;
import com.qdf.interceptor.QdfInterceptor;
import com.qdf.model.Action;
import com.qdf.servlet.IRequest;
import com.qdf.servlet.IResponse;

/**
 * Action代理类
 * 执行Action中的方法
 * @author xiezq
 *
 */
public class ActionHandle {

	public void handle(Action actionModel,String methodName,IRequest request,
			IResponse response) {
		try {

			QdfAction action = (QdfAction) actionModel.getQdfAction().newInstance();
			
			
			Method method = actionModel.getMethod();
			
			Object [] objects = new Object[]{request,response};
			
			new Invocation(actionModel, method, objects).invoke();
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
}
