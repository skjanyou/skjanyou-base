package com.skjanyou.log.simple;

import com.skjanyou.log.core.Logger;
import com.skjanyou.log.core.LoggerFactory;

public class SystemLoggerFactory implements LoggerFactory {

	@Override
	public Logger create(Class<?> clazz) {
		return this.create(clazz.getName());
	}

	@Override
	public Logger create(String className) {
		return new SystemLogger(className);
	}

}
