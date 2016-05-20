package com.qdf.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.qdf.annotation.Action;
import com.qdf.util.JsonUtil;

/**
 * 路由
 * @author xiezq
 *
 */
public class Route {

	private Map<String, Class<?>> routeMap = new HashMap<>();
	
	private String actionPackage;
	
	public void setRoute(String url,Class<?> action) {
		routeMap.put(url, action);
	}
	
	public Class<?> getRoute(String url) {
		return routeMap.get(url);
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
					Action action = clazz.getAnnotation(Action.class);
					if(null != action) {
						String url = action.url();
						routeMap.put(url, clazz);
					}
				}
			}
			System.out.println("添加路由:" + JsonUtil.toJsonString(routeMap));
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
