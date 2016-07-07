package com.qdf.config;

import java.security.KeyStore.PrivateKeyEntry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


public class MysqlConfig extends AbstractConfig {

	private static final String TABLENAME = "sys_config";
	private String jdbcUrl;
	private String user;
	private String password;

	public MysqlConfig(String jdbcUrl,String user,String password) {
		Objects.requireNonNull(jdbcUrl);
		Objects.requireNonNull(user);
		Objects.requireNonNull(password);
		this.jdbcUrl = jdbcUrl;
		this.user = user;
		this.password = password;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			init();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected boolean hasTable() {
		String querySql = "SHOW TABLES LIKE '".concat(TABLENAME).concat("'; ");
		try (Connection conn = getConn();
				PreparedStatement pstmt = conn.prepareStatement(querySql);
				ResultSet rs = pstmt.executeQuery();) {
			if (rs.next()) {
				return null != rs.getString(1);
			}
			return false;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	protected void createTable(){
		StringBuilder sb = new StringBuilder();

		sb.append( "CREATE TABLE `" ).append( TABLENAME ).append( "` (" );
		sb.append( "`id` int NOT NULL AUTO_INCREMENT," );
		sb.append( "`key` varchar(192) NOT NULL," );
		sb.append( "`value` text NOT NULL," );
		sb.append( "`description` varchar(255) DEFAULT NULL," );
		sb.append( "PRIMARY KEY (`id`)" );
		sb.append( ") ENGINE=InnoDB DEFAULT CHARSET=utf8;" );
		String sql = sb.toString();

		try( Connection conn = getConn(); PreparedStatement pstmt = conn.prepareStatement( sql ); ){
			pstmt.executeUpdate();
		}catch( SQLException e ){
			throw new RuntimeException( e );
		}
	}
	
	
	protected void initTable() {
		String sql = "insert into `".concat( TABLENAME ).concat( "` (`key`, `value`, `description` ) values( ?, ?, ?)" );
		List<String[]> params = new ArrayList<>();


		params.add( new String[]{ "qdf.encoding","utf-8","" } );
		params.add( new String[]{ "qdf.ignoreUrl","jsp|html|css|js|json|ico|png|gif|woff|map|txt","" } );
		params.add( new String[]{ "qdf.scan.action","com.test.action","" } );
		params.add( new String[]{ "qdf.scan.model","com.test.model","" } );
		params.add( new String[]{ "db.url","jdbc:mysql://127.0.0.1:3306/qdf","" } );
		params.add( new String[]{ "db.username","root","" } );
		params.add( new String[]{ "db.password","root","" } );
		params.add( new String[]{ "db.maxActive","300","" } );
		
		params.add( new String[]{ "qdf.global.interceptors","","" } );
		params.add( new String[]{ "qdf.plugins","","" } );
		params.add( new String[]{ "qdf.tx.method","","" } );
		params.add( new String[]{ "qdf.tx.level","","" } );

		try( Connection conn = getConn(); PreparedStatement pstmt = conn.prepareStatement( sql ); ){
			for( String[] rowParams : params ){
				for( int i = 1; i < 4; i++ ){
					pstmt.setString( i, rowParams[i - 1] );
				}
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		}catch( SQLException e ){
			throw new RuntimeException( e );
		}
	}
	
	protected Connection getConn() {
		try {
			return DriverManager.getConnection(jdbcUrl,user,password);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void loadConfig() {
		ResultSet rs = null;
		String querySql = "select `key`, `value` from ".concat( TABLENAME );
		try( Connection conn = getConn(); PreparedStatement pstmt = conn.prepareStatement( querySql ); ){
			rs = pstmt.executeQuery();


			Map<String, String> rowData = new HashMap<>();
			while( rs.next() ){
				rowData.put( rs.getString(1),rs.getString(2) );
			}

			// 锁定配置数据，进行数据更新。
			synchronized( rowData ){

				Set<String> keys = rowData.keySet();
				for (String key : keys) {
					set(key, rowData.get(key));
				}
				
			}
		}catch( SQLException e ){
			throw new RuntimeException( e );
		}finally{
			if( null != rs ){
				try{
					rs.close();
				}catch( SQLException e ){
					throw new RuntimeException( e );
				}
			}
		}
	}
	
	protected void init() {
		if(!hasTable()) {
			createTable();
			initTable();
		}
		loadConfig();
	}
	

}
