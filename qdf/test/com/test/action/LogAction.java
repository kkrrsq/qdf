package com.test.action;

import com.qdf.annotation.Action;
import com.qdf.core.QdfAction;
import com.qdf.log.ILogger;
import com.qdf.log.ILoggerFactory;
import com.qdf.servlet.IRequest;
import com.qdf.servlet.IResponse;
import com.sun.istack.internal.logging.Logger;

/**
 * 日志
 * @author xiezq
 *
 */
@Action(url = "/log")
public class LogAction implements QdfAction {

	private static final ILogger LOGGER	= ILoggerFactory.getLogger(LogAction.class);
	@Override
	public void execute(IRequest request, IResponse response) {
		LOGGER.info("this is test log,action:{}",LogAction.class.getName());
	}

	
}
