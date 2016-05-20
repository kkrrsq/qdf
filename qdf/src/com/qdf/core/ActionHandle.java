package com.qdf.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qdf.annotation.Interceptor;
import com.qdf.interceptor.QdfInterceptor;
import com.qdf.servlet.IRequest;
import com.qdf.servlet.IResponse;

/**
 * Action代理类
 * 执行Action中的方法
 * @author xiezq
 *
 */
public class ActionHandle {

	public void handle(Class<?> clazz,String methodName,IRequest request,
			IResponse response) {
		try {

			QdfAction action = (QdfAction) clazz.newInstance();
			
			List<String> methodNameList = new ArrayList<>();
			Method []methods = clazz.getMethods();
			for (Method m : methods) {
				methodNameList.add(m.getName());
			}
			
			if(!methodNameList.contains(methodName)) {
				methodName = "execute";
			}
			
			Method method = clazz.getMethod(methodName, IRequest.class,IResponse.class);
			
			Interceptor interceptor = method.getAnnotation(Interceptor.class);
			if(interceptor != null) {
				Class<QdfInterceptor> interceptorClass = (Class<QdfInterceptor>) interceptor.value();
				QdfInterceptor qdfInterceptor = interceptorClass.newInstance();
				qdfInterceptor.before();
				method.invoke(action, request,response);
				qdfInterceptor.after();
			} else {
				method.invoke(action, request,response);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
}
