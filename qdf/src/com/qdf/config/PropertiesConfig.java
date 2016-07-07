package com.qdf.config;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import com.qdf.util.PropertiesUtil;

public class PropertiesConfig extends AbstractConfig {

	public PropertiesConfig() {

		PropertiesUtil propertiesUtil = new PropertiesUtil("qdfConfig.properties");
		Properties ps = propertiesUtil.getProperties();
		Iterator<Entry<Object, Object>> it = ps.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			set(key.toString(), value.toString());
		}
	}

}
