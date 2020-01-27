package com.skjanyou.log.util;

import com.skjanyou.annotation.api.Application.Bean;
import com.skjanyou.annotation.api.Application.Component;
import com.skjanyou.log.core.Logger;
import com.skjanyou.log.core.LoggerFactory;

@Component
public class LogUtil {
	@Bean
	private static LoggerFactory loggerFactory;
	
	
	public static Logger getLogger( Class<?> clazz ){
		return loggerFactory.create(clazz);
	}
	
}