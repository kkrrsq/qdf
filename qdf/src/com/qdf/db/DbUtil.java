package com.qdf.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.alibaba.fastjson.parser.deserializer.StringFieldDeserializer;
import com.qdf.core.Qdf;
import com.qdf.db.TxRun;
import com.qdf.util.LogUtil;

/**
 * 数据库帮助类
 * @author xiezq
 *
 */
public class DbUtil {

	/**
	 * 事务执行sql
	 * @param txRun sql写在txRun.run()里面
	 * @param txLevel 事务级别
	 */
	public static void tx(String dsName,int txLevel,TxRun txRun){
		
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
			conn = Qdf.me().getPool().getConnection(dsName);
			Boolean autoCommit = null;
			try {
				autoCommit = conn.getAutoCommit();
				conn.setTransactionIsolation(txLevel);
				conn.setAutoCommit(false);
				boolean flag = txRun.run();
				if(flag) {
					conn.commit();
					LogUtil.info("事务提交...");
				} else {
					conn.rollback();
					LogUtil.info("事务回滚...");
				}
				
			} catch (Exception e) {
				try {
					conn.rollback();
					LogUtil.info("事务回滚...");
				} catch (SQLException e1) {
					LogUtil.error(e1.getMessage(),e1);
				}
				throw new RuntimeException(e);
			} finally {
				if(null != conn) {
					try {
						conn.setAutoCommit(autoCommit);
						conn.close();
					} catch (SQLException e) {
						LogUtil.error(e.getMessage(),e);
					}
				}
			}
		}
	}
}
