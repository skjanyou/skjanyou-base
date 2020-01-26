package com.skjanyou.log.plugin;

import java.util.List;

import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.util.ClassUtil;

public class LogPlugin implements PluginSupport {

	@Override
	public PluginSupport startup() {
		List<Class<?>> list = ClassUtil.getClasses("com.skjanyou.log");
		
		return this;
	}


	@Override
	public PluginSupport shutdown() {
		return this;
	}

}
