package com.skjanyou.start.config;

import java.util.Properties;

import com.skjanyou.plugin.bean.PluginConfig;
import com.skjanyou.start.exception.StartFailException;
import com.skjanyou.util.StringUtil;

public class ComplexPluginConfig implements PluginConfig {
	private ConfigManager sysConfig;
	private Properties defaultConfig;
	
	public ComplexPluginConfig( ConfigManager sysConfig,Properties defaultConfig ){
		this.sysConfig = sysConfig;
		this.defaultConfig = defaultConfig;
	}

	@Override
	public String getProperty(String key) {
		// 1.查询应用配置
		String result = sysConfig.getString(key);
		// 2.应用配置未查询到,查询默认配置
		if( StringUtil.isBlank(result) ){
			result = defaultConfig.getProperty(key);
		}
		// 3.全部未查询到,抛出异常
		if( StringUtil.isBlank(result) ){
			throw new StartFailException("找不到key为" + key + "的配置");
		}
		return result;
	}

}
