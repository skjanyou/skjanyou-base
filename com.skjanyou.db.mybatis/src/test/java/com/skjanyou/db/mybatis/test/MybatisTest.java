package com.skjanyou.db.mybatis.test;

import com.skjanyou.db.mybatis.plugin.MybatisPlugin;
import com.skjanyou.start.start.ApplicationStart;

public class MybatisTest extends UserTest {
	@Override
	public void runBare() throws Throwable {
		System.setProperty("skjanyou.configfile", "classpath:skjanyou.config.properties");
		ApplicationStart.start(MybatisPlugin.class);
		super.runBare();
	}
}
