package com.skjanyou.log.util;

import com.skjanyou.log.core.Logger;
import com.skjanyou.log.core.LoggerFactory;
import com.skjanyou.log.simple.SystemLogger;

public class LogUtil {
	// TODO  暂时使用默认
	private static LoggerFactory loggerFactory = new SystemLogger();
	
	
	public static Logger getLogger( Class<?> clazz ){
		return loggerFactory.create(clazz);
	}
	
}