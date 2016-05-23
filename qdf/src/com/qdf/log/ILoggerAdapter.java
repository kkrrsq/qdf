package com.qdf.log;

public interface ILoggerAdapter {

	ILogger getLogger(String name);
	
	ILogger getLogger(Class<?> clazz);
}
