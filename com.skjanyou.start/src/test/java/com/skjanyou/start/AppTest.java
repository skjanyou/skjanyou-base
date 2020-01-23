package com.skjanyou.start;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.skjanyou.start.plugin.PluginSupport;
import com.skjanyou.util.ScanUtil;

public class AppTest implements PluginSupport{

	public static void main(String[] args) throws IOException {
		List<URL> list = ScanUtil.findResourcesByPattern("", "plugin/\\S+.plugin.xml$", AppTest.class.getClassLoader());
		for (URL url : list) {
			System.out.println(url);
		}
	}

	@Override
	public PluginSupport init() {
		System.out.println("AppTest.init() ");
		return this;
	}

	@Override
	public PluginSupport startup() {
		System.out.println("AppTest.startup() ");
		return this;
	}

	@Override
	public PluginSupport shutdown() {
		System.out.println("AppTest.shutdown() ");
		return this;
	}
}
