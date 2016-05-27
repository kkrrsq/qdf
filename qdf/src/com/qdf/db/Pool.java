package com.qdf.db;

import java.sql.Connection;

/**
 * 连接池接口
 * @author xiezq
 *
 */
public interface Pool {

	Connection getConnection();
	
	String getDbType();
	
	Connection getThreadLocalConnection();
}
