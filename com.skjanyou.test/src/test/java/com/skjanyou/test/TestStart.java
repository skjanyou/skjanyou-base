package com.skjanyou.test;

import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.config.impl.PropertiesConfig;
import com.skjanyou.start.start.ApplicationStart;

@Configure(
	name = "test的props",
	configManagerFactory = PropertiesConfig.class
)
public class TestStart {
	public static void main(String[] args) {
		System.setProperty("skjanyou.configfile", "classpath:/config/test.properties");
		ApplicationStart.start(TestStart.class);
	}
}
