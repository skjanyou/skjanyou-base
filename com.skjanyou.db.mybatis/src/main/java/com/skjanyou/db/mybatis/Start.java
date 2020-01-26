package com.skjanyou.db.mybatis;

import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.config.impl.PropertiesConfig;
import com.skjanyou.start.start.ApplicationStart;

@Configure(
	configManagerFactory = PropertiesConfig.class,
	name = "mybatis测试配置props"
)
public class Start implements PluginSupport {

	@Override
	public PluginSupport startup() {
		System.out.println("mybatis插件初始化!");
		return this;
	}

	@Override
	public PluginSupport shutdown() {
		System.out.println("mybatis插件关闭!");
		return this;
	}
	
	public static void main(String[] args) {
		System.setProperty("skjanyou.configfile", "classpath:skjanyou.config.properties");
		ApplicationStart.start(Start.class);
	}
}
