package com.qdf.config;

import java.util.HashMap;
import java.util.Map;

/**
 * qdf框架配置
 * @author xiezq
 *
 */
public class QdfConfig {

	private String ENCODING = "utf-8";
	
	private String ignoreUrl = ".+(?i)\\.(html|css|js|json|ico|png|gif|woff|map)$";
	
	//保存db配置
	private Map<String, Object> properties = new HashMap<>();

	public String getENCODING() {
		return ENCODING;
	}

	public void setENCODING(String eNCODING) {
		ENCODING = eNCODING;
	}

	public String getIgnoreUrl() {
		return ignoreUrl;
	}

	public void setIgnoreUrl(String ignoreUrl) {
		this.ignoreUrl = ignoreUrl;
	}
	
	public Object getDBProperty(String key) {
		return properties.get(key);
	}
	
	public void setDBProperty(String key,Object value) {
		properties.put(key, value);
	}
}
