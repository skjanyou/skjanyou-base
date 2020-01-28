package com.skjanyou.plugin.bean;

import java.util.Map;

import com.skjanyou.util.StringUtil;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ComplexProperties {
	private Map<String,String> sysProperties = null;
	private Map<String,String> defaultProperties = null;
	
	public String getProperty( String key ){
		// 第一步,获取应用配置文件中的配置
		String result = sysProperties.get(key);
		// 如果第一步没有获取到,则使用默认配置
		if( StringUtil.isBlank(result) ){
			result = defaultProperties.get(key);
		}
		return result;
	}
}
