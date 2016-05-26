package com.qdf.db;

import java.util.regex.Pattern;

import com.qdf.core.Invocation;
import com.qdf.interceptor.QdfInterceptor;

public class TxByMethodRegex implements QdfInterceptor {

	private String regex;
	private Pattern pattern;
	private int txLevel;
	
	public TxByMethodRegex(String regex,int txLevel) {
		this.regex = regex;
		this.txLevel = txLevel;
		this.pattern = Pattern.compile(regex);
	}


	@Override
	public void intercept(Invocation in) {
		if(pattern.matcher(in.getMethod().getName()).matches()) {
			//符合tx配置的正则,添加事务
			DbUtil.tx(()->{in.invoke();return true;}, txLevel);
		} else {
			in.invoke();
		}
	}
	
	
}
