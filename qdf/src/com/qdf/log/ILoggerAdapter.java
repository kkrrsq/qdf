package com.qdf.log;
/**
 * 日志适配器接口
 * 用于获取日志对象
 * @author xiezq
 *
 */
public interface ILoggerAdapter {

	ILogger getLogger(String name);
	
	ILogger getLogger(Class<?> clazz);
}
