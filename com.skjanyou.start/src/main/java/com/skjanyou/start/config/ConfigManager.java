package com.skjanyou.start.config;

public interface ConfigManager {
	/** 通过key去获取配置信息 **/
	public String getString( String key );
	/** 通过key去获取配置信息,并设置缺省值 **/
	public String getString( String key, String defaultValue );
	/** 通过一个类去关联配置,得到一个实例 **/
	public<T> T getBean( String key,Class<T> clazz );
	/** 更新配置信息 **/
	public void update();
}
