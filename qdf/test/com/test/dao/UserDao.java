package com.test.dao;

import java.util.List;

import com.test.model.User;

public interface UserDao {

	int add(User user);
	
	int delete(User user);
	
	int update(User user);
	
	User getUserById(String id);
	
	List<User> list();
	
	List<User> page(int page,int pageSize);
	
	void tx();

	void tx2();
	
}
