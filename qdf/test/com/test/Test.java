package com.test;

import com.qdf.log.ILogger;
import com.qdf.log.ILoggerFactory;


public class Test {

	private static final ILogger LOG = ILoggerFactory.getLogger(Test.class);
	
	public static void main(String[] args) throws Exception{
		LOG.info("LGO TEST");
	}
	
}
