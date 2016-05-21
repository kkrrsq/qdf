package com.test.interceptor;

import com.qdf.core.Invocation;
import com.qdf.interceptor.QdfInterceptor;

public class GlobalIntercep implements QdfInterceptor {

	@Override
	public void intercept(Invocation in) {

		System.out.println("global before");
		in.invoke();
		System.out.println("global after");
	}

}
