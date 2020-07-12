package com.skjanyou.start.provider;

import com.skjanyou.start.config.ConfigManager;

public interface ConfigureProvider {
	public ConfigManager getConfigure( Class<?> startClass );
}
