package com.qdf.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

import com.qdf.core.Qdf;

/**
 * 配置文件帮助类
 * 
 * @author xiezq
 *
 */
public class PropertiesUtil {

	private Properties properties = null;

	public PropertiesUtil(String fileName, String encoding) {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		if (null == is) {
			throw new RuntimeException("properties file can not be found: " + fileName);
		}
		properties = new Properties();
		try {
			properties.load(new InputStreamReader(is, encoding));
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
		}
	}

	public PropertiesUtil(String fileName) {
		this(fileName, Qdf.me().getConfig().getENCODING());
	}

	public PropertiesUtil(File file) {
		this(file, Qdf.me().getConfig().getENCODING());
	}

	public PropertiesUtil(File file, String encoding) {
		if (file == null)
			throw new IllegalArgumentException("File can not be null.");
		if (file.isFile() == false)
			throw new IllegalArgumentException("File not found : " + file.getName());

		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			properties = new Properties();
			properties.load(new InputStreamReader(inputStream, encoding));
		} catch (IOException e) {
			throw new RuntimeException("Error loading properties file.", e);
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
		}
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public Object get(String key) {
		return properties.get(key);
	}

	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public Integer getInt(String key) {
		return getInt(key, null);
	}

	public Integer getInt(String key, Integer defaultValue) {
		String value = properties.getProperty(key);
		if (value != null)
			return Integer.parseInt(value.trim());
		return defaultValue;
	}

	public Long getLong(String key) {
		return getLong(key, null);
	}

	public Long getLong(String key, Long defaultValue) {
		String value = properties.getProperty(key);
		if (value != null)
			return Long.parseLong(value.trim());
		return defaultValue;
	}

	public Boolean getBoolean(String key) {
		return getBoolean(key, null);
	}

	public Boolean getBoolean(String key, Boolean defaultValue) {
		String value = properties.getProperty(key);
		if (value != null) {
			value = value.toLowerCase().trim();
			if ("true".equals(value))
				return true;
			else if ("false".equals(value))
				return false;
			throw new RuntimeException("The value can not parse to Boolean : " + value);
		}
		return defaultValue;
	}

	public boolean containsKey(String key) {
		return properties.containsKey(key);
	}

	public Properties getProperties() {
		return properties;
	}
	
}
