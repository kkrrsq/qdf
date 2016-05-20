package com.test.interceptor;

import com.qdf.annotation.GlobalInterceptor;
import com.qdf.core.Invocation;
import com.qdf.interceptor.QdfInterceptor;

@GlobalInterceptor
public class GlobalIntercep2 implements QdfInterceptor {

	@Override
	public void intercept(Invocation in) {

		System.out.println("global before 2");
		in.invoke();
		System.out.println("global after 2");
	}

}
