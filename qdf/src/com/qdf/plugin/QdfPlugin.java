package com.qdf.plugin;

/**
 * qdf插件接口
 * 所有插件必须实现该接口
 * @author xiezq
 *
 */
public interface QdfPlugin {

	/**
	 * 插件初始化
	 * @return
	 */
	boolean init();
	
	/**
	 * 插件销毁
	 * @return
	 */
	boolean destroy();
}
