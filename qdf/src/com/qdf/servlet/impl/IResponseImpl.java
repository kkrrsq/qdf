package com.qdf.servlet.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.qdf.servlet.IResponse;
import com.qdf.util.JsonUtil;
/**
 * qdf http响应对象实现类
 * @author xiezq
 *
 */
public class IResponseImpl implements IResponse{

	
	private HttpServletResponse response;
	private int statusCode = SC_OK;
	private Map<String, String> headers = new HashMap<>();
	private Set<Cookie> cookies = new HashSet<>();
	
	private Type dataType;
	private String data;
	private InputStream dataStream;
	
	public IResponseImpl(HttpServletResponse response) {
		this.response = response;
	}
	
	@Override
	public int getStatus() {
		return this.statusCode;
	}

	@Override
	public IResponse setStatus(int statusCode) {
		this.statusCode = statusCode;
		this.response.setStatus(statusCode);
		return this;
	}

	@Override
	public IResponse setHeader(String header, String value) {
		 this.headers.put(header, value);
		 this.response.setHeader(header, value);
		 return this;
	}

	@Override
	public Map<String, String> getHeader() {
		return this.headers;
	}

	@Override
	public Set<String> getHeaderNames() {
		return this.headers.keySet();
	}

	@Override
	public String getHeader(String headerName) {
		return this.headers.get(headerName);
	}

	@Override
	public boolean hasHeader(String headerName) {
		return this.headers.containsKey(headerName);
	}
	
	@Override
	public IResponse addCookie(Cookie paramCookie) {
		this.cookies.add(paramCookie);
		this.response.addCookie(paramCookie);
		return this;
	}

	@Override
	public IResponse setData(Type dataType, String data) {
		Objects.requireNonNull( dataType, "dataType can not be null!" );
		Objects.requireNonNull( data, "data can not be null!" );
		this.dataType = dataType;
		this.data = data;
		return this;
	}

	@Override
	public IResponse setDataByJsonCMD(int code, String msg, Object data) {
		setData(Type.JSON, JsonUtil.toJsonCDM(code, data, msg));
		return this;
	}

	@Override
	public IResponse setDataByJsonCMD(Object data) {
		setDataByJsonCMD(this.SC_OK,null,data);
		return this;
	}

	@Override
	public IResponse setDataByJsonCMD(int code, String msg) {
		setDataByJsonCMD(code, msg, null);
		return this;
	}

	@Override
	public IResponse setDataInputStream(String contentType, InputStream dataInputStream) {
		this.dataType = Type.STREAM;
		this.headers.put( HEAD_CONTENT_TYPE, null == contentType ? "application/octet-stream" : contentType );
		this.dataStream = dataInputStream;
		return this;
	}
	
	@Override
	public IResponse setDataInputStream(InputStream dataInputStream) {
		setDataInputStream(null, dataInputStream);
		return this;
	}

	@Override
	public Type getDataType() {
		return this.dataType;
	}

	@Override
	public String getData() {
		return this.data;
	}

	@Override
	public InputStream getDataInputStream() {
		return this.dataStream;
	}

	@Override
	public String getContentType() {
		return this.headers.get( HEAD_CONTENT_TYPE );
	}

	@Override
	public Set<Cookie> getCookies() {
		return this.cookies;
	}

	@Override
	public void redirect(String url) {
		this.dataType = Type.Redirect;
		this.data = url;
	}

	@Override
	public void forward(String url) {
		this.dataType = Type.FORWARD;
		this.data = url;
	}

	@Override
	public void download(String path) {
		download(new File(path));
	}

	@Override
	public void download(File file) {
		InputStream fis = null;
		try {
			String filename = new String(file.getName().getBytes(), "ISO8859-1");
			
			// 以流的形式下载文件。
			fis = new BufferedInputStream(new FileInputStream(file));
			
			// 设置response的Header
			setHeader("Content-Disposition", "attachment;filename=" + filename);
			setHeader("Content-Length", "" + file.length());
			setDataInputStream("application/octet-stream", fis);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
