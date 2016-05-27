package com.qdf.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Splitter;
import com.qdf.plugin.QdfPlugin;
import com.qdf.util.JsonUtil;
import com.qdf.util.LogUtil;

/**
 * 保存插件信息
 * @author xiezq
 *
 */
public class Plugin {

	//保存注册的插件
	private Map<String, QdfPlugin> pluginMap = new HashMap();
	
	/**
	 * 添加插件
	 * @param plugin 插件
	 */
	public void addPlugin(QdfPlugin plugin) {
		pluginMap.put(plugin.getClass().getName(), plugin);
	}
	
	/**
	 * 移除插件
	 * @param plugin 插件
	 */
	public void removePlugin(QdfPlugin plugin) {
		pluginMap.remove(plugin.getClass().getName());
	}

	public Map<String, QdfPlugin> getPluginMap() {
		return pluginMap;
	}
	
	/**
	 * 将字符串(格式:p1,p2,p3)解析后,添加到插件map保存
	 * @param classNames
	 */
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
	
	/**
	 * 初始化map中所有插件
	 */
	public void initPlugins() {
		this.pluginMap.keySet().forEach(key -> {
			
			QdfPlugin plugin = pluginMap.get(key);
			plugin.init();
			
		});
	}
	
}
