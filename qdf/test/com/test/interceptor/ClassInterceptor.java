package com.test.interceptor;

import com.qdf.core.Invocation;
import com.qdf.interceptor.QdfInterceptor;

public class ClassInterceptor implements QdfInterceptor{

	@Override
	public void intercept(Invocation in) {
		System.out.println("class-interceptor-befor");
		in.invoke();
		System.out.println("class-interceptor-after");
	}

}
