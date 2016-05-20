package com.qdf.interceptor;

import com.qdf.core.Invocation;

/**
 * 拦截器接口
 * @author xiezq
 *
 */
public interface QdfInterceptor {

	public abstract void intercept(Invocation in);
	
}
