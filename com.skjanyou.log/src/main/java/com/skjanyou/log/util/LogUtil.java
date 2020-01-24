package com.skjanyou.log.util;

import com.skjanyou.log.core.Logger;
import com.skjanyou.log.core.LoggerFactory;
import com.skjanyou.start.ioc.BeanContainer;


public class LogUtil {
	public static Logger getLogger( Class<?> clazz ){
		LoggerFactory loggerFactory = BeanContainer.getBean(LoggerFactory.class);
		return loggerFactory.create(clazz);
	}
	
}