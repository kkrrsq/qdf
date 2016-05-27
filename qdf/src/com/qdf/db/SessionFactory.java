package com.qdf.db;

/**
 * 工厂类,用于获取数据库操作的对象
 * @author xiezq
 *
 */
public class SessionFactory {

	private static Session defaultSession = MysqlSession.me();
	
	public static Session getSession() {
		return defaultSession;
	}
}
