package com.skjanyou.start;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.bean.ComplexProperties;
import com.skjanyou.util.ScanUtil;

public class AppTest implements PluginSupport{
	
	@Override
	public void init(List<Class<?>> plugnInnerClass,ComplexProperties properties) {
		
	}
	
	public static void main(String[] args) throws IOException {
		List<URL> list = ScanUtil.findResourcesByPattern("", "plugin/\\S+.plugin.xml$", AppTest.class.getClassLoader());
		for (URL url : list) {
			System.out.println(url);
		}
	}

	@Override
	public void startup() {
		System.out.println("AppTest.init() ");
	}


	@Override
	public void shutdown() {
		System.out.println("AppTest.shutdown() ");
	}


}
