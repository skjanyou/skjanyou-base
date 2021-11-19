package com.skjanyou.start.config.impl;

import java.util.Properties;

import com.skjanyou.start.config.ConfigManager;
import com.skjanyou.start.config.ConfigManagerFactory;
import com.skjanyou.start.util.PropertiesUtil;

public class PropertiesConfig implements ConfigManager,ConfigManagerFactory{
	private Properties props = null;
	public PropertiesConfig(){
		String file = System.getProperty("skjanyou.configfile");
		if( file == null ){
			throw new RuntimeException("skjanyou.configfile 没有配置");
		}
		
		try {
			props = PropertiesUtil.getResources(file);
		} catch (Exception e){
			throw new RuntimeException("读取配置失败",e);
		}
	}
	
	@Override
	public ConfigManager create() {
		return this;
	}

	@Override
	public String getString(String key) {
		return props.getProperty(key);
	}

	@Override
	public String getString(String key, String defaultValue) {
		return props.getProperty(key);
	}

	@Override
	public <T> T getBean(String key, Class<T> clazz) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update() {
		
	}

}
