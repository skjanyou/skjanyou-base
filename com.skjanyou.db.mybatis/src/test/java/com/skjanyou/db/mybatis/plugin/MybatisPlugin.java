package com.skjanyou.db.mybatis.plugin;

import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.config.impl.PropertiesConfig;
import com.skjanyou.start.plugin.PluginSupport;
import com.skjanyou.start.start.ApplicationStart;

@Configure(
	configManagerFactory = PropertiesConfig.class,
	name = "mybatis测试配置props"
)
public class MybatisPlugin implements PluginSupport {

	@Override
	public PluginSupport init() {
		System.out.println("mybatis插件初始化!");
		return this;
	}

	@Override
	public PluginSupport startup() {
		System.out.println("mybatis插件启动!");
		return this;
	}

	@Override
	public PluginSupport shutdown() {
		System.out.println("mybatis插件关闭!");
		return this;
	}
	
	public static void main(String[] args) {
		System.setProperty("skjanyou.configfile", "classpath:skjanyou.config.properties");
		ApplicationStart.start(MybatisPlugin.class);
	}
}
