package com.qdf.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Splitter;
import com.qdf.annotation.Interceptor;

public class InterceptorManage {

	private static final InterceptorManage _me = new InterceptorManage();
	
	private List<QdfInterceptor> globalInterceptors = new ArrayList<>();
	
	private InterceptorManage(){}
	
	public static InterceptorManage me() {
		return _me;
	}
	
	public void addGlobalInterceptor(QdfInterceptor interceptor) {
		this.globalInterceptors.add(interceptor);
	}
	
	public void addGlobalInterceptor(String classNames) {
		List<String> classNameList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(classNames);
		try {
			for (String className : classNameList) {
				QdfInterceptor interceptor =  (QdfInterceptor) Class.forName(className).newInstance();
				this.globalInterceptors.add(interceptor);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void removeGlobalInterceptor(QdfInterceptor interceptor) {
		this.globalInterceptors.remove(interceptor);
	}
	
	public QdfInterceptor[] getInterceptors(Class<?> clazz,Method method) {
		
		List<QdfInterceptor> interceptorList = new ArrayList<>();
		
		if(null != globalInterceptors) {
			//全局拦截器
			for (QdfInterceptor inter : globalInterceptors) {
				interceptorList.add(inter);
			}
		}
		
		Interceptor classInterceptorAno =  clazz.getAnnotation(Interceptor.class);
		if(null != classInterceptorAno) {
			Class<?> []interceptors =  classInterceptorAno.value();
			for (Class<?> interc : interceptors) {
				Class<QdfInterceptor> qcClass = (Class<QdfInterceptor>) interc;
				try {
					interceptorList.add(qcClass.newInstance());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		
		Interceptor methodInterceptorAno =  method.getAnnotation(Interceptor.class);
		if(null != methodInterceptorAno) {
			Class<?> []interceptors =  methodInterceptorAno.value();
			for (Class<?> interc : interceptors) {
				Class<QdfInterceptor> qcClass = (Class<QdfInterceptor>) interc;
				try {
					interceptorList.add(qcClass.newInstance());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		return interceptorList.toArray(new QdfInterceptor[interceptorList.size()]);
	}
	
	
}
 