package com.test.interceptor;

import com.qdf.interceptor.QdfInterceptor;

public class MyInterceptor implements QdfInterceptor{

	@Override
	public void before() {
		System.out.println("*******before*********");
	}

	@Override
	public void after() {
		System.out.println("*******after*********");
	}

	
}
