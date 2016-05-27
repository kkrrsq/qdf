package com.qdf.db;
/**
 * 在DbUtil.tx(TxRun txRun,int txLevel)中调用,用于实现事务
 * run方法需自己实现
 * @author xiezq
 *
 */
public interface TxRun {

	boolean run();
}
