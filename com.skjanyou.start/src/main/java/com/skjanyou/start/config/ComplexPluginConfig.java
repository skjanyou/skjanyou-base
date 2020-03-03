package com.skjanyou.start.config;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.skjanyou.plugin.bean.PluginConfig;
import com.skjanyou.start.exception.StartFailException;

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
		if( result == null ){
			result = defaultConfig.getProperty(key);
		}
		// 3.全部未查询到,抛出异常
		if( result == null ){
			throw new StartFailException("找不到key为" + key + "的配置");
		}
		// 4.添加获取${}处理, sys:获取System.Property, app:获取应用中的配置, 插件id: 获取其他插件中的配置
		if( result != null ){
			
			Pattern pattern = Pattern.compile("\\$\\{([^}]*)\\}");
			Matcher matcher = pattern.matcher(result);
			
			String resultBuilder = new String( result );
			while( matcher.find() ){
				String patternString = matcher.group();
				String findValue = null;
				if( patternString.toLowerCase().indexOf("sys:") != -1 ){
					String newKey = patternString.substring(patternString.toLowerCase().indexOf("sys:") + 4, patternString.length() - 1);
					findValue = System.getProperty(newKey);
				}else if( patternString.toLowerCase().indexOf("app:") != -1 ){
					String newKey = patternString.substring(patternString.toLowerCase().indexOf("app:") + 4, patternString.length() - 1);
					findValue = getProperty(newKey);
				}
				if( findValue != null ){
					resultBuilder.replace(patternString, findValue);
				}
			}
			result = resultBuilder.toString();
		}
		
		return result;
	}

}
