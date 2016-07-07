package com.qdf.servlet.impl;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.qdf.core.Injector;
import com.qdf.core.Qdf;
import com.qdf.servlet.AbstractRequest;
import com.qdf.servlet.IRequest;
/**
 * qdf http请求对象实现类
 * @author xiezq
 *
 */
public class HttpServletRequestWrapper extends AbstractRequest implements IRequest{

	private HttpServletRequest request;
	private String ENCODING;

	private byte[] content;
	private String contentString;
	
	private List<String> uploadFile;
	
	public HttpServletRequestWrapper(HttpServletRequest request) {
		
		Objects.requireNonNull(request);
	
		this.request = request;
		this.ENCODING = Qdf.me().getConfig().get("qdf.encoding");
		
		this.uri = request.getRequestURI();
		this.url = request.getRequestURL().toString();
		this.servletPath = request.getServletPath();
		this.contextPath = request.getContextPath();
		this.method = request.getMethod();
		this.queryString = request.getQueryString();
		this.httpSession = request.getSession();
		
		//header
		Enumeration<String> headerNames = request.getHeaderNames();
		while( headerNames.hasMoreElements() ){
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader( headerName );
			this.headers.put( headerName, headerValue );
		}
		
		//parameter
		request.getParameterMap().entrySet().forEach( ( paramsEntry ) -> {
			String key = paramsEntry.getKey();
			String[] values = paramsEntry.getValue();
			String value = null == values ? null : values[0];
			this.parameters.put( key, value );
		} );
		
		//attribute
		Enumeration<String> attrNames = request.getAttributeNames();
		while( attrNames.hasMoreElements() ){
			String attrName = attrNames.nextElement();
			if( null == attrName || 0 == ( attrName = attrName.trim() ).length() )
				continue;
			this.attributes.put( attrName, request.getAttribute( attrName ) );
		}
		
		//cookies
		Cookie []cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			this.cookies.add(cookie);
		}
		
	}
	
	@Override
	public HttpSession getSession(boolean paramBoolean) {
		return request.getSession(paramBoolean);
	}

	@Override
	public String getContent() {
		if( null == this.contentString ){
			try{
				this.contentString = new String( getContentData(), this.ENCODING );
			}catch( UnsupportedEncodingException e ){
				this.contentString = new String( getContentData() );
			}
		}
		return this.contentString;
	}

	@Override
	public byte[] getContentData() {
		if( null == this.content ){
			int cLen = request.getContentLength();
			try( InputStream inStream = request.getInputStream(); DataInputStream dis = new DataInputStream( inStream ); ){
				content = new byte[cLen];
				dis.readFully( content );
			}catch( IOException e ){
				throw new RuntimeException( e );
			}
		}
		return this.content;
	}

	@Override
	public List<String> getUploadFiles(String uploadDir, int maxPostSize) {
		if( null != uploadFile )
			return uploadFile;

		if( null == uploadDir || 0 == ( uploadDir = uploadDir.trim() ).length() )
			throw new RuntimeException( "uploadDir can not be empty!" );

		if( !uploadDir.endsWith( "/" ) ){
			uploadDir = uploadDir.concat( "/" );
		}

		try{
			String path = HttpServletRequestWrapper.class.getResource( "/" ).toURI().getPath();
			String webRootDir = new File( path ).getParentFile().getParentFile().getCanonicalPath();

			File destDir = new File( webRootDir, uploadDir );
			if( !destDir.exists() ){
				if( !destDir.mkdirs() ){ throw new RuntimeException( "Directory " + destDir + " not exists and can not create directory." ); }
			}
			path = destDir.getCanonicalPath();

			MultipartRequest mReq = new MultipartRequest( request, path, maxPostSize, ENCODING, new DefaultFileRenamePolicy() );

            uploadFile = new ArrayList<>() ;
			Enumeration fileNames = mReq.getFileNames();
			while( fileNames.hasMoreElements() ){
				String paramName = ( String )fileNames.nextElement();
				String fileName = mReq.getFilesystemName( paramName );
				if( null == fileName ){
					// 跳过未上传成功的文件。
					continue;
				}

				// 处理用户上传jsp等恶意文件。
				String fileLowerName = fileName.trim().toLowerCase();
				if( fileLowerName.endsWith( ".jsp" ) || fileLowerName.endsWith( ".jspx" ) ){
					mReq.getFile( paramName ).delete();
					continue;
				}

				// 返回上传成功后的文件相对路径。
				uploadFile.add( uploadDir.concat( fileName ) );
			}

			Enumeration parNames = mReq.getParameterNames();
			while( parNames.hasMoreElements() ){
				String parName = ( String )parNames.nextElement();
				this.parameters.put( parName, mReq.getParameter( parName ) );
			}
			return uploadFile;
		}catch( Exception e ){
			throw new RuntimeException( e );
		}
	}

	@Override
	public File getUploadFile(String uploadFileName) {
		try{
			String path = HttpServletRequestWrapper.class.getResource( "/" ).toURI().getPath();
			String webRootDir = new File( path ).getParentFile().getParentFile().getCanonicalPath();

			File destFile = new File( webRootDir, uploadFileName );
			return destFile;
		}catch( Exception e ){
			throw new RuntimeException( e );
		}
	}

	@Override
	public <T> T getBean(Class<T> clazz) {
		return Injector.injectBean(clazz, parameters);
	}

}
