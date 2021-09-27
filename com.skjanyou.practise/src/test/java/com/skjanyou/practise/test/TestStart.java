package com.skjanyou.practise.test;

import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.config.impl.PropertiesConfig;
import com.skjanyou.start.start.SkjanyouApplicationStart;

@Configure(
	name = "test的props",
	configManagerFactory = PropertiesConfig.class,
	scanPath = "com.skjanyou.test"
)
public class TestStart {
	public static void main(String[] args) {
		System.setProperty("skjanyou.configfile", "classpath:/config/test.properties");
		SkjanyouApplicationStart.start(TestStart.class,args);
	}
}
