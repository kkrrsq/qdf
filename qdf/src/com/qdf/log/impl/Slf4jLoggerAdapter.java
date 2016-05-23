package com.qdf.log.impl;

import com.qdf.log.ILogger;
import com.qdf.log.ILoggerAdapter;

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
