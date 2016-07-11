package com.qdf.config;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractConfig implements Config {

	protected Map<String, String> configMap = new HashMap<>();
	
	public AbstractConfig() {
		configMap.put("qdf.encoding", "utf-8");
		configMap.put("qdf.ignoreUrl", ".+(?i)\\.(html|css|js|json|ico|png|gif|woff|map)$");
	}

	@Override
	public String get(String key) {
		return configMap.get(key);
	}
	
	@Override
	public void set(String key,String value) {
		if("qdf.ignoreUrl".equals(key)) {
			configMap.put(key, ".+(?i)\\.("+value+")$");
			return;
		}
		configMap.put(key, value);
	}
}
