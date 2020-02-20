package com.skjanyou.start.config.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.skjanyou.start.config.ConfigManager;
import com.skjanyou.start.config.ConfigManagerFactory;

public class PropertiesConfig implements ConfigManager,ConfigManagerFactory{
	private Properties props = new Properties();
	public PropertiesConfig(){
		String file = System.getProperty("skjanyou.configfile");
		if( file == null ){
			throw new RuntimeException("skjanyou.configfile 没有配置");
		}
		InputStream is = null;
		if( file.startsWith("classpath:") ){
			String resources = file.substring(10);
			if( !resources.startsWith("/")){ resources = "/" + resources; }
			is = PropertiesConfig.class.getResourceAsStream(resources);
		}else{
			try{
				is = new FileInputStream(file);
			} catch ( FileNotFoundException e ){
				e.printStackTrace();
			}
		}
		if( is == null ){
			throw new RuntimeException("找不到文件");
		}
		try {
			props.load(new InputStreamReader(is,"UTF-8"));
		} catch (IOException e){
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
		return null;
	}

	@Override
	public void update() {
		
	}

}
