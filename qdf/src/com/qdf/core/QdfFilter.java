package com.qdf.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.io.ByteStreams;
import com.qdf.model.ActionBean;
import com.qdf.servlet.IResponse;
import com.qdf.servlet.impl.HttpServletRequestWrapper;
import com.qdf.servlet.impl.IResponseImpl;
import com.qdf.util.LogUtil;
import com.qdf.util.UrlUtil;
/**
 * qdf框架主过滤器
 * @author xiezq
 *
 */
public class QdfFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse rep = (HttpServletResponse) response;
		
		//设置编码
		req.setCharacterEncoding(Qdf.me().getConfig().getENCODING());
		rep.setCharacterEncoding(Qdf.me().getConfig().getENCODING());
		
		String url = req.getRequestURI().substring(req.getContextPath().length());
		
		if(null != Qdf.me().getConfig().getIgnoreUrl() && ( "/".equals(url) || url.matches(Qdf.me().getConfig().getIgnoreUrl()))) {
			LogUtil.info("跳过拦截url:{}",url);
			filterChain.doFilter(request, response);
			return;
		}
		
		//如果找不到方法,就调用默认方法execute
		ActionBean actionBean = null;
		if(Qdf.me().getRoute().contain(url)) {
			actionBean = Qdf.me().getRoute().getRoute(url);
		} else {
			String []urlItem = UrlUtil.parseUrl(url);
			url = urlItem[0] + "/execute";
			actionBean = Qdf.me().getRoute().getRoute(url);
		}
		
		HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(req);
		IResponse iResponse = new IResponseImpl(rep);
		
		if(null != actionBean) {
			try {
				Qdf.me().getActionHandle().handle(actionBean, url, requestWrapper, iResponse);
			
				// 根据响应数据类型进行响应处理。
				IResponse.Type resType = iResponse.getDataType();
				if( null == resType ){
					resType = IResponse.Type.TEXT;
				}

				// 根据响应数据类型设置指定的头信息。
				if( !iResponse.hasHeader( IResponse.HEAD_CONTENT_TYPE ) )
					iResponse.setHeader( IResponse.HEAD_CONTENT_TYPE, resType.getContentType() );

				// 下载响应特殊处理
				if( IResponse.Type.STREAM == resType ){
					InputStream inStream = null;
					ServletOutputStream outStream = null;
					try{
						inStream = iResponse.getDataInputStream(); 
						outStream = response.getOutputStream();
						ByteStreams.copy( inStream, outStream );
						outStream.flush();
					} catch (Exception e) {
						throw new RuntimeException(e);
					} finally {
						if(null != outStream)
							outStream.close();
						if(null != inStream)
							inStream.close();
					}
				} else if(IResponse.Type.FORWARD == resType) {
					req.getRequestDispatcher(iResponse.getData()).forward(req, rep);
				} else if(IResponse.Type.Redirect == resType) {
					rep.sendRedirect(iResponse.getData());
				} else{
					
					// 禁止浏览器缓存
					rep.setHeader( "Pragma", "no-cache" );
					rep.setHeader( "Cache-Control", "no-cache" );
					rep.setDateHeader( "Expires", 0 );

					try( PrintWriter writer = response.getWriter(); ){
						writer.write( iResponse.getData() == null ? "" : iResponse.getData());
						writer.flush();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				
			} catch (SecurityException | IllegalArgumentException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("找不到对应的Action:".concat(url));
		}
	}

	@Override
	public void init(FilterConfig config) {
		
		Qdf.me().init();
	}
	
	@Override
	public void destroy() {
		Qdf.me().destroy();
	}
	
}
