package com.qdf.db;

import java.sql.Connection;

import javax.sql.DataSource;

public interface Pool {

	Connection getConnection();
	
	String getDbType();
}
