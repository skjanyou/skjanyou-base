package com.skjanyou.log.util;

import com.skjanyou.log.core.Logger;
import com.skjanyou.log.core.LoggerFactory;
import com.skjanyou.log.simple.SystemLoggerFactory;

class SimpleLogUtil {
	private static LoggerFactory loggerFactory = new SystemLoggerFactory();
	public static Logger getLogger( Class<?> clazz ){
		return loggerFactory.create(clazz);
	}
}
