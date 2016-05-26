package com.test;


import java.util.List;

import org.omg.PortableInterceptor.USER_EXCEPTION;

import com.qdf.annotation.Action;
import com.qdf.annotation.CacheName;
import com.qdf.annotation.Interceptor;
import com.qdf.annotation.Skip;
import com.qdf.annotation.TxLevel;
import com.qdf.core.QdfAction;
import com.qdf.db.DbUtil;
import com.qdf.db.SessionFactory;
import com.qdf.db.Tx;
import com.qdf.log.ILogger;
import com.qdf.plugin.ehcache.CacheInterceptor;
import com.qdf.plugin.ehcache.CacheUtil;
import com.qdf.plugin.ehcache.DataLoader;
import com.qdf.servlet.IRequest;
import com.qdf.servlet.IResponse;
import com.qdf.util.JsonUtil;
import com.qdf.util.LogUtil;
import com.test.interceptor.ClassInterceptor;
import com.test.interceptor.ClassInterceptor2;
import com.test.interceptor.MyInterceptor;

@Skip
@Action(url = "/user")
@CacheName("/user")
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
		User user = request.getBean(User.class);
		
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
	
	@Interceptor(CacheInterceptor.class)
	public void find(IRequest request,IResponse response) {
		String id = request.getParameter("id");
		User user = SessionFactory.getSession().findById(User.class, id);
		response.setDataByJsonCMD(user);
	}
	
	/**
	 * cache
	 * @param request
	 * @param response
	 */
	public void list(IRequest request,IResponse response) {
		/*List<Object> list =  CacheUtil.get("/user/list", "list");
		if(list == null) {
			list = SessionFactory.getSession().queryList("select * from user", null);
			CacheUtil.put("/user/list", "list",list);
		}*/
		List<User> list = CacheUtil.get("/user", "/user/list",()->{
			return SessionFactory.getSession().queryList("select * from user", null);
		});
		response.setDataByJsonCMD(list);
	}
	
	@Skip
	@Interceptor(Tx.class)
	@TxLevel(2)
	public void tx(IRequest request,IResponse response) {
		
		UserService userService = new UserService();
		userService.tx();
	}
	
	@Skip
	@Interceptor(Tx.class)
	@TxLevel(2)
	public void saveUser(IRequest request,IResponse response) {
		User user = new User();
		user.setId("101");
		user.setAge(10);
		user.setName("xzq");
		user.setSex("男");
		SessionFactory.getSession().save(user);
		
		User user2 = new User();
		user2.setId("102");
		user2.setAge(11);
		user2.setName("xsy");
		user2.setSex("女");
		SessionFactory.getSession().save(user2);
	}
	
	@Skip
	public void log(IRequest request,IResponse response) {
	}
	
	
	public void redirect(IRequest request,IResponse responses) {
		responses.redirect("http://www.baidu.com");
	}
	
	public void forward(IRequest request,IResponse responses) {
		responses.forward("list");
	}
	
	public void page(IRequest request,IResponse response) {
		List<User> list = SessionFactory.getSession().queryPage("select * from user order by age", 1, 10);
		response.setDataByJsonCMD(list);
	}
	
	public void insert(IRequest request,IResponse response) {
		for(int i = 2;i<50;i++) {
			User user = new User();
			user.setId(i+"");
			user.setName("NO."+i);
			user.setAge(i);
			SessionFactory.getSession().save(user);
		}
	}
	
	/**
	 * 测试dbUtil的tx
	 * @param request
	 * @param response
	 */
	public void dbtx(IRequest request,IResponse response) {
		DbUtil.tx(() -> {
			User user = new User();
			user.setId("1111222");
			user.setName("xxx");
			SessionFactory.getSession().save(user);
			return false;
		}, 2);
	}
	
}
