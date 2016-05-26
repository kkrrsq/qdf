package com.test.service.impl;

import java.util.List;

import com.test.dao.UserDao;
import com.test.dao.impl.UserDaoImpl;
import com.test.model.User;
import com.test.service.UserService;

public class UserServiceImpl implements UserService {

	private UserDao userDao = new UserDaoImpl();

	@Override
	public int add(User user) {
		return userDao.add(user);
	}

	@Override
	public int delete(User user) {
		return userDao.delete(user);
	}

	@Override
	public int update(User user) {
		return userDao.update(user);
	}

	@Override
	public User getUserById(String id) {
		return userDao.getUserById(id);
	}

	@Override
	public List<User> list() {
		return userDao.list();
	}

	@Override
	public List<User> page(int page,int pageSize) {
		return userDao.page(page,pageSize);
	}

	@Override
	public void tx() {
		userDao.tx();
	}
	
	@Override
	public void tx2() {
		userDao.tx2();
	}
}
