package com.qdf.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.qdf.annotation.TxLevel;
import com.qdf.core.Invocation;
import com.qdf.core.Qdf;
import com.qdf.interceptor.QdfInterceptor;
import com.qdf.util.LogUtil;
import com.sun.org.apache.xml.internal.security.Init;

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
