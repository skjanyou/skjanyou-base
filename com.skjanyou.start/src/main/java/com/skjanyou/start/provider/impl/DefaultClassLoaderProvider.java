package com.skjanyou.start.provider.impl;

import java.net.URL;

import com.skjanyou.start.core.SkjanyouClassLoader;
import com.skjanyou.start.provider.ClassLoaderProvider;

public class DefaultClassLoaderProvider implements ClassLoaderProvider {

	@Override
	public SkjanyouClassLoader getClassLoader() {
		return new SkjanyouClassLoader(new URL[]{});
	}

}
