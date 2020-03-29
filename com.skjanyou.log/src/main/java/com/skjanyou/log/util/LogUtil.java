package com.skjanyou.log.util;

import com.skjanyou.log.core.Logger;
import com.skjanyou.log.core.LoggerFactory;
import com.skjanyou.log.simple.SystemLogger;

public class LogUtil {
	private static LoggerFactory loggerFactory = new SystemLogger();
	
	
	public static Logger getLogger( Class<?> clazz ){
		return loggerFactory.create(clazz);
	}
	
	public static void setLoggerFactory( LoggerFactory loggerFactory ){
		if( loggerFactory != null ){
			LogUtil.loggerFactory = loggerFactory;
		}
	}
}