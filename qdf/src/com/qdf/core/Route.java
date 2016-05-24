package com.qdf.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.qdf.interceptor.InterceptorManage;
import com.qdf.interceptor.QdfInterceptor;
import com.qdf.model.ActionBean;
import com.qdf.servlet.IRequest;
import com.qdf.servlet.IResponse;
import com.qdf.util.JsonUtil;
import com.qdf.util.LogUtil;

/**
 * 路由
 * @author xiezq
 *
 */
public class Route {

	private Map<String, ActionBean> routeMap = new HashMap<>();
	
	private String actionPackage;
	
	public void setRoute(String url,ActionBean actionBean) {
		routeMap.put(url, actionBean);
	}
	
	public ActionBean getRoute(String url) {
		return routeMap.get(url);
	}
	
	public boolean contain(String url) {
		return this.routeMap.containsKey(url);
	}
	
	/**
	 * 扫描并注册所有的请求处理器。
	 */
	public void scanActions(){
		try{
			ImmutableSet<ClassInfo> classInfos = ClassPath.from(Thread.currentThread().getContextClassLoader()).getTopLevelClasses(actionPackage);
			for (ClassInfo classInfo : classInfos) {
				Class<?> clazz = classInfo.load();
				if(QdfAction.class.isAssignableFrom(clazz)) {
					//符合条件的Action
					com.qdf.annotation.Action action = clazz.getAnnotation(com.qdf.annotation.Action.class);
					if(null != action) {
						String baseurl = action.url();
						Method []methods = clazz.getMethods();
						for (Method method : methods) {
							if(method.getParameterTypes().length == 2 && method.getParameterTypes()[0] == IRequest.class && 
									method.getParameterTypes()[1] == IResponse.class) {
								//符合action的方法
								String url = baseurl + "/" + method.getName();
								
								QdfInterceptor []interceptors = InterceptorManage.me().getInterceptors(clazz, method);
								
								ActionBean actionBean = new ActionBean(clazz, method, interceptors, url);
								
								routeMap.put(url, actionBean);
							}
						}
					}
				}
			}
			LogUtil.info("添加路由:" + JsonUtil.toJsonString(routeMap.keySet()));
		}catch( IOException e ){
			throw new RuntimeException( e );
		}
	}

	public String getActionPackage() {
		return actionPackage;
	}

	public void setActionPackage(String actionPackage) {
		this.actionPackage = actionPackage;
	}

}
