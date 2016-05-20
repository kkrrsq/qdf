package com.qdf.interceptor;
/**
 * 拦截器接口
 * @author xiezq
 *
 */
public interface QdfInterceptor {

	public abstract void before();
	
	public abstract void after();
}
