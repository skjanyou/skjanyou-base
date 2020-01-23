package com.skjanyou.start.config.impl;

import com.skjanyou.start.config.ConfigManager;
import com.skjanyou.start.config.ConfigManagerFactory;

public class WithoutConfig implements ConfigManager, ConfigManagerFactory {

	@Override
	public ConfigManager create() {
		return this;
	}

	@Override
	public String getString(String key) {
		return null;
	}

	@Override
	public String getString(String key, String defaultValue) {
		return defaultValue;
	}

	@Override
	public <T> T getBean(String key, Class<T> clazz) {
		return null;
	}

	@Override
	public void update() {

	}

}
