package com.skjanyou.log.plugin;

import java.util.List;

import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.util.ClassUtil;

public class LogPlugin implements PluginSupport {

	@Override
	public void init(List<Class<?>> plugnInnerClass) {
		
	}

	@Override
	public void startup() {
		List<Class<?>> list = ClassUtil.getClasses("com.skjanyou.log");
		
	}


	@Override
	public void shutdown() {
	}

}
