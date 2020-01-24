package com.skjanyou.log.util;

import com.skjanyou.log.core.Logger;
import com.skjanyou.log.core.LoggerFactory;
import com.skjanyou.start.anno.Bean;
import com.skjanyou.start.anno.Component;

@Component
public class LogUtil {
	@Bean
	private static LoggerFactory loggerFactory;
	
	
	public static Logger getLogger( Class<?> clazz ){
		return loggerFactory.create(clazz);
	}
	
}