package com.skjanyou.log.core;

public interface LoggerFactory {
	public Logger create( Class<?> clazz );
	public Logger create( String className );
}
