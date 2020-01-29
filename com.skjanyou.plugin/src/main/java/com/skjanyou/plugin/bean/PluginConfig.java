package com.skjanyou.plugin.bean;


public interface PluginConfig {
	/** 通过key获取配置信息,允许查询到value为null的情况 **/
	public String getProperty( String key );
}
