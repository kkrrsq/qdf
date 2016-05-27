package com.qdf.plugin.ehcache;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.qdf.annotation.Action;
import com.qdf.annotation.CacheName;
import com.qdf.core.Invocation;
import com.qdf.interceptor.QdfInterceptor;
import com.qdf.servlet.IResponse;
import com.qdf.servlet.IResponse.Type;
import com.qdf.util.LogUtil;

/**
 * 缓存拦截器
 * 可以注解实现缓存action
 * @author xiezq
 *
 */
public class CacheInterceptor implements QdfInterceptor {

	private static ConcurrentHashMap<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();
	
	private ReentrantLock getLock(String key) {
		ReentrantLock lock = lockMap.get(key);
		if(null == lock) {
			lock = new ReentrantLock();
			ReentrantLock previousLock = lockMap.putIfAbsent(key, lock);
			return previousLock == null ? lock : previousLock;
		}
		return lock;
	}
	
	@Override
	public void intercept(Invocation in) {
		Map<String, Object> cacheData = CacheUtil.get(getCacheName(in), getCacheKey(in));
		
		if(null == cacheData) {
			Lock lock = getLock(getCacheName(in));
			lock.lock();
			cacheData = CacheUtil.get(getCacheName(in), getCacheKey(in));
			try {
				if(null == cacheData) {
					in.invoke();
					cacheData = cacheAction(in);
					CacheUtil.put(getCacheName(in), getCacheKey(in), cacheData);
					return;
				}
			} finally {
				lock.unlock();
			}
		}
		
		returnByCache(in, cacheData);
		
	}
	
	private String getCacheName(Invocation in) {
		CacheName cache = in.getAction().getAnnotation(CacheName.class);
		if(null != cache){
			return cache.value();
		} else {
			Action action = in.getAction().getAnnotation(Action.class);
			return action.url().startsWith("/") ? action.url() : "/"+action.url();
		}
	}
	 
	private String getCacheKey(Invocation in) {
		String cacheName = getCacheName(in);
		String methodName = in.getMethod().getName();
		return cacheName.concat("/").concat(methodName);
	}
	
	private Map<String, Object> cacheAction(Invocation in) {
		
		Map<String, Object> cacheMap = new HashMap<>();
		IResponse response = in.getIResponse();

		Type dataType = response.getDataType();
		String data = response.getData();
		String contentType = response.getContentType();
		InputStream dataInputStream = response.getDataInputStream();
		
		if(null != dataType)
			cacheMap.put("dataType", response.getDataType());
		if(null != data)
			cacheMap.put("data", response.getData());
		if(null != contentType)
			cacheMap.put("contentType", response.getContentType());
		if(null != dataInputStream)
			cacheMap.put("dataInputStream", response.getDataInputStream());
		return cacheMap;
	}
	
	private void returnByCache(Invocation in,Map<String, Object> cacheData) {
		IResponse response = in.getIResponse();
		Type dataType = (Type) cacheData.get("dataType");
		String data = (String) cacheData.get("data");
		InputStream dataInputStream = (InputStream) cacheData.get("dataInputStream");
		String contentType = (String) cacheData.get("contentType");
		if(null !=dataType && null != data)
			response.setData(dataType, data);
		if(null != contentType && null != dataInputStream)
			response.setDataInputStream(contentType, dataInputStream);
		LogUtil.info("从缓存直接返回数据...");
	}

}
