package com.qdf.log.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qdf.log.ILogger;

public class Slf4jLogger implements ILogger{

	private Logger logger;
	
	public Slf4jLogger(String name) {
		logger = LoggerFactory.getLogger(name);
	}
	
	public Slf4jLogger(Class<?> clazz) {
		logger = LoggerFactory.getLogger(clazz);
	}

	@Override
	public void debug(String message) {
		logger.debug(message);
	}

	@Override
	public void debug(String message, Object... objects) {
		logger.debug(message, objects);
	}
	
	@Override
	public void debug(String message, Throwable t) {
		logger.debug(message, t);
	}

	@Override
	public void info(String message) {
		logger.info(message);
	}
	
	@Override
	public void info(String message, Object... objects) {
		logger.info(message, objects);
	}

	@Override
	public void info(String message, Throwable t) {
		logger.info(message,t);
	}

	@Override
	public void warn(String message) {
		logger.warn(message);
	}

	@Override
	public void warn(String message, Object... objects) {
		logger.warn(message, objects);		
	}

	@Override
	public void warn(String message, Throwable t) {
		logger.warn(message,t);
	}

	@Override
	public void error(String message) {
		logger.error(message);
	}

	@Override
	public void error(String message, Object... objects) {
		logger.error(message, objects);	
	}
	
	@Override
	public void error(String message, Throwable t) {
		logger.error(message,t);
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return isErrorEnabled();
	}

}
