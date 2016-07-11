package com.qdf.db;

import java.util.regex.Pattern;

import com.qdf.core.Invocation;
import com.qdf.interceptor.QdfInterceptor;

/**
 * 声明式事务
 * @author xiezq
 *
 */
public class TxByMethodRegex implements QdfInterceptor {

	private Pattern pattern;
	private int txLevel;
	private String dsName;
	
	public TxByMethodRegex(String regex,String dsName,int txLevel) {
		this.txLevel = txLevel;
		this.dsName = dsName;
		this.pattern = Pattern.compile(regex);
	}


	@Override
	public void intercept(Invocation in) {
		if(pattern.matcher(in.getMethod().getName()).matches()) {
			//符合tx配置的正则,添加事务
			DbUtil.tx(dsName,txLevel,()->{in.invoke();return true;});
		} else {
			in.invoke();
		}
	}
	
	
}
