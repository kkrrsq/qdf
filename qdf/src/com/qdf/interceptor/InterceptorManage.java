package com.qdf.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Splitter;
import com.qdf.annotation.Interceptor;
import com.qdf.annotation.Skip;
import com.qdf.util.JsonUtil;
import com.qdf.util.LogUtil;
import com.sun.swing.internal.plaf.metal.resources.metal;

import sun.util.resources.cldr.az.CalendarData_az_Cyrl_AZ;

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
				Class<?> clazz = Class.forName(className);
				if(QdfInterceptor.class.isAssignableFrom(clazz)) {
					QdfInterceptor interceptor =  (QdfInterceptor) clazz.newInstance();
					this.globalInterceptors.add(interceptor);
				}
			}
			LogUtil.info("添加全局拦截器:"+JsonUtil.toJsonString(classNameList));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void removeGlobalInterceptor(QdfInterceptor interceptor) {
		this.globalInterceptors.remove(interceptor);
	}
	
	public QdfInterceptor[] getInterceptors(Class<?> clazz,Method method) {
		
		List<QdfInterceptor> interceptorList = new ArrayList<>();
		
		Skip skipAtClass = clazz.getAnnotation(Skip.class);
		Skip skipAtMethod = method.getAnnotation(Skip.class);
		
		if(null == skipAtClass && null != globalInterceptors) {
			//全局拦截器
			for (QdfInterceptor inter : globalInterceptors) {
				interceptorList.add(inter);
			}
		}
		
		Interceptor classInterceptorAno =  clazz.getAnnotation(Interceptor.class);
		if(null == skipAtMethod && null != classInterceptorAno) {
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
 