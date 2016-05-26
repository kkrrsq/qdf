package com.qdf.servlet;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
/**
 * qdf http请求对象接口
 * @author xiezq
 *
 */
public interface IRequest {


	String getURL();
	
	String getURI();
	
	String getServletPath();
	
	String getContextPath();
	
	String getHeader(String paramString);
	
	Set<String> getHeaderNames();
	
	String getMethod();
	
	String getQueryString();
	
	Map<String, String> getParameterMap();
	
	String getParameter(String paramString);
	
	Set<Cookie> getCookies();
	
	HttpSession getSession();
	
	HttpSession getSession(boolean paramBoolean);
	
	void setAttribute(String paramString, Object paramObject);
	
	Object getAttribute(String paramString);
	
	Map<String, Object> getAttributeMap();
	
	Set<String> getAttributeNames();
	
	String getContent();
	
	byte[] getContentData();
	
	/**
	 * 获取上传文件的相对路径集合的方法。
	 *
	 * @param uploadDir 上传文件保存的目录名称
	 * @param maxPostSize 上传文件的大小限制
	 * @return 最终保存的相对路径，获取文件最终路径时使用getUploadFile方法传入此路径获取。
	 */
	List<String> getUploadFiles( String uploadDir, int maxPostSize );

	/**
	 * 根据上传文件时返回的相对路径获取文件的最终路径。
	 *
	 * @param uploadFileName 上传文件时返回的相对路径
	 * @return 最终文件路径。
	 */
	File getUploadFile( String uploadFileName );
	
	<T> T getBean(Class<T> clazz);
	
}
