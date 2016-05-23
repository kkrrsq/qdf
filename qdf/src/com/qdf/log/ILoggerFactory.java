package com.qdf.log;

import com.qdf.log.impl.Slf4jLoggerAdapter;

public class ILoggerFactory {

	private static ILoggerAdapter defaultLogFactory;
	
	static {
		defaultLogFactory = new Slf4jLoggerAdapter();
	}
	
	public static ILogger getLogger(String name) {
		return defaultLogFactory.getLogger(name);
	}
	
	public static ILogger getLogger(Class<?> clazz) {
		return defaultLogFactory.getLogger(clazz);
	}
	
	/**
	 * 设置日志对象工厂类
	 * 
	 * @param factory 日志对象工厂类
	 */
	public synchronized static void setLogFactory( ILoggerAdapter factory ){
		defaultLogFactory = factory;
	}

	
}
