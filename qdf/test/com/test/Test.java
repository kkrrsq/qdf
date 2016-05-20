package com.test;

import java.lang.reflect.Method;

import com.qdf.servlet.IRequest;
import com.qdf.servlet.IResponse;

public class Test {

	
	public static void main(String[] args) throws Exception{
		
		Method []methods = UserAction.class.getMethods();
		for (Method method : methods) {
				if(method.getParameterTypes().length == 2 && method.getParameterTypes()[0] == IRequest.class && 
						method.getParameterTypes()[1] == IResponse.class) {
					
				}
		}
		
	}
}
