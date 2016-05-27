package com.qdf.log.impl;

import com.qdf.log.ILogger;
import com.qdf.log.ILoggerAdapter;

/**
 * slf4j日志适配器实现
 * @author xiezq
 *
 */
public class Slf4jLoggerAdapter implements ILoggerAdapter {

	@Override
	public ILogger getLogger(String name) {
		return new Slf4jLogger(name);
	}

	@Override
	public ILogger getLogger(Class<?> clazz) {
		return new Slf4jLogger(clazz);
	}

}
