package com.qdf.util;

import java.sql.Connection;
import java.sql.SQLException;

import com.qdf.core.Qdf;
import com.qdf.db.TxRun;

public class DbUtil {

	public static void tx(TxRun txRun,int txLevel){
		
		Connection conn = Qdf.me().getPool().getThreadLocalConnection();
		if(null != conn) {
			try {
				if(conn.getTransactionIsolation() < txLevel) {
					conn.setTransactionIsolation(txLevel);
				}
				txRun.run();
				return;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		} else {
			conn = Qdf.me().getPool().getConnection();
			Boolean autoCommit = null;
			try {
				autoCommit = conn.getAutoCommit();
				conn.setTransactionIsolation(txLevel);
				conn.setAutoCommit(false);
				txRun.run();
				conn.commit();
			} catch (Exception e) {
				try {
					conn.rollback();
					System.out.println("rollback...");
				} catch (SQLException e1) {
					System.err.println(e1.toString());
				}
				throw new RuntimeException(e);
			} finally {
				if(null != conn) {
					try {
						conn.setAutoCommit(autoCommit);
						conn.close();
					} catch (SQLException e) {
						System.err.println(e.toString());
					}
				}
			}
		}
	}
}
