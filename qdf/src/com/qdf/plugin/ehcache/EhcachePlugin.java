package com.qdf.plugin.ehcache;

import com.qdf.plugin.QdfPlugin;

import net.sf.ehcache.CacheManager;

/**
 * Ehcache缓存插件
 * @author xiezq
 *
 */
public class EhcachePlugin implements QdfPlugin {

	private static CacheManager cacheManager;
	
	@Override
	public boolean init() {
		if(null == cacheManager) {
			cacheManager = CacheManager.create();
			CacheUtil.init(cacheManager);
		}
		return true;
	}

	@Override
	public boolean destroy() {
		if(null != cacheManager)
			cacheManager.shutdown();
		return true;
	}

}
