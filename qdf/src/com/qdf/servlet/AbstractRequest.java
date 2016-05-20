package com.qdf.servlet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 * qdf http请求对象抽象类
 * 实现了一些基本方法
 * @author xiezq
 *
 */
public abstract class AbstractRequest implements IRequest {
	
	protected String url;
	protected String uri;
	protected String servletPath;
	protected String contextPath;
	protected String method;
	protected String queryString;
	protected HttpSession httpSession;
	
	
	protected Map<String, String> headers = new HashMap<>();
	protected Map<String, String> parameters = new HashMap<>();
	protected Map<String, Object> attributes = new HashMap<>();
	protected Set<Cookie> cookies = new HashSet<>();

	@Override
	public String getURL() {
		return this.url;
	}

	@Override
	public String getURI() {
		// TODO Auto-generated method stub
		return this.uri;
	}

	@Override
	public String getServletPath() {
		// TODO Auto-generated method stub
		return this.servletPath;
	}

	@Override
	public String getContextPath() {
		// TODO Auto-generated method stub
		return this.contextPath;
	}

	@Override
	public String getHeader(String paramString) {
		// TODO Auto-generated method stub
		return this.headers.get(paramString);
	}

	@Override
	public Set<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return this.headers.keySet();
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return this.method;
	}

	@Override
	public String getQueryString() {
		// TODO Auto-generated method stub
		return this.queryString;
	}

	@Override
	public Map<String, String> getParameterMap() {
		// TODO Auto-generated method stub
		return this.parameters;
	}

	@Override
	public String getParameter(String paramString) {
		// TODO Auto-generated method stub
		return this.parameters.get(paramString);
	}

	@Override
	public Set<Cookie> getCookies() {
		// TODO Auto-generated method stub
		return this.cookies;
	}

	@Override
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return this.httpSession;
	}


	@Override
	public void setAttribute(String paramString, Object paramObject) {
		this.attributes.put(paramString, paramObject);
	}

	@Override
	public Object getAttribute(String paramString) {
		return attributes.get(paramString);
	}

	@Override
	public Map<String, Object> getAttributeMap() {
		return this.attributes;
	}

	@Override
	public Set<String> getAttributeNames() {
		// TODO Auto-generated method stub
		return this.attributes.keySet();
	}

}
