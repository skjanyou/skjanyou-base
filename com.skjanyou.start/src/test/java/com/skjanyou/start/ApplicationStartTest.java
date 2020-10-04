package com.skjanyou.start;

import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.config.impl.PropertiesConfig;
import com.skjanyou.start.start.SkjanyouApplicationStart;


@Configure(
	configManagerFactory = PropertiesConfig.class,
	name = "测试配置props",
	scanPath = "com.skjanyou.start"
)
public class ApplicationStartTest {

	public static void main(String[] args) {
		System.setProperty("skjanyou.configfile", "classpath:skjanyou.config.properties");
		SkjanyouApplicationStart.start(ApplicationStartTest.class,args);
	}

}
