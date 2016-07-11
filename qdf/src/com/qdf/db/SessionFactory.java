package com.qdf.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Splitter;
import com.qdf.core.Qdf;

import sun.print.resources.serviceui;

/**
 * 工厂类,用于获取数据库操作的对象
 * @author xiezq
 *
 */
public class SessionFactory {

	private SessionFactory(){}
	private static final String DEFAULT = "default";
	private static final Map<String, Session> SESSIONMAP = new HashMap<>();
	static {
		List<String> dsList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(Qdf.me().getConfig().get("ds.list"));
		dsList.forEach(dsName -> {
			Session session = new MysqlSession(dsName);
			if(null == SESSIONMAP.get(DEFAULT))
				SESSIONMAP.put(DEFAULT, session);
			SESSIONMAP.put(dsName, session);
		});
	}
	
	public static Session getSession() {
		return getSession(DEFAULT);
	}
	
	public static Session getSession(String dsName) {
		return SESSIONMAP.get(dsName);
	}
}
