package com.qdf.core;

import com.google.common.base.Strings;
import com.qdf.config.QdfConfig;
import com.qdf.db.DruidPool;
import com.qdf.db.Pool;
import com.qdf.util.PropertiesUtil;

/**
 * Qdf主文件
 * @author xiezq
 *
 */
public class Qdf {
	
	private Route route = null;
	
	private Table table = null;

	private QdfConfig config = null;
	
	private ActionHandle actionHandle = null;
	
	private Pool pool = null;

	//单例
	private Qdf(){}

	private static final Qdf _me = new Qdf();
	
	public static Qdf me() {
		return _me;
	}
	
	//初始化框架
	public void init() {
		
		route = new Route();
		table = new Table();
		config = new QdfConfig();
		actionHandle = new ActionHandle();
		
		//加载qdfConfig配置文件
		PropertiesUtil propertiesUtil = new PropertiesUtil("qdfConfig.properties");
		
		String dbUrl = propertiesUtil.getProperty("db.url");
		String dbUsername = propertiesUtil.getProperty("db.username");
		String dbPassword = propertiesUtil.getProperty("db.password");
		int dbMaxActive = propertiesUtil.getInt("db.maxActive",300);
		String ignoreUrl = propertiesUtil.getProperty("qdf.ignoreUrl");
		String scanPackage = propertiesUtil.getProperty("qdf.scan.package");
		
		if(Strings.isNullOrEmpty(dbUrl) || Strings.isNullOrEmpty(dbUsername) ||
				Strings.isNullOrEmpty(dbPassword) || Strings.isNullOrEmpty(scanPackage)) {
			throw new RuntimeException("qdf初始化失败,读取配置出错,请检查配置...");
		}
		
		if(!Strings.isNullOrEmpty(ignoreUrl)) {
			config.setIgnoreUrl(ignoreUrl);
		}
		
		route.setActionPackage(scanPackage);
		table.setModelPackage(scanPackage);
		
		config.setDBProperty("url", dbUrl);
		config.setDBProperty("username", dbUsername);
		config.setDBProperty("password", dbPassword);
		config.setDBProperty("maxActive", dbMaxActive);
		
		pool = DruidPool.me();
		
		route.scanActions();
		
		table.scanModel();
		
	}
	
	//框架销毁
	public void destroy(){
		if(null != route) {
			route = null;
		}
		if(null != config) {
			config = null;
		}
		if(null != actionHandle) {
			actionHandle = null;
		}
	}

	public Route getRoute() {
		return route;
	}

	public QdfConfig getConfig() {
		return config;
	}

	public ActionHandle getActionHandle() {
		return actionHandle;
	}
	
	public Pool getPool() {
		return pool;
	}
	
	public Table getTable() {
		return table;
	}

	public static void main(String[] args) {
		System.out.println("qdk");
	}
}
