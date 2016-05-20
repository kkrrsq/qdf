package com.test;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.qdf.annotation.Action;
import com.qdf.annotation.Interceptor;
import com.qdf.core.Qdf;
import com.qdf.core.QdfAction;
import com.qdf.db.SessionFactory;
import com.qdf.servlet.IRequest;
import com.qdf.servlet.IResponse;
import com.qdf.util.JsonUtil;
import com.test.interceptor.ClassInterceptor;
import com.test.interceptor.ClassInterceptor2;
import com.test.interceptor.MyInterceptor;

@Action(url = "/user")
@Interceptor({ClassInterceptor.class,ClassInterceptor2.class})
public class UserAction implements QdfAction {

	@Override
	public void execute(IRequest request, IResponse response) {
		// TODO Auto-generated method stub
		System.out.println("user execute");
	}
	
	@Interceptor({MyInterceptor.class,MyInterceptor.class})
	public void hello(IRequest request, IResponse response) {
		System.out.println("hello");
	}
	
	public void add(IRequest request,IResponse response) {
		User user = (User) request.getBean(User.class);
		System.out.println(JsonUtil.toJsonString(user));
		SessionFactory.getSession().save(user);
	}
	
	public void delete(IRequest request,IResponse response) {
		User user = (User) request.getBean(User.class);
		SessionFactory.getSession().delete(user);
	}
	
	public void update(IRequest request,IResponse response) {
		User user = (User) request.getBean(User.class);
		SessionFactory.getSession().update(user);
	}
	
	public void find(IRequest request,IResponse response) {
		String id = request.getParameter("id");
		User user = (User) SessionFactory.getSession().findById(User.class, id);
		response.setDataByJsonCMD(user);
	}
	
	public void list(IRequest request,IResponse response) {
		List<Object> list = SessionFactory.getSession().queryList("select * from user", null);
		response.setDataByJsonCMD(list);
	}
	
	
	public void tx(IRequest request,IResponse response) {
		
		Connection connection = Qdf.me().getPool().getConnection();
		
		String sql = "update user set age = 10 where id = ?";
		
		try {
			
			connection.setAutoCommit(false);
			
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, "1");
			ps.executeUpdate();
			
			
			ps.setString(1, "2");
			ps.executeUpdate();
			
			connection.commit();
			System.out.println("事务提交");
		} catch (Exception e) {
		
			try {
				connection.rollback();
				System.out.println("事务回滚");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		
	}
}
