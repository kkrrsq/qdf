package com.qdf.config;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractConfig implements Config {

	protected Map<String, String> configMap = new HashMap<>();
	
	public AbstractConfig() {
		configMap.put("ENCODING", "utf-8");
		configMap.put("ignoreUrl", ".+(?i)\\.(html|css|js|json|ico|png|gif|woff|map)$");
	}

	@Override
	public String get(String key) {
		return configMap.get(key);
	}
}
