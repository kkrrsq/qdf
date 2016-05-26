package com.qdf.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Splitter;
import com.qdf.interceptor.QdfInterceptor;
import com.qdf.plugin.QdfPlugin;
import com.qdf.util.JsonUtil;
import com.qdf.util.LogUtil;
import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;


public class Plugin {

	private Map<String, QdfPlugin> pluginMap = new HashMap();
	
	public void addPlugin(QdfPlugin plugin) {
		pluginMap.put(plugin.getClass().getName(), plugin);
	}
	
	public void removePlugin(QdfPlugin plugin) {
		pluginMap.remove(plugin.getClass().getName());
	}

	public Map<String, QdfPlugin> getPluginMap() {
		return pluginMap;
	}
	
	public void addPlugins(String classNames) {
		List<String> classNameList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(classNames);
		try {
			for (String className : classNameList) {
				Class<?> clazz = Class.forName(className);
				if(QdfPlugin.class.isAssignableFrom(clazz)) {
					QdfPlugin plugin = (QdfPlugin) clazz.newInstance();
					addPlugin(plugin);
				}
			}
			LogUtil.info("注册插件:"+JsonUtil.toJsonString(classNameList));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public void initPlugins() {
		this.pluginMap.keySet().forEach(key -> {
			
			QdfPlugin plugin = pluginMap.get(key);
			plugin.init();
			
		});
	}
	
}
