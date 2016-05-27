package com.qdf.db;

import com.qdf.annotation.TxLevel;
import com.qdf.core.Invocation;
import com.qdf.interceptor.QdfInterceptor;

/**
 * 事务拦截器
 * @author xiezq
 *
 */
public class Tx implements QdfInterceptor {
	
	private int txLevel;
	
	public void init(Invocation in) {
		TxLevel txLevel = in.getMethod().getAnnotation(TxLevel.class);
		if(null == txLevel) {
			txLevel = in.getAction().getAnnotation(TxLevel.class);
		}
		if(null != txLevel) {
			this.txLevel = txLevel.value();
		}
	}

	@Override
	public void intercept(Invocation in) {

		this.init(in);
		
		DbUtil.tx(()->{in.invoke();return true;}, this.txLevel);
	}

}
