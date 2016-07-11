package com.qdf.db;

import com.qdf.annotation.TxConfig;
import com.qdf.core.Invocation;
import com.qdf.interceptor.QdfInterceptor;

/**
 * 事务拦截器
 * @author xiezq
 *
 */
public class Tx implements QdfInterceptor {
	
	private int txLevel;
	private String dsName;
	
	public void init(Invocation in) {
		TxConfig tx =  in.getMethod().getAnnotation(TxConfig.class);
		if(null == tx) {
			tx = in.getAction().getAnnotation(TxConfig.class);
		}
		if(null != tx) {
			this.txLevel = tx.level();
			this.dsName = tx.dsName();
		}
	}

	@Override
	public void intercept(Invocation in) {

		this.init(in);
		
		DbUtil.tx(this.dsName,this.txLevel,()->{in.invoke();return true;});
	}

}
