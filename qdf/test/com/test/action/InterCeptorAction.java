package com.test.action;

import com.qdf.annotation.Action;
import com.qdf.annotation.Interceptor;
import com.qdf.annotation.Skip;
import com.qdf.core.QdfAction;
import com.qdf.servlet.IRequest;
import com.qdf.servlet.IResponse;
import com.test.interceptor.ClassInterceptor;
import com.test.interceptor.ClassInterceptor2;

/**
 * 拦截器测试
 * @author xiezq
 *
 */
@Action(url = "/inter")
@Interceptor({ClassInterceptor.class,ClassInterceptor2.class})
public class InterCeptorAction implements QdfAction {

	
	@Override
	public void execute(IRequest request, IResponse response) {
		System.out.println("execute");
	}
	
	
	public void i1(IRequest request, IResponse response) {
		System.out.println("i1");
	}
	
	@Skip
	public void i2(IRequest request, IResponse response) {
		System.out.println("i2");
	}

}
