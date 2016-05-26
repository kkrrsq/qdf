package com.qdf.servlet;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
/**
 * qdf http响应对象接口
 * @author xiezq
 *
 */
public interface IResponse {

	public static final int SC_OK = 200;
	public static final int SC_BAD_REQUEST = 400;
	public static final int SC_UNAUTHORIZED = 401;
	public static final int SC_FORBIDDEN = 403;
	public static final int SC_NOT_FOUND = 404;
	public static final int SC_INTERNAL_SERVER_ERROR = 500;
	
	String HEAD_CONTENT_TYPE = "Content-Type";
	
	
	/**
	 * 响应数据类型
	 */
	enum Type{
		
		FORWARD("forward"),
		
		Redirect("redirect"),

		/**
		 * json数据
		 */
		JSON( "application/json;charset=UTF-8" ),

		/**
		 * javascript数据
		 */
		JS( "text/javascript;charset=UTF-8" ),

		/**
		 * jsoup数据
		 */
		JSOUP( "text/javascript;charset=UTF-8" ),

		/**
		 * xml数据
		 */
		XML( "text/xml;charset=UTF-8" ),

		/**
		 * Html格式数据
		 */
		HTML( "text/html;charset=UTF-8" ),

		/**
		 * 简单文本数据
		 */
		TEXT( "text/plain;charset=UTF-8" ),

		/**
		 * 二进制流文件
		 */
		STREAM( "application/octet-stream" );

		/**
		 * 类型对应的Content-Type值。
		 */
		private final String _CONTENT_TYPE;

		Type( String contentType ){
			this._CONTENT_TYPE = contentType;
		}

		/**
		 * @return 当前类型对应的Content-Type值。
		 */
		public String getContentType(){
			return this._CONTENT_TYPE;
		}

	}
	
	int getStatus();
	
	IResponse setStatus( int statusCode );
	
	IResponse setHeader( String header, String value );
	
	Map<String, String> getHeader();
	
	Set<String> getHeaderNames();
	
	String getHeader( String headerName );
	
	boolean hasHeader(String headerName);
	
	IResponse addCookie(Cookie paramCookie);
	
	Set<Cookie> getCookies();
	
	/**
	 * 设置响应数据,重复设置以最后一次设置的为最终结果，后边的设置会覆盖前边的设置。
	 *
	 * @param dataType 数据类型
	 * @param data 数据
	 * @return 当前对象
	 */
	IResponse setData( Type dataType, String data );

	/**
	 * 设置一个标准CMD格式的json响应数据。C:code, M:message, D:data.
	 *
	 * @param code 响应代码
	 * @param msg 响应消息
	 * @param data 响应数据
	 * @return CMD格式的json响应数据
	 */
	IResponse setDataByJsonCMD( int code, String msg, Object data );

	/**
	 * 设置一个标准CMD格式的json响应数据。code: 200, msg: null
	 *
	 * @param data 响应数据
	 * @return CMD格式的json响应数据
	 */
	IResponse setDataByJsonCMD( Object data );

	/**
	 * 设置一个标准CMD格式的json响应数据。通常用于响应错误信息。data: null.
	 *
	 * @param code 响应代码
	 * @param msg 响应消息
	 * @return CMD格式的json响应数据
	 */
	IResponse setDataByJsonCMD( int code, String msg );

	/**
	 * 设置响应数据流,重复设置以最后一次设置的为最终结果，后边的设置会覆盖前边的设置。
	 *
	 * @param contentType http协议响应头信息中的数据类型
	 * @param dataInputStream 数据流
	 * @return 当前对象
	 */
	IResponse setDataInputStream( String contentType, InputStream dataInputStream );
	
	IResponse setDataInputStream( InputStream dataInputStream );

	/**
	 * 获取响应数据类型
	 *
	 * @return 响应数据类型
	 */
	Type getDataType();

	/**
	 * 获取响应数据
	 *
	 * @return 响应数据
	 */
	String getData();

	/**
	 * 获取响应数据流。
	 *
	 * @return 响应数据流。
	 */
	InputStream getDataInputStream();

	/**
	 * 获取返回类型的HTTP协议响应头信息字符串。
	 *
	 * @return HTTP协议响应内容类型
	 */
	String getContentType();
	
	void redirect(String url);
	
	void forward(String url);
	
	void download(String path);
	
	void download(File file);
	
}
