package com.test.action;

import com.qdf.annotation.Action;
import com.qdf.annotation.Interceptor;
import com.qdf.annotation.TxLevel;
import com.qdf.core.QdfAction;
import com.qdf.db.Tx;
import com.qdf.servlet.IRequest;
import com.qdf.servlet.IResponse;
import com.test.service.UserService;
import com.test.service.impl.UserServiceImpl;

/**
 * 事务测试
 * @author xiezq
 *
 */
@Action(url="tx")
public class TxAction implements QdfAction {

	private UserService us = new UserServiceImpl();
	
	@Override
	public void execute(IRequest request, IResponse response) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 声明式事务测试
	 * 在配置文件中配置
	 * @param request
	 * @param response
	 */
	public void save(IRequest request, IResponse response) {
		us.tx();
	}
	
	/**
	 * 注解事务
	 * @param request
	 * @param response
	 */
	@Interceptor(Tx.class)
	@TxLevel(2)
	public void tx1(IRequest request, IResponse response) {
		us.tx();
	}
	
	
	/**
	 * 代码实现事务
	 * 
	 * @param request
	 * @param response
	 */
	public void tx2(IRequest request, IResponse response) {
		/*  dao层代码
		    DbUtil.tx(()->{
			User u1 = new User();
			u1.setId("7777");
			u1.setName("7777");
			SessionFactory.getSession().save(u1);
			return false;
		} , 2);
		*/
		us.tx2();
	}

}
