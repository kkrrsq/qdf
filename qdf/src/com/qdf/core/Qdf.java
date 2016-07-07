package com.qdf.core;

import com.google.common.base.Strings;
import com.qdf.config.Config;
import com.qdf.config.MysqlConfig;
import com.qdf.config.PropertiesConfig;
import com.qdf.db.DruidPool;
import com.qdf.db.Pool;
import com.qdf.db.TxByMethodRegex;
import com.qdf.interceptor.InterceptorManage;
import com.qdf.util.LogUtil;

/**
 * Qdf主文件 保存框架的路由、数据库映射、插件、配置、数据库连接池等信息
 * 
 * @author xiezq
 *
 */
public class Qdf {

	private Route route = null;

	private Table table = null;

	private Config config = null;

	private ActionHandle actionHandle = null;

	private Pool pool = null;

	private Plugin plugin = null;

	// 单例
	private Qdf() {
	}

	private static final Qdf _me = new Qdf();

	public static Qdf me() {
		return _me;
	}

	/**
	 * 读取配置文件,初始化框架
	 */
	public void init(String configImplName,String jdbcUrl,String user,String password) {

		if (Strings.isNullOrEmpty(configImplName))
			throw new RuntimeException("qdf初始化失败,configImpl为空...");
		if ("properties".equals(configImplName)) {
			config = new PropertiesConfig();
		} else if("mysql".equals(configImplName)) {
			config = new MysqlConfig(jdbcUrl, user, password);
		} else {
			throw new RuntimeException("qdf初始化失败,configImpl无效...");
		}

		route = new Route();
		table = new Table();
		actionHandle = new ActionHandle();
		plugin = new Plugin();

		// 加载qdfConfig配置文件
		String dbUrl = config.get("db.url");
		String dbUsername = config.get("db.username");
		String dbPassword = config.get("db.password");
		String dbMaxActive = config.get("db.maxActive");

		String actionPackage = config.get("qdf.scan.action");
		String modelPackage = config.get("qdf.scan.model");
		String globalInterceptor = config.get("qdf.global.interceptors");
		String plguins = config.get("qdf.plugins");

		String txMehtodRegex = config.get("qdf.tx.method");
		String txLevel = config.get("qdf.tx.level");

		if (Strings.isNullOrEmpty(dbUrl) || Strings.isNullOrEmpty(dbUsername) || Strings.isNullOrEmpty(dbPassword)
				|| Strings.isNullOrEmpty(dbMaxActive) || Strings.isNullOrEmpty(actionPackage)
				|| Strings.isNullOrEmpty(modelPackage)) {
			throw new RuntimeException("qdf初始化失败,读取配置出错,请检查配置...");
		}

		route.setActionPackage(actionPackage);
		table.setModelPackage(modelPackage);

		pool = DruidPool.me();

		if (!Strings.isNullOrEmpty(globalInterceptor)) {
			InterceptorManage.me().addGlobalInterceptor(globalInterceptor);
		}

		if (!Strings.isNullOrEmpty(txMehtodRegex)) {
			InterceptorManage.me().addGlobalInterceptor(new TxByMethodRegex(txMehtodRegex, Integer.parseInt(txLevel)));
		}

		if (!Strings.isNullOrEmpty(plguins)) {
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
	public void destroy() {
		if (null != actionHandle)
			actionHandle = null;
		if (null != config)
			config = null;
		if (null != table)
			table = null;
		if (null != route)
			route = null;
		LogUtil.info("qdf销毁成功...");
	}

	public Route getRoute() {
		return route;
	}

	public Config getConfig() {
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
