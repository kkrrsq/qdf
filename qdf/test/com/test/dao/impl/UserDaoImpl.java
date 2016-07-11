package com.test.dao.impl;

import java.util.List;

import com.qdf.db.DbUtil;
import com.qdf.db.SessionFactory;
import com.test.dao.UserDao;
import com.test.model.User;

public class UserDaoImpl implements UserDao	{

	@Override
	public int add(User user) {
		return SessionFactory.getSession("ds1").save(user);
	}

	@Override
	public int delete(User user) {
		return SessionFactory.getSession().delete(user);
	}

	@Override
	public int update(User user) {
		return SessionFactory.getSession().update(user);
	}

	@Override
	public User getUserById(String id) {
		return SessionFactory.getSession().findById(User.class, id);
	}

	@Override
	public List<User> list() {
		return SessionFactory.getSession("ds1").queryList(User.class,"select * from user", new Object[]{});
	}

	@Override
	public List<User> page(int page,int pageSize) {
		return SessionFactory.getSession().queryPage(User.class, page,pageSize);
	}

	@Override
	public void tx() {
		User u1 = new User();
		u1.setId("7777");
		u1.setName("7777");
		SessionFactory.getSession().save(u1);
		
		int i = 1/0;
		
		User u2 = new User();
		u1.setId("8888");
		u1.setName("88888");
		SessionFactory.getSession().save(u2);
	}
	
	@Override
	public void tx2() {
		DbUtil.tx("ds1",2,()->{
			User u1 = new User();
			u1.setId("q1");
			u1.setName("q1");
			SessionFactory.getSession().save(u1);
			return false;
		} );
	}

}
