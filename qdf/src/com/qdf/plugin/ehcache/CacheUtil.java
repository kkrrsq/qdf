package com.qdf.plugin.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CacheUtil {

	private static CacheManager cacheManager;

	//初始化
	public static void init(CacheManager cacheManager) {
		CacheUtil.cacheManager = cacheManager;
	}
	
	private static Cache getOrNewCache(String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		if(null == cache) {
			cache = cacheManager.getCache(cacheName);
			if(null == cache) {
				synchronized(CacheUtil.class) {
					cacheManager.addCacheIfAbsent(cacheName);
					cache = cacheManager.getCache(cacheName);
				}
			}
		}
		return cache;
	}
	
	public static void put(String cacheName,Object key,Object value) {
		getOrNewCache(cacheName).put(new Element(key, value));
	}
	
	public static <T> T get(String cacheName,Object key) {
		Element element = getOrNewCache(cacheName).get(key);
		return element == null ? null : (T)element.getValue() ;
	}
}
