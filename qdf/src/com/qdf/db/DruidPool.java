package com.qdf.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.qdf.core.Qdf;

/**
 * druid连接池
 * @author xiezq
 *
 */
public class DruidPool implements Pool{

	private static final String DEFAULT_DS_NAME = "default_ds_name";
	//保存数据源
	private static final Map<String, DruidDataSource> DSMAP = new HashMap<>();
	//ThreadLocal保存连接,保证每个线程得到同一个连接,用于事务处理
	private ThreadLocal<Connection> connections = new ThreadLocal<Connection>();
	private static final String VALIDATION_QUERY = "select 1";
	
	private static final DruidPool _me = new DruidPool();
	
	public static DruidPool me() {
		return _me;
	}
	
	/**
	 * 初始化druid连接池
	 */
	private DruidPool(){
		List<String> dsList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(Qdf.me().getConfig().get("ds.list"));
		dsList.forEach(dsName -> {
			DruidDataSource dataSource = new DruidDataSource();
			dataSource.setUrl(Qdf.me().getConfig().get("ds.".concat(dsName).concat(".url")).toString());
			dataSource.setUsername(Qdf.me().getConfig().get("ds.".concat(dsName).concat(".username")).toString());
			dataSource.setPassword(Qdf.me().getConfig().get("ds.".concat(dsName).concat(".password")).toString());
			dataSource.setInitialSize( 10 );
			dataSource.setMinIdle( 10 );
			dataSource.setMaxActive(Integer.parseInt(Qdf.me().getConfig().get("ds.".concat(dsName).concat(".maxActive")).toString()));
			dataSource.setValidationQuery( VALIDATION_QUERY );
			dataSource.setTestOnReturn( true );
			dataSource.setFailFast( true );
			List<Filter> filters = new ArrayList<>( 1 );
			filters.add( new SqlReport() );
			dataSource.setProxyFilters( filters );
			if(null == DSMAP.get(DEFAULT_DS_NAME)) {
				DSMAP.put(DEFAULT_DS_NAME, dataSource);
			}
			DSMAP.put(dsName, dataSource);
		});
		
		
	}
	
	/**
	 * 获取连接
	 */
	@Override
	public Connection getConnection() {
		
		return getConnection(DEFAULT_DS_NAME);
	}

	@Override
	public String getDbType() {
		return getDbType(DEFAULT_DS_NAME);
	}
	
	@Override
	public Connection getThreadLocalConnection() {
		return connections.get();
	}

	@Override
	public Connection getConnection(String dsName) {
		
		Connection conn = connections.get();
		
		try {
			if(null == conn || conn.isClosed()) {
				conn = DSMAP.get(dsName).getConnection();
				connections.set(conn);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return conn;
	}

	@Override
	public String getDbType(String dsName) {
		
		return DSMAP.get(dsName).getDbType();
	}

	@Override
	public Connection getThreadLocalConnection(String dsName) {
		return connections.get();
	}

	
}
