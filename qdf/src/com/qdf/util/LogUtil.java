package com.qdf.util;

import com.qdf.log.ILogger;
import com.qdf.log.ILoggerFactory;

public class LogUtil {

	private static final ILogger LOGGER = ILoggerFactory.getLogger("QDF");
	
	public static void debug(String message) {
		LOGGER.debug(message);
	}
	
	public static void debug(String message, Object... objects) {
		LOGGER.debug(message, objects);
	}
	
	public static void debug(String message, Throwable t) {
		LOGGER.debug(message, t);
	}
	
	public static void info(String message) {
		LOGGER.info(message);
	}
	
	public static void info(String message, Object... objects) {
		LOGGER.info(message, objects);
	}
	
	public static void info(String message, Throwable t) {
		LOGGER.info(message, t);
	}
	
	public static void warn(String message) {
		LOGGER.warn(message);
	}
	
	public static void warn(String message, Object... objects) {
		LOGGER.warn(message, objects);		
	}
	
	public static void warn(String message, Throwable t) {
		LOGGER.warn(message, t);
	}
	
	public static void error(String message) {
		LOGGER.error(message);
	}
	
	public static void error(String message, Object... objects) {
		LOGGER.error(message, objects);	
	}
	
	public static void error(String message, Throwable t) {
		LOGGER.error(message, t);
	}
	
	public static boolean isDebugEnabled() {
		return LOGGER.isDebugEnabled();
	}
	
	public static boolean isInfoEnabled() {
		return LOGGER.isInfoEnabled();
	}
	
	public static boolean isWarnEnabled() {
		return LOGGER.isWarnEnabled();
	}
	
	public static boolean isErrorEnabled() {
		return LOGGER.isErrorEnabled();
	}
	
}
