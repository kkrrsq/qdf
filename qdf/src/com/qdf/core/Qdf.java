package com.qdf.core;

import com.google.common.base.Strings;
import com.qdf.config.QdfConfig;
import com.qdf.db.DruidPool;
import com.qdf.db.Pool;
import com.qdf.db.TxByMethodRegex;
import com.qdf.interceptor.InterceptorManage;
import com.qdf.util.LogUtil;
import com.qdf.util.PropertiesUtil;

/**
 * Qdf主文件
 * 保存框架的路由、数据库映射、插件、配置、数据库连接池等信息
 * @author xiezq
 *
 */
public class Qdf {
	
	private Route route = null;
	
	private Table table = null;

	private QdfConfig config = null;
	
	private ActionHandle actionHandle = null;
	
	private Pool pool = null;
	
	private Plugin plugin = null;

	//单例
	private Qdf(){}

	private static final Qdf _me = new Qdf();
	
	public static Qdf me() {
		return _me;
	}
	
	/**
	 * 读取配置文件,初始化框架
	 */
	public void init() {
		
		route = new Route();
		table = new Table();
		config = new QdfConfig();
		actionHandle = new ActionHandle();
		plugin = new Plugin();
		
		//加载qdfConfig配置文件
		PropertiesUtil propertiesUtil = new PropertiesUtil("qdfConfig.properties");
		
		String encoding = propertiesUtil.getProperty("qdf.encoding");
		
		String dbUrl = propertiesUtil.getProperty("db.url");
		String dbUsername = propertiesUtil.getProperty("db.username");
		String dbPassword = propertiesUtil.getProperty("db.password");
		int dbMaxActive = propertiesUtil.getInt("db.maxActive",300);
		String ignoreUrl = propertiesUtil.getProperty("qdf.ignoreUrl");
		
		String actionPackage = propertiesUtil.getProperty("qdf.scan.action");
		String modelPackage = propertiesUtil.getProperty("qdf.scan.model");
		String globalInterceptor = propertiesUtil.getProperty("qdf.global.interceptors");
		String plguins = propertiesUtil.getProperty("qdf.plugins");
		
		String txMehtodRegex = propertiesUtil.getProperty("qdf.tx.method");
		int txLevel = propertiesUtil.getInt("qdf.tx.level",4);
		
		if(Strings.isNullOrEmpty(dbUrl) || Strings.isNullOrEmpty(dbUsername) ||
				Strings.isNullOrEmpty(dbPassword) || Strings.isNullOrEmpty(actionPackage) ||
				Strings.isNullOrEmpty(modelPackage) ) {
			throw new RuntimeException("qdf初始化失败,读取配置出错,请检查配置...");
		}
		
		if(!Strings.isNullOrEmpty(encoding)) {
			config.setENCODING(encoding);
		}
		
		if(!Strings.isNullOrEmpty(ignoreUrl)) {
			ignoreUrl = ".+(?i)\\.("+ ignoreUrl +")$";
			config.setIgnoreUrl(ignoreUrl);
		}
		
		route.setActionPackage(actionPackage);
		table.setModelPackage(modelPackage);
		
		config.setDBProperty("url", dbUrl);
		config.setDBProperty("username", dbUsername);
		config.setDBProperty("password", dbPassword);
		config.setDBProperty("maxActive", dbMaxActive);
		
		pool = DruidPool.me();
		
		if(!Strings.isNullOrEmpty(globalInterceptor)) {
			InterceptorManage.me().addGlobalInterceptor(globalInterceptor);
		}
		
		if(!Strings.isNullOrEmpty(txMehtodRegex)) {
			InterceptorManage.me().addGlobalInterceptor(new TxByMethodRegex(txMehtodRegex, txLevel));
		}
		
		if(!Strings.isNullOrEmpty(plguins)) {
			plugin.addPlugins(plguins);
		}
		
		route.scanActions();
		
		table.scanModel();
		
		plugin.initPlugins();
		
		LogUtil.info("qdf启动成功...");
		
	}
	
	/**
	 * 销毁框架
	 */
	public void destroy(){
		if(null != actionHandle)
			actionHandle = null;
		if(null != config)
			config = null;
		if(null != table)
			table = null;
		if(null != route) 
			route = null;
		LogUtil.info("qdf销毁成功...");
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
