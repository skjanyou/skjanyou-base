package com.skjanyou.log.util;

import com.skjanyou.log.core.Logger;
import com.skjanyou.log.core.LoggerFactory;
import com.skjanyou.log.core.LoggerLoader;
import com.skjanyou.log.simple.SystemLoggerFactory;

public class LogUtil {
	private static LoggerFactory loggerFactory = new SystemLoggerFactory();
	static {
		// 提供一个开关用于手动设置Logger
		String autoLog = System.getProperty("auto_log");
		if( autoLog == null || "Y".equalsIgnoreCase(autoLog) ) {
			LoggerFactory factory = LoggerLoader.load(LoggerFactory.class, Thread.currentThread().getContextClassLoader());
			setLoggerFactory(factory);
		}		
	}
	
	public static Logger getLogger( Class<?> clazz ){
		return loggerFactory.create(clazz);
	}
	
	public synchronized static void setLoggerFactory( LoggerFactory loggerFactory ){
		if( loggerFactory != null ){
			LogUtil.loggerFactory = loggerFactory;
		}
	}
}