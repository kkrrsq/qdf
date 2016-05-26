package com.test.action;

import java.util.List;

import com.qdf.annotation.Action;
import com.qdf.annotation.CacheName;
import com.qdf.annotation.Interceptor;
import com.qdf.core.QdfAction;
import com.qdf.plugin.ehcache.CacheInterceptor;
import com.qdf.plugin.ehcache.CacheUtil;
import com.qdf.servlet.IRequest;
import com.qdf.servlet.IResponse;
import com.test.model.User;
import com.test.service.UserService;
import com.test.service.impl.UserServiceImpl;

/**
 * 缓存测试
 * 使用:
 * 1.配置文件中注册缓存插件
 * 2.配置cache.xml
 * 3...
 * @author xiezq
 *
 */
@Action(url="/cache")
@CacheName("/cache")
public class CacheAction implements QdfAction {

	UserService us = new UserServiceImpl();
	
	@Override
	public void execute(IRequest request, IResponse response) {
		System.out.println("execute");
	}
	
	/**
	 * 需要在cache.xml中配置cacheName
	 * @param request
	 * @param response
	 */
	@Interceptor(CacheInterceptor.class)
	public void list1(IRequest request, IResponse response){
		List<User> list = us.list();
		response.setDataByJsonCMD(list);
	}
	
	/**
	 * 通过CacheUtile缓存
	 * @param request
	 * @param response
	 */
	public void list2(IRequest request, IResponse response){
		
		List<User> list = CacheUtil.get("/cache", "/cache/list2", () -> {
			//如果缓存不存在,则执行该方法获取数据,然后将数据缓存
			return us.list();
		});
		
		response.setDataByJsonCMD(list);
	}
	
}


