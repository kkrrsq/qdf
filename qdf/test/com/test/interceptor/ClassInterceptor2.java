package com.test.interceptor;

import com.qdf.core.Invocation;
import com.qdf.interceptor.QdfInterceptor;

public class ClassInterceptor2 implements QdfInterceptor{

	@Override
	public void intercept(Invocation in) {
		System.out.println("class-interceptor-befor2");
		in.invoke();
		System.out.println("class-interceptor-after2");
	}

}
