package com.qdf.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.qdf.core.Qdf;

public class DruidPool implements Pool{

	private DruidDataSource dataSource = null;
	private ThreadLocal<Connection> connections = new ThreadLocal<Connection>();
	
	private static final DruidPool _me = new DruidPool();
	
	public static DruidPool me() {
		return _me;
	}
	
	private DruidPool(){
		dataSource = new DruidDataSource();
		dataSource.setUrl(Qdf.me().getConfig().getDBProperty("url").toString());
		dataSource.setUsername(Qdf.me().getConfig().getDBProperty("username").toString());
		dataSource.setPassword(Qdf.me().getConfig().getDBProperty("password").toString());
		dataSource.setMaxActive(Integer.parseInt(Qdf.me().getConfig().getDBProperty("maxActive").toString()));
	}
	
	@Override
	public Connection getConnection() {
		
		Connection conn = connections.get();
		
		try {
			if(null == conn || conn.isClosed()) {
				conn = dataSource.getConnection();
				connections.set(conn);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return conn;
	}

	@Override
	public String getDbType() {
		return dataSource.getDbType();
	}

	
}
