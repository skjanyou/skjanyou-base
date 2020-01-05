package com.skjanyou.log.core;

public interface Logger {
	public Logger trace(Object... msg);
	public Logger debug(Object... msg);
	public Logger info(Object... msg);
	public Logger warn(Object... msg);
	public Logger error(Object... msg);
	public Logger fatal(Object... msg);
}
