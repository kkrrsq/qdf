package com.qdf.db;

public class SessionFactory {

	private static Session defaultSession = MysqlSession.me();
	
	public static Session getSession() {
		return defaultSession;
	}
}
