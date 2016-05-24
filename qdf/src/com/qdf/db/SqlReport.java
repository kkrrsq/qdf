
package com.qdf.db;

import com.alibaba.druid.filter.logging.LogFilter;
import com.qdf.log.ILogger;
import com.qdf.log.ILoggerFactory;

/**
 * 打印sql日志
 * @author xiezq
 *
 */
final class SqlReport extends LogFilter{

	// 日志对象
//	private ILogger	dataSourceLogger	= ILoggerFactory.getLogger( dataSourceLoggerName );
	private ILogger	connectionLogger	= ILoggerFactory.getLogger( connectionLoggerName );
	private ILogger	statementLogger		= ILoggerFactory.getLogger( statementLoggerName );
	private ILogger	resultSetLogger		= ILoggerFactory.getLogger( resultSetLoggerName );

	SqlReport(){
        super.setStatementExecutableSqlLogEnable( true );
		super.setStatementCreateAfterLogEnabled( false );
		super.setStatementCloseAfterLogEnabled( false );
		super.setStatementPrepareAfterLogEnabled( false );
		super.setStatementPrepareCallAfterLogEnabled( false );
		super.setStatementExecuteQueryAfterLogEnabled( false );
		super.setStatementExecuteBatchAfterLogEnabled( false );
		super.setStatementExecuteUpdateAfterLogEnabled( false );
	}

	@Override
	protected void connectionLog( String message ){
		connectionLogger.debug( formatMessage( message ) );
	}

	@Override
	protected void statementLog( String message ){
		statementLogger.debug( formatMessage( message ) );
	}

	@Override
	protected void statementLogError( String message, Throwable error ){
		statementLogger.error( formatMessage( message ) );
	}

	@Override
	protected void resultSetLog( String message ){
		resultSetLogger.debug( formatMessage( message ) );
	}

	@Override
	protected void resultSetLogError( String message, Throwable error ){
		resultSetLogger.error( formatMessage( message ) );
	}

	@Override
	public String getDataSourceLoggerName(){
		return dataSourceLoggerName;
	}

	@Override
	public void setDataSourceLoggerName( String loggerName ){
		this.dataSourceLoggerName = loggerName;
	}

	@Override
	public String getConnectionLoggerName(){
		return connectionLoggerName;
	}

	@Override
	public void setConnectionLoggerName( String loggerName ){
		this.connectionLoggerName = loggerName;
		this.connectionLogger = ILoggerFactory.getLogger( loggerName );
	}

	@Override
	public String getStatementLoggerName(){
		return statementLoggerName;
	}

	@Override
	public void setStatementLoggerName( String loggerName ){
		this.statementLoggerName = loggerName;
		this.statementLogger = ILoggerFactory.getLogger( loggerName );
	}

	@Override
	public String getResultSetLoggerName(){
		return resultSetLoggerName;
	}

	@Override
	public void setResultSetLoggerName( String loggerName ){
		this.resultSetLoggerName = loggerName;
		this.resultSetLogger = ILoggerFactory.getLogger( loggerName );
	}

	public boolean isConnectionLogErrorEnabled(){
		return true;
	}

	@Override
	public boolean isDataSourceLogEnabled(){
		return false;
	}

	@Override
	public boolean isConnectionLogEnabled(){
		return false;
	}

	@Override
	public boolean isStatementLogEnabled(){
		return true;
		// return statementLogger.isDebugEnabled() && super.isStatementLogEnabled();
	}

	@Override
	public boolean isResultSetLogEnabled(){
		return false;
	}

	@Override
	public boolean isResultSetLogErrorEnabled(){
		return true;
	}

	@Override
	public boolean isStatementLogErrorEnabled(){
		return true;
	}

	@Override
	public boolean isStatementParameterSetLogEnabled(){
		return false;
	}

	private static String formatMessage( String message ){
		return message.replace( '\n', ' ' );
	}

}
