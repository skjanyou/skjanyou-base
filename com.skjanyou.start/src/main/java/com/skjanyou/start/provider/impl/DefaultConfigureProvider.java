package com.skjanyou.start.provider.impl;

import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.config.ConfigManager;
import com.skjanyou.start.config.ConfigManagerFactory;
import com.skjanyou.start.provider.ConfigureProvider;
import com.skjanyou.start.util.InstanceUtil;

public class DefaultConfigureProvider implements ConfigureProvider {

	@Override
	public ConfigManager getConfigure(Class<?> startClass) {
		Configure configure = startClass.getAnnotation(Configure.class);
		Class<? extends ConfigManagerFactory> cmFactoryClazz = configure.configManagerFactory();
		ConfigManagerFactory cmFactory = (ConfigManagerFactory)InstanceUtil.newInstance(cmFactoryClazz);
		return cmFactory.create();
	}

}
