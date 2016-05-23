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
		
		Connection conn = Qdf.me().getPool().getThreadLocalConnection();
		
		if(null != conn) {
			
			try {
				if(conn.getTransactionIsolation() < this.txLevel) {
					conn.setTransactionIsolation(this.txLevel);
				}
				in.invoke();
				return;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			
		} else {
			
			Boolean autoCommit = null;
			
			try {
				
				conn = Qdf.me().getPool().getConnection();
				conn.setTransactionIsolation(txLevel);
				autoCommit = conn.getAutoCommit();
				conn.setAutoCommit(false);
				in.invoke();
				conn.commit();
				
			} catch (Exception e) {
				try {
					conn.rollback();
					System.out.println("rollback...");
				} catch (SQLException e1) {
					LogUtil.error(e1.getMessage(),e1);
				}
				throw new RuntimeException(e);
			} finally {
				try {
					if(null != conn) {
						conn.setAutoCommit(autoCommit);
						conn.close();
					}
				} catch (SQLException e) {
					LogUtil.error(e.getMessage(),e);
				}
			}
		}
	}

}
