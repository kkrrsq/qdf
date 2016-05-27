package com.qdf.plugin.ehcache;
/**
 * 在CacheUtil.get(String cacheName,Object key,DataLoader loader)
 * 如果找不到缓存,则执行load方法,load方法需自己实现
 * @author xiezq
 *
 */
public interface DataLoader {

	Object load();
}
