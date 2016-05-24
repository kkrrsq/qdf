package com.qdf.db;

import java.util.List;

public interface Session {

	int save(Object obj);
	
	int delete(Object obj);
	
	int update(Object obj);
	
	Object findById(Class<?> clazz, Object id);
	
	List<Object> queryList(String sql, Object... objects);
	
	List<Object> queryPage(Class<?> clazz,int page,int pageSize);
	
	List<Object> queryPage(String sql,int page,int pageSize);
	
}
