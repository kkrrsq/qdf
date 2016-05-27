package com.qdf.util;

import java.util.Objects;
/**
 * URL帮助类
 * @author xiezq
 *
 */
public class UrlUtil {


	/**
	 * 解析url
	 * @param url url
	 * @return action名,方法名
	 */
	public static String[] parseUrl(String url) {
		Objects.requireNonNull(url,"url can not be null");
		if(url.endsWith("/"))
			url = url.substring(0, url.length() - 1).trim();
		if(url.startsWith("/"))
			url = url.substring(1,url.length()).trim();
		String []urlItem = url.split("/");
		String []result = {"",""};
		if(urlItem.length > 2) {
			result[0] = "/"+url.substring(0,url.length() - urlItem[urlItem.length-1].length() - 1);
			result[1] = urlItem[urlItem.length-1];
		} else if(urlItem.length == 2) {
			result[0] = "/" + urlItem[0];
			result[1] = urlItem[1];
		} else {
			result[0] = "/" + urlItem[0];
			result[1] = "execute";
		}
		return result;
	}
	
	public static void main(String[] args) {
		String []urlItem = parseUrl("/user/admin/add");
		System.out.println(urlItem.length);
		for (String url : urlItem) {
			System.out.println(url);
		}
	}
}
